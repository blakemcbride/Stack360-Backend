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
package com.arahant.services.standard.project.projectTimesheetReportClientLimited;

import java.util.*;

import com.arahant.beans.Project;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BProject;
import com.arahant.business.BProperty;
import com.arahant.utils.ArahantSession;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetProjectReportDataReturn extends TransmitReturnBase {

	public GetProjectReportDataReturn() {
		super();
	}

	/**
	 * @param message
	 */
	public GetProjectReportDataReturn(final String message) {
		super(message);
	}
	private double billableHours;
	private double billableAmount;
	private double nonBillableHours;
	private double nonBillableAmount;
	private double unknownHours;
	private double unknownAmount;
	private double estimatedHours;
	private double totalHours;
	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	ProjectReportDetail projectDetail[];
	
	public double getEstimatedHours() {
		return estimatedHours;
	}

	public void setEstimatedHours(double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public double getTotalHours() {
		return totalHours;
	}
	public void setTotalHours(final double totalHours) {
		this.totalHours = totalHours;
	}
	
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#getBillableHours()
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#getBillableHours()
	 */
	public double getBillableHours() {
		return billableHours;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#setBillableHours(double)
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#setBillableHours(double)
	 */
	public void setBillableHours(final double billableHours) {
		this.billableHours = billableHours;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#getBillableAmount()
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#getBillableAmount()
	 */
	public double getBillableAmount() {
		return billableAmount;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#setBillableAmount(double)
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#setBillableAmount(double)
	 */
	public void setBillableAmount(final double billeableAmount) {
		this.billableAmount = billeableAmount;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#getNonBillableAmount()
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#getNonBillableAmount()
	 */
	public double getNonBillableAmount() {
		return nonBillableAmount;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#setNonBillableAmount(double)
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#setNonBillableAmount(double)
	 */
	public void setNonBillableAmount(final double nonBillableAmount) {
		this.nonBillableAmount = nonBillableAmount;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#getNonBillableHours()
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#getNonBillableHours()
	 */
	public double getNonBillableHours() {
		return nonBillableHours;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#setNonBillableHours(double)
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#setNonBillableHours(double)
	 */
	public void setNonBillableHours(final double nonBillableHours) {
		this.nonBillableHours = nonBillableHours;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#getProjectDetail()
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#getProjectDetail()
	 */
	public ProjectReportDetail[] getProjectDetail() {
		return projectDetail;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#setProjectDetail(com.arahant.operations.transmit.ProjectReportDetail[])
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#setProjectDetail(com.arahant.services.projectTimesheetReport.ProjectReportDetail[])
	 */
	public void setProjectDetail(final ProjectReportDetail[] projectDetail) {
		List<ProjectReportDetail> billableList = new ArrayList<ProjectReportDetail>();
		for(int x = 0; x < projectDetail.length; x++)
		{
			if(projectDetail[x].getBillableHours() > 0)
			{
				billableList.add(projectDetail[x]);
			}
		}
		ProjectReportDetail[] arr = new ProjectReportDetail[billableList.size()];
		for(int y = 0; y < billableList.size(); y++)
		{
			arr[y] = billableList.get(y);
		}
		this.projectDetail = arr;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#getUnknownAmount()
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#getUnknownAmount()
	 */
	public double getUnknownAmount() {
		return unknownAmount;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#setUnknownAmount(double)
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#setUnknownAmount(double)
	 */
	public void setUnknownAmount(final double unknownAmount) {
		this.unknownAmount = unknownAmount;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#getUnknownHours()
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#getUnknownHours()
	 */
	public double getUnknownHours() {
		return unknownHours;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectReportTransmit#setUnknownHours(double)
	 */
	/* (non-Javadoc)
	 * @see com.arahant.services.projectTimesheetReport.IProjectReport#setUnknownHours(double)
	 */
	public void setUnknownHours(final double unknownHours) {
		this.unknownHours = unknownHours;
	}

	/**
	 * @param bt
	 */
	@SuppressWarnings("unchecked")
	void makeProjectReportDetails(final HibernateScrollUtil bt, String sortOn, boolean sortAsc, final int max) {
		final HashMap<Project, ProjectReportDetail> projectsMap = new HashMap<Project, ProjectReportDetail>();


		while (bt.next()) {
			final Object[] elems = bt.getObjects();

			//first element is hours
			final Double d = (Double) elems[0];

			//second is a project
			Project p = (Project) elems[1];

			//third is billable state
			final Character c = (Character) elems[2];

			if (p.getBillable() == 'I') {
				//need to find parent billable project
				BProject bp = new BProject(p);
				p = ArahantSession.getHSU().get(Project.class, bp.getPrimaryParentId());
			}

			ProjectReportDetail prd = projectsMap.get(p);

			if (prd == null) {
				prd = new ProjectReportDetail();
				projectsMap.put(p, prd);
				prd.setProjectName(p.getProjectName());
				prd.setProjectId(p.getProjectId());
				prd.setSummary(p.getDescription());
				prd.setEstimatedHours(p.getEstimateHours());
				prd.setInvoicedHours(new BProject(p).getInvoicedHours());
			}

			prd.setHours(d.doubleValue(), c.charValue(), p.getBillingRate());

		}

		bt.close();

		final List<ProjectReportDetail> projectsList = new LinkedList<ProjectReportDetail>();
		projectsList.addAll(projectsMap.values());

		Collections.sort(projectsList, new ProjectReportDetailComparator());

		int sizeToUse = projectsList.size();

		if (max != -1 && sizeToUse > max) {
			sizeToUse = max;
		}
		
		ProjectReportDetail prda[] = new ProjectReportDetail[sizeToUse];

		final Iterator<ProjectReportDetail> prdItr = projectsList.iterator();

		for (int loop = 0; loop < prda.length; loop++) {
			prda[loop] = prdItr.next();
			prda[loop].setBillableHours(roundToHundredths(prda[loop].getBillableHours()));
			prda[loop].setNonBillableHours(roundToHundredths(prda[loop].getNonBillableHours()));
			prda[loop].setUnknownHours(roundToHundredths(prda[loop].getUnknownHours()));

			if (prda[loop].getEstimatedHours() > 0) {
				prda[loop].setRemainingEstimatedHours(prda[loop].getEstimatedHours() - prda[loop].getInvoicedHours());
			}
			setEstimatedHours(getEstimatedHours() + prda[loop].getEstimatedHours());
			setBillableAmount(getBillableAmount() + prda[loop].getBillableAmount());
			setBillableHours(getBillableHours() + prda[loop].getBillableHours());
			setNonBillableAmount(getNonBillableAmount() + prda[loop].getNonBillableAmount());
			setNonBillableHours(getNonBillableHours() + prda[loop].getNonBillableHours());
			setUnknownAmount(getUnknownAmount() + prda[loop].getUnknownAmount());
			setUnknownHours(getUnknownHours() + prda[loop].getUnknownHours());
		}
		setTotalHours(getBillableHours() + getNonBillableHours() + getUnknownHours());
		
		List <ProjectReportDetail> prdal=new ArrayList(prda.length);
		Collections.addAll(prdal, prda);
		Collections.sort(prdal, new ProjectReportDetailComparator2(sortOn,sortAsc));
		prda=prdal.toArray(prda);
		
		setProjectDetail(prda);
	}

	private double roundToHundredths(final double x) {
		return (double) Math.round(x * 100) / 100;
	}

	private static class ProjectReportDetailComparator implements Comparator<ProjectReportDetail> {

		public ProjectReportDetailComparator() {
		}

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(final ProjectReportDetail arg0, final ProjectReportDetail arg1) {
			final ProjectReportDetail p1 = arg0;
			final ProjectReportDetail p2 = arg1;

			if (p1.getProjectName() == null) {
				if (p2.getProjectName() == null) {
					return 0;
				} else {
					return 1;
				}
			}
			return p1.getProjectName().compareTo(p2.getProjectName());
		}
	}

	private static class ProjectReportDetailComparator2 implements Comparator<ProjectReportDetail> {

		public ProjectReportDetailComparator2(String sortOn, boolean sortAsc) {
			this.sortOn = sortOn;
			this.sortAsc = sortAsc;
			if (sortOn == null || "".equals(sortOn.trim())) {
				sortOn = "projectName";
			}
		}
		private String sortOn;
		private boolean sortAsc;

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(final ProjectReportDetail arg0, final ProjectReportDetail arg1) {

			ProjectReportDetail p1 = arg0;
			ProjectReportDetail p2 = arg1;

			if (sortAsc) {
				p2 = arg0;
				p1 = arg1;
			}
			
			if ("projectName".equals(sortOn))
				return p1.getProjectName().compareTo(p2.getProjectName());
			if ("summary".equals(sortOn))
				return p1.getSummary().compareTo(p2.getSummary());
			if ("estimatedHours".equals(sortOn))
				return (int)(p1.getEstimatedHours()-p2.getEstimatedHours());
			if ("invoicedHours".equals(sortOn))
				return (int)(p1.getInvoicedHours()-p2.getInvoicedHours());
			if ("remainingEstimatedHours".equals(sortOn))
				return (int)(p1.getRemainingEstimatedHours()-p2.getRemainingEstimatedHours());
			if ("billableHours".equals(sortOn))
				return (int)(p1.getBillableHours()-p2.getBillableHours());
			if ("nonBillableHours".equals(sortOn))
				return (int)(p1.getNonBillableHours()-p2.getNonBillableHours());
			if ("unknownHours".equals(sortOn))
				return (int)(p1.getUnknownHours()-p2.getUnknownHours());
			if ("billableAmount".equals(sortOn))
				return (int)(p1.getBillableAmount()-p2.getBillableAmount());
			if ("nonBillableAmount".equals(sortOn))
				return (int)(p1.getNonBillableAmount()-p2.getNonBillableAmount());
			if ("unknownAmount".equals(sortOn))
				return (int)(p1.getUnknownAmount()-p2.getUnknownAmount());
			
			return p1.getProjectName().compareTo(p2.getProjectName());
		}
	}

	/**
	 * @return Returns the cap.
	 */
	public int getCap() {
		return cap;
	}

	/**
	 * @param cap The cap to set.
	 */
	public void setCap(final int cap) {
		this.cap = cap;
	}
}

	
