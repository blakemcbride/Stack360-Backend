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

package com.arahant.services.standard.billing.productivity;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.arahant.utils.KissConnection;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Cursor;
import org.kissweb.database.Record;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardBillingProductivityOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProductivityOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(ProductivityOps.class);

	public ProductivityOps() {
		super();
	}

	@WebMethod()
	public GetProductivityDataReturn getProductivityData(/*@WebParam(name = "in")*/final GetProductivityDataInput in) {
		final GetProductivityDataReturn ret = new GetProductivityDataReturn();
		try {
			checkLogin(in);
			Connection db = KissConnection.get();
			Command cmd = db.newCommand();
			ArrayList<Object> args = new ArrayList<>();

			String select = "with hours as (" +
					"select ts.person_id, p.fname, p.mname, p.lname, SUM(ts.total_hours) total_hours " +
					"from timesheet ts " +
					"join person p " +
					"  on ts.person_id = p.person_id " +
					"where billable = 'Y' ";
			if (in.getFromDate() > 0) {
				select += "and beginning_date >= ? ";
				args.add(in.getFromDate());
			}
			if (in.getToDate() > 0) {
				select += "and beginning_date <= ? ";
				args.add(in.getToDate());
			}
			select += "group by ts.person_id, p.fname, p.lname, p.mname " +
				 	  "order by ts.person_id) " +
					"" +
					"select * from hours " +
					"order by total_hours desc";
			Cursor cursor = cmd.query(select, args);
			ArrayList<GetProductivityDataReturnItem> items = new ArrayList<>();
			double total = 0;
			while (cursor.isNext()) {
				Record rec = cursor.getRecord();
				String name = rec.getString("lname") + ", " + rec.getString("fname");
				String mname = rec.getString("mname");
				if (mname != null && !mname.isEmpty())
					name += " " + mname;
				GetProductivityDataReturnItem item = new GetProductivityDataReturnItem();
				item.setPersonId(rec.getString("person_id"));
				item.setName(name);
				double hours = rec.getDouble("total_hours");
				item.setHours(hours);
				total += hours;
				items.add(item);
			}

			for (GetProductivityDataReturnItem item : items)
				item.setPercentage((float)(item.getHours() * 100.0 / total));

			ret.setItems(items.toArray(new GetProductivityDataReturnItem[0]));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetDetailReturn getDetail(/*@WebParam(name = "in")*/final GetDetailInput in) {
		final GetDetailReturn ret = new GetDetailReturn();
		try {
			checkLogin(in);
			Connection db = KissConnection.get();
			Command cmd = db.newCommand();
			ArrayList<Object> args = new ArrayList<>();

			String select = "with hours as (" +
					"select p.project_id, p.description, SUM(ts.total_hours) total_hours " +
					"from timesheet ts " +
					"join project p " +
					"  on ts.project_id = p.project_id " +
					"where ts.billable = 'Y' and person_id = ? ";
			args.add(in.getPersonId());
			if (in.getFromDate() > 0) {
				select += "and beginning_date >= ? ";
				args.add(in.getFromDate());
			}
			if (in.getToDate() > 0) {
				select += "and beginning_date <= ? ";
				args.add(in.getToDate());
			}
			select += "group by p.project_id, p.description " +
					"order by p.project_id) " +
					"" +
					"select * from hours " +
					"order by total_hours desc";
			Cursor cursor = cmd.query(select, args);
			ArrayList<GetDetailReturnItem> items = new ArrayList<>();
			double total = 0;
			while (cursor.isNext()) {
				Record rec = cursor.getRecord();
				GetDetailReturnItem item = new GetDetailReturnItem();
				item.setName(rec.getString("description"));
				double hours = rec.getDouble("total_hours");
				item.setHours(hours);
				total += hours;
				items.add(item);
			}

			for (GetDetailReturnItem item : items)
				item.setPercentage((float)(item.getHours() * 100.0 / total));

			ret.setItems(items.toArray(new GetDetailReturnItem[0]));


			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
