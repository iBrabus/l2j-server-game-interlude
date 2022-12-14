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
package net.sf.l2j.gameserver.datatables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Logger;

import javolution.util.FastMap;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.L2TeleportLocation;

/**
 * @version $Revision: 1.3.2.2.2.3 $ $Date: 2005/03/27 15:29:18 $
 */
public class TeleportLocationTable
{
	private static Logger _log = Logger.getLogger(TeleportLocationTable.class.getName());
	
	private static TeleportLocationTable _instance;
	
	private Map<Integer, L2TeleportLocation> _teleports;
	
	public static TeleportLocationTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new TeleportLocationTable();
		}
		return _instance;
	}
	
	private TeleportLocationTable()
	{
		reloadAll();
	}
	
	public void reloadAll()
	{
		_teleports = new FastMap<>();
		
		java.sql.Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT Description, id, loc_x, loc_y, loc_z, price, fornoble FROM teleport");
			ResultSet rset = statement.executeQuery();
			L2TeleportLocation teleport;
			
			while (rset.next())
			{
				teleport = new L2TeleportLocation();
				
				teleport.setTeleId(rset.getInt("id"));
				teleport.setLocX(rset.getInt("loc_x"));
				teleport.setLocY(rset.getInt("loc_y"));
				teleport.setLocZ(rset.getInt("loc_z"));
				teleport.setPrice(rset.getInt("price"));
				teleport.setIsForNoble(rset.getInt("fornoble") == 1);
				
				_teleports.put(teleport.getTeleId(), teleport);
			}
			
			rset.close();
			statement.close();
			
			_log.config("TeleportLocationTable: Loaded " + _teleports.size() + " Teleport Location Templates.");
		}
		catch (Exception e)
		{
			_log.warning("error while creating teleport table " + e);
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
	
	public L2TeleportLocation getTemplate(int id)
	{
		return _teleports.get(id);
	}
}
