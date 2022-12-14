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

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SummonInstance;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;

/**
 * @version $Revision: 1.2.4.4 $ $Date: 2005/03/29 23:15:33 $
 */
public final class RequestPetGetItem extends L2GameClientPacket
{
	private static final String _C__8f_REQUESTPETGETITEM = "[C] 8F RequestPetGetItem";
	
	private int _objectId;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		L2World world = L2World.getInstance();
		L2ItemInstance item = (L2ItemInstance) world.findObject(_objectId);
		if ((item == null) || (getClient().getActiveChar() == null))
		{
			return;
		}
		if (getClient().getActiveChar().getPet() instanceof L2SummonInstance)
		{
			sendPacket(new ActionFailed());
			return;
		}
		L2PetInstance pet = (L2PetInstance) getClient().getActiveChar().getPet();
		if ((pet == null) || pet.isDead() || pet.isOutOfControl())
		{
			sendPacket(new ActionFailed());
			return;
		}
		pet.getAI().setIntention(CtrlIntention.AI_INTENTION_PICK_UP, item);
	}
	
	@Override
	public String getType()
	{
		return _C__8f_REQUESTPETGETITEM;
	}
}
