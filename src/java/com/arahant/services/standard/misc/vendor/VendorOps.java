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


package com.arahant.services.standard.misc.vendor;

import com.arahant.beans.OrgGroup;
import com.arahant.beans.VendorCompany;
import com.arahant.beans.VendorGroup;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.Crypto;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardMiscVendorOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class VendorOps extends ServiceBase {

	private static final ArahantLogger logger = new ArahantLogger(VendorOps.class);

	@WebMethod()
	public ListGlAccountsByTypeReturn listGlAccountsByType(/*
			 * @WebParam(name = "in")
			 */final ListGlAccountsByTypeInput in) {

		final ListGlAccountsByTypeReturn ret = new ListGlAccountsByTypeReturn();

		try {
			checkLogin(in);

			ret.setGLAccounts(BGlAccount.listByType(hsu, 10));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public DeleteCompanyReturn deleteCompany(/*
			 * @WebParam(name = "in")
			 */final DeleteCompanyInput in) {
		final DeleteCompanyReturn ret = new DeleteCompanyReturn();

		try {
			checkLogin(in);

			BVendorCompany.deleteCompanies(hsu, in.getVendorCompanyId());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public LoadVendorCompanyReturn loadVendorCompany(/*
			 * @WebParam(name = "in")
			 */final LoadVendorCompanyInput in) {
		final LoadVendorCompanyReturn ret = new LoadVendorCompanyReturn();

		try {
			checkLogin(in);

			ret.setData(new BVendorCompany(in.getVendorCompanyId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public NewVendorCompanyReturn newVendorCompany(/*
			 * @WebParam(name = "in")
			 */final NewVendorCompanyInput in) {
		final NewVendorCompanyReturn ret = new NewVendorCompanyReturn();

		try {
			checkLogin(in);

			final BVendorCompany v = new BVendorCompany();
			ret.setId(v.create());
			in.makeVendor(v);
			v.insert();

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchVendorCompanyReturn searchVendorCompany(/*
			 * @WebParam(name = "in")
			 */final SearchVendorCompanyInput in) {
		final SearchVendorCompanyReturn ret = new SearchVendorCompanyReturn();

		try {
			checkLogin(in);

			final BVendorCompany v[] = BVendorCompany.search(hsu, in.getName(), in.getMainContactFirstName(),
					in.getMainContactLastName(), in.getIdentifier(),
					in.getExpenseGLAccountId(), in.getAccountNumber(), ret.getCap());

			ret.setVendorList(v);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SaveVendorCompanyReturn saveVendorCompany(/*
			 * @WebParam(name = "in")
			 */final SaveVendorCompanyInput in) {
		final SaveVendorCompanyReturn ret = new SaveVendorCompanyReturn();

		try {
			checkLogin(in);

			final BVendorCompany v = new BVendorCompany(in.getOrgGroupId());
			in.makeCompany(v);
			v.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;

	}

	@WebMethod()
	public SearchScreenGroupsReturn searchScreenGroups(/*
			 * @WebParam(name = "in")
			 */final SearchScreenGroupsInput in) {
		final SearchScreenGroupsReturn ret = new SearchScreenGroupsReturn();

		try {
			checkLogin(in);

			ret.setScreenDef(BScreenGroup.searchScreenGroups(hsu, in.getName(), in.getExtId(), in.getSearchTopLevelOnly() ? 2 : 0, null, 2, ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchSecurityGroupsReturn searchSecurityGroups(/*
			 * @WebParam(name = "in")
			 */final SearchSecurityGroupsInput in) {
		final SearchSecurityGroupsReturn ret = new SearchSecurityGroupsReturn();

		try {
			checkLogin(in);
			ret.setSecurityDef(BSecurityGroup.searchSecurityGroups(in.getName(), ret.getCap()));
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListEDICommunicationSchemesReturn listEDICommunicationSchemes(/*
			 * @WebParam(name = "in")
			 */final ListEDICommunicationSchemesInput in) {
		final ListEDICommunicationSchemesReturn ret = new ListEDICommunicationSchemesReturn();
		try {
			checkLogin(in);

			ListEDICommunicationSchemesReturnItem[] item = new ListEDICommunicationSchemesReturnItem[2];
			item[0] = new ListEDICommunicationSchemesReturnItem();
			item[0].setDefaultPort(21);
			item[0].setId("ftp");
			item[0].setDescription("ftp");
			item[1] = new ListEDICommunicationSchemesReturnItem();
			item[1].setDefaultPort(22);
			item[1].setId("sftp");
			item[1].setDescription("sftp");
			ret.setItem(item);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public VerifyEncryptionKeyReturn verifyEncryptionKey(/*
			 * @WebParam(name = "in")
			 */final VerifyEncryptionKeyInput in) {
		final VerifyEncryptionKeyReturn ret = new VerifyEncryptionKeyReturn();
		try {
			checkLogin(in);

			short result = Crypto.verifyPGPPublicKeyText(in.getKeyText(), in.getKeyIdInHex());

			if (result == 1)
				throw new ArahantException("The Public Key Text is invalid.");
			else if (result == 2)
				throw new ArahantException("The Public Key ID is invalid.");
			else if (result == 3)
				throw new ArahantException("The Public Key ID was found but the key is not an encryption key.");
			else if (result != 0)
				throw new ArahantException("There was an unexpected error processing the Public Key data.");

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEdiInterfacesReturn listEdiInterfaces(/*
			 * @WebParam(name = "in")
			 */final ListEdiInterfacesInput in) {
		final ListEdiInterfacesReturn ret = new ListEdiInterfacesReturn();
		try {
			checkLogin(in);

			EdiInterface[] edi = new EdiInterface[18];
			edi[0] = new EdiInterface(VendorCompany.BCBS, "Blue Cross and Blue Shield");
			edi[1] = new EdiInterface(VendorCompany.BCN, "Blue Care Network");
			edi[2] = new EdiInterface(VendorCompany.VSP, "VSP");
			edi[3] = new EdiInterface(VendorCompany.HUMANA, "Humana");
			edi[4] = new EdiInterface(VendorCompany.EBC, "Employee Benefit Concepts");
			edi[5] = new EdiInterface(VendorCompany.EHIM, "Employee Health Insurance Management");
			edi[6] = new EdiInterface(VendorCompany.DELTA_DENTAL, "Delta Dental");
			edi[7] = new EdiInterface(VendorCompany.AMERITAS, "Ameritas");
			edi[8] = new EdiInterface(VendorCompany.MUTUAL, "Mutual of Omaha");
			edi[9] = new EdiInterface(VendorCompany.CONSOCIATES, "Consociates");
			edi[10] = new EdiInterface(VendorCompany.EYEMED, "EyeMed Vision");
			edi[11] = new EdiInterface(VendorCompany.CIGNA, "CIGNA");
			edi[12] = new EdiInterface(VendorCompany.CAREMARK, "Caremark");
			edi[13] = new EdiInterface(VendorCompany.CBG, "CBG");
			edi[14] = new EdiInterface(VendorCompany.AETNA, "Aetna");
			edi[15] = new EdiInterface(VendorCompany.MADISON_DENTAL, "Madison Dental");
			edi[16] = new EdiInterface(VendorCompany.PRISM_EXPORT, "Prism / Vision Financial");
			edi[17] = new EdiInterface(VendorCompany.NORTH_AMERICA_ADMINISTRATORS, "North America Administrators");
			// Make sure you increase the size of the array

			ret.setItem(edi);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchOrgGroupsReturn searchOrgGroups(/*
			 * @WebParam(name = "in")
			 */final SearchOrgGroupsInput in) {
		final SearchOrgGroupsReturn ret = new SearchOrgGroupsReturn();

		try {
			checkLogin(in);
			@SuppressWarnings("unchecked")
			List<String> excludeGroups = (List) ArahantSession.getHSU().createCriteria(OrgGroup.class).selectFields(OrgGroup.ORGGROUPID).joinTo(OrgGroup.VENDOR_GROUPS).eq(VendorGroup.VENDOR, new BVendorCompany(in.getVendorId()).getBean()).list();
			ret.setOrgGroups(new BCompany(ArahantSession.getHSU().getCurrentCompany()).searchOrgGroups(in.getName(), 50, false, excludeGroups));

			if (!isEmpty(in.getOrgGroupId()))
				ret.setSelectedItem(new BOrgGroup(in.getOrgGroupId()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListVendorGroupDetailsReturn listVendorGroupDetails(/*
			 * @WebParam(name = "in")
			 */final ListVendorGroupDetailsInput in) {

		final ListVendorGroupDetailsReturn ret = new ListVendorGroupDetailsReturn();

		try {
			checkLogin(in);

			ret.setItem(BVendorGroup.list(in.getVendorId(), ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewVendorGroupReturn newVendorGroup(/*
			 * @WebParam(name = "in")
			 */final NewVendorGroupInput in) {

		final NewVendorGroupReturn ret = new NewVendorGroupReturn();

		try {
			checkLogin(in);

			final BVendorGroup x = new BVendorGroup();
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
	public SaveVendorGroupReturn saveVendorGroup(/*
			 * @WebParam(name = "in")
			 */final SaveVendorGroupInput in) {

		final SaveVendorGroupReturn ret = new SaveVendorGroupReturn();

		try {
			checkLogin(in);

			final BVendorGroup x = new BVendorGroup(in.getId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteVendorGroupsReturn deleteVendorGroups(/*
			 * @WebParam(name = "in")
			 */final DeleteVendorGroupsInput in) {

		final DeleteVendorGroupsReturn ret = new DeleteVendorGroupsReturn();

		try {
			checkLogin(in);

			BVendorGroup.delete(in.getIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
