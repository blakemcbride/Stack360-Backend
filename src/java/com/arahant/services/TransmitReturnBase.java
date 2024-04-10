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
 *
 *  Created on Jun 16, 2006
 */

package com.arahant.services;

import com.arahant.business.BSearchOutput;
import com.arahant.business.BusinessLogicBase;
import com.arahant.services.standard.webservices.dynamicWebServices.DataObject;

public class TransmitReturnBase<T extends BusinessLogicBase> {
    private String wsMessage = "";
    private int wsStatus = 0;
    protected SearchMetaOutput searchMeta;
    private DataObject validations;

    public TransmitReturnBase() {
    }

    public TransmitReturnBase(String msg) {
        if (msg == null)
            msg = "";

        if (msg.equalsIgnoreCase("Success"))
            msg = ""; //success was messing Ryan up
        wsMessage = msg;
    }

    public String getWsMessage() {
        return wsMessage;
    }

    public void setWsMessage(final String msg) {
        wsMessage = msg;
    }

    /**
     * @return Returns the status.
     */
    public int getWsStatus() {
        return wsStatus;
    }

    public SearchMetaOutput getSearchMeta() {
        return searchMeta;
    }

    public void setSearchMeta(SearchMetaOutput searchMeta) {
        this.searchMeta = searchMeta;
    }

    protected void setStandard(BSearchOutput<T> bSearchOutput) {
        searchMeta = new SearchMetaOutput();

        searchMeta.setSortField(bSearchOutput.getSortField());

        searchMeta.setSortAsc(bSearchOutput.isSortAsc());
        searchMeta.setItemCapForSmartChooser(bSearchOutput.getItemCapForSmartChooser());
        searchMeta.setFirstItemIndexPaging(bSearchOutput.getPagingFirstItemIndex());
        searchMeta.setTotalItemsPaging(bSearchOutput.getTotalItemsPaging());
        searchMeta.setItemsPerPage(bSearchOutput.getItemsPerPage());
    }

    /**
     * @param status The status to set.
     */
    public void setWsStatus(final int status) {
        this.wsStatus = status;
    }

    protected boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static String preview(String str) {
        if (str == null)
            return "";
        str = str.replaceAll("\n", "  ");
        if (str.length() > 200)
            str = str.substring(0, 200);
        return str;
    }

    public DataObject getValidations() {
        return validations;
    }

    public void setValidations(DataObject validations) {
        this.validations = validations;
    }

}

	
