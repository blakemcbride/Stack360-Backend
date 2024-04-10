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
 *
 * Created on May 1, 2007
 */
package com.arahant.utils;

public interface SearchConstants {

	/* The following is inconsistent likely because different people used/edited it at different times with different needs.
	   It probably works as-is because it is used consistently in each particular instance.
	   It should be reconciled and corrected.
	 */

	//int SELECT_VALUE = 0;
	int ALL_VALUE = 0;
	int STARTS_WITH_VALUE = 2;
	int ENDS_WITH_VALUE = 3;
	int CONTAINS_VALUE = 4;
	int EXACT_MATCH_VALUE = 5;
	int BEFORE_VALUE = 6;
	int AFTER_VALUE = 7;
	int ON_VALUE = 8;
	int IS_SET_VALUE = 9;
	int IS_NOT_SET_VALUE = 10;
	int GREATER_THAN_VALUE = 11;
	int LESS_THAN_VALUE = 12;
	int EQUAL_TO_VALUE = 13;
	int NOT_EQUAL_TO_VALUE = 14;
}

	
