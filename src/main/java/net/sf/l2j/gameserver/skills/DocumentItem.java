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
package net.sf.l2j.gameserver.skills;

import java.io.File;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;
import net.sf.l2j.gameserver.Item;
import net.sf.l2j.gameserver.templates.L2Armor;
import net.sf.l2j.gameserver.templates.L2ArmorType;
import net.sf.l2j.gameserver.templates.L2EtcItem;
import net.sf.l2j.gameserver.templates.L2EtcItemType;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.gameserver.templates.L2WeaponType;
import net.sf.l2j.gameserver.templates.StatsSet;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author mkizub
 */
final class DocumentItem extends DocumentBase
{
	private Item _currentItem = null;
	private final List<L2Item> _itemsInFile = new FastList<>();
	private Map<Integer, Item> _itemData = new FastMap<>();
	
	public DocumentItem(Map<Integer, Item> pItemData, File file)
	{
		super(file);
		_itemData = pItemData;
	}
	
	private void setCurrentItem(Item item)
	{
		_currentItem = item;
	}
	
	@Override
	protected StatsSet getStatsSet()
	{
		return _currentItem.set;
	}
	
	@Override
	protected String getTableValue(String name)
	{
		return _tables.get(name)[_currentItem.currentLevel];
	}
	
	@Override
	protected String getTableValue(String name, int idx)
	{
		return _tables.get(name)[idx - 1];
	}
	
	@Override
	protected void parseDocument(Document doc)
	{
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equalsIgnoreCase(n.getNodeName()))
			{
				
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("item".equalsIgnoreCase(d.getNodeName()))
					{
						setCurrentItem(new Item());
						parseItem(d);
						_itemsInFile.add(_currentItem.item);
						resetTable();
					}
				}
			}
			else if ("item".equalsIgnoreCase(n.getNodeName()))
			{
				setCurrentItem(new Item());
				parseItem(n);
				_itemsInFile.add(_currentItem.item);
			}
		}
	}
	
	protected void parseItem(Node n)
	{
		int itemId = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
		String itemName = n.getAttributes().getNamedItem("name").getNodeValue();
		
		_currentItem.id = itemId;
		_currentItem.name = itemName;
		
		Item item;
		if ((item = _itemData.get(_currentItem.id)) == null)
		{
			throw new IllegalStateException("No SQL data for Item ID: " + itemId + " - name: " + itemName);
		}
		_currentItem.set = item.set;
		_currentItem.type = item.type;
		
		Node first = n.getFirstChild();
		for (n = first; n != null; n = n.getNextSibling())
		{
			if ("table".equalsIgnoreCase(n.getNodeName()))
			{
				parseTable(n);
			}
		}
		for (n = first; n != null; n = n.getNextSibling())
		{
			if ("set".equalsIgnoreCase(n.getNodeName()))
			{
				parseBeanSet(n, _itemData.get(_currentItem.id).set, 1);
			}
		}
		for (n = first; n != null; n = n.getNextSibling())
		{
			if ("for".equalsIgnoreCase(n.getNodeName()))
			{
				makeItem();
				parseTemplate(n, _currentItem.item);
			}
		}
	}
	
	private void makeItem()
	{
		if (_currentItem.item != null)
		{
			return;
		}
		if (_currentItem.type instanceof L2ArmorType)
		{
			_currentItem.item = new L2Armor((L2ArmorType) _currentItem.type, _currentItem.set);
		}
		else if (_currentItem.type instanceof L2WeaponType)
		{
			_currentItem.item = new L2Weapon((L2WeaponType) _currentItem.type, _currentItem.set);
		}
		else if (_currentItem.type instanceof L2EtcItemType)
		{
			_currentItem.item = new L2EtcItem((L2EtcItemType) _currentItem.type, _currentItem.set);
		}
		else
		{
			throw new Error("Unknown item type " + _currentItem.type);
		}
	}
	
	public List<L2Item> getItemList()
	{
		return _itemsInFile;
	}
}
