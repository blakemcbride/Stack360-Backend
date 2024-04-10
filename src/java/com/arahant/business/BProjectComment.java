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

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BProjectComment extends BusinessLogicBase implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BProjectComment.class);

	ProjectComment projectComment;

	public static void delete(String[] ids) {
		for (String id : ids)
			new BProjectComment(id).delete();
	}

	public BProjectComment(final ProjectComment comment) {
		projectComment = comment;
	}

	public BProjectComment(final String commentId) throws ArahantException {
		internalLoad(commentId);
	}

	public BProjectComment() {
	}

	@Override
	public String create() throws ArahantException {
		projectComment = new ProjectComment();
		projectComment.generateId();
		projectComment.setDateEntered(new java.util.Date());
		return getCommentId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		if (BRight.checkRight("OldProjectComments") != ACCESS_LEVEL_WRITE) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(projectComment.getDateEntered());
			final Calendar cal2 = Calendar.getInstance();

			cal2.add(Calendar.DAY_OF_YEAR, -1);

			if (cal2.after(cal))
				throw new ArahantException("You do not have permission to delete old comments.");
		}
		try {
			ArahantSession.getHSU().delete(projectComment);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	@Override
	public void insert() throws ArahantException {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.insert(projectComment);

		//send out comment added notification

		if (BProperty.getBoolean("ProjectNotifications")) {
			List<Person> l = hsu.createCriteria(Person.class).joinTo(Person.PROJECT_PERSON_JOIN).joinTo(ProjectEmployeeJoin.PROJECTSHIFT).eq(ProjectShift.PROJECT, projectComment.getProjectShift().getProject()).list();

			for (Person p : l) {
				if (p.equals(projectComment.getPerson()))
					continue;
				if (projectComment.getInternal() == 'Y' && p.isClient())
					continue;
				BMessage.send(projectComment.getPerson(), p, "Comment entered for project " + projectComment.getProjectShift().getProject().getProjectName(), projectComment.getCommentTxt());
			}
		}

	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		projectComment = ArahantSession.getHSU().get(ProjectComment.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public void update() throws ArahantException {
		if (BRight.checkRight("OldProjectComments") != ACCESS_LEVEL_WRITE) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(projectComment.getDateEntered());
			final Calendar cal2 = Calendar.getInstance();

			cal2.add(Calendar.DAY_OF_YEAR, -1);

			if (cal2.after(cal))
				throw new ArahantException("You do not have permission to modify old comments.");
		}
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.saveOrUpdate(projectComment);

		//send out comment changed notification
		if (BProperty.getBoolean("CommentMessages")) {
			List<Person> l = hsu.createCriteria(Person.class).joinTo(Person.PROJECT_PERSON_JOIN).joinTo(ProjectEmployeeJoin.PROJECTSHIFT).eq(ProjectShift.PROJECT, projectComment.getProjectShift().getProject()).list();

			for (Person p : l) {
				if (p.equals(projectComment.getPerson()))
					continue;
				if (projectComment.getInternal() == 'Y' && p.isClient())
					continue;

				BMessage.send(projectComment.getPerson(), p, "Comment changed for project " + projectComment.getProjectShift().getProject().getProjectName(), projectComment.getCommentTxt());
			}
		}
	}

	public void debug(final Object o) {
		logger.debug(o);
	}

	public void error(final Throwable e) {
		logger.error(e);
	}

	public void error(final String str, final Throwable e) {
		logger.error(str, e);
	}

	public void info(final Object o) {
		logger.info(o);
	}

	@Override
	public boolean equals(final Object obj) {
		return logger.equals(obj);
	}

	@Override
	public int hashCode() {
		return logger.hashCode();
	}

	@Override
	public String toString() {
		return logger.toString();
	}

	public String getCommentId() {
		return projectComment.getCommentId();
	}

	public String getCommentTxt() {
		return projectComment.getCommentTxt();
	}

	public Date getDateEntered() {
		return projectComment.getDateEntered();
	}

	public char getInternal() {
		return projectComment.getInternal();
	}

	public String getProjectId() {
		return projectComment.getProjectShift().getProject().getProjectId();
	}

	public void setCommentId(final String commentId) {
		projectComment.setCommentId(commentId);
	}

	public void setCommentTxt(final String commentTxt) {
		projectComment.setCommentTxt(commentTxt);
	}

	public void setDateEntered(final Date dateEntered) {
		projectComment.setDateEntered(dateEntered);
	}

	public void setInternal(final char internal) {
		projectComment.setInternal(internal);
	}

	public void setProjectId(final String projectId) {
		if (true) throw new ArahantException("XXYY");
		projectComment.getProjectShift().setProject(ArahantSession.getHSU().get(Project.class, projectId));
	}

	public void setProjectShiftId(final String shiftId) {
		projectComment.setProjectShift(new BProjectShift(shiftId).getProjectShift());
	}

	public String getProjectShiftId() {
		return projectComment.getProjectShift().getProjectShiftId();
	}

	public void setPerson(final BPerson current) {
		projectComment.setPerson(current.person);
	}

	public String getPersonId() {
		return projectComment.getPerson().getPersonId();
	}

	public String getLastName() {
		return projectComment.getPerson().getLname();
	}

	public String getFirstName() {
		return projectComment.getPerson().getFname();
	}

	static BProjectComment[] makeArray(final List<ProjectComment> ptl) {
		final BProjectComment[] ptt = new BProjectComment[ptl.size()];

		final Iterator<ProjectComment> ptlItr = ptl.iterator();

		for (int loop = 0; loop < ptt.length; loop++)
			ptt[loop] = new BProjectComment(ptlItr.next());

		return ptt;
	}

	public String getEmployeeId() {
		return projectComment.getPerson().getPersonId();
	}
}
