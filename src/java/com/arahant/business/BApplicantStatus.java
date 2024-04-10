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

import com.arahant.beans.ApplicantStatus;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;

import java.util.List;

public class BApplicantStatus extends SimpleBusinessObjectBase<ApplicantStatus> {

	public static void delete(String[] ids) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (String id : ids)
			new BApplicantStatus(id).delete();

		hsu.commitTransaction();
		hsu.beginTransaction();

		// renumber the items from 0
		List<ApplicantStatus> prospectStatuses = hsu.createCriteria(ApplicantStatus.class).orderBy(ApplicantStatus.SEQ).list();
		for (short newSeq = 0; newSeq < prospectStatuses.size(); newSeq++) {
			ApplicantStatus prospectStatus = prospectStatuses.get(newSeq);
			prospectStatus.setStatusOrder(newSeq);
			hsu.update(prospectStatus);
		}
	}

	static BApplicantStatus[] makeArray(List<ApplicantStatus> l) {
		BApplicantStatus[] ret = new BApplicantStatus[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BApplicantStatus(l.get(loop));
		return ret;
	}

	public BApplicantStatus(String id) {
		super(id);
	}

	public BApplicantStatus(ApplicantStatus o) {
		super();
		bean = o;
	}

	public BApplicantStatus() {
		super();
	}

	@Override
	public String create() throws ArahantException {
		bean = new ApplicantStatus();
		return bean.generateId();
	}

	@Override
	public void insert() throws ArahantException {
		bean.setCompany(ArahantSession.getHSU().getCurrentCompany());
		ArahantSession.getHSU().insert(bean);
	}

	public boolean getConsiderForHire() {
		return bean.getConsiderForHire() == 'Y';
	}

	public String getId() {
		return bean.getApplicantStatusId();
	}

	public int getInactiveDate() {
		return bean.getLastActiveDate();
	}

	public String getName() {
		return bean.getName();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ApplicantStatus.class, key);
	}

	public void setConsiderForHire(boolean considerForHire) {
		bean.setConsiderForHire(considerForHire ? 'Y' : 'N');
	}

	public void setInactiveDate(int inactiveDate) {
		bean.setLastActiveDate(inactiveDate);
	}

	public void setMoveUp(boolean moveUp) {
		if (moveUp)
			moveUp();
		else
			moveDown();
	}

	public void setName(String name) {
		bean.setName(name);
	}

	public static BApplicantStatus[] list(int activeType) {
		HibernateCriteriaUtil<ApplicantStatus> hcu = ArahantSession.getHSU().createCriteria(ApplicantStatus.class)
				.orderBy(ApplicantStatus.SEQ);

		switch (activeType) {
			case 1:
				hcu.eq(ApplicantStatus.LAST_ACTIVE_DATE, 0);
				break;
			case 2:
				hcu.ne(ApplicantStatus.LAST_ACTIVE_DATE, 0);
				break;
		}
		return makeArray(hcu.list());
	}

	public void moveUp() {
		if (bean.getStatusOrder() > 0) {
			ApplicantStatus pvj = ArahantSession.getHSU().createCriteria(ApplicantStatus.class)
					.eq(ApplicantStatus.SEQ, (short) (bean.getStatusOrder() - 1))
					.first();

			short temp = bean.getStatusOrder();
			pvj.setStatusOrder((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(pvj);
			ArahantSession.getHSU().flush();
			bean.setStatusOrder((short) (bean.getStatusOrder() - 1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			ArahantSession.getHSU().flush();
			pvj.setStatusOrder(temp);
			ArahantSession.getHSU().saveOrUpdate(pvj);
		} else //shift them all
		{
			List<ApplicantStatus> l = ArahantSession.getHSU().createCriteria(ApplicantStatus.class)
					.orderBy(ApplicantStatus.SEQ)
					.list();

			l.get(0).setStatusOrder((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
			ArahantSession.getHSU().flush();
			for (int loop = 1; loop < l.size(); loop++) {
				l.get(loop).setStatusOrder((short) (l.get(loop).getStatusOrder() - 1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}

			l.get(0).setStatusOrder((short) (l.size() - 1));
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
		}
	}

	public void moveDown() {
		if (bean.getStatusOrder() != ArahantSession.getHSU().createCriteria(ApplicantStatus.class)
				.count() - 1) {
			ApplicantStatus pvj = ArahantSession.getHSU().createCriteria(ApplicantStatus.class)
					.eq(ApplicantStatus.SEQ, (short) (bean.getStatusOrder() + 1))
					.first();

			short temp = bean.getStatusOrder();
			pvj.setStatusOrder((short) 999999);
			ArahantSession.getHSU().saveOrUpdate(pvj);
			ArahantSession.getHSU().flush();
			bean.setStatusOrder((short) (bean.getStatusOrder() + 1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			pvj.setStatusOrder(temp);
			ArahantSession.getHSU().saveOrUpdate(pvj);
		} else //shift them all
		{
			List<ApplicantStatus> l = ArahantSession.getHSU().createCriteria(ApplicantStatus.class)
					.orderBy(ApplicantStatus.SEQ)
					.list();

			l.get(l.size() - 1).setStatusOrder((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
			ArahantSession.getHSU().flush();

			for (int loop = l.size() - 1; loop > -1; loop--) {
				l.get(loop).setStatusOrder((short) (loop + 1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}

			l.get(l.size() - 1).setStatusOrder((short) 0);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
		}
	}

	public void setAddAfterId(String addAfterId) {
		if (isEmpty(addAfterId)) {
			bean.setStatusOrder((short) ArahantSession.getHSU().createCriteria(ApplicantStatus.class).count(ApplicantStatus.SEQ));
		} else {
			BApplicantStatus bcq = new BApplicantStatus(addAfterId);
			int initialSequence = bcq.bean.getStatusOrder() + 1;
			bean.setStatusOrder((short) ArahantSession.getHSU().createCriteria(ApplicantStatus.class).count(ApplicantStatus.SEQ));

			ArahantSession.getHSU().insert(bean);
			//move up until it gets there
			while (bean.getStatusOrder() != initialSequence)
				moveUp();
		}
	}

	public static BApplicantStatus[] search(String name, int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(ApplicantStatus.class)
				.setMaxResults(max)
				.like(ApplicantStatus.NAME, name)
				.orderBy(ApplicantStatus.NAME)
				.geOrEq(ApplicantStatus.LAST_ACTIVE_DATE, DateUtils.now(), 0)
				.list());
	}

	public static String findOrMake(String code) {
		ApplicantStatus as = ArahantSession.getHSU().createCriteria(ApplicantStatus.class)
				.eq(ApplicantStatus.NAME, code)
				.first();
		if (as != null)
			return as.getApplicantStatusId();

		BApplicantStatus bas = new BApplicantStatus();
		bas.create();
		bas.setName(code);
		bas.setConsiderForHire(true);
		bas.getBean().setStatusOrder(Short.parseShort("100"));
		bas.insert();
		return bas.getId();
	}


	public String getEmailSource() {
		return bean.getEmailSource();
	}

	public void setEmailSource(String emailSource) {
		bean.setEmailSource(emailSource);
	}

	public String getEmailText() {
		return bean.getEmailText();
	}

	public void setEmailText(String emailText) {
		bean.setEmailText(emailText);
	}


	public char getSendEmail() {
		return bean.getSendEmail();
	}

	public void setSendEmail(char sendEmail) {
		bean.setSendEmail(sendEmail);
	}

	public String getEmailSubject() {
		return bean.getEmailSubject();
	}

	public void setEmailSubject(String emailSubject) {
		bean.setEmailSubject(emailSubject);
	}

	public static void main(String[] args) {
		List<ApplicantStatus> appstat = ArahantSession.getHSU().createCriteria(ApplicantStatus.class)
				.like(ApplicantStatus.NAME, "%")
				.orderBy(ApplicantStatus.NAME)
				.geOrEq(ApplicantStatus.LAST_ACTIVE_DATE, DateUtils.now(), 0)
				.list();

		for (ApplicantStatus a : appstat) {
			System.out.println(a.getName());
		}
	}
}
