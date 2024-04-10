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



package com.arahant.services.main;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.InstantPDF;
import com.arahant.services.ArahantLoginException;
import com.arahant.services.ServiceBase;
import com.arahant.services.standard.systemInterface.LoginToken;
import com.arahant.utils.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.kissweb.StringUtils;

/**
 *
 *
 *
 */
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "MainOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class MainOps extends ServiceBase {

	/*
	 * A single WS call where we had three before (combine ListChildScreens,
	 * ListTopLevelScreenGroupsForCurrentUser, and
	 * ListScreensAndGroupsForScreenGroup
	 *
	 * Input: group id (may be "" if at top)
	 *
	 * Output: Array of objects that have:
	 *
	 * 1. title 2. type (Screen/Group) 3. screenFile (valid for type=Screen) 4.
	 * groupId (valid for type=Group or parent screens - see below) 5. isDefault
	 * (valid for type=Screen, should be true or false)
	 *
	 * Parent screen groups return type=Screen AND a groupId. All other screens
	 * would not have a groupId.
	 */
	private static final ArahantLogger logger = new ArahantLogger(MainOps.class);
	private static String userAgreement = "User Agreement:  \n\n"
			+ "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultricies mauris at ligula lobortis fringilla. Curabitur luctus blandit eros eleifend rhoncus. Vivamus eget enim at magna consequat feugiat. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Praesent egestas, diam id porta consectetur, risus justo sagittis libero, in hendrerit neque massa ut urna. Aliquam venenatis auctor est ac molestie. Phasellus nunc ipsum, laoreet et faucibus vitae, dapibus ac sem. Vestibulum at lacus at neque dictum mollis. Duis in justo dolor. Donec diam ipsum, dignissim ut bibendum vitae, lobortis sit amet lorem. Cras pharetra velit in nisi mattis ac venenatis risus semper. Ut ut mauris augue, dapibus malesuada mi. Quisque est eros, aliquam et eleifend et, fermentum nec nulla. Maecenas semper iaculis purus accumsan tempor.\n"
			+ "Etiam viverra, sapien sed ornare feugiat, urna felis scelerisque ligula, vitae posuere odio mi sit amet neque. Cras suscipit nisl viverra nisi sagittis eget laoreet magna fermentum. Nulla sodales risus vitae risus ultricies bibendum. Curabitur elit nisl, vestibulum eu aliquet ac, eleifend sed nunc. Ut aliquam erat ac lectus tincidunt feugiat. Suspendisse massa eros, blandit eu ornare et, placerat eu nunc. Nam aliquet condimentum porta. Vivamus tincidunt neque in enim venenatis eleifend.  \n\n"
			+ "Nulla feugiat fermentum arcu condimentum aliquam. Curabitur sit amet risus arcu. Cras sit amet urna varius sapien fringilla congue sed id nulla. Sed laoreet aliquet lorem at ullamcorper. Vivamus sem nunc, convallis ac congue id, lobortis sed justo. Vivamus placerat consequat justo, nec porta nulla rhoncus et. Duis id elementum purus. Fusce non scelerisque tortor. Quisque sollicitudin scelerisque pulvinar. Proin egestas consequat massa, eu fermentum lacus consequat sit amet. Etiam molestie quam vel turpis cursus mattis. Duis turpis libero, fermentum vel sagittis eu, elementum vitae metus.  \n\n"
			+ "In mollis porta nulla a ultrices. Sed ullamcorper, nulla in iaculis egestas, justo lorem egestas libero, ac aliquam metus lacus lacinia risus. Duis vitae risus ante. Cras nec tortor ut arcu egestas gravida id vulputate odio. Duis pharetra mollis dui, eu volutpat risus bibendum nec. Etiam posuere, est sed commodo dictum, magna mauris porta massa, non lobortis nulla lacus id lorem. Vivamus euismod commodo eleifend.  \n\n"
			+ "Pellentesque adipiscing interdum nisi, at dignissim neque faucibus nec. Integer quis metus eget erat sollicitudin vehicula. Donec faucibus felis et sapien facilisis congue. Vivamus non tortor vel leo consequat imperdiet. Pellentesque pulvinar congue tellus, non imperdiet lectus placerat a. Fusce non mauris lectus. Ut vehicula tempus volutpat. Suspendisse potenti. Morbi fermentum libero eu libero semper ornare. Aliquam sit amet ipsum nibh, eget mattis elit. \n\n";

		
	@WebMethod()
	public ListScreensAndGroupsReturn listScreensAndGroups(/*
			 * @WebParam(name = "in")
			 */final ListScreensAndGroupsInput in) {
		final ListScreensAndGroupsReturn ret = new ListScreensAndGroupsReturn();

		try {
			checkLogin(in);
			
			final BPerson pers = BPerson.getCurrent();

			if (!isEmpty(in.getGroupId())) {
				BScreenGroup sg1 = new BScreenGroup(in.getGroupId());
				//check to see if this is a wizard group
                switch (sg1.getWizardType()) {
                    case "E":
                        if (BProperty.isWizard3()) {
                            //From the employer homepage, they could be
                            //going through the wizard as someone else
                            //so check to see if they sent employeeId
                            //else use the person logged in
                            BEmployee be;
                            if (!isEmpty(in.getEmployeeId()))
                                be = new BEmployee(in.getEmployeeId());
                            else
                                try {
                                    be = new BEmployee(pers);
                                } catch (ArahantException e) {
                                    throw new ArahantException("User must be employee to view wizard.");
                                }

                            BWizardConfiguration wizConf = be.getWizardConfiguration(sg1.getWizardType());
                            List<BWizardScreen> screens = wizConf.loadWizard(be);
                            int totalLength = screens.size();
                            //if they still have a finalized state, just show review and finish screen... can't make changes.
                            if (be.getBenefitWizardStatus().equals(Employee.BENEFIT_WIZARD_STATUS_FINALIZED + "") && wizConf.getLockOnFinalizeBool()) {
                                screens = screens.subList(screens.size() - 2, screens.size());
                                totalLength = screens.size();
                                screens.get(0).setIsDefault(true);
                            } else if (be.getIsNewHire() && AIProperty.getBoolean("IsWmCo") && DateUtils.addDays(be.getCurrentHiredDate(), 31) <= DateUtils.now()) {
                                screens = screens.subList(screens.size() - 2, screens.size());
                                totalLength = screens.size();
                                screens.get(0).setIsDefault(true);
                            } else {
                                String defaultWizardBCRId = BProperty.get(StandardProperty.DefaultWizardBcrId);
                                int wizardStartScreensCount = 1;
                                if (isEmpty(defaultWizardBCRId)) //setting this removes the QE selection screen
                                    wizardStartScreensCount++;
                                if (wizConf.getShowDemographics() == 'Y')
                                    wizardStartScreensCount++;
                                if (wizConf.getShowDependents() == 'Y')
                                    wizardStartScreensCount++;
                                if (in.getDepth() < wizardStartScreensCount) //dependents and demographics
                                    screens = screens.subList(0, wizardStartScreensCount);
                                if (in.getDepth() < 2 && isEmpty(defaultWizardBCRId)) //haven't done the qualify event screen
                                    screens = screens.subList(0, 2);
                                if (in.getDepth() > 0)
                                    screens.get(in.getDepth()).setIsDefault(true);
                                else
                                    screens.get(0).setIsDefault(true);
                            }
                            ret.setItem(screens, wizConf.getWizardConfigurationId(), totalLength);
                        } else
                            doWiz(in, ret, null);
                        break;
                    case "O":
                        List<BScreenOrGroup> screenList = new ArrayList<BScreenOrGroup>();
//					BScreenOrGroup welcomeScreen = BScreen.getByFilename("com/arahant/app/screen/standard/misc/genericWelcome/GenericWelcomeScreen.swf");
//					BScreenOrGroup welcomeGroup = new BScreenGroup(ArahantSession.getHSU().createCriteria(ScreenGroup.class).eq(ScreenGroup.PARENT_SCREEN, ((BScreen)welcomeScreen).getBean()).first());
                        screenList.add(BScreen.getByFilename("com/arahant/app/screen/standard/misc/genericWelcome/GenericWelcomeScreen.swf"));
                        screenList.add(BScreen.getByFilename("com/arahant/app/screen/standard/misc/onboardingHomePage/OnboardingHomePageScreen.swf"));
                        BEmployee be;
                        try {
                            be = new BEmployee(pers);
                        } catch (ArahantException e) {
                            throw new ArahantException("User must be employee to view wizard.");
                        }
                        List<HrChecklistItem> l = ArahantSession.getHSU().createCriteria(HrChecklistItem.class).eq(HrChecklistItem.HREMPLOYEESTATUS, be.getLastActiveStatusHistory().getHrEmployeeStatus()).eq(HrChecklistItem.RESPONSIBILITY, HrChecklistItem.RESPONSIBILITY_EMPLOYEE).list();
                        for (HrChecklistItem cli : l)
                            if (!isEmpty(cli.getScreenId()))
                                screenList.add(new BScreen(cli.getScreenId()));
                            else if (!isEmpty(cli.getScreenGroupId()))
                                screenList.add(new BScreenGroup(cli.getScreenGroupId()));
                        final BScreenOrGroup[] sga = new BScreenOrGroup[screenList.size()];

                        int loop = 0;
                        for (BScreenOrGroup bsog : screenList)
                            sga[loop++] = bsog;
                        ret.setData(sga);
                        break;
                    default:
                        ret.setData(sg1.listChildren(), in.getGroupId(), isLocalHost());
                        break;
                }
			} else	//is their top level group a wizard?  load it!
				//logger.info(pers.getTopLevelGroup().getWizardType());
				if (pers.getTopLevelGroup().getWizardType().equals("E"))
					if (BProperty.isWizard3()) {
						BEmployee be;
						try {
							be = new BEmployee(pers);
						} catch (ArahantException e) {
							throw new ArahantException("User must be employee to view wizard.");
						}

						BWizardConfiguration wizConf = be.getWizardConfiguration(pers.getTopLevelGroup().getWizardType());
						List<BWizardScreen> screens = wizConf.loadWizard(be);
						int totalLength = screens.size();
						//if they still have a finalized state, just show review and finish screen... can't make changes.
						if (be.getBenefitWizardStatus().equals(Employee.BENEFIT_WIZARD_STATUS_FINALIZED + "") && wizConf.getLockOnFinalizeBool()) {
							screens = screens.subList(screens.size() - 2, screens.size());
							totalLength = screens.size();
							screens.get(0).setIsDefault(true);
						} else if (be.getIsNewHire() && AIProperty.getBoolean("IsWmCo") && DateUtils.addDays(be.getCurrentHiredDate(), 31) <= DateUtils.now()) {
							screens = screens.subList(screens.size() - 2, screens.size());
							totalLength = screens.size();
							screens.get(0).setIsDefault(true);
						} else {
							if (in.getDepth() > -1)
								in.setDepth(in.getDepth() + 1);
							String defaultWizardBCRId = BProperty.get(StandardProperty.DefaultWizardBcrId);
							int wizardStartScreensCount = 1;
							if (isEmpty(defaultWizardBCRId)) //setting this removes the QE selection screen
								wizardStartScreensCount++;
							if (wizConf.getShowDemographics() == 'Y')
								wizardStartScreensCount++;
							if (wizConf.getShowDependents() == 'Y')
								wizardStartScreensCount++;
							if (in.getDepth() < wizardStartScreensCount) //dependents and demographics
								screens = screens.subList(0, wizardStartScreensCount);
							if (in.getDepth() < 2) //haven't done the qualify event screen
								screens = screens.subList(0, 2);
							if (in.getDepth() > 0)
								screens.get(in.getDepth()).setIsDefault(true);
							else
								screens.get(0).setIsDefault(true);
						}
						ret.setItem(screens, wizConf.getWizardConfigurationId(), totalLength);
					} else
						doWiz(in, ret, null);
				else if (isEmpty(in.getContextCompanyId()))
					ret.setData(pers.listTopLevelNoCompanyScreens(), pers.getTopLevelNoCompanyGroup().getScreenGroupId(), isLocalHost());
				else
					ret.setData(pers.listTopLevelScreens(), pers.getTopLevelGroup().getScreenGroupId(), isLocalHost());
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public LoginReturn login(/*
			 * @WebParam(name = "in")
			 */final LoginInput in) {
		logger.info(in.getUser() + " tried to log in.");

		final LoginReturn ret = new LoginReturn();

		try {
			UserCache.LoginType loginType;
			String sLoginType = in.getLoginType();
			if (sLoginType == null  ||  "NORMAL".equals(sLoginType))
				loginType = UserCache.LoginType.NORMAL;
			else if (sLoginType.equals("WORKER"))
				loginType = UserCache.LoginType.WORKER;
			else if (sLoginType.equals("APPLICANT"))
				loginType = UserCache.LoginType.APPLICANT;
			else
				loginType = null;

			final boolean moreThanOneCompany = !checkLogin(in, loginType);

			if (ArahantSession.systemName().equalsIgnoreCase(in.getUser()))
				ret.setSuperUser(true);

			logger.info("Checked password");

			final BPerson bp = BPerson.getCurrent();

			logger.info("Got current user");

			if (BAgreementForm.listNotAccepted().length > 0) {
				ret.setScreen("com/arahant/app/screen/standard/misc/userAgreement/UserAgreementScreen.swf");
				ret.setScreenTitle("User Agreements");
			} else if (BProperty.getBoolean("ShowMessagesScreenDefault", true) && bp.hasMessages()) {
				ret.setScreen("com/arahant/app/screen/standard/misc/message/MessageScreen.swf");
				ret.setScreenTitle("Messages");
			} else {
				ret.setScreen("");
				ret.setScreenTitle("");
			}

			logger.info("Checked messages");

			ret.setPersonId(bp.getPersonId());
			ret.setPersonFName(bp.getFirstName());
			ret.setPersonLName(bp.getLastName());
			ret.setCanSendEmail(Email.canSendEmail(bp.getPersonalEmail()));

			logger.info("Set return data");


			//log the login
			logger.info("Recording the login");
			LoginLogId llid = new LoginLogId();
			llid.setLogName(in.getUser());
			llid.setLtime(new java.util.Date());

			String host = getRemoteHost();
			String url = getRemoteAddr();

			if (host.length() > 40)
				host = host.substring(0, 40);
			if (url.length() > 40)
				url = url.substring(0, 40);

			LoginLog ll = new LoginLog();
			ll.setId(llid);
			ll.setPersonId(bp.getPersonId());
			ll.setAddressIp(host);
			ll.setAddressUrl(url);
			ll.setSuccessful('Y');
			hsu.insert(ll);

			logger.info("Checking password strength");

			String password = in.getPassword();

			int minimumLength = BProperty.getInt(StandardProperty.PasswordMinimumLength);
			int minimumLetters = BProperty.getInt(StandardProperty.PasswordMinimumLetters);
			int minimumUpperCaseLetters = BProperty.getInt(StandardProperty.PasswordMinimumUpperCase);
			int minimumLowerCaseLetters = BProperty.getInt(StandardProperty.PasswordMinimumLowerCase);
			int minimumDigits = BProperty.getInt(StandardProperty.PasswordMinimumDigits);
			int minimumSpecialChars = BProperty.getInt(StandardProperty.PasswordMinimumSpecialChars);

			logger.info("Got strength preferences");

			ret.setHasMinimumPasswordRequirements(minimumLength > 0 || minimumLetters > 0 || minimumUpperCaseLetters > 0 || minimumLowerCaseLetters > 0 || minimumDigits > 0 || minimumSpecialChars > 0);

			if (password.length() < minimumLength)
				ret.setMeetsMinimumPasswordRequirements(false);
			else {
				int upperCount = 0;
				int lowerCount = 0;
				int digitCount = 0;
				int specialCount = 0;
				for (int loop = 0; loop < password.length(); loop++) {
					char c = password.charAt(loop);
					if (c >= 'A' && c <= 'Z')
						upperCount++;
					else if (c >= 'a' && c <= 'z')
						lowerCount++;
					else if (c >= '0' && c <= '9')
						digitCount++;
					else
						specialCount++;
				}

				if (digitCount < minimumDigits || upperCount < minimumUpperCaseLetters || lowerCount < minimumLowerCaseLetters || upperCount + lowerCount < minimumLetters || specialCount < minimumSpecialChars)
					ret.setMeetsMinimumPasswordRequirements(false);
			}

			logger.info("Checking password expiration");

			int expiresAfterDays = BProperty.getInt(StandardProperty.PasswordExpiresAfterDays);
			if (expiresAfterDays > 0)
//                - could be empty last changed date for migration
//                - need to update server code so that when it inserts new logins it sets password changed date ? or
//                   maybe we should leave it empty so it forces users to change their password the first time they log in?
//                - if last time changed is older than
				if (BPerson.getCurrent().passwordExpired())
					ret.setExpiredPasswordOlderThan(expiresAfterDays);
				
			logger.info("Getting allowed companies");
			List<CompanyDetail> compList = bp.getAllowedCompanies();
			if (compList.isEmpty()) {
				logger.info("Setting allowed companies to none");
				ret.setCompany(new LoginReturnItem[0]);				
			} else {
				LoginReturnItem[] lri = new LoginReturnItem[compList.size()];
				for (int loop = 0; loop < lri.length; loop++)
					lri[loop] = new LoginReturnItem(compList.get(loop));
				logger.info("Setting allowed companies");
				ret.setCompany(lri);
			}

			logger.info("checking auto clock in");
			if (BPerson.getCurrent().isAutoClockIn())
				BPerson.getCurrent().punchIn();

			if (BProperty.getBoolean("UserAgreement") && BPerson.getCurrent().getAgreementDate() == null)
				ret.setUserAgreement(userAgreement);

			if (!moreThanOneCompany)  // we don't have enough information to create a UUID
				ret.setUuid(UserCache.newUser(in.getUser(), in.getPassword(), bp.getPersonId(), hsu.getCurrentCompany().getCompanyId(), loginType));

			finishService(ret);
		} catch (final ArahantLoginException ae) {
			hsu.rollbackTransaction();
			ret.setScreen("");
			ret.setScreenTitle("");
			ret.setWsStatus(2);
			ret.setWsMessage(ae.getMessage());

			try {
				hsu.beginTransaction();
				//			log the login
				LoginLogId llid = new LoginLogId();
				llid.setLogName(in.getUser());
				llid.setLtime(new java.util.Date());

				String host = getRemoteHost();
				String url = getRemoteAddr();
				if (host.length() > 40)
					host = host.substring(0, 40);
				if (url.length() > 40)
					url = url.substring(0, 40);

				LoginLog ll = new LoginLog();
				ll.setId(llid);
				ll.setPersonId("");
				ll.setAddressIp(host);
				ll.setAddressUrl(url);

				ll.setSuccessful('N');

				hsu.insert(ll);
				hsu.commitTransaction();
			} catch (Exception ex) {
				//don't care, keep going
			}

		} catch (final Throwable e) {
			hsu.rollbackTransaction();
			logger.error(e);
			try {
				hsu.beginTransaction();
				//			log the login
				LoginLogId llid = new LoginLogId();
				llid.setLogName(in.getUser());
				llid.setLtime(new java.util.Date());

				String host = getRemoteHost();
				String url = getRemoteAddr();
				if (host.length() > 40)
					host = host.substring(0, 40);
				if (url.length() > 40)
					url = url.substring(0, 40);

				LoginLog ll = new LoginLog();
				ll.setId(llid);
				ll.setPersonId("");
				ll.setAddressIp(host);
				ll.setAddressUrl(url);

				ll.setSuccessful('N');

				hsu.insert(ll);
				hsu.commitTransaction();
			} catch (Exception ex) {
				//don't care, keep going
			}

		}
		if (hsu != null  &&  hsu.isOpen())
			ArahantSession.clearSession();

		ret.setSoftwareRevision(SourceCodeRevision.getSourceCodeRevisionNumber());

		logger.info(in.getUser() + " login response :" + ret.getScreenTitle());
		return ret;
	}

	@WebMethod()
	public SaveUserAgreementReturn saveUserAgreement(/*
			 * @WebParam(name = "in")
			 */final SaveUserAgreementInput in) {
		final SaveUserAgreementReturn ret = new SaveUserAgreementReturn();

		try {
			checkLogin(in);
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("VersionData.txt");
			Properties props = new Properties();
			props.load(is);
			is.close();

			String host = getRemoteHost();
			String url = getRemoteAddr();

			BPerson bp = new BPerson(ArahantSession.getCurrentPerson());
			bp.setAgreementAddressIp(host);
			bp.setAgreementAddressUrl(url);
			bp.setAgreementDate(new Date());
			bp.setAgreementRevision(Integer.parseInt((String) props.get("SourceCodeRevisionNumber")));
			bp.update();

			logger.info("User: " + bp.getNameFML() + " (" + in.getUser() + ") accepted user agreement. \n"
					+ "Client: " + host + " (" + url + ") \n"
					+ "Time: " + DateUtils.getDateAndTimeFormatted(new Date()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public GetUserAgreementReportReturn getUserAgreementReport(/*
			 * @WebParam(name = "in")
			 */final GetUserAgreementReportInput in) {
		final GetUserAgreementReportReturn ret = new GetUserAgreementReportReturn();
		try {

			ret.setReportUrl(new InstantPDF("End User License Agreement").build(userAgreement));

		} catch (final Exception e) {
			logger.error(e);
			ret.setReportUrl("");
		}
		if (hsu != null  &&  hsu.isOpen())
			ArahantSession.clearSession();
		return ret;
	}

	@WebMethod()
	public GetAnnouncementsReturn getAnnouncements(/*
			 * @WebParam(name = "in")
			 */final GetAnnouncementsInput in) {
		final GetAnnouncementsReturn ret = new GetAnnouncementsReturn();

		try {
			checkLogin(in);

			ret.setItem(BAnnouncement.getAnnouncements());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SingleSignOnReturn singleSignOn(/*
			 * @WebParam(name = "in")
			 */final SingleSignOnInput in) {
		final SingleSignOnReturn ret = new SingleSignOnReturn();

		boolean ssoLogging = BProperty.getBoolean("SSOLogging");

		try {

			hsu = ArahantSession.getHSU();
			if (hsu == null || !hsu.isOpen())
				hsu = ArahantSession.openHSU(true);
			hsu.beginTransaction();

			if (ssoLogging) {
				System.out.println("SystemInterface:  Begin MainOps.singleSignOn *****");
				System.out.println("SystemInterface:  ssoToken = " + in.getSsoToken());
			}
//////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////  SINGLE SIGN ON ////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////

			if (!isEmpty(in.getSsoToken())) {
				String host = getRemoteHost();
				String url = getRemoteAddr();
				String userGuid = LoginToken.getUserGuid(in.getSsoToken(), host);
				if (!isEmpty(userGuid)) {
					Person p = hsu.createCriteriaNoCompanyFilter(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSON_GUID, userGuid).first();
					if (p != null && p.getProphetLogin() != null && p.getProphetLogin().getCanLogin() == 'Y') {
						BPerson bp = new BPerson(p);
						hsu.setCurrentPerson(p);
						ret.setCompanyId(p.getCompanyBase().getOrgGroupId());
						ret.setCompanyName(p.getCompanyBase().getName());
						ret.setPassword(bp.getUserPassword());
						ret.setUsername(p.getProphetLogin().getUserLogin());
						ret.setPersonFName(p.getFname());
						ret.setPersonLName(p.getLname());
						ret.setPersonId(p.getPersonId());
						ret.setScreen("");
						ret.setScreenTitle("");
						ret.setSuperUser(hsu.currentlySuperUser());
						if (ssoLogging) {
							System.out.println("SystemInterface:  Person found! userName = " + ret.getUsername());
							System.out.println("SystemInterface:  Person found! password = "); // + ret.getPassword());
							System.out.println("SystemInterface:  Person found! companyId = " + ret.getCompanyId() + " (" + ret.getCompanyName() + ") ");
						}
						try {
							//			log the login
							LoginLogId llid = new LoginLogId();
							llid.setLogName(ret.getUsername());
							llid.setLtime(new java.util.Date());
							if (host.length() > 40)
								host = host.substring(0, 40);
							if (url.length() > 40)
								url = url.substring(0, 40);

							LoginLog ll = new LoginLog();
							ll.setId(llid);
							ll.setPersonId(ret.getPersonId());
							ll.setAddressIp(host);
							ll.setAddressUrl(url);

							ll.setSuccessful('Y');

							hsu.insert(ll);
						} catch (Exception ex) {
							//don't care, keep going
						}
					} else {
						if (ssoLogging)
							if (p == null  ||  p.getProphetLogin() == null)
								System.out.println("SystemInterface:  Person does not have an " + ArahantSession.systemName() + " login ");
						try {
							//			log the login
							LoginLogId llid = new LoginLogId();
							llid.setLogName("[SSO]");
							llid.setLtime(new java.util.Date());
							if (host.length() > 40)
								host = host.substring(0, 40);
							if (url.length() > 40)
								url = url.substring(0, 40);

							LoginLog ll = new LoginLog();
							ll.setId(llid);
							ll.setPersonId("");
							ll.setAddressIp(host);
							ll.setAddressUrl(url);

							ll.setSuccessful('N');

							hsu.insert(ll);
						} catch (Exception ex) {
							//don't care, keep going
						}
						throw new ArahantWarning("Single Sign On attempt failed!");
					}
				} else
					throw new ArahantWarning("Single Sign On attempt failed!");

			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		if (ssoLogging)
			System.out.println("SystemInterface:  End MainOps.singleSignOn *******");

		return ret;
	}

	@WebMethod()
	public LoadPreferencesReturn loadPreferences(/*
			 * @WebParam(name = "in")
			 */final LoadPreferencesInput in) {
		final LoadPreferencesReturn ret = new LoadPreferencesReturn();
		try {
			checkLogin(in);

			final BPerson current = new BPerson(hsu.getCurrentPerson());

			/*
			 * System.out.println(""); System.out.println("Load Preference: " +
			 * current.getNumericPreference(UserAttribute.PRESET_COLOR_THEME));
			 * System.out.println("Load Preference: " +
			 * current.getNumericPreference(UserAttribute.USE_PRESET_COLOR_THEME));
			 * System.out.println("Load Preference: " +
			 * current.getNumericColorPreference(UserAttribute.BACKGROUND_COLOR_1));
			 * System.out.println("Load Preference: " +
			 * current.getNumericColorPreference(UserAttribute.BACKGROUND_COLOR_2));
			 * System.out.println("Load Preference: " +
			 * current.getNumericColorPreference(UserAttribute.UNHIGHLIGHTED_BUTTON_COLOR));
			 * System.out.println("Load Preference: " +
			 * current.getNumericColorPreference(UserAttribute.HIGHLIGHTED_BUTTON_COLOR));
			 * System.out.println("Load Preference: " +
			 * current.getNumericColorPreference(UserAttribute.UNHIGHLIGHTED_TEXT_COLOR));
			 * System.out.println("Load Preference: " +
			 * current.getNumericColorPreference(UserAttribute.HIGHLIGHTED_TEXT_COLOR));
                        System.out.println("-----------------------------------------");
			 */


			BProperty bp = new BProperty("colorThemeOverride");
			if (bp.getValue().equals("")) {
				ret.setColorThemeOverride("");
				ret.setPresetColorThemeChoice(current.getNumericPreference(UserAttribute.PRESET_COLOR_THEME));
				ret.setUsePresetColorTheme(current.getNumericPreference(UserAttribute.USE_PRESET_COLOR_THEME));
				ret.setBackgroundColor1(current.getNumericColorPreference(UserAttribute.BACKGROUND_COLOR_1));
				ret.setBackgroundColor2(current.getNumericColorPreference(UserAttribute.BACKGROUND_COLOR_2));
				ret.setUnhighlightedButtonColor(current.getNumericColorPreference(UserAttribute.UNHIGHLIGHTED_BUTTON_COLOR));
				ret.setHighlightedButtonColor(current.getNumericColorPreference(UserAttribute.HIGHLIGHTED_BUTTON_COLOR));
				ret.setUnhighlightedTextColor(current.getNumericColorPreference(UserAttribute.UNHIGHLIGHTED_TEXT_COLOR));
				ret.setHighlightedTextColor(current.getNumericColorPreference(UserAttribute.HIGHLIGHTED_TEXT_COLOR));
			} else {
				ret.setColorThemeOverride(bp.getValue());
				ret.setPresetColorThemeChoice(current.getNumericPreference(UserAttribute.PRESET_COLOR_THEME));
				ret.setUsePresetColorTheme(current.getNumericPreference(UserAttribute.USE_PRESET_COLOR_THEME));
				ret.setBackgroundColor1(current.getNumericColorPreference(UserAttribute.BACKGROUND_COLOR_1));
				ret.setBackgroundColor2(current.getNumericColorPreference(UserAttribute.BACKGROUND_COLOR_2));
				ret.setUnhighlightedButtonColor(current.getNumericColorPreference(UserAttribute.UNHIGHLIGHTED_BUTTON_COLOR));
				ret.setHighlightedButtonColor(current.getNumericColorPreference(UserAttribute.HIGHLIGHTED_BUTTON_COLOR));
				ret.setUnhighlightedTextColor(current.getNumericColorPreference(UserAttribute.UNHIGHLIGHTED_TEXT_COLOR));
				ret.setHighlightedTextColor(current.getNumericColorPreference(UserAttribute.HIGHLIGHTED_TEXT_COLOR));
			}

			bp = new BProperty("helpUrl");
			if (bp.getValue().equals(""))
				ret.setHelpUrl("");
			else
				ret.setHelpUrl(bp.getValue());

			//ret.setPresetColorThemeChoice(current.getNumericPreference(UserAttribute.COLOR_THEME));
			ret.setLayout(current.getNumericPreference(UserAttribute.LAYOUT));
			if (current.getTopLevelGroup() != null && current.getTopLevelGroup().getWizardType().equals("E")) {
				ret.setMenuBarHideAnimationStyle(1);    //immediate
				ret.setMenuBarShowAnimationStyle(1);    //immediate
				ret.setHistoryBarHideAnimationStyle(1); //immediate
				ret.setHistoryBarShowAnimationStyle(1); //immediate
			} else {
				ret.setMenuBarHideAnimationStyle(current.getNumericPreference(UserAttribute.MENU_BAR_HIDE));
				ret.setMenuBarShowAnimationStyle(current.getNumericPreference(UserAttribute.MENU_BAR_SHOW));
				ret.setHistoryBarHideAnimationStyle(current.getNumericPreference(UserAttribute.HISTORY_BAR_HIDE));
				ret.setHistoryBarShowAnimationStyle(current.getNumericPreference(UserAttribute.HISTORY_BAR_SHOW));
			}
			ret.setInactiveUserMaxSeconds(BProperty.getInt("InactiveUserMaxSeconds", 1800)); // 30 mins
			ret.setInactiveUserAlertMaxSeconds(BProperty.getInt("InactiveUserAlertMaxSeconds", 30)); // 30 secs
			ret.setInventoryLabel(new BProperty(StandardProperty.SKU).getValue());
			ret.setAraChat(BProperty.getBoolean("AraChat"));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

//			MessageContext msgx = wsContext.getMessageContext();
//
//			HttpServletRequest req = (HttpServletRequest)
//				msgx.get(MessageContext.SERVLET_REQUEST);
//			System.out.println("Client: " +
//				   req.getRemoteHost() + " (" +
//				   req.getRemoteAddr() + ").");
//
//			//System.out.println(req.getRemoteUser());
//
//			System.out.println("instanceID = " + instanceID + ", " + insNum);
//			insNum++;
	@WebMethod()
	public SavePreferencesReturn savePreferences(/*
			 * @WebParam(name = "in")
			 */final SavePreferencesInput in) {
		final SavePreferencesReturn ret = new SavePreferencesReturn();

		try {
			checkLogin(in);

			/*
			 * System.out.println(""); System.out.println("Save Preference: " +
			 * in.getPresetColorThemeChoice()); System.out.println("Save
			 * Preference: " + in.getUsePresetColorTheme());
			 * System.out.println("Save Preference: " +
			 * in.getBackgroundColor1()); System.out.println("Save Preference: "
			 * + in.getBackgroundColor2()); System.out.println("Save Preference:
			 * " + in.getUnhighlightedButtonColor()); System.out.println("Save
			 * Preference: " + in.getHighlightedButtonColor());
			 * System.out.println("Save Preference: " +
			 * in.getUnhighlightedTextColor()); System.out.println("Save
			 * Preference: " + in.getHighlightedTextColor());
                        System.out.println("-----------------------------------------");
			 */

			final BPerson current = new BPerson(hsu.getCurrentPerson());
			current.savePreference(UserAttribute.PRESET_COLOR_THEME, in.getPresetColorThemeChoice());
			current.savePreference(UserAttribute.USE_PRESET_COLOR_THEME, in.getUsePresetColorTheme());
			current.savePreference(UserAttribute.BACKGROUND_COLOR_1, in.getBackgroundColor1());
			current.savePreference(UserAttribute.BACKGROUND_COLOR_2, in.getBackgroundColor2());
			current.savePreference(UserAttribute.UNHIGHLIGHTED_BUTTON_COLOR, in.getUnhighlightedButtonColor());
			current.savePreference(UserAttribute.HIGHLIGHTED_BUTTON_COLOR, in.getHighlightedButtonColor());
			current.savePreference(UserAttribute.UNHIGHLIGHTED_TEXT_COLOR, in.getUnhighlightedTextColor());
			current.savePreference(UserAttribute.HIGHLIGHTED_TEXT_COLOR, in.getHighlightedTextColor());
			current.savePreference(UserAttribute.HISTORY_BAR_HIDE, in.getHistoryBarHideAnimationStyle());
			current.savePreference(UserAttribute.HISTORY_BAR_SHOW, in.getHistoryBarShowAnimationStyle());
			current.savePreference(UserAttribute.LAYOUT, in.getLayout());
			current.savePreference(UserAttribute.MENU_BAR_HIDE, in.getMenuBarHideAnimationStyle());
			current.savePreference(UserAttribute.MENU_BAR_SHOW, in.getMenuBarShowAnimationStyle());


			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public GetReleaseDetailsReturn getReleaseDetails(/*
			 * @WebParam(name = "in")
			 */final GetReleaseDetailsInput in) {
		final GetReleaseDetailsReturn ret = new GetReleaseDetailsReturn();
		try {
			//	checkLogin(in);
			checkNoLogin(in);

			ArahantSession.openHSU();
            
			ret.setDetail(getReleaseData(in.getUser(), in.getPassword(), in.getContextCompanyId(), ret));
            ret.setMemoryStats();

			ArahantSession.clearSession();
			//	finishService(ret);
		} catch (final Exception e) {
			logger.error(e);
			ret.setDetail("");
		}
		if (hsu != null  &&  hsu.isOpen())
			ArahantSession.clearSession();
		return ret;
	}

	private boolean configCoversEmployeeOnly(HrBenefit b) {
		BHRBenefitConfig bc = new BHRBenefitConfig(b.getBenefitConfigs().iterator().next());
		return (bc.getCoversEmployee() && !bc.getCoversChildren() && !bc.getCoversEmployeeSpouse() && !bc.getCoversEmployeeSpouseOrChildren() && !bc.getSpouseNonEmpOrChildren() && !bc.getSpouseNonEmployee());
	}

	private String getReleaseData(String userId, String password, String companyId, GetReleaseDetailsReturn ret) throws IOException, SQLException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("VersionData.txt");
		InputStream bis = this.getClass().getClassLoader().getResourceAsStream("buildConfig.properties");
		Properties props = new Properties();
		Properties bprops = new Properties();

		props.load(is);
		bprops.load(bis);
		is.close();
		bis.close();

		StringBuilder sb = new StringBuilder();
		
		boolean goodUser = !isEmpty(userId) && !isEmpty(password) && BProphetLogin.checkPassword(userId, password);
		boolean displayNullAbout = BProperty.getBoolean("displayNullAbout", false);

		if (!displayNullAbout) {
			sb.append("Name: ");
			sb.append(props.get("Name"));
		}
		sb.append("\nCurrent Time: ");
		sb.append(new java.util.Date());
		
		if (goodUser) {
			hsu = ArahantSession.getHSU();
			Person per = ArahantSession.getCurrentPerson();
			sb.append("\nUser ID:  ").append(userId).append(" (").append(IDGenerator.shrinkKey(per.getPersonId())).append(")");
			sb.append("\nUser Name:  ").append(per.getNameLFM());
			BCompany bc = new BCompany(companyId);
			sb.append("\nCompany logged into:  ").append(bc.getName()).append(" (").append(IDGenerator.shrinkKey(bc.getOrgGroupId())).append(")");
			ScreenGroup scrng = per.getProphetLogin().getScreenGroup();
			if (!hsu.currentlyArahantUser())
				sb.append("\nCompany Person is a member of:  ").append(per.getCompanyBase().getName()).append(" (").append(IDGenerator.shrinkKey(per.getCompanyBase().getOrgGroupId())).append(")");
			sb.append("\nScreen Group:  ").append(scrng.getName()).append(" (").append(IDGenerator.shrinkKey(scrng.getScreenGroupId())).append(")");
			if (!hsu.currentlyArahantUser()) {
				SecurityGroup sec = per.getProphetLogin().getSecurityGroup();
				sb.append("\nSecurity Group:  ").append(sec.getName()).append(" (").append(IDGenerator.shrinkKey(sec.getSecurityGroupId())).append(")");
			}
			sb.append("\n");
		}
		
		sb.append("\nVersion: ");
		sb.append(props.get("Version"));
		sb.append("\nConfiguration: ");
		sb.append(bprops.get("config"));
		sb.append("\nBuild Date: ");
		sb.append(props.get("BuildDate"));

		sb.append("\nSource Code Revision Number: ");
		sb.append(props.get("SourceCodeRevisionNumber"));

		if (ret != null) {
			ret.setVersion((String) props.get("Version"));
			ret.setBuildDate((String) props.get("BuildDate"));
			ret.setName((String) props.get("Name"));
			ret.setSourceCodeRevisionNumber((String) props.get("SourceCodeRevisionNumber"));
			ret.setSourceCodeRevisionPath((String) props.get("SourceCodeRevisionPath"));
			ret.setApplicationPath(FileSystemUtils.getWorkingDirectory().getAbsolutePath());
			ret.setDatabase(StartupListener.databaseURL.substring(5));
		}
		
		sb.append("\nSource Code Revision Path: ");
		String src_path = (String) props.get("SourceCodeRevisionPath");
		try {
			src_path = src_path.substring(src_path.indexOf('/', 3));
		} catch (Exception e) {
			src_path = "no source information available";
		}
		if (displayNullAbout)
			src_path = src_path.replaceAll(ArahantSession.systemName(), "...");
		sb.append(src_path);
		
		sb.append("\nUse Daylight Savings: ");
		sb.append(Calendar.getInstance().getTimeZone().useDaylightTime());

		if (goodUser  &&  hsu.currentlySuperUser()) {
			sb.append("\n\nUp Since or Uptime: ");
			sb.append(DateUtils.dateFormat("MM/dd/yyyy kk:mm:ss", ArahantSession.getUpTime()));

			String host = "Unknown Host";
			String ip = "Unknown IP Address";
			try {
				InetAddress addr = InetAddress.getLocalHost();
				String ipAddress = addr.getHostAddress();
				String hostName = addr.getHostName();

				if (ipAddress != null && ipAddress.length() > 0)
					ip = ipAddress;

				if (!ip.equals(hostName))
					host = hostName;

			} catch (UnknownHostException e) {
			}
			sb.append("\n\nApplication Machine: ");
			sb.append(host).append(" [").append(ip).append("]");
			sb.append("\nApplication OS: ");
			sb.append(System.getProperty("os.name")).append(" ").append(System.getProperty("os.version"));
			sb.append("\nApplication Path: ");
			String app_path = ArahantSession.getLocation();
			if (displayNullAbout) {
				app_path = app_path.replaceAll(ArahantSession.systemName(), "...");
				app_path = app_path.replaceAll("arahant", "...");
			}
			sb.append(app_path);


			DatabaseMetaData dmd = ArahantSession.getHSU().getConnection().getMetaData();
			sb.append("\n\nDatabase Server Type: ");
			sb.append(dmd.getDatabaseProductName()).append(" ").append(dmd.getDatabaseProductVersion());
			sb.append("\nDatabase: ");
			sb.append(dmd.getURL());
			sb.append("\nDatabase User: ");
			sb.append(dmd.getUserName()).append("\n");

			sb.append("\n");

			Runtime rt = Runtime.getRuntime();
			//rt.gc();

			sb.append("Max memory available to Java:  ").append(formatMemory(rt.maxMemory())).append("\n");
			long tm = rt.totalMemory();
			long fm = rt.freeMemory();
			sb.append("Total allocated memory:  ").append(formatMemory(tm)).append("\n");
			sb.append("Free memory:  ").append(formatMemory(fm)).append("\n");
			sb.append("Memory used:  ").append(formatMemory(tm - fm)).append("\n");
		}

		return sb.toString();
	}

	private static String formatMemory(long v) {
		return StringUtils.sprintf("%,d MB", v / (1024L * 1024L));
	}

	@WebMethod()
	public GetReleaseDetailsReportReturn getReleaseDetailsReport(/*
			 * @WebParam(name = "in")
			 */final GetReleaseDetailsReportInput in) {
		final GetReleaseDetailsReportReturn ret = new GetReleaseDetailsReportReturn();
		try {

			ret.setReportUrl(new InstantPDF("Release Details").build(getReleaseData(in.getUser(), in.getPassword(), in.getContextCompanyId(), null)));

		} catch (final Exception e) {
			logger.error(e);
			ret.setReportUrl("");
		}
		if (hsu != null  &&  hsu.isOpen())
			ArahantSession.clearSession();
		return ret;
	}

	@WebMethod()
	public ListCompaniesReturn listCompanies(/*
			 * @WebParam(name = "in")
			 */final ListCompaniesInput in) {
		final ListCompaniesReturn ret = new ListCompaniesReturn();
		try {
			checkLogin(in);

			ret.setItem(BPerson.getCurrent().getAllowedCompanies());

			final BPerson bp = BPerson.getCurrent();


			if (bp.hasMessages()) {
				ret.setScreen("com/arahant/app/screen/standard/misc/message/MessageScreen.swf");
				ret.setScreenTitle("Messages");
			} else {
				ret.setScreen("");
				ret.setScreenTitle("");
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ChangePasswordReturn changePassword(/*
			 * @WebParam(name = "in")
			 */final ChangePasswordInput in) {
		final ChangePasswordReturn ret = new ChangePasswordReturn();

		try {
			checkLogin(in);

            UserCache.UserData ud = UserCache.findUuid(in.getUuid());
			if (!BProphetLogin.checkPassword(ud.username, in.getPassword()))
                throw new ArahantWarning("Invalid Old Password.");
			else
                BPerson.getCurrent().setNewPassword(in.getNewPassword());
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public LoadPasswordRulesReturn loadPasswordRules(/*
			 * @WebParam(name = "in")
			 */final LoadPasswordRulesInput in) {
		final LoadPasswordRulesReturn ret = new LoadPasswordRulesReturn();
		try {
			checkLogin(in);



			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetUnauthenticatedMetaReturn getUnauthenticatedMeta(/*
			 * @WebParam(name = "in")
			 */final GetUnauthenticatedMetaInput in) {
		final GetUnauthenticatedMetaReturn ret = new GetUnauthenticatedMetaReturn();
		ret.setYear(SourceCodeRevision.getBuildYear());
		if (hsu != null  &&  hsu.isOpen())
			ArahantSession.clearSession();
		return ret;
	}

	@WebMethod()
	public LogoutReturn logout(/*
			 * @WebParam(name = "in")
			 */final LogoutInput in) {
		final LogoutReturn ret = new LogoutReturn();
		try {
			checkLogin(in);

			if (BPerson.getCurrent().isAutoClockIn())
				BPerson.getCurrent().punchOut();

			UserCache.removeUuid(in.getUuid());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchCompaniesReturn searchCompanies(/*
			 * @WebParam(name = "in")
			 */final SearchCompaniesInput in) {
		final SearchCompaniesReturn ret = new SearchCompaniesReturn();
		try {
			checkLogin(in);

			ret.setItem(BCompany.search(in.getName(), ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetSysDataReturn getSysData(/*
			 * @WebParam(name = "in")
			 */final GetSysDataInput in) {
		final GetSysDataReturn ret = new GetSysDataReturn();
		try {
			String msg = Crypto.decryptTripleDES(in.getSysData());

			logger.info("GetSysData decrypted value from HRSP -> " + msg);

			String login = msg.substring(0, msg.indexOf("<DCMARAHANT>"));

			msg = msg.substring(msg.indexOf("<DCMARAHANT>") + "<DCMARAHANT>".length());

			String password = msg.substring(0, msg.indexOf("<DCMARAHANT>"));

			msg = msg.substring(msg.indexOf("<DCMARAHANT>") + "<DCMARAHANT>".length());

			String returnUrl = msg.substring(0, msg.indexOf("<DCMARAHANT>"));

			msg = msg.substring(msg.indexOf("<DCMARAHANT>") + "<DCMARAHANT>".length());

			String date = msg;

			int dt = Integer.parseInt(date);
			if (dt != DateUtils.now())
				throw new ArahantLoginException("Login credentials have expired.  Please log in again.");

			in.setUser(login);
			in.setPassword(password);

			checkLogin(in);

			ret.setLogin(login);
			ret.setPassword(password);
			ret.setUrl(returnUrl);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	/*
	 *
	 * public static void main (String []args) { MainOps ops=new MainOps();
	 * GetSysDataInput in=new GetSysDataInput();
	 * in.setSysData("db0523b04b3df4120c1030b3ea7381e4c0f0fe4c9b3747e53dadba416c89af20968860bcb15643840e893c36f73cd89a968860bcb15643843d855c1b8e92ca6bf2d5fe2a40f608cd");
	 * GetSysDataReturn r=ops.getSysData(in); System.out.println(r.getLogin());
	 * System.out.println(r.getPassword()); System.out.println(r.getUrl());
	 *
	 * }
	 */
	@WebMethod()
	public GetLoginPropertiesReturn getLoginProperties(/*
			 * @WebParam(name = "in")
			 */final GetLoginPropertiesInput in) {
		final GetLoginPropertiesReturn ret = new GetLoginPropertiesReturn();
		try {
			//checkLogin(in);		user hasn't filled in password leave commented out
			checkNoLogin(in);
			ArahantSession.openHSU();
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CompleteTaskReturn completeTask(/*
			 * @WebParam(name = "in")
			 */final CompleteTaskInput in) {
		final CompleteTaskReturn ret = new CompleteTaskReturn();
		try {
			checkLogin(in);

			String empId = isEmpty(in.getPersonId()) ? BPerson.getCurrent().getPersonId() : in.getPersonId();
			BPerson bp = new BPerson(empId);
			BHRCheckListItem cli = new BHRCheckListItem();
			if (isEmpty(in.getTaskId())) {
				HibernateCriteriaUtil<HrChecklistItem> hcu = ArahantSession.getHSU().createCriteria(HrChecklistItem.class).eq(HrChecklistItem.HREMPLOYEESTATUS, bp.getBEmployee().getLastActiveStatusHistory().getHrEmployeeStatus());
				if (!isEmpty(in.getScreenId()))
					hcu.eq(HrChecklistItem.SCREEN, new BScreen(in.getScreenId()).getScreenId());
				HrChecklistItem cl = hcu.first();
				if (cl != null)
					cli = new BHRCheckListItem(cl);
			} else
				cli = new BHRCheckListItem(in.getTaskId());

			if (cli.getBean() != null) {
				BHRCheckListDetail cld = new BHRCheckListDetail();
				cld.create();
				cld.setEmployeeId(empId);
				cld.setSupervisorId(empId);
				cld.setChecklistItemId(cli.getItemId());
				cld.setDateCompleted(DateUtils.now());
				cld.setTimeCompleted(DateUtils.nowTime());
				cld.insert();
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	//THIS MAIN AND 2 FUNCTIONS
	//IS FOR GENERATING WIZARD CONFIGURATION DEFAULTS 

	public static void main(String[] args) {

		String projectStatusId = BProjectStatus.findOrMake("Wizard Default");
		String projectCategoryId = BProjectCategory.findOrMake("Wizard Default");
		String projectTypeId = BProjectType.findOrMake("Wizard Default");

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.commitTransaction();
		hsu.beginTransaction();

		String sqlString = "";
		int wizardConfigurationCount = hsu.createCriteria(WizardConfiguration.class).count() + 1;
		int categoryCount = hsu.createCriteria(WizardConfigurationCategory.class).count() + 1;
		int benefitCount = hsu.createCriteria(WizardConfigurationBenefit.class).count() + 1;
		int configCount = hsu.createCriteria(WizardConfigurationConfig.class).count() + 1;

		List<String> companyNames = new ArrayList<String>();

//		companyNames.add("The WW Group");
		companyNames.add("CAS Resources, Inc.");
//		companyNames.add("New Passages");
//		companyNames.add("ESI North America");
//		companyNames.add("Detroit Regional Chamber");
//		companyNames.add("Navigating Business Space");
//		companyNames.add("Agent Benefits");
//		companyNames.add("Imagine Schools, Inc");
//		companyNames.add("Complete Fitness Rehabilitation, Inc.");
//		companyNames.add("Mason Tool and Die");

		List<CompanyDetail> companies = hsu.createCriteria(CompanyDetail.class).in(CompanyDetail.NAME, companyNames).list();
		//ScreenGroup screenGroup = hsu.get(ScreenGroup.class, "00001-0000000327");
		for (CompanyDetail cd : companies)
			System.out.println(cd.getName() + " found.");
		HashMap<String, Integer> wizConfSeqNos = new HashMap<String, Integer>();
		HashMap<String, Integer> categorySeqNos = new HashMap<String, Integer>();
		HashMap<String, Integer> benefitSeqNos = new HashMap<String, Integer>();
		try {
			for (CompanyDetail company : companies) {
				System.out.println("Doing " + company.getName());
				hsu.setCurrentCompany(company);
				List<BenefitClass> benefitClasses = hsu.createCriteria(BenefitClass.class).eq(BenefitClass.ORG_GROUP, company).list();

				for (BenefitClass benefitClass : benefitClasses) {
					Employee emp = hsu.createCriteria(Employee.class).eq(Employee.BENEFIT_CLASS, benefitClass).first();
					if (emp == null)
						System.out.println("    class " + benefitClass.getName() + " has no employees.");
					else {
						String wizConfId = (wizardConfigurationCount < 10 ? "00000-000000000" + wizardConfigurationCount : (wizardConfigurationCount < 100 ? "00000-00000000" + wizardConfigurationCount : "00000-0000000" + wizardConfigurationCount));
						wizardConfigurationCount++;
						sqlString += "INSERT INTO wizard_configuration VALUES ('" + wizConfId + "','" + (benefitClass.getName().length() > 20 ? benefitClass.getName().substring(0, 20) : benefitClass.getName()) + "','" + "Default" + "','" + "E" + "','" + "N" + "','" + "Y" + "','" + "N" + "','" + "N" + "','" + "Y" + "','" + "Y" + "','" + "N" + "','" + "Y" + "','" + "Y" + "','" + company.getOrgGroupId() + "','" + benefitClass.getBenefitClassId() + "','" + "Default" + "','" + projectStatusId + "','" + projectCategoryId + "','" + projectTypeId + "','" + "2');";
						wizConfSeqNos.put(wizConfId, 0);
						System.out.println("    class " + benefitClass.getName() + " using " + emp.getNameFL());
						ListScreensAndGroupsInput in = new ListScreensAndGroupsInput();
						ListScreensAndGroupsReturn ret = new ListScreensAndGroupsReturn();
						in.setEmployeeId(emp.getPersonId());
						in.setGroupId("00001-0000000327");
						in.setDepth(4);
						MainOps mainOps = new MainOps();
						mainOps.doWiz(in, ret, company);
						System.out.println("        Returned " + ret.getItem().length + " wizard screens.");
						HashMap<String, String> nonExclusiveCategories = new HashMap<String, String>();
						HashMap<String, String> flexBenefits = new HashMap<String, String>();
						HashMap<String, String> flexCategories = new HashMap<String, String>();
						for (int i = 4; i < ret.getItem().length - 2; i++) {
							ListScreensAndGroupsItem item = ret.getItem()[i];
							if (item.getScreenFile().toLowerCase().contains("category")) {
								BHRBenefitCategory bBenefitCategory = new BHRBenefitCategory(item.getContextId());
								List<HrBenefit> benefits = mainOps.getBenefitsForCategory(bBenefitCategory, benefitClass);
								if (benefits.isEmpty())
									continue;
								String wizCatId = (categoryCount < 10 ? "00000-000000000" + categoryCount : (categoryCount < 100 ? "00000-00000000" + categoryCount : (categoryCount < 1000 ? "00000-0000000" + categoryCount : "00000-000000" + categoryCount)));
								categoryCount++;
								categorySeqNos.put(wizCatId, 0);
								List<String> l = new ArrayList<String>();
								l.add(wizCatId);
								l.add(wizConfId);
								l.add(String.valueOf(wizConfSeqNos.get(wizConfId)));
								l.add(item.getContextId());
								l.add(null);
								l.add(bBenefitCategory.getDescription());
								l.add(item.getAvatarPath());
								l.add("0 ");
								l.add(bBenefitCategory.getInstructions());
								sqlString += mainOps.insertInto("wizard_config_category", l);
								wizConfSeqNos.put(wizConfId, wizConfSeqNos.get(wizConfId) + 1);

								System.out.println("            Doing Category Screen (" + item.getContextId() + ") - found " + benefits.size() + " benefits");
								for (HrBenefit benefit : benefits) {
									BHRBenefit bBenefit = new BHRBenefit(benefit);
									String wizBenId = (benefitCount < 10 ? "00000-000000000" + benefitCount : (benefitCount < 100 ? "00000-00000000" + benefitCount : (benefitCount < 1000 ? "00000-0000000" + benefitCount : "00000-000000" + benefitCount)));
									benefitCount++;
									benefitSeqNos.put(wizBenId, 0);
									List<String> bl = new ArrayList<String>();
									bl.add(wizBenId);
									bl.add(wizCatId);
									bl.add(String.valueOf(categorySeqNos.get(wizCatId)));
									bl.add(bBenefit.getBenefitId());
									bl.add(null);
									bl.add(bBenefit.getName());
									bl.add(item.getAvatarPath());
									bl.add("0 ");
									bl.add(bBenefit.getAdditionalInstructions());
									sqlString += mainOps.insertInto("wizard_config_benefit", bl);
									categorySeqNos.put(wizCatId, categorySeqNos.get(wizCatId) + 1);

									for (BHRBenefitConfig bConfig : new BEmployee(emp).getAllValidConfigs(benefit.getBenefitId())) {
										String wizConId = (configCount < 10 ? "00000-000000000" + configCount : (configCount < 100 ? "00000-00000000" + configCount : (configCount < 1000 ? "00000-0000000" + configCount : (configCount < 10000 ? "00000-000000" + configCount : "00000-00000" + configCount))));
										configCount++;
										List<String> cl = new ArrayList<String>();
										cl.add(wizConId);
										cl.add(wizBenId);
										cl.add(String.valueOf(benefitSeqNos.get(wizBenId)));
										cl.add(bConfig.getBenefitConfigId());
										cl.add(bConfig.getName());
										sqlString += mainOps.insertInto("wizard_config_config", cl);
										benefitSeqNos.put(wizBenId, benefitSeqNos.get(wizBenId) + 1);
									}
								}
							} else if (item.getScreenFile().toLowerCase().contains("flex")) {
								List<HrBenefitConfig> flexConfigs = mainOps.getFlexBenefitConfigs(benefitClass);
								System.out.println("            Doing Flex Screen (" + item.getContextId() + ") - found " + flexConfigs.size() + " flex benefit configs");
								for (HrBenefitConfig flexConfig : flexConfigs) {
									BHRBenefitConfig bConfig = new BHRBenefitConfig(flexConfig);
									String wizCatId = flexCategories.get(bConfig.getCategoryId());
									if (wizCatId == null || wizCatId.length() == 0) {
										BHRBenefitCategory bBenefitCategory = new BHRBenefitCategory(bConfig.getCategoryId());
										wizCatId = (categoryCount < 10 ? "00000-000000000" + categoryCount : (categoryCount < 100 ? "00000-00000000" + categoryCount : (categoryCount < 1000 ? "00000-0000000" + categoryCount : "00000-000000" + categoryCount)));
										categoryCount++;
										categorySeqNos.put(wizCatId, 0);
										List<String> l = new ArrayList<String>();
										l.add(wizCatId);
										l.add(wizConfId);
										l.add(String.valueOf(wizConfSeqNos.get(wizConfId)));
										l.add(bConfig.getCategoryId());
										l.add(null);
										l.add(bBenefitCategory.getDescription());
										l.add("");
										l.add("0 ");
										l.add(bBenefitCategory.getInstructions());
										sqlString += mainOps.insertInto("wizard_config_category", l);
										wizConfSeqNos.put(wizConfId, wizConfSeqNos.get(wizConfId) + 1);
										nonExclusiveCategories.put(bBenefitCategory.getCategoryId(), wizCatId);
									}
									String wizBenId = flexBenefits.get(bConfig.getBenefitId());
									if (wizBenId == null || wizBenId.length() == 0) {
										BHRBenefit bBenefit = new BHRBenefit(bConfig.getBenefitId());
										wizBenId = (benefitCount < 10 ? "00000-000000000" + benefitCount : (benefitCount < 100 ? "00000-00000000" + benefitCount : (benefitCount < 1000 ? "00000-0000000" + benefitCount : "00000-000000" + benefitCount)));
										benefitCount++;
										benefitSeqNos.put(wizBenId, 0);
										List<String> bl = new ArrayList<String>();
										bl.add(wizBenId);
										bl.add(wizCatId);
										bl.add(String.valueOf(categorySeqNos.get(wizCatId)));
										bl.add(bBenefit.getBenefitId());
										bl.add(null);
										bl.add(bBenefit.getName());
										bl.add(bBenefit.getAvatarPath());
										bl.add("0 ");
										bl.add(bBenefit.getAdditionalInstructions());
										sqlString += mainOps.insertInto("wizard_config_benefit", bl);
										categorySeqNos.put(wizCatId, categorySeqNos.get(wizCatId) + 1);
									}
									String wizConId = (configCount < 10 ? "00000-000000000" + configCount : (configCount < 100 ? "00000-00000000" + configCount : (configCount < 1000 ? "00000-0000000" + configCount : (configCount < 10000 ? "00000-000000" + configCount : "00000-00000" + configCount))));
									configCount++;
									List<String> cl = new ArrayList<String>();
									cl.add(wizConId);
									cl.add(wizBenId);
									cl.add(String.valueOf(benefitSeqNos.get(wizBenId)));
									cl.add(bConfig.getBenefitConfigId());
									cl.add(bConfig.getName());
									sqlString += mainOps.insertInto("wizard_config_config", cl);
									benefitSeqNos.put(wizBenId, benefitSeqNos.get(wizBenId) + 1);
								}
							} else {
								BHRBenefit bBenefit = new BHRBenefit(item.getContextId());
								String wizCatId = nonExclusiveCategories.get(bBenefit.getCategoryId());
								if (wizCatId == null || wizCatId.length() == 0) {
									BHRBenefitCategory bBenefitCategory = new BHRBenefitCategory(bBenefit.getCategoryId());
									wizCatId = (categoryCount < 10 ? "00000-000000000" + categoryCount : (categoryCount < 100 ? "00000-00000000" + categoryCount : (categoryCount < 1000 ? "00000-0000000" + categoryCount : "00000-000000" + categoryCount)));
									categoryCount++;
									categorySeqNos.put(wizCatId, 0);
									List<String> l = new ArrayList<String>();
									l.add(wizCatId);
									l.add(wizConfId);
									l.add(String.valueOf(wizConfSeqNos.get(wizConfId)));
									l.add(bBenefit.getCategoryId());
									l.add(null);
									l.add(bBenefitCategory.getDescription());
									l.add("");
									l.add("0 ");
									l.add(bBenefitCategory.getInstructions());
									sqlString += mainOps.insertInto("wizard_config_category", l);
									wizConfSeqNos.put(wizConfId, wizConfSeqNos.get(wizConfId) + 1);
									nonExclusiveCategories.put(bBenefitCategory.getCategoryId(), wizCatId);
								}

								String wizBenId = (benefitCount < 10 ? "00000-000000000" + benefitCount : (benefitCount < 100 ? "00000-00000000" + benefitCount : (benefitCount < 1000 ? "00000-0000000" + benefitCount : "00000-000000" + benefitCount)));
								benefitCount++;
								benefitSeqNos.put(wizBenId, 0);
								List<String> bl = new ArrayList<String>();
								bl.add(wizBenId);
								bl.add(wizCatId);
								bl.add(String.valueOf(categorySeqNos.get(wizCatId)));
								bl.add(bBenefit.getBenefitId());
								bl.add(null);
								bl.add(bBenefit.getName());
								bl.add(item.getAvatarPath());
								bl.add("0 ");
								bl.add(bBenefit.getAdditionalInstructions());
								sqlString += mainOps.insertInto("wizard_config_benefit", bl);
								categorySeqNos.put(wizCatId, categorySeqNos.get(wizCatId) + 1);

								BHRBenefitConfig[] benefitConfigArray = new BEmployee(emp).getAllValidConfigs(bBenefit.getBenefitId());
								System.out.println("            Doing Benefit Screen (" + item.getContextId() + ") - found " + benefitConfigArray.length + " benefit configs");
								for (BHRBenefitConfig bConfig : benefitConfigArray) {
									String wizConId = (configCount < 10 ? "00000-000000000" + configCount : (configCount < 100 ? "00000-00000000" + configCount : (configCount < 1000 ? "00000-0000000" + configCount : (configCount < 10000 ? "00000-000000" + configCount : "00000-00000" + configCount))));
									configCount++;
									List<String> cl = new ArrayList<String>();
									cl.add(wizConId);
									cl.add(wizBenId);
									cl.add(String.valueOf(benefitSeqNos.get(wizBenId)));
									cl.add(bConfig.getBenefitConfigId());
									cl.add(bConfig.getName());
									sqlString += mainOps.insertInto("wizard_config_config", cl);
									benefitSeqNos.put(wizBenId, benefitSeqNos.get(wizBenId) + 1);
								}
							}
						}
					}
				}

				Employee emp = hsu.createCriteria(Employee.class).eq(Employee.COMPANYBASE, company).isNull(Employee.BENEFIT_CLASS).first();
				if (emp == null)
					continue;
				String wizConfId = (wizardConfigurationCount < 10 ? "00000-000000000" + wizardConfigurationCount : (wizardConfigurationCount < 100 ? "00000-00000000" + wizardConfigurationCount : "00000-0000000" + wizardConfigurationCount));
				wizardConfigurationCount++;
				sqlString += "INSERT INTO wizard_configuration VALUES ('" + wizConfId + "','" + "(No Benefit Class)" + "','" + "Default" + "','" + "E" + "','" + "N" + "','" + "Y" + "','" + "N" + "','" + "N" + "','" + "Y" + "','" + "Y" + "','" + "N" + "','" + "Y" + "','" + "Y" + "','" + company.getOrgGroupId() + "'," + "NULL" + ",'" + "Default" + "','" + projectStatusId + "','" + projectCategoryId + "','" + projectTypeId + "','" + "2'); ";
				wizConfSeqNos.put(wizConfId, 0);
				System.out.println("    class " + "(No Benefit Class)" + " using " + emp.getNameFL());
				ListScreensAndGroupsInput in = new ListScreensAndGroupsInput();
				ListScreensAndGroupsReturn ret = new ListScreensAndGroupsReturn();
				in.setEmployeeId(emp.getPersonId());
				in.setGroupId("00001-0000000327"); //Enrollment Wizard Screen Group ID for DRC
				in.setDepth(4);
				MainOps mainOps = new MainOps();
				mainOps.doWiz(in, ret, company);

				HashMap<String, String> nonExclusiveCategories = new HashMap<String, String>();
				HashMap<String, String> flexBenefits = new HashMap<String, String>();
				HashMap<String, String> flexCategories = new HashMap<String, String>();
				for (int i = 4; i < ret.getItem().length - 2; i++) {
					ListScreensAndGroupsItem item = ret.getItem()[i];
					if (item.getScreenFile().toLowerCase().contains("category")) {
						BHRBenefitCategory bBenefitCategory = new BHRBenefitCategory(item.getContextId());
						List<HrBenefit> benefits = mainOps.getBenefitsForCategory(bBenefitCategory, null);
						if (benefits.isEmpty())
							continue;
						String wizCatId = (categoryCount < 10 ? "00000-000000000" + categoryCount : (categoryCount < 100 ? "00000-00000000" + categoryCount : (categoryCount < 1000 ? "00000-0000000" + categoryCount : "00000-000000" + categoryCount)));
						categorySeqNos.put(wizCatId, 0);
						categoryCount++;
						List<String> l = new ArrayList<String>();
						l.add(wizCatId);
						l.add(wizConfId);
						l.add(String.valueOf(wizConfSeqNos.get(wizConfId)));
						l.add(item.getContextId());
						l.add(null);
						l.add(bBenefitCategory.getDescription());
						l.add(item.getAvatarPath());
						l.add("0 ");
						l.add(bBenefitCategory.getInstructions());
						wizConfSeqNos.put(wizConfId, wizConfSeqNos.get(wizConfId) + 1);
						sqlString += mainOps.insertInto("wizard_config_category", l);

						System.out.println("            Doing Category Screen (" + item.getContextId() + ") - found " + benefits.size() + " benefits");
						for (HrBenefit benefit : benefits) {
							BHRBenefit bBenefit = new BHRBenefit(benefit);
							String wizBenId = (benefitCount < 10 ? "00000-000000000" + benefitCount : (benefitCount < 100 ? "00000-00000000" + benefitCount : (benefitCount < 1000 ? "00000-0000000" + benefitCount : "00000-000000" + benefitCount)));
							benefitSeqNos.put(wizBenId, 0);
							benefitCount++;
							List<String> bl = new ArrayList<String>();
							bl.add(wizBenId);
							bl.add(wizCatId);
							bl.add(String.valueOf(categorySeqNos.get(wizCatId)));
							bl.add(bBenefit.getBenefitId());
							bl.add(null);
							bl.add(bBenefit.getName());
							bl.add(item.getAvatarPath());
							bl.add("0 ");
							bl.add(bBenefit.getAdditionalInstructions());
							categorySeqNos.put(wizCatId, categorySeqNos.get(wizCatId) + 1);
							sqlString += mainOps.insertInto("wizard_config_benefit", bl);

							for (BHRBenefitConfig bConfig : new BEmployee(emp).getAllValidConfigs(benefit.getBenefitId())) {
								String wizConId = (configCount < 10 ? "00000-000000000" + configCount : (configCount < 100 ? "00000-00000000" + configCount : (configCount < 1000 ? "00000-0000000" + configCount : (configCount < 10000 ? "00000-000000" + configCount : "00000-00000" + configCount))));
								configCount++;
								List<String> cl = new ArrayList<String>();
								cl.add(wizConId);
								cl.add(wizBenId);
								cl.add(String.valueOf(benefitSeqNos.get(wizBenId)));
								cl.add(bConfig.getBenefitConfigId());
								cl.add(bConfig.getName());
								benefitSeqNos.put(wizBenId, benefitSeqNos.get(wizBenId) + 1);
								sqlString += mainOps.insertInto("wizard_config_config", cl);
							}
						}
					} else if (item.getScreenFile().toLowerCase().contains("flex")) {
						List<HrBenefitConfig> flexConfigs = mainOps.getFlexBenefitConfigs(null);
						System.out.println("            Doing Flex Screen (" + item.getContextId() + ") - found " + flexConfigs.size() + " flex benefit configs");
						for (HrBenefitConfig flexConfig : flexConfigs) {
							BHRBenefitConfig bConfig = new BHRBenefitConfig(flexConfig);
							String wizCatId = flexCategories.get(bConfig.getCategoryId());
							if (wizCatId == null || wizCatId.length() == 0) {
								BHRBenefitCategory bBenefitCategory = new BHRBenefitCategory(bConfig.getCategoryId());
								wizCatId = (categoryCount < 10 ? "00000-000000000" + categoryCount : (categoryCount < 100 ? "00000-00000000" + categoryCount : (categoryCount < 1000 ? "00000-0000000" + categoryCount : "00000-000000" + categoryCount)));
								categorySeqNos.put(wizCatId, 0);
								categoryCount++;
								List<String> l = new ArrayList<String>();
								l.add(wizCatId);
								l.add(wizConfId);
								l.add(String.valueOf(wizConfSeqNos.get(wizConfId)));
								l.add(bConfig.getCategoryId());
								l.add(null);
								l.add(bBenefitCategory.getDescription());
								l.add("");
								l.add("0 ");
								l.add(bBenefitCategory.getInstructions());
								sqlString += mainOps.insertInto("wizard_config_category", l);
								wizConfSeqNos.put(wizConfId, wizConfSeqNos.get(wizConfId) + 1);
								nonExclusiveCategories.put(bBenefitCategory.getCategoryId(), wizCatId);
							}
							String wizBenId = flexBenefits.get(bConfig.getBenefitId());
							if (wizBenId == null || wizBenId.length() == 0) {
								BHRBenefit bBenefit = new BHRBenefit(bConfig.getBenefitId());
								wizBenId = (benefitCount < 10 ? "00000-000000000" + benefitCount : (benefitCount < 100 ? "00000-00000000" + benefitCount : (benefitCount < 1000 ? "00000-0000000" + benefitCount : "00000-000000" + benefitCount)));
								benefitSeqNos.put(wizBenId, 0);
								benefitCount++;
								List<String> bl = new ArrayList<String>();
								bl.add(wizBenId);
								bl.add(wizCatId);
								bl.add(String.valueOf(categorySeqNos.get(wizCatId)));
								bl.add(bBenefit.getBenefitId());
								bl.add(null);
								bl.add(bBenefit.getName());
								bl.add(bBenefit.getAvatarPath());
								bl.add("0 ");
								bl.add(bBenefit.getAdditionalInstructions());
								categorySeqNos.put(wizCatId, categorySeqNos.get(wizCatId) + 1);
								sqlString += mainOps.insertInto("wizard_config_benefit", bl);
							}
							String wizConId = (configCount < 10 ? "00000-000000000" + configCount : (configCount < 100 ? "00000-00000000" + configCount : (configCount < 1000 ? "00000-0000000" + configCount : (configCount < 10000 ? "00000-000000" + configCount : "00000-00000" + configCount))));
							configCount++;
							List<String> cl = new ArrayList<String>();
							cl.add(wizConId);
							cl.add(wizBenId);
							cl.add(String.valueOf(benefitSeqNos.get(wizBenId)));
							cl.add(bConfig.getBenefitConfigId());
							cl.add(item.getTitle());
							benefitSeqNos.put(wizBenId, benefitSeqNos.get(wizBenId) + 1);
							sqlString += mainOps.insertInto("wizard_config_config", cl);
						}
					} else {
						BHRBenefit bBenefit = new BHRBenefit(item.getContextId());
						String wizCatId = nonExclusiveCategories.get(bBenefit.getCategoryId());
						if (wizCatId == null || wizCatId.length() == 0) {
							BHRBenefitCategory bBenefitCategory = new BHRBenefitCategory(bBenefit.getCategoryId());
							wizCatId = (categoryCount < 10 ? "00000-000000000" + categoryCount : (categoryCount < 100 ? "00000-00000000" + categoryCount : (categoryCount < 1000 ? "00000-0000000" + categoryCount : "00000-000000" + categoryCount)));
							categorySeqNos.put(wizCatId, 0);
							categoryCount++;
							List<String> l = new ArrayList<String>();
							l.add(wizCatId);
							l.add(wizConfId);
							l.add(String.valueOf(wizConfSeqNos.get(wizConfId)));
							l.add(bBenefit.getCategoryId());
							l.add(null);
							l.add(bBenefitCategory.getDescription());
							l.add("");
							l.add("0 ");
							l.add(bBenefitCategory.getInstructions());
							sqlString += mainOps.insertInto("wizard_config_category", l);
							wizConfSeqNos.put(wizConfId, wizConfSeqNos.get(wizConfId) + 1);
							nonExclusiveCategories.put(bBenefitCategory.getCategoryId(), wizCatId);
						}

						String wizBenId = (benefitCount < 10 ? "00000-000000000" + benefitCount : (benefitCount < 100 ? "00000-00000000" + benefitCount : (benefitCount < 1000 ? "00000-0000000" + benefitCount : "00000-000000" + benefitCount)));
						benefitSeqNos.put(wizBenId, 0);
						benefitCount++;
						List<String> bl = new ArrayList<String>();
						bl.add(wizBenId);
						bl.add(wizCatId);
						bl.add(String.valueOf(categorySeqNos.get(wizCatId)));
						bl.add(bBenefit.getBenefitId());
						bl.add(null);
						bl.add(bBenefit.getName());
						bl.add(item.getAvatarPath());
						bl.add("0 ");
						bl.add(bBenefit.getAdditionalInstructions());
						categorySeqNos.put(wizCatId, categorySeqNos.get(wizCatId) + 1);
						sqlString += mainOps.insertInto("wizard_config_benefit", bl);



						BHRBenefitConfig[] benefitConfigArray = new BEmployee(emp).getAllValidConfigs(bBenefit.getBenefitId());
						System.out.println("            Doing Benefit Screen (" + item.getContextId() + ") - found " + benefitConfigArray.length + " benefit configs");
						for (BHRBenefitConfig bConfig : benefitConfigArray) {
							String wizConId = (configCount < 10 ? "00000-000000000" + configCount : (configCount < 100 ? "00000-00000000" + configCount : (configCount < 1000 ? "00000-0000000" + configCount : (configCount < 10000 ? "00000-000000" + configCount : "00000-00000" + configCount))));
							configCount++;
							List<String> cl = new ArrayList<String>();
							cl.add(wizConId);
							cl.add(wizBenId);
							cl.add(String.valueOf(benefitSeqNos.get(wizBenId)));
							cl.add(bConfig.getBenefitConfigId());
							cl.add(item.getTitle());
							benefitSeqNos.put(wizBenId, benefitSeqNos.get(wizBenId) + 1);
							sqlString += mainOps.insertInto("wizard_config_config", cl);
						}
					}
				}
			}
		} catch (ArahantException arahantException) {
			logger.error(arahantException);
		}
		System.out.println(sqlString);

	}

	public String insertInto(String table, List<String> strL) {
		String ret = "INSERT INTO " + table + " VALUES (";
		for (int x = 0; x < strL.size(); x++)
			if (strL.get(x) != null)
				try {
					short i = Short.parseShort(strL.get(x));
					ret += i + ",";
				} catch (NumberFormatException e) {
					ret += "'" + strL.get(x) + "',";
				}
			else
				ret += "null,";
		ret = ret.substring(0, ret.lastIndexOf(","));
		ret += "); ";
		return ret;
	}

	public List<HrBenefit> getBenefitsForCategory(BHRBenefitCategory bc, BenefitClass bclass) {
		HibernateCriteriaUtil<HrBenefit> bhcu = ArahantSession.getHSU().createCriteria(HrBenefit.class).orderBy(HrBenefit.SEQ).eq(HrBenefit.BENEFIT_CATEGORY, bc.getBean()).eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y').dateInside(HrBenefit.START_DATE, HrBenefit.END_DATE, DateUtils.now());

//		if (bclass!=null)
//		{
//			HibernateCriteriaUtil chcu=bhcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
//			//either has no classes specified or they match
//			HibernateCriterionUtil orcri=chcu.makeCriteria();
//			HibernateCriterionUtil cri1=chcu.makeCriteria();
//
//			HibernateCriteriaUtil classHcu=chcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES,"classes");
//
//			HibernateCriterionUtil cri2=classHcu.makeCriteria();
//
//			cri1.sizeEq(HrBenefitConfig.BENEFIT_CLASSES, 0);
//			cri2.eq("classes."+BenefitClass.BENEFIT_CLASS_ID, bclass.getBenefitClassId());
//
//			orcri.or(cri1, cri2);
//
//			orcri.add();
//		}

		if (bclass != null) {
			//HibernateCriteriaUtil chcu=hcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
			//either has no classes specified or they match
			HibernateCriterionUtil orcri = bhcu.makeCriteria();
			HibernateCriterionUtil cri1 = bhcu.makeCriteria();

			HibernateCriteriaUtil classHcu = bhcu.leftJoinTo("benefitClasses", "classes");

			HibernateCriterionUtil cri2 = classHcu.makeCriteria();

			cri1.sizeEq("benefitClasses", 0);
			cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, bclass.getBenefitClassId());

			orcri.or(cri1, cri2);

			orcri.add();
		}

		return bhcu.list();
	}

	public List<HrBenefitConfig> getFlexBenefitConfigs(BenefitClass bclass) {
		HibernateCriteriaUtil<HrBenefitConfig> hcu = hsu.createCriteria(HrBenefitConfig.class);

		//check benefit classes
		if (bclass != null) {
			HibernateCriteriaUtil chcu = hcu;
			//either has no classes specified or they match
			HibernateCriterionUtil orcri = chcu.makeCriteria();
			HibernateCriterionUtil cri1 = chcu.makeCriteria();

			HibernateCriteriaUtil classHcu = chcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES, "classes");

			HibernateCriterionUtil cri2 = classHcu.makeCriteria();

			cri1.sizeEq(HrBenefitConfig.BENEFIT_CLASSES, 0);
			cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, bclass.getBenefitClassId());

			orcri.or(cri1, cri2);

			orcri.add();
		}

		hcu.dateInside(HrBenefitConfig.START_DATE, HrBenefitConfig.END_DATE, DateUtils.now()).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y').joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.FLEX_TYPE);

		return hcu.list();
	}

	/**
	 * Not used for Wizard version 3
	 * 
	 * @param in
	 * @param ret
	 * @param cd 
	 */
	private void doWiz(ListScreensAndGroupsInput in, ListScreensAndGroupsReturn ret, CompanyDetail cd) {
		if (hsu == null)
			hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<HrBenefitCategory> hcu;
		if (cd == null)
			hcu = hsu.createCriteria(HrBenefitCategory.class).orderBy(HrBenefitCategory.SEQ);
		else
			hcu = hsu.createCriteria(HrBenefitCategory.class).eq(HrBenefitCategory.COMPANY, cd).orderBy(HrBenefitCategory.SEQ);

		List<ListScreensAndGroupsItem> itemList = new ArrayList<ListScreensAndGroupsItem>(20);

		boolean isLocalHost = isLocalHost();

		BScreenOrGroup welcome = BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/welcome/BenefitWizardWelcomeScreen.swf");
		BScreenOrGroup qualScreen = BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/qualifyingEvent/BenefitWizardQualifyingEventScreen.swf");

		itemList.add(new ListScreensAndGroupsItem(welcome, isLocalHost));
		itemList.add(new ListScreensAndGroupsItem(qualScreen, isLocalHost));
		itemList.add(new ListScreensAndGroupsItem(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/demographics/BenefitWizardDemographicsScreen.swf"), isLocalHost));
		itemList.add(new ListScreensAndGroupsItem(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/dependents/BenefitWizardDependentsScreen.swf"), isLocalHost));

		for (HrBenefitCategory bc : hcu.list())
			if (bc.getOpenEnrollmentWizard() == 'Y')
				if (bc.getBenefitType() == HrBenefitCategory.FLEX_TYPE) {
					ListScreensAndGroupsItem i;
					if (bc.getOpenEnrollmentScreen() != null)
						itemList.add(new ListScreensAndGroupsItem(new BScreen(bc.getOpenEnrollmentScreen()), isLocalHost));
					else {
						//itemList.add(new ListScreensAndGroupsItem(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/flex/BenefitWizardFlexScreen.swf"), wsContext));
						i = new ListScreensAndGroupsItem(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/flex/BenefitWizardFlexScreen.swf"), isLocalHost);
						i.setTitle("Flex");
						i.setContextId(bc.getBenefitCatId());
						itemList.add(i);
					}
				} else {
					ListScreensAndGroupsItem i;
					if (bc.getOpenEnrollmentScreen() != null)
						i = new ListScreensAndGroupsItem(new BScreen(bc.getOpenEnrollmentScreen()), isLocalHost);
					else
						i = new ListScreensAndGroupsItem(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/category/BenefitWizardCategoryScreen.swf"), isLocalHost);
					i.setTitle(bc.getDescription());
					i.setContextId(bc.getBenefitCatId());
					itemList.add(i);
					//itemList.add(new ListScreensAndGroupsItem(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/category/BenefitWizardCategoryScreen.swf"), wsContext));
				}
			else if (bc.getBenefitType() == HrBenefitCategory.VOLUNTARY) {
				HibernateCriteriaUtil<HrBenefit> bhcu = hsu.createCriteria(HrBenefit.class).orderBy(HrBenefit.SEQ).dateInside(HrBenefit.START_DATE, HrBenefit.END_DATE, DateUtils.now()).eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y').eq(HrBenefit.BENEFIT_CATEGORY, bc);

				BEmployee bemp = isEmpty(in.getEmployeeId()) ? BPerson.getCurrent().getBEmployee() : new BEmployee(in.getEmployeeId());

				//check benefit classes
				if (bemp.getBenefitClass() != null) {
					HibernateCriteriaUtil chcu = bhcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
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


				for (HrBenefit b : bhcu.list()) {
					ListScreensAndGroupsItem i;
					if (b.getOpenEnrollmentScreen() != null)
						i = new ListScreensAndGroupsItem(new BScreen(b.getOpenEnrollmentScreen()), isLocalHost);
					else
						i = new ListScreensAndGroupsItem(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/voluntary/BenefitWizardVoluntaryScreen.swf"), isLocalHost);
					i.setTitle(b.getName());
					i.setContextId(b.getBenefitId());
					itemList.add(i);
					if (!isEmpty(b.getAvatarPath()))
						i.setAvatarPath(b.getAvatarPath());
					else if (!isEmpty(b.getHrBenefitCategory().getAvatarPath()))
						i.setAvatarPath(b.getHrBenefitCategory().getAvatarPath());
				}

			} else {
				HibernateCriteriaUtil<HrBenefit> bhcu = hsu.createCriteria(HrBenefit.class).orderBy(HrBenefit.SEQ).dateInside(HrBenefit.START_DATE, HrBenefit.END_DATE, DateUtils.now()).eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y').eq(HrBenefit.BENEFIT_CATEGORY, bc);

				BEmployee bemp = isEmpty(in.getEmployeeId()) ? BPerson.getCurrent().getBEmployee() : new BEmployee(in.getEmployeeId());

				//check benefit classes
				if (bemp.getBenefitClass() != null) {
					HibernateCriteriaUtil chcu = bhcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
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


				for (HrBenefit b : bhcu.list()) {
					ListScreensAndGroupsItem i;
					if (b.getOpenEnrollmentScreen() != null)
						i = new ListScreensAndGroupsItem(new BScreen(b.getOpenEnrollmentScreen()), isLocalHost);
					else
						i = new ListScreensAndGroupsItem(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/benefit/BenefitWizardBenefitScreen.swf"), isLocalHost);
					i.setTitle(b.getName());
					i.setContextId(b.getBenefitId());
					itemList.add(i);
					if (!isEmpty(b.getAvatarPath()))
						i.setAvatarPath(b.getAvatarPath());
					else if (!isEmpty(b.getHrBenefitCategory().getAvatarPath()))
						i.setAvatarPath(b.getHrBenefitCategory().getAvatarPath());
				}

			}

		itemList.add(new ListScreensAndGroupsItem(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/review/BenefitWizardReviewScreen.swf"), isLocalHost));
		itemList.add(new ListScreensAndGroupsItem(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/finish/FinishScreen.swf"), isLocalHost));

		for (ListScreensAndGroupsItem i : itemList)
			i.setShowNextButton(true);

		itemList.get(0).setIsDefault(true);
		itemList.get(itemList.size() - 1).setLastScreen(true);


		//filter out the ones I don't want to show now
		//I have certain checkpoints
		if (in.getDepth() < 4) //haven't done deps and demogs
			itemList = itemList.subList(0, 4);
		if (in.getDepth() < 2) //haven't done the qualify event screen
			itemList = itemList.subList(0, 2);


		ListScreensAndGroupsItem[] items = new ListScreensAndGroupsItem[itemList.size()];

		for (int loop = 0; loop < items.length; loop++) {
			itemList.get(loop).setShowNextButton(true);
			items[loop] = itemList.get(loop);
		}

		if (in.getDepth() > -1 && in.getDepth() < items.length) {
			itemList.get(0).setIsDefault(false);
			items[in.getDepth()].setIsDefault(true);
		}

		ret.setItem(items);
	}

	@WebMethod()
	public ListOnboardingTasksReturn listOnboardingTasks(/*
			 * @WebParam(name = "in")
			 */final ListOnboardingTasksInput in) {
		final ListOnboardingTasksReturn ret = new ListOnboardingTasksReturn();
		try {
			checkLogin(in);

			BEmployee be = BPerson.getCurrent().getBEmployee();

			int screenIndex = 3; //0 is the Go Back screen, 1 is generic welcome, and 2 is the onboarding homepage

			List<HrChecklistItem> l = ArahantSession.getHSU().createCriteria(HrChecklistItem.class).eq(HrChecklistItem.HREMPLOYEESTATUS, be.getLastActiveStatusHistory().getHrEmployeeStatus()).dateInside(HrChecklistItem.FIRSTACTIVEDATE, HrChecklistItem.LASTACTIVEDATE, DateUtils.now()).eq(HrChecklistItem.RESPONSIBILITY, HrChecklistItem.RESPONSIBILITY_EMPLOYEE).list();

			ListOnboardingTasksReturnItem[] ltri = new ListOnboardingTasksReturnItem[l.size()];

			int i = 0;
			for (HrChecklistItem cl : l) {
				BHRCheckListDetail cld = new BHRCheckListDetail(ArahantSession.getHSU().createCriteria(HrChecklistDetail.class).eq(HrChecklistDetail.HRCHECKLISTITEM, cl).eq(HrChecklistDetail.EMPLOYEEBYEMPLOYEEID, be.getPerson()).first());
				ltri[i] = new ListOnboardingTasksReturnItem();
				ltri[i].setSelect(cld.getBean() != null);
				ltri[i].setStatus(ltri[i].getSelect() ? "Completed" : "In Progress");
				ltri[i].setStatusDate(ltri[i].getSelect() ? DateUtils.getDateFormatted(cld.getDateCompleted()) : DateUtils.getDateFormatted(be.getLastActiveStatusHistory().getEffectiveDate()));
				ltri[i].setTaskDescription("");
				ltri[i].setTaskName(cl.getName());
				ltri[i].setTaskId(cl.getItemId());
				if (cl.getScreen() != null)
					ltri[i].setIndex(screenIndex++);
				else if (cl.getScreenGroup() != null)
					ltri[i].setIndex(screenIndex++);
				else
					ltri[i].setIndex(-1);
				if (cl.getCompanyForm() != null)
					ltri[i].setFileUrl(new BCompanyForm(cl.getCompanyForm()).getForm());

				i++;

			}

			ret.setItem(ltri);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
