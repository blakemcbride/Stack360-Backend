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
 * Created on Oct 18, 2007
 * 
 */
package com.arahant.services.utils;
import com.arahant.utils.DateUtils;
import java.util.ArrayList;



/**
 * 
 *
 * Created on Oct 18, 2007
 *
 */
public class CoverageValidator
{

	//////////////////////////////////////////////////////////////////////
	// Constants
	
	
	//////////////////////////////////////////////////////////////////////
	// Instance Vars
	private boolean benefitCoversEmployee;
	private boolean benefitCoversEmployeeSpouse;
	private boolean benefitCoversNonEmployeeSpouse;
	private boolean benefitCoversChildren;
	private boolean benefitCoversEmployeeSpouseOrChildren;
	private boolean benefitCoversNonEmployeeSpouseOrChildren;
	private int benefitMaxDependents;
	private boolean requestedEmployee;
	private boolean requestedSpouse;
	private int requestedChildren;
	private int requestedOther;
	private boolean foundActiveSpouse;
	private boolean foundActiveEmployeeSpouse;
	private int foundActiveChildren;
	private int foundActiveOther;
	private boolean useDatesInMessages;
	private ArrayList<String> errors=new ArrayList<String>(0);
	private ArrayList<String> warnings=new ArrayList<String>(0);
	

	
	public static class InputItem
	{

		public char relationship;
		public boolean active;
		public boolean employeeSpouse;
		public int endDate;
		public int startDate;
		
	}
	//////////////////////////////////////////////////////////////////////
	// Properties



	//////////////////////////////////////////////////////////////////////
	// Public Methods
	public void validate(InputItem []items)
	{
		this.evaluate(items);
		
		this.checkRequestedCovered(items);
	}


	//////////////////////////////////////////////////////////////////////
	// Event Handlers


	//////////////////////////////////////////////////////////////////////
	// Internal Methods
	private void evaluate(InputItem []items)
	{
		requestedEmployee = false;
		requestedSpouse = false;
		requestedChildren = 0;
		requestedOther = 0;
		foundActiveSpouse = false;
		foundActiveEmployeeSpouse = false;
		foundActiveChildren = 0;
		foundActiveOther = 0;
		
		this.evaluateEmployeeCoverage(items);
		this.evaluateSpouseCoverage(items);
		this.evaluateChildrenCoverage(items);
		this.evaluateOtherCoverage(items);
	}
	
	private void evaluateEmployeeCoverage(InputItem []items)
	{
		for (InputItem item: items)
		{
			if (item.relationship == 'E')
			{
				if (item.endDate==0 || item.endDate>=DateUtils.now())
					requestedEmployee = this.hasEnrollmentDates(item);
				break;
			}
		}
	}
	
	private void evaluateSpouseCoverage(InputItem []items)
	{
		for (InputItem item: items)
		{
			if (item.relationship == 'S')
			{
				// should only have one active spouse/employee spouse
				if (item.active)
				{
					foundActiveSpouse = !item.employeeSpouse;
					foundActiveEmployeeSpouse = item.employeeSpouse;
					if (item.endDate==0 || item.endDate>=DateUtils.now())
						requestedSpouse = this.hasEnrollmentDates(item);
				}
				break;
			}
		}
	}
	
	private void evaluateChildrenCoverage(InputItem []items)
	{
		for (InputItem item: items)
		{
			if (item.relationship == 'C')
			{
				if (item.active)
				{
					foundActiveChildren++;
					if (item.endDate==0 || item.endDate>=DateUtils.now())
						requestedChildren += this.hasEnrollmentDates(item) ? 1 : 0;
					
					// TODO - this should really be smarter with dates in that
					// 4 children may be enrolled, but if the dates don't overlap
					// it may actually only be 3 at an given time
				}
			}
		}
	}
	
	private void evaluateOtherCoverage(InputItem []items)
	{			
		for (InputItem item: items)
		{
			if (item.relationship == 'O')
			{
				if (item.active)
				{
					foundActiveOther++;
					if (item.endDate==0 || item.endDate>=DateUtils.now())
						requestedOther += this.hasEnrollmentDates(item) ? 1 : 0;
					
					// TODO - this should really be smarter with dates in that
					// 4 others may be enrolled, but if the dates don't overlap
					// it may actually only be 3 at an given time
				}
			}
		}
	}
	
	private boolean hasEnrollmentDates(InputItem item)
	{
		boolean enrollmentDates = false;
		
		if (item.startDate > 0 || item.endDate > 0) 
		{
			enrollmentDates = true;
		}
		
		return enrollmentDates;
	}
	
	private void checkRequestedCovered(InputItem []items)
	{
		warnings = new ArrayList<String>();
		errors =  new ArrayList<String>();
		
		this.checkEmployeeRequestedCovered(items);
		this.checkSpouseRequestedCovered(items);
		this.checkChildrenRequestedCovered(items);
		this.checkSpouseOrChildrenRequestedCovered(items);
		this.checkOtherRequestedCovered(items);
	}
	
	private void checkEmployeeRequestedCovered(InputItem []items)
	{	
		if (benefitCoversEmployee)
		{
			if (!requestedEmployee)
			{
				// employee covered, not enrolled
				warnings.add("Benefit covers Employee but the Employee is not being enrolled.  Continue anyway?");					
			}
		}
		else
		{
			if (requestedEmployee)
			{
				// employee not covered, enrolled
				errors.add("Benefit does not cover Employee but the Employee " + (useDatesInMessages ? "has enrollment dates." : "is being enrolled."));
			}
		}
	}
	
	private void checkSpouseRequestedCovered(InputItem []items)
	{
		if (benefitCoversNonEmployeeSpouse || benefitCoversEmployeeSpouse || benefitCoversNonEmployeeSpouseOrChildren || benefitCoversEmployeeSpouseOrChildren)
		{
			if (requestedSpouse)
			{
				if (foundActiveSpouse && !benefitCoversNonEmployeeSpouse && !benefitCoversNonEmployeeSpouseOrChildren)
				{
					// employee spouse covered, non-employee spouse being enrolled
					errors.add("Benefit covers Employee Spouse but the Spouse is not an Employee at the time of this policy start date.");
				}
				else if (foundActiveEmployeeSpouse && !benefitCoversEmployeeSpouse && !benefitCoversEmployeeSpouseOrChildren)
				{
					// non-employee spouse covered, employee spouse being enrolled
					errors.add("Benefit covers Non-Employee Spouse but the Spouse is an Employee at the time of this policy start date.");
				}
			}
			else if (foundActiveSpouse || foundActiveEmployeeSpouse)
			{
				// spouse covered, not enrolled
				warnings.add("Benefit covers Spouse but the Spouse " + (useDatesInMessages ? "has no enrollment dates." : "is being enrolled.") + "  Continue anyway?");
			}
		}
		else
		{
			if (requestedSpouse)
			{
				// spouse not covered, enrolled	
				errors.add("Benefit does not cover Spouse but the Spouse " + (useDatesInMessages ? "has enrollment dates." : "is being enrolled."));
			}
		}
	}
	
	private void checkChildrenRequestedCovered(InputItem []items)
	{	
		if (benefitCoversChildren)
		{
			if (foundActiveChildren == 0)
			{
				// selected a plan that covers children but no children found
				warnings.add("Benefit covers Children but no Children were found as Dependents.  Continue anyway?");
			}
			else if (benefitMaxDependents > 0)
			{
				if (requestedChildren > benefitMaxDependents)
				{
					// requested more children than covered
					errors.add("Benefit covers " + benefitMaxDependents + (benefitMaxDependents == 1 ? " Child" : " Children") + " but " + requestedChildren + " Children " + (useDatesInMessages ? "have enrollment dates." : "are being enrolled."));
				}
				else if (requestedChildren < benefitMaxDependents && requestedChildren < foundActiveChildren)
				{
					String message;
					
					message = "Benefit covers ";
					message += benefitMaxDependents;
					message += " ";
					message += (benefitMaxDependents == 1 ? "Child" : "Children");
					message += " but only ";
					message += requestedChildren;
					if (useDatesInMessages)
					{
						message += (requestedChildren == 1 ? " has enrollment dates" : " have enrollment dates");
					}
					else
					{
						message += (requestedChildren == 1 ? " is being enrolled" : " are being enrolled");
					}
					message += ".  Continue anyway?";
					
					// did not enroll all children and still have room
					warnings.add(message);
				}
			}
			else if (requestedChildren < foundActiveChildren)
			{
				String message;
				
				message = "Benefit covers any number of Children but only ";
				message += requestedChildren;
				message += " ";
				if (useDatesInMessages)
				{
					message += (requestedChildren == 1 ? " has enrollment dates" : " have enrollment dates");
				}
				else
				{
					message += (requestedChildren == 1 ? " is being enrolled" : " are being enrolled");
				}
				message += ".  Continue anyway?";
				
				// did not enroll all children and still have room
				warnings.add(message);
			}
		}
		else if (!(benefitCoversEmployeeSpouseOrChildren || benefitCoversNonEmployeeSpouseOrChildren))
		{
			if (requestedChildren != 0)
			{
				// children not covered
				errors.add("Benefit does not cover Children but Children " + (useDatesInMessages ? "have enrollment dates." : "are being enrolled."));
			}
		}
	}
	
	private void checkSpouseOrChildrenRequestedCovered(InputItem []items)
	{	
		if (benefitCoversNonEmployeeSpouseOrChildren || benefitCoversEmployeeSpouseOrChildren)
		{
			int foundTotal = ((foundActiveSpouse || foundActiveEmployeeSpouse) ? 1 : 0) + foundActiveChildren;
			int requestedTotal = (requestedSpouse ? 1 : 0) + requestedChildren;
	
			if (foundTotal == 0)
			{
				// selected a plan that covers dependents but no dependents found
				warnings.add("Benefit covers Spouse or Children but no Spouse or Children were found as Dependents.  Continue anyway?");
			}
			else if (benefitMaxDependents > 0)
			{
				if (requestedTotal > benefitMaxDependents)
				{
					// requested more dependents than covered
					errors.add("Benefit covers " + benefitMaxDependents + (benefitMaxDependents == 1 ? " Dependent" : " Dependents") + " but only " + requestedTotal + " Dependents " + (useDatesInMessages ? "have enrollment dates." : "are being enrolled."));
				}
				else if (requestedTotal < benefitMaxDependents && requestedTotal < foundTotal)
				{
					String message;
					
					message = "Benefit covers ";
					message += benefitMaxDependents;
					message += (benefitMaxDependents == 1 ? " Dependent but " : " Dependents but ");
					message += requestedTotal;
					if (useDatesInMessages)
					{
						message += (requestedTotal == 1 ? " has enrollment dates" : " have enrollment dates");
					}
					else
					{
						message += (requestedTotal == 1 ? " is being enrolled" : " are being enrolled");
					}
					message += ".  Continue anyway?";
					
					// did not enroll all dependents and still have room
					warnings.add(message);
				}
			}
			else if (requestedTotal < foundTotal)
			{
				String message;
				
				message = "Benefit covers any number of Dependents but only ";
				message += requestedTotal;
				message += " ";
				if (useDatesInMessages)
				{
					message += (requestedTotal == 1 ? " has enrollment dates" : " have enrollment dates");
				}
				else
				{
					message += (requestedTotal == 1 ? " is being enrolled" : " are being enrolled");
				}
				message += ".  Continue anyway?";

				// did not enroll all children and still have room
				warnings.add(message);
			}
		}
		// else covered above in spouse and children sections
	}
	
	private void checkOtherRequestedCovered(InputItem []items)
	{
		if (requestedOther > 0)
		{
			// requested other dependents
			errors.add("Only dependents with Relationship of Employee, Spouse, or Child may be enrolled.");
		}
	}


	/**
	 * @param benefitCoversChildren The benefitCoversChildren to set.
	 */
	public void setBenefitCoversChildren(boolean benefitCoversChildren) {
		this.benefitCoversChildren = benefitCoversChildren;
	}


	/**
	 * @param benefitCoversEmployee The benefitCoversEmployee to set.
	 */
	public void setBenefitCoversEmployee(boolean benefitCoversEmployee) {
		this.benefitCoversEmployee = benefitCoversEmployee;
	}


	/**
	 * @param benefitCoversEmployeeSpouse The benefitCoversEmployeeSpouse to set.
	 */
	public void setBenefitCoversEmployeeSpouse(boolean benefitCoversEmployeeSpouse) {
		this.benefitCoversEmployeeSpouse = benefitCoversEmployeeSpouse;
	}


	/**
	 * @param benefitCoversEmployeeSpouseOrChildren The benefitCoversEmployeeSpouseOrChildren to set.
	 */
	public void setBenefitCoversEmployeeSpouseOrChildren(
			boolean benefitCoversEmployeeSpouseOrChildren) {
		this.benefitCoversEmployeeSpouseOrChildren = benefitCoversEmployeeSpouseOrChildren;
	}


	/**
	 * @param benefitCoversNonEmployeeSpouse The benefitCoversNonEmployeeSpouse to set.
	 */
	public void setBenefitCoversNonEmployeeSpouse(
			boolean benefitCoversNonEmployeeSpouse) {
		this.benefitCoversNonEmployeeSpouse = benefitCoversNonEmployeeSpouse;
	}


	/**
	 * @param benefitCoversNonEmployeeSpouseOrChildren The benefitCoversNonEmployeeSpouseOrChildren to set.
	 */
	public void setBenefitCoversNonEmployeeSpouseOrChildren(
			boolean benefitCoversNonEmployeeSpouseOrChildren) {
		this.benefitCoversNonEmployeeSpouseOrChildren = benefitCoversNonEmployeeSpouseOrChildren;
	}


	/**
	 * @param benefitMaxDependents The benefitMaxDependents to set.
	 */
	public void setBenefitMaxDependents(int benefitMaxDependents) {
		this.benefitMaxDependents = benefitMaxDependents;
	}


	/**
	 * @param foundActiveChildren The foundActiveChildren to set.
	 */
	public void setFoundActiveChildren(int foundActiveChildren) {
		this.foundActiveChildren = foundActiveChildren;
	}


	/**
	 * @param foundActiveEmployeeSpouse The foundActiveEmployeeSpouse to set.
	 */
	public void setFoundActiveEmployeeSpouse(boolean foundActiveEmployeeSpouse) {
		this.foundActiveEmployeeSpouse = foundActiveEmployeeSpouse;
	}


	/**
	 * @param foundActiveOther The foundActiveOther to set.
	 */
	public void setFoundActiveOther(int foundActiveOther) {
		this.foundActiveOther = foundActiveOther;
	}


	/**
	 * @param foundActiveSpouse The foundActiveSpouse to set.
	 */
	public void setFoundActiveSpouse(boolean foundActiveSpouse) {
		this.foundActiveSpouse = foundActiveSpouse;
	}


	/**
	 * @param requestedChildren The requestedChildren to set.
	 */
	public void setRequestedChildren(int requestedChildren) {
		this.requestedChildren = requestedChildren;
	}


	/**
	 * @param requestedEmployee The requestedEmployee to set.
	 */
	public void setRequestedEmployee(boolean requestedEmployee) {
		this.requestedEmployee = requestedEmployee;
	}


	/**
	 * @param requestedOther The requestedOther to set.
	 */
	public void setRequestedOther(int requestedOther) {
		this.requestedOther = requestedOther;
	}

	/**
	 * @param requestedSpouse The requestedSpouse to set.
	 */
	public void setRequestedSpouse(boolean requestedSpouse) {
		this.requestedSpouse = requestedSpouse;
	}

	public void setUseDatesInMessages(boolean useDatesInMessages) {
		this.useDatesInMessages = useDatesInMessages;
	}

	public String[] getErrors() {
		if (errors==null)
			return new String[0];
		String []r=new String[errors.size()];
		return errors.toArray(r);
	}

	public String[] getWarnings() {
		if (warnings==null)
			return new String[0];
		String []r=new String[warnings.size()];
		return warnings.toArray(r);
	}

	public boolean hasProblems() {
		return errors.size()+warnings.size()>0;
	}
}
	
