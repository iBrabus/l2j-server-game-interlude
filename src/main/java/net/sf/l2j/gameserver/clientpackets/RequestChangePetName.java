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

import net.sf.l2j.gameserver.datatables.PetNameTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.NpcInfo;
import net.sf.l2j.gameserver.serverpackets.PetInfo;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * @version $Revision: 1.3.4.4 $ $Date: 2005/04/06 16:13:48 $
 */
public final class RequestChangePetName extends L2GameClientPacket
{
	private static final String REQUESTCHANGEPETNAME__C__89 = "[C] 89 RequestChangePetName";
	
	private String _name;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
	}
	
	@Override
	protected void runImpl()
	{
		L2Character activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		final L2Summon pet = activeChar.getPet();
		if (pet == null)
		{
			return;
		}
		
		if (pet.getName() != null)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.NAMING_YOU_CANNOT_SET_NAME_OF_THE_PET));
			return;
		}
		else if (PetNameTable.getInstance().doesPetNameExist(_name, pet.getTemplate().npcId))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.NAMING_ALREADY_IN_USE_BY_ANOTHER_PET));
			return;
		}
		else if ((_name.length() < 3) || (_name.length() > 16))
		{
			SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
			sm.addString("Your pet's name can be up to 16 characters.");
			// SystemMessage sm = new SystemMessage(SystemMessage.NAMING_PETNAME_UP_TO_8CHARS);
			activeChar.sendPacket(sm);
			sm = null;
			
			return;
		}
		else if (!PetNameTable.getInstance().isValidPetName(_name))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.NAMING_PETNAME_CONTAINS_INVALID_CHARS));
			return;
		}
		
		pet.setName(_name);
		pet.broadcastPacket(new NpcInfo(pet, activeChar));
		activeChar.sendPacket(new PetInfo(pet));
		// The PetInfo packet wipes the PartySpelled (list of active spells' icons). Re-add them
		pet.updateEffectIcons(true);
		
		// set the flag on the control item to say that the pet has a name
		if (pet instanceof L2PetInstance)
		{
			L2ItemInstance controlItem = pet.getOwner().getInventory().getItemByObjectId(pet.getControlItemId());
			if (controlItem != null)
			{
				controlItem.setCustomType2(1);
				controlItem.updateDatabase();
				InventoryUpdate iu = new InventoryUpdate();
				iu.addModifiedItem(controlItem);
				activeChar.sendPacket(iu);
			}
		}
	}
	
	@Override
	public String getType()
	{
		return REQUESTCHANGEPETNAME__C__89;
	}
}
