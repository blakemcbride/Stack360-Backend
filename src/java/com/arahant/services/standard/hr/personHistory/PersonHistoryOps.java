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


/**
 * 
 */
package com.arahant.services.standard.hr.personHistory;

import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrPersonHistoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class PersonHistoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(PersonHistoryOps.class);
	
	public PersonHistoryOps() {
	}
	
    @WebMethod()
	public SearchPersonsReturn searchPersons(/*@WebParam(name = "in")*/final SearchPersonsInput in)		
	{
		final SearchPersonsReturn ret=new SearchPersonsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BPerson.searchPersons(hsu.getCurrentCompany().getCompanyId(), in.getLastName(), in.getFirstName(), ret.getHighCap()));

			if (BRight.checkRight("AccessHrEmployee") == ACCESS_LEVEL_WRITE)
			{
				ret.setCanAddOrEdit(true);
			}
			else
			{
				ret.setSelectedItem(new SearchPersonsReturnItem(new BPerson(ArahantSession.getCurrentPerson())));
				ret.setCanAddOrEdit(false);
			}

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public SearchPersonHistoryReturn searchPersonHistory(/*@WebParam(name = "in")*/final SearchPersonHistoryInput in)		
	{
		final SearchPersonHistoryReturn ret=new SearchPersonHistoryReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BPersonH.listHistory(ret.getCap(), in.getPersonId(), in.getFromDate(), in.getToDate()), in.getPersonId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public ListHistoryComparisonsReturn listHistoryComparisons(/*@WebParam(name = "in")*/final ListHistoryComparisonsInput in)		
	{
		final ListHistoryComparisonsReturn ret=new ListHistoryComparisonsReturn();
		try
		{
			checkLogin(in);

			List<ListHistoryComparisonsReturnItem> l = new ArrayList<ListHistoryComparisonsReturnItem>();

			if(!isEmpty(in.getHistoryId()))
			{
				BPersonH ph = new BPersonH(in.getHistoryId());
				BPersonH bph = BPersonH.getPreviousHistory(ret.getCap(), ph.getRecordChangeDate(), ph.getPersonId());

				l.add(new ListHistoryComparisonsReturnItem("First Name", bph!=null?checkNull(bph.getFname(), "(No First Name)"):"(First Record)", checkNull(ph.getFname(), "(No First Name)")));
				l.add(new ListHistoryComparisonsReturnItem("Middle Name", bph!=null?checkNull(bph.getMname(), "(No Middle Name)"):"(First Record)", checkNull(ph.getMname(), "(No Middle Name)")));
				l.add(new ListHistoryComparisonsReturnItem("Last Name", bph!=null?checkNull(bph.getLname(), "(No Last Name)"):"(First Record)", checkNull(ph.getLname(), "(No Last Name)")));
				l.add(new ListHistoryComparisonsReturnItem("Nick Name", bph!=null?checkNull(bph.getNickName(), "(No NickName)"):"(First Record)", checkNull(ph.getNickName(), "(No NickName)")));
				l.add(new ListHistoryComparisonsReturnItem("SSN", bph!=null?checkNull(bph.getSsn(), "(No SSN)"):"(First Record)", checkNull(ph.getSsn(), "(No SSN)")));
				l.add(new ListHistoryComparisonsReturnItem("Sex", bph!=null?checkNull(bph.getSex().equals("M")?"Male":(bph.getSex().equals("F")?"Female":"Unknown"), "(No Sex)"):"(First Record)", checkNull(ph.getSex().equals("M")?"Male":(ph.getSex().equals("F")?"Female":"Unknown"), "(No Sex)")));
				l.add(new ListHistoryComparisonsReturnItem("Date of Birth", bph!=null?checkNull(bph.getDob(), "(No Date of Birth)"):"(First Record)", checkNull(ph.getDob(), "(No Date of Birth)")));
				l.add(new ListHistoryComparisonsReturnItem("Email", bph!=null?checkNull(bph.getPersonalEmail(), "(No Email)"):"(First Record)", checkNull(ph.getPersonalEmail(), "(No Email)")));
				l.add(new ListHistoryComparisonsReturnItem("Job Title", bph!=null?checkNull(bph.getJobTitle(), "(No Job Title)"):"(First Record)", checkNull(ph.getJobTitle(), "(No Job Title)")));
				l.add(new ListHistoryComparisonsReturnItem("Handicap", bph!=null?checkNull(bph.getHandicap().equals("Y")?"Yes":"No", "(No Handicap)"):"(First Record)", checkNull(ph.getHandicap().equals("Y")?"Yes":"No", "(No Handicap)")));
				l.add(new ListHistoryComparisonsReturnItem("Student", bph!=null?checkNull(bph.getStudent().equals("Y")?"Yes":"No", "(No Student)"):"(First Record)", checkNull(ph.getStudent().equals("Y")?"Yes":"No", "(No Student)")));
				l.add(new ListHistoryComparisonsReturnItem("Citizenship", bph!=null?checkNull(bph.getCitizenship(), "(No Citizenship)"):"(First Record)", checkNull(ph.getCitizenship(), "(No Citizenship)")));
				l.add(new ListHistoryComparisonsReturnItem("Height", bph!=null?checkNull(bph.getHeight(), "(No Height)"):"(First Record)", checkNull(ph.getHeight(), "(No Height)")));
				l.add(new ListHistoryComparisonsReturnItem("Weight", bph!=null?checkNull(bph.getWeight(), "(No Weight)"):"(First Record)", checkNull(ph.getWeight(), "(No Weight)")));
				l.add(new ListHistoryComparisonsReturnItem("Visa", bph!=null?checkNull(bph.getVisa(), "(No Visa)"):"(First Record)", checkNull(ph.getVisa(), "(No Visa)")));
				l.add(new ListHistoryComparisonsReturnItem("Visa Status Date", bph!=null?checkNull(bph.getVisaStatusDate(), "(No Visa Status Date)"):"(First Record)", checkNull(ph.getVisaStatusDate(), "(No Visa Status Date)")));
				l.add(new ListHistoryComparisonsReturnItem("Visa Exp Date", bph!=null?checkNull(bph.getVisaExpirationDate(), "(No Visa Exp Date)"):"(First Record)", checkNull(ph.getVisaExpirationDate(), "(No Visa Exp Date)")));
				l.add(new ListHistoryComparisonsReturnItem("I9 Completed", bph!=null?checkNull(bph.getI9Part1().equals("Y")&&bph.getI9Part2().equals("Y")?"Yes":"No", "(No I9)"):"(First Record)", checkNull(ph.getI9Part1().equals("Y")&&ph.getI9Part2().equals("Y")?"Yes":"No", "(No I9)")));
				l.add(new ListHistoryComparisonsReturnItem("Driver's License State", bph!=null?checkNull(bph.getDriversLicenseState(), "(No Driver's License State)"):"(First Record)", checkNull(ph.getDriversLicenseState(), "(No Driver's License State)")));
				l.add(new ListHistoryComparisonsReturnItem("Driver's License Number", bph!=null?checkNull(bph.getDriversLicenseNumber(), "(No Driver's License Number)"):"(First Record)", checkNull(ph.getDriversLicenseNumber(), "(No Driver's License Number)")));
				l.add(new ListHistoryComparisonsReturnItem("Driver's License Exp Date", bph!=null?checkNull(bph.getDriversLicenseExp(), "(No Driver's License Exp Date)"):"(First Record)", checkNull(ph.getDriversLicenseExp(), "(No Driver's License Exp Date)")));
				l.add(new ListHistoryComparisonsReturnItem("Smoker", bph!=null?checkNull(bph.getSmoker().equals("Y")?"Yes":"No", "(No Smoker)"):"(First Record)", checkNull(ph.getSmoker().equals("Y")?"Yes":"No", "(No Smoker)")));
				l.add(new ListHistoryComparisonsReturnItem("Auto Insurance Carrier", bph!=null?checkNull(bph.getAutoInsuranceCarrier(), "(No Auto Insurance Carrier)"):"(First Record)", checkNull(ph.getAutoInsuranceCarrier(), "(No Auto Insurance Carrier)")));
				l.add(new ListHistoryComparisonsReturnItem("Auto Insurance Policy", bph!=null?checkNull(bph.getAutoInsurancePolicy(), "(No Auto Insurance Policy)"):"(First Record)", checkNull(ph.getAutoInsurancePolicy(), "(No Auto Insurance Policy)")));
				l.add(new ListHistoryComparisonsReturnItem("Auto Insurance Exp Date", bph!=null?checkNull(bph.getAutoInsuranceExp(), "(No Auto Insurance Exp Date)"):"(First Record)", checkNull(ph.getAutoInsuranceExp(), "(No Auto Insurance Exp Date)")));
				l.add(new ListHistoryComparisonsReturnItem("Auto Insurance Start Date", bph!=null?checkNull(bph.getAutoInsuranceStart(), "(No Auto Insurance Start Date)"):"(First Record)", checkNull(ph.getAutoInsuranceStart(), "(No Auto Insurance Start Date)")));
				l.add(new ListHistoryComparisonsReturnItem("Auto Insurance Coverage", bph!=null?checkNull(bph.getAutoInsuranceCoverage(), "(No Auto Insurance Coverage)"):"(First Record)", checkNull(ph.getAutoInsuranceCoverage(), "(No Auto Insurance Coverage)")));
				l.add(new ListHistoryComparisonsReturnItem("Default Project", bph!=null?checkNull(bph.getDefaultProjectName(), "(No Default Project)"):"(First Record)", checkNull(ph.getDefaultProjectName(), "(No Default Project)")));
			}
			else
			{
				BPerson p = new BPerson(in.getPersonId());
				BPersonH bph = BPersonH.getPreviousHistory(ret.getCap(), p.getPerson().getRecordChangeDate(), in.getPersonId());


				l.add(new ListHistoryComparisonsReturnItem("First Name", bph!=null?checkNull(bph.getFname(), "(No First Name)"):"(First Record)", checkNull(p.getFirstName(), "(No First Name)")));
				l.add(new ListHistoryComparisonsReturnItem("Middle Name", bph!=null?checkNull(bph.getMname(), "(No Middle Name)"):"(First Record)", checkNull(p.getMiddleName(), "(No Middle Name)")));
				l.add(new ListHistoryComparisonsReturnItem("Last Name", bph!=null?checkNull(bph.getLname(), "(No Last Name)"):"(First Record)", checkNull(p.getLastName(), "(No Last Name)")));
				l.add(new ListHistoryComparisonsReturnItem("Nick Name", bph!=null?checkNull(bph.getNickName(), "(No NickName)"):"(First Record)", checkNull(p.getNickName(), "(No NickName)")));
				l.add(new ListHistoryComparisonsReturnItem("SSN", bph!=null?checkNull(bph.getSsn(), "(No SSN)"):"(First Record)", checkNull(p.getSsn(), "(No SSN)")));
				l.add(new ListHistoryComparisonsReturnItem("Sex", bph!=null?checkNull(bph.getSex().equals("M")?"Male":(bph.getSex().equals("F")?"Female":"Unknown"), "(No Sex)"):"(First Record)", checkNull(p.getSex().equals("M")?"Male":(p.getSex().equals("F")?"Female":"Unknown"), "(No Sex)")));
				l.add(new ListHistoryComparisonsReturnItem("Date of Birth", bph!=null?checkNull(bph.getDob(), "(No Date of Birth)"):"(First Record)", checkNull(DateUtils.getDateFormatted(p.getDob()), "(No Date of Birth)")));
				l.add(new ListHistoryComparisonsReturnItem("Email", bph!=null?checkNull(bph.getPersonalEmail(), "(No Email)"):"(First Record)", checkNull(p.getPersonalEmail(), "(No Email)")));
				l.add(new ListHistoryComparisonsReturnItem("Job Title", bph!=null?checkNull(bph.getJobTitle(), "(No Job Title)"):"(First Record)", checkNull(p.getJobTitle(), "(No Job Title)")));
				l.add(new ListHistoryComparisonsReturnItem("Disabled", bph!=null?checkNull(bph.getHandicap().equals("Y")?"Yes":"No", "(No Handicap)"):"(First Record)", checkNull(p.getHandicap()?"Yes":"No", "(No Handicap)")));
				l.add(new ListHistoryComparisonsReturnItem("Student", bph!=null?checkNull(bph.getStudent().equals("Y")?"Yes":"No", "(No Student)"):"(First Record)", checkNull(p.getStudent()?"Yes":"No", "(No Student)")));
				l.add(new ListHistoryComparisonsReturnItem("Citizenship", bph!=null?checkNull(bph.getCitizenship(), "(No Citizenship)"):"(First Record)", checkNull(p.getCitizenship(), "(No Citizenship)")));
				l.add(new ListHistoryComparisonsReturnItem("Height", bph!=null?checkNull(bph.getHeight(), "(No Height)"):"(First Record)", checkNull(p.getHeight(), "(No Height)")));
				l.add(new ListHistoryComparisonsReturnItem("Weight", bph!=null?checkNull(bph.getWeight(), "(No Weight)"):"(First Record)", checkNull(p.getWeight(), "(No Weight)")));
				l.add(new ListHistoryComparisonsReturnItem("Visa", bph!=null?checkNull(bph.getVisa(), "(No Visa)"):"(First Record)", checkNull(p.getVisa(), "(No Visa)")));
				l.add(new ListHistoryComparisonsReturnItem("Visa Status Date", bph!=null?checkNull(bph.getVisaStatusDate(), "(No Visa Status Date)"):"(First Record)", checkNull(DateUtils.getDateFormatted(p.getVisaStatusDate()), "(No Visa Status Date)")));
				l.add(new ListHistoryComparisonsReturnItem("Visa Exp Date", bph!=null?checkNull(bph.getVisaExpirationDate(), "(No Visa Exp Date)"):"(First Record)", checkNull(DateUtils.getDateFormatted(p.getVisaExpirationDate()), "(No Visa Exp Date)")));
				l.add(new ListHistoryComparisonsReturnItem("I9 Completed", bph!=null?checkNull(bph.getI9Part1().equals("Y")&&bph.getI9Part2().equals("Y")?"Yes":"No", "(No I9)"):"(First Record)", checkNull(p.getI9Part1()&&p.getI9Part2()?"Yes":"No", "(No I9)")));
				l.add(new ListHistoryComparisonsReturnItem("Driver's License State", bph!=null?checkNull(bph.getDriversLicenseState(), "(No Driver's License State)"):"(First Record)", checkNull(p.getDriversLicenseState(), "(No Driver's License State)")));
				l.add(new ListHistoryComparisonsReturnItem("Driver's License Number", bph!=null?checkNull(bph.getDriversLicenseNumber(), "(No Driver's License Number)"):"(First Record)", checkNull(p.getDriversLicenseNumber(), "(No Driver's License Number)")));
				l.add(new ListHistoryComparisonsReturnItem("Driver's License Exp Date", bph!=null?checkNull(bph.getDriversLicenseExp(), "(No Driver's License Exp Date)"):"(First Record)", checkNull(DateUtils.getDateFormatted(p.getDriversLicenseExpirationDate()), "(No Driver's License Exp Date)")));
				l.add(new ListHistoryComparisonsReturnItem("Smoker", bph!=null?checkNull(bph.getSmoker().equals("Y")?"Yes":"No", "(No Smoker)"):"(First Record)", checkNull(p.getPerson().getSmoker() == 'Y'?"Yes":"No", "(No Smoker)")));
				l.add(new ListHistoryComparisonsReturnItem("Auto Insurance Carrier", bph!=null?checkNull(bph.getAutoInsuranceCarrier(), "(No Auto Insurance Carrier)"):"(First Record)", checkNull(p.getAutomotiveInsuranceCarrier(), "(No Auto Insurance Carrier)")));
				l.add(new ListHistoryComparisonsReturnItem("Auto Insurance Policy", bph!=null?checkNull(bph.getAutoInsurancePolicy(), "(No Auto Insurance Policy)"):"(First Record)", checkNull(p.getAutomotiveInsurancePolicyNumber(), "(No Auto Insurance Policy)")));
				l.add(new ListHistoryComparisonsReturnItem("Auto Insurance Exp Date", bph!=null?checkNull(bph.getAutoInsuranceExp(), "(No Auto Insurance Exp Date)"):"(First Record)", checkNull(DateUtils.getDateFormatted(p.getAutomotiveInsuranceExpirationDate()), "(No Auto Insurance Exp Date)")));
				l.add(new ListHistoryComparisonsReturnItem("Auto Insurance Start Date", bph!=null?checkNull(bph.getAutoInsuranceStart(), "(No Auto Insurance Start Date)"):"(First Record)", checkNull(DateUtils.getDateFormatted(p.getAutomotiveInsuranceStartDate()), "(No Auto Insurance Start Date)")));
				l.add(new ListHistoryComparisonsReturnItem("Auto Insurance Coverage", bph!=null?checkNull(bph.getAutoInsuranceCoverage(), "(No Auto Insurance Coverage)"):"(First Record)", checkNull(p.getAutomotiveInsuranceCoverage(), "(No Auto Insurance Coverage)")));
				l.add(new ListHistoryComparisonsReturnItem("Default Project", bph!=null?checkNull(bph.getDefaultProjectName(), "(No Default Project)"):"(First Record)", checkNull(p.getDefaultProjectName(), "(No Default Project)")));
			}


			//Remove the items that have not changed
			List<ListHistoryComparisonsReturnItem> unchanged = new ArrayList<ListHistoryComparisonsReturnItem>();

			for (ListHistoryComparisonsReturnItem r : l)
			{
				if (r.getNewValue().equals(r.getOldValue()))
					unchanged.add(r);
			}

			l.removeAll(unchanged);

			//Move list to array
			ListHistoryComparisonsReturnItem[] ri = new ListHistoryComparisonsReturnItem[l.size()];

			for (int loop = 0; loop < l.size(); loop++)
			{
				ri[loop] = l.get(loop);
			}

			ret.setItem(ri);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	private String checkNull(final String value, final String message)
	{
		return isEmpty(value)?message:value;
	}

	private String checkNull(final short value, final String message)
	{
		return value==0?message:value + "";
	}

}
