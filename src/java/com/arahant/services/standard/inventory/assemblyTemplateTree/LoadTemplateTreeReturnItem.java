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


/**
 * 
 */
package com.arahant.services.standard.inventory.assemblyTemplateTree;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BAssemblyTemplate;
import com.arahant.business.BAssemblyTemplateDetail;
import com.arahant.business.BProperty;

public class LoadTemplateTreeReturnItem {

    private LoadTemplateTreeReturnItem(BAssemblyTemplateDetail bat) {
        setData(bat);
    }

    public LoadTemplateTreeReturnItem()
    {
        
    }

    public LoadTemplateTreeReturnItem(BAssemblyTemplate bat) {

    }

    public void setData(BAssemblyTemplate bt)
    {
        id = bt.getId();
        name = bt.getName();
        type = 1;
        
        BAssemblyTemplateDetail[] bat = bt.listChildren();
        child = new LoadTemplateTreeReturnItem[bat.length];
        for (int loop = 0; loop < child.length; loop++)
            child[loop] = new LoadTemplateTreeReturnItem(bat[loop]);

        childCount=getChild().length;

        reqQuantity = 0;
        location = "";
        lot = "";

    }

    public void setData(BAssemblyTemplateDetail btd)
    {
        id = btd.getProductId();
        name = btd.getProductDescription();
        type = 2;

        BAssemblyTemplateDetail[] bat = btd.listChildren();
        child = new LoadTemplateTreeReturnItem[bat.length];
        for (int loop = 0; loop < child.length; loop++)
            child[loop] = new LoadTemplateTreeReturnItem(bat[loop]);

        childCount=getChild().length;

        reqQuantity = btd.getQuantity();
        location = "";
        lot = "";

    }

    private String id;
    private String name;
    private int childCount;
    private LoadTemplateTreeReturnItem[] child;
    private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);
    private int type = 0;
    
    private int reqQuantity;
    private String location;
    private String lot;

    public void setCap(int x) {
        cap = x;
    }

    public int getCap() {
        return cap;
    }

    public LoadTemplateTreeReturnItem[] getChild() {
        return child;
    }

    public void setChild(LoadTemplateTreeReturnItem[] child) {
        this.child = child;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public int getReqQuantity() {
        return reqQuantity;
    }

    public void setReqQuantity(int reqQuantity) {
        this.reqQuantity = reqQuantity;
    }
}

	
