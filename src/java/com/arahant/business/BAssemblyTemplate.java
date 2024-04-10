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

package com.arahant.business;

import com.arahant.beans.AssemblyTemplate;
import com.arahant.beans.AssemblyTemplateDetail;
import com.arahant.beans.ProductTypeChild;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

/**
 *
 */
public class BAssemblyTemplate extends SimpleBusinessObjectBase<AssemblyTemplate> {

	public static void delete(String []ids)
	{
		for (String id : ids)
			new BAssemblyTemplate(id).delete();
	}

	@Override
	public void delete()
	{
		for (BAssemblyTemplateDetail d : BAssemblyTemplateDetail.makeArray(ArahantSession.getHSU().createCriteria(AssemblyTemplateDetail.class)
				.eq(AssemblyTemplateDetail.TEMPLATE, bean)
				.list()))
			d.delete();
		super.delete();
	}
	public static BAssemblyTemplate [] listTopLevel()
	{
		return makeArray(ArahantSession.getHSU().createCriteria(AssemblyTemplate.class)
			.orderBy(ProductTypeChild.DESCRIPTION)
			.list());
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public String getName() {
		return bean.getAssemblyName();
	}


	public BAssemblyTemplate [] list()
	{
		return makeArray(ArahantSession.getHSU().createCriteria(AssemblyTemplate.class)
			.orderBy(ProductTypeChild.DESCRIPTION)
			.list());
	}

        public static BAssemblyTemplate [] list(final int cap)
	{
		return makeArray(ArahantSession.getHSU().createCriteria(AssemblyTemplate.class)
			.orderBy(ProductTypeChild.DESCRIPTION)
                        .setMaxResults(cap)
			.list());
	}

	public BAssemblyTemplate()
	{
		super();
	}

	public BAssemblyTemplate(String id)
	{
		super(id);
	}

	public BAssemblyTemplate(AssemblyTemplate o)
	{
		super();
		bean=o;
	}

	static BAssemblyTemplate[] makeArray(List<AssemblyTemplate> l)
	{
		BAssemblyTemplate[] ret=new BAssemblyTemplate[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BAssemblyTemplate(l.get(loop));
		return ret;
	}

	@Override
	public String create() throws ArahantException {
		bean=new AssemblyTemplate();
		return bean.generateId();
	}

	public String getId() {
		return bean.getAssemblyTemplateId();
	}



	@Override
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(AssemblyTemplate.class, key);
	}


	public BAssemblyTemplateDetail [] listChildren()
	{
		return BAssemblyTemplateDetail.makeArray(ArahantSession.getHSU().createCriteria(AssemblyTemplateDetail.class)
			.eq(AssemblyTemplateDetail.TEMPLATE, bean)
			.joinTo(AssemblyTemplateDetail.PRODUCT)
			.orderBy(ProductTypeChild.DESCRIPTION)
			.list());
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public void setName(String name) {
		bean.setAssemblyName(name);
	}
}
