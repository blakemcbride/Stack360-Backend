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

package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.WizardReviewReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.*;
import java.util.*;
import java.util.Collections;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardWizardBenefitWizardOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class BenefitWizardOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(BenefitWizardOps.class);

	public BenefitWizardOps() {
	}

	@WebMethod()
	public GetCurrentBenefitStatementReturn getCurrentBenefitStatement(/*
			 * @WebParam(name = "in")
			 */final GetCurrentBenefitStatementInput in) {
		final GetCurrentBenefitStatementReturn ret = new GetCurrentBenefitStatementReturn();
		try {
			checkLogin(in);
/*
			if (!isEmpty(in.getWizardConfigurationId())) {
				BWizardConfiguration bwc = new BWizardConfiguration(in.getWizardConfigurationId());
				if (bwc.getBenefitReport() == 0)
					ret.setReportUrl(new com.arahant.reports.EmployeeBenefitStatement().build((isEmpty(in.getEmployeeId()) ? new BPerson(hsu.getCurrentPerson()) : new BPerson(in.getEmployeeId())), false, DateUtils.now()));
				else if (bwc.getBenefitReport() == 1)
					ret.setReportUrl(new BPerson(hsu.getCurrentPerson()).getWmCoBenefitStatusReport(false, DateUtils.now()));
			} else
				ret.setReportUrl(new com.arahant.reports.EmployeeBenefitStatement().build((isEmpty(in.getEmployeeId()) ? new BPerson(hsu.getCurrentPerson()) : new BPerson(in.getEmployeeId())), false, DateUtils.now()));
*/
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitConfigsReturn listBenefitConfigs(/*
			 * @WebParam(name = "in")
			 */final ListBenefitConfigsInput in) {
		final ListBenefitConfigsReturn ret = new ListBenefitConfigsReturn();
		try {
			checkLogin(in);

			String empId;

			if (!isEmpty(in.getEmployeeId()))
				empId = in.getEmployeeId();
			else
				empId = ArahantSession.getCurrentPerson().getPersonId();

			BEmployee bemp = new BEmployee(empId);

			int date;

			if (in.getAsOfDate() == 0)
				date = DateUtils.now();
			else
				date = in.getAsOfDate();

			if (BProperty.isWizard3()) {
				List<WizardConfigurationConfig> wizConfigs = hsu.createCriteria(WizardConfigurationConfig.class).joinTo(WizardConfigurationConfig.WIZARD_CONFIGURATION_BENEFIT).eq(WizardConfigurationBenefit.BENEFIT, new BHRBenefit(in.getId()).getBean()).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, new BWizardConfiguration(in.getWizardConfigurationId()).getBean()).list();

				ret.setItem(wizConfigs, empId, date);
			} else
				ret.setItem(bemp.getAllValidConfigs(in.getId()), empId, date);
			/*
			 * List<String> l = new BHRBenefit(in.getId()).getConfigIds();
			 *
			 * BHRBenefitConfig[] hrb = new BHRBenefitConfig[l.size()];
			 *
			 * int loop = 0; for (String s: l) { hrb[loop] = new
			 * BHRBenefitConfig(s);
			 *
			 * loop++; }
			 *
			 * ret.setItem(hrb, empId);
             *
			 */

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitConfigsSimpleReturn listBenefitConfigsSimple(/*
			 * @WebParam(name = "in")
			 */final ListBenefitConfigsSimpleInput in) {
		final ListBenefitConfigsSimpleReturn ret = new ListBenefitConfigsSimpleReturn();
		try {
			checkLogin(in);

			String empId;

			if (!isEmpty(in.getEmployeeId()))
				empId = in.getEmployeeId();
			else
				empId = ArahantSession.getCurrentPerson().getPersonId();

			BEmployee bemp = new BEmployee(empId);

			if (in.getAsOfDate() == 0)
				in.setAsOfDate(DateUtils.now());

			BHRBenefit bh = new BHRBenefit(in.getId());

			if ((bh.getEndDate() > 0) && (in.getAsOfDate() > bh.getEndDate()) && (bh.getReplacingBenefit() != null))
				in.setId(bh.getReplacingBenefit().getBenefitId());
			else if ((bh.getEndDate() > 0) && (in.getAsOfDate() > bh.getEndDate()) && (bh.getReplacingBenefit() == null))
				throw new ArahantWarning("You may no longer enroll in this benefit as of " + DateUtils.getDateFormatted(bh.getEndDate()));

			List<WizardConfigurationConfig> wizConfigs = hsu.createCriteria(WizardConfigurationConfig.class).joinTo(WizardConfigurationConfig.WIZARD_CONFIGURATION_BENEFIT).eq(WizardConfigurationBenefit.BENEFIT, bh.getBean()).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, new BWizardConfiguration(in.getWizardConfigurationId()).getBean()).list();
			//if there are no configs, they either weren't set up, or this is a dependent benefit unlocked... so just show them all
			if (wizConfigs.isEmpty())
				ret.setItem2(bh.listConfigs(new String[0], 50), empId, in.getAsOfDate());
			else {

				BHRBenefitConfig[] validConfigsArray;
				List<String> validConfigIds = new ArrayList<String>();
				//does this wizard show inapplicable benefits?
				if (new BWizardConfiguration(in.getWizardConfigurationId()).getShowInapplicable() == 'Y')
					//get all the configs
					validConfigsArray = bemp.getAllValidConfigs(in.getId()); //ret.setItem(bemp.getAllValidConfigs(in.getId()), empId, in.getAsOfDate());
				else
					//only get applicable configs
					validConfigsArray = new BHRBenefit(in.getId()).listValidConfigs(empId, "", in.getId()); //ret.setItem(new BHRBenefit(in.getId()).listValidConfigs(empId, "", in.getId()), empId, in.getAsOfDate());

				for (BHRBenefitConfig c : validConfigsArray)
					validConfigIds.add(c.getId());

				List<WizardConfigurationConfig> keepers = new ArrayList<WizardConfigurationConfig>();
				//only want to keep the configs that match
				for (WizardConfigurationConfig wc : wizConfigs)
					if (validConfigIds.contains(wc.getBenefitConfig().getBenefitConfigId()))
						keepers.add(wc);

				//if there is a benefit dependency met,
				//limit to single config if necessary
				List<BenefitDependency> bdl = BBenefitDependency.getBenefitDependenciesWhereDependent(bh);
				if (bdl.size() > 0) {
					List<BenefitDependency> needBenefits = new ArrayList<BenefitDependency>();
					HrBenefitJoin requiredEnrollment = null;
					boolean limit = false;
					for (BenefitDependency bd : bdl) {
						HrBenefitJoin j = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, bemp.getPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getRequiredBenefit()).first();
						if (j != null) {
							limit = bd.getRequired() == 'Y';
							requiredEnrollment = j;
						} else
							needBenefits.add(bd);
					}
					if (requiredEnrollment != null && limit) {
						HrBenefitConfig config = requiredEnrollment.getHrBenefitConfig();
						HrBenefitConfig match = null;
						for (HrBenefitConfig newConfig : bh.getConfigs())
							if (config.getEmployee() == newConfig.getEmployee()
									&& config.getChildren() == newConfig.getChildren()
									&& config.getSpouseEmployee() == newConfig.getSpouseEmployee()
									&& config.getSpouseNonEmployee() == newConfig.getSpouseNonEmployee()
									&& config.getSpouseEmpOrChildren() == newConfig.getSpouseEmpOrChildren()
									&& config.getSpouseNonEmpOrChildren() == newConfig.getSpouseNonEmpOrChildren()
									&& config.getMaxChildren() == newConfig.getMaxChildren()) {
								match = newConfig;
								break;
							}
						if (match != null) {
							List<WizardConfigurationConfig> removeAgain = new ArrayList<WizardConfigurationConfig>();
							for (WizardConfigurationConfig c : keepers)
								if (c.getBenefitConfig() != match)
									removeAgain.add(c);
							keepers.removeAll(removeAgain);
							ret.setAutoEnrollSingleConfig(true);
						}
					}
				}

				ItemComparator comparator = new ItemComparator();
				Collections.sort(keepers, comparator);
				ret.setItem(keepers, empId, in.getAsOfDate());
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	public class ItemComparator implements Comparator<WizardConfigurationConfig> {

		@Override
		public int compare(WizardConfigurationConfig item1, WizardConfigurationConfig item2) {

			double seqno1 = item1.getSeqNo();
			double seqno2 = item2.getSeqNo();

			if (seqno1 > seqno2)
				return +1;
			else if (seqno1 < seqno2)
				return -1;
			else
				return 0;
		}
	}

	@WebMethod()
	public ListDependentsAndEmployeeReturn listDependentsAndEmployee(/*
			 * @WebParam(name = "in")
			 */final ListDependentsAndEmployeeInput in) {
		final ListDependentsAndEmployeeReturn ret = new ListDependentsAndEmployeeReturn();
		try {
			checkLogin(in);

			BPerson bpp = new BPerson();
			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();
			//if there is a pending record, get it
			if (bpp.hasPending(empId))
				bpp.loadPending(empId);
			else
				//otherwise, get the Real person record
				bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());
			List<HrEmplDependentWizard> lpend = hsu.createCriteria(HrEmplDependentWizard.class).eq(HrEmplDependentWizard.EMPLOYEE_ID, empId).gtOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0).list();

			ret.setItem(lpend, bpp, new BEmployee(empId), in.getBenefitId(), in.getCategoryId());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListDependentsAndEmployeeSimpleReturn listDependentsAndEmployeeSimple(/*
			 * @WebParam(name = "in")
			 */final ListDependentsAndEmployeeSimpleInput in) {
		final ListDependentsAndEmployeeSimpleReturn ret = new ListDependentsAndEmployeeSimpleReturn();
		try {
			checkLogin(in);

			BPerson bpp = new BPerson();
			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();
			//if there is a pending record, get it
			if (bpp.hasPending(empId))
				bpp.loadPending(empId);
			else
				//otherwise, get the Real person record
				bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());
			List<HrEmplDependentWizard> lpend = hsu.createCriteria(HrEmplDependentWizard.class).eq(HrEmplDependentWizard.EMPLOYEE_ID, empId).gtOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0).list();

			ret.setItem(lpend, bpp, new BEmployee(empId), in.getBenefitId(), in.getCategoryId());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadDemographicsReturn loadDemographics(/*
			 * @WebParam(name = "in")
			 */final LoadDemographicsInput in) {
		final LoadDemographicsReturn ret = new LoadDemographicsReturn();
		try {
			checkLogin(in);

			BPerson bpp = new BPerson();
			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();

			//if there is a pending record, get it
			if (bpp.hasPending(empId)) {
				bpp.loadPending(empId);
				ret.setData(bpp);
			} else {
				//otherwise, get the Real person record
				bpp.loadRealPerson(empId);
				//bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());
				ret.setData(new BPerson(empId));
			}
			if (!isEmpty(in.getWizardConfigurationId())) {
				BWizardConfiguration bwc = new BWizardConfiguration(in.getWizardConfigurationId());
				ret.setInstructions(bwc.getDemographicInstructions());
				ret.setAllowDemographicChanges(bwc.getAllowDemographicChanges() == 'Y');
			} else
				ret.setAllowDemographicChanges(true);
			if (isEmpty(ret.getInstructions()))
				if (ret.isAllowDemographicChanges())
					ret.setInstructions("Take a moment to review your personal information.  Make any necessary changes before continuing.");
				else
					ret.setInstructions("Take a moment to review your personal information.  Please contact your HR department if changes are needed.");	

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	private boolean hasChanges(SaveDemographicsInput in, BPerson bpp) {
		if (in == null)
			return false;

		if (!bpp.getCityPending().equalsIgnoreCase(in.getCity()))
			return true;

		if (bpp.getDob() != in.getDob())
			return true;

		if ((isEmpty(bpp.getPersonalEmail()) && !isEmpty(in.getEmail())) || (bpp.getPersonalEmail() != null && !bpp.getPersonalEmail().equalsIgnoreCase(in.getEmail())))
			return true;

		if (!bpp.getFirstName().equalsIgnoreCase(in.getFirstName()))
			return true;

		if (!bpp.getLastName().equalsIgnoreCase(in.getLastName()))
			return true;

		if (bpp.getHomePhonePending() == null || in.getHomePhone().equalsIgnoreCase("")) {
			//continue
		} else if (!bpp.getHomePhonePending().equalsIgnoreCase(in.getHomePhone()))
			return true;

		if (!bpp.getMiddleName().equalsIgnoreCase(in.getMiddleName()))
			return true;

		if (!bpp.getSex().equalsIgnoreCase(in.getSex()))
			return true;

		if (!bpp.getSsn().equalsIgnoreCase(in.getSsn()))
			return true;
		if (!bpp.getStatePending().equalsIgnoreCase(in.getState()))
			return true;

		if (!bpp.getStreetPending().equalsIgnoreCase(in.getStreet1()))
			return true;

		if (!bpp.getStreet2Pending().equalsIgnoreCase(in.getStreet2()))
			return true;
		if (!bpp.getWorkPhoneNumberPending().equalsIgnoreCase(in.getWorkPhone()))
			return true;

		if (!bpp.getZipPending().equalsIgnoreCase(in.getZip()))
			return true;
		if (!bpp.getMobilePhonePending().equalsIgnoreCase(in.getMobilePhone()))
			return true;

		return false;
	}

	@WebMethod()
	public SaveDemographicsReturn saveDemographics(/*
			 * @WebParam(name = "in")
			 */final SaveDemographicsInput in) {
		final SaveDemographicsReturn ret = new SaveDemographicsReturn();
		try {
			checkLogin(in);

			BPerson bpp = new BPerson();
			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();
			//if there is a pending record, get it
			if (bpp.hasPending(empId))
				bpp.loadPending(empId);
			else
				//otherwise, get the Real person record
				bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());

			//now let's check if we have changes to be saved
			if (hasChanges(in, bpp)) {
				bpp.loadPending(empId);
				in.setData(bpp);
				bpp.update();
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteDependentsReturn deleteDependents(/*
			 * @WebParam(name = "in")
			 */final DeleteDependentsInput in) {
		final DeleteDependentsReturn ret = new DeleteDependentsReturn();
		try {
			checkLogin(in);

			for (String id : in.getIds()) {

				HrEmplDependent dep = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.RELATIONSHIP_ID, id).first();

				if (dep != null) {
					dep.setDateInactive(in.getEffectiveDate());
					hsu.saveOrUpdate(dep);
				} else
					BHREmplDependent.terminate(new String[]{id}, in.getEffectiveDate());
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetInstructionsForDependentReturn getInstructionsForDependent(/*
			 * @WebParam(name = "in")
			 */final GetInstructionsForDependentInput in) {
		final GetInstructionsForDependentReturn ret = new GetInstructionsForDependentReturn();
		try {
			checkLogin(in);

			in.getDob();
			in.getHandicap();
			in.getRelationshipType();
			in.getSsn();
			in.getStudent();
			in.getType();

			//TODO: Move to wizard

			ret.setMessageHtml("");

			//how old is 19
			Calendar cal = DateUtils.getNow();
			cal.add(Calendar.YEAR, -19);

			Calendar dob = DateUtils.getCalendar(in.getDob());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListDependentsReturn listDependents(/*
			 * @WebParam(name = "in")
			 */final ListDependentsInput in) {
		final ListDependentsReturn ret = new ListDependentsReturn();
		try {
			checkLogin(in);

			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();

			List<HrEmplDependentWizard> dw = hsu.createCriteria(HrEmplDependentWizard.class).eq(HrEmplDependentWizard.EMPLOYEE_ID, empId).list();
			ret.setItem(dw);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadSpouseSexReturn loadSpouseSex(/*
			 * @WebParam(name = "in")
			 */final LoadSpouseSexInput in) {
		final LoadSpouseSexReturn ret = new LoadSpouseSexReturn();
		try {
			checkLogin(in);

			ret.setData(new BEmployee(isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewDependentReturn newDependent(/*
			 * @WebParam(name = "in")
			 */final NewDependentInput in) {
		final NewDependentReturn ret = new NewDependentReturn();
		try {
			checkLogin(in);

			BPerson bpp = new BPerson();
			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();

			//since we are adding a dependent, we need to create a pending record for the Employee
			bpp.loadPending(empId);

			HrEmplDependent deppend = bpp.addDepedendent(null, in.getRelationshipType().charAt(0), DateUtils.now(), 0, "", empId); //TODO:where is other type?

			BHREmplDependent x = new BHREmplDependent(deppend);
			ret.setId(x.getDependentId());
			in.setData(x, empId);
			x.update(true);

			finishService(ret);
		} catch (final Exception e) {
			ret.setId("");
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveDependentReturn saveDependent(/*
			 * @WebParam(name = "in")
			 */final SaveDependentInput in) {
		final SaveDependentReturn ret = new SaveDependentReturn();
		try {
			checkLogin(in);

			HrEmplDependent dep = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.RELATIONSHIP_ID, in.getId()).first();

			if (dep != null) {
				BHREmplDependent d = new BHREmplDependent(dep);
				in.setData(d);
				d.update(true);
			} else {
				final BHREmplDependent x = new BHREmplDependent(in.getId());
				in.setData(x);
				x.update(true);
			}
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetMetaReturn getMeta(/*
			 * @WebParam(name = "in")
			 */final GetMetaInput in) {
		final GetMetaReturn ret = new GetMetaReturn();
		try {
			checkLogin(in);
			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();

			if (BProperty.isWizard3())
				if (!isEmpty(in.getWizardConfigurationId()))
					ret.setData(new BCompany(hsu.getCurrentCompany()), new BWizardConfiguration(in.getWizardConfigurationId()), empId);
				else
					throw new ArahantException("Wizard Configuration ID not specified for this screen.");
			else
				ret.setData(new BCompany(hsu.getCurrentCompany()), empId);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadPaymentInfoReturn loadPaymentInfo(/*
			 * @WebParam(name = "in")
			 */final LoadPaymentInfoInput in) {
		final LoadPaymentInfoReturn ret = new LoadPaymentInfoReturn();
		try {
			checkLogin(in);

			BPaymentInfo bpi;
			String paymentInfoId = new BPerson(in.getPersonId()).getPaymentInfoId();
			if (!isEmpty(paymentInfoId)) {
				bpi = new BPaymentInfo(paymentInfoId);
				ret.setData(bpi);
			} else
				ret.setData(in.getPersonId());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetQualifyingEventReturn getQualifyingEvent(/*
			 * @WebParam(name = "in")
			 */final GetQualifyingEventInput in) {
		final GetQualifyingEventReturn ret = new GetQualifyingEventReturn();
		try {
			checkLogin(in);

			//TODO: move the create pending to the save life event method when it's ready
			// BPerson pend = new BPerson();
			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();


			//pend.loadPending(empId);
			//pend.createPendingJoins();

			//get the BEmployee to determine hired date and whether the employee is a new hired
			BEmployee emp = new BEmployee(empId);
			if (emp.getIsNewHire()) {
				ret.setNewHireEffectiveDate(emp.getCurrentHiredDate());
				ret.setNewHireId(new BHRBenefitChangeReason().getNewHireIdOrCreate());
			}
			ret.setItem(BHRBenefitChangeReason.listQualifyingReasons());

			ret.setOpenEnrollmentId(BHRBenefitChangeReason.getActiveOpenEnrollmentId());
			ret.setOpenEnrollmentEffectiveDate(BHRBenefitChangeReason.getActiveOpenEffectiveDate());
			
			BWizardConfiguration wc = emp.getWizardConfiguration("E");
			ret.setShowQualifyingEvent(wc.getShowQualifyingEvent() == 'Y');
			
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetBenefitMetaReturn getBenefitMeta(/*
			 * @WebParam(name = "in")
			 */final GetBenefitMetaInput in) {
		final GetBenefitMetaReturn ret = new GetBenefitMetaReturn();
		try {
			checkLogin(in);
			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();

			if (BProperty.isWizard3()) {
				BHRBenefit bb = new BHRBenefit(in.getBenefitId());
				WizardConfigurationBenefit wcb = hsu.createCriteria(WizardConfigurationBenefit.class).eq(WizardConfigurationBenefit.BENEFIT, bb.getBean()).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, new BWizardConfiguration(in.getWizardConfigurationId()).getBean()).first();
				//this may not be in the wizard if it's a dependent benefit that gets auto loaded, so need to populate its data from the benefit
				if (wcb != null)
					ret.setData(new BWizardConfigurationBenefit(wcb), empId);
				else
					ret.setData2(bb, empId);

			} else
				ret.setData(new BHRBenefit(in.getBenefitId()), empId);
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveConfigAndEnrolleesReturn saveConfigAndEnrollees(/*
			 * @WebParam(name = "in")
			 */final SaveConfigAndEnrolleesInput in) {
		final SaveConfigAndEnrolleesReturn ret = new SaveConfigAndEnrolleesReturn();
		try {
			checkLogin(in);

			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();
			int qualifyingEventDate = in.getQualifyingEventDate();
			if (qualifyingEventDate < DateUtils.now())
				qualifyingEventDate = DateUtils.now();  // can't start a plan befor today

			if (BProperty.isWizard3())
				//are we actually doing an enrollment or just checking if we would allow the enrollment
				if (!in.getDoEnrollment()) {
					if (!isEmpty(in.getConfigId()) && in.getEnrolleeIds().length != 0) {
						List<BHRBenefitJoin> bjs = new ArrayList<BHRBenefitJoin>();
						for (String s : in.getEnrolleeIds()) {
							BHRBenefitJoin bj = new BHRBenefitJoin();
							bj.create();
							bj.setBenefitConfigId(in.getConfigId());
							bj.setPayingPersonId(empId);
							bj.setCoveredPersonId(s);
							if (!empId.equals(s))
								try {
									HrEmplDependent dep = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE_ID, empId).eq(HrEmplDependent.PERSON, hsu.get(Person.class, s)).first();
									if (dep != null)
										bj.setRelationship(dep);
								} catch (ArahantException arahantException) {
									//just continue
								}
							bjs.add(bj);


						}
						if (BHRBenefitJoin.checkPotentialOverCoverage(bjs))
							throw new ArahantWarning("You have selected a coverage level that is excessive given the dependents you are enrolling.");
					}
				} else {

					BPerson bpp = new BPerson();
					BEmployee bemp = new BEmployee(empId);
					bemp.setBenefitWizardStatus(Employee.BENEFIT_WIZARD_STATUS_UNFINALIZED + "");
					BWizardConfiguration bz = bemp.getWizardConfiguration("E");
					bemp.update();
					//if there is a pending record, get it
					if (bpp.hasPending(empId))
						bpp.loadPending(empId);
					else
						//otherwise, get the Real person record
						bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());
hsu.flush();
					if (isEmpty(in.getConfigId())) {
						if (!isEmpty(in.getBenefitId()))
							ret.setBenefitJoinId(bpp.noEnrollments(in.getBenefitId(), in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), bz.getAutoApproveDeclines()));
						else
							ret.setBenefitJoinId(bpp.noEnrollments(null, in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), bz.getAutoApproveDeclines(), in.getCategoryId()));
hsu.flush();
					} else {
						if (in.getCoverageAmount() == 0)
							ret.setBenefitJoinId(bpp.enrollInConfig(in.getConfigId(), in.getChangeReasonId(), qualifyingEventDate, in.getEnrolleeIds(), 0, true, in.getExplanation()));
						else
							ret.setBenefitJoinId(bpp.enrollInConfig(in.getConfigId(), in.getChangeReasonId(), qualifyingEventDate, in.getEnrolleeIds(), in.getCoverageAmount(), true, in.getExplanation()));
hsu.flush();

						BHRBenefitConfig bc = new BHRBenefitConfig(in.getConfigId());
						BHRBenefit bb = new BHRBenefit(bc.getBenefit());
						ret.setRequiredDocument(bb.getRequiredDocuments(), ret.getBenefitJoinId());

						@SuppressWarnings("unchecked")
						List<String> excludeRiders = (List) hsu.createCriteria(com.arahant.beans.BenefitRider.class).selectFields(com.arahant.beans.BenefitRider.BENEFIT_RIDER_ID).eq(com.arahant.beans.BenefitRider.HIDDEN, 'Y').eq(com.arahant.beans.BenefitRider.REQUIRED, 'N').list();
						List<com.arahant.beans.BenefitRider> includeRiders = new ArrayList<com.arahant.beans.BenefitRider>();
						HibernateCriteriaUtil<com.arahant.beans.BenefitRider> hcu = hsu.createCriteria(com.arahant.beans.BenefitRider.class).notIn(com.arahant.beans.BenefitRider.BENEFIT_RIDER_ID, excludeRiders).eq(com.arahant.beans.BenefitRider.BASE_BENEFIT_ID, bb.getBenefitId());
						for (com.arahant.beans.BenefitRider br : hcu.list()) {
							//check if they are age eligible
							short min = br.getRiderBenefit().getMinAge();
							short max = br.getRiderBenefit().getMaxAge();
							short age = bpp.getAgeAsOf(qualifyingEventDate);
							if (max > 0 && age > max)
								continue;
							if (min > 0 && age < min)
								continue;
							includeRiders.add(br);
						}
						ret.setBenefitRiders(includeRiders, new BHRBenefitJoin(ret.getBenefitJoinId()), qualifyingEventDate);
					}

					//Check if any other benefits in the category require decline, if they do create a decline benefit join record
					if (!isEmpty(in.getCategoryId()))
						if (!new BHRBenefitCategory(in.getCategoryId()).getAllowsMultipleBenefits()) {
							List<HrBenefit> otherBenefits = hsu.createCriteria(HrBenefit.class).ne(HrBenefit.BENEFITID, in.getBenefitId()).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, in.getCategoryId()).list();
							for (HrBenefit otherBen : otherBenefits) {
								BHRBenefit b = new BHRBenefit(otherBen);
								if (b.getRequiresDecline())
									bpp.noEnrollments(b.getBenefitId(), in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), bz.getAutoApproveDeclines());
							}
						}
				}
			else
				//are we actually doing an enrollment or just checking if we would allow the enrollment
				if (!in.getDoEnrollment()) {
					if (!isEmpty(in.getConfigId()) && in.getEnrolleeIds().length != 0) {
						List<BHRBenefitJoin> bjs = new ArrayList<BHRBenefitJoin>();
						for (String s : in.getEnrolleeIds()) {
							BHRBenefitJoin bj = new BHRBenefitJoin();
							bj.create();
							bj.setBenefitConfigId(in.getConfigId());
							bj.setPayingPersonId(empId);
							bj.setCoveredPersonId(s);
							bjs.add(bj);
						}
						if (BHRBenefitJoin.checkPotentialOverCoverage(bjs))
							throw new ArahantWarning("You have selected a coverage level that is excessive given the dependents you are enrolling.");
					}
				} else {

					BPerson bpp = new BPerson();
					BEmployee bemp = new BEmployee(empId);
					bemp.setBenefitWizardStatus(Employee.BENEFIT_WIZARD_STATUS_UNFINALIZED + "");
					bemp.update();
					//if there is a pending record, get it
					if (bpp.hasPending(empId))
						bpp.loadPending(empId);
					else
						//otherwise, get the Real person record
						bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());
					if (isEmpty(in.getConfigId())) {
						if (!isEmpty(in.getBenefitId()))
							ret.setBenefitJoinId(bpp.noEnrollments(in.getBenefitId(), in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), false));
					} else {
						if (in.getCoverageAmount() == 0)
							ret.setBenefitJoinId(bpp.enrollInConfig(in.getConfigId(), in.getChangeReasonId(), qualifyingEventDate, in.getEnrolleeIds(), 0, true, in.getExplanation()));
						else
							ret.setBenefitJoinId(bpp.enrollInConfig(in.getConfigId(), in.getChangeReasonId(), qualifyingEventDate, in.getEnrolleeIds(), in.getCoverageAmount(), true, in.getExplanation()));
						BHRBenefitConfig bc = new BHRBenefitConfig(in.getConfigId());
						BHRBenefit bb = new BHRBenefit(bc.getBenefit());
						ret.setRequiredDocument(bb.getRequiredDocuments(), ret.getBenefitJoinId());
					}

					//Check if any other benefits in the category require decline, if they do create a decline benefit join record
					if (!isEmpty(in.getCategoryId()))
						if (!new BHRBenefitCategory(in.getCategoryId()).getAllowsMultipleBenefits()) {
							List<HrBenefit> otherBenefits = hsu.createCriteria(HrBenefit.class).ne(HrBenefit.BENEFITID, in.getBenefitId()).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, in.getCategoryId()).list();
							for (HrBenefit otherBen : otherBenefits) {
								BHRBenefit b = new BHRBenefit(otherBen);
								if (b.getRequiresDecline())
									bpp.noEnrollments(b.getBenefitId(), in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), false);
							}
						}
				}

			if (!isEmpty(ret.getBenefitJoinId())  &&  !isEmpty(in.getBenefitId())) {
				BHRBenefitJoin bbj = new BHRBenefitJoin(ret.getBenefitJoinId());
				List<String> l = bbj.autoEnrollRidersAndDependencies(empId);
				String[] sa = new String[l.size()];
				int count = 0;
				for (String s : l) {
					sa[count] = s;
					count++;
				}
				ret.setBenefitJoinIds(sa);
			}

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
			int qualifyingEventDate = in.getQualifyingEventDate();
			if (qualifyingEventDate < DateUtils.now())
				qualifyingEventDate = DateUtils.now();  // can't start a plan befor today

			String empId = !in.getEmployeeId().isEmpty() ? in.getEmployeeId() : "";

			if (!isEmpty(in.getWizardConfigurationId())) {
				BWizardConfiguration bwc = new BWizardConfiguration(in.getWizardConfigurationId());
				if (bwc.getReviewReport() == 0)
					ret.setReportUrl(new WizardReviewReport().build(empId));
				/*
				else if (bwc.getReviewReport() == 1)
					ret.setReportUrl(new BPerson(hsu.getCurrentPerson()).getWmCoBenefitStatusReportNewWizard(false, qualifyingEventDate));
				 */
			} else
				ret.setReportUrl(new WizardReviewReport().build(empId));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveQualifyingEventReturn saveQualifyingEvent(/*
			 * @WebParam(name = "in")
			 */final SaveQualifyingEventInput in) {
		final SaveQualifyingEventReturn ret = new SaveQualifyingEventReturn();
		try {
			checkLogin(in);

			if (isEmpty(in.getEmployeeId()))
				in.setEmployeeId(hsu.getCurrentPerson().getPersonId());

			List<BenefitChangeReasonDoc> reqDocs = hsu.createCriteria(BenefitChangeReasonDoc.class).eq(BenefitChangeReasonDoc.BENEFIT_CHANGE_REASON_ID, in.getEventId()).list();

			BLifeEvent ble = new BLifeEvent();
			ble.create();
			in.setData(ble);
			ble.insert();

			ret.setDocs(reqDocs, new BHRBenefitChangeReason(in.getEventId()).getBean(), in.getEventDate(), new BEmployee(in.getEmployeeId()).getHireDate());

			if (BProperty.getBoolean("WizardDemoMode")) {

				List<HrBenefitJoin> bjs = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, in.getEmployeeId()).list();
				hsu.createCriteria(WizardProject.class).in(WizardProject.BENEFIT_JOIN, bjs).delete();
				hsu.createCriteria(HrBeneficiary.class).in(HrBeneficiary.EMPL_BENEFIT_JOIN, bjs).delete();
				hsu.flush();
				hsu.delete(bjs);

				//hsu.doSQL("update person set replace_employer_plan = ' ', not_missed_five_days = ' ', drug_alcohol_abuse = ' ', two_family_heart_cond = ' ', two_family_cancer = ' ', two_family_diabetes = ' ',  has_other_medical = ' ', 	actively_at_work = ' ', unable_to_perform = ' ', has_aids = ' ', has_cancer = ' ', has_heart_condition = ' ', smoker = 'U';");

				BEmployee be = new BEmployee(in.getEmployeeId());
				be.setReplaceEmployerPlan(' ');
				be.setNotMissedFiveDays(' ');
				be.setDrugAlcoholAbuse(' ');
				be.setTwoFamilyCancer(' ');
				be.setTwoFamilyDiabetes(' ');
				be.setTwoFamilyHeartCond(' ');
				be.setHasOtherMedical(' ');
				be.setActivelyAtWork(' ');
				be.setUnableToPerform(' ');
				be.setHasAids(' ');
				be.setHasCancer(' ');
				be.setHasHeartCondition(' ');
				be.getPerson().setSmoker('U');
				be.update();

				for (HrEmplDependent dep : hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, be.getEmployee()).list()) {
					BPerson bdep = new BPerson(dep.getDependentId());
					bdep.setReplaceEmployerPlan(' ');
					bdep.setNotMissedFiveDays(' ');
					bdep.setDrugAlcoholAbuse(' ');
					bdep.setTwoFamilyCancer(' ');
					bdep.setTwoFamilyDiabetes(' ');
					bdep.setTwoFamilyHeartCond(' ');
					bdep.setHasOtherMedical(' ');
					bdep.setActivelyAtWork(' ');
					bdep.setUnableToPerform(' ');
					bdep.setHasAids(' ');
					bdep.setHasCancer(' ');
					bdep.setHasHeartCondition(' ');
					bdep.getPerson().setSmoker('U');
					bdep.update();
				}

				BWizardConfiguration bwc = be.getWizardConfiguration("E");
				Project pp = hsu.createCriteria(Project.class).eq(Project.PROJECTSTATUS, bwc.getProjectStatus()).eq(Project.PROJECTCATEGORY, bwc.getProjectCategory()).eq(Project.PROJECTTYPE, bwc.getProjectType()).eq(Project.DONE_FOR_PERSON, be.getPerson()).first();
				if (pp != null)
					hsu.createCriteria(WizardProject.class).eq(WizardProject.PROJECT, pp).delete();
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	@SuppressWarnings({"unchecked", "unchecked"})
	public GetCostsReturn getCosts(/*
			 * @WebParam(name = "in")
			 */final GetCostsInput in) {
		final GetCostsReturn ret = new GetCostsReturn();
		try {
			checkLogin(in);

			int date;
			BPerson bpp = new BPerson();

			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();
			BEmployee employee = new BEmployee(empId);

			//if there is a pending record, get it
			if (bpp.hasPending(empId))
				bpp.loadPending(empId);
			else
				//otherwise, get the Real person record
				bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());

			if (in.getAsOfDate() != 0)
				date = in.getAsOfDate();
			else
				date = DateUtils.now();

			//Costs c=bpp.getPendingBenefitTotalCost(in.getBenefitId(), date, in.getCategoryId());
			//type 0=ppp, 1=monthly, 2=yearly

			String benefitId = in.getBenefitId();
			String categoryId = in.getCategoryId();
			List possibleIds = new ArrayList();
			possibleIds.add(bpp.getPersonId());
			PersonCR pcr = hsu.createCriteria(PersonCR.class).eq(PersonCR.PERSON_PENDING, bpp.getPerson()).eq(PersonCR.CHANGE_STATUS, PersonCR.STATUS_PENDING).first();

			if (pcr == null) {
				pcr = hsu.createCriteria(PersonCR.class).eq(PersonCR.PERSON, bpp.getPerson()).eq(PersonCR.CHANGE_STATUS, PersonCR.STATUS_PENDING).first();
				//throw new ArahantException("Could not find change request with id " + bean.getPersonId());
				if (pcr == null)
					return null;
			}
			if (pcr.getRealRecord() == null)
				throw new ArahantException("This functionality requires an approved employee.");
			else
				possibleIds.add(pcr.getRealRecord().getPersonId());

			HashSet<HrBenefit> doneBenefits = new HashSet<HrBenefit>();
			HashSet<HrBenefitCategory> doneCategories = new HashSet<HrBenefitCategory>();

			//make sure to skip any declines
			doneBenefits.addAll(hsu.createCriteria(HrBenefit.class).joinTo(HrBenefit.HREMPLOYEEBENEFITJOINS).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).list());

			doneCategories.addAll(hsu.createCriteria(HrBenefitCategory.class).joinTo(HrBenefitCategory.HREMPLOYEEBENEFITJOINS).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).list());

			//First check unapproved benefits
			HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).orderBy(HrBenefitJoin.APPROVED) //N will come before Y, so unapproved will go first and block approved - saving queries
					.in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds);
			if (!isEmpty(benefitId)) {

				List<String> notInBenefits = new ArrayList<String>();
				notInBenefits.add(benefitId);
				if (BProperty.isWizard3()) {
					for (BenefitDependency bd : BBenefitDependency.getBenefitDependenciesWhereRequired(new BHRBenefit(in.getBenefitId()))) {
						BBenefitDependency bbd = new BBenefitDependency(bd);
						if (bbd.getRequiredBoolean() && bbd.getHiddenBoolean()) //if it's required and hidden, it will be auto enrolled when enrolling in base benefit
							notInBenefits.add(bd.getDependentBenefitId());
					}
					for (com.arahant.beans.BenefitRider br : BBenefitRider.getRidersForBaseBenefit(new BHRBenefit(in.getBenefitId())))
						//all rider benefit costs are handled on the screen
						notInBenefits.add(br.getRiderBenefitId());
				}
				hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).notIn(HrBenefit.BENEFITID, notInBenefits);
			}
			if (!isEmpty(categoryId)) {
				List<String> notInBenefits = new ArrayList<String>();
				if (BProperty.isWizard3()) {
					BHRBenefitCategory bbc = new BHRBenefitCategory(categoryId);
					//check if there are any dependent benefits that need to be auto enrolled that aren't in the wizard (the wizard takes care of auto enrolls too)
					List<HrBenefit> benefitsInCategory = bbc.getBenefits();
					for (HrBenefit bic : benefitsInCategory) {
						BHRBenefit bhrb = new BHRBenefit(bic);
						for (BenefitDependency bd : BBenefitDependency.getBenefitDependenciesWhereRequired(bhrb)) {
							BBenefitDependency bbd = new BBenefitDependency(bd);
							if (bbd.getRequiredBoolean() && bbd.getHiddenBoolean()) //if it's required and hidden, it will be auto enrolled when enrolling in base benefit
								notInBenefits.add(bd.getDependentBenefitId());
						}
						for (com.arahant.beans.BenefitRider br : BBenefitRider.getRidersForBaseBenefit(bhrb))
							//all rider benefit costs are handled on the screen
							notInBenefits.add(br.getRiderBenefitId());
					}
				}
				if (notInBenefits.size() > 0)
					hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).notIn(HrBenefitConfig.HR_BENEFIT_ID, notInBenefits).joinTo(HrBenefitConfig.HR_BENEFIT).ne(HrBenefit.BENEFIT_CATEGORY, new BHRBenefitCategory(categoryId).getBean());
				else
					hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).ne(HrBenefit.BENEFIT_CATEGORY, new BHRBenefitCategory(categoryId).getBean());
			}

			int today = DateUtils.today();
			for (HrBenefitJoin bj : hcu.list()) {
				BHRBenefitJoin bbj = new BHRBenefitJoin(bj);
				if (bj.getHrBenefitConfig() != null && bbj.isPolicyBenefitJoin()) {
					BHRBenefitConfig bbc = new BHRBenefitConfig(bj.getHrBenefitConfig());
//					List<IPerson> covered = bbc.estimateCoveredPeople(employee);
					
					double tempAnnual;
					double tempMonthly;
					double tempPpy;
					if (bbj.getEmployeeCovered().equals("Y")) {
						tempAnnual = BenefitCostCalculator.calculateEmployeeAnnualCost(bbc, employee, 0, today, bbj.getAmountCovered());
						int ppy = BEmployee.getPPY(bbj.getPayingPersonId());
						tempPpy = tempAnnual / ppy;
						tempMonthly = tempAnnual / 12;
						ret.setAnnualTotalCost(ret.getAnnualTotalCost() + tempAnnual);
						ret.setMonthlyTotalCost(ret.getMonthlyTotalCost() + tempMonthly);
						ret.setPppTotalCost(ret.getPppTotalCost() + tempPpy);
						//					}
					} else {
						
						BHRBenefit ben = new BHRBenefit(bbc.getBenefit());
						BBenefitConfigCost bcc = BenefitCostCalculator.findConfigCost(bbc, employee.getStatusId(), employee.getOrgGroups(), date);
						BBenefitConfigCostAge bcca = BenefitCostCalculator.findBenefitConfigCostAge(bcc, employee, 0, today);

						for (HrBenefitJoin currentBj : bbj.getDependentBenefitJoins()) {
							tempAnnual = BenefitCostCalculator.calculateEmployeeAnnualCost(employee, ben, bcc, bcca, currentBj.getAmountCovered());
							int ppy = BEmployee.getPPY(currentBj.getPayingPersonId());
							tempPpy = Utils.round(tempAnnual / ppy, 2);
							tempMonthly = Utils.round(tempAnnual / 12, 2);
							ret.setAnnualTotalCost(ret.getAnnualTotalCost() + tempAnnual);
							ret.setMonthlyTotalCost(ret.getMonthlyTotalCost() + tempMonthly);
							ret.setPppTotalCost(ret.getPppTotalCost() + tempPpy);
						}
					}
				}
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitElectionsReturn listBenefitElections(/*
			 * @WebParam(name = "in")
			 */final ListBenefitElectionsInput in) {
		final ListBenefitElectionsReturn ret = new ListBenefitElectionsReturn();
		try {
			checkLogin(in);

			BPerson bpp = new BPerson();

			int date;

			String empId = isEmpty(in.getEmployeeId()) ? ArahantSession.getCurrentPerson().getPersonId() : in.getEmployeeId();
			//if there is a pending record, get it
			if (bpp.hasPending(empId))
				bpp.loadPending(empId);
			else
				//otherwise, get the Real person record
				bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());
			BEmployee emp = new BEmployee(empId);
			ret.setBenefitWizardStatus(emp.getBenefitWizardStatus());
			if (BProperty.isWizard3()) {
				BWizardConfiguration wc = emp.getWizardConfiguration("E");
				ret.setLockAfterFinalize(wc.getLockOnFinalizeBool());
				//if this is a new hire 31 days after hire date, they should always be locked out for wmco
				if (emp.getIsNewHire() && AIProperty.getBoolean("IsWmCo") && DateUtils.addDays(emp.getCurrentHiredDate(), 31) <= DateUtils.now()) {
					ret.setBenefitWizardStatus("F");
					ret.setLockAfterFinalize(true);
				}
				BHRBenefitJoin[] bja = bpp.listCurrentBenefitElections();

				List<BHRBenefitJoin> keepers = new ArrayList<BHRBenefitJoin>();
				List<HrBenefit> benefitsInWizard = new ArrayList<HrBenefit>();
				List<HrBenefitCategory> categoriesInWizard = new ArrayList<HrBenefitCategory>();
				for (WizardConfigurationCategory wizCat : wc.getWizardCategories()) {
					categoriesInWizard.add(wizCat.getBenefitCategory());
					BWizardConfigurationCategory bwizCat = new BWizardConfigurationCategory(wizCat);
					for (WizardConfigurationBenefit wizBen : bwizCat.getWizardBenefits())
						benefitsInWizard.add(wizBen.getBenefit());
				}
				for (BHRBenefitJoin bj : bja) {
					if (keepers.contains(bj))
						continue;
					boolean match = false;
					for (HrBenefit b : benefitsInWizard)
						if (!match)
							if (bj.getBenefitId().equals(b.getBenefitId())) {
								match = true;
								break;
							}
					if (match) {
						keepers.add(bj);

						BHRBenefit bb = new BHRBenefit(bj.getBenefitId());
						List<com.arahant.beans.BenefitRider> riders = BBenefitRider.getRidersForBaseBenefit(bb);
						List<BenefitDependency> dependencies = BBenefitDependency.getBenefitDependenciesWhereRequired(bb);
						for (BHRBenefitJoin bj2 : bja) {
							boolean matchRider = false;
							if (keepers.contains(bj2))
								continue;
							for (com.arahant.beans.BenefitRider br : riders)
//								if(br.getHidden() == 'N' && bj2.getBenefitId().equals(br.getRiderBenefitId())) {
//									matchRider = true;
//									break;
//								}
//								else if(br.getHidden() == 'Y' && br.getRequired() == 'N' && bj2.getBenefitId().equals(br.getRiderBenefitId())){
//									matchRider = true;
//									break;
//								}
								if (bj2.getBenefitId().equals(br.getRiderBenefitId())) {
									matchRider = true;
									break;
								}
							for (BenefitDependency bd : dependencies)
								if (bd.getHidden() == 'N' && bj2.getBenefitId().equals(bd.getDependentBenefitId())) {
									matchRider = true;
									break;
								}
							if (matchRider)
								keepers.add(bj2);
						}
						continue;
					}

					for (HrBenefitCategory c : categoriesInWizard)
						if (bj.getBenefitCategoryId().equals(c.getBenefitCatId())) {
							match = true;
							break;
						}
					if (match)
						keepers.add(bj);
				}

				List<BHRBenefitJoin> tempKeepers = new ArrayList<BHRBenefitJoin>();
				for (BHRBenefitJoin bbj : keepers) {
					if (bbj.getEmployeeCovered().equals("Y"))
						tempKeepers.add(bbj);
					if (!bbj.isDecline()) {
						BHRBenefit bb = new BHRBenefit(bbj.getBenefitId());
						if (bb.getBean().getShowAllCoverages() == 'Y')
							for (HrBenefitJoin dbj : bbj.getDependentBenefitJoins())
								tempKeepers.add(new BHRBenefitJoin(dbj));
						else if (bb.getBean().getShowAllCoverages() == 'N' && bbj.getEmployeeCovered().equals("N"))
							tempKeepers.add(bbj);
					}
				}
				keepers = tempKeepers;

				BHRBenefitJoin[] keepersArray = new BHRBenefitJoin[keepers.size()];
				int count = 0;
				for (BHRBenefitJoin bj : keepers)
					keepersArray[count++] = bj;
				if (in.getAsOfDate() == 0)
					date = DateUtils.now();
				else
					date = in.getAsOfDate();
				
				// sort it to agree with buttons on left
				BHRBenefitJoin[] sortedKeepersArray = new BHRBenefitJoin[keepers.size()];
				int si=0;
				//  get elected in right order
				for (HrBenefit ben : benefitsInWizard) {
					for (int i=0 ; i < keepers.size() ; i++) {
						if (keepersArray[i] != null) {
							HrBenefitConfig bc = keepersArray[i].getBean().getHrBenefitConfig();
							if (bc != null) {
								HrBenefit benefit = bc.getHrBenefit();
								if (ben.getBenefitId().equals(benefit.getBenefitId())  &&  !keepersArray[i].isDecline()) {
									sortedKeepersArray[si++] = keepersArray[i];
									keepersArray[i] = null;  // mark as taken
									break;
								}
							}
						}
					}
				}
				//  get declines in right order
				for (HrBenefit ben : benefitsInWizard) {
					for (int i=0 ; i < keepers.size() ; i++) {
						if (keepersArray[i] != null) {
							HrBenefit benefit = keepersArray[i].getBean().getHrBenefit();
							if (benefit != null)
								if (ben.getBenefitId().equals(benefit.getBenefitId())) {
									sortedKeepersArray[si++] = keepersArray[i];
									keepersArray[i] = null;  // mark as taken
									break;
								}
						}
					}
				}
				//  get any remaining
				for (int i=0 ; i < keepers.size() ; i++)
					if (keepersArray[i] != null)
						sortedKeepersArray[si++] = keepersArray[i];
				

				ret.setItem(sortedKeepersArray, bpp, empId, date);
			} else {
				if (in.getAsOfDate() == 0)
					date = DateUtils.now();
				else
					date = in.getAsOfDate();
				ret.setItem(bpp.listCurrentBenefitElections(), bpp, empId, date);
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveSmokerReturn saveSmoker(/*
			 * @WebParam(name = "in")
			 */final SaveSmokerInput in) {
		final SaveSmokerReturn ret = new SaveSmokerReturn();
		try {
			checkLogin(in);
			//if there is no employeeId then get the current login user id
			if (isEmpty(in.getEmployeeId()))
				in.setEmployeeId(hsu.getCurrentPerson().getPersonId());
			final BEmployee x = new BEmployee(in.getEmployeeId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetSmokerMetaReturn getSmokerMeta(/*
			 * @WebParam(name = "in")
			 */final GetSmokerMetaInput in) {
		final GetSmokerMetaReturn ret = new GetSmokerMetaReturn();
		try {
			checkLogin(in);

			//if there is no employeeId then get the current login user id
			if (isEmpty(in.getEmployeeId()))
				in.setEmployeeId(hsu.getCurrentPerson().getPersonId());

			final BEmployee x = new BEmployee(in.getEmployeeId());

			ret.setEmployeeNonSmoker(x.getEmployee().getSmoker() == 'N');
			ret.setEmployeeProgram(x.getEmployee().getSmokingProgram() == 'Y');

			// boolean hasSpouse = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, x.getEmployee()).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').exists();
			HrEmplDependent de = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, x.getEmployee()).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').first();

			if (de != null) {
				ret.setHasSpouse(true);
				ret.setSpouseNonSmoker(de.getPerson().getSmoker() == 'N');
				ret.setSpouseProgram(de.getPerson().getSmokingProgram() == 'Y');
			} else {
				HrEmplDependent dep = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, x.getEmployee()).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').first();
				if (dep != null) {
					BPerson bpp = new BPerson(dep.getDependentId());
					if (bpp.getRecordType() != 'C')
						bpp.loadRealPerson(dep.getDependentId()); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, dep.getPersonId()).first());
					ret.setHasSpouse(true);
					ret.setSpouseNonSmoker(bpp.getTabaccoUse().indexOf(0) == 'N');
					ret.setSpouseProgram(bpp.getSmokingProgram() == 'Y');
				}
			}
			finishService(ret);

		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListFlexBenefitsReturn listFlexBenefits(/*
			 * @WebParam(name = "in")
			 */final ListFlexBenefitsInput in) {
		final ListFlexBenefitsReturn ret = new ListFlexBenefitsReturn();
		try {
			checkLogin(in);

			String empId;

			if (!isEmpty(in.getEmployeeId()))
				empId = in.getEmployeeId();
			else
				empId = ArahantSession.getCurrentPerson().getPersonId();

			int date;

			if (in.getAsOfDate() == 0)
				date = DateUtils.now();
			else
				date = in.getAsOfDate();

			// BEmployee bemp = isEmpty(in.getEmployeeId()) ? BPerson.getCurrent().getBEmployee() : new BEmployee(in.getEmployeeId());
			if (BProperty.isWizard3()) {
				List<WizardConfigurationConfig> wizConfs = hsu.createCriteria(WizardConfigurationConfig.class).joinTo(WizardConfigurationConfig.WIZARD_CONFIGURATION_BENEFIT).orderBy(WizardConfigurationBenefit.SEQNO).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, new BWizardConfiguration(in.getWizardConfigurationId()).getBean()).joinTo(WizardConfigurationCategory.CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.FLEX_TYPE).list();

				//hcu.dateInside(HrBenefitConfig.START_DATE, HrBenefitConfig.END_DATE, DateUtils.now()).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y').joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.FLEX_TYPE);

				ret.setItem(wizConfs, empId, date);
			} else {
				HibernateCriteriaUtil<HrBenefitConfig> hcu = hsu.createCriteria(HrBenefitConfig.class);

				BEmployee bemp = isEmpty(in.getEmployeeId()) ? BPerson.getCurrent().getBEmployee() : new BEmployee(in.getEmployeeId());

				//check benefit classes
				if (bemp.getBenefitClass() != null) {
					HibernateCriteriaUtil chcu = hcu;
					//either has no classes specified or they match
					HibernateCriterionUtil orcri = chcu.makeCriteria();
					HibernateCriterionUtil cri1 = chcu.makeCriteria();

					HibernateCriteriaUtil classHcu = chcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES, "classes");

					HibernateCriterionUtil cri2 = classHcu.makeCriteria();

					cri1.sizeEq(HrBenefitConfig.BENEFIT_CLASSES, 0);
					cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, bemp.getBenefitClass().getBenefitClassId());

					orcri.or(cri1, cri2);

					orcri.add();
				}

				hcu.dateInside(HrBenefitConfig.START_DATE, HrBenefitConfig.END_DATE, DateUtils.now()).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y').joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.FLEX_TYPE);

				ret.setItem(BHRBenefitConfig.makeArray(hcu.list()), empId, date);
			}
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveFlexConfigsAndCostsReturn saveFlexConfigsAndCosts(/*
			 * @WebParam(name = "in")
			 */final SaveFlexConfigsAndCostsInput in) {
		final SaveFlexConfigsAndCostsReturn ret = new SaveFlexConfigsAndCostsReturn();
		try {
			checkLogin(in);

			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();

			int date = in.getAsOfDate() == 0 ? DateUtils.now() : in.getAsOfDate();

			String[] selectedIds = new String[in.getFlexElections().length];

			HashMap<String, Double> map = new HashMap<String, Double>();

			//Fill selectedIds with all the configIds from flexElections
			//Fill map with the info from flexElections
			int i = 0;
			for (FlexElection fe : in.getFlexElections()) {
				selectedIds[i++] = fe.getConfigId();
				map.put(fe.getConfigId(), fe.getCost());
			}

//            HashSet<String> done = new HashSet<String>();
//
//            List<HrBenefitJoin> l = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON_ID, empId).in(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, selectedIds).eq(HrBenefitJoin.APPROVED, 'N').list();



			BPerson bpp = new BPerson();

			//if there is a pending record, get it
			if (bpp.hasPending(empId))
				bpp.loadPending(empId);
			else
				//otherwise, get the Real person record
				bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());

			HibernateCriteriaUtil<HrBenefit> bhcu = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y');

			bhcu.joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.FLEX_TYPE);
			BEmployee be = bpp.getBEmployee();
			if (be.getBenefitClass() != null) {
				HibernateCriterionUtil orcriBen = bhcu.makeCriteria();
				HibernateCriterionUtil orcriAll = bhcu.makeCriteria();
				HibernateCriterionUtil cri1Ben = bhcu.makeCriteria();

				HibernateCriteriaUtil classHcuBen = bhcu.leftJoinTo("benefitClasses", "classesBen");

				HibernateCriterionUtil cri2Ben = classHcuBen.makeCriteria();

				cri1Ben.sizeEq("benefitClasses", 0);
				cri2Ben.eq("classesBen." + BenefitClass.BENEFIT_CLASS_ID, be.getBenefitClassId());

				orcriBen.or(cri1Ben, cri2Ben);

				HibernateCriteriaUtil chcu = bhcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
				//either has no classes specified or they match
				HibernateCriterionUtil orcri = chcu.makeCriteria();
				HibernateCriterionUtil cri1 = chcu.makeCriteria();

				HibernateCriteriaUtil classHcu = chcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES, "classes");

				HibernateCriterionUtil cri2 = classHcu.makeCriteria();

				cri1.sizeEq(HrBenefitConfig.BENEFIT_CLASSES, 0);
				cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, be.getBenefitClass().getBenefitClassId());

				orcri.or(cri1, cri2);
				orcriAll.or(orcri, orcriBen);
				orcriAll.add();
			}

			List<String> benefitIds = new ArrayList<String>();
			for (String s : selectedIds) {
				BHRBenefitConfig bc = new BHRBenefitConfig(s);
				benefitIds.add(bc.getBenefitId());
				bpp.enrollInConfig(s, in.getChangeReasonId(), date, new String[]{}, map.get(s), true, in.getExplanation());
			}

			for (HrBenefit hb : bhcu.notIn(HrBenefit.BENEFITID, benefitIds).list())
				bpp.noEnrollments(hb.getBenefitId(), in.getChangeReasonId(), date, in.getExplanation());

			BEmployee bemp = new BEmployee(empId);
			bemp.setBenefitWizardStatus(Employee.BENEFIT_WIZARD_STATUS_UNFINALIZED + "");
			bemp.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListVoluntaryBenefitsReturn listVoluntaryBenefits(/*
			 * @WebParam(name = "in")
			 */final ListVoluntaryBenefitsInput in) {
		final ListVoluntaryBenefitsReturn ret = new ListVoluntaryBenefitsReturn();
		try {
			checkLogin(in);

			String empId;

			if (!isEmpty(in.getEmployeeId()))
				empId = in.getEmployeeId();
			else
				empId = ArahantSession.getCurrentPerson().getPersonId();

			int date;

			if (in.getAsOfDate() == 0)
				date = DateUtils.now();
			else
				date = in.getAsOfDate();

			HibernateCriteriaUtil<HrBenefitConfig> hcu = hsu.createCriteria(HrBenefitConfig.class);

			BEmployee bemp = isEmpty(in.getEmployeeId()) ? BPerson.getCurrent().getBEmployee() : new BEmployee(in.getEmployeeId());

			//check benefit classes
			if (bemp.getBenefitClass() != null) {
				HibernateCriteriaUtil chcu = hcu;
				//either has no classes specified or they match
				HibernateCriterionUtil orcri = chcu.makeCriteria();
				HibernateCriterionUtil cri1 = chcu.makeCriteria();

				HibernateCriteriaUtil classHcu = chcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES, "classes");

				HibernateCriterionUtil cri2 = classHcu.makeCriteria();

				cri1.sizeEq(HrBenefitConfig.BENEFIT_CLASSES, 0);
				cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, bemp.getBenefitClass().getBenefitClassId());

				orcri.or(cri1, cri2);

				orcri.add();
			}

//			hcu.joinTo(HrBenefitConfig.HR_BENEFIT)
//				.joinTo(HrBenefit.BENEFIT_CATEGORY)
//				.eq(HrBenefitCategory.TYPE, HrBenefitCategory.VOLUNTARY);
//
//			ret.setItem(BHRBenefitConfig.makeArray(hcu.list()),empId,date);

			ret.setItem(bemp.getAllValidConfigs(in.getId()), empId, date);
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetVoluntaryCostsReturn getVoluntaryCosts(/*
			 * @WebParam(name = "in")
			 */final GetVoluntaryCostsInput in) {
		final GetVoluntaryCostsReturn ret = new GetVoluntaryCostsReturn();
		try {
			checkLogin(in);

			ret.setData(in);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveVoluntaryEnrollmentReturn saveVoluntaryEnrollment(/*
			 * @WebParam(name = "in")
			 */final SaveVoluntaryEnrollmentInput in) {
		final SaveVoluntaryEnrollmentReturn ret = new SaveVoluntaryEnrollmentReturn();
		try {
			checkLogin(in);
			//in.getAnnualCoverage(), in.getAsOfDate(), in.getBenefitId(), in.getChangeReasonId(), in.getConfigId(), in.getEmployeeId(), in.getEnrolleeIds()
			BHRBenefitJoin bj = new BHRBenefitJoin();
			bj.create();

			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();

			BPerson bp = new BPerson();

			//if there is a pending record, get it
			if (bp.hasPending(empId))
				bp.loadPending(empId);
			else
				//otherwise, get the Real person record
				bp.loadRealPerson(empId); //bp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());

			if (isEmpty(in.getConfigId()))
				bp.noEnrollments(in.getBenefitId(), in.getChangeReasonId(), in.getAsOfDate(), in.getExplanation());
			else
				bp.enrollInConfig(in.getConfigId(), in.getChangeReasonId(), in.getAsOfDate(), in.getEnrolleeIds(), in.getAnnualCoverage(), true, in.getExplanation());

			BEmployee bemp = new BEmployee(empId);
			bemp.setBenefitWizardStatus(Employee.BENEFIT_WIZARD_STATUS_UNFINALIZED + "");
			bemp.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetCategoryMetaReturn getCategoryMeta(/*
			 * @WebParam(name = "in")
			 */final GetCategoryMetaInput in) {
		final GetCategoryMetaReturn ret = new GetCategoryMetaReturn();
		try {
			//		Date now=new Date();
			checkLogin(in);

			String empId = isEmpty(in.getEmployeeId()) ? BPerson.getCurrent().getPersonId() : in.getEmployeeId();

			if (in.getAsOfDate() == 0)
				in.setAsOfDate(DateUtils.now());

			if (BProperty.isWizard3())
				ret.setData(new BHRBenefitCategory(in.getCategoryId()), empId, in.getAsOfDate(), new BWizardConfiguration(in.getWizardConfigurationId()), in.getBcrId());
			else
				ret.setData(new BHRBenefitCategory(in.getCategoryId()), empId, in.getAsOfDate());
//System.out.println("category meta "+((new Date().getTime()-now.getTime())/1000));
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBeneficiariesReturn listBeneficiaries(/*
			 * @WebParam(name = "in")
			 */final ListBeneficiariesInput in) {
		final ListBeneficiariesReturn ret = new ListBeneficiariesReturn();
		try {
			checkLogin(in);

			String empId = !isEmpty(in.getEmployeeId()) ? in.getEmployeeId() : BPerson.getCurrent().getPersonId();
			BHRBenefitJoin bj;
			if (isEmpty(in.getBenefitJoinId()))
				bj = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, in.getConfigId()).eq(HrBenefitJoin.APPROVED, 'N').first());
			else
				bj = new BHRBenefitJoin(in.getBenefitJoinId());
			ret.setPrimary(BHRBeneficiary.listPrimaries(bj));
			ret.setContingent(BHRBeneficiary.listSecondaries(bj));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewBeneficiaryReturn newBeneficiary(/*
			 * @WebParam(name = "in")
			 */final NewBeneficiaryInput in) {
		final NewBeneficiaryReturn ret = new NewBeneficiaryReturn();
		try {
			checkLogin(in);

			String empId = !isEmpty(in.getEmployeeId()) ? in.getEmployeeId() : BPerson.getCurrent().getPersonId();
			BHRBenefitJoin bj;
			if (isEmpty(in.getBenefitJoinId())) {
				HrBenefitJoin j = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, in.getConfigId()).eq(HrBenefitJoin.APPROVED, 'N').first();

				if (j == null)
					j = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, in.getConfigId()).eq(HrBenefitJoin.APPROVED, 'Y').first();

				bj = new BHRBenefitJoin(j);
			} else
				bj = new BHRBenefitJoin(in.getBenefitJoinId());
			final BHRBeneficiary x = new BHRBeneficiary();
			ret.setId(x.create());
			in.setData(x, bj);
			x.insert();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveBeneficiaryReturn saveBeneficiary(/*
			 * @WebParam(name = "in")
			 */final SaveBeneficiaryInput in) {
		final SaveBeneficiaryReturn ret = new SaveBeneficiaryReturn();
		try {
			checkLogin(in);


			final BHRBeneficiary x = new BHRBeneficiary(in.getBeneficiaryId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteBeneficiariesReturn deleteBeneficiaries(/*
			 * @WebParam(name = "in")
			 */final DeleteBeneficiariesInput in) {
		final DeleteBeneficiariesReturn ret = new DeleteBeneficiariesReturn();
		try {
			checkLogin(in);

			BHRBeneficiary.delete(in.getIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	//Start out at category get benefit and then get config from that
	@WebMethod()
	public ListCategoryConfigsReturn listCategoryConfigs(/*
			 * @WebParam(name = "in")
			 */final ListCategoryConfigsInput in) {
		final ListCategoryConfigsReturn ret = new ListCategoryConfigsReturn();
		try {
			//		Date now=new Date();
			checkLogin(in);

			String empId = !isEmpty(in.getEmployeeId()) ? in.getEmployeeId() : BPerson.getCurrent().getPersonId();

			HibernateCriteriaUtil<HrBenefitConfig> hcu = hsu.createCriteria(HrBenefitConfig.class);

			hcu.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'Y');

			List<HrBenefitConfig> l = hcu.joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, in.getCategoryId()).list();



			ret.setItem(BHRBenefitConfig.makeArray(l), empId, in.getAsOfDate());
//		System.out.println("category configs list "+((new Date().getTime()-now.getTime())/1000));
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetApprovedEnrollmentReturn getApprovedEnrollment(/*
			 * @WebParam(name = "in")
			 */final GetApprovedEnrollmentInput in) {
		final GetApprovedEnrollmentReturn ret = new GetApprovedEnrollmentReturn();
		try {
//			Date now=new Date();
			checkLogin(in);

			HrBenefitJoin bj = new HrBenefitJoin();

			String empId = !isEmpty(in.getEmployeeId()) ? in.getEmployeeId() : BPerson.getCurrent().getPersonId();

			List<HrBenefitConfig> configIds;

			if (!isEmpty(in.getBenefitId())) {
				configIds = hsu.createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID).eq(HrBenefitConfig.HR_BENEFIT, new BHRBenefit(in.getBenefitId()).getBean()).list();

				bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'Y').in(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configIds).first();

				if (bj == null && new BHRBenefit(in.getBenefitId()).getReplacedBenefits() != null) {
					configIds = hsu.createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID).in(HrBenefitConfig.HR_BENEFIT, new BHRBenefit(in.getBenefitId()).getReplacedBenefits()).list();

					//There could be multiple bj's if this benefit replaces multiple benefits
					bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'Y').in(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configIds).first();
				}

			} else if (!isEmpty(in.getCategoryId())) {
				configIds = hsu.createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, in.getCategoryId()).list();

				bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'Y').in(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configIds).first();

				if (bj == null)
					for (BHRBenefit bhr : new BHRBenefitCategory(in.getCategoryId()).listActiveBenefits(in.getAsOfDate()))
						if (bhr.getReplacedBenefits() != null) {
							configIds = hsu.createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID).in(HrBenefitConfig.HR_BENEFIT, bhr.getReplacedBenefits()).list();

							//There could be multiple bj's if this benefit replaces multiple benefits
							bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'Y').in(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configIds).first();

							if (bj != null)
								break;
						}
			}

			BEmployee bemp = new BEmployee(empId);
			if (bj != null)
				ret.setData(new BHRBenefitJoin(bj), empId, in.getAsOfDate());
			else
				if (!isEmpty(in.getBenefitId()))
					ret.setData(bemp.declinedBenefit(in.getBenefitId()));
				else if (!isEmpty(in.getCategoryId()))
					ret.setData(bemp.declinedCategory(in.getCategoryId()));
				else
					ret.setData(false);
//			System.out.println("approved enrollment "+((new Date().getTime()-now.getTime())/1000));
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListPhysiciansReturn listPhysicians(/*
			 * @WebParam(name = "in")
			 */final ListPhysiciansInput in) {

		final ListPhysiciansReturn ret = new ListPhysiciansReturn();

		try {
			checkLogin(in);
			BWizardConfiguration bwc = new BWizardConfiguration(in.getWizardConfigurationId());
			if (!bwc.getPhysicianModeImmediate()) //specify physicians at end, so list all by employee
			{
				if (isEmpty(in.getEmployeeId()))
					in.setEmployeeId(hsu.getCurrentPerson().getPersonId());

				ret.setItem(BHRPhysician.listPhysiciansForWizard(in.getEmployeeId(), in.getWizardConfigurationId(), ret.getCap()));
			} else //specify physicians as you enroll, so list all by the enrollment
			
				ret.setItem(BHRPhysician.listAllPhysiciansInGroup(new BHRBenefitJoin(in.getBenefitJoinId())));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewPhysicianReturn newPhysician(/*
			 * @WebParam(name = "in")
			 */final NewPhysicianInput in) {

		final NewPhysicianReturn ret = new NewPhysicianReturn();

		try {
			checkLogin(in);

			final BHRPhysician x = new BHRPhysician();
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
	public SavePhysicianReturn savePhysician(/*
			 * @WebParam(name = "in")
			 */final SavePhysicianInput in) {

		final SavePhysicianReturn ret = new SavePhysicianReturn();

		try {
			checkLogin(in);

			final BHRPhysician x = new BHRPhysician(in.getId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitElectionsNonDeclinesReturn listBenefitElectionsNonDeclines(/*
			 * @WebParam(name = "in")
			 */final ListBenefitElectionsNonDeclinesInput in) {
		final ListBenefitElectionsNonDeclinesReturn ret = new ListBenefitElectionsNonDeclinesReturn();
		try {
			checkLogin(in);

			BPerson bpp = new BPerson();

			int date;

			String empId = isEmpty(in.getEmployeeId()) ? ArahantSession.getCurrentPerson().getPersonId() : in.getEmployeeId();
			//if there is a pending record, get it
			if (bpp.hasPending(empId))
				bpp.loadPending(empId);
			else
				//otherwise, get the Real person record
				bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());

			ret.setItem(bpp.listCurrentBenefitElectionsWithPhysicians(false));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEnrolleesReturn listEnrollees(/*
			 * @WebParam(name = "in")
			 */final ListEnrolleesInput in) {

		final ListEnrolleesReturn ret = new ListEnrolleesReturn();

		try {
			checkLogin(in);

			BHRBenefitJoin bj = new BHRBenefitJoin(in.getBenefitJoinId());
			if (bj.isPolicyBenefitJoin())
				ret.setItem(bj.getPolicyAndDependentBenefitJoins(false));
			else {
				BHRBenefitJoin policyJoin = new BHRBenefitJoin(bj.getPolicyBenefitJoin());
				ret.setItem(policyJoin.getPolicyAndDependentBenefitJoins(false));
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeletePhysiciansReturn deletePhysicians(/*
			 * @WebParam(name = "in")
			 */final DeletePhysiciansInput in) {

		final DeletePhysiciansReturn ret = new DeletePhysiciansReturn();

		try {
			checkLogin(in);

			BHRPhysician.delete(in.getIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public FinalizeBenefitWizardReturn finalizeBenefitWizard(/*
			 * @WebParam(name = "in")
			 */final FinalizeBenefitWizardInput in) {

		final FinalizeBenefitWizardReturn ret = new FinalizeBenefitWizardReturn();

		try {
			checkLogin(in);

			String empId = isEmpty(in.getEmployeeId()) ? ArahantSession.getCurrentPerson().getPersonId() : in.getEmployeeId();

			BEmployee emp = new BEmployee(empId);
			Date now = new Date();
			//if the wizard is already finalized, it shouldn't re-finalize for no reason and create duplicate projects
			if (!emp.getBenefitWizardStatus().equals(Employee.BENEFIT_WIZARD_STATUS_FINALIZED + "")) {
				emp.setBenefitWizardStatus(Employee.BENEFIT_WIZARD_STATUS_FINALIZED + "");
				emp.update();

				if (BProperty.isWizard3()) {
					BWizardConfiguration bwc = new BWizardConfiguration(in.getWizardConfigurationId());
					bwc.createWizardProjects(emp);

					if (bwc.getPaymentInfoBoolean()) {
						BPaymentInfo bpi = new BPaymentInfo();
						if (!isEmpty(in.getPaymentInfoId())) {
							bpi = new BPaymentInfo(in.getPaymentInfoId());
							in.savePaymentInfoData(bpi, now, getRemoteHost());
						} else {
							bpi.create();
							in.newPaymentInfoData(bpi, now, getRemoteHost());
						}

					}
				}
			}

			List<HrBenefitJoin> joins = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'N').list();
			for (HrBenefitJoin j : joins)
//				if (!in.getModal().equals("P")) {
//					j.setRequestedCostPeriod(in.getModal().charAt(0));
//					new BHRBenefitJoin(j).update();
//				} else 
				{
					int ppy = emp.getPayPeriodsPerYear();

					switch (ppy) {
						case 1:
							j.setRequestedCostPeriod('A');
							break;
						case 2:
							j.setRequestedCostPeriod('S');
							break;
						case 4:
							j.setRequestedCostPeriod('Q');
							break;
						case 12:
							j.setRequestedCostPeriod('M');
							break;
						case 52:
							j.setRequestedCostPeriod('W');
							break;
					}
				}

			for (BScreenOrGroup bsog : BScreenGroup.getEnrollmentWizardGroups()) {
				boolean check = BHRCheckListItem.checkComplete(bsog);
				if (check) {
					ret.setTaskComplete(check);
					break;
				}
			}

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

	/////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////// CUSTOM WMCO GROUP CRITICAL ILLNESS QUESTIONS PAGE ////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	@WebMethod()
	public LoadQuestionAnswersReturn loadQuestionAnswers(/*
			 * @WebParam(name = "in")
			 */final LoadQuestionAnswersInput in) {
		final LoadQuestionAnswersReturn ret = new LoadQuestionAnswersReturn();
		try {
			checkLogin(in);
			if (isEmpty(in.getEmployeeId()))
				in.setEmployeeId(hsu.getCurrentPerson().getPersonId());
			ret.setData(in.getEmployeeId());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveQuestionAnswersReturn saveQuestionAnswers(/*
			 * @WebParam(name = "in")
			 */final SaveQuestionAnswersInput in) {
		final SaveQuestionAnswersReturn ret = new SaveQuestionAnswersReturn();
		try {
			checkLogin(in);
			if (isEmpty(in.getEmployeeId()))
				in.setEmployeeId(hsu.getCurrentPerson().getPersonId());
			BEmployee be = new BEmployee(in.getEmployeeId());
			if (!isEmpty(in.getActivelyAtWork()))
				be.setActivelyAtWork(in.getActivelyAtWork().charAt(0));
			if (!isEmpty(in.getSmoker()))
				be.getPerson().setSmoker(in.getSmoker().charAt(0));
			if (!isEmpty(in.getHasAids()))
				be.getPerson().setHasAids(in.getHasAids().charAt(0));
			if (!isEmpty(in.getHasCancer()))
				be.getPerson().setHasCancer(in.getHasCancer().charAt(0));
			if (!isEmpty(in.getHasHeartCondition()))
				be.getPerson().setHasHeartCondition(in.getHasHeartCondition().charAt(0));

			if (be.hasSpouse()) {
				if (!isEmpty(in.getSmokerSpouse()))
					be.getSpouse().getPerson().setSmoker(in.getSmokerSpouse().charAt(0));
				if (!isEmpty(in.getUnableToPerform()))
					be.getSpouse().getPerson().setUnableToPerform(in.getUnableToPerform().charAt(0));
				if (!isEmpty(in.getHasAidsSpouse()))
					be.getSpouse().getPerson().setHasAids(in.getHasAidsSpouse().charAt(0));
				if (!isEmpty(in.getHasCancerSpouse()))
					be.getSpouse().getPerson().setHasCancer(in.getHasCancerSpouse().charAt(0));
				if (!isEmpty(in.getHasHeartConditionSpouse()))
					be.getSpouse().getPerson().setHasHeartCondition(in.getHasHeartConditionSpouse().charAt(0));
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////// CUSTOM HUMANA CRITICAL ILLNESS QUESTIONS PAGE ////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	@WebMethod()
	public LoadQuestionAnswersHumanaReturn loadQuestionAnswersHumana(/*
			 * @WebParam(name = "in")
			 */final LoadQuestionAnswersHumanaInput in) {
		final LoadQuestionAnswersHumanaReturn ret = new LoadQuestionAnswersHumanaReturn();
		try {
			checkLogin(in);
			if (isEmpty(in.getEmployeeId()))
				in.setEmployeeId(hsu.getCurrentPerson().getPersonId());

			int personCount = 1;

			BEmployee be = new BEmployee(in.getEmployeeId());
			if (!isEmpty(be.getState())) {
				String state = be.getState();
				if (state.equalsIgnoreCase("california") || state.equalsIgnoreCase("ca"))
					ret.setCalifornia(true);
				else
					ret.setCalifornia(false);
			} else if (!isEmpty(be.getCompany().getState())) {
				String state = be.getCompany().getState();
				if (state.equalsIgnoreCase("california") || state.equalsIgnoreCase("ca"))
					ret.setCalifornia(true);
				else
					ret.setCalifornia(false);
			} else
				ret.setCalifornia(false);

			if (in.getEnrolleeIds() != null) {
				LoadQuestionAnswersHumanaReturnItem[] arr = new LoadQuestionAnswersHumanaReturnItem[1 + in.getEnrolleeIds().length];
				arr[0] = getAnswers(new BPerson(in.getEmployeeId()), 0, personCount, ret.getCalifornia());
				ret.setItem(arr);
				personCount++;
				int count = 1;
				for (String s : in.getEnrolleeIds()) {
					BHREmplDependent dep = new BHREmplDependent(s);
					arr[count++] = getAnswers(new BPerson(dep.getPerson()), dep.getRelationshipType().equals("S") ? 1 : 2, personCount, ret.getCalifornia());
					personCount++;
				}
			} else {
				LoadQuestionAnswersHumanaReturnItem[] arr = new LoadQuestionAnswersHumanaReturnItem[1];
				arr[0] = getAnswers(new BPerson(in.getEmployeeId()), 0, personCount, ret.getCalifornia());
				ret.setItem(arr);
				personCount++;
			}
//			else
//			{
//				BHRBenefitJoin bbj = new BHRBenefitJoin(in.getBenefitJoinId());
//				BHRBenefitJoin[] bbjs = bbj.getPolicyAndDependentBenefitJoins(false);
//				LoadQuestionAnswersHumanaReturnItem[] arr = new LoadQuestionAnswersHumanaReturnItem[bbjs.length];
//				int count = 0;
//				for (BHRBenefitJoin bbj2 : bbjs)
//				{
//					arr[count++] = getAnswers(bbj2.getCoveredPerson(), bbj2.getRelationship() == null ? 0 : bbj2.getRelationship().getRelationshipType().equals("S") ? 1 : 2, personCount, ret.getCalifornia());
//					personCount++;
//				}
//				ret.setItem(arr);
//			}


			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveQuestionAnswersHumanaReturn saveQuestionAnswersHumana(/*
			 * @WebParam(name = "in")
			 */final SaveQuestionAnswersHumanaInput in) {
		final SaveQuestionAnswersHumanaReturn ret = new SaveQuestionAnswersHumanaReturn();
		try {
			checkLogin(in);



			for (LoadQuestionAnswersHumanaReturnItem item : in.getInput())
				if (in.getCalifornia()) {
					BPerson bp = new BPerson(item.getPersonId());
					if (!item.getRow1().equals("NA") && !isEmpty(item.getRow1()))
						bp.setTabaccoUse(item.getRow1());
					if (!item.getRow2().equals("NA") && !isEmpty(item.getRow2()))
						bp.setActivelyAtWork(item.getRow2().charAt(0));
					if (!item.getRow3().equals("NA") && !isEmpty(item.getRow3()))
						bp.setReplaceEmployerPlan(item.getRow3());
					if (!item.getRow4().equals("NA") && !isEmpty(item.getRow4()))
						bp.setNotMissedFiveDays(item.getRow4());
					if (!item.getRow5().equals("NA") && !isEmpty(item.getRow5()))
						bp.setHasOtherMedical(item.getRow5());

					bp.setHasAids(item.getRow6().charAt(0));
					bp.setUnableToPerform(item.getRow7().charAt(0));
					bp.setHasHeartCondition(item.getRow8().charAt(0));
					bp.setHasCancer(item.getRow9().charAt(0));
					bp.setDrugAlcoholAbuse(item.getRow10());
					bp.setTwoFamilyHeartCond(item.getRow11());
					bp.setTwoFamilyCancer(item.getRow12());
					bp.setTwoFamilyDiabetes(item.getRow13());
					bp.update();
				} else {
					BPerson bp = new BPerson(item.getPersonId());
					if (!item.getRow1().equals("NA") && !isEmpty(item.getRow1()))
						bp.setTabaccoUse(item.getRow1());
					if (!item.getRow2().equals("NA") && !isEmpty(item.getRow2()))
						bp.setActivelyAtWork(item.getRow2().charAt(0));
					if (!item.getRow3().equals("NA") && !isEmpty(item.getRow3()))
						bp.setReplaceEmployerPlan(item.getRow3());
					if (!item.getRow4().equals("NA") && !isEmpty(item.getRow4()))
						bp.setNotMissedFiveDays(item.getRow4());

					bp.setHasAids(item.getRow5().charAt(0));
					bp.setUnableToPerform(item.getRow6().charAt(0));
					bp.setHasHeartCondition(item.getRow7().charAt(0));
					bp.setHasCancer(item.getRow8().charAt(0));
					bp.setDrugAlcoholAbuse(item.getRow9());
					bp.setTwoFamilyHeartCond(item.getRow10());
					bp.setTwoFamilyCancer(item.getRow11());
					bp.setTwoFamilyDiabetes(item.getRow12());
					bp.update();
				}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	private LoadQuestionAnswersHumanaReturnItem getAnswers(BPerson bp, int type, int count, boolean isCalifornia) {
		LoadQuestionAnswersHumanaReturnItem ret = new LoadQuestionAnswersHumanaReturnItem();
		ret.setDataField("person" + count);
		ret.setColumnName(bp.getFirstName() + " " + bp.getLastName());
		ret.setPersonId(bp.getPersonId());
		if (type == 0) // employee
		
			if (isCalifornia) {
				ret.setRow1(bp.getTabaccoUse());
				ret.setRow2(bp.getActivelyAtWork());
				ret.setRow3(bp.getReplaceEmployerPlan()); //replace CI plan provided by employer?
				ret.setRow4(bp.getHasOtherMedical());
				ret.setRow5(bp.getNotMissedFiveDays()); //actively at work and missed less than 5 days last 12 months
				ret.setRow6(bp.getHasAids() + "");
				ret.setRow7(bp.getUnableToPerform());
				ret.setRow8(bp.getHasHeartCondition() + "");
				ret.setRow9(bp.getHasCancer() + "");
				ret.setRow10(bp.getDrugAlcoholAbuse());
				ret.setRow11(bp.getTwoFamilyHeartCond());
				ret.setRow12(bp.getTwoFamilyCancer());
				ret.setRow13(bp.getTwoFamilyDiabetes());
			} else {
				ret.setRow1(bp.getTabaccoUse());
				ret.setRow2(bp.getActivelyAtWork());
				ret.setRow3(bp.getReplaceEmployerPlan()); //replace CI plan provided by employer?
				ret.setRow4(bp.getNotMissedFiveDays()); //actively at work and missed less than 5 days last 12 months
				ret.setRow5(bp.getHasAids() + "");
				ret.setRow6(bp.getUnableToPerform());
				ret.setRow7(bp.getHasHeartCondition() + "");
				ret.setRow8(bp.getHasCancer() + "");
				ret.setRow9(bp.getDrugAlcoholAbuse());
				ret.setRow10(bp.getTwoFamilyHeartCond());
				ret.setRow11(bp.getTwoFamilyCancer());
				ret.setRow12(bp.getTwoFamilyDiabetes());
			}
		else if (type == 1) // spouse
		
			if (isCalifornia) {
				ret.setRow1(bp.getTabaccoUse());
				ret.setRow2("NA");
				ret.setRow3("NA"); //replace CI plan provided by employer?
				ret.setRow4("NA");
				ret.setRow5("NA"); //actively at work and missed less than 5 days last 12 months
				ret.setRow6(bp.getHasAids() + "");
				ret.setRow7(bp.getUnableToPerform());
				ret.setRow8(bp.getHasHeartCondition() + "");
				ret.setRow9(bp.getHasCancer() + "");
				ret.setRow10(bp.getDrugAlcoholAbuse());
				ret.setRow11(bp.getTwoFamilyHeartCond());
				ret.setRow12(bp.getTwoFamilyCancer());
				ret.setRow13(bp.getTwoFamilyDiabetes());
			} else {
				ret.setRow1(bp.getTabaccoUse());
				ret.setRow2("NA");
				ret.setRow3("NA"); //replace CI plan provided by employer?
				ret.setRow4("NA"); //actively at work and missed less than 5 days last 12 months
				ret.setRow5(bp.getHasAids() + "");
				ret.setRow6(bp.getUnableToPerform());
				ret.setRow7(bp.getHasHeartCondition() + "");
				ret.setRow8(bp.getHasCancer() + "");
				ret.setRow9(bp.getDrugAlcoholAbuse());
				ret.setRow10(bp.getTwoFamilyHeartCond());
				ret.setRow11(bp.getTwoFamilyCancer());
				ret.setRow12(bp.getTwoFamilyDiabetes());
			}
		else if (type == 2) // child/other
		
			if (isCalifornia) {
				ret.setRow1("NA");
				ret.setRow2("NA");
				ret.setRow3("NA"); //replace CI plan provided by employer?
				ret.setRow4("NA");
				ret.setRow5("NA"); //actively at work and missed less than 5 days last 12 months
				ret.setRow6(bp.getHasAids() + "");
				ret.setRow7(bp.getUnableToPerform());
				ret.setRow8(bp.getHasHeartCondition() + "");
				ret.setRow9(bp.getHasCancer() + "");
				ret.setRow10(bp.getDrugAlcoholAbuse());
				ret.setRow11(bp.getTwoFamilyHeartCond());
				ret.setRow12(bp.getTwoFamilyCancer());
				ret.setRow13(bp.getTwoFamilyDiabetes());
			} else {
				ret.setRow1("NA");
				ret.setRow2("NA");
				ret.setRow3("NA"); //replace CI plan provided by employer?
				ret.setRow4("NA"); //actively at work and missed less than 5 days last 12 months
				ret.setRow5(bp.getHasAids() + "");
				ret.setRow6(bp.getUnableToPerform());
				ret.setRow7(bp.getHasHeartCondition() + "");
				ret.setRow8(bp.getHasCancer() + "");
				ret.setRow9(bp.getDrugAlcoholAbuse());
				ret.setRow10(bp.getTwoFamilyHeartCond());
				ret.setRow11(bp.getTwoFamilyCancer());
				ret.setRow12(bp.getTwoFamilyDiabetes());
			}
		return ret;

	}

	@WebMethod()
	public SaveConfigAndEnrolleesHumanaReturn saveConfigAndEnrolleesHumana(/*
			 * @WebParam(name = "in")
			 */final SaveConfigAndEnrolleesHumanaInput in) {
		final SaveConfigAndEnrolleesHumanaReturn ret = new SaveConfigAndEnrolleesHumanaReturn();
		try {
			checkLogin(in);
			int qualifyingEventDate = in.getQualifyingEventDate();
			if (qualifyingEventDate < DateUtils.now())
				qualifyingEventDate = DateUtils.now();  // can't start a plan befor today

			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();

			boolean adjustDependentCoverageAmounts = false;
			if (BProperty.isWizard3())
				//are we actually doing an enrollment or just checking if we would allow the enrollment
				if (!in.getDoEnrollment()) {
					if (!isEmpty(in.getConfigId()) && in.getEnrolleeIds().length != 0) {
						List<BHRBenefitJoin> bjs = new ArrayList<BHRBenefitJoin>();
						for (String s : in.getEnrolleeIds()) {
							BHRBenefitJoin bj = new BHRBenefitJoin();
							bj.create();
							bj.setBenefitConfigId(in.getConfigId());
							bj.setPayingPersonId(empId);
							bj.setCoveredPersonId(s);
							bjs.add(bj);


						}
						if (BHRBenefitJoin.checkPotentialOverCoverage(bjs))
							throw new ArahantWarning("You have selected a coverage level that is excessive given the dependents you are enrolling.");
					}
				} else {

					BPerson bpp = new BPerson();
					BEmployee bemp = new BEmployee(empId);
					bemp.setBenefitWizardStatus(Employee.BENEFIT_WIZARD_STATUS_UNFINALIZED + "");
					BWizardConfiguration bz = bemp.getWizardConfiguration("E");
					bemp.update();
					//if there is a pending record, get it
					if (bpp.hasPending(empId))
						bpp.loadPending(empId);
					else
						//otherwise, get the Real person record
						bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());
					if (isEmpty(in.getConfigId()))
						if (!isEmpty(in.getBenefitId()))
							ret.setBenefitJoinId(bpp.noEnrollments(in.getBenefitId(), in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), bz.getAutoApproveDeclines()));
						else
							ret.setBenefitJoinId(bpp.noEnrollments(null, in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), bz.getAutoApproveDeclines(), in.getCategoryId()));
					else {
						if (in.getCoverageAmount() == 0)
							ret.setBenefitJoinId(bpp.enrollInConfig(in.getConfigId(), in.getChangeReasonId(), qualifyingEventDate, in.getEnrolleeIds(), 0, true, in.getExplanation()));
						else {
							if (in.getEnrolleeIds().length > 0)
								adjustDependentCoverageAmounts = true;
							ret.setBenefitJoinId(bpp.enrollInConfig(in.getConfigId(), in.getChangeReasonId(), qualifyingEventDate, in.getEnrolleeIds(), in.getCoverageAmount(), true, in.getExplanation()));
						}
						BHRBenefitConfig bc = new BHRBenefitConfig(in.getConfigId());
						BHRBenefit bb = new BHRBenefit(bc.getBenefit());
						ret.setRequiredDocument(bb.getRequiredDocuments(), ret.getBenefitJoinId());

						@SuppressWarnings("unchecked")
						List<String> excludeRiders = (List) hsu.createCriteria(com.arahant.beans.BenefitRider.class).selectFields(com.arahant.beans.BenefitRider.BENEFIT_RIDER_ID).eq(com.arahant.beans.BenefitRider.HIDDEN, 'Y').eq(com.arahant.beans.BenefitRider.REQUIRED, 'N').list();
						List<com.arahant.beans.BenefitRider> includeRiders = new ArrayList<com.arahant.beans.BenefitRider>();
						HibernateCriteriaUtil<com.arahant.beans.BenefitRider> hcu = hsu.createCriteria(com.arahant.beans.BenefitRider.class).notIn(com.arahant.beans.BenefitRider.BENEFIT_RIDER_ID, excludeRiders).eq(com.arahant.beans.BenefitRider.BASE_BENEFIT_ID, bb.getBenefitId());
						for (com.arahant.beans.BenefitRider br : hcu.list()) {
							//check if they are age eligible
							short min = br.getRiderBenefit().getMinAge();
							short max = br.getRiderBenefit().getMaxAge();
							short age = bpp.getAgeAsOf(qualifyingEventDate);
							if (max > 0 && age > max)
								continue;
							if (min > 0 && age < min)
								continue;
							includeRiders.add(br);
						}
						ret.setBenefitRiders(includeRiders, new BHRBenefitJoin(ret.getBenefitJoinId()), qualifyingEventDate);
					}

					//Check if any other benefits in the category require decline, if they do create a decline benefit join record
					if (!isEmpty(in.getCategoryId()))
						if (!new BHRBenefitCategory(in.getCategoryId()).getAllowsMultipleBenefits()) {
							List<HrBenefit> otherBenefits = hsu.createCriteria(HrBenefit.class).ne(HrBenefit.BENEFITID, in.getBenefitId()).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, in.getCategoryId()).list();
							for (HrBenefit otherBen : otherBenefits) {
								BHRBenefit b = new BHRBenefit(otherBen);
								if (b.getRequiresDecline())
									bpp.noEnrollments(b.getBenefitId(), in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), bz.getAutoApproveDeclines());
							}
						}
				}
			//this is the only difference in this custom web method and the corresponding standard one
			if (adjustDependentCoverageAmounts) {
				hsu.commitTransaction();
				hsu.beginTransaction();

				HrBenefitJoin policyJoin = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, in.getEmployeeId()).eq(HrBenefitJoin.COVERED_PERSON_ID, in.getEmployeeId()).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, in.getConfigId()).first();
				if (policyJoin != null) {
					BHRBenefitJoin bbj = new BHRBenefitJoin(policyJoin);
					for (HrBenefitJoin dbj : bbj.getDependentBenefitJoins()) {
						BHRBenefitJoin bdbj = new BHRBenefitJoin(dbj);
						String relType = bdbj.getRelationship().getRelationshipType();
						if (relType.equals("S"))
							bdbj.setAmountCovered(bbj.getAmountCovered() / 2.0);
						else {
							double newAmount = bbj.getAmountCovered() / 2.0;
							if (newAmount > 5000.0)
								newAmount = 5000.0;
							bdbj.setAmountCovered(newAmount);
						}
						bdbj.update();
					}
				}
			}

			if (!isEmpty(ret.getBenefitJoinId())) {
				BHRBenefitJoin bbj = new BHRBenefitJoin(ret.getBenefitJoinId());
				List<String> l = bbj.autoEnrollRidersAndDependencies(empId);
				String[] sa = new String[l.size()];
				int count = 0;
				for (String s : l) {
					sa[count] = s;
					count++;
				}
				ret.setBenefitJoinIds(sa);
			}

			finishService(ret);


		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitConfigsSimpleHumanaReturn listBenefitConfigsSimpleHumana(/*
			 * @WebParam(name = "in")
			 */final ListBenefitConfigsSimpleHumanaInput in) {
		final ListBenefitConfigsSimpleHumanaReturn ret = new ListBenefitConfigsSimpleHumanaReturn();
		try {
			checkLogin(in);

			String empId;

			if (!isEmpty(in.getEmployeeId()))
				empId = in.getEmployeeId();
			else
				empId = ArahantSession.getCurrentPerson().getPersonId();

			BEmployee bemp = new BEmployee(empId);

			if (in.getAsOfDate() == 0)
				in.setAsOfDate(DateUtils.now());

			BHRBenefit bh = new BHRBenefit(in.getId());

			if ((bh.getEndDate() > 0) && (in.getAsOfDate() > bh.getEndDate()) && (bh.getReplacingBenefit() != null))
				in.setId(bh.getReplacingBenefit().getBenefitId());
			else if ((bh.getEndDate() > 0) && (in.getAsOfDate() > bh.getEndDate()) && (bh.getReplacingBenefit() == null))
				throw new ArahantWarning("You may no longer enroll in this benefit as of " + DateUtils.getDateFormatted(bh.getEndDate()));

			List<WizardConfigurationConfig> wizConfigs = hsu.createCriteria(WizardConfigurationConfig.class).joinTo(WizardConfigurationConfig.WIZARD_CONFIGURATION_BENEFIT).eq(WizardConfigurationBenefit.BENEFIT, new BHRBenefit(in.getId()).getBean()).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, new BWizardConfiguration(in.getWizardConfigurationId()).getBean()).list();

			BHRBenefitConfig[] validConfigsArray;
			List<String> validConfigIds = new ArrayList<String>();
			//does this wizard show inapplicable benefits?
			if (new BWizardConfiguration(in.getWizardConfigurationId()).getShowInapplicable() == 'Y')
				//get all the configs
				validConfigsArray = bemp.getAllValidConfigs(in.getId()); //ret.setItem(bemp.getAllValidConfigs(in.getId()), empId, in.getAsOfDate());
			else
				//only get applicable configs
				validConfigsArray = new BHRBenefit(in.getId()).listValidConfigs(empId, "", in.getId()); //ret.setItem(new BHRBenefit(in.getId()).listValidConfigs(empId, "", in.getId()), empId, in.getAsOfDate());

			for (BHRBenefitConfig c : validConfigsArray)
				validConfigIds.add(c.getId());

			List<WizardConfigurationConfig> keepers = new ArrayList<WizardConfigurationConfig>();
			//only want to keep the configs that match
			for (WizardConfigurationConfig wc : wizConfigs)
				if (validConfigIds.contains(wc.getBenefitConfig().getBenefitConfigId()))
					keepers.add(wc);

			//if there is a benefit dependency met,
			//limit to single config if necessary
			List<BenefitDependency> bdl = BBenefitDependency.getBenefitDependenciesWhereDependent(bh);
			if (bdl.size() > 0) {
				List<BenefitDependency> needBenefits = new ArrayList<BenefitDependency>();
				HrBenefitJoin requiredEnrollment = null;
				boolean limit = false;
				for (BenefitDependency bd : bdl) {
					HrBenefitJoin j = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, bemp.getPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getRequiredBenefit()).first();
					if (j != null) {
						limit = bd.getRequired() == 'Y';
						requiredEnrollment = j;
					} else
						needBenefits.add(bd);
				}
				if (requiredEnrollment != null && limit) {
					HrBenefitConfig config = requiredEnrollment.getHrBenefitConfig();
					HrBenefitConfig match = null;
					for (HrBenefitConfig newConfig : bh.getConfigs())
						if (config.getEmployee() == newConfig.getEmployee()
								&& config.getChildren() == newConfig.getChildren()
								&& config.getSpouseEmployee() == newConfig.getSpouseEmployee()
								&& config.getSpouseNonEmployee() == newConfig.getSpouseNonEmployee()
								&& config.getSpouseEmpOrChildren() == newConfig.getSpouseEmpOrChildren()
								&& config.getSpouseNonEmpOrChildren() == newConfig.getSpouseNonEmpOrChildren()
								&& config.getMaxChildren() == newConfig.getMaxChildren()) {
							match = newConfig;
							break;
						}
					if (match != null) {
						List<WizardConfigurationConfig> removeAgain = new ArrayList<WizardConfigurationConfig>();
						for (WizardConfigurationConfig c : keepers)
							if (c.getBenefitConfig() != match)
								removeAgain.add(c);
						keepers.removeAll(removeAgain);
						ret.setAutoEnrollSingleConfig(true);
					}
				}
			}

			ItemComparator comparator = new ItemComparator();
			Collections.sort(keepers, comparator);
			ret.setItem(keepers, empId, in.getAsOfDate());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetVoluntaryCostsHumanaReturn getVoluntaryCostsHumana(/*
			 * @WebParam(name = "in")
			 */final GetVoluntaryCostsHumanaInput in) {
		final GetVoluntaryCostsHumanaReturn ret = new GetVoluntaryCostsHumanaReturn();
		try {
			checkLogin(in);

			ret.setData(in);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveRiderEnrollmentsReturn saveRiderEnrollments(/*
			 * @WebParam(name = "in")
			 */final SaveRiderEnrollmentsInput in) {
		final SaveRiderEnrollmentsReturn ret = new SaveRiderEnrollmentsReturn();
		try {
			checkLogin(in);
			
			String empId = null;
			List<String> benefitJoinIds = new ArrayList<String>();
			if (in.getRiderEnrollment() != null)
				for (SaveRiderEnrollmentsInputItem enrollment : in.getRiderEnrollment()) {
					empId = isEmpty(enrollment.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : enrollment.getEmployeeId();
					int qualifyingEventDate = enrollment.getQualifyingEventDate();
					if (qualifyingEventDate < DateUtils.now())
						qualifyingEventDate = DateUtils.now();  // can't start a plan befor today

					BPerson bpp = new BPerson();
					BEmployee bemp = new BEmployee(empId);
					BWizardConfiguration bz = bemp.getWizardConfiguration("E");
					//if there is a pending record, get it
					if (bpp.hasPending(empId))
						bpp.loadPending(empId);
					else
						//otherwise, get the Real person record
						bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());
					if (isEmpty(enrollment.getConfigId())) {
						benefitJoinIds.add(bpp.noEnrollments(enrollment.getBenefitId(), enrollment.getChangeReasonId(), qualifyingEventDate, enrollment.getExplanation(), bz.getAutoApproveDeclines()));
						BHRBenefit bcc = new BHRBenefit(enrollment.getBenefitId());
						//System.out.println("(R1) added " + benefitJoinIds.get(benefitJoinIds.size() - 1) + " for decline = " + bcc.getName());
					} else
						if (enrollment.getCoverageAmount() == 0) {
							BHRBenefitConfig bcc = new BHRBenefitConfig(enrollment.getConfigId());
							if (bcc.getBenefitName().equals("Waiver of Premium Optional Benefit - LTR")) {
								int x = 0;
							}
							benefitJoinIds.add(bpp.enrollInConfig(enrollment.getConfigId(), enrollment.getChangeReasonId(), qualifyingEventDate, enrollment.getEnrolleeIds(), 0, true, enrollment.getExplanation()));
							//BHRBenefitConfig bcc = new BHRBenefitConfig(enrollment.getConfigId());
							//System.out.println("(R1) added " + benefitJoinIds.get(benefitJoinIds.size() - 1) + " for match = " + bcc.getBenefitName() + " - " + bcc.getName());
						} else {
							benefitJoinIds.add(bpp.enrollInConfig(enrollment.getConfigId(), enrollment.getChangeReasonId(), qualifyingEventDate, enrollment.getEnrolleeIds(), enrollment.getCoverageAmount(), true, enrollment.getExplanation()));
							BHRBenefitConfig bcc = new BHRBenefitConfig(enrollment.getConfigId());
							//System.out.println("(R2) added " + benefitJoinIds.get(benefitJoinIds.size() - 1) + " for match = " + bcc.getBenefitName() + " - " + bcc.getName());
						}
				}

			List<String> retIds = new ArrayList<String>();
			retIds.addAll(benefitJoinIds);
			for (String s : benefitJoinIds) {
				BHRBenefitJoin bj = new BHRBenefitJoin(s);
				retIds.addAll(bj.autoEnrollRidersAndDependencies(empId));
			}

			String[] ss = new String[retIds.size()];
			int count = 0;
			for (String s : retIds)
				ss[count++] = s;
			ret.setBenefitJoinIds(ss);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetBenefitMetaFidelityReturn getBenefitMetaFidelity(/*
			 * @WebParam(name = "in")
			 */final GetBenefitMetaFidelityInput in) {
		final GetBenefitMetaFidelityReturn ret = new GetBenefitMetaFidelityReturn();
		try {
			checkLogin(in);
			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();

			new BPerson(empId).loadPending(empId);

			BHRBenefit bb = new BHRBenefit(in.getBenefitId());
			WizardConfigurationBenefit wcb = hsu.createCriteria(WizardConfigurationBenefit.class).eq(WizardConfigurationBenefit.BENEFIT, bb.getBean()).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, new BWizardConfiguration(in.getWizardConfigurationId()).getBean()).first();

			if (wcb != null)
				ret.setData(new BWizardConfigurationBenefit(wcb), empId);
			else
				ret.setData2(bb, empId);
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListDependentsAndEmployeeSimpleFidelityReturn listDependentsAndEmployeeSimpleFidelity(/*
			 * @WebParam(name = "in")
			 */final ListDependentsAndEmployeeSimpleFidelityInput in) {
		final ListDependentsAndEmployeeSimpleFidelityReturn ret = new ListDependentsAndEmployeeSimpleFidelityReturn();
		try {
			checkLogin(in);

			BPerson bpp = new BPerson();
			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();
			//if there is a pending record, get it
			if (bpp.hasPending(empId))
				bpp.loadPending(empId);
			else
				//otherwise, get the Real person record
				bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());
			List<HrEmplDependentWizard> lpend = hsu.createCriteria(HrEmplDependentWizard.class).eq(HrEmplDependentWizard.EMPLOYEE_ID, empId).gtOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0).list();

			ret.setItem(lpend, bpp, new BEmployee(empId), in.getBenefitId(), in.getCategoryId());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveConfigAndEnrolleesFidelityReturn saveConfigAndEnrolleesFidelity(/*
			 * @WebParam(name = "in")
			 */final SaveConfigAndEnrolleesFidelityInput in) {
		final SaveConfigAndEnrolleesFidelityReturn ret = new SaveConfigAndEnrolleesFidelityReturn();
		try {
			checkLogin(in);

			String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();
			int qualifyingEventDate = in.getQualifyingEventDate();
			if (qualifyingEventDate < DateUtils.now())
				qualifyingEventDate = DateUtils.now();  // can't start a plan befor today

			if (BProperty.isWizard3())
				//are we actually doing an enrollment or just checking if we would allow the enrollment
				if (!in.getDoEnrollment()) {
					if (!isEmpty(in.getConfigId()) && in.getEnrollee().length != 0) {
						List<BHRBenefitJoin> bjs = new ArrayList<BHRBenefitJoin>();
						for (Enrollee s : in.getEnrollee()) {
							BHRBenefitJoin bj = new BHRBenefitJoin();
							bj.create();
							bj.setBenefitConfigId(in.getConfigId());
							bj.setPayingPersonId(empId);
							bj.setCoveredPersonId(s.getRelationshipId());
							bjs.add(bj);
						}
						if (BHRBenefitJoin.checkPotentialOverCoverage(bjs))
							throw new ArahantWarning("You have selected a coverage level that is excessive given the dependents you are enrolling.");
					}
				} else {

					BPerson bpp = new BPerson();
					BEmployee bemp = new BEmployee(empId);
					bemp.setBenefitWizardStatus(Employee.BENEFIT_WIZARD_STATUS_UNFINALIZED + "");
					BWizardConfiguration bz = bemp.getWizardConfiguration("E");
					bemp.update();
					//if there is a pending record, get it
					if (bpp.hasPending(empId))
						bpp.loadPending(empId);
					else
						//otherwise, get the Real person record
						bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());
					if (isEmpty(in.getConfigId()))
						if (!isEmpty(in.getBenefitId()))
							ret.setBenefitJoinId(bpp.noEnrollments(in.getBenefitId(), in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), bz.getAutoApproveDeclines()));
						else
							ret.setBenefitJoinId(bpp.noEnrollments(null, in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), bz.getAutoApproveDeclines(), in.getCategoryId()));
					else {
						ret.setBenefitJoinId(bpp.enrollInConfigMultipleCoverages(in.getConfigId(), in.getChangeReasonId(), qualifyingEventDate, in.getEnrollee(), in.getCoverageAmount(), true, in.getExplanation()));

						BHRBenefitConfig bc = new BHRBenefitConfig(in.getConfigId());
						BHRBenefit bb = new BHRBenefit(bc.getBenefit());
						ret.setRequiredDocument(bb.getRequiredDocuments(), ret.getBenefitJoinId());
					}

					//Check if any other benefits in the category require decline, if they do create a decline benefit join record
					if (!isEmpty(in.getCategoryId()))
						if (!new BHRBenefitCategory(in.getCategoryId()).getAllowsMultipleBenefits()) {
							List<HrBenefit> otherBenefits = hsu.createCriteria(HrBenefit.class).ne(HrBenefit.BENEFITID, in.getBenefitId()).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, in.getCategoryId()).list();
							for (HrBenefit otherBen : otherBenefits) {
								BHRBenefit b = new BHRBenefit(otherBen);
								if (b.getRequiresDecline())
									bpp.noEnrollments(b.getBenefitId(), in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), bz.getAutoApproveDeclines());
							}
						}
				}
			else
				//are we actually doing an enrollment or just checking if we would allow the enrollment
				if (!in.getDoEnrollment()) {
					if (!isEmpty(in.getConfigId()) && in.getEnrollee().length != 0) {
						List<BHRBenefitJoin> bjs = new ArrayList<BHRBenefitJoin>();
						for (Enrollee s : in.getEnrollee()) {
							BHRBenefitJoin bj = new BHRBenefitJoin();
							bj.create();
							bj.setBenefitConfigId(in.getConfigId());
							bj.setPayingPersonId(empId);
							bj.setCoveredPersonId(s.getRelationshipId());
							bjs.add(bj);
						}
						if (BHRBenefitJoin.checkPotentialOverCoverage(bjs))
							throw new ArahantWarning("You have selected a coverage level that is excessive given the dependents you are enrolling.");
					}
				} else {

					BPerson bpp = new BPerson();
					BEmployee bemp = new BEmployee(empId);
					bemp.setBenefitWizardStatus(Employee.BENEFIT_WIZARD_STATUS_UNFINALIZED + "");
					bemp.update();
					//if there is a pending record, get it
					if (bpp.hasPending(empId))
						bpp.loadPending(empId);
					else
						//otherwise, get the Real person record
						bpp.loadRealPerson(empId); //bpp = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());
					if (isEmpty(in.getConfigId())) {
						if (!isEmpty(in.getBenefitId()))
							ret.setBenefitJoinId(bpp.noEnrollments(in.getBenefitId(), in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), false));
					} else {
						ret.setBenefitJoinId(bpp.enrollInConfigMultipleCoverages(in.getConfigId(), in.getChangeReasonId(), qualifyingEventDate, in.getEnrollee(), in.getCoverageAmount(), true, in.getExplanation()));

						BHRBenefitConfig bc = new BHRBenefitConfig(in.getConfigId());
						BHRBenefit bb = new BHRBenefit(bc.getBenefit());
						ret.setRequiredDocument(bb.getRequiredDocuments(), ret.getBenefitJoinId());
					}

					//Check if any other benefits in the category require decline, if they do create a decline benefit join record
					if (!isEmpty(in.getCategoryId()))
						if (!new BHRBenefitCategory(in.getCategoryId()).getAllowsMultipleBenefits()) {
							List<HrBenefit> otherBenefits = hsu.createCriteria(HrBenefit.class).ne(HrBenefit.BENEFITID, in.getBenefitId()).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, in.getCategoryId()).list();
							for (HrBenefit otherBen : otherBenefits) {
								BHRBenefit b = new BHRBenefit(otherBen);
								if (b.getRequiresDecline())
									bpp.noEnrollments(b.getBenefitId(), in.getChangeReasonId(), qualifyingEventDate, in.getExplanation(), false);
							}
						}
				}

			if (!isEmpty(ret.getBenefitJoinId())) {
				BHRBenefitJoin bbj = new BHRBenefitJoin(ret.getBenefitJoinId());
				List<String> l = bbj.autoEnrollRidersAndDependencies(empId);
				String[] sa = new String[l.size()];
				int count = 0;
				for (String s : l) {
					sa[count] = s;
					count++;
				}
				ret.setBenefitJoinIds(sa);
			}

			finishService(ret);


		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadQuestionAnswersFidelityReturn loadQuestionAnswersFidelity(/*
			 * @WebParam(name = "in")
			 */final LoadQuestionAnswersFidelityInput in) {
		final LoadQuestionAnswersFidelityReturn ret = new LoadQuestionAnswersFidelityReturn();
		try {
			checkLogin(in);
			if (isEmpty(in.getEmployeeId()))
				in.setEmployeeId(hsu.getCurrentPerson().getPersonId());

			int personCount = 1;

			BHRBenefit bb = new BHRBenefit(in.getBenefitId());
			List<HrBenefitConfig> configs = bb.getConfigs();
			boolean spouseOnly = false;
			if (configs.size() == 1) {
				HrBenefitConfig config = configs.get(0);
				if ((config.getSpouseEmployee() == 'Y' || config.getSpouseNonEmployee() == 'Y') && config.getEmployee() == 'N')
					spouseOnly = true;
			}

			if (!spouseOnly)
				if (in.getEnrolleeIds() != null) {
					LoadQuestionAnswersFidelityReturnItem[] arr = new LoadQuestionAnswersFidelityReturnItem[1 + in.getEnrolleeIds().length];
					arr[0] = getAnswersFidelity(new BPerson(in.getEmployeeId()), 0, personCount, null);
					personCount++;
					int count = 1;
					for (String s : in.getEnrolleeIds()) {
						BHREmplDependent dep = new BHREmplDependent(s);
						arr[count++] = getAnswersFidelity(new BPerson(dep.getPerson()), dep.getRelationshipType().equals("S") ? 1 : 2, personCount, dep.getDependentId());
						personCount++;
					}
					ret.setItem(arr);
				} else {
					LoadQuestionAnswersFidelityReturnItem[] arr = new LoadQuestionAnswersFidelityReturnItem[1];
					arr[0] = getAnswersFidelity(new BPerson(in.getEmployeeId()), 0, personCount, null);
					ret.setItem(arr);
					personCount++;
				}
			else //spouse only base plan
			
				if (in.getEnrolleeIds() != null) {
					int addIndex = !isEmpty(in.getSpouseId()) ? 1 : 0;
					addIndex += !isEmpty(in.getEmployeeId()) ? 1 : 0;
					BHREmplDependent spouse = new BHREmplDependent(in.getSpouseId());
					LoadQuestionAnswersFidelityReturnItem[] arr = new LoadQuestionAnswersFidelityReturnItem[addIndex + in.getEnrolleeIds().length];
					arr[0] = getAnswersFidelity(new BPerson(spouse.getPerson()), 1, personCount, spouse.getDependentId());
					personCount++;
					if (!isEmpty(in.getEmployeeId())) {
						arr[1] = getAnswersFidelity(new BPerson(in.getEmployeeId()), 0, personCount, null);
						ret.setItem(arr);
						personCount++;
					}
					int count = addIndex;
					for (String s : in.getEnrolleeIds()) {
						BHREmplDependent dep = new BHREmplDependent(s);
						arr[count++] = getAnswersFidelity(new BPerson(dep.getPerson()), dep.getRelationshipType().equals("S") ? 1 : 2, personCount, dep.getDependentId());
						personCount++;
					}
					ret.setItem(arr);
				} else {
					int addIndex = !isEmpty(in.getSpouseId()) ? 1 : 0;
					addIndex += !isEmpty(in.getEmployeeId()) ? 1 : 0;
					BHREmplDependent spouse = new BHREmplDependent(in.getSpouseId());
					LoadQuestionAnswersFidelityReturnItem[] arr = new LoadQuestionAnswersFidelityReturnItem[addIndex];
					arr[0] = getAnswersFidelity(new BPerson(spouse.getPerson()), 1, personCount, spouse.getDependentId());
					personCount++;
					if (!isEmpty(in.getEmployeeId())) {
						arr[1] = getAnswersFidelity(new BPerson(in.getEmployeeId()), 0, personCount, null);
						ret.setItem(arr);
						personCount++;
					}
				}


			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveQuestionAnswersFidelityReturn saveQuestionAnswersFidelity(/*
			 * @WebParam(name = "in")
			 */final SaveQuestionAnswersFidelityInput in) {
		final SaveQuestionAnswersFidelityReturn ret = new SaveQuestionAnswersFidelityReturn();
		try {
			checkLogin(in);

			for (LoadQuestionAnswersHumanaReturnItem item : in.getInput()) {
				BPerson bp;
				try {
					bp = new BPerson(item.getPersonId());
				} catch (ArahantException arahantException) {
					bp = new BPerson(new BHREmplDependent(item.getPersonId()).getPerson());
				}
				if (!item.getRow1().equals("NA") && !isEmpty(item.getRow1()))
					bp.setTabaccoUse(item.getRow1());
				if (!item.getRow2().equals("NA") && !isEmpty(item.getRow2()))
					bp.setNotMissedFiveDays(item.getRow2().charAt(0));
				if (!item.getRow3().equals("NA") && !isEmpty(item.getRow3()))
					bp.setUnableToPerform(item.getRow3().charAt(0));
				if (!item.getRow4().equals("NA") && !isEmpty(item.getRow4()))
					bp.setHasAids(item.getRow4().charAt(0));
				if (!item.getRow5().equals("NA") && !isEmpty(item.getRow5()))
					bp.setActivelyAtWork(item.getRow5().charAt(0));
				bp.setHasHeartCondition(item.getRow6().charAt(0));
				bp.setHasCancer(item.getRow7().charAt(0));
				bp.setDrugAlcoholAbuse(item.getRow8());
				bp.setHasOtherMedical(item.getRow9());
				bp.setTwoFamilyHeartCond(item.getRow10());
				bp.setTwoFamilyCancer(item.getRow11());
				bp.setTwoFamilyDiabetes(item.getRow12());
				bp.update();

//				ret.setRow1(bp.getTabaccoUse());
//				ret.setRow2("NA");
//				ret.setRow3(bp.getUnableToPerform());
//				ret.setRow4(bp.getHasAids() + "");
//				ret.setRow5(bp.getActivelyAtWork());
//				ret.setRow6(bp.getHasHeartCondition() + "");
//				ret.setRow7(bp.getHasCancer()+ "");
//				ret.setRow8(bp.getDrugAlcoholAbuse());
//				ret.setRow9(bp.getHasOtherMedical());  //todo - digestive disorder or mental disorder
//				ret.setRow10(bp.getTwoFamilyHeartCond()); //todo - prescription meds
//				ret.setRow11(bp.getTwoFamilyCancer());  //todo - xray
//				ret.setRow12(bp.getTwoFamilyDiabetes());
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	private LoadQuestionAnswersFidelityReturnItem getAnswersFidelity(BPerson bp, int type, int count, String dependentId) {
		LoadQuestionAnswersFidelityReturnItem ret = new LoadQuestionAnswersFidelityReturnItem();
		ret.setDataField("person" + count);
		ret.setColumnName(bp.getFirstName() + " " + bp.getLastName());
		ret.setPersonId(bp.getPersonId());
		if (type == 0) // employee
		{
			ret.setRow1(bp.getTabaccoUse());
			ret.setRow2(bp.getNotMissedFiveDays());
			ret.setRow3(bp.getUnableToPerform());
			ret.setRow4(bp.getHasAids() + "");
			ret.setRow5("NA");  //todo - treated by licensed physician last 6 months?
			ret.setRow6(bp.getHasHeartCondition() + "");
			ret.setRow7(bp.getHasCancer() + "");
			ret.setRow8(bp.getDrugAlcoholAbuse());
			ret.setRow9(bp.getHasOtherMedical()); //todo - digestive disorder or mental disorder
			ret.setRow10(bp.getTwoFamilyHeartCond()); //todo - prescription meds
			ret.setRow11(bp.getTwoFamilyCancer()); //todo - xray
			ret.setRow12(bp.getTwoFamilyDiabetes()); //todo - other

		} else if (type == 1) // spouse
		{
			ret.setRow1(bp.getTabaccoUse());
			ret.setRow2("NA");
			ret.setRow3(bp.getUnableToPerform());
			ret.setRow4(bp.getHasAids() + "");
			ret.setRow5(bp.getActivelyAtWork());
			ret.setRow6(bp.getHasHeartCondition() + "");
			ret.setRow7(bp.getHasCancer() + "");
			ret.setRow8(bp.getDrugAlcoholAbuse());
			ret.setRow9(bp.getHasOtherMedical());  //todo - digestive disorder or mental disorder
			ret.setRow10(bp.getTwoFamilyHeartCond()); //todo - prescription meds
			ret.setRow11(bp.getTwoFamilyCancer());  //todo - xray
			ret.setRow12(bp.getTwoFamilyDiabetes()); //todo - other

		} else if (type == 2) // child/other
		{
			ret.setRow1(bp.getTabaccoUse());
			ret.setRow2("NA");
			ret.setRow3(bp.getUnableToPerform());
			ret.setRow4(bp.getHasAids() + "");
			ret.setRow5(bp.getActivelyAtWork());
			ret.setRow6(bp.getHasHeartCondition() + "");
			ret.setRow7(bp.getHasCancer() + "");
			ret.setRow8(bp.getDrugAlcoholAbuse());
			ret.setRow9(bp.getHasOtherMedical());  //todo - digestive disorder or mental disorder
			ret.setRow10(bp.getTwoFamilyHeartCond()); //todo - prescription meds
			ret.setRow11(bp.getTwoFamilyCancer()); //todo - xray
			ret.setRow12(bp.getTwoFamilyDiabetes()); //todo - other
		}
		return ret;

	}

	@WebMethod()
	public GetFidelityCostsReturn getFidelityCosts(/*
			 * @WebParam(name = "in")
			 */final GetFidelityCostsInput in) {
		final GetFidelityCostsReturn ret = new GetFidelityCostsReturn();
		try {
			checkLogin(in);

			ret.setData(in);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetFidelityCostRangeReturn getFidelityCostRange(/*
			 * @WebParam(name = "in")
			 */final GetFidelityCostRangeInput in) {
		final GetFidelityCostRangeReturn ret = new GetFidelityCostRangeReturn();
		try {
			checkLogin(in);

			ret.setData(in);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitConfigsSimpleFidelityReturn listBenefitConfigsSimpleFidelity(/*
			 * @WebParam(name = "in")
			 */final ListBenefitConfigsSimpleFidelityInput in) {
		final ListBenefitConfigsSimpleFidelityReturn ret = new ListBenefitConfigsSimpleFidelityReturn();
		try {
			checkLogin(in);

			String empId;

			if (!isEmpty(in.getEmployeeId()))
				empId = in.getEmployeeId();
			else
				empId = ArahantSession.getCurrentPerson().getPersonId();

			BEmployee bemp = new BEmployee(empId);

			if (in.getAsOfDate() == 0)
				in.setAsOfDate(DateUtils.now());

			BHRBenefit bh = new BHRBenefit(in.getId());

			if ((bh.getEndDate() > 0) && (in.getAsOfDate() > bh.getEndDate()) && (bh.getReplacingBenefit() != null))
				in.setId(bh.getReplacingBenefit().getBenefitId());
			else if ((bh.getEndDate() > 0) && (in.getAsOfDate() > bh.getEndDate()) && (bh.getReplacingBenefit() == null))
				throw new ArahantWarning("You may no longer enroll in this benefit as of " + DateUtils.getDateFormatted(bh.getEndDate()));

			List<WizardConfigurationConfig> wizConfigs = hsu.createCriteria(WizardConfigurationConfig.class).joinTo(WizardConfigurationConfig.WIZARD_CONFIGURATION_BENEFIT).eq(WizardConfigurationBenefit.BENEFIT, bh.getBean()).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, new BWizardConfiguration(in.getWizardConfigurationId()).getBean()).list();
			//if there are no configs, they either weren't set up, or this is a dependent benefit unlocked... so just show them all
			if (wizConfigs.isEmpty())
				ret.setItem2(bh.listConfigs(new String[0], 50), empId, in.getAsOfDate());
			else {

				BHRBenefitConfig[] validConfigsArray;
				List<String> validConfigIds = new ArrayList<String>();
				//does this wizard show inapplicable benefits?
				if (new BWizardConfiguration(in.getWizardConfigurationId()).getShowInapplicable() == 'Y')
					//get all the configs
					validConfigsArray = bemp.getAllValidConfigs(in.getId()); //ret.setItem(bemp.getAllValidConfigs(in.getId()), empId, in.getAsOfDate());
				else
					//only get applicable configs
					validConfigsArray = new BHRBenefit(in.getId()).listValidConfigs(empId, "", in.getId()); //ret.setItem(new BHRBenefit(in.getId()).listValidConfigs(empId, "", in.getId()), empId, in.getAsOfDate());

				for (BHRBenefitConfig c : validConfigsArray)
					validConfigIds.add(c.getId());

				List<WizardConfigurationConfig> keepers = new ArrayList<WizardConfigurationConfig>();
				//only want to keep the configs that match
				for (WizardConfigurationConfig wc : wizConfigs)
					if (validConfigIds.contains(wc.getBenefitConfig().getBenefitConfigId()))
						keepers.add(wc);

				//if there is a benefit dependency met,
				//limit to single config if necessary
				List<BenefitDependency> bdl = BBenefitDependency.getBenefitDependenciesWhereDependent(bh);
				if (bdl.size() > 0) {
					List<BenefitDependency> needBenefits = new ArrayList<BenefitDependency>();
					HrBenefitJoin requiredEnrollment = null;
					boolean limit = false;
					for (BenefitDependency bd : bdl) {
						HrBenefitJoin j = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, bemp.getPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getRequiredBenefit()).first();
						if (j != null) {
							limit = bd.getRequired() == 'Y';
							requiredEnrollment = j;
						} else
							needBenefits.add(bd);
					}
					if (requiredEnrollment != null && limit) {
						HrBenefitConfig config = requiredEnrollment.getHrBenefitConfig();
						HrBenefitConfig match = null;
						for (HrBenefitConfig newConfig : bh.getConfigs())
							if (config.getEmployee() == newConfig.getEmployee()
									&& config.getChildren() == newConfig.getChildren()
									&& config.getSpouseEmployee() == newConfig.getSpouseEmployee()
									&& config.getSpouseNonEmployee() == newConfig.getSpouseNonEmployee()
									&& config.getSpouseEmpOrChildren() == newConfig.getSpouseEmpOrChildren()
									&& config.getSpouseNonEmpOrChildren() == newConfig.getSpouseNonEmpOrChildren()
									&& config.getMaxChildren() == newConfig.getMaxChildren()) {
								match = newConfig;
								break;
							}
						if (match != null) {
							List<WizardConfigurationConfig> removeAgain = new ArrayList<WizardConfigurationConfig>();
							for (WizardConfigurationConfig c : keepers)
								if (c.getBenefitConfig() != match)
									removeAgain.add(c);
							keepers.removeAll(removeAgain);
							ret.setAutoEnrollSingleConfig(true);
						}
					}
				}

				ItemComparator comparator = new ItemComparator();
				Collections.sort(keepers, comparator);
				ret.setItem(keepers, empId, in.getAsOfDate());
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
