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
package com.arahant.services.standard.hr.dynamicReport;

import com.arahant.beans.Report;
import com.arahant.beans.ReportGraphic;
import com.arahant.beans.ReportSelection;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.dynamic.reports.DynamicReportBase;
import com.arahant.dynamic.reports.DynamicReportColumn;
import com.arahant.dynamic.reports.ExportGenerator;
import com.arahant.dynamic.reports.ReportGenerator;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrDynamicReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class DynamicReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(DynamicReportOps.class);
	
	public DynamicReportOps() {
		super();
	}
	
    @WebMethod()
	public GetExportReturn getExport(/*@WebParam(name = "in")*/final GetExportInput in) {

		final GetExportReturn ret = new GetExportReturn();

		try
		{
			checkLogin(in);

			Report r = new BReport(in.getDynamicReportId()).getBean();

			ret.setExportUrl(new ExportGenerator().build(r));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListCategoriesReturn listCategories(/*@WebParam(name = "in")*/final ListCategoriesInput in) {

		final ListCategoriesReturn ret = new ListCategoriesReturn();

		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitCategory.list(in.getExcludeIdsArray(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListBenefitsReturn listBenefits(/*@WebParam(name = "in")*/final ListBenefitsInput in) {

		final ListBenefitsReturn ret = new ListBenefitsReturn();

		try
		{
			checkLogin(in);

			BHRBenefitCategory bcat = new BHRBenefitCategory(in.getCategoryId());
			ret.setItem(bcat.listBenefits(in.getExcludeIdsArray(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListConfigsReturn listConfigs(/*@WebParam(name = "in")*/final ListConfigsInput in) {

		final ListConfigsReturn ret = new ListConfigsReturn();

		try
		{
			checkLogin(in);

			BHRBenefit bben = new BHRBenefit(in.getBenefitId());
			ret.setItem(bben.listConfigs(in.getExcludeIdsArray(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchVendorsReturn searchVendors(/*@WebParam(name = "in")*/final SearchVendorsInput in)
	{
		final SearchVendorsReturn ret=new SearchVendorsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BVendorCompany.listInsuranceProviders(in.getName(),ret.getHighCap()));

			if(!isEmpty(in.getReportId())) {
				String vendorId = "";
				BReport br = new BReport(in.getReportId());
				for(ReportSelection sel : br.getReportSelections()) {
					if(sel.getDescription().equals("Benefit Provider is"))
						vendorId = sel.getSelectionValue();
				}

				if(!isEmpty(vendorId))
					ret.setSelectedItem(new SearchVendorsReturnItem(new BVendorCompany(vendorId)));
			}

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchOrgGroupsReturn searchOrgGroups(/*@WebParam(name = "in")*/final SearchOrgGroupsInput in)	{
		final SearchOrgGroupsReturn ret=new SearchOrgGroupsReturn();

		try
		{
			checkLogin(in);

			if(!isEmpty(in.getReportId())) {
				String selectedGroupId = "";
				BReport br = new BReport(in.getReportId());
				for(ReportSelection sel : br.getReportSelections()) {
					if(sel.getDescription().equals("Organizational Group is"))
						selectedGroupId = sel.getSelectionValue();
				}

				if(!isEmpty(selectedGroupId))
					ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(selectedGroupId)));
			}
			
			String groupId = hsu.getCurrentCompany().getOrgGroupId();

			ret.setItem(BOrgGroup.listAssociatedOrgGroups(hsu, groupId, EMPLOYEE_TYPE, in.getName(), ret.getHighCap()));

			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SaveDynamicReportReturn saveDynamicReport(/*@WebParam(name = "in")*/final SaveDynamicReportInput in) {

		final SaveDynamicReportReturn ret = new SaveDynamicReportReturn();

		try
		{
			checkLogin(in);

			BReport br = new BReport(in.getReportId());
			in.setData(br);
			br.update();

			ArahantSession.getHSU().flush();
			
			for(ReportSelection rs : br.getReportSelections())
			if(rs.getSelectionType() == 'R') {
				ret.setRunTime(true);
				break;
			}

			ret.setReportColumns(new BReport(br.getReportId()).getReportColumns());

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadDynamicReportReturn loadDynamicReport(/*@WebParam(name = "in")*/final LoadDynamicReportInput in) {

		final LoadDynamicReportReturn ret = new LoadDynamicReportReturn();

		try
		{
			checkLogin(in);

			ret.setData(new BReport(in.getReportId()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListReportColumnsReturn listReportColumns(/*@WebParam(name = "in")*/final ListReportColumnsInput in) {

		final ListReportColumnsReturn ret = new ListReportColumnsReturn();

		try
		{
			checkLogin(in);

			ret.setData(new BReport(in.getReportId()));

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

			Report r = new BReport(in.getDynamicReportId()).getBean();
			
			ret.setReportUrl(new ReportGenerator(r).build(r));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListFormatCodesReturn listFormatCodes(/*@WebParam(name = "in")*/final ListFormatCodesInput in) {

		final ListFormatCodesReturn ret = new ListFormatCodesReturn();

		try
		{
			checkLogin(in);

			DynamicReportColumn drc = new DynamicReportColumn();

			if(!isEmpty(in.getReportColumnId())) {
				BReportColumn rc = new BReportColumn(in.getReportColumnId());
				ret.setCurrentFormatCode(new BReportColumn(in.getReportColumnId()).getFormatCode());
				drc = DynamicReportBase.getColumnByTypeIndex(rc.getReport().getReportType(), rc.getColumnId());
			}
			else {
				drc = new DynamicReportColumn();
				drc.setFormatType(in.getFormatType());
			}

			ret.setItem(drc);
			
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

    @WebMethod()
	public SaveGraphicReturn saveGraphic(/*@WebParam(name = "in")*/final SaveGraphicInput in) {

		final SaveGraphicReturn ret = new SaveGraphicReturn();

		try
		{
			checkLogin(in);

			BReportGraphic brg = new BReportGraphic(in.getGraphicId());
			in.setData(brg);
			brg.update();
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListReportGraphicsReturn listReportGraphics(/*@WebParam(name = "in")*/final ListReportGraphicsInput in) {

		final ListReportGraphicsReturn ret = new ListReportGraphicsReturn();

		try
		{
			checkLogin(in);

			ret.setData(new BReport(in.getReportId()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteReportGraphicsReturn deleteReportGraphics(/*@WebParam(name = "in")*/final DeleteReportGraphicsInput in) {

		final DeleteReportGraphicsReturn ret = new DeleteReportGraphicsReturn();

		try
		{
			checkLogin(in);

			ArahantSession.getHSU().createCriteria(ReportGraphic.class).in(ReportGraphic.REPORT_GRAPHIC_ID, in.getIds()).delete();

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
