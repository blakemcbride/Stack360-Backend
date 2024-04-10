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

package com.arahant.imports;

import com.arahant.beans.BenefitClass;
import com.arahant.beans.BenefitConfigCost;
import com.arahant.beans.BenefitConfigCostAge;
import com.arahant.beans.CompanyDetail;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.business.BBenefitClass;
import com.arahant.business.BBenefitConfigCost;
import com.arahant.business.BBenefitConfigCostAge;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BWageType;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class CimplifyBenefitsImport {
	HibernateSessionUtil hsu = ArahantSession.getHSU();

	public void importBenefits(String classFilename, String healthFilename, String disabilityFilename, String lifeFilename) {

		try {
			DelimitedFileReader cfr = new DelimitedFileReader(classFilename);
			DelimitedFileReader hfr = new DelimitedFileReader(healthFilename);
			DelimitedFileReader dfr = new DelimitedFileReader(disabilityFilename);
			DelimitedFileReader lfr = new DelimitedFileReader(lifeFilename);
			List<HrBenefitConfig> beneConfigs = new ArrayList<HrBenefitConfig>();
			Set<HrBenefitConfig> configSet = new HashSet<HrBenefitConfig>();
			List<String> configNames = new ArrayList<String>();
			HrBenefitCategory beneCat = new HrBenefitCategory();
			HrBenefitConfig config = new HrBenefitConfig();
			HashMap<String, List<Integer>> employeeBenefitGroups = new HashMap<String, List<Integer>>();
			List<HrBenefit> allBenefits = new ArrayList<HrBenefit>();

			hsu.beginTransaction();

			//Read in Health Benefits
			while(hfr.nextLine()) {
				if(hfr.getString(1).equals("Employee Monthly Cost")) {
					String beneCategoryName = hfr.getString(0).split(" ")[0];
					BHRBenefitCategory currentBeneCat = new BHRBenefitCategory();
					currentBeneCat.create();
					beneCat = currentBeneCat.getBean();
					beneCat.setDescription(beneCategoryName);
					configNames.clear();

					hfr.nextLine();
					for(int i = 0; i < 4; i++)
						configNames.add(hfr.getString(i + 1));
					hfr.nextLine();
					hfr.nextLine();
					hsu.insert(beneCat);
					hsu.flush();
				}

				BHRBenefit bBene = new BHRBenefit();
				bBene.create();
				HrBenefit bene = bBene.getBean();
				List<BenefitConfigCost> costList = new ArrayList<BenefitConfigCost>();

				bene.setName(hfr.getString(0).split("\n")[0]);
				bene.setDependentMaxAge((short)25);
				bene.setDependentMaxAgeStudent((short)25);
				bene.setWageType(new BWageType("00001-0000000001").getBean());
				bene.setEligibilityType((short)2);
				bene.setEligibilityPeriod((short)30);
				bene.setCoverageEndPeriod((short)0);
				bene.setCoverageEndType((short)3);
				bene.setRuleName(beneCat.getDescription());

				for(int i = 0; i < 4; i++) {
					BHRBenefitConfig bConfig = new BHRBenefitConfig();
					BBenefitConfigCost bBeneCost = new BBenefitConfigCost();
					bBeneCost.create(true);
					BenefitConfigCost beneCost = bBeneCost.getBean();
					bConfig.create();
					beneConfigs.add(bConfig.getBean());
					beneConfigs.get(i).setName(bene.getName() + " " + configNames.get(i));

					if(!hfr.getString(i + 1).trim().isEmpty()) {
						beneConfigs.get(i).setEmployeeCost(12 * Float.valueOf(hfr.getString(i + 1).contains("$-") ? "0" : hfr.getString(i + 1).replace("$", "").replace(",", "").trim()));
						beneCost.setBaseAmount(Float.valueOf(hfr.getString(i + 1).contains("$-") ? "0" : hfr.getString(i + 1).replace("$", "").replace(",", "").trim()));
						beneCost.setConfig(beneConfigs.get(i));
						costList.add(beneCost);
					}
					else
						beneConfigs.get(i).setEmployeeCost((float)0.0);
					
					if(!hfr.getString(i + 5 + 1).trim().isEmpty())
						beneConfigs.get(i).setEmployerCost(12 * Float.valueOf(hfr.getString(i + 5 + 1).contains("$-") ? "0" : hfr.getString(i + 5 + 1).replace("$", "").replace(",", "").trim()));
					else
						beneConfigs.get(i).setEmployerCost((float)0.0);

					if(i == 0)
						beneConfigs.get(i).setEmployee('Y');
					if(i == 1) {
						beneConfigs.get(i).setEmployee('Y');
						beneConfigs.get(i).setSpouseEmployee('Y');
						beneConfigs.get(i).setSpouseNonEmployee('Y');
					}
					else if(i == 2) {
						beneConfigs.get(i).setEmployee('Y');
						beneConfigs.get(i).setChildren('Y');
					}
					else if(i == 3) {
						beneConfigs.get(i).setEmployee('Y');
						beneConfigs.get(i).setSpouseEmpOrChildren('Y');
						beneConfigs.get(i).setSpouseNonEmpOrChildren('Y');
					}

					beneConfigs.get(i).setHrBenefit(bene);
				}
				configSet.addAll(beneConfigs);
				bene.setBenefitConfigs(configSet);
				bene.setHrBenefitCategory(beneCat);
				hsu.insert(bene);
				hsu.insert(beneConfigs);
				hsu.insert(costList);
				hsu.commitTransaction();
				hsu.beginTransaction();
				allBenefits.add(bene);
				beneConfigs.clear();
				configSet.clear();
			}

			//Read in Disability Benefits
			while(dfr.nextLine()) {
				if(dfr.getString(0).trim().contains("Term Disability") && dfr.getString(6).isEmpty()) {
					String beneCategoryName = dfr.getString(0).split("\n")[0];
					BHRBenefitCategory currentBeneCat = new BHRBenefitCategory();
					currentBeneCat.create();
					beneCat = currentBeneCat.getBean();
					beneCat.setDescription(beneCategoryName.replaceAll("Long Term Disability", "LTD").replaceAll("Short Term Disability", "STD"));
					configNames.clear();

					dfr.nextLine();
					dfr.nextLine();
					dfr.nextLine();
					hsu.insert(beneCat);
					hsu.flush();
				}

				BBenefitConfigCost bBeneCost = new BBenefitConfigCost();
				bBeneCost.create(true);
				BenefitConfigCost beneCost = bBeneCost.getBean();
				BHRBenefit bBene = new BHRBenefit();
				bBene.create();
				HrBenefit bene = bBene.getBean();
				List<BenefitConfigCost> costList = new ArrayList<BenefitConfigCost>();

				bene.setName(dfr.getString(0).split("\n")[0]);
				bene.setWageType(new BWageType("00001-0000000001").getBean());
				bene.setEligibilityType((short)2);
				bene.setEligibilityPeriod((short)30);
				bene.setCoverageEndPeriod((short)0);
				bene.setCoverageEndType((short)3);
				bene.setRuleName(beneCat.getDescription());

				BHRBenefitConfig bConfig = new BHRBenefitConfig();
				bConfig.create();
				bConfig.setName(bene.getName().replaceAll("Long Term Disability", "LTD").replaceAll("Short Term Disability", "STD"));
				bConfig.getBean().setEmployee('Y');

				if(!dfr.getString(4).trim().isEmpty()) {
					bConfig.setEmployeeCost(Float.valueOf(dfr.getString(4).contains("$-") ? "0" : dfr.getString(4).replace("$", "").replace(",", "").trim()));
					beneCost.setBaseAmount(Float.valueOf(dfr.getString(4).contains("$-") ? "0" : dfr.getString(4).replace("$", "").replace(",", "").trim()));
					beneCost.setMultiplier(12 * 100);
					beneCost.setConfig(bConfig.getBean());
					costList.add(beneCost);
				}
				else
					bConfig.setEmployeeCost(0);
				
				if(!dfr.getString(5).trim().isEmpty())
					bConfig.setEmployerCost(12 * Float.valueOf(dfr.getString(5).contains("$-") ? "0" : dfr.getString(5).replace("$", "").replace(",", "").trim()));
				else
					bConfig.setEmployerCost(0);

				bConfig.getBean().setHrBenefit(bene);
				
				configSet.add(bConfig.getBean());
				bene.setBenefitConfigs(configSet);
				bene.setHrBenefitCategory(beneCat);
				hsu.insert(bene);
				hsu.insert(bConfig.getBean());
				hsu.insert(costList);
				hsu.commitTransaction();
				hsu.beginTransaction();
				allBenefits.add(bene);
				config = bConfig.getBean();
				beneConfigs.clear();
				configSet.clear();
			}

			List<BenefitConfigCost> costList = new ArrayList<BenefitConfigCost>();

			//Read in Life Benefits
			while(lfr.nextLine()) {
				BBenefitConfigCost bBeneCost = new BBenefitConfigCost();
				bBeneCost.create(true);
				BenefitConfigCost beneCost = bBeneCost.getBean();
				BHRBenefit bBene = new BHRBenefit();
				bBene.create();
				HrBenefit bene = bBene.getBean();

				if(lfr.getString(0).trim().contains("Life") && lfr.getString(8).isEmpty()) {
					String beneCategoryName = lfr.getString(0).split("\n")[0];
					BHRBenefitCategory currentBeneCat = new BHRBenefitCategory();
					currentBeneCat.create();
					beneCat = currentBeneCat.getBean();
					beneCat.setDescription(beneCategoryName);
					configNames.clear();

					lfr.nextLine();
					lfr.nextLine();
					hsu.insert(beneCat);
					hsu.flush();
				}
				else if(lfr.getString(1).toLowerCase().contains("costs per $1000 of coverage") && lfr.getString(2).toLowerCase().contains("employee cost per $1000")) {
					List<BenefitConfigCostAge> ageList = new ArrayList<BenefitConfigCostAge>();
					BenefitConfigCost cost = costList.get(costList.size() - 1); //new BBenefitConfigCost();
					costList.clear();
					cost.setBaseAmountSource('C');
					cost.setMultiplierSource('A');
					cost.setMultiplier(1000);

					for(int i = 0; i < 11; i++) {
						BBenefitConfigCostAge age = new BBenefitConfigCostAge();
						age.create();
						lfr.nextLine();
						if(Integer.valueOf(lfr.getString(1).replace("+", "").replace("'","").trim().substring(lfr.getString(1).replace("+", "").length() - 2)) == 75)
							age.setMaxAge(150);
						else
							age.setMaxAge(Integer.valueOf(lfr.getString(1).replace("+", "").replace("'","").trim().substring(lfr.getString(1).replace("+", "").length() - 2)));
						age.setMultiplier(Float.valueOf(lfr.getString(2).contains("$-") ? "0" : lfr.getString(2).replace("$", "").replace(",", "").trim()));
						age.getBean().setCost(cost/*.getBean()*/);
						ageList.add(age.getBean());
					}
					cost.setConfig(config);

					hsu.insert(cost);
					hsu.insert(ageList);
					hsu.commitTransaction();
					hsu.beginTransaction();
					continue;
				}
//				int x;
//				if(lfr.getString(0).contains("Voluntary Child"))
//					x = 0;
				bene.setName(lfr.getString(0).split("\n")[0]);
				bene.setWageType(new BWageType("00001-0000000001").getBean());
				bene.setEligibilityType((short)2);
				bene.setEligibilityPeriod((short)30);
				bene.setCoverageEndPeriod((short)0);
				bene.setCoverageEndType((short)3);
				bene.setRuleName(beneCat.getDescription());

				BHRBenefitConfig bConfig = new BHRBenefitConfig();
				bConfig.create();
				bConfig.setName(bene.getName());
				bConfig.getBean().setEmployee('Y');
				
				if(lfr.getString(5).trim().equals("see rate table"))
					;
				else if(!lfr.getString(5).trim().isEmpty()) {
					bConfig.setEmployeeCost(12 * Float.valueOf(lfr.getString(5).contains("$-") ? "0" : lfr.getString(5).replace("$", "").replace(",", "").trim()));
					beneCost.setBaseCapAmount(Float.valueOf(lfr.getString(5).contains("$-") ? "0" : lfr.getString(5).replace("$", "").replace(",", "").trim()));
					beneCost.setBaseAmountSource('S');
					beneCost.setConfig(bConfig.getBean());
				}
				else {
					bConfig.setEmployeeCost(0);
					beneCost.setBaseCapAmount(0);
					beneCost.setConfig(bConfig.getBean());
				}
				
				if(!lfr.getString(6).trim().isEmpty()) {
					bConfig.setEmployerCost(12 * Float.valueOf(lfr.getString(6).contains("$-") ? "0" : lfr.getString(6).replace("$", "").replace(",", "").trim()));
				}
				else
					bConfig.setEmployerCost((float)0.0);
				
				if(lfr.getString(0).trim().contains("Voluntary Life Insurance") && !lfr.getString(8).isEmpty() && !lfr.getString(7).equals("WAIVE")) {
					bene.setEmployeeChoosesAmount('Y');
					bene.setEmployeeIsProvider('Y');
					bConfig.setMinValue(5000);
					bConfig.setStepValue(5000);
					bConfig.setMaxAmount(500000);
					beneCost.setBaseCapAmount(500000 / 7);
					beneCost.setMultiplier(7);
					costList.add(beneCost);
					config = bConfig.getBean();
				}

				if(lfr.getString(0).trim().contains("Spouse") && !lfr.getString(8).isEmpty() && !lfr.getString(7).equals("WAIVE")) {
					bene.setEmployeeChoosesAmount('Y');
					bene.setEmployeeIsProvider('Y');
					bConfig.setMinValue(5000);
					bConfig.setStepValue(5000);
					bConfig.setMaxAmount(250000);
					beneCost.setBaseCapAmount(250000 / 7);
					beneCost.setMultiplier(7);
					bConfig.getBean().setEmployee('N');
					bConfig.getBean().setSpouseEmployee('Y');
					bConfig.getBean().setSpouseNonEmployee('Y');
					costList.add(beneCost);
					config = bConfig.getBean();
				}

				if(lfr.getString(0).trim().contains("Child") && !lfr.getString(8).isEmpty() && !lfr.getString(7).equals("WAIVE")) {
					bene.setEmployeeChoosesAmount('Y');
					bene.setEmployeeIsProvider('Y');
					bConfig.setMinValue(250);
					bConfig.setMaxAmount(10000);
					beneCost.setBaseAmountSource('B');
					beneCost.setBaseCapAmount(10000);
					beneCost.setMultiplier((float)3.5);
					bConfig.getBean().setEmployee('N');
					bConfig.getBean().setChildren('Y');
					costList.add(beneCost);
					config = bConfig.getBean();
				}

				if(lfr.getString(0).trim().contains("Waive") && lfr.getString(7).equals("WAIVE"))
					bConfig.setCoversEmployee(false);

				bConfig.getBean().setHrBenefit(bene);
				configSet.add(bConfig.getBean());
				bene.setBenefitConfigs(configSet);
				bene.setHrBenefitCategory(beneCat);
				hsu.insert(bene);
				hsu.insert(configSet);
//				hsu.insert(costList);
				hsu.commitTransaction();
				hsu.beginTransaction();
				allBenefits.add(bene);
				configSet.clear();
			}

			boolean plans = false;
			List<String> names = new ArrayList<String>();
			//Read in Benefit Classes
			while(cfr.nextLine()) {
				List<Integer> offerings = new ArrayList<Integer>();

				if(cfr.getString(0).startsWith("Corporate") || plans) {
					plans = true;
					BBenefitClass bBeneClass = new BBenefitClass();
					BenefitClass beneClass = new BenefitClass();
					bBeneClass.create();
					beneClass = bBeneClass.getBean();
					Set<HrBenefit> beneSet = new HashSet<HrBenefit>();
					
					if(cfr.getString(0).length() > 20)
						bBeneClass.setName(cfr.getString(0).substring(0, 19));
					else
						bBeneClass.setName(cfr.getString(0));

					for(int i = 0; i < cfr.size(); i++) {
						offerings.add(cfr.getString(i + 4).equals("X") ? 1 : 0);
					}
					for(int i = 0; i < offerings.size(); i++) {
						if(offerings.get(i) == 1) {
							String[] name = names.get(i).split(" ");
							for(HrBenefit h : allBenefits) {
								switch(i) {
									case 0: if(h.getName().contains(name[0]) && !h.getName().contains("EE") && !h.getName().contains("ER"))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 1: if(h.getName().contains(name[0]) && h.getName().contains(name[2]) && h.getName().contains(name[3]))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 2: if(h.getName().contains(name[0]) && h.getName().contains(name[2]) && h.getName().contains(name[3]))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 4: if(h.getName().contains(name[0]) && !h.getName().contains("EE") && !h.getName().contains("ER"))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 5: if(h.getName().contains(name[0]) && h.getName().contains(name[2]) && h.getName().contains(name[3]))
												bBeneClass.associateBenefit(h.getBenefitId()); break;
									case 6: if(h.getName().contains("Short Term Disability"))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 7: if(h.getName().equals("Long Term Disability"))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 8: if(h.getName().contains("Long Term Disability Administrators"))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 9: if(h.getName().contains("Long Term Disability") && h.getName().contains(name[2]) && h.getName().contains(name[3]))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 10:if(h.getName().contains("Long Term Disability") && h.getName().contains(name[2]) && h.getName().contains(name[3]))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 11:if(h.getName().contains("Class 2") && h.getName().contains("All Others"))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 12:if(h.getName().contains("Class 1"))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 13:if(h.getName().contains("Voluntary Life"))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 14:if(h.getName().contains("Spouse Life"))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
									case 15:if(h.getName().contains("Child Life"))
												bBeneClass.associateBenefit(h.getBenefitId()); break; //beneSet.add(h); break;
								}
							}
						}
					}

					if(beneClass.getName().contains("Jackson: Employee P"))
						for(HrBenefit h : allBenefits)
							if(h.getName().contains("Dental Plan 100% ER"))
								bBeneClass.associateBenefit(h.getBenefitId());
//					beneClass.setBenefits(beneSet);
					beneSet.clear();
					employeeBenefitGroups.put(cfr.getString(0), offerings);
					hsu.insert(beneClass);
					hsu.commitTransaction();
				}
				else
					names.add(cfr.getString(0));
			}

		} catch (IOException ex) {
            Logger.getLogger(CimplifyBenefitsImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CimplifyBenefitsImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) {
		ArahantSession.getHSU().setCurrentPersonToArahant();
		ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().get(CompanyDetail.class,"00000-0000000005"));
		new CimplifyBenefitsImport().importBenefits("/home/xichen/Desktop/Cimplify - Employee Status Groups.csv", 
													"/home/xichen/Desktop/Cimplify - Health Benefits.csv",
													"/home/xichen/Desktop/Cimplify - Disability Benefits.csv",
													"/home/xichen/Desktop/Cimplify - Life Benefits.csv");
	}
}
