/*
    STACK360 - Web-based Business Management System
    Copyright (C) 2024 Arahant LLC

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses.
*/

/*
*/


/*
 * Created on Jan 12, 2006
 */
package com.arahant.utils.generators.flexWSClient.wsdl;

import java.util.*;

/**
 * Arahant
 */
public class Message extends WSDLEntity
{
	private Map<String, MessagePart>		partsMap;
	private List<MessagePart>	partsList;

	public Message()
	{
		this.partsMap = new HashMap<String, MessagePart>();
		this.partsList = new ArrayList<MessagePart>();
	}

	public void addPart(final MessagePart part)
	{
		this.partsList.add(part);
		this.partsMap.put(part.getName(), part);
	}

	public int getPartCount()
	{
		return this.partsList.size();
	}

	public MessagePart getPart(final int index)
	{
		return this.partsList.get(index);
	}

	public MessagePart getPart(String name)
	{
		final int index = name.indexOf(":");
		if (index > -1)
			name = name.substring(index + 1);
		
		return this.partsMap.get(name);
	}
	
	public Iterator<MessagePart> parts()
	{
		return this.partsList.iterator();
	}
}
