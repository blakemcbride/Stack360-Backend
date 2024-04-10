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
import javax.persistence.FetchType;
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
@Table (name=CompanyForm.TABLE_NAME)
public class CompanyForm extends Setup implements Comparable<CompanyForm>, Serializable {

	public static final String TABLE_NAME="company_form";
	public static final String COMPANY_FORM_ID = "companyformId";
	public static final String DATE = "formDate";
	public static final String FORM_TYPE = "formType";
	public static final String PEOPLE_WITH_ACCESS="peopleWithAccess";
	public static final String ACCESS_TYPE="accessType";
	public static final String SOURCE = "source";
	public static final short ACCESS_TYPE_ALL=0;
	public static final short ACCESS_TYPE_EMPLOYEE_LIST=1;

	private String companyFormId;

	private String comments;
        public static String COMMENTS = "comments";
	private String source;
	private byte[] image;
	private String fileNameExtension="pdf";  //default to pdf for old code

	private FormType formType;

	private HashSet<Person> peopleWithAccess=new HashSet<Person>();
	private Set<CompanyFormFolder> folders=new HashSet<CompanyFormFolder>();
    public static final String FOLDERS = "folders";

	private char electronicSignature='N';



	@Override
	@Column (name="last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	@Override
	@ManyToOne
	@JoinColumn(name="company_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "company_form_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return companyFormId = IDGenerator.generate(this);
	}



	/**
	 * @return Returns the comments.
	 */
	@Column (name="comments")
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments The comments to set.
	 */
	public void setComments(final String comments) {
		this.comments = comments;
	}




	/**
	 * @return Returns the source.
	 */
	@Column (name="source")
	public String getSource() {
		return source;
	}

	/**
	 * @param source The source to set.
	 */
	public void setSource(final String source) {
		this.source = source;
	}

	/**
	 * @return Returns the formType.
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="form_type_id")
	public FormType getFormType() {
		return formType;
	}

	/**
	 * @param formType The formType to set.
	 */
	public void setFormType(final FormType formType) {
		this.formType = formType;
	}



	/**
	 * @return Returns the image.
	 */
	@Column (name="form")
	public byte[] getImage() {
		return image;
	}

	/**
	 * @param image The image to set.
	 */
	public void setImage(final byte[] image) {
		this.image = image;
	}

	@Column (name="file_name_extension")
	public String getFileNameExtension() {
		return fileNameExtension;
	}

	public void setFileNameExtension(String fileNameExtension) {
		if (fileNameExtension!=null)
			fileNameExtension=fileNameExtension.toLowerCase();
		this.fileNameExtension = fileNameExtension;
	}

        /*
	public HashSet<Person> getPeopleWithAccess() {
		return peopleWithAccess;
	}

	public void setPeopleWithAccess(HashSet<Person> peopleWithAccess) {
		this.peopleWithAccess = peopleWithAccess;
	}

	public short getAccessType() {
		return accessType;
	}

	public void setAccessType(short accessType) {
		this.accessType = accessType;
	}
*/

	@Override
	public int compareTo(CompanyForm o) {
		if (o==null)
			return -1;
		return o.firstActiveDate-firstActiveDate;
	}

	@Override
	@Column(name="first_active_date")
	public int getFirstActiveDate() {
		return firstActiveDate;
	}

	@Id
	@Column(name="company_form_id")
	public String getCompanyFormId() {
		return companyFormId;
	}

	public void setCompanyFormId(String companyFormId) {
		this.companyFormId = companyFormId;
	}

	@ManyToMany
	@JoinTable(name = "company_form_folder_join",
        joinColumns = {@JoinColumn(name = "form_id")},
        inverseJoinColumns = {@JoinColumn(name = "folder_id")})
	public Set<CompanyFormFolder> getFolders() {
		return folders;
	}

	public void setFolders(Set<CompanyFormFolder> folders) {
		this.folders = folders;
	}

	@Column(name="electronic_signature")
	public char getElectronicSignature() {
		return electronicSignature;
	}

	public void setElectronicSignature(char electronicSignature) {
		this.electronicSignature = electronicSignature;
	}


}
