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
package com.arahant.services.standard.at.applicantPosition;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 *  
 * 
 * Created on Feb 8, 2007
 *
 */
public class ListHrPositionsInput extends TransmitInputBase {

	@Validation(required=false)
	private boolean includeNew;
	@Validation(required=false)
	private boolean includeAccepting;
	@Validation(required=false)
	private boolean includeFilled;
	@Validation(required=false)
	private boolean includeSuspended;
	@Validation(required=false)
	private boolean includeCancelled;
	@Validation(type="date",required=false)
	private int acceptingFrom;
	@Validation(type="date",required=false)
	private int acceptingTo;
	@Validation(type="date",required=false)
	private int jobStartFrom;
	@Validation(type="date",required=false)
	private int jobStartTo;
	@Validation(required=false,min=0,max=16)
	private String orgGroupId;
	@Validation(required=false,min=0,max=16)
	private String jobTypeId;

	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}
	
	public int getAcceptingFrom() {
		return acceptingFrom;
	}

	public void setAcceptingFrom(int acceptingFrom) {
		this.acceptingFrom = acceptingFrom;
	}

	public int getAcceptingTo() {
		return acceptingTo;
	}

	public void setAcceptingTo(int acceptingTo) {
		this.acceptingTo = acceptingTo;
	}

	public boolean getIncludeCancelled() {
		return includeCancelled;
	}

	public void setIncludeCancelled(boolean includeCancelled) {
		this.includeCancelled = includeCancelled;
	}

	public boolean getIncludeFilled() {
		return includeFilled;
	}

	public void setIncludeFilled(boolean includeFilled) {
		this.includeFilled = includeFilled;
	}

	public boolean getIncludeNew() {
		return includeNew;
	}

	public void setIncludeNew(boolean includeNew) {
		this.includeNew = includeNew;
	}

	public boolean getIncludeSuspended() {
		return includeSuspended;
	}

	public void setIncludeSuspended(boolean includeSuspended) {
		this.includeSuspended = includeSuspended;
	}

	public boolean getIncludeAccepting() {
		return includeAccepting;
	}

	public void setIncludeAccepting(boolean includeAccepting) {
		this.includeAccepting = includeAccepting;
	}

	public int getJobStartFrom() {
		return jobStartFrom;
	}

	public void setJobStartFrom(int jobStartFrom) {
		this.jobStartFrom = jobStartFrom;
	}

	public int getJobStartTo() {
		return jobStartTo;
	}

	public void setJobStartTo(int jobStartTo) {
		this.jobStartTo = jobStartTo;
	}

	public String getJobTypeId() {
		return jobTypeId;
	}

	public void setJobTypeId(String jobTypeId) {
		this.jobTypeId = jobTypeId;
	}
	
	


}

	
