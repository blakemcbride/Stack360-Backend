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

import org.w3c.dom.Element;

/**
 * Arahant
 * 
 * 
 */

/*
<wsdl:definitions name="nmtoken"? targetNamespace="uri"?>

    <import namespace="uri" location="uri"/>*

    <wsdl:documentation .... /> ?

    <wsdl:types> ?
        <wsdl:documentation .... />?
        <xsd:schema .... />*
        <-- extensibility element --> *
    </wsdl:types>

    <wsdl:message name="nmtoken"> *
        <wsdl:documentation .... />?
        <part name="nmtoken" element="qname"? type="qname"?/> *
    </wsdl:message>

    <wsdl:portType name="nmtoken">*
        <wsdl:documentation .... />?
        <wsdl:operation name="nmtoken">*
           <wsdl:documentation .... /> ?
           <wsdl:input name="nmtoken"? message="qname">?
               <wsdl:documentation .... /> ?
           </wsdl:input>
           <wsdl:output name="nmtoken"? message="qname">?
               <wsdl:documentation .... /> ?
           </wsdl:output>
           <wsdl:fault name="nmtoken" message="qname"> *
               <wsdl:documentation .... /> ?
           </wsdl:fault>
        </wsdl:operation>
    </wsdl:portType>

    <wsdl:binding name="nmtoken" type="qname">*
        <wsdl:documentation .... />?
        <-- extensibility element --> *
        <soap:binding style="rpc|document" transport="uri">
        <wsdl:operation name="nmtoken">*
           <wsdl:documentation .... /> ?
           <-- extensibility element --> *
           <soap:operation soapAction="uri"? style="rpc|document"?>?
           <wsdl:input> ?
               <wsdl:documentation .... /> ?
               <-- extensibility element --> *
               <soap:body parts="nmtokens"? use="literal|encoded"
                          encodingStyle="uri-list"? namespace="uri"?>
               <soap:header message="qname" part="nmtoken" use="literal|encoded"
                            encodingStyle="uri-list"? namespace="uri"?>*
                 <soap:headerfault message="qname" part="nmtoken" use="literal|encoded"
                                   encodingStyle="uri-list"? namespace="uri"?/>*
               <soap:header>                                
           </wsdl:input>
           <wsdl:output> ?
               <wsdl:documentation .... /> ?
               <-- extensibility element --> *
               <soap:body parts="nmtokens"? use="literal|encoded"
                          encodingStyle="uri-list"? namespace="uri"?>
               <soap:header message="qname" part="nmtoken" use="literal|encoded"
                            encodingStyle="uri-list"? namespace="uri"?>*
                 <soap:headerfault message="qname" part="nmtoken" use="literal|encoded"
                                   encodingStyle="uri-list"? namespace="uri"?/>*
               <soap:header>                                
           </wsdl:output>
           <wsdl:fault name="nmtoken"> *
               <wsdl:documentation .... /> ?
               <-- extensibility element --> *
               <soap:fault name="nmtoken" use="literal|encoded"
                           encodingStyle="uri-list"? namespace="uri"?>
           </wsdl:fault>
        </wsdl:operation>
    </wsdl:binding>

    <wsdl:service name="nmtoken"> *
        <wsdl:documentation .... />?
        <wsdl:port name="nmtoken" binding="qname"> *
           <wsdl:documentation .... /> ?
           <-- extensibility element -->
           <soap:address location="uri"/> 
        </wsdl:port>
        <-- extensibility element -->
    </wsdl:service>

    <-- extensibility element --> *

</wsdl:definitions>



------------------------------------------
RPC WSDL
------------------------------------------

<?xml version="1.0" encoding="utf-8"?>
<wsdl:definitions 
	xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" 
	xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" 
	xmlns:tns="TARGET_NAMESPACE" 
	xmlns:s="http://www.w3.org/2001/XMLSchema" 
	xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" 
	targetNamespace="TARGET_NAMESPACE" 
	xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">
  
  <wsdl:types>
    <s:schema elementFormDefault="qualified" targetNamespace="TARGET_NAMESPACE">
      <s:complexType name="Person">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="1" name="name" type="tns:Name" />
          <s:element minOccurs="1" maxOccurs="1" name="age" type="s:int" />
          <s:element minOccurs="1" maxOccurs="1" name="married" type="s:boolean" />
          <s:element minOccurs="1" maxOccurs="1" name="netWorth" type="s:double" />
          <s:element minOccurs="0" maxOccurs="1" name="spousesName" type="tns:Name" />
        </s:sequence>
      </s:complexType>
      <s:complexType name="Name">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="1" name="firstName" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="lastName" type="s:string" />
        </s:sequence>
      </s:complexType>
    </s:schema>
  </wsdl:types>

  <wsdl:message name="_RPC_MESSAGE_NAME_SoapIn">
    <wsdl:part name="_RPC_PARAMETER_1_" type="s:string" />
    <wsdl:part name="_RPC_PARAMETER_2_" type="tns:Person" />
  </wsdl:message>

  <wsdl:message name="_RPC_MESSAGE_NAME_SoapOut">
    <wsdl:part name="_RPC_MESSAGE_NAME_Result" type="s:string" />
  </wsdl:message>
  
  <wsdl:portType name="RPCBinding">
    <wsdl:operation name="_RPC_METHOD_NAME_">
      <wsdl:input name="_RPC_MESSAGE_NAME_" message="tns:_RPC_MESSAGE_NAME_SoapIn" />
      <wsdl:output name="_RPC_MESSAGE_NAME_" message="tns:_RPC_MESSAGE_NAME_SoapOut" />
    </wsdl:operation>
  </wsdl:portType>

  <wsdl:binding name="RPCBinding" type="tns:RPCBinding">
    <soap:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="_RPC_METHOD_NAME_">
      <soap:operation soapAction="_RPC_SOAP_ACTION_" style="rpc" />
      <wsdl:input name="_RPC_MESSAGE_NAME_">
        <soap:body use="literal" namespace="TARGET_NAMESPACE" />
      </wsdl:input>
      <wsdl:output name="_RPC_MESSAGE_NAME_">
        <soap:body use="literal" namespace="TARGET_NAMESPACE" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>

  <wsdl:service name="JohnsServiceName">
    <wsdl:port name="RPCBinding" binding="tns:RPCBinding">
      <soap:address location="http://localhost:3226/WSTest/Service.asmx" />
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>

------------------------------------------
RPC SOAP Request over HTTP
------------------------------------------

POST /WSTest/Service.asmx HTTP/1.1
Host: localhost
Content-Type: text/xml; charset=utf-8
Content-Length: length
SOAPAction: "_RPC_SOAP_ACTION_"

<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
	xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	
  <soap:Body>
    <_RPC_METHOD_NAME_ xmlns="TARGET_NAMESPACE">
      <_RPC_PARAMETER_1_ xmlns="">string</_RPC_PARAMETER_1_>
      <_RPC_PARAMETER_2_ xmlns="">
        <name xmlns="TARGET_NAMESPACE">
          <firstName>string</firstName>
          <lastName>string</lastName>
        </name>
        <age xmlns="TARGET_NAMESPACE">int</age>
        <married xmlns="TARGET_NAMESPACE">boolean</married>
        <netWorth xmlns="TARGET_NAMESPACE">double</netWorth>
        <spousesName xmlns="TARGET_NAMESPACE">
          <firstName>string</firstName>
          <lastName>string</lastName>
        </spousesName>
      </_RPC_PARAMETER_2_>
    </_RPC_METHOD_NAME_>
  </soap:Body>
</soap:Envelope>

------------------------------------------
RPC SOAP Response over HTTP
------------------------------------------

HTTP/1.1 200 OK
Content-Type: text/xml; charset=utf-8
Content-Length: length

<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <_RPC_METHOD_NAME_Response xmlns="TARGET_NAMESPACE">
      <_RPC_MESSAGE_NAME_Result xmlns="">string</_RPC_MESSAGE_NAME_Result>
    </_RPC_METHOD_NAME_Response>
  </soap:Body>
</soap:Envelope>

------------------------------------------
Document WSDL
------------------------------------------

<?xml version="1.0" encoding="utf-8"?>
<wsdl:definitions 
	xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" 
	xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" 
	xmlns:s="http://www.w3.org/2001/XMLSchema" 
	xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" 
	xmlns:tns="TARGET_NAMESPACE" 
	targetNamespace="TARGET_NAMESPACE" 
	xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">
	
  <wsdl:types>
    <s:schema elementFormDefault="qualified" targetNamespace="TARGET_NAMESPACE">
      <s:element name="_DOCUMENT_REQUEST_ELEMENT_NAME_">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="person" type="tns:Person" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="Person">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="1" name="name" type="tns:Name" />
          <s:element minOccurs="1" maxOccurs="1" name="age" type="s:int" />
          <s:element minOccurs="1" maxOccurs="1" name="married" type="s:boolean" />
          <s:element minOccurs="1" maxOccurs="1" name="netWorth" type="s:double" />
          <s:element minOccurs="0" maxOccurs="1" name="spousesName" type="tns:Name" />
        </s:sequence>
      </s:complexType>
      <s:complexType name="Name">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="1" name="firstName" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="lastName" type="s:string" />
        </s:sequence>
      </s:complexType>
    </s:schema>

	<s:schema elementFormDefault="qualified" targetNamespace="TARGET_NAMESPACE">
      <s:element name="_DOCUMENT_MESSAGE_NAME_Response">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="_DOCUMENT_MESSAGE_NAME_Result" type="tns:Person" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="Person">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="1" name="name" type="tns:Name" />
          <s:element minOccurs="1" maxOccurs="1" name="age" type="s:int" />
          <s:element minOccurs="1" maxOccurs="1" name="married" type="s:boolean" />
          <s:element minOccurs="1" maxOccurs="1" name="netWorth" type="s:double" />
          <s:element minOccurs="0" maxOccurs="1" name="spousesName" type="tns:Name" />
        </s:sequence>
      </s:complexType>
      <s:complexType name="Name">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="1" name="firstName" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="lastName" type="s:string" />
        </s:sequence>
      </s:complexType>
    </s:schema>
    
  </wsdl:types>
  
  <wsdl:message name="_DOCUMENT_MESSAGE_NAME_SoapIn">
    <wsdl:part name="parameters" element="tns:_DOCUMENT_REQUEST_ELEMENT_NAME_" />
  </wsdl:message>
  
  <wsdl:message name="_DOCUMENT_MESSAGE_NAME_SoapOut">
    <wsdl:part name="parameters" element="tns:_DOCUMENT_MESSAGE_NAME_Response" />
  </wsdl:message>
  
  <wsdl:portType name="DocumentBinding">
    <wsdl:operation name="documentMethod">
      <wsdl:input name="_DOCUMENT_MESSAGE_NAME_" message="tns:_DOCUMENT_MESSAGE_NAME_SoapIn" />
      <wsdl:output name="_DOCUMENT_MESSAGE_NAME_" message="tns:_DOCUMENT_MESSAGE_NAME_SoapOut" />
    </wsdl:operation>
  </wsdl:portType>
  
  <wsdl:binding name="DocumentBinding" type="tns:DocumentBinding">
    <soap:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="documentMethod">
      <soap:operation soapAction="_DOCUMENT_SOAP_ACTION_" style="document" />
      <wsdl:input name="_DOCUMENT_MESSAGE_NAME_">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="_DOCUMENT_MESSAGE_NAME_">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  
  <wsdl:service name="JohnsServiceName">
    <wsdl:port name="DocumentBinding" binding="tns:DocumentBinding">
      <soap:address location="http://localhost:3226/WSTest/Service.asmx" />
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>

------------------------------------------
Document SOAP Request over HTTP
------------------------------------------

POST /WSTest/Service.asmx HTTP/1.1
Host: localhost
Content-Type: text/xml; charset=utf-8
Content-Length: length
SOAPAction: "_DOCUMENT_SOAP_ACTION_"

<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <_DOCUMENT_REQUEST_ELEMENT_NAME_ xmlns="TARGET_NAMESPACE">
      <person>
        <name>
          <firstName>string</firstName>
          <lastName>string</lastName>
        </name>
        <age>int</age>
        <married>boolean</married>
        <netWorth>double</netWorth>
        <spousesName>
          <firstName>string</firstName>
          <lastName>string</lastName>
        </spousesName>
      </person>
    </_DOCUMENT_REQUEST_ELEMENT_NAME_>
  </soap:Body>
</soap:Envelope>

------------------------------------------
Document SOAP Response over HTTP
------------------------------------------

HTTP/1.1 200 OK
Content-Type: text/xml; charset=utf-8
Content-Length: length

<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <_DOCUMENT_MESSAGE_NAME_Response xmlns="TARGET_NAMESPACE">
      <_DOCUMENT_MESSAGE_NAME_Result>
        <name>
          <firstName>string</firstName>
          <lastName>string</lastName>
        </name>
        <age>int</age>
        <married>boolean</married>
        <netWorth>double</netWorth>
        <spousesName>
          <firstName>string</firstName>
          <lastName>string</lastName>
        </spousesName>
      </_DOCUMENT_MESSAGE_NAME_Result>
    </_DOCUMENT_MESSAGE_NAME_Response>
  </soap:Body>
</soap:Envelope>

 */

public class WebServiceDefinition extends WSDLEntity
{
	private String	targetNamespace;
	private Element	typesElement;
	private Map<String, String>		namespaceMap;
	private Map<String, Message>		messages;
	private Map<String, Binding>		bindings;
	private Map<String, Service>		services;

	public WebServiceDefinition()
	{
		this.namespaceMap = new HashMap<String, String>();
		this.messages = new HashMap<String, Message>();
		this.bindings = new HashMap<String, Binding>();
		this.services = new HashMap<String, Service>();
	}

	public String getTargetNamespace()
	{
		return this.targetNamespace;
	}

	public void setTargetNamespace(final String namespace)
	{
		this.targetNamespace = namespace;
	}

	public void addNamespace(final String abbr, final String namespace)
	{
		this.namespaceMap.put(abbr, namespace);
	}

	public String getNamespace(final String abbr)
	{
		return this.namespaceMap.get(abbr);
	}

	public Element getTypesElement()
	{
		return this.typesElement;
	}

	public void setTypesElement(final Element typesElement)
	{
		this.typesElement = typesElement;
	}

	public void addMessage(final Message message)
	{
		this.messages.put(message.getName(), message);
	}

	public Message getMessage(String name)
	{
		final int index = name.indexOf(":");
		if (index > -1)
			name = name.substring(index + 1);

		return this.messages.get(name);
	}

	public Iterator<Message> messages()
	{
		return this.messages.values().iterator();
	}

	public void addBinding(final Binding binding)
	{
		this.bindings.put(binding.getName(), binding);
	}

	public Binding getBinding(String name)
	{
		if (name == null && this.bindings.size() > 0)
			return this.bindings.values().iterator().next();

		final int index = name.indexOf(":");
		if (index > -1)
			name = name.substring(index + 1);

		return this.bindings.get(name);
	}

	public Iterator<Binding> bindings()
	{
		return this.bindings.values().iterator();
	}

	public void addService(final Service service)
	{
		this.services.put(service.getName(), service);
	}

	public Service getService(String name)
	{
		final int index = name.indexOf(":");
		if (index > -1)
			name = name.substring(index + 1);

		return this.services.get(name);
	}

	public Iterator<Service> services()
	{
		return this.services.values().iterator();
	}

}
