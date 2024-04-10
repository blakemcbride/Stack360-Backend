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
 * All rights reserved.
 *
 * Author: Blake McBride
 * Date: 5/8/20
 */

package com.arahant.services.standard.components.employeeSearch

import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.exceptions.ArahantException
import com.arahant.servlets.REST
import com.arahant.utils.DateUtils
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Record

import java.sql.SQLException

class EmployeeSearch {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String lastName = injson.getString('lastName')
        String lastNameSearchType = injson.getString('lastNameSearchType')
        String firstName = injson.getString('firstName')
        String firstNameSearchType = injson.getString('firstNameSearchType')
        String employeeId = injson.getString('employeeId')
        String employeeStatus = injson.getString('employeeStatus')
        boolean needPhoneNumbers = injson.getBoolean('needPhoneNumbers')
        boolean onlyEmployeesWithLogin = injson.getBoolean('onlyEmployeesWithLogin')

        int lowCap = BProperty.getInt(StandardProperty.COMBO_MAX);
        int highCap = BProperty.getInt(StandardProperty.SEARCH_MAX);

        outjson.put('lowCap', lowCap)
        outjson.put('highCap', highCap)

        Connection db = KissConnection.get()
        Command cmd = db.newCommand()
        boolean needAnd = false
        ArrayList<Object> args = new ArrayList<>()

        String select =  """
                select p.person_id, e.ext_ref, p.fname, p.mname, p.lname, p.job_title, p.personal_email 
                from employee e 
                join person p 
                  on e.person_id = p.person_id 
                join current_employee_status esh
                  on e.person_id = esh.employee_id 
                join hr_employee_status es 
                  on esh.status_id = es.status_id 
                """

        if (onlyEmployeesWithLogin)
            select += """join prophet_login pl
                           on p.person_id = pl.person_id """

        if (employeeStatus == "active") {
            select += " where active='Y'"
            needAnd = true
        } else if (employeeStatus == "inactive") {
            select += " where active='N'"
            needAnd = true
        }
        if (onlyEmployeesWithLogin) {
            if (!needAnd) {
                select += " where "
                needAnd = true
            } else
                select += " and "
            select += " pl.can_login = 'Y' "
        }
        if (firstName != null  &&  !firstName.isEmpty()) {
            if (!needAnd) {
                select += " where "
                needAnd = true
            } else
                select += " and "
            switch (firstNameSearchType) {
                case '1':  // starts with
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "lower(fname) like lower(?)"
                    else
                        select += "fname like ?";
                    args.add(firstName + '%')
                    break
                case '2':  // ends with
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "lower(fname) like lower(?)";
                    else
                        select += "fname like ?";
                    args.add('%' + firstName)
                    break
                case '3':  // contains
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "lower(fname) like lower(?)";
                    else
                        select += "fname like ?"
                    args.add('%' + firstName + '%')
                    break
                case '4':  // exact match
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "lower(fname) = lower(?)"
                    else
                        select += "fname = ?"
                    args.add(firstName);
                    break
            }
        }
        if (lastName != null  &&  !lastName.isEmpty()) {
            if (!needAnd) {
                select += " where ";
                needAnd = true;
            } else
                select += " and ";

            switch (lastNameSearchType) {
                case '1':  // starts with
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "lower(lname) like lower(?)";
                    else
                        select += "lname like ?";
                    args.add(lastName + '%');
                    break
                case '2':  // ends with
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "lower(lname) like lower(?)";
                    else
                        select += "lname like ?";
                    args.add('%' + lastName);
                    break
                case '3':  // contains
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "lower(lname) like lower(?)";
                    else
                        select += "lname like ?";
                    args.add('%' + lastName + '%');
                    break
                case '4':  // exact match
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "lower(lname) = lower(?)";
                    else
                        select += "lname = ?";
                    args.add(lastName);
                    break
            }

        }
        if (employeeId != null  &&  !employeeId.isEmpty()) {
            if (!needAnd) {
                select += " where ";
                needAnd = true;
            } else
                select += " and ";
            select += "ext_ref = ?";
            args.add(employeeId);
        }

        if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
            select += " order by lower(p.lname), lower(p.fname), lower(p.mname), p.person_id ";
        else
            select += " order by p.lname, p.fname, p.mname, p.person_id ";

        try {
            if (highCap < 1)
                highCap = 70;
            List<Record> recs = db.fetchAll(highCap, select, args.toArray());
            JSONArray ary = new JSONArray()
            for (Record rec : recs) {
                JSONObject obj = new JSONObject();
                obj.put('employeeid', rec.getString('person_id'))
                obj.put('fname', rec.getString('fname'))
                obj.put('mname', rec.getString('mname'))
                obj.put('lname', rec.getString('lname'))
                obj.put('ext_ref', rec.getString('ext_ref'))
                obj.put('job_title', rec.getString('job_title'))
                obj.put('email', rec.getString('personal_email'))
                if (needPhoneNumbers) {
                    List<Record> precs = cmd.fetchAll("select phone_number, phone_type from phone where person_join = ? and (phone_type = 2 or phone_type = 3) and record_type = 'R'", rec.getString('person_id'))
                    for (Record prec : precs)
                        if (prec.getInt('phone_type') == 2)
                            obj.put('home_phone', prec.getString('phone_number'))
                        else
                            obj.put('cell_phone', prec.getString('phone_number'))
                }
                ary.put(obj)
            }
            outjson.put('employees', ary)
        } catch (SQLException e) {
            throw new ArahantException(e)
        }

    }


}