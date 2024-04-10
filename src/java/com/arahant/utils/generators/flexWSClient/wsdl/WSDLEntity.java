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
 * Created on Feb 14, 2006
 */
package com.arahant.utils.generators.flexWSClient.wsdl;

import org.w3c.dom.Element;

/**
 * Arahant
 */
public class WSDLEntity
{
	private String	name;
	private Element	element;

	public WSDLEntity()
	{
		super();
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public Element getElement()
	{
		return this.element;
	}

	public void setElement(final Element element)
	{
		this.element = element;
	}
}
