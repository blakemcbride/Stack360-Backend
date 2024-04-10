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
 * Created on Feb 3, 2006
 */
package com.arahant.utils.generators.flexWSClient.wsdl;

/**
 * Arahant
 */
public class SOAPOperationPart extends OperationPart
{
	private String	parts;
	private boolean	encoded;
	private String	encodingStyle;
	private String	namespace;
	private String	style;

	public SOAPOperationPart()
	{
		super();
	}

	public boolean isEncoded()
	{
		return this.encoded;
	}

	public void setEncoded(final boolean encoded)
	{
		this.encoded = encoded;
	}

	public String getEncodingStyle()
	{
		return this.encodingStyle;
	}

	public void setEncodingStyle(final String encodingStyle)
	{
		this.encodingStyle = encodingStyle;
	}

	public String getNamespace()
	{
		return this.namespace;
	}

	public void setNamespace(final String namespace)
	{
		this.namespace = namespace;
	}

	public String getParts()
	{
		return this.parts;
	}

	public void setParts(final String parts)
	{
		this.parts = parts;
	}

	public String getStyle()
	{
		return this.style;
	}

	public void setStyle(final String style)
	{
		this.style = style;
	}
}
