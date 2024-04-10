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

package com.arahant.groovy

import com.arahant.groovy.GroovyJavaTest


public class GroovyTest {

	public static void main(Integer arg) {
		System.out.println("Hello from groovy!");
		GroovyJavaTest.fun();
		def ins = new Xyz2()
		
		ins.y = arg
		
		println 'Groovy version ' + GroovySystem.getVersion()
		
		println "$ins.y"
	}
}


class Xyz2 {
	def x
	private y
}
