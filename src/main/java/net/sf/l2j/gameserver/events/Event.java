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
package net.sf.l2j.gameserver.events;

import javolution.util.FastList;

/**
 * @author Layane
 */
public class Event
{
	private final FastList<EventHandler> _handlers = new FastList<>();
	
	public void add(EventHandler handler)
	{
		if (!_handlers.contains(handler))
		{
			_handlers.add(handler);
		}
	}
	
	public void remove(EventHandler handler)
	{
		if (handler != null)
		{
			_handlers.remove(handler);
		}
	}
	
	public void fire(Object trigger, IEventParams params)
	{
		for (EventHandler handler : _handlers)
		{
			handler.handler(trigger, params);
		}
	}
	
	public void clear()
	{
		_handlers.clear();
	}
}
