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
package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.serverpackets.ExQuestInfo;
import net.sf.l2j.gameserver.serverpackets.RadarControl;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author LBaldi
 */
public class L2AdventurerInstance extends L2FolkInstance
{
	public L2AdventurerInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (command.startsWith("npcfind_byid"))
		{
			try
			{
				int bossId = Integer.parseInt(command.substring(12).trim());
				switch (RaidBossSpawnManager.getInstance().getRaidBossStatusId(bossId))
				{
					case ALIVE:
					case DEAD:
						L2Spawn spawn = RaidBossSpawnManager.getInstance().getSpawns().get(bossId);
						player.sendPacket(new RadarControl(0, 1, spawn.getLocx(), spawn.getLocy(), spawn.getLocz()));
						break;
					case UNDEFINED:
						player.sendMessage("This Boss isn't in game - notify L2J Datapack Dev Team");
						break;
				}
			}
			catch (NumberFormatException e)
			{
				_log.warning("Invalid Bypass to Server command parameter.");
			}
		}
		else if (command.startsWith("raidInfo"))
		{
			int bossLevel = Integer.parseInt(command.substring(9).trim());
			String filename = "data/html/adventurer_guildsman/raid_info/info.htm";
			if (bossLevel != 0)
			{
				filename = "data/html/adventurer_guildsman/raid_info/level" + bossLevel + ".htm";
			}
			showChatWindow(player, bossLevel, filename);
		}
		else if (command.equalsIgnoreCase("questlist"))
		{
			player.sendPacket(new ExQuestInfo());
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String pom = "";
		
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		
		return "data/html/adventurer_guildsman/" + pom + ".htm";
	}
	
	private void showChatWindow(L2PcInstance player, int bossLevel, String filename)
	{
		showChatWindow(player, filename);
	}
}
