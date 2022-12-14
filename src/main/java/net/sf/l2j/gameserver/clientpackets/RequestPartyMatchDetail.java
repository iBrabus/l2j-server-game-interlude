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

import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.PartyMatchDetail;

/**
 * @version $Revision: 1.1.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestPartyMatchDetail extends L2GameClientPacket
{
	private static final String _C__71_REQUESTPARTYMATCHDETAIL = "[C] 71 RequestPartyMatchDetail";
	
	private int _objectId;
	@SuppressWarnings("unused")
	private int _unk1;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		// TODO analyze value unk1
		_unk1 = readD();
	}
	
	@Override
	protected void runImpl()
	{
		// TODO: this packet is currently for starting auto join
		L2PcInstance player = (L2PcInstance) L2World.getInstance().findObject(_objectId);
		if (player == null)
		{
			return;
		}
		PartyMatchDetail details = new PartyMatchDetail(player);
		sendPacket(details);
	}
	
	@Override
	public String getType()
	{
		return _C__71_REQUESTPARTYMATCHDETAIL;
	}
}
