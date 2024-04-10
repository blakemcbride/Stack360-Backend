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
import com.arahant.business.BPersonNote;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class ListPersonNotesItem {


	public ListPersonNotesItem() {
		super();
	}
	
	/**
	 * @param note2
	 */
	ListPersonNotesItem(final BPersonNote n) {
		noteCategoryId=n.getNoteCategoryId();
		noteId=n.getNoteId();
		note=n.getNote();
		noteCategoryName=n.getNoteCategoryName();
		
		if (note.length()>100)
			note=note.substring(0,100);
	}


	private String noteCategoryName;
	private String noteId;
	private String note;
	private String noteCategoryId;

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
	/**
	 * @return Returns the noteCategoryName.
	 */
	public String getNoteCategoryName() {
		return noteCategoryName;
	}
	/**
	 * @param noteCategoryName The noteCategoryName to set.
	 */
	public void setNoteCategoryName(final String noteCategoryName) {
		this.noteCategoryName = noteCategoryName;
	}
	/**
	 * @return Returns the noteId.
	 */
	public String getNoteId() {
		return noteId;
	}
	/**
	 * @param noteId The noteId to set.
	 */
	public void setNoteId(final String noteId) {
		this.noteId = noteId;
	}
}

	
