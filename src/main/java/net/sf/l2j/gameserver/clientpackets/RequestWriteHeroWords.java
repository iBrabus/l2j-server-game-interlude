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
 * Format chS c (id) 0xD0 h (subid) 0x0C S the hero's words :)
 * @author -Wooden-
 */
public final class RequestWriteHeroWords extends L2GameClientPacket
{
	private static final String _C__FE_0C_REQUESTWRITEHEROWORDS = "[C] D0:0C RequestWriteHeroWords";
	@SuppressWarnings("unused")
	private String _heroWords;
	
	@Override
	protected void readImpl()
	{
		_heroWords = readS();
	}
	
	@Override
	protected void runImpl()
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public String getType()
	{
		return _C__FE_0C_REQUESTWRITEHEROWORDS;
	}
}