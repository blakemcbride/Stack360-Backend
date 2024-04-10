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


/**
 *
 */
package com.arahant.services.standard.hr.hrMiscReport;

import com.arahant.beans.Person;
import com.arahant.beans.PersonChangeRequest;
import com.arahant.business.BPerson;
import com.arahant.reports.PendingChangeRequests;
import com.arahant.reports.PersonChangeRequestReport;
import com.arahant.reports.ReportBase;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

/**
 *
 *
 *
 */
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrHrMiscReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class HrMiscReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HrMiscReportOps.class);

	public HrMiscReportOps() {
		super();
	}

	@WebMethod()
	public ListReportsReturn listReports(/*
			 * @WebParam(name = "in")
			 */final ListReportsInput in) {
		final ListReportsReturn ret = new ListReportsReturn();
		try {
			checkLogin(in);

			final String[][] reports = new String[][]{
				{"1", "CIGNA, METLIFE, M of O, VSP Benefit Change Requests"},
				{"2", "Pending Change Requests"}, /*
			 * {"2", "CIGNA Changes CSV"}, {"3", "MetLife Changes CSV"}, {"4",
			 * "Mutual Of Omaha Changes CSV"}, {"5", "VSP Changes CSV"},
			 */};

			ret.setItem(reports);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*
			 * @WebParam(name = "in")
			 */final GetReportInput in) {
		final GetReportReturn ret = new GetReportReturn();
		try {
			checkLogin(in);

			if ("2".equals(in.getReportId()))
				ret.setReportUrl(new PendingChangeRequests().build());
			if ("1".equals(in.getReportId())) {
				HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class).selectFields(Person.PERSONID).orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME).orderBy(Person.SSN).joinTo(Person.CHANGE_REQUESTS).orderBy(PersonChangeRequest.REQUEST_DATE);



				HashSet<String> ids = new HashSet<String>();
				ids.addAll((List) hcu.list());

				List<File> files = new LinkedList<File>();
				for (String id : ids)
					try {
						BPerson bp = new BPerson(id);

						//System.out.println(bp.getNameLFM());

						File fyle = new File(new PersonChangeRequestReport().build(id, false));

						File newName = new File(fyle.getParentFile(), bp.getLastName() + "_" + bp.getFirstName() + "_" + bp.getMiddleName() + ".pdf");
						fyle.renameTo(newName);

						files.add(newName);
					} catch (Exception e) {
						continue; //do the next one
					} //break;

				File zipFile;
				if (files.isEmpty())
					zipFile = new File("No data to report");
				else
					zipFile = new File(files.get(0).getParentFile(), "Changes.zip");
				ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
				for (File fyle : files) {
					ZipEntry ze = new ZipEntry(fyle.getName());
					ze.setSize(fyle.length());
					zos.putNextEntry(ze);

					FileInputStream fis = new FileInputStream(fyle);

					int x;
					while ((x = fis.read()) != -1)
						zos.write(x);
					zos.closeEntry();

					fyle.delete();
				}
				zos.close();

				ret.setReportUrl(FileSystemUtils.getHTTPPath(zipFile));
			}
			/*
			 * else if ("2".equals(in.getReportId())) { ret.setReportUrl(); }
			 *
			 */

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
