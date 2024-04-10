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
package com.arahant.business;

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.ProspectSource;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BProspectSource extends SimpleBusinessObjectBase<ProspectSource> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BProspectSource(id).delete();
	}

	public BProspectSource() {
	}

	public BProspectSource(ProspectSource o) {
		bean = o;
	}

	public BProspectSource(String id) {
		super(id);
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProspectSource();
		return bean.generateId();
	}

	@Override
	public void insert() throws ArahantException {
		super.insert();
		//bean.setCompany(ArahantSession.getHSU().getCurrentCompany());
		// ArahantSession.getHSU().insert(bean);
	}

	public String getCode() {
		return bean.getSourceCode();
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public String getId() {
		return bean.getProspectSourceId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProspectSource.class, key);
	}

	public void setCode(String code) {
		bean.setSourceCode(code);

	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public static BProspectSource[] list() {
		return makeArray(ArahantSession.getHSU().createCriteria(ProspectSource.class)
				.orderBy(ProspectSource.SOURCE_CODE)
				.list());
	}

	static BProspectSource[] makeArray(HibernateScrollUtil<ProspectSource> scr) {
		List<ProspectSource> l = new ArrayList<ProspectSource>();
		while (scr.next())
			l.add(scr.get());

		return BProspectSource.makeArray(l);
	}

	static BProspectSource[] makeArray(List<ProspectSource> list) {
		BProspectSource[] ret = new BProspectSource[list.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProspectSource(list.get(loop));

		return ret;
	}

	public static BSearchOutput<BProspectSource> search(BSearchMetaInput bSearchMetaInput, String code, String description, String[] excludeIds) {
		BSearchOutput<BProspectSource> bSearchOutput = new BSearchOutput<BProspectSource>(bSearchMetaInput);
		HibernateCriteriaUtil<ProspectSource> hcu = search(code, description, excludeIds, bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), true);

		HibernateScrollUtil<ProspectSource> prospectSources;
		if (bSearchMetaInput.isUsingPaging())
			prospectSources = hcu.getPage(bSearchMetaInput.getPagingFirstItemIndex(), bSearchMetaInput.getItemsPerPage());
		else
			prospectSources = hcu.scroll();

		// set output
		bSearchOutput.setItems(makeArray(prospectSources));
		bSearchOutput.setSortAsc(bSearchMetaInput.isSortAsc());
		bSearchOutput.setSortType((bSearchMetaInput.getSortType() == 2) ? bSearchMetaInput.getSortType() : 1);
		if (bSearchMetaInput.isUsingPaging()) {
			hcu = search(code, description, excludeIds, bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), false);

			int totalRecords = hcu.count(ProspectSource.SOURCE_CODE);

			// TODO - THIS IS A PROBLEM - WE NEED A WAY TO DETERMINE WHAT THE ACTUAL START INDEX IS
			// this is the record to start from if using paging, but note that it may be asking for an
			// index that is now out of bounds in which case it must back up to earlier page boundary 
			// (e.g. 50 items per page, was initially 102 items -> asking for first index of 100, but
			// now only 98 items o should return first index of 50)
			bSearchOutput.setPagingFirstItemIndex(bSearchMetaInput.getPagingFirstItemIndex());
			bSearchOutput.setTotalItemsPaging(totalRecords);
			bSearchOutput.setItemsPerPage(bSearchMetaInput.getItemsPerPage());
		}

		return bSearchOutput;
	}

	private static HibernateCriteriaUtil<ProspectSource> search(String code, String description, String[] excludeIds, int sortType, boolean sortAsc, boolean includeSorting) {
		HibernateCriteriaUtil<ProspectSource> hcu = ArahantSession.getHSU().createCriteria(ProspectSource.class)
				.like(ProspectSource.SOURCE_CODE, code)
				.like(ProspectSource.DESCRIPTION, description)
				.notIn(ProspectSource.PROSPECT_SOURCE_ID, excludeIds)
				.geOrEq(ProspectSource.LAST_ACTIVE_DATE, DateUtils.now(), 0);

		if (includeSorting)
			// establish sort
			switch (sortType) {
				default: // source code
					if (sortAsc)
						hcu.orderBy(ProspectSource.SOURCE_CODE);
					else
						hcu.orderByDesc(ProspectSource.SOURCE_CODE);
					break;
				case 2: // description
					if (sortAsc)
						hcu.orderBy(ProspectSource.DESCRIPTION);
					else
						hcu.orderByDesc(ProspectSource.DESCRIPTION);
					break;
			}

		return hcu;
	}

	///////////////////////////////////////////////////////////////////////////////
	/////// TODO REMOVE THESE ONCE EVERYONE IS CONVERTED TO PAGING & SORTING METHOD
	public static BProspectSource[] search(String code, String description, final int max) {
		return search(code, description, null, max);
	}

	public static BProspectSource[] search(String code, String description, String[] excludeIds, final int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(ProspectSource.class)
				.like(ProspectSource.SOURCE_CODE, code)
				.like(ProspectSource.DESCRIPTION, description)
				.notIn(ProspectSource.PROSPECT_SOURCE_ID, excludeIds)
				.orderBy(ProspectSource.SOURCE_CODE)
				.geOrEq(ProspectSource.LAST_ACTIVE_DATE, DateUtils.now(), 0)
				.setMaxResults(max).list());
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public static String findOrMake(String code) {
		ProspectSource ps = ArahantSession.getHSU().createCriteria(ProspectSource.class).eq(ProspectSource.SOURCE_CODE, code).first();
		if (ps == null) {
			BProspectSource bps = new BProspectSource();
			String id = bps.create();
			bps.setDescription(code);
			bps.setCode(code);
			bps.insert();
			ArahantSession.getHSU().flush();
			return id;
		}
		return ps.getProspectSourceId();

	}

	public void setCompany(CompanyDetail c) {
		bean.setCompany(c);
	}

	public CompanyDetail getCompany() {
		return bean.getCompany();
	}

	public static void clone(BCompany from_company, BCompany to_company) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<ProspectSource> crit = hsu.createCriteriaNoCompanyFilter(ProspectSource.class);
		crit.eq(ProspectSource.COMPANY_ID, from_company.getCompanyId());
		crit.le(ProspectSource.LAST_ACTIVE_DATE, DateUtils.today());
		HibernateScrollUtil<ProspectSource> scr = crit.scroll();
		while (scr.next()) {
			ProspectSource es = scr.get();
			ProspectSource nrec = new ProspectSource();
			HibernateSessionUtil.copyCorresponding(nrec, es, "prospectSourceId", "companyId");
			nrec.generateId();
			nrec.setCompany(to_company.getBean());
			hsu.insert(nrec);
		}
	}
}
