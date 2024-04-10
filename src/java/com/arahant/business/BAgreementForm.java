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

import com.arahant.beans.AgreementForm;
import com.arahant.beans.AgreementPersonJoin;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.TiffReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.util.Date;
import java.util.List;


public class BAgreementForm extends SimpleBusinessObjectBase<AgreementForm> {

	public BAgreementForm()
	{
	}
	
	public BAgreementForm(String id)
	{
		super(id);
	}
	
	public BAgreementForm (AgreementForm o)
	{
		bean=o;
	}

	static BAgreementForm[] makeArray(List<AgreementForm> l) {
		BAgreementForm []ret=new BAgreementForm[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BAgreementForm(l.get(loop));
		return ret;
	}

	public void accept() {
		AgreementPersonJoin apj=ArahantSession.getHSU().createCriteria(AgreementPersonJoin.class)
			.eq(AgreementPersonJoin.PERSON, ArahantSession.getHSU().getCurrentPerson())
			.eq(AgreementPersonJoin.FORM, bean)
			.first();

		if (apj==null)
		{
			apj=new AgreementPersonJoin();
			apj.generateId();
			apj.setForm(bean);
			apj.setPerson(ArahantSession.getHSU().getCurrentPerson());
			apj.setAgreementTime(new Date());
			ArahantSession.getHSU().insert(apj);
		}
		else
		{
			apj.setAgreementTime(new Date());
			ArahantSession.getHSU().saveOrUpdate(apj);
		}
	}

	@Override
	public String create() throws ArahantException {
		bean=new AgreementForm();
		bean.setFormDate(new Date());
		return bean.generateId();
	}

	public String getDescription() {
		return bean.getSummary();
	}

	public int getExpirationDate() {
		return bean.getExpirationDate();
	}

	public String getExpirationDateFormatted() {
		return DateUtils.getDateFormatted(bean.getExpirationDate());
	}

	public String getId() {
		return bean.getAgreementFormId();
	}

	public String getName() {
		return bean.getName();
	}

	public String getReport() throws ArahantException {

		return new TiffReport().tifToPdf(bean.getImage(),bean.getFileNameExtension(), getFilename());
	}

	public void setDate(Date date) {
		bean.setFormDate(date);
	}

	public void setExpirationDate(int expirationDate) {
		bean.setExpirationDate(expirationDate);
	}

	public void setExtension(String extension) {
		bean.setFileNameExtension(extension);
	}

	public void setFormData(byte[] formData) {
		bean.setImage(formData);
	}

	public void setName(String name) {
		bean.setName(name);
	}

	public void setSummary(String summary) {
		bean.setSummary(summary);
	}

	private String getFilename() {
		try
		{
			return "Agreement"+getId();
		}
		catch (Exception e)
		{
			return "FormRpt";
		}

	}

	@Override
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(AgreementForm.class,key);
	}

	public static BAgreementForm [] listNotAccepted()
	{
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		List<AgreementForm> forms=hsu.createCriteria(AgreementForm.class)
			.orderByDesc(AgreementForm.NAME)
			.geOrEq(AgreementForm.EXPIRE_DATE, DateUtils.now(), 0)
			.list();

		//get accepteds
		List<AgreementPersonJoin> agrees=hsu.createCriteria(AgreementPersonJoin.class)
			.eq(AgreementPersonJoin.PERSON, hsu.getCurrentPerson())
			.list();

		for (AgreementPersonJoin agj : agrees)
		{
	//		System.out.println(agj.getForm().getFormDate());
	//		System.out.println(agj.getAgreementTime());
			if (agj.getAgreementTime().after(agj.getForm().getFormDate()))
				forms.remove(agj.getForm());
		}

		return makeArray(forms);
	}

	public static BAgreementForm [] list()
	{
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		List<AgreementForm> forms=hsu.createCriteria(AgreementForm.class)
			.orderByDesc(AgreementForm.NAME)
			.list();

		return makeArray(forms);
	}
}
