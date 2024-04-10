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
package com.arahant.tutorial;

import com.arahant.beans.HrBenefitJoin;
import jess.Rete;

public class Jess1 {

	public Jess1() {
	}

	@SuppressWarnings("unchecked")
	public static void main(final String args[]) {
		try {
			Rete r = new Rete();

			r.defclass("HrBenefitJoin", "com.arahant.beans.HrBenefitJoin", null);
			r.eval("(set-nonvalue-class com.arahant.beans.HrBenefitJoin)");
			HrBenefitJoin bj = new HrBenefitJoin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
