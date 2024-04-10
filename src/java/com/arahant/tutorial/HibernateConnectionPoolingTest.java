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
package com.arahant.tutorial;

import com.arahant.beans.Person;
import com.arahant.utils.HibernateUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateConnectionPoolingTest {

	public void runTest() {
		int count = 0;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); //new Configuration().configure().buildSessionFactory();
			Session session = sessionFactory.openSession();

			while (count < 100) {
				count++;
				Runnable c = new ConnectionThread(count);
				Thread t = new Thread(c);
				t.start();
			}
		} catch (final Exception e) {
			System.out.println("FINAL TOTAL SESSIONS: " + count);
			e.printStackTrace();
		}
	}

	public static void main(final String args[]) {
		HibernateConnectionPoolingTest h = new HibernateConnectionPoolingTest();
		h.runTest();
	}

	public class ConnectionThread implements Runnable {

		int threadCounter;

		public ConnectionThread(int a) {
			threadCounter = a;
		}

		@Override
		public void run() {

			//HibernateSessionUtil hsu=ArahantSession.getHSU();
//			ProjectParentOps p = new ProjectParentOps();
//			ListProjectTypesInput in = new ListProjectTypesInput();
//			in.setUser(ArahantSession.systemName());
//			in.setPassword("password");
//			p.listProjectTypes(in);


//			if(true)
//			{
//				return;
//			}

			Session session;
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); //new Configuration().configure().buildSessionFactory();
			session = sessionFactory.openSession();
			Criteria crit = session.createCriteria(Person.class);
			crit.setMaxResults(50);
			ScrollableResults p = crit.scroll();

			if (threadCounter % 10 == 0)
				System.out.println("running sessions: " + threadCounter);
			try {
				Thread.sleep(60000);
			} catch (InterruptedException ex) {
				Logger.getLogger(HibernateConnectionPoolingTest.class.getName()).log(Level.SEVERE, null, ex);
			}

			if (threadCounter % 10 == 0)
				System.out.println("FINISHED Thread: " + threadCounter);
//			while (true) {
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException ex) {
//					Logger.getLogger(HibernateConnectionPooling.class.getName()).log(Level.SEVERE, null, ex);
//				}
//			}
		}
	}
//			public static void main(String[] args)
//        {
//            Session session = null;
//            SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); //new Configuration().configure().buildSessionFactory();
//            session = sessionFactory.openSession();
//            Criteria crit = session.createCriteria(Person.class);
//            crit.setMaxResults(50);
//            //final Order o = org.hibernate.criterion.Order.asc("person");
//	    crit.addOrder(Order.asc("lname"));
//
//            List persons = crit.list();
////            List<Person> per = crit.list();
////
////            System.out.println("Before");
////
////            for (Person p : per)
////            {
////                System.out.println(p.getLname());
////            }
//
//            for (Iterator it = persons.iterator(); it.hasNext();)
//            {
//                Person p = (Person) it.next();
//                System.out.println(p.getLname() + ", " + p.getFname());
//            }
//
//            //System.out.println("After");
//
//
//
}
