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
import net.sf.l2j.gameserver.serverpackets.RecipeItemMakeInfo;

public final class RequestRecipeItemMakeInfo extends L2GameClientPacket
{
	private static final String _C__AE_REQUESTRECIPEITEMMAKEINFO = "[C] AE RequestRecipeItemMakeInfo";
	
	private int _id;
	private L2PcInstance _activeChar;
	
	@Override
	protected void readImpl()
	{
		_id = readD();
		_activeChar = getClient().getActiveChar();
	}
	
	@Override
	protected void runImpl()
	{
		RecipeItemMakeInfo response = new RecipeItemMakeInfo(_id, _activeChar);
		sendPacket(response);
	}
	
	@Override
	public String getType()
	{
		return _C__AE_REQUESTRECIPEITEMMAKEINFO;
	}
}
