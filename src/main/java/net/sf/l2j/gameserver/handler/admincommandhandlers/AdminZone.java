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
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GmListTable;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class AdminZone implements IAdminCommandHandler
{
	private static final int REQUIRED_LEVEL = Config.GM_TEST;
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_zone_check",
		"admin_zone_reload"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (activeChar == null)
		{
			return false;
		}
		
		if (!Config.ALT_PRIVILEGES_ADMIN)
		{
			if (activeChar.getAccessLevel() < REQUIRED_LEVEL)
			{
				return false;
			}
		}
		
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken(); // Get actual command
		
		// String val = "";
		// if (st.countTokens() >= 1) {val = st.nextToken();}
		
		if (actualCommand.equalsIgnoreCase("admin_zone_check"))
		{
			if (activeChar.isInsideZone(L2Character.ZONE_PVP))
			{
				activeChar.sendMessage("This is a PvP zone.");
			}
			else
			{
				activeChar.sendMessage("This is NOT a PvP zone.");
			}
			
			if (activeChar.isInsideZone(L2Character.ZONE_NOLANDING))
			{
				activeChar.sendMessage("This is a no landing zone.");
			}
			else
			{
				activeChar.sendMessage("This is NOT a no landing zone.");
			}
			
			activeChar.sendMessage("MapRegion: x:" + MapRegionTable.getInstance().getMapRegionX(activeChar.getX()) + " y:" + MapRegionTable.getInstance().getMapRegionX(activeChar.getY()));
			
			activeChar.sendMessage("Closest Town: " + MapRegionTable.getInstance().getClosestTownName(activeChar));
			
			Location loc;
			
			loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.Castle);
			activeChar.sendMessage("TeleToLocation (Castle): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			
			loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.ClanHall);
			activeChar.sendMessage("TeleToLocation (ClanHall): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			
			loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.SiegeFlag);
			activeChar.sendMessage("TeleToLocation (SiegeFlag): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			
			loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.Town);
			activeChar.sendMessage("TeleToLocation (Town): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
		}
		else if (actualCommand.equalsIgnoreCase("admin_zone_reload"))
		{
			// TODO: ZONETODO ZoneManager.getInstance().reload();
			GmListTable.broadcastMessageToGMs("Zones can not be reloaded in this version.");
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
