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

import com.arahant.beans.CompanyForm;
import com.arahant.beans.CompanyFormFolder;
import com.arahant.beans.FormType;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.TiffReport;
import com.arahant.services.standard.misc.documentSearch.SearchCompanyDocumentsInput;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class BCompanyForm extends SimpleBusinessObjectBase<CompanyForm> {

	public static BCompanyForm[] makeArray(List<CompanyForm> l) {
		BCompanyForm[] ret = new BCompanyForm[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BCompanyForm(l.get(loop));
		return ret;
	}

	public BCompanyForm() {
	}

	public BCompanyForm(String id) {
		super(id);
	}

	public BCompanyForm(CompanyForm o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new CompanyForm();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(CompanyForm.class, key);
	}

	public void expireNow() {
		bean.setLastActiveDate(DateUtils.add(DateUtils.now(), -1));
		update();
	}

	public void copyTo(String folderId) {
		bean.getFolders().add(ArahantSession.getHSU().get(CompanyFormFolder.class, folderId));
		update();
	}

	public void moveTo(String fromFolderId, String toFolderId) {
		remove(fromFolderId);
		copyTo(toFolderId);
	}

	public void remove(String fromFolderId) {
		bean.getFolders().remove(ArahantSession.getHSU().get(CompanyFormFolder.class, fromFolderId));
		update();
	}

	@Override
	public void delete() {
		bean.getFolders().clear();
		ArahantSession.getHSU().saveOrUpdate(bean);
		super.delete();
	}

	public void setComments(String comments) {
		bean.setComments(comments);
	}

	public String getExtension() {
		return bean.getFileNameExtension();
	}

	public void setExtension(String extension) {
		bean.setFileNameExtension(extension);
	}

	public int getFirstActiveDate() {
		return bean.getFirstActiveDate();
	}

	public void setFirstActiveDate(int firstActiveDate) {
		bean.setFirstActiveDate(firstActiveDate);
	}

	public void setFolderId(String folderId) {
		bean.getFolders().add(ArahantSession.getHSU().get(CompanyFormFolder.class, folderId));
	}

	public void setFormData(byte[] formData) {
		bean.setImage(formData);
	}

	public void setFormTypeId(String formTypeId) {
		bean.setFormType(ArahantSession.getHSU().get(FormType.class, formTypeId));
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public String getComments() {
		return bean.getComments();
	}

	public String getCompanyFormId() {
		return bean.getCompanyFormId();
	}

	public FormType getFormType() {
		return bean.getFormType();
	}

	/**
	 * @param formType The formType to set.
	 */
	public void setFormType(final FormType formType) {
		bean.setFormType(formType);
	}

	public String getForm() throws ArahantException {
		return new TiffReport().tifToPdf(bean.getImage(), bean.getFileNameExtension(), getFilename());
	}

	public String getSource() {
		return bean.getSource();
	}

	public void setSource(final String source) {
		bean.setSource(source);
	}

	private String getFilename() {
		try {
			File tif = FileSystemUtils.createTempFile(bean.getFormType().getFormCode(), "." + bean.getFileNameExtension().toLowerCase());
			return tif.getName();
		} catch (Exception e) {
			return "FormRpt";
		}
	}

	public static BCompanyForm[] searchDocuments(SearchCompanyDocumentsInput in) {
		List<CompanyForm> allowed = new ArrayList<CompanyForm>();

		HibernateCriteriaUtil<CompanyForm> hcu = ArahantSession.getHSU().createCriteria(CompanyForm.class).orderBy(CompanyForm.COMMENTS);

		if (!isEmpty(in.getName()))
			hcu.like(CompanyForm.COMMENTS, in.getName());

		if (!isEmpty(in.getTypeId()))
			hcu.eq(CompanyForm.FORM_TYPE, new BFormType(in.getTypeId()).getBean());

		hcu.dateSpanCompare(CompanyForm.FIRSTACTIVEDATE, CompanyForm.LASTACTIVEDATE, in.getFromDate(), in.getToDate());

		if (!isEmpty(in.getOrgGroupId()))
			hcu.joinTo(CompanyForm.FOLDERS).eqOrNull(CompanyFormFolder.ORG_GROUP, new BOrgGroup(in.getOrgGroupId()).getOrgGroup());

		if (allowed.size() > 0)
			return makeArray(allowed);
		else
			return makeArray(hcu.list());
	}
}
