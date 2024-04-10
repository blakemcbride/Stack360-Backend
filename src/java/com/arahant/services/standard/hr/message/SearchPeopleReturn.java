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
 *  Created on Feb 8, 2007
 */

package com.arahant.services.standard.hr.message;

import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnBase;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.List;

public class SearchPeopleReturn extends TransmitReturnBase {

    private FoundPeople[] people;

    public SearchPeopleReturn() {
        super();
    }

    /**
     * @return Returns the logins.
     */
    public FoundPeople[] getPeople() {
        return people;
    }

    /**
     * @param logins The logins to set.
     */
    public void setPeople(final FoundPeople[] logins) {
        this.people = logins;
    }

    void setPeople(final List<Record> recs) throws SQLException {
        people = new FoundPeople[recs.size()];
        int loop = 0;
        for (Record rec : recs) {
            people[loop++] = new FoundPeople(new BPerson(rec.getString("person_id")));
        }
    }
}

	
