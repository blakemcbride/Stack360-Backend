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

import com.arahant.beans.QuoteTemplate;
import com.arahant.beans.QuoteTemplateProduct;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BQuoteTemplate extends SimpleBusinessObjectBase<QuoteTemplate> {

	public BQuoteTemplate() {
	}

	public BQuoteTemplate(final QuoteTemplate bean) {
		this.bean = bean;
	}

	public BQuoteTemplate(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().update(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public String create() throws ArahantException {
		bean = new QuoteTemplate();
		bean.generateId();
		return getQuoteTemplateId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(QuoteTemplate.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public static void delete(final String quoteTemplateId) throws ArahantException {
		new BQuoteTemplate(quoteTemplateId).delete();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public String getQuoteTemplateId() {
		return bean.getQuoteTemplateId();
	}

	public String getTemplateDescription() {
		return bean.getTemplateDescription();
	}

	public String getTemplateName() {
		return bean.getTemplateName();
	}

	public void setTemplateDescription(String templateDescription) {
		bean.setTemplateDescription(templateDescription);
	}

	public void setTemplateName(String templateName) {
		bean.setTemplateName(templateName);
	}

	public static BQuoteTemplate[] list(int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(QuoteTemplate.class).setMaxResults(max).orderBy(QuoteTemplate.NAME).list());
	}

	private static BQuoteTemplate[] makeArray(List<QuoteTemplate> l) {
		final BQuoteTemplate[] ret = new BQuoteTemplate[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BQuoteTemplate(l.get(loop));
		return ret;
	}

	public List<QuoteTemplateProduct> getQuoteTemplateProducts() {
		return ArahantSession.getHSU().createCriteria(QuoteTemplateProduct.class).eq(QuoteTemplateProduct.QUOTE_TEMPLATE, bean).list();
	}

	public static BQuoteTemplate[] searchQuoteTemplates(final int max, final String name, final String description) {
		HibernateCriteriaUtil<QuoteTemplate> hcu = ArahantSession.getHSU().createCriteria(QuoteTemplate.class).setMaxResults(max).orderBy(QuoteTemplate.NAME);

		if (!isEmpty(name))
			hcu.like(QuoteTemplate.NAME, name);

		if (!isEmpty(description))
			hcu.like(QuoteTemplate.DESCRIPTION, description);

		return makeArray(hcu.list());
	}
}
