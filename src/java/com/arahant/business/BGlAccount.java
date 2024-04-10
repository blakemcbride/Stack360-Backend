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

import com.arahant.beans.CompanyDetail;
import java.io.FileNotFoundException;
import java.util.*;

import com.arahant.beans.GlAccount;
import com.arahant.beans.QuickbooksAccountChange;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.GLAccountReport;
import com.arahant.utils.*;
import com.arahant.utils.Collections;
import com.itextpdf.text.DocumentException;


/**
 * 
 *
 * Created on Feb 15, 2007
 *
 */
public class BGlAccount extends SimpleBusinessObjectBase<GlAccount> {

	public static BGlAccount[] search(String accountName, String accountNumber, boolean showOnlyARAccounts, int max) {
		HibernateCriteriaUtil<GlAccount> hcu= ArahantSession.getHSU().createCriteria(GlAccount.class)
				.like(GlAccount.ACCOUNTNAME, accountName)
				.like(GlAccount.ACCOUNTNUMBER, accountNumber)
				.setMaxResults(max);
		
		if (showOnlyARAccounts)
			hcu.eq(GlAccount.ACCOUNTTYPE,GlAccount.TYPE_ACCOUNTS_RECIEVABLE);
		
		return makeArray(hcu.list());
	}

	public BGlAccount() {
	}
	
	public BGlAccount(final GlAccount glAccount) {
        this.bean = glAccount;
    }

	public BGlAccount(final String key) throws ArahantException {
		load(key);
	}

	public static BGlAccount [] listByType(final HibernateSessionUtil hsu, final int type)
	{
		return makeArray(hsu.createCriteria(GlAccount.class)
			.eq(GlAccount.ACCOUNTTYPE, type)
			.orderBy(GlAccount.ACCOUNTNAME)
			.list());
	}
	
	public static BGlAccount[] list(final HibernateSessionUtil hsu)
	{
		return makeArray(hsu.createCriteria(GlAccount.class).orderBy(GlAccount.ACCOUNTNAME).list());
	}
	
	static BGlAccount[] makeArray(final List<GlAccount> plist) {
        final BGlAccount[] glat = new BGlAccount[plist.size()];

        int index = 0;

        final Iterator plistItr = plist.iterator();

        while (plistItr.hasNext())
            glat[index++] = new BGlAccount((GlAccount) plistItr.next());

        return glat;
    }

	public String getAccountName() {
		return bean.getAccountName();
	}

	public String getAccountNumber() {
		return bean.getAccountNumber();
	}

	public int getAccountType() {
		return bean.getAccountType();
	}

	public short getDefaultFlag() {
		return bean.getDefaultFlag();
	}

	public String getGlAccountId() {
		return bean.getGlAccountId();
	}

	public Set getInvoices() {
		return bean.getInvoices();
	}

	public void setAccountName(final String accountName) {
		bean.setAccountName(accountName);
	}

	public void setAccountNumber(final String accountNumber) {
		bean.setAccountNumber(accountNumber);
	}

	public void setAccountType(final int accountType) {
		bean.setAccountType(accountType);
	}

    public void setAccountType(String accountType) {
       setAccountType(getAccountType(accountType));
    }

	public void setDefaultFlag(final short defaultFlag) {
		bean.setDefaultFlag(defaultFlag);
	}

	public void setGlAccountId(final String glAccountId) {
		bean.setGlAccountId(glAccountId);
	}

	public CompanyDetail getCompany() {
		return bean.getCompany();
	}

	public void setCompany(CompanyDetail company) {
		bean.setCompany(company);
	}

	public String getCompanyId() {
		return bean.getCompanyId();
	}

	public void setCompanyId(String companyId) {
		bean.setCompany(ArahantSession.getHSU().get(CompanyDetail.class, companyId));
	}

	/**
	 * @return
	 * @throws ArahantException 
	 */
	public String create() throws ArahantException {
		bean=new GlAccount();
		bean.setGlAccountId(IDGenerator.generate(bean));
		return bean.getGlAccountId();
	}

    @Override
	public void delete() throws ArahantDeleteException {
        ArahantSession.getHSU().createCriteria(QuickbooksAccountChange.class)
            .eq(QuickbooksAccountChange.ACCOUNT, bean)
            .delete();
		ArahantSession.getHSU().delete(bean);
	}

    @Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	public void load(final String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(GlAccount.class, key);
	}

    @Override
	public void update() throws ArahantException {
		if (getDefaultFlag()==1) {
            removeDefaultForType(getAccountType());
            setDefaultFlag(true);
        }
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	private void removeDefaultForType(final int accountType) {
		final GlAccount gla=ArahantSession.getHSU().createCriteria(GlAccount.class)
			.eq(GlAccount.ACCOUNTTYPE, accountType)
			.eq(GlAccount.DEFAULTFLAG, (short)1).first();
			
		if (gla!=null) {
            gla.setDefaultFlag((short) 0);
            ArahantSession.getHSU().saveOrUpdate(gla);
        }
	}

	public void setDefaultFlag(final boolean defaultFlag) {
		setDefaultFlag((short)(defaultFlag?1:0));
	}

    public String getQuickbooksAccountType()
    {
        switch (bean.getAccountType())
        {
            case 0 : return "Bank";
            case 1 : return "AccountsReceivable";
            case 4 : return "OtherCurrentAsset";
            case 5 : return "FixedAsset";
            case 10 : return "AccountsPayable";
            case 100 : return "CreditCard";
            case 12 : return "OtherCurrentLiability";
            case 14 : return "LongTermLiability";
            case 101 : return "Equity";
            case 21 : return "Income";
            case 102 : return "OtherIncome";
            case 23 : return "CostOfGoodsSold";
            case 24 : return "Expense";
            case 104 : return "NonPosting";

            default: throw new ArahantException("Unknown Quickbooks account type "+bean.getAccountType());
        }
    }

    private static int getAccountType(String name)
    {
        if (name.equals("Bank"))
            return 0;
        if (name.equals("AccountsReceivable"))
            return 1;
        if (name.equals("OtherCurrentAsset"))
            return 4;
        if (name.equals("FixedAsset"))
            return 5;
        if (name.equals("AccountsPayable"))
            return 10;
        if (name.equals("CreditCard"))
            return 100;
        if (name.equals("OtherCurrentLiability"))
            return 12;
        if (name.equals("LongTermLiability"))
            return 14;
        if (name.equals("Equity"))
            return 101;
        if (name.equals("Income"))
            return 21;
        if (name.equals("CostOfGoodsSold"))
            return 23;
        if (name.equals("Expense"))
            return 24;
        if (name.equals("OtherIncome"))
            return 102;
        if (name.equals("NonPosting"))
            return 104;
        return 0; 
    }

	private static String getAccountTypeName(final int x)
	{
		switch (x) {
            case 0: return "Cash";
            case 1: return "Accounts Receivable";
            case 2: return "Inventory";
            case 3: return "Receivables Retainage (PPAC Only)";
            case 4: return "Other Current Assets";
            case 5: return "Fixed Asset";
            case 6: return "Accumulated Depreciation";
            case 8: return "Other Asset";
            case 10: return "Accounts Payable";
            case 11: return "Payables Retainage (PPAC Only)";
            case 12: return "Other Current Liabilities";
            case 14: return "Long Term Liabilities";
            case 16: return "Equity - Doesn't Close";
            case 18: return "Equity - Retained Earnings";
            case 19: return "Equity - Gets Closed";
            case 21: return "Income";
            case 23: return "Cost of Sales";
            case 24: return "Expenses";
            case 100: return "CreditCard";
            case 101: return "Equity";
            case 102: return "OtherIncome";
            case 103: return "OtherExpense";
            case 104: return "NonPosting";

            default:
                return "Invalid Account Type";
		}
	}

	public String getAccountTypeFormatted() {
		
		return getAccountTypeName(getAccountType());

	}

	/**
	 * @param accountIds
	 * @throws ArahantException 
	 * @throws ArahantDeleteException 
	 */
	public static void delete(final String[] accountIds) throws ArahantDeleteException, ArahantException {
		for (String element : accountIds)
			new BGlAccount(element).delete();
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static GLAccountType[] getTypes() {
        final int type[] = {0, 1, 2, 3, 4, 5, 6, 8, 10, 11, 12, 13, 14, 16, 18, 19, 21, 23, 24};
        GLAccountType[] ret = new GLAccountType[type.length];

        for (int loop = 0; loop < type.length; loop++)
            ret[loop] = new GLAccountType(type[loop], getAccountTypeName(type[loop]));

        final List<GLAccountType> arrList = new ArrayList<GLAccountType>(type.length);
        Collections.addAll(arrList, ret);

        java.util.Collections.sort(arrList);

        ret = arrList.toArray(ret);
        return ret;
    }
	
	public static class GLAccountType implements Comparable {
        /**
         * @param i
         * @param accountTypeName
         */
        public GLAccountType(final int i, final String accountTypeName) {
            type = i;
            typeFormatted = accountTypeName;
        }

        public String typeFormatted;
        public int type;

        public int compareTo(final Object o) {
            if (o instanceof GLAccountType)
                return typeFormatted.compareTo(((GLAccountType) o).typeFormatted);
            return -1;
        }
    }

	/**
	 * @return
	 * @throws ArahantException 
	 * @throws DocumentException 
	 * @throws FileNotFoundException 
	 */
	public static String getReport() throws FileNotFoundException, DocumentException, ArahantException {
		return new GLAccountReport().build(makeArray(ArahantSession.getHSU().createCriteria(GlAccount.class).orderBy(GlAccount.ACCOUNTNUMBER).orderBy(GlAccount.ACCOUNTNAME).list()));
	}

	public static String findOrMake(String name) {
		GlAccount gla = ArahantSession.getHSU().createCriteria(GlAccount.class)
			.eq(GlAccount.ACCOUNTNAME, name)
			.first();

		if (gla!=null)
			return gla.getGlAccountId();

		BGlAccount glAccount=new BGlAccount();
		String ret = glAccount.create();
		glAccount.setAccountName(name);
		glAccount.setDefaultFlag(false);
		glAccount.setAccountType(24);
		glAccount.setAccountNumber("");
		glAccount.insert();

		return ret;
	}
	
}

	
