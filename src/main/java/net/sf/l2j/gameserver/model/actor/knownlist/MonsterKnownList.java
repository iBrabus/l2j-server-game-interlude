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
package net.sf.l2j.gameserver.model.actor.knownlist;

import net.sf.l2j.gameserver.ai.CtrlEvent;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class MonsterKnownList extends AttackableKnownList
{
	public MonsterKnownList(L2MonsterInstance activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public boolean addKnownObject(L2Object object)
	{
		return addKnownObject(object, null);
	}
	
	@Override
	public boolean addKnownObject(L2Object object, L2Character dropper)
	{
		if (!super.addKnownObject(object, dropper))
		{
			return false;
		}
		
		// Set the L2MonsterInstance Intention to AI_INTENTION_ACTIVE if the state was AI_INTENTION_IDLE
		if ((object instanceof L2PcInstance) && (getActiveChar().getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE))
		{
			getActiveChar().getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null);
		}
		return true;
	}
	
	@Override
	public boolean removeKnownObject(L2Object object)
	{
		if (!super.removeKnownObject(object))
		{
			return false;
		}
		
		if (!(object instanceof L2Character))
		{
			return true;
		}
		
		if (getActiveChar().hasAI())
		{
			// Notify the L2MonsterInstance AI with EVT_FORGET_OBJECT
			getActiveChar().getAI().notifyEvent(CtrlEvent.EVT_FORGET_OBJECT, object);
			
			// TODO Remove this function because it's already done in L2Character.removeKnownObject
			// Set the current target to null if the forgotten L2Object was the targeted L2Object
			// L2Character temp = (L2Character)object;
			
			// if (getTarget() == temp)
			// setTarget(null);
		}
		
		if (getActiveChar().isVisible() && getKnownPlayers().isEmpty())
		{
			// Clear the _aggroList of the L2MonsterInstance
			getActiveChar().clearAggroList();
			
			// Remove all L2Object from _knownObjects and _knownPlayer of the L2MonsterInstance then cancel Attak or Cast and notify AI
			// removeAllKnownObjects();
			
			// TODO Remove this function because it's already done in L2Attackable.removeKnownObject
			// Set the L2MonsterInstance AI to AI_INTENTION_IDLE
			// if (hasAI())
			// getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, null);
		}
		
		return true;
	}
	
	@Override
	public final L2MonsterInstance getActiveChar()
	{
		return (L2MonsterInstance) super.getActiveChar();
	}
}
