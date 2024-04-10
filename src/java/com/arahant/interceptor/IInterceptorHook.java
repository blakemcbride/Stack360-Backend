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


package com.arahant.interceptor;

import java.io.Serializable;
import org.hibernate.type.Type;

/**
 *
 * Arahant
 */
public interface IInterceptorHook {

	public void add(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types);

	public void delete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types);

	public boolean edit(Object entity,	Serializable id, Object[] currentState,	Object[] previousState,	String[] propertyNames,	Type[] types);

	public void execute();
}
