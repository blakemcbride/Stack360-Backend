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


/**
 * Arahant
 */
public class MessagePart extends WSDLEntity
{
	private String	type;
	private boolean	isType;
	private String	typeNamespace;

	public MessagePart()
	{
		super();
	}

	public String getType()
	{
		return this.type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public String getTypeNamespace()
	{
		return this.typeNamespace;
	}

	public void setTypeNamespace(final String typeNamespace)
	{
		this.typeNamespace = typeNamespace;
	}

	public boolean isType()
	{
		return this.isType;
	}

	public void setIsType(final boolean isType)
	{
		this.isType = isType;
	}
}
