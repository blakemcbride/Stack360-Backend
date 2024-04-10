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

import com.arahant.beans.AppointmentLocation;
import com.arahant.beans.CompanyDetail;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import java.util.List;

/**
 *
 */
public class BAppointmentLocation extends SimpleBusinessObjectBase<AppointmentLocation> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BAppointmentLocation(id).delete();
	}

	private static BAppointmentLocation[] makeArray(List<AppointmentLocation> list) {
		BAppointmentLocation ret[] = new BAppointmentLocation[list.size()];
		for (int loop = 0; loop < list.size(); loop++)
			ret[loop] = new BAppointmentLocation(list.get(loop));
		return ret;
	}

	@Override
	public String create() throws ArahantException {
		bean = new AppointmentLocation();
		return bean.generateId();
	}

	@Override
	public void insert() throws ArahantException {
		//bean.setCompany(ArahantSession.getHSU().getCurrentCompany());
		ArahantSession.getHSU().insert(bean);
	}

	public String getCode() {
		return bean.getCode();
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public String getId() {
		return bean.getLocationId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(AppointmentLocation.class, key);
	}

	public BAppointmentLocation() {
	}

	public BAppointmentLocation(String id) {
		super(id);
	}

	public BAppointmentLocation(AppointmentLocation o) {
		bean = o;
	}

	public static BAppointmentLocation[] list() {
		return makeArray(ArahantSession.getHSU().createCriteria(AppointmentLocation.class).orderBy(AppointmentLocation.CODE).list());
	}

	public void setCode(String code) {
		bean.setCode(code);
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public static BAppointmentLocation[] search(String code, String description, int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(AppointmentLocation.class).setMaxResults(max).geOrEq(AppointmentLocation.LAST_ACTIVE_DATE, DateUtils.now(), 0).like(AppointmentLocation.CODE, code).like(AppointmentLocation.DESCRIPTION, description).list());
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

	public static void clone(BCompany from_company, BCompany to_company) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<AppointmentLocation> crit = hsu.createCriteriaNoCompanyFilter(AppointmentLocation.class);
		crit.eq(AppointmentLocation.COMPANYID, from_company.getCompanyId());
		crit.le(AppointmentLocation.LAST_ACTIVE_DATE, DateUtils.today());
		HibernateScrollUtil<AppointmentLocation> scr = crit.scroll();
		while (scr.next()) {
			AppointmentLocation es = scr.get();
			AppointmentLocation nrec = new AppointmentLocation();
			HibernateSessionUtil.copyCorresponding(nrec, es, "locationId", "companyId");
			nrec.generateId();
			nrec.setCompany(to_company.getBean());
			hsu.insert(nrec);
		}
	}
}
