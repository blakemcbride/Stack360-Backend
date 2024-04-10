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
 * Created on Jul 10, 2007
 */

package com.arahant.beans;

import javax.persistence.*;
import java.io.Serializable;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import static javax.persistence.FetchType.LAZY;


@Entity
@Table(name="person_form")
public class PersonForm extends ArahantBean implements Serializable, Comparable<PersonForm> {

	private String personFormId;

	private int formDate;
	private Person person;
	private String personId;
	private String comments;
	private String source;
	private String fileNameExtension = "pdf";  //default to pdf for old code
	private char electronicallySigned = 'N';
	private FormType formType;

	private static final long serialVersionUID = -8421569462508477734L;
	public static final String PERSON = "person";
	public static final String PERSON_ID = "personId";
	public static final String DATE = "formDate";
	public static final String FORM_TYPE = "formType";
	public static final String FORM_ID = "personFormId";
	public static final String COMMENTS = "comments";
	public static final String EXTENSION = "fileNameExtension";

	public PersonForm() {
	}

	@Override
	public String generateId() throws ArahantException {
		personFormId = IDGenerator.generate(this);
		return personFormId;
	}

	@Override
	public String keyColumn() {
		return "person_form_id";
	}

	@Override
	public String tableName() {
		return "person_form";
	}

	/**
	 * @return Returns the comments.
	 */
	@Column(name = "comments")
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
	 * @return Returns the formDate.
	 */
	@Column(name = "form_date")
	public int getFormDate() {
		return formDate;
	}

	/**
	 * @param formDate The formDate to set.
	 */
	public void setFormDate(final int formDate) {
		this.formDate = formDate;
	}

	/**
	 * @return Returns the personFormId.
	 */
	@Id
	@Column(name = "person_form_id")
	public String getPersonFormId() {
		return personFormId;
	}

	/**
	 * @param personFormId The personFormId to set.
	 */
	public void setPersonFormId(final String personFormId) {
		this.personFormId = personFormId;
	}

	/**
	 * @return Returns the person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person The person to set.
	 */
	public void setPerson(final Person person) {
		this.person = person;
	}

	@Column(name = "person_id", insertable = false, updatable = false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	/**
	 * @return Returns the source.
	 */
	@Column(name = "source")
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "form_type_id")
	public FormType getFormType() {
		return formType;
	}

	/**
	 * @param formType The formType to set.
	 */
	public void setFormType(final FormType formType) {
		this.formType = formType;
	}

	@Column(name = "electronically_signed")
	public char getElectronicallySigned() {
		return electronicallySigned;
	}

	public void setElectronicallySigned(char electronicallySigned) {
		this.electronicallySigned = electronicallySigned;
	}

	@Override
	public int compareTo(final PersonForm o) {

		return o.getFormDate() - getFormDate();
	/*	int ret=formType.getFormCode().compareTo(o.formType.getFormCode());
		
		if (ret==0)
			ret=formDate-o.formDate;
		
		return ret;
		*/
	}

	@Column(name = "file_name_extension")
	public String getFileNameExtension() {
		return fileNameExtension;
	}

	public void setFileNameExtension(String fileNameExtension) {
		if (fileNameExtension != null)
			fileNameExtension = fileNameExtension.toLowerCase();
		this.fileNameExtension = fileNameExtension;
	}

	@Override
	public boolean equals(Object o) {
		if (personFormId == null && o == null)
			return true;
		if (personFormId != null && o instanceof PersonForm)
			return personFormId.equals(((PersonForm) o).getPersonFormId());

		return false;
	}

	@Override
	public int hashCode() {
		if (personFormId == null)
			return 0;
		return personFormId.hashCode();
	}
}

	
