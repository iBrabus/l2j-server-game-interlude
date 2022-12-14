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
package net.sf.l2j.gameserver.templates;

import java.util.List;

import javolution.util.FastList;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.base.Race;

/**
 * @author mkizub
 */
public class L2PcTemplate extends L2CharTemplate
{
	/** The Class object of the L2PcInstance */
	public final ClassId classId;
	
	public final Race race;
	public final String className;
	
	public final int spawnX;
	public final int spawnY;
	public final int spawnZ;
	
	public final int classBaseLevel;
	public final float lvlHpAdd;
	public final float lvlHpMod;
	public final float lvlCpAdd;
	public final float lvlCpMod;
	public final float lvlMpAdd;
	public final float lvlMpMod;
	
	private final List<L2Item> _items = new FastList<>();
	
	public L2PcTemplate(StatsSet set)
	{
		super(set);
		classId = ClassId.values()[set.getInteger("classId")];
		race = Race.values()[set.getInteger("raceId")];
		className = set.getString("className");
		
		spawnX = set.getInteger("spawnX");
		spawnY = set.getInteger("spawnY");
		spawnZ = set.getInteger("spawnZ");
		
		classBaseLevel = set.getInteger("classBaseLevel");
		lvlHpAdd = set.getFloat("lvlHpAdd");
		lvlHpMod = set.getFloat("lvlHpMod");
		lvlCpAdd = set.getFloat("lvlCpAdd");
		lvlCpMod = set.getFloat("lvlCpMod");
		lvlMpAdd = set.getFloat("lvlMpAdd");
		lvlMpMod = set.getFloat("lvlMpMod");
	}
	
	/**
	 * add starter equipment
	 * @param itemId
	 */
	public void addItem(int itemId)
	{
		L2Item item = ItemTable.getInstance().getTemplate(itemId);
		if (item != null)
		{
			_items.add(item);
		}
	}
	
	/**
	 * @return itemIds of all the starter equipment
	 */
	public L2Item[] getItems()
	{
		return _items.toArray(new L2Item[_items.size()]);
	}
}
