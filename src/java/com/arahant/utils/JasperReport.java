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

package com.arahant.utils;

import com.arahant.exceptions.ArahantException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;
import org.kissweb.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Author: Blake McBride
 * Date: 11/19/21
 */
public class JasperReport {

    private String rptfile;
    private final HashMap<String, Object> vars;

    public JasperReport(String rptfile, HashMap<String, Object> vars) {
        this.rptfile = rptfile;
        this.vars = vars;
    }

    public JasperReport(String rptfile) {
        this.rptfile = rptfile;
        this.vars = null;
    }

    public String createReport() {
        if (rptfile.endsWith(".jrxml"))
            rptfile = StringUtils.drop(rptfile, -6);
        File cfp = null;
        int idx = rptfile.lastIndexOf("/");
        String fname = idx == -1 ? rptfile : rptfile.substring(idx+1);
        String spath = FileSystemUtils.getSourcePath() + "../JasperReports/" + rptfile + ".jrxml";
        String cpath = null;
        try {
            cpath = (cfp = FileSystemUtils.createTempFile(fname, ".jasper")).getAbsolutePath();
            String ppath = FileSystemUtils.createReportFile(fname, ".pdf").getAbsolutePath();
            JasperCompileManager.compileReportToFile(spath, cpath);
            JasperRunManager.runReportToPdfFile(cpath, ppath, vars, ArahantSession.getHSU().getConnection());
            return ppath;
        } catch (IOException | JRException e) {
            throw new ArahantException(e);
        } finally {
            if (cfp != null)
                cfp.delete();
        }
    }

    public static void main(String [] args) {
        JasperReport rpt = new JasperReport("Test/Coffee");
        String file = rpt.createReport();
        System.out.println(file);
    }

}
