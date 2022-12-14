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
package net.sf.l2j.gameserver.ai2;

import java.util.logging.Logger;

/**
 * @author -Wooden-
 */
public abstract class EventHandler
{
	protected static final Logger _log = Logger.getLogger(EventHandler.class.getName());
	
	abstract AiEventType getEvenType();
	
	/**
	 * @return the priority of this EventHandler INSIDE the EventHandlerSet
	 */
	abstract int getPriority();
	
	abstract void runImpl(AiParameters aiParams, AiEvent event);
	
	abstract AiPlugingParameters getPlugingParameters();
	
	@Override
	public String toString()
	{
		return "EventHandler: " + getEvenType().name() + " Priority:" + getPriority();
	}
}
