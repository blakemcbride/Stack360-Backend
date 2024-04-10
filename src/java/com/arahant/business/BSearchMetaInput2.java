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

package com.arahant.business;

/*
 * Author: Blake McBride
 * Date: 7/28/22
 */

import java.util.ArrayList;

/**
 * This class is used to specify generic sort and selection criteria.
 *
 * This class is a simpler, cleaner, and more generic and powerful solution than the BSearchMetaInput class.
 */
public class BSearchMetaInput2 {
    private final ArrayList<Sort> sortList = new ArrayList<>();
    /**
     * selectionList is an OR collection of AND conditions.
     */
    private final ArrayList<ArrayList<Selection>> selectionList = new ArrayList<>();

    private boolean usingPaging;
    private int pagingFirstItemIndex;
    private int itemsPerPage = BProperty.getInt("Search Max");

    // Code dealing with selection

    public enum Operation {
        LessThan,
        LessThanOrEqualTo,
        EqualTo,
        GreaterThanOrEqualTo,
        GreaterThan,
        isNull,
        isNotNull,
        in,
        notIn,
        Like
    }

    public static class Selection {
        public String field;
        public Operation operator;
        public Object value;
        public ArrayList<Object> valueList;
    }

    public Selection addAndSelection(String field, Operation op, Object val) {
        Selection sel = new Selection();
        sel.field = field;
        sel.operator = op;
        sel.value = val;
        ArrayList<Selection> sl;
        if (selectionList.isEmpty()) {
            sl = new ArrayList<>();
            selectionList.add(sl);
        } else
            sl = selectionList.get(selectionList.size() - 1);
        sl.add(sel);
        return sel;
    }

    public void addOrSelection() {
        if (!selectionList.isEmpty())
            selectionList.add(new ArrayList<>());
    }

    public Selection addAndSelection(String field, Operation op) {
        return addAndSelection(field, op, null);
    }

    public void addValue(Selection sel, Object val) {
        if (sel.valueList == null)
            sel.valueList = new ArrayList<>();
        sel.valueList.add(val);
    }

    public int numberOfOrSelections() {
        return selectionList.size();
    }

    public int numberOfAndSelections(int i) {
        return selectionList.get(i).size();
    }

    public Selection getSelection(int i, int j) {
        return selectionList.get(i).get(j);
    }

    public int numberOfValues(Selection sel) {
        return sel.valueList.size();
    }

    public Object getValue(Selection sel, int i) {
        return sel.valueList.get(i);
    }

    //  Code dealing with sorting

    public enum SortDirection {
        Ascending,
        Descending
    }

    public static class Sort {
        public String fieldName;
        public SortDirection sortDirection;
    }

    public Sort addSort(String field, SortDirection dir) {
        Sort sort = new Sort();
        sort.fieldName = field;
        sort.sortDirection = dir;
        sortList.add(sort);
        return sort;
    }

    public int numberOfSorts() {
        return sortList.size();
    }

    public Sort getSort(int i) {
        return sortList.get(i);
    }

    //  Other functionality

    public void setUsingPaging(boolean usingPaging) {
        this.usingPaging = usingPaging;
    }

    public boolean isUsingPaging() {
        return usingPaging;
    }

    public void setPagingFirstItemIndex(int pagingFirstItemIndex) {
        this.pagingFirstItemIndex = pagingFirstItemIndex;
    }

    public int getPagingFirstItemIndex() {
        return pagingFirstItemIndex;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }
}


