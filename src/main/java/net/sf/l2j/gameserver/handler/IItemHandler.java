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
package net.sf.l2j.gameserver.handler;

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;

/**
 * Mother class of all itemHandlers.<BR>
 * <BR>
 * an IItemHandler implementation has to be stateless
 * @version $Revision: 1.1.4.3 $ $Date: 2005/03/27 15:30:09 $
 */

public interface IItemHandler
{
	/**
	 * Launch task associated to the item.
	 * @param playable
	 * @param item : L2ItemInstance designating the item to use
	 */
	public void useItem(L2PlayableInstance playable, L2ItemInstance item);
	
	/**
	 * Returns the list of item IDs corresponding to the type of item.<BR>
	 * <BR>
	 * <B><I>Use :</I></U><BR>
	 * This method is called at initialization to register all the item IDs automatically
	 * @return int[] designating all itemIds for a type of item.
	 */
	public int[] getItemIds();
}
