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
package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;

/**
 * A peaceful zone
 * @author durgus
 */
public class L2PeaceZone extends L2ZoneType
{
	public L2PeaceZone()
	{
		super();
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		character.setInsideZone(L2Character.ZONE_PEACE, true);
		
		/*
		 * if (character instanceof L2PcInstance) { ((L2PcInstance)character).sendMessage("You entered a peace zone!"); }
		 */
	}
	
	@Override
	protected void onExit(L2Character character)
	{
		character.setInsideZone(L2Character.ZONE_PEACE, false);
		
		/*
		 * if (character instanceof L2PcInstance) { ((L2PcInstance)character).sendMessage("You exited a peace zone!"); }
		 */
	}
	
	@Override
	protected void onDieInside(L2Character character)
	{
	}
	
	@Override
	protected void onReviveInside(L2Character character)
	{
	}
	
}
