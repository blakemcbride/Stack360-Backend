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
 * All rights reserved.
 */

package com.arahant.business;

import com.arahant.beans.TimeType;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;

/**
 *
 * 
 */
public class BTimeType implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BTimeType.class);
	private TimeType timeType;

	public BTimeType() {
	}

	/**
	 * @param type
	 */
	public BTimeType(final TimeType type) {
		timeType = type;
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BTimeType(final String key) throws ArahantException {
		timeType = ArahantSession.getHSU().get(TimeType.class, key);
	}

    @Override
    public void update() throws ArahantException {
        ArahantSession.getHSU().saveOrUpdate(timeType);
    }

    @Override
    public void insert() throws ArahantException {
        ArahantSession.getHSU().insert(timeType);
    }

    @Override
    public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
        try {
			ArahantSession.getHSU().delete(timeType);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
    }

    @Override
    public String create() throws ArahantException {
        timeType = new TimeType();
		timeType.generateId();
		return getTimeTypeId();
    }

    @Override
    public void load(String key) throws ArahantException {
        timeType = ArahantSession.getHSU().get(TimeType.class, key);
    }

    public String getTimeTypeId() {
        return timeType.getTimeTypeId();
    }
    
    public String getDescription() {
        return timeType.getDescription();
    }
    
    public BTimeType setDescription(String desc) {
        timeType.setDescription(desc);
        return this;
    }
    
    public int getLastActiveDate() {
        return timeType.getLastActiveDate();
    }
    
    public BTimeType setLastActiveDate(int dt) {
        timeType.setLastActiveDate(dt);
        return this;
    }
    
    public char getDefaultBillable() {
        return timeType.getDefaultBillable();
    }
    
    public BTimeType setDefaultBillable(char bt) {
        timeType.setDefaultBillable(bt);
        return this;
    }
    
    public char getDefaultType() {
        return timeType.getDefaultType();
    }
    
    public BTimeType setDefaultType(char type) {
        timeType.setDefaultType(type);
        return this;
    }
    
}
