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


package com.arahant.services.standard.project.projectForm;

import com.arahant.beans.ProjectForm;
import com.arahant.beans.ProjectShift;
import com.arahant.business.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.arahant.utils.*;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.services.ServiceBase;
import com.arahant.services.standard.project.projectFormType.CheckRightInput;
import com.arahant.services.standard.project.projectFormType.CheckRightReturn;
import org.kissweb.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardProjectProjectFormOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProjectFormOps extends ServiceBase {

    private static final transient ArahantLogger logger = new ArahantLogger(ProjectFormOps.class);

    public ProjectFormOps() {
    }

    @WebMethod()
    public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
        final CheckRightReturn ret = new CheckRightReturn();

        try {
            checkLogin(in);

            ret.setAccessLevel(BRight.checkRight("AccessProjectForms"));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public GetFormReturn getForm(/*@WebParam(name = "in")*/final GetFormInput in) {
        final GetFormReturn ret = new GetFormReturn();
        try {
            checkLogin(in);

            ret.setReportUrl(new BProjectForm(in.getId()).getReport());

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public DownloadZipReturn downloadZip(/*@WebParam(name = "in")*/final DownloadZipInput in) {
        final DownloadZipReturn ret = new DownloadZipReturn();
        try {
            checkLogin(in);

            List<ProjectForm> forms;
            if (in.isAllFormTypes()  &&  in.isIncludeInternal())
                forms = hsu.createCriteria(ProjectForm.class)
                        .joinTo(ProjectForm.PROJECTSHIFT)
                        .eq(ProjectShift.PROJECT, (new BProject(in.getProjectId())).getBean())
                        .list();
            else if (in.isIncludeInternal())
                forms = hsu.createCriteria(ProjectForm.class)
                        .joinTo(ProjectForm.PROJECTSHIFT)
                        .eq(ProjectShift.PROJECT, (new BProject(in.getProjectId())).getBean())
                        .eq(ProjectForm.FORM_TYPE, (new BFormType(in.getFormType())).getBean())
                        .list();
            else if (in.isAllFormTypes())
                forms = hsu.createCriteria(ProjectForm.class)
                        .joinTo(ProjectForm.PROJECTSHIFT)
                        .eq(ProjectShift.PROJECT, (new BProject(in.getProjectId())).getBean())
                        .eq(ProjectForm.INTERNAL, 'N')
                        .list();
            else
                forms = null;
            if (forms == null  ||  forms.isEmpty())
                ret.setFileUrl("");
            else {
                File zipFile = FileSystemUtils.createTempFile("ProjectForms-", ".zip");
                try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
                    int fnum = 1;
                    for (ProjectForm pf : forms) {
                        String c = pf.getComments();
                        if (c == null)
                            c = "";
                        else {
                            c = c.replaceAll("[./\\\\*&()'\",<>;:|\\[\\]?`]", " ");
                            c = c.replaceAll(" {2,}", " ");
                            c = c.trim();
                        }
                        if (c.isEmpty())
                            c = "ProjectForm";
                        if (c.length() > 60)
                            c = StringUtils.take(c, 60).trim();
                        byte [] image = ExternalFile.getBinary(ExternalFile.PROJECT_FORM_IMAGE, pf.getProjectFormId(), pf.getFileNameExtension());
                        // Reduce image size to roughly 3000 pixels along the longest dimension
                        image = Image.resizeImage(image, pf.getFileNameExtension(), 3000);
                        ZipEntry ze = new ZipEntry(c + "-" + fnum++ + "." + pf.getFileNameExtension());
                        zos.putNextEntry(ze);
                        zos.write(image, 0, image.length);
                        zos.closeEntry();
                    }
                }
                ret.setFileUrl(FileSystemUtils.getHTTPPath(zipFile));
            }
            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public RotateLeftReturn rotateLeft(/*@WebParam(name = "in")*/final RotateLeftInput in) {
        final RotateLeftReturn ret = new RotateLeftReturn();
        try {
            checkLogin(in);

            (new BProjectForm(in.getId())).rotateLeft();

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public RotateRightReturn rotateRight(/*@WebParam(name = "in")*/final RotateRightInput in) {
        final RotateRightReturn ret = new RotateRightReturn();
        try {
            checkLogin(in);

            (new BProjectForm(in.getId())).rotateRight();

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public DeleteFormReturn deleteForm(/*@WebParam(name = "in")*/final DeleteFormInput in) {
        final DeleteFormReturn ret = new DeleteFormReturn();
        try {
            checkLogin(in);

            for (String id : in.getIds()) {
                ExternalFile.deleteAllExternalFiles(ProjectForm.TABLE_NAME, id);
                hsu.delete(ProjectForm.class, id);
            }

//            Not sure what this was supposed to do
//            BProjectForm.resetExportedForms(in.getProjectId(), null);

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListFormsForProjectReturn listFormsForProject(/*@WebParam(name = "in")*/final ListFormsForProjectInput in) {
        final ListFormsForProjectReturn ret = new ListFormsForProjectReturn();
        try {
            checkLogin(in);

            int rights = BRight.checkRight("AccessProjectForms");

            BProject bp = new BProject(in.getProjectId());
            try {  // in case requesting company is not a client company
                BClientCompany bc = (BClientCompany) bp.getRequestingCompany();
                String picturePath = bc.getPictureDiskPath();
                ret.setCanExport(bc.getCopyPicturesToDisk() == 'Y' && picturePath != null && picturePath.length() > 0);
            } catch (Exception e) {
                // ignore
            }

            ret.setItem(in.getProjectId(), in.getShiftId(), rights);

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ExportFormsReturn exportForms(/*@WebParam(name = "in")*/final ExportFormsInput in) {
        final ExportFormsReturn ret = new ExportFormsReturn();
        try {
            checkLogin(in);

            int rights = BRight.checkRight("AccessProjectForms");

            BProjectForm.resetExportedForms(in.getProjectId(), null);

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListFormTypesReturn listFormTypes(/*@WebParam(name = "in")*/final ListFormTypesInput in) {
        final ListFormTypesReturn ret = new ListFormTypesReturn();
        try {
            checkLogin(in);

            ret.setItem(BFormType.list('P'));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public EditFormReturn editForm(/*@WebParam(name = "in")*/final EditFormInput in) {
        final EditFormReturn ret = new EditFormReturn();
        try {
            checkLogin(in);

            BProjectForm bpf = new BProjectForm(in.getId());
            bpf.setInternal("Yes".equals(in.getInternal()) ? 'Y' : 'N');
            bpf.setComments(in.getComments());
            bpf.setFormTypeId(in.getTypeCodeId());
            bpf.update();

            BProjectForm.resetExportedForms(in.getProjectId(), null);

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

}
