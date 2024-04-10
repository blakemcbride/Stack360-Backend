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

import com.arahant.beans.*;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import java.util.ArrayList;
import java.util.List;

public class BProspectType extends SimpleBusinessObjectBase<ProspectType> {

	public BProspectType() {
	}

	public BProspectType(final ProspectType bean) {
		this.bean = bean;
	}

	public BProspectType(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BProspectType(id).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProspectType();
		bean.generateId();
		return getProspectTypeId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProspectType.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public static BProspectType[] makeArray(HibernateScrollUtil<ProspectType> scr) {
		List<ProspectType> l = new ArrayList<ProspectType>();
		while (scr.next())
			l.add(scr.get());
		return BProspectType.makeArray(l);
	}

	public static BProspectType[] makeArray(List<ProspectType> list) {
		BProspectType[] ret = new BProspectType[list.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProspectType(list.get(loop));
		return ret;
	}

	public static BSearchOutput<BProspectType> search(BSearchMetaInput bSearchMetaInput, String code, String description, String[] excludeIds) {
		BSearchOutput<BProspectType> bSearchOutput = new BSearchOutput<BProspectType>(bSearchMetaInput);
		HibernateCriteriaUtil<ProspectType> hcu = search(code, description, excludeIds, bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), true);

		HibernateScrollUtil<ProspectType> prospectTypes;
		if (bSearchMetaInput.isUsingPaging())
			prospectTypes = hcu.getPage(bSearchMetaInput.getPagingFirstItemIndex(), bSearchMetaInput.getItemsPerPage());
		else
			prospectTypes = hcu.scroll();

		// set output
		bSearchOutput.setItems(makeArray(prospectTypes));
		bSearchOutput.setSortAsc(bSearchMetaInput.isSortAsc());
		bSearchOutput.setSortType((bSearchMetaInput.getSortType() == 2) ? bSearchMetaInput.getSortType() : 1);
		if (bSearchMetaInput.isUsingPaging()) {
			hcu = search(code, description, excludeIds, bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), false);

			int totalRecords = hcu.count(ProspectType.TYPE_CODE);

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

	public static BProspectType[] search(String code, String description, final int max) {
		return search(code, description, null, max);
	}

	public static BProspectType[] search(String code, String description, String[] excludeIds, final int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(ProspectType.class).like(ProspectType.TYPE_CODE, code).like(ProspectType.DESCRIPTION, description).notIn(ProspectType.PROSPECT_TYPE_ID, excludeIds).orderBy(ProspectType.TYPE_CODE).geOrEq(ProspectType.LAST_ACTIVE_DATE, DateUtils.now(), 0).setMaxResults(max).list());
	}

	private static HibernateCriteriaUtil<ProspectType> search(String code, String description, String[] excludeIds, int sortType, boolean sortAsc, boolean includeSorting) {
		HibernateCriteriaUtil<ProspectType> hcu = ArahantSession.getHSU().createCriteria(ProspectType.class).like(ProspectType.TYPE_CODE, code).like(ProspectType.DESCRIPTION, description).geOrEq(ProspectType.LAST_ACTIVE_DATE, DateUtils.now(), 0).notIn(ProspectType.PROSPECT_TYPE_ID, excludeIds);

		if (includeSorting)
			// establish sort
			switch (sortType) {
				default: // source code
					if (sortAsc)
						hcu.orderBy(ProspectType.TYPE_CODE);
					else
						hcu.orderByDesc(ProspectType.TYPE_CODE);
					break;
				case 2: // description
					if (sortAsc)
						hcu.orderBy(ProspectType.DESCRIPTION);
					else
						hcu.orderByDesc(ProspectType.DESCRIPTION);
					break;
			}

		return hcu;
	}

	public String getProspectTypeId() {
		return bean.getProspectTypeId();
	}

	public void setProspectTypeId(String prospectTypeId) {
		bean.setProspectTypeId(prospectTypeId);
	}

	public CompanyDetail getCompany() {
		return bean.getCompany();
	}

	public void setCompany(CompanyDetail company) {
		bean.setCompany(company);
	}

	public String getCompanyId() {
		return bean.getCompanyId();
	}

	public void setCompanyId(String companyId) {
		bean.setCompany(ArahantSession.getHSU().get(CompanyDetail.class, companyId));
	}

	public String getTypeCode() {
		return bean.getTypeCode();
	}

	public void setTypeCode(String typeCode) {
		bean.setTypeCode(typeCode);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(final String description) {
		bean.setDescription(description);
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public static void clone(BCompany from_company, BCompany to_company) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<ProspectType> crit = hsu.createCriteriaNoCompanyFilter(ProspectType.class);
		crit.eq(ProspectType.COMPANY_ID, from_company.getCompanyId());
		crit.le(ProspectType.LAST_ACTIVE_DATE, DateUtils.today());
		HibernateScrollUtil<ProspectType> scr = crit.scroll();
		while (scr.next()) {
			ProspectType es = scr.get();
			ProspectType nrec = new ProspectType();
			HibernateSessionUtil.copyCorresponding(nrec, es, "prospectStatusId", "companyId");
			nrec.generateId();
			nrec.setCompany(to_company.getBean());
			hsu.insert(nrec);
		}
	}
}
