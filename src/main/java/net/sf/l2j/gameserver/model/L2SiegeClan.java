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

import java.util.List;

import javolution.util.FastList;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;

public class L2SiegeClan
{
	private int _clanId = 0;
	private List<L2NpcInstance> _flag = new FastList<>();
	private int _numFlagsAdded = 0;
	private SiegeClanType _type;
	
	public enum SiegeClanType
	{
		OWNER,
		DEFENDER,
		ATTACKER,
		DEFENDER_PENDING
	}
	
	public L2SiegeClan(int clanId, SiegeClanType type)
	{
		_clanId = clanId;
		_type = type;
	}
	
	public int getNumFlags()
	{
		return _numFlagsAdded;
	}
	
	public void addFlag(L2NpcInstance flag)
	{
		_numFlagsAdded++;
		getFlag().add(flag);
	}
	
	public boolean removeFlag(L2NpcInstance flag)
	{
		if (flag == null)
		{
			return false;
		}
		boolean ret = getFlag().remove(flag);
		// flag.deleteMe();
		// check if null objects or dups remain in the list.
		// for some reason, this might be happening sometimes...
		// delete false duplicates: if this flag got deleted, delete its copies too.
		if (ret)
		{
			while (getFlag().remove(flag))
			{
				
			}
		}
		
		// now delete nulls
		int n;
		boolean more = true;
		while (more)
		{
			more = false;
			n = getFlag().size();
			if (n > 0)
			{
				for (int i = 0; i < n; i++)
				{
					if (getFlag().get(i) == null)
					{
						getFlag().remove(i);
						more = true;
						break;
					}
				}
			}
		}
		
		flag.deleteMe();
		return ret;
	}
	
	public void removeFlags()
	{
		for (L2NpcInstance flag : getFlag())
		{
			removeFlag(flag);
		}
	}
	
	public final int getClanId()
	{
		return _clanId;
	}
	
	public final List<L2NpcInstance> getFlag()
	{
		if (_flag == null)
		{
			_flag = new FastList<>();
		}
		return _flag;
	}
	
	public SiegeClanType getType()
	{
		return _type;
	}
	
	public void setType(SiegeClanType setType)
	{
		_type = setType;
	}
}
