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
package com.arahant.services.standard.hr.benefitCategory;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BHrBenefitCategoryAnswer;
import com.arahant.business.BHrBenefitCategoryQuestion;
import com.arahant.business.BRight;
import com.arahant.business.BScreen;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

 
/**
 * 
 * 
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrBenefitCategoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BenefitCategoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BenefitCategoryOps.class);
	
	public BenefitCategoryOps() {
		super();
	}
	
	@WebMethod()
	public ListCategoriesReturn listCategories(/*@WebParam(name = "in")*/final ListCategoriesInput in)			{
		final ListCategoriesReturn ret=new ListCategoriesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitCategory.list());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public NewCategoryReturn newCategory(/*@WebParam(name = "in")*/final NewCategoryInput in)			{
		final NewCategoryReturn ret=new NewCategoryReturn();
		try
		{
			checkLogin(in);
			
			final BHRBenefitCategory x=new BHRBenefitCategory();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public SaveCategoryReturn saveCategory(/*@WebParam(name = "in")*/final SaveCategoryInput in)			{
		final SaveCategoryReturn ret=new SaveCategoryReturn();
		try
		{
			checkLogin(in);
			
			final BHRBenefitCategory x=new BHRBenefitCategory(in.getCategoryId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public DeleteCategoriesReturn deleteCategories(/*@WebParam(name = "in")*/final DeleteCategoriesInput in)			{
		final DeleteCategoriesReturn ret=new DeleteCategoriesReturn();
		try
		{
			checkLogin(in);
			
			BHRBenefitCategory.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public GetCategoryReportReturn getCategoryReport(/*@WebParam(name = "in")*/final GetCategoryReportInput in)			{
		final GetCategoryReportReturn ret=new GetCategoryReportReturn();
		try
		{  
			checkLogin(in);
			
			ret.setReportUrl(new BHRBenefitCategory().getReport());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight(ACCESS_HRSETUPS));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	} 	
	
	@WebMethod()
	public ListTypesReturn listTypes(/*@WebParam(name = "in")*/final ListTypesInput in)	{
		final ListTypesReturn ret=new ListTypesReturn();
		
		try {
			checkLogin(in);
			
			ret.setItem(BHRBenefitCategory.listTypes());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

    @WebMethod()
	public ReorderCategoriesReturn reorderCategories(/*@WebParam(name = "in")*/final ReorderCategoriesInput in)		
	{
		final ReorderCategoriesReturn ret=new ReorderCategoriesReturn();
		try
		{
			checkLogin(in);

			BHRBenefitCategory bc;

			int i = 0;
			for (String s: in.getIds())
			{
				bc = new BHRBenefitCategory(s);
				bc.setSequence(i);
				bc.update();
				i++;
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public MoveCategoryReturn moveCategory(/*@WebParam(name = "in")*/final MoveCategoryInput in)		
	{
		final MoveCategoryReturn ret=new MoveCategoryReturn();
		try
		{
			checkLogin(in);
			
			final BHRBenefitCategory x=new BHRBenefitCategory(in.getId());
			x.moveUp(in.getMoveUp());
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public SearchScreensReturn searchScreens(/*@WebParam(name = "in")*/final SearchScreensInput in)	{
		final SearchScreensReturn ret=new SearchScreensReturn();

		try
		{
			checkLogin(in);
			
			ret.setData(BScreen.searchScreens(in.getName(),in.getExtId()));
					
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
    @WebMethod()
	public ListQuestionsReturn listQuestions(/*@WebParam(name = "in")*/final ListQuestionsInput in) {

		final ListQuestionsReturn ret = new ListQuestionsReturn();

		try
		{
			checkLogin(in);

			ret.setItem(BHrBenefitCategoryQuestion.list(in.getCategoryId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public NewQuestionReturn newQuestion(/*@WebParam(name = "in")*/final NewQuestionInput in) {

		final NewQuestionReturn ret = new NewQuestionReturn();

		try
		{
			checkLogin(in);
			
			final BHrBenefitCategoryQuestion x = new BHrBenefitCategoryQuestion();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SaveQuestionReturn saveQuestion(/*@WebParam(name = "in")*/final SaveQuestionInput in) {

		final SaveQuestionReturn ret = new SaveQuestionReturn();

		try
		{
			checkLogin(in);
			
			final BHrBenefitCategoryQuestion x = new BHrBenefitCategoryQuestion(in.getQuestionId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ReorderQuestionsReturn reorderQuestions(/*@WebParam(name = "in")*/final ReorderQuestionsInput in) {

		final ReorderQuestionsReturn ret = new ReorderQuestionsReturn();

		try
		{
			checkLogin(in);
			
			Random rand = new Random();
			int newIndex = 1000;
			int count = 0;
			List<BHrBenefitCategoryQuestion> questions = new ArrayList<BHrBenefitCategoryQuestion>();
			for(String id : in.getIds()) {
				BHrBenefitCategoryQuestion newQ = new BHrBenefitCategoryQuestion(id);
				newQ.setSequenceNumber(newIndex + count);
				questions.add(newQ);
				++count;
			}
			ArahantSession.getHSU().flush();
			for(int i = 0; i < questions.size(); i++)
				questions.get(i).setSequenceNumber(i);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListBenefitsForQuestionReturn listBenefitsForQuestion(/*@WebParam(name = "in")*/final ListBenefitsForQuestionInput in) {

		final ListBenefitsForQuestionReturn ret = new ListBenefitsForQuestionReturn();

		try
		{
			checkLogin(in);

			ret.setItem(new BHrBenefitCategoryQuestion(in.getQuestionId()).listAvailableBenefits());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListAnswersForQuestionReturn listAnswersForQuestion(/*@WebParam(name = "in")*/final ListAnswersForQuestionInput in) {

		final ListAnswersForQuestionReturn ret = new ListAnswersForQuestionReturn();

		try
		{
			checkLogin(in);
			BHrBenefitCategoryQuestion q =new BHrBenefitCategoryQuestion(in.getQuestionId());
			ret.setItem(q.listAnswers(ret.getCap()), q.getBenefitCat().getBenefitCatId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public NewAnswerReturn newAnswer(/*@WebParam(name = "in")*/final NewAnswerInput in) {

		final NewAnswerReturn ret = new NewAnswerReturn();

		try
		{
			checkLogin(in);
			
			final BHrBenefitCategoryAnswer x = new BHrBenefitCategoryAnswer();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			ret.setId("");
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SaveAnswerReturn saveAnswer(/*@WebParam(name = "in")*/final SaveAnswerInput in) {

		final SaveAnswerReturn ret = new SaveAnswerReturn();

		try
		{
			checkLogin(in);
			
			final BHrBenefitCategoryAnswer x = new BHrBenefitCategoryAnswer(in.getAnswerId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
    public DeleteAnswersReturn deleteAnswers(/*@WebParam(name = "in")*/final DeleteAnswersInput in) {

		final DeleteAnswersReturn ret = new DeleteAnswersReturn();

		try
		{
			checkLogin(in);
			
			BHrBenefitCategoryAnswer.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
    public DeleteQuestionsReturn deleteQuestions(/*@WebParam(name = "in")*/final DeleteQuestionsInput in) {

		final DeleteQuestionsReturn ret = new DeleteQuestionsReturn();

		try
		{
			checkLogin(in);
			
			BHrBenefitCategoryQuestion.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
