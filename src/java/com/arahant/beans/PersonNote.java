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
import org.hibernate.annotations.Fetch;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;



@Entity
@Table(name=PersonNote.TABLE_NAME)
public class PersonNote extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "person_note";

	// Fields    

	private String noteId;

	public static final String NOTEID = "noteId";

	private Person person;

	public static final String PERSON = "person";

	private HrNoteCategory hrNoteCategory;

	public static final String HRNOTECATEGORY = "hrNoteCategory";

	private String note;

	public static final String NOTE = "note";

	// Constructors

	/** default constructor */
	public PersonNote() {
	}

	/** minimal constructor */
	public PersonNote(final String noteId, final Employee employee, final HrNoteCategory hrNoteCategory) {
		this.noteId = noteId;
		this.person = employee;
		this.hrNoteCategory = hrNoteCategory;
	}

	/** full constructor */
	public PersonNote(final String noteId, final Employee employee, final HrNoteCategory hrNoteCategory, final String note) {
		this.noteId = noteId;
		this.person = employee;
		this.hrNoteCategory = hrNoteCategory;
		this.note = note;
	}

	// Property accessors
	@Id
	@Column (name="note_id")
	public String getNoteId() {
		return this.noteId;
	}

	public void setNoteId(final String noteId) {
		this.noteId = noteId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="person_id")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(final Person person) {
		this.person = person;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="cat_id")
	public HrNoteCategory getHrNoteCategory() {
		return this.hrNoteCategory;
	}

	public void setHrNoteCategory(final HrNoteCategory hrNoteCategory) {
		this.hrNoteCategory = hrNoteCategory;
	}

	@Column (name="note")
	public String getNote() {
		return this.note;
	}

	public void setNote(final String note) {
		this.note = note;
	}
	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	public String keyColumn() {
		
		return "note_id";
	}

	public String tableName() {
		
		return TABLE_NAME;
	}

	public String generateId() throws ArahantException {
		setNoteId(IDGenerator.generate(this));
		return noteId;
	}
	

	public boolean equals(Object o)
	{
		if (noteId==null && o==null)
			return true;
		if (noteId!=null && o instanceof PersonNote)
			return noteId.equals(((PersonNote)o).getNoteId());
		
		return false;
	}
	
	public int hashCode()
	{
		if (noteId==null)
			return 0;
		return noteId.hashCode();
	}
}
