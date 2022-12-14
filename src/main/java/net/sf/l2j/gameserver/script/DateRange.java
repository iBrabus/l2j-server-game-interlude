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
package net.sf.l2j.gameserver.script;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * @author Luis Arias
 */
public class DateRange
{
	
	private final Date _startDate, _endDate;
	
	public DateRange(Date from, Date to)
	{
		_startDate = from;
		_endDate = to;
	}
	
	public static DateRange parse(String dateRange, DateFormat format)
	{
		String[] date = dateRange.split("-");
		if (date.length == 2)
		{
			try
			{
				Date start = format.parse(date[0]);
				Date end = format.parse(date[1]);
				
				return new DateRange(start, end);
			}
			catch (ParseException e)
			{
				System.err.println("Invalid Date Format.");
				e.printStackTrace();
			}
		}
		return new DateRange(null, null);
	}
	
	public boolean isValid()
	{
		return (_startDate == null) || (_endDate == null);
	}
	
	public boolean isWithinRange(Date date)
	{
		return date.after(_startDate) && date.before(_endDate);
	}
	
	public Date getEndDate()
	{
		return _endDate;
	}
	
	public Date getStartDate()
	{
		return _startDate;
	}
}
