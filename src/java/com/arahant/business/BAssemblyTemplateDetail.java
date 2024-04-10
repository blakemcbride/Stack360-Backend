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
import com.arahant.beans.Product;
import com.arahant.beans.ProductTypeChild;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

/**
 *
 */
public class BAssemblyTemplateDetail extends SimpleBusinessObjectBase<AssemblyTemplateDetail> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BAssemblyTemplateDetail(id).delete();
	}

	@Override
	public void delete()
	{
		for (BAssemblyTemplateDetail x : makeArray(ArahantSession.getHSU().createCriteria(AssemblyTemplateDetail.class)
				.eq(AssemblyTemplateDetail.PARENT, bean)
				.list()))
			x.delete();
		super.delete();
	}
	public BAssemblyTemplateDetail()
	{
		super();
	}

	public BAssemblyTemplateDetail(String key)
	{
		super(key);
	}

	public BAssemblyTemplateDetail(AssemblyTemplateDetail o)
	{
		super();
		bean=o;
	}



	@Override
	public String create() throws ArahantException {
		bean=new AssemblyTemplateDetail();
		return bean.generateId();
	}

	public String getId() {
		return bean.getAssemblyTemplateDetailId();
	}

	public String getItemParticulars() {
		return bean.getItemParticulars();
	}

	public String getProductDescription() {
		return bean.getProduct().getDescription();
	}

	public String getProductSku() {
		return bean.getProduct().getSku();
	}

	public int getQuantity() {
		return bean.getQuantity();
	}

	public boolean getTrackToItem() {
		return bean.getTrackToItem()=='Y';
	}

	@Override
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(AssemblyTemplateDetail.class, key);
	}


	public BAssemblyTemplateDetail [] listChildren()
	{
		return makeArray(ArahantSession.getHSU().createCriteria(AssemblyTemplateDetail.class)
			.eq(AssemblyTemplateDetail.PARENT, bean)
			.joinTo(AssemblyTemplateDetail.PRODUCT)
			.orderBy(ProductTypeChild.DESCRIPTION)
			.list());
	}

	static BAssemblyTemplateDetail[] makeArray(List<AssemblyTemplateDetail> l) {
		BAssemblyTemplateDetail ret[]=new BAssemblyTemplateDetail[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BAssemblyTemplateDetail(l.get(loop));
		return ret;
	}

	public void setDescription(String description) {
		bean.setItemParticulars(description);
	}

	public void setParentId(String parentId) {
		bean.setParentDetail(ArahantSession.getHSU().get(AssemblyTemplateDetail.class, parentId));
	}

	public void setProductId(String productId) {
		bean.setProduct(ArahantSession.getHSU().get(Product.class, productId));
	}

	public void setQuantity(int quantity) {
		bean.setQuantity(quantity);
	}

	public void setTemplateId(String templateId) {
		bean.setAssemblyTemplate(ArahantSession.getHSU().get(AssemblyTemplate.class, templateId));
	}

	public void setTrackToItem(boolean trackToItem) {
		bean.setTrackToItem(trackToItem?'Y':'N');
	}

    public String getProductId() {
        return bean.getProduct().getProductId();
    }


}
