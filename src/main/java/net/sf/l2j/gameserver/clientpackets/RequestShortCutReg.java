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

import net.sf.l2j.gameserver.model.L2ShortCut;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.ShortCutRegister;

/**
 * @version $Revision: 1.3.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestShortCutReg extends L2GameClientPacket
{
	private static final String _C__33_REQUESTSHORTCUTREG = "[C] 33 RequestShortCutReg";
	
	private int _type;
	private int _id;
	private int _slot;
	private int _page;
	private int _unk;
	
	@Override
	protected void readImpl()
	{
		_type = readD();
		int slot = readD();
		_id = readD();
		_unk = readD();
		
		_slot = slot % 12;
		_page = slot / 12;
		
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		switch (_type)
		{
			case 0x01: // item
			case 0x03: // action
			case 0x04: // macro
			case 0x05: // recipe
			{
				L2ShortCut sc = new L2ShortCut(_slot, _page, _type, _id, -1, _unk);
				sendPacket(new ShortCutRegister(sc));
				activeChar.registerShortCut(sc);
				break;
			}
			case 0x02: // skill
			{
				int level = activeChar.getSkillLevel(_id);
				if (level > 0)
				{
					L2ShortCut sc = new L2ShortCut(_slot, _page, _type, _id, level, _unk);
					sendPacket(new ShortCutRegister(sc));
					activeChar.registerShortCut(sc);
				}
				break;
			}
		}
		
	}
	
	@Override
	public String getType()
	{
		return _C__33_REQUESTSHORTCUTREG;
	}
}
