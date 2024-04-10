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

import com.arahant.beans.ApplicantAppStatus;
import com.arahant.beans.CompanyDetail;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;

import java.util.List;

public class BApplicationStatus extends SimpleBusinessObjectBase<ApplicantAppStatus> {

	public BApplicationStatus(String id) {
		super(id);
	}

	public BApplicationStatus(ApplicantAppStatus o) {
		bean = o;
	}

	public BApplicationStatus() {
	}

	@Override
	public String create() throws ArahantException {
		bean = new ApplicantAppStatus();
		return bean.generateId();
	}

	@Override
	public void insert() throws ArahantException {
		bean.setCompany(ArahantSession.getHSU().getCurrentCompany());
		ArahantSession.getHSU().insert(bean);
	}

	public boolean getActive() {
		return bean.getIsActive() == 'Y';
	}

	public String getId() {
		return bean.getApplicantAppStatusId();
	}

	public int getInactiveDate() {
		return bean.getLastActiveDate();
	}

	public String getName() {
		return bean.getStatusName();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ApplicantAppStatus.class, key);
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

	public void setActive(boolean active) {
		bean.setIsActive(active ? 'Y' : 'N');
	}

	public void setInactiveDate(int inactiveDate) {
		bean.setLastActiveDate(inactiveDate);
	}

	public short getPhase() {
		return bean.getPhase();
	}

	public void setPhase(short phase) {
        bean.setPhase(phase);
    }

	public void setMoveUp(boolean moveUp) {
		if (moveUp)
			moveUp();
		else
			moveDown();
	}

	public void setName(String name) {
		bean.setStatusName(name);
	}

	public static BApplicationStatus[] list(int activeType) {
		HibernateCriteriaUtil<ApplicantAppStatus> hcu = ArahantSession.getHSU().createCriteria(ApplicantAppStatus.class).orderBy(ApplicantAppStatus.SEQ);

		switch (activeType) {
			case 1:
				hcu.eq(ApplicantAppStatus.LAST_ACTIVE_DATE, 0);
				break;
			case 2:
				hcu.ne(ApplicantAppStatus.LAST_ACTIVE_DATE, 0);
				break;
		}
		return makeArray(hcu.list());
	}

	public void moveUp() {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (bean.getStatusOrder() > 0) {
			ApplicantAppStatus pvj = hsu.createCriteria(ApplicantAppStatus.class).eq(ApplicantAppStatus.SEQ, (short) (bean.getStatusOrder() - 1)).first();

			short temp = bean.getStatusOrder();
			pvj.setStatusOrder((short) 99999);
			hsu.saveOrUpdate(pvj);
			hsu.flush();
			bean.setStatusOrder((short) (bean.getStatusOrder() - 1));
			hsu.saveOrUpdate(bean);
			hsu.flush();
			pvj.setStatusOrder(temp);
			hsu.saveOrUpdate(pvj);
		} else //shift them all
		{
			List<ApplicantAppStatus> l = hsu.createCriteria(ApplicantAppStatus.class).orderBy(ApplicantAppStatus.SEQ).list();

			l.get(0).setStatusOrder((short) 99999);
			hsu.saveOrUpdate(l.get(0));
			hsu.flush();
			for (int loop = 1; loop < l.size(); loop++) {
				l.get(loop).setStatusOrder((short) (l.get(loop).getStatusOrder() - 1));
				hsu.saveOrUpdate(l.get(loop));
				hsu.flush();
			}

			l.get(0).setStatusOrder((short) (l.size() - 1));
			hsu.saveOrUpdate(l.get(0));
		}
	}

	public void moveDown() {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (bean.getStatusOrder() != hsu.createCriteria(ApplicantAppStatus.class).count() - 1) {
			ApplicantAppStatus pvj = hsu.createCriteria(ApplicantAppStatus.class).eq(ApplicantAppStatus.SEQ, (short) (bean.getStatusOrder() + 1)).first();
			short temp = bean.getStatusOrder();
			pvj.setStatusOrder((short) 999999);
			hsu.saveOrUpdate(pvj);
			hsu.flush();
			bean.setStatusOrder((short) (bean.getStatusOrder() + 1));
			hsu.saveOrUpdate(bean);
			pvj.setStatusOrder(temp);
			hsu.saveOrUpdate(pvj);
		} else //shift them all
		{
			List<ApplicantAppStatus> l = hsu.createCriteria(ApplicantAppStatus.class).orderBy(ApplicantAppStatus.SEQ).list();

			l.get(l.size() - 1).setStatusOrder((short) 99999);
			hsu.saveOrUpdate(l.get(l.size() - 1));
			hsu.flush();

			for (int loop = l.size() - 1; loop > -1; loop--) {
				l.get(loop).setStatusOrder((short) (loop + 1));
				hsu.saveOrUpdate(l.get(loop));
				hsu.flush();
			}

			l.get(l.size() - 1).setStatusOrder((short) 0);
			hsu.saveOrUpdate(l.get(l.size() - 1));
		}
	}

	public void setAddAfterId(String addAfterId) {
		if (isEmpty(addAfterId))
			bean.setStatusOrder((short) ArahantSession.getHSU().createCriteria(ApplicantAppStatus.class).count());
		else {
			BApplicationStatus bcq = new BApplicationStatus(addAfterId);
			int initialSequence = bcq.bean.getStatusOrder() + 1;
			bean.setStatusOrder((short) ArahantSession.getHSU().createCriteria(ApplicantAppStatus.class).count());

			ArahantSession.getHSU().insert(bean);
			//move up until it gets there
			while (bean.getStatusOrder() != initialSequence)
				moveUp();
		}

	}

	public static BApplicationStatus[] search(String name, int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(ApplicantAppStatus.class).setMaxResults(max).like(ApplicantAppStatus.NAME,
				name).orderBy(ApplicantAppStatus.STATUS_ORDER).geOrEq(ApplicantAppStatus.LAST_ACTIVE_DATE, DateUtils.now(), 0).list());
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BApplicationStatus(id).delete();

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.commitTransaction();
		hsu.beginTransaction();
		// renumber the items from 0
		List<ApplicantAppStatus> prospectStatuses = hsu.createCriteria(ApplicantAppStatus.class).orderBy(ApplicantAppStatus.SEQ).list();
		for (short newSeq = 0; newSeq < prospectStatuses.size(); newSeq++) {
			ApplicantAppStatus prospectStatus = prospectStatuses.get(newSeq);
			prospectStatus.setStatusOrder(newSeq);
			hsu.update(prospectStatus);
		}
	}

	public static BApplicationStatus[] makeArray(List<ApplicantAppStatus> l) {
		BApplicationStatus[] ret = new BApplicationStatus[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BApplicationStatus(l.get(loop));
		return ret;
	}
}
