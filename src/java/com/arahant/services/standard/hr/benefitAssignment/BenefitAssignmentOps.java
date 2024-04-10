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



package com.arahant.services.standard.hr.benefitAssignment;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import com.arahant.services.utils.CoverageValidator;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrBenefitAssignmentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class BenefitAssignmentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(BenefitAssignmentOps.class);

	public BenefitAssignmentOps() {
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*
			 * @WebParam(name = "in")
			 */final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight(ACCESS_HR));

			finishService(ret);
		} catch (final Throwable e) {
			hsu.rollbackTransaction();
			logger.error(e);
			return new CheckRightReturn("Failed: Contact Administrator");
		}

		return ret;
	}

	@WebMethod()
	public ListAssignedBenefitConfigurationsReturn listAssignedBenefitConfigurations(/*
			 * @WebParam(name = "in")
			 */final ListAssignedBenefitConfigurationsInput in) {
		final ListAssignedBenefitConfigurationsReturn ret = new ListAssignedBenefitConfigurationsReturn();
		try {
			checkLogin(in);

			final BPerson bPerson = new BPerson(in.getPersonId());
			ArahantSession.setCalcDate(DateUtils.now());
			bPerson.deleteExpiredBenefits();
			hsu.commitTransaction();
			hsu.beginTransaction();
			try {
				BEmployee emp = new BEmployee(in.getPersonId());
				boolean hasUnapproved = hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON, emp.getRealAndChangePerson()).eq(HrBenefitJoin.APPROVED, 'N').exists();

				hsu.rollbackTransaction();
				hsu.beginTransaction();

				if (!hasUnapproved) {
					emp.setBenefitWizardStatus(Employee.BENEFIT_WIZARD_STATUS_PROCESSED + "");
					emp.update();
					hsu.commitTransaction();
					hsu.beginTransaction();
				}
			} catch (Exception e) {
				//probably wasn't an employee, just continue
			}
			ret.setItem(bPerson.listBenefitConfigs(in.getShowApprovedBenefit(), in.getShowApprovedDecline(), in.getShowNotYetApproved()), bPerson);
			hsu.rollbackTransaction();
			hsu.beginTransaction();
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		//logger.info("list assigned done");
		return ret;
	}

	@WebMethod()
	public ListBenefitCategoriesReturn listBenefitCategories(/*
			 * @WebParam(name = "in")
			 */final ListBenefitCategoriesInput in) {
		final ListBenefitCategoriesReturn ret = new ListBenefitCategoriesReturn();
		try {
			checkLogin(in);

			final BPerson bemp = new BPerson(in.getPersonId());
			ret.setItem(bemp.listBenefitCats(in.isForDecline(), in.getNoFiltering()), bemp);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	/*
	 * honors active flag - but - if benefitId is passed this is an edit and you
	 * should use this to decide if you need to include a possibly inactive
	 * benefit (the one coming in) .. for example, suppose benefit ABC is
	 * inactive, but i pass that one in (benefitId) ... in this case you should
	 * still include it since I have to display it for edit
	 */
	@WebMethod()
	public ListBenefitsReturn listBenefits(/*
			 * @WebParam(name = "in")
			 */final ListBenefitsInput in) {
		final ListBenefitsReturn ret = new ListBenefitsReturn();
		try {
			checkLogin(in);

			final BPerson bemp = new BPerson(in.getPersonId());
			ret.setItem(new BHRBenefitCategory(in.getCategoryId()).listActiveBenefitsByClass(bemp, in.getBenefitId(), in.getForDecline(), DateUtils.now()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	/*
	 * use employeeId to determine what are the possible valid configs (based on
	 * linked spouse, statuses, etc) honors active flag - but - if configId is
	 * passed this is an edit and you should use this to decide if you need to
	 * include a possibly inactive config (the one coming in) .. for example,
	 * suppose config XYZ is inactive, but i pass that one in (configId) ... in
	 * this case you should still include it since I have to display it for edit
	 * coverage is the same coverage logic to generate the string of who is
	 * covered (see previous email for these strings, things like "Employee")
	 * also, if person is a non-employee dependent, only configs that link to a
	 * COBRA benefit should be returned
	 *
	 */
	@WebMethod()
	public ListBenefitConfigsReturn listBenefitConfigs(/*
			 * @WebParam(name = "in")
			 */final ListBenefitConfigsInput in) {
		final ListBenefitConfigsReturn ret = new ListBenefitConfigsReturn();
		try {
			checkLogin(in);
			ArahantSession.setCalcDate(DateUtils.now());
			BEmployee be = new BEmployee(in.getPersonId());
			ret.setItem(be, new BHRBenefit(in.getBenefitId()).listValidConfigs(in.getPersonId(), in.getConfigId(), in.getBenefitId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	private void doAIValidations(String personId, int startDate, String configId, double amountOverrideAnnual) {
		BHRBenefitConfig c = new BHRBenefitConfig(configId);
		int type = c.getBenefit().getHrBenefitCategory().getBenefitType();

		//	ArahantSession.getAI().watchAll();
		//Let AI engine do some quick validation
		if (new BPerson(personId).isEmployee()) {
			BEmployee bemp = new BEmployee(personId);
			long daysOut = DateUtils.getDaysBetween(startDate, bemp.getStartDateInt());
			int afterSixty = DateUtils.getStartOfMonthAfter(startDate, 60);
			long daysFromSixty = DateUtils.getDaysBetween(afterSixty, bemp.getStartDateInt());
			ArahantSession.AIEval("(assert (benefitStartValidation \"" + personId + "\" " + type + " " + daysOut + " " + daysFromSixty + " " + bemp.getStartDateInt() + " " + startDate + "))");
			//that will throw exception if it doesn't like it
		}

//		if (c.getBenefit().deprecatedGetEmployeeIsProvider() == 'Y') //only validate cost if they could have set it
//		
//			ArahantSession.AIEval("(assert (benefitCostValidation " + type + " " + amountOverrideAnnual + "))");

	}

	@WebMethod()
	public SaveAssignedBenefitConfigReturn saveAssignedBenefitConfig(/*
			 * @WebParam(name = "in")
			 */final SaveAssignedBenefitConfigInput in) {
		final SaveAssignedBenefitConfigReturn ret = new SaveAssignedBenefitConfigReturn();
		try {
			checkLogin(in);

			final BHRBenefitJoin bBenefitJoin = new BHRBenefitJoin(in.getPersonConfigId());
			BPerson bPerson = bBenefitJoin.getPayingPerson();

			doAIValidations(bPerson.getPersonId(), in.getStartDate(), bBenefitJoin.getBenefitConfig().getBenefitConfigId(), in.getAmountOverrideAnnual());

			// validate the elected coverages if they have not accepted warnings yet
			if (!in.isWarningsAccepted()) {
				CoverageValidator cv = new CoverageValidator();
				BHRBenefitConfig conf = new BHRBenefitConfig(bBenefitJoin.getBenefitConfig().getBenefitConfigId());
				cv.setBenefitCoversChildren(conf.getCoversChildren());
				cv.setBenefitCoversEmployee(conf.getCoversEmployee());
				cv.setBenefitCoversEmployeeSpouse(conf.getCoversEmployeeSpouse());
				cv.setBenefitCoversEmployeeSpouseOrChildren(conf.getCoversEmployeeSpouseOrChildren());
				cv.setBenefitCoversNonEmployeeSpouse(conf.getSpouseNonEmployee());
				cv.setBenefitCoversNonEmployeeSpouseOrChildren(conf.getSpouseNonEmpOrChildren());
				cv.setBenefitMaxDependents(conf.getMaxChildren());

				int foundActiveChildren;
				boolean activeEmpSpouse = false;
				if (bPerson.isEmployee()) {
					BEmployee be = new BEmployee(bPerson);
					foundActiveChildren = be.getActiveChildren(DateUtils.now());
					cv.setFoundActiveChildren(foundActiveChildren);

					activeEmpSpouse = be.getActiveEmployeeSpouse(in.getStartDate());
					cv.setFoundActiveEmployeeSpouse(activeEmpSpouse);
					cv.setFoundActiveOther(be.getActiveOther());
				} else {
					cv.setFoundActiveChildren(0);
					cv.setFoundActiveEmployeeSpouse(false);
					cv.setFoundActiveOther(0);
				}
				cv.setUseDatesInMessages(true);




				CoverageValidator.InputItem items[] = new CoverageValidator.InputItem[in.getItem().length];

				int requestedChildren = 0;
				boolean requestedEmployee = false;
				int requestedOther = 0;
				boolean requestedSpouse = false;

				for (int loop = 0; loop < in.getItem().length; loop++) {
					items[loop] = new CoverageValidator.InputItem();
					items[loop].startDate = in.getItem()[loop].getStartDate();
					items[loop].endDate = in.getItem()[loop].getEndDate();
					items[loop].active = true;

					if (bPerson.getPersonId().equals(in.getItem()[loop].getPersonId())) {
						items[loop].relationship = 'E';
						requestedEmployee = true;
					} else {
						BHREmplDependent dep = new BHREmplDependent(bPerson.getPersonId(), in.getItem()[loop].getPersonId());
						items[loop].relationship = dep.getRelationship();

						if (dep.getRelationship() == 'S') {
							items[loop].employeeSpouse = activeEmpSpouse;
							requestedSpouse = true;
						}
						if (dep.getRelationship() == 'C')
							requestedChildren++;
						if (dep.getRelationship() == 'O')
							requestedOther++;
					}

				}

				cv.setRequestedChildren(requestedChildren);
				cv.setRequestedEmployee(requestedEmployee);
				cv.setRequestedOther(requestedOther);
				cv.setRequestedSpouse(requestedSpouse);

				cv.validate(items);

				// set cv warnings
				ret.addErrors(cv.getErrors());
				ret.addWarnings(cv.getWarnings());
			}

			// check for non-employee dependent
			if (!bPerson.isEmployee()) {
				boolean currentRelationshipStillValid = false;

				// make sure the relationship is still a valid one for a possible updated start date
				// (just do this by rechecking against the valid list of dependent sponsors for the update assignment)
				BHREmplDependent[] relationships = BHREmplDependent.getDependentSponsors(bPerson.getPersonId(), bBenefitJoin.getBenefitConfig().getConfigId(), in.getStartDate());
				if (relationships.length != 0)
					// make sure the existing relationship is still there
					for (BHREmplDependent relationship : relationships)
						// should never be null, if it is we have a problem in the system with the relationship not getting
						// set for non-employee dependents, or getting lost
						if (relationship.getRelationshipId().equals(bBenefitJoin.getRelationship().getRelationshipId())) {
							currentRelationshipStillValid = true;
							break;
						}

				if (!currentRelationshipStillValid)
					throw new ArahantWarning("The benefit Policy Start Date is invalid for the Sponsoring Employee (" + bBenefitJoin.getRelationship().getEmployeeSSN() + ").  The Dependent must have an Inactive Date prior to the benefit Policy Start Date, or the Sponsoring Employee must have an inactive Employee Status dated prior to the benefit Policy Start Date.");
			}

			in.setData(bBenefitJoin);
			bBenefitJoin.update();

			// if this is an employee, check for errors about changes to the benefit due to how it compares to the status history
			if (bPerson.isEmployee())
				ret.addErrors(new BEmployee(bPerson).getErrorsForBenefitChange(bBenefitJoin));

			// rollback if we have issues that have not been accepted
			if (!in.isWarningsAccepted() && ret.hasErrorsOrWarnings()) {
				hsu.rollbackTransaction();
				hsu.beginTransaction();
			}
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadProposedDetailReturn loadProposedDetail(/*
			 * @WebParam(name = "in")
			 */final LoadProposedDetailInput in) {
		final LoadProposedDetailReturn ret = new LoadProposedDetailReturn();
		try {
			checkLogin(in);

			setSubjectPerson(in.getPersonId());
			ArahantSession.setCalcDate(DateUtils.now());
			final BPerson bPerson = new BPerson(in.getPersonId());

			String changeReasonId = "";

			HrBenefitChangeReason cr = hsu.createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.INTERNAL_STAFF_EDIT).dateInside(HrBenefitChangeReason.START_DATE, HrBenefitChangeReason.END_DATE, DateUtils.now()).first();
			if (cr != null)
				changeReasonId = cr.getHrBenefitChangeReasonId();


			if (in.isCopyExistingCoverages()) {
				// check for an already provided benefit that is provided by someone else (dependent benefit join)
				BHRBenefitJoin bBenefitJoin = bPerson.getCurrentBenefitForNewBenefit(in.getConfigId());
				if (bBenefitJoin != null && bBenefitJoin.isDependentBenefitJoin())
					// yes, so we must auto-separate out the dependent benefit join so that changeBenefitTo
					// can copy the existing coverage information - only reason we are calling this
					bBenefitJoin.separateOutDependentBenefitJoinForPerson(in.getPersonId());

				// copy existing also means there is already an existing, so changeBenefitTo call is safe
				ret.setData(bPerson.changeBenefitTo(in.getConfigId(), in.getUsingCOBRA(), true, false));
				ret.setStartDate(0);
				ret.setEndDate(0);
			} else {
				//	ArahantSession.getAI().watchAll();
				int startDate = bPerson.getNextBenefitStartDate(in.getConfigId());

				BHRBenefitJoin benefitJoin = new BHRBenefitJoin(bPerson.assignBenefitCategories(in.getConfigId(), 0, startDate, in.getUsingCOBRA(), changeReasonId, true, false));


				benefitJoin.setDefaults();

				List<HrBenefitJoin> l = new ArrayList<HrBenefitJoin>();
				l.add(benefitJoin.getBean());
				ret.setData(l);
			}
			ret.setBenefitSupportsPhysicians(true);


			// should not change data
			//hsu.rollbackTransaction();
			hsu.beginTransaction(); //just to prevent error in finish service

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadDetailReturn loadDetail(/*
			 * @WebParam(name = "in")
			 */final LoadDetailInput in) {
		final LoadDetailReturn ret = new LoadDetailReturn();
		try {
			checkLogin(in);
			//	ArahantSession.getAI().watchAll();
			//  ArahantSession.setCalcDate(DateUtils.now());
			BHRBenefitJoin bBenefitJoin = new BHRBenefitJoin(in.getPersonConfigId());

			// make sure we have the policy benefit join in case a dependent benefit join was passed
			if (bBenefitJoin.isDependentBenefitJoin())
				bBenefitJoin = new BHRBenefitJoin(bBenefitJoin.getPolicyBenefitJoin());

			setSubjectPerson(bBenefitJoin.getPayingPersonId());

			ret.setBenefitSupportsPhysicians(true);
			ret.setData(bBenefitJoin);


			//	ArahantSession.AIEval("(facts)");
			// should not change data
			hsu.rollbackTransaction();
			hsu.beginTransaction(); //just to prevent error in finish service

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetBenefitReportReturn getBenefitReport(/*
			 * @WebParam(name = "in")
			 */final GetBenefitReportInput in) {
		final GetBenefitReportReturn ret = new GetBenefitReportReturn();
		try {
			checkLogin(in);

			//TODO In the future when we genericise this, take advantage of in.getShowAsDependent() to change header to say Dependent instead of Employee

			setSubjectPerson(in.getPersonId());
/*
			if (AIProperty.getBoolean("IsWmCo"))
				ret.setReportUrl(new com.arahant.reports.williamsonCounty.EmployeeBenefitStatement().build(new BPerson(in.getPersonId()), in.isIncludeCredentials(), in.getReportDate()));
			else
 */
			    ret.setReportUrl(new com.arahant.reports.EmployeeBenefitStatement().build(new BPerson(in.getPersonId()), in.isIncludeCredentials(), in.getReportDate()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SelfProvideBenefitConfigReturn selfProvideBenefitConfig(/*
			 * @WebParam(name = "in")
			 */final SelfProvideBenefitConfigInput in) {
		final SelfProvideBenefitConfigReturn ret = new SelfProvideBenefitConfigReturn();
		try {
			checkLogin(in);

			// load the specified benefit join
			BHRBenefitJoin benefitJoin = new BHRBenefitJoin(in.getPersonConfigId());

			// make sure we have the policy benefit join in case a dependent benefit join was passed (likely)
			if (benefitJoin.isDependentBenefitJoin())
				benefitJoin = new BHRBenefitJoin(benefitJoin.getPolicyBenefitJoin());

			benefitJoin.switchPolicyOwnerToEmployee(in.getPersonId());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public UnassignBenefitConfigReturn unassignBenefitConfig(/*
			 * @WebParam(name = "in")
			 */final UnassignBenefitConfigInput in) {
		final UnassignBenefitConfigReturn ret = new UnassignBenefitConfigReturn();
		try {
			checkLogin(in);

			// note that it is assumed the person config ids are policy benefit join ids, and not depenent benefit join ids
			BHRBenefitJoin.delete(in.getPersonConfigIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewAssignedBenefitConfigReturn newAssignedBenefitConfig(/*
			 * @WebParam(name = "in")
			 */final NewAssignedBenefitConfigInput in) {
		final NewAssignedBenefitConfigReturn ret = new NewAssignedBenefitConfigReturn();
		try {
			checkLogin(in);
			//System.out.println("IN NewAssignedBenefitConfigReturn");

			doAIValidations(in.getPersonId(), in.getStartDate(), in.getConfigId(), in.getAmountOverrideAnnual());


			// validate the elected coverages if they have not accepted warnings yet
			if (!in.isWarningsAccepted()) {
				CoverageValidator cv = new CoverageValidator();
				BHRBenefitConfig conf = new BHRBenefitConfig(in.getConfigId());
				BPerson bemp = new BPerson(in.getPersonId());
				cv.setBenefitCoversChildren(conf.getCoversChildren());
				cv.setBenefitCoversEmployee(conf.getCoversEmployee());
				cv.setBenefitCoversEmployeeSpouse(conf.getCoversEmployeeSpouse());
				cv.setBenefitCoversEmployeeSpouseOrChildren(conf.getCoversEmployeeSpouseOrChildren());
				cv.setBenefitCoversNonEmployeeSpouse(conf.getSpouseNonEmployee());
				cv.setBenefitCoversNonEmployeeSpouseOrChildren(conf.getSpouseNonEmpOrChildren());
				cv.setBenefitMaxDependents(conf.getMaxChildren());

				int foundActiveChildren;
				boolean activeEmpSpouse = false;
				if (bemp.isEmployee()) {
					BEmployee be = new BEmployee(bemp);
					foundActiveChildren = be.getActiveChildren(DateUtils.now());
					cv.setFoundActiveChildren(foundActiveChildren);

					activeEmpSpouse = be.getActiveEmployeeSpouse(in.getStartDate());
					cv.setFoundActiveEmployeeSpouse(activeEmpSpouse);
					cv.setFoundActiveOther(be.getActiveOther());
				} else {
					cv.setFoundActiveChildren(0);
					cv.setFoundActiveEmployeeSpouse(false);
					cv.setFoundActiveOther(0);
				}
				cv.setUseDatesInMessages(true);




				CoverageValidator.InputItem items[] = new CoverageValidator.InputItem[in.getItem().length];

				int requestedChildren = 0;
				boolean requestedEmployee = false;
				int requestedOther = 0;
				boolean requestedSpouse = false;

				for (int loop = 0; loop < in.getItem().length; loop++) {
					items[loop] = new CoverageValidator.InputItem();
					items[loop].startDate = in.getItem()[loop].getStartDate();
					items[loop].endDate = in.getItem()[loop].getEndDate();
					items[loop].active = true;

					if (in.getPersonId().equals(in.getItem()[loop].getPersonId())) {
						items[loop].relationship = 'E';
						requestedEmployee = true;
					} else {
						BHREmplDependent dep = new BHREmplDependent(in.getPersonId(), in.getItem()[loop].getPersonId());
						items[loop].relationship = dep.getRelationship();

						if (dep.getRelationship() == 'S')
							if (items[loop].endDate == 0 || items[loop].endDate >= DateUtils.now()) {
								items[loop].employeeSpouse = activeEmpSpouse;
								requestedSpouse = true;
							}
						if (dep.getRelationship() == 'C')
							if (items[loop].endDate == 0 || items[loop].endDate >= DateUtils.now())
								requestedChildren++;
						if (dep.getRelationship() == 'O')
							requestedOther++;
					}

				}

				cv.setRequestedChildren(requestedChildren);
				cv.setRequestedEmployee(requestedEmployee);
				cv.setRequestedOther(requestedOther);
				cv.setRequestedSpouse(requestedSpouse);

				cv.validate(items);

				// set cv warnings
				ret.addErrors(cv.getErrors());
				ret.addWarnings(cv.getWarnings());

				if (in.getStartDate() > 0 && in.getEndDate() != 0 && in.getStartDate() > in.getEndDate())
					ret.addWarnings(new String[]{"Final Date is < Start Date.  This should only be done to mark a benefit is an incorrect assignment.  Continue anyway?"});
			}

			// if policy start date is before another benefit start date, that is an error -
			// this is handled farther down-stream in the process

			BPerson bPerson = new BPerson(in.getPersonId());

			// check for an already provided benefit that is provided by someone else (dependent benefit join)
			BHRBenefitJoin bBenefitJoin = bPerson.getCurrentBenefitForNewBenefit(in.getConfigId());
			if (bBenefitJoin != null && bBenefitJoin.isDependentBenefitJoin())
				// yes, so we need to term coverage on that old benefit dependent benefit join
				//if a dependent is now an employee, then don't set the Coverage End Date
				//unless it's 0 or a future date
				if (bBenefitJoin.getCoverageEndDate() == 0 || bBenefitJoin.getCoverageEndDate() > in.getStartDate()) {
					bBenefitJoin.setCoverageEndDate(DateUtils.add(in.getStartDate(), -1));
					bBenefitJoin.update();
				} // NOTE THE ABOVE IS QUESTIONABLE - IF IT IS NOT DONE, THE OLD POLICY LOOKS LIKE THEY ARE STILL COVERED
			// AND THE OLD POLICY CONTINUES TO SHOW UP IN THIS PERSON'S DISPLAY AS PROVIDED BY SOMEONE ELSE

			// check for non-employee dependent with no sponsoring employee id provided
			if (!bPerson.isEmployee() && isEmpty(in.getSponsoringEmployeeId())) {
				// we need to locate one...

				BHREmplDependent[] relationships = BHREmplDependent.getDependentSponsors(in.getPersonId(), in.getConfigId(), in.getStartDate());

				// if none, this is an error to the user - no inactive relationships or inactive employees matching this policy start date
				if (relationships.length == 0)
					throw new ArahantWarning("Cannot assign a COBRA benefit to the Dependent unless the Dependent has an Inactive Date prior to the benefit Policy Start Date, or a Sponsoring Employee has an inactive Employee Status dated prior to the benefit Policy Start Date.");
				else if (relationships.length == 1)
					in.setSponsoringEmployeeId(relationships[0].getEmployeeId());
				else
					ret.setSponsoringEmployees(relationships);
			}

			bBenefitJoin = new BHRBenefitJoin();
			ret.setPersonConfigId(bBenefitJoin.create());
			in.setData(bBenefitJoin);

			//if this is a policy join, check to see if I auto create any other benefits
			if (bBenefitJoin.getPayingPersonId().equals(bBenefitJoin.getCoveredPersonId())) {
				AIProperty newConfig = new AIProperty("AutoAssociatedBenefit", bBenefitJoin.getBenefitConfig().getBenefitCategoryCategoryId());
				if (!isEmpty(newConfig.getValue())) {
					BHRBenefitJoin assocBj = new BHRBenefitJoin();
					assocBj.create();
					assocBj.setPayingPersonId(bBenefitJoin.getPayingPersonId());
					assocBj.setCoveredPersonId(bBenefitJoin.getPayingPersonId());
					assocBj.setCoverageStartDate(bBenefitJoin.getCoverageStartDate());
					assocBj.setCoverageEndDate(bBenefitJoin.getCoverageEndDate());
					assocBj.setPolicyStartDate(bBenefitJoin.getPolicyStartDate());
					assocBj.setPolicyEndDate(bBenefitJoin.getPolicyEndDate());
					assocBj.setBenefitConfigId(newConfig.getValue());
					assocBj.setDefaults();
					assocBj.copyReason(bBenefitJoin.getBean());
					assocBj.insert();

				}
			}

			// if this is an employee, check for errors about changes to the benefit due to how it compares to the status history
			if (bPerson.isEmployee())
				ret.addErrors(new BEmployee(bPerson).getErrorsForBenefitChange(bBenefitJoin));

			// rollback if we have issues that have not been accepted or we are requesting that the user select the sponsoring employee
			if ((!in.isWarningsAccepted() && ret.hasErrorsOrWarnings()) || ret.getSponsoringEmployees() != null) {
				ret.setPersonConfigId(""); // no person config id yet
				hsu.rollbackTransaction();
				hsu.beginTransaction();
			}

			finishService(ret);
		} catch (final Exception e) {
			ret.setPersonConfigId(""); // make sure any exceptions cause the person config id to be removed
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveDeclineBenefitReturn saveDeclineBenefit(/*
			 * @WebParam(name = "in")
			 */final SaveDeclineBenefitInput in) {
		final SaveDeclineBenefitReturn ret = new SaveDeclineBenefitReturn();
		try {
			checkLogin(in);

			final BHRBenefitJoin x = new BHRBenefitJoin(in.getPersonConfigId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewDeclineBenefitReturn newDeclineBenefit(/*
			 * @WebParam(name = "in")
			 */final NewDeclineBenefitInput in) {
		final NewDeclineBenefitReturn ret = new NewDeclineBenefitReturn();
		try {
			checkLogin(in);

			final BHRBenefitJoin x = new BHRBenefitJoin();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadCustomColorsReturn loadCustomColors(/*
			 * @WebParam(name = "in")
			 */final LoadCustomColorsInput in) {
		final LoadCustomColorsReturn ret = new LoadCustomColorsReturn();
		try {
			checkLogin(in);

			final BPerson current = new BPerson(hsu.getCurrentPerson());

			ret.setData(current);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveCustomColorsReturn saveCustomColors(/*
			 * @WebParam(name = "in")
			 */final SaveCustomColorsInput in) {
		final SaveCustomColorsReturn ret = new SaveCustomColorsReturn();
		try {
			checkLogin(in);

			final BPerson current = new BPerson(hsu.getCurrentPerson());
			in.setData(current);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetPayrollReportReturn getPayrollReport(/*
			 * @WebParam(name = "in")
			 */final GetPayrollReportInput in) {
		final GetPayrollReportReturn ret = new GetPayrollReportReturn();
		try {
			checkLogin(in);

			//TODO In the future when we genericise this, take advantage of in.getShowAsDependent() to change header to say Dependent instead of Employee

			setSubjectPerson(in.getPersonId());
/*
			if (AIProperty.getBoolean("IsWmCo"))
				ret.setReportUrl(new com.arahant.reports.williamsonCounty.WmCoPayrollNotification().build(new BPerson(in.getPersonId()), in.getReportDate()));
			else
				throw new ArahantWarning("Payroll notification not valid for this deployment.");
*/

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitChangeReasonsReturn listBenefitChangeReasons(/*
			 * @WebParam(name = "in")
			 */final ListBenefitChangeReasonsInput in) {
		final ListBenefitChangeReasonsReturn ret = new ListBenefitChangeReasonsReturn();
		try {
			checkLogin(in);

			ret.setItem(BHRBenefitChangeReason.listActives(in.getPersonConfigId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	public static void main(String args[]) {
		ArahantSession.getHSU().setCurrentPersonToArahant();
		//ArahantSession.getAI().watchAll();
		HrBenefitJoin bj = new HrBenefitJoin();
		bj.linkToEngine();
		bj.setAmountCovered(30000);
		ArahantSession.runAI();
		//ArahantSession.AIEval("(facts)");

	}

	@WebMethod()
	public CalculateAmountReturn calculateAmount(/*
			 * @WebParam(name = "in")
			 */final CalculateAmountInput in) {
		final CalculateAmountReturn ret = new CalculateAmountReturn();
		try {
			checkLogin(in);

			if (isEmpty(in.getPersonId()))
				ret.setAmount(0);
			else {
				int ppy = new BEmployee(in.getPersonId()).getPayPeriodsPerYear();

				if (in.getCalculateToPPP())
					ret.setAmount(in.getAmount() / ppy);
				else
					ret.setAmount(in.getAmount() * ppy);
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ApproveConfigReturn approveConfig(/*
			 * @WebParam(name = "in")
			 */final ApproveConfigInput in) {
		final ApproveConfigReturn ret = new ApproveConfigReturn();
		try {
			checkLogin(in);
			//if I have an approved benefit join with same policy start and same config, etc, delete it
			//If I don't delete it now, the system will try to delete them both once approved.
			String empId = "";
			for (String configId : in.getIds()) {
				BHRBenefitJoin ebj = new BHRBenefitJoin(configId);
				empId = ebj.getPayingPersonId();
				HrBenefitJoin oldbj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.PAYING_PERSON_ID, ebj.getPayingPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, ebj.getBean().getHrBenefitConfig()).eq(HrBenefitJoin.HRBENEFIT, ebj.getBean().getHrBenefit()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, ebj.getBean().getHrBenefitCategory()).eq(HrBenefitJoin.POLICY_START_DATE, ebj.getPolicyStartDate()).first();

				if (oldbj != null)
					new BHRBenefitJoin(oldbj).delete();

				ebj.setPolicyApproved(true);
				//System.out.println("Approved policy: " + ebj.getPayingPerson().getNameFML() + " -- " + ebj.getBenefitConfigName() + " -- " + ebj.getBenefitName() + " (" + ebj.getBenefitJoinId() + ")");

			}

			hsu.commitTransaction();
			hsu.beginTransaction();


			BEmployee emp = new BEmployee(empId);
			boolean hasUnapproved = hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON, emp.getRealAndChangePerson()).eq(HrBenefitJoin.APPROVED, 'N').exists();

			hsu.rollbackTransaction();
			hsu.beginTransaction();

			if (!hasUnapproved) {
				emp.setBenefitWizardStatus(Employee.BENEFIT_WIZARD_STATUS_PROCESSED + "");
				emp.update();
			}
			/*
			 * ebj.setBenefitApproved('Y'); //System.out.println("BC id " +
			 * ebj.getBean().getHrBenefitConfig().getBenefitConfigId());
			 * List<HrBenefitJoin> bj =
			 * hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID,ebj.getPayingPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID,
			 * ebj.getBean().getHrBenefitConfig().getBenefitConfigId()).joinTo(HrBenefitJoin.PROJECT).list();
			 * for (HrBenefitJoin bjoin : bj){ BHRBenefitJoin bjb = new
			 * BHRBenefitJoin(bjoin); bjb.setBenefitApproved('Y'); }
			 *
			 */
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetBenefitChangeProjectInfoReturn getBenefitChangeProjectInfo(/*
			 * @WebParam(name = "in")
			 */final GetBenefitChangeProjectInfoInput in) {
		final GetBenefitChangeProjectInfoReturn ret = new GetBenefitChangeProjectInfoReturn();
		try {
			checkLogin(in);

			if (new BHRBenefitJoin(in.getBenefitJoinId()).getBean().getProject() != null)
				ret.setProjectDescription(new BHRBenefitJoin(in.getBenefitJoinId()).getBean().getProject().getDetailDesc());
			else
				ret.setProjectDescription("No Details Available");

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public CalcVoluntaryCostsReturn calcVoluntaryCosts(CalcVoluntaryCostsInput in) {
		final CalcVoluntaryCostsReturn ret = new CalcVoluntaryCostsReturn();

		try {
			checkLogin(in);

			BHRBenefitConfig bbc = new BHRBenefitConfig(in.getBenefitConfigId());
			BEmployee emp = new BEmployee(in.getEmployeeId());
			BHRBenefit ben = new BHRBenefit(bbc.getBenefit());
			
			BBenefitConfigCost bcc = BenefitCostCalculator.findConfigCost(bbc, emp.getStatusId(), emp.getOrgGroups(), 0);
			BBenefitConfigCostAge bcca = BenefitCostCalculator.findBenefitConfigCostAge(bcc, emp, 0, bbc.getPolicyStartDate());
			int ppy = BEmployee.getPPY(emp.getPerson());

			double employeeAnnualCost = BenefitCostCalculator.calculateEmployeeAnnualCost(emp, ben, bcc, bcca, in.getBenefitAmount());
			ret.setCostAnnually(employeeAnnualCost);
			ret.setCostMonthly(Utils.round(employeeAnnualCost / 12.0, 2));
			ret.setCostPPP(Utils.round(employeeAnnualCost / ppy, 2));
			
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadUpdatedCostReturn loadUpdatedCost(/*
			 * @WebParam(name = "in")
			 */final LoadUpdatedCostInput in) {
		final LoadUpdatedCostReturn ret = new LoadUpdatedCostReturn();

		try {
			checkLogin(in);

			BHRBenefitConfig config = new BHRBenefitConfig(in.getConfigId());
			BHRBenefit bene = new BHRBenefit(config.getBenefitId());
			char benefitAmountModel = bene.getBenefitAmountModel();

			String costCalcType = bene.getCostCalcType() + "";
			int calcDate = 0;
			if (costCalcType.equals("C"))
				if (ArahantSession.getCalcDate() != 0)
					calcDate = ArahantSession.getCalcDate();
				else
					calcDate = DateUtils.now();
			else if (costCalcType.equals("P"))
				calcDate = in.getPolicyStartDate();

//			List<IPerson> deps = new ArrayList<IPerson>();
//			if (in.getItem() != null)
//				for (LoadUpdatedCostInputItem item : in.getItem())
//					deps.add(new BPerson(item.getPersonId()).getPerson());
			
			
			BPerson coveredPerson = new BPerson(in.getPersonId());
			BEmployee employee = coveredPerson.getBEmployee();			
			
			BHRBenefit ben = new BHRBenefit(config.getBenefit());
			BBenefitConfigCost bcc = BenefitCostCalculator.findConfigCost(config, employee.getStatusId(), employee.getOrgGroups(), 0);
			BBenefitConfigCostAge bcca = BenefitCostCalculator.findBenefitConfigCostAge(bcc, employee, 0, config.getPolicyStartDate());
			int ppy = BEmployee.getPPY(employee.getPerson());
			ret.setPpy(ppy);
			
			BHRBenefitJoin pbj = new BHRBenefitJoin(in.getPolicyBenefitJoinId());
			double coverageAmount = in.getAmount();
			
			if (benefitAmountModel == 'F'  ||  benefitAmountModel == 'S'  ||  benefitAmountModel == 'M')
				coverageAmount = BenefitCostCalculator.calculateCoverageAmount(employee, ben, bcc);
			else if (coverageAmount < 0.0)  //  initial load, coverageAmount not set yet, use orig value
				if (pbj.getBean() == null)
					coverageAmount = 0.0;
				else
					coverageAmount = pbj.getAmountCovered();

			double employeeAnnualCost = BenefitCostCalculator.calculateEmployeeAnnualCost(employee, ben, bcc, bcca, coverageAmount);
			ret.setEmployeeAnnualCost(employeeAnnualCost);
			ret.setEmployeeMonthlyCost(Utils.round(employeeAnnualCost / 12.0, 2));
			ret.setEmployeePPPCost(Utils.round(employeeAnnualCost / ppy, 2));
			
			double employerAnnualCost = BenefitCostCalculator.calculateEmployerAnnualCost(ben, bcc, bcca, coverageAmount);
			ret.setEmployerAnnualCost(employerAnnualCost);
			ret.setEmployerMonthlyCost(Utils.round(employerAnnualCost / 12.0, 2));
			ret.setEmployerPPPCost(Utils.round(employerAnnualCost / ppy, 2));
			
			ret.setBenefitAnnualAmount(coverageAmount);
			

//			ret.setCalculatedAnnualCost(BenefitCostCalculator.calculateCostNewMethodAnnual(calcDate,
//					config.getBean(),
//					new BPerson(in.getPersonId()).getPerson(),
//					in.getUsingCobra(),
//					in.getAmount(),
//					in.getPolicyStartDate(),
//					in.getPolicyEndDate(),
//					deps,
//					new BHRBenefitChangeReason(in.getBcrId()).getType(),
//					""));
//			ret.setCalculatedPPPCost(BenefitCostCalculator.calculateCostNewMethod(calcDate,
//					config.getBean(),
//					new BPerson(in.getPersonId()).getPerson(),
//					in.getUsingCobra(),
//					in.getAmount(),
//					in.getPolicyStartDate(),
//					in.getPolicyEndDate(),
//					deps,
//					new BHRBenefitChangeReason(in.getBcrId()).getType(),
//					""));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
