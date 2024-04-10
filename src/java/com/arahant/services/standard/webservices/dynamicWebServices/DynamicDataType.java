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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.webservices.dynamicWebServices;

/**
 *
 * Arahant
 */
public class DynamicDataType {

    private DynamicDataType[] myself;
    private String name;
    private String arahantString;
    private Integer arahantInteger;
    private Double arahantDouble;
    private String[] arahantStringArray;

    /**
     * @return the myself
     */
    public DynamicDataType[] getMyself() {
        return myself;
    }

    /**
     * @param myself the myself to set
     */
    public void setMyself(DynamicDataType[] myself) {
        this.myself = myself;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the arahantInteger
     */
    public Integer getArahantInteger() {
        return arahantInteger;
    }

    /**
     * @param arahantInteger the arahantInteger to set
     */
    public void setArahantInteger(Integer arahantInteger) {
        this.arahantInteger = arahantInteger;
    }

    /**
     * @return the arahantDouble
     */
    public Double getArahantDouble() {
        return arahantDouble;
    }

    /**
     * @param arahantDouble the arahantDouble to set
     */
    public void setArahantDouble(Double arahantDouble) {
        this.arahantDouble = arahantDouble;
    }

    /**
     * @return the arahantStringArray
     */
    public String[] getArahantStringArray() {
        return arahantStringArray;
    }

    /**
     * @param arahantStringArray the arahantStringArray to set
     */
    public void setArahantStringArray(String[] arahantStringArray) {
        this.arahantStringArray = arahantStringArray;
    }

    /**
     * @return the arahantString
     */
    public String getArahantString() {
        return arahantString;
    }

    /**
     * @param arahantString the arahantString to set
     */
    public void setArahantString(String arahantString) {
        this.arahantString = arahantString;
    }


}
