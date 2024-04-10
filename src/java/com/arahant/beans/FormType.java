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

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

@Entity
@Table(name = FormType.TABLE_NAME)
public class FormType extends Filtered implements Serializable {

    public static final String TABLE_NAME = "form_type";

    private static final long serialVersionUID = -6968717398890055870L;
    public static final String FORM_CODE = "formCode";
    public static final String TYPE = "formType";
    public static final String ID = "formTypeId";
	public static final char TYPE_PERSON='E';
	public static final char TYPE_PROJECT='P';
	public static final char TYPE_BOTH='B';
	public static final String FIELD_DOWNLOADABLE = "fieldDownloadable";
	
    private String formTypeId;
    private String formCode;
    private String description;
    private char formType; // P = Project, E = Person, B = Both
    private Set<PersonForm> personForms = new HashSet<PersonForm>(0);
    private Set<ProjectForm> projectForms = new HashSet<ProjectForm>(0);
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
	private int lastActiveDate;
	private char fieldDownloadable = 'N';
    private char internal = 'N';

    /**
     * @param form_type_id The form_type_id to set.
     */
    public void setFormTypeId(final String form_type_id) {
        this.formTypeId = form_type_id;
    }

    public FormType() {
    }

    @Override
    public String generateId() throws ArahantException {
        formTypeId = IDGenerator.generate(this);
        return formTypeId;
    }

    @Override
    public String keyColumn() {

        return "form_type_id";
    }

    @Override
    public String tableName() {

        return "form_type";
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        firePropertyChange("description", this.description, description);
        this.description = description;
    }

    /**
     * @return Returns the formCode.
     */
    @Column(name = "form_code")
    public String getFormCode() {
        return formCode;
    }

    /**
     * @param formCode The formCode to set.
     */
    public void setFormCode(String formCode) {
        firePropertyChange("formCode", this.formCode, formCode);
        this.formCode = formCode;
    }

    /**
     * @return Returns the formType.
     */
    @Column(name = "form_type")
    public char getFormType() {
        return formType;
    }

    /**
     * @param formType The formType to set.
     */
    public void setFormType(char formType) {
        firePropertyChange("formType", this.formType, formType);
        this.formType = formType;
    }

    @Column(name="last_active_date")
    public int getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(int lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    @Column(name="field_downloadable")
    public char getFieldDownloadable() {
        return fieldDownloadable;
    }

    public void setFieldDownloadable(char fieldDownloadable) {
        this.fieldDownloadable = fieldDownloadable;
    }

    @Column(name="internal")
    public char getInternal() {
        return internal;
    }

    public void setInternal(char internal) {
        this.internal = internal;
    }

    /**
     * @return Returns the personForms.
     */
    @OneToMany(mappedBy = PersonForm.FORM_TYPE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<PersonForm> getPersonForms() {
        return personForms;
    }

    /**
     * @param personForms The personForms to set.
     */
    public void setPersonForms(Set<PersonForm> personForms) {
        this.personForms = personForms;
    }

    /**
     * @return Returns the projectForms.
     */
    @OneToMany(mappedBy = ProjectForm.FORM_TYPE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ProjectForm> getProjectForms() {
        return projectForms;
    }

    /**
     * @param projectForms The projectForms to set.
     */
    public void setProjectForms(Set<ProjectForm> projectForms) {
        this.projectForms = projectForms;
    }

    /**
     * @return Returns the formTypeId.
     */
    @Id
    @Column(name = "form_type_id")
    public String getFormTypeId() {
        return formTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (formTypeId == null && o == null)
            return true;
        if (formTypeId != null && o instanceof FormType)
            return formTypeId.equals(((FormType) o).getFormTypeId());
        return false;
    }

    @Override
    public int hashCode() {
        if (formTypeId == null)
            return 0;
        return formTypeId.hashCode();
    }

	@Override
	@ManyToOne
	@JoinColumn(name="org_group_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}
}


	
