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

import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * @version $Revision: 1.4.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestReplyStartPledgeWar extends L2GameClientPacket
{
	private static final String _C__4e_REQUESTREPLYSTARTPLEDGEWAR = "[C] 4e RequestReplyStartPledgeWar";
	
	private int _answer;
	
	@Override
	protected void readImpl()
	{
		readS();
		_answer = readD();
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		L2PcInstance requestor = activeChar.getActiveRequester();
		if (requestor == null)
		{
			return;
		}
		
		if (_answer == 1)
		{
			ClanTable.getInstance().storeclanswars(requestor.getClanId(), activeChar.getClanId());
		}
		else
		{
			requestor.sendPacket(new SystemMessage(SystemMessageId.WAR_PROCLAMATION_HAS_BEEN_REFUSED));
		}
		activeChar.setActiveRequester(null);
		requestor.onTransactionResponse();
	}
	
	@Override
	public String getType()
	{
		return _C__4e_REQUESTREPLYSTARTPLEDGEWAR;
	}
}