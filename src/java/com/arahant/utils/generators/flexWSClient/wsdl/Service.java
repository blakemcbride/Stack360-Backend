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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Arahant
 */
public class Service extends WSDLEntity
{
	private Map<String, Port>		ports;

	public Service()
	{
		super();
		this.ports = new HashMap<String, Port>();
	}

	public void addPort(final Port port)
	{
		this.ports.put(port.getName(), port);
	}

	public Port getPort(String name)
	{
		final int index = name.indexOf(":");
		if (index > -1)
			name = name.substring(index + 1);
		
		return this.ports.get(name);
	}
	
	public Iterator<Port> ports()
	{
		return this.ports.values().iterator();
	}
}
