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
public class Binding extends WSDLEntity
{
	private String	type;
	private String	style;
	private String	transport;
	private Map<String, Operation>		operations;
	private String	soapVersion = "text/xml";

	public Binding()
	{
		this.operations = new HashMap<String, Operation>();
	}

	public String getStyle()
	{
		return this.style;
	}

	public void setStyle(final String style)
	{
		this.style = style;
	}

	public String getTransport()
	{
		return this.transport;
	}

	public void setTransport(final String transport)
	{
		this.transport = transport;
	}

	public String getType()
	{
		return this.type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public void addOperation(final Operation op)
	{
		this.operations.put(op.getName(), op);
	}

	public Operation getOperation(String name)
	{
		final int index = name.indexOf(":");
		if (index > -1)
			name = name.substring(index + 1);

		return this.operations.get(name);
	}

	public Iterator<Operation> operations()
	{
		return this.operations.values().iterator();
	}

	public String getSoapVersion()
	{
		return this.soapVersion;
	}

	public void setSoapVersion(final String soapVersion)
	{
		this.soapVersion = soapVersion;
	}
}
