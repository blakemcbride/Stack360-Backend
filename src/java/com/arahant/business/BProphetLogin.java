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
 * Created on Feb 15, 2007
*/

package com.arahant.business;

import com.arahant.beans.HrEmplStatusHistory;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.beans.LoginLog;
import com.arahant.beans.Person;
import com.arahant.beans.ProphetLogin;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ArahantLoginException;
import com.arahant.services.main.LoadPasswordRulesReturn;
import com.arahant.services.main.UserCache;
import com.arahant.utils.*;
import java.util.ArrayList;
import java.util.List;

public class BProphetLogin extends BusinessLogicBase {

    private static final ArahantLogger logger = new ArahantLogger(BProphetLogin.class);

    public static boolean checkPassword(final String user, final String password) throws ArahantLoginException {
        return checkPassword(user, password, UserCache.LoginType.NORMAL);
    }

    /**
     * Validate user password
     *
     * @param user
     * @param password
     * @return true if login is good, false for bad password
     * @throws ArahantLoginException
     */
    public static boolean checkPassword(final String user, final String password, UserCache.LoginType loginType) throws ArahantLoginException {
        logger.debug("Getting hibernate connection");
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        logger.debug("Execute login check query");
        ProphetLogin login = hsu.createCriteria(ProphetLogin.class).eq(ProphetLogin.USERLOGIN, user).first();

        logger.debug("Got query results");

        if (login != null) {
            try {
                logger.debug("Running encryption");

				BPerson bpers = new BPerson(login.getPerson());
                String encryptedPassword = bpers.encryptPassword(password);
				String noSaltEncryptedPassword = Crypto.encryptTripleDES(password);
                String storedPassword = login.getUserPassword();  // presumed encrypted

                logger.debug("Comparing passwords");

                // compare against non-encrypted and encrypted passwords as older deployments did not encrypt passwords
                if (((isUnencryptedPassword(storedPassword)  &&  storedPassword.equals(password)) || 
						storedPassword.equals(encryptedPassword)  ||
						storedPassword.equals(noSaltEncryptedPassword))) {
                    logger.debug("Got match checking account active");
                    if ((login.getCanLogin() != null)) {
                        if (login.getCanLogin() != 'Y')
							if (BProperty.getBoolean("WmCoEDI"))
								throw new ArahantLoginException("The Online Wizard is not available at this time for employees to enroll in the benefit programs as a new hire or to make changes due to a qualifying event.  The appropriate paperwork must be completed and returned to the Williamson County Benefits Department.");
							else
								throw new ArahantLoginException("Account disabled.");
                    }
					if (new BPerson(login.getPerson()).isEmployee()) {
						// We have to do this the hard way because there is no current company yet
						HrEmplStatusHistory sh = hsu.createCriteriaNoCompanyFilter(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, login.getPerson()).le(HrEmplStatusHistory.EFFECTIVEDATE, DateUtils.now()).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).first();
						if (loginType != UserCache.LoginType.APPLICANT) {
                            if (sh == null)
                                throw new ArahantLoginException("Account login cannot occur until employment becomes active.");
                            HrEmployeeStatus stat = sh.getHrEmployeeStatus();
                            if (stat == null)
                                throw new ArahantLoginException("Account login cannot occur until employment becomes active.");
                            if (stat.getActive() == 'N')
                                throw new ArahantLoginException("Account login cannot occur because employee is not active.");
                        } else {
                            //  is applicant login
                            if (sh != null) {
                                HrEmployeeStatus stat = sh.getHrEmployeeStatus();
                                if (stat != null && stat.getActive() == 'Y')
                                    throw new ArahantLoginException("Account login cannot occur because applicant is an active employee.");
                            }
                        }
					}
                    logger.debug("Setting current user");
                    hsu.setCurrentPerson(login.getPerson());
                    if (!ArahantSession.disableJess)
                        ArahantSession.getAI().eval("(assert (current-person-id \"" + hsu.getCurrentPerson().getPersonId() + "\"))");
                    logger.debug("Returning");
                    return true;
                }
            } catch (ArahantLoginException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new ArahantLoginException(ex);
            }
        }

        try {
            logger.debug("Password was wrong, sleeping 2 seconds");
            Thread.sleep(2000); //penalty for getting it wrong
        } catch (InterruptedException e) {
            //do nothing
        }

        logger.debug("Return bad password");

        return false;
    }

    private static boolean isUnencryptedPassword(String pw)  {
        return pw.length() < 16  ||  !pw.matches("[0-9a-f]+");
    }

    public BProphetLogin() {
        super();
        logger.debug("pl");
    }

    public void validatePassword(String password) {
        // check if we meet requirements
        int totalCharacters = 0;
        int totalDigits = 0;
        int totalLetters = 0;
        int totalUpperCaseLetters = 0;
        int totalLowerCaseLetters = 0;
        int totalSpecialCharacters = 0;
        int failed = 0;

        //Loop and count each char in the password
        for (int idx = 0; idx < password.length(); idx++) {

            char currentChar = password.charAt(idx);
            totalCharacters++;

            if (Character.isDigit(currentChar)) {
                totalDigits++;
            } else if (Character.isLetter(currentChar)) {
                totalLetters++;

                if (Character.isUpperCase(currentChar)) {
                    totalUpperCaseLetters++;
                } else {
                    totalLowerCaseLetters++;
                }
            } else {
                totalSpecialCharacters++;
            }
        }

        //Load the rules and verify
        //if anything failed then return false
        LoadPasswordRulesReturn passwordRules = new LoadPasswordRulesReturn();
        String passwordHint = "Password did not meet the following requirements: \n";
        String validHint = "Password met the following requirements: \n";

        if (passwordRules.getMinimumLength() > 0) {
            if (totalCharacters < passwordRules.getMinimumLength()) {
                failed++;
                passwordHint += " Must have at least " + passwordRules.getMinimumLength() + " characters\n";
            } else {
                validHint += " Must have at least " + passwordRules.getMinimumLength() + " characters\n";
            }
        }

        if (passwordRules.getMinimumDigits() > 0) {
            if (totalDigits < passwordRules.getMinimumDigits()) {
                failed++;
                passwordHint += " Must have at least " + passwordRules.getMinimumDigits() + " digits\n";
            } else {
                validHint += " Must have at least " + passwordRules.getMinimumDigits() + " digits\n";
            }
        }

        if (passwordRules.getMinimumLetters() > 0) {
            if (totalLetters < passwordRules.getMinimumLetters()) {
                failed++;
                passwordHint += " Must have at least " + passwordRules.getMinimumLetters() + " letters\n";
            } else {
                validHint += " Must have at least " + passwordRules.getMinimumLetters() + " letters\n";
            }
        }

        if (passwordRules.getMinimumLowerCaseLetters() > 0) {
            if (totalLowerCaseLetters < passwordRules.getMinimumLowerCaseLetters()) {
                failed++;
                passwordHint += " Must have at least " + passwordRules.getMinimumLowerCaseLetters() + " lowercase characters\n";
            } else {
                validHint += " Must have at least " + passwordRules.getMinimumLowerCaseLetters() + " lowercase characters\n";
            }
        }
        if (passwordRules.getMinimumSpecialChars() > 0) {
            if (totalSpecialCharacters < passwordRules.getMinimumSpecialChars()) {
                failed++;
                passwordHint += " Must have at least " + passwordRules.getMinimumSpecialChars() + " special characters\n";
            } else {
                validHint += " Must have at least " + passwordRules.getMinimumSpecialChars() + " special characters\n";
            }
        }
        if (passwordRules.getMinimumUpperCaseLetters() > 0) {
            if (totalUpperCaseLetters < passwordRules.getMinimumUpperCaseLetters()) {
                failed++;
                passwordHint += " Must have at least " + passwordRules.getMinimumUpperCaseLetters() + " uppercase characters\n";
            } else {
                validHint += " Must have at least " + passwordRules.getMinimumUpperCaseLetters() + " uppercase characters\n";
            }
        }

        if (failed > 0) {
            throw new ArahantWarning("Password Validation\n" + validHint + "\n\n" + passwordHint);
        }

    }

	public static void main (String args[]) {
//        BProphetLogin ops = new BProphetLogin();
//        String password = "==43T5BBBB";
//        ops.validatePassword(password);
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.beginTransaction();
			List<LoginLog> login = hsu.createCriteria(LoginLog.class).list();
			HibernateCriteriaUtil<ProphetLogin> prophetHcu = hsu.createCriteria(ProphetLogin.class)
																.joinTo(ProphetLogin.PERSON)
																.orderBy(Person.LNAME)
																.orderBy(Person.FNAME);
			List<ProphetLogin> prophet = prophetHcu.list();
			List<String> loginIds = new ArrayList<String>();
			List<String> prophetIds = new ArrayList<String>();
//			List<Person> newPw = new ArrayList<Person>();

			for(LoginLog l : login)
				loginIds.add(l.getPersonId());
			for(ProphetLogin p : prophet)
				prophetIds.add(p.getPersonId());

			prophetIds.removeAll(loginIds);

			for(String id: prophetIds) {
				BPerson bp = new BPerson(id);
//				newPw.add(bp.getPerson());
				System.out.print(bp.getNameFML() + " - " + bp.getUserPassword() + " " + bp.getLastName().substring(0,1) + bp.getSsn().substring(7) + " to: ");
				bp.setNewPassword(bp.getLastName().substring(0,1).toLowerCase() + bp.getSsn().substring(7));
				System.out.println(bp.getUserPassword());

				hsu.update(bp.getPerson());
			}

			hsu.commitTransaction();
		}
		catch (Exception e)
		{
			logger.error(e);
			hsu.rollbackTransaction();
		}
	}
}

	
