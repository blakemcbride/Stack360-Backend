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

import com.arahant.utils.ArahantSession;
import java.util.LinkedList;

/**
 * This class provides a great testbed for lisp code running in the context of Arahant.
 *
 * @author Blake McBride
 */
public class LispCall {
	public static void main(final String args[]) {

		ABCL.init();

//		ABCL.eval("(load \"src/java/com/arahant/lisp/test\")");
//		ABCL.executeLisp("TEST", "main", ArahantSession.getConnection());

		LispPackage lp = null;
		try {
			lp = new LispPackage("TEST", "com/arahant/lisp/test");

//			LispObject mvals = lp.executeLisp("try");
//			int a = mvals.NTH(0).intValue();
//			int b = mvals.NTH(1).intValue();
//			int c = mvals.NTH(2).intValue();
//
//			int i = 4;


			LinkedList<String> ll = new LinkedList<String>();
			ll.add("00001-0000000828");
//			ll.add("00001-0000000829");
//			ll.add("00001-0000000830");


			double res = lp.executeLispReturnDouble("calc-cost", ArahantSession.getConnection(),
					"00001-0000000238",
					"00001-0000000145",
					"LBT100",
					"00001-0000000828",
					ABCL.JavaObjectToLispObject(ll),
					50000.0,
					"Monthly",
					true,
					"Monthly"
					);
			System.out.println("Return value = " + res);
//			LispObject ret = lp.executeLisp("main", ArahantSession.getConnection());
		} catch (Exception ex) {
			ex.printStackTrace();
			//  error loading file
		}
		


//		LinkedList ll = (LinkedList) ret.javaInstance();
//
//		System.out.println("instanceof LinkedList = " + (ret.javaInstance() instanceof LinkedList));
//		for (Object val : ll) {
//			if (val instanceof java.lang.String)
//				System.out.println("val = " + (String) val);
//			else if (val instanceof Integer)
//				System.out.println("val = " + (Integer) val);
//		}



//		System.out.println("atom() = " + ret.atom());
//		System.out.println("characterp() = " + ret.characterp());
//		System.out.println("constantp() = " + ret.constantp());
////		System.out.println("endp() = " + ret.endp());   // end of a list
//		System.out.println("floatp() = " + ret.floatp());
//		System.out.println("integerp() = " + ret.integerp());
//		System.out.println("listp() = " + ret.listp());
//		System.out.println("realp() = " + ret.realp());
//		System.out.println("stringp() = " + ret.stringp());
//		System.out.println("vectorp() = " + ret.vectorp());



		if (lp != null)
			lp.packageDone();

    }

	public static void myStaticMethod() {
		System.out.println("From Java myStaticMethod");
	}

	public void myInstanceMethod() {
		System.out.println("From Java myInstanceMethod");
	}


}
