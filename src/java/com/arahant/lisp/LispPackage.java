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

import com.arahant.utils.ArahantLogger;
import com.arahant.utils.FileSystemUtils;
import org.kissweb.StringUtils;
import org.armedbear.lisp.LispObject;
import org.armedbear.lisp.Nil;

/**
 *
 * @author Blake McBride
 */
public class LispPackage {
	private String packageName;
	private String fileName;
	private static ArahantLogger logger = new ArahantLogger(LispPackage.class);

	private static boolean showError = true;

	public LispPackage(String lispPackage, String fileName) throws Exception {
		this.packageName = lispPackage;
		this.fileName = fileName;
		ABCL.loadPackage(packageName, this.fileName);
	}

	public LispPackage(String lispReference) throws Exception {
		if(!StringUtils.isEmpty(lispReference)) {
			String[] lispSplit = lispReference.split(":");
			if(lispSplit.length == 3) {
				this.packageName = lispSplit[1];
				this.fileName = lispSplit[0];
			}
			else {
				this.packageName = lispSplit[0];
				this.fileName = lispSplit[0];
			}
			ABCL.loadPackage(packageName, fileName);
		}
	}

	public void load() throws Exception {
		ABCL.loadPackage(packageName, fileName);
	}

	public void packageDone() {
		ABCL.packageDone(packageName);
	}

    public void packageUnload() {
        ABCL.packageUnload(packageName);
    }

	public LispObject executeLisp(String fun, Object... args) {
		return ABCL.executeLispArray(packageName, fun, args);
	}

	private LispObject _executeLisp(String fun, Object[] args) {
		return ABCL.executeLispArray(packageName, fun, args);
	}

	public static void executeLispMethod(String lispPackage, String fileName, String lispMethod, Object... args) {
		LispPackage lp = null;
		try {
			if (FileSystemUtils.isUnderIDE())
				ABCL.reset();
			lp = new LispPackage(lispPackage, fileName);
			lp._executeLisp(lispMethod, args);

		} catch (Throwable t) {
			//  couldn't find lisp file
			if (showError) {
				logger.error(t);
			}
			showError = false;
		} finally {
			if (lp != null) {
				lp.packageDone();
			}
		}
	}

	public String executeLispReturnString(String fun, Object... args) {
		LispObject ret = ABCL.executeLispArray(packageName, fun, args);
		if (ret instanceof Nil)
			return null;
		return ret.getStringValue();
	}

	private String _executeLispReturnString(String fun, Object[] args) {
		LispObject ret = ABCL.executeLispArray(packageName, fun, args);
		if (ret instanceof Nil)
			return null;
		return ret.getStringValue();
	}

	public static String executeLispMethodReturnString(String lispPackage, String fileName, String lispMethod, Object... args) {
		LispPackage lp = null;
		String ret = null;
		try {
			if (FileSystemUtils.isUnderIDE())
				ABCL.reset();
			lp = new LispPackage(lispPackage, fileName);
			ret = lp._executeLispReturnString(lispMethod, args);

		} catch (Throwable t) {
			//  couldn't find lisp file
			if (showError) {
				logger.error(t);
			}
			showError = false;
		} finally {
			if (lp != null) {
				lp.packageDone();
			}
			return ret;
		}
	}

	public double executeLispReturnDouble(String fun, Object... args) {
		LispObject ret = ABCL.executeLispArray(packageName, fun, args);
		if (ret instanceof Nil)
			return 0.0;
		return ret.doubleValue();
	}

	private double _executeLispReturnDouble(String fun, Object[] args) {
		LispObject ret = ABCL.executeLispArray(packageName, fun, args);
		if (ret instanceof Nil)
			return 0.0;
		return ret.doubleValue();
	}

	public static double executeLispMethodReturnDouble(String lispPackage, String fileName, String lispMethod, Object... args) {
		LispPackage lp = null;
		double ret = 0.0;
		try {
			if (FileSystemUtils.isUnderIDE())
				ABCL.reset();
			lp = new LispPackage(lispPackage, fileName);
			ret = lp._executeLispReturnDouble(lispMethod, args);

		} catch (Throwable t) {
			//  couldn't find lisp file
			if (showError) {
				logger.error(t);
			}
			showError = false;
		} finally {
			if (lp != null) {
				lp.packageDone();
			}
			return ret;
		}
	}

	public int executeLispReturnInt(String fun, Object... args) {
		LispObject ret = ABCL.executeLispArray(packageName, fun, args);
		if (ret instanceof Nil)
			return 0;
		return ret.intValue();
	}

	private int _executeLispReturnInt(String fun, Object[] args) {
		LispObject ret = ABCL.executeLispArray(packageName, fun, args);
		if (ret instanceof Nil)
			return 0;
		return ret.intValue();
	}

	public static int executeLispMethodReturnInt(String lispPackage, String fileName, String lispMethod, Object... args) {
		LispPackage lp = null;
		int ret = 0;
		try {
			if (FileSystemUtils.isUnderIDE())
				ABCL.reset();
			lp = new LispPackage(lispPackage, fileName);
			ret = lp._executeLispReturnInt(lispMethod, args);

		} catch (Throwable t) {
			//  couldn't find lisp file
			if (showError) {
				logger.error(t);
			}
			showError = false;
		} finally {
			if (lp != null) {
				lp.packageDone();
			}
			return ret;
		}
	}

	public boolean executeLispReturnBoolean(String fun, Object... args) {
		LispObject ret = ABCL.executeLispArray(packageName, fun, args);
		if (ret instanceof Nil)
			return false;
		return ret.getBooleanValue();
	}

	private boolean _executeLispReturnBoolean(String fun, Object [] args) {
		LispObject ret = ABCL.executeLispArray(packageName, fun, args);
		if (ret instanceof Nil)
			return false;
		return ret.getBooleanValue();
	}

	public static Boolean executeLispMethodReturnBoolean(String lispPackage, String fileName, String lispMethod, Object... args) {
		LispPackage lp = null;
		boolean ret = false;
		try {
			if (FileSystemUtils.isUnderIDE())
				ABCL.reset();
			lp = new LispPackage(lispPackage, fileName);
			ret = lp._executeLispReturnBoolean(lispMethod, args);

		} catch (Throwable t) {
			//  couldn't find lisp file
			if (showError) {
				logger.error(t);
			}
			showError = false;
		} finally {
			if (lp != null) {
				lp.packageDone();
			}
			return ret;
		}
	}
}
