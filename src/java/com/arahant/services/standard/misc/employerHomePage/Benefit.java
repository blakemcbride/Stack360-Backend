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

package com.arahant.services.standard.misc.employerHomePage;

import com.arahant.beans.HrBenefit;
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.DateUtils;
import org.kissweb.StringUtils;


public class Benefit {

	public Benefit() {
	}

	public Benefit(BHRBenefitJoin hrbc) {
		name = hrbc.getBenefitConfig().getBenefitName() + " - " + hrbc.getBenefitConfigName();
		endDate = hrbc.getCoverageEndDate();
		if(endDate == 0)
		{			

            if (hrbc.getBenefitConfig() != null)
            {
                if (hrbc.getBenefitConfig().getBenefit() != null)
                {
                    HrBenefit benefit = hrbc.getBenefitConfig().getBenefit();
                    if(benefit.getCoverageEndType() == 1)
                    {
                        endDate = DateUtils.endOfMonth(DateUtils.now());
                    }
                    else if(benefit.getCoverageEndType() == 2)
                    {
                        endDate = DateUtils.now();
                    }
                    else if(benefit.getCoverageEndType() == 3)
                    {
                        endDate = DateUtils.addDays(DateUtils.now(), benefit.getCoverageEndPeriod());
                    }
                }
            }
			
		}
		id = hrbc.getBenefitJoinId();
	}

	private String name;
	private int endDate;
	private String id;

	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
