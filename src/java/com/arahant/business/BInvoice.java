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

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.business.interfaces.IInvoiceSearchCriteria;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.InvoiceReport;
import com.arahant.reports.InvoiceReport2;
import com.arahant.reports.InvoiceReport3;
import com.arahant.utils.*;
import org.kissweb.StringUtils;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.*;

public class BInvoice extends SimpleBusinessObjectBase<Invoice> implements IDBFunctions {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BInvoice(id).delete();
	}

	public static BInvoice[] searchPersonInvoices(int fromDate, int toDate, boolean limitToNonZeroBalanceInvoices, String[] personId, String[] invoiceIds, int cap) {
		HibernateCriteriaUtil<Invoice> hcu = ArahantSession.getHSU().createCriteria(Invoice.class).orderBy(Invoice.CREATEDATE).setMaxResults(cap);

		if (invoiceIds != null && invoiceIds.length > 0)
			hcu.in(Invoice.INVOICEID, invoiceIds);

		if (toDate != 0)
			hcu.le(Invoice.CREATEDATE, DateUtils.getDate(toDate));
		if (fromDate != 0)
			hcu.ge(Invoice.CREATEDATE, DateUtils.getDate(fromDate));

		//	.between(Invoice.CREATEDATE, DateUtils.getDate(toDate), DateUtils.getDate(fromDate));

		if (personId != null && personId.length > 0)
			hcu.joinTo(Invoice.PERSON).in(Person.PERSONID, personId);
		else
			hcu.isNotNull(Invoice.PERSON);

		List<Invoice> l;
		if (limitToNonZeroBalanceInvoices)
			l = filterNonZeroInvoices(hcu.list(), "");
		else
			l = hcu.list();

		return makeArray(l);
	}

	public static BInvoice[] search(String[] excludeIds, int fromDate, int toDate, String personId, String paymentId, int cap) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<Invoice> hcu = hsu.createCriteria(Invoice.class);

		hcu.notIn(Invoice.INVOICEID, excludeIds);

		if (fromDate != 0)
			hcu.ge(Invoice.CREATEDATE, DateUtils.getDate(fromDate));
		if (toDate != 0)
			hcu.le(Invoice.CREATEDATE, DateUtils.getDate(toDate));

		hcu.joinTo(Invoice.PERSON).eq(Person.PERSONID, personId);

		hcu.orderBy(Invoice.CREATEDATE);

		hcu.setMaxResults(cap);

		List<Invoice> l = hcu.list();
		List<Invoice> rl;

		rl = filterNonZeroInvoices(l, paymentId);

		return makeArray(rl);
	}

	protected static List<Invoice> filterNonZeroInvoices(List<Invoice> l, String paymentId) {
		//now filter out of list any that have 0 balance that weren't associated with paymentId
		List<Invoice> rl = new ArrayList<Invoice>(l.size());
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		for (Invoice i : l) {
			if (new BInvoice(i).getBalance() < .01)
				if (!hsu.createCriteria(Receipt.class).eq(Receipt.RECEIPT_ID, paymentId).joinTo(Receipt.RECEIPT_JOIN).eq(ReceiptJoin.INVOICE, i).exists())
					continue;
			rl.add(i);
		}
		return rl;
	}

	static BInvoice[] getOutstandingInvoices(Person person) {
		//TODO: this is not the most efficient way to do this
		List<Invoice> rlist = ArahantSession.getHSU().createCriteria(Invoice.class).eq(Invoice.PERSON, person).orderBy(Invoice.CREATEDATE).list();
		ArrayList<BInvoice> brl = new ArrayList<BInvoice>();
		for (Invoice r : rlist) {
			BInvoice br = new BInvoice(r);
			if (br.getBalance() > 0)
				brl.add(br);
		}
		return brl.toArray(new BInvoice[brl.size()]);
	}

	static BInvoice[] makeArray(List<Invoice> l) {
		BInvoice[] ret = new BInvoice[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BInvoice(l.get(loop));
		return ret;
	}

	public BInvoice() {
	}

	public BInvoice(final Invoice i) {
		bean = i;
	}

	public BInvoice(final String key) throws ArahantException {
		super(key);
	}

	public void applyPayment(BReceipt br, double appliedToThisInvoice) {
		BReceiptJoin rj = new BReceiptJoin();
		rj.create();
		rj.setAmount(appliedToThisInvoice);
		rj.setReceipt(br);
		rj.setInvoice(this);
		updates.add(rj.bean);
	}

	/**
	 * Generate default invoice numbers
	 * 
	 * Format YYYYMMDDnnnP
	 * 
	 * where nnn is a sequential number for that day.
	 */
	private void generateInvoiceNumber_1() {
		final Calendar cal = DateUtils.getNow();

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		//find out highest bean number
		final HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(Invoice.class);
		hcu.ge(Invoice.CREATEDATE, new java.sql.Date(cal.getTime().getTime()));
		int size = 0;
		final List invTodayList = hcu.list();
		if (invTodayList != null)
			size = invTodayList.size();

		size++;

		String sz = "" + size;

		if (size <= 9)
			sz = "00" + size;
		else if (size <= 99)
			sz = "0" + size;

		String month = "" + (cal.get(Calendar.MONTH) + 1);

		String day = "" + (cal.get(Calendar.DAY_OF_MONTH));

		if (month.length() < 2)
			month = "0" + month;

		if (day.length() < 2)
			day = "0" + day;

		final String id = "" + cal.get(Calendar.YEAR) + month + day + sz + "P";
		bean.setAccountingInvoiceIdentifier(id);
	}

	/**
	 * Generate invoice numbers
	 * 
	 * Format YYMMnnn
	 * 
	 * where nnn is a sequential number for that year.
	 */
	private void generateInvoiceNumber_2() {
		Calendar cal;

		if (bean.getCreateDate() == null)
			cal = DateUtils.getNow();
		else {
			cal = Calendar.getInstance();
			cal.setTime(bean.getCreateDate());
		}

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		String  year = (cal.get(Calendar.YEAR) % 2000) + "";
		String month = (cal.get(Calendar.MONTH) + 1) + "";
		if (month.length() < 2)
			month = "0" + month;

		int invNum = 1;

		Connection db = new Connection(ArahantSession.getHSU().getConnection());
		Command cmd = db.newCommand();
		try {
			Record rec = cmd.fetchOne("select accounting_invoice_identifier from invoice where accounting_invoice_identifier like ? " +
					"order by accounting_invoice_identifier desc", year + month + "%");
			if (rec != null) {
				String oldNum = rec.getString("accounting_invoice_identifier");
				if (oldNum.length() > 5) {
					try {
						invNum = Integer.parseInt(StringUtils.drop(oldNum, 4).trim()) + 1;
					} catch (Exception e) {
						// do nothing
					}
				}
			}
		} catch (Exception e) {
			throw new ArahantDeleteException(e);
		}
		cmd.close();
		/*
		Invoice inv = ArahantSession.getHSU().createCriteria(Invoice.class).like(Invoice.ACCOUNTINGINVOICEIDENTIFIER, year + month + "%")
				.orderByDesc(Invoice.ACCOUNTINGINVOICEIDENTIFIER).setMaxResults(1).first();
		if (inv != null) {
			String oldNum = inv.getAccountingInvoiceIdentifier();
			if (oldNum.length() > 5) {
				try {
					invNum = Integer.parseInt(StringUtils.drop(oldNum, 4).trim()) + 1;
				} catch (Exception e) {
					// do nothing
				}
			}
		}
		 */
		String sInvNum = "" + invNum;

		if (invNum <= 9)
			sInvNum = "00" + invNum;
		else if (invNum <= 99)
			sInvNum = "0" + invNum;
		
		String id = year + month + sInvNum;
		bean.setAccountingInvoiceIdentifier(id);
	}

	@Override
	public String create() throws ArahantException {
		return create(new Date());
	}

	public String create(Date invoiceDate) throws ArahantException {
		bean = new Invoice();
		bean.generateId();
		bean.setCreateDate(invoiceDate);
		int scheme = BProperty.getInt(StandardProperty.InvoiceNumberScheme, 1);
		if (scheme != 2)
			generateInvoiceNumber_1();
		else
			generateInvoiceNumber_2();
		return bean.getInvoiceId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			for (InvoiceLineItem ili : bean.getInvoiceLineItems())
				for (Timesheet t : ili.getTimesheets()) {
					t.setInvoiceLineItem(null);
					t.setState(TIMESHEET_APPROVED);
					ArahantSession.getHSU().saveOrUpdate(t);
				}
			//delete any adjustments
			List<Receipt> rlist = ArahantSession.getHSU().createCriteria(Receipt.class).eq(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT).joinTo(Receipt.RECEIPT_JOIN).eq(ReceiptJoin.INVOICE, bean).list();

			//delete adjustment join
			ArahantSession.getHSU().createCriteria(ReceiptJoin.class).in(ReceiptJoin.RECEIPT, rlist).delete();

			//now delete adjustments
			ArahantSession.getHSU().delete(rlist);

			//delete any payment associations
			ArahantSession.getHSU().createCriteria(ReceiptJoin.class).eq(ReceiptJoin.INVOICE, bean).delete();
			ArahantSession.getHSU().delete(bean.getInvoiceLineItems());
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public void deleteAdjustments() {
		Set<String> adjustIds = getAdjustmentIds();
		ArahantSession.getHSU().createCriteria(ReceiptJoin.class).eq(ReceiptJoin.INVOICE, bean).joinTo(ReceiptJoin.RECEIPT).eq(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT).delete();

		ArahantSession.getHSU().createCriteria(Receipt.class).in(Receipt.RECEIPT_ID, adjustIds).delete();
	}

	public void deletePaymentJoins() {
		ArahantSession.getHSU().createCriteria(ReceiptJoin.class).eq(ReceiptJoin.INVOICE, bean).joinTo(ReceiptJoin.RECEIPT).ne(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT).delete();
	}

	public String getAccountingInvoiceId() {
		return bean.getAccountingInvoiceIdentifier();
	}

	public double getAmount() {
		double total = 0;
		for (InvoiceLineItem ili : bean.getInvoiceLineItems())
			if (ili.getBillingType() == 'D')
				total += ili.getAmount();
			else
				total += ili.getAdjHours() * ili.getAdjRate();
		return total;
	}

	public double getBalance() {
		double amount = getAmount();
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		amount -= hsu.createCriteria(ReceiptJoin.class).sum(ReceiptJoin.AMOUNT).eq(ReceiptJoin.INVOICE, bean).joinTo(ReceiptJoin.RECEIPT).ne(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT).doubleVal();
		amount += hsu.createCriteria(ReceiptJoin.class).sum(ReceiptJoin.AMOUNT).eq(ReceiptJoin.INVOICE, bean).joinTo(ReceiptJoin.RECEIPT).eq(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT).doubleVal();
		return amount;
	}

	public int getDate() {
		return DateUtils.getDate(bean.getCreateDate());
	}

	public BGlAccount getGLAccount() {
		return new BGlAccount(bean.getGlAccount());
	}

	public String getId() {
		return bean.getInvoiceId();
	}

	public Set<String> getLineItemIds() {
		@SuppressWarnings("unchecked")
		List<String> l = (List) ArahantSession.getHSU().createCriteria(InvoiceLineItem.class).selectFields(InvoiceLineItem.INVOICELINEITEMID).eq(InvoiceLineItem.INVOICE, bean).list();

		HashSet<String> ret = new HashSet<String>();
		ret.addAll(l);
		return ret;
	}

	public Set<String> getPaymentIds() {
		HashSet<String> ret = new HashSet<String>();
		for (Receipt r : ArahantSession.getHSU().createCriteria(Receipt.class).ne(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT).joinTo(Receipt.RECEIPT_JOIN).eq(ReceiptJoin.INVOICE, bean).list())
			ret.add(r.getReceiptId());
		return ret;
	}

	public Set<String> getAdjustmentIds() {
		HashSet<String> ret = new HashSet<String>();
		for (Receipt r : ArahantSession.getHSU().createCriteria(Receipt.class).eq(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT).joinTo(Receipt.RECEIPT_JOIN).eq(ReceiptJoin.INVOICE, bean).list())
			ret.add(r.getReceiptId());

		return ret;
	}

	public String getPersonName() {
		if (bean.getBilledPerson() == null)
			return "";
		return bean.getBilledPerson().getNameLFM();
	}

	public BReceipt[] getReceipts() {
		return BReceipt.makeArray(ArahantSession.getHSU().createCriteria(Receipt.class).ne(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT).joinTo(Receipt.RECEIPT_JOIN).eq(ReceiptJoin.INVOICE, bean).list());

	}

	public BReceipt[] getAdjustments() {
		return BReceipt.makeArray(ArahantSession.getHSU().createCriteria(Receipt.class).eq(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT).joinTo(Receipt.RECEIPT_JOIN).eq(ReceiptJoin.INVOICE, bean).list());
	}

	public double getTotal() {
		double amount = getAmount();
		amount += ArahantSession.getHSU().createCriteria(ReceiptJoin.class).sum(ReceiptJoin.AMOUNT).eq(ReceiptJoin.INVOICE, bean).joinTo(ReceiptJoin.RECEIPT).eq(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT).doubleVal();
		return amount;
	}

	@Override
	public void load(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(Invoice.class, key);
	}

	public void setBilledPerson(Person person) {
		bean.setBilledPerson(person);
	}

	public void setDate(int date) {
		setCreateDate(DateUtils.getDate(date));
	}

	public void setGlAccountId(String glAccountId) {
		setArAccount(glAccountId);
	}

	public void setPersonId(String personId) {
		setBilledPerson(ArahantSession.getHSU().get(Person.class, personId));
	}

	public String getAccountingInvoiceIdentifier() {
		return bean.getAccountingInvoiceIdentifier();
	}

	public Date getCreateDate() {
		return bean.getCreateDate();
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public Date getExportDate() {
		return bean.getExportDate();
	}

	public String getInvoiceId() {
		return bean.getInvoiceId();
	}

	public void setAccountingInvoiceIdentifier(final String accountingInvoiceIdentifier) {
		bean.setAccountingInvoiceIdentifier(accountingInvoiceIdentifier);
	}

	public void setCompany(final CompanyBase company) {
		bean.setCompanyBase(company);
	}

	public void setCreateDate(final Date createDate) {
		bean.setCreateDate(createDate);
	}

	public void setDescription(final String description) {
		bean.setDescription(description);
	}

	public void setExportDate(final Date exportDate) {
		bean.setExportDate(exportDate);
	}

	public void setInvoiceId(final String invoiceId) {
		bean.setInvoiceId(invoiceId);
	}

	public void setArAccount(final String arAccount) {
		final GlAccount gl = ArahantSession.getHSU().get(GlAccount.class, arAccount);
		bean.setGlAccount(gl);
	}

	public void setCustomerId(final String customerProphetId) {
		final CompanyBase c = ArahantSession.getHSU().get(CompanyBase.class, customerProphetId);
		bean.setCompanyBase(c);
	}
	
	public String getPurchaseOrder() {
		return bean.getPurchaseOrder();
	}
	
	public void setPurchaseOrder(String po) {
		bean.setPurchaseOrder(po);
	}
	
	public short getPaymentTerms() {
		return bean.getPaymentTerms();
	}
	
	public void setPaymentTerms(short terms) {
		bean.setPaymentTerms(terms);
	}

	public void createInvoiceLineItem(final float adjHours, final double adjRate, final String description,
									  final String productServiceProphetId, final String[] timesheetIds, char type, double lineAmount, String projectId)
			throws ArahantException {
		final InvoiceLineItem ilm = new InvoiceLineItem();
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		ilm.generateId();
		ilm.setAmount(lineAmount);  // only used for Project-based billing
		ilm.setAdjHours(adjHours);
		ilm.setAdjRate(adjRate);
		ilm.setBillingType(type);
		if (projectId != null && !projectId.isEmpty())
			ilm.setProjectId(projectId);

		ilm.setDescription(description);
		ilm.setProductService(hsu.get(ProductService.class, productServiceProphetId));
		if (ilm.getProductService() == null)
			throw new ArahantException("Could not find Product!");
		ilm.setGlAccount(ilm.getProductService().getExpenseAccount());
		if (ilm.getGlAccount() == null)
			throw new ArahantException("Product/Service [" + ilm.getProductService().getAccsysId() + "] does not have default GL Expense Account!");
		ilm.setInvoice(bean);
		hsu.insert(ilm);
		for (final String element : timesheetIds) {
			final Timesheet ts = hsu.get(Timesheet.class, element);
			ts.setInvoiceLineItem(ilm);
			ts.setState(TIMESHEET_INVOICED);
			hsu.saveOrUpdate(ts);
		}
	}

	public void createInvoiceLineItem(final double amount, final String description, final String productServiceProphetId, String benefitJoinId) throws ArahantException {
		final InvoiceLineItem ilm = new InvoiceLineItem();
		ilm.setInvoice(bean);
		ilm.generateId();
		ilm.setAmount(amount);
		ilm.setDescription(description);
		ilm.setBillingType('D');
		ilm.setBenefitJoinId(benefitJoinId);
		ilm.setProductService(ArahantSession.getHSU().get(ProductService.class, productServiceProphetId));
		if (ilm.getProductService() == null)
			throw new ArahantException("Could not find Product!");
		ilm.setGlAccount(ilm.getProductService().getExpenseAccount());
		if (ilm.getGlAccount() == null)
			throw new ArahantException("Product/Service [" + ilm.getProductService().getAccsysId() + "] does not have default GL Expense Account!");

		addPendingInsert(new BInvoiceLineItem(ilm));
		//	hsu.insert(ilm);
	}

	public static String getReport(final HibernateSessionUtil hsu, String user, final String[] invoiceIds,
			final boolean includeDescription, final boolean includeLineItems, final boolean includeDetail) throws ArahantException {
		// get a list of requested invoices

		final ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
		for (final String element : invoiceIds)
			invoiceList.add(hsu.get(Invoice.class, element));

		user = hsu.getCurrentPerson().getProphetLogin().getUserLogin();

		// get the company of the person logged in
		CompanyDetail companyDetail = null;
		if (ArahantSession.systemName().equalsIgnoreCase(user)) {
			final CompanyDetail company = hsu.getFirst(CompanyDetail.class);
			if (company != null)
				companyDetail = company;
		} else {
			final Person p = hsu.getCurrentPerson();
			if (p.getCompanyBase() != null)
				companyDetail = hsu.get(CompanyDetail.class, p.getCompanyBase().getOrgGroupId());
			else
				companyDetail = hsu.getFirst(CompanyDetail.class);
		}

		InvoiceReport invoiceReport;
		switch (BProperty.getInt(StandardProperty.InvoiceFormat, 1)) {
			case 2:
				invoiceReport = new InvoiceReport2();  // Nestor
				break;
			case 3:
				invoiceReport = new InvoiceReport3();  // Way To Go
				break;
			default:
				invoiceReport = new InvoiceReport();
				break;
		}
		invoiceReport.setDescriptionIncluded(includeDescription);
		invoiceReport.setLineItemsIncluded(includeLineItems);
		invoiceReport.setDetailIncluded(includeDetail);

		// build the bean(s) report

		return invoiceReport.getReport(hsu, invoiceList, companyDetail);
	}

	public static void markForRetransmit(final HibernateSessionUtil hsu, final String[] invoiceIds) {
		for (final String element : invoiceIds) {
			final Invoice i = hsu.get(Invoice.class, element);
			i.setExportDate(null);
			hsu.saveOrUpdate(i);
		}
	}

	public static BInvoice[] searchCompanyInvoices(final HibernateSessionUtil hsu, final IInvoiceSearchCriteria in, final int max) {		
		final HibernateCriteriaUtil<Invoice> hcu = hsu.createCriteria(Invoice.class).isNull(Invoice.PERSON);

		hcu.orderByDesc(Invoice.CREATEDATE);

		hcu.setMaxResults(max);

		if (!isEmpty(in.getInvoiceId()))
			hcu.like(Invoice.ACCOUNTINGINVOICEIDENTIFIER, in.getInvoiceId());

		//	hcu.dateBetween(Invoice.CREATEDATE, DateUtils.getDate(in.getInvoiceStartDate()), DateUtils.getDate(in.getInvoiceEndDate()));
		if (in.getInvoiceStartDate() > 0)
			hcu.ge(Invoice.CREATEDATE, DateUtils.getDate(in.getInvoiceStartDate()));
		if (in.getInvoiceEndDate() > 0)
			hcu.le(Invoice.CREATEDATE, DateUtils.getDate(in.getInvoiceEndDate()));
		
		hcu.joinTo(Invoice.CLIENTCOMPANY).eq(ClientCompany.COMPANY, hsu.getCurrentCompany());

		if (!isEmpty(in.getClientId()))
			hcu.joinTo(Invoice.COMPANYBASE).eq(OrgGroup.ORGGROUPID, in.getClientId());

		switch (in.getInvoiceStatus()) {
			case 0:
				break;
			case 1:
				hcu.isNull(Invoice.EXPORTDATE);
				break;
			case 2:
				hcu.notNull(Invoice.EXPORTDATE);
				break;
		}

		final List<Invoice> res = hcu.list();

		final List<Invoice> results = new LinkedList<Invoice>();

		for (Invoice i : res) {
			//calculate the total
			double total = 0;

			for (InvoiceLineItem ili : i.getInvoiceLineItems())
				total += ili.getAdjRate() * ili.getAdjHours() + ili.getAmount();

			switch (in.getAmountSearchType()) {
				case 0:
					results.add(i);
					break;
				case 11:
				case 1:
					if (total >= in.getAmount())
						results.add(i);
					break;
				case 12:
				case 2:
					if (total <= in.getAmount())
						results.add(i);
					break;
				case 13:
				case 3:
					if (Double.compare(total, in.getAmount()) == 0)
						results.add(i);
					break;
				case 14:
				case 4:
					if (Double.compare(total, in.getAmount()) != 0)
						results.add(i);
					break;
			}
		}

		final BInvoice[] ret = new BInvoice[results.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BInvoice(results.get(loop));

		return ret;
	}

	public String getCompanyName() {
		return bean.getCompanyBase().getName();
	}

	public double getInvoiceAmount() {
		double invoiceAmount = 0;
		for (InvoiceLineItem ili : bean.getInvoiceLineItems())
			invoiceAmount += ili.getAdjHours() * ili.getAdjRate() + ili.getAmount();

		invoiceAmount = ((double) Math.round(invoiceAmount * 100)) / 100;

		return invoiceAmount;
	}

	public String getInvoiceAmountFormatted() {
		return MoneyUtils.formatMoney(getInvoiceAmount());
	}

	/**
	 * @param hsu
	 * @param invoiceIds
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] invoiceIds) throws ArahantException {
		for (final String element : invoiceIds)
			new BInvoice(element).delete();
	}

	public static BInvoice[] listUnexported(final HibernateSessionUtil hsu) {
		final List ibList = hsu.createCriteria(Invoice.class).isNull(Invoice.EXPORTDATE).orderBy(Invoice.CREATEDATE).list();

		final BInvoice[] ibl = new BInvoice[ibList.size()];

		for (int loop = 0; loop < ibList.size(); loop++)
			ibl[loop] = new BInvoice((Invoice) ibList.get(loop));

		return ibl;
	}

	public String getArAccount() {
		if (bean.getGlAccount() == null) {
			logger.debug("Found invoice " + bean.getInvoiceId() + " with a null GL Account");
			return "";
		}
		return bean.getGlAccount().getAccountNumber();
	}

	public String getCustomerAcctId() {

		if (BProperty.getBoolean("Use Quickbooks")) {
			if (bean.getCompanyBase().getOrgGroupType() == CLIENT_TYPE) {
				QuickbooksClientChange qcc = ArahantSession.getHSU().createCriteria(QuickbooksClientChange.class).eq(QuickbooksClientChange.CLIENT, bean.getCompanyBase()).first();
				if (qcc == null)
					throw new ArahantWarning("Please synchronise Clients with Quickbooks first.");
				return qcc.getQbRecordId();
			}

			if (bean.getCompanyBase().getOrgGroupType() == VENDOR_TYPE) {
				QuickbooksVendorChange qvc = ArahantSession.getHSU().createCriteria(QuickbooksVendorChange.class).eq(QuickbooksVendorChange.VENDOR, bean.getCompanyBase()).first();

				if (qvc == null)
					throw new ArahantWarning("Please synchronise Vendors with Quickbooks first.");

				return qvc.getQbRecordId();
			}
		}

		return bean.getCompanyBase().getExternalId();
	}

	public double getTotalBillableHours() {

		final HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(Timesheet.class);
		hcu.joinTo(Timesheet.INVOICELINEITEM).eq(InvoiceLineItem.INVOICE, bean);
		hcu.eq(Timesheet.BILLABLE, 'Y');
		hcu.sum(Timesheet.TOTALHOURS);

		final Double d = (Double) hcu.list().get(0);

		if (d == null)
			return 0;

		return d;
	}

	public String getCustomerProphetId() {
		return bean.getCompanyBase().getOrgGroupId();
	}

	public BInvoiceLineItem[] getLineItems() {

		final List l = ArahantSession.getHSU().createCriteria(InvoiceLineItem.class).eq(InvoiceLineItem.INVOICE, bean).orderBy(InvoiceLineItem.INVOICELINEITEMID).list();

		final BInvoiceLineItem[] ret = new BInvoiceLineItem[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BInvoiceLineItem((InvoiceLineItem) l.get(loop));

		return ret;
	}

	/**
	 * @param hsu
	 * @param invoiceIds
	 * @throws ArahantException
	 */
	public static void markExported(final HibernateSessionUtil hsu, final String[] invoiceIds) throws ArahantException {
		for (final String element : invoiceIds)
			new BInvoice(element).markExported();
	}

	private void markExported() {
		bean.setExportDate(new Date());
	}

	public static void makeInvoicesForPersons(int asOfDate) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		ArahantSession.setCalcDate(asOfDate);

		//create invoices for everybody that should be paying for Benefits
		HibernateScrollUtil<HrBillingStatusHistory> scr = hsu.createCriteria(HrBillingStatusHistory.class).dateInside(HrBillingStatusHistory.DATE, HrBillingStatusHistory.FINAL_DATE, asOfDate).joinTo(HrBillingStatusHistory.PERSON).joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING).dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, asOfDate).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).notNull(HrBenefit.PRODUCTSERVICE).scroll();

		Calendar sixMonthsAgo = Calendar.getInstance();
		sixMonthsAgo.add(Calendar.MONTH, -6);

		HashSet<String> doneList = new HashSet<>();
		HashSet<String> personDoneList = new HashSet<>();

		while (scr.next()) {
			HrBillingStatusHistory h = scr.get();

			if (personDoneList.contains(h.getPerson().getPersonId()))
				continue;
			personDoneList.add(h.getPerson().getPersonId());

			if (doneList.contains(h.getBillingStatusHistoryId()))
				continue;

			doneList.add(h.getBillingStatusHistoryId());

			//if this is a special one where I wait 6 months, skip it
			if (AIProperty.getBoolean("SixMonthDelayBillingStatus", h.getBillingStatus().getBillingStatusId()))
				if (DateUtils.getCalendar(h.getStartDate()).after(sixMonthsAgo))
					continue;

			//Some billing status's don't get billed
			if (AIProperty.getBoolean("NoBillBillingStatus", h.getBillingStatus().getBillingStatusId()))
				continue;

			BInvoice inv = new BInvoice();
			inv.create(new Date());
			inv.setBilledPerson(h.getPerson());
			inv.setDescription("Benefit Invoice");
			inv.setCreateDate(asOfDate);

			BCompany company = new BCompany(hsu.getCurrentCompany());
			if (isEmpty(company.getEmployeeAdvanceAccountId()))
				throw new ArahantWarning("Employee Advance Account is not set up.  Can not continue.");

			inv.setArAccount(company.getEmployeeAdvanceAccountId());

			//for each benefit they pay for, create a line item
			HibernateScrollUtil<HrBenefitJoin> bjScr = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, h.getPerson()).eq(HrBenefitJoin.COVERED_PERSON, h.getPerson()).dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, asOfDate).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).notNull(HrBenefit.PRODUCTSERVICE).scroll();

			while (bjScr.next()) {
				HrBenefitJoin bj = bjScr.get();

				//  need to check the benefit and call hrBenefit.getProductService() to get the product service
				ProductService ps = bj.getHrBenefitConfig().getHrBenefit().getProductService();

				//String hold=bj.getCalculatedCost();
				char cobraSave = bj.getUsingCOBRA();
				if (AIProperty.getBoolean("ForceCOBRAAmountBillingStatus", h.getBillingStatus().getBillingStatusId())) {
					bj.setUsingCOBRA('Y');
					bj.setCalculatedCost(null);
					bj.removeFromAIEngine();
					bj.linkToEngine();
					ArahantSession.runAI();
					//	System.out.println("Cobra cost change did "+hold+" to "+bj.getCalculatedCost());
				}

				double cost = bj.getCalculatedCostMonthly();

				//if leave is in middle of month, use pay period cost
				Calendar leaveDate = DateUtils.getCalendar(h.getStartDate());
				if (leaveDate.get(Calendar.DAY_OF_MONTH) < 15)
					if (leaveDate.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)
							&& leaveDate.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR))
						cost = cost * 12 / bj.getPpy();

				inv.createInvoiceLineItem(cost, bj.getHrBenefitConfig().getName(),
						ps.getProductId(), bj.getBenefitJoinId());
				bj.setUsingCOBRA(cobraSave);
			}

			inv.insert();

			System.out.println("inserted invoice " + inv.getInvoiceId() + " " + inv.getPersonName());
		}
		scr.close();
	}

	private void setCreateDate(int asOfDate) {
		setCreateDate(DateUtils.getDate(asOfDate));
	}

	public static void main(String args[]) {
		ArahantSession.getHSU().setCurrentPersonToArahant();
		ArahantSession.getHSU().beginTransaction();
		BInvoice.makeInvoicesForPersons(DateUtils.now());
		ArahantSession.getHSU().commitTransaction();
	}
}
