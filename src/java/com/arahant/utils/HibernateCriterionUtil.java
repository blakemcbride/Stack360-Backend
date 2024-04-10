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
package com.arahant.utils;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class HibernateCriterionUtil {

	private Criterion criterion;
	private final Criteria criteria;
	private final String alias;

	public HibernateCriterionUtil(final Criteria c, final String alias) {
		criteria = c;
		this.alias = alias;
	}

	public HibernateCriterionUtil dateBetween(String propName, java.util.Date date1, java.util.Date date2) {
		propName = addAlias(propName);
		criterion = Restrictions.and(Restrictions.ge(propName, date1), Restrictions.le(propName, date2));
		return this;
	}

	public HibernateCriterionUtil isNull(String propName) {
		propName = addAlias(propName);

		criterion = Restrictions.isNull(propName);

		return this;
	}

	public HibernateCriterionUtil isNotNull(String propName) {
		propName = addAlias(propName);

		criterion = Restrictions.isNotNull(propName);

		return this;
	}

	private String addAlias(final String propName) {
		if (alias.equals(""))
			return propName;
		if (propName.indexOf('.') != -1)
			return propName;
		return alias + "." + propName;
	}

	public HibernateCriterionUtil eq(String propName, final Object value) {
		propName = addAlias(propName);

		if (value == null)
			criterion = Restrictions.isNull(propName);
		else
			criterion = Restrictions.eq(propName, value);
		return this;
	}

	public HibernateCriterionUtil ne(String propName, final Object value) {
		propName = addAlias(propName);

		if (value == null)
			criterion = Restrictions.isNotNull(propName);
		else
			criterion = Restrictions.ne(propName, value);
		return this;
	}

	public HibernateCriterionUtil lt(String propName, final Object value) {
		propName = addAlias(propName);
		criterion = Restrictions.lt(propName, value);
		return this;
	}

	public HibernateCriterionUtil gt(String propName, final Object value) {
		propName = addAlias(propName);
		criterion = Restrictions.gt(propName, value);
		return this;
	}

	public HibernateCriterionUtil ge(String propName, Object value) {
		propName = addAlias(propName);
		criterion = Restrictions.ge(propName, value);
		return this;
	}

	public HibernateCriterionUtil or(final List<HibernateCriterionUtil> hc) {
		if (hc.size() < 2)
			return this;

		criterion = hc.get(0).criterion;

		for (int loop = 1; loop < hc.size(); loop++)
			criterion = Restrictions.or(criterion, hc.get(loop).criterion);

		return this;
	}

	public HibernateCriterionUtil leJoinedField(String prop1, final String prop2) {
		prop1 = addAlias(prop1);
		criterion = Restrictions.leProperty(prop1, prop2);
		return this;
	}

	public HibernateCriterionUtil eqJoinedField(String prop1, final String prop2) {
		prop1 = addAlias(prop1);
		criterion = Restrictions.eqProperty(prop1, prop2);
		return this;
	}

	public HibernateCriterionUtil gtJoinedField(String prop1, final String prop2) {
		prop1 = addAlias(prop1);
		criterion = Restrictions.gtProperty(prop1, prop2);
		return this;
	}

	public HibernateCriterionUtil add() {
		criteria.add(criterion);
		return this;
	}

	public HibernateCriterionUtil in(String propName, final Collection values) {
		propName = addAlias(propName);
		criterion = Restrictions.in(propName, values);
		return this;
	}

	public HibernateCriterionUtil sizeNe(String propName, final int size) {
		propName = addAlias(propName);
		criterion = Restrictions.sizeNe(propName, size);
		return this;
	}

	public HibernateCriterionUtil sizeEq(String propName, final int size) {
		propName = addAlias(propName);
		criterion = Restrictions.sizeEq(propName, size);
		return this;
	}

	public HibernateCriterionUtil le(final String propName, final Object value) {
		criterion = Restrictions.le(propName, value);
		return this;
	}

	public HibernateCriterionUtil between(String propName, Date startDate, Date endDate) {
		propName = addAlias(propName);
		criterion = Restrictions.between(propName, startDate, endDate);
		return this;
	}

	public HibernateCriterionUtil between(String propName, int startDate, int endDate) {
		propName = addAlias(propName);
		criterion = Restrictions.between(propName, startDate, endDate);
		return this;
	}

	public HibernateCriterionUtil dateBetween(String propName, int date1, int date2) {
		propName = addAlias(propName);
		criterion = Restrictions.and(Restrictions.ge(propName, date1), Restrictions.le(propName, date2));
		return this;
	}
    
    public HibernateCriterionUtil or(HibernateCriterionUtil ... hcv) {
		if (hcv.length < 2)
			return this;

		criterion = hcv[0].criterion;

		for (int loop = 1; loop < hcv.length; loop++)
			criterion = Restrictions.or(criterion, hcv[loop].criterion);
        
       
        return this;
    }
    
    public HibernateCriterionUtil and(HibernateCriterionUtil ... hcv) {
		if (hcv.length < 2)
			return this;

		criterion = hcv[0].criterion;

		for (int loop = 1; loop < hcv.length; loop++)
			criterion = Restrictions.and(criterion, hcv[loop].criterion);
        
       
        return this;
    }
}
