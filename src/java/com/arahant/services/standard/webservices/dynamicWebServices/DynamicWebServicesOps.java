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
 * 
 * 
 * I am pretty sure this class isn't used anymore.
 * See: com.arahant.services.standard.dynamicwebservices.DynamicWebServiceOps
 * 
 */
package com.arahant.services.standard.webservices.dynamicWebServices;

import com.arahant.exceptions.ArahantException;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardWebservicesDynamicWebServicesOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class DynamicWebServicesOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(DynamicWebServicesOps.class);
	private String dynamicPath = "";
	private boolean encrypted = true;
	private String VALIDATION_CLASS = ".Validations";
	private String GETVALIDATIONFOR = "getValidationFor";

	public DynamicWebServicesOps() {
		super();
	}

	private void getDevelopmentConfigProps() {
		InputStream is = null;
		try {
			Properties props = new Properties();
			is = this.getClass().getClassLoader().getResourceAsStream("devConfig.properties");
			if (is == null) {
				return; //did not find file, production or wrong file name
			}
			props.load(is);
			dynamicPath = props.getProperty("dynamic.path");
			if (props.getProperty("dev.mode").equals("TRISFMOTP")) {
				encrypted = false;
			}
			is.close();
		} catch (IOException ex) {
			Logger.getLogger(DynamicWebServicesOps.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception e) {
			System.out.println("");
		}

	}

	private void validateService(DataObject in, Object validationInstance, String methodName) throws ArahantException {
		try {
			//methodname is like saveDemographics
			//need to convert it to getValidationForSaveDemographics
			//because this is the validation method for saveDemographics
			methodName = GETVALIDATIONFOR + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
			Method meth = validationInstance.getClass().getMethod("validate", new Class[]{String.class, DataObject.class});
			meth.invoke(validationInstance, new Object[]{methodName, in});
		} catch (Exception ex) {
			//System.out.println("ERROR " + ex.getCause().getMessage());
			//System.out.println("ERROR localized" + ex.getLocalizedMessage());
			//System.out.println("ERROR toString" + ex.toString());
			throw new ArahantException("DynamicWebServicesOps.validateService error: " + ex.getCause().getMessage());
		}
	}

	private Object getValidationInstance(String className) {
		try {
			//all web service package must contain Validations class
			int pos = className.lastIndexOf(".");
			String validationClassString = className.substring(0, pos) + VALIDATION_CLASS;
			ArahantClassLoader validationLoader = new ArahantClassLoader();
			Class<?> validationClass = validationLoader.loadArahantClass(encrypted, dynamicPath, validationClassString);
			Object opsInstance = validationClass.newInstance();
			return opsInstance;
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(DynamicWebServicesOps.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception ex) {
			Logger.getLogger(DynamicWebServicesOps.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@WebMethod
	public ExecuteReturn execute(/*@WebParam(name = "in")*/ExecuteInput in) {
		ExecuteReturn ret = new ExecuteReturn();
		try {
			checkLogin(in);
			getDevelopmentConfigProps();
			ArahantClassLoader acl = new ArahantClassLoader();
			Class<?> opsClass = acl.loadArahantClass(encrypted, dynamicPath, in.getClassToBeLoaded());
			Object opsInstance = opsClass.newInstance();
			Object validationInstance = getValidationInstance(in.getClassToBeLoaded());

			//validate the class fields
			validateService(in.getData(), validationInstance, in.getMethodName());

			Method meth = opsInstance.getClass().getMethod(in.getMethodName(), new Class[]{DataObject.class});
			DataObject outputMap = (DataObject) meth.invoke(opsInstance, new Object[]{in.getData()});
			//do I need to return validations for this call?
			if (in.getSendValidations()) {
				meth = validationInstance.getClass().getMethod("getAllValidations", new Class[]{DataObject.class});
				DataObject validations = (DataObject) meth.invoke(validationInstance, new Object[]{in.getData()});
				ret.setValidations(validations);
			}
			ret.setData(outputMap);
			finishService(ret);
		} catch (Exception ex) {
			Throwable t = ex.getCause();
			if (t instanceof ArahantException) {
				handleError(hsu, t, ret, logger);
			} else {
				handleError(hsu, ex, ret, logger);
			}
		}

		return ret;
	}
}
