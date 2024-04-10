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

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.business.BProperty;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.HibernateScrollUtil;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 *
 */
public class ExportPCPs {
	
	private static final ArahantLogger logger=new ArahantLogger(ExportPCPs.class);
	public String export(Date lastDate) throws IOException, Exception{
		String filename= new BProperty("LargeReportDir").getValue()+File.separator +"PCPList_"+DateUtils.now()+"_"+DateUtils.nowTime()+".csv";
		
		HibernateScrollUtil <HrBenefitJoin> scr=ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
			.notNull(HrBenefitJoin.COMMENTS)
			.gt(HrBenefitJoin.HISTORY_DATE, lastDate)
			.ne(HrBenefitJoin.COMMENTS, "")
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			
			.eq(HrBenefit.BENEFITID,"00001-0000000023")
			.scroll();
		
		DelimitedFileWriter fw=new DelimitedFileWriter(filename);
		
		fw.writeField("ssn");
		fw.writeField("pcp");
		fw.writeField("dob");
		fw.writeField("fname");
		fw.writeField("lname");
		fw.writeField("mname");
		fw.writeField("effdate");
		fw.endRecord();
			
		
		while (scr.next())
		{
			HrBenefitJoin bj=scr.get();
			if (bj.getComments().equals("Marriage"))
				continue;
			if (bj.getComments().equals("Birth"))
				continue;
			if (bj.getComments().equals("New Born"))
				continue;
			if (bj.getComments().indexOf("spousal")!=-1)
				continue;
			if (bj.getComments().indexOf("Spousal")!=-1)
				continue;
			if (bj.getComments().indexOf("coverage")!=-1)
				continue;
			if (bj.getComments().indexOf("prophet")!=-1)
				continue;
			if (bj.getComments().indexOf("student")!=-1)
				continue;
			if (bj.getComments().indexOf("disclaimer")!=-1)
				continue;
			if (bj.getComments().indexOf("boe ee eff")!=-1)
				continue;
			if (bj.getComments().indexOf("benefits")!=-1)
				continue;
			
			
			
			
			String ssn=bj.getCoveredPerson().getUnencryptedSsn();
			String pcp=bj.getComments();
			String dob=DateUtils.getDateFormatted(bj.getCoveredPerson().getDob());
			String fname=bj.getCoveredPerson().getFname();
			String lname=bj.getCoveredPerson().getLname();
			String mname=bj.getCoveredPerson().getMname();
			String effdate=DateUtils.getDateFormatted(bj.getCoverageStartDate());
			
			logger.info(bj.getComments());
			
			fw.writeField(ssn);
			fw.writeField(pcp);
			fw.writeField(dob);
			fw.writeField(fname);
			fw.writeField(lname);
			fw.writeField(mname);
			fw.writeField(effdate);
			fw.endRecord();
		}
		fw.close();
		scr.close();
		return filename;
	}
	
	
	public static void main (String args[]) throws IOException, Exception
	{
		ArahantSession.getHSU().setCurrentPersonToArahant();
		ExportPCPs x=new ExportPCPs();
		x.export(new Date());
	}

}
