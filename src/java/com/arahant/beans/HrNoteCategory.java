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
import java.util.HashSet;
import java.util.Set;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;


@Entity
@Table(name = HrNoteCategory.TABLE_NAME)
public class HrNoteCategory extends Setup implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "hr_note_category";
    // Fields
    private String catId;
    public static final String CATID = "catId";
    private String name;
    public static final String NAME = "name";
    private Set<PersonNote> personNotes = new HashSet<PersonNote>(0);
    public static final String PERSON_NOTES = "personNotes";
    private String catCode;
    public static final String CATCODE = "catCode";

    // Constructors
    /** default constructor */
    public HrNoteCategory() {
    }

    // Property accessors
    @Id
    @Column(name = "cat_id")
    public String getCatId() {
        return this.catId;
    }

    public void setCatId(final String catId) {
        this.catId = catId;
    }

    @Column(name = "name")
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = PersonNote.HRNOTECATEGORY, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<PersonNote> getPersonNotes() {
        return this.personNotes;
    }

    public void setPersonNotes(final Set<PersonNote> personNotes) {
        this.personNotes = personNotes;
    }

    @Column(name = "cat_code")
    public String getCatCode() {
        return catCode;
    }

    public void setCatCode(String catCode) {
        this.catCode = catCode;
    }

    @Override
    public String keyColumn() {
        return "cat_id";
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String generateId() throws ArahantException {
        setCatId(IDGenerator.generate(this));
        return catId;
    }

    @Column(name = "last_active_date")
    @Override
    public int getLastActiveDate() {
        return lastActiveDate;
    }

    @Column(name = "first_active_date")
    @Override
    public int getFirstActiveDate() {
        return firstActiveDate;
    }

    @ManyToOne
    @JoinColumn(name = "org_group_id")
    @Override
    public OrgGroup getOrgGroup() {
        return orgGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (catId == null && o == null)
            return true;
        if (catId != null && o instanceof HrNoteCategory)
            return catId.equals(((HrNoteCategory) o).getCatId());
        return false;
    }

    @Override
    public int hashCode() {
        if (catId == null)
            return 0;
        return catId.hashCode();
    }
}
