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
import com.arahant.business.BProperty;

public class SaveFillInTemplateInputItem {

    public SaveFillInTemplateInputItem()
    {

    }

    public SaveFillInTemplateInputItem(BAssemblyTemplate bat) {

    }

    public void build(String parentId, String finalLocationId)
    {
        for(int loop=0;loop<getRecord().length;loop++)
            record[loop].build(parentId, id, finalLocationId);

        if (record.length > 0 && record[0]!=null)
            for (int loop=0;loop<getChild().length;loop++)
                child[loop].build(record[0].itemId, finalLocationId);
		else
			for (int loop=0;loop<getChild().length;loop++)
                child[loop].build(null, finalLocationId);

    }

    private String id;
    private SaveFillInTemplateInputItem[] child;
    private RecordInputItem[] record;
    private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);


    public void setCap(int x) {
        cap = x;
    }

    public int getCap() {
        return cap;
    }

    public SaveFillInTemplateInputItem[] getChild() {
        if (child==null)
            child=new SaveFillInTemplateInputItem[0];
        return child;
    }

    public void setChild(SaveFillInTemplateInputItem[] child) {
        this.child = child;
    }

    public RecordInputItem[] getRecord() {
        if (record==null)
            record=new RecordInputItem[0];
        return record;
    }

    public void setRecord(RecordInputItem[] record) {
        this.record = record;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


