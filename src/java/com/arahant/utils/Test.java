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

package com.arahant.utils;

import com.arahant.lisp.ABCL;
import org.kissweb.NumberFormat;

/**
 * The purpose of this class is to demonstrate how to run a Java program
 * in the context of Arahant through Ant.
 *
 * However, while you can use all the Arahant utilities (jars), Hibernate and the Arahant
 * system have not been initialized.
 *
 * See the "test" ant target.
 */
public class Test {

    public static void main2(String [] args) {
        System.out.println("Hello 3");
        System.out.println(NumberFormat.Format(2+4.141, "", 0, 3));
    }

    /*
         Sample that includes hibernate.
     */
    public static void main(String [] args) {
        ABCL.init();  //  this code initializes the hibernate system
        // ...
        System.exit(0);  // This is necessary
    }
}
