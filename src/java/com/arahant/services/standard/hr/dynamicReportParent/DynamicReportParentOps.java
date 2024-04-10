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
package com.arahant.services.standard.hr.dynamicReportParent;

import com.arahant.beans.ReportColumn;
import com.arahant.beans.ReportSelection;
import com.arahant.beans.ReportTitle;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.dynamic.reports.ExportGenerator;
import com.arahant.dynamic.reports.ReportGenerator;
import com.arahant.exceptions.ArahantWarning;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrDynamicReportParentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class DynamicReportParentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(DynamicReportParentOps.class);
	
	public DynamicReportParentOps() {
		super();
	}
	
    @WebMethod()
	public SearchDynamicReportsReturn searchDynamicReports(/*@WebParam(name = "in")*/final SearchDynamicReportsInput in) {

		final SearchDynamicReportsReturn ret = new SearchDynamicReportsReturn();

		try
		{
			checkLogin(in);

			ret.setItem(BReport.search(in.getReportType(), in.getReportName(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public NewDynamicReportReturn newDynamicReport(/*@WebParam(name = "in")*/final NewDynamicReportInput in) {

		final NewDynamicReportReturn ret = new NewDynamicReportReturn();

		try
		{
			checkLogin(in);
			BReport br = new BReport();
			ret.setId(br.create());
			in.setData(br);
			br.insert();

			finishService(ret);
		}
		catch (final Exception e) {
			ret.setId("");
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public DeleteDynamicReportsReturn deleteDynamicReports(/*@WebParam(name = "in")*/final DeleteDynamicReportsInput in) {

		final DeleteDynamicReportsReturn ret = new DeleteDynamicReportsReturn();

		try
		{
			checkLogin(in);
			
			for (String id : in.getIds())
			{
				new BReport(id).delete();
			}

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public GetExportReturn getExport(/*@WebParam(name = "in")*/final GetExportInput in) {

		final GetExportReturn ret = new GetExportReturn();

		try
		{
			checkLogin(in);

			BReport br = new BReport(in.getDynamicReportId());

			if(br.getReportColumns().size() == 0)
				throw new ArahantWarning("There are no columns specified for this export.  Please add columns before running.");

			ret.setExportUrl(new ExportGenerator().build(br.getBean()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {

		final GetReportReturn ret = new GetReportReturn();

		try
		{
			checkLogin(in);
			BReport br = new BReport(in.getDynamicReportId());
			
			if(br.getReportColumns().size() == 0)
				throw new ArahantWarning("There are no columns specified for this report.  Please add columns before running.");

			ret.setReportUrl(new ReportGenerator(br.getBean()).build(br.getBean()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
    @WebMethod()
	public CopyDynamicReportReturn copyDynamicReport(/*@WebParam(name = "in")*/final CopyDynamicReportInput in) {

		final CopyDynamicReportReturn ret = new CopyDynamicReportReturn();

		try
		{
			checkLogin(in);
			
			BReport br = new BReport(in.getDynamicReportId());
			List<ReportColumn> cols = br.getReportColumns();
			List<ReportTitle> titles = br.getReportTitles();
			List<ReportSelection> sels = br.getReportSelections();
//			List<ReportGraphic> graphs = br.getReportGraphics();
			BReportColumn newC = new BReportColumn();
			BReportTitle newT = new BReportTitle();
			BReportSelection newSe = new BReportSelection();
//			BReportColumn newG = new BReportColumn();

			BReport newBr = new BReport(br.getBean().clone());
			newBr.setReportName(in.getReportName());
			String newBrId = newBr.getReportId();
			newBr.insert();
			hsu.flush();
			
			for(ReportColumn c : cols) {
				newC = new BReportColumn(c.clone());
				newC.setReportId(newBrId);
				newC.insert();
			}
			for(ReportTitle t : titles) {
				newT = new BReportTitle(t.clone());
				newT.setReportId(newBrId);
				newT.insert();
			}
			for(ReportSelection se : sels) {
				newSe = new BReportSelection(se.clone());
				newSe.setReportId(newBrId);
				newSe.insert();
			}
//			for(ReportGraphic g : graphs) {
//				BReportGraphic newD = new BReportGraphic(d.clone());
//				newG.setReportId(newBrId);
//			}
//			newG.insert();

			ret.setData(newBr);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListCriteriaReturn listCriteria(/*@WebParam(name = "in")*/final ListCriteriaInput in) {

		final ListCriteriaReturn ret = new ListCriteriaReturn();

		try
		{
			checkLogin(in);

			BReport rp = new BReport(in.getId());
			List<ReportSelection> rsList = rp.getReportSelections();
			List<ReportSelection> runtime = new ArrayList<ReportSelection>();
			for(ReportSelection rs : rsList)
				if(rs.getSelectionType() == 'R')
					runtime.add(rs);

			ret.setItem(BReportSelection.makeArray(runtime));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SaveReportReturn saveReport(/*@WebParam(name = "in")*/final SaveReportInput in) {

		final SaveReportReturn ret = new SaveReportReturn();

		try
		{
			checkLogin(in);
			
			for(SaveReportInputItem i : in.getItem()) {
				BReportSelection bs = new BReportSelection(hsu.get(ReportSelection.class, i.getSelectionId()));

				bs.setSelectionValue(i.getValue());
				bs.update();
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
