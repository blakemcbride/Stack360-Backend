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



package com.arahant.services.standard.hr.hrEmployee;

import com.arahant.beans.Employee;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import com.arahant.services.main.UserCache;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrHrEmployeeOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class HrEmployeeOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(HrEmployeeOps.class);

	public HrEmployeeOps() {
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
	public SearchScreenGroupsReturn searchScreenGroups(/*@WebParam(name = "in")*/final SearchScreenGroupsInput in) {
		final SearchScreenGroupsReturn ret = new SearchScreenGroupsReturn();

		try {
			checkLogin(in);

			ret.setScreenDef(BScreenGroup.searchScreenGroups(hsu, in.getName(), in.getExtId(), in.getSearchTopLevelOnly() ? 2 : 0, "", 2, ret.getHighCap()));

			if (!isEmpty(in.getPersonId())) {
				BPerson p = new BPerson(in.getPersonId());
				if (p.getScreenGroup() != null)
					ret.setSelectedItem(new SearchScreenGroupsReturnItem(new BScreenGroup(p.getScreenGroup())));
			}
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchNoCompanyScreenGroupsReturn searchNoCompanyScreenGroups(/*@WebParam(name = "in")*/final SearchNoCompanyScreenGroupsInput in) {
		final SearchNoCompanyScreenGroupsReturn ret = new SearchNoCompanyScreenGroupsReturn();

		try {
			checkLogin(in);

			ret.setScreenDef(BScreenGroup.searchScreenGroups(hsu, in.getName(), in.getExtId(), in.getSearchTopLevelOnly() ? 2 : 0, "", 2, ret.getHighCap()));

			if (!isEmpty(in.getPersonId())) {
				BPerson p = new BPerson(in.getPersonId());
				if (p.getNoCompanyScreenGroup() != null)
					ret.setSelectedItem(new SearchNoCompanyScreenGroupsReturnItem(new BScreenGroup(p.getNoCompanyScreenGroup())));
			}
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight("AccessHrEmployee"));
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

			if (!isEmpty(in.getPersonId())) {
				BPerson p = new BPerson(in.getPersonId());
				if (!isEmpty(p.getSecurityGroupId()))
					ret.setSelectedItem(new SearchSecurityGroupsItem(new BSecurityGroup(p.getSecurityGroupId())));
			}
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

			ret.setNewPersonDefaultExternalId(BProperty.getInt("DefaultEmployeeIDType"));
			ret.setMultipleCompanySupport(ArahantSession.multipleCompanySupport);

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


			if (!isEmpty(in.getPersonId())) {
				String defaultProjectId = new BEmployee(in.getPersonId()).getExplicitProjectId();

				if (!isEmpty(defaultProjectId))
					ret.setSelectedItem(new SearchProjectsReturnItem(new BProject(defaultProjectId)));
			}
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

			String id = new BPerson(in.getPersonId()).getInheritedDefaultProjectId();
			if (!isEmpty(id))
				ret.setData(new BProject(id));

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
			BEmployee bp = new BEmployee(in.getId());
			bp.makeLoginDefaults();

			ret.setLogin(bp.getUserLogin());
			ret.setPassword(bp.getUserPassword());
			bp.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadPersonBasicReturn loadPersonBasic(/*@WebParam(name = "in")*/final LoadPersonBasicInput in) {
		final LoadPersonBasicReturn ret = new LoadPersonBasicReturn();
		try {
			checkLogin(in);

			ret.setData(new BPerson(in.getPersonId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadPersonAddressReturn loadPersonAddress(/*@WebParam(name = "in")*/final LoadPersonAddressInput in) {
		final LoadPersonAddressReturn ret = new LoadPersonAddressReturn();
		try {
			checkLogin(in);

			ret.setData(new BPerson(in.getPersonId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadPersonStatusReturn loadPersonStatus(/*@WebParam(name = "in")*/final LoadPersonStatusInput in) {
		final LoadPersonStatusReturn ret = new LoadPersonStatusReturn();
		try {
			checkLogin(in);

			ret.setData(new BPerson(in.getPersonId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadPersonMiscReturn loadPersonMisc(/*@WebParam(name = "in")*/final LoadPersonMiscInput in) {
		final LoadPersonMiscReturn ret = new LoadPersonMiscReturn();
		try {
			checkLogin(in);

			ret.setData(new BPerson(in.getPersonId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadPersonLoginReturn loadPersonLogin(/*@WebParam(name = "in")*/final LoadPersonLoginInput in) {
		final LoadPersonLoginReturn ret = new LoadPersonLoginReturn();
		try {
			checkLogin(in);

			ret.setData(new BPerson(in.getPersonId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadPersonTimeReturn loadPersonTime(/*@WebParam(name = "in")*/final LoadPersonTimeInput in) {
		final LoadPersonTimeReturn ret = new LoadPersonTimeReturn();
		try {
			checkLogin(in);

			ret.setData(new BPerson(in.getPersonId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SavePersonBasicReturn savePersonBasic(/*@WebParam(name = "in")*/final SavePersonBasicInput in) {
		final SavePersonBasicReturn ret = new SavePersonBasicReturn();
		try {
			checkLogin(in);

			String extRef = in.getExtRef();
			if (extRef != null  &&  !extRef.isEmpty()) {
				Employee e = hsu.createCriteria(Employee.class).eq(Employee.EXTREF, extRef).ne(Employee.PERSONID, in.getPersonId()).first();
				if (e != null)
					throw new ArahantWarning("Empoyee ID " + extRef + " is already in use by another employee (" + e.getNameFL() + ')');
			}

			BPerson per = new BPerson(in.getPersonId());
			if (per.isEmployee()) {
				final BEmployee be = new BEmployee(in.getPersonId());
				in.makeEmployee(be);
				be.update();
			} else {
				in.makePerson(per);
				per.update();
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SavePersonAddressReturn savePersonAddress(/*@WebParam(name = "in")*/final SavePersonAddressInput in) {
		final SavePersonAddressReturn ret = new SavePersonAddressReturn();
		try {
			checkLogin(in);

			BPerson per = new BPerson(in.getPersonId());
			if (per.isEmployee()) {
				final BEmployee be = new BEmployee(in.getPersonId());
				in.makeEmployee(be);
				be.update();
			} else {
				in.makePerson(per);
				per.update();
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SavePersonStatusReturn savePersonStatus(/*@WebParam(name = "in")*/final SavePersonStatusInput in) {
		final SavePersonStatusReturn ret = new SavePersonStatusReturn();
		try {
			checkLogin(in);

			BPerson per = new BPerson(in.getPersonId());
			if (per.isEmployee()) {
				final BEmployee be = new BEmployee(in.getPersonId());
				in.makeEmployee(be);
				be.update();
			} else {
				in.makePerson(per);
				per.update();
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SavePersonMiscReturn savePersonMisc(/*@WebParam(name = "in")*/final SavePersonMiscInput in) {
		final SavePersonMiscReturn ret = new SavePersonMiscReturn();
		try {
			checkLogin(in);

			BPerson per = new BPerson(in.getPersonId());
			if (per.isEmployee()) {
				final BEmployee be = new BEmployee(in.getPersonId());
				in.makeEmployee(be);
				be.update();
			} else {
				in.makePerson(per);
				per.update();
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SavePersonLoginReturn savePersonLogin(/*@WebParam(name = "in")*/final SavePersonLoginInput in) {
		final SavePersonLoginReturn ret = new SavePersonLoginReturn();
		try {
			checkLogin(in);

			BPerson per = new BPerson(in.getPersonId());
			if (per.isEmployee()) {
				final BEmployee be = new BEmployee(in.getPersonId());
				in.makeEmployee(be);
				be.update();
			} else {
				in.makePerson(per);
				per.update();
			}
			if (!in.isCanLogin())
				UserCache.removePerson(in.getPersonId(), null);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SavePersonTimeReturn savePersonTime(/*@WebParam(name = "in")*/final SavePersonTimeInput in) {
		final SavePersonTimeReturn ret = new SavePersonTimeReturn();
		try {
			checkLogin(in);

			BPerson per = new BPerson(in.getPersonId());
			if (per.isEmployee()) {
				final BEmployee be = new BEmployee(in.getPersonId());
				in.makeEmployee(be);
				be.update();
			} else {
				in.makePerson(per);
				per.update();
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadPersonBackgroundReturn loadPersonBackground(/*@WebParam(name = "in")*/final LoadPersonBackgroundInput in) {
		final LoadPersonBackgroundReturn ret = new LoadPersonBackgroundReturn();
		try {
			checkLogin(in);

			ret.setData(new BPerson(in.getPersonId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SavePersonBackgroundReturn savePersonBackground(/*@WebParam(name = "in")*/final SavePersonBackgroundInput in) {
		final SavePersonBackgroundReturn ret = new SavePersonBackgroundReturn();
		try {
			checkLogin(in);

			final BPerson x = new BPerson(in.getPersonId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
