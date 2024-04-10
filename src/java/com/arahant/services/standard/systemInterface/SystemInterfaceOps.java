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


package com.arahant.services.standard.systemInterface;

/**
 *
 * @author Blake McBride
 */
import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.lisp.ABCL;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.arahant.utils.StandardProperty;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardSystemInterfaceOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class SystemInterfaceOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(SystemInterfaceOps.class);
	private static final int interfaceVersion = 1;
	private boolean ssoLogging;

	public SystemInterfaceOps() {
	}

	/**
	 * The following web service is what is used to obtain the <SSOGuid> used
	 * for SSO auto-login.
	 *
	 * @param in the UserGuid and the IP address of the user's browser (in
	 * addition to server authentication information)
	 * @return SSOToken and returnCode
	 */
	@WebMethod()
	public GetSSOTokenReturn getSSOToken(/* @WebParam(name = "in")*/ final GetSSOTokenInput in) {
		final GetSSOTokenReturn ret = new GetSSOTokenReturn();
		String remoteIP = getRemoteHost();

		try {
			if (!in.isValid(remoteIP))
				ret.setReturnCode(-1);  // unauthorized system
			else {
				ret.setSSOToken(LoginToken.makeSSOGuid(in.getUserGuid(), in.getUserIP()));
				ret.setReturnCode(0);
			}
		} catch (final Exception e) {
			ret.setReturnCode(-2);
		}
		return ret;
	}
	
	/**
	 * This web service creates a new company.  If a companyGuid is not supplied one is created and returned.
	 * 
	 * @param in companyName & companyGUID
	 * @return companyGuid & returnCode
	 */
	@WebMethod()
	public AddCompanyReturn addCompany(/* @WebParam(name = "in")*/ final AddCompanyInput in) {
		final AddCompanyReturn ret = new AddCompanyReturn();
		hsu = ArahantSession.openHSU();
		hsu.beginTransaction();
		hsu.setCurrentPersonToArahant();

		String remoteIP = getRemoteHost();
		
		try {
			if (!in.isValid(remoteIP))
				ret.setReturnCode(-1);  // unauthorized system
			else if (isEmpty(in.getCompanyName()))
				ret.setReturnCode(-3);  // missing company name
			else {
				boolean goodGuid = true;
				String companyGuid;
				if (isEmpty(in.getCompanyGuid()))
					companyGuid = makeGUID();
				else {
					companyGuid = in.getCompanyGuid();
					if (companyGuid.length() != 32) {
						goodGuid = false;
						ret.setReturnCode(-4);   //  companyGuid invalid
					} else {
						List<OrgGroup> lst = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).eq(OrgGroup.ORGGROUP_GUID, companyGuid).list();
						if (!lst.isEmpty()) {
							goodGuid = false;
							ret.setReturnCode(-5);  //  duplicate companyGuid
						}
					}
				}
				if (goodGuid) {
					BCompany bc = new BCompany();
					bc.create();
					bc.setName(in.getCompanyName());
					bc.setEeo1Establishment(true);
					bc.setEeo1Headquarters(true);
					bc.getBean().setOrgGroupGuid(companyGuid);
					bc.setStreet(in.getStreet());
					bc.setStreet2(in.getStreet2());
					bc.setCity(in.getCity());
					bc.setState(in.getState());
					bc.setZip(in.getZip());
					if (!isEmpty(in.getMainPhone()))
						bc.setMainPhoneNumber(in.getMainPhone());
					short defaultFiscalBeginningMonth  = 1;
					bc.setFiscalBeginningMonth(defaultFiscalBeginningMonth);
					bc.insert();
					ret.setCompanyGuid(companyGuid);
					ret.setReturnCode(0);  //  company added
				}
			}
			hsu.commitTransaction();
		} catch (final Exception e) {
			hsu.rollbackTransaction();
			logger.error(e);
			ret.setReturnCode(-2);
		} finally {
			ArahantSession.clearSession();
		}
		return ret;
	}
	
	/**
	 * Finds a company based on its companyGuid.
	 * 
	 * @param in companyGuid
	 * @return returnCode and companyName
	 */
	@WebMethod()
	public FindCompanyByGuidReturn findCompanyByGuid(/* @WebParam(name = "in")*/ final FindCompanyByGuidInput in) {
		final FindCompanyByGuidReturn ret = new FindCompanyByGuidReturn();
		hsu = ArahantSession.openHSU();
		hsu.beginTransaction();
		hsu.setCurrentPersonToArahant();

		String remoteIP = getRemoteHost();
		
		try {
			String companyGuid = in.getCompanyGuid();
			if (!in.isValid(remoteIP))
				ret.setReturnCode(-1);  // unauthorized system
			else if (isEmpty(companyGuid)  ||  companyGuid.length() != 32)
				ret.setReturnCode(-3);  //  companyGuid missing or invalid
			else {
				List<OrgGroup> lst = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).eq(OrgGroup.ORGGROUP_GUID, companyGuid).list();
				if (lst.isEmpty())
					ret.setReturnCode(1);  //  companyGuid not found
				else {
					ret.setCompanyName(lst.get(0).getName());
					ret.setReturnCode(0);  //  companyGuid found
				}
			}
			hsu.commitTransaction();
		} catch (final Exception e) {
			hsu.rollbackTransaction();
			logger.error(e);
			ret.setReturnCode(-2);
		} finally {
			ArahantSession.clearSession();
		}
		return ret;
	}
	
	/**
	 * Finds a company based on its companyName.
	 * 
	 * @param in companyGuid
	 * @return returnCode and companyGuid
	 */
	@WebMethod()
	public FindCompanyByNameReturn findCompanyByName(/* @WebParam(name = "in")*/ final FindCompanyByNameInput in) {
		final FindCompanyByNameReturn ret = new FindCompanyByNameReturn();
		hsu = ArahantSession.openHSU();
		hsu.beginTransaction();
		hsu.setCurrentPersonToArahant();

		String remoteIP = getRemoteHost();
		
		try {
			String companyName = in.getCompanyName();
			if (!in.isValid(remoteIP))
				ret.setReturnCode(-1);  // unauthorized system
			else if (isEmpty(companyName))
				ret.setReturnCode(-3);  //  companyName missing
			else {
				List<OrgGroup> lst = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).eq(OrgGroup.NAME, companyName).list();
				if (lst.isEmpty())
					ret.setReturnCode(1);  //  companyName not found
				else if (lst.size() > 1)
					ret.setReturnCode(2);  //  multiple companys with the same name found
				else {
					ret.setCompanyGuid(lst.get(0).getOrgGroupGuid());
					ret.setReturnCode(0);  //  company found
				}
			}
			hsu.commitTransaction();
		} catch (final Exception e) {
			hsu.rollbackTransaction();
			logger.error(e);
			ret.setReturnCode(-2);
		} finally {
			ArahantSession.clearSession();
		}
		return ret;
	}
	
	/**
	 * Update a company's Guid or name.
	 * 
	 * @param in companyGuid and copanyName
	 * @return companyGuid (if a new one was created)
	 */
	@WebMethod()
	public UpdateCompanyReturn updateCompany(/* @WebParam(name = "in")*/ final UpdateCompanyInput in) {
		final UpdateCompanyReturn ret = new UpdateCompanyReturn();
		hsu = ArahantSession.openHSU();
		hsu.beginTransaction();
		hsu.setCurrentPersonToArahant();

		String remoteIP = getRemoteHost();
		
		try {
			String companyGuid = in.getCompanyGuid();
			String companyName = in.getCompanyName();
			BCompany bc;
			List<OrgGroup> lst;
			OrgGroup og;			
			if (!in.isValid(remoteIP))
				ret.setReturnCode(-1);  // unauthorized system
			else if (isEmpty(companyName)  &&  isEmpty(companyGuid))
				ret.setReturnCode(-3);  // companyName & companyGuid both missing
			else if (!isEmpty(companyGuid))
				if (companyGuid.length() != 32)
					ret.setReturnCode(-4);  //  invalid companyGuid
				else {
					lst = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).eq(OrgGroup.ORGGROUP_GUID, companyGuid).list();
					if (lst.isEmpty()) 
						if (isEmpty(companyName))
							ret.setReturnCode(-5);  //  companyGuid not found and no companyName to match supplied
						else {
							lst = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).eq(OrgGroup.NAME, companyName).list();
							if (lst.isEmpty())
								ret.setReturnCode(-6);  //  companyGuid not found and companyName not found
							else if (lst.size() > 1)
								ret.setReturnCode(-7);  //  companyGuid not found and more than one org group name matched
							else {
								og = lst.get(0);
								bc = new BCompany(og.getOrgGroupId());
								if (bc.getBean() == null)
									ret.setReturnCode(-12);  //  not a company org group
								else if (!isEmpty(og.getOrgGroupGuid()))
									ret.setReturnCode(-8);  // companyName match but company already has a different companyGuid
								else {
									updateAddress(in, bc);
									og.setOrgGroupGuid(companyGuid);
									ret.setCompanyGuid(companyGuid);
									ret.setReturnCode(0);  // successfully set companyGuid
								}
							}
						}
					else if (isEmpty(companyName))
						ret.setReturnCode(-9);  //  companyGuid found but no new companyName supplied
					else {
						og = lst.get(0);
						bc = new BCompany(og.getOrgGroupId());
						if (bc.getBean() == null)
							ret.setReturnCode(-12);  //  not a company org group
						else {
							updateAddress(in, bc);
							og.setName(companyName);
							ret.setReturnCode(1);   //  successfully updated companyName
						}
					}  
				}
			else {  //  companyGuid not supplied
				lst = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).eq(OrgGroup.NAME, companyName).list();
				if (lst.isEmpty())
					ret.setReturnCode(-10);  //  companyGuid not supplied and companyName not found
				else if (lst.size() > 1)
					ret.setReturnCode(-11);  //  companyGuid not supplied and more than one org group name matched
				else {
					og = lst.get(0);
					bc = new BCompany(og.getOrgGroupId());
					if (bc.getBean() == null)
						ret.setReturnCode(-12);  //  not a company org group
					else if (!isEmpty(og.getOrgGroupGuid())) {
						updateAddress(in, bc);
						ret.setCompanyGuid(og.getOrgGroupGuid());
						ret.setReturnCode(2);  // companyName match returning existing companyGuid
					} else {
						updateAddress(in, bc);
						companyGuid = makeGUID();
						og.setOrgGroupGuid(companyGuid);
						ret.setCompanyGuid(companyGuid);
						ret.setReturnCode(3);  // companyName match returning new companyGuid
					}
				}
			}
			hsu.commitTransaction();
		} catch (final Exception e) {
			hsu.rollbackTransaction();
			logger.error(e);
			ret.setReturnCode(-2);
		} finally {
			ArahantSession.clearSession();
		}
		return ret;
	}
	
	private void updateAddress(UpdateCompanyInput in, BCompany bc) {
		if (!isEmpty(in.getStreet()))
			bc.setStreet(in.getStreet());
		if (!isEmpty(in.getStreet2()))
			bc.setStreet2(in.getStreet2());
		if (!isEmpty(in.getCity()))
			bc.setCity(in.getCity());
		if (!isEmpty(in.getState()))
			bc.setState(in.getState());
		if (!isEmpty(in.getZip()))
			bc.setZip(in.getZip());
		if (!isEmpty(in.getMainPhone()))
			bc.setMainPhoneNumber(in.getMainPhone());
	}
		
	/**
	 * Find a userGuid given a variety of information
	 * 
	 *				companyGuid supplied and valid? 
	 *					No:  return -3
	 *				userGuid supplied?
	 * 				yes:
	 * 					Check userGuid:
	 * 						Found in company - return 0
	 *						Found in another company - return -4
	 *					Search for user that matches supplied name, ssn, bdate, etc. in indicated company
	 *					Match found?
	 *					Yes:
	 *						install supplied userGuid
	 *						return 1
	 *					No:
	 *						return 2
	 *				no:
	 * 					Search for user that matches supplied name, ssn, bdate, etc. in indicated company
	 *					Match found?
	 *					Yes:
	 *						return 3 and userGuid
	 *					No:
	 *						return 4
	 * @param in user name, companyGuid, userGuis, SSN, DOB, etc.
	 * @return userGuid and a returnCode
	 */
	@WebMethod()
	public FindUserReturn findUser(/* @WebParam(name = "in")*/ final FindUserInput in) {
		final FindUserReturn ret = new FindUserReturn();

		hsu = ArahantSession.openHSU();
		hsu.beginTransaction();
		hsu.setCurrentPersonToArahant();

		String remoteIP = getRemoteHost();

		if (in.SSOLogging) {
			System.out.println("SystemInterface:  Begin findUser *****");
			System.out.println("SystemInterface:  companyGuid = " + in.getCompanyGuid());
			System.out.println("SystemInterface:  userGuid = " + in.getUserGuid());
			System.out.println("SystemInterface:  fname = " + in.getFname());
			System.out.println("SystemInterface:  lname = " + in.getLname());
			System.out.println("SystemInterface:  ssn = "); //  + in.getSsn());
			System.out.println("SystemInterface:  dob = "); //  + in.getDob());
			System.out.println("SystemInterface:  End findUser *******");
		}

		try {
			if (!in.isValid(remoteIP))
				ret.setReturnCode(-1);  // unauthorized system
			else if (isEmpty(in.getCompanyGuid()) || (!isEmpty(in.getCompanyGuid()) && !hsu.createCriteriaNoCompanyFilter(CompanyBase.class).eq(CompanyBase.ORGGROUP_GUID, in.getCompanyGuid()).exists()))
				ret.setReturnCode(-3);
			else {
				Person p;
				if (!isEmpty(in.getUserGuid())) {
					p = hsu.createCriteriaNoCompanyFilter(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSON_GUID, in.getUserGuid()).first();
					if (p != null)
						if (p.getCompanyBase().getOrgGroupGuid().trim().equals(in.getCompanyGuid()))
							ret.setReturnCode(0);
						else
							ret.setReturnCode(-4);
					else {
						p = hsu.createCriteriaNoCompanyFilter(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.DOB, in.getDob()).eq(Person.SSN, in.getSsn()).joinTo(Person.COMPANYBASE).eq(CompanyBase.ORGGROUP_GUID, in.getCompanyGuid()).first();
						if (p != null) {
							BPerson bp = new BPerson(p);
							bp.setPersonGuid(in.getUserGuid());
							bp.update();
							ret.setReturnCode(1);
						} else
							ret.setReturnCode(2);
					}
				} else {
					p = hsu.createCriteriaNoCompanyFilter(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.DOB, in.getDob()).eq(Person.SSN, in.getSsn()).joinTo(Person.COMPANYBASE).eq(CompanyBase.ORGGROUP_GUID, in.getCompanyGuid()).first();
					if (p != null) {
						ret.setReturnCode(3);
						ret.setUserGuid(p.getPersonGuid());
					} else
						ret.setReturnCode(4);
				}
			}
			hsu.commitTransaction();
		} catch (final Exception e) {
			hsu.rollbackTransaction();
			logger.error(e);
			ret.setReturnCode(-2);
		} finally {
			ArahantSession.clearSession();	
		}
		return ret;
	}

	/** Add a new user or update an existing user.
	 * 
	 *				companyGuid supplied and valid?
	 *					No:  return -3
	 *				switch on mode
	 *				case 1 - add user
	 *					If userGuid is included and already exists (in any company)  - return -10
	 *					Search for user that matches fname, lname, dob, ssn - if exists return -11 and (userGuid of found user if exists)
	 *					If supplied data is insufficient to add a new user return -12
	 *					if userGuid was supplied and createGuid is true return -13
	 *					Add new user with supplied data in specified company
	 *					Install userGuid if supplied else
	 *					create and return userGuid if createGuid is true
	 *					return 0
	 *				case 2 - update user
	 *					If userGuid supplied, find user with specified userGuid
	 *					If found - use that user
	 *						If user in wrong company return -20
	 *					else if not found - search for user with fname, lname, ssn, dob in specified company
	 *					if still not found return -21
	 *					if found and database GUID does not match supplied GUID return -22
	 *					Update user data fields (including name changes) with all supplied fields
	 *					Install userGuid if provided and not already existing in record
	 *					else create and return userGuid if createGuid is true
	 *					return 0
	 *				case 3 - add / update user
	 *					If userGuid supplied, find user with specified userGuid
	 *					If found - use that user
	 *						If user in wrong company return -30
	 *						Update user data fields (including name changes) with all supplied fields
	 *						return 0
	 *					else if not found - search for user with fname, lname, ssn, dob in specified company
	 *					If found and database GUID does not match supplied GUID return -33
	 *					If found - use that user
	 *						Update user data fields (including name changes) with all supplied fields
	 *						Install userGuid if provided and not already existing in record
	 *						else create and return userGuid if createGuid is true
	 *						return 0
	 *					If not found
	 *						If supplied data is insufficient to add a new user return -31
	 *						if userGuid was supplied and createGuid is true return -32
	 *						Add new user with supplied data in specified company
	 *						Install userGuid if supplied else
	 *						create and return userGuid if createGuid is true
	 *						return 0
	 *				case N where N is not 1, 2, or 3
	 *						return -4
	 */
	@WebMethod()
	public AddUserReturn addUser(/* @WebParam(name = "in")*/ final AddUserInput in) {
		final AddUserReturn ret = new AddUserReturn();
		hsu = ArahantSession.openHSU();
		hsu.beginTransaction();
		hsu.setCurrentPersonToArahant();

		String remoteIP = getRemoteHost();

		if (ssoLogging = in.SSOLogging) {
			System.out.println("SystemInterface:  Begin addUser *****");
			System.out.println("SystemInterface:  mode = " + in.getMode());
			System.out.println("SystemInterface:  companyGuid = " + in.getCompanyGuid());
			System.out.println("SystemInterface:  createGuid = " + in.isCreateGuid());
			System.out.println("SystemInterface:  userGuid = " + in.getUserGuid());
			System.out.println("SystemInterface:  user = " + in.getUser());
			System.out.println("SystemInterface:  password = "); // + in.getPassword());
			System.out.println("SystemInterface:  fname = " + in.getFname());
			System.out.println("SystemInterface:  lname = " + in.getLname());
			System.out.println("SystemInterface:  mname = " + in.getMname());
			System.out.println("SystemInterface:  ssn = "); //  + in.getSsn());
			System.out.println("SystemInterface:  sex = " + in.getSex());
			System.out.println("SystemInterface:  dob = "); //  + in.getDob());
			System.out.println("SystemInterface:  workEmail = "); //  + in.getWorkEmail());
			System.out.println("SystemInterface:  jobTitle = " + in.getJobTitle());
			System.out.println("SystemInterface:  homeAddress1 = "); //  + in.getHomeAddress1());
			System.out.println("SystemInterface:  homeAddress2 = "); //  + in.getHomeAddress2());
			System.out.println("SystemInterface:  homeCity = "); //  + in.getHomeCity());
			System.out.println("SystemInterface:  homeState = "); //  + in.getHomeState());
			System.out.println("SystemInterface:  homeZip = "); //  + in.getHomeZip());
			System.out.println("SystemInterface:  homePhone = "); //  + in.getHomePhone());
			System.out.println("SystemInterface:  workPhone = "); //  + in.getWorkPhone());
			System.out.println("SystemInterface:  cellPhone = "); //  + in.getCellPhone());
			System.out.println("SystemInterface:  isSupervisor = " + in.getIsSupervisor());
			System.out.println("SystemInterface:  End addUser *******");
		}

		try {
			if (!in.isValid(remoteIP))
				ret.setReturnCode(-1);  // unauthorized system
			else if (isEmpty(in.getCompanyGuid()) || (!isEmpty(in.getCompanyGuid()) && !hsu.createCriteriaNoCompanyFilter(CompanyBase.class).eq(CompanyBase.ORGGROUP_GUID, in.getCompanyGuid()).exists()))
				ret.setReturnCode(-3);
			else {
				hsu.setCurrentCompany(hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).eq(CompanyDetail.ORGGROUP_GUID, in.getCompanyGuid()).first());
				switch (in.getMode()) {
					case 1:
						if (!isEmpty(in.getUserGuid()) && hsu.createCriteriaNoCompanyFilter(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSON_GUID, in.getUserGuid()).exists())
							ret.setReturnCode(-10);
						else {
							Person p = hsu.createCriteriaNoCompanyFilter(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.DOB, in.getDob()).eq(Person.SSN, in.getSsn()).joinTo(Person.COMPANYBASE).eq(CompanyBase.ORGGROUP_GUID, in.getCompanyGuid()).first();
							if (p != null) {
								ret.setReturnCode(-11);
								ret.setUserGuid(p.getPersonGuid());
							} else if (isEmpty(in.getFname()) || isEmpty(in.getLname()) || isEmpty(in.getSsn()) || in.getDob() <= 0)
								ret.setReturnCode(-12);
							else if (!isEmpty(in.getUserGuid()) && in.isCreateGuid())
								ret.setReturnCode(-13);
							else
								addEmployee(in, ret);
						}
						break;
					case 2:
						Person p = null;
						if (!isEmpty(in.getUserGuid()))
							p = hsu.createCriteriaNoCompanyFilter(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSON_GUID, in.getUserGuid()).first();
						if (p == null) {
							p = hsu.createCriteriaNoCompanyFilter(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.DOB, in.getDob()).eq(Person.SSN, in.getSsn()).joinTo(Person.COMPANYBASE).eq(CompanyBase.ORGGROUP_GUID, in.getCompanyGuid()).first();
							if (p == null)
								ret.setReturnCode(-21);
							else if (!isEmpty(p.getPersonGuid()) && !isEmpty(in.getUserGuid()) && !p.getPersonGuid().equals(in.getUserGuid()))
								ret.setReturnCode(-22);
							else {
								String userGUID = !isEmpty(in.getUserGuid()) && isEmpty(p.getPersonGuid()) ? in.getUserGuid() : isEmpty(p.getPersonGuid()) && in.isCreateGuid() ? makeGUID() : p.getPersonGuid();
								updateEmployee(p, in, ret, userGUID);
							}
						} else if (!p.getCompanyBase().getOrgGroupGuid().trim().equals(in.getCompanyGuid()))
							ret.setReturnCode(-20);
						else {
							BEmployee be = new BEmployee(p.getPersonId());
							String userGUID = !isEmpty(in.getUserGuid()) && isEmpty(be.getPersonGuid()) ? in.getUserGuid() : isEmpty(be.getPersonGuid()) && in.isCreateGuid() ? makeGUID() : be.getPersonGuid();
							updateEmployee(p, in, ret, userGUID);
						}
						break;
					case 3:
						Person pp = null;
						if (!isEmpty(in.getUserGuid()))
							pp = hsu.createCriteriaNoCompanyFilter(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSON_GUID, in.getUserGuid()).first();
						if (pp != null && !pp.getCompanyBase().getOrgGroupGuid().trim().equals(in.getCompanyGuid()))
							ret.setReturnCode(-30);
						else if (pp != null) {
							updateEmployee(pp, in, ret, null);
							ret.setReturnCode(0);
						} else {
							pp = hsu.createCriteriaNoCompanyFilter(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.DOB, in.getDob()).eq(Person.SSN, in.getSsn()).joinTo(Person.COMPANYBASE).eq(CompanyBase.ORGGROUP_GUID, in.getCompanyGuid()).first();
							if (pp == null)
								if (isEmpty(in.getFname()) || isEmpty(in.getLname()) || isEmpty(in.getSsn()) || in.getDob() <= 0)
									ret.setReturnCode(-31);
								else if (!isEmpty(in.getUserGuid()) && in.isCreateGuid())
									ret.setReturnCode(-32);
								else
									addEmployee(in, ret);
							else if (!isEmpty(pp.getPersonGuid()) && !isEmpty(in.getUserGuid()) && !pp.getPersonGuid().equals(in.getUserGuid()))
								ret.setReturnCode(-33);
							else {
								String userGUID = !isEmpty(in.getUserGuid()) && isEmpty(pp.getPersonGuid()) ? in.getUserGuid() : isEmpty(pp.getPersonGuid()) && in.isCreateGuid() ? makeGUID() : pp.getPersonGuid();
								updateEmployee(pp, in, ret, userGUID);
							}
						}
						break;
					default:
						ret.setReturnCode(-4);
						break;
				}
			}
			hsu.commitTransaction();
		} catch (final Exception e) {
			logger.error(e);
			hsu.rollbackTransaction();
			ret.setReturnCode(-2);
		} finally {
			ArahantSession.clearSession();
		}
		return ret;
	}

	private static String makeGUID() {
		UUID uuid = UUID.randomUUID();
		return Long.toHexString(uuid.getLeastSignificantBits()) + Long.toHexString(uuid.getMostSignificantBits());
	}
	
	private void updateEmployee(Person pp, final AddUserInput in, final AddUserReturn ret, String guid) throws ArahantException {
		BEmployee be = new BEmployee(pp.getPersonId());
		be.setFirstName(in.getFname());
		be.setLastName(in.getLname());
		if (!isEmpty(in.getMname()))
			be.setMiddleName(in.getMname());
		be.setDob(in.getDob());
		be.setSsn(in.getSsn());
		if (!isEmpty(in.getSex()))
			be.setSex(in.getSex().toUpperCase());
		else
			be.setSex("U");
		if (!isEmpty(in.getHomeAddress1()))
			be.setStreet(in.getHomeAddress1());
		if (!isEmpty(in.getHomeAddress2()))
			be.setStreet2(in.getHomeAddress2());
		if (!isEmpty(in.getHomeCity()))
			be.setCity(in.getHomeCity());
		if (!isEmpty(in.getHomeState()))
			be.setState(in.getHomeState());
		if (!isEmpty(in.getHomeZip()))
			be.setZip(in.getHomeZip());
		if (!isEmpty(in.getHomePhone()))
			be.setHomePhone(in.getHomePhone());
		if (!isEmpty(in.getCellPhone()))
			be.setMobilePhone(in.getCellPhone());
		if (!isEmpty(in.getWorkPhone()))
			be.setWorkPhone(in.getWorkPhone());
		if (!isEmpty(in.getWorkEmail()))
			be.setPersonalEmail(in.getWorkEmail());
		if (!isEmpty(in.getJobTitle()))
			be.setJobTitle(in.getJobTitle());
		if (!hsu.createCriteria(ProphetLogin.class).eq(ProphetLogin.PERSONID, pp.getPersonId()).exists()) {
			if (!isEmpty(in.getUser()) && !isEmpty(in.getPassword())) {
				be.setUserLogin(in.getUser());
				be.setUserPassword(in.getPassword(), true);
				String securityGroupId = BProperty.get(StandardProperty.DEFAULT_SEC_GROUP);
				String screenGroupId = BProperty.get(StandardProperty.DEFAULT_SCREEN_GROUP);
				if (ssoLogging) {
					if (isEmpty(screenGroupId))
						System.out.println("MakeLoginDefaults ERROR (" + DateUtils.getDateAndTimeFormatted(new Date()) + "): Property " + StandardProperty.DEFAULT_SCREEN_GROUP + " is empty so username/password was not generated for " + be.getNameFML());
					if (isEmpty(securityGroupId))
						System.out.println("MakeLoginDefaults ERROR (" + DateUtils.getDateAndTimeFormatted(new Date()) + "): Property " + StandardProperty.DEFAULT_SEC_GROUP + " is empty so username/password was not generated for " + be.getNameFML());
				}
				be.setSecurityGroupId(securityGroupId);
				be.setScreenGroupId(screenGroupId);
			} else
				be.makeLoginDefaults(true);
		}
		if (!isEmpty(guid))
			be.setPersonGuid(guid);
		be.update();
		ret.setReturnCode(0);
		ret.setUserGuid(in.isCreateGuid() && !isEmpty(guid) ? guid : "");
	}

	private void addEmployee(final AddUserInput in, final AddUserReturn ret) throws ArahantException {
		String companyId = hsu.createCriteriaNoCompanyFilter(CompanyBase.class).eq(CompanyBase.ORGGROUP_GUID, in.getCompanyGuid()).first().getOrgGroupId();
		String userGUID = !isEmpty(in.getUserGuid()) ? in.getUserGuid() : in.isCreateGuid() ? makeGUID() : null;
		BEmployee be = new BEmployee();
		String id = be.create();
		be.setFirstName(in.getFname());
		be.setLastName(in.getLname());
		if (!isEmpty(in.getMname()))
			be.setMiddleName(in.getMname());
		be.setCompanyId(companyId);
		be.setDob(in.getDob());
		be.setSsn(in.getSsn());
		if (!isEmpty(in.getSex()))
			be.setSex(in.getSex().toUpperCase());
		if (!isEmpty(in.getHomeAddress1()))
			be.setStreet(in.getHomeAddress1());
		if (!isEmpty(in.getHomeAddress2()))
			be.setStreet2(in.getHomeAddress2());
		if (!isEmpty(in.getHomeCity()))
			be.setCity(in.getHomeCity());
		if (!isEmpty(in.getHomeState()))
			be.setState(in.getHomeState());
		if (!isEmpty(in.getHomeZip()))
			be.setZip(in.getHomeZip());
		if (!isEmpty(in.getHomePhone()))
			be.setHomePhone(in.getHomePhone());
		if (!isEmpty(in.getCellPhone()))
			be.setMobilePhone(in.getCellPhone());
		if (!isEmpty(in.getWorkPhone()))
			be.setWorkPhone(in.getWorkPhone());
		if (!isEmpty(in.getWorkEmail()))
			be.setPersonalEmail(in.getWorkEmail());
		if (!isEmpty(in.getJobTitle()))
			be.setJobTitle(in.getJobTitle());
		be.setPersonGuid(userGUID);
		if (!isEmpty(in.getUser()) && !isEmpty(in.getPassword())) {
			be.setUserLogin(in.getUser());
			be.setUserPassword(in.getPassword(), true);
			String securityGroupId = BProperty.get(StandardProperty.DEFAULT_SEC_GROUP);
			String screenGroupId = BProperty.get(StandardProperty.DEFAULT_SCREEN_GROUP);
			if (ssoLogging) {
				if (isEmpty(screenGroupId))
					System.out.println("MakeLoginDefaults ERROR (" + DateUtils.getDateAndTimeFormatted(new Date()) + "): Property " + StandardProperty.DEFAULT_SCREEN_GROUP + " is empty so username/password was not generated for " + be.getNameFML());
				if (isEmpty(securityGroupId))
					System.out.println("MakeLoginDefaults ERROR (" + DateUtils.getDateAndTimeFormatted(new Date()) + "): Property " + StandardProperty.DEFAULT_SEC_GROUP + " is empty so username/password was not generated for " + be.getNameFML());
			}
			be.setSecurityGroupId(securityGroupId);
			be.setScreenGroupId(screenGroupId);
		} else
			be.makeLoginDefaults(true);
		be.insert();

		//ArahantSession.getHSU().flush();
		hsu.commitTransaction();
		hsu.beginTransaction();
		be = new BEmployee(id);
		//be.setStatusId(BHREmployeeStatus.findOrMake("Active", true), DateUtils.now());
		BHREmplStatusHistory statHist = new BHREmplStatusHistory();
		statHist.create();
		statHist.setEffectiveDate(DateUtils.now());
		statHist.setEmployee(be.getEmployee());
		statHist.setHrEmployeeStatus(new BHREmployeeStatus(BHREmployeeStatus.findOrMake("Active", true)).getBean());
		statHist.setNotes("Initial status generated externally via SystemInterface");

		statHist.insert();

		be.assignToSingleOrgGroup(companyId, !isEmpty(in.getIsSupervisor()) ? in.getIsSupervisor().equals("Y") : false);

		be.update();
		ret.setReturnCode(0);
		ret.setUserGuid(in.isCreateGuid() ? be.getPersonGuid() : "");
	}
	
	/**
	 * The following method is a starting template for SystemInterface web services.
	 * To use do the following:
	 * 
	 * 1. copy the method
	 * 2. rename it
	 * 3. change the 'private' to 'public'
	 * 4. create and set the input and return items
	 * 5. fill in the method's functionality
	 * 
	 * @param in
	 * @return 
	 */
	@WebMethod()
	private AddUserReturn template(/* @WebParam(name = "in")*/ final AddUserInput in) {
		final AddUserReturn ret = new AddUserReturn();
		hsu = ArahantSession.openHSU();
		hsu.beginTransaction();
		hsu.setCurrentPersonToArahant();

		String remoteIP = getRemoteHost();
		
		try {
			if (!in.isValid(remoteIP))
				ret.setReturnCode(-1);  // unauthorized system
			else {
			
				//  Web service functionality goes here
			
				hsu.commitTransaction();
			}
		} catch (final Exception e) {
			hsu.rollbackTransaction();
			logger.error(e);
			ret.setReturnCode(-2);
		} finally {
			ArahantSession.clearSession();
		}
		return ret;
	}
	
	public static void main(String [] args) {
		ABCL.init();
		ArahantSession.clearSession();
		
		
//		SystemInterfaceOps sio = new SystemInterfaceOps();
//		AddCompanyInput inp = new AddCompanyInput();
//		inp.setVendorCode(1);  // Five Points Test
//		inp.setAuthCode("Five Points TEST 3322");
//		inp.setCompanyName("New Company 1");
//		AddCompanyReturn ret = sio.addCompany(inp);
		
//		SystemInterfaceOps sio = new SystemInterfaceOps();
//		FindCompanyByGuidInput inp = new FindCompanyByGuidInput();
//		inp.setVendorCode(1);  // Five Points Test
//		inp.setAuthCode("Five Points TEST 3322");
//		inp.setCompanyGuid("9B100520C37F4A71883F33FF8C8CBCC5");
//		FindCompanyByGuidReturn ret = sio.findCompanyByGuid(inp);
		
//		SystemInterfaceOps sio = new SystemInterfaceOps();
//		UpdateCompanyInput inp = new UpdateCompanyInput();
//		inp.setVendorCode(1);  // Five Points Test
//		inp.setAuthCode("Five Points TEST 3322");
//		inp.setCompanyGuid("802aabe0ee90a31e7a80973799444837");
//		inp.setCompanyName("New Company 2");
//		UpdateCompanyReturn ret = sio.updateCompany(inp);
		
		SystemInterfaceOps sio = new SystemInterfaceOps();
		AddUserInput inp = new AddUserInput();
		inp.setVendorCode(3);  // Five Points Production
		inp.setAuthCode("F2AADDE01E4A42208C422687D01BA68D");
		inp.setCompanyGuid("AF879F7A0A734C3B9CE21504A2FDA237");
		inp.setMode(3);
		inp.setCreateGuid(false);
		inp.setUserGuid("372657DDB5164A89AA12A3C8C91D1114");
		inp.setFname("Blake");
		inp.setLname("McBride");
		inp.setMname("X");
		inp.setSsn("275-43-6614");
		inp.setSex("M");
		inp.setDob(19590806);
		inp.setWorkEmail("blake@arahant.com");
		inp.setHomeAddress1("4535 Scenic Hills Lane");
		inp.setHomeCity("Franklin");
		inp.setHomeState("TN");
		inp.setHomeZip("37064");
		inp.setHomePhone("(615) 426-2221");
		inp.setWorkPhone("6157910404102");
//		inp.setUser("abc");
//		inp.setPassword("def");
		AddUserReturn ret = sio.addUser(inp);
		
		
		ArahantSession.clearSession();
	}

}
