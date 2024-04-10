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


package com.arahant.services.standard.webservices.dynamicWebServices;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DynamicClassLoadingExample {
	 
    public static void main(String[] args) {
        try {
            ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
            /*
             * Step 2: Define a class to be loaded.
             */
            String classNameToBeLoaded = "com.arahant.kalvin.DemoClass";
 
            /*
             * Step 3: Load the class
             */
            Class<?> myClass = myClassLoader.loadClass(classNameToBeLoaded);
 
            /*
             *Step 4: create a new instance of that class
             */
            Object whatInstance = myClass.newInstance();
 
            String methodParameter = "a quick brown fox";
            /*
             * Step 5: get the method, with proper parameter signature.
             * The second parameter is the parameter type.
             * There can be multiple parameters for the method we are trying to call,
             * hence the use of array.
             */
 
            Method myMethod = myClass.getMethod("demoMethod",
                    new Class[] { String.class , Integer.class});
 
            /*
             *Step 6:
             *Calling the real method. Passing methodParameter as
             *parameter. You can pass multiple parameters based on
             *the signature of the method you are calling. Hence
             *there is an array.
             */
            String returnValue = (String) myMethod.invoke(whatInstance,
                    new Object[] { methodParameter,1 });
 
            System.out.println("The value returned from the method is:"
                    + returnValue);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
