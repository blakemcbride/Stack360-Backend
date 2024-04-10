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
 *
 */

package com.arahant.business;

import com.arahant.beans.ClientStatus;
import com.arahant.beans.CompanyDetail;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.Collection;
import java.util.List;

public class BClientStatus extends SimpleBusinessObjectBase<ClientStatus> {

	public static BClientStatus findOrMake(String code) {

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		ClientStatus cs = hsu.createCriteria(ClientStatus.class).eq(ClientStatus.CODE, code).first();

		if (cs != null)
			return new BClientStatus(cs);

		BClientStatus bcs = new BClientStatus();
		bcs.create();

		bcs.setCode(code);
		bcs.setDescription(code);
		bcs.setInitialSequence(hsu.createCriteria(ClientStatus.class).count());
		bcs.setSeqNo((short) hsu.createCriteria(ClientStatus.class).count());

		bcs.insert();

		return bcs;
	}

	public BClientStatus() {
		super();
	}

	public BClientStatus(String id) {
		super(id);
	}

	public BClientStatus(ClientStatus o) {
		bean = o;
	}

	public static void delete(String[] ids) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (String id : ids)
			new BClientStatus(id).delete();
		hsu.flush();
		hsu.commitTransaction();
		hsu.beginTransaction();

		// renumber the items from 0
		List<ClientStatus> clientStatuses = hsu.createCriteria(ClientStatus.class).orderBy(ClientStatus.SEQ).list();
		for (short newSeq = 0; newSeq < clientStatuses.size(); newSeq++) {
			ClientStatus clientStatus = clientStatuses.get(newSeq);
			clientStatus.setSeqNo(newSeq);
			hsu.saveOrUpdate(clientStatus);
		}
	}

	@Override
	public String create() throws ArahantException {
		bean = new ClientStatus();
		return bean.generateId();
	}

	@Override
	public void insert() throws ArahantException {
		//bean.setCompany(ArahantSession.getHSU().getCurrentCompany());
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ClientStatus.class, key);
	}

	/**
	 * @param l
	 * @return
	 * @throws ArahantException
	 */
	public static BClientStatus[] makeArray(final Collection<ClientStatus> l) throws ArahantException {


		final BClientStatus[] ret = new BClientStatus[l.size()];

		int loop = 0;

		for (ClientStatus p : l)
			ret[loop++] = new BClientStatus(p);

		return ret;
	}

	public String getClientStatusId() {
		return bean.getClientStatusId();
	}

	public String getCode() {
		return bean.getCode();
	}

	public void setCode(String code) {
		bean.setCode(code);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public short getSeqNo() {
		return bean.getSeqNo();
	}

	public void setSeqNo(short seqNo) {
		bean.setSeqNo(seqNo);
	}

	public char getActive() {
		return bean.getActive();
	}

	public void setActive(char active) {
		bean.setActive(active);
	}

	public void setInitialSequence(int initialSequence) {
		if (initialSequence == -1)
			bean.setSeqNo((short) ArahantSession.getHSU().createCriteria(ClientStatus.class).count());
		else {
			bean.setSeqNo((short) ArahantSession.getHSU().createCriteria(ClientStatus.class).count());
			//move up until it gets there
			while (bean.getSeqNo() != initialSequence)
				moveUp();
		}
		return;
	}

	public void moveUp(boolean moveUp) {
		if (moveUp)
			moveUp();
		else
			moveDown();
	}

	public void moveUp() {
		if (bean.getSeqNo() > 0) {
			ClientStatus pvj = ArahantSession.getHSU().createCriteria(ClientStatus.class).eq(ClientStatus.SEQ, (short) (bean.getSeqNo() - 1)).first();

			short temp = bean.getSeqNo();
			pvj.setSeqNo((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(pvj);
			ArahantSession.getHSU().flush();
			bean.setSeqNo((short) (bean.getSeqNo() - 1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			ArahantSession.getHSU().flush();
			pvj.setSeqNo(temp);
			ArahantSession.getHSU().saveOrUpdate(pvj);
		} else //shift them all
		{
			List<ClientStatus> l = ArahantSession.getHSU().createCriteria(ClientStatus.class).orderBy(ClientStatus.SEQ).list();

			l.get(0).setSeqNo((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
			ArahantSession.getHSU().flush();
			for (int loop = 1; loop < l.size(); loop++) {
				l.get(loop).setSeqNo((short) (l.get(loop).getSeqNo() - 1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}

			l.get(0).setSeqNo((short) (l.size() - 1));
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
		}
	}

	public void moveDown() {
		if (bean.getSeqNo() != ArahantSession.getHSU().createCriteria(ClientStatus.class).count() - 1) {
			ClientStatus pvj = ArahantSession.getHSU().createCriteria(ClientStatus.class).eq(ClientStatus.SEQ, (short) (bean.getSeqNo() + 1)).first();

			short temp = bean.getSeqNo();
			pvj.setSeqNo((short) 999999);
			ArahantSession.getHSU().saveOrUpdate(pvj);
			ArahantSession.getHSU().flush();
			bean.setSeqNo((short) (bean.getSeqNo() + 1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			pvj.setSeqNo(temp);
			ArahantSession.getHSU().saveOrUpdate(pvj);
		} else //shift them all
		{
			List<ClientStatus> l = ArahantSession.getHSU().createCriteria(ClientStatus.class).orderBy(ClientStatus.SEQ).list();

			l.get(l.size() - 1).setSeqNo((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
			ArahantSession.getHSU().flush();

			for (int loop = l.size() - 1; loop > -1; loop--) {
				l.get(loop).setSeqNo((short) (loop + 1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}

			l.get(l.size() - 1).setSeqNo((short) 0);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
		}
	}

	public static BClientStatus[] list(boolean activesOnly) {
		HibernateCriteriaUtil<ClientStatus> hcu = ArahantSession.getHSU().createCriteria(ClientStatus.class).orderBy(ClientStatus.SEQ);
		if (activesOnly)
			hcu.geOrEq(ClientStatus.LAST_ACTIVE_DATE, DateUtils.now(), 0);
		return makeArray(hcu.list());
	}

	static BClientStatus[] makeArray(List<ClientStatus> list) {
		BClientStatus[] ret = new BClientStatus[list.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BClientStatus(list.get(loop));

		return ret;
	}

	public CompanyDetail getCompany() {
		return bean.getCompany();
	}

	public void setCompany(CompanyDetail cd) {
		bean.setCompany(cd);
	}
}
