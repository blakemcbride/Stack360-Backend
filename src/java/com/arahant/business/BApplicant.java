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
 *
 *  Created on Feb 15, 2007
*/

package com.arahant.business;

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.*;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.*;

public class BApplicant extends BPerson implements IDBFunctions {

	private static final ArahantLogger logger = new ArahantLogger(BApplicant.class);

	public Applicant applicant;

	public static void delete(String[] ids) throws SQLException {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		final Connection db = KissConnection.get();
		for (String id : ids){	
			BApplicant app = new BApplicant(id);
			Person per = app.getPerson();
			String personId = per.getPersonId();

			BPerson bper = new BPerson(per);
			if (bper.isEmployee())
				throw new ArahantWarning("Person is an employee.  Cannot delete.");

			HibernateCriteriaUtil<ApplicantApplication> crit = hsu.createCriteria(ApplicantApplication.class);
			crit.eq(ApplicantApplication.APPLICANT, new BApplicant(personId).getPerson());
			List<ApplicantApplication> lst = crit.list();
			for (ApplicantApplication rec : lst)
				db.execute("delete from applicant_application where person_id = ?", personId);
			db.execute("delete from person_form where person_id = ?", personId);
			db.execute("delete from previous_employment where person_id = ?", personId);
			db.execute("delete from personal_reference where person_id = ?", personId);
			db.execute("delete from education where person_id = ?", personId);
			db.execute("delete from applicant_answer where person_id = ?", personId);
			db.execute("delete from applicant_contact where person_id = ?", personId);
			db.execute("delete from hr_emergency_contact where person_id = ?", personId);
			db.execute("delete from login_log where person_id = ?", personId);
			db.execute("delete from prophet_login where person_id = ?", personId);
			db.execute("delete from applicant_application where person_id = ?", personId);
			db.execute("delete from applicant where person_id = ?", personId);
			db.execute("delete from change_log where change_person_id = ? or person_id = ?", personId, personId);
			db.execute("delete from phone where person_join = ?", personId);
			db.execute("delete from address where person_join = ?", personId);
			db.execute("delete from person where person_id = ?", personId);
		}
	}

	public String getRaceName()
	{
		return applicant.getHrEeoRace().getName();
	}

	public String getRaceId()
	{
		if (applicant.getHrEeoRace()==null)
			return "";
		
		return applicant.getHrEeoRace().getEeoId();
	}

	public void setRaceId(String key) {
		applicant.setHrEeoRace(ArahantSession.getHSU().get(HrEeoRace.class, key));
		//currently EEO Race is constrained to employee, not person
		//needs to be person for applicant
	}

	public interface ISearchApplicantsInputAnswer
	{
		public String getId();
		public String getListAnswerId();
		public String getTextAnswer();
		public double getTriAnswer();
		public double getNumericAnswer();
		public int getDateAnswer();
		public int getComparator();
	}


	public static BSearchOutput<BApplicant> makeSearchOutputApplicant(BSearchMetaInput searchMeta, HibernateCriteriaUtil<Applicant> hcu)
	{
		BSearchOutput<BApplicant> ret = new BSearchOutput<>(searchMeta);
		
		HibernateScrollUtil<Applicant> scr=hcu.getPage(searchMeta);
		
//		if (searchMeta.isUsingPaging())
			ret.setTotalItemsPaging(hcu.countNoOrder());
			
		// set output
		ret.setItems(BApplicant.makeArray(scr));
		
		return ret;
	}
	

	public static BApplicant[] search(String applicantSource, String applicantStatus, String applicationSource, 
			String applicationStatus, String firstName, String lastName, String jobTypeId, String positionId, 
			int searchType, int cap, boolean includeEmployees) {
		
		/*
		 * - if searchType is 2:
                - searching applicants that have applicant_application
                - possible fields are: positionId, applicationStatus, applicationSource, lastName, lastNameSearchType, firstName, firstNameSearchType, applicantStatus, applicantSource
            - if searchType is 3:
                - searching applicants that have application_interest
                - possible fields are: jobTypeId, lastName, lastNameSearchType, firstName, firstNameSearchType, applicantStatus, applicantSource
            - if searchType is 1
                - searching all applicants
                - possible fields are: lastName, lastNameSearchType, firstName, firstNameSearchType, applicantStatus, applicantSource
		 * */
		
		HibernateCriteriaUtil<Applicant> hcu=ArahantSession.getHSU().createCriteria(Applicant.class)
				.setMaxResults(cap);

		HibernateCriteriaUtil<Applicant> phcu=hcu.joinTo(Applicant.PERSON)
			.orderBy(Person.LNAME)
			.orderBy(Person.FNAME)
			.like(Person.LNAME, lastName)
			.like(Person.FNAME, firstName);

		if (!includeEmployees)
			hcu.isNotEmployee();
		
		if (!isEmpty(applicantSource))
			hcu.joinTo(Applicant.APPLICANT_SOURCE).eq(ApplicantSource.ID, applicantSource);
		
		if (!isEmpty(applicantStatus))
			hcu.joinTo(Applicant.APPLICANT_STATUS).eq(ApplicantStatus.ID, applicantStatus);
			
		if (searchType==2){
			HibernateCriteriaUtil<Applicant> appHcu=hcu.joinTo(Applicant.APPLICATIONS);
			
			if (!isEmpty(applicationStatus))
				appHcu.joinTo(ApplicantApplication.STATUS).eq(ApplicantAppStatus.ID,applicationStatus);
			if (!isEmpty(applicationSource))
				appHcu.joinTo(ApplicantApplication.SOURCE).eq(ApplicantSource.ID, applicationSource);
				
		}
		
		return makeArray(hcu.list());
	}
	
	public static BApplicant[] search (String fname, String lname, String ssn, int cap, boolean includeEmployees)
	{
		HibernateCriteriaUtil <Applicant> hcu=ArahantSession.getHSU().createCriteria(Applicant.class)
			.setMaxResults(cap);

		HibernateCriteriaUtil phcu=hcu.joinTo(Applicant.PERSON)
			.like(Person.LNAME, lname)
			.like(Person.FNAME, fname)
			.orderBy(Person.LNAME)
			.orderBy(Person.FNAME);

		if(!includeEmployees)
			hcu.isNotEmployee();
			
		
		if (!isEmpty(ssn))
			phcu.eq(Person.SSN, ssn);
		
		return makeArray(hcu.list());
	}

	private static BApplicant[] makeArray(HibernateScrollUtil<Applicant> scr) {
		List<Applicant> l = new ArrayList<Applicant>();
		while(scr.next())
			l.add(scr.get());

		return BApplicant.makeArray(l);
	}

	static BApplicant[] makeArray(List<Applicant> l) {
		BApplicant [] ret=new BApplicant[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BApplicant(l.get(loop));
		return ret;
	}

	public BApplicant() {
	}
	
	public BApplicant(final String key) throws ArahantException {
		internalLoad(key);
	}

	public BApplicant(final Person person, final String groupId) throws ArahantException {
		super(person,groupId);
		applicant=ArahantSession.getHSU().createCriteria(Applicant.class).eq(Applicant.PERSON, person).first();
	}

	public BApplicant(final String personId, final String groupId) throws ArahantException {
		super(ArahantSession.getHSU().get(Person.class,personId),groupId);
		applicant=ArahantSession.getHSU().createCriteria(Applicant.class).eq(Applicant.PERSON, person).first();
	}

	/**
	 * @param contact
	 * @throws ArahantException 
	 */
	public BApplicant(final Applicant contact) throws ArahantException {
		super(contact.getPerson());
		applicant=contact;
	}


	public BApplicant(final Person contact) throws ArahantException {
		super(contact);
		applicant=ArahantSession.getHSU().createCriteria(Applicant.class).eq(Applicant.PERSON, person).first();
	}

	@Override
	public void assignToOrgGroup(final String orgGroupId, final boolean isPrimary) throws ArahantException {
	
		super.assignToOrgGroup(orgGroupId, isPrimary);
		if (isPrimary){
			//if this one is primary, remove any other primaries for that org group

			for (OrgGroupAssociation oga : ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class)
					.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y')
					.joinTo(OrgGroupAssociation.ORGGROUP)
					.eq(OrgGroup.ORGGROUPID, orgGroupId)
					.list()) {
				if (oga.getPerson().equals(person))
					continue;
				oga.setPrimaryIndicator('N');
				ArahantSession.getHSU().saveOrUpdate(oga);
			}
		}
	}

	public void clearApplicationAnswers() {
		ArahantSession.getHSU().createCriteria(ApplicantAnswer.class)
			.eq(ApplicantAnswer.APPLICANT, applicant)
			.delete();
	}
	
	@Override
	public String create() throws ArahantException{
		super.create();

		applicant=new Applicant();
		applicant.setPerson(person);
		person.setOrgGroupType(APPLICANT_TYPE);
		
		// create applicant contact specific stuff
		this.createOther();		
		
		return getPersonId();
	}

	public void deleteApplicationsNotIn(List<String> ids) {
		for (ApplicantApplication aa : ArahantSession.getHSU().createCriteria(ApplicantApplication.class)
			.eq(ApplicantApplication.APPLICANT, applicant)
			.notIn(ApplicantApplication.ID, ids)
			.list())
			new BApplication(aa).delete();
	}

	public void deleteContactsNotIn(List<String> contactIds) {
		ArahantSession.getHSU().createCriteria(ApplicantContact.class)
			.eq(ApplicantContact.APPLICANT, applicant)
			.isNull(ApplicantContact.APPLICATION)
			.notIn(ApplicantContact.ID, contactIds)
			.delete();
	}


	public BApplicantContact[] getApplicantContacts() {
		return BApplicantContact.makeArray(ArahantSession.getHSU().createCriteria(ApplicantContact.class)
				.orderByDesc(ApplicantContact.DATE)
				.orderByDesc(ApplicantContact.TIME)
				.eq(ApplicantContact.APPLICANT, applicant)
				.isNull(ApplicantContact.APPLICATION)
				.list());
	}

	public BApplicantQuestion[] getApplicantQuestions() {
		return BApplicantQuestion.makeArray(ArahantSession.getHSU().createCriteria(ApplicantQuestion.class)
			.orderBy(ApplicantQuestion.SEQ)
			.list());
	}

	public BApplicantQuestion[] getApplicantAnsweredQuestions() {
		/*
		HibernateCriteriaUtil<ApplicantQuestion> hcu1=ArahantSession.getHSU()
				.createCriteria(ApplicantQuestion.class)
				.orderBy(ApplicantQuestion.SEQ);
		
		hcu1.isNull(ApplicantQuestion.JOB_TYPE);
		
		hcu1.geOrEq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.now(),0);
		
		hcu1.joinTo(ApplicantQuestion.ANSWERS).eq(ApplicantAnswer.APPLICANT,applicant);
		*/
		HibernateCriteriaUtil<ApplicantQuestion> hcu=ArahantSession.getHSU()
				.createCriteria(ApplicantQuestion.class)
				.orderBy(ApplicantQuestion.SEQ);
		
		hcu.geOrEq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.now(),0);
		
		hcu.joinTo(ApplicantQuestion.ANSWERS).eq(ApplicantAnswer.APPLICANT,applicant);
			
	//	List<ApplicantQuestion> l=hcu1.list();
		List<ApplicantQuestion> l=new ArrayList<>();
		l.addAll(hcu.list());

		List<ApplicantAnswer> ansList=ArahantSession.getHSU()
			.createCriteria(ApplicantAnswer.class)
			.eq(ApplicantAnswer.APPLICANT, applicant)
			.joinTo(ApplicantAnswer.QUESTION)
			.ltAndNeq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0)
			.orderBy(ApplicantQuestion.SEQ)
			.list();

		for (ApplicantAnswer ans : ansList)
			l.add(ans.getApplicantQuestion());

		return BApplicantQuestion.makeArray(l);
	}

	public String getApplicantSource() {
		try{
			return applicant.getApplicantSource().getDescription();
		}catch (Exception e){
			return "";
		}
	}

	public String getApplicantSourceId() {
		try{
			return applicant.getApplicantSource().getApplicantSourceId();
		}catch (Exception e){
			return "";
		}
	}

	public String getApplicantStatus() {
		try{
			return applicant.getApplicantStatus().getName();
		}catch (Exception e){
			return "";
		}
	}

	public String getApplicantStatusId() {
		try{
			return applicant.getApplicantStatus().getApplicantStatusId();
		}catch (Exception e){
			return "";
		}
	}

	public BApplication[] getApplications() {
		List<ApplicantApplication> aps = ArahantSession.getHSU().createCriteria(ApplicantApplication.class)
				.eq(ApplicantApplication.APPLICANT, applicant)
				//.orderByDesc(ApplicantApplication.DATE)
				.list();
		return BApplication.makeArray(aps);
	}

	public String getComments() {
		return applicant.getComments();
	}

	public String getCompanyId() {
		try{
			return applicant.getPerson().getCompanyBase().getOrgGroupId();
			//return applicant.getApplications().iterator().next().getPosition().getOrgGroup().getOwningCompany().getOrgGroupId();
		}catch (Exception e){
			return "";
		}
	}

	
	public int getFirstAwareDate() {
		return applicant.getFirstAwareDate();
	}

	public boolean hasPendingAnswer(String questionId) {
		
		for (IDBFunctions pend : pendingInserts){
			if (pend instanceof BApplicantAnswer){
				BApplicantAnswer bans=(BApplicantAnswer)pend;
				if (bans.getQuestionId().equals(questionId))
					return true;
			}
		}
		for (IDBFunctions pend : pendingUpdates){
			if (pend instanceof BApplicantAnswer){
				BApplicantAnswer bans=(BApplicantAnswer)pend;
				if (bans.getQuestionId().equals(questionId))
					return true;
			}
		}
		return false;
	}

	
	public void setApplicantSourceId(String applicantSourceId) {
		applicant.setApplicantSource(ArahantSession.getHSU().get(ApplicantSource.class, applicantSourceId));
	}

	public void setApplicantStatusId(String applicantStatusId) {
		applicant.setApplicantStatus(ArahantSession.getHSU().get(ApplicantStatus.class,applicantStatusId));
	}

	public void setComments(String comments) {
		applicant.setComments(comments);
	}

	public void setFirstAwareDate(int firstAwareDate) {
		applicant.setFirstAwareDate(firstAwareDate);
	}

	public int getDateAvailable() {
		return applicant.getDateAvailable();
	}

	public void setDateAvailable(int dateAvailable) {
		applicant.setDateAvailable(dateAvailable);
	}

	public int getDesiredSalary() {
		return applicant.getDesiredSalary();
	}

	public void setDesiredSalary(int desiredSalary) {
		applicant.setDesiredSalary(desiredSalary);
	}

	public String getReferredBy() {
		return applicant.getReferredBy();
	}

	public void setReferredBy(String referredBy) {
		applicant.setReferredBy(referredBy);
	}

	public Short getYearsExperience() {
		return applicant.getYearsExperience();
	}

	public void setYearsExperience(Short yearsExperience) {
		applicant.setYearsExperience(yearsExperience);
	}

	public Character getDayShift() {
		return applicant.getDayShift();
	}

	public void setDayShift(Character dayShift) {
		applicant.setDayShift(dayShift);
	}

	public Character getNightShift() {
		return applicant.getNightShift();
	}

	public void setNightShift(Character nightShift) {
		applicant.setNightShift(nightShift);
	}

	public Character getVeteran() {
		return applicant.getVeteran();
	}

	public void setVeteran(Character veteran) {
		applicant.setVeteran(veteran);
	}

	public String getSignature() {
		return applicant.getSignature();
	}

	public void setSignature(String signature) {
		applicant.setSignature(signature);
	}

	public Date getWhenSigned() {
		return applicant.getWhenSigned();
	}

    public void setWhenSigned(Date whenSigned) {
		applicant.setWhenSigned(whenSigned);
	}

	public char getTravelPersonal() {
		return applicant.getTravelPersonal();
	}

	public void setTravelPersonal(char travelPersonal) {
		applicant.setTravelPersonal(travelPersonal);
	}

	public char getTravelFriend() {
		return applicant.getTravelFriend();
	}

	public void setTravelFriend(char travelFriend) {
		applicant.setTravelFriend(travelFriend);
	}

	public char getTravelPublic() {
		return applicant.getTravelPublic();
	}

	public void setTravelPublic(char travelPublic) {
		applicant.setTravelPublic(travelPublic);
	}

	public char getTravelUnknown() {
		return applicant.getTravelUnknown();
	}

	public void setTravelUnknown(char travelUnknown) {
		applicant.setTravelUnknown(travelUnknown);
	}

	public char getBackgroundCheckAuthorized() {
		return applicant.getBackgroundCheckAuthorized();
	}

	public void setBackgroundCheckAuthorized(char backgroundCheckAuthorized) {
		applicant.setBackgroundCheckAuthorized(backgroundCheckAuthorized);
	}

	public char getAgrees() {
		return applicant.getAgrees();
	}

	public void setAgrees(char agrees) {
		applicant.setAgrees(agrees);
	}

	public String getAgreementName() {
		return applicant.getAgreementName();
	}

	public void setAgreementName(String agreementName) {
		applicant.setAgreementName(agreementName);
	}

	public Date getAgreementDate() {
		return applicant.getAgreementDate();
	}

	public void setAgreementDate(Date agreementDate) {
		applicant.setAgreementDate(agreementDate);
	}

	@Override
	protected void createOther() throws ArahantException
	{
		super.createOther();

		// applicant specific creation stuff goes here
	}
	
	private void internalLoad(final String key) throws ArahantException
	{
		logger.debug("Loading "+key);
		super.load(key);
		applicant=ArahantSession.getHSU().get(Applicant.class, key);
	}
	
	@Override
	public void load(final String key) throws ArahantException
	{
		internalLoad(key);
	}
	
	@Override
	public void insert() throws ArahantException
	{
		Set<IDBFunctions> pendIs=new HashSet<IDBFunctions>();
		pendIs.addAll(pendingInserts);
		Set<IDBFunctions> pendUs=new HashSet<IDBFunctions>();
		pendUs.addAll(pendingUpdates);

		pendingInserts.clear();
		pendingUpdates.clear();

		super.insert();
		ArahantSession.getHSU().saveOrUpdate(applicant);

		for (IDBFunctions i : pendIs)
			i.insert();
		for (IDBFunctions i : pendUs)
			i.update();

	}

	@Override
	public void update() throws ArahantException
	{
		super.update();
		ArahantSession.getHSU().saveOrUpdate(applicant);
	}

	@Override
	public void delete() throws ArahantDeleteException
	{
		clearApplicationAnswers();
		//hsu.delete(applicant);
		for (ApplicantApplication app : applicant.getApplications())
			new BApplication(app).delete();

		ArahantSession.getHSU().delete(applicant);
		super.delete();
	}
	
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BApplicant(element).delete();
	}

	public static BApplicant[] list(final HibernateSessionUtil hsu, final String groupId) throws ArahantException {
		final List<Applicant> plist =hsu.createCriteria(Applicant.class)
		.orderBy(Person.LNAME)
		.orderBy(Person.FNAME)
		.joinTo(Person.ORGGROUPASSOCIATIONS)
		.joinTo(OrgGroupAssociation.ORGGROUP)
		.eq(OrgGroup.ORGGROUPID, groupId)
		.list();
	
		return makeAray(plist, groupId);
	}
	
	static BApplicant[] makeAray(final List<Applicant> plist, final String groupId) throws ArahantException
	{
		final BApplicant[]ret=new BApplicant[plist.size()];
		
		for (int loop=0;loop<plist.size();loop++)
			ret[loop]=new BApplicant(plist.get(loop).getPerson(),groupId);
		return ret;
	}

	public static BApplicant[] search(final HibernateSessionUtil hsu, final String firstName, final String lastName, final String orgGroupId, final int associatedIndicator, final int max, boolean includeEmployees) throws ArahantException {
		
		final HibernateCriteriaUtil<Person> hcu=hsu.createCriteria(Person.class).eq(Person.ORGGROUPTYPE, APPLICANT_TYPE);

		hcu.setMaxResults(max);
		
		final HibernateCriteriaUtil personHcu=hcu;//hcu.joinTo(Applicant.MAIN_CONTACT);
		personHcu.orderBy(Person.LNAME);
		if (!isEmpty(firstName))
			personHcu.like(Person.FNAME, firstName);
		if (!isEmpty(lastName))
			personHcu.like(Person.LNAME, lastName);

		if (!includeEmployees)
			hcu.isNotEmployee();
		
		OrgGroup og=null;
		if (!isEmpty(orgGroupId)){
			og=hsu.get(OrgGroup.class, orgGroupId);
			if (og.getOwningCompany()!=null)
				personHcu.joinTo(Person.COMPANYBASE).eq(OrgGroup.ORGGROUPID, og.getOwningCompany().getOrgGroupId());
			
			//TODO figure out how to do this in the query
		//	HibernateCriteriaUtil orgAssocHcu=personHcu.leftJoinTo(Person.ORGGROUPASSOCIATIONS);
		//	HibernateCriteriaUtil orgGroupHcu=orgAssocHcu.leftJoinTo(OrgGroupAssociation.ORGGROUP);
		//	orgGroupHcu.ne(OrgGroup.ORGGROUPID, ccsc.getOrgGroupId());
		}
		
		if (!isEmpty(orgGroupId)){
			final List p=hsu.createCriteria(Person.class)
				.selectFields(Person.PERSONID)
				.joinTo(Person.ORGGROUPASSOCIATIONS)
				.joinTo(OrgGroupAssociation.ORGGROUP)
				.eq(OrgGroup.ORGGROUPID, orgGroupId)
				.distinct()
				.list();
			hcu.notIn(Person.PERSONID, p);
		}
		
	
		switch (associatedIndicator){
			case 0: break;//all
			case 1: 		//associated
				hcu.isNotNull(Person.ORGGROUPASSOCIATIONS); //make sure there are some, inner join
					break;
			case 2: //not associated	
				// hibernate can't handle sizeEq here
				hcu.isNull(Person.ORGGROUPASSOCIATIONS);
				//hcu.sizeEq(ProspectContact.ORGGROUPASSOCIATIONS,0);
					break;
		}
		
		
		List<Person> res=new LinkedList<Person>();
		
		if (og != null){
			final Iterator<Person> resItr=hcu.list().iterator();
			
			while (resItr.hasNext())
				try{
					final Person cc=resItr.next();
					final Iterator ogaItr=cc.getOrgGroupAssociations().iterator();
					boolean found=false;
					while (ogaItr.hasNext()){
						final OrgGroupAssociation oga=(OrgGroupAssociation)ogaItr.next();
						if (oga.getOrgGroup().equals(og)){
							found=true;
							break;
						}
					}
					if (!found)
						res.add(cc);
				}catch (final Exception e){
					continue; //if it failed to clear, skip it
				}
		}
		else
			res=hcu.list();
		
		final BApplicant[]ret=new BApplicant[res.size()];
		
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BApplicant(res.get(loop));
		
		return ret;
	}

	public static BApplicant[] list(final HibernateSessionUtil hsu, final String groupId, final String lastNameStartsWith, final boolean primary, boolean includeEmployees, final int cap) throws ArahantException {
		
		final HibernateCriteriaUtil<Applicant> hcu=hsu.createCriteria(Applicant.class)
			.orderBy(Person.LNAME)
			.orderBy(Person.FNAME)
			.like(Person.LNAME, lastNameStartsWith);

		if (!includeEmployees)
			hcu.isNotEmployee();
		
		final HibernateCriteriaUtil ogaHcu=hcu.joinTo(Person.ORGGROUPASSOCIATIONS);
		
		if (primary)
			ogaHcu.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y');
		
		ogaHcu.joinTo(OrgGroupAssociation.ORGGROUP)
			.eq(OrgGroup.ORGGROUPID, groupId)
			.list();
	
		return makeAray(hcu.list(), groupId);
	}

	public void deleteFormsNotIn(List<String> formIds) {
		ArahantSession.getHSU().createCriteria(PersonForm.class)
			.eq(PersonForm.PERSON, applicant)
			.notIn(PersonForm.FORM_ID, formIds)
			.delete();
	}

	public boolean getIsEmployee() {
		return new BPerson(applicant.getPerson()).isEmployee();
	}

}

	
