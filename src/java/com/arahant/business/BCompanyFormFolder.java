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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arahant.business;

import com.arahant.beans.CompanyForm;
import com.arahant.beans.CompanyFormFolder;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class BCompanyFormFolder extends SimpleBusinessObjectBase<CompanyFormFolder> {

	public BCompanyFormFolder(CompanyFormFolder o) {
		super();
		bean = o;
	}

	public BCompanyFormFolder() {
		super();
	}

	public BCompanyFormFolder(String id) {
		super(id);
	}

	@Override
	public String create() throws ArahantException {
		bean = new CompanyFormFolder();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(CompanyFormFolder.class, key);
	}

	@Override
	public void delete() {
		//recursively delete all the folders and screens all the way down
		//front end had better give them an are you sure message!
		for (CompanyFormFolder h : ArahantSession.getHSU().createCriteria(CompanyFormFolder.class).eq(CompanyFormFolder.PARENT, bean).list()) {
			new BCompanyFormFolder(h).delete();
		}

		super.delete();

	}

	public String getFolderId() {
		return bean.getFolderId();
	}

	public void setFolderId(String folderId) {
		bean.setFolderId(folderId);
	}

	public String getFolderName() {
		return bean.getFolderName();
	}

	public void setFolderName(String folderName) {
		bean.setFolderName(folderName);
	}

	public CompanyFormFolder getParentFolder() {
		return bean.getParentFolder();
	}

	public void setParentFolder(CompanyFormFolder parentFolder) {
		bean.setParentFolder(parentFolder);
	}

	public BCompanyFormFolder[] listChildFolders() {
		return makeArray(ArahantSession.getHSU().createCriteria(CompanyFormFolder.class).eq(CompanyFormFolder.PARENT, bean).orderBy(CompanyFormFolder.FOLDER_NAME).list());
	}

	static BCompanyFormFolder[] makeArray(List<CompanyFormFolder> l) {
		BCompanyFormFolder ret[] = new BCompanyFormFolder[l.size()];
		for (int loop = 0; loop < ret.length; loop++) {
			ret[loop] = new BCompanyFormFolder(l.get(loop));
		}
		return ret;
	}

	public BCompanyForm[] listChildForms() {

		return makeFormArray(ArahantSession.getHSU().createCriteria(CompanyForm.class)
				.orderBy(CompanyForm.COMMENTS)
				.joinTo(CompanyForm.FOLDERS)
				.eq(CompanyFormFolder.ID, bean.getFolderId())
				.list());
	}

	static BCompanyForm[] makeFormArray(List<CompanyForm> l) {
		BCompanyForm ret[] = new BCompanyForm[l.size()];
		for (int loop = 0; loop < ret.length; loop++) {
			ret[loop] = new BCompanyForm(l.get(loop));
		}
		return ret;
	}

	public BCompanyFormFolder[] listParentFolders() {

		return makeArray(ArahantSession.getHSU().createCriteria(CompanyFormFolder.class).isNull(CompanyFormFolder.PARENT).orderBy(CompanyFormFolder.FOLDER_NAME).list());
	}

	/**
	 * this method returns all the folders the current person is allowed to see
	 * @param folderId the id of parent folder, send in null if you want the top level
	 * @return an array of form folders they are allowed to see
	 */
	public static BCompanyFormFolder[] listFoldersForCurrentPerson(String folderId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		//if I'm a form manager, just return them all
		if (BRight.checkRight("CompanyFormManager") == ACCESS_LEVEL_WRITE) {
			if (isEmpty(folderId)) {
				return BCompanyFormFolder.makeArray(hsu.createCriteria(CompanyFormFolder.class).orderBy(CompanyFormFolder.FOLDER_NAME).isNull(CompanyFormFolder.PARENT).list());
			} else {
				return BCompanyFormFolder.makeArray(hsu.createCriteria(CompanyFormFolder.class).orderBy(CompanyFormFolder.FOLDER_NAME).joinTo(CompanyFormFolder.PARENT).eq(CompanyFormFolder.ID, folderId).list());
			}
		}


		//what are all of the org groups above me
		List<String> orgids = BOrgGroup.getAllOrgGroupParents((List) hsu.createCriteria(OrgGroup.class).selectFields(OrgGroup.ORGGROUPID).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson()).list());


		HibernateCriteriaUtil<CompanyFormFolder> hcu = hsu.createCriteria(CompanyFormFolder.class);

		if (isEmpty(folderId)) {
			hcu.isNull(CompanyFormFolder.PARENT);
		} else {
			hcu.joinTo(CompanyFormFolder.PARENT).eq(CompanyFormFolder.ID, folderId);
		}

		hcu.joinTo(CompanyFormFolder.ALLOWED_GROUPS).in(OrgGroup.ORGGROUPID, orgids);

		List<CompanyFormFolder> l = hcu.list();

		hcu = hsu.createCriteria(CompanyFormFolder.class).sizeEq(CompanyFormFolder.ALLOWED_GROUPS, 0);

		if (isEmpty(folderId)) {
			hcu.isNull(CompanyFormFolder.PARENT);
		} else {
			hcu.joinTo(CompanyFormFolder.PARENT).eq(CompanyFormFolder.ID, folderId);
		}

		l.addAll(hcu.list());

		List<String> ids = new LinkedList<String>();
		for (CompanyFormFolder cf : l) {
			ids.add(cf.getFolderId());
		}

		//add any folders that have children that are acceptable
		hcu = hsu.createCriteria(CompanyFormFolder.class).notIn(CompanyFormFolder.ID, ids);

		if (isEmpty(folderId)) {
			hcu.isNull(CompanyFormFolder.PARENT);
		} else {
			hcu.joinTo(CompanyFormFolder.PARENT).eq(CompanyFormFolder.ID, folderId);
		}

		for (CompanyFormFolder cff : hcu.list()) {
			if (listFoldersForCurrentPerson(cff.getFolderId()).length > 0) {
				l.add(cff);
			}
		}

		HashSet<CompanyFormFolder> set = new HashSet<CompanyFormFolder>();
		set.addAll(l);
		l.clear();
		l.addAll(set);

		Collections.sort(l);

		return BCompanyFormFolder.makeArray(l);

	}

	public BOrgGroup[] listOrgGroups() {
		return BOrgGroup.makeArray(ArahantSession.getHSU().createCriteriaNoCompanyFilter(OrgGroup.class).orderBy(OrgGroup.NAME).joinTo(OrgGroup.FOLDERS).eq(CompanyFormFolder.ID, bean.getFolderId()).list());
	}


	public static String findOrMake(String name)
	{
		CompanyFormFolder cff = ArahantSession.getHSU().createCriteria(CompanyFormFolder.class).eq(CompanyFormFolder.FOLDER_NAME, name).first();
		if(cff == null)
		{
			BCompanyFormFolder bcff = new BCompanyFormFolder();
			String id = bcff.create();
			bcff.setFolderName(name);
			bcff.insert();
			return id;
		}
		else
		{
			return cff.getFolderId();
		}
	}

}
