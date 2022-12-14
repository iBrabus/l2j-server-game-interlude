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
package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub
 */
public abstract class Condition implements ConditionListener
{
	private ConditionListener _listener;
	private String _msg;
	private boolean _result;
	
	public final void setMessage(String msg)
	{
		_msg = msg;
	}
	
	public final String getMessage()
	{
		return _msg;
	}
	
	void setListener(ConditionListener listener)
	{
		_listener = listener;
		notifyChanged();
	}
	
	final ConditionListener getListener()
	{
		return _listener;
	}
	
	public final boolean test(Env env)
	{
		boolean res = testImpl(env);
		if ((_listener != null) && (res != _result))
		{
			_result = res;
			notifyChanged();
		}
		return res;
	}
	
	abstract boolean testImpl(Env env);
	
	@Override
	public void notifyChanged()
	{
		if (_listener != null)
		{
			_listener.notifyChanged();
		}
	}
}
