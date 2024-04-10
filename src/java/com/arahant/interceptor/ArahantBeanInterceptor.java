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
 * Created on Aug 17, 2006
 * 
 */
package com.arahant.interceptor;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.arahant.beans.ArahantBean;
import com.arahant.beans.ArahantHistoryBean;
import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.IAuditedBean;
import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BProperty;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.JessBean;
import com.arahant.utils.MoneyUtils;
import org.kissweb.StringUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * 
 * @coauthor Arahant edited on Oct 18, 2010
 * 
 */
public class ArahantBeanInterceptor extends EmptyInterceptor {

	private static final ArahantLogger al=new ArahantLogger(ArahantBeanInterceptor.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 3021921313986753690L;

	private static List<IInterceptorHook> hooks = new LinkedList<IInterceptorHook>();

	public static void addHook(IInterceptorHook hook) {
		hooks.add(hook);
	}

	/**
	 *  this is called anytime a NEW object is created in the system, just before it is put into the database
	 */
	@Override
	public boolean onSave(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) {

//		for (IInterceptorHook hook : hooks)
//			hook.add(entity, id, state, propertyNames, types);

		//	logger.info("Interceptor got hit");
		if (entity instanceof ArahantBean)
		{
			//add this to the engine
			//((ArahantBean)entity).linkToEngine();
			//tell the AI engine I'm saving one of whatever this is
			//logger.info("Save doing "+entity.getClass().getSimpleName());
			//ArahantSession.AICmd("(assert (saving "+entity.getClass().getSimpleName()+" \""+id.toString()+"\"))");
			//ArahantSession.runAI();
		}
	
		return false; // state did not change
	}
	/**
	 *  this is called anytime an object is deleted in the system, just before it is put into the database
	 */
	@Override
	public void onDelete(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) {

//		for (IInterceptorHook hook : hooks)
//			hook.delete(entity, id, state, propertyNames, types);

	
		ArahantSession.AIEval("(assert (deleting "+entity.getClass().getSimpleName()+" \""+id.toString()+"\"))");
		try {
			ArahantSession.getAI().remove(entity);
		} catch (Exception e) {
			//I don't care
		}		
//		if (entity instanceof IAuditedBean)
//		{
//			IAuditedBean ab=(IAuditedBean)entity;
//
//			ArahantHistoryBean hb=ab.historyObject();
//			hb.copy(ab);
//			hb.generateId();
//			ArahantSession.getHSU().insert(hb);
//
//			ArahantHistoryBean hb2=ab.historyObject();
//			hb2.copy(ab);
//			hb2.generateId();
//			hb2.setRecordChangeDate(new java.util.Date());
//			hb2.setRecordChangeType('D');
//			hb2.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());
//			ArahantSession.getHSU().insert(hb2);
//		}
	
		return;
	}

	@Override
	public void preFlush(final Iterator entities)
	{
		//make sure AI engine can make updates
	//	ArahantSession.runAI();
		return;
	}
	/**
	 *  this is called right after onSave, onFlushDirty, or onDelete, AFTER it is put into the database
	 */
	@Override
	public void postFlush(Iterator entities)
	{
//		make sure AI engine can make updates
	//	while (entities.hasNext())
	//	{
	//		logger.info(entities.next().getClass().getSimpleName());
	//	}

		inPostFlush++;
		for (IInterceptorHook hook : hooks)
			hook.execute();

		ArahantSession.runAI();
		inPostFlush--;
		return;
	}
	
	
	@Override
	final public boolean onLoad(
			Object entity, 
			Serializable id, 
			Object[] state, 
			String[] propertyNames, 
			Type[] types) 
	{
		if (entity instanceof JessBean)
		{
			((JessBean)entity).linkToEngine();
		
		//	ArahantSession.runAI();
		}

		return false;
	}
	
	/**
	 *  this is called anytime an existing object is edited in the system, just before it is put into the database
	 */

	private int inPostFlush = 0;

	@Override
	public boolean onFlushDirty(
			Object entity, 
			Serializable id, 
			Object[] currentState, 
			Object[] previousState, 
			String[] propertyNames, 
			Type[] types) {

//		boolean ret = false;

//		if (inPostFlush != 0)
			return false;

//		for (IInterceptorHook hook : hooks)
//		{
//			if(hook.edit(entity, id, currentState, previousState, propertyNames, types))
//			{
//				ret = true;
//			}
//		}

		//System.out.println("IN flush dirty");
//		if (entity instanceof ArahantBean && !(entity instanceof IAuditedBean))
//		{
//			tell the AI engine I'm saving one of whatever this is
	//		ArahantSession.AICmd("(assert (saving "+entity.getClass().getSimpleName()+" \""+id.toString()+"\"))");
	//		ArahantSession.runAI();
//		}
//		if (entity instanceof IAuditedBean)
//		{
//			//System.out.println("doing audited bean");
//			IAuditedBean ab=(IAuditedBean)entity;
//
//
//			Object []savedHistory=ArahantSession.getHistory().get(ab);
//			boolean allMatch=savedHistory!=null;
//
//			for (int loop=0;allMatch && loop<savedHistory.length;loop++)
//			{
//				if (savedHistory[loop]==null)
//				{
//					allMatch=previousState[loop]==null;
//					continue;
//				}
//
//				if ((!(savedHistory[loop] instanceof Collection)) && (!(savedHistory[loop] instanceof ArahantBean)) && !savedHistory[loop].equals(previousState[loop]))
//					allMatch=false;
//			}
//
//			if (!allMatch)
//			{
//		//		logger.info("Save doing "+entity.getClass().getSimpleName());
//		//		ArahantSession.AICmd("(assert (saving "+entity.getClass().getSimpleName()+" \""+id.toString()+"\"))");
//				try
//				{
//					ArahantSession.getHistory().put(ab, previousState);
//
//					ArahantHistoryBean hb=ab.historyObject();
//					hb.copy(ab);
//
//					hb.setRecordChangeDate(ab.getRecordChangeDate());
//					hb.setRecordChangeType(ab.getRecordChangeType());
//					hb.setRecordPersonId(ab.getRecordPersonId());
//
//					//need to build history from previous state
//					for (int loop=0;loop<propertyNames.length;loop++)
//						hb.setField(propertyNames[loop],previousState[loop]);
//
//					//for some reason, hibernate is sometimes giving me multiple calls
//					//so need to skip some of them
//
//					if (!hb.alreadyThere())
//					{
//						if (hb.getRecordChangeDate()==null)
//							hb.setRecordChangeDate(new java.util.Date());
//						if (hb.getRecordChangeType()==0)
//							hb.setRecordChangeType('M');
//						if (hb.getRecordPersonId()==null)
//							hb.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());
//						hb.generateId();
//						ArahantSession.getHSU().insert(hb);
//					}
//				}
//				catch (Exception e)
//				{
//
//					al.error(e);
//				}
//			}
//
//			for (int loop=0;loop<propertyNames.length;loop++)
//			{
//				if (propertyNames[loop].equals("recordChangeDate"))
//					currentState[loop]=new java.util.Date();
//				if (propertyNames[loop].equals("recordChangeType"))
//					currentState[loop]='M';
//				if (propertyNames[loop].equals("recordPersonId"))
//				{
//					Person p=ArahantSession.getHSU().getCurrentPerson();
//					if (p==null)
//					{
//						System.out.println("Copying " + previousState[loop] + " to " + currentState[loop]);
//						currentState[loop]=previousState[loop];
//				//		throw new ArahantException("Current person is not set!");
//					/*	try
//						{
//							ArahantSession.getHSU().setCurrentPersonToArahant();
//							p=ArahantSession.getHSU().getCurrentPerson();
//							currentState[loop]=p.getPersonId();
//						}
//						catch (Throwable e)
//						{
//							currentState[loop]="00000-0000000000";
//						}
//					 * */
//					}
//					else
//					{
//						System.out.println("Copying " + p.getPersonId() + " to " + currentState[loop]);
//						currentState[loop]=p.getPersonId();
//					}
//				}
//			}
			//recursive = false;
			//return true;
		
//		return ret;
	}
	
	
	@Override
	public String onPrepareStatement(String sql) {
		
		//This is a pretty specific fix at the moment
		//If we upgrade PostgreSQL or if Hibernate adds ability to handle null sort to end
		//then we don't need this at all
		//so I'll just handle on a case by case basis for now

		 try {
			int orderByPos=sql.toLowerCase().indexOf("order by");
			if (orderByPos!=-1)
			{
				if (sql.toLowerCase().indexOf("select max", orderByPos)!=-1 && sql.toLowerCase().indexOf("SELECT max(pl.contact_date) FROM prospect_log".toLowerCase(), orderByPos)!=-1)
				{
					java.sql.Connection con = ArahantSession.getHSU().getConnection();

					if ("PostgreSQL".equals(con.getMetaData().getDatabaseProductName()))
					{
						sql=sql.replaceAll("order by", " order by  ( SELECT max(pl.contact_date) FROM prospect_log pl WHERE pl.org_group_id =this_.org_group_id ) is not null desc, ");
						//sql=sql.replaceAll(" desc, ", " is not null desc, ");
					//	logger.info(sql);
					}
				}
			}
			
		} catch (SQLException ex) {
			Logger.getLogger(ArahantBeanInterceptor.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return sql;
	}

}
