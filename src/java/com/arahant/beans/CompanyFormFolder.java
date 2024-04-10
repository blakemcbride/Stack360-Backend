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

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name=CompanyFormFolder.TABLE_NAME)
public class CompanyFormFolder extends Filtered implements Serializable, Comparable<CompanyFormFolder> {

	public static final String TABLE_NAME="company_form_folder";

	public static final String ID="folderId";
	public static final String PARENT="parentFolder";
	public static final String ALLOWED_GROUPS="allowedGroups";

	private String folderId;
	private String folderName;
        public static final String FOLDER_NAME = "folderName";
	private CompanyFormFolder parentFolder;
	private Set<CompanyForm> forms=new HashSet<CompanyForm>();
        private Set<OrgGroup> allowedGroups=new HashSet<OrgGroup>();

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "folder_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return folderId=IDGenerator.generate(this);
	}

	@Column(name="folder_name")
	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	@Id
	@Column(name="folder_id")
	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	@Override
	@ManyToOne
	@JoinColumn(name="company_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	@ManyToOne
	@JoinColumn(name="parent_folder_id")
	public CompanyFormFolder getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(CompanyFormFolder parentFolder) {
		this.parentFolder = parentFolder;
	}

	
	@ManyToMany
	@JoinTable(name = "company_form_folder_join",
        joinColumns = {@JoinColumn(name = "folder_id")},
        inverseJoinColumns = {@JoinColumn(name = "form_id")})
	public Set<CompanyForm> getForms() {
		return forms;
	}

	public void setForms(Set<CompanyForm> forms) {
		this.forms = forms;
	}

        @ManyToMany
	@JoinTable(name = "company_form_org_join",
        joinColumns = {@JoinColumn(name = "folder_id")},
        inverseJoinColumns = {@JoinColumn(name = "org_group_id")})
	public Set<OrgGroup> getAllowedGroups() {
		return allowedGroups;
	}

	public void setAllowedGroups(Set<OrgGroup> allowedGroups) {
		this.allowedGroups = allowedGroups;
	}

	@Override
	public int compareTo(CompanyFormFolder o) {
		return folderName.compareTo(o.folderName);
	}

}
