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


package com.arahant.services.standard.hrConfig.wizardRequiredDocuments;

import com.arahant.beans.BenefitChangeReasonDoc;
import com.arahant.beans.BenefitDocument;
import com.arahant.beans.CompanyForm;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitChangeReason;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import java.util.List;



/**
 *
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrConfigWizardRequiredDocumentsOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class WizardRequiredDocumentsOps extends ServiceBase {

	static ArahantLogger logger = new ArahantLogger(WizardRequiredDocumentsOps.class);

	
    @WebMethod()
	public NewBenefitDocumentReturn newBenefitDocument(/*@WebParam(name = "in")*/final NewBenefitDocumentInput in) {

		final NewBenefitDocumentReturn ret = new NewBenefitDocumentReturn();

		try
		{
			checkLogin(in);
			
			BBenefitDocument bd = new BBenefitDocument();
			bd.create();
			bd.setBenefit(hsu.get(HrBenefit.class, in.getBenefitId()));
			if(!isEmpty(in.getCompanyFormId()))
				bd.setCompanyForm(hsu.get(CompanyForm.class, in.getCompanyFormId()));
			if(!isEmpty(in.getInstructions()))
				bd.setInstructions(in.getInstructions());
			bd.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public SaveBenefitDocumentReturn saveBenefitDocument(/*@WebParam(name = "in")*/final SaveBenefitDocumentInput in) {

		final SaveBenefitDocumentReturn ret = new SaveBenefitDocumentReturn();

		try
		{
			checkLogin(in);
			
			BBenefitDocument bd = new BBenefitDocument(hsu.get(BenefitDocument.class, in.getBenefitDocumentId()));
			if(bd != null) {
				if(!isEmpty(in.getCompanyFormId()))
					bd.setCompanyForm(hsu.get(CompanyForm.class, in.getCompanyFormId()));
				if(!isEmpty(in.getInstructions()))
					bd.setInstructions(in.getInstructions());
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public DeleteBenefitDocumentsReturn deleteBenefitDocuments(/*@WebParam(name = "in")*/final DeleteBenefitDocumentsInput in) {

		final DeleteBenefitDocumentsReturn ret = new DeleteBenefitDocumentsReturn();

		try
		{
			checkLogin(in);

			BBenefitDocument.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public DeleteBcdDocumentsReturn deleteBcdDocuments(/*@WebParam(name = "in")*/final DeleteBcdDocumentsInput in) {

		final DeleteBcdDocumentsReturn ret = new DeleteBcdDocumentsReturn();

		try
		{
			checkLogin(in);
			
			BBenefitChangeReasonDoc.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public SaveBcrDocumentsReturn saveBcrDocuments(/*@WebParam(name = "in")*/final SaveBcrDocumentsInput in) {

		final SaveBcrDocumentsReturn ret = new SaveBcrDocumentsReturn();

		try
		{
			checkLogin(in);
			
			BBenefitChangeReasonDoc bcrd = new BBenefitChangeReasonDoc(hsu.get(BenefitChangeReasonDoc.class, in.getBcrDocumentId()));
			if(bcrd != null) {
				if(!isEmpty(in.getCompanyFormId()))
					bcrd.setCompanyForm(hsu.get(CompanyForm.class, in.getCompanyFormId()));
				if(!isEmpty(in.getInstructions()))
					bcrd.setInstructions(in.getInstructions());
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public NewBcrDocumentReturn newBcrDocument(/*@WebParam(name = "in")*/final NewBcrDocumentInput in) {

		final NewBcrDocumentReturn ret = new NewBcrDocumentReturn();

		try
		{
			checkLogin(in);
			
			BBenefitChangeReasonDoc bd = new BBenefitChangeReasonDoc();
			bd.create();
			bd.setBenefitChangeReason(hsu.get(HrBenefitChangeReason.class, in.getBcrId()));
			if(!isEmpty(in.getCompanyFormId()))
				bd.setCompanyForm(hsu.get(CompanyForm.class, in.getCompanyFormId()));
			if(!isEmpty(in.getInstructions()))
				bd.setInstructions(in.getInstructions());
			bd.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListBenefitDocumentsReturn listBenefitDocuments(/*@WebParam(name = "in")*/final ListBenefitDocumentsInput in) {

		final ListBenefitDocumentsReturn ret = new ListBenefitDocumentsReturn();

		try
		{
			checkLogin(in);
			
			List<BenefitDocument> bdList = hsu.createCriteria(BenefitDocument.class).list();

			ret.setItem(BBenefitDocument.makeArray(bdList));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListAvailableDocumentsReturn listAvailableDocuments(/*@WebParam(name = "in")*/final ListAvailableDocumentsInput in) {

		final ListAvailableDocumentsReturn ret = new ListAvailableDocumentsReturn();

		try
		{
			checkLogin(in);
			
			List<CompanyForm> forms = hsu.createCriteria(CompanyForm.class).notIn(CompanyForm.COMPANY_FORM_ID, in.getExcludeIds())
																		   .like(CompanyForm.COMMENTS, in.getName()).list();
			if(!isEmpty(in.getCompanyFormId()))
				ret.setSelectedItem(new ListAvailableDocumentsReturnItem(new BCompanyForm(in.getCompanyFormId())));
			ret.setItem(BCompanyForm.makeArray(forms));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public ListBcrDocumentsReturn listBcrDocuments(/*@WebParam(name = "in")*/final ListBcrDocumentsInput in) {

		final ListBcrDocumentsReturn ret = new ListBcrDocumentsReturn();

		try
		{
			checkLogin(in);

			List<BenefitChangeReasonDoc> bcrList = hsu.createCriteria(BenefitChangeReasonDoc.class).list();

			ret.setItem(BBenefitChangeReasonDoc.makeArray(bcrList));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public ListAvailableBcrsReturn listAvailableBcrs(/*@WebParam(name = "in")*/final ListAvailableBcrsInput in) {

		final ListAvailableBcrsReturn ret = new ListAvailableBcrsReturn();

		try
		{
			checkLogin(in);
			
			List<HrBenefitChangeReason> bcrList = hsu.createCriteria(HrBenefitChangeReason.class).list();

			ret.setItem(BHRBenefitChangeReason.makeArray(bcrList));

			if(!isEmpty(in.getBcrId()))
			{
				ret.setSelectedItem(new ListAvailableBcrsReturnItem(new BHRBenefitChangeReason(in.getBcrId())));
			}

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public ListAvailableBenefitsReturn listAvailableBenefits(/*@WebParam(name = "in")*/final ListAvailableBenefitsInput in) {

		final ListAvailableBenefitsReturn ret = new ListAvailableBenefitsReturn();

		try
		{
			checkLogin(in);
			
			List<HrBenefit> beneList = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.TIMERELATED, 'N')
																		  .like(HrBenefit.NAME, in.getName()).list();
			if(!isEmpty(in.getBenefitId()))
				ret.setSelectedItem(new ListAvailableBenefitsReturnItem(new BHRBenefit(in.getBenefitId())));
			ret.setItem(BHRBenefit.makeArray(beneList));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListFormTypesReturn listFormTypes(/*@WebParam(name = "in")*/final ListFormTypesInput in)	{
		final ListFormTypesReturn ret=new ListFormTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BFormType.list('E'));

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
