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


package com.arahant.services.standard.at.applicantQuestion;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ApplicantQuestionsReport;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.arahant.utils.IDGenerator;
import com.arahant.utils.KissConnection;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardAtApplicantQuestionOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ApplicantQuestionOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(ApplicantQuestionOps.class);

	public ApplicantQuestionOps() {
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

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
	public SearchPositionsReturn searchPositions(/*@WebParam(name = "in")*/final SearchPositionsInput in) {
		final SearchPositionsReturn ret = new SearchPositionsReturn();
		try {
			checkLogin(in);
			ret.fillItems();
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListQuestionsForPositionReturn listQuestionsForPosition(/*@WebParam(name = "in")*/final ListQuestionsForPositionInput in) {
		final ListQuestionsForPositionReturn ret = new ListQuestionsForPositionReturn();
		try {
			checkLogin(in);

			ret.setItem(BApplicantQuestion.list(in.getPositionId(), in.getActiveType(), 0));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewQuestionReturn newQuestion(/*@WebParam(name = "in")*/final NewQuestionInput in) {
		final NewQuestionReturn ret = new NewQuestionReturn();
		try {
			checkLogin(in);

			Connection db = KissConnection.get();
			Record aqr = db.newRecord("applicant_question");
			aqr.set("company_id", hsu.getCurrentCompany().getCompanyId());
			aqr.set("applicant_question_id", IDGenerator.generate("applicant_question", "applicant_question_id"));

			String positionId = in.getPositionId();
			Record rec;
			if (positionId == null ||  positionId.isEmpty())
				rec = db.fetchOne("select question_order from applicant_question where company_id=? and position_id is null order by question_order desc", hsu.getCurrentCompany().getCompanyId());
			else
				rec = db.fetchOne("select question_order from applicant_question where company_id=? and position_id=? order by question_order desc", hsu.getCurrentCompany().getCompanyId(), positionId);
			short seq;
			if (rec != null)
				seq = (short) (rec.getShort("question_order") + 1);
			else
				seq = 1;
			aqr.set("question_order", seq);

			aqr.set("question", in.getQuestion());
			aqr.set("last_active_date", in.getInactiveDate());
			aqr.set("position_id", positionId != null  && !positionId.isEmpty() ? positionId : null);
			aqr.set("data_type", in.getAnswerType());
			aqr.set("internal_use", in.isInternalUse() ? "Y" : "N");
			aqr.addRecord();

			if ("L".equals(in.getAnswerType())) {
				String[] al = in.getListAnswer();
				if (al != null)
					for (String a : al) {
						Record ar = db.newRecord("applicant_question_choice");
						ar.set("applicant_question_choice_id", IDGenerator.generate("applicant_question_choice", "applicant_question_choice_id"));
						ar.set("applicant_question_id", aqr.get("applicant_question_id"));
						ar.set("description", a);
						ar.addRecord();
					}
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveQuestionReturn saveQuestion(/*@WebParam(name = "in")*/final SaveQuestionInput in) {
		final SaveQuestionReturn ret = new SaveQuestionReturn();
		try {
			checkLogin(in);

			final BApplicantQuestion x = new BApplicantQuestion(in.getId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public MoveQuestionReturn moveQuestion(/*@WebParam(name = "in")*/final MoveQuestionInput in) {
		final MoveQuestionReturn ret = new MoveQuestionReturn();
		try {
			checkLogin(in);

			final BApplicantQuestion x = new BApplicantQuestion(in.getId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteQuestionsReturn deleteQuestions(/*@WebParam(name = "in")*/final DeleteQuestionsInput in) {
		final DeleteQuestionsReturn ret = new DeleteQuestionsReturn();
		try {
			checkLogin(in);

			BApplicantQuestion.delete(in.getIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {
		final GetReportReturn ret = new GetReportReturn();
		try {
			checkLogin(in);

			ret.setReportUrl(new ApplicantQuestionsReport().build(BApplicantQuestion.list(in.getJobTypeId(), in.getActiveType(), 0)));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadQuestionReturn loadQuestion(/*@WebParam(name = "in")*/final LoadQuestionInput in) {
		final LoadQuestionReturn ret = new LoadQuestionReturn();
		try {
			checkLogin(in);

			ret.setItem(BApplicantQuestionChoice.list(in.getId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
