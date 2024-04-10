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
package com.arahant.business;

import com.arahant.beans.Employee;
import com.arahant.beans.HrBeneficiary;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.Person;
import com.arahant.beans.PersonChangeRequest;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DOMUtils;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.w3c.dom.*;

public class BPersonChangeRequest extends SimpleBusinessObjectBase<PersonChangeRequest> {

	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final transient ArahantLogger logger = new ArahantLogger(BPersonChangeRequest.class);

	public BPersonChangeRequest(PersonChangeRequest o) {
		bean = o;
	}

	public BPersonChangeRequest() {
	}

	public BPersonChangeRequest(String id) {
		super(id);
	}

	@Override
	public String create() throws ArahantException {
		bean = new PersonChangeRequest();
		bean.setRequestDate(new Date());
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(PersonChangeRequest.class, key);
	}

	public void setType(int type) {
		bean.setRequestType((short) type);
	}

	public int getType() {
		return bean.getRequestType();
	}

	public void setData(String data) {
		bean.setRequestData(data);
	}

	public static boolean currentUserHasPending(short type) {
		return ArahantSession.getHSU().createCriteria(PersonChangeRequest.class)
				.eq(PersonChangeRequest.PERSON, ArahantSession.getHSU().getCurrentPerson())
				.eq(PersonChangeRequest.REQUEST_TYPE, type)
				.exists();
	}

	public void setPerson(Person currentPerson) {
		bean.setPerson(currentPerson);
	}

	private HrBenefitConfig findRightConfig(HrBenefit bene, int count) {
		List<HrBenefitConfig> configs = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
				.eq(HrBenefitConfig.HR_BENEFIT, bene)
				.list();

		if (configs.isEmpty()) {
			//need to make a config
			BHRBenefitConfig config = new BHRBenefitConfig();
			config.create();
			config.setCoversEmployee(true);
			config.setName(bene.getName());
			config.setBenefitId(bene.getBenefitId());
			config.insert();
			return config.getBean();
		}

		if (configs.size() == 1)
			return configs.get(0);

		if (count <= 1)
			//look for employee only
			for (HrBenefitConfig con : configs) {
				if (con.getEmployee() == 'Y'
						&& con.getChildren() == 'N'
						&& con.getSpouseEmployee() == 'N'
						&& con.getSpouseNonEmpOrChildren() == 'N'
						&& con.getSpouseNonEmployee() == 'N'
						&& con.getSpouseEmpOrChildren() == 'N')
					return con;

				if (con.getEmployee() == 'Y'
						&& con.getChildren() == 'N'
						&& con.getSpouseEmployee() == 'Y'
						&& con.getSpouseNonEmpOrChildren() == 'N'
						&& con.getSpouseNonEmployee() == 'Y'
						&& con.getSpouseEmpOrChildren() == 'N')
					return con;

				if (con.getMaxChildren() == 1)
					return con;

			}

		if (count <= 2)
			//look for employee + 1
			for (HrBenefitConfig con : configs) {
				if (con.getMaxChildren() == 1)
					return con;

				if (con.getEmployee() == 'Y'
						&& con.getChildren() == 'N'
						&& con.getSpouseEmployee() == 'Y'
						&& con.getSpouseNonEmpOrChildren() == 'N'
						&& con.getSpouseNonEmployee() == 'Y'
						&& con.getSpouseEmpOrChildren() == 'N')
					return con;
			}


		//Return a family one if (count>2)
		{
			//look for employee only
			for (HrBenefitConfig con : configs)
				if (con.getEmployee() == 'Y'
						&& con.getSpouseNonEmpOrChildren() == 'Y'
						&& con.getSpouseEmpOrChildren() == 'Y'
						&& con.getMaxChildren() != 1)
					return con;

		}

		throw new ArahantException("Couldn't find what benefit to use.  " + bene.getName() + "  " + count);

	}

	@SuppressWarnings("unchecked")
	public static List<String> getWizardBenefits() {
		return null;
	}

	public void applyRequest(int effectiveDate, List<String> benefitIds) throws Exception {
		PersonChangeRequest pcr = bean;

		if (pcr == null)
			return;

		org.w3c.dom.Document doc = DOMUtils.createDocument(pcr.getRequestData());


		Node personNode = DOMUtils.getNode(doc, "GenericWizardData/demographics");

		String elname = DOMUtils.getString(personNode, "lastName").replaceAll("amp;", "").replaceAll("&apos;", "'");
		String efname = DOMUtils.getString(personNode, "firstName");
		String emname = DOMUtils.getString(personNode, "middleName");
		String essn = DOMUtils.getString(personNode, "ssn");

		BEmployee bp = new BEmployee(pcr.getPerson().getPersonId());

		bp.setLastName(elname);
		bp.setFirstName(efname);
		bp.setMiddleName(emname);
		bp.setSsn(essn);
		bp.setDob(DOMUtils.getInt(personNode, "dob"));
		bp.setSex(DOMUtils.getString(personNode, "sex"));
		bp.setHomePhone(DOMUtils.getString(personNode, "homePhone"));
		bp.setWorkPhone(DOMUtils.getString(personNode, "workPhone"));
		bp.setStreet(DOMUtils.getString(personNode, "street1"));
		bp.setStreet2(DOMUtils.getString(personNode, "street2"));
		bp.setCity(DOMUtils.getString(personNode, "city"));
		bp.setState(DOMUtils.getString(personNode, "state").toUpperCase());
		bp.setZip(DOMUtils.getString(personNode, "zip"));

		bp.setSmoker(DOMUtils.getString(personNode, "nonSmokerDiscount").equals("FALSE"));

		HrEmplDependent dep = bp.getSpouse();
		if (dep != null)
			dep.getPerson().setSmoker(DOMUtils.getString(personNode, "spouseNonSmokerDiscount").equals("FALSE") ? 'Y' : 'N');


		NodeList depNodes = DOMUtils.getNodes(doc, "GenericWizardData/dependents/dependent");


		for (int dl = 0; dl < depNodes.getLength(); dl++) {


			Node dependent = depNodes.item(dl);

			String personId = DOMUtils.getString(dependent, "personId");

			BHREmplDependent bdep;
			if (isEmpty(personId)) {
				String ssn = DOMUtils.getString(dependent, "ssn");

				boolean exists = !ssn.isEmpty() && ArahantSession.getHSU().createCriteria(Person.class)
						.eq(Person.SSN, ssn)
						.exists();


				bdep = new BHREmplDependent();

				//make him
				if (exists)
					bdep.create(ArahantSession.getHSU().createCriteria(Person.class)
							.eq(Person.SSN, ssn)
							.first()
							.getPersonId(), bp.getPersonId(), HrEmplDependent.TYPE_OTHER + "");
				else {
					bdep.create();
					bdep.setEmployeeId(bp.getPersonId());
				}

				bdep.setLastName(DOMUtils.getString(dependent, "lastName").replaceAll("amp;", "").replaceAll("&apos;", "'"));
				bdep.setFirstName(DOMUtils.getString(dependent, "firstName"));
				bdep.setMiddleName(DOMUtils.getString(dependent, "middleName"));
				bdep.setSsn(DOMUtils.getString(dependent, "ssn"));

				bdep.setDob(DOMUtils.getInt(dependent, "dob"));
				bdep.setSex(DOMUtils.getString(dependent, "sex"));
				bdep.setHandicap(DOMUtils.getString(dependent, "handicap").equals("TRUE"));
				bdep.setStudent(DOMUtils.getString(dependent, "student").equals("TRUE"));

				String relationship = DOMUtils.getString(dependent, "relationship");
				if (relationship.toLowerCase().indexOf("child") != -1)
					bdep.setRelationshipType(HrEmplDependent.TYPE_CHILD);
				else if (relationship.toLowerCase().indexOf("spouse") != -1) {
					bdep.setRelationshipType(HrEmplDependent.TYPE_SPOUSE);

					//if the person already has a spouse, term that spouse
					HrEmplDependent oldDep = ArahantSession.getHSU().createCriteria(HrEmplDependent.class)
							.geOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0)
							.eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S')
							.joinTo(HrEmplDependent.EMPLOYEE)
							.eq(Employee.PERSONID, bp.getPersonId())
							.first();

					if (oldDep != null) {
						BHREmplDependent oldBDep = new BHREmplDependent(oldDep);
						oldBDep.setInactiveDate(DateUtils.add(DateUtils.now(), -1));
						oldBDep.update();
					}
				} else {
					bdep.setRelationshipType(HrEmplDependent.TYPE_OTHER);
					bdep.setRelationship(relationship);
				}

				bdep.insert();

			} else
				//TODO: had somebody come in here with same ssn as somebody else
				try {
					bdep = new BHREmplDependent(bp.getPersonId(), personId);
					bdep.setLastName(DOMUtils.getString(dependent, "lastName").replaceAll("amp;", "").replaceAll("&apos;", "'"));
					bdep.setFirstName(DOMUtils.getString(dependent, "firstName"));
					bdep.setMiddleName(DOMUtils.getString(dependent, "middleName"));
					bdep.setSsn(DOMUtils.getString(dependent, "ssn"));
					bdep.setDob(DOMUtils.getInt(dependent, "dob"));
					bdep.setSex(DOMUtils.getString(dependent, "sex"));
					bdep.setHandicap(DOMUtils.getString(dependent, "handicap").equals("TRUE"));
					bdep.setStudent(DOMUtils.getString(dependent, "student").equals("TRUE"));
					bdep.update();
				} catch (Exception e) {
					logger.debug(e);
				}


		}


		NodeList beneNodes = DOMUtils.getNodes(doc, "GenericWizardData/benefits/benefit");


		for (String benefitId : benefitIds) {
			HrBenefit bene = ArahantSession.getHSU().get(HrBenefit.class, benefitId);

			for (int loop = 0; loop < beneNodes.getLength(); loop++) {
				Node beneNode = beneNodes.item(loop);

				//see if selected
				String selectedId = DOMUtils.getString(beneNode, "selectedId");
				String pendBeneId = DOMUtils.getString(beneNode, "benefitId");

				//	if (benefitId.equals("00001-0000000012"))
				//		System.out.println(DOMUtils.DOMToString(beneNode));

				if (!isEmpty(pendBeneId) && !pendBeneId.equals(benefitId))
					continue;

				if (isEmpty(pendBeneId)) {
					String pendCatId = DOMUtils.getString(beneNode, "benefitCategoryId");
					if (!pendCatId.equals(bene.getHrBenefitCategory().getBenefitCatId()))
						continue;
				}

				if (isEmpty(selectedId)) {
					//if they had this benefit, terminate it
					List<HrBenefitJoin> bjl = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
							.eq(HrBenefitJoin.PAYING_PERSON, bp.getPerson())
							.eq(HrBenefitJoin.COVERED_PERSON, bp.getPerson())
							.geOrEq(HrBenefitJoin.POLICY_END_DATE, effectiveDate, 0)
							.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
							.eq(HrBenefitConfig.HR_BENEFIT, bene)
							.list();

					for (HrBenefitJoin bj : bjl) {
						BHRBenefitJoin bbj = new BHRBenefitJoin(bj);
						bbj.terminate(effectiveDate);
						bbj.update();
					}
				}

				//			if (selectedId.equals("00001-0000000012"))
				//				System.out.println(DOMUtils.DOMToString(beneNode));

				if (selectedId.equals(benefitId)) {


					//they selected the benefit
					HashSet<String> enrollees = findEnrollees(beneNode);

					enrollees.add(bp.getPersonId());

					HrBenefitConfig config = findRightConfig(bene, enrollees.size());

					//Did they have it before
					HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
							.eq(HrBenefitJoin.PAYING_PERSON, bp.getPerson())
							.eq(HrBenefitJoin.COVERED_PERSON, bp.getPerson())
							.eq(HrBenefitJoin.HR_BENEFIT_CONFIG, config)
							.first();


					if (bj == null) //handle case where they had it before
					{
						//terminate ones that don't have it anymore
						for (HrBenefitJoin delBJ : ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
								.eq(HrBenefitJoin.PAYING_PERSON, bp.getPerson())
								.notIn(HrBenefitJoin.COVERED_PERSON_ID, enrollees)
								.eq(HrBenefitJoin.HR_BENEFIT_CONFIG, config)
								.list())
							new BHRBenefitJoin(delBJ).terminate(effectiveDate);

						//add anybody that isn't there
						HashSet<String> enrollNew = new HashSet<String>();
						enrollNew.addAll(enrollees);
						enrollNew.removeAll(ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
								.selectFields(HrBenefitJoin.COVERED_PERSON_ID)
								.eq(HrBenefitJoin.PAYING_PERSON, bp.getPerson())
								.eq(HrBenefitJoin.HR_BENEFIT_CONFIG, config)
								.list());

						for (String enrollee : enrollNew)
							enroll(enrollee, beneNode, config, effectiveDate, bp);


					} else //handle case where its new to them
					
						for (String enrollee : enrollees)
							enroll(enrollee, beneNode, config, effectiveDate, bp);
				}
			}
		}

	}

	private void enroll(String enrollee, Node beneNode, HrBenefitConfig config, int effectiveDate, BEmployee bp) throws Exception {
		BPerson covered = new BPerson(enrollee);

		Node enrolleeNode = findEnrollee(beneNode, covered.getLastName(),
				covered.getFirstName(), covered.getMiddleName(),
				covered.getSsn());


		//	if (config.getBenefitId().equals("00001-0000000012"))
		//		System.out.println(DOMUtils.DOMToString(beneNode));

		BHRBenefitJoin bbj = new BHRBenefitJoin();
		bbj.create();
		bbj.setBenefitApproved(true);
		bbj.setHrBenefitConfig(config);
		bbj.setPayingPerson(bp.getPerson());
		bbj.setCoveredPerson(covered.getPerson());
		bbj.setPolicyStartDate(effectiveDate);
		bbj.setCoverageStartDate(effectiveDate);
		bbj.setComments(DOMUtils.getString(enrolleeNode, "physician"));
		bbj.setOtherInsuanceIsPrimary(DOMUtils.getString(enrolleeNode, "otherInsurancePrimary").equals("TRUE"));
		bbj.setOtherInsurance(DOMUtils.getString(enrolleeNode, "otherInsurance"));
		bbj.setAmountCovered(DOMUtils.getDouble(beneNode, "selectedAmount"));
		bbj.insert();

		//HashSet<HrBeneficiary> ficiaries=new HashSet<HrBeneficiary>();

		if (config.getHrBenefit().getHasBeneficiaries() == 'Y') {

			NodeList primaryNodes = DOMUtils.getNodes(beneNode, "primaryBeneficiaries/primaryBeneficiary");

			for (int prloop = 0; prloop < primaryNodes.getLength(); prloop++)
				addBeneficiary(bbj, primaryNodes.item(prloop), HrBeneficiary.PRIMARY);


			NodeList secondaryNodes = DOMUtils.getNodes(beneNode, "contingentBeneficiaries/contingentBeneficiary");

			for (int prloop = 0; prloop < secondaryNodes.getLength(); prloop++)
				addBeneficiary(bbj, primaryNodes.item(prloop), HrBeneficiary.CONTINGENT);

		}
	}

	private void addBeneficiary(BHRBenefitJoin bbj, Node primary, char type) throws Exception {
		BHRBeneficiary bf = new BHRBeneficiary();
		bf.create();
		bf.setAssociatedBenefit(bbj);
		bf.setAddress(DOMUtils.getString(primary, "address"));
		bf.setDob(DOMUtils.getInt(primary, "dob"));
		bf.setRelationship(DOMUtils.getString(primary, "relationship"));
		bf.setPercentage(DOMUtils.getInt(primary, "percent"));
		bf.setBeneficiaryType(type);
		bf.setBeneficiary(DOMUtils.getString(primary, "beneficiary"));
		bf.setSsn(DOMUtils.getString(primary, "ssn"));
		bf.insert();
	}

	private HashSet<String> findEnrollees(Node beneNode) throws Exception {
		HashSet<String> ret = new HashSet<String>();
		NodeList enrollNodes = DOMUtils.getNodes(beneNode, "enrollees/enrollee");

		for (int enloop = 0; enloop < enrollNodes.getLength(); enloop++) {
			Node enrollee = enrollNodes.item(enloop);

			String personId = DOMUtils.getString(enrollee, "personId");

			if (isEmpty(personId)) {

				String elname = DOMUtils.getString(enrollee, "lastName");
				String mname = DOMUtils.getString(enrollee, "middleName");
				String fname = DOMUtils.getString(enrollee, "firstName");
				String ssn = DOMUtils.getString(enrollee, "ssn");

				ret.add(ArahantSession.getHSU().createCriteria(Person.class)
						.eq(Person.LNAME, elname)
						.eq(Person.FNAME, fname)
						.eq(Person.MNAME, mname)
						.eq(Person.SSN, ssn)
						.first()
						.getPersonId());
			} else
				ret.add(personId);
		}

		return ret;
	}

	private Node findEnrollee(Node beneNode, String fname, String lname, String mname, String ssn) throws Exception {
		NodeList enrollNodes = DOMUtils.getNodes(beneNode, "enrollees/enrollee");

		for (int enloop = 0; enloop < enrollNodes.getLength(); enloop++) {
			Node enrollee = enrollNodes.item(enloop);
			String elname = DOMUtils.getString(enrollee, "lastName");
			if (lname.equals(elname)
					&& mname.equals(DOMUtils.getString(enrollee, "middleName"))
					&& fname.equals(DOMUtils.getString(enrollee, "firstName"))
					&& ssn.equals(DOMUtils.getString(enrollee, "ssn")))
				return enrollee;

			if (lname.equals(elname)
					&& mname.equals(DOMUtils.getString(enrollee, "middleName"))
					&& fname.equals(DOMUtils.getString(enrollee, "firstName")) //	&&
					//ssn.equals(DOMUtils.getString(enrollee, "ssn"))
					)
				return enrollee;

			if (lname.equals(elname)
					&& //	mname.equals(DOMUtils.getString(enrollee, "middleName"))
					//		&&
					fname.equals(DOMUtils.getString(enrollee, "firstName"))
					&& ssn.equals(DOMUtils.getString(enrollee, "ssn")))
				return enrollee;

			if (lname.equals(elname)
					&& //	mname.equals(DOMUtils.getString(enrollee, "middleName"))
					//		&&
					fname.equals(DOMUtils.getString(enrollee, "firstName")) //		&&
					//	ssn.equals(DOMUtils.getString(enrollee, "ssn"))
					)
				return enrollee;
		}

		return null;
	}

	public static BPersonChangeRequest[] makeArray(List<PersonChangeRequest> l) {
		BPersonChangeRequest[] ret = new BPersonChangeRequest[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BPersonChangeRequest(l.get(loop));

		return ret;
	}

	public static void main(String args[]) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		try {
			hsu.beginTransaction();
			//
			hsu.setCurrentPersonToArahant();

			List<String> benefitIds = getWizardBenefits();

			hsu.dontAIIntegrate();

			Date old = new Date();
			int count = 0;
			for (BPersonChangeRequest pcr : makeArray(hsu.createCriteria(PersonChangeRequest.class)
					.orderBy(PersonChangeRequest.REQUEST_DATE)
					.le(PersonChangeRequest.REQUEST_DATE, DateUtils.getDate(20100101))
					//		.joinTo(PersonChangeRequest.PERSON)
					//		.eq(Person.PERSONID,"00001-0000000489")
					.list())) {
				++count;

				if (count % 25 == 0) {
					hsu.commitTransaction();
					hsu.beginTransaction();


				}

				try {
					pcr.applyRequest(20100101, benefitIds);
				} catch (Exception e) {
					logger.error(e);
				}
				Date now = new Date();
				long dif = now.getTime() - old.getTime();
				dif = dif / 1000;
				old = now;
				System.out.println(count + " " + dif);
			}

			hsu.commitTransaction();

		} catch (Exception e) {
			logger.error(e);
			hsu.rollbackTransaction();
		}
	}


	/*
	 public static void main (String args[])
	 {
	 HibernateSessionUtil hsu=ArahantSession.getHSU();
	 try
	 {
	 hsu.beginTransaction();
	 //	hsu.dontAIIntegrate();
	 hsu.setCurrentPersonToArahant();


	 HashSet<String> pset=new HashSet<String>();

	 pset.addAll((List)hsu.createCriteria(Person.class)
	 .selectFields(Person.PERSONID)
	 .joinTo(Person.CHANGE_REQUESTS)
	 .list());

	 Date old = new Date();
	 int count=0;
	 for (String id : pset)
	 {
	 ++count;

	 if (count%25==0)
	 {
	 hsu.commitTransaction();
	 hsu.beginTransaction();


	 }

	 try
	 {
	 createRequestFromCurrent(id, 20100101, "00001-0000000001");
	 }
	 catch (Exception e)
	 {
	 e.printStackTrace();
	 }
	 Date now=new Date();
	 long dif=now.getTime()-old.getTime();
	 dif=dif/1000;
	 old=now;
	 System.out.println(count +" "+dif);
	 }

	 hsu.commitTransaction();

	 }
	 catch (Exception e)
	 {
	 e.printStackTrace();
	 hsu.rollbackTransaction();
	 }
	 }
	 */
	public static void createRequestFromCurrent(String personId, int date, String reasonId) {
		BPersonChangeRequest pcr = new BPersonChangeRequest();
		pcr.create();
		BEmployee bemp = new BEmployee(personId);
		bemp.deleteExpiredBenefits();
		String xml = tag("GenericWizardData",
				tag("qualifyingEventDate", 0)
				+ tag("benefitChangeReasonId", reasonId) + //TODO: change sometime to have them select it
				tag("demographics",
				etag("firstName", bemp.getFirstName())
				+ etag("middleName", bemp.getMiddleName())
				+ etag("lastName", bemp.getLastName())
				+ tag("dob", bemp.getDob())
				+ tag("ssn", bemp.getSsn())
				+ tag("sex", bemp.getSex())
				+ tag("homePhone", bemp.getHomePhone())
				+ tag("workPhone", bemp.getWorkPhone())
				+ etag("email", bemp.getPersonalEmail())
				+ etag("street1", bemp.getStreet())
				+ etag("street2", bemp.getStreet2())
				+ etag("city", bemp.getCity())
				+ etag("state", bemp.getState())
				+ etag("zip", bemp.getZip())
				+ tag("nonSmokerDiscount", bemp.getSmoker() ? "FALSE" : "TRUE")
				+ tag("spouseNonSmokerDiscount", bemp.getSpouseSmoker() ? "FALSE" : "TRUE")
				+ tag("mobilePhone", bemp.getMobilePhone()))
				+ tag("dependents", getDependents(bemp))
				+ tag("benefits", getBenefits(bemp, date)));
		pcr.setData(xml);
		pcr.setPerson(bemp.getPerson());
		pcr.setType(PersonChangeRequest.TYPE_GENERICWIZARD);
		pcr.insert();
	}

	private static String getBenefits(BEmployee bemp, int date) {
		String ret = "";

		List<String> benefitIds = getWizardBenefits();

		for (String id : benefitIds) {
			BHRBenefit bene = new BHRBenefit(id);
			ret += tag("benefit",
					tag("benefitCategoryId", bene.getBenefitCategoryId())
					+ tag("benefitId", bene.getBenefitId())
					+ getBenefitDetails(bemp, bene, date));
		}
		return ret;
	}

	private static String getBenefitDetails(BEmployee bemp, BHRBenefit bene, int date) {

		String ret = "";
		//do they have this benefit?
		HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
				.eq(HrBenefitJoin.PAYING_PERSON, bemp.getPerson())
				.eq(HrBenefitJoin.COVERED_PERSON, bemp.getPerson())
				.orderByDesc(HrBenefitJoin.POLICY_START_DATE)
				.dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, date)
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.eq(HrBenefit.BENEFITID, bene.getBenefitId())
				.first();

		if (bj == null)
			return "<selectedId></selectedId><selectedAmount>0.0</selectedAmount><primaryBeneficiaries></primaryBeneficiaries><contingentBeneficiaries></contingentBeneficiaries><enrollees></enrollees><order>0</order>";

		ret += tag("selectedId", bene.getBenefitId());
		ret += tag("selectedAmount", bj.getAmountCovered() + "");
		ret += tag("primaryBeneficiaries", getPrimaryBeneficiaries(bj));
		ret += tag("contingentBeneficiaries", getContingentBeneficiaries(bj));
		ret += tag("enrollees", getEnrollees(bj));


		return ret;
	}

	private static String getEnrollees(HrBenefitJoin bj) {
		BHRBenefitJoin bbj = new BHRBenefitJoin(bj);
		String ret = "";

		ret += buildEnrollee(bbj);

		for (HrBenefitJoin dpbj : bbj.getActiveDependentBenefitJoins())
			ret += buildEnrollee(new BHRBenefitJoin(dpbj));

		return ret;
	}

	private static String buildEnrollee(BHRBenefitJoin bbj) {
		BPerson bp = new BPerson(bbj.getBean().getCoveredPerson());
		return tag("enrollee",
				tag("personId", bbj.getCoveredPersonId())
				+ etag("ssn", bp.getSsn())
				+ etag("firstName", bp.getFirstName())
				+ etag("middleName", bp.getMiddleName())
				+ etag("lastName", bp.getLastName())
				+ etag("physician", bbj.getComments())
				+ etag("otherInsurance", bbj.getOtherInsurance())
				+ tag("otherInsurancePrimary", bbj.getOtherInsurancePrimary() ? "TRUE" : "FALSE"));
	}

	private static String getContingentBeneficiaries(HrBenefitJoin bj) {
		BHRBenefitJoin bbj = new BHRBenefitJoin(bj);

		String ret = "";

		for (BHRBeneficiary b : BHRBeneficiary.listPrimaries(bbj))
			ret += tag("contingentBeneficiary",
					etag("ssn", b.getSsn())
					+ tag("dob", b.getDob())
					+ etag("relationship", b.getRelationship())
					+ tag("percent", b.getPercentage())
					+ etag("address", b.getAddress())
					+ etag("beneficiary", b.getBeneficiary())
					+ etag("beneficiaryId", b.getBeneficiaryId()));

		return ret;
	}

	private static String getPrimaryBeneficiaries(HrBenefitJoin bj) {
		BHRBenefitJoin bbj = new BHRBenefitJoin(bj);

		String ret = "";

		for (BHRBeneficiary b : BHRBeneficiary.listPrimaries(bbj))
			ret += tag("primaryBeneficiary",
					etag("ssn", b.getSsn())
					+ tag("dob", b.getDob())
					+ etag("relationship", b.getRelationship())
					+ tag("percent", b.getPercentage())
					+ etag("address", b.getAddress())
					+ etag("beneficiary", b.getBeneficiary())
					+ etag("beneficiaryId", b.getBeneficiaryId()));

		return ret;
	}

	private static String getDependents(BEmployee bemp) {
		String ret = "";

		for (BHREmplDependent dep : bemp.getDependents())
			ret += tag("dependent",
					etag("firstName", dep.getFirstName())
					+ etag("middleName", dep.getMiddleName())
					+ etag("lastName", dep.getLastName())
					+ etag("relationship", dep.getTextRelationship())
					+ etag("sex", dep.getSex())
					+ etag("ssn", dep.getSsn())
					+ tag("dob", dep.getDob())
					+ tag("student", dep.getStudent() ? "TRUE" : "FALSE")
					+ tag("disabled", dep.getHandicap() ? "TRUE" : "FALSE")
					+ tag("personId", dep.getPersonId()));
		return ret;
	}

	private static String tag(String tagName, String value) {
		return "<" + tagName + ">" + value + "</" + tagName + ">\n";
	}

	private static String etag(String tagName, String value) {
		return "<" + tagName + ">" + DOMUtils.escapeText(value) + "</" + tagName + ">\n";
	}

	private static String tag(String tagName, int value) {
		return "<" + tagName + ">" + value + "</" + tagName + ">\n";
	}

	public static void applyLatestRequest(String personId, int date) throws Exception {
		PersonChangeRequest pcr = ArahantSession.getHSU().createCriteria(PersonChangeRequest.class)
				.orderByDesc(PersonChangeRequest.REQUEST_DATE)
				.joinTo(PersonChangeRequest.PERSON)
				.eq(Person.PERSONID, personId)
				.first();

		if (pcr == null)
			throw new ArahantWarning("No change requests found for this person.");

		new BPersonChangeRequest(pcr).applyRequest(date, BPersonChangeRequest.getWizardBenefits());

	}
}
