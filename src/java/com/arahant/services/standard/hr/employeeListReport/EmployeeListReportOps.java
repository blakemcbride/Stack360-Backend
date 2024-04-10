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
package com.arahant.services.standard.hr.employeeListReport;

import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BRight;
import com.arahant.exports.EmployeeListExport;
import com.arahant.fields.EmployeeListFields;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.reports.EmployeeListReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 *
 *
 */
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrEmployeeListReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class EmployeeListReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			EmployeeListReportOps.class);

	public EmployeeListReportOps() {
		super();
	}

	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {
		final GetReportReturn ret = new GetReportReturn();
		try {
			checkLogin(in);

			if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
				Set<OrgGroup> availOrgGroups = new HashSet<OrgGroup>();
				Set<OrgGroupAssociation> oga = hsu.getCurrentPerson().getOrgGroupAssociations();

				for(OrgGroupAssociation o : oga) {
					BOrgGroup bog = new BOrgGroup(o.getOrgGroup());
					availOrgGroups.add(bog.getOrgGroup());
					availOrgGroups.addAll(bog.getAllOrgGroupsInHierarchy2());
				}

				String[] orgGroupIds = new String[availOrgGroups.size()];
				int i = 0;
				for(OrgGroup og : availOrgGroups) {
					orgGroupIds[i] = og.getOrgGroupId();
					++i;
				}

				ret.setReportUrl(new EmployeeListReport(in.getIds(), in.getLastNameStartsWithFrom(),
					in.getLastNameStartsWithTo(), in.getDobFrom(), in.getDobTo(), in.getSortType(),
					in.isSortAsc(), in.getStatusType(), orgGroupIds, in.getOrgGroupCodes(),
					in.getConfigIds()).build());
			}
			else
				ret.setReportUrl(new EmployeeListReport(in.getIds(), in.getLastNameStartsWithFrom(),
					in.getLastNameStartsWithTo(), in.getDobFrom(), in.getDobTo(), in.getSortType(),
					in.isSortAsc(), in.getStatusType(), in.getOrgGroupIds(), in.getOrgGroupCodes(), 
					in.getConfigIds()).build());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetExportReturn getExport(/*@WebParam(name = "in")*/final GetExportInput in) {
		final GetExportReturn ret = new GetExportReturn();
		try {
			checkLogin(in);

			if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
				Set<OrgGroup> availOrgGroups = new HashSet<OrgGroup>();
				Set<OrgGroupAssociation> oga = hsu.getCurrentPerson().getOrgGroupAssociations();

				for(OrgGroupAssociation o : oga) {
					BOrgGroup bog = new BOrgGroup(o.getOrgGroup());
					availOrgGroups.add(bog.getOrgGroup());
					availOrgGroups.addAll(bog.getAllOrgGroupsInHierarchy2());
				}

				String[] orgGroupIds = new String[availOrgGroups.size()];
				int i = 0;
				for(OrgGroup og : availOrgGroups) {
					orgGroupIds[i] = og.getOrgGroupId();
					++i;
				}

				ret.setCsvUrl(new EmployeeListExport(in.getIds(), in.getLastNameStartsWithFrom(),
					in.getLastNameStartsWithTo(), in.getDobFrom(), in.getDobTo(), in.getSortType(),
					in.isSortAsc(), in.getStatusType(), orgGroupIds, in.getOrgGroupCodes(),
					in.getConfigIds()).build());
			}
			else
				ret.setCsvUrl(new EmployeeListExport(in.getIds(), in.getLastNameStartsWithFrom(),
					in.getLastNameStartsWithTo(), in.getDobFrom(), in.getDobTo(), in.getSortType(),
					in.isSortAsc(), in.getStatusType(), in.getOrgGroupIds(), in.getOrgGroupCodes(), in.getConfigIds()).build());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListFieldsReturn listFields(/*@WebParam(NAME = "in")*/final ListFieldsInput in) {
		final ListFieldsReturn ret = new ListFieldsReturn();

		try {
			checkLogin(in);

			ListFieldsReturnItem[] returnItemsArray = new ListFieldsReturnItem[EmployeeListFields.displayIds.length];

			for (int loop = 0; loop < returnItemsArray.length; loop++) {
				returnItemsArray[loop] = new ListFieldsReturnItem();
				returnItemsArray[loop].setId(EmployeeListFields.displayIds[loop]);
			}

			ret.setItem(returnItemsArray);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListOrgGroupsForGroupReturn listOrgGroupsForGroup(/*@WebParam(name = "in")*/final ListOrgGroupsForGroupInput in) {
		final ListOrgGroupsForGroupReturn ret = new ListOrgGroupsForGroupReturn();
		try {
			checkLogin(in);

			if (!isEmpty(in.getOrgGroupId())) {
				ret.setItem(new BOrgGroup(in.getOrgGroupId()).getChildren(in.getExcludeIds()));
			} else if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
				Set<OrgGroupAssociation> oga = hsu.getCurrentPerson().getOrgGroupAssociations();
				Set<BOrgGroup> orgGroups = new HashSet<BOrgGroup>();

				for(OrgGroupAssociation o : oga)
					orgGroups.add(new BOrgGroup(o.getOrgGroup()));
				BOrgGroup[] bogs = new BOrgGroup[orgGroups.size()];

				ret.setItem(orgGroups.toArray(bogs));
			}
			else
				ret.setItem(BOrgGroup.listTopLevel(in.getExcludeIds()));
			
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListOrgGroupCodesReturn listOrgGroupCodes(/*@WebParam(name = "in")*/final ListOrgGroupCodesInput in) {
		final ListOrgGroupCodesReturn ret = new ListOrgGroupCodesReturn();
		try {
			checkLogin(in);

			ret.setItem(BOrgGroup.getAllCodes(in.getExcludeCodes()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitCategoriesReturn listBenefitCategories(/*@WebParam(name = "in")*/final ListBenefitCategoriesInput in) {
		final ListBenefitCategoriesReturn ret = new ListBenefitCategoriesReturn();
		try {
			checkLogin(in);

			ret.setItem(BHRBenefitCategory.list());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitsReturn listBenefits(/*@WebParam(name = "in")*/final ListBenefitsInput in) {
		final ListBenefitsReturn ret = new ListBenefitsReturn();
		try {
			checkLogin(in);

			ret.setItem(BHRBenefit.list(in.getCategoryId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListCoverageConfigurationsReturn listCoverageConfigurations(/*@WebParam(name = "in")*/final ListCoverageConfigurationsInput in) {
		final ListCoverageConfigurationsReturn ret = new ListCoverageConfigurationsReturn();
		try {
			checkLogin(in);

			BHRBenefitConfig[] arr = BHRBenefitConfig.list(in.getBenefitId(), in.getExcludeConfigIds());

			ret.setItem(arr);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
