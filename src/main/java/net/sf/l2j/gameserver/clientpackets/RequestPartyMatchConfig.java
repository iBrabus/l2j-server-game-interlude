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

/**
 * @version $Revision: 1.1.4.2 $ $Date: 2005/03/27 15:29:30 $
 */

public final class RequestPartyMatchConfig extends L2GameClientPacket
{
	private static final String _C__6F_REQUESTPARTYMATCHCONFIG = "[C] 6F RequestPartyMatchConfig";
	
	private int _automaticRegistration;
	private int _showLevel;
	private int _showClass;
	private String _memo;
	
	@Override
	protected void readImpl()
	{
		_automaticRegistration = readD();
		_showLevel = readD();
		_showClass = readD();
		
		/*
		 * TODO: Check if this this part of the packet has been removed by latest versions. try { _memo = readS(); } catch (BufferUnderflowException e) { _memo = ""; _log.warning("Memo field non existant in packet. Notify devs."); e.printStackTrace(); }
		 */
	}
	
	@Override
	protected void runImpl()
	{
		// TODO: this packet is currently for creating a new party room
		if (getClient().getActiveChar() == null)
		{
			return;
		}
		
		getClient().getActiveChar().setPartyMatchingAutomaticRegistration(_automaticRegistration == 1);
		getClient().getActiveChar().setPartyMatchingShowLevel(_showLevel == 1);
		getClient().getActiveChar().setPartyMatchingShowClass(_showClass == 1);
		getClient().getActiveChar().setPartyMatchingMemo(_memo);
	}
	
	@Override
	public String getType()
	{
		return _C__6F_REQUESTPARTYMATCHCONFIG;
	}
}
