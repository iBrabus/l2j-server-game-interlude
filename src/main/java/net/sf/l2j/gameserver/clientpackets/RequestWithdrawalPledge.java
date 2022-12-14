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
package net.sf.l2j.gameserver.clientpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.PledgeShowMemberListDelete;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestWithdrawalPledge extends L2GameClientPacket
{
	private static final String _C__26_REQUESTWITHDRAWALPLEDGE = "[C] 26 RequestWithdrawalPledge";
	
	@Override
	protected void readImpl()
	{
		// trigger
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (activeChar.getClan() == null)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER));
			return;
		}
		if (activeChar.isClanLeader())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.CLAN_LEADER_CANNOT_WITHDRAW));
			return;
		}
		if (activeChar.isInCombat())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_CANNOT_LEAVE_DURING_COMBAT));
			return;
		}
		
		L2Clan clan = activeChar.getClan();
		
		clan.removeClanMember(activeChar.getName(), System.currentTimeMillis() + (Config.ALT_CLAN_JOIN_DAYS * 86400000L)); // 24*60*60*1000 = 86400000
		
		SystemMessage sm = new SystemMessage(SystemMessageId.S1_HAS_WITHDRAWN_FROM_THE_CLAN);
		sm.addString(activeChar.getName());
		clan.broadcastToOnlineMembers(sm);
		sm = null;
		
		// Remove the Player From the Member list
		clan.broadcastToOnlineMembers(new PledgeShowMemberListDelete(activeChar.getName()));
		
		activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_HAVE_WITHDRAWN_FROM_CLAN));
		activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_MUST_WAIT_BEFORE_JOINING_ANOTHER_CLAN));
	}
	
	@Override
	public String getType()
	{
		return _C__26_REQUESTWITHDRAWALPLEDGE;
	}
}
