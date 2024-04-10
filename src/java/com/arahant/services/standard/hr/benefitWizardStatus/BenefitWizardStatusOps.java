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


/**
 *
 */
package com.arahant.services.standard.hr.benefitWizardStatus;

import com.arahant.beans.*;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.reports.BenefitWizardStatusReport;
import com.arahant.reports.WizardReviewReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrBenefitWizardStatusOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class BenefitWizardStatusOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(BenefitWizardStatusOps.class);

	public BenefitWizardStatusOps() {
		super();
	}

	@WebMethod()
	public SearchEmployeeWizardStatusesReturn searchEmployeeWizardStatuses(/*
			 * @WebParam(name = "in")
			 */final SearchEmployeeWizardStatusesInput in) {

		final SearchEmployeeWizardStatusesReturn ret = new SearchEmployeeWizardStatusesReturn();

		try {
			checkLogin(in);
			//Get all the employees that match search criteria
			List<Employee> empList = BEmployee.searchEmployeesByWizardStatus(in.getFirstName(), in.getLastName(), in.getFromDate(), in.getToDate(), in.getWizardStatus(), ret.getCap());

			///////////////////////////////////////////////////////////////////////////////////////////////
			//get all their benefit joins
			List<HrBenefitJoin> empJoins = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON, empList).ne(HrBenefitJoin.EMPLOYEE_COVERED, 'N').list();

			//put emp joins in a hashmap so we can use them later
			HashMap<String, List<HrBenefitJoin>> empJoinsMap = new HashMap<String, List<HrBenefitJoin>>();
			for (HrBenefitJoin join : empJoins)
				if (empJoinsMap.containsKey(join.getPayingPersonId())) {
					List<HrBenefitJoin> tempJoins = empJoinsMap.get(join.getPayingPersonId());
					tempJoins.add(join);
					empJoinsMap.put(join.getPayingPersonId(), tempJoins);
				} else {
					List<HrBenefitJoin> tempJoins = new ArrayList<HrBenefitJoin>();
					tempJoins.add(join);
					empJoinsMap.put(join.getPayingPersonId(), tempJoins);
				}

			/////////////////////////////////////////////////////////////////////////////////////////////////
			//get emp change requests
			List<PersonCR> personChangeRequests = hsu.createCriteria(PersonCR.class).in(PersonCR.PERSON, empList).eq(PersonCR.CHANGE_STATUS, PersonCR.STATUS_PENDING).list();

			//put change requests in a hashmap so we can use them later
			HashMap<String, PersonCR> empCRMap = new HashMap<String, PersonCR>();
			for (PersonCR cr : personChangeRequests)
				empCRMap.put(cr.getRealRecord().getPersonId(), cr);

			/////////////////////////////////////////////////////////////////////////////////////////////////
			//get dep change requests
			List<Employee> changeEmps = hsu.createCriteria(Employee.class).joinTo(Employee.CHANGE_REQS).in(PersonCR.PERSON, empList).list();

			HibernateCriteriaUtil<HrEmplDependent> hcu = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.RECORD_TYPE, 'C');
			if (changeEmps == null || changeEmps.isEmpty())
				hcu.in(HrEmplDependent.EMPLOYEE, empList);
			else
				hcu.makeCriteria().or(hcu.makeCriteria().in(HrEmplDependent.EMPLOYEE, empList),
						hcu.makeCriteria().in(HrEmplDependent.EMPLOYEE, changeEmps)).add();
			List<HrEmplDependent> empDepList = hcu.list();

			List<Person> tempDepList = new ArrayList<Person>();
			HashMap<String, String> empDepMap = new HashMap<String, String>();
			for (HrEmplDependent d : empDepList) {
				tempDepList.add(d.getPerson());
				empDepMap.put(d.getDependentId(), d.getEmployee().getPersonId());
			}

			List<PersonCR> empDepCRList = hsu.createCriteria(PersonCR.class).in(PersonCR.PERSON_PENDING, tempDepList).eq(PersonCR.CHANGE_STATUS, PersonCR.STATUS_PENDING).list();

			//put change requests in a hashmap so we can use them later
			HashMap<String, List<PersonCR>> empDepCRMap = new HashMap<String, List<PersonCR>>();
			for (PersonCR dep : empDepCRList)
				if (empDepCRMap.containsKey(empDepMap.get(dep.getChangeRecord().getPersonId()))) {
					List<PersonCR> tempJoins = empDepCRMap.get(empDepMap.get(dep.getChangeRecord().getPersonId()));
					tempJoins.add(dep);
					empDepCRMap.put(empDepMap.get(dep.getChangeRecord().getPersonId()), tempJoins);
				} else {
					List<PersonCR> tempJoins = new ArrayList<PersonCR>();
					tempJoins.add(dep);
					empDepCRMap.put(empDepMap.get(dep.getChangeRecord().getPersonId()), tempJoins);
				}

			ret.setItem(BEmployee.makeArray(empList), empJoinsMap, empCRMap, empDepCRMap);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*
			 * @WebParam(name = "in")
			 */final GetReportInput in) {
		final GetReportReturn ret = new GetReportReturn();
		try {
			checkLogin(in);

			ret.setReportUrl(new BenefitWizardStatusReport().build(in.getFirstName(), in.getLastName(), in.getFromDate(), in.getToDate(), in.getWizardStatus()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetPendingChangesReportReturn getPendingChangesReport(/*
			 * @WebParam(name = "in")
			 */final GetPendingChangesReportInput in) {
		final GetPendingChangesReportReturn ret = new GetPendingChangesReportReturn();
		try {
			checkLogin(in);

			ret.setReportUrl(new WizardReviewReport().build(in.getEmployeeId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ApproveChangesReturn approveChanges(/*
			 * @WebParam(name = "in")
			 */final ApproveChangesInput in) {
		final ApproveChangesReturn ret = new ApproveChangesReturn();

		try {
			checkLogin(in);

			for (String empId : in.getIds())
				BEmployee.approveChanges(empId, in.getApproveDemographics(), in.getApproveDependents(), in.getApproveBenefits());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
