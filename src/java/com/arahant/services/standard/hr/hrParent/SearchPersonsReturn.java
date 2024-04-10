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

package com.arahant.services.standard.hr.hrParent;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BPerson;
import com.arahant.business.BProperty;
import com.arahant.business.BSearchOutput;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitReturnBase;
import org.kissweb.DateUtils;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Cursor;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/*
 *
 * Created on Feb 5, 2007
 *
 */
public class SearchPersonsReturn extends TransmitReturnBase <BPerson> {

	private SearchPersonsReturnItem[] persons;
	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	private int total;
	private int groupNumber = 1;

	public int getCap() {
		return cap;
	}

	/**
	 * @param cap The cap to set.
	 */
	public void setCap(final int cap) {
		this.cap = cap;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	/**
	 * @return Returns the employees.
	 */
	public SearchPersonsReturnItem[] getPersons() {
		return persons;
	}

	/**
	 * @param employees The employees to set.
	 */
	public void setPersons(final SearchPersonsReturnItem[] employees) {
		this.persons = employees;
	}

	void setEmployees(final BSearchOutput<BPerson> e) throws ArahantException {
		persons = new SearchPersonsReturnItem[e.getItems().length];
		for (int loop = 0; loop < persons.length; loop++)
			persons[loop] = new SearchPersonsReturnItem(e.getItems()[loop]);

		setStandard(e);
	}

	private enum AssignedType {
		EITHER,
		ASSIGNED,
		UNASSIGNED
	}

	/**
	 * 	Only return the number of records we're supposed to.  However, we still have to read
	 * 	all of the records in order to get the total count.
	 *
	 * 	This ignores weather the project is active or not.
	 *
	 * @param db the database
	 * @param c  the list of employees
	 * @param assigned "either", "assigned", or "unassigned"
	 * @param assignedFrom date or zero
	 * @param assignedTo date or zero
	 * @param cap max number of records to display
	 * @param searchType 0=search, 1=previous group, 2=next group (previous group has not been implemented)
	 * @param firstPerson person_id of person first shown
	 * @param lastPerson person_id of person last shown
	 * @throws ArahantException
	 * @throws SQLException
	 */
    void setEmployees(Connection db, Cursor c, String assigned, int assignedFrom, int assignedTo, int cap,
					  int searchType, String firstPerson, String lastPerson, String [] labels) throws Exception {
	    List<SearchPersonsReturnItem> lst = new ArrayList<>();
	    int today = DateUtils.today();
	    Command lblcmd = db.newCommand();

	    AssignedType assignedType;
	    switch (assigned) {
			default:
			case "either":
				assignedType = AssignedType.EITHER;
				break;
			case "assigned":
				assignedType = AssignedType.ASSIGNED;
				break;
			case "unassigned":
				assignedType = AssignedType.UNASSIGNED;
				break;
		}

	    int nDisplayed = 0;
	    int numberSkipped = 0;
		if (searchType == 1 && (firstPerson == null || firstPerson.isEmpty()))
			searchType = 0;
		if (searchType == 2 && (lastPerson == null || lastPerson.isEmpty()))
			searchType = 0;
		boolean inCorrectPage = searchType == 0;
	    total = 0;
	    while (c.isNext()) {
	    	Record rec = c.getRecord();
			String personId = rec.getString("person_id");
			List<Record> ap;
			if (assignedType == AssignedType.EITHER)
				ap = db.fetchAll("select project_employee_join_id, p.description " +
								"from project_employee_join pej " +
								"join project_shift ps " +
								"  on pej.project_shift_id = ps.project_shift_id " +
								"join project p " +
								"  on ps.project_id = p.project_id " +
								"where pej.person_id = ? " +
								"    and p.estimated_last_date >= ?",
						personId, today);
			else
				ap = db.fetchAll("select project_employee_join_id, p.description " +
								"from project_employee_join pej " +
								"join project_shift ps " +
								"  on pej.project_shift_id = ps.project_shift_id " +
								"join project p " +
								"  on ps.project_id = p.project_id " +
								"where pej.person_id = ? " +
								"    and (p.estimated_first_date <= ? and p.estimated_last_date >= ?" +
								"    or p.estimated_first_date <= ? and p.estimated_last_date >= ? " +
								"    or p.estimated_first_date > ? and p.estimated_last_date < ?)",
						personId, assignedFrom, assignedFrom, assignedTo, assignedTo, assignedFrom, assignedTo);

	    	if (assignedFrom > 20100101  &&  assignedTo > 20100101 && assignedType != AssignedType.EITHER) {
				if (assignedType == AssignedType.ASSIGNED) {
					if (ap.isEmpty())
						continue;
				} else /* if (assignedType == AssignedType.UNASSIGNED) */ {
					if (!ap.isEmpty())
						continue;
				}
			}

	    	// Label search
	    	if (labels != null  &&  labels.length > 0) {
	    		StringBuilder select = new StringBuilder("select employee_label_id " +
						"from employee_label_association " +
						"where employee_id = ? and completed = 'N' and (");
	    		boolean needOr = false;
	    		for (String lblid : labels) {
	    			if (needOr)
	    				select.append(" or ");
	    			else
	    				needOr = true;
					select.append(" employee_label_id = '");
					select.append(lblid);
					select.append("' ");
				}
	    		select.append(')');
	    		List<Record> lblrecs = lblcmd.fetchAll(select.toString(), personId);
	    		if (lblrecs.size() != labels.length)
	    			continue;
			}

	    	total++;
			if (nDisplayed < cap  &&  inCorrectPage) {
				Command cmd = db.newCommand();
				nDisplayed++;
				lst.add(new SearchPersonsReturnItem(rec, ap, cmd));
				cmd.close();
			} else
				numberSkipped++;
			if (searchType == 2 && !inCorrectPage && personId.equals(lastPerson)) {
				inCorrectPage = true;
				groupNumber = 1 + numberSkipped / cap;
			}
		}
	    persons = new SearchPersonsReturnItem[lst.size()];
        persons = lst.toArray(persons);
    }

}

	
