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


/**
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrNote;
import com.arahant.annotation.Validation;
import com.arahant.business.BPersonNote;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class SaveNoteInput extends TransmitInputBase {

	@Validation (required=true)
	private String personId;

	@Validation (required=true)
	private String noteCategoryId;

	@Validation (min=1,max=4000,table="person_note",column="note",required=false)
	private String note;
	
	@Validation (required=true)
	private String noteId;

	/**
	 * @return Returns the employeeNoteId.
	 */
	public String getNoteId() {
		return noteId;
	}

	/**
	 * @param employeeNoteId The employeeNoteId to set.
	 */
	public void setNoteId(final String employeeNoteId) {
		this.noteId = employeeNoteId;
	}

	/**
	 * @return Returns the employeeId.
	 */
	public String getPersonId() {
		return personId;
	}

	/**
	 * @param employeeId The employeeId to set.
	 */
	public void setPersonId(final String employeeId) {
		this.personId = employeeId;
	}

	/**
	 * @return Returns the note.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note The note to set.
	 */
	public void setNote(final String note) {
		this.note = note;
	}

	/**
	 * @return Returns the noteCategoryId.
	 */
	public String getNoteCategoryId() {
		return noteCategoryId;
	}

	/**
	 * @param noteCategoryId The noteCategoryId to set.
	 */
	public void setNoteCategoryId(final String noteCategoryId) {
		this.noteCategoryId = noteCategoryId;
	}

	public SaveNoteInput() {
		super();
	}

	/**
	 * @param n
	 */
	void setData(final BPersonNote n) {
		n.setNote(note);
		n.setPersonId(personId);
		n.setNoteCategoryId(noteCategoryId);
	}
}

	
