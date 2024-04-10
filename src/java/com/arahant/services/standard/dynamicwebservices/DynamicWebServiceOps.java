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

import com.arahant.annotation.DynamicValidation;
import com.arahant.business.BProperty;
import com.arahant.lisp.ABCL;
import com.arahant.lisp.LispPackage;
import com.arahant.services.ServiceBase;
import com.arahant.services.TransmitInputBase;
import com.arahant.services.main.UserCache;
import com.arahant.services.standard.webservices.dynamicWebServices.ArahantClassLoader;
import com.arahant.utils.*;
import com.arahant.utils.dynamicwebservices.DataObject;
import com.arahant.utils.dynamicwebservices.DataObjectMap;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.armedbear.lisp.Function;
import org.armedbear.lisp.LispObject;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import static com.arahant.rest.GroovyService.GroovyClassInfo;
import static com.arahant.rest.GroovyService.loadGroovyClass;

/**
 *
 * @author Blake McBride
 */
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardDynamicwebservicesOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class DynamicWebServiceOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(DynamicWebServiceOps.class);

	public DynamicWebServiceOps() {
	}

	/**
	 * Web service operation
	 */
	@WebMethod(operationName = "execute")
	public WsTopLevel execute(/*@WebParam(name = "ClientData")*/ final WsTopLevel in) {
		WsTopLevel envelope;

		try {
			DataObjectMap inDOM = new DataObjectMap(in.getObjectList());
			DataObjectMap outDOM = new DataObjectMap();

			handleRequest(inDOM, outDOM, getRemoteHost());

			envelope = new WsTopLevel();
			envelope.setObjectList(outDOM.toWsList());
		} catch (Throwable e) {
			if (e instanceof InvocationTargetException)
				e = e.getCause();
			DataObjectMap outDOM = new DataObjectMap();
			handleError(hsu, e, outDOM, logger);
			//  set error return stuff here
			envelope = new WsTopLevel();
			envelope.setObjectList(outDOM.toWsList());
			return envelope;
		}
		return envelope;
	}
	
	/**
	 * This is the main method to handle both Arahant dynamic web services and
	 * GWT socket requests
	 *
	 * @param inDOM
	 * @param outDOM
	 * @throws Throwable
	 */
	public void handleRequest(final DataObjectMap inDOM, final DataObjectMap outDOM, String hostIP) throws Throwable {
		final boolean printInDom = false;
		final boolean printOutDom = false;
		final boolean debug = false;
		final String _dontCheckLoginPassword = "asdfasdfasdfadfadfgtw445y4ih*&O*GUOUGOUlihIHIuiuiu^*#Fhvj";

		if (printInDom) {
			System.out.println("********** inDOM ************");
			System.out.println(DateUtils.getDateTimeFormatted(new Date()));
			inDOM.print();
			System.out.println("*****************************");
		}

		if (debug && FileSystemUtils.isUnderIDE())			//  Use the following for testing purposes only
			ABCL.reset();	//  causes the entire lisp system to reset and reload all the initial lisp files


		final String _package = getStringValue(inDOM, "_package");
		final String _className = getStringValue(inDOM, "_class");
		final String _method = getStringValue(inDOM, "_method");
		final boolean _sendValidations = getBooleanValue(inDOM, "_sendValidations");
		String _frontEndType = getStringValueAllowNull(inDOM, "_frontEndType");
		_frontEndType = _frontEndType == null ? "Flash" : _frontEndType;

		//dont validate login if this is the password is set and correct
		DataObject _dontCheckLogin = inDOM.get("_dontCheckLogin");
		if (_dontCheckLogin == null || !((String) _dontCheckLogin.getValue()).equals(_dontCheckLoginPassword)) {
			String _user = getStringValue(inDOM, "_user");
			String _password = getStringValue(inDOM, "_password");
			String _uuid = getStringValue(inDOM, "_uuid");
			String _contextCompanyId = getStringValue(inDOM, "_contextCompanyId");
			checkLogin(new TransmitInputBase(_user, _password, _uuid, _contextCompanyId, _sendValidations, _frontEndType, "NORMAL"));
		}

        try {
            ServiceBase.recordScreenBeingUsedDynamic(hsu, hostIP, _package);
        } catch (Exception ignored) {
        }

		if (_sendValidations)
			addValidations(outDOM, _package, debug);

		final String _fullClassPath = _package + "." + _className;
		
		boolean codeRun = false;  //  true if we found something and ran it
		
		//  Let's try Groovy first
		{
			GroovyClassInfo ci = loadGroovyClass(FileSystemUtils.getSourcePath() + _fullClassPath.replace(".", "/") + ".groovy");
			if (ci != null) {
				Class[] ca = {
					DataObjectMap.class,
					DataObjectMap.class,
					HibernateSessionUtil.class,
					DynamicWebServiceOps.class
				};

				@SuppressWarnings("unchecked")
				Method methp = ci.gclass.getMethod(_method, ca);
				if (methp == null)
					throw new Error("Method " + _method + " not found in class " + this.getClass().getName());
				methp.invoke(null, inDOM, outDOM, ArahantSession.getHSU(), this);
				codeRun = true;
			}
		}

		if (!codeRun) {  // wasn't Groovy
			// Try Lisp
			LispPackage lp = new LispPackage("PACKAGE-MAP", "com/arahant/lisp/package-map");
			String lispFileName = lp.executeLispReturnString("lisp-file-name-from-package-name", _fullClassPath);
			lp.packageDone();
			if (lispFileName != null) {

				// if Lisp
				lp = null;
				try {
					Function makeWebServiceArgs = ABCL.getMakeWebServiceArgs();
					LispObject args = ABCL.executeLispFunction(makeWebServiceArgs, inDOM, outDOM, ArahantSession.getHSU());
					LispObject lispIn = args.NTH(0);
					LispObject lispOut = args.NTH(1);
					LispObject lispHSU = args.NTH(2);

					lp = new LispPackage(_fullClassPath, lispFileName);
					lp.executeLisp(_method, lispIn, lispOut, lispHSU);
				} catch (Throwable e) {
					if (lp != null)
						lp.packageDone();
					throw e;
				}
				lp.packageDone();
			} else {

				// if Java
				Class cls = null;
				String dynamicClassPath = BProperty.get("DynamicClassPath");
				if (!isEmpty(dynamicClassPath)) {
					if (dynamicClassPath.charAt(dynamicClassPath.length() - 1) != '/')
						dynamicClassPath += "/";
					String classPath = dynamicClassPath + _package.replace(".", "/") + "/";
					cls = new ArahantClassLoader().loadArahantClass(false, classPath, _className);
				}
				if (cls == null)
					try {
						cls = Class.forName(_fullClassPath);
					} catch (ClassNotFoundException classNotFoundException) {
						throw new Error("Class " + _package + "." + _className + " not found.");
					}
				if (cls == null)
					throw new Error("Class " + _package + "." + _className + " not found.");

				Class[] ca = {
					DataObjectMap.class,
					DataObjectMap.class,
					HibernateSessionUtil.class,
					DynamicWebServiceOps.class
				};

				@SuppressWarnings("unchecked")
				Method methp = cls.getMethod(_method, ca);
				if (methp == null)
					throw new Error("Method " + _method + " not found in class " + this.getClass().getName());
				methp.invoke(null, inDOM, outDOM, ArahantSession.getHSU(), this);
			}
		}
		if (printOutDom) {
			System.out.println("********** outDOM ***********");
			outDOM.print();
			System.out.println("*****************************");
		}
		finishService(outDOM);
	}

	private static String getStringValue(DataObjectMap dom, String key) {
		DataObject val = dom.get(key);
		if (val == null)
			throw new Error("Data element \"" + key + "\" needed by backend but not provided by frontend.");
		return (String) val.getValue();
	}

	private static String getStringValueAllowNull(DataObjectMap dom, String key) {
		DataObject val = dom.get(key);
		return val == null ? null : (String) val.getValue();
	}

	private static boolean getBooleanValue(DataObjectMap dom, String key) {
		DataObject val = dom.get(key);
		if (val == null)
			throw new Error("Data element \"" + key + "\" needed by backend but not provided by frontend.");
		return (Boolean) val.getValue();
	}

	private void addValidations(DataObjectMap outDOM, String _package, boolean debug) throws ClassNotFoundException, IOException {
		DataObjectMap allValidations = new DataObjectMap();

		for (Class cls : getClasses(_package)) //get all the classes in this package, each class represents 1 web method
		{
			DataObjectMap serviceValidations = new DataObjectMap();

			for (Field field : cls.getFields()) //get each field in the class
			{
				Annotation annotation = field.getAnnotation(DynamicValidation.class); //grab the validation annnotation

				if (annotation instanceof DynamicValidation) {
					DynamicValidation validation = (DynamicValidation) annotation;

					if (debug) {
						System.out.println("Field: " + field.getName());
						System.out.println(" type: " + validation.type());
						System.out.println("  min: " + validation.min());
						System.out.println("  max: " + validation.max());
						System.out.println("  req: " + validation.required());
						System.out.println(" zero: " + validation.canbezero());
					}

					DataObjectMap input = new DataObjectMap();
					input.put("_type", validation.type()); //string, number, date, array, boolean
					input.put("_max", validation.max());
					input.put("_min", validation.min());
					input.put("_required", validation.required());
					input.put("_canbezero", validation.canbezero());

					serviceValidations.put(field.getName(), input);
				}
			}
			String className = cls.getSimpleName();
			className = className.toLowerCase().charAt(0) + className.substring(1, className.length());
			allValidations.put(className, serviceValidations);
		}

		//this is the web method these apply to
		outDOM.put("_validations", allValidations);
	}

	private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs)
			classes.addAll(findClasses(directory, packageName));
		return classes.toArray(new Class[classes.size()]);
	}

	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists())
			return classes;
		File[] files = directory.listFiles();
		for (File file : files)
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class"))
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
		return classes;
	}
}
