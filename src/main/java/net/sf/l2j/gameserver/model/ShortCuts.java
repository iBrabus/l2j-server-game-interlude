/*
 * Copyright © 2004-2020 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.ExAutoSoulShot;
import net.sf.l2j.gameserver.serverpackets.ShortCutInit;
import net.sf.l2j.gameserver.templates.L2EtcItemType;

/**
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:33 $
 */
public class ShortCuts
{
	private static Logger _log = Logger.getLogger(ShortCuts.class.getName());
	
	private final L2PcInstance _owner;
	private final Map<Integer, L2ShortCut> _shortCuts = new TreeMap<>();
	
	public ShortCuts(L2PcInstance owner)
	{
		_owner = owner;
	}
	
	public L2ShortCut[] getAllShortCuts()
	{
		return _shortCuts.values().toArray(new L2ShortCut[_shortCuts.values().size()]);
	}
	
	public L2ShortCut getShortCut(int slot, int page)
	{
		L2ShortCut sc = _shortCuts.get(slot + (page * 12));
		
		// verify shortcut
		if ((sc != null) && (sc.getType() == L2ShortCut.TYPE_ITEM))
		{
			if (_owner.getInventory().getItemByObjectId(sc.getId()) == null)
			{
				deleteShortCut(sc.getSlot(), sc.getPage());
				sc = null;
			}
		}
		
		return sc;
	}
	
	public synchronized void registerShortCut(L2ShortCut shortcut)
	{
		L2ShortCut oldShortCut = _shortCuts.put(shortcut.getSlot() + (12 * shortcut.getPage()), shortcut);
		registerShortCutInDb(shortcut, oldShortCut);
	}
	
	private void registerShortCutInDb(L2ShortCut shortcut, L2ShortCut oldShortCut)
	{
		if (oldShortCut != null)
		{
			deleteShortCutFromDb(oldShortCut);
		}
		
		java.sql.Connection con = null;
		
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			
			PreparedStatement statement = con.prepareStatement("REPLACE INTO character_shortcuts (char_obj_id,slot,page,type,shortcut_id,level,class_index) values(?,?,?,?,?,?,?)");
			statement.setInt(1, _owner.getObjectId());
			statement.setInt(2, shortcut.getSlot());
			statement.setInt(3, shortcut.getPage());
			statement.setInt(4, shortcut.getType());
			statement.setInt(5, shortcut.getId());
			statement.setInt(6, shortcut.getLevel());
			statement.setInt(7, _owner.getClassIndex());
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			_log.warning("Could not store character shortcut: " + e);
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
			}
		}
	}
	
	/**
	 * @param slot
	 * @param page
	 */
	public synchronized void deleteShortCut(int slot, int page)
	{
		L2ShortCut old = _shortCuts.remove(slot + (page * 12));
		
		if ((old == null) || (_owner == null))
		{
			return;
		}
		deleteShortCutFromDb(old);
		if (old.getType() == L2ShortCut.TYPE_ITEM)
		{
			L2ItemInstance item = _owner.getInventory().getItemByObjectId(old.getId());
			
			if ((item != null) && (item.getItemType() == L2EtcItemType.SHOT))
			{
				_owner.removeAutoSoulShot(item.getItemId());
				_owner.sendPacket(new ExAutoSoulShot(item.getItemId(), 0));
			}
		}
		
		_owner.sendPacket(new ShortCutInit(_owner));
		
		for (int shotId : _owner.getAutoSoulShot().values())
		{
			_owner.sendPacket(new ExAutoSoulShot(shotId, 1));
		}
	}
	
	public synchronized void deleteShortCutByObjectId(int objectId)
	{
		L2ShortCut toRemove = null;
		
		for (L2ShortCut shortcut : _shortCuts.values())
		{
			if ((shortcut.getType() == L2ShortCut.TYPE_ITEM) && (shortcut.getId() == objectId))
			{
				toRemove = shortcut;
				break;
			}
		}
		
		if (toRemove != null)
		{
			deleteShortCut(toRemove.getSlot(), toRemove.getPage());
		}
	}
	
	/**
	 * @param shortcut
	 */
	private void deleteShortCutFromDb(L2ShortCut shortcut)
	{
		java.sql.Connection con = null;
		
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			
			PreparedStatement statement = con.prepareStatement("DELETE FROM character_shortcuts WHERE char_obj_id=? AND slot=? AND page=? AND class_index=?");
			statement.setInt(1, _owner.getObjectId());
			statement.setInt(2, shortcut.getSlot());
			statement.setInt(3, shortcut.getPage());
			statement.setInt(4, _owner.getClassIndex());
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			_log.warning("Could not delete character shortcut: " + e);
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
			}
		}
	}
	
	public void restore()
	{
		_shortCuts.clear();
		java.sql.Connection con = null;
		
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT char_obj_id, slot, page, type, shortcut_id, level FROM character_shortcuts WHERE char_obj_id=? AND class_index=?");
			statement.setInt(1, _owner.getObjectId());
			statement.setInt(2, _owner.getClassIndex());
			
			ResultSet rset = statement.executeQuery();
			
			while (rset.next())
			{
				int slot = rset.getInt("slot");
				int page = rset.getInt("page");
				int type = rset.getInt("type");
				int id = rset.getInt("shortcut_id");
				int level = rset.getInt("level");
				
				L2ShortCut sc = new L2ShortCut(slot, page, type, id, level, 1);
				_shortCuts.put(slot + (page * 12), sc);
			}
			
			rset.close();
			statement.close();
		}
		catch (Exception e)
		{
			_log.warning("Could not restore character shortcuts: " + e);
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
			}
		}
		
		// verify shortcuts
		for (L2ShortCut sc : getAllShortCuts())
		{
			if (sc.getType() == L2ShortCut.TYPE_ITEM)
			{
				if (_owner.getInventory().getItemByObjectId(sc.getId()) == null)
				{
					deleteShortCut(sc.getSlot(), sc.getPage());
				}
			}
		}
	}
}
