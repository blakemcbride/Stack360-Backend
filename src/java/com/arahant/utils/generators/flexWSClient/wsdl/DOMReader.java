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

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

import com.arahant.utils.DOMUtils;

/**
 * Arahant
 */
public class DOMReader
{
	private Element definitionsElement;
	private WebServiceDefinition def;
	
	public DOMReader()
	{
	}
	
	public WebServiceDefinition load(Element element) throws TransformerException
	{
		if (element.getNodeType() == Node.DOCUMENT_NODE)
			element = ((Document)element).getDocumentElement();
		
		this.definitionsElement = element;
		this.def = new WebServiceDefinition();
		this.def.setElement(element);
		this.def.setName(element.getAttribute("name"));
		this.def.setTargetNamespace(element.getAttribute("targetNamespace"));
		this.addNamespaces(element);
		
		this.loadTypes();
		this.loadMessages();
		this.loadBindings();
		this.loadServices();
		
		return this.def;
	}
	
	public void addNamespaces(final Element element)
	{
		final NamedNodeMap attrs = element.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++)
		{
			final Attr attr = (Attr)attrs.item(i);
			String name = attr.getName();
			if (name.startsWith("xmlns:"))
				name = DOMUtils.getNCName(name);
			
			this.def.addNamespace(name, attr.getValue());
		}
	}
	
	private void loadTypes() throws TransformerException
	{
		final Element element = (Element)DOMUtils.getNode(this.definitionsElement, "types");
		def.setTypesElement(element);
	}
	
	private void loadMessages() throws TransformerException
	{
		final NodeList nodes = DOMUtils.getNodes(this.definitionsElement, "message");
		for (int i = 0; i < nodes.getLength(); i++)
		{
			final Element element = (Element)nodes.item(i);
			final Message message = new Message();
			message.setElement(element);
			message.setName(element.getAttribute("name"));
			
			this.loadMessageParts(message);
			this.def.addMessage(message);
		}
	}
	
	private void loadBindings() throws TransformerException
	{
		final NodeList nodes = DOMUtils.getNodes(this.definitionsElement, "binding");
		for (int i = 0; i < nodes.getLength(); i++)
		{
			final Element element = (Element)nodes.item(i);
			final Binding binding = new Binding();
			binding.setElement(element);
			binding.setName(element.getAttribute("name"));
			binding.setType(element.getAttribute("type"));
			
			final Element subelement = (Element)DOMUtils.getNode(element, "binding");
			if (subelement != null)
			{
				binding.setStyle(subelement.getAttribute("style"));
				binding.setTransport(element.getAttribute("transport"));
			}
			
			this.loadOperations(binding);
			this.def.addBinding(binding);
		}
	}
	
	private void loadServices() throws TransformerException
	{
		final NodeList nodes = DOMUtils.getNodes(this.definitionsElement, "service");
		for (int i = 0; i < nodes.getLength(); i++)
		{
			final Element element = (Element)nodes.item(i);
			final Service service = new Service();
			service.setElement(element);
			service.setName(element.getAttribute("name"));
			this.loadPorts(service);
			this.def.addService(service);
		}
	}
	
	private void loadMessageParts(final Message message) throws TransformerException
	{
		final NodeList nodes = DOMUtils.getNodes(message.getElement(), "part");
		for (int i = 0; i < nodes.getLength(); i++)
		{
			final Element element = (Element)nodes.item(i);
			final MessagePart part = new MessagePart();
			part.setElement(element);
			part.setName(element.getAttribute("name"));
			
			Attr attr = element.getAttributeNode("type");
			if (attr == null)
			{
				attr = element.getAttributeNode("element");
				part.setIsType(false);
			} else
				part.setIsType(true);
			
			final String name = attr.getValue();
			final String namespace = DOMUtils.getNamespace(name);
			if (namespace != null)
			{
				final String uri = this.def.getNamespace(namespace);
				if (uri != null)
					part.setTypeNamespace(uri);
			}
			
			part.setType(name);
			message.addPart(part);
		}
	}
	
	private void loadOperations(final Binding binding) throws TransformerException
	{
		final NodeList nodes = DOMUtils.getNodes(binding.getElement(), "operation");
		for (int i = 0; i < nodes.getLength(); i++)
		{
			// operation element under Binding
			final Element bindingOpElement = (Element)nodes.item(i);
			final Operation op = new Operation();
			op.setElement(bindingOpElement);
			op.setName(bindingOpElement.getAttribute("name"));

			// operation element under portType element
			final Element portOpElement = (Element)DOMUtils.getNode(definitionsElement, "portType[@name='" + this.getNCName(binding.getType()) + "']/operation[@name='" + this.getNCName(op.getName()) + "']");
			if (portOpElement == null)
			{
				Logger.getLogger("com.integra.application.services").warn("Encountered a binding operation named '" + 
					op.getName() + "' for binding '" + binding.getName() + 
					"' that does not have a corresponding portType/operation node. Operation will be ignored.");
				continue;
			}
			
			op.setParameterOrder(portOpElement.getAttribute("parameterOrder"));

			final Element soapOpElement = (Element)DOMUtils.getNode(bindingOpElement, "operation");
			if (soapOpElement != null)
			{
				op.setSoapAction(soapOpElement.getAttribute("soapAction"));
				op.setStyle(soapOpElement.getAttribute("style"));
			}
			
			SOAPOperationPart part = this.loadOperation(op, portOpElement, bindingOpElement, "input");
			if (part == null)
				continue;

			op.setInputPart(part);
			
			part = this.loadOperation(op, portOpElement, bindingOpElement, "output");
			if (part == null)
				continue;

			op.setOutputPart(part);
			
			
			binding.addOperation(op);
		}
	}
	
	private SOAPOperationPart loadOperation(final Operation op, final Element portOpElement, final Element bindingOpElement, final String type) throws TransformerException
	{
		final SOAPOperationPart part = new SOAPOperationPart();

		// portType/operation/input
		final Element portPart = (Element)DOMUtils.getNode(portOpElement, type);
		if (portPart == null)
			return null;

		final String messageName = portPart.getAttribute("message");
		if (messageName == null)
			return null;
		
		final Message message = this.def.getMessage(messageName);
		if (message == null)
			return null;
		
		part.setMessage(message);
		
		// binding/operation/input
		final Element bindingPart = (Element)DOMUtils.getNode(bindingOpElement, type);
		if (bindingPart == null)
			return null;
		
		String name = bindingPart.getAttribute("name");
		if (name == null || name.length() == 0 || name.trim().length() == 0)
			name = op.getName() + "Request";
		
		part.setName(name);
		
		final Element bodyPart = (Element)DOMUtils.getNode(bindingPart, "body");
		if (bodyPart != null)
		{
			final String use = bodyPart.getAttribute("use");
			part.setEncoded(use != null && use.equals("encoded"));
			part.setEncodingStyle(bodyPart.getAttribute("encodingStyle"));
			part.setNamespace(bodyPart.getAttribute("namespace"));
			part.setParts(bodyPart.getAttribute("parts"));
		}
		
		return part;
	}
	
	private void loadPorts(final Service service) throws TransformerException
	{
		final NodeList nodes = DOMUtils.getNodes(service.getElement(), "port");
		for (int i = 0; i < nodes.getLength(); i++)
		{
			final Element portElement = (Element)nodes.item(i);
			final Port port = new Port();
			port.setName(portElement.getAttribute("name"));
			port.setElement(portElement);
			
			final String bindingName = portElement.getAttribute("binding");
			if (bindingName == null)
			{
				Logger.getLogger("com.integra.application.services").error("binding attribute not specified for port '" + port.getName() + "'. Binding will not be used.");
				continue;
			}
			
			final Binding binding = this.def.getBinding(this.getNCName(bindingName));
			if (binding == null)
			{
				Logger.getLogger("com.integra.application.services").error("Invalid binding attribute value '" + bindingName + "'. Binding will not be used.");
				continue;
			}
			
			port.setBinding(binding);
			
			final Element addressElement = (Element)DOMUtils.getNode(portElement, "address");
			if (addressElement == null)
			{
				Logger.getLogger("com.integra.application.services").error("Port " + port.getName() + " does not have a soap address element. As a result, the web service address cannot be determined.");
				continue;
			}

			final String location = addressElement.getAttribute("location");

			
			port.setLocation(location);
			
			
			service.addPort(port);
		}
	}
	
	private String getNCName(final String name)
	{
		if (name == null)
			return null;
		
		final int index = name.indexOf(":");
		if (index <= -1)
			return name;
		
		return name.substring(index + 1);
	}
}
