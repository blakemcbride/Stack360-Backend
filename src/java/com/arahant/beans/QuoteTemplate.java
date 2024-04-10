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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = QuoteTemplate.TABLE_NAME)
public class QuoteTemplate extends ArahantBean implements java.io.Serializable {

	public static final String TABLE_NAME = "quote_template";
	public static final String NAME = "templateName";
	public static final String DESCRIPTION = "templateDescription";
	public static final String ID = "quoteTemplateId";
	private static final long serialVersionUID = 1L;
	private String quoteTemplateId;
	private String templateName;
	private String templateDescription;

	public QuoteTemplate() {}

	@Id
	@Column(name = "quote_template_id")
	public String getQuoteTemplateId() {
		return quoteTemplateId;
	}

	public void setQuoteTemplateId(String quoteTemplateId) {
		this.quoteTemplateId = quoteTemplateId;
	}

	@Column(name = "template_description")
	public String getTemplateDescription() {
		return templateDescription;
	}

	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}

	@Column(name = "template_name")
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "quote_template_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setQuoteTemplateId(IDGenerator.generate(this));
		return getQuoteTemplateId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final QuoteTemplate other = (QuoteTemplate) obj;
		if ((this.quoteTemplateId == null) ? (other.quoteTemplateId != null) : !this.quoteTemplateId.equals(other.quoteTemplateId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 53 * hash + (this.quoteTemplateId != null ? this.quoteTemplateId.hashCode() : 0);
		return hash;
	}
}
