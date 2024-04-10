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
package com.arahant.services.standard.tutorial.tutorialKalvin;

import com.arahant.services.standard.misc.agency.SaveAgencyInput;
import com.arahant.services.standard.webservices.dynamicWebServices.DataObject;
import com.arahant.services.standard.webservices.dynamicWebServices.ExecuteInput;
import java.lang.reflect.Method;

/**
 *
 * Arahant
 */
public class PrintObjectValues {

	public static void print(Object object) {

		final Method[] mems = object.getClass().getMethods();
		for (final Method element : mems) {
			final String name = element.getName();
			if (name.startsWith("get")) {
				try {
					Object r1 = element.invoke(object, (Object[]) null);
					System.out.println("GET " + name.substring(3) + " = " + r1.toString());
				} catch (final Throwable e) {
					continue;
				}
			}

		}
	}

	public static void generateDynamicWebServiceInputDataFromFrontEnd(ExecuteInput in) {
		String fieldName = "";
		String fieldValue = "";
		// this will generate the same input as the front-end,
		//copy and past into a class in the dynamic project
		//paste into main, run debug

		//----------------- standard input
		System.out.println("DynamicWebServicesOps ws = new DynamicWebServicesOps();");
		System.out.println("DataObject dataObject = new DataObject();");
		System.out.println("ExecuteInput inputData = new ExecuteInput();");
		System.out.println("inputData.setUser(\"" + in.getUser() + "\");");
		System.out.println("inputData.setPassword(\"" + in.getPassword() + "\");");
		System.out.println("inputData.setMethodName(\"" + in.getMethodName() + "\");");
		System.out.println("inputData.setClassToBeLoaded(\"" + in.getClassToBeLoaded() + "\");");

		for (DataObject inputfields : in.getData()) {
			fieldName = inputfields.getName();
			fieldValue = inputfields.getAttrValue();
			if (fieldValue.contains("true") || fieldValue.contains("false")) {
				System.out.println("dataObject.put(\"" + fieldName + "\", " + fieldValue + ");");
			} else {
				try {
					System.out.println("dataObject.put(\"" + fieldName + "\", " + Integer.parseInt(fieldValue) + ");");
				} catch (Exception ex) {
					System.out.println("dataObject.put(\"" + fieldName + "\", \"" + fieldValue + "\");");
				}
			}

		}
		System.out.println("inputData.setData(dataObject);");
		System.out.println("ExecuteReturn output = ws.execute(inputData);");
	}

	public static void generateWebServiceInputDataFromFrontEnd(Object object) {

		System.out.println(object.getClass().getSimpleName() + " inputData = new " + object.getClass().getSimpleName() + "();");
		final Method[] mems = object.getClass().getMethods();
		for (final Method element : mems) {
			final String name = element.getName();
			if (name.startsWith("get")) {
				try {
					if (name.substring(3).equalsIgnoreCase("class")) {
						continue;
					}
					Class x = element.getReturnType();
					Object r1 = element.invoke(object, (Object[]) null);
					if (x.getSimpleName().equalsIgnoreCase("string")) {
						System.out.println("inputData.set" + name.substring(3) + "(\"" + r1.toString() + "\");");
					} else {
						System.out.println("inputData.set" + name.substring(3) + "(" + r1.toString() + ");");
					}
				} catch (final Throwable e) {
					continue;
				}
			}

		}
	}

	private static String getType(String data) {

		int pos = data.lastIndexOf(".");
		if (pos > 0) {
			return data.substring(pos + 1);
		} else {
			return data;
		}

	}
//    public static List<DWSInputModel> createSetters(Object object) {
//
//        final Method[] mems = object.getClass().getMethods();
//        List<DWSInputModel> inputs = new ArrayList<DWSInputModel>();
//        String setters = new String();
//        String vars = new String();
//        String var = "";
//        String type = "";
//        for (final Method element : mems) {
//            final String name = element.getName();
//            if (name.startsWith("set")) {
//                var=name.substring(3);
//                var=var.substring(0, 1).toLowerCase() + var.substring(1);
//                type =getType(element.getGenericParameterTypes()[0].toString());
//                setters += getType(object.getClass().getName().toLowerCase()) +"." + name + "(" + var + ")\n";
//                vars += type  + " " + var + " = new " + type + "();\n";
//                DWSInputModel dws = new DWSInputModel();
//                dws.setClazz(object.getClass().getName());
//                dws.setDatatype(type);
//                dws.setMethod(var);
//                inputs.add(dws);
//                dws=null;
//            }
//        }
//        System.out.println(vars);
//        System.out.println("\n");
//        System.out.println(setters);
//        return inputs;
//
////        final Field[] fields = object.getClass().getDeclaredFields();
////        for (Field f : fields){
////            System.out.println("Field " + f.getName() + " type " + f.getType().getName());
////        }
//
//    }

	public static void main(String[] args) {
		PrintObjectValues po = new PrintObjectValues();
		//Person p = new Person();
		//BApplication app = new BApplication();
		SaveAgencyInput x = new SaveAgencyInput();
		po.generateWebServiceInputDataFromFrontEnd(x);

		// po.createSetters(app);
	}
}
