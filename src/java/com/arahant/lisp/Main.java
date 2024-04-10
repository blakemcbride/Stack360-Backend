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

import org.armedbear.lisp.*;

/**
 * Example code that interfaces between Java and Lisp.
 * This example code is unrelated to the Arahant code.
 * See LispCall.java
 *
 * @author Blake McBride (blake@mcbride.name)
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello from Java");

        ABCL.eval("(progn (princ \"Hello from Lisp!\")" +
                "         (terpri))");

        //  Execute a Lisp expression and use its result
        LispObject val = ABCL.eval("(+ 3 4 5)");
        System.out.println("Lisp code \"(+ 3 4 5)\" returned " + val.intValue());

        //  Load some Lisp files
        ABCL.eval("(load \"src/java/com/arahant/lisp/utils\")");
        ABCL.eval("(load \"src/java/com/arahant/lisp/examples\")");

        //  Execute a defined Lisp function.
        ABCL.eval("(fun1)");

        /* ABCL.eval has limited usefulness because you can't pass Java or other objects.
         * You can only pass things that have string representations.  ABCL.executeLisp
         * is much better.  You can pass Lisp and Java objects between the worlds.
         *
         * "null" is specificed as the package (of the Lisp function) since it is in
         * the default Lisp package, "CL-USER".  If it is in another package you'd have
         * to specify it.
         *
         * "fun2" is the name of Lisp function being called.  The "executeLisp" method
         * a variable number of arguments.  Each argument represents an argument to the
         * Lisp function being called.
         */
        ABCL.executeLisp(null, "fun2", System.out);

        //  Tell Lisp about the value of the instance variable System.out
        //  by calling a lisp function, "set-stdout", with the value so that
        //  Lisp can store a Lisp reference to it.
        ABCL.executeLisp(null, "set-stdout", System.out);

        ABCL.executeLisp(null, "fun3");

        //  Call Lisp, Lisp calls Java, Java returns a value, Lisp returns a value.
        //  All Lisp & Java native values are auto-translated into native data
        //  elements in both environments.
        val = ABCL.executeLisp("", "fun4", -3);
        System.out.println("fun4 returned = " + val.intValue());


        //  Pass Strings around
        val = ABCL.executeLisp("", "fun5", "Java argument 5");
        System.out.println("fun5 returned = " + val.getStringValue());

        //  This example demonstrates calling a Java constructure from Lisp
        ABCL.eval("(fun6)");

    }

    public static String cmeth1(String arg) {
        return "cmeth1 " + arg;
    }

    public int imeth1(int x)
    {
        return x + 4;
    }

}
