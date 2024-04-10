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

import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import java.io.File;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

/**
 *
 * Arahant
 */

public class Test extends ServiceBase {

    private static final transient ArahantLogger logger = new ArahantLogger(DynamicWebServicesOps.class);

    public Test() {
        super();
    }

    @WebMethod
    public ExecuteReturn execute(/*@WebParam(name = "in")*/ExecuteInput in) {

        final ExecuteReturn ret = new ExecuteReturn();

        // The following is all good code, I just commented out for my tunnel testing//

        try {
            checkLogin(in);
            ArahantClassLoader acl = new ArahantClassLoader();
            Class<?> opsClass = acl.loadArahantClass(false, "", in.getClassToBeLoaded());
            Object opsInstance = opsClass.newInstance();
            IArahantDynamicWebService concreteClass = (IArahantDynamicWebService) opsInstance;
            DataObject outputMap = concreteClass.run(in.getData(), in.getMethodName());
            ret.setData(outputMap);
            //return ret;
            finishService(ret);
        } catch (ArahantWarning ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } catch (InstantiationException ex) {
            handleError(hsu, ex, ret, logger);
        } catch (IllegalAccessException ex) {
            handleError(hsu, ex, ret, logger);
        } catch (ClassNotFoundException ex) {
            handleError(hsu, ex, ret, logger);
        } catch (final Throwable ex) {
            handleError(hsu, ex, ret, logger);
        }

        return ret;
    }

    public static void currentDir() {
        File dir1 = new File(".");
        File dir2 = new File("..");
        try {
            System.out.println("Current dir : " + dir1.getCanonicalPath());
            System.out.println("Parent  dir : " + dir2.getCanonicalPath());
            String path = new java.io.File(".").getCanonicalPath();
            System.out.println("getCanonicalPath " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayReturnDataObjectArray( DataObject[] dos){
        for (DataObject dao : dos){
            System.out.println("OrgID " + dao.getName() + " -->" + dao.getAttrValue());
        }
    }
    private static void displayReturnTwoDimensionalArray( DataObject[] dos){
        for (DataObject dao : dos){
            System.out.println("OrgInfo: " + dao.getName() + " -->" + dao.getAttrValue());
        }
    }

    public static void main(String[] args) {

        try {
            DynamicWebServicesOps ws = new DynamicWebServicesOps();
            DataObject inputMap = new DataObject();
            //String methodName = "getOrgGroupListAsDataObjectArray";
            //String methodName = "getOrgGroupListAsTwoDimenionalArray";
            String methodName = "getOrgGroupListAsDataObjectArray";

            String classNameToBeLoaded = "com.arahant.services.standard.webservices.dynamicWebServices.EmployeeTest";
            ExecuteInput inputData = new ExecuteInput();
            inputData.setData(inputMap);
            inputData.setMethodName(methodName);
            inputData.setClassToBeLoaded(classNameToBeLoaded);
            inputData.setUser("arahant");
            inputData.setPassword("kkk");
            ExecuteReturn output = ws.execute(inputData);
            DataObject[] x = output.getData().getDataVals();
            displayReturnDataObjectArray(x);
            System.out.println("DataObject[] size ->" + x.length);
            displayReturnDataObjectArray(output.getData().get("personx").getDataVals());
            System.out.println("=====");
             displayReturnDataObjectArray(output.getData().get("persony").getDataVals());

        } catch (Exception e) {
            e.printStackTrace();
        }
        //ws.showMap(output.getOutputMap());


//            ArahantClassLoader acl = new ArahantClassLoader();
//        try {
//            acl.loadArahantClass(false, classNameToBeLoaded);
//            acl.encryptClass(classNameToBeLoaded);
//        } catch (ClassNotFoundException classNotFoundException) {
//        }
    }
}
