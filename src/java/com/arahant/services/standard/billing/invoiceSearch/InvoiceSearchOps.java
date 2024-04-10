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
package com.arahant.services.standard.billing.invoiceSearch;

import com.arahant.business.BClientCompany;
import com.arahant.business.BInvoice;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardBillingInvoiceSearchOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class InvoiceSearchOps extends ServiceBase {

	static ArahantLogger logger = new ArahantLogger(InvoiceSearchOps.class);

	@WebMethod()
	public GetInvoiceReportReturn getInvoiceReport(/*@WebParam(name = "in")*/final GetInvoiceReportInput in) {
		final GetInvoiceReportReturn ret = new GetInvoiceReportReturn();
		try {
			checkLogin(in);

			ret.setReportUrl(BInvoice.getReport(hsu, in.getUser(), in.getInvoiceIds(),
					in.getDescriptionIncluded(), in.getLineItemsIncluded(), in.getDetailIncluded()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public MarkForRetransmitReturn markForRetransmit(/*@WebParam(name = "in")*/final MarkForRetransmitInput in) {
		final MarkForRetransmitReturn ret = new MarkForRetransmitReturn();

		try {
			checkLogin(in);

			BInvoice.markForRetransmit(hsu, in.getInvoiceIds());
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchInvoicesReturn searchInvoices(/*@WebParam(name = "in")*/final SearchInvoicesInput in) {
		final SearchInvoicesReturn ret = new SearchInvoicesReturn();

		try {
			checkLogin(in);
			ret.setInvoices(BInvoice.searchCompanyInvoices(hsu, in, ret.getCap()));
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ReportReturn report(/*@WebParam(name = "in")*/final ReportInput in) {
		final ReportReturn ret = new ReportReturn();

		try {
			checkLogin(in);

			ret.setReportUrl((new Report()).build(BInvoice.searchCompanyInvoices(hsu, in, 0), in));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public DeleteInvoiceReturn deleteInvoice(/*@WebParam(name = "in")*/final DeleteInvoiceInput in) {
		final DeleteInvoiceReturn ret = new DeleteInvoiceReturn();

		try {
			checkLogin(in);
			BInvoice.delete(hsu, in.getInvoiceIds());
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchClientCompanyReturn searchClientCompany(/*@WebParam(name = "in")*/final SearchClientCompanyInput in) {
		final SearchClientCompanyReturn ret = new SearchClientCompanyReturn();

		try {
			checkLogin(in);

			ret.setClientList(BClientCompany.searchClientCompanies(hsu, in.getName(), ret.getHighCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
}

/*
 AccountingManagementService.getInvoiceReportObj																																																																																																																																																																																																																																																													
 AccountingManagementService.markForRetransmitObj																																																																																																																																																																																																																																																													
 AccountingManagementService.searchInvoicesObj																																																																																																																																																																																																																																																													
 TimesheetManagementService.deleteInvoiceObj																																																																																																																																																																																																																																																													

 */
