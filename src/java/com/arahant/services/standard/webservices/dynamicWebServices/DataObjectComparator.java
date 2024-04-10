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


/**
 *
 *
 */
package com.arahant.services.standard.webservices.dynamicWebServices;

import java.util.Comparator;

public class DataObjectComparator implements Comparator<DataObjectOrderedItem> {

	@Override
	public int compare(DataObjectOrderedItem item1, DataObjectOrderedItem item2) {

		Object cost1 = item1.getItem1();
		Object cost2 = item2.getItem1();

		if (cost1 instanceof Integer) {
			if ((Integer) cost1 > (Integer) cost2) {
				return +1;
			} else if ((Integer) cost1 < (Integer) cost2) {
				return -1;
			} else {
				return 0;
			}
		} else if (cost1 instanceof String) {
			String s1 = (String) cost1;
			String s2 = (String) cost2;

			return s1.compareTo(s2);
		} else if (cost1 instanceof Double) {
			if ((Double) cost1 > (Double) cost2) {
				return +1;
			} else if ((Double) cost1 < (Double) cost2) {
				return -1;
			} else {
				return 0;
			}
		}

		return 0;
	}
}


