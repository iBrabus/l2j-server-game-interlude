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

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.cache.CrestCache;
import net.sf.l2j.gameserver.serverpackets.AllyCrest;

/**
 * @version $Revision: 1.3.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestAllyCrest extends L2GameClientPacket
{
	private static final String _C__88_REQUESTALLYCREST = "[C] 88 RequestAllyCrest";
	private static Logger _log = Logger.getLogger(RequestAllyCrest.class.getName());
	
	private int _crestId;
	
	/**
	 * packet type id 0x88 format: cd
	 */
	@Override
	protected void readImpl()
	{
		_crestId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		if (Config.DEBUG)
		{
			_log.fine("allycrestid " + _crestId + " requested");
		}
		
		byte[] data = CrestCache.getInstance().getAllyCrest(_crestId);
		
		if (data != null)
		{
			AllyCrest ac = new AllyCrest(_crestId, data);
			sendPacket(ac);
		}
		else
		{
			if (Config.DEBUG)
			{
				_log.fine("allycrest is missing:" + _crestId);
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__88_REQUESTALLYCREST;
	}
}
