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

import com.arahant.beans.Employee;
import com.arahant.beans.Person;
import com.arahant.utils.StandardProperty;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import com.arahant.utils.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.kissweb.database.Connection;
import org.kissweb.database.Cursor;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.kissweb.database.Record;

import java.util.ArrayList;
import java.util.Date;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrHrParentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class HRParentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(HRParentOps.class);

	public HRParentOps() {}

	@WebMethod()
	public SearchPersonsReturn searchPersons(/*@WebParam(name = "in")*/final SearchPersonsInput in) {
		final SearchPersonsReturn ret = new SearchPersonsReturn();
		logger.debug("Entering search employees ");
		try {
			checkLogin(in);


			if (true) {

                /////////////////////////////////////////   New
                /*
                    I have not done the dependent search yet because of its complexity and the fact that my only client doesn't use that.
                    It just isn't worth the time right now.  Shouldn't be too bad when needed.
                 */
                Connection db = KissConnection.get();
                boolean needAnd = false;
                ArrayList<Object> args = new ArrayList<>();
                int today = DateUtils.today();
                args.add(today);

                String select =  "select p.person_id, p.lname, p.fname, p.mname, p.ssn, p.job_title, es.name status_name, " +
						"(case when esh.effective_date <= ? then es.active else 'N' end) active, esh.effective_date, " +
						" pos.position_name, p.i9_part1 " +
                        "from employee e " +
                        "join person p " +
                        "  on e.person_id = p.person_id " +

						"left join current_employee_status esh " +
						"  on p.person_id = esh.employee_id " +

						"left join current_employee_wage w " +
						"  on p.person_id = w.employee_id " +

						"left join hr_position pos " +
						"  on w.position_id = pos.position_id " +

                        "join hr_employee_status es " +
                        "  on esh.status_id = es.status_id ";

                if (in.getActiveIndicator() == 1) {
                    select += " where es.active='Y' and esh.effective_date <= ? ";
					args.add(DateUtils.today());
                    needAnd = true;
                } else if (in.getActiveIndicator() == 2) {
                    select += " where (es.active='N' or esh.effective_date > ?) ";
					args.add(DateUtils.today());
                    needAnd = true;
                }
                if ("employee".equals(in.getWorkerType())) {
					if (!needAnd) {
						select += " where ";
						needAnd = true;
					} else
						select += " and ";
					select += " e.employment_type = 'E'";
				} else if ("contractor".equals(in.getWorkerType())) {
					if (!needAnd) {
						select += " where ";
						needAnd = true;
					} else
						select += " and ";
					select += " e.employment_type = 'C'";
				}
                if (!in.getFirstName().isEmpty()) {
                    if (!needAnd) {
                        select += " where ";
                        needAnd = true;
                    } else
                        select += " and ";
                    switch (in.getFirstNameSearchType()) {
                        case 2:  // starts with
                            if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                                select += "lower(fname) like lower(?)";
                            else
                                select += "fname like ?";
                            args.add(in.getFirstName() + '%');
                            break;
                        case 3:  // ends with
                            if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                                select += "lower(fname) like lower(?)";
                            else
                                select += "fname like ?";
                            args.add('%' + in.getFirstName());
                            break;
                        case 4:  // contains
                            if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                                select += "lower(fname) like lower(?)";
                            else
                                select += "fname like ?";
                            args.add('%' + in.getFirstName() + '%');
                            break;
                        case 5:  // exact match
                            if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                                select += "lower(fname) = lower(?)";
                            else
                                select += "fname = ?";
                            args.add(in.getFirstName());
                            break;
                    }
                }

                if (!in.getLastName().isEmpty()) {
                    if (!needAnd) {
                        select += " where ";
                        needAnd = true;
                    } else
                        select += " and ";
                    switch (in.getLastNameSearchType()) {
                        case 2:  // starts with
                            if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                                select += "lower(lname) like lower(?)";
                            else
                                select += "lname like ?";
                            args.add(in.getLastName() + '%');
                            break;
                        case 3:  // ends with
                            if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                                select += "lower(lname) like lower(?)";
                            else
                                select += "lname like ?";
                            args.add('%' + in.getLastName());
                            break;
                        case 4:  // contains
                            if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                                select += "lower(lname) like lower(?)";
                            else
                                select += "lname like ?";
                            args.add('%' + in.getLastName() + '%');
                            break;
                        case 5:  // exact match
                            if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                                select += "lower(lname) = lower(?)";
                            else
                                select += "lname = ?";
                            args.add(in.getLastName());
                            break;
                    }
                }

                if (!in.getSsn().isEmpty()) {
                    if (!needAnd) {
                        select += " where ";
                        needAnd = true;
                    } else
                        select += " and ";
                    select += "ssn = ?";
                    args.add(Person.encryptSsn(in.getSsn()));
                }
                if (!in.getEmployeeStatusId().isEmpty()) {
                    if (!needAnd) {
                        select += " where ";
                        needAnd = true;
                    } else
                        select += " and ";
                    select += "esh.status_id = ?";
                    args.add(in.getEmployeeStatusId());
                }

                select += " order by p.lname, p.fname, p.mname, p.person_id ";

                Cursor c = db.newCommand().query(select, args.toArray());
                ret.setEmployees(db, c, in.getAssigned(), in.getAssignedFrom(), in.getAssignedTo(), ret.getCap(),
						in.getSearchType(), in.getFirstPerson(), in.getLastPerson(), in.getLabels());
                c.close();
            } else {
                //final boolean includeUser=BRight.checkRight(SEE_SELF_IN_SEARCHES)==ACCESS_LEVEL_WRITE;
                ret.setEmployees(BPerson.searchPersons(in.getSearchMetaInput(), in.getSearchEmployees(), in.getEmployeeStatusId(), in.getSsn(), in.getFirstName(), in.getLastName(), ret.getCap(), in.getActiveIndicator()));
            }
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		logger.debug("exiting search employees");
		return ret;
	}

	private static void promoteApplicantToEmployee(Connection db, String personId) throws Exception {
		Record arec = db.fetchOne("select * from applicant_application where person_id = ? order by application_date desc" , personId);
		if (arec == null)
			throw new ArahantException("No applicant application found");  // should never happen
		Float wageAmount = arec.getFloat("pay_rate");
		String positionId = arec.getString("position_id");
		arec.set("phase", 4);  //  hired
		BApplication.updateApplicationStatusId(arec, (short) 4);
		arec.update();

		Record aprec = db.fetchOne("select * from applicant where person_id = ?", personId);
		if (aprec != null) {
			aprec.set("applicant_status_id", BProperty.getIDWithCheck(StandardProperty.APPLICANT_STATUS_HIRED));
			aprec.update();
		}

		Record erec = db.fetchOne("select * from employee where person_id =?", personId);
		if (erec == null) {
			erec = db.newRecord("employee");
			erec.set("person_id", personId);
			erec.addRecord();
		} else {
			//erec.update();
		}

		Record shrec = db.newRecord("hr_empl_status_history");
		IDGenerator.generate(shrec, "status_hist_id");
		shrec.set("employee_id", personId);
		shrec.set("effective_date", DateUtils.today());
		String statusId = BProperty.getIDWithCheck(StandardProperty.NEW_EMPLOYEE_STATUS);
		shrec.set("status_id", statusId);
		shrec.setDateTime("record_change_date", new Date());
		shrec.addRecord();

		Record wrec = db.newRecord("hr_wage");
		IDGenerator.generate(wrec, "wage_id");
		wrec.set("employee_id", personId);
		String wageTypeId = BProperty.getIDWithCheck(StandardProperty.NEW_EMPLOYEE_WAGE_TYPE);
        wrec.set("wage_type_id", wageTypeId);
		wrec.set("wage_amount", wageAmount == null? 0.0f : wageAmount);
		wrec.set("effective_date", DateUtils.today());
		wrec.set("position_id", positionId);
		wrec.addRecord();

		BChangeLog.personLog(personId, "Promoted applicant to employee manually");
	}

	@WebMethod()
	public NewEmployeeReturn newEmployee(/*@WebParam(name = "in")*/final NewEmployeeInput in) {
		final NewEmployeeReturn ret = new NewEmployeeReturn();

		try {
			checkLogin(in);
			final BEmployee be;

			String extRef = in.getExtRef();
			if (extRef != null  &&  !extRef.isEmpty()) {
				Employee e = hsu.createCriteria(Employee.class).eq(Employee.EXTREF, extRef).first();
				if (e != null)
					throw new ArahantWarning("Employee ID " + extRef + " is already in use by another employee (" + e.getNameFL() + ')');
			}

			if (!isEmpty(in.getApplicantId())) {
				promoteApplicantToEmployee(hsu.getKissConnection(), in.getApplicantId());
				ret.setPersonId(in.getApplicantId());
				finishService(ret);
				return ret;
				/*
				ret.setPersonId(new BPerson(in.getApplicantId()).makeEmployee(in.getEeoCategoryId(), in.getEeoRaceId(), 'U', in.getExtRef(), true, in.getEmployeeStatusId()));
				be = new BEmployee(ret.getPersonId());
				in.makeEmployee(be);
				be.setStatusId(in.getEmployeeStatusId(), in.getEmployeeStatusDate());
				//hsu.doSQLQuery("delete from applicant where person_id='"+in.getApplicantId()+"'");
				 */
			} else if (!isEmpty(in.getDependentId())) {
				ret.setPersonId(new BPerson(in.getDependentId()).makeEmployee(in.getEeoCategoryId(), in.getEeoRaceId(), 'U', in.getExtRef(), true, in.getEmployeeStatusId()));
				be = new BEmployee(ret.getPersonId());
				in.makeEmployee(be);
				be.setStatusId(in.getEmployeeStatusId(), in.getEmployeeStatusDate());
			} else {
				be = new BEmployee(hsu);
				be.create();
				in.makeEmployee(be);
				be.insert();
				be.setStatusId(in.getEmployeeStatusId(), in.getEmployeeStatusDate());
				hsu.flush();
				if (in.getWorkerType().equals("E"))
					BEmployee.applyLabels(be.getPersonId());

				ret.setPersonId(be.getPersonId());
			}

			be.setWageAndPosition(in.getPositionId(), in.getWageTypeId(), in.getWageAmount(), in.getEmployeeStatusDate());
			if (!isEmpty(in.getOrgGroupId()))
				be.assignToSingleOrgGroup(in.getOrgGroupId(), false);
			hsu.flush();
			//ArahantSession.getAI().watchAll();

			ArahantSession.AIEval("(assert (InsertedNewEmployee \"" + be.getPersonId() + "\" ))");


			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}


		return ret;
	}

	@WebMethod()
	public ListEEOCategoriesReturn listEEOCategories(/*@WebParam(name = "in")*/final ListEEOCategoriesInput in) {
		final ListEEOCategoriesReturn ret = new ListEEOCategoriesReturn();
		try {
			checkLogin(in);

			ret.setItem(BHREEOCategory.list(hsu));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEEORacesReturn listEEORaces(/*@WebParam(name = "in")*/final ListEEORacesInput in) {
		final ListEEORacesReturn ret = new ListEEORacesReturn();
		try {
			checkLogin(in);

			ret.setItem(BHREEORace.list(hsu));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

		try {
			checkLogin(in);

			ret.setCheckMaxEmployees(hsu.checkMaxEmployees());
			ret.setAccessLevel(BRight.checkRight(ACCESS_HRPARENT));
			ret.setCanEditHicNumber(ArahantSession.getHSU().currentlyArahantUser());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchSecurityGroupsReturn searchSecurityGroups(/*@WebParam(name = "in")*/final SearchSecurityGroupsInput in) {
		final SearchSecurityGroupsReturn ret = new SearchSecurityGroupsReturn();

		try {
			checkLogin(in);
			ret.setSecurityDef(BSecurityGroup.searchSecurityGroups(in.getName(), ret.getHighCap()));

			if (!isEmpty(in.getSecurityGroupId()))
				ret.setSelectedItem(new SearchSecurityGroupsItem(new BSecurityGroup(in.getSecurityGroupId())));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchDependentsReturn searchDependents(/*@WebParam(name = "in")*/final SearchDependentsInput in) {
		final SearchDependentsReturn ret = new SearchDependentsReturn();
		try {
			checkLogin(in);

			ret.setItem(BHREmplDependent.searchDependents(in.getFirstName(), in.getLastName(), in.getSsn(), ret.getCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadDependentReturn loadDependent(/*@WebParam(name = "in")*/final LoadDependentInput in) {
		final LoadDependentReturn ret = new LoadDependentReturn();
		try {
			checkLogin(in);

			ret.setData(new BPerson(in.getDependentId()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEmployeeStatusesReturn listEmployeeStatuses(/*@WebParam(name = "in")*/final ListEmployeeStatusesInput in) {
		final ListEmployeeStatusesReturn ret = new ListEmployeeStatusesReturn();
		try {
			checkLogin(in);

			ret.setItem(BHREmployeeStatus.list(hsu));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadApplicantReturn loadApplicant(/*@WebParam(name = "in")*/final LoadApplicantInput in) {
		final LoadApplicantReturn ret = new LoadApplicantReturn();
		try {
			checkLogin(in);

			ret.setData(new BApplicant(in.getApplicantId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchApplicantsReturn searchApplicants(/*@WebParam(name = "in")*/final SearchApplicantsInput in) {
		final SearchApplicantsReturn ret = new SearchApplicantsReturn();
		try {
			checkLogin(in);

			ret.setItem(BApplicant.search(in.getFirstName(), in.getLastName(), in.getSsn(), ret.getCap(), false));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListPositionsReturn listPositions(/*@WebParam(name = "in")*/final ListPositionsInput in) {
		final ListPositionsReturn ret = new ListPositionsReturn();
		try {
			checkLogin(in);

			ret.setItem(BHRPosition.list(hsu));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadMetaReturn loadMeta(/*@WebParam(name = "in")*/final LoadMetaInput in) {
		final LoadMetaReturn ret = new LoadMetaReturn();
		try {
			checkLogin(in);

			ret.setNewPersonDefaultExternalId(BProperty.getInt(StandardProperty.DefaultEmployeeIDType));
			ret.setNewEmpOpenScreenGroupId(BProperty.get(StandardProperty.NewEmpOpenScreenGroupId));
			ret.setMultipleCompanySupport(ArahantSession.multipleCompanySupport);
			ret.setCanEditHicNumber(ArahantSession.getHSU().currentlyArahantUser());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchCompanyReturn searchCompany(/*@WebParam(name = "in")*/final SearchCompanyInput in) {
		final SearchCompanyReturn ret = new SearchCompanyReturn();

		try {
			checkLogin(in);

			ret.setCompanies(BCompanyBase.search(in.getName(), false, ret.getHighCap()));

			if (!isEmpty(in.getCompanyId()))
				ret.setSelectedItem(new SearchCompanyReturnItem(BCompanyBase.get(in.getCompanyId())));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in) {
		final SearchProjectCategoriesReturn ret = new SearchProjectCategoriesReturn();

		try {
			checkLogin(in);

			ret.setProjectCategories(BProjectCategory.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectStatusesReturn searchProjectStatuses(/*@WebParam(name = "in")*/final SearchProjectStatusesInput in) {
		final SearchProjectStatusesReturn ret = new SearchProjectStatusesReturn();

		try {
			checkLogin(in);

			ret.setProjectStatuses(BProjectStatus.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in) {
		final SearchProjectTypesReturn ret = new SearchProjectTypesReturn();

		try {
			checkLogin(in);

			ret.setProjectTypes(BProjectType.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectsReturn searchProjects(/*@WebParam(name = "in")*/final SearchProjectsInput in) {
		final SearchProjectsReturn ret = new SearchProjectsReturn();

		try {
			checkLogin(in);

			ret.setProjects(BProject.search(hsu, in.getSummary(), in.getCategory(), in.getStatus(), in.getType(), in.getCompanyId(), in.getProjectName(),
					0, 0, null, false, in.getUser(), null, null, ret.getHighCap(), true));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetInheritedDefaultProjectReturn getInheritedDefaultProject(/*@WebParam(name = "in")*/final GetInheritedDefaultProjectInput in) {
		final GetInheritedDefaultProjectReturn ret = new GetInheritedDefaultProjectReturn();
		try {
			checkLogin(in);

			BOrgGroup company = new BOrgGroup(hsu.getCurrentCompany());

			if (!isEmpty(company.getDefaultProjectId()))
				ret.setData(new BProject(company.getDefaultProjectId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchScreenGroupsReturn searchScreenGroups(/*@WebParam(name = "in")*/final SearchScreenGroupsInput in) {
		final SearchScreenGroupsReturn ret = new SearchScreenGroupsReturn();

		try {
			checkLogin(in);

			ret.setScreenDef(BScreenGroup.searchScreenGroups(hsu, in.getName(), in.getExtId(), in.getSearchTopLevelOnly() ? 2 : 0, "", 2, ret.getHighCap()));
			if (!isEmpty(in.getScreenGroupId()))
				ret.setSelectedItem(new SearchScreenGroupsReturnItem(new BScreenGroup(in.getScreenGroupId())));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListWageTypesReturn listWageTypes(/*@WebParam(name = "in")*/final ListWageTypesInput in) {
		final ListWageTypesReturn ret = new ListWageTypesReturn();
		try {
			checkLogin(in);

			ret.setItem(BWageType.listActiveNonDedutionsPlus(null));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitClassesReturn listBenefitClasses(/*@WebParam(name = "in")*/final ListBenefitClassesInput in) {
		final ListBenefitClassesReturn ret = new ListBenefitClassesReturn();
		try {
			checkLogin(in);

			ret.setItem(BBenefitClass.listActive());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GenerateLoginAndPasswordReturn generateLoginAndPassword(/*@WebParam(name = "in")*/final GenerateLoginAndPasswordInput in) {
		final GenerateLoginAndPasswordReturn ret = new GenerateLoginAndPasswordReturn();
		try {
			checkLogin(in);

			ret.setLogin(BEmployee.makeUserLogin(in.getFirstName(), in.getLastName()));
			ret.setPassword(BEmployee.makeUserPassword());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
