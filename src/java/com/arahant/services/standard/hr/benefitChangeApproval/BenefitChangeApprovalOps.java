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
package com.arahant.services.standard.hr.benefitChangeApproval;

import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrBenefitChangeApprovalOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class BenefitChangeApprovalOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(BenefitChangeApprovalOps.class);

	public BenefitChangeApprovalOps() {
	}

	@WebMethod()
	public SearchChangeRequestsReturn SearchChangeRequests(/*@WebParam(name = "in")*/final SearchChangeRequestsInput in) {
		final SearchChangeRequestsReturn ret = new SearchChangeRequestsReturn();
		try {
			checkLogin(in);

			ret.setItem(searchChangeRequests(in.getLastName(), in.getFirstName(), in.getFromDate(), in.getToDate(), ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ApproveChangeRequestsReturn approveChangeRequests(/*@WebParam(name = "in")*/final ApproveChangeRequestsInput in) {
		final ApproveChangeRequestsReturn ret = new ApproveChangeRequestsReturn();
		try {
			checkLogin(in);

			BPerson bp = new BPerson();
			BProject bpp;

			if (in.getApprove())
				for (String st : in.getIds()) {
					bpp = new BProject(st);
					bp.loadPending(bpp.getBean().getDoneForPersonId());
					if (bp.getRecordType() != 'C')
						throw new ArahantException("Person record is Real.  Change record required.");
					bp.apply();
					
					for (BWizardProject bwp : bpp.getWizardProjects(true))
						bwp.approve();
				}
			else
				for (String st : in.getIds()) {
					bpp = new BProject(st);
					bp.loadPending(bpp.getBean().getDoneForPersonId());
					bp.reject();
					for (BWizardProject bwp : bpp.getWizardProjects(true))
						bwp.reject();
				}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
		
	@SuppressWarnings({"unchecked"})
	private static BProject[] searchChangeRequests(final String lname, final String fname, final int dateFrom, int dateTo, final int max) {
		List<Project> prjlst;
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		HibernateCriteriaUtil hcu = hsu.createCriteria(Project.class).eq(Project.REQUESTING_ORG_GROUP, hsu.getCurrentCompany()).setMaxResults(max);

		hcu.dateBetween(Project.DATEREPORTED, dateFrom, dateTo);
		
		hcu.orderBy(Project.DATEREPORTED).orderBy(Project.PROJECTID);
		
		hcu.joinTo(Project.DONE_FOR_PERSON).like(Person.LNAME, lname).like(Person.FNAME, fname);

//		hcu.joinTo(Project.CHANGE_REQUEST).joinTo(PersonCR.PERSON).like(Person.LNAME, lname).like(Person.FNAME, fname);

		prjlst = hcu.list();

//		java.util.Collections.sort(prjlst, new Comparator<Project>() {
//
//			@Override
//			public int compare(Project arg0, Project arg1) {
//				PersonCR cr1 = arg0.getPersonChangeRequest().iterator().next();
//				PersonCR cr2 = arg1.getPersonChangeRequest().iterator().next();
//
//				if (cr1.getChangeStatus() == cr2.getChangeStatus())
//					return 0;
//
//				if (cr1.getChangeStatus() == PersonCR.STATUS_PENDING)
//					return -1;
//				if (cr2.getChangeStatus() == PersonCR.STATUS_PENDING)
//					return 1;
//
//				if (cr1.getChangeStatus() == PersonCR.STATUS_REJECTED)
//					return -1;
//				if (cr2.getChangeStatus() == PersonCR.STATUS_REJECTED)
//					return 1;
//
//				if (cr1.getChangeStatus() == PersonCR.STATUS_APPROVED)
//					return -1;
//				if (cr2.getChangeStatus() == PersonCR.STATUS_APPROVED)
//					return 1;
//
//				return 0;
//			}
//		});
		
		return BProject.makeArray(prjlst);
	}

}
