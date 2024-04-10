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

package com.arahant.rest;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;
import com.arahant.servlets.REST;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.HibernateSessionUtil;
import org.json.JSONObject;
import com.arahant.services.standard.webservices.dynamicWebServices.ArahantClassLoader;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static com.arahant.servlets.REST.ExecutionReturn;

/**
 * Author: Blake McBride
 * Date: 5/5/18
 */
public class CompiledJavaService extends RestBase {

    private static final transient ArahantLogger logger = new ArahantLogger(CompiledJavaService.class);

    public ExecutionReturn tryCompiledJava(REST ms, HttpServletResponse response, String _package, String _className, String _method, JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu) {
        Class cls = null;
        String dynamicClassPath = BProperty.get(StandardProperty.DynamicClassPath);
        if (!isEmpty(dynamicClassPath)) {
            if (dynamicClassPath.charAt(dynamicClassPath.length() - 1) != '/')
                dynamicClassPath += "/";
            String classPath = dynamicClassPath + _package.replace(".", "/") + "/";
            try {
                cls = new ArahantClassLoader().loadArahantClass(false, classPath, _className);
            } catch (Throwable e) {
                // ignore
            }
        }
        if (cls == null)
            try {
                cls = Class.forName(_package + "." + _className);
            } catch (ClassNotFoundException classNotFoundException) {
                // ignore
            }
        if (cls != null) {
            Class[] ca = {
                    JSONObject.class,
                    JSONObject.class,
                    HibernateSessionUtil.class,
                    REST.class
            };

            Method methp;
            try {
                @SuppressWarnings("unchecked")
                Method tmethp = cls.getMethod(_method, ca);
                methp = tmethp;  // had to use tmethp to be able to use @SuppressWarnings
            } catch (Exception e) {
                ms.errorReturn(response, "Java method " + _method + " not found in class " + this.getClass().getName(), e);
                return ExecutionReturn.Error;
            }
            try {
                methp.invoke(null, injson, outjson, hsu, this);
            } catch (Exception e) {
                ms.errorReturn(response, "Error executing Java method " + _method + " not found in class " + this.getClass().getName(), e);
                return ExecutionReturn.Error;
            }
        }
        return ExecutionReturn.NotFound;
    }

}
