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


package com.arahant.services.standard.hr.benefitExport;

import com.arahant.beans.Person;
import com.arahant.business.BEDITransaction;
import com.arahant.business.BVendorCompany;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrBenefitExportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class BenefitExportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(BenefitExportOps.class);

	public BenefitExportOps() {
		super();
	}

	@WebMethod()
	public SearchVendorsReturn searchVendors(/*
			 * @WebParam(name = "in")
			 */final SearchVendorsInput in) {
		final SearchVendorsReturn ret = new SearchVendorsReturn();
		try {
			checkLogin(in);

			ret.setItem(BVendorCompany.listInsuranceProviders(in.getName(), ret.getHighCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchBenefitExportsReturn searchBenefitExports(/*
			 * @WebParam(name = "in")
			 */final SearchBenefitExportsInput in) {
		final SearchBenefitExportsReturn ret = new SearchBenefitExportsReturn();
		try {
			checkLogin(in);

			ret.setItem(BEDITransaction.search(in.getFromDate(), in.getToDate(), in.getVendorId(), ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewBenefitExportReturn newBenefitExport(/*
			 * @WebParam(name = "in")
			 */final NewBenefitExportInput in) {
		final NewBenefitExportReturn ret = new NewBenefitExportReturn();
		try {
			checkLogin(in);

			new EDIThread(in.getVendorId(), in.getEdiFileType(), in.getEdiFileStatus(), ArahantSession.getHSU().getCurrentPerson().getPersonId()).start();

			ret.setId("");
			//x.send834Insurance();


			//hsu and errors will be handled by thread
			//	finishService(ret);

		} catch (final Exception e) {
			//	handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	private static class EDIThread extends Thread {

		private String vendorId;
		private String personId;
		private String ediFileType;
		private String ediFileStatus;

		public EDIThread(String vendorId, String ediFileType, String ediFileStatus, String personId) {
			this.personId = personId;
			this.vendorId = vendorId;
			this.ediFileStatus = ediFileStatus;
			this.ediFileType = ediFileType;
			setDaemon(true);
		}

		@Override
		public void run() {
			HibernateSessionUtil hsu = ArahantSession.openHSU();
			hsu.beginTransaction();
			hsu.setCurrentPerson(hsu.get(Person.class, personId));

			final BEDITransaction x = new BEDITransaction();
			x.create();
			x.setReceiver(vendorId);
			x.sendExport(true, ediFileType, ediFileStatus);
			hsu.commitTransaction();
			ArahantSession.clearSession();
		}
	}

	@WebMethod()
	public GetReportReturn getReport(/*
			 * @WebParam(name = "in")
			 */final GetReportInput in) {
		final GetReportReturn ret = new GetReportReturn();
		try {
			checkLogin(in);

			ret.setReportUrl(""/*
					 * new BEDITransaction().getReport()
					 */);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
