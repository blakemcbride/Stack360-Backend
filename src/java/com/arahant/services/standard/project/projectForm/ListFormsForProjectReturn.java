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


package com.arahant.services.standard.project.projectForm;

import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ListFormsForProjectReturn extends TransmitReturnBase {

	private boolean canExport = false;
	private ListFormsForProjectReturnItem item[];

	/**
	 * @return Returns the item.
	 */
	public ListFormsForProjectReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListFormsForProjectReturnItem[] item) {
		this.item = item;
	}

	void setItem(final String projectId, final String shiftId, int rights) {

		Connection db = new Connection(ArahantSession.getHSU().getConnection());
		try {
			List<Record> recs;
			if (shiftId != null && !shiftId.isEmpty()) {
				if (rights == 2)
					recs = db.fetchAll("select pf.project_form_id, ft.form_code, pf.form_date, pf.comments, ft.description, " +
							"                  pf.file_name_extension, pf.internal " +
							"from project_form pf " +
							"join form_type ft " +
							"  on pf.form_type_id = ft.form_type_id " +
							"where pf.project_shift_id=? " +
							"order by pf.form_date", shiftId);
				else
					recs = db.fetchAll("select pf.project_form_id, ft.form_code, pf.form_date, pf.comments, ft.description, " +
							"                  pf.file_name_extension, pf.internal " +
							"from project_form pf " +
							"join form_type ft " +
							"  on pf.form_type_id = ft.form_type_id " +
							"where pf.project_shift_id=? and pf.internal='N' " +
							"order by pf.form_date", shiftId);
			} else {
				if (rights == 2)
					recs = db.fetchAll("select pf.project_form_id, ft.form_code, pf.form_date, pf.comments, ft.description, " +
							"                  pf.file_name_extension, pf.internal " +
							"from project_form pf " +
							"join project_shift ps " +
							"  on pf.project_shift_id = ps.project_shift_id " +
							"join form_type ft " +
							"  on pf.form_type_id = ft.form_type_id " +
							"where ps.project_id=? " +
							"order by pf.form_date", projectId);
				else
					recs = db.fetchAll("select pf.project_form_id, ft.form_code, pf.form_date, pf.comments, ft.description, " +
							"                  pf.file_name_extension, pf.internal " +
							"from project_form pf " +
							"join form_type ft " +
							"  on pf.form_type_id = ft.form_type_id " +
							"join project_shift ps " +
							"  on pf.project_shift_id = ps.project_shift_id " +
							"where ps.project_id=? and pf.internal='N' " +
							"order by pf.form_date", projectId);
			}
			int len = recs.size();
			item = new ListFormsForProjectReturnItem[len];
			for (int loop = 0; loop < len; loop++) {
				item[loop] = new ListFormsForProjectReturnItem(recs.get(loop));
			}
		} catch (Exception throwables) {
			throw new ArahantException(throwables);
		}
	}

	public boolean getCanExport() {
		return canExport;
	}

	public void setCanExport(boolean canExport) {
		this.canExport = canExport;
	}
}

	
