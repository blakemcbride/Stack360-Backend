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
import com.arahant.beans.CompanyQuestion;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;

import java.util.List;

public class BCompanyQuestion extends SimpleBusinessObjectBase<CompanyQuestion> {

	public BCompanyQuestion() {
	}

	public BCompanyQuestion(CompanyQuestion o) {
		bean = o;
	}

	public BCompanyQuestion(String key) {
		super(key);
	}

	@Override
	public String create() throws ArahantException {
		bean = new CompanyQuestion();
		return bean.generateId();
	}

	@Override
	public void insert() throws ArahantException {
		//bean.setCompany(ArahantSession.getHSU().getCurrentCompany());
		ArahantSession.getHSU().insert(bean);
	}

	public String getId() {
		return bean.getCompanyQuesId();
	}

	public String getQuestion() {
		return bean.getQuestion();
	}

	public int getSequence() {
		return bean.getSeqno();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(CompanyQuestion.class, key);
	}

	static BCompanyQuestion[] makeArray(List<CompanyQuestion> l) {
		BCompanyQuestion[] ret = new BCompanyQuestion[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BCompanyQuestion(l.get(loop));

		return ret;
	}

	public static void delete(String[] ids) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (String id : ids)
			new BCompanyQuestion(id).delete();
		hsu.flush();

		// renumber the items from 0
		List<CompanyQuestion> questions = ArahantSession.getHSU().createCriteria(CompanyQuestion.class).orderBy(CompanyQuestion.SEQ).list();
		for (short newSeq = 0; newSeq < questions.size(); newSeq++) {
			CompanyQuestion question = questions.get(newSeq);

			question.setSeqno(newSeq);
			hsu.saveOrUpdate(question);
		}
	}

	public static BCompanyQuestion[] list(boolean activesOnly) {
		HibernateCriteriaUtil<CompanyQuestion> hcu = ArahantSession.getHSU().createCriteria(CompanyQuestion.class).orderBy(CompanyQuestion.SEQ);
		if (activesOnly)
			hcu.geOrEq(CompanyQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0);
		return makeArray(hcu.list());
	}

	public void move(boolean moveUp) {
		if (moveUp)
			moveUp();
		else
			moveDown();
	}

	public void moveUp() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (bean.getSeqno() > 0) {
			CompanyQuestion pvj = hsu.createCriteria(CompanyQuestion.class).eq(CompanyQuestion.SEQ, (short) (bean.getSeqno() - 1)).first();

			short temp = bean.getSeqno();
			pvj.setSeqno((short) 99999);
			hsu.saveOrUpdate(pvj);
			hsu.flush();
			bean.setSeqno((short) (bean.getSeqno() - 1));
			hsu.saveOrUpdate(bean);
			hsu.flush();
			pvj.setSeqno(temp);
			hsu.saveOrUpdate(pvj);
		} else //shift them all
		{
			List<CompanyQuestion> l = hsu.createCriteria(CompanyQuestion.class).orderBy(CompanyQuestion.SEQ).list();

			l.get(0).setSeqno((short) 99999);
			hsu.saveOrUpdate(l.get(0));
			hsu.flush();
			for (int loop = 1; loop < l.size(); loop++) {
				l.get(loop).setSeqno((short) (l.get(loop).getSeqno() - 1));
				hsu.saveOrUpdate(l.get(loop));
				hsu.flush();
			}

			l.get(0).setSeqno((short) (l.size() - 1));
			hsu.saveOrUpdate(l.get(0));
		}
	}

	public void moveDown() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (bean.getSeqno() != hsu.createCriteria(CompanyQuestion.class).count() - 1) {
			CompanyQuestion pvj = hsu.createCriteria(CompanyQuestion.class).eq(CompanyQuestion.SEQ, (short) (bean.getSeqno() + 1)).first();

			short temp = bean.getSeqno();
			pvj.setSeqno((short) 999999);
			hsu.saveOrUpdate(pvj);
			hsu.flush();
			bean.setSeqno((short) (bean.getSeqno() + 1));
			hsu.saveOrUpdate(bean);
			pvj.setSeqno(temp);
			hsu.saveOrUpdate(pvj);
		} else //shift them all
		{
			List<CompanyQuestion> l = hsu.createCriteria(CompanyQuestion.class).orderBy(CompanyQuestion.SEQ).list();

			l.get(l.size() - 1).setSeqno((short) 99999);
			hsu.saveOrUpdate(l.get(l.size() - 1));
			hsu.flush();

			for (int loop = l.size() - 1; loop > -1; loop--) {
				l.get(loop).setSeqno((short) (loop + 1));
				hsu.saveOrUpdate(l.get(loop));
				hsu.flush();
			}

			l.get(l.size() - 1).setSeqno((short) 0);
			hsu.saveOrUpdate(l.get(l.size() - 1));
		}
	}

	public void setAddAfterId(String addAfterId) {
		if (isEmpty(addAfterId))
			bean.setSeqno((short) ArahantSession.getHSU().createCriteria(CompanyQuestion.class).count());
		else {
			BCompanyQuestion bcq = new BCompanyQuestion(addAfterId);
			int initialSequence = bcq.getSequence() + 1;
			bean.setSeqno((short) ArahantSession.getHSU().createCriteria(CompanyQuestion.class).count());

			ArahantSession.getHSU().insert(bean);
			//move up until it gets there
			while (bean.getSeqno() != initialSequence)
				moveUp();
		}

	}

	public void setQuestion(String question) {
		bean.setQuestion(question);
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public CompanyDetail getCompany() {
		return bean.getCompany();
	}

	public void setCompany(CompanyDetail cd) {
		bean.setCompany(cd);
	}
}
