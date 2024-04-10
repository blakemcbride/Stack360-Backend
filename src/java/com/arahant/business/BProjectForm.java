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
package com.arahant.business;

import com.arahant.beans.FormType;
import com.arahant.beans.ProjectForm;
import com.arahant.beans.ProjectShift;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.TiffReport;
import com.arahant.utils.*;
import com.arahant.utils.Image;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class BProjectForm extends SimpleBusinessObjectBase<ProjectForm> {

	private static final transient ArahantLogger logger = new ArahantLogger(BProjectForm.class);

	public BProjectForm() {
	}

	public BProjectForm(final String id) throws ArahantException {
		internalLoad(id);
	}

	public BProjectForm(final ProjectForm form) {
		bean = form;
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProjectForm();
		return bean.generateId();
	}

	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProjectForm.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public String getReport() throws ArahantException {
		String ext = bean.getFileNameExtension();
		byte[] image = new byte[0];
		try {
			image = ExternalFile.getBinary(ExternalFile.PROJECT_FORM_IMAGE, bean.getProjectFormId(), bean.getFileNameExtension());
		} catch (IOException e) {
			throw new ArahantException(e);
		}
		//  This line saves the image to a file AND also converts a tiff file to a PDF file.
		return new TiffReport().tifToPdf(image, ext, getFileName());
	}

	public void rotateLeft() {
		try {
			final String fileName = ExternalFile.makeExternalFilePath(ExternalFile.PROJECT_FORM_IMAGE, bean.getProjectFormId(), bean.getFileNameExtension());
			Image.rotateLeft(fileName);
		} catch (Exception ignored) {

		}
	}

	public void rotateRight() {
		try {
			final String fileName = ExternalFile.makeExternalFilePath(ExternalFile.PROJECT_FORM_IMAGE, bean.getProjectFormId(), bean.getFileNameExtension());
			Image.rotateRight(fileName);
		} catch (Exception ignored) {

		}
	}

	/**
	 * prefers the shiftId but will use the projectId if the shiftId is null
	 *
	 * @param projectId
	 * @param shiftId
	 * @return
	 */
	public static BProjectForm[] list(final String projectId, final String shiftId) {
		if (shiftId == null || shiftId.isEmpty())
			return makeArray(ArahantSession.getHSU().createCriteria(ProjectForm.class)
					.joinTo(ProjectForm.PROJECTSHIFT)
					.eq(ProjectShift.PROJECT_ID, projectId)
					.list());
		else
			return makeArray(ArahantSession.getHSU().createCriteria(ProjectForm.class)
					.eq(ProjectForm.PROJECTSHIFT, (new BProjectShift(shiftId)).getProjectShift())
					.list());
	}

	static BProjectForm[] makeArray(final List<ProjectForm> l) {
		Collections.sort(l);

		final BProjectForm[] ret = new BProjectForm[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProjectForm(l.get(loop));
		return ret;
	}

	public String getId() {
		return bean.getProjectFormId();
	}

	public String getFormCode() {
		return bean.getFormType().getFormCode();
	}

	public String getComments() {
		return bean.getComments();
	}

	public String getDateFormatted() {
		return DateUtils.getDateFormatted(bean.getFormDate());
	}

	public String getFormDescription() {
		return bean.getFormType().getDescription();
	}

	public void setComments(String comments) {
		bean.setComments(comments);
	}

	public void setFormDate(int now) {
		bean.setFormDate(now);
	}

	public void setFormTypeId(String formTypeId) {
		bean.setFormType(ArahantSession.getHSU().get(FormType.class, formTypeId));
	}

	public ProjectShift getProjectShift() {
		return bean.getProjectShift();
	}

	public void setProjectShift(ProjectShift projectShift) {
		bean.setProjectShift(projectShift);
	}

	public void setProjectShiftId(String projectShiftId) {
		bean.setProjectShift(ArahantSession.getHSU().get(ProjectShift.class, projectShiftId));
	}

	public String getExtension() {
		return bean.getFileNameExtension();
	}

	public void setExtension(String extension) {
		bean.setFileNameExtension(extension);
	}

	public void setInternal(final char internal) {
		bean.setInternal(internal);
	}

	public char getInternal() {
		return bean.getInternal();
	}

	private static Integer formNum = 0;

	private String getFileName() {
		try {
			String n;
			synchronized (logger) {
				n = "ProjectPhoto-" + formNum++;
			}
			return n;
		} catch (Exception e) {
			return "FormRpt";
		}
	}

	private static String appendSlash(String s) {
		if (s == null  ||  s.isEmpty())
			return s;
		return s.endsWith("/") ? s : s + "/";
	}

	private static void deleteAllFiles(String path) {
		try {
			FileUtils.cleanDirectory(new File(path));
		} catch (IOException e) {
			// ignore
		}
	}

	/**
	 * Builds the path string and then makes sure the directory exists.
	 *
	 * @param bc
	 * @return
	 */
	private static String getPath(BClientCompany bc, BProject bp) {
		String picturePath = bc.getPictureDiskPath();
		String dir = appendSlash(FileSystemUtils.getWorkingDirectory().getAbsolutePath()) + "../";
		if (picturePath.startsWith("/"))
			picturePath = picturePath.substring(1);
		dir += appendSlash(picturePath);
		(new File(dir)).mkdir();

		String projectName = bp.getDescription().replaceAll("/", "-");
		dir += appendSlash(projectName);
		(new File(dir)).mkdir();

		return dir;
	}

	private static String addSuffix(String path, String suffix) {
		if (suffix == null  ||  suffix.isEmpty())
			return path;
		return suffix.startsWith(".") ? path + suffix : path + "." + suffix;
	}

	public static void deleteExpotredForms(String project_id) {
		BProject bp = new BProject(project_id);
		try {  // in case requesting company is not a client company
			BClientCompany bc = (BClientCompany) bp.getRequestingCompany();
			String picturePath = bc.getPictureDiskPath();
			if (picturePath != null && picturePath.length() > 0) {
				String dir = getPath(bc, bp);
				deleteAllFiles(dir);
			}
		} catch (Exception e) {
			// ignore
		}
	}

	/**
	 * Reset all of the exported files for a project.  Also deletes all files if
	 * client no longer show.
	 * <br><br>
	 * Provide project_id or shift_id.  Prefers the shiftId.
	 *
	 * @param project_id
	 * @param shift_id
	 */
	public static void resetExportedForms(String project_id, String shift_id) {
		BProject bp;
		if (shift_id != null && !shift_id.isEmpty())
			bp = new BProjectShift(shift_id).getProject();
		else
			bp = new BProject(project_id);
		try {  // in case requesting company is not a client company
			BClientCompany bc = (BClientCompany) bp.getRequestingCompany();
			String picturePath = bc.getPictureDiskPath();
			if (picturePath != null && picturePath.length() > 0) {
				String dir = getPath(bc, bp);
				deleteAllFiles(dir);
				if (bc.getCopyPicturesToDisk() == 'Y' && (bc.getCopyInactiveProjects() == 'Y' || bp.getActive())) {
					List<ProjectShift> shifts = bp.getAllShifts();
					for (ProjectShift ps : shifts) {
						BProjectForm[] forms = BProjectForm.list(null, ps.getProjectShiftId());
						for (BProjectForm pf : forms) {
							if (pf.getInternal() == 'N' || bc.getCopyOnlyExternal() == 'N') {
								String formName = addSuffix(pf.getId().substring(10), pf.getExtension());
								formName = dir + formName.replaceAll("/", "-");
								byte[] pic = ExternalFile.getBinary(ExternalFile.PROJECT_FORM_IMAGE, pf.getId(), pf.getExtension());
								if (pic != null)
									FileUtils.writeByteArrayToFile(new File(formName), pic);
//							FileUtils.writeByteArrayToFile(new File(formName), pf.getBean().getImage());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// ignore
		}
	}

	/*
	    OLD: All this work is done because I don't want more than one image in memory at a time.
	    Hibernate wants to hold on to previously loaded images.
	 */
	public byte [] getImage() {
		try {
			byte[] ret = ExternalFile.getBinary(ExternalFile.PROJECT_FORM_IMAGE, bean.getProjectFormId(), bean.getFileNameExtension());

			// Reduce image size to roughly 3000 pixels along the longest dimension
			ret = Image.resizeImage(ret, bean.getFileNameExtension(), 3000);

			return ret;
		} catch (Exception e) {
			return null;
		}
	}
}
