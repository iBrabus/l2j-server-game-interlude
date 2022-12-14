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

import java.util.Map;

import javolution.util.FastMap;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.skills.SkillsEngine;
import net.sf.l2j.gameserver.templates.L2WeaponType;

/**
 * @version $Revision: 1.8.2.6.2.18 $ $Date: 2005/04/06 16:13:25 $
 */
public class SkillTable
{
	private static SkillTable _instance;
	
	private final Map<Integer, L2Skill> _skills;
	private final boolean _initialized = true;
	
	public static SkillTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new SkillTable();
		}
		return _instance;
	}
	
	private SkillTable()
	{
		_skills = new FastMap<>();
		SkillsEngine.getInstance().loadAllSkills(_skills);
	}
	
	public void reload()
	{
		_instance = new SkillTable();
	}
	
	public boolean isInitialized()
	{
		return _initialized;
	}
	
	/**
	 * Provides the skill hash
	 * @param skill The L2Skill to be hashed
	 * @return SkillTable.getSkillHashCode(skill.getId(), skill.getLevel())
	 */
	public static int getSkillHashCode(L2Skill skill)
	{
		return SkillTable.getSkillHashCode(skill.getId(), skill.getLevel());
	}
	
	/**
	 * Centralized method for easier change of the hashing sys
	 * @param skillId The Skill Id
	 * @param skillLevel The Skill Level
	 * @return The Skill hash number
	 */
	public static int getSkillHashCode(int skillId, int skillLevel)
	{
		return (skillId * 256) + skillLevel;
	}
	
	public L2Skill getInfo(int skillId, int level)
	{
		return _skills.get(SkillTable.getSkillHashCode(skillId, level));
	}
	
	public int getMaxLevel(int magicId, int level)
	{
		L2Skill temp;
		
		while (level < 100)
		{
			level++;
			temp = _skills.get(SkillTable.getSkillHashCode(magicId, level));
			
			if (temp == null)
			{
				return level - 1;
			}
		}
		
		return level;
	}
	
	private static final L2WeaponType[] weaponDbMasks =
	{
		L2WeaponType.ETC,
		L2WeaponType.BOW,
		L2WeaponType.POLE,
		L2WeaponType.DUALFIST,
		L2WeaponType.DUAL,
		L2WeaponType.BLUNT,
		L2WeaponType.SWORD,
		L2WeaponType.DAGGER,
		L2WeaponType.BIGSWORD,
		L2WeaponType.ROD,
		L2WeaponType.BIGBLUNT
	};
	
	public int calcWeaponsAllowed(int mask)
	{
		if (mask == 0)
		{
			return 0;
		}
		
		int weaponsAllowed = 0;
		
		for (int i = 0; i < weaponDbMasks.length; i++)
		{
			if ((mask & (1 << i)) != 0)
			{
				weaponsAllowed |= weaponDbMasks[i].mask();
			}
		}
		
		return weaponsAllowed;
	}
}
