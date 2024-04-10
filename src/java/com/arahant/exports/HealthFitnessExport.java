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
 *
 */

package com.arahant.exports;

import com.arahant.beans.CompanyBase;
import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.beans.VendorCompany;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.utils.AddressUtils;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.Crypto;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FTP;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;

public class HealthFitnessExport {

	private HibernateSessionUtil hsu = ArahantSession.getHSU();

	public String build(String vendorId) throws Exception {

        File csvFile = new File(FileSystemUtils.getWorkingDirectory(), "drc.csv");
		//Each client file naming convention is [acctID].csv.pgp

        DelimitedFileWriter writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

		String street = "";
		String street2 = "";
		String city = "";
		String state = "";
		String zip = "";
		String homePhone = "";

		int i = 0;

        try {
            writer.writeField("UniqueID");
            writer.writeField("FirstName");
            writer.writeField("LastName");
			writer.writeField("Birthdate");
			writer.writeField("Gender");
			writer.writeField("Addr1");
			writer.writeField("Addr2");
			writer.writeField("City");
			writer.writeField("State");
			writer.writeField("Zip");
			writer.writeField("HmPh");
			writer.writeField("WkPh");
			writer.writeField("Email");
			writer.writeField("PrimPhys");
			writer.writeField("PhysPhone");
			writer.writeField("EmerContact");
			writer.writeField("EmerPhone");
			writer.writeField("CompanyName");
			writer.writeField("ParticipantType");
			writer.writeField("Location");
			writer.writeField("SSN");
			writer.endRecord();

			for (HrBenefit bene : hsu.createCriteria(HrBenefit.class).joinTo(HrBenefit.BENEFIT_PROVIDER)
				.eq(VendorCompany.ORGGROUPID, vendorId)
				.list())
			{

			 HibernateCriteriaUtil<Employee> hcu = hsu.createCriteria(Employee.class)
					.joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING)
					.dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now())
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
					.eq(HrBenefitConfig.HR_BENEFIT, bene);

				HibernateScrollUtil<Employee> scr = hcu.scroll();

				while (scr.next()) {

					BEmployee be = new BEmployee(scr.get());

					street = be.getStreet();
					street2 = be.getStreet2();
					city = be.getCity();
					if (be.getState().length() > 2)
						state = AddressUtils.getState(be.getState());
					else
						state = be.getState();
					zip = be.getZip();
					homePhone = checkPhone(be.getHomePhone());

					//Do Employee
					writer.writeField(scr.get().getUnencryptedSsn());
					writer.writeField(scr.get().getFname());
					writer.writeField(scr.get().getLname());
					//writer.writeField(DateUtils.getDateFormatted(be.getDob()));
					writer.writeField(DateUtils.getDateFormatted(be.getDob()));
					writer.writeField(scr.get().getSex() + "");
					writer.writeField(street);
					writer.writeField(street2);
					writer.writeField(city);
					writer.writeField(state);
					writer.writeField(zip);
					writer.writeField(homePhone);

					writer.writeField(checkPhone(be.getWorkPhone()));
					writer.writeField(be.getPersonalEmail());

					String phys = "";
					if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HRBENEFIT, bene).first() != null)
						phys = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HRBENEFIT, bene).first().getComments();
					writer.writeField(phys); //Primary Physican
					writer.writeField("");//Primary Phys Phone

					if (be.getMainEmerContact() == null)
					{
						writer.writeField("");
						writer.writeField("");
					}
					else
					{
						writer.writeField(be.getMainEmerContact().getName());  //EmerContact
						writer.writeField(be.getMainEmerContact().getHomePhone());  //EmerPhone
					}
					writer.writeField(be.getCompanyName());
					writer.writeField("Employee");  //ParticipantType
					writer.writeField("");  //Location
					writer.writeField(scr.get().getUnencryptedSsn());

					writer.endRecord();

					//Do Dependents
					for (BHREmplDependent ed : be.getDependents())
					{
						writer.writeField(ed.getSsn());
						writer.writeField(ed.getFirstName());
						writer.writeField(ed.getLastName());
						writer.writeField(DateUtils.getDateFormatted(ed.getDob()));
						writer.writeField(ed.getSex() + "");

						writer.writeField(street);
						writer.writeField(street2);
						writer.writeField(city);
						writer.writeField(state);
						writer.writeField(zip);
						writer.writeField(homePhone);

						writer.writeField(""); //workPhone
						writer.writeField(""); //email

						phys = "";
						if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HRBENEFIT, bene).first() != null)
							phys = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HRBENEFIT, bene).first().getComments();
						writer.writeField(phys); //Primary Physican
						writer.writeField("");//Primary Phys Phone
						writer.writeField(be.getNameFML());  //Employee name
						writer.writeField(checkPhone(be.getWorkPhone()));  //Employee workPhone
						writer.writeField(be.getCompanyName());
						//ParticipantType
						writer.writeField(ed.getRelationshipType().equals("S")?"Spouse":(ed.getRelationshipType().equals("C")?"Child":"Other"));

						writer.writeField("");  //Location
						writer.writeField(ed.getSsn());

						writer.endRecord();
						System.out.println(i);
						i++;
					}
					
				}

			    scr.close();
				writer.endRecord();
			}

        } finally {
            writer.close();
        }

		//transmit
		encryptAndSend(csvFile, hsu.get(VendorCompany.class,vendorId));


		//File f = new File(DateUtils.now() + "-QTEelections-EmployerNickname");
		//csvFile.renameTo(f);
        return csvFile.getName();

    }

	public void encryptAndSend(File decryptedFile, CompanyBase co) throws Exception
	{

			//////////////////////////////////////////////////
			///Comment this to send file				 ////
			/////////////////////////////////////////////////
		//	if (!BProperty.getBoolean("EDIProduction"))
		//		return;


			//encrypt
			File encryptedFile=new File(decryptedFile.getAbsolutePath()+".pgp");



			Crypto.encryptPGP(decryptedFile, encryptedFile, co.getPublicEncryptionKey(), co.getEncryptionKeyId());


			//ftp
			new FTP().send(co.getComUrl(), co.getComPassword(), co.getComDirectory(), encryptedFile, true);



	}

	public static String checkPhone(String x)
	{
		x.trim();

		if (x.length() < 12)
			return "";

		//Get rid of extension if there is one
		if (x.length() > 12)
		{
			x = x.substring(0, 12);
			x.trim();
		}
		
		if (x.charAt(3) == '-' && x.charAt(7) == '-')
		{
			int i = 0;
			while (i < x.length())
			{
				if (isDigit(x.charAt(i)))
				{
					i++;
				}
				else
					return "";

				if (i == 3 || i == 7)
					i++;

			}
		}
		else
			return "";

		return x;
	}

	public static boolean isDigit(char c)
	{
		if (c >= '0' && c <= '9')
			return true;
		return false;
	}

	public static void main(String args[])
	{
		try
		{
			String vendorId = "00001-0000072505";
			//BPerson bp = new BPerson("00000-0000000004");
			//ArahantSession.getHSU().setCurrentPerson(bp.getPerson());
			HealthFitnessExport hfe = new HealthFitnessExport();
			hfe.build(vendorId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }

}
