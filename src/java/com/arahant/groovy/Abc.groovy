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

/**
 *
 * @author Blake McBride
 */
class Abc {
	public static void main(String [] args) {
		System.out.println("Hello from Abc")
		
		def ins = new Xyz()
		
		ins.y = 4
		
		println "$ins.y"
	}
}

class Xyz {
	def x
	private y
}

