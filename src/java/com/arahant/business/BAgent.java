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
 *  Created on Oct 8, 2009
 *
 */
package com.arahant.business;

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BAgent extends BPerson implements IDBFunctions {

    private static final transient ArahantLogger logger = new ArahantLogger(BAgent.class);
	
    Agent agent;


    public BAgent() {
    }

    public BAgent(final String key) throws ArahantException {
        internalLoad(key);
    }

    public BAgent(final Person person, final String groupId) throws ArahantException {
        super(person, groupId);
        agent = (Agent) person;
    }

    public BAgent(final String personId, final String groupId) throws ArahantException {
        super(ArahantSession.getHSU().get(Person.class, personId), groupId);
        agent = (Agent) person;
    }

    public BAgent(final Agent contact) throws ArahantException {
        super(contact);
        agent = contact;
    }

    @Override
    public void assignToOrgGroup(final String orgGroupId, final boolean isPrimary) throws ArahantException {

        super.assignToOrgGroup(orgGroupId, isPrimary);
        if (isPrimary) {
            //if this one is primary, remove any other primaries for that org group
            final Iterator ogaItr = ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroupId).list().iterator();

            while (ogaItr.hasNext()) {
                final OrgGroupAssociation oga = (OrgGroupAssociation) ogaItr.next();
                if (oga.getPerson().equals(person)) {
                    continue;
                }
                oga.setPrimaryIndicator('N');
                ArahantSession.getHSU().saveOrUpdate(oga);
            }
        }
    }

    @Override
    public String create() throws ArahantException {
        agent = new Agent();
        person = agent;
        person.generateId();
        person.setOrgGroupType(AGENT_TYPE);
		person.setRecordType('R');
        // create agent contact specific stuff
        this.createOther();

        return getPersonId();
    }

    @Override
    protected void createOther() throws ArahantException {
        super.createOther();

        // agent contact specific creation stuff goes here
        //final Set <Agent> s=person.getAgents();
        //s.add(agent);
        //person.setAgents(s);
	}
	
    private void internalLoad(final String key) throws ArahantException {
        logger.debug("Loading " + key);
        super.load(key);
        agent = (Agent) person;
    }

    @Override
    public void load(final String key) throws ArahantException {
		internalLoad(key);
    }

    @Override
    public void insert() throws ArahantException {
        super.insert();
        ArahantSession.getHSU().insert(agent);
    }

    @Override
    public void update() throws ArahantException {
        super.update();
        ArahantSession.getHSU().saveOrUpdate(agent);
    }

    @Override
    public void delete() throws ArahantDeleteException {
        //hsu.delete(agent);
        ArahantSession.getHSU().createCriteria(AgentJoin.class)
            .eq(AgentJoin.AGENT,agent)
            .delete();

        super.delete();
    }

    public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
        for (final String element : ids) {
            new BAgent(element).delete();
        }
    }

    public static BAgent[] list(final HibernateSessionUtil hsu, final String groupId) throws ArahantException {

        final List<Agent> plist = hsu.createCriteria(Agent.class).orderBy(Person.LNAME).orderBy(Person.FNAME).joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, groupId).list();


        return makeAray(plist, groupId);
    }

    static BAgent[] makeAray(final List<Agent> plist, final String groupId) throws ArahantException {
        final BAgent[] ret = new BAgent[plist.size()];

        for (int loop = 0; loop < plist.size(); loop++) {
            ret[loop] = new BAgent(plist.get(loop), groupId);
        }
        return ret;
    }

    public static BAgent[] search(final HibernateSessionUtil hsu, final String firstName, final String lastName, final String orgGroupId, final int associatedIndicator, final int max) throws ArahantException {

        final HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class).eq(Person.ORGGROUPTYPE, AGENT_TYPE);

        hcu.setMaxResults(max);

        final HibernateCriteriaUtil personHcu = hcu;//hcu.joinTo(Agent.MAIN_CONTACT);
        personHcu.orderBy(Person.LNAME);
        if (!isEmpty(firstName)) {
            personHcu.like(Person.FNAME, firstName);
        }
        if (!isEmpty(lastName)) {
            personHcu.like(Person.LNAME, lastName);
        }

        OrgGroup og = null;
        if (!isEmpty(orgGroupId)) {
            og = hsu.get(OrgGroup.class, orgGroupId);
            if (og.getOwningCompany() != null) {
                personHcu.joinTo(Person.COMPANYBASE).eq(OrgGroup.ORGGROUPID, og.getOwningCompany().getOrgGroupId());
            }

            //TODO figure out how to do this in the query
            //	HibernateCriteriaUtil orgAssocHcu=personHcu.leftJoinTo(Person.ORGGROUPASSOCIATIONS);
            //	HibernateCriteriaUtil orgGroupHcu=orgAssocHcu.leftJoinTo(OrgGroupAssociation.ORGGROUP);
            //	orgGroupHcu.ne(OrgGroup.ORGGROUPID, ccsc.getOrgGroupId());
        }

        if (!isEmpty(orgGroupId)) {
            final List p = hsu.createCriteria(Person.class).selectFields(Person.PERSONID).joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroupId).distinct().list();
            hcu.notIn(Person.PERSONID, p);
        }


        switch (associatedIndicator) {
            case 0:
                break;//all
            case 1: 		//associated
                //hcu.isNotNull(Person.ORGGROUPASSOCIATIONS); //make sure there are some, inner join
                hcu.sizeNe(Person.ORGGROUPASSOCIATIONS, 0);
                break;
            case 2: //not associated
                // hibernate can't handle sizeEq here
                //hcu.isNull(Person.ORGGROUPASSOCIATIONS);
                //hcu.sizeEq(ProspectContact.ORGGROUPASSOCIATIONS,0);
                hcu.sizeEq(Person.ORGGROUPASSOCIATIONS, 0);
                break;
        }


        List<Person> res = new LinkedList<Person>();

        if (og != null) {
            final Iterator<Person> resItr = hcu.list().iterator();

            while (resItr.hasNext()) {
                try {
                    final Person cc = resItr.next();
                    final Iterator ogaItr = cc.getOrgGroupAssociations().iterator();
                    boolean found = false;
                    while (ogaItr.hasNext()) {
                        final OrgGroupAssociation oga = (OrgGroupAssociation) ogaItr.next();
                        if (oga.getOrgGroup().equals(og)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        res.add(cc);
                    }
                } catch (final Exception e) {
                    continue; //if it failed to clear, skip it
                }
            }
        } else {
            res = hcu.list();
        }

        final BAgent[] ret = new BAgent[res.size()];

        for (int loop = 0; loop < ret.length; loop++) {
            ret[loop] = new BAgent((Agent) res.get(loop));
        }

        return ret;
    }

    public void assignToThisGroup(final String[] companyIds) throws ArahantException {
        //hsu.createCriteria(AgencyJoin.class).eq(AgencyJoin.AGENCY, agency).delete();

        //hsu.createCriteriaNoCompanyFilter(AgentJoin.class).joinTo(AgentJoin.COMPANY).in(CompanyDetail.ORGGROUPID, companyIds).delete();

        for (final String element : companyIds) {
            AgentJoin j = new AgentJoin();
            j.generateId();
            j.setAgent(agent);
            j.setCompany(ArahantSession.getHSU().get(CompanyDetail.class, element));
            ArahantSession.getHSU().insert(j);
        }
    }

    public static BAgent[] list(final HibernateSessionUtil hsu, final String groupId, final int cap, final String name) throws ArahantException {

        final HibernateCriteriaUtil<Agent> hcu = hsu.createCriteria(Agent.class)
                        .orderBy(Person.LNAME)
                        .orderBy(Person.FNAME)
                        .like(Person.LNAME, name);

        final HibernateCriteriaUtil ogaHcu = hcu.joinTo(Person.ORGGROUPASSOCIATIONS);

        ogaHcu.joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, groupId).list();


        return makeAray(hcu.list(), groupId);
    }

    public BCompany[] getAgentCompanies() {
        return BCompany.makeArray(ArahantSession.getHSU()
                .createCriteriaNoCompanyFilter(CompanyDetail.class)
                .orderBy(CompanyDetail.NAME)
                .joinTo(CompanyDetail.AGENT_JOINS)
                .eq(AgentJoin.AGENT, agent)
                .list());

    }

	public BCompany[] getActiveAgentCompanies() {
		HibernateCriteriaUtil<CompanyDetail> hcu= ArahantSession.getHSU()
                .createCriteriaNoCompanyFilter(CompanyDetail.class)
				.orderBy(CompanyDetail.NAME);

		hcu.joinTo(CompanyDetail.AGENT_JOINS)
                .eq(AgentJoin.AGENT, agent);

		hcu.joinTo(CompanyDetail.ORGGROUPASSOCIATIONS)
				.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y');

        return BCompany.makeArray(hcu.list());

    }

    public BCompany[] getAgentCompanies(final String agentId, final String name) {
        final HibernateCriteriaUtil<CompanyDetail> hcu = ArahantSession.getHSU()
                .createCriteriaNoCompanyFilter(CompanyDetail.class)
                .orderBy(CompanyDetail.NAME)
                .like(CompanyDetail.NAME, name)
                .joinTo(CompanyDetail.AGENT_JOINS)
                .eq(AgentJoin.AGENT, agent);

        return BCompany.makeArray(hcu.list());

    }

	public BCompany[] getAssignedCompanies(final boolean activeAuthorized, final boolean active, final boolean inactive, BAgent agent) {
		HibernateCriteriaUtil<CompanyDetail> hcu = ArahantSession.getHSU()
                .createCriteriaNoCompanyFilter(CompanyDetail.class)
                .orderBy(CompanyDetail.NAME)
                .joinTo(CompanyDetail.AGENCY_JOINS)
                .joinTo(AgencyJoin.AGENCY)
				.joinTo(Agency.ORGGROUPASSOCIATIONS)
				.eq(OrgGroupAssociation.PERSON, agent.getBean());

		List <CompanyDetail> ret=new ArrayList<CompanyDetail>();
		for (CompanyDetail cd : hcu.list())
		{
			BCompany bco=new BCompany(cd);
			if (activeAuthorized && bco.getAgentStatus(agent).equals("Active/Authorized"))
				ret.add(cd);
			if (active && bco.getAgentStatus(agent).equals("Active"))
				ret.add(cd);
			if(inactive && bco.getAgentStatus(agent).equals("Inactive"))
				ret.add(cd);

		}
        return BCompany.makeArray(ret);
    }


    public BCompany[] search(String name, int assocInd, int cap) {

        List orgids = ArahantSession.getHSU().createCriteriaNoCompanyFilter(CompanyDetail.class)
                .selectFields(CompanyDetail.ORGGROUPID)
                .joinTo(CompanyDetail.AGENCY_JOINS)
                .eq(AgencyJoin.AGENCY, agent.getCompanyBase()).list();

        List excludes=ArahantSession.getHSU().createCriteriaNoCompanyFilter(CompanyDetail.class)
                .selectFields(CompanyDetail.ORGGROUPID)
                .joinTo(CompanyDetail.AGENT_JOINS)
                .eq(AgentJoin.AGENT, agent)
                .list();

        final HibernateCriteriaUtil<CompanyDetail> hcu = ArahantSession.getHSU().createCriteriaNoCompanyFilter(CompanyDetail.class)
                .like(CompanyDetail.NAME, name)
                .in(CompanyDetail.ORGGROUPID, orgids)
                .notIn(CompanyDetail.ORGGROUPID, excludes)
                .setMaxResults(cap).orderBy(CompanyDetail.NAME);


        switch (assocInd) {
            case 0:
                break;//all
            case 1:
                hcu.sizeNe(CompanyDetail.AGENT_JOINS, 0);
                break;
            case 2:
                hcu.sizeEq(CompanyDetail.AGENT_JOINS, 0);
                break;
        }


        return BCompany.makeArray(hcu.list());

    }

    public void setCompanyIds(String[] companyIds) {

        ArahantSession.getHSU().createCriteria(AgentJoin.class)
            .eq(AgentJoin.AGENT, agent)
            .delete();

        for (String id : companyIds)
        {
            BAgentJoin a = new BAgentJoin();
            a.create();
            a.setAgent(agent);
            a.setCompanyId(id);
            a.setApproved('N');
            addPendingInsert(a);
        }
    }

	public void setExtRef(String ref)
	{
		agent.setExtRef(ref);
	}

	public String getExtRef()
	{
		return agent.getExtRef();
	}

	public Agent getBean()
	{
		return agent;
	}

	public boolean userNameTaken(final String username) throws ArahantException {

		final HibernateCriteriaUtil<Agent> hcu = ArahantSession.getHSU().createCriteria(Agent.class)
				.joinTo(Agent.PROPHETLOGINS)
				.eq(ProphetLogin.USERLOGIN, username);

		if (hcu.list().isEmpty())
			return false;

		return true;
	}
}

