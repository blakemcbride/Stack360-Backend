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
package com.arahant.business;

import com.arahant.beans.ProcessHistory;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.List;

public class BProcessHistory extends SimpleBusinessObjectBase<ProcessHistory> {

	static BProcessHistory[] makeArray(List<ProcessHistory> l) {
		BProcessHistory[] ret = new BProcessHistory[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProcessHistory(l.get(loop));
		return ret;
	}

	private BProcessHistory(ProcessHistory o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProcessHistory();
		return bean.generateId();
	}

	public String getId() {
		return bean.getProcessHistoryId();
	}

	public String getRunSuccess() {
		return (bean.getSuccess() == 'Y') ? "Success" : "Failure";
	}

	public String getRunTimeFormatted() {
		return DateUtils.getDateTimeFormatted(bean.getRunTime());
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProcessHistory.class, key);
	}
}
