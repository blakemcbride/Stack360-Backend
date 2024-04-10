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

import com.arahant.utils.ArahantLogger;
import com.arahant.utils.generators.flexWSClient.DynamicWebServiceClientGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClientStubGeneratorServlet extends HttpServlet {

	private static final transient ArahantLogger logger = new ArahantLogger(ClientStubGeneratorServlet.class);
	private static final long serialVersionUID = 5021628855839079563L;

	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String screenType = request.getParameter("screenType"); // standard or custom for now
		String screenClientOrModule = request.getParameter("screenClientModule"); // e.g. project, williamsonCounty
		String screenFolder = request.getParameter("screenFolder"); // e.g. projectParent, orgGroupProjectList

		// TESTING - http://localhost:8084/Arahant/ClientStubGeneratorServlet?screenType=standard&screenClientModule=project&screenFolder=orgGroupProjectList
		System.out.println("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());

		File jarFile = null;
		try {
			jarFile = new DynamicWebServiceClientGenerator().generate(screenType, screenClientOrModule, "http://localhost:" + request.getServerPort() + request.getContextPath() + "/", screenFolder);
			//jarFile=new DynamicWebServiceClientGenerator(null).generate(screenType, screenClientOrModule, "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/", screenFolder);
		} catch (Throwable t) {
			logger.error(t);
			throw new ServletException();
		}
		String fileName = jarFile.getName();

		// write the response jar file
		response.setContentType("application/jar");
		response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		// returning raw binary data, no need to base64 encode since we are not wrapping it up in XML
		FileInputStream fis = new FileInputStream(jarFile);
		ServletOutputStream out = response.getOutputStream();
		try {
			byte[] bytes = new byte[1024 * 4]; // 4 k at a time
			int bytesRead;

			while ((bytesRead = fis.read(bytes)) != -1)
				out.write(bytes, 0, bytesRead);
		} catch (Exception e) {
			response.setStatus(500);
		} finally {
			try {
				fis.close();
			} catch (Exception ignored) {
			}
			try {
				out.flush();
			} catch (Exception ignored) {
			}
		}

		try {
			jarFile.delete();
		} catch (Exception ignored) {
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
		return "Short description";
	}// </editor-fold>
}
