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

import java.util.Iterator;
import java.util.Map;

/**
 * Arahant
 */
public class Operation extends WSDLEntity
{
	private String				parameterOrder;

	private SOAPOperationPart	inputPart;
	private SOAPOperationPart	outputPart;
	private Map<String, SOAPOperationPart>					faults;

	private String				soapAction;
	private String				style;

	public Operation()
	{
		super();
	}

	public String getParameterOrder()
	{
		return this.parameterOrder;
	}

	public void setParameterOrder(final String parameterOrder)
	{
		this.parameterOrder = parameterOrder;
	}

	public String getSoapAction()
	{
		return this.soapAction;
	}

	public void setSoapAction(final String soapAction)
	{
		this.soapAction = soapAction;
	}

	public String getStyle()
	{
		return this.style;
	}

	public void setStyle(final String style)
	{
		this.style = style;
	}

	public SOAPOperationPart getInputPart()
	{
		return this.inputPart;
	}

	public void setInputPart(final SOAPOperationPart inputPart)
	{
		this.inputPart = inputPart;
	}

	public SOAPOperationPart getOutputPart()
	{
		return this.outputPart;
	}

	public void setOutputPart(final SOAPOperationPart outputPart)
	{
		this.outputPart = outputPart;
	}

	public void addFault(final SOAPOperationPart faultPart)
	{
		this.faults.put(faultPart.getName(), faultPart);
	}

	public SOAPOperationPart getFault(String name)
	{
		final int index = name.indexOf(":");
		if (index > -1)
			name = name.substring(index + 1);
		
		return this.faults.get(name);
	}

	public Iterator<SOAPOperationPart> faults()
	{
		return this.faults.values().iterator();
	}

}
