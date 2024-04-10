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

import com.arahant.beans.ProphetLogin;
import com.arahant.business.BEmployee;
import com.arahant.business.BFormType;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.FileSystemUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *  Process W4 PDF form update Employee personal and W4 form info upload sealed
 *  PDF to database
 *
 *  In order to connect an external URL to this servlet, see servletNames in
 *  com/arahant/utils/generators/wsconfiggen/WsConfigGen.java
 */
public class W4PDFFormServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String PDF_OutputDirectory = "/SignedPDF/";

	private File createSignedPDFFolder(String prefix) {
		try {
			//create the directory to put the filled in PDF files
			File reportDir = new File(FileSystemUtils.getWorkingDirectory(), PDF_OutputDirectory);
			if (!reportDir.exists())
				reportDir.mkdir();
			//create the output PDF
			if (prefix.isEmpty())
				prefix = "signedPDF";
			File outputPDF = File.createTempFile(prefix, ".pdf", reportDir);
			return outputPDF;
		} catch (IOException ex) {
			Logger.getLogger(W4PDFFormServlet.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private void updateEmployeeW4(AcroFields pdfFormFields) {
		ArahantSession.openHSU().beginTransaction();
		BEmployee emp = new BEmployee(pdfFormFields.getField("fld_employeeId"));
		if (pdfFormFields.getField("fld_exemptions").length() > 0)
			emp.setFederalExemptions(Integer.parseInt(pdfFormFields.getField("fld_exemptions")));
		if (pdfFormFields.getField("fld_additionalAmount").length() > 0)
			emp.setFederalExtraWithheld(Double.parseDouble(pdfFormFields.getField("fld_additionalAmount")));

		emp.setExempt(pdfFormFields.getField("fld_exempt").equalsIgnoreCase("exempt") ? true : false);
		if (pdfFormFields.getField("fld_single").equalsIgnoreCase("yes"))
			emp.setMaritalStatus("S");
		else if (pdfFormFields.getField("fld_married").equalsIgnoreCase("yes"))
			emp.setMaritalStatus("M");
		else if (pdfFormFields.getField("fld_married2").equalsIgnoreCase("yes"))
			emp.setMaritalStatus("H");

		if (pdfFormFields.getField("fld_lastNameDifferent").equalsIgnoreCase("yes"))
			emp.getEmployee().setW4nameDiffers(('Y'));

		//update peronal info
		emp.setLastName(pdfFormFields.getField("fld_lastName"));
		emp.setFirstName(pdfFormFields.getField("fld_firstName"));
		emp.setCity(pdfFormFields.getField("fld_city"));
		emp.setState(pdfFormFields.getField("fld_state"));
		emp.setZip(pdfFormFields.getField("fld_zip"));
		emp.setStreet(pdfFormFields.getField("fld_street1"));
		emp.setSsn(pdfFormFields.getField("fld_ssn1") + "-" + pdfFormFields.getField("fld_ssn2") + "-" + pdfFormFields.getField("fld_ssn3"));
		emp.update();
		ArahantSession.getHSU().commitTransaction();
		ArahantSession.clearSession();
	}

	private ProphetLogin getArahantLogin() {
		return ArahantSession.getHSU().createCriteria(ProphetLogin.class).eq(ProphetLogin.USERLOGIN, ArahantSession.systemName()).first();
	}

	private void uploadFileToServer(File file, AcroFields pdfFormFields) {
		try {
			//save the pdf file to database
			ProphetLogin login = getArahantLogin();
			FileUploadServlet fs = new FileUploadServlet();
			String user = login.getUserLogin(); // pdfFormFields.getField("fld_user");
			String password = login.getUserPassword();// pdfFormFields.getField("fld_password");
			String personId = pdfFormFields.getField("fld_employeeId");
			String comment = "Electronically signed W4 form";
			String extension = "pdf";
			String formTypeId = BFormType.findOrMake("SignW4PDF", comment);//pdfFormFields.getField("fld_formTypeId");
			//String formTypeId = pdfFormFields.getField("fld_formTypeId");

			HashMap<String, String> nameValuePairs = new HashMap<String, String>();
			nameValuePairs.put("formTypeId", formTypeId);
			nameValuePairs.put("personId", personId);
			nameValuePairs.put("comments", comment);
			nameValuePairs.put("extension", extension);

			List<File> pdfFile = new ArrayList<File>();
			pdfFile.add(file);
			fs.processUploadRequest("hrFormUpload", user, password, nameValuePairs, pdfFile, null, null);
			//delete the file
			file.delete();
		} catch (Exception ex) {
			Logger.getLogger(W4PDFFormServlet.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void sealPDF(String inputFilename) {
		try {
			//basically load the PDF and setFormFlattening(true);
			PdfReader reader = new PdfReader(inputFilename);
			AcroFields pdfFormFields = reader.getAcroFields();
			String prefix = pdfFormFields.getField("fld_lastName") + pdfFormFields.getField("fld_firstName") + pdfFormFields.getField("fld_employeeId") + "_";
			File outputPDF = createSignedPDFFolder(prefix);
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(outputPDF));
			pdfFormFields.removeField("Submit");
			pdfFormFields.removeField("fld_calculate");
			stamp.setFormFlattening(true);//this seals it            
			stamp.close();
			File fileToDelete = new File(inputFilename);
			fileToDelete.delete();
			updateEmployeeW4(pdfFormFields);
			uploadFileToServer(outputPDF, pdfFormFields);
		} catch (DocumentException ex) {
			Logger.getLogger(W4PDFFormServlet.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(W4PDFFormServlet.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File pdfOut = createSignedPDFFolder("");
		//Here request is the reference of HttpServletRequest.
		BufferedInputStream is = new BufferedInputStream(request.getInputStream());
		OutputStream os = new FileOutputStream(pdfOut);
		byte[] buffer = new byte[4096];
		int bytesRead;
		while ((bytesRead = is.read(buffer)) != -1)
			os.write(buffer, 0, bytesRead);
		is.close();
		os.close();

		//return response
		response.setContentType("text/html"); // Code 1
		PrintWriter out = response.getWriter();
		out.println("Thank you for filling out your W4 Form.");
		sealPDF(pdfOut.getAbsolutePath());
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public static void main(String[] args) {
		W4PDFFormServlet pdf = new W4PDFFormServlet();
		File f = new File("test.pdf");
		AcroFields pdfFormFields = null;
		pdf.uploadFileToServer(f, pdfFormFields);
	}
}
