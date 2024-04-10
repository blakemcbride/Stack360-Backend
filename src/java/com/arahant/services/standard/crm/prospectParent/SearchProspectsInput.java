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
 */
package com.arahant.services.standard.crm.prospectParent;

import com.arahant.annotation.Validation;

import com.arahant.business.BSearchMetaInput;
import com.arahant.services.SearchMetaInput;
import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchProspectsInput extends TransmitInputBase {

    @Validation(table = "person", column = "fname", required = false)
    private String mainContactFirstName;
    @Validation(min = 2, max = 5, required = false)
    private int mainContactFirstNameSearchType;
    @Validation(required = false)
    private String identifier;
    @Validation(min = 2, max = 5, required = false)
    private int identifierSearchType;
    @Validation(table = "person", column = "lname", required = false)
    private String mainContactLastName;
    @Validation(min = 2, max = 5, required = false)
    private int mainContactLastNameSearchType;
    @Validation(required = false)
    private String name;
    @Validation(min = 2, max = 5, required = false)
    private int nameSearchType;
    @Validation(min = 1, max = 16, required = false)
    private String statusId;
    @Validation(required = false)
    private String sortOn;
    @Validation(required = false)
    private String sourceId;
    @Validation(min = 1, max = 16, required = false)
    private String typeId;
    @Validation(required = false)
    private String excludeId;
    @Validation(required = false)
    private boolean hasPhone;
    @Validation(required = false)
    private boolean hasEmail;
    @Validation(required = false)
    private SearchMetaInput searchMeta;
    @Validation(required = false)
    private String salesPersonId;
    @Validation(required = false)
    private boolean activesOnly;

    @Validation(required = false, type="date")
    private int firstContactDateAfter;
    @Validation(required = false, type="date")
    private int firstContactDateBefore;
    @Validation(required = false, type="date")
    private int lastContactDateAfter;
    @Validation(required = false, type="date")
    private int lastContactDateBefore;
    @Validation(required = false, type="date")
    private int lastLogDateAfter;
    @Validation(required = false, type="date")
    private int lastLogDateBefore;
    @Validation(required = false, type="date")
    private int statusDateAfter;
    @Validation(required = false, type="date")
    private int statusDateBefore;
	@Validation (table="address",column="time_zone_offset",required=false)
	private short timeZone;
    private String sort1;
    private boolean sortAsc1;
    private String sort2;
    private boolean sortAsc2;
    private int whenAddedDateAfter;
    private int whenAddedDateBefore;
    private int activityDateAfter;
    private int activityDateBefore;

	public short getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(short timeZone) {
		this.timeZone = timeZone;
	}

	public boolean getActivesOnly() {
		return activesOnly;
	}

	public void setActivesOnly(boolean activesOnly) {
		this.activesOnly = activesOnly;
	}

	public int getFirstContactDateAfter() {
		return firstContactDateAfter;
	}

	public void setFirstContactDateAfter(int firstContactDateAfter) {
		this.firstContactDateAfter = firstContactDateAfter;
	}

	public int getFirstContactDateBefore() {
		return firstContactDateBefore;
	}

	public void setFirstContactDateBefore(int firstContactDateBefore) {
		this.firstContactDateBefore = firstContactDateBefore;
	}

	public int getLastContactDateAfter() {
		return lastContactDateAfter;
	}

	public void setLastContactDateAfter(int lastContactDateAfter) {
		this.lastContactDateAfter = lastContactDateAfter;
	}

	public int getLastContactDateBefore() {
		return lastContactDateBefore;
	}

	public void setLastContactDateBefore(int lastContactDateBefore) {
		this.lastContactDateBefore = lastContactDateBefore;
	}

	public int getLastLogDateAfter() {
		return lastLogDateAfter;
	}

	public void setLastLogDateAfter(int lastLogDateAfter) {
		this.lastLogDateAfter = lastLogDateAfter;
	}

	public int getLastLogDateBefore() {
		return lastLogDateBefore;
	}

	public void setLastLogDateBefore(int lastLogDateBefore) {
		this.lastLogDateBefore = lastLogDateBefore;
	}

	public int getStatusDateAfter() {
		return statusDateAfter;
	}

	public void setStatusDateAfter(int statusDateAfter) {
		this.statusDateAfter = statusDateAfter;
	}

	public int getStatusDateBefore() {
		return statusDateBefore;
	}

	public void setStatusDateBefore(int statusDateBefore) {
		this.statusDateBefore = statusDateBefore;
	}

	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public boolean getHasEmail() {
		return hasEmail;
	}

	public void setHasEmail(boolean hasEmail) {
		this.hasEmail = hasEmail;
	}

    public String getExcludeId() {
        return excludeId;
    }

    public void setExcludeId(String excludeId) {
        this.excludeId = excludeId;
    }

    public String getMainContactFirstName() {
        return mainContactFirstName;
    }

    public void setMainContactFirstName(String mainContactFirstName) {
        this.mainContactFirstName = mainContactFirstName;
    }

    public int getMainContactFirstNameSearchType() {
        return mainContactFirstNameSearchType;
    }

    public void setMainContactFirstNameSearchType(int mainContactFirstNameSearchType) {
        this.mainContactFirstNameSearchType = mainContactFirstNameSearchType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getIdentifierSearchType() {
        return identifierSearchType;
    }

    public void setIdentifierSearchType(int identifierSearchType) {
        this.identifierSearchType = identifierSearchType;
    }

    public String getMainContactLastName() {
        return mainContactLastName;
    }

    public void setMainContactLastName(String mainContactLastName) {
        this.mainContactLastName = mainContactLastName;
    }

    public int getMainContactLastNameSearchType() {
        return mainContactLastNameSearchType;
    }

    public void setMainContactLastNameSearchType(int mainContactLastNameSearchType) {
        this.mainContactLastNameSearchType = mainContactLastNameSearchType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNameSearchType() {
        return nameSearchType;
    }

    public void setNameSearchType(int nameSearchType) {
        this.nameSearchType = nameSearchType;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getSortOn() {
        return sortOn;
    }

    public void setSortOn(String sortOn) {
        this.sortOn = sortOn;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public boolean getHasPhone() {
        return hasPhone;
    }

    public void setHasPhone(boolean hasPhone) {
        this.hasPhone = hasPhone;
    }

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

    public SearchMetaInput getSearchMeta() {
        return searchMeta;
    }

    public void setSearchMeta(SearchMetaInput searchMeta) {
        this.searchMeta = searchMeta;
    }

    BSearchMetaInput getSearchMetaInput() {
        if (searchMeta == null) {
            return new BSearchMetaInput(7, true, false, 0);
        } else {
            return new BSearchMetaInput(searchMeta, new String[]{"name", "identifier", "firstContactDate", "lastContactDate", "lastLogDate", "nextContactDate", "status", "statusDate", "source", "salesPerson", "certainty", "timeZone"});
            /*	int sortType = 0;

            // map our web service specific name (from return item) to the sort type used by query
            if (searchMeta.getSortField().equals("name")) {
            sortType = 1;
            } else if (searchMeta.getSortField().equals("statusCode")) {
            sortType = 2;
            } else if (searchMeta.getSortField().equals("sourceCode")) {
            sortType = 3;
            } else if (searchMeta.getSortField().equals("lastLogDate")) {
            sortType = 4;
            } else if (searchMeta.getSortField().equals("firstContactDate")) {
            sortType = 5;
            } else if (searchMeta.getSortField().equals("lastName")) {
            sortType = 6;
            } else if (searchMeta.getSortField().equals("firstName")) {
            sortType = 7;
            }

            return new BSearchMetaInput(sortType, searchMeta.isSortAsc(), searchMeta.isUsingPaging(), searchMeta.getFirstItemIndexPaging());
             */
        }
    }

    public String getSort1() {
        return sort1;
    }

    public void setSort1(String sort1) {
        this.sort1 = sort1;
    }

    public boolean isSortAsc1() {
        return sortAsc1;
    }

    public void setSortAsc1(boolean sortAsc1) {
        this.sortAsc1 = sortAsc1;
    }

    public String getSort2() {
        return sort2;
    }

    public void setSort2(String sort2) {
        this.sort2 = sort2;
    }

    public boolean isSortAsc2() {
        return sortAsc2;
    }

    public void setSortAsc2(boolean sortAsc2) {
        this.sortAsc2 = sortAsc2;
    }

    public int getWhenAddedDateAfter() {
        return whenAddedDateAfter;
    }

    public void setWhenAddedDateAfter(int whenAddedDateAfter) {
        this.whenAddedDateAfter = whenAddedDateAfter;
    }

    public int getWhenAddedDateBefore() {
        return whenAddedDateBefore;
    }

    public void setWhenAddedDateBefore(int whenAddedDateBefore) {
        this.whenAddedDateBefore = whenAddedDateBefore;
    }

    public int getActivityDateAfter() {
        return activityDateAfter;
    }

    public void setActivityDateAfter(int activityDateAfter) {
        this.activityDateAfter = activityDateAfter;
    }

    public int getActivityDateBefore() {
        return activityDateBefore;
    }

    public void setActivityDateBefore(int activityDateBefore) {
        this.activityDateBefore = activityDateBefore;
    }
}

	
