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

import java.util.logging.Logger;

/**
 * Format (ch) dd
 * @author -Wooden-
 */
public final class RequestWithdrawPartyRoom extends L2GameClientPacket
{
	private static Logger _log = Logger.getLogger(RequestWithdrawPartyRoom.class.getName());
	private static final String _C__D0_02_REQUESTWITHDRAWPARTYROOM = "[C] D0:02 RequestWithdrawPartyRoom";
	private int _data1;
	private int _data2;
	
	@Override
	protected void readImpl()
	{
		_data1 = readD();
		_data2 = readD();
	}
	
	@Override
	protected void runImpl()
	{
		// TODO Auto-generated method stub
		_log.info("This packet is not well known : RequestWithdrawPartyRoom");
		_log.info("Data received: d:" + _data1 + " d:" + _data2);
	}
	
	@Override
	public String getType()
	{
		return _C__D0_02_REQUESTWITHDRAWPARTYROOM;
	}
}
