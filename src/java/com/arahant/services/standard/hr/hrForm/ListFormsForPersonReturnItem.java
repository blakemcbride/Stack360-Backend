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

package com.arahant.services.standard.hr.hrForm;

import com.arahant.business.BPersonForm;

public class ListFormsForPersonReturnItem {
    private String id;
    private int date;
    private String commentPreview;
    private String formTypeDescription;
    private String extension;
    private String filename;
    private String formTypeCode;
    private String internal;

    public ListFormsForPersonReturnItem() {
    }

    ListFormsForPersonReturnItem(final BPersonForm bc) {
        id = bc.getId();
        date = bc.getDate();
        commentPreview = bc.getCommentPreview();
        formTypeDescription = bc.getDescription();
        formTypeId = bc.getFormTypeId();
        formTypeCode = bc.getFormCode();
        extension = bc.getExtension();
        filename = bc.getSource();
        internal = bc.getFormType().getInternal() == 'Y' ? "Yes" : "";
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFormTypeCode() {
        return formTypeCode;
    }

    public void setFormTypeCode(String formTypeCode) {
        this.formTypeCode = formTypeCode;
    }

    public String getFormTypeId() {
        return formTypeId;
    }

    public void setFormTypeId(String formTypeId) {
        this.formTypeId = formTypeId;
    }

    private String formTypeId;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getCommentPreview() {
        return commentPreview;
    }

    public void setCommentPreview(final String comments) {
        this.commentPreview = comments;
    }

    /**
     * @return Returns the formDescription.
     */
    public String getFormTypeDescription() {
        return formTypeDescription;
    }

    /**
     * @param formDescription The formDescription to set.
     */
    public void setFormTypeDescription(final String formDescription) {
        this.formTypeDescription = formDescription;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getInternal() {
        return internal;
    }

    public void setInternal(String internal) {
        this.internal = internal;
    }
}

	
