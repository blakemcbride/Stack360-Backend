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

import com.arahant.beans.ApplicantQuestion;
import com.arahant.beans.ApplicantQuestionChoice;
import com.arahant.business.BApplicant;
import com.arahant.business.BApplicantAnswer;
import com.arahant.business.BApplicantQuestion;
import com.arahant.business.BApplicantQuestionChoice;
import com.arahant.business.BProperty;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *  In order to connect an external URL to this servlet, see servletNames in
 *  com/arahant/utils/generators/wsconfiggen/WsConfigGen.java
 */
public class FreeSystemSignUpServlet extends HttpServlet {

	private static final ArahantLogger logger = new ArahantLogger(FreeSystemSignUpServlet.class);
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
			HibernateSessionUtil hsu = ArahantSession.openHSU();
			hsu.beginTransaction();

			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory(0, FileSystemUtils.createTempDirFile(null)); // use files for everything
			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
			@SuppressWarnings("unchecked")
			List<FileItem> allFileItems = servletFileUpload.parseRequest(request); // this will generate temp files
			Iterator<FileItem> iterator = allFileItems.iterator();
			HashMap<String, String> nameValuePairs = new HashMap<String, String>();

			// tell our disk factory we will do file cleanup
			diskFileItemFactory.setFileCleaningTracker(null);
			while (iterator.hasNext()) {
				FileItem fileItem = iterator.next();

				nameValuePairs.put(fileItem.getFieldName(), fileItem.getString());
			}

			String fname = nameValuePairs.get("fname");
			String lname = nameValuePairs.get("lname");
			String email = nameValuePairs.get("email");
			String workPhone = nameValuePairs.get("workPhone");
			String companyName = nameValuePairs.get("companyName");
			String address1 = nameValuePairs.get("address1");
			String address2 = nameValuePairs.get("address2");
			String city = nameValuePairs.get("city");
			String state = nameValuePairs.get("state");
			String zip = nameValuePairs.get("zip");
			String employeeCount = nameValuePairs.get("employeeCount");
			String website = nameValuePairs.get("website");

//			BProspectCompany bp = new BProspectCompany();
//			bp.create();
//			bp.setName(companyName);
//			bp.setSourceId(BProspectSource.findOrMake("Free System SignUp"));
//			bp.setStatusId(BProspectStatus.findOrMake("Requested Free System"));
//			bp.setAddedDate(new Date());
//			bp.setMainContactFname(fname);
//			bp.setMainContactLname(lname);
//			bp.setMainContactLogin(fname.charAt(0) + lname);
//			bp.setMainContactPassword("password", true);
//			bp.setMainContactPersonalEmail(email);
//			bp.setMainContactWorkPhone(workPhone);
//			bp.insert();
//			ArahantSession.getHSU().flush();

			//BProspectContact pc = new BProspectContact(bp.getMainContactPersonId());
			//BEmployee be = new BEmployee(ArahantSession.getHSU().createCriteria(Employee.class).eq(Employee.FNAME, "Blake").eq(Employee.LNAME, "McBride").first());

			String message = "Free System Request for " + companyName + "\n\n";
			message += "   Contact Info" + "\n";
			message += "           First Name: " + fname + "\n";
			message += "            Last Name: " + lname + "\n";
			message += "                Email: " + email + "\n";
			message += "           Work Phone: " + workPhone + "\n\n";
			message += "   Company Info" + "\n";
			message += "            Address 1: " + address1 + "\n";
			message += "            Address 2: " + address2 + "\n";
			message += "                 City: " + city + "\n";
			message += "                State: " + state + "\n";
			message += "                  Zip: " + zip + "\n";
			message += "       Employee Count: " + employeeCount + "\n";
			message += "              Website: " + website + "\n";

			//BMessage.send(pc.getPerson(), be.getEmployee(), "Free System Request - " + companyName,  message);
			//Mail.send(email, be.getPersonalEmail(), "Free System Request - " + companyName, message);
			System.out.println(message);
			//Mail.send(email, be.getPersonalEmail(), "Free System Request - " + companyName, message);

			ArahantSession.getHSU().commitTransaction();
		} catch (Exception e) {
			ArahantSession.getHSU().rollbackTransaction();
			logger.error(e);
			throw new ServletException(e);
		} finally {
			ArahantSession.clearSession();
		}
	}

	private void checkQuestion(String property, String question) {
		String id = BProperty.get(property);
		if (id == null || id.equals("")) {
			BApplicantQuestion baq = new BApplicantQuestion();
			baq.create();
			baq.setQuestion(question);
			baq.setInternalUse(false);
			baq.setAnswerType(ApplicantQuestion.TYPE_STRING);
			baq.insert();

			BProperty prop = new BProperty(property);
			prop.setValue(baq.getId());
			prop.update();
		}
	}

	private void setAnswer(BApplicant bap, String property, String answer) {
		String id = BProperty.get(property);
		if (id != null && !id.equals("")) {
			BApplicantAnswer bans = new BApplicantAnswer();
			bans.create();
			bans.setApplicant(bap);
			bans.setAnswer(answer);
			bans.setQuestionId(BProperty.get(property));
			bans.insert();
		}
	}

	private void setAnswerChoice(BApplicant bap, String property, String description) {
		String id = BProperty.get(property);

		ApplicantQuestionChoice aqc = ArahantSession.getHSU().createCriteria(ApplicantQuestionChoice.class)
				.eq(ApplicantQuestionChoice.DESCRIPTION, description)
				.first();
		BApplicantQuestionChoice baqc = new BApplicantQuestionChoice(aqc);
		if (aqc == null) {
			baqc.create();
			baqc.setDescription(description);
			baqc.setQuestion(new BApplicantQuestion(BProperty.get(property)).getBean());
			baqc.insert();

		}
		if (id != null && !id.equals("")) {
			BApplicantAnswer bans = new BApplicantAnswer();
			bans.create();
			bans.setApplicant(bap);
			bans.setAnswer(ApplicantQuestion.TYPE_LIST + "", "", 0, baqc.getBean().getApplicantQuestionChoiceId());
			bans.setQuestionId(BProperty.get(property));
			bans.insert();
		}
	}

	protected boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
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
