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

import com.arahant.beans.OrgGroup;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Arahant
 */
public class EmployeeTest implements IArahantDynamicWebService {

    public EmployeeTest() {
        super();
    }

    @Override
    public DataObject run(DataObject inputMap, String methodName) throws ArahantWarning {
        DataObject outputMap = new DataObject();

        if (methodName.equalsIgnoreCase("getEmployeeName")) {
            getEmployeeName(inputMap, outputMap);
        } else if (methodName.equalsIgnoreCase("getOrgGroupListAsDataObjectArray")) {
            getOrgGroupListAsDataObjectArray(outputMap);
        } else if (methodName.equalsIgnoreCase("getOrgGroupListAsTwoDimenionalArray")) {
            getOrgGroupListAsDataObjectArray(outputMap);
        } else if (methodName.equalsIgnoreCase("getPerson")) {
            getPerson(outputMap);
        } else {
            throw new ArahantWarning("Method " + methodName + " does not exist.");
        }

        return outputMap;
    }

    private void getPerson(DataObject outputMap) {
        DataObject person = new DataObject();
        person.put("fname", "John");
        person.put("lname", "Doe");
        person.put("ssn", "123-45-6789");
        outputMap.put("personx", person);
        person = new DataObject();
        person.put("fname", "jane");
        person.put("lname", "Doe");
        person.put("ssn", "333-45-6789");
        outputMap.put("persony", person);

    }

    private void getEmployeeName(DataObject inputMap, DataObject outputMap) {
        outputMap.put("key", "Kalvin Khetsavanh employee ");
    }

    private void getOrgGroupListAsDataObjectArray(DataObject outputMap) {

        //this example uses the DataObject[] as the return object
        //normally, this should be a value pair but this example uses value,value
        //you can use this as a return list, just make sure you understand that they are both value,value
        //This gets a list of Org Groups and pass back the Org Name and Id

        List ogs = ArahantSession.getHSU().createCriteria(OrgGroup.class).list();
        if (ogs.size() > 0) {
            System.out.println("Org size " + ogs.size());
            DataObject[] daoArray = new DataObject[ogs.size()];
            for (int i = 0; i < ogs.size(); i++) {
                OrgGroup og = (OrgGroup) ogs.get(i);
                DataObject dao = new DataObject();
                dao.setName(og.getName());
                dao.setAttrValue(og.getOrgGroupId());
                daoArray[i] = dao;
            }
            outputMap.setDataVals(daoArray);
            outputMap.setName("OrgGroups");

        }
    }

    private void getOrgGroupListAsTwoDimenionalArray(DataObject outputMap) {
        List ogs = ArahantSession.getHSU().createCriteria(OrgGroup.class).list();

        if (ogs.size() > 0) {
            System.out.println("Org size " + ogs.size());
            String[][] orgArray = new String[ogs.size()][2];
            for (int i = 0; i < ogs.size(); i++) {
                OrgGroup og = (OrgGroup) ogs.get(i);
                orgArray[i][0] = og.getName();
                orgArray[i][1] = og.getOrgGroupId();
            }
            outputMap.put("org_groups", orgArray);
            outputMap.put("myinput", "hello");
        }
    }
}
