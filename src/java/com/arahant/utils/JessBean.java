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

import com.arahant.beans.ArahantBean;
import java.awt.Image;
import java.beans.*;
import java.lang.reflect.Method;
import java.sql.*;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import jess.*;

/**
 * This class is used to interface with the JESS AI engine. It should be
 * subclassed for your actual implementation. Your subclass should be java bean
 * based to allow access to its properties within the AI engine. Remember to
 * fire property changes in your set methods.
 *
 * This base class will take care of adding the bean to the engine. You can also
 * call the setAutoRetract method which will add a rule to remove the instance
 * of the object at the end of an AI engine run. This will allow you to keep
 * using the engine without having to reload rules over and over or having old
 * beans react to each run.
 *
 *
 * There are really two ways to use this bean. One is to subclass it and add a
 * bunch of JavaBean properties to your subclassed bean. Then construct rules to
 * operate off of those properties, and send them in via loadScript or
 * executeAICommand. Then run the engine and have decisions made off of the
 * properties.
 *
 * The other way is to subclass the bean and use the assert and retract
 * mechanisms. You can think of facts in this system as named lists. You can add
 * values to the lists or remove them using assert and retract respectively. All
 * beans tied to the fact name you use will get the notifications. Also, you can
 * add other rules to automatically assert and retract using loadScript and
 * executeAICommand. You should be able to figure out how to construct these
 * rules by looking at the assert and retract methods.
 *
 *
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class JessBean implements BeanInfo {

	private static final ArahantLogger logger = new ArahantLogger(JessBean.class);

	/**
	 * This is an instance of the AI engine.
	 */
	private Rete m_AI;

	/**
	 * This is the property change system member variable.
	 */
	private final PropertyChangeSupport m_pcs = new PropertyChangeSupport(this);

	/*
	 * This class is used to tie an asserter method into the engine.
	 *
	 * @version 1.0
	 */


	/**
	 * This method is to simplify property change calls in the java bean
	 * mechanism. Call it in your setXXXX methods of your subclasses.
	 *
	 * @param propertyName The name of the property that has been changed.
	 * @param oldValue The old value of the property.
	 * @param newValue The new value of the property.
	 */
	final protected void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
		//notify listeners that a property changed
		try {
			m_pcs.firePropertyChange(propertyName, oldValue, newValue);
		} catch (Throwable e) {
			logger.debug(e);//I don't care
		}
	}

	/**
	 * This method is for the JavaBean property notification mechanism. Call it
	 * to register for notifications that properties of this bean have changed.
	 *
	 * @param pcl An object that is a PropertyChangeListener.
	 */
	public void addPropertyChangeListener(final PropertyChangeListener pcl) {
		//add a new listener
		m_pcs.addPropertyChangeListener(pcl);
	}

	/**
	 * Call this method to stop listing to property change notifications from
	 * this bean.
	 *
	 * @param pcl Your property change listener.
	 */
	public void removePropertyChangeListener(final PropertyChangeListener pcl) {
		//remove a listener
		m_pcs.removePropertyChangeListener(pcl);
	}

	//transient protected Connection con=ArahantSession.getHSU().getConnection();;
	/**
	 * This method returns the current instance of the AI Engine so it can be
	 * passed to other JessBeans.
	 *
	 * @return The AI engine instance used by this bean.
	 */
	public Rete getAIEngine() {
		if (ArahantSession.disableJess)
			return null;
		if (m_AI == null)
			m_AI = new Rete();
		return m_AI;
	}

	protected void setAIEngine(final Rete r) {
		m_AI = r;
	}

	/**
	 * This method ties the bean into the AI engine. It's JavaBean properties
	 * become available to the AI engine. It will also receive assert and
	 * retract calls for the facts that it binds to.
	 *
	 */
	public void linkToEngine() {
		//describe bean to engine
		try {
			if (ArahantSession.getHSU().aiIntegrate) {
				// logger.info("linking to engine");
				if (m_AI == null)
					m_AI = ArahantSession.getAI();
				m_AI.add(this);
			}
		} catch (JessException e) {
			logger.error(e);
		}
	}
	
    public JessBean(HibernateSessionUtil hsu) {
		try {
			m_AI = ArahantSession.getAI();
		} catch (Exception ignored) {
		}
	}

	/**
	 * This constructor will create an instance of the AI engine and link the
	 * bean to the AI engine.
	 */
	public JessBean() {
		try {
			//Create Rete Engine
			m_AI = ArahantSession.getAI();
			//        con=ArahantSession.getHSU().getConnection();

		} catch (final Exception e) {
			logger.error(e);
		}
	}
    
    public void debugAI() {
		executeAICommand("(watch all)");
	}

	public void initialize() {
		//load standard scripts
		loadScript("jess/scriptlib.clp");

		//turn on debugging
		//executeAICommand("(watch all)");

		//issue initial reset
		executeAICommand("(reset)");
	}

	/**
	 * This constructor requires an instance of the AI engine to be passed in.
	 * This is so multiple beans can be tied to the same engine.
	 *
	 * @param AIEngine An instance of the AI engine.
	 */
	public JessBean(final Rete AIEngine) {
		try {
			if (AIEngine == null)
				return;

			//set Rete Engine
			m_AI = AIEngine;

			//        con=ArahantSession.getHSU().getConnection();
			//tie this bean to the engine
			//linkToEngine();

		} catch (final Exception e) {
			logger.error(e);
		}
	}
    
    /**
     * This method activates the engine to process the facts and rules.
     */
    public void runAIEngine() {
		if (ArahantSession.disableJess)
			return;
		try {
			//run the engine
			m_AI.run();
		} catch (final Exception e) {
			logger.error(e);
		}
	}

	/**
	 * This method allows you to pass Jess commands to the engine.
	 *
	 * @param sCommand A string containing a valid Jess command.
	 */
	public void executeAICommand(final String sCommand) {
		if (ArahantSession.disableJess)
			return;
		try {
			//pass command to engine
			m_AI.eval(sCommand);
		} catch (final Exception e) {
			logger.error(e);
		}
	}

	/**
	 * This method will load a Jess command file. You can take a batch of Jess
	 * commands, facts, and rules, and put them in a file. Calling this method
	 * will load them into the engine.
	 *
	 * This file traditionally has a .clp extension.
	 *
	 * @param sFilename Filename of Jess command script to process.
	 */
	public void loadScript(final String sFilename) {
		if (ArahantSession.disableJess)
			return;
		try {
			//load ai script to engine
			m_AI.eval("(batch " + sFilename + ")");
		} catch (final Exception e) {
			logger.error(e);
		}
	}

	public List<Fact> queryForFacts(final String factName) {
		return queryForFacts(factName, null);
	}

	//String lastDefQuery="";
	public List<Fact> queryForFacts(final String factName, final Map keys) {
		final List<Fact> factList = new LinkedList<Fact>();

		try {
			StringBuilder defquery = new StringBuilder("(defquery search ");
			StringBuilder defPart2 = new StringBuilder();

			StringBuilder runquery = new StringBuilder("(store RESULT (run-query search ");

			if (keys != null && keys.size() > 0) {
				defquery.append("(declare (variables ");

				final Iterator entryItr = keys.entrySet().iterator();
				int loop = 0;
				while (entryItr.hasNext()) {
					defquery.append("?X").append(loop).append(" ");
					final Map.Entry entry = (Map.Entry) entryItr.next();
					defPart2.append("(").append(entry.getKey()).append(" ?X").append(loop).append(")");

					//	logger.info(entry.getKey()+"='"+entry.getValue()+"'");

					runquery.append(" ").append(entry.getValue());
					loop++;
				}
				defquery.append(")) ");
			}

			runquery.append("))");

			defquery.append("(").append(factName).append(" ");

			defquery.append(defPart2);

			defquery.append("))");

			//		if (!defquery.equals(lastDefQuery))
			{
				executeAICommand(defquery.toString());
				//		lastDefQuery=defquery;
			}

			executeAICommand(runquery.toString());

			final Value v = m_AI.fetch("RESULT");
			if (v != null) {
				final Iterator fItr = (Iterator) v.externalAddressValue(null);

				while (fItr.hasNext())
					factList.add(((Token) fItr.next()).fact(1));
			}

		} catch (final JessException e) {
			logger.error(e);
		}
		return factList;
	}
	
	/*
	 protected List queryForFacts(String factName, HashMap keys) {
	 List factList=new LinkedList();
		
	 Set entrySet=null;
		
	 if (keys!=null)
	 entrySet=keys.entrySet();
		
	 Context globalContext=m_AI.getGlobalContext();
		
	 Iterator flist=m_AI.listFacts();
	 while (flist.hasNext())
	 {
	 Fact f=(Fact)flist.next();
			
	 if (f.getName().equals("MAIN::"+factName))
	 {
	 if (keys==null)
	 {
	 factList.add(f);
	 }
	 else
	 {
	 Iterator itr=entrySet.iterator();
	 boolean match=true;
	 while (itr.hasNext())  //this should verify ALL keys match
	 {
	 Map.Entry entry=(Map.Entry)itr.next();
	 String key=(String)entry.getKey();
	 try
	 {
	 //Value val=f.getSlotValue(key);
	 //		System.out.print(val.stringValue(m_AI.getGlobalContext())+"=");
	 //		logger.info(keys.get(key));
	 if (!f.getSlotValue(key).stringValue(globalContext).equals(entry.getValue()))
	 {
	 match=false;
	 break;
	 }
	 }
	 catch (Exception e)
	 {
	 match=false;
	 break;
	 }
	 }
	 if (match)
	 factList.add(f);
	 }
	 }
	 }
	 return factList;
	 }
	 */

	public int getFactIntValue(final Fact f, final String slot) throws JessException {
		try {
			return f.getSlotValue(slot).intValue(m_AI.getGlobalContext());
		} catch (final JessException e) {
			return 0;
		}
	}

	public long getFactLongValue(final Fact f, final String slot) throws JessException {
		try {
			return f.getSlotValue(slot).longValue(m_AI.getGlobalContext());
		} catch (final JessException e) {
			return 0;
		}
	}

	public double getFactFloatValue(final Fact f, final String slot) throws JessException {
		try {
			return f.getSlotValue(slot).floatValue(m_AI.getGlobalContext());
		} catch (final JessException e) {
			return 0;
		}
	}

	public String getFactStringValue(final Fact f, final String slot) throws JessException {
		try {
			return f.getSlotValue(slot).stringValue(m_AI.getGlobalContext());
		} catch (final JessException e) {
			return "";
		}
	}

	public void makeTableTemplate(final String tableName) {
		try {
			final StringBuilder def = new StringBuilder("(deftemplate ");
			def.append(tableName);
			def.append(" (slot valid)");

			final Statement st = ArahantSession.getHSU().getConnection().createStatement();

			final ResultSet rs = st.executeQuery("select * from " + tableName);

			for (int loop = 0; loop < rs.getMetaData().getColumnCount(); loop++) {
				def.append("(slot ");
				def.append(rs.getMetaData().getColumnName(loop + 1).toLowerCase());
				def.append(")");
			}
			def.append(")");

			rs.close();
			st.close();

			executeAICommand(def.toString());
		} catch (final SQLException e) {
			logger.error(e);
		}
	}

	public String loadTableFacts(final String tableName) {
		return loadTableFacts(tableName, "");
	}

	public String loadFacts(final String tableName, final String where) {
		final StringBuilder ret = new StringBuilder();
		try {
			final Statement st = ArahantSession.getHSU().getConnection().createStatement();

			final ResultSet rs = st.executeQuery("select * from " + tableName + " " + where);

			if (rs.next())
				do {
					final StringBuilder f = new StringBuilder("(assert (");
					f.append(tableName);
					f.append(" ");
					for (int loop = 0; loop < rs.getMetaData().getColumnCount(); loop++) {
						String t;
						switch (rs.getMetaData().getColumnType(loop + 1)) {
							case Types.CHAR:
							case Types.VARCHAR:
							case Types.CLOB:
								t = rs.getString(loop + 1);
								if (t == null)
									t = "";
								if (t.indexOf('"') != -1)
									t = t.replace('"', ' ').trim();
								f.append("(");
								f.append(rs.getMetaData().getColumnName(loop + 1).toLowerCase());
								f.append(" \"");
								f.append(t);
								f.append("\")");
								break;
							default: {
								f.append("(");
								f.append(rs.getMetaData().getColumnName(loop + 1).toLowerCase());
								f.append(" ");
								f.append(rs.getString(loop + 1));
								f.append(")");
							}
						}
					}

					f.append("))");
					ret.append(f);
					executeAICommand(f.toString());
				} while (rs.next());

			rs.close();
			st.close();
		} catch (final SQLException e) {
			logger.error(e);
		}
		return ret.toString();
	}

	public String loadTableFacts(final String tableName, final String where) {
		return loadTableFacts(new String[]{"*"}, tableName, where);
	}

	public String loadTableFactsNoTemplate(final String[] columns, final String tableName, final String where) {
		final StringBuilder ret = new StringBuilder();
		try {
			final Statement st = ArahantSession.getHSU().getConnection().createStatement();

			StringBuilder query = new StringBuilder("select ");
			boolean first = true;
			for (String element : columns) {
				if (first)
					first = false;
				else
					query.append(",");
				query.append(" ").append(element);
			}
			query.append(" from ").append(tableName).append(" ").append(where);
			final ResultSet rs = st.executeQuery(query.toString());

			if (rs.next())
				do {
					final StringBuilder f = new StringBuilder("(assert (");
					f.append(tableName);
					f.append(" ");
					for (int loop = 0; loop < rs.getMetaData().getColumnCount(); loop++) {
						String t;
						switch (rs.getMetaData().getColumnType(loop + 1)) {
							case Types.CHAR:
							case Types.VARCHAR:
							case Types.CLOB:
								t = rs.getString(loop + 1);
								if (t == null)
									t = "";
								if (t.indexOf('"') != -1)
									t = t.replace('"', ' ').trim();
								f.append("(");
								f.append(rs.getMetaData().getColumnName(loop + 1).toLowerCase());
								f.append(" \"");
								f.append(t);
								f.append("\")");
								break;
							default: {
								f.append("(");
								f.append(rs.getMetaData().getColumnName(loop + 1).toLowerCase());
								f.append(" ");
								f.append(rs.getString(loop + 1));
								f.append(")");
							}
						}
					}

					f.append("))");
					ret.append(f);
					executeAICommand(f.toString());
				} while (rs.next());

			rs.close();
			st.close();

		} catch (final SQLException e) {
			logger.error(e);
		}
		return ret.toString();
	}

	public void loadTableFacts(final List<ArahantBean> l) {
		try {
			if (l.size() < 1)
				return;

			final BeanInfo bi = java.beans.Introspector.getBeanInfo(l.getClass());
			final PropertyDescriptor[] pd = bi.getPropertyDescriptors();

			final StringBuilder def = new StringBuilder("(deftemplate ");
			def.append(l.get(0).tableName());
			def.append(" (slot valid)");


			for (PropertyDescriptor element : pd) {
				def.append("(slot ");
				def.append(element.getName().toLowerCase());
				def.append(")");
			}

			def.append(")");
			executeAICommand(def.toString());

			for (final ArahantBean bean : l)
				for (PropertyDescriptor element : pd) {
					element.getName();

					element.getPropertyType();
					element.getReadMethod().invoke(bean, (Object[]) null);
				}
		} catch (final Exception e) {
			logger.error(e);
		}
	}

	public String loadTableFacts(final String[] columns, final String tableName, final String where) {
		if (ArahantSession.disableJess)
			return null;
		final StringBuilder ret = new StringBuilder();
		try {
			final StringBuilder def = new StringBuilder("(deftemplate ");
			def.append(tableName);
			def.append(" (slot valid)");

			final Statement st = ArahantSession.getHSU().getConnection().createStatement();

			StringBuilder query = new StringBuilder("select ");
			boolean first = true;
			for (String element : columns) {
				if (first)
					first = false;
				else
					query.append(",");
				query.append(" ").append(element);
			}
			query.append(" from ").append(tableName).append(" ").append(where);
			//	logger.info(query);
			final ResultSet rs = st.executeQuery(query.toString());

			for (int loop = 0; loop < rs.getMetaData().getColumnCount(); loop++) {
				def.append("(slot ");
				def.append(rs.getMetaData().getColumnName(loop + 1).toLowerCase());
				def.append(")");
			}
			def.append(")");

			ret.append(def);
			executeAICommand(def.toString());

			if (rs.next())
				do {
					final StringBuilder f = new StringBuilder("(assert (");
					f.append(tableName);
					f.append(" ");
					for (int loop = 0; loop < rs.getMetaData().getColumnCount(); loop++) {
						String t;

						switch (rs.getMetaData().getColumnType(loop + 1)) {
							case Types.CHAR:
							case Types.VARCHAR:
							case Types.CLOB:
							case Types.DATE:
							case Types.TIME:
							case Types.TIMESTAMP:
								t = rs.getString(loop + 1);
								if (t == null)
									t = "";
								if (t.indexOf('"') != -1)
									t = t.replace('"', ' ').trim();
								f.append("(");
								f.append(rs.getMetaData().getColumnName(loop + 1).toLowerCase());
								f.append(" \"");
								f.append(t);
								f.append("\")");
								break;
							default: {
								f.append("(");
								f.append(rs.getMetaData().getColumnName(loop + 1).toLowerCase());
								f.append(" ");
								f.append(rs.getString(loop + 1));
								f.append(")");
							}
						}
					}

					f.append("))");
					ret.append(f);
					executeAICommand(f.toString());
				} while (rs.next());

			rs.close();
			st.close();
		} catch (final SQLException e) {
			logger.error(e);
		}
		return ret.toString();
	}

	protected double getFactDoubleValue(final Fact f, final String slot) throws JessException {
		return f.getSlotValue(slot).floatValue(m_AI.getGlobalContext());
	}

	public void removeFromAIEngine() {
		try {
			m_AI.remove(this);
		} catch (JessException ex) {
			Logger.getLogger(JessBean.class.getName()).log(Level.WARNING, null, ex);
		}
	}

	@Override
	public BeanInfo[] getAdditionalBeanInfo() {
		return null;
	}

	@Override
	public BeanDescriptor getBeanDescriptor() {
		return null;
	}

	@Override
	public int getDefaultEventIndex() {
		return 0;
	}

	@Override
	public int getDefaultPropertyIndex() {
		return 0;
	}

	@Override
	public EventSetDescriptor[] getEventSetDescriptors() {
		return new EventSetDescriptor[0];
	}

	@Override
	public Image getIcon(int iconKind) {
		return null;
	}

	@Override
	public MethodDescriptor[] getMethodDescriptors() {
		return null;
	}

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		//override this to not return collection properties

		List<PropertyDescriptor> pdList = new LinkedList<>();

		// get all of the methods 

		for (Method m : this.getClass().getMethods())
			if (m.getName().startsWith("get") && !Collection.class.isInstance(m.getReturnType()) && !JessBean.class.isInstance(m.getReturnType()) && m.getParameterTypes().length == 0) {
				Class<?> ret = m.getReturnType();
				if (!ret.equals(java.lang.Integer.class) && !ret.equals(Integer.TYPE) && !ret.equals(java.lang.String.class) && !ret.equals(Character.class)
						&& !ret.equals(Character.TYPE) && !ret.equals(Short.class) && !ret.equals(Short.TYPE) && !ret.equals(Long.class) && !ret.equals(Long.TYPE)
						&& !ret.equals(Float.class) && !ret.equals(Float.TYPE) && !ret.equals(Double.class) && !ret.equals(Double.TYPE)
						&& !ret.equals(Boolean.class) && !ret.equals(Boolean.TYPE))
					continue;

				String propName = m.getName().substring(3, 4).toLowerCase();

				if (m.getName().length() > 4)
					propName = propName + m.getName().substring(4);

				try {
					Class<?>[] ar = new Class[0];
					if (JessBean.class.getMethod(m.getName(), ar) != null)
						continue;
				} catch (NoSuchMethodException e) {
					//good
				}

				try {
					Class<?>[] ar = new Class[]{m.getReturnType()};
					this.getClass().getMethod("set" + m.getName().substring(3), ar);
				} catch (NoSuchMethodException e) {
					continue; //good
				}


				try {
					pdList.add(new PropertyDescriptor(propName, this.getClass(), m.getName(), "set" + m.getName().substring(3)));
				} catch (Exception e) {
					logger.error(e);
				}
				//		logger.info(propName);
			}
        return pdList.toArray(new PropertyDescriptor[0]);
	}
}
