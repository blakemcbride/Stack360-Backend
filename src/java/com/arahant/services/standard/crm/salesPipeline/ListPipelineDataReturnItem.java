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

package com.arahant.services.standard.crm.salesPipeline;

import com.arahant.beans.Employee;
import com.arahant.beans.ProspectCompany;
import com.arahant.business.BProspectCompany;
import com.arahant.business.BProspectStatus;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


public class ListPipelineDataReturnItem {

	public ListPipelineDataReturnItem()
	{

	}

	ListPipelineDataReturnItem (BProspectStatus bc, Employee person)
	{
		name = makeVar(bc.getCode());

		List<ProspectCompany> l = ArahantSession.getHSU().createCriteria(ProspectCompany.class)
				.orderByDesc(ProspectCompany.LAST_CONTACT_DATE)
				.eq(ProspectCompany.PROSPECT_STATUS, bc.getBean())
				.eq(ProspectCompany.SALESPERSON, person)
				.list();

		item = new SalesPipelineProspects[l.size()];

		Calendar cal=Calendar.getInstance();

		Calendar now = Calendar.getInstance();
		now.setTime(new Date());

		List<ProspectCompany> p = new ArrayList<ProspectCompany>();

		int i = 0;
		for (ProspectCompany pc : l)
		{
			BProspectCompany psh = new BProspectCompany(pc);

			cal.setTime(psh.getStatusChangeDate());
			cal.add(Calendar.DAY_OF_YEAR, bc.getFallbackDays());

			if (now.before(cal) || bc.getFallbackDays() == 0)
			{
				p.add(pc);
			}
		}

		item = new SalesPipelineProspects[p.size()];

		for (ProspectCompany pc : p)
		{
			BProspectCompany bpc = new BProspectCompany(pc);
			item[i] = new SalesPipelineProspects(bpc, false);
			i++;
		}
	}

	ListPipelineDataReturnItem (BProspectStatus bc)
	{
		name = makeVar(bc.getCode());

		List<ProspectCompany> l = ArahantSession.getHSU().createCriteria(ProspectCompany.class)
				.orderByDesc(ProspectCompany.LAST_CONTACT_DATE)
				.eq(ProspectCompany.PROSPECT_STATUS, bc.getBean())
				.list();

		Calendar cal=Calendar.getInstance();

		Calendar now = Calendar.getInstance();
		now.setTime(new Date());

		List<ProspectCompany> p = new ArrayList<ProspectCompany>();

		int i = 0;
		for (ProspectCompany pc : l)
		{
			BProspectCompany psh = new BProspectCompany(pc);

			cal.setTime(psh.getStatusChangeDate());
			cal.add(Calendar.DAY_OF_YEAR, bc.getFallbackDays());

			if (now.before(cal) || bc.getFallbackDays() == 0)
			{
				p.add(pc);
			}
		}

		item = new SalesPipelineProspects[p.size()];

		for (ProspectCompany pc : p)
		{
			BProspectCompany bpc = new BProspectCompany(pc);
			item[i] = new SalesPipelineProspects(bpc, true);
			i++;
		}
	}

	ListPipelineDataReturnItem (BProspectStatus[] bc)
	{
		name = "fallback";

		List<ProspectCompany> p = new ArrayList<ProspectCompany>();

		Calendar cal=Calendar.getInstance();

		Calendar now = Calendar.getInstance();
		now.setTime(new Date());

		for (BProspectStatus bps : bc)
		{
			List<ProspectCompany> l = ArahantSession.getHSU().createCriteria(ProspectCompany.class)
					.orderByDesc(ProspectCompany.LAST_CONTACT_DATE)
					.eq(ProspectCompany.PROSPECT_STATUS, bps.getBean())
					.list();

			for(ProspectCompany pc : l)
			{
				BProspectCompany psh = new BProspectCompany(pc);

				cal.setTime(psh.getStatusChangeDate());
				cal.add(Calendar.DAY_OF_YEAR, bps.getFallbackDays());

				if (now.after(cal) && bps.getFallbackDays() != 0)
				{
					p.add(pc);
				}

			}
		}

		item = new SalesPipelineProspects[p.size()];

		int i = 0;
		for (ProspectCompany pc : p)
		{
			BProspectCompany bpc = new BProspectCompany(pc);
			item[i] = new SalesPipelineProspects(bpc, true);
			i++;
		}
	}

	ListPipelineDataReturnItem (BProspectStatus[] bc, Employee employee)
	{
		name = "fallback";

		List<ProspectCompany> p = new ArrayList<ProspectCompany>();

		Calendar cal=Calendar.getInstance();

		Calendar now = Calendar.getInstance();
		now.setTime(new Date());

		for (BProspectStatus bps : bc)
		{
			List<ProspectCompany> l = ArahantSession.getHSU().createCriteria(ProspectCompany.class)
					.orderByDesc(ProspectCompany.LAST_CONTACT_DATE)
					.eq(ProspectCompany.PROSPECT_STATUS, bps.getBean())
					.eq(ProspectCompany.SALESPERSON, employee)
					.list();

			for(ProspectCompany pc : l)
			{
				BProspectCompany psh = new BProspectCompany(pc);

				cal.setTime(psh.getStatusChangeDate());
				cal.add(Calendar.DAY_OF_YEAR, bps.getFallbackDays());

				if (now.after(cal) && bps.getFallbackDays() != 0)
				{
					p.add(pc);
				}

			}
		}

		item = new SalesPipelineProspects[p.size()];

		int i = 0;
		for (ProspectCompany pc : p)
		{
			BProspectCompany bpc = new BProspectCompany(pc);
			item[i] = new SalesPipelineProspects(bpc, false);
			i++;
		}
	}

	private String name;

	private SalesPipelineProspects[] item;

	public String makeVar(String x)
	{
		String var = "";
		StringTokenizer st = new StringTokenizer(x);
		String temp = "";
		char f;

		//do first word
		temp = st.nextToken();
		temp = temp.toLowerCase();
		var += temp;

		//do the rest
		while (st.hasMoreTokens()) {
			temp = st.nextToken();
			temp = temp.toUpperCase();
			f = temp.charAt(0);
			temp = temp.toLowerCase();
			temp = f + temp.substring(1, temp.length());
			var += temp;
		}

		return var;
	}

	public SalesPipelineProspects[] getItem() {
		return item;
	}

	public void setItem(SalesPipelineProspects[] item) {
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
