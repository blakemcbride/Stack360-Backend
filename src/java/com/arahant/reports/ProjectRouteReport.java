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
 * Created on May 14, 2008
 * 
 * Arahant
 */
package com.arahant.reports;


import com.arahant.beans.OrgGroup;
import com.arahant.beans.ProjectStatus;
import com.arahant.beans.Route;
import com.arahant.beans.RoutePath;
import java.io.FileNotFoundException;

import com.arahant.beans.RouteStop;
import com.arahant.beans.RouteTypeAssoc;
import com.arahant.business.BProjectPhase;
import com.arahant.business.BRoute;
import com.arahant.business.BRouteStop;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;



/**
 * 
 *
 * Created on May 14, 2008
 *
 */
public class ProjectRouteReport extends ReportBase {
    protected boolean firstRouteStop = true;
	protected HashMap<String, String> routeStopAliases;
    
    public ProjectRouteReport() {
    	super("prjrte","Project Route Report");
    }

    public String build(String routeId) throws FileNotFoundException, DocumentException {
        try {
            BRoute bRoute = new BRoute(routeId);
			
			writeHeaderLine("Route", bRoute.getName());
            
            addHeaderLine();
			
            // get all route stops with the UBER SORTING ROUTINE OF DEATH VERSION 3.2 ALPHA applied
            ArrayList<String> routeStopIds = this.sortRouteStops(bRoute);
			
			// create route stop aliases for each route stop in order that they will appear
			this.routeStopAliases = new HashMap<String, String>();
			for (int idx = 0; idx < routeStopIds.size(); idx++) {
				this.routeStopAliases.put(routeStopIds.get(idx), "" + (idx + 1));
			}
            
			// write route defaults
			writeRoute(bRoute);
			
            // spin through route stop ids and write them out
            PdfPTable table = makeTable(new int[] {20, 80});
            for (String routeStopId : routeStopIds) {
                this.writeRouteStop(table, new BRouteStop(routeStopId));
            }
            addTable(table);
        } finally {
            close();
        }
        
        return getFilename();
    }
    
    /**
     * Route Stop Sorting spec:
     * 
     * General Note: Route Stops are grouped by Org Group, so show all route stops
     *               in an org group, then move on to another org group
     * 
     * General Strategy:
     * 
     * - Start with Org Group of Initial Route Stop, if there is an Initial Route Stop 
     *   (otherwise just start with first Org Group)
     * - Next show everything else in this Org Group
     * - Next show Org Group of first transition (first Route Stop that has a transition
     *   to a different Org Group)
     * - Repeat
     * 
     * Ordering Route Stops inside an Org Group:
     * 
     * - First show the Route Stop being transitioned to (if is one)
     * - Second show Route Stops that can be transitioned to from previous Org Group (if is one)
     * - Third show Route Stops with transitions back to previous Org Group (if is one)
     * - Fourth show Route Stops with transitions to same Org Group (loop back)
     * - Fifth show Route Stops without a transition
     * - Sixth show Route Stops that transition out to another Org Group
     * - Seventh show rest of Route Stops
     * 
     * 
     * @param bRoute
     * @return
     */
    protected ArrayList<String> sortRouteStops(BRoute bRoute) {
        ArrayList<String> allOrgGroupIds = this.getOrgGroupIdsUsedInRoute(bRoute);
        ArrayList<String> processedOrgGroupIds = new ArrayList<String>();
        String previousOrgGroupId = "none"; // can't use null as that is 'requesting company'
        String currentOrgGroupId = null;
        BRouteStop nextRouteStop = null;
        ArrayList<String> routeStopIds = new ArrayList<String>();
        
        // find the first org group id to kick us off
        BRouteStop initialRouteStop = bRoute.getRouteStop();
        if (initialRouteStop == null) {
            if (allOrgGroupIds.size() == 0) {
                // no route stops
                return routeStopIds;
            } else {
                // just grab first one we got
                currentOrgGroupId = allOrgGroupIds.get(0);
            }
        } else {
            currentOrgGroupId = this.cleanOrgGroupId(initialRouteStop.getOrgGroupId());
            nextRouteStop = initialRouteStop;
        }
        
        // process all org groups used across all route stops for this route
        while (processedOrgGroupIds.size() < allOrgGroupIds.size()) {
            // add the current org group id as processed
            processedOrgGroupIds.add(currentOrgGroupId);
            
            // add in all route stops that use the current org group, starting with the specified nextRouteStop
            nextRouteStop = this.addRouteStopIdsInOrgGroup(bRoute, processedOrgGroupIds, currentOrgGroupId, previousOrgGroupId, routeStopIds, nextRouteStop);
            
            previousOrgGroupId = currentOrgGroupId;
                
            // find next org group id to process
            if (nextRouteStop == null) {
                // just select next org group id that has not yet been processed
                for (String orgGroupId : allOrgGroupIds) {
                    if (!processedOrgGroupIds.contains(orgGroupId)) {
                        currentOrgGroupId = orgGroupId;
                        break;
                    }
                }
            } else {
                currentOrgGroupId = this.cleanOrgGroupId(nextRouteStop.getOrgGroupId());
            }
        }
        
        return routeStopIds;
    }
    
    protected ArrayList<String> getOrgGroupIdsUsedInRoute(BRoute bRoute) {
        Route route = hsu.get(Route.class, bRoute.getRouteId());
        java.util.List orgGroupIdList = hsu.createCriteria(RouteStop.class)
            .eq(RouteStop.ROUTE, route)
            .selectFields(RouteStop.ORG_GROUP_ID)
            .leftJoinTo(RouteStop.ORG_GROUP)
            .orderBy(OrgGroup.NAME)
            .list();
        String[] orgGroupIds = new String[orgGroupIdList.size()];
        ArrayList<String> cleanedOrgGroupIds = new ArrayList<String>();
        
        orgGroupIds = (String[])orgGroupIdList.toArray(orgGroupIds);
        
        // clean org group ids for uniqueness
        for (String orgGroupId : orgGroupIds) {
            if (!cleanedOrgGroupIds.contains(orgGroupId)) {
                cleanedOrgGroupIds.add(orgGroupId);
            }
        }
        
        return cleanedOrgGroupIds;
    }
    
    protected String cleanOrgGroupId(String orgGroupId) {
        return orgGroupId == null ? orgGroupId : (orgGroupId.equals("ReqOrg") ? null : orgGroupId);
    }
    
    protected BRouteStop addRouteStopIdsInOrgGroup(BRoute bRoute, ArrayList<String> processedOrgGroupIds, String currentOrgGroupId, String previousOrgGroupId, ArrayList<String> routeStopIds, BRouteStop nextRouteStop) {
        Route route = hsu.get(Route.class, bRoute.getRouteId());
        Boolean isPreviousOrgGroupId = previousOrgGroupId == null || !previousOrgGroupId.equals("none");
        ArrayList<String> routeStopIdsToAdd = new ArrayList<String>();
        java.util.List<RouteStop> tmpRouteStops;
        
        // first is route stop being transitioned to from previous org group
        if (nextRouteStop != null) {
            routeStopIdsToAdd.add(nextRouteStop.getRouteStopId());
        }

		OrgGroup current=hsu.get(OrgGroup.class, currentOrgGroupId);
		OrgGroup previous=hsu.get(OrgGroup.class, previousOrgGroupId);
        
        // second is other route stops in org group that can be transitioned to from previous org group
        if (isPreviousOrgGroupId) {
            tmpRouteStops = hsu.createCriteria(RouteStop.class)
                .eq(RouteStop.ROUTE, route)
                .eq(RouteStop.ORG_GROUP, current) // converts to isNull when null
                .joinTo(RouteStop.ROUTE_PATH_AS_TO)
                .joinTo(RoutePath.FROM_ROUTE_STOP)
                .eq(RouteStop.ORG_GROUP, previous) // converts to isNull when null
                .list();
            this.addRouteStopIdsIfNotPresent(routeStopIdsToAdd, tmpRouteStops);
        }

        // third is route stops in org group that can transition back to previous org group
        if (isPreviousOrgGroupId) { 
            tmpRouteStops = hsu.createCriteria(RouteStop.class)
                .eq(RouteStop.ROUTE, route)
                .eq(RouteStop.ORG_GROUP, current) // converts to isNull when null
                .joinTo(RouteStop.ROUTE_PATH_AS_FROM)
                .joinTo(RoutePath.TO_ROUTE_STOP)
                .eq(RouteStop.ORG_GROUP, previous) // converts to isNull when null
                .list();
            this.addRouteStopIdsIfNotPresent(routeStopIdsToAdd, tmpRouteStops);         
        }
        
        // fourth is route stops in org group that transition to same org group (loop back)
        tmpRouteStops = hsu.createCriteria(RouteStop.class)
            .eq(RouteStop.ROUTE, route)
            .eq(RouteStop.ORG_GROUP, current) // converts to isNull when null
            .joinTo(RouteStop.ROUTE_PATH_AS_FROM)
            .joinTo(RoutePath.TO_ROUTE_STOP)
            .eq(RouteStop.ORG_GROUP, current) // converts to isNull when null
            .list();
        this.addRouteStopIdsIfNotPresent(routeStopIdsToAdd, tmpRouteStops);
        
        // fifth is route stops in org group that don't transition
        tmpRouteStops = hsu.createCriteria(RouteStop.class)
            .eq(RouteStop.ROUTE, route)
            .eq(RouteStop.ORG_GROUP, current) // converts to isNull when null
            .sizeEq(RouteStop.ROUTE_PATH_AS_FROM, 0)
            .list();
        this.addRouteStopIdsIfNotPresent(routeStopIdsToAdd, tmpRouteStops);
        
        // sixth is route stops in org group that transition out of org group to some other org group
        if (previousOrgGroupId == null) {
            tmpRouteStops = hsu.createCriteria(RouteStop.class)
                .eq(RouteStop.ROUTE, route)
                .eq(RouteStop.ORG_GROUP, current) // converts to isNull when null
                .joinTo(RouteStop.ROUTE_PATH_AS_FROM)
                .joinTo(RoutePath.TO_ROUTE_STOP)
                .ne(RouteStop.ORG_GROUP, current) // converts to isNull when null
                .list();
        } else {
            tmpRouteStops = hsu.createCriteria(RouteStop.class)
                .eq(RouteStop.ROUTE, route)
                .eq(RouteStop.ORG_GROUP, current) // converts to isNull when null
                .joinTo(RouteStop.ROUTE_PATH_AS_FROM)
                .joinTo(RoutePath.TO_ROUTE_STOP)
                .ne(RouteStop.ORG_GROUP, previous) // converts to isNull when null
                .ne(RouteStop.ORG_GROUP, current) // converts to isNull when null
                .list();
        }
        this.addRouteStopIdsIfNotPresent(routeStopIdsToAdd, tmpRouteStops);
        
        // additional processing for step six before moving on to 7 - grab the first route stop
        // that has a transition to an org group we have not yet processed
        nextRouteStop = null;
        for (RouteStop routeStopThatTransitionsOutOfOrgGroup : tmpRouteStops) {
            java.util.List<RouteStop> transitionedToRouteStops = hsu.createCriteria(RouteStop.class)
                .joinTo(RouteStop.ROUTE_PATH_AS_TO)
                .joinTo(RoutePath.FROM_ROUTE_STOP)
                .eq(RouteStop.ROUTE_STOP_ID, routeStopThatTransitionsOutOfOrgGroup.getRouteStopId())
                .list();
            
            for (RouteStop transitionedToRouteStop : transitionedToRouteStops) {
                String transitionedToOrgGroupId = this.cleanOrgGroupId(transitionedToRouteStop.getOrgGroupId());
                        
                if (!processedOrgGroupIds.contains(transitionedToOrgGroupId)) {
                    nextRouteStop = new BRouteStop(transitionedToRouteStop.getRouteStopId());
                    break;
                }
            }
        }
        
        // seventh is the rest of the route stops (e.g. route stops that can be transitioned to from a non-previous org group)
        tmpRouteStops = hsu.createCriteria(RouteStop.class)
            .eq(RouteStop.ROUTE, route)
            .eq(RouteStop.ORG_GROUP, current) // converts to isNull when null
            .list();
        this.addRouteStopIdsIfNotPresent(routeStopIdsToAdd, tmpRouteStops);
        
        // finally add all route stops for this org group to the main list
        routeStopIds.addAll(routeStopIdsToAdd);
        
        return nextRouteStop;
    }
    
    protected void addRouteStopIdsIfNotPresent(ArrayList<String> routeStopIds, java.util.List<RouteStop> routeStops) {
        for (RouteStop checkRouteStop : routeStops) {
            if (!routeStopIds.contains(checkRouteStop.getRouteStopId())) {
                routeStopIds.add(checkRouteStop.getRouteStopId());
            }
        }
    }
	
	protected void writeRoute(BRoute bRoute) throws DocumentException {
		PdfPTable table = makeTable(new int[] {50, 50});
		Set<RouteTypeAssoc> associations = bRoute.getRouteTypeAssociations();
		boolean alternateRow = false;
		
		// description
		writeColHeaderBold(table, "Route Description", Element.ALIGN_LEFT, 12f, 2);
		writeLeft(table, bRoute.getDescription(), false, 2);
		writeLeft(table, " ", false, 2);
		writeLeft(table, " ", false, 2);
		
		// initial route stop
		writeColHeaderBold(table, "Initial Route Stop", Element.ALIGN_LEFT, 12f, 2);
		if (isEmpty(bRoute.getRouteStopId())) {
			writeLeft(table, "(not set)", false, 2);
		} else {
			writeLeft(table, this.getRouteStopFormatted(new BRouteStop(bRoute.getRouteStopId())), false, 2);
		}
		writeLeft(table, " ", false, 2);
		writeLeft(table, " ", false, 2);
		
		// initial route stop
		writeColHeaderBold(table, "Initial Route Status", Element.ALIGN_LEFT, 12f, 2);
		if (isEmpty(bRoute.getProjectStatusId())) {
			writeLeft(table, "(not set)", false, 2);
		} else {
			writeLeft(table, bRoute.getProjectStatusCode(), false, 2);
		}
		writeLeft(table, " ", false, 2);
		writeLeft(table, " ", false, 2);
		
		// triggers
		writeColHeaderBold(table, "Route Triggers", Element.ALIGN_LEFT, 12f, 2);
		writeColHeaderBold(table, "Project Category", 10f);
		writeColHeaderBold(table, "Project Type", 10f);
		if (associations != null) {
			for (RouteTypeAssoc association : associations) {
				writeLeft(table, association.getProjectCategory().getCode(), alternateRow);
				writeLeft(table, association.getProjectType().getCode(), alternateRow);

				alternateRow = !alternateRow;
			}
		}
		writeLeft(table, " ", false, 2);
		writeLeft(table, " ", false, 2);
		
		writeColHeaderBold(table, "Route Stops", Element.ALIGN_LEFT, 12f, 2);
		addTable(table);
	}
    
    protected void writeRouteStop(PdfPTable table, BRouteStop bRouteStop) {
        boolean alternateRow = true;
        java.util.List<ProjectStatus> projectStatuses = hsu.createCriteria(ProjectStatus.class)
            .orderBy(ProjectStatus.CODE)
            .joinTo(ProjectStatus.ALLOWED_ROUTE_STOPS)
            .eq(RouteStop.ROUTE_STOP_ID, bRouteStop.getRouteStopId())
            .list();
        
        // clear some space
        if (!this.firstRouteStop) {
            writeLeft(table, " ", false, 2);
            writeLeft(table, " ", false, 2);
        }
        this.firstRouteStop = false;
        
        // write out route stop header
        writeColHeaderBold(table, this.getRouteStopFormatted(bRouteStop), Element.ALIGN_LEFT, 10F, 2);
        
        // write out each allowed status
        for (ProjectStatus projectStatus : projectStatuses) {
            alternateRow = !alternateRow;
            
            // write basic info
            write(table, projectStatus.getCode(), alternateRow);
            write(table, projectStatus.getDescription(), alternateRow);
            
            // write transition info, if it exists
            this.writeRouteStopTransition(table, alternateRow, bRouteStop, projectStatus);
        }
    }
    
    protected String getRouteStopFormatted(BRouteStop bRouteStop) {
        OrgGroup orgGroup = bRouteStop.getOrgGroup();
        String routeStopAlias = this.routeStopAliases.get(bRouteStop.getRouteStopId());
		String tmp = "(" + routeStopAlias + ") ";
        
        if (orgGroup == null) {
            tmp += "Requesting Company (Requesting Organizational Group)";
        } else {
            OrgGroup owningCompany = orgGroup.getOwningCompany();
            
            tmp += owningCompany.getName();
            
            if (!orgGroup.getOrgGroupId().equals(owningCompany.getOrgGroupId())) {
                tmp += " (" + orgGroup.getName() + ")";
            }
        }
        tmp += " - " + bRouteStop.getDescription() + " (" + new BProjectPhase(bRouteStop.getPhaseId()).getCode() + ")";
        
        return tmp;
    }
    
    protected void writeRouteStopTransition(PdfPTable table, boolean alternateRow, BRouteStop bRouteStop, ProjectStatus projectStatus) {
        Set<RoutePath> routePaths = bRouteStop.getFromRoutePaths();
        
        // look for a route path form this route stop via this from status
        for (RoutePath routePath : routePaths) {
            if (routePath.getFromProjectStatus().getProjectStatusId().equals(projectStatus.getProjectStatusId())) {
				String tmp = this.routeStopAliases.get(routePath.getToRouteStop().getRouteStopId());
						
                tmp += " (";
				tmp += routePath.getToProjectStatus().getCode();
				tmp += ")";
                
                writeRight(table, ">", alternateRow);
                write(table, tmp, alternateRow);
                
                break;
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            //String r = new ProjectRouteReport().build("00001-0000000002");
            String r = new ProjectRouteReport().build("00000-0000000001");
            
            logger.info(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

	
