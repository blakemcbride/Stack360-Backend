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
 * Created on Sep 19, 2006
 */
package com.arahant.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.criterion.*;

import com.arahant.beans.ArahantBean;

public class HibernateDetachedCriteriaUtil < T extends ArahantBean >{

	DetachedCriteria criteria;

	private String alias;
	
	public HibernateDetachedCriteriaUtil (final Class clazz, final String alias)
	{
		criteria=DetachedCriteria.forClass(clazz,alias);
		this.alias=alias;
	}
	
	private HibernateDetachedCriteriaUtil(final DetachedCriteria crit)
	{
		criteria=crit;
	}
	
	private String fixProp(final String prop)
	{
		return alias+"."+prop;
	}
	public HibernateDetachedCriteriaUtil<T> in (String propName, final Collection values)
	{
		propName=fixProp(propName);
		if (values.size()==0)
		{
			//there is no way it could be in, so I need to force a failure 
			//without messing up the query
			criteria.add(Restrictions.isNotNull(propName));
			criteria.add(Restrictions.isNull(propName));	
		}
		else
			criteria.add(Restrictions.in(propName,values));
		
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> notIn (String propName, final Collection values)
	{	
		propName=fixProp(propName);
		//if there are no values, then I don't need the criterion
		if (values.size()!=0)
			criteria.add(Restrictions.not(Restrictions.in(propName,values)));
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> between (String propName, final int low, final int high)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.between(propName, low, high));
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> eq (String propName, final Object value)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.eq(propName,value));
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> eq (String propName, final long value)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.eq(propName, value));
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> isNull (String propName)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.isNull(propName));
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> notNull (String propName)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.isNotNull(propName));
		return this;
	}
	public HibernateDetachedCriteriaUtil<T> ne (String propName, final Object value)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.ne(propName,value));
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> eq (String propName, final char value)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.eq(propName, value));
		return this;
	}
	public HibernateDetachedCriteriaUtil<T> eq (String propName, final int value)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.eq(propName, value));
		return this;
	}
	public HibernateDetachedCriteriaUtil<T> eq (String propName, final short value)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.eq(propName, value));
		return this;
	}
	public HibernateDetachedCriteriaUtil<T> like (String propName, final String value)
	{
		propName=fixProp(propName);
		if (value==null || value.equals("") || value.equals("*") || value.equals("%"))
			return this;
		
		if (value.indexOf('%')==-1 && value.indexOf('*')==-1)
			return eq(propName,value);
		criteria.add(Restrictions.like(propName, value.replaceAll("\\*", "%")));
		return this;
	}
	public HibernateDetachedCriteriaUtil<T> ge (String propName, final Object value)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.ge(propName,value));
		return this;
	}
	public HibernateDetachedCriteriaUtil<T> gt (String propName, final Object value)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.gt(propName, value));
		return this;
	}
	public HibernateDetachedCriteriaUtil<T> lt (String propName, final Object value)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.lt(propName, value));
		return this;
	}
	public HibernateDetachedCriteriaUtil<T> le (String propName, final Object value)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.le(propName,value));
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> sizeEq(String propName, final int size)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.sizeEq(propName, size));
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> sizeNe(String propName, final int size)
	{
		propName=fixProp(propName);
		criteria.add(Restrictions.sizeNe(propName, size));
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> ge(final String propName, final int x)
	{
		ge(propName,x);
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> le(final String propName, final int x)
	{
		le(propName,x);
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> lt(final String propName, final int x)
	{
		lt(propName,x);
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> gt(final String propName, final int x)
	{
		gt(propName,x);
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> gt(final String propName, final short x)
	{
		gt(propName,x);
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> ne(final String propName, final int x)
	{
		ne(propName,x);
		return this;
	}

	public HibernateDetachedCriteriaUtil<T> ne(final String propName, final short x)
	{
		ne(propName,x);
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> ne(final String propName, final char x)
	{
		ne(propName, x);
		return this;
	}


	
	public HibernateDetachedCriteriaUtil<T> max (String col, final String alias)
	{
		col=fixProp(col);
		final Projection p=Projections.max(col);
		Projections.alias(p, alias);
		criteria.setProjection(p);
		return this;
	}
	
	public HibernateDetachedCriteriaUtil<T> min (String col, final String alias)
	{
		col=fixProp(col);
		final Projection p=Projections.min(col);
		Projections.alias(p, alias);
		criteria.setProjection(p);
		return this;
	}

	
	public void setExample(final Object example)
	{
		final Example ex=Example.create(example);
		ex.excludeZeroes();
		criteria.add(ex);
	}

	
	public HibernateDetachedCriteriaUtil<T> joinTo(String propertyName)
	{
		propertyName=fixProp(propertyName);
		return new HibernateDetachedCriteriaUtil<T>(criteria.createCriteria(propertyName));
	}
	

	public HibernateDetachedCriteriaUtil<T> joinTo(String propertyName, final String alias)
	{
		propertyName=fixProp(propertyName);
		return new HibernateDetachedCriteriaUtil<T>(criteria.createCriteria(propertyName,alias));
	}
	


	public HibernateDetachedCriteriaUtil <T>distinct()
	{
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);		
		return this;
	}
	

	public HibernateDetachedCriteriaUtil<T> orEq(String propertyName1, String propertyName2, final Object val)
	{
		propertyName1=fixProp(propertyName1);
		propertyName2=fixProp(propertyName2);
		criteria.add(Restrictions.or(Restrictions.eq(propertyName1, val), Restrictions.eq(propertyName2, val)));
		return this;
	}
	
	
	public HibernateDetachedCriteriaUtil<T> leJoinedField(String prop1, final String prop2)
	{
		prop1=fixProp(prop1);
		criteria.add(Restrictions.leProperty(prop1, prop2));
		return this;
	}
	public HibernateDetachedCriteriaUtil<T> eqJoinedField(String prop1, final String prop2)
	{
		prop1=fixProp(prop1);
		criteria.add(Restrictions.eqProperty(prop1, prop2));
		return this;
	}
	public HibernateDetachedCriteriaUtil<T> gtJoinedField(String prop1, final String prop2)
	{
		prop1=fixProp(prop1);
		criteria.add(Restrictions.gtProperty(prop1, prop2));
		return this;
	}

	
	/**
	 * @param propName
	 * @param date
	 * @param dateSearchType
	 */
	public HibernateDetachedCriteriaUtil<T> dateCompare(String propName, final int date, final int dateSearchType) {

		propName=fixProp(propName);
		
		if (dateSearchType==0)
			return this;
		
		switch (dateSearchType) {
		case 1:
			eq(propName,date);
			break;
		case 2:
			lt(propName,date);
			break;
		case 3:
			gt(propName,date);
			break;
		case 4:
			ne(propName,date);
			break;
		default:
			break;
		}
		return this;
	}

	public HibernateDetachedCriteriaUtil<T> in(String column, final Object[] ids) {
		column=fixProp(column);
		final ArrayList<Object> al=new ArrayList<Object>(ids.length);
		Collections.addAll(al, ids);
		return in(column,al);
	}

	public HibernateDetachedCriteriaUtil<T> in(final String col, final char[] c) {
		final Character[] car =new Character[c.length];
		for (int loop=0;loop<c.length;loop++)
			car[loop]=c[loop];
		return in(col,car);
	}

	public HibernateDetachedCriteriaUtil<T> sum(String col) {
		col=fixProp(col);
		criteria.setProjection(Projections.projectionList().add(Projections.sum(col)));
		return this;
	}

	public HibernateDetachedCriteriaUtil<T> addReturnField(final String col) {
		criteria.setProjection(Projections.projectionList().add(Projections.property(col)));
		return this;
	}


}

	
