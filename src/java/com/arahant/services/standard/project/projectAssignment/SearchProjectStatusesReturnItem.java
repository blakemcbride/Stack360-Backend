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

package com.arahant.services.standard.project.projectAssignment;

import com.arahant.business.BProject;
import com.arahant.business.BProjectStatus;
import com.arahant.business.BProperty;

public class SearchProjectStatusesReturnItem {

    private String code;
    private String description;
    private String id;
    private String statusEffect;
    private int statusType;
    private boolean autoProjectUnassign;

    public SearchProjectStatusesReturnItem() {
    }

    SearchProjectStatusesReturnItem(final BProjectStatus bc, BProject proj) {
        code = bc.getCode();
        description = bc.getDescription();
        id = bc.getProjectStatusId();
        statusType = bc.getActive();
        statusEffect = proj != null ? proj.getStatusEffect(bc) : "";
        autoProjectUnassign = BProperty.getBoolean("AutoProjectUnassign", false);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusEffect() {
        return statusEffect;
    }

    public void setStatusEffect(String statusEffect) {
        this.statusEffect = statusEffect;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public int getStatusType() {
        return statusType;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }

    public boolean isAutoProjectUnassign() {
        return autoProjectUnassign;
    }

    public void setAutoProjectUnassign(boolean autoProjectUnassign) {
        this.autoProjectUnassign = autoProjectUnassign;
    }
}

	
