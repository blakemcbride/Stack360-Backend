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




package com.arahant.services.standard.dynamicwebservices;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Blake McBride
 */
public class WsDataObject {

	private String name;
	private	String	type;
	private int rank = 0;
	private String shape;
	private String value;
	private String longValue;
	private List<WsDataObject> objectList;
	private List<String> stringList;

	public WsDataObject() {
		//  required for WS layer
	}

	@XmlAttribute(name = "name")  // not required for lists
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "type", required = true)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute(name = "rank", required = true)
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@XmlAttribute(name = "shape")
	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	@XmlAttribute(name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

        // @XmlValue - makes it a nameless value
	@XmlElement
	public String getLongValue() {
		return longValue;
	}

	public void setLongValue(String longValue) {
		this.longValue = longValue;
	}

	@XmlElement
	public List<WsDataObject> getObjectList() {
		if (objectList == null)
			objectList = new ArrayList<WsDataObject>();
		return objectList;
	}

	//  No setter!  Client side doesn't generate one.

	@XmlElement
	public List<String> getStringList() {
		if (stringList == null)
			stringList = new ArrayList<String>();
		return stringList;
	}

	//  No setter!  Client side doesn't generate one.

}
