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


package com.arahant.services.standard.hr.workHistory;

import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.StringUtils;
import org.hibernate.ScrollableResults;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Cursor;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class WorkerHistoryItemsReturn extends TransmitReturnBase {

	private WorkerHistoryItem [] item;

	public WorkerHistoryItem[] getItem() {
		return item;
	}

	public void setItem(final WorkerHistoryItem[] item) {
		this.item = item;
	}

	void setItem(HibernateSessionUtil hsu, String employeeId, int firstDate, int lastDate) throws Exception {

		Connection db = hsu.getKissConnection();
		Command cmd = db.newCommand();
		Cursor cursor = cmd.query("Select p.project_id, p.project_name, p.reference, p.description, " +
				"ts.total_hours, ts.end_date, ts.billable, ps.shift_start " +
				"from timesheet ts " +
				"left join project_shift ps " +
				"  on ts.project_shift_id = ps.project_shift_id " +
				"left join project p " +
				" on ps.project_id = p.project_id " +
				"where person_id = ? and entry_state <> 'R' " +
				"      and ts.end_date >= ? " +
				"      and ts.end_date <= ? " +
				"order by ts.end_date desc, p.project_id", employeeId, firstDate, lastDate);

        ArrayList<WorkerHistoryItem> itemList = new ArrayList<>();
	    String last_project_id = null;
	    String last_extRef = null;
	    String last_desc = null;
	    double last_billable_hours = 0;
	    double last_nonbillable_hours = 0;
	    int last_date = 0;
	    boolean one_more = false;
	    while (cursor.isNext()) {
			Record rec = cursor.getRecord();
	        String project_id = StringUtils.centerStrip(rec.getString("project_name"));
	        String extRef = rec.getString("reference");
	        String desc = rec.getString("description");
            String shiftStart = rec.getString("shift_start");
            if (shiftStart != null  &&  !shiftStart.isEmpty())
                desc += " (" + shiftStart + ")";
	        double hours = rec.getDouble("total_hours");
	        int date = rec.getInt("end_date");
	        String billable = rec.getString("billable");
			double billableHours, nonbillableHours;
			if (billable.equals("Y")) {
				billableHours = hours;
				nonbillableHours = 0;
			} else {
				nonbillableHours = hours;
				billableHours = 0;
			}
            if (last_project_id != null  &&  Objects.equals(project_id, last_project_id)  &&  date == last_date) {
				last_billable_hours += billableHours;
				last_nonbillable_hours += nonbillableHours;
			} else {
                if (last_project_id != null && (last_billable_hours > 0.009 || last_nonbillable_hours > 0.009))
                    itemList.add(new WorkerHistoryItem(last_project_id, last_extRef, last_desc, (float) last_billable_hours, (float) last_nonbillable_hours, last_date));
                last_project_id = project_id;
                last_extRef = extRef;
                last_desc = desc;
                last_billable_hours = billableHours;
                last_nonbillable_hours = nonbillableHours;
                last_date = date;
                one_more = true;
            }
        }
        if (one_more && last_billable_hours > 0.009)
            itemList.add(new WorkerHistoryItem(last_project_id, last_extRef, last_desc, (float) last_billable_hours, (float) last_nonbillable_hours, last_date));

        item = itemList.toArray(new WorkerHistoryItem[itemList.size()]);
	}
}

	
