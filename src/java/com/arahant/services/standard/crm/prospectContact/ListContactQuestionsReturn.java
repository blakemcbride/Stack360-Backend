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
package com.arahant.services.standard.crm.prospectContact;

import com.arahant.business.BContactQuestionDetail;
import com.arahant.services.TransmitReturnBase;

public class ListContactQuestionsReturn extends TransmitReturnBase {

	private ListContactQuestionsReturnItem[] item;

	public ListContactQuestionsReturnItem[] getItem() {
		return item;
	}

	public void setItem(ListContactQuestionsReturnItem[] item) {
		this.item = item;
	}

	void setItem(final BContactQuestionDetail[] cqd) {
		item = new ListContactQuestionsReturnItem[cqd.length];
		for (int loop = 0; loop < cqd.length; loop++)
			item[loop] = new ListContactQuestionsReturnItem(cqd[loop]);
	}
}
