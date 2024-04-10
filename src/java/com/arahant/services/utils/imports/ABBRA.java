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


package com.arahant.services.utils.imports;


import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrWage;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.Person;
import com.arahant.beans.ProphetLogin;
import com.arahant.beans.WageType;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BHREmplStatusHistory;
import com.arahant.business.BHREmployeeStatus;
import com.arahant.business.BHRPosition;
import com.arahant.business.BHRWage;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.business.BWageType;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 *
 */
public class ABBRA {

	HibernateSessionUtil hsu;

	PreparedStatement codeTranslate = null;

	HashMap<String, String> empNoToPersonId = new HashMap<String, String>();
	
	HashSet<String> activeNewSsn = new HashSet<String>();


	protected void createEmployee(ResultSet rs) throws SQLException, Exception {

		BEmployee bemp=new BEmployee();
		bemp.create();

		bemp.setLastName(fixCase(rs.getString("p_lname")));
		bemp.setFirstName(fixCase(rs.getString("p_fname")));
		bemp.setMiddleName(fixCase(rs.getString("p_mi")));
		bemp.setJobTitle(fixCase(rs.getString("p_jobtitle")));


		String sex = rs.getString("p_sex");
		if (!sex.equals("M") && !sex.equals("F")) {
			sex = "U";
		}

		bemp.setSex(sex);

		bemp.setDob(DateUtils.getDate(rs.getDate("p_birth")));
		bemp.setSsn(rs.getString("p_ssn"));


		String eeoClass = rs.getString("p_eeoclass");

		if (eeoClass.length()>0)
			switch (eeoClass.charAt(0))
			{
				case 'A' : bemp.setEEOCategoryId("00000-0000000000");
							break;
				case 'B' : bemp.setEEOCategoryId("00000-0000000001");
				break;
				case 'C' : bemp.setEEOCategoryId("00000-0000000002");
				break;
				case 'D' : bemp.setEEOCategoryId("00000-0000000008");
				break;
				case 'E' : bemp.setEEOCategoryId("00000-0000000006");
				break;
				case 'F' : bemp.setEEOCategoryId("00000-0000000004");
				break;
				case 'G' : bemp.setEEOCategoryId("00000-0000000005");
				break;
				case 'H' : bemp.setEEOCategoryId("00000-0000000007");
				break;
			}




		String race = rs.getString("p_race");

		if (race.length()>0)
			switch (race.charAt(0))
			{
				case 'W' :  bemp.setEEORaceId("00000-0000000000");
					break;
				case 'B' :  bemp.setEEORaceId("00000-0000000001");
					break;
				case 'H' :  bemp.setEEORaceId("00000-0000000002");
					break;
				case 'A' :  bemp.setEEORaceId("00000-0000000003");
					break;
				case 'I' :  bemp.setEEORaceId("00000-0000000004");
					break;

			}

		String married = rs.getString("p_married");
		if (race.length()>0)
			switch (race.charAt(0))
			{
				case 'S' :  bemp.setW4Status('S');
					break;
				case 'M' :  bemp.setW4Status('M');
					break;
				case 'D' :  bemp.setW4Status('S');
					break;
				case 'W' :  bemp.setW4Status('S');
					break;
				case 'I' :  bemp.setW4Status('S');
					break;

			}


		
		String empNo = rs.getString("p_empno");
		bemp.setExtRef(empNo);

		empNoToPersonId.put(empNo, bemp.getPersonId());
		//TODO: make sure empNo is not empty

		String exempt = rs.getString("p_exempt");
		if (isEmpty(exempt)) {
			exempt = "N";
		}

		bemp.setExempt(!"Y".equals(exempt));


		int normWorked = rs.getInt("p_lnormu");
		bemp.setWorkHours(normWorked);

		if (married.equals("O")) {
			married = " ";
		}
		bemp.setMaritalStatus(married);

		bemp.setStreet(fixCase(rs.getString("p_hstreet1")));
		bemp.setStreet2(fixCase(rs.getString("p_hstreet2")));
		bemp.setCity(fixCase(rs.getString("p_hcity")));
		bemp.setState(fixCase(rs.getString("p_hstate")));
		bemp.setZip(fixCase(rs.getString("p_hzip")));

		

		String bPhone = rs.getString("p_busphone");
		bemp.setWorkPhone(bPhone);

                //System.out.println(bemp.getSsn());
		String user=bemp.getFirstName().charAt(0)+bemp.getLastName()+bemp.getSsn().substring(9);//(int)(Math.random()*10)+""+(int)(Math.random()*10);

		user=user.toLowerCase();

		while (ArahantSession.getHSU().createCriteria(ProphetLogin.class)
				.eq(ProphetLogin.USERLOGIN,user).exists())
			throw new ArahantException("DRAT! LOGIN COLLISION");

		bemp.setUserLogin(user);

		String password="";

		password=bemp.getSsn();


		bemp.setUserPassword(password,true);
		bemp.setSecurityGroupId("00000-0000000001");
		bemp.setScreenGroupId("00001-0000000158");


		bemp.insert();

		BHREmplStatusHistory hist=new BHREmplStatusHistory();
		hist.create();
                hist.setEmployeeId(bemp.getPersonId());
		//("insert into hr_empl_status_history (status_hist_id, employee_id, effective_date, status_id, notes)" +
		//			" values (?,?,?,?,?)");
		String active = rs.getString("p_active");

		String empStat = rs.getString("p_employ");
		String id = getStatusId(empStat);

		if (!active.equals("A")) {
			hist.setEffectiveDate(DateUtils.getDate(rs.getDate("p_termdate")));
			if (id == null) {
				id = "00001-0000000000";
			}
			hist.setStatusId(id);
			hist.setNotes(getReason(rs.getString("p_termreas")));

		} else {
			int edate = DateUtils.getDate(rs.getDate("p_effdate"));
			if (edate < 19000102) {
				edate = 19000102;
			}
			hist.setEffectiveDate(edate);

			hist.setStatusId(id);

			hist.setNotes(getReason(rs.getString("p_reason")));

		}
		
		hist.insert();

		assignOrgGroup(rs, bemp.getPersonId());
                saveWageHistory(rs.getString("p_jobtitle"), bemp.getPersonId(), rs.getFloat("p_unitrate"), rs.getDate("p_effdate"), rs.getString("p_reason"));



	}


        protected void createStatus(String code) throws Exception
        {
            String result=code;
            codeTranslate.setString(1, code);
            codeTranslate.setString(2, "MC");
            ResultSet codeRs = codeTranslate.executeQuery();
            if (codeRs.next()) {
                result = codeRs.getString(1);
            }
            codeRs.close();

            BHREmployeeStatus s=new BHREmployeeStatus();
            codes.put(code,s.create());
            s.setName(result);
            s.setActiveFlag('Y');
            s.setBenefitType('B');
            s.insert();
        }



        HashMap<String,String> codes=new HashMap<String, String>();
	protected String getStatusId(String code) throws Exception {

            if (codes.get(code)==null)
                createStatus(code);

            return codes.get(code);

	}

	protected String getReason(String code) throws SQLException {
		String result = code;
             //   System.out.println(code);
                
		codeTranslate.setString(1, code);
                codeTranslate.setString(2, "RE");
		ResultSet codeRs = codeTranslate.executeQuery();
		if (codeRs.next()) {
			result = codeRs.getString(1);
		}
		codeRs.close();
                 
		return fixCase(result);
	}

        protected String translate(String code, String table) throws SQLException {
		String result = code;
             //   System.out.println(code);

		codeTranslate.setString(1, code);
                codeTranslate.setString(2, table);
		ResultSet codeRs = codeTranslate.executeQuery();
		if (codeRs.next()) {
			result = codeRs.getString(1);
		}
		codeRs.close();

		return fixCase(result);
	}


	protected void assignOrgGroup(ResultSet rs, String personId) throws SQLException, Exception {
		//now I have to figure out where to put them in org group
		String dept = rs.getString("p_level1");
		String loc = rs.getString("p_level2");

		OrgGroup locOg=hsu.createCriteria(OrgGroup.class).eq(OrgGroup.NAME, loc).first();

		if (locOg==null)
		{
			BOrgGroup boloc=new BOrgGroup();
			boloc.create();
			boloc.setName(loc);
                        boloc.setOrgGroupType(ArahantConstants.COMPANY_TYPE);
			boloc.insert();



			OrgGroup og=hsu.createCriteria(OrgGroup.class)
				.eq(OrgGroup.NAME,dept)
				.first();
			if (og==null)
			{
				BOrgGroup borg=new BOrgGroup();
				borg.create();
				borg.setName(dept);
                                borg.setOrgGroupType(ArahantConstants.COMPANY_TYPE);
				borg.insert();
				borg.setParent(hsu.getCurrentCompany().getOrgGroupId());
				og=borg.getOrgGroup();
			}

			boloc.setParent(og.getOrgGroupId());

			locOg=boloc.getOrgGroup();
		}

		new BPerson(personId).assignToOrgGroup(locOg.getOrgGroupId(), false);
	}



	protected String getPersonId(String ssn) throws Exception {

		String pid=hsu.createCriteria(Person.class)
				.selectFields(Person.SSN)
				.eq(Person.SSN, ssn)
				.stringVal();

		if (!isEmpty(pid))
			return pid;

		return null;
	}

	protected boolean isEmpty(String x) {
		return (x == null || x.trim().equals(""));
	}

	protected void importHrPersnl(Connection con) throws SQLException, Exception {
		Statement stmt = con.createStatement();

		ResultSet rs = stmt.executeQuery("select * from hrpersnl");


		while (rs.next()) {

			String personId = getPersonId(rs.getString("p_ssn"));


			if (personId == null) {
				if (!isEmpty(rs.getString("p_ssn"))) {
					createEmployee(rs);
				}
				continue;
			}

			//TODO: may need to promote dependent to employee here

			//insert the current wage history
			
		}

		rs.close();
		stmt.close();
	}

	protected void saveWageHistory(String jobTitle, String personId, float unitRate, Date effDate, String reasonCode) throws SQLException {
		
		if (isEmpty(jobTitle)) {
			jobTitle = "Unknown";
		}
		jobTitle = fixCase(jobTitle);
		
		String positionId = getPositionId(jobTitle);

		int efDate = DateUtils.getDate(effDate);
		if (efDate < 19000102) {
			efDate = 19000102;
		}

     
                if (hsu.createCriteria(HrWage.class)
                        .eq(HrWage.EFFECTIVEDATE,efDate)
                        .joinTo(HrWage.EMPLOYEE)
                        .eq(Employee.PERSONID, personId)
                        .exists())
                    return;

                BHRWage wage=new BHRWage();
                wage.create();
                wage.setWageAmount(unitRate);
                wage.setWageTypeId(hourlyId);
                wage.setEmployeeId(personId);
                wage.setPositionId(positionId);
                wage.setNotes(getReason(reasonCode));
                wage.setEffectiveDate(efDate);
                wage.insert();



	}



	protected void saveSalaryHistory(String jobTitle, String personId, float unitRate, Date effDate, String reasonCode) throws SQLException {
		
		if (isEmpty(jobTitle)) {
			jobTitle = "Unknown";
		}
		jobTitle = fixCase(jobTitle);

		String positionId = getPositionId(jobTitle);

		int efDate = DateUtils.getDate(effDate);
		if (efDate < 19000102) {
			efDate = 19000102;
		}

		BHRWage wage=new BHRWage();
		wage.create();
		wage.setWageAmount(unitRate);
		wage.setWageId(hsu.createCriteria(WageType.class).eq(WageType.PERIOD_TYPE, WageType.PERIOD_SALARY).first().getWageTypeId());
		wage.setEmployeeId(personId);
		wage.setPositionId(positionId);
		wage.setNotes(getReason(reasonCode));
		wage.insert();


	}


	protected String getPositionId(String jobTitle)
	{
		String positionId = positionMap.get(jobTitle);

		if (positionId == null) {
			//create new position
			BHRPosition pos=new BHRPosition();
			positionId = pos.create();
			pos.setName(jobTitle);
			pos.insert();
			positionMap.put(jobTitle, positionId);
		//	System.out.println("Made new job title "+jobTitle);
		}

		return positionId;
	}
	
	protected void saveBonusHistory(String jobTitle, String personId, float unitRate, Date effDate, String reasonCode) throws SQLException {
		
		if (isEmpty(jobTitle)) {
			jobTitle = "Unknown";
		}
		jobTitle = fixCase(jobTitle);

		String positionId = getPositionId(jobTitle);

		int efDate = DateUtils.getDate(effDate);
		if (efDate < 19000102) {
			efDate = 19000102;
		}

		BHRWage wage=new BHRWage();
		wage.create();
		wage.setWageAmount(unitRate);
		wage.setWageId(hsu.createCriteria(WageType.class).eq(WageType.PERIOD_TYPE, WageType.PERIOD_ONE_TIME).first().getWageTypeId());
		wage.setEmployeeId(personId);
		wage.setPositionId(positionId);
		wage.setNotes(getReason(reasonCode));
		wage.insert();


	}
	
	
	HashMap<String, String> positionMap = new HashMap<String, String>();

	protected void importHJobHis(Connection con) throws SQLException {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from hjobhis");


		while (rs.next()) {
			//j_active - active flag A, T or ' '

			//j_annual - annual pay

			String jobTitle = rs.getString("j_jobtitle");
			

			float unitRate = rs.getFloat("j_unitrate");
			Date effDate = rs.getDate("j_effdate");
			String empNo = rs.getString("j_empno");

			String personId = empNoToPersonId.get(empNo);

			if (personId == null) {
				continue;
			}
			saveWageHistory(jobTitle, personId, unitRate, effDate, rs.getString("j_reason"));


		}

		rs.close();
		stmt.close();
	}

	public static String fixCase(String x) {
		StringTokenizer stok = new StringTokenizer(x, " ", true);
		String ret = "";

		while (stok.hasMoreTokens()) {
			String t = stok.nextToken();

			if (t.startsWith("MC") && t.length() > 2) {
				ret += "Mc" + t.substring(2, 3).toUpperCase() + t.substring(3).toLowerCase();
			} else if (t.length() == 1) {
				ret += t;
			} else {
				ret += t.substring(0, 1).toUpperCase() + t.substring(1).toLowerCase();
			}
		}

		//	System.out.println(ret),
		return ret.trim();
	}

	public static void main(String[] args) throws SQLException {

		Connection con = null;
		try {

			Properties props = new Properties();
			//props.load(ABBRA.class.getClassLoader().getResourceAsStream("AbbraConnection.props"));
                        props.setProperty("user","sa");
                        props.setProperty("password","");
                        props.setProperty("url", "jdbc:jtds:sqlserver://127.0.0.1:1433/crystal;namedPipe=true;instance=SQLEXPRESS");
                        props.setProperty("driver", "net.sourceforge.jtds.jdbc.Driver");

			System.out.println(props);

			Class.forName((String) props.get("driver"));

			con = DriverManager.getConnection((String) props.get("url"), props);
			con.setAutoCommit(false);

			ABBRA abbra = new ABBRA();

			HibernateSessionUtil hsu=ArahantSession.getHSU(false);
			hsu.beginTransaction();
			hsu.setCurrentPersonToArahant();
                        hsu.dontAIIntegrate();


			abbra.hsu=hsu;

                        ArahantSession.setFastKeys(true);
                        
                        abbra.makeWageTypes();


			abbra.codeTranslate = con.prepareStatement("select fdesc from hrtables where code=? and ftable=?");

                        abbra.importHBeplan(con);
                        abbra.loadEmployeeIds(con);
		//	abbra.importHrPersnl(con);
		//	abbra.importHJobHis(con);
                    //    abbra.importHDepend(con);
                        
                      //  abbra.importHBene(con);
                        


			hsu.commitTransaction();

                        abbra.codeTranslate.close();
			con.rollback();
			con.close();


		} catch (Exception ex) {
                    ex.printStackTrace();
			con.rollback();
			ArahantSession.getHSU().rollbackTransaction();
			
		}
	}

        private void loadEmployeeIds(Connection con) throws Exception
        {
            HibernateScrollUtil<Employee> scr=hsu.createCriteria(Employee.class)
                    .scroll();

            int count=0;

            long lastTime=new Date().getTime();
            while (scr.next())
            {
                if (++count%50==0)
                {
                    System.out.println(count);
              //      break;
                }

                String empNo=scr.get().getExtRef();
                empNoToPersonId.put(empNo, scr.get().getPersonId());
                importHDepend(con, scr.get().getExtRef());
                importHBene(con, empNo);

            }
            scr.close();
        }
        private String hourlyId;
    private void makeWageTypes() {
        BWageType wt=new BWageType();
        wt.create();
        wt.setName("Salary");
        wt.setPeriodType(WageType.PERIOD_SALARY);
        wt.setType(WageType.TYPE_REGULAR);
        wt.setIsDeduction(false);
        wt.insert();

        wt=new BWageType();
        hourlyId=wt.create();
        wt.setName("Hourly");
        wt.setPeriodType(WageType.PERIOD_HOURLY);
        wt.setType(WageType.TYPE_REGULAR);
        wt.setIsDeduction(false);
        wt.insert();

        wt=new BWageType();
        wt.create();
        wt.setName("Bonus");
        wt.setPeriodType(WageType.PERIOD_ONE_TIME);
        wt.setType(WageType.TYPE_REGULAR);
        wt.setIsDeduction(false);
        wt.insert();
    }

    private HashMap<String,String> depIdToPersonId=new HashMap<String, String>();
    private void importHDepend(Connection con, String empno) throws Exception {
        Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("select * from hdepend where d_empno="+empno);


        while (rs.next()) {


            String depid=rs.getString("d_depid");

            //they could be in there twice for some reason
            if (depIdToPersonId.containsKey(depid))
                continue;

            String empid=empNoToPersonId.get(rs.getString("d_empno"));
            if (isEmpty(empid))
            {
                System.out.println("Employee id is missing"+rs.getString("d_empno"));
                continue;
            }
            BHREmplDependent dep=new BHREmplDependent();
            dep.create();
            dep.setEmployeeId(empid);
            dep.setFirstName(fixCase(rs.getString("d_fname")));
            dep.setLastName(fixCase(rs.getString("d_lname")));
            dep.setMiddleName(fixCase(rs.getString("d_mi")));
            String sex=rs.getString("d_sex");

            if (sex.length()==0)
                sex="U";

            if (sex.charAt(0)!='M' && sex.charAt(0)!='F')
                sex="U";

            dep.setSex(sex);

            String ssn=rs.getString("d_ssno");
            if (isEmpty(ssn))
                ssn="999-99-9999";

            dep.setSsn(ssn);

            String relationship=rs.getString("d_relation");

            if (relationship.equals("CHILD"))
                dep.setRelationshipType("C");
            else
                if (relationship.equals("SPOUSE"))
                    dep.setRelationshipType("S");
                else
                {
                    dep.setRelationshipType("O");
                 
                    dep.setRelationship(translate(relationship,"DP"));
                }

            dep.setDob(DateUtils.getDate(rs.getDate("d_birth")));

            depIdToPersonId.put(rs.getString("d_depid"), dep.getPersonId());

            
            dep.insert();
 
            //hdepben contains dependent benefit assocs

            //hbene contains employee benefit assocs

            //hbeplan contains benefit setups

        }

        rs.close();
        stmt.close();
    }


    HashMap<String,String> benefitCodeToId=new HashMap<String,String>();
    HashMap<String,String> benefitCodeToConfigId=new HashMap<String,String>();
    private void importHBeplan(Connection con) throws Exception
    {
        importBenefitCategories(con);
         Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("select * from hbeplan where code<>'_ENT_'");

        PreparedStatement ps=con.prepareStatement("select distinct b_covertyp from hbene where b_benecode=?");

        while (rs.next()) {

            BHRBenefit b=new BHRBenefit();
            benefitCodeToId.put(rs.getString("code"), b.create());

            String catid=benefitCategoryMap.get(rs.getString("bt_type"));
            if (isEmpty(catid))
                catid=benefitCategoryMap.get("");
            b.setBenefitCategoryId(catid);
            b.setWageTypeId(benefitWageTypeId);
            b.setName(fixCase(rs.getString("fdesc")));
            b.setEndDate(DateUtils.getDate(rs.getDate("bt_expdate")));
            b.setStartDate(DateUtils.getDate(rs.getDate("bt_effdate")));
            b.insert();

            ps.setString(1, rs.getString("code"));

            ResultSet configRs=ps.executeQuery();

            while (configRs.next())
            {
                BHRBenefitConfig config=new BHRBenefitConfig();
                benefitCodeToConfigId.put((rs.getString("code")+" "+configRs.getString("b_covertyp")).trim(), config.create());
                config.setAutoAssign(rs.getString("bt_autoadd").equals("Y"));
                config.setBenefitId(b.getBenefitId());
                config.setName((b.getName()+" "+configRs.getString("b_covertyp")).trim());
                config.setStartDate(b.getStartDate());
                config.setEndDate(b.getEndDate());
                config.setCoversChildren(true);
                config.setCoversEmployee(true);
                config.setCoversEmployeeSpouseOrChildren(true);
                config.setSpouseEmployee(true);
                config.setSpouseNonEmpOrChildren(true);
                config.setSpouseNonEmployee(true);
                config.insert();
            }

            //hdepben contains dependent benefit assocs

            //hbene contains employee benefit assocs

        }

        ps.close();
        rs.close();
        stmt.close();
    }

    HashMap<String,String> benefitCategoryMap=new HashMap<String, String>();
    String benefitWageTypeId;
    private void importBenefitCategories(Connection con) throws Exception
    {
         Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("select code, fdesc from hrtables where ftable='BY' and code<>'_ENT_' and code<>''");


        while (rs.next()) {
        
            BHRBenefitCategory cat=new BHRBenefitCategory();
            benefitCategoryMap.put(rs.getString("code"), cat.create());
            cat.setAllowsMultipleBenefits(true);
            cat.setDescription(fixCase(rs.getString("fdesc")));
            cat.setRequiresDecline(false);
            cat.setTypeId(HrBenefitCategory.HEALTH);
            cat.insert();

        }

        rs.close();
        stmt.close();

        BHRBenefitCategory cat=new BHRBenefitCategory();
        benefitCategoryMap.put("", cat.create());
        cat.setAllowsMultipleBenefits(true);
        cat.setDescription("Misc");
        cat.setRequiresDecline(false);
        cat.setTypeId(HrBenefitCategory.MISC);
        cat.insert();

        BWageType wageType=new BWageType();
        benefitWageTypeId=wageType.create();
        wageType.setIsDeduction(true);
        wageType.setName("Benefits");
        wageType.setPeriodType(WageType.PERIOD_ONE_TIME);
        wageType.setType(WageType.TYPE_DEDUCTION);
        wageType.insert();
    }


    private void importHBene(Connection con, String empno) throws Exception
    {
        Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("select * from hbene where b_empno="+empno+" and id_col " +
                "not in (select b2.id_col from hbene b2, hbene b3 where b2.b_empno=b3.b_empno and " +
                "b2.b_benecode=b3.b_benecode and b2.id_col<b3.id_col and b2.b_empno="+empno+")");
        //"select * from hbene where b_benecode='MEDCHPLUS' and  b_empno=530003"

        PreparedStatement ps=con.prepareStatement("select * from hdepben where d_empno=? and d_benecode=? " +
                "and id_col "+
                "not in (select b2.id_col from hdepben b2, hdepben b3 where b2.d_empno=b3.d_empno and " +
                "b2.d_benecode=b3.d_benecode and b2.id_col<b3.id_col and b2.d_depid=b3.d_depid)");

        while (rs.next()) {

            try
            {
                BHRBenefitJoin bj=new BHRBenefitJoin();
                bj.create();
                bj.setBenefitConfigId(benefitCodeToConfigId.get((rs.getString("b_benecode")+" "+rs.getString("b_covertyp")).trim()));
                bj.setPayingPersonId(empNoToPersonId.get(rs.getString("b_empno")));
                bj.setCoveredPersonId(bj.getPayingPersonId());
                bj.setPolicyStartDate(DateUtils.getDate(rs.getDate("b_effdate")));
                bj.setCoverageStartDate(bj.getPolicyStartDate());
                bj.setPolicyEndDate(DateUtils.getDate(rs.getDate("b_expdate")));
                bj.setCoverageEndDate(bj.getPolicyEndDate());

                bj.setUseAmountOverride(false);
                if ("B".equals(rs.getString("b_period")))
                {
                    bj.setAmountPaid(rs.getDouble("b_epremium")*24);
                    bj.setAmountPaidType("F");
                    bj.setUseAmountOverride(true);
                }

                if ("M".equals(rs.getString("b_period")))
                {
                    bj.setAmountPaid(rs.getDouble("b_epremium")*12);
                    bj.setAmountPaidType("F");
                    bj.setUseAmountOverride(true);
                }

                bj.setBenefitApproved(true);

                bj.setAmountCovered(rs.getDouble("b_coverage"));

                bj.insert(true);

                //import benefits for dependents
                importHDepben(con,rs.getString("b_empno"),rs.getString("b_benecode"),bj, ps);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        ps.close();
        rs.close();
        stmt.close();
    }


    private void importHDepben(Connection con,String empno, String benefitCode, BHRBenefitJoin parentBJ, PreparedStatement ps) throws Exception
    {
        ps.setInt(1, Integer.parseInt(empno.trim()));
        ps.setString(2, benefitCode.trim());

       // String query="select * from hdepben where d_empno='"+empno.trim()+"' and d_benecode='"+benefitCode.trim()+"'";
        //con.prepareStatement(query);
        //System.out.println(query);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            try
            {
                String depid=depIdToPersonId.get(rs.getString("d_depid"));


                BHREmplDependent dep=new BHREmplDependent(parentBJ.getPayingPersonId(), depid);

                BHRBenefitJoin bj=new BHRBenefitJoin();
                bj.create();
                bj.setBenefitConfigId(parentBJ.getBenefitConfig().getBenefitConfigId());
                bj.setPayingPersonId(parentBJ.getPayingPersonId());
                bj.setCoveredPersonId(depid);
                bj.setPolicyStartDate(parentBJ.getPolicyStartDate());
                bj.setCoverageStartDate(DateUtils.getDate(rs.getDate("d_effdate")));
                bj.setPolicyEndDate(parentBJ.getPolicyEndDate());
                bj.setCoverageEndDate(DateUtils.getDate(rs.getDate("d_expdate")));
                bj.setRelationship(dep.getEmplDependent());
                bj.setBenefitApproved(true);
                bj.setUseAmountOverride(false);
                bj.setAmountCovered(rs.getDouble("d_coverage"));

                bj.insert(true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        rs.close();
    }
}
