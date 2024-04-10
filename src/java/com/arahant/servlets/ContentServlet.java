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
import com.arahant.utils.FileSystemUtils;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  In order to connect an external URL to this servlet, see servletNames in
 *  com/arahant/utils/generators/wsconfiggen/WsConfigGen.java
 */
public class ContentServlet extends HttpServlet {

	private static final ArahantLogger logger = new ArahantLogger(ContentServlet.class);
	private static final long serialVersionUID = 1L;

	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//logger.info(request.getParameter("contentType"));
		//logger.info(request.getParameter("fileName"));

		String contentType = request.getParameter("contentType");
		String relFileName = request.getParameter("fileName").replace('\\', '/');
		int idx = relFileName.lastIndexOf('/');
		String fileName;
		if (idx == -1)
			fileName = relFileName;
		else
			fileName = relFileName.substring(idx + 1);


		if ("pdf".equals(contentType))
			response.setContentType("application/pdf");
		else if ("csv".equals(contentType))
			response.setContentType("application/ms-excel");
		else
			response.setContentType("application/octet-stream");

//		response.addHeader("Content-Disposition", "attachment; filename=\"" + request.getParameter("fileName") + "\"");
		response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		//	BufferedReader fr=new BufferedReader(new FileReader(com.arahant.utils.FileSystemUtils.getWorkingDirectory()+"/"+request.getParameter("fileName")));

		ServletOutputStream os = null;
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(FileSystemUtils.getWorkingDirectory() + "/" + relFileName);
			os = response.getOutputStream();

			byte[] data = new byte[1024];
			int read;
			while ((read = fis.read(data)) != -1)
				os.write(data, 0, read);
		} catch (IOException e) {
			logger.error(e);
		} finally {
			if (os != null) {
				os.flush();
				os.close();
			}
			if (fis != null)
				fis.close();
		}
		/*
		 * PrintWriter out = response.getWriter(); try {
		 *
		 * String line=fr.readLine(); //	logger.info(line); while (line!=null) {
		 * out.println(line); line=fr.readLine(); }
		 *
		 * fr.close(); } finally { out.close(); }
		 *
		 */
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
