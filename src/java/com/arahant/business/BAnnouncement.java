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
 *
 * Created on Jan 28, 2008
 */
package com.arahant.business;

import com.arahant.utils.StandardProperty;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.standard.misc.announcement.ListAnnouncementsReturnItem;

public class BAnnouncement extends BusinessLogicBase implements IDBFunctions {

	public BAnnouncement() {
	}

	public BAnnouncement(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public String create() throws ArahantException {
		//announcement=new Announcement();
		//announcement.setAnnouncementId(IDGenerator.generate(announcement));

		//return getAnnouncementId();
		return "";
	}

	@Override
	public void delete() throws ArahantDeleteException {
		//hsu.delete(announcement);
	}

	@Override
	public void insert() throws ArahantException {
		//hsu.save(announcement);
	}

	private void internalLoad(String key) {
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		//hsu.saveOrUpdate(announcement);		
	}

	public String getAnnouncementId() {
		return null;
	}

	public void setFinalDate(int finalDate) {
	}

	public int getFinalDate() {
		return 0;
	}

	public void setMessage(String message) {
	}

	public String getMessage() {
		return null;
	}

	public void setStartDate(int startDate) {
	}

	public int getStartDate() {
		return 0;
	}

	public void setOrgGroupId(String orgGroupId) {
	}

	public String getReport(String orgGroupId) {
		return "";
	}

	public static void delete(String[] announcementIds) {
		for (String announcementId : announcementIds)
			new BAnnouncement(announcementId).delete();
	}

	public static void moveDown(String announcementId) {
	}

	public static void moveUp(String announcementId) {
	}

	public static ListAnnouncementsReturnItem[] list() {

		return null;
	}

	public static String[] getAnnouncements() {
		BProperty bp = new BProperty(StandardProperty.ANNOUNCEMENT);
		return new String[]{bp.getValue()};
	}
}
