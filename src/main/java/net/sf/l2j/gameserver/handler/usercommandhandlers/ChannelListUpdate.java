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
package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.model.L2CommandChannel;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * When User press the "List Update" button in CCInfo window.
 * @author chris_00
 */
public class ChannelListUpdate implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		97
	};
	
	@Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
	{
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		
		L2CommandChannel channel = activeChar.getParty().getCommandChannel();
		
		activeChar.sendMessage("================");
		activeChar.sendMessage("Command Channel Information is not fully implemented now.");
		activeChar.sendMessage("There are " + channel.getPartys().size() + " Party's in the Channel.");
		activeChar.sendMessage(channel.getMemberCount() + " Players overall.");
		activeChar.sendMessage("Leader is " + channel.getChannelLeader().getName() + ".");
		activeChar.sendMessage("Partyleader, Membercount:");
		for (L2Party party : channel.getPartys())
		{
			activeChar.sendMessage(party.getPartyMembers().get(0).getName() + ", " + party.getMemberCount());
		}
		activeChar.sendMessage("================");
		return true;
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}
