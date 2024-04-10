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
 *
 *  Created on Jul 10, 2007
*/

package com.arahant.business;

import com.arahant.beans.AIProperty;
import java.io.FileNotFoundException;
import java.util.List;

import com.arahant.beans.FormType;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.FormTypeReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;

public class BFormType extends SimpleBusinessObjectBase<FormType>{

	public BFormType() {
	}

	/**
	 * @param id
	 * @throws ArahantException 
	 */
	public BFormType(final String id) throws ArahantException {
		load(id);
	}

	/**
	 * @param type
	 */
	public BFormType(final FormType type) {
		bean = type;
	}

	@Override
	public String create() throws ArahantException {
		bean = new FormType();
		return bean.generateId();
	}

	@Override
	public void load(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(FormType.class, key);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException 
	 * @throws ArahantDeleteException 
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantDeleteException, ArahantException {
		for (String element : ids)
			new BFormType(element).delete();
	}

	/**
	 * @return
	 * @throws ArahantException 
	 * @throws DocumentException 
	 * @throws FileNotFoundException 
	 */
	public static String getReport(final char type) throws FileNotFoundException, DocumentException, ArahantException {
		return new FormTypeReport().build(list(type)); 
	}

	public String getCode() {
		return bean.getFormCode();
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public String getId() {
		return bean.getFormTypeId();
	}


	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public static BFormType[] list(final char type) {
		final char[] types ={type,'B'};
		final HibernateCriteriaUtil<FormType> hcu = ArahantSession.getHSU().createCriteria(FormType.class)
			.in(FormType.TYPE, types)
			.orderBy(FormType.FORM_CODE);
		return makeArray(hcu.list());
	}

	private static BFormType[] makeArray(final List<FormType> l) {
		final BFormType[] ret = new BFormType[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BFormType(l.get(loop));
		return ret;
	}

	public void setCode(final String code) {
		bean.setFormCode(code);
	}

	public void setDescription(final String description) {
		bean.setDescription(description);
	}

	public char getFormType() {
		return bean.getFormType();
	}

	public void setFormType(final char c) {
		bean.setFormType(c);
	}

	public char getFieldDownloadable() {
	    return bean.getFieldDownloadable();
    }

    public void setFieldDownloadable(char fd) {
	    bean.setFieldDownloadable(fd);
    }

	public char getInternal() {
		return bean.getInternal();
	}

	public void setInternal(char internal) {
        bean.setInternal(internal);
    }

	public static BFormType[] list() {
		final HibernateCriteriaUtil<FormType> hcu = ArahantSession.getHSU().createCriteria(FormType.class)
			.orderBy(FormType.TYPE)
			.orderBy(FormType.FORM_CODE);
		/*
		List<String> restrictedList = AIProperty.getList("RestrictedFormTypes");
		if (restrictedList.size()>0)
			hcu.in(FormType.ID, restrictedList);

		 */
		return makeArray(hcu.list());
	}

	public static BFormType[] listTypes()
	{
		final HibernateCriteriaUtil<FormType> hcu = ArahantSession.getHSU().createCriteria(FormType.class)
			.orderBy(FormType.TYPE)
			.orderBy(FormType.FORM_CODE);
		return makeArray(hcu.list());
	}

    public static String findOrMake(String code, String description)
	{
		FormType as = ArahantSession.getHSU().createCriteria(FormType.class)
			.eq(FormType.FORM_CODE, code)
			.first();
                if (as!=null)
                    return as.getFormTypeId();

                BFormType bas = new BFormType();
                bas.create();
                bas.setCode(code);
                bas.setDescription(description);
                bas.setFormType('E');
                bas.insert();
		return bas.getId();
	}
}

	
