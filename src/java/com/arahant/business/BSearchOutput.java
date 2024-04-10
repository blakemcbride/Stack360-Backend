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
 *  Created on Oct 30, 2008
 */

package com.arahant.business;

import com.arahant.utils.StandardProperty;
import com.arahant.services.standard.webservices.dynamicWebServices.DataObject;

public class BSearchOutput<T extends BusinessLogicBase> {
    private int sortType;
    private boolean sortAsc;
    private int itemCapForSmartChooser = BProperty.getInt(StandardProperty.COMBO_MAX) == 0 ? 20 : BProperty.getInt(StandardProperty.COMBO_MAX);
    private int pagingFirstItemIndex;
    private int totalItemsPaging;
    private int itemsPerPage;
    private T[] items;
    private String sortField;

    public BSearchOutput(T[] items, boolean paging) {
        setItems(items);
        setSortAsc(true);
        setSortType(1);
        if (paging) {
            setPagingFirstItemIndex(0);
            setTotalItemsPaging(1);
            setItemsPerPage(1);
        }
    }

    public BSearchOutput(BSearchMetaInput bSearchMetaInput) {
        setSortAsc(bSearchMetaInput.isSortAsc());
        if (bSearchMetaInput.getSortType() < 1 || bSearchMetaInput.getSortType() > bSearchMetaInput.getSortFieldCount())
            setSortType(1);
        else
            setSortType(bSearchMetaInput.getSortType());
        sortField = bSearchMetaInput.getSortField(sortType);
        if (bSearchMetaInput.isUsingPaging()) {
            setPagingFirstItemIndex(bSearchMetaInput.getPagingFirstItemIndex());
            setItemsPerPage(bSearchMetaInput.getItemsPerPage());
        }
    }

    public String getSortField() {
        return sortField;
    }

    public boolean isSortAsc() {
        return sortAsc;
    }

    public final void setSortAsc(boolean sortAsc) {
        this.sortAsc = sortAsc;
    }

    public int getSortType() {
        return sortType;
    }

    public final void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public int getItemCapForSmartChooser() {
        return itemCapForSmartChooser;
    }

    public void setItemCapForSmartChooser(int itemCapForSmartChooser) {
        this.itemCapForSmartChooser = itemCapForSmartChooser;
    }

    public int getPagingFirstItemIndex() {
        return pagingFirstItemIndex;
    }

    public final void setPagingFirstItemIndex(int pagingFirstItemIndex) {
        this.pagingFirstItemIndex = pagingFirstItemIndex;
    }

    public int getTotalItemsPaging() {
        return totalItemsPaging;
    }

    public final void setTotalItemsPaging(int totalItemsPaging) {
        this.totalItemsPaging = totalItemsPaging;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public final void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public T[] getItems() {
        return items;
    }

    public final void setItems(T[] items) {
        this.items = items;
    }

    public void setMetaSearchReturn(DataObject d) {
        DataObject meta = new DataObject();
        meta.put("sortField", getSortField());
        meta.put("sortAsc", isSortAsc());
        meta.put("itemCapForSmartChooser", getItemCapForSmartChooser());
        meta.put("firstItemIndexPaging", getPagingFirstItemIndex());
        meta.put("totalItemsPaging", getTotalItemsPaging());
        meta.put("itemsPerPage", getItemsPerPage());

        d.put("searchMeta", meta);
    }

}

	
