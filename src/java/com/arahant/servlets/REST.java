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

import com.arahant.exceptions.ArahantInfo;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.rest.*;
import com.arahant.services.ServiceBase;
import com.arahant.services.TransmitInputBase;
import com.arahant.services.main.UserCache;
import com.arahant.utils.*;
import org.json.JSONObject;
import org.kissweb.FileUtils;
import org.kissweb.FrontendException;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Enumeration;
import java.util.stream.Collectors;

/**
 * Author: Blake McBride
 * Date: 2/24/18
 *
 *  In order to connect an external URL to this servlet, see servletNames in
 *  com/arahant/utils/generators/wsconfiggen/WsConfigGen.java
 */
@MultipartConfig
public class REST extends HttpServlet {

    private static final ArahantLogger logger = new ArahantLogger(REST.class);
    private ServletContext servletContext;
    private HttpServletRequest request;

    public enum ExecutionReturn {
        Success,
        NotFound,
        Error
    }

    /**
     * Get the absolute path of the root of the back-end application.
     *
     * @return
     */
    public String getRealPath() {
        return servletContext.getRealPath("/");
    }

    /**
     * Returns the ServletContext.
     *
     * @return
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Returns the HttpServletRequest.
     *
     * @return
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Returns the IP address of the client.
     *
     * @return
     */
    public String getRemoteAddr() {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr))
                remoteAddr = request.getRemoteAddr();
        }
        return remoteAddr;
    }

    /**
     * Return the number of files being uploaded.
     *
     * @return
     *
     * @see #getUploadFileName(int)
     * @see #getUploadBufferedInputStream(int)
     */
    public int getUploadFileCount() {
        int i = 0;
        for ( ; true ; i++) {
            Part filePart = null;
            try {
                filePart = request.getPart("_file-" + i);
            } catch (Exception e) {
            }
            if (filePart == null)
                break;
        }
        return i;
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    /**
     * Returns the name of the file being uploaded.
     *
     * @param i beginning at 0
     * @return
     *
     * @see #getUploadFileCount()
     * @see #getUploadBufferedInputStream(int)
     */
    public String getUploadFileName(int i) {
        try {
            Part filePart = request.getPart("_file-" + i);
            return getFileName(filePart);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the file extension of a given file upload.
     *
     * @param i starts from 0
     * @return
     */
    public String getUploadFileType(int i) {
        try {
            final Part filePart = request.getPart("_file-" + i);
            final String fn = getFileName(filePart);
            final int idx = fn.lastIndexOf('.');
            return idx == -1 ? "" : fn.substring(idx + 1);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * In file upload scenarios, this method returns a BufferedInputStream
     * associated with file number i.  When done, the stream must be
     * closed by the application.
     *
     * @param i starting from 0
     * @return
     *
     * @see BufferedInputStream#close()
     * @see #getUploadFileCount()
     * @see #getUploadFileName(int)
     */
    public BufferedInputStream getUploadBufferedInputStream(int i) {
        try {
            Part filePart = request.getPart("_file-" + i);
            return new BufferedInputStream(filePart.getInputStream());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * In a file upload scenario, this method returns a byte array of the data that was uploaded.
     *
     * @param i starting from 0
     * @return
     * @throws IOException
     */
    public byte [] getUploadBytes(int i) throws IOException {
        BufferedInputStream bis = getUploadBufferedInputStream(i);
        if (bis == null)
            return null;
        byte [] ba = FileUtils.readAllBytes(bis);
        bis.close();
        return ba;
    }

    /**
     * Reads upload file "n", saves it to a temporary file, and returns the path to that file.
     *
     * @param n
     * @return
     * @throws IOException
     */
    public String saveUploadFile(int n) throws IOException {
        File f = FileSystemUtils.createTempFile("save", "tmp");
        try (
                BufferedInputStream bis = getUploadBufferedInputStream(n);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
        ) {
            int c;
            while (-1 != (c = bis.read()))
                bos.write(c);
        }
        return f.getAbsolutePath();
    }

    /**
     * Reads upload file "n", saves it to the file name passed in, and returns the file name passed in
     *
     * @param n file number
     * @param fileName the name of the file to save the upload to
     * @return the fileName passed in
     * @throws IOException
     *
     * @see #getUploadFileName(int)
     * @see #getUploadBufferedInputStream(int)
     */
    public String saveUploadFile(int n, String fileName) throws IOException {
        try (
                BufferedInputStream bis = getUploadBufferedInputStream(n);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
        ) {
            int c;
            while (-1 != (c = bis.read()))
                bos.write(c);
        }
        return fileName;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        servletContext = request.getServletContext();
        JSONObject injson;
        JSONObject outjson = new JSONObject();
        String _method, _package;
        ExecutionReturn res;

        String _className = request.getParameter("_class");
        if (_className != null) {
            // is file upload
            _method = request.getParameter("_method");
            injson = new JSONObject();
            Enumeration<String> names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                String value = request.getParameter(name);
                injson.put(name, value);
            }
        } else {
            String instr;
            try {
                instr = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (Exception e) {
                errorReturn(response, "Unable to parse request json", e);
                return;
            }
            injson = new JSONObject(instr);
            _method = injson.getString("_method");
            _className = injson.getString("_class");
        }

        if (injson.has("_uuid")  &&  "fa89a547-fcc0-448a-a9ee-b9777b920f4b".equals(injson.getString("_uuid"))) {
            // special code to check system health without actually logging in
            _package = injson.getString("_package");
            res = (new GroovyService()).tryGroovy(this, response, _package, _className, _method, injson, outjson, null);
            if (res == ExecutionReturn.Error  ||  res == ExecutionReturn.NotFound)
                errorReturn(response, "Invalid monitor", new Exception());
            else
                successReturn(response, outjson);
            return;
        }

        ServiceBase sb = new ServiceBase();

        _package = injson.getString("_package");

        if (!NoBuiltinAuthentication.bypassBuiltinAuthentication(_package, _className))
            if (_package != null && _package.equals("com.arahant.services.custom.waytogo.apply")) {
                // Applicant login logic
                try {
                    // This is to assure that applicants can't get to regular web services
                    sb.checkLogin(new TransmitInputBase(
                            injson.has("_user") ? injson.getString("_user") : "",
                            injson.has("_password") ? injson.getString("_password") : "",
                            injson.has("_uuid") ? injson.getString("_uuid") : "",
                            injson.has("_contextCompanyId") ? injson.getString("_contextCompanyId") : "",
                            injson.getBoolean("_sendValidations"),
                            injson.getString("_frontEndType"), "APPLICANT"), UserCache.LoginType.APPLICANT);
                } catch (Exception e) {
                    logoutReturn(response);
                    return;
                }
            } else if (_package != null && _package.equals("com.arahant.services.custom.waytogo.worker")) {
                // Worker login logic
                try {
                    // This is to assure that workers can't get to regular web services
                    sb.checkLogin(new TransmitInputBase(
                            injson.has("_user") ? injson.getString("_user") : "",
                            injson.has("_password") ? injson.getString("_password") : "",
                            injson.has("_uuid") ? injson.getString("_uuid") : "",
                            injson.has("_contextCompanyId") ? injson.getString("_contextCompanyId") : "",
                            injson.getBoolean("_sendValidations"),
                            injson.getString("_frontEndType"), "WORKER"), UserCache.LoginType.WORKER);
                } catch (Exception e) {
                    logoutReturn(response);
                    return;
                }
            } else {
                try {
                    sb.checkLogin(new TransmitInputBase(
                            injson.has("_user") ? injson.getString("_user") : "",
                            injson.has("_password") ? injson.getString("_password") : "",
                            injson.has("_uuid") ? injson.getString("_uuid") : "",
                            injson.has("_contextCompanyId") ? injson.getString("_contextCompanyId") : "",
                            injson.getBoolean("_sendValidations"),
                            injson.getString("_frontEndType"), "NORMAL"), UserCache.LoginType.NORMAL);
                } catch (Exception e) {
                    logoutReturn(response);
                    return;
                }
            }

        HibernateSessionUtil hsu = ArahantSession.getHSU();
        if (hsu == null)
            hsu = ArahantSession.openHSU(false);   // This is needed on Worker logins

        try {
            ServiceBase.recordScreenBeingUsedHTML(hsu, request.getRemoteAddr(), _package);
        } catch (Exception ignored) {
        }

        res = (new GroovyService()).tryGroovy(this, response, _package, _className, _method, injson, outjson, hsu);
        if (res == ExecutionReturn.Error)
            return;

        if (res == ExecutionReturn.NotFound) {
            res = (new JavaService()).tryJava(this, response, _package, _className, _method, injson, outjson, hsu);
            if (res == ExecutionReturn.Error)
                return;
        }

        if (res == ExecutionReturn.NotFound) {
            res = (new LispService()).tryLisp(this, response, _package, _className, _method, injson, outjson, hsu);
            if (res == ExecutionReturn.Error)
                return;
        }

        if (res == ExecutionReturn.NotFound) {
            res = (new CompiledJavaService()).tryCompiledJava(this, response, _package, _className, _method, injson, outjson, hsu);
            if (res == ExecutionReturn.Error)
                return;
        }

        if (res == ExecutionReturn.NotFound)
            errorReturn(response, "No back-end code found for " + _package + "." + _className, null);
        else
            successReturn(response, outjson);
    }

    private void successReturn(HttpServletResponse response, JSONObject outjson) throws IOException {
        ArahantSession.runAI();
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        if (hsu != null)
            hsu.commitTransaction();
        response.setContentType("application/json");
        response.setStatus(200);
        outjson.put("_Success", true);
        outjson.put("_ErrorCode", 0);  // success
        PrintWriter out = response.getWriter();
        out.print(outjson);
        out.flush();
        closeSession();
    }

    private void closeSession() {
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        if (hsu != null) {
            ArahantSession.clearSession();
            ArahantSession.setAI(null);  //  since this is not done in clearSession()
        }
        ArahantSession.runawayServiceChecker.removeService(Thread.currentThread());
    }

    public void errorReturn(HttpServletResponse response, String msg, Exception e) {
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        if (hsu != null)
            hsu.rollbackTransaction();
        closeSession();

        JSONObject outjson = new JSONObject();
        outjson.put("_Success", false);

        if (e != null && e.getCause() != null && e.getCause().getClass() == FrontendException.class) {
            outjson.put("_ErrorMessage", e.getCause().getMessage());
        } else {
            String finalMsg = msg;
            if (e != null) {
                String m = e.getMessage();
                if (m != null)
                    finalMsg = m;
                Throwable cause = e.getCause();
                if (cause != null) {
                    m = cause.getMessage();
                    if (m != null)
                        finalMsg = m;
                    if (!(cause instanceof ArahantWarning) && !(cause instanceof ArahantInfo))
                        logger.error(finalMsg, cause);
                } else if (!(e instanceof ArahantWarning) && !(e instanceof ArahantInfo))
                    logger.error(finalMsg, e);
            }
            outjson.put("_ErrorMessage", finalMsg);
        }
        outjson.put("_ErrorCode", 1);  // general error
        response.setContentType("application/json");
        response.setStatus(200);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e1) {
            // ignore
        }
        out.print(outjson);
        out.flush();
    }

    public void logoutReturn(HttpServletResponse response) {
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        if (hsu != null)
            hsu.rollbackTransaction();
        closeSession();
        JSONObject outjson = new JSONObject();
        outjson.put("_Success", false);
        outjson.put("_ErrorMessage", "Login failure.");
        outjson.put("_ErrorCode", 2);  // logout
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e1) {
            // ignore
        }
        out.print(outjson);
        out.flush();
        response.setContentType("application/json");
        response.setStatus(200);
    }

}
