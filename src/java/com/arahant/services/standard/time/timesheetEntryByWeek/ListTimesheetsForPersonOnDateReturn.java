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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.time.timesheetEntryByWeek;
import com.arahant.business.BTimesheet;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ListTimesheetsForPersonOnDateReturn extends TransmitReturnBase  {

	public ListTimesheetsForPersonOnDateReturn() {
		super();
	}

	private TimesheetTransmit timesheetTransmit[];
	
	private double totalHours;
	
	private double totalBillableHours;
	
	private int employeeFinalizedDate;
	
	private int mode;  //0 = normal; 1=rejections and currently read only; 2=rejections and on reject day
	
	private int remainingRejectedDays;
	

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#getTimesheetTransmit()
	 */
	public TimesheetTransmit[] getTimesheetTransmit() {
		return timesheetTransmit;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#setTimesheetTransmit(com.arahant.operations.transmit.ITimesheetTransmit[])
	 */
	public void setTimesheetTransmit(final TimesheetTransmit[] timesheetTransmit) {
		this.timesheetTransmit = timesheetTransmit;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#getTotalBillableHours()
	 */
	public double getTotalBillableHours() {
		return totalBillableHours;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#setTotalBillableHours(double)
	 */
	public void setTotalBillableHours(final double totalBillableHours) {
		this.totalBillableHours = totalBillableHours;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#getTotalHours()
	 */
	public double getTotalHours() {
		return totalHours;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#setTotalHours(double)
	 */
	public void setTotalHours(final double totalHours) {
		this.totalHours = totalHours;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#getEmployeeFinalizedDate()
	 */
	public int getEmployeeFinalizedDate() {
		return employeeFinalizedDate;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#setEmployeeFinalizedDate(int)
	 */
	public void setEmployeeFinalizedDate(final int employeeFinalizedDate) {
		this.employeeFinalizedDate = employeeFinalizedDate;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#getMode()
	 */
	public int getMode() {
		return mode;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#setMode(int)
	 */
	public void setMode(final int mode) {
		this.mode = mode;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#getRemainingRejectedDays()
	 */
	public int getRemainingRejectedDays() {
		return remainingRejectedDays;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#setRemainingRejectedDays(int)
	 */
	public void setRemainingRejectedDays(final int remainingRejectedDays) {
		this.remainingRejectedDays = remainingRejectedDays;
	}

	/**
	 * @param ts
	 */
	void setTimesheetTransmit(final BTimesheet[] ts) {
		timesheetTransmit=new TimesheetTransmit[ts.length];
		for (int loop=0;loop<ts.length;loop++)
			timesheetTransmit[loop]=new TimesheetTransmit(ts[loop]);
	}

}

	
