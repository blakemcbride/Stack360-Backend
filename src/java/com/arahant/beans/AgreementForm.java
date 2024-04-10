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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = AgreementForm.TABLE_NAME)
public class AgreementForm extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "agreement_form";
	public static final String NAME = "name";
	public static final String DATE = "formDate";
	public static final String EXPIRE_DATE = "expirationDate";
	private String agreementFormId;
	private Date formDate;
	private String summary;
	private byte[] image;
	private String name;
	private String fileNameExtension;
	private int expirationDate;

	public AgreementForm() { }

	@Column(name = "expiration_date")
	public int getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(int expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Column(name = "agreement_form_id")
	@Id
	public String getAgreementFormId() {
		return agreementFormId;
	}

	public void setAgreementFormId(String agreementFormId) {
		this.agreementFormId = agreementFormId;
	}

	@Column(name = "file_name_ext")
	public String getFileNameExtension() {
		return fileNameExtension;
	}

	public void setFileNameExtension(String fileNameExtension) {
		this.fileNameExtension = fileNameExtension;
	}

	@Column(name = "form_date")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getFormDate() {
		return formDate;
	}

	public void setFormDate(Date formDate) {
		this.formDate = formDate;
	}

	@Column(name = "form")
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	@Column(name = "description")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "summary")
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "agreement_form_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return agreementFormId = IDGenerator.generate(this);
	}
}
