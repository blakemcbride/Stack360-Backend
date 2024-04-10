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

import com.arahant.beans.Holiday;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.util.Calendar;
import java.util.List;

public class BHoliday extends SimpleBusinessObjectBase<Holiday> implements IDBFunctions {

	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final transient ArahantLogger logger = new ArahantLogger(BHoliday.class);

	/**
	 */
	public BHoliday() {
	}

	/**
	 * @param holiday
	 */
	public BHoliday(final Holiday holiday) {
		bean = holiday;
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BHoliday(final String string) throws ArahantException {
		super(string);
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean = new Holiday();
		return bean.generateId();
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#load(java.lang.String)
	 */
	@Override
	public void load(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(Holiday.class, key);
	}

	/**
	 * @return @see com.arahant.beans.Holiday#getDescription()
	 */
	public String getDescription() {
		return bean.getDescription();
	}

	/**
	 * @return @see com.arahant.beans.Holiday#getHdate()
	 */
	public int getHdate() {
		return bean.getHdate();
	}

	/**
	 * @return @see com.arahant.beans.Holiday#getHolidayId()
	 */
	public String getHolidayId() {
		return bean.getHolidayId();
	}

	/**
	 * @param description
	 * @see com.arahant.beans.Holiday#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		bean.setDescription(description);
	}

	/**
	 * @param hdate
	 * @see com.arahant.beans.Holiday#setHdate(int)
	 */
	public void setHdate(final int hdate) {
		bean.setHdate(hdate);
	}

	/**
	 * @param holidayId
	 * @see com.arahant.beans.Holiday#setHolidayId(java.lang.String)
	 */
	public void setHolidayId(final String holidayId) {
		bean.setHolidayId(holidayId);
	}

	public char getPartOfDay() {
		return bean.getPartOfDay();
	}

	public void setPartOfDay(char partOfDay) {
		bean.setPartOfDay(partOfDay);
	}

	public static BHoliday[] list(final HibernateSessionUtil hsu) {
		final List<Holiday> hlist = hsu.createCriteria(Holiday.class).orderBy(Holiday.HDATE).list();

		final BHoliday[] ret = new BHoliday[hlist.size()];

		int i = 0;
		for (Holiday h : hlist)
			ret[i++] = new BHoliday(h);

		return ret;
	}

	/**
	 * @param hsu
	 * @param holidayId
	 * @throws ArahantException
	 */
	public static void deleteHolidays(final HibernateSessionUtil hsu, final String[] holidayId) throws ArahantException {
		for (final String element : holidayId)
			new BHoliday(element).delete();
	}

	public static void resetHolidays(final HibernateSessionUtil hsu) throws ArahantException {

		final Calendar now = DateUtils.getNow();
		int year = now.get(Calendar.YEAR);

		if (now.get(Calendar.MONTH) > Calendar.AUGUST)
			year++;

		year = year * 10000;


//		delete all holidays for this year
		hsu.delete(hsu.createCriteria(Holiday.class).between(Holiday.HDATE, year, year + 10000).list());
		hsu.flush();

		final BHoliday christmasEve = new BHoliday();
		christmasEve.create();
		christmasEve.setDescription("Christmas Eve");
		christmasEve.setHdate(year + 1224);
		christmasEve.insert();

		final BHoliday christmas = new BHoliday();
		christmas.create();
		christmas.setDescription("Christmas");
		christmas.setHdate(year + 1225);
		christmas.insert();

		final BHoliday july4 = new BHoliday();
		july4.create();
		july4.setDescription("July 4");
		july4.setHdate(year + 704);
		july4.insert();

		final BHoliday newYears = new BHoliday();
		newYears.create();
		newYears.setDescription("New Year's Day");
		newYears.setHdate(year + 101);
		newYears.insert();


//		Memorial Day - last Monday in May
		final BHoliday memorialDay = new BHoliday();
		memorialDay.create();

		memorialDay.setDescription("Memorial Day");

		for (int dloop = year + 524; dloop <= year + 530; dloop++)
			if (DateUtils.getCalendar(dloop).get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
				memorialDay.setHdate(dloop);
		memorialDay.insert();


//		Labor Day - First Monday in September
		final BHoliday laborDay = new BHoliday();
		laborDay.create();
		laborDay.setDescription("Labor Day");
		for (int dloop = year + 901; dloop < year + 908; dloop++)
			if (DateUtils.getCalendar(dloop).get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
				laborDay.setHdate(dloop);
		laborDay.insert();


//		Thanksgiving  -- 4th Thursday in November
		final BHoliday thanksGiving = new BHoliday();
		thanksGiving.create();
		thanksGiving.setDescription("Thanksgiving Day");

//		Day after Thanksgiving
		final BHoliday thanksGiving2 = new BHoliday();
		thanksGiving2.create();
		thanksGiving2.setDescription("Thanksgiving Day After");

		for (int dloop = year + 1122; dloop < year + 1128; dloop++)
			if (DateUtils.getCalendar(dloop).get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
				thanksGiving.setHdate(dloop);
				thanksGiving2.setHdate(dloop + 1);
			}

		thanksGiving.insert();
		thanksGiving2.insert();
	}
}
