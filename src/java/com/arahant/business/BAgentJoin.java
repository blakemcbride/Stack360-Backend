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

import com.arahant.beans.Agent;
import com.arahant.beans.AgentJoin;
import com.arahant.beans.CompanyDetail;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.Date;
import java.util.List;

public class BAgentJoin extends SimpleBusinessObjectBase<AgentJoin> {

    public BAgentJoin()
    {
    }

    public BAgentJoin(AgentJoin o)
    {
        bean=o;
    }

    public BAgentJoin(String key)
    {
        super(key);
    }

    @Override
    public String create() throws ArahantException {
        bean=new AgentJoin();
        return bean.generateId();
    }

    @Override
    public void load(String key) throws ArahantException {
        bean=ArahantSession.getHSU().get(AgentJoin.class,key);
    }

    public void setAgent(Agent agent) {
        bean.setAgent(agent);
    }

	public void setCompany(BCompany company) {
		bean.setCompany(company.getBean());
	}

    public void setCompanyId(String id) {
       bean.setCompany( ArahantSession.getHSU().get(CompanyDetail.class, id));
    }

	 public Agent getAgent() {
        return bean.getAgent();
    }

    public String getAgentJoinId() {
        return bean.getAgentJoinId();
    }

    public void setAgentJoinId(String agentJoinId) {
        bean.setAgentJoinId(agentJoinId);
    }

    public CompanyDetail getCompany() {
        return bean.getCompany();
    }

	public char getApproved() {
		return bean.getApproved();
	}

	public void setApproved(char approved) {
		bean.setApproved(approved);
	}

	public String getApprovedByPersonId() {
		return bean.getApprovedByPersonId();
	}

	public void setApprovedByPersonId(String approvedByPersonId) {
		bean.setApprovedByPersonId(approvedByPersonId);
	}

	public Date getApprovedDate() {
		return bean.getApprovedDate();
	}

	public void setApprovedDate(Date approvedDate) {
		bean.setApprovedDate(approvedDate);
	}

	public static BAgentJoin[] list(final String companyId)
	{
		return makeArray(ArahantSession.getHSU().createCriteria(AgentJoin.class).eq(AgentJoin.COMPANY, new BCompany(companyId).getBean()).list());
	}

	static BAgentJoin[] makeArray(final List<AgentJoin> plist) throws ArahantException {
        final BAgentJoin[] ret = new BAgentJoin[plist.size()];

        for (int loop = 0; loop < plist.size(); loop++) {
            ret[loop] = new BAgentJoin(plist.get(loop));
        }
        return ret;
    }
}
