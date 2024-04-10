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

package com.arahant.timertasks;

import com.arahant.beans.Employee;
import com.arahant.business.BEmployee;
import com.arahant.utils.DateUtils;


//@ThreadScope()
public class CheckOvertimeLogouts extends TimerTaskBase {

	@Override
	public void execute() {

		//get date and time at the beginning in case we roll into tomorrow while processing
		int date = DateUtils.now();
		int time = DateUtils.nowTime();

		for (Employee emp : hsu.createCriteria(Employee.class)
				.eq(Employee.AUTO_OVERTIME_LOGOUT, 'Y')
				.list()) {
			new BEmployee(emp).checkOvertimeLogout(date, time);
		}

	}

}
