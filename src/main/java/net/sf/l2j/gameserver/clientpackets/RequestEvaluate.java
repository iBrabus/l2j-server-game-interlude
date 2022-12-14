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

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;

public final class RequestEvaluate extends L2GameClientPacket
{
	private static final String _C__B9_REQUESTEVALUATE = "[C] B9 RequestEvaluate";
	
	@SuppressWarnings("unused")
	private int _targetId;
	
	@Override
	protected void readImpl()
	{
		_targetId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		SystemMessage sm;
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (!(activeChar.getTarget() instanceof L2PcInstance))
		{
			sm = new SystemMessage(SystemMessageId.TARGET_IS_INCORRECT);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}
		
		if (activeChar.getLevel() < 10)
		{
			sm = new SystemMessage(SystemMessageId.ONLY_LEVEL_SUP_10_CAN_RECOMMEND);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}
		
		if (activeChar.getTarget() == activeChar)
		{
			sm = new SystemMessage(SystemMessageId.YOU_CANNOT_RECOMMEND_YOURSELF);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}
		
		if (activeChar.getRecomLeft() <= 0)
		{
			sm = new SystemMessage(SystemMessageId.NO_MORE_RECOMMENDATIONS_TO_HAVE);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}
		
		L2PcInstance target = (L2PcInstance) activeChar.getTarget();
		
		if (target.getRecomHave() >= 255)
		{
			sm = new SystemMessage(SystemMessageId.YOU_NO_LONGER_RECIVE_A_RECOMMENDATION);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}
		
		if (!activeChar.canRecom(target))
		{
			sm = new SystemMessage(SystemMessageId.THAT_CHARACTER_IS_RECOMMENDED);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}
		
		activeChar.giveRecom(target);
		
		sm = new SystemMessage(SystemMessageId.YOU_HAVE_RECOMMENDED);
		sm.addString(target.getName());
		sm.addNumber(activeChar.getRecomLeft());
		activeChar.sendPacket(sm);
		
		sm = new SystemMessage(SystemMessageId.YOU_HAVE_BEEN_RECOMMENDED);
		sm.addString(activeChar.getName());
		target.sendPacket(sm);
		sm = null;
		
		activeChar.sendPacket(new UserInfo(activeChar));
		target.broadcastUserInfo();
	}
	
	@Override
	public String getType()
	{
		return _C__B9_REQUESTEVALUATE;
	}
}
