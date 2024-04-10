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
 * Created on Feb 4, 2007
*/

package com.arahant.services.standard.project.projectSummary;

import com.arahant.beans.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.arahant.utils.*;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import org.kissweb.DateTime;
import org.kissweb.GoogleDistance;
import org.kissweb.TimeUtils;
import org.kissweb.database.ArrayListString;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.Date;
import java.util.List;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectProjectSummaryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProjectSummaryOps extends ServiceBase {
	
	private final static ArahantLogger logger = new ArahantLogger(ProjectSummaryOps.class);

	// The following is duplicated in SaveTimeEntry.groovy
	private static final String standardDescription = "[Computer Generated Travel Pay]";

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessProjectSummary"));
			ret.setManagingEmployeeAccessLevel(BRight.checkRight("AccessProjectManagingEmployee"));
			
			BProject bp=new BProject(in.getProjectId());
			int phase=bp.getPhaseSecurityLevel();
	
			BRouteStop brs=new BRouteStop(BRouteStop.findOrMake(bp.getRouteStopId()));
			boolean atLocation=brs.getOrgGroup()==null || hsu.createCriteria(OrgGroupAssociation.class)
					.eq(OrgGroupAssociation.PERSON,hsu.getCurrentPerson())
					.eq(OrgGroupAssociation.ORGGROUP,brs.getOrgGroup())
					.exists();
			//if I'm a client and it's in my court and it's in estimate phase, then write level
			if (phase==ProjectPhase.ESTIMATE &&((hsu.getCurrentPerson().getOrgGroupType()==EMPLOYEE_TYPE) 
					||(hsu.getCurrentPerson().getOrgGroupType()==CLIENT_TYPE && atLocation))) //estimate
				ret.setSummaryAccessLevel(ACCESS_LEVEL_WRITE);
			else	
				ret.setSummaryAccessLevel(BRight.checkRight("summaryAccessLevel"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public SearchCompanyByTypeReturn searchCompanyByType(/*@WebParam(name = "in")*/final SearchCompanyByTypeInput in)	{
		final SearchCompanyByTypeReturn ret=new SearchCompanyByTypeReturn();

		try {
			checkLogin(in);
						
			ret.setCompanies(BCompanyBase.search(in.getName(),false,ret.getCap()));
	
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

    @WebMethod()
    public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in) {
        final SearchEmployeesReturn ret = new SearchEmployeesReturn();

        try {
            checkLogin(in);

            ret.setEmployees(BEmployee.searchEmployees(hsu, in.getSsn(), in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(), null));

            if (!isEmpty(in.getProjectId())) {
                BProject bp = new BProject(in.getProjectId());
                if (bp.getManagingEmployee() != null)
                    ret.setSelectedItem(new SearchEmployeesReturnItem(bp.getManagingEmployee()));
            }
            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
	public LoadSummaryReturn loadSummary(/*@WebParam(name = "in")*/final LoadSummaryInput in)	{
		final LoadSummaryReturn ret = new LoadSummaryReturn();
				
		try {
			checkLogin(in);

			final BProject bp = new BProject(in.getProjectId());
			
			ret.setData(bp, in.getProjectShiftId());
			
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveSummaryReturn saveSummary(/*@WebParam(name = "in")*/final SaveSummaryInput in) {
		logger.debug("In saveProject");

		final SaveSummaryReturn ret = new SaveSummaryReturn();

		try {
			checkLogin(in);
			HibernateSessionUtil hsu = ArahantSession.getHSU();

			if (!isEmpty(in.getProjectName()) && hsu.createCriteria(Project.class).ne(Project.PROJECTID, in.getProjectId()).eq(Project.PROJECTNAME, in.getProjectName()).exists()) {
				throw new ArahantWarning("Project ID not unique.");
			}

			final BProject bp = new BProject(in.getProjectId());

			in.makeProject(bp, in.getProjectShiftId());

			bp.update();

			if (!isEmpty(in.getProjectName()) && hsu.createCriteria(Project.class).ne(Project.PROJECTID, bp.getProjectId()).eq(Project.PROJECTNAME, in.getProjectName()).exists())
				throw new ArahantWarning("Project ID not unique.");

			if (in.isAutoUnassign())
				unassignAll(in.getProjectId(), hsu);
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		logger.debug("return from saveProject");
		return ret;
	}

	private static String getWageTypeId(Connection db) throws Exception {
		Record wrec = db.fetchOne("select wage_type_id " +
				"from wage_type " +
				"where wage_name = 'Hourly' " +
				"      and last_active_date <= ?", org.kissweb.DateUtils.today());
		return wrec != null ? wrec.getString("wage_type_id") : null;
	}

	private static String val(String x) {
		return x == null ? "" : x;
	}

	private static boolean isEmpty2(String p) {
		return p == null || p.isEmpty();
	}
    
	/**
	 * This code is very similar to com/arahant/rest/custom/waytogo/mobileapp/Verify.groovy
	 * and com/arahant/rest/custom/waytogo/mobileapp/SaveTimeEntry.groovy
	 */
	private static void addTravelPay(Connection db, String projectId, String personId, String supervisorId, String shiftId) throws Exception {
        /*
               We know one end of the distance calculation - the location of the project.
               The other end is either the workers home, or, if they worked in the prior 7 days,
               the prior store location.
         */
        final int today = DateUtils.today();
		String wageTypeId = getWageTypeId(db);
		if (wageTypeId == null)
			return;
        final String travelTimeTypeId = BProperty.getIDWithCheck(StandardProperty.TRAVEL_PAY_TIME_TYPE);
                    
		//  Get a list of all shifts associated with this project
		final ArrayListString shifts = new ArrayListString();
		List<Record> recs = db.fetchAll("select project_shift_id from project_shift where project_id = ?", projectId);
		for (Record rec : recs)
			shifts.add(rec.getString("project_shift_id"));
        
        // What is their last day on the project?
        Record rec = db.fetchOne("select ending_date " +
                                 "from timesheet " +
                                 "where person_id = ? " +
                                 "      and time_type_id <> ? " +
                                 "      and billable = 'Y' " +
                                 "      and project_shift_id = ANY(?) " +
                                 "      and total_hours > .2 " +
                                 "order by beginning_date desc", personId, travelTimeTypeId, shifts);
        if (rec == null)
            return;  //  no hours reported
        final int lastBillableDate = rec.getInt("ending_date");
        
        
        // Were they already paid?
        rec = db.fetchOne("select ending_date " +
                                 "from timesheet " +
                                 "where person_id = ? " +
                                 "      and time_type_id = ? " +
                                 "      and total_hours > .2 " +
                                 "      and beginning_date >= ?", personId, travelTimeTypeId, lastBillableDate);
        if (rec != null)
            return;  //  already paid
        
        // Are they scheduled for a following project within 7 days
		rec = db.fetchOne("select p.project_id " +
						  "from project_employee_join pej " +
					      "join project_shift ps " +
					      "  on pej.project_shift_id = ps.project_shift_id " +
					      "join project p " +
					      "  on ps.project_id = p.project_id " +
						  "where pej.project_id = ? " +
						  "      and pej.person_id = ?" +
						  "      and (p.estimated_last_date = 0 or p.estimated_last_date > ?)" +
                          "      and p.estimated_first_date <= ? " +
                          "      and (pej.start_date = 0 or pej.start_date <= ?)",
				projectId, personId, lastBillableDate, DateUtils.add(lastBillableDate, 7), DateUtils.add(lastBillableDate, 7));
		if (rec == null) {
            // pay to home
			rec = db.fetchOne("select street, city, state, zip " +
                                 "from address " +
                                 "where person_join = ? " +
                                 "      and record_type = 'R' " +
                                 "      and address_type = 2 ", personId);
        } else {
            // pay to next project
            final String nextProjectId = rec.getString("project_id");
			rec = db.fetchOne("select ad.street, ad.city, ad.state, ad.zip " +
					"from project p " +
					"join address ad " +
					"  on p.address_id = ad.address_id " +
					"where p.project_id = ?", nextProjectId);
        }
		if (rec == null)
			return;  //  no address, can't calc distance
		String street = rec.getString("street");
		String city = rec.getString("city");
		String state = rec.getString("state");
		String zip = rec.getString("zip");
		if (isEmpty2(zip) && (isEmpty2(city) || isEmpty2(state)))
			return; // not enough of an address is available
		String startingAddress = val(street) + ", " + val(city) + ", " + val(state) + " " + val(zip);

		rec = db.fetchOne("select ad.street, ad.city, ad.state, ad.zip " +
				"from project pr " +
				"join address ad " +
				"  on pr.address_id = ad.address_id " +
				"where pr.project_id = ?", projectId);
		if (rec == null)
			return; // no project address
		street = rec.getString("street");
		city = rec.getString("city");
		state = rec.getString("state");
		zip = rec.getString("zip");
		if (isEmpty2(zip) && (isEmpty2(city) || isEmpty2(state)))
			return; // not enough of an address is available
		String endingAddress = val(street) + ", " + val(city) + ", " + val(state) + " " + val(zip);

		// Find distance between two addresses
		GoogleDistance.setAPIKey(ArahantConstants.GOOGLE_API_KEY);
		GoogleDistance gd = new GoogleDistance(startingAddress, endingAddress);
		int minutes = gd.minutes();
		if (minutes < 60)
			return; //  no value

		minutes = (int)(Math.round(minutes / 15.0) * 15.0);  //  round to quarter hour
		double hours = minutes / 60.0 - 2.0;
		if (hours <= 0.24)
			return;  //  no pay

		rec = db.newRecord("timesheet");
		IDGenerator.generate(rec, "timesheet_id");
        rec.set("person_id", personId);
        rec.set("entry_state", "N");
        rec.set("billable", "N");
        rec.set("description", standardDescription);
        rec.set("beginning_entry_person_id", supervisorId);
        rec.set("end_entry_person_id", supervisorId);
        rec.setDateTime("beginning_entry_date", new Date());
        rec.set("beginning_date", DateUtils.today());
        rec.set("beginning_time", TimeUtils.now());
        rec.setDateTime("end_entry_date", DateUtils.today());
        rec.set("end_date", DateUtils.today());
        rec.set("end_time", TimeUtils.now());
        rec.set("total_hours", hours);
        rec.set("wage_type_id", wageTypeId);
        rec.set("project_shift_id", shiftId);
        rec.set("timeTypeId", travelTimeTypeId);
		rec.addRecord();
	}

	private static void unassignAll(String projectId, HibernateSessionUtil hsu) throws Exception {
		int today = DateUtils.today();
		Connection db = hsu.getKissConnection();
		List<Record> assignments = db.fetchAll("select pej.*, ps.project_id " +
				"from project_employee_join pej " +
				"join project_shift ps " +
				"  on pej.project_shift_id = ps.project_shift_id " +
				"where ps.project_id=?", projectId);
		for (Record ass : assignments) {
			BHREmployeeEvent be = new BHREmployeeEvent();
			be.create();
			be.setSupervisorId(hsu.getCurrentPerson().getPersonId());
			be.setEmployeeId(ass.getString("person_id"));
			be.setEventDate(today);
			be.setEventType('N');
			be.setEmployeeNotified('N');
			be.setSummary("Project made inactive");
			be.setDetail("(Unassigned at - " + DateTime.currentDateTimeFormatted() + ") ");
			be.insert();

			if (BProperty.getBoolean("AutoTravelPay"))
				addTravelPay(db, ass.getString("project_id"), ass.getString("person_id"), hsu.getCurrentPerson().getPersonId(), ass.getString("project_shift_id"));

			ass.delete();
		}
	}

}
