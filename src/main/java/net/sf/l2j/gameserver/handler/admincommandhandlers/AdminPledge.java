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

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.GMViewPledgeInfo;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * <B>Pledge Manipulation:</B><BR>
 * <LI>With target in a character without clan:<BR>
 * //pledge create clanname <LI>With target in a clan leader:<BR>
 * //pledge info<BR>
 * //pledge dismiss<BR>
 * //pledge setlevel level<BR>
 * //pledge rep reputation_points<BR>
 */
public class AdminPledge implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_pledge"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (!Config.ALT_PRIVILEGES_ADMIN)
		{
			if (!activeChar.isGM() || (activeChar.getAccessLevel() < Config.GM_ACCESSLEVEL) || (activeChar.getTarget() == null) || !(activeChar.getTarget() instanceof L2PcInstance))
			{
				return false;
			}
		}
		
		L2Object target = activeChar.getTarget();
		L2PcInstance player = null;
		if (target instanceof L2PcInstance)
		{
			player = (L2PcInstance) target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.INCORRECT_TARGET));
			showMainPage(activeChar);
			return false;
		}
		String name = player.getName();
		if (command.startsWith("admin_pledge"))
		{
			String action = null;
			String parameter = null;
			GMAudit.auditGMAction(activeChar.getName(), command, activeChar.getName(), "");
			StringTokenizer st = new StringTokenizer(command);
			try
			{
				st.nextToken();
				action = st.nextToken(); // create|info|dismiss|setlevel|rep
				parameter = st.nextToken(); // clanname|nothing|nothing|level|rep_points
			}
			catch (NoSuchElementException nse)
			{
			}
			if (action.equals("create"))
			{
				long cet = player.getClanCreateExpiryTime();
				player.setClanCreateExpiryTime(0);
				L2Clan clan = ClanTable.getInstance().createClan(player, parameter);
				if (clan != null)
				{
					activeChar.sendMessage("Clan " + parameter + " created. Leader: " + player.getName());
				}
				else
				{
					player.setClanCreateExpiryTime(cet);
					activeChar.sendMessage("There was a problem while creating the clan.");
				}
			}
			else if (!player.isClanLeader())
			{
				activeChar.sendPacket(new SystemMessage(SystemMessageId.S1_IS_NOT_A_CLAN_LEADER).addString(name));
				showMainPage(activeChar);
				return false;
			}
			else if (action.equals("dismiss"))
			{
				ClanTable.getInstance().destroyClan(player.getClanId());
				L2Clan clan = player.getClan();
				if (clan == null)
				{
					activeChar.sendMessage("Clan disbanded.");
				}
				else
				{
					activeChar.sendMessage("There was a problem while destroying the clan.");
				}
			}
			else if (action.equals("info"))
			{
				activeChar.sendPacket(new GMViewPledgeInfo(player.getClan(), player));
			}
			else if (parameter == null)
			{
				activeChar.sendMessage("Usage: //pledge <setlevel|rep> <number>");
			}
			else if (action.equals("setlevel"))
			{
				int level = Integer.parseInt(parameter);
				if ((level >= 0) && (level < 9))
				{
					player.getClan().changeLevel(level);
					activeChar.sendMessage("You set level " + level + " for clan " + player.getClan().getName());
				}
				else
				{
					activeChar.sendMessage("Level incorrect.");
				}
			}
			else if (action.startsWith("rep"))
			{
				try
				{
					int points = Integer.parseInt(parameter);
					L2Clan clan = player.getClan();
					if (clan.getLevel() < 5)
					{
						activeChar.sendMessage("Only clans of level 5 or above may receive reputation points.");
						showMainPage(activeChar);
						return false;
					}
					clan.setReputationScore(clan.getReputationScore() + points, true);
					activeChar.sendMessage("You " + (points > 0 ? "add " : "remove ") + Math.abs(points) + " points " + (points > 0 ? "to " : "from ") + clan.getName() + "'s reputation. Their current score is " + clan.getReputationScore());
				}
				catch (Exception e)
				{
					activeChar.sendMessage("Usage: //pledge <rep> <number>");
				}
			}
		}
		showMainPage(activeChar);
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void showMainPage(L2PcInstance activeChar)
	{
		AdminHelpPage.showHelpPage(activeChar, "game_menu.htm");
	}
	
}
