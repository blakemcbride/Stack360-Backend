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


package com.arahant.business;

import com.arahant.beans.HrNoteCategory;
import com.arahant.beans.Person;
import com.arahant.beans.PersonNote;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.HRNoteReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;

public class BPersonNote extends BusinessLogicBase implements IDBFunctions {

	private PersonNote hrEmployeeNote;

	public BPersonNote() {
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BPersonNote(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param note
	 */
	public BPersonNote(final PersonNote note) {
		hrEmployeeNote = note;
	}

	public void stub() {
		hrEmployeeNote = new PersonNote();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		hrEmployeeNote = new PersonNote();
		hrEmployeeNote.generateId();
		return hrEmployeeNote.getNoteId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(hrEmployeeNote);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(hrEmployeeNote);
	}

	private void internalLoad(final String key) throws ArahantException {
		hrEmployeeNote = ArahantSession.getHSU().get(PersonNote.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(hrEmployeeNote);
	}

	/**
	 * @return @see com.arahant.beans.PersonNote#getNote()
	 */
	public String getNote() {
		return hrEmployeeNote.getNote();
	}

	/**
	 * @return @see com.arahant.beans.PersonNote#getNoteId()
	 */
	public String getNoteId() {
		return hrEmployeeNote.getNoteId();
	}

	/**
	 * @param note
	 * @see com.arahant.beans.PersonNote#setNote(java.lang.String)
	 */
	public void setNote(final String note) {
		hrEmployeeNote.setNote(note);
	}

	/**
	 * @param noteId
	 * @see com.arahant.beans.PersonNote#setNoteId(java.lang.String)
	 */
	public void setNoteId(final String noteId) {
		hrEmployeeNote.setNoteId(noteId);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 * @throws ArahantDeleteException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantDeleteException, ArahantException {
		for (final String element : ids)
			new BPersonNote(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static String getReport(final HibernateSessionUtil hsu, final String personId, final boolean showAsDependent) throws Exception {
		final File fyle = FileSystemUtils.createTempFile("HRNoteRept", ".pdf");

		new HRNoteReport().build(hsu, fyle, new BPerson(personId).listNotes(), personId, showAsDependent);

		return FileSystemUtils.getHTTPPath(fyle);
	}

	/**
	 * @param personId
	 */
	public void setPersonId(final String personId) {
		hrEmployeeNote.setPerson(ArahantSession.getHSU().get(Person.class, personId));
	}

	/**
	 * @param catId
	 */
	public void setNoteCategoryId(final String catId) {
		hrEmployeeNote.setHrNoteCategory(ArahantSession.getHSU().get(HrNoteCategory.class, catId));
	}

	/**
	 * @return
	 */
	public String getPersonId() {

		return hrEmployeeNote.getPerson().getPersonId();
	}

	/**
	 * @return
	 */
	public String getNoteCategoryId() {

		if (hrEmployeeNote.getHrNoteCategory() != null)
			return hrEmployeeNote.getHrNoteCategory().getCatId();
		return "";
	}

	/**
	 * @return
	 */
	public String getNoteCategoryName() {
		if (hrEmployeeNote.getHrNoteCategory() != null)
			return hrEmployeeNote.getHrNoteCategory().getName();
		return "";
	}
}
