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

package com.arahant.services.standard.crm.prospectParent;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exports.ProspectExport;
import com.arahant.reports.ProspectReport;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.kissweb.DateUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.ArrayList;
import java.util.List;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardCrmProspectParentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProspectParentOps extends ServiceBase {

	private static final ArahantLogger logger = new ArahantLogger(ProspectParentOps.class);

	public ProspectParentOps() {
		super();
	}

	@WebMethod()
	public SearchProspectsReturn searchProspects(/* @WebParam(name = "in")*/final SearchProspectsInput in) {
		final SearchProspectsReturn ret = new SearchProspectsReturn();
		try {
			checkLogin(in);

			final Connection db = hsu.getKissConnection();
			String select;
			final ArrayList<Object> args = new ArrayList<>();

			select = "select pr.*, og.group_name, cd.last_log_date, pt.type_code, pstat.certainty, " +
					"        mp.fname, mp.mname, mp.lname, mp.person_id, ad.time_zone_offset, og.external_id," +
					"        pstat.code project_status_code, pstat.seqno project_status_seqno, psrc.source_code," +
					"        ad.street, ad.street2, ad.city, ad.state, ad.zip, ad.country_code " +
					"from prospect pr " +
					"join org_group og" +
					"  on pr.org_group_id = og.org_group_id " +
					"left join (select org_group_id, max(contact_date) last_log_date" +
					"      from prospect_log" +
					"      group by org_group_id) cd " +
					"  on pr.org_group_id = cd.org_group_id " +
					"left join (select p2.fname, p2.mname, p2.lname, p2.person_id, oga.org_group_id" +
					"           from org_group_association oga" +
					"           join person p2" +
					"             on oga.person_id = p2.person_id" +
					"           where oga.primary_indicator = 'Y') mp" +
					"  on pr.org_group_id = mp.org_group_id " +
					"left join address ad " +
					"  on pr.org_group_id = ad.org_group_join " +
					"join prospect_status pstat " +
					"  on pr.prospect_status_id = pstat.prospect_status_id " +
					"join prospect_source psrc " +
					"  on pr.prospect_source_id = psrc.prospect_source_id " +
					"join person pers " +
					"  on pr.person_id = pers.person_id " +
					"join prospect_type pt " +
					"  on pr.prospect_type_id = pt.prospect_type_id " +
					"where 1=1 ";
			if (in.getName() != null && !in.getName().isEmpty())
				switch (in.getNameSearchType()) {
					case 2:  //  starts with
						select += "and upper(og.group_name) like ? ";
						args.add(in.getName().toUpperCase() + "%");
						break;
					case 3:  //  ends with
						select += "and upper(og.group_name) like ? ";
						args.add("%" + in.getName().toUpperCase());
						break;
					case 4:  //  contains
						select += "and upper(og.group_name) like ? ";
						args.add("%" + in.getName().toUpperCase() + "%");
						break;
					case 5:  //  exact match
						select += "and upper(og.group_name) = ? ";
						args.add(in.getName().toUpperCase());
						break;
				}
			if (in.getStatusId() != null && !in.getStatusId().isEmpty()) {
				select += "and pstat.prospect_status_id = ? ";
				args.add(in.getStatusId());
			}
			if (in.getActivesOnly())
				select += "and pstat.active = 'Y' ";
			if (in.getTypeId() != null && !in.getTypeId().isEmpty()) {
				select += "and pt.prospect_type_id = ? ";
				args.add(in.getTypeId());
			}
			if (in.getSourceId() != null && !in.getSourceId().isEmpty()) {
				select += "and psrc.prospect_source_id = ? ";
				args.add(in.getSourceId());
			}
			if (in.getIdentifier() != null  &&  !in.getIdentifier().isEmpty())
				switch (in.getIdentifierSearchType()) {
					case 2:  //  starts with
						select += "and upper(og.external_id) like ? ";
						args.add(in.getIdentifier().toUpperCase() + "%");
						break;
					case 3:  //  ends with
						select += "and upper(og.external_id) like ? ";
						args.add("%" + in.getIdentifier().toUpperCase());
						break;
					case 4:  //  contains
						select += "and upper(og.external_id) like ? ";
						args.add("%" + in.getIdentifier().toUpperCase() + "%");
						break;
					case 5:  //  exact match
						select += "and upper(og.external_id) = ? ";
						args.add(in.getIdentifier().toUpperCase());
						break;
				}
			if (in.getMainContactFirstName() != null  &&  !in.getMainContactFirstName().isEmpty())
				switch (in.getMainContactFirstNameSearchType()) {
					case 2:  //  starts with
						select += "and upper(mp.fname) like ? ";
						args.add(in.getMainContactFirstName().toUpperCase() + "%");
						break;
					case 3:  //  ends with
						select += "and upper(mp.fname) like ? ";
						args.add("%" + in.getMainContactFirstName().toUpperCase());
						break;
					case 4:  //  contains
						select += "and upper(mp.fname) like ? ";
						args.add("%" + in.getMainContactFirstName().toUpperCase() + "%");
						break;
					case 5:  //  exact match
						select += "and upper(mp.fname) = ? ";
						args.add(in.getMainContactFirstName().toUpperCase());
						break;
				}
			if (in.getMainContactLastName() != null  &&  !in.getMainContactLastName().isEmpty())
				switch (in.getMainContactLastNameSearchType()) {
					case 2:  //  starts with
						select += "and upper(mp.lname) like ? ";
						args.add(in.getMainContactLastName().toUpperCase() + "%");
						break;
					case 3:  //  ends with
						select += "and upper(mp.lname) like ? ";
						args.add("%" + in.getMainContactLastName().toUpperCase());
						break;
					case 4:  //  contains
						select += "and upper(mp.lname) like ? ";
						args.add("%" + in.getMainContactLastName().toUpperCase() + "%");
						break;
					case 5:  //  exact match
						select += "and upper(mp.lname) = ? ";
						args.add(in.getMainContactLastName().toUpperCase());
						break;
				}
			if (in.getSalesPersonId() != null  &&  !in.getSalesPersonId().isEmpty()) {
				select += "and pr.person_id = ? ";
				args.add(in.getSalesPersonId());
			}
			if (in.getFirstContactDateBefore() > 0) {
				select += "and pr.first_contact_date <= ? ";
				args.add(in.getFirstContactDateBefore());
			}
			if (in.getFirstContactDateAfter() > 0) {
				select += "and pr.first_contact_date >= ? ";
				args.add(in.getFirstContactDateAfter());
			}
			if (in.getLastContactDateBefore() > 0) {
				select += "and pr.last_contact_date <= ? ";
				args.add(in.getLastContactDateBefore());
			}
			if (in.getLastContactDateAfter() > 0) {
				select += "and pr.last_contact_date >= ? ";
				args.add(in.getLastContactDateAfter());
			}
			if (in.getLastLogDateBefore() > 0) {
				select += "and cd.last_log_date <= ? ";
				args.add(in.getLastLogDateBefore());
			}
			if (in.getLastLogDateAfter() > 0) {
				select += "and cd.last_log_date >= ? ";
				args.add(in.getLastLogDateAfter());
			}
			if (in.getStatusDateBefore() > 0) {
				select += "and pr.status_change_date <= ? ";
				args.add(DateUtils.toDate(in.getStatusDateBefore()));
			}
			if (in.getStatusDateAfter() > 0) {
				select += "and pr.status_change_date >= ? ";
				args.add(DateUtils.toDate(in.getStatusDateAfter()));
			}
			if (in.getActivityDateBefore() > 0) {
				select += "and (pr.first_contact_date <= ? or pr.status_change_date <= ?" +
						"       or pr.last_contact_date <= ? or cd.last_log_date <= ? or pr.when_added <= ?) ";
				args.add(in.getActivityDateBefore());
				args.add(DateUtils.toDate(in.getActivityDateBefore()));
				args.add(in.getActivityDateBefore());
				args.add(in.getActivityDateBefore());
				args.add(DateUtils.toDate(in.getActivityDateBefore()));
			}
			if (in.getActivityDateAfter() > 0) {
				select += "and (pr.first_contact_date >= ? or pr.status_change_date >= ?" +
						"       or pr.last_contact_date >= ? or cd.last_log_date >= ? or pr.when_added >= ?) ";
				args.add(in.getActivityDateAfter());
				args.add(DateUtils.toDate(in.getActivityDateAfter()));
				args.add(in.getActivityDateAfter());
				args.add(in.getActivityDateAfter());
				args.add(DateUtils.toDate(in.getActivityDateAfter()));
			}
			if (in.getTimeZone() != 0) {
				select += "and ad.time_zone_offset = ? ";
				args.add(in.getTimeZone());
			}
			if (in.getWhenAddedDateBefore() > 0) {
				select += "and pr.when_added <= ? ";
				args.add(DateUtils.toDate(in.getWhenAddedDateBefore()));
			}
			if (in.getWhenAddedDateAfter() > 0) {
				select += "and pr.when_added >= ? ";
				args.add(DateUtils.toDate(in.getWhenAddedDateAfter()));
			}

			// now sorts
			if (in.getSort1() != null && !in.getSort1().isEmpty()) {
				select += "order by " + in.getSort1() + " ";
				if (!in.isSortAsc1())
					select += "desc ";
				if (in.getSort2() != null && !in.getSort2().isEmpty()) {
					select += ", " + in.getSort2() + " ";
					if (!in.isSortAsc2())
						select += "desc ";
				}
			} else
				select += "order by group_name";

			List<Record> recs = db.fetchAll(BProperty.getInt("Search Max", 100), select, args);

//			BSearchOutput<BProspectCompany> recs = BProspectCompany.search(in.getSearchMetaInput(), in.getIdentifier(), in.getMainContactFirstName(), in.getMainContactLastName(), in.getName(), in.getStatusId(), in.getSourceId(), in.getTypeId(), in.getExcludeId(), in.getHasPhone(), in.getHasEmail(), in.getSalesPersonId(), in.getFirstContactDateAfter(), in.getFirstContactDateBefore(), in.getLastContactDateAfter(), in.getLastContactDateBefore(), in.getLastLogDateAfter(), in.getLastLogDateBefore(), in.getStatusDateAfter(), in.getStatusDateBefore(), in.getActivesOnly(), in.getTimeZone(), BProperty.getInt(StandardProperty.SEARCH_MAX));
			ret.fillFromSearchOutput(recs);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteProspectsReturn deleteProspects(/*
			 * @WebParam(name = "in")
			 */final DeleteProspectsInput in) {
		final DeleteProspectsReturn ret = new DeleteProspectsReturn();
		try {
			checkLogin(in);

			BProspectCompany.delete(in.getIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewProspectReturn newProspect(/*
			 * @WebParam(name = "in")
			 */final NewProspectInput in) {
		final NewProspectReturn ret = new NewProspectReturn();
		try {
			checkLogin(in);

			final BProspectCompany x = new BProspectCompany();
			ret.setId(x.create());
			in.setData(x);
			//x.insert();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*
			 * @WebParam(name = "in")
			 */final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();
		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight("AccessCRM"));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProspectStatusesReturn searchProspectStatuses(/*
			 * @WebParam(name = "in")
			 */final SearchProspectStatusesInput in) {
		final SearchProspectStatusesReturn ret = new SearchProspectStatusesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProspectStatus.search(in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getStatusId())) //this is a prospect id
				ret.setSelectedItem(new SearchProspectStatusesReturnItem(new BProspectStatus(in.getStatusId())));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProspectSourcesReturn searchProspectSources(/*
			 * @WebParam(name = "in")
			 */final SearchProspectSourcesInput in) {
		final SearchProspectSourcesReturn ret = new SearchProspectSourcesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProspectSource.search(in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getId())) //this is a prospect id
				ret.setSelectedItem(new SearchProspectSourcesReturnItem(new BProspectCompany(in.getId()).getSource()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProspectTypesReturn searchProspectTypes(/*
			 * @WebParam(name = "in")
			 */final SearchProspectTypesInput in) {
		final SearchProspectTypesReturn ret = new SearchProspectTypesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProspectType.search(in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getTypeId())) //this is a prospect id
				ret.setSelectedItem(new SearchProspectTypesReturnItem(new BProspectType(new BProspectCompany(in.getTypeId()).getProspectTypeId())));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	//empty or one of name, identifier, status, firstContactDate, mainContactLastName, mainContactFirstName
//	private class ProspectSearchComparator implements Comparator<SearchProspectsReturnItem> {
//
//		private String field;
//		private boolean asc;
//
//		@Override
//		public int compare(SearchProspectsReturnItem o1, SearchProspectsReturnItem o2) {
//
//			if (!asc) {
//				SearchProspectsReturnItem t = o2;
//				o2 = o1;
//				o1 = t;
//			}
//
//			if ("identifier".equals(field))
//				return o1.getIdentifier().compareTo(o2.getIdentifier());
//
//			if ("status".equals(field))
//				return o1.getStatus().compareTo(o2.getStatus());
//
//			if ("firstContactDate".equals(field))
//				return new Integer(o1.getFirstContactDate()).compareTo(new Integer(o2.getFirstContactDate()));
//
//			if ("mainContactLastName".equals(field))
//				return o1.getMainContactLastName().compareTo(o2.getMainContactLastName());
//
//			if ("mainContactFirstName".equals(field))
//				return o1.getMainContactFirstName().compareTo(o2.getMainContactFirstName());
//
//			//must be the name
//			return o1.getName().compareTo(o2.getName());
//		}
//	}

	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*
			 * @WebParam(name = "in")
			 */final SearchEmployeesInput in) {
		final SearchEmployeesReturn ret = new SearchEmployeesReturn();
		try {
			checkLogin(in);

//			BSearchOutput<BEmployee> bSearchOutput;
//			if (hsu.currentlySuperUser() || BPerson.getCurrent().isManager())
//			{
// 				bSearchOutput = BProspectCompany.searchSalesPeople(in.getSearchMetaInput(), in.getFirstName(), in.getLastName());
//			}
//			else
//			{
//				// set output manually
//				bSearchOutput = new BSearchOutput<BEmployee>(new BEmployee[]{new BEmployee(hsu.getCurrentPerson().getPersonId())},in.getSearchMetaInput().isUsingPaging());
//
//			}
//			ret.fillFromSearchOutput(bSearchOutput);

			ret.setItem(BEmployee.searchSalesPeople(in.getFirstName(), in.getLastName(), "", ret.getHighCap()));

			if (!isEmpty(in.getId()))  //prospect id
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BProspectCompany(in.getId()).getSalesPerson()));
			else if (!isEmpty(in.getPersonId())) //sales person id
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(in.getPersonId())));
			else if (!hsu.currentlySuperUser() && !BPerson.getCurrent().isManager()) //sales person id
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(BPerson.getCurrent())));


			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetProspectDetailsReturn getProspectDetails(/*
			 * @WebParam(name = "in")
			 */final GetProspectDetailsInput in) {
		final GetProspectDetailsReturn ret = new GetProspectDetailsReturn();
		try {
			checkLogin(in);

			ret.setData(new BProspectCompany(in.getProspectId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveProspectReturn saveProspect(/*
			 * @WebParam(name = "in")
			 */final SaveProspectInput in) {
		final SaveProspectReturn ret = new SaveProspectReturn();
		try {
			checkLogin(in);

			final BProspectCompany x = new BProspectCompany();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			//now copy all the logs and stuff into the new one from the two old ones
			x.copyFromAndDelete(in.getProspect1Id());
			x.copyFromAndDelete(in.getProspect2Id());

			//in.setDataPostCreate(x);
			//x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetSourceDetailsReturn getSourceDetails(/*
			 * @WebParam(name = "in")
			 */final GetSourceDetailsInput in) {
		final GetSourceDetailsReturn ret = new GetSourceDetailsReturn();
		try {
			checkLogin(in);

			if (!isEmpty(in.getId()))
				ret.setData(new BProspectSource(in.getId()));
			else
				ret.setDescription("");

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
			BSearchOutput<BProspectCompany> bSearchOutput = new BSearchOutput(in.getSearchMetaInput());
			ret.setReportUrl(new ProspectReport().build(bSearchOutput.getSortType(), in.isSortAsc(), in.getIdentifier(), in.getMainContactFirstName(), in.getMainContactLastName(), in.getName(), in.getStatusId(), in.getSourceId(), in.getExcludeId(), 100000, in.getHasPhone(), in.getHasPhone(), in.getSalesPersonId(), in.getFirstContactDateAfter(), in.getFirstContactDateBefore(), in.getLastContactDateAfter(), in.getLastContactDateBefore(), in.getLastLogDateAfter(), in.getLastLogDateBefore(), in.getStatusDateAfter(), in.getStatusDateBefore(), in.getActivesOnly(), in.getTimeZone()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetExportReturn getExport(/*
			 * @WebParam(name = "in")
			 */final GetExportInput in) {
		final GetExportReturn ret = new GetExportReturn();
		try {
			checkLogin(in);

			BSearchOutput<BProspectCompany> bSearchOutput = new BSearchOutput(in.getSearchMetaInput());
			ret.setReportUrl(new ProspectExport().build(bSearchOutput.getSortType(), in.isSortAsc(), in.getIdentifier(), in.getMainContactFirstName(), in.getMainContactLastName(), in.getName(), in.getStatusId(), in.getSourceId(), in.getTypeId(), in.getExcludeId(), 100000, in.getHasPhone(), in.getHasPhone(), in.getSalesPersonId(), in.getFirstContactDateAfter(), in.getFirstContactDateBefore(), in.getLastContactDateAfter(), in.getLastContactDateBefore(), in.getLastLogDateAfter(), in.getLastLogDateBefore(), in.getStatusDateAfter(), in.getStatusDateBefore(), in.getActivesOnly(), in.getTimeZone()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
