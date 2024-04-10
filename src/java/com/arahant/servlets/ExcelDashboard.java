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


package com.arahant.servlets;

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.beans.Timesheet;
import com.arahant.business.BEmployee;
import com.arahant.business.BProject;
import com.arahant.business.BTimesheet;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  In order to connect an external URL to this servlet, see servletNames in
 *  com/arahant/utils/generators/wsconfiggen/WsConfigGen.java
 */
public class ExcelDashboard extends HttpServlet {
	
	static ArahantLogger logger = new ArahantLogger(ExcelDashboard.class);

	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
//		response.setContentType("text;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String pw = request.getParameter("pw");  //     URL?pw=abc&val=hhhjjj then pw="abc"
			if (pw == null  ||  !pw.equals("mypassword"))
				return;
			String val = request.getParameter("val");  //  val="hhhjjj"
			int n = 0;
			
			HibernateSessionUtil hsu = ArahantSession.openHSU(false);
			hsu.setCurrentPersonToArahant();
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, "00001-0000000001"));
			
			HibernateCriteriaUtil<Project> hcu = hsu.createCriteria(Project.class);
			hcu.orderBy(Project.PROJECTNAME);
			HibernateScrollUtil<Project> scr = hcu.scroll();
			int nts;
			int begdate = DateUtils.addDays(DateUtils.now(), -90);
			StringBuilder sb = new StringBuilder();

			out.println("<data>");
			while (scr.next()  &&  n++ < 20000) {
				Project p = scr.get();
				Set<Timesheet> timesheets = p.getTimesheets();
				nts = 0;
				
				for (Timesheet ts : timesheets) {
										
					if (ts.getWorkDate() < begdate)
						continue;
					try {
						Person per = ts.getPerson();
						BEmployee e = new BEmployee(per.getPersonId());
						BTimesheet bts = new BTimesheet(ts);
						BProject bp = bts.getProject();

						sb.append("\t<record>\n");
						sb.append("\t\t<Date>" + DateUtils.getDateFormatted(ts.getWorkDate()) + "</Date>\n");
						sb.append("\t\t<Resource>" + per.getNameLFM() + "</Resource>\n");
						sb.append("\t\t<ResourceType>" + (e.getEmploymentType() =='E' ? "W-2" : "1099") + "</ResourceType>\n");
						sb.append("\t\t<BillRate>" + Utils.Format(bts.getBillingRate(), "", 0, 2) + "</BillRate>\n");

						sb.append("\t\t<PayRate>" + Utils.Format(e.getHourlyRate(), "", 0, 2) + "</PayRate>\n");
//						bp.getAssociatedBenefitConfigs();
						Set<HrBenefitConfig> configs = bp.getBenefitConfigs();
						if (bts.getBillable() == 'Y') {
							sb.append("\t\t<NonBillableHours>" + "0.00" + "</NonBillableHours>\n");
							sb.append("\t\t<BillableHours>" + Utils.Format(ts.getTotalHours(), "", 0, 2) + "</BillableHours>\n");
							sb.append("\t\t<PTOHours>" + "0.00" + "</PTOHours>\n");
						} else {
							if (configs.isEmpty())
								sb.append("\t\t<NonBillableHours>" + Utils.Format(ts.getTotalHours(), "", 0, 2) + "</NonBillableHours>\n");
							else
								sb.append("\t\t<NonBillableHours>" + "0.00" + "</NonBillableHours>\n");
							sb.append("\t\t<BillableHours>" + "0.00" + "</BillableHours>\n");
							if (configs.isEmpty())
								sb.append("\t\t<PTOHours>" + "0.00" + "</PTOHours>\n");
							else
								sb.append("\t\t<PTOHours>" + Utils.Format(ts.getTotalHours(), "", 0, 2) + "</PTOHours>\n");
						}
						
						sb.append("\t\t<ProjectName>" + p.getDescription() + "</ProjectName>\n");
						sb.append("\t\t<ProjectNumber>" + p.getProjectName() + "</ProjectNumber>\n");
						sb.append("\t\t<Estimated>" + Utils.Format(p.getEstimateHours(), "", 0, 1) + "</Estimated>\n");
						sb.append("\t</record>\n");

						out.println(sb);
						nts++;
					} catch (Exception e) {
						logger.error(e);
					} finally {
						sb.setLength(0);
					}
				}
				if (nts == 0) try {
					sb.append("\t<record>");
					sb.append("\t\t<Date>" + "" + "</Date>");
					sb.append("\t\t<Resource>" + "" + "</Resource>");
					sb.append("\t\t<ResourceType>" + "" + "</ResourceType>");
					sb.append("\t\t<BillRate>" + "" + "</BillRate>");
					
					sb.append("\t\t<PayRate>" + "" + "</PayRate>");
					sb.append("\t\t<NonBillableHours>" + "0.00" + "</NonBillableHours>");
					sb.append("\t\t<BillableHours>" + "0.00" + "</BillableHours>");
					sb.append("\t\t<PTOHours>" + "0.00" + "</PTOHours>");
					sb.append("\t\t<ProjectName>" + p.getDescription() + "</ProjectName>");
					sb.append("\t\t<ProjectNumber>" + p.getProjectName() + "</ProjectNumber>");
					sb.append("\t\t<Estimated>" + Utils.Format(p.getEstimateHours(), "", 0, 1) + "</Estimated>");
					sb.append("\t</record>");
					out.println(sb);
				} catch (Exception e) {
					logger.error(e);
				} finally {
					sb.setLength(0);
				}
			}
			out.println("</data>");
		} catch (Exception e) {
			logger.error(e);
		} finally {			
			out.close();
			ArahantSession.closeHSU();
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP
	 * <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
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
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
