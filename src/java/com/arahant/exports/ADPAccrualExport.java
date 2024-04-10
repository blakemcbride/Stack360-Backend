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


package com.arahant.exports;

import com.arahant.beans.OrgGroup;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRAccruedTimeOff;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BOrgGroup;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import java.io.File;
import java.util.*;

/**
 *
 */
public class ADPAccrualExport {

	public String build(int startDate, int endDate, String[] orgGroupID) throws Exception {
		File csvFile = new File(FileSystemUtils.getWorkingDirectory(), "ADP Accrual Export File " + DateUtils.now() + ".csv");
		DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

		try {
			//Writing title
			dfw.writeField("e-TIME");
			dfw.endRecord();

			//Writing column headers
			dfw.writeField("Co Code");
			dfw.writeField("Batch Id");
			dfw.writeField("File #");
			dfw.writeField("Pay #");
			dfw.writeField("Memo Code");
			dfw.writeField("Memo Amount");
			dfw.endRecord();

			Set<String> empSet = new HashSet<String>();
			for (String og : orgGroupID) {
				BOrgGroup bog = new BOrgGroup(og);

				empSet.addAll(bog.getAllPersonIdsForOrgGroupHierarchy(true));
			}
			Iterator<String> empIterator = empSet.iterator();

			Set<BEmployee> beSet = new HashSet<BEmployee>();
			while (empIterator.hasNext()) {
				String temp = empIterator.next();
				if (!temp.equals(""))
					beSet.add(new BEmployee(temp));
			}
			Iterator<BEmployee> beIterator = beSet.iterator();

			while (beIterator.hasNext()) {
				BEmployee be = new BEmployee(beIterator.next());
				if (BEmployee.isActive(ArahantSession.getHSU(), be.getPerson())) {
					List<BHRAccruedTimeOff> accVacHours = new ArrayList<BHRAccruedTimeOff>();
//                    List<BHRAccruedTimeOff> accSickHours = new ArrayList<BHRAccruedTimeOff>();
					List<BHRAccruedTimeOff> accPersonalHours = new ArrayList<BHRAccruedTimeOff>();
					BHRBenefitConfig vacTime = null;
					BHRBenefitConfig personalTime = null;
//                    BHRBenefitConfig sickTime = null;
					BHRBenefitConfig[] hrConfigs = be.getBenefitConfigs();

					for (BHRBenefitConfig bhr : hrConfigs)
						if (bhr.getTimeRelated())
							if (bhr.getName().toLowerCase().contains("vacation"))
								vacTime = bhr;
							else if (bhr.getName().toLowerCase().contains("personal") || bhr.getName().toLowerCase().contains("pto"))
								personalTime = bhr; //                            else if(bhr.getRuleName().toLowerCase().contains("sick"))
						//                                sickTime = bhr;

					if (be.getLastName().equals("Burchett"))
						be.getLastName();

					if (vacTime != null)
						accVacHours.addAll(Arrays.asList(be.listTimeOff(vacTime.getBenefitId(), startDate, endDate, (int) (vacTime.getMaxAmount() + 0.5))));
					if (personalTime != null)
						accPersonalHours.addAll(Arrays.asList(be.listTimeOff(personalTime.getBenefitId(), startDate, endDate, (int) (personalTime.getMaxAmount() + 0.5))));
//                    if(sickTime != null)
//                        accSickHours.addAll(Arrays.asList(be.listTimeOff(sickTime.getBenefitId(), startDate, endDate, (int)(sickTime.getMaxAmount() + 0.5))));

					if (!accVacHours.isEmpty()) {
						//Writing Co Code
						dfw.writeField(new BOrgGroup(getSuperGroup(be.getOrgGroupId())).getExternalId());

						//Writing Batch Id volumn values
						dfw.writeField(75);

						//Writing File #
						dfw.writeField(be.getExtRef());

						//Writing Pay # columm values
						dfw.writeField(1);

						//Writing Vacation Hours
						dfw.writeField("V");
						dfw.writeField(accVacHours.get(accVacHours.size() - 1).getRunningTotal());

						dfw.endRecord();
					}

					if (!accPersonalHours.isEmpty()) {
						//Writing Co Code
						dfw.writeField(new BOrgGroup(getSuperGroup(be.getOrgGroupId())).getExternalId());

						//Writing Batch Id volumn values
						dfw.writeField(75);

						//Writing File #
						dfw.writeField(be.getExtRef()); //Use be.getNameFML() for testing

						//Writing Pay # columm values
						dfw.writeField(1);

						//Writing Personal Hours
						dfw.writeField("P");
						dfw.writeField(accPersonalHours.get(accPersonalHours.size() - 1).getRunningTotal());

						dfw.endRecord();
					}

// Per client, no sick time to be reported
//                    if(!accSickHours.isEmpty()) {
//                        //Writing Co Code
//                        dfw.writeField(new BOrgGroup(getSuperGroup(be.getOrgGroupId())).getExternalId());
//
//                        //Writing Batch Id volumn values
//                        dfw.writeField(75);
//
//                        //Writing File #
//                        dfw.writeField(be.getExtRef()); //Use be.getNameFML() for testing
//
//                        //Writing Pay # columm values
//                        dfw.writeField(1);
//
//                        //Writing Sick Hours
//                        dfw.writeField("F");
//                        dfw.writeField(accSickHours.get(accSickHours.size() - 1).getRunningTotal());
//
//                        dfw.endRecord();
//                    }
				}
			}
		} finally {
			dfw.close();
		}

		return csvFile.getName();
	}

	private String getSuperGroup(String orgID) {
		BOrgGroup bog = new BOrgGroup(orgID);
		if (bog.getParent() != null)
			if (bog.getParent().getParent() != null)
				return getSuperGroup(bog.getParent().getOrgGroupId());
		return orgID;
	}

	public static void main(String args[]) {
		try {
			ArahantSession.getHSU().dontAIIntegrate();
			int startDate = 20110220;
			int endDate = 20110305;
			List<OrgGroup> orgGroups = ArahantSession.getHSU().createCriteria(OrgGroup.class).list();
			List<String> orgGroupIDs = new ArrayList<String>();

			for (OrgGroup og : orgGroups)
				orgGroupIDs.add(og.getOrgGroupId());
			String[] orgGroupID = new String[orgGroupIDs.size()];

			orgGroupID = orgGroupIDs.toArray(orgGroupID);

//            String[] orgGroupID = {
//									"00001-0000000016"
//									,
//									"00001-0000000118"
//									,
//									"00001-0000000034"
//									,
//									"00001-0000000072"
//									,
//									"00001-0000000031"
//								  };

			ADPAccrualExport adpAccrual = new ADPAccrualExport();
			adpAccrual.build(startDate, endDate, orgGroupID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
