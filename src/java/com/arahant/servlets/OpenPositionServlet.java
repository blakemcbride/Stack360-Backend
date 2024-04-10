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

import com.arahant.jspserver.applicant.PositionInformation;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DOMUtils;
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
public class OpenPositionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final ArahantLogger logger = new ArahantLogger(OpenPositionServlet.class);

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HibernateSessionUtil hsu = ArahantSession.openHSU();

		hsu.beginTransaction();
		try {
			hsu.setCurrentPersonToArahant();
/*
			PositionInformation[] positions = PositionInformation.getAvailablePositions();

			String ret = "<Positions>";

			for (PositionInformation p : positions) {
				ret += "<PositionInformation>";
				ret += "<name>" + DOMUtils.escapeText(p.getName()) + "</name>";
				ret += "<date>" + DOMUtils.escapeText(p.getDateFormatted()) + "</date>";
				ret += "<location>" + DOMUtils.escapeText(p.getLocation()) + "</location>";
				ret += "<jobType>" + DOMUtils.escapeText(p.getJobType()) + "</jobType>";
				ret += "<id>" + DOMUtils.escapeText(p.getId()) + "</id>";

				ret += "<items>";

				for (PositionInformationItem pii : p.getItems()) {
					ret += "<item>";
					ret += "<name>" + DOMUtils.escapeText(pii.getName()) + "</name>";
					ret += "<data>" + DOMUtils.escapeText(pii.getData()) + "</data>";
					ret += "</item>";
				}
				ret += "</items>";

				ret += "</PositionInformation>";

			}
			ret += "</Positions>";

			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.print(ret);

			out.flush();
			out.close();

 */
		} catch (Exception e) {
			logger.error(e);
		}
		hsu.rollbackTransaction();
		ArahantSession.clearSession();
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
		return "Applicant info servlet";
	}// </editor-fold>
}
/*
 * <PositionInformation>
 <name />  the name of the position
 <date /> the date the job is posted
 <location /> the location the position is for
 <jobType /> the job type
 <id />  The internal ID of the job - not to be displayed, just returned with form post
 <item>   Repeating elements of information about a job - could be salary or other descriptions of the duties, etc..
 <name />  title or header of data about job
 <data /> data corresponding to name
 </item>

 </PositionInformation>

 */
