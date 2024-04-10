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



package com.arahant.lisp;

import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateUtil;
import com.arahant.utils.dynamicclassloader.ArahantClassLoader;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import org.armedbear.lisp.*;

/**
 * Utility functions to make it easy to interface between Java and Lisp.
 *
 * @author Blake McBride (blake@mcbride.name)
 */
public class ABCL {
    private static Interpreter interpreter;
	private static boolean invertCase = false;
	private static Function makeWebServiceArgs;
    private static Function makeRestServiceArgs;
	private static int lispRelease = 0;
	private static boolean once = true;

	/*
	      Believe it or not, the following method initializes Hibernate!  It does it in this tremendously convoluted
	      way in an effort to obfuscate and protect the system.
	 */
	public static void init() {
		interpreter = Interpreter.createInstance();
		invertCase();

        installDebuggerHook();
		eval("(let ((error-orig #'error))"
				+ "  (handler-bind ((style-warning #'(lambda (c)"
				+ "                                    (declare (ignore c))"
				+ "                                    (muffle-warning))))"
				+ "    (defun error (&rest a)"
				+ "      (let ()"
				+ "        (apply error-orig a)))))");

		if (HibernateUtil.x4) {
			ABCL.eval("(load \"" + FileSystemUtils.getSourcePath() + "data2\")");
			if (once) {
				once = false;
				try {
					Class cls = new ArahantClassLoader().loadArahantClassFromFile(true, FileSystemUtils.getSourcePath() + "data1");
					if (cls == null) {
						System.err.println("Error loading class");
						return;
					}
					cls.newInstance();

					cls = new ArahantClassLoader().loadArahantClassFromFile(true, FileSystemUtils.getSourcePath() + "data3");
					Class[] ca = {};
					@SuppressWarnings("unchecked")
					Method meth = cls.getMethod("setupHibernateEnvironment", ca);
					if (meth == null) {
						System.out.println("Error initializing system");
						return;
					}
					meth.invoke(cls);
				} catch (Exception e) {
					System.out.println("Error initializing system. (22)");
					System.exit(0);
				}
			}
		}

		load("com/arahant/lisp/clos-utils");
		load("com/arahant/lisp/package-lru");
		load("com/arahant/lisp/utils");
		load("com/arahant/lisp/mappings");
        makeWebServiceArgs = findLispFunction("MAPPINGS", "make-web-service-args"); // these lines are repeated in multiple places
        makeRestServiceArgs = ABCL.findLispFunction("MAPPINGS", "make-rest-service-args");
    }

	private static void invertCase() {
		interpreter.eval("(setf (readtable-case *readtable*) :invert)");  // make lisp case sensitive
		invertCase = true;
	}

	public static String fixCase(String symbol) {
		if (invertCase) {
			int ucl = 0, lcl = 0;
			char[] vec = symbol.toCharArray();
			for (int i=0 ; i < vec.length && (ucl == 0  ||  lcl == 0) ; i++)
				if (Character.isUpperCase(vec[i]))
					ucl++;
				else if (Character.isLowerCase(vec[i]))
					lcl++;
			if (ucl != 0  &&  lcl != 0  ||  ucl == 0  &&  lcl == 0)
				return symbol;
			else
				if (ucl != 0)
					return symbol.toLowerCase();
				else
					return symbol.toUpperCase();
		} else
			return symbol.toUpperCase();
	}

	public static void deletePackage(String pkg) {
        if (interpreter == null)
            return;
        try {
            interpreter.eval("(delete-package \"" +  pkg + "\")");
        } catch (Throwable t) {
        }
    }

	public static void reset() {
//		if (interpreter == null)
//			return;
//		try {
//			interpreter.eval("(delete-package \"ARAHANT-UTILS\")");
//		} catch (Throwable t) {
//		}

		if (interpreter == null)
			return;
		try {
			interpreter.eval("(delete-package \"MAPPINGS\")");
		} catch (Throwable t) {
		}
		if (interpreter == null)
			return;
		try {
			interpreter.eval("(delete-package \"UTILS\")");
		} catch (Throwable t) {
		}
		if (interpreter == null)
			return;
		try {
			interpreter.eval("(delete-package \"PACKAGE-LRU\")");
		} catch (Throwable t) {
		}
		if (interpreter == null)
			return;
		try {
			interpreter.eval("(delete-package \"CLOS-UTILS\")");
		} catch (Throwable t) {
		}
		load("com/arahant/lisp/clos-utils");
		load("com/arahant/lisp/package-lru");
		load("com/arahant/lisp/utils");
		load("com/arahant/lisp/mappings");
		makeWebServiceArgs = findLispFunction("MAPPINGS", "make-web-service-args"); // these lines are repeated in multiple places
        makeRestServiceArgs = ABCL.findLispFunction("MAPPINGS", "make-rest-service-args");
	}

	public static LispObject load(String fileName) {
		return eval("(load \"" + FileSystemUtils.getSourcePath() + fileName + "\")");
	}

	public static LispObject compileFile(String fileName) {
		return eval("(compile-file \"" + FileSystemUtils.getSourcePath() + fileName + "\")");
	}

	public static void loadPackage(String lispPackage, String fileName) throws Exception {
		try {
			eval("(package-lru:load-package \"" + lispPackage + "\" \"" + FileSystemUtils.getSourcePath() + fileName + "\")");
		} catch (Throwable t) {
			// Convert Throwable to Exception
			throw new Exception("Error loading lisp file " + fileName, t);
		}
	}

	public static void packageDone(String lispPackage) {
		if (FileSystemUtils.isUnderIDE())
			eval("(package-lru:package-done-unload \"" + lispPackage + "\")");
		else
			eval("(package-lru:package-done \"" + lispPackage + "\")");
	}

    public static void packageUnload(String lispPackage) {
	    eval("(package-lru:package-done-unload \"" + lispPackage + "\")");
    }

    public static LispObject eval(String str) {
        return interpreter.eval(str);
    }

    public static Function findLispFunction(String packageName, String funName) {
        if (packageName == null  ||  packageName.isEmpty())
            packageName = "CL-USER";
//        else
//            packageName = fixCase(packageName);
        org.armedbear.lisp.Package lispPackage = Packages.findPackage(packageName);
		if (lispPackage == null)
			throw new RuntimeException("Package " + packageName + " not found");
        Symbol symbol = lispPackage.findAccessibleSymbol(fixCase(funName));
		if (symbol == null)
			throw new RuntimeException("Symbol " + packageName + ":" + fixCase(funName) + " not found");
        Function fun = (Function) symbol.getSymbolFunction();
        return fun;
    }

    public static LispObject executeLispFunction(Function fun, Object ... args) {
        LispObject [] jargs;
        jargs = new LispObject[args.length];
        for (int i=0 ; i < args.length ; i++)
            jargs[i] = JavaObject.getInstance(args[i], true);
        return fun.execute(jargs);
    }

    public static LispObject executeLisp(String packageName, String funName, Object ... args) {
        Function fun = findLispFunction(packageName, funName);
		if (fun == null)
			return null;
        LispObject [] jargs;
        jargs = new LispObject[args.length];
        for (int i=0 ; i < args.length ; i++)
            jargs[i] = JavaObject.getInstance(args[i], true);
        return fun.execute(jargs);
    }

    public static LispObject executeLispArray(String packageName, String funName, Object [] args) {
        Function fun = findLispFunction(packageName, funName);
		if (fun == null)
			return null;
        LispObject [] jargs;
        jargs = new LispObject[args.length];
        for (int i=0 ; i < args.length ; i++)
            jargs[i] = JavaObject.getInstance(args[i], true);
        return fun.execute(jargs);
    }

	public static Function getMakeWebServiceArgs() {
		return makeWebServiceArgs;
	}

    public static Function getMakeRestServiceArgs() {
        return makeRestServiceArgs;
    }

@SuppressWarnings("unchecked")
	public static Object LispObjectToJavaObject(LispObject obj) {
		if (obj.atom())
			if (obj.characterp())
				return obj.princToString().charAt(0);
			else if (obj.stringp())
				return obj.princToString();
			else if (obj.integerp())
				return obj.intValue();
			else if (obj.realp())
				return obj.doubleValue();
			else if (obj.listp())
				return null;
			else if (obj.constantp())
				return true;
			else
				return obj.princToString();
		else if (obj.listp()) {
			LinkedList ll = new LinkedList();
			while (!obj.endp()) {
				ll.addLast(LispObjectToJavaObject(obj.car()));
				obj = obj.cdr();
			}
			return ll;
		} else if (obj.vectorp()) {
			int len = obj.length();
			Object [] vec = new Object[len];
			for (int i=0 ; i < len ; i++)
				vec[i] = LispObjectToJavaObject(obj.AREF(i));
			return vec;
		} else
			return null;
	}

	public static LispObject JavaObjectToLispObject(Object jobj) {
		if (jobj instanceof Boolean)
			return ((Boolean)jobj) ? Lisp.T : Lisp.NIL;
		else if (jobj instanceof Character)
			return LispCharacter.getInstance((Character)jobj);
		else if (jobj instanceof Short)
			return LispInteger.getInstance((Short)jobj);
		else if (jobj instanceof Integer)
			return LispInteger.getInstance((Integer)jobj);
		else if (jobj instanceof Long)
			return LispInteger.getInstance((Long)jobj);
		else if (jobj instanceof Float)
			return SingleFloat.getInstance((Float)jobj);
		else if (jobj instanceof Double)
			return DoubleFloat.getInstance((Double)jobj);
		else if (jobj instanceof String)
			return new SimpleString((String)jobj);
		else if (jobj instanceof StringBuilder)
			return new SimpleString((StringBuilder)jobj);
		else if (jobj instanceof LinkedList) {
			LispObject lobj = Lisp.NIL;
			ListIterator it = ((LinkedList) jobj).listIterator();
			while (it.hasNext())
				lobj = new Cons(JavaObjectToLispObject(it.next()), lobj);
			return lobj;
		} else if (jobj instanceof Set) {
			LispObject lobj = Lisp.NIL;
			Iterator it = ((Set) jobj).iterator();
			while (it.hasNext())
				lobj = new Cons(JavaObjectToLispObject(it.next()), lobj);
			return lobj;
		} else if (jobj instanceof Array) {
			Array a = (Array) jobj;
			int len = Array.getLength(a);
			SimpleVector vec = new SimpleVector(len);
			for (int i=0 ; i < len ; i++)
				vec.setSlotValue(i, JavaObjectToLispObject(Array.get(a, i)));
			return null;
		}
		return null;
	}

	public static void printStackTrace(Throwable e) {
		try {
			Function fun = findLispFunction("UTILS", "print-stack-trace");
			if (fun != null) {
				LispObject stackTrace = LispThread.currentThread().backtrace(0);
				if (stackTrace != null && stackTrace != Lisp.NIL) {
					System.err.println("Lisp execution error");
					fun.execute(stackTrace);
				}
			}
		} catch (Throwable t) {
		}
		e.printStackTrace();
	}

	/* disable the debugger. raise a RuntimeException instead */
	private static void installDebuggerHook() {
		Symbol.DEBUGGER_HOOK
				.setSymbolValue(new Function() {
					public LispObject execute(LispObject c, LispObject h) {
						throw translateCondition(c);}});
	}

	private static RuntimeException translateCondition(LispObject c) {
		return new RuntimeException(
				c instanceof Condition
						? String.format("%s: %s",
						c.princToString(),
						((Condition) c).getConditionReport())
						: c.princToString());
	}


	//  DO NOT REMOVE THIS METHOD
	public static int getLispRelease() {
		return lispRelease;
	}

	//  DO NOT REMOVE THIS METHOD
	public static void setLispRelease(int lispRelease) {
		ABCL.lispRelease = lispRelease;
	}

}