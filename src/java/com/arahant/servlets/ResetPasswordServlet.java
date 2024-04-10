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
package com.arahant.servlets;

import com.arahant.beans.Person;
import com.arahant.beans.ProphetLogin;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  In order to connect an external URL to this servlet, see servletNames in
 *  com/arahant/utils/generators/wsconfiggen/WsConfigGen.java
 */
public class ResetPasswordServlet extends HttpServlet {

	private static final ArahantLogger logger = new ArahantLogger(ResetPasswordServlet.class);
	private static final long serialVersionUID = 1L;

	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String login = request.getParameter("login");

			HibernateSessionUtil hsu = ArahantSession.openHSU();
			hsu.beginTransaction();

			Person p = hsu.createCriteriaNoCompanyFilter(Person.class)
					.joinTo(Person.PROPHETLOGINS)
					.eq(ProphetLogin.USERLOGIN, login)
					.first();

			String ret;

			if (p == null)
				ret = "User login " + login + " not found.";
			else
				ret = new BPerson(p).resetPassword();

			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.print("<Response>" + ret + "</Response>");

			out.flush();
			out.close();

			ArahantSession.getHSU().commitTransaction();
		} catch (Exception e) {
			ArahantSession.getHSU().rollbackTransaction();
			logger.error(e);
			throw new ServletException(e);
		} finally {
			ArahantSession.clearSession();
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP
	 * <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP
	 * <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 */
	@Override
	public String getServletInfo() {
		return "Check login servlet";
	}// </editor-fold>
}
