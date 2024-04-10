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

import com.arahant.beans.ProspectStatus;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.*;
import java.util.ArrayList;
import java.util.List;


public class BProspectStatus extends SimpleBusinessObjectBase<ProspectStatus> {

	public static void delete(String[] ids) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (String id : ids)
			new BProspectStatus(id).delete();
		try {
			hsu.commitTransaction();
		} catch (Exception e) {
			hsu.rollbackTransaction();
			throw new ArahantWarning("Can't delete prospect status because it is being used.");
		} finally {
			hsu.beginTransaction();
		}


		// renumber the items from 0
//		List<ProspectStatus> prospectStatuses = ArahantSession.getHSU().createCriteria(ProspectStatus.class).orderBy(ProspectStatus.SEQ).list();
//		for (short newSeq = 0; newSeq < prospectStatuses.size(); newSeq++) {
//			ProspectStatus prospectStatus = prospectStatuses.get(newSeq);
//
//			prospectStatus.setSequence(newSeq);
//			hsu.saveOrUpdate(prospectStatus);
//		}
	}

	static BProspectStatus[] makeArray(HibernateScrollUtil<ProspectStatus> scr) {
		List<ProspectStatus> l = new ArrayList<ProspectStatus>();
		while (scr.next())
			l.add(scr.get());
		return BProspectStatus.makeArray(l);
	}

	static BProspectStatus[] makeArray(List<ProspectStatus> list) {
		BProspectStatus[] ret = new BProspectStatus[list.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProspectStatus(list.get(loop));
		return ret;
	}

	public BProspectStatus() {
		super();
	}

	public BProspectStatus(String id) {
		super(id);
	}

	public BProspectStatus(ProspectStatus o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProspectStatus();
		return bean.generateId();
	}

	public String getActive() {
		return bean.getActive() + "";
	}

	public String getCode() {
		return bean.getCode();
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public String getId() {
		return bean.getProspectStatusId();
	}

	public int getSequence() {
		return bean.getSequence();
	}
	
	public short getCertainty() {
		return bean.getCertanity();
	}
	
	public void setCertainty(short cert) {
		bean.setCertanity(cert);
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProspectStatus.class, key);
	}

	public void moveUp(boolean moveUp) {
		if (moveUp)
			moveUp();
		else
			moveDown();
	}

	/**
	 * This is the one re-done by Blake
	 */
	@SuppressWarnings("unchecked")
	public void moveUp() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		Object[] recs;
		List<ProspectStatus> reclist;
		int n, i, ri = 0;

		HibernateCriteriaUtil hcu = hsu.createCriteria(ProspectStatus.class);
		if (bean.getCompanyId() == null)
			hcu.isNull(ProspectStatus.COMPANY_ID);
		else
			hcu.eq(ProspectStatus.COMPANY_ID, bean.getCompanyId());
		hcu.orderBy(ProspectStatus.SEQ);
		reclist = hcu.list();
		recs = reclist.toArray();

		n = recs.length;
		for (i = 0; i < n; i++)
			if (recs[i] == bean) {
				ri = i;
				break;
			}

		//  The following lines are needed to be sure we don't have a clash
		for (i = 0; i < n; i++)
			((ProspectStatus) (recs[i])).setSequence((short) -(i + 1));
		hsu.update(reclist);
		hsu.flush();

		for (i = 0; i < n; i++)
			((ProspectStatus) (recs[i])).setSequence((short) i);
		if (ri != 0) {
			ProspectStatus rec = (ProspectStatus) recs[ri];
			ProspectStatus prec = (ProspectStatus) recs[ri - 1];
			rec.setSequence((short) (rec.getSequence() - 1));
			prec.setSequence((short) (prec.getSequence() + 1));
		}
		hsu.update(reclist);
	}

	/*
	 * This method re-done by Blake
	 */
	@SuppressWarnings("unchecked")
	public void moveDown() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		Object[] recs;
		List<ProspectStatus> reclist;
		int n, i, ri = 0;

		HibernateCriteriaUtil hcu = hsu.createCriteria(ProspectStatus.class);
		if (bean.getCompanyId() == null)
			hcu.isNull(ProspectStatus.COMPANY_ID);
		else
			hcu.eq(ProspectStatus.COMPANY_ID, bean.getCompanyId());
		hcu.orderBy(ProspectStatus.SEQ);
		reclist = hcu.list();
		recs = reclist.toArray();

		n = recs.length;
		for (i = 0; i < n; i++)
			if (recs[i] == bean) {
				ri = i;
				break;
			}

		//  The following lines are needed to be sure we don't have a clash
		for (i = 0; i < n; i++)
			((ProspectStatus) (recs[i])).setSequence((short) -(i + 1));
		hsu.update(reclist);
		hsu.flush();

		for (i = 0; i < n; i++)
			((ProspectStatus) (recs[i])).setSequence((short) i);
		if (ri != n - 1) {
			ProspectStatus rec = (ProspectStatus) recs[ri];
			ProspectStatus nrec = (ProspectStatus) recs[ri + 1];
			rec.setSequence((short) (rec.getSequence() + 1));
			nrec.setSequence((short) (nrec.getSequence() - 1));
		}
		hsu.update(reclist);
	}

	public void setActive(String active) {
		if (active.length() > 0)
			bean.setActive(active.charAt(0));
	}

	public void setCode(String code) {
		bean.setCode(code);
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public void setInitialSequence(int initialSequence) {
		if (initialSequence == -1)
			bean.setSequence((short) ArahantSession.getHSU().createCriteria(ProspectStatus.class).count());
		else {
			bean.setSequence((short) ArahantSession.getHSU().createCriteria(ProspectStatus.class).count());
			//move up until it gets there
			while (bean.getSequence() != initialSequence)
				moveUp();
		}
	}

	public static BSearchOutput<BProspectStatus> search(BSearchMetaInput bSearchMetaInput, String code, String description, String[] excludeIds) {
		BSearchOutput<BProspectStatus> bSearchOutput = new BSearchOutput<BProspectStatus>(bSearchMetaInput);
		HibernateCriteriaUtil<ProspectStatus> hcu = search(code, description, excludeIds, bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), true);

		HibernateScrollUtil<ProspectStatus> prospectStatuses;
		if (bSearchMetaInput.isUsingPaging())
			prospectStatuses = hcu.getPage(bSearchMetaInput.getPagingFirstItemIndex(), bSearchMetaInput.getItemsPerPage());
		else
			prospectStatuses = hcu.scroll();

		// set output
		bSearchOutput.setItems(makeArray(prospectStatuses));
		bSearchOutput.setSortAsc(bSearchMetaInput.isSortAsc());
		bSearchOutput.setSortType((bSearchMetaInput.getSortType() >= 2 && bSearchMetaInput.getSortType() <= 4) ? bSearchMetaInput.getSortType() : 1);
		if (bSearchMetaInput.isUsingPaging()) {
			hcu = search(code, description, excludeIds, bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), false);

			int totalRecords = hcu.count(ProspectStatus.CODE);

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

	private static HibernateCriteriaUtil<ProspectStatus> search(String code, String description, String[] excludeIds, int sortType, boolean sortAsc, boolean includeSorting) {
		HibernateCriteriaUtil<ProspectStatus> hcu = ArahantSession.getHSU().createCriteria(ProspectStatus.class).like(ProspectStatus.CODE, code).like(ProspectStatus.DESCRIPTION, description).notIn(ProspectStatus.PROSPECT_STATUS_ID, excludeIds).geOrEq(ProspectStatus.LAST_ACTIVE_DATE, DateUtils.now(), 0);

		if (includeSorting)
			// establish sort
			switch (sortType) {
				default: // status code
					if (sortAsc)
						hcu.orderBy(ProspectStatus.CODE);
					else
						hcu.orderByDesc(ProspectStatus.CODE);
					break;
				case 2: // description
					if (sortAsc)
						hcu.orderBy(ProspectStatus.DESCRIPTION);
					else
						hcu.orderByDesc(ProspectStatus.DESCRIPTION);
					break;
				case 3: // active
					if (sortAsc)
						hcu.orderBy(ProspectStatus.ACTIVE);
					else
						hcu.orderByDesc(ProspectStatus.ACTIVE);
					break;
				case 4: // sequence
					if (sortAsc)
						hcu.orderBy(ProspectStatus.SEQ);
					else
						hcu.orderByDesc(ProspectStatus.SEQ);
					break;
			}

		return hcu;
	}

	///////////////////////////////////////////////////////////////////////////////
	/////// TODO REMOVE THESE ONCE EVERYONE IS CONVERTED TO PAGING & SORTING METHOD
	public static BProspectStatus[] list() {
		return makeArray(ArahantSession.getHSU().createCriteria(ProspectStatus.class).orderBy(ProspectStatus.SEQ).list());
	}

	public static BProspectStatus[] listPipelineStatuses() {
		return makeArray(ArahantSession.getHSU().createCriteria(ProspectStatus.class).orderBy(ProspectStatus.SEQ).eq(ProspectStatus.IN_PIPELINE, 'Y').list());
	}

	public static BProspectStatus[] search(String code, String description, final int max) {
		return search(code, description, null, max);
	}

	public static BProspectStatus[] search(String code, String description, String[] excludeIds, final int max) {
		return makeArray(ArahantSession.getHSU()
				.createCriteria(ProspectStatus.class)
				.like(ProspectStatus.CODE, code)
				.like(ProspectStatus.DESCRIPTION, description)
				.notIn(ProspectStatus.PROSPECT_STATUS_ID, excludeIds)
				.orderBy(ProspectStatus.SEQ)
				.geOrEq(ProspectStatus.LAST_ACTIVE_DATE, DateUtils.now(), 0)
				.setMaxResults(max).list());
	}
	/////// TODO REMOVE THESE ONCE EVERYONE IS CONVERTED TO PAGING & SORTING METHOD
	///////////////////////////////////////////////////////////////////////////////

	public String getCompanyId() {
		return bean.getCompanyId();
	}

	public void setCompanyId(String companyId) {
		bean.setCompanyId(companyId);
	}

	public short getFallbackDays() {
		return bean.getFallbackDays();
	}

	public void setFallbackDays(short fallbackDays) {
		bean.setFallbackDays(fallbackDays);
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public short getSalesPoints() {
		return bean.getSalesPoints();
	}

	public void setSalesPoints(short salesPoints) {
		bean.setSalesPoints(salesPoints);
	}

	public char getShowInSalesPipeline() {
		return bean.getShowInSalesPipeline();
	}

	public void setShowInSalesPipeline(char showInSalesPipeline) {
		bean.setShowInSalesPipeline(showInSalesPipeline);
	}

	public static String findOrMake(String code) {
		ProspectStatus ps = ArahantSession.getHSU().createCriteria(ProspectStatus.class).eq(ProspectStatus.CODE, code).first();
		if (ps == null) {
			BProspectStatus bps = new BProspectStatus();
			String id = bps.create();
			bps.setDescription(code);
			bps.setCode(code);
			bps.setActive("Y");
			bps.insert();
			ArahantSession.getHSU().flush();
			return id;
		}
		return ps.getProspectStatusId();
	}
	
	public static void clone(BCompany from_company, BCompany to_company) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<ProspectStatus> crit = hsu.createCriteriaNoCompanyFilter(ProspectStatus.class);
		crit.eq(ProspectStatus.COMPANY_ID, from_company.getCompanyId());
		crit.le(ProspectStatus.LAST_ACTIVE_DATE, DateUtils.today());
		HibernateScrollUtil<ProspectStatus> scr = crit.scroll();
		while (scr.next()) {
			ProspectStatus es = scr.get();
			ProspectStatus nrec = new ProspectStatus();
			HibernateSessionUtil.copyCorresponding(nrec, es, "prospectStatusId", "companyId");
			nrec.generateId();
			nrec.setCompanyId(to_company.getOrgGroupId());
			hsu.insert(nrec);
		}
	}

}
