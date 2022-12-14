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

import net.sf.l2j.gameserver.datatables.HennaTreeTable;
import net.sf.l2j.gameserver.model.L2HennaInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.HennaEquipList;

/**
 * RequestHennaList - 0xba
 * @author Tempy
 */
public final class RequestHennaList extends L2GameClientPacket
{
	private static final String _C__BA_RequestHennaList = "[C] ba RequestHennaList";
	
	// This is just a trigger packet...
	@SuppressWarnings("unused")
	private int _unknown;
	
	@Override
	protected void readImpl()
	{
		_unknown = readD(); // ??
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		L2HennaInstance[] henna = HennaTreeTable.getInstance().getAvailableHenna(activeChar.getClassId());
		HennaEquipList he = new HennaEquipList(activeChar, henna);
		activeChar.sendPacket(he);
	}
	
	@Override
	public String getType()
	{
		return _C__BA_RequestHennaList;
	}
}