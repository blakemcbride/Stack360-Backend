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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.exports;

import com.arahant.beans.Address;
import com.arahant.business.BAgent;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import java.text.SimpleDateFormat;

/**
 *
 */
public class AllstateExport {


	String out="";

	public String tag(String tagName,String value)
	{
		return "<"+tagName+">"+value+"</"+tagName+">\n";
	}
	public String tag(String tagName,int value)
	{
		return "<"+tagName+">"+value+"</"+tagName+">\n";
	}

	public String tag(String tagName,String props, String value)
	{
		return "<"+tagName+" "+props+">"+value+"</"+tagName+">\n";
	}

	public String tagDate(String tagName, int date)
	{
		return tag(tagName,sdf.format(DateUtils.getDate(date)));
	}

	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

	public void export(BEmployee bemp, String allstateLogin, String allstatePassword, BAgent agent)
	{
		out="<TXLife xmlns:FIP=\"http://www.fiservinsurance.com/LPES/AHLB2B/Option1\" xmlns=\"http://ACORD.org/Standards/Life/2\" " +
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";

		out+=tag("UserAuthRequest",tag("UserLoginName",allstateLogin)+tag("UserPswd", tag("Pswd",allstatePassword)));

		String transactionGuid=""; //TODO
		String tc=""; //TODO
		String partyId=""; //TODO
		String partyId2=""; //TODO
		String partyTc="";//TODO
		String partyTc2="";//TODO
		String caseLinkKey=""; //TODO
		String firstDeductionDate=""; //TODO
		String requestedIssueDate=""; //TODO
		String fipLocationLinkKey=""; //TODO
		String locationEmployeeId=""; //TODO

		out+=tag("TXLifeRequest",
				tag("TransRefGUID",transactionGuid)+
				tag("TransType","tc=\"130\"","Enroll Employee")+
				tag("TransSubType","tc=\""+tc+"\"")+
				tag("OLifE",
					tag("Party","id=\""+partyId+"\"",
						tag("PartyTypeCode","tc=\""+partyTc+"\"","")+
						tag("Person",
							tag("FirstName",agent.getFirstName())+
							tag("MiddleName",agent.getMiddleName())+
							tag("LastName", agent.getLastName())+
							tag("Suffix","")
						)+
						tag("Producer","")
					)+
					tag("Party","id=\""+partyId2+"\"",
						tag("PartyTypeCode","tc=\""+partyTc2+"\"","")+
						tag("Organization",
							tag("OLifEExtension","VendorCode=\"Fiserv\"",
								tag("FIP:Case",
									tag("FIP:CaseLinkKey",caseLinkKey)+
									tag("FIP:FirstDeductionDate",firstDeductionDate)+
									tag("FIP:RequestedIssueDate", requestedIssueDate)+
									tag("FIP:DeductionsPerYear", bemp.getPayPeriodsPerYear())
								)+
								tag("FIP:Location",
									tag("FIP:LocationLinkKey",fipLocationLinkKey)+
									tag("FIP:LocationEE","EmployeeID=\""+locationEmployeeId+"\"","")
								)
							)
						)
					)+
					tag("Party","id=\""+locationEmployeeId+"\"",
						tag("PartyTypeCode","tc=\"1\"","")+
						tag("GovtID",bemp.getSsn())+
						tag("GovtIDTC","tc=\"1\"","SOCIAL SECURITY NUMBER")+
						getPerson(bemp)+
						getAddress(bemp.getHomeAddress())+
						getPhone(bemp.getHomePhone())+
						tag("EMailAddress",tag("AddrLine",bemp.getPersonalEmail()))+
						tag("Employment",
							tag("EmployeeID",bemp.getExtRef())+
							tagDate("HireDate",bemp.getHireDate())
						)
					)+
					getDependents(bemp)+
					getRelationships(bemp)


				)


			);

		out+="</TXLife>";
	}

	public String getPhone(String phone)
	{
		//remove anything not a number
		String x="";

		for (int loop=0;loop<phone.length();loop++)
			if (phone.charAt(loop)>='0' && phone.charAt(loop)<='9')
				x+=phone.charAt(loop);

		phone=x;

		String area="";
		String ext="";
		if (phone.length()>7)
		{
			area=phone.substring(0, 3);
			phone=phone.substring(3);
		}
		if (phone.length()>7)
		{
			ext=phone.substring(7);
			phone=phone.substring(0,7);
		}
		return tag("Phone", tag("AreaCode",area)+tag("DialNumber",phone)+tag("Ext",ext));
	}

	public String getStateCode(String state)
	{
            if (state.equalsIgnoreCase("Alabama"))
                return "1";
            if (state.equalsIgnoreCase("Alaska"))
                return "2";
            if (state.equalsIgnoreCase("Arizona"))
                return "4";
            if (state.equalsIgnoreCase("Arkansas"))
                return "5";
            if (state.equalsIgnoreCase("California"))
                return "6";
            if (state.equalsIgnoreCase("Colorado"))
                return "7";
            if (state.equalsIgnoreCase("Connecticut"))
                return "8";
            if (state.equalsIgnoreCase("Delaware"))
                return "9";
            if (state.equalsIgnoreCase("District of Columbia"))
                return "10";
            if (state.equalsIgnoreCase("Florida"))
                return "12";
            if (state.equalsIgnoreCase("Georgia"))
                return "13";
            if (state.equalsIgnoreCase("Hawaii"))
                return "15";
            if (state.equalsIgnoreCase("Idaho"))
                return "16";
            if (state.equalsIgnoreCase("Illinois"))
                return "17";
            if (state.equalsIgnoreCase("Indiana"))
                return "18";
            if (state.equalsIgnoreCase("Iowa"))
                return "19";
            if (state.equalsIgnoreCase("Kansas"))
                return "20";
            if (state.equalsIgnoreCase("Kentucky"))
                return "21";
            if (state.equalsIgnoreCase("Louisiana"))
                return "22";
            if (state.equalsIgnoreCase("Maine"))
                return "23";
            if (state.equalsIgnoreCase("Maryland"))
                return "25";
            if (state.equalsIgnoreCase("Massachusetts"))
                return "26";
            if (state.equalsIgnoreCase("Michigan"))
                return "27";
            if (state.equalsIgnoreCase("Minnesota"))
                return "28";
            if (state.equalsIgnoreCase("Mississippi"))
                return "29";
            if (state.equalsIgnoreCase("Missouri"))
                return "30";
            if (state.equalsIgnoreCase("Montana"))
                return "31";
            if (state.equalsIgnoreCase("Nebraska"))
                return "32";
            if (state.equalsIgnoreCase("Nevada"))
                return "33";
            if (state.equalsIgnoreCase("New Hampshire"))
                return "34";
            if (state.equalsIgnoreCase("New Jersey"))
                return "35";
            if (state.equalsIgnoreCase("New Mexico"))
                return "36";
            if (state.equalsIgnoreCase("New York"))
                return "37";
            if (state.equalsIgnoreCase("North Carolina"))
                return "38";
            if (state.equalsIgnoreCase("North Dakota"))
                return "39";
            if (state.equalsIgnoreCase("Ohio"))
                return "41";
            if (state.equalsIgnoreCase("Oklahoma"))
                return "42";
            if (state.equalsIgnoreCase("Oregon"))
                return "43";
            if (state.equalsIgnoreCase("Pennsylvania"))
                return "45";
            if (state.equalsIgnoreCase("Rhode Island"))
                return "47";
            if (state.equalsIgnoreCase("South Carolina"))
                return "48";
            if (state.equalsIgnoreCase("South Dakota"))
                return "49";
            if (state.equalsIgnoreCase("Tennessee"))
                return "50";
            if (state.equalsIgnoreCase("Texas"))
                return "51";
            if (state.equalsIgnoreCase("Utah"))
                return "52";
            if (state.equalsIgnoreCase("Vermont"))
                return "53";
            if (state.equalsIgnoreCase("Virginia"))
                return "55";
            if (state.equalsIgnoreCase("Washington"))
                return "56";
            if (state.equalsIgnoreCase("West Virginia"))
                return "57";
            if (state.equalsIgnoreCase("Wisconsin"))
                return "58";
            if (state.equalsIgnoreCase("Wyoming"))
                return "59";

            throw new ArahantException("State code not found - " + state);
	}
	public String getAddress(Address addr)
	{
		return tag("Address",
                       tag("Line1",addr.getStreet())+
                       tag("Line2",addr.getStreet2())+
                       tag("City",addr.getCity())+
                       tag("AddressState",addr.getState())+
                       tag("AddressStateTC","tc=\""+getStateCode(addr.getState())+"\"","")+
                       tag("Zip",addr.getZip()));
	}

	private String getDependents(BEmployee bemp) {
		String ret="";
		BHREmplDependent []deps=bemp.getDependents();
		for (int loop=0;loop<deps.length;loop++)
			ret+=getDependent(new BPerson(deps[loop].getPerson()), loop+1);

		return ret;
	}

	private String getPerson (BPerson bemp)
	{
		return	tag("Person",
                        tag("FirstName",bemp.getFirstName())+
                        tag("MiddleName",bemp.getMiddleName())+
                        tag("LastName",bemp.getLastName())+
                        tag("Prefix","")+
                        tag("Suffix","")+
		//TODO: handle for BPerson	tag("MarStat","tc=\"2\"", bemp.hasSpouse()?"Married":"Single")+
                        tag("Gender","tc=\""+(bemp.getSex().equals("M")?"1":"2")+"\"",bemp.getSex().equals("M")?"Male":"Female")+
                        tagDate("BirthDate",bemp.getDob())+
                        tag("DOBEstimated","tc=\"0\"","False")+
                        tag("Age",bemp.getAge())+
                        tag("Height",bemp.getHeight())+
                        tag("Weight",bemp.getWeight()));
	}
	private String getDependent(BPerson bp, int count)
	{
		return tag("Party","id=\"PR-"+count+"-\"",
                       tag("PartyTypeCode","tc=\"1\"","")+
                       tag("GovtID",bp.getSsn().equals("999-99-9999")?"":bp.getSsn())+
                       tag("GovtIDTC","tc=\"1\"","SOCIAL SECURITY NUMBER")+
                       getPerson(bp)+
                       getAddress(bp.getHomeAddress())+
                       getPhone(bp.getHomePhone()));

	}

	private String getRelationships(BEmployee bemp) {

		String ret="";
		BHREmplDependent []deps=bemp.getDependents();
		for (int loop=1;loop<=deps.length;loop++)
		{
			ret+=tag("Relation","id=\"REL"+loop+"\" OriginatingObjectID=\"EE-1-\" RelatedObjectID=\"PR-"+loop+"-\"",
                             tag("OriginatingObjectType","tc=\"6\"","")+
                             tag("RelatedObjectType","tc=\"6\"","")+
                             tag("RelationRoleCode","tc=\""+getRelationshipCode(deps[loop-1])+"\""));

		}
		return ret;
	}

	private int getRelationshipCode(BHREmplDependent dep)
	{
		/*
		 * 1 - Spouse 2 - Child 3 - Parent 4 - Sibling 94 - Step Child 92 - Grand Parent 93 - Grand Child 2147483647 - Other
		 */
		if (dep.getRelationshipType().equals("S"))
			return 1;

		if (dep.getRelationshipType().equals("C"))
			return 2;

		return 2147483647;
	}

        public static void main(String args[])
        {
            
        }

	/*
	 * <TXLife xmlns:FIP="http://www.fiservinsurance.com/LPES/AHLB2B/Option1" xmlns="http://ACORD.org/Standards/Life/2"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<UserAuthRequest>
<UserLoginName>JDoe</UserLoginName>
<UserPswd>
<Pswd>11111</Pswd>
</UserPswd>
</UserAuthRequest>
<TXLifeRequest>
<TransRefGUID>26768C61AB1C5345AED04E9A38C6BC3E</TransRefGUID>
<TransType tc="130">Enroll Employee</TransType>
<TransSubType tc="2147483647"/>
<OLifE>
<Party id="WA-1112B">
<PartyTypeCode tc="1"/>
<Person>
<FirstName>Bill</FirstName>
<MiddleName/>
<LastName>Agent</LastName>
<Suffix/>
</Person>
<Producer/>
</Party>
<Party id="ER-9695CFE6A4B07F4C92CA243F3F27A165">
<PartyTypeCode tc="2"/>
<Organization>
<OLifEExtension VendorCode="Fiserv">
<FIP:Case>
<FIP:CaseLinkKey>CS-0A768C61AB1C5345AED04E9A38C61467</FIP:CaseLinkKey>
<FIP:FirstDeductionDate>2005-01-01</FIP:FirstDeductionDate>
<FIP:RequestedIssueDate>2005-05-05</FIP:RequestedIssueDate>
<FIP:DeductionsPerYear>24</FIP:DeductionsPerYear>
</FIP:Case>
<FIP:Location>
<FIP:LocationLinkKey>LC-C88AB1DE5A19244789F616E10EB59F60</FIP:LocationLinkKey>
<FIP:LocationEE EmployeeID="EE-1-"/>
</FIP:Location>
</OLifEExtension>
</Organization>
</Party>
<Party id="EE-1-">
<PartyTypeCode tc="1"/>
<GovtID>111-00-0101</GovtID>
<GovtIDTC tc="1">SOCIAL SECURITY NUMBER</GovtIDTC>
<Person>
<FirstName>John</FirstName> <MiddleName/> <LastName>Doe</LastName> <Prefix/>
<Suffix/> <MarStat tc="2">Single</MarStat> <Gender tc="1">Male</Gender> <BirthDate>1967-01-01</BirthDate> <DOBEstimated tc="0">False</DOBEstimated> <Age>38</Age> <Height>56</Height> <Weight>135</Weight>
</Person> <Address>
<Line1>Some Street 1</Line1> <Line2>Street 2</Line2> <City>Arlington Heights</City> <AddressState>IL</AddressState> <AddressStateTC tc="17"/> <Zip>60040</Zip>
</Address> <Phone>
<AreaCode>233</AreaCode> <DialNumber>1110999</DialNumber> <Ext/>
</Phone> <EMailAddress>
<AddrLine>John@Doe.com</AddrLine> </EMailAddress> <Employment>
<EmployeeID/>
<HireDate>2001-02-02</HireDate> </Employment>
</Party> <Party id="PR-1-">
<PartyTypeCode tc="1"/> <GovtID>111-00-0091</GovtID> <GovtIDTC tc="1">SOCIAL SECURITY NUMBER</GovtIDTC> <Person>
<FirstName>Spouse</FirstName> <MiddleName/> <LastName>Doe</LastName> <Prefix/>
<Suffix/> <MarStat tc="1">Married</MarStat> <Gender tc="2">Female</Gender> <BirthDate>1967-01-01</BirthDate> <DOBEstimated tc="0">False</DOBEstimated> <Age>38</Age> <Height>56</Height> <Weight>140</Weight>
</Person> <Address>
<Line1>Line 1</Line1> <Line2/> <City/> <AddressState>IL</AddressState> <AddressStateTC tc="17"/> <Zip>10202</Zip>
</Address> <Phone>
<AreaCode/> <DialNumber/> <Ext/>
</Phone> <EMailAddress>
<AddrLine/> </EMailAddress>
</Party> <Party id="PR-2-">
<PartyTypeCode tc="1"/> <GovtID>223-44-0909</GovtID> <GovtIDTC tc="1">SOCIAL SECURITY NUMBER</GovtIDTC> <Person>
<FirstName>Child</FirstName> <MiddleName/> <LastName>Doe</LastName> <Prefix/>
<Suffix/> <MarStat tc="2">Single</MarStat> <Gender tc="2">Female</Gender> <BirthDate>2000-01-01</BirthDate> <DOBEstimated tc="0">False</DOBEstimated> <Age>5</Age> <Height>23</Height> <Weight>45</Weight>
</Person> <Address>
<Line1/> <Line2/> <City/> <AddressState>IL</AddressState> <AddressStateTC tc="17"/>
<Zip/> </Address>
<Phone> <AreaCode/>
<DialNumber/>
<Ext/> </Phone>
<EMailAddress> <AddrLine/>
</EMailAddress> </Party>
<Relation id="REL1" OriginatingObjectID="EE-1-" RelatedObjectID="PR-1-"> <OriginatingObjectType tc="6"/> <RelatedObjectType tc="6"/> <RelationRoleCode tc="1"/>
</Relation> <Relation id="REL2" OriginatingObjectID="EE-1-" RelatedObjectID="PR-2-">
<OriginatingObjectType tc="6"/> <RelatedObjectType tc="6"/> <RelationRoleCode tc="2"/>
</Relation> </OLifE>
</TXLifeRequest> </TXLife>
	 */
}
