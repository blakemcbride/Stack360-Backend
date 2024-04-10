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

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.genericImport.GenericImport;
import com.arahant.imports.*;
import com.arahant.imports.census.DataImport;
import com.arahant.imports.census.DependentImport;
import com.arahant.imports.census.StandardMap;
import com.arahant.imports.glaggs.AgentImport;
import com.arahant.imports.williamsonCounty.SalaryImport;
import com.arahant.imports.williamsonCounty.SalaryImport2;
import com.arahant.services.ArahantLoginException;
import com.arahant.services.ServiceBase;
import com.arahant.services.TransmitInputBase;
import com.arahant.services.main.UserCache;
import com.arahant.services.utils.imports.SheaklyActivityImport;
import com.arahant.services.utils.imports.SheaklyProspectImport;
import com.arahant.services.utils.imports.VisitorTrackImport;
import com.arahant.utils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;
import org.kissweb.StringUtils;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

/**
 *  In order to connect an external URL to this servlet, see servletNames in
 *  com/arahant/utils/generators/wsconfiggen/WsConfigGen.java
 */
public class FileUploadServlet extends HttpServlet {
	private static final ArahantLogger logger = new ArahantLogger(FileUploadServlet.class);
	private static final long serialVersionUID = 1L;

	private final JSONObject responseJsonObject = new JSONObject();
	private boolean respondOk = true;
	private boolean loginAlreadyValidated = false;

	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			DiskFileItemFactory diskFileItemFactory;
			ServletFileUpload servletFileUpload;
			List<FileItem> allFileItems;
			final List<File> files = new ArrayList<>();
			final List<ClientFile> clientFiles = new ArrayList<>();
			Iterator<FileItem> iterator;
			String uploadType = null;
			String userId = null;
			String password = null;
			String uuid = null;
			final HashMap<String, String> nameValuePairs = new HashMap<>();

			/*
			 * There is some unexpected behavior with respect to the hsu.  The existing code is meant to correctly handle it.
			 * See processUploadRequest (which is called externally)
			 */
			ArahantSession.openHSU();

			diskFileItemFactory = new DiskFileItemFactory(0, FileSystemUtils.createTempDirFile(null)); // use files for everything
			servletFileUpload = new ServletFileUpload(diskFileItemFactory);
			try {
				allFileItems = servletFileUpload.parseRequest(request); // this will generate temp files
			} catch (Exception e) {
//				logger.error(e);
				throw new ArahantWarning(e);
			}
			iterator = allFileItems.iterator();

			// tell our disk factory we will do file cleanup
			diskFileItemFactory.setFileCleaningTracker(null);

			// spin through file items and separate out the file items from the name value pairs and get the uploadType
			while (iterator.hasNext()) {
				FileItem fileItem = iterator.next();

				if (fileItem.isFormField())
					if (fileItem.getFieldName().equalsIgnoreCase("Filename")) {
						String ext = "";
						try {
							String fname = fileItem.getString();
							if (fname.lastIndexOf('.') == -1)
								if (fname.length() < 10)
									ext = fname;
								else
									ext = fname.substring(0, 10);
							else
								ext = fname.substring(fname.lastIndexOf('.') + 1);
						} catch (Exception e) {
							//extension must be bad so we'll insert ""
						}
						nameValuePairs.put("extension", ext);

					} else if (fileItem.getFieldName().equalsIgnoreCase("uploadType"))
						uploadType = fileItem.getString();
					else if (fileItem.getFieldName().equalsIgnoreCase("user"))
						userId = fileItem.getString();
					else if (fileItem.getFieldName().equalsIgnoreCase("password"))
						password = fileItem.getString();
                    else if (fileItem.getFieldName().equalsIgnoreCase("uuid"))
                        uuid = fileItem.getString();
					else
						nameValuePairs.put(fileItem.getFieldName(), fileItem.getString());
				else {
					DiskFileItem diskFileItem = (DiskFileItem) fileItem;
					File file = FileSystemUtils.moveToTempFile(diskFileItem.getStoreLocation(), "fileUpload", ".dat");
					files.add(file);
					clientFiles.add(new ClientFile(file, fileItem.getName()));
				}
			}

			if ("taskPictureUpload-worker".equals(uploadType)) {
				// worker interface, alt authentication
				try {
					ServiceBase sb = new ServiceBase();
					sb.checkLogin(new TransmitInputBase(
							userId,
							password,
							uuid,
							"",
							false,
							"HTML", "WORKER"),
							UserCache.LoginType.WORKER);
				} catch (Exception e) {
					throw new Exception("Invalid login");
				}
				UserCache.UserData ud = UserCache.findUuid(uuid);
				if (ud != null) {
					userId = ud.username;
					password = ud.password;
				} else
					throw new ArahantException("Invalid login");
				ArahantSession.getHSU().setCurrentPerson(new BPerson(ud.personId).getPerson());
				uploadType = "taskPictureUpload";
			} else {
				UserCache.UserData ud = UserCache.findUuid(uuid);
				if (ud != null) {
					userId = ud.username;
					password = ud.password;
				} else
					throw new ArahantWarning("Please logout and back in."); // system was rebooted while someone was in the system
				if (uploadType == null)
					throw new Exception("No uploadType specified");
				else if (userId == null || userId.isEmpty())
					throw new Exception("Invalid login");
				else if (password == null)
					throw new Exception("Invalid login");
				ArahantSession.getHSU().setCurrentPerson(new BPerson(ud.personId).getPerson());
			}
			loginAlreadyValidated = true;

            processUploadRequest(uploadType, userId, password, nameValuePairs, files, clientFiles, response);

            for (File f : files)
            	f.delete();
			responseJsonObject.put("wsStatus", "0");
			responseJsonObject.put("wsMessage", "");
		} catch (Throwable e) {
			responseJsonObject.put("wsStatus", "1");
			responseJsonObject.put("wsMessage", e.getMessage());
			// TODO LOG A MESSAGE TO THE USER
			if (!(e instanceof ArahantWarning)) {
				//logger.error(e);
				throw new ServletException(e);
			}
		} finally {
			if (respondOk)
				try {
					response.getOutputStream().print(responseJsonObject.toString());
				} catch (IOException e) {
					//throw new ArahantException(e);
				}
			ArahantSession.clearSession();
		}
	}

	/*
	 * FileUploadServlet
	 *
	 * hrFormUpload (add) params: formTypeId (string) remarks: i am planning on
	 * sending up pdf files only, but if user selects a non-pdf it is up to you
	 * what to do - i think it ultimately needs to get stored as a PDF because
	 * of the split/combine/separate operations - you can either complain or,
	 * for instance, if it is an image, convert it to a PDF - the upload should
	 * be synchronous, meaning i want to re-list the forms and see it in the
	 * list when this call returns
	 *
	 * projectFormUpload (add) params: formTypeId (string) remarks: same as
	 * above remarks
	 *
	 */
	protected void processUploadRequest(String uploadType, String userName, String password, HashMap<String, String> nameValuePairs, List<File> files, List<ClientFile> clientFiles, HttpServletResponse response)
			throws Exception {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (hsu == null  ||  !hsu.isOpen())
			hsu = ArahantSession.openHSU();

		if (!loginAlreadyValidated && !BProphetLogin.checkPassword(userName, password)) {
			throw new ArahantLoginException(("Failed login"));
		}

		if (uploadType.equalsIgnoreCase("benefitPriceUpload")) {
			BenefitPriceUploadRunner runner = new BenefitPriceUploadRunner();
			runner.configId = nameValuePairs.get("configId");
			runner.ssnColumn = nameValuePairs.get("ssnColumn");
			runner.priceColumn = nameValuePairs.get("priceColumn");
			runner.effectiveDate = nameValuePairs.get("effectiveDate");
			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("projectViewUpload")) {
			ProjectViewUploadRunner runner = new ProjectViewUploadRunner();
			runner.locationType = nameValuePairs.get("locationType");
			runner.locationTypeRelativeToId = nameValuePairs.get("locationTypeRelativeToId");
			runner.parentId = nameValuePairs.get("parentId");
			runner.importType = Integer.parseInt(nameValuePairs.get("importType"));
			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("employeeOrderUpload")) {
			EmployeeOrderUploadRunner runner = new EmployeeOrderUploadRunner();
			runner.employeeIdColumn = nameValuePairs.get("employeeIdColumn");
			runner.orderColumn = nameValuePairs.get("orderColumn");
			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("userAgreementFormUpload")) {
			userAgreementFormUpload(hsu, nameValuePairs, files);
		} else if (uploadType.equalsIgnoreCase("createMessage")) {
			createMessage(hsu, nameValuePairs, clientFiles);
		} else if (uploadType.equalsIgnoreCase("hrFormUpload")) {
			hsu.beginTransaction();
			String formTypeId = nameValuePairs.get("formTypeId");
			String personId = nameValuePairs.get("personId");
			String comments = nameValuePairs.get("comments");
			String extension = nameValuePairs.get("extension");
			String filename = nameValuePairs.get("myFilename");

			if (files.isEmpty())
				return;

			File fyle = new File(files.get(0).getAbsolutePath());
			byte[] formData = new byte[(int) fyle.length()];
			try (FileInputStream fis = new FileInputStream(fyle)) {
				fis.read(formData);
			}

			// Reduce image size to roughly 3000 pixels along the longest dimension
			formData = Image.resizeImage(formData, extension, 3000);

			BPersonForm pf = new BPersonForm();
			pf.create();
			pf.setComments(comments);
			pf.setFormTypeId(formTypeId);
			pf.setPersonId(personId);
			pf.setFormDate(DateUtils.now());
			pf.setFormData(formData, extension);
			pf.setExtension(extension);
			pf.setSource(filename);
			pf.insert();
			hsu.commitTransaction();
		} else if (uploadType.equalsIgnoreCase("reportGraphicUpload")) {
			hsu.beginTransaction();
			String description = nameValuePairs.get("description");
			double xPos = Double.parseDouble(nameValuePairs.get("xPos"));
			double yPos = Double.parseDouble(nameValuePairs.get("yPos"));
			String reportId = nameValuePairs.get("reportId");
			String extension = nameValuePairs.get("extension");
			String filename = nameValuePairs.get("myFilename");

			if (files.isEmpty())
				return;

			File fyle = new File(files.get(0).getAbsolutePath());
			byte[] formData = new byte[(int) fyle.length()];
			try (FileInputStream fis = new FileInputStream(fyle)) {
				fis.read(formData);
			}

			// Reduce image size to roughly 3000 pixels along the longest dimension
			formData = Image.resizeImage(formData, extension, 3000);

			BReportGraphic x = new BReportGraphic();
			x.create();
			x.setDescription(description);
			x.setReportId(reportId);
			x.setXPos(xPos);
			x.setYPos(yPos);
			x.setGraphic(formData);
			x.insert();
			hsu.commitTransaction();
		} else if (uploadType.equalsIgnoreCase("projectFormUpload")) {
			hsu.beginTransaction();
			String formTypeId = nameValuePairs.get("formTypeId");
			String shiftId = nameValuePairs.get("shiftId");
			String comments = nameValuePairs.get("comments");
			String internal = nameValuePairs.get("internal");

			int n = files.size();
			for (int i=0 ; i < n ; i++) {

				File fyle = new File(files.get(i).getAbsolutePath());
				byte[] formData = new byte[(int) fyle.length()];
				try (FileInputStream fis = new FileInputStream(fyle)) {
					fis.read(formData);
				}
				String extension = nameValuePairs.get("extension-" + i);

				// Reduce image size to roughly 3000 pixels along the longest dimension
				formData = Image.resizeImage(formData, extension, 3000);

				BProjectForm x = new BProjectForm();
				x.create();
				x.setComments(comments);
				x.setFormTypeId(formTypeId);
				x.setProjectShiftId(shiftId);
				x.setFormDate(DateUtils.now());
				x.setExtension(extension);
				x.setInternal("false".equals(internal) ? 'N' : 'Y');
				x.insert();
				ExternalFile.saveData(ExternalFile.PROJECT_FORM_IMAGE, x.getId(), x.getExtension(), formData);
			}
			hsu.commitTransaction();
			BProjectForm.resetExportedForms(null, shiftId);
		} else if (uploadType.equalsIgnoreCase("tasklistUpload")) {
			hsu.beginTransaction();
			String formTypeId = nameValuePairs.get("formTypeId");
			String shiftId = nameValuePairs.get("shiftId");
			String comments = nameValuePairs.get("comments");
			String internal = nameValuePairs.get("internal");

			int n = files.size();
			for (int i = 0; i < n; i++) {

				File fyle = new File(files.get(i).getAbsolutePath());
				byte[] formData = new byte[(int) fyle.length()];
				try (FileInputStream fis = new FileInputStream(fyle)) {
					fis.read(formData);
				}
				String extension = nameValuePairs.get("extension-" + i);

				// Reduce image size to roughly 3000 pixels along the longest dimension
				formData = Image.resizeImage(formData, extension, 3000);

				BProjectForm x = new BProjectForm();
				x.create();
				x.setComments(comments);
				x.setFormTypeId(formTypeId);
				x.setProjectShiftId(shiftId);
				x.setFormDate(DateUtils.now());
				x.setExtension(extension);
				x.setInternal("false".equals(internal) ? 'N' : 'Y');
				x.insert();
				ExternalFile.saveData(ExternalFile.PROJECT_FORM_IMAGE, x.getId(), x.getExtension(), formData);
			}
			hsu.commitTransaction();
			BProjectForm.resetExportedForms(null, shiftId);
		} else if (uploadType.equalsIgnoreCase("taskPictureUpload")) {
			taskPictureUpload(hsu, nameValuePairs, files);
		} else if (uploadType.equalsIgnoreCase("receiptUpload")) {
			receiptUpload(hsu, nameValuePairs, files);
		} else if (uploadType.equalsIgnoreCase("workerWriteupUpload")) {
			workerWriteupUpload(hsu, nameValuePairs, files);
		} else if (uploadType.equalsIgnoreCase("workerAccidentUpload")) {
			workerAccidentUpload(hsu, nameValuePairs, files);
		} else if (uploadType.equalsIgnoreCase("workerAccidentUpload2")) {
			workerAccidentUpload2(hsu, nameValuePairs, files);
		} else if (uploadType.equalsIgnoreCase("comdataUpload")) {
			uploadComdata(hsu, nameValuePairs, files);
		} else if (uploadType.equalsIgnoreCase("companyFormUpload")) {
			companyFormUpload(hsu, nameValuePairs, files);
		} else if (uploadType.equalsIgnoreCase("companyLogoUpload")) {
			companyLogoUpload(hsu, nameValuePairs, files);
		} else if (uploadType.equalsIgnoreCase("boxMakerEmployeeImport")) {
			BoxMakerEmployeeImport runner = new BoxMakerEmployeeImport();

			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("agentFormUpload")) {
			agentFormUpload(hsu, nameValuePairs, files);
		} else if (uploadType.equalsIgnoreCase("benefitFileUpload")) {
			BenefitFileUploadRunner runner = new BenefitFileUploadRunner();
			runner.importTypeId = nameValuePairs.get("importTypeId");
			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("employeeFileUpload")) {
			EmpCensusUploadRunner runner = new EmpCensusUploadRunner();
			runner.companyId = nameValuePairs.get("currentCompanyId");
			runner.screenGroupId = nameValuePairs.get("screenGroupId");
			runner.securityId = nameValuePairs.get("securityGroupId");

			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("genericEmployeeFileUpload")) {
			
			//  this one is being done synchronously not asynchronously like the others
			
			respondOk = false;
			String companyId = nameValuePairs.get("currentCompanyId");
			String filename = files.get(0).getAbsolutePath();
			String importType = nameValuePairs.get("importType");
			BProphetLogin.checkPassword(userName, password);
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, companyId));
			
			int it;
			switch (importType.charAt(0)) {
				case 'A':
				default:
					it = com.arahant.genericImport.EmployeeImport.APPEND_MODE;
					break;
				case 'U':
					it = com.arahant.genericImport.EmployeeImport.UPDATE_MODE;
					break;
				case 'F':
					it = com.arahant.genericImport.EmployeeImport.FULL_MODE;
					break;
			}
			com.arahant.genericImport.EmployeeImport imp = new com.arahant.genericImport.EmployeeImport(filename, it);
			try {
				String outfile = imp.parse();
				response.getOutputStream().println(outfile);
//				sendFile(outfile, response);
			} catch (Exception ex) {
				logger.error(ex);
			}
		} else if (uploadType.equalsIgnoreCase("waytogoEmployeeFileUpload")) {
			
			//  this one is being done synchronously not asynchronously like the others
			
			respondOk = false;
			String companyId = nameValuePairs.get("contextCompanyId");
			String filename = files.get(0).getAbsolutePath();
			String importType = nameValuePairs.get("importType");
			BProphetLogin.checkPassword(userName, password);
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, companyId));
			
			int it;
			switch (importType.charAt(0)) {
				case 'A':
				default:
					it = com.arahant.imports.waytogo.EmployeeImport.APPEND_MODE;
					break;
				case 'U':
					it = com.arahant.imports.waytogo.EmployeeImport.UPDATE_MODE;
					break;
				case 'F':
					it = com.arahant.imports.waytogo.EmployeeImport.FULL_MODE;
					break;
			}
			com.arahant.imports.waytogo.EmployeeImport imp = new com.arahant.imports.waytogo.EmployeeImport(filename, it);
			try {
				String outfile = imp.parse();
				response.getOutputStream().println(outfile);
//				sendFile(outfile, response);
			} catch (Exception ex) {
				logger.error(ex);
			}
		} else if (uploadType.equalsIgnoreCase("waytogoApplicantFileUpload")) {
			
			//  this one is being done synchronously not asynchronously like the others
			
			respondOk = false;
			String companyId = nameValuePairs.get("contextCompanyId");
			String filename = files.get(0).getAbsolutePath();
			BProphetLogin.checkPassword(userName, password);
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, companyId));

			com.arahant.imports.waytogo.ApplicantImport imp = new com.arahant.imports.waytogo.ApplicantImport(filename, GenericImport.APPEND_MODE);
			try {
				String outfile = imp.parse();
				response.getOutputStream().println(outfile);
//				sendFile(outfile, response);
			} catch (Exception ex) {
				logger.error(ex);
			}

		} else if (uploadType.equalsIgnoreCase("dependentFileUpload")) {
			DepCensusUploadRunner runner = new DepCensusUploadRunner();
			runner.companyId = nameValuePairs.get("contextCompanyId");

			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("agentFileUpload")) {
			AgentImport ai = new AgentImport();
			try {
				ai.importFile(files.get(0).getAbsolutePath());
			} catch (Exception e) {
				logger.error(e);
			}
		} else if (uploadType.equalsIgnoreCase("StandardProspectFileUpload")) {
			StandardProspectUploadRunner runner = new StandardProspectUploadRunner();
			runner.companyId = nameValuePairs.get("contextCompanyId");
			runner.statusId = nameValuePairs.get("statusId");
			runner.sourceId = nameValuePairs.get("sourceId");
			runner.typeId = nameValuePairs.get("typeId");
			runner.currentPersonId = nameValuePairs.get("employeeId");
			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("VisitorTrackFileUpload")) {

			VisitorTrackUploadRunner runner = new VisitorTrackUploadRunner();
			runner.companyId = nameValuePairs.get("contextCompanyId");
			runner.statusId = nameValuePairs.get("statusId");

			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("SalesGenieFileUpload")) {
			SalesGenieUploadRunner runner = new SalesGenieUploadRunner();
			runner.companyId = nameValuePairs.get("contextCompanyId");

			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("ProspectActivityFileUpload")) {
			ProspectActivityUploadRunner runner = new ProspectActivityUploadRunner();
			runner.companyId = nameValuePairs.get("contextCompanyId");

			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("CustomWmCoSalaryImport1")) {
			WmCoSalaryCustom1UploadRunner runner = new WmCoSalaryCustom1UploadRunner();

			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("CustomWmCoSalaryImport2")) {
			WmCoSalaryCustom2UploadRunner runner = new WmCoSalaryCustom2UploadRunner();

			runner.filename = files.get(0).getAbsolutePath();
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("accrualImport")) {
			AccrualImport runner = new AccrualImport();

			runner.fileName = files.get(0).getAbsolutePath();
			runner.benefitId = nameValuePairs.get("benefitId");
			runner.configId = nameValuePairs.get("configId");
			runner.userId = userName;
			runner.password = password;
			runner.start();
		} else if (uploadType.equalsIgnoreCase("waytogoTimesheetUpload")) {

			//  I do not believe this is ever called!
			
			//  this one is being done synchronously not asynchronously like the others
			
			respondOk = false;
			String companyId = nameValuePairs.get("contextCompanyId");
			String filename = files.get(0).getAbsolutePath();
			BProphetLogin.checkPassword(userName, password);
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, companyId));
			
			com.arahant.imports.waytogo.TimesheetImport imp = new com.arahant.imports.waytogo.TimesheetImport(filename, com.arahant.imports.waytogo.EmployeeImport.APPEND_MODE);
			try {
				String outfile = imp.parse();
				response.getOutputStream().println(outfile);
//				sendFile(outfile, response);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}

	}

	private void companyLogoUpload(HibernateSessionUtil hsu, HashMap<String, String> nameValuePairs, List<File> files) throws IOException {
		hsu.beginTransaction();
		String companyId = nameValuePairs.get("companyId");
		BCompany bc = new BCompany(companyId);
		try {
			String extension = nameValuePairs.get("extension");
			String source = nameValuePairs.get("source");
			File fyle = new File(files.get(0).getAbsolutePath());
			byte[] formData = new byte[(int) fyle.length()];
			try (FileInputStream fis = new FileInputStream(fyle)) {
				fis.read(formData);
			}
			bc.setLogoData(formData);
			bc.setLogoExtension(extension);
			bc.setLogoSource(source);
		} catch (IOException | ArahantException iOException) {
			bc.setLogoData(null);
			bc.setLogoExtension(null);
			bc.setLogoSource(null);
		} finally {
			bc.update();
			hsu.commitTransaction();
		}
	}

	private void createMessage(HibernateSessionUtil hsu, HashMap<String, String> nameValuePairs, List<ClientFile> clientFiles) throws Exception {
		if (BProperty.getBoolean("DontUseMessageSystem"))
			return;
		String subject = nameValuePairs.get("subject");
		String message = nameValuePairs.get("message");
		String fromPersonId = nameValuePairs.get("fromPersonId");
		String toPersonIds = nameValuePairs.get("toPersonId");
		String ccPersonIds = nameValuePairs.get("ccPersonIds");
		String bccPersonIds = nameValuePairs.get("bccPersonIds");
		String backEndUrl = nameValuePairs.get("backEndUrl");
		boolean sendEmail = nameValuePairs.get("sendEmail").equals("true");
		boolean sendText = nameValuePairs.get("sendText").equals("true");
		boolean sendInternal = nameValuePairs.get("sendInternal").equals("true");
		boolean dontSendBody = nameValuePairs.get("dontSendBody").equals("true");
		SendSMSTwilio sm = null; // text message
		String detail = null;    // text message

		if (sendText) {
			sm = new SendSMSTwilio(false);
			if (sm.getStatus() != SendSMSTwilio.Status.OK)
				throw new ArahantException("Twilio is not configured. It is required to send text messages.");
			detail = subject == null ? "" : subject;
			if (message != null && !message.isEmpty())
				detail += ": " + StringUtils.htmlToText(message);
		}

		final Connection db = hsu.getKissConnection();

		Record fprec = db.fetchOne("select personal_email, fname, lname from person where person_id=?", fromPersonId);
		String fromEmail = fprec.getString("personal_email");

		String name = fprec.getString("fname");
		if (name == null)
			name = "";
		if (!name.isEmpty())
			name += " ";
		String lname = fprec.getString("lname");
		if (lname != null && !lname.isEmpty())
			name += lname;

		if (sendEmail && !Email.canSendEmail(fromEmail)) {
			sendEmail = false;
			logger.warn("User " + name + " is attempting to send email but cannot.");
		}

		Record rec = db.newRecord("message");
		IDGenerator.generate(rec, "message_id");
		String messageId = rec.getString("message_id");
		rec.set("message", message);
		rec.set("from_person_id", fromPersonId);
		rec.setDateTime("created_date", new Date());
		rec.set("subject", subject);
		rec.set("from_address", fromEmail);
		rec.set("from_name", name);
		rec.set("dont_send_body", dontSendBody ? "Y" : "N");
		rec.set("send_email", sendEmail ? "Y" : "N");
		rec.set("send_text", sendText ? "Y" : "N");
		rec.set("send_internal", sendInternal ? "Y" : "N");
		rec.addRecord();

		List<EmailAttachment> attachments = new ArrayList<>();
		for (ClientFile clientFile : clientFiles) {
			byte [] data = new byte[(int) clientFile.file.length()];
			try (FileInputStream fis = new FileInputStream(clientFile.file)) {
				fis.read(data);
			}
			rec = db.newRecord("message_attachment");
			String messageAttachmentId = IDGenerator.generate(rec, "message_attachment_id");
			rec.set("message_id", messageId);
			rec.set("source_file_name", clientFile.clientFileName);
			ExternalFile.saveData(ExternalFile.MESSAGE_ATTACHMENT_ATTACHMENT, messageAttachmentId, clientFile.clientFileName, data);
			rec.addRecord();

			attachments.add(new EmailAttachment(clientFile.file.getAbsolutePath(), clientFile.clientFileName));
		}

		for (String toPersonId : StringUtils.split(toPersonIds, ",")) {
			rec = db.newRecord("message_to");
			IDGenerator.generate(rec, "message_to_id");
			rec.set("message_id", messageId);
			rec.set("to_person_id", toPersonId);
			rec.set("send_type", "T");
			rec.set("to_show", "Y");
			rec.set("sent", sendEmail ? "Y" : "N");

			if (sendText)
				sm.sendSMStoPerson(toPersonId, detail, attachments, backEndUrl);

			Record prec = db.fetchOne("select personal_email, fname, lname from person where person_id=?", toPersonId);
			if (prec != null) {
				String toadd = prec.getString("personal_email");
				rec.set("to_address", toadd);
				if (sendEmail && !Email.isValidDomain(toadd))
					rec.set("sent", "N");
				name = prec.getString("fname");
				if (name == null)
					name = "";
				lname = prec.getString("lname");
				if (lname != null && !lname.isEmpty())
					name += " " + lname;
				if (!name.isEmpty())
					rec.set("to_name", name);
				rec.addRecord();
			}
		}

		for (String ccPersonId : StringUtils.split(ccPersonIds, ",")) {
			rec = db.newRecord("message_to");
			IDGenerator.generate(rec, "message_to_id");
			rec.set("message_id", messageId);
			rec.set("to_person_id", ccPersonId);
			rec.set("send_type", "C");
			rec.set("to_show", "Y");
			rec.set("sent", sendEmail ? "Y" : "N");

			if (sendText)
				sm.sendSMStoPerson(ccPersonId, detail, attachments, backEndUrl);

			Record prec = db.fetchOne("select personal_email, fname, lname from person where person_id=?", ccPersonId);
			if (prec != null) {
				String toadd = prec.getString("personal_email");
				rec.set("to_address", toadd);
				if (sendEmail && !Email.isValidDomain(toadd))
					rec.set("sent", "N");
				name = prec.getString("fname");
				if (name == null)
					name = "";
				lname = prec.getString("lname");
				if (lname != null && !lname.isEmpty())
					name += " " + lname;
				if (!name.isEmpty())
					rec.set("to_name", name);
				rec.addRecord();
			}
		}

		for (String bccPersonId : StringUtils.split(bccPersonIds, ",")) {
			rec = db.newRecord("message_to");
			IDGenerator.generate(rec, "message_to_id");
			rec.set("message_id", messageId);
			rec.set("to_person_id", bccPersonId);
			rec.set("send_type", "B");
			rec.set("to_show", "Y");
			rec.set("sent", sendEmail ? "Y" : "N");

			if (sendText)
				sm.sendSMStoPerson(bccPersonId, detail, attachments, backEndUrl);

			Record prec = db.fetchOne("select personal_email, fname, lname from person where person_id=?", bccPersonId);
			if (prec != null) {
				String toadd = prec.getString("personal_email");
				rec.set("to_address", toadd);
				if (sendEmail && !Email.isValidDomain(toadd))
					rec.set("sent", "N");
				name = prec.getString("fname");
				if (name == null)
					name = "";
				lname = prec.getString("lname");
				if (lname != null && !lname.isEmpty())
					name += " " + lname;
				if (!name.isEmpty())
					rec.set("to_name", name);
				rec.addRecord();
			}
		}

		if (sendEmail) {
			if (dontSendBody) {
				String system = BProperty.get(StandardProperty.System_Name, ArahantSession.systemName());
				message = "You have a message waiting for you on the " + system + " system.";
				attachments = null;
			}
			BMessage.copyHTMLToEmail(fromPersonId, StringUtils.split(toPersonIds, ","), StringUtils.split(ccPersonIds, ","), StringUtils.split(bccPersonIds, ","), subject, message, attachments);
		}

		db.commit();

/*
		if (subject.indexOf("Task Failed") == -1 && BProperty.getBoolean(StandardProperty.SEND_MESSAGE_NOTIFICATIONS_BY_EMAIL))
			Mail.send(fromPerson, toPerson, subject, BProperty.get("MessageReceivedText", "You have received a message in the Arahant system."));
		else if (BProperty.getBoolean(StandardProperty.SEND_MESSAGES_BY_EMAIL))
			Mail.send(fromPerson, toPerson, subject, message);
 */
	}

	private void userAgreementFormUpload(HibernateSessionUtil hsu, HashMap<String, String> nameValuePairs, List<File> files) throws IOException {
		hsu.beginTransaction();
		String id = nameValuePairs.get("id");
		String comments = nameValuePairs.get("comments");
		String name = nameValuePairs.get("name");
		String extension = nameValuePairs.get("extension");
		String expire = nameValuePairs.get("expirationDate");
		if (expire == null || expire.trim().isEmpty())
			expire = "0";
		int expirationDate = Integer.parseInt(expire);

		if (id == null || id.trim().isEmpty()) {
			File fyle = new File(files.get(0).getAbsolutePath());
			byte[] formData = new byte[(int) fyle.length()];
			try (FileInputStream fis = new FileInputStream(fyle)) {
				fis.read(formData);
			}
			BAgreementForm x = new BAgreementForm();
			x.create();
			x.setSummary(comments);
			x.setFormData(formData);
			x.setExtension(extension);
			x.setName(name);
			x.setExpirationDate(expirationDate);
			x.insert();
		} else {
			File fyle = new File(files.get(0).getAbsolutePath());
			byte[] formData = new byte[(int) fyle.length()];
			try (FileInputStream fis = new FileInputStream(fyle)) {
				fis.read(formData);
			}
			BAgreementForm x = new BAgreementForm(id);
			x.setSummary(comments);
			x.setFormData(formData);
			x.setExtension(extension);
			x.setDate(new Date());
			x.setName(name);
			x.setExpirationDate(expirationDate);
			x.update();
		}
		hsu.commitTransaction();
	}

	private void taskPictureUpload(HibernateSessionUtil hsu, HashMap<String, String> nameValuePairs, List<File> files) throws Exception {
		hsu.beginTransaction();
		final String projectTaskDetailId = nameValuePairs.get("projectTaskDetailId");
		final String comments = nameValuePairs.get("comments");
		final Connection db = hsu.getKissConnection();

		final int n = files.size();
		for (int i = 0; i < n; i++) {

			File fyle = new File(files.get(i).getAbsolutePath());
			byte[] formData = new byte[(int) fyle.length()];
			try (FileInputStream fis = new FileInputStream(fyle)) {
				fis.read(formData);
			}
			String extension = nameValuePairs.get("extension-" + i);

			// Reduce image size to roughly 3000 pixels along the longest dimension
			formData = Image.resizeImage(formData, extension, 3000);

			Record pnrec = db.fetchOne("select picture_number " +
					"from project_task_picture " +
					"where project_task_detail_id = ? " +
					"order by picture_number desc", projectTaskDetailId);
			short pn = pnrec == null ? 1 : (short) (pnrec.getShort("picture_number") + 1);

			if (comments == null || comments.trim().isEmpty())
				logger.error("taskpictureUpload: Missing comments for project task picture - " + nameValuePairs.get("fromWhere"));

			Record rec = db.newRecord("project_task_picture");
			IDGenerator.generate(rec, "project_task_picture_id");
			rec.set("project_task_detail_id", projectTaskDetailId);
			rec.set("file_name_extension", extension);
			rec.set("comments", comments);
			rec.set("picture_number", pn);
			rec.set("who_uploaded", hsu.getCurrentPerson().getPersonId());

			String pictureFullPath = ExternalFile.makeExternalFilePath(ExternalFile.PROJECT_TASK_PICTURE, rec.getString("project_task_picture_id"), extension);
			org.kissweb.FileUtils.write(pictureFullPath, formData);

			rec.addRecord();
		}
		hsu.commitTransaction();
	}

	private void receiptUpload(HibernateSessionUtil hsu, HashMap<String, String> nameValuePairs, List<File> files) throws IOException {
		hsu.beginTransaction();
		String projectId = nameValuePairs.get("projectId");
		String comments = nameValuePairs.get("comments");
		String extension = nameValuePairs.get("extension");
		String personId = nameValuePairs.get("personId");
		double receiptAmount = Double.parseDouble(nameValuePairs.get("receiptAmount"));
		int receiptDate = Integer.parseInt(nameValuePairs.get("receiptDate"));
		String expenseAccountId = nameValuePairs.get("expenseAccountId");
		String paymentMethod = nameValuePairs.get("paymentMethod");
		File fyle = new File(files.get(0).getAbsolutePath());
		byte[] formData = new byte[(int) fyle.length()];
		try (FileInputStream fis = new FileInputStream(fyle)) {
			fis.read(formData);
		}

		// Reduce image size to roughly 3000 pixels along the longest dimension
		formData = Image.resizeImage(formData, extension, 3000);

		BExpenseReceipt er = new BExpenseReceipt();
		er.create();
		er.setPerson((new BPerson(personId)).getPerson());
		er.setProject((new BProject(projectId)).getBean());
		er.setExpenseAccount((new BExpenseAccount(expenseAccountId)).getBean());
		er.setReceiptDate(receiptDate);
		er.setWhenUploaded(new Date());
		if (comments != null && comments.length() > 80)
			comments = org.kissweb.StringUtils.take(comments, 80);
		er.setBusinessPurpose(comments);
		er.setAmount(receiptAmount);
		er.setFileType(extension);
		er.setWhoUploaded(hsu.getCurrentPerson());
		er.setPaymentMethod(paymentMethod.charAt(0));
		er.setApproved('N');
		er.insert();
		ExternalFile.saveData(ExternalFile.EXPENSE_RECEIPT_PICTURE1, er.getExpenseReceiptId(), extension, formData);
		hsu.commitTransaction();
	}

	private void companyFormUpload(HibernateSessionUtil hsu, HashMap<String, String> nameValuePairs, List<File> files) throws IOException {
		hsu.beginTransaction();
		String formTypeId = nameValuePairs.get("formTypeId");
		String folderId = nameValuePairs.get("folderId");
		if (StringUtils.isEmpty(folderId))
			folderId = BCompanyFormFolder.findOrMake("Company Documents");
		String comments = nameValuePairs.get("comments");
		String extension = nameValuePairs.get("extension");
		String companyId = nameValuePairs.get("companyId");
		String filename = nameValuePairs.get("myFilename");
		String requiresSignature = nameValuePairs.get("signatureRequired");
		int firstActiveDate = Integer.parseInt(nameValuePairs.get("firstActiveDate"));
		int lastActiveDate = Integer.parseInt(nameValuePairs.get("lastActiveDate"));
		File fyle = new File(files.get(0).getAbsolutePath());
		byte[] formData = new byte[(int) fyle.length()];
		try (FileInputStream fis = new FileInputStream(fyle)) {
			fis.read(formData);
		}
		BCompanyForm x = new BCompanyForm();
		x.create();
		x.setComments(comments);

		if (!StringUtils.isEmpty(requiresSignature))
			x.getBean().setElectronicSignature(requiresSignature.charAt(0));
		else
			x.getBean().setElectronicSignature('N');

		x.setFormTypeId(formTypeId);
		x.setFolderId(folderId);
		x.setFirstActiveDate(firstActiveDate);
		x.setLastActiveDate(lastActiveDate);
		x.setFormData(formData);
		x.setExtension(extension);
		x.setAllCompanies(true);  //the folder id will do it
		x.setForceAllCompanies(true);
		x.setSource(filename);
		x.insert();
		hsu.commitTransaction();
	}

	private void agentFormUpload(HibernateSessionUtil hsu, HashMap<String, String> nameValuePairs, List<File> files) throws IOException {
		hsu.beginTransaction();
		String formTypeId = nameValuePairs.get("formTypeId");
		String comments = nameValuePairs.get("comments");
		String extension = nameValuePairs.get("extension");
		String companyIds = nameValuePairs.get("companyIdsArray");
		int firstActiveDate = Integer.parseInt(nameValuePairs.get("firstActiveDate"));
		int lastActiveDate = Integer.parseInt(nameValuePairs.get("lastActiveDate"));
		File fyle = new File(files.get(0).getAbsolutePath());
		byte[] formData = new byte[(int) fyle.length()];
		try (FileInputStream fis = new FileInputStream(fyle)) {
			fis.read(formData);
		}
		BCompanyFormFolder bcff = new BCompanyFormFolder();
		String fid = bcff.create();
		bcff.setFolderName(hsu.getCurrentPerson().getNameLFM() + " " + DateUtils.getDate(DateUtils.now()) + " " + DateUtils.getTimeFormatted(DateUtils.nowTime()));
		bcff.insert();

		BCompanyForm x = new BCompanyForm();
		x.create();
		x.setComments(comments);
		x.setFormTypeId(formTypeId);
		x.setFolderId(fid);
		x.setFirstActiveDate(firstActiveDate);
		x.setLastActiveDate(lastActiveDate);
		x.setFormData(formData);
		x.setExtension(extension);
		x.insert();

		BAgent ba = new BAgent(hsu.getCurrentPerson().getPersonId());

		new BOrgGroup(ba.getOrgGroupId()).associateTo(fid);

		if (companyIds != null) {
			StringTokenizer stok = new StringTokenizer(companyIds, ",");
			while (stok.hasMoreTokens())
				new BOrgGroup(stok.nextToken()).associateTo(fid);
		} else
			for (BCompany bc : ba.getActiveAgentCompanies())
				new BOrgGroup(bc).associateTo(fid);

		hsu.commitTransaction();
	}

	private void workerWriteupUpload(HibernateSessionUtil hsu, HashMap<String, String> nameValuePairs, List<File> files) throws IOException {
		hsu.beginTransaction();
		String projectId = nameValuePairs.get("projectId");
		String extension = nameValuePairs.get("extension");
		String employeeId = nameValuePairs.get("employeeId");
		String supervisorId = nameValuePairs.get("supervisorId");
		int eventDate = Integer.parseInt(nameValuePairs.get("eventDate"));
		boolean employeeNotified = "true".equals(nameValuePairs.get("employeeNotified"));
		int dateNotfied = Integer.parseInt(nameValuePairs.get("dateNotified"));
		String summary = nameValuePairs.get("summary");
		String detail = nameValuePairs.get("detail");

		File fyle = new File(files.get(0).getAbsolutePath());
		byte[] formData = new byte[(int) fyle.length()];
		try (FileInputStream fis = new FileInputStream(fyle)) {
			fis.read(formData);
		}

		// Reduce image size to roughly 3000 pixels along the longest dimension
		formData = Image.resizeImage(formData, extension, 3000);

		String formCode = BProperty.get(StandardProperty.WORKER_WRITEUP_FORM_CODE);
		if (formCode == null || formCode.isEmpty())
			throw new ArahantException("WORKER_WRITEUP_FORM_CODE not set");
		FormType ft = hsu.createCriteria(FormType.class).eq(FormType.FORM_CODE, formCode).first();
		if (ft == null)
			throw new ArahantException("Form type code " + formCode + " not found");

		BPersonForm form = new BPersonForm();
		form.create();
		form.setFormTypeId(ft.getFormTypeId());
		form.setFormDate(eventDate);
		form.setPersonId(employeeId);
		form.setComments(summary);
		form.setSource("file." + extension);
		form.setFormData(formData, extension);
		form.setExtension(extension);
		form.insert();

		BHREmployeeEvent e = new BHREmployeeEvent();
		e.create();
		e.setEmployeeId(employeeId);
		e.setSupervisorId(supervisorId);
		e.setEventDate(eventDate);
		e.setEventType('M');
		e.setEmployeeNotified(employeeNotified ? 'Y' : 'N');
		e.setSummary(summary);
		e.setDetail(detail);
		e.setDateNotified(dateNotfied);
		e.insert();

		hsu.commitTransaction();
/*
		try {
			e.sendEmailNotification('W', extension, formData);
		} catch (Exception ignored) {

		}
 */
	}

	private void workerAccidentUpload(HibernateSessionUtil hsu, HashMap<String, String> nameValuePairs, List<File> files) throws Exception {
		try {
			hsu.beginTransaction();
			String projectId = nameValuePairs.get("projectId");
			String extension = nameValuePairs.get("extension");
			String employeeId = nameValuePairs.get("employeeId");
			String supervisorId = nameValuePairs.get("supervisorId");
			int eventDate = Integer.parseInt(nameValuePairs.get("eventDate"));
			boolean employeeNotified = "true".equals(nameValuePairs.get("employeeNotified"));
			int dateNotfied = Integer.parseInt(nameValuePairs.get("dateNotified"));
			String summary = nameValuePairs.get("summary");
			String detail = nameValuePairs.get("detail");

			File fyle = new File(files.get(0).getAbsolutePath());
			byte[] formData = new byte[(int) fyle.length()];
			try (FileInputStream fis = new FileInputStream(fyle)) {
				fis.read(formData);
			}

			// Reduce image size to roughly 3000 pixels along the longest dimension
			formData = Image.resizeImage(formData, extension, 3000);

			String formCode = BProperty.getIDWithCheck(StandardProperty.WORKER_ACCIDENT_FORM_CODE);
			FormType ft = hsu.createCriteria(FormType.class).eq(FormType.ID, formCode).first();
			if (ft == null)
				throw new ArahantException("Form type code " + formCode + " not found");

			BPersonForm form = new BPersonForm();
			String personFormId = form.create();
			form.setFormTypeId(ft.getFormTypeId());
			form.setFormDate(eventDate);
			form.setPersonId(employeeId);
			form.setComments("[Accident] " + summary);
			form.setSource("file." + extension);
			form.setFormData(formData, extension);
			form.setExtension(extension);
			form.insert();

			BHREmployeeEvent e = new BHREmployeeEvent();
			String eventId = e.create();
			e.setEmployeeId(employeeId);
			e.setSupervisorId(supervisorId);
			e.setEventDate(eventDate);
			e.setEventType('M');
			e.setEmployeeNotified(employeeNotified ? 'Y' : 'N');
			e.setSummary(StringUtils.take("[Accident] " + summary, 60).trim());
			e.setDetail(detail);
			e.setDateNotified(dateNotfied);
			e.insert();

			hsu.commitTransaction();

			responseJsonObject.put("personFormId", personFormId);
			responseJsonObject.put("eventId", eventId);

			//	e.sendEmailAccidentReport('W', extension, formData);
		} catch (Exception e) {
			throw e;
		}
	}

	private void workerAccidentUpload2(HibernateSessionUtil hsu, HashMap<String, String> nameValuePairs, List<File> files) throws Exception {
		hsu.beginTransaction();
		String projectId = nameValuePairs.get("projectId");
		String extension = nameValuePairs.get("extension");
		final String employeeId = nameValuePairs.get("employeeId");
		final String supervisorId = nameValuePairs.get("supervisorId");
		int eventDate = Integer.parseInt(nameValuePairs.get("eventDate"));
		boolean employeeNotified = "true".equals(nameValuePairs.get("employeeNotified"));
		int dateNotfied = Integer.parseInt(nameValuePairs.get("dateNotified"));
		String summary = nameValuePairs.get("summary");
		String detail = nameValuePairs.get("detail");
		final String personFormId = nameValuePairs.get("personFormId");
		final String eventId = nameValuePairs.get("eventId");
		final int pageNumber = Integer.parseInt(nameValuePairs.get("pageNumber"));

		File fyle = new File(files.get(0).getAbsolutePath());
		byte[] formData = new byte[(int) fyle.length()];
		try (FileInputStream fis = new FileInputStream(fyle)) {
			fis.read(formData);
		}

		// Reduce image size to roughly 3000 pixels along the longest dimension
		formData = Image.resizeImage(formData, extension, 3000);

		String formCode = BProperty.getIDWithCheck(StandardProperty.WORKER_ACCIDENT_FORM_CODE);
		FormType ft = hsu.createCriteria(FormType.class).eq(FormType.ID, formCode).first();
		if (ft == null)
			throw new ArahantException("Form type code " + formCode + " not found");

		BPersonForm form = new BPersonForm();
		form.create();
		form.setFormTypeId(ft.getFormTypeId());
		form.setFormDate(eventDate);
		form.setPersonId(employeeId);
		form.setComments("[Accident] [Page " + pageNumber + "] " + summary);
		form.setSource("file." + extension);
		form.setFormData(formData, extension);
		form.setExtension(extension);
		form.insert();

		hsu.commitTransaction();

		//	e.sendEmailAccidentReport('W', extension, formData);
	}

	private static String nullToEmptyString(String s) {
		return s == null ? "" : s;
	}

	private static String makeName(String personId) {
		BPerson bp = new BPerson(personId);
		return bp.getFirstName() + " " + bp.getLastName();
	}

	private void uploadComdata(HibernateSessionUtil hsu, HashMap<String, String> nameValuePairs, List<File> files) throws Exception {
		hsu.beginTransaction();
		String projectId = nameValuePairs.get("projectId");
		String sourceFileName = nameValuePairs.get("sourceFileName");
		String employeeId = nameValuePairs.get("employeeId");
		String supervisorId = nameValuePairs.get("supervisorId");
		String description = nameValuePairs.get("description");

		if (description == null)
			description = "";
		String odesc = description;
		Connection db = hsu.getKissConnection();
		Command cmd = db.newCommand();
		Record erec = cmd.fetchOne("select p.fname, p.mname, p.lname, p.ssn, p.dob, e.ext_ref, ph.phone_number, " +
				"  a.street, a.street2, a.city, a.state, a.zip " +
				"from person p " +
				"left outer join employee e " +
				"  on p.person_id = e.person_id " +
				"left outer join phone ph " +
				"  on p.person_id = ph.person_join " +
				"left outer join address a " +
				"  on p.person_id = a.person_join " +
				"where p.person_id=? and " +
				"(ph.phone_type = 3 or ph.phone_type is null) and (a.address_type = 2 or a.address_type is null)", employeeId);
		if (erec != null) {
			if (!description.isEmpty())
				description += "\n\n";
			String name = erec.getString("fname");
			String mname = erec.getString("mname");
			if (mname != null  && !mname.isEmpty())
				name += " " + mname;
			name += " " + erec.getString("lname");
			description += "Worker: " + name + "\n";
			description += "SSN: " + nullToEmptyString(Person.decryptSsn(erec.getString("ssn"))) + "\n";
			description += "DOB: " + org.kissweb.DateUtils.format4(erec.getInt("dob")) + "\n";
			description += "Employee ID: " + nullToEmptyString(erec.getString("ext_ref")) + "\n";
			String phone = erec.getString("phone_number");
			description += "Cell Phone: " + (phone == null || phone.isEmpty() ? "(unknown)" : phone) + "\n\n";

			description += nullToEmptyString(erec.getString("street")) + "\n";
			String s2 = erec.getString("street2");
			if (s2 != null  &&  !s2.isEmpty())
				description += s2 + "\n";
			String csz = nullToEmptyString(erec.getString("city"));
			if (!csz.isEmpty())
				csz += ", ";
			csz += nullToEmptyString(erec.getString("state"));
			if (!csz.isEmpty())
				csz += " ";
			description += csz + nullToEmptyString(erec.getString("zip")) + "\n\n";
		}

		File fyle = new File(files.get(0).getAbsolutePath());
		byte[] formData = new byte[(int) fyle.length()];
		try (FileInputStream fis = new FileInputStream(fyle)) {
			fis.read(formData);
		}

		// Reduce image size to roughly 3000 pixels along the longest dimension
		formData = Image.resizeImage(formData, org.kissweb.FileUtils.getMimeType(sourceFileName), 3000);

		String formCode = BProperty.get(StandardProperty.WORKER_WRITEUP_FORM_CODE);
		if (formCode == null || formCode.isEmpty())
			throw new ArahantException("WORKER_WRITEUP_FORM_CODE not set");
		FormType ft = hsu.createCriteria(FormType.class).eq(FormType.FORM_CODE, formCode).first();
		if (ft == null)
			throw new ArahantException("Form type code " + formCode + " not found");

		List<String> ids = BProperty.getIDList("COMDATA_NOTIFICATIONS", "person", "person_id");

		Record message = db.newRecord("message");
		IDGenerator.generate(message, "message_id");
		message.set("message", description);
		message.set("from_person_id", supervisorId);
		message.setDateTime("created_date", new Date());
		message.set("subject", "Comdata Notification");
		message.set("from_show", "Y");
		message.set("from_name", makeName(supervisorId));
		message.addRecord();

		Record att = db.newRecord("message_attachment");
		String messageAttachmentId = IDGenerator.generate(att, "message_attachment_id");
		att.set("message_id", message.getString("message_id"));
		att.set("source_file_name", sourceFileName);
		ExternalFile.saveData(ExternalFile.MESSAGE_ATTACHMENT_ATTACHMENT, messageAttachmentId, sourceFileName, formData);
		att.addRecord();

		for (String id : ids) {
			if (!id.isEmpty()) {
				Record to = db.newRecord("message_to");
				IDGenerator.generate(to, "message_to_id");
				to.set("message_id", message.getString("message_id"));
				to.set("to_person_id", id);
				to.set("send_type", "T");
				to.set("to_show", "Y");
				to.set("to_name", makeName(id));
				to.set("sent", "N");
				to.addRecord();
				BMessage.copyMessageToEmail(supervisorId, id, description);
			}
		}

		// Now save to worker record
		String formTypeId = BProperty.getOneID("COMDATA_FORM_TYPE", "form_type", "form_type_id");

		Record rec = db.newRecord("person_form");
		String personFormId = IDGenerator.generate(rec, "person_form_id");
		rec.set("form_Type_id", formTypeId);
		rec.set("form_date", org.kissweb.DateUtils.today());
		rec.set("person_id", employeeId);
		if (!odesc.isEmpty()) {
			if (odesc.length() > 255)
				odesc = org.kissweb.StringUtils.take(odesc, 255);
			rec.set("comments", odesc);
		}
		rec.set("source", sourceFileName);
		rec.set("file_name_extension", org.kissweb.FileUtils.getExtension(sourceFileName));
		rec.addRecord();
		ExternalFile.saveData(ExternalFile.PERSON_FORM, personFormId, org.kissweb.FileUtils.getExtension(sourceFileName), formData);
		db.commit();
	}
	
	private void sendFile(String relFileName, HttpServletResponse response) throws IOException {
		int idx = relFileName.lastIndexOf('/');
		if (idx == -1)
			idx = relFileName.lastIndexOf('\\');
		String fileName;
		if (idx == -1)
			fileName = relFileName;
		else
			fileName = relFileName.substring(idx + 1);
		
		idx = relFileName.lastIndexOf('.');
		String contentType = "";
		if (idx != -1)
			contentType = relFileName.substring(idx + 1);

		if ("pdf".equalsIgnoreCase(contentType))
			response.setContentType("application/pdf");
		else if ("csv".equalsIgnoreCase(contentType))
			response.setContentType("application/ms-excel");
		else
			response.setContentType("application/octet-stream");

		response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		ServletOutputStream os = null;

		try (FileInputStream fis = new FileInputStream(FileSystemUtils.getWorkingDirectory() + "/" + relFileName)) {
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
		}
	}
	
	private static class BenefitFileUploadRunner extends Thread {

		String importTypeId;
		String password;
		String filename;
		String userId;

		@Override
		public void run() {
			HibernateSessionUtil hsu = ArahantSession.openHSU();
			try {
				hsu.beginTransaction();
				GenericFileImport gfi = new GenericFileImport();
				BProphetLogin.checkPassword(userId, password);
				gfi.importFile(filename, Objects.requireNonNull(hsu.get(ImportType.class, importTypeId)));
				hsu.dontAIIntegrate();
				hsu.commitTransaction();
				hsu.useAIIntegrate();
			} catch (Exception ex) {
				hsu.rollbackTransaction();
				Logger.getLogger(FileUploadServlet.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
				ArahantSession.clearSession();    //  this closes the HSU session
			}
		}
	}

	protected abstract static class AsynchRequest extends Thread {

		protected HibernateSessionUtil hsu;
		protected String currentPersonId;

		public AsynchRequest(String currentPersonId) {
			this.currentPersonId = currentPersonId;
			setDaemon(true);
		}

		@Override
		public void run() {
			hsu = ArahantSession.openHSU();
			try {
				hsu.beginTransaction();
				Person p = hsu.get(Person.class, currentPersonId);
				hsu.setCurrentPerson(p);

				try {
					doRequest();
					hsu.commitTransaction();
				} catch (Exception e) {
					new ArahantLogger(FileUploadServlet.class).error(e);
					BMessage.send(hsu.getCurrentPerson(), hsu.getCurrentPerson(), "Task Failed", e.getMessage());
					hsu.rollbackTransaction();
				}
			} finally {
				ArahantSession.clearSession();    //  this closes the HSU session
			}
		}

		protected abstract void doRequest() throws Exception;
	}

	private static class EmpCensusUploadRunner extends AsynchRequest {

		String password;
		String filename;
		String userId;
		String companyId;
		String securityId;
		String screenGroupId;

		public EmpCensusUploadRunner() {
			super("");
		}

		@Override
		public void doRequest() throws Exception {
			BProphetLogin.checkPassword(userId, password);
			this.currentPersonId = hsu.getCurrentPerson().getPersonId();
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, companyId));
			StandardMap importMap = new StandardMap(filename);					// For standard import (everything else)
//			StandardCASMap importMap=new StandardCASMap(filename);				// For CAS import screen hookup only
			importMap.setScreenGroupId(screenGroupId);
			importMap.setSecurityGroupId(securityId);
			DataImport.doEmployeeImport(importMap);
		}
	}

	private static class DepCensusUploadRunner extends AsynchRequest {

		String password;
		String filename;
		String userId;
		String companyId;

		public DepCensusUploadRunner() {
			super("");
		}

		@Override
		public void doRequest() throws Exception {
			BProphetLogin.checkPassword(userId, password);
			this.currentPersonId = hsu.getCurrentPerson().getPersonId();
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, companyId));
			new DependentImport().doImport(filename);
		}
	}

	private static class StandardProspectUploadRunner extends AsynchRequest {

		String password;
		String filename;
		String userId;
		String companyId;
		String statusId;
		String typeId;
		String sourceId;
		String salesPersonId;

		public StandardProspectUploadRunner() {
			super("");
		}

		@Override
		public void doRequest() throws Exception {
			BProphetLogin.checkPassword(userId, password);
			this.salesPersonId = currentPersonId;
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, companyId));
			StandardProspectImport.doImport(filename, salesPersonId, sourceId, statusId, typeId);
		}
	}

	private static class VisitorTrackUploadRunner extends AsynchRequest {

		String password;
		String filename;
		String userId;
		String companyId;
		String statusId;

		public VisitorTrackUploadRunner() {
			super("");
		}

		@Override
		public void doRequest() throws Exception {
			BProphetLogin.checkPassword(userId, password);
			this.currentPersonId = hsu.getCurrentPerson().getPersonId();
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, companyId));
			VisitorTrackImport.doImport(filename, currentPersonId, statusId);
		}
	}

	private static class SalesGenieUploadRunner extends AsynchRequest {

		String password;
		String filename;
		String userId;
		String companyId;

		public SalesGenieUploadRunner() {
			super("");
		}

		@Override
		public void doRequest() throws Exception {
			BProphetLogin.checkPassword(userId, password);
			this.currentPersonId = hsu.getCurrentPerson().getPersonId();
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, companyId));
			SheaklyProspectImport.doImport(filename);
		}
	}

	private static class ProspectActivityUploadRunner extends AsynchRequest {

		String password;
		String filename;
		String userId;
		String companyId;

		public ProspectActivityUploadRunner() {
			super("");
		}

		@Override
		public void doRequest() throws Exception {
			BProphetLogin.checkPassword(userId, password);
			this.currentPersonId = hsu.getCurrentPerson().getPersonId();
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, companyId));
			SheaklyActivityImport.doImport(filename);
		}
	}

	private static class EmployeeOrderUploadRunner extends Thread {

		String employeeIdColumn;
		String orderColumn;
		String userId;
		String password;
		String filename;

		@Override
		public void run() {
			HibernateSessionUtil hsu = ArahantSession.openHSU();
			try {
				BProphetLogin.checkPassword(userId, password);
				EmployeeOrderImport imp = new EmployeeOrderImport();
				hsu.beginTransaction();
				hsu.setCurrentPersonToArahant();
				imp.updateOrder(employeeIdColumn, orderColumn, filename, userId);
				hsu.commitTransaction();
			} finally {
				ArahantSession.clearSession();    //  this closes the HSU session
			}
		}
	}

	private static class BenefitPriceUploadRunner extends Thread {

		String configId;
		String ssnColumn;
		String effectiveDate;
		String userId;
		String password;
		String filename;
		String priceColumn;

		@Override
		public void run() {
			HibernateSessionUtil hsu = ArahantSession.openHSU();
			try {
				BProphetLogin.checkPassword(userId, password);
				PricingUpdateImport imp = new PricingUpdateImport();
				hsu.beginTransaction();
				//hsu.setCurrentPersonToArahant();
				imp.updatePricing(configId, effectiveDate, filename, ssnColumn, priceColumn);
				hsu.commitTransaction();
			} finally {
				ArahantSession.clearSession();    //  this closes the HSU session
			}
		}
	}

	private static class ProjectViewUploadRunner extends Thread {

		String locationType;
		String locationTypeRelativeToId;
		String parentId;
		String userId;
		String password;
		String filename;
		int importType;

		@Override
		public void run() {
			ArahantSession.openHSU();
			try {
				//new ViewParentOps().importProjectView(userId, password, parentId, locationTypeRelativeToId, locationType, filename, importType);
				ProjectImport.importProjectView(userId, password, parentId, locationTypeRelativeToId, locationType, filename, importType);
			} finally {
				ArahantSession.clearSession();    //  this closes the HSU session
			}
		}
	}

	private static class BoxMakerEmployeeImport extends AsynchRequest {

		String password;
		String filename;
		String userId;

		public BoxMakerEmployeeImport() {
			super("");
		}

		@Override
		public void doRequest() throws Exception {
			System.out.println("BoxMaker import started.");
			BProphetLogin.checkPassword(userId, password);
			this.currentPersonId = hsu.getCurrentPerson().getPersonId();
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, "00000-0000000005"));
			new BoxMakerImport().importEmployees(filename);
			System.out.println("BoxMaker import finished.");
		}
	}

	private static class AccrualImport extends AsynchRequest {

		String password;
		String benefitId;
		String configId;
		String fileName;
		String userId;

		public AccrualImport() {
			super("");
		}

		@Override
		public void doRequest() throws Exception {
			System.out.println("Employee Accrual Totals import started.");
			BProphetLogin.checkPassword(userId, password);
			this.currentPersonId = hsu.getCurrentPerson().getPersonId();
			new com.arahant.imports.AccrualImport().importAccruals(fileName, benefitId, configId);
			System.out.println("Employee Accrual Totals import finished.");
		}
	}

	private static class WmCoSalaryCustom1UploadRunner extends AsynchRequest {

		String password;
		String filename;
		String userId;

		public WmCoSalaryCustom1UploadRunner() {
			super("");
		}

		@Override
		public void doRequest() throws Exception {
			BProphetLogin.checkPassword(userId, password);
			this.currentPersonId = hsu.getCurrentPerson().getPersonId();
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, "00001-0000000000"));
			SalaryImport.doImport(filename);
		}
	}

	private static class WmCoSalaryCustom2UploadRunner extends AsynchRequest {

		String password;
		String filename;
		String userId;

		public WmCoSalaryCustom2UploadRunner() {
			super("");
		}

		@Override
		public void doRequest() throws Exception {
			BProphetLogin.checkPassword(userId, password);
			this.currentPersonId = hsu.getCurrentPerson().getPersonId();
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class, "00001-0000000000"));
			SalaryImport2.doImport(filename);
		}
	}

	private static class ClientFile {
		File file;
		String clientFileName;

		ClientFile(File file, String fname) {
			this.file = file;
			this.clientFileName = fname;
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP
	 * <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
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
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * @return 
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
