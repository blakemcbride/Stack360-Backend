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

import com.arahant.beans.ApplicantSource;
import com.arahant.beans.CompanyDetail;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;


public class BApplicantSource extends SimpleBusinessObjectBase<ApplicantSource> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BApplicantSource(id).delete();
	}

	static BApplicantSource[] makeArray(List<ApplicantSource> l) {
		BApplicantSource []ret=new BApplicantSource[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BApplicantSource(l.get(loop));
		return ret;
	}

	public BApplicantSource(String id) {
		super(id);
	}

	public BApplicantSource() {
		super();
	}

	public BApplicantSource(ApplicantSource o) {
		super();
		bean=o;
	}

	@Override
	public String create() throws ArahantException {
		bean=new ApplicantSource();
		return bean.generateId();
	}

	@Override
    public void insert() throws ArahantException {
		bean.setCompany(ArahantSession.getHSU().getCurrentCompany());
        ArahantSession.getHSU().insert(bean);
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

	public String getDescription() {
		return bean.getDescription();
	}

	public String getId() {
		return bean.getApplicantSourceId();
	}

	public int getInactiveDate() {
		return bean.getLastActiveDate();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(ApplicantSource.class,key);
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public void setInactiveDate(int inactiveDate) {
		bean.setLastActiveDate(inactiveDate);
	}

	public static BApplicantSource [] list(int activeType)
	{
		HibernateCriteriaUtil<ApplicantSource> hcu=ArahantSession.getHSU().createCriteria(ApplicantSource.class)
			.orderBy(ApplicantSource.DESCRIPTION);
		
		switch (activeType){
			case 1 : hcu.eq(ApplicantSource.LAST_ACTIVE_DATE, 0);
				break;
			case 2 : hcu.ne(ApplicantSource.LAST_ACTIVE_DATE, 0);
				break;
		}
		
		return makeArray(hcu.list());
	}
	
	public static BApplicantSource[] search (String description, int max)
	{
		return makeArray(ArahantSession.getHSU().createCriteria(ApplicantSource.class)
			.setMaxResults(max)
			.like(ApplicantSource.DESCRIPTION, description)
			.orderBy(ApplicantSource.DESCRIPTION)
			.geOrEq(ApplicantSource.LAST_ACTIVE_DATE,DateUtils.now(),0)
			.list());
	}
	
    public static String findOrMake(String code)
	{
		ApplicantSource as=ArahantSession.getHSU().createCriteria(ApplicantSource.class)
			.eq(ApplicantSource.DESCRIPTION, code)
			.first();
                if (as!=null)
                    return as.getApplicantSourceId();

                BApplicantSource bas = new BApplicantSource();
                bas.create();
                bas.setDescription(code);
                bas.insert();
		return bas.getId();
	}
}
