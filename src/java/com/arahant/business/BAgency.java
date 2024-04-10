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
 * Created on Oct 8, 2009
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
import java.util.List;

public class BAgency extends BCompanyBase implements IDBFunctions {

    private static final transient ArahantLogger logger = new ArahantLogger(BAgency.class);

    private Agency agency;


    public BAgency() {
    }

    /**
     * @param orgGroupId
     * @throws ArahantException
     */
    public BAgency(final String orgGroupId) throws ArahantException {
        super();
        agency=ArahantSession.getHSU().get(Agency.class, orgGroupId);
        company_base=agency;
    }

    /**
     * @param company
     * @throws ArahantException
     */
    public BAgency(final Agency company) throws ArahantException {
        super();
        agency = company;
        initMembers(agency);
    }

    public String getExternalId() {
        return agency.getExternalId();
    }

	public void setExternalId(String extId) {
        agency.setExternalId(extId);
    }

	public String getAgencyExternalId() {
		return agency.getAgencyExternalId();
	}

	public void setAgencyExternalId(final String agencyExternalId) {
		agency.setAgencyExternalId(agencyExternalId);
	}

	@Override
    public Agency getBean() {
        return agency;
    }

    @Override
    public String getId() {
        return agency.getOrgGroupId();
    }

    @Override
    public int getOrgGroupType() {

        return AGENT_TYPE;
    }

    @Override
    boolean checkMainContact(final String val) throws ArahantException {
        if (getMainContact() == null) {
            if (isEmpty(val)) {
                return false;
            }

            logger.debug("Decided to make a new contact because of value of '" + val + "'");

            final BAgent be = new BAgent();
            be.create();
            setMainContact(be);
        }
        return true;
    }

    @Override
    public String getOrgType() {
        return "Agent";
    }

    public static void deleteCompanies(final HibernateSessionUtil hsu, final String[] agencyId) throws ArahantException {
        for (final String element : agencyId) {
            new BAgency(element).delete();
        }
    }

    @Override
    public String create() throws ArahantException {
        agency = new Agency();
        company_base = agency;
        final String id = super.create();
        agency.setOrgGroupId(id);
        agency.setOrgGroupType(AGENT_TYPE);
        return id;
    }

    @Override
    public void load(final String key) throws ArahantException {
        logger.debug("Loading " + key);
        agency = ArahantSession.getHSU().get(Agency.class, key);
        initMembers(agency);
    }

    @Override
    public void update() throws ArahantDeleteException {
        super.update();
        ArahantSession.getHSU().saveOrUpdate(agency);
    }

    @Override
    public void insert() throws ArahantException {
        company_base.setOwningCompany(null);
        ArahantSession.getHSU().insert(agency);
        super.insert();
    }

    @Override
    public void delete() throws ArahantDeleteException {
        super.delete();
        ArahantSession.getHSU().delete(agency);
    }

    public void removeCompanies(final String[] ids) throws ArahantException {
        ArahantSession.getHSU().createCriteria(AgencyJoin.class)
               .eq(AgencyJoin.AGENCY,agency)
               .joinTo(AgencyJoin.COMPANY)
               .in(CompanyDetail.ORGGROUPID, ids)
               .delete();
    }

    /**
     * @return
     */
    public String getOrgGroupTypeName() {
        return "Agent";
    }

    /**
     * @param hsu
     * @param name
     * @param mainContactFirstName
     * @param mainContactLastName
     * @param max
     * @param inactiveDate
     * @param inactiveDateSearchType
     * @return
     * @throws ArahantException
     */
    public static BAgency[] search(final HibernateSessionUtil hsu, final String name, final String agentFirstName,
            final String agentLastName, final String identifier, final int max) throws ArahantException {

        final HibernateCriteriaUtil hcu = hsu.createCriteria(Agency.class);

        hcu.setMaxResults(max);

        hcu.like(OrgGroup.NAME, name);
        hcu.like(CompanyBase.EXTERNAL_REF, identifier);
        hcu.orderBy(OrgGroup.NAME);

        if ((!isEmpty(agentFirstName) && !agentFirstName.equals("%")) || (!isEmpty(agentLastName) && !agentLastName.equals("%"))) {
            final HibernateCriteriaUtil joinOrgAssoc = hcu.joinTo(OrgGroup.ORGGROUPASSOCIATIONS);
            final HibernateCriteriaUtil joinPerson = joinOrgAssoc.joinTo(OrgGroupAssociation.PERSON);
            joinPerson.like(Person.FNAME, agentFirstName);
            joinPerson.like(Person.LNAME, agentLastName);
        }

        final List results = hcu.list();

        final BAgency[] ret = new BAgency[results.size()];
        for (int loop = 0; loop < results.size(); loop++) {
            ret[loop] = new BAgency((Agency) results.get(loop));
        }

        return ret;
    }

    public static BAgency[] searchAgents(String name, int max) {
        return makeArray(ArahantSession.getHSU().createCriteria(Agency.class).like(Agency.NAME, name).setMaxResults(max).orderBy(Agency.NAME).list());
    }

    public static BAgency[] searchAgents(String name, String[] excludeIds, int max) {
        return makeArray(ArahantSession.getHSU().createCriteria(Agency.class).like(Agency.NAME, name).notIn(Agency.ORGGROUPID, excludeIds).setMaxResults(max).orderBy(Agency.NAME).list());
    }

    public BCompany[] listAssociatedCompanies(String name, int max) throws ArahantException {
        final HibernateCriteriaUtil<CompanyDetail> hcu = ArahantSession.getHSU()
                .createCriteriaNoCompanyFilter(CompanyDetail.class)
				.like(CompanyDetail.NAME, name)
				.setMaxResults(max)
                .orderBy(CompanyDetail.NAME)
				.joinTo(CompanyDetail.AGENCY_JOINS)
				.eq(AgencyJoin.AGENCY, agency);
        return BCompany.makeArray(hcu.list());

	/*
        return BCompany.makeArrayEx(hsu.createCriteriaNoCompanyFilter(CompanyDetail.class)
        .orderBy(CompanyDetail.NAME)
        .joinTo(CompanyDetail.AGENCY_JOINS)
        .eq(AgencyJoin.AGENCY, agency)
        .list());
         */
    }


    public static BAgency[] makeArray(List<Agency> l) {
        BAgency[] ret = new BAgency[l.size()];

        for (int loop = 0; loop < ret.length; loop++) {
            ret[loop] = new BAgency(l.get(loop));
        }
        return ret;
    }

    public String getAgencyName() {
        return agency.getName();
    }

    public String getAgencyIdentifier() {
        return agency.getExternalId();
    }

    public void assignToThisGroup(final String []companyIds) throws ArahantException
    {
        //hsu.createCriteria(AgencyJoin.class).eq(AgencyJoin.AGENCY, agency).delete();

        ArahantSession.getHSU().createCriteria(AgencyJoin.class)
            .joinTo(AgencyJoin.COMPANY)
            .in(CompanyDetail.ORGGROUPID,companyIds)
            .delete();

        for (final String element : companyIds) {
            AgencyJoin j=new AgencyJoin();
            j.generateId();
            j.setAgency(agency);
            j.setCompany(ArahantSession.getHSU().get(CompanyDetail.class,element));
            ArahantSession.getHSU().insert(j);
        }
    }

    public BCompany[] search(String name, int assocInd, int cap) {

        List orgids=ArahantSession.getHSU().createCriteriaNoCompanyFilter(CompanyDetail.class)
               .selectFields(CompanyDetail.ORGGROUPID)
               .joinTo(CompanyDetail.AGENCY_JOINS)
               .eq(AgencyJoin.AGENCY,agency)
               .list();

        final HibernateCriteriaUtil<CompanyDetail> hcu = ArahantSession.getHSU()
                .createCriteriaNoCompanyFilter(CompanyDetail.class)
                .notIn(CompanyDetail.ORGGROUPID, orgids)
                .setMaxResults(cap)
                .orderBy(CompanyDetail.NAME);

        hcu.like(CompanyDetail.NAME, name);
        
        switch (assocInd) {
            case 0:
                break;//all
            case 1:
                hcu.sizeNe(CompanyDetail.AGENCY_JOINS, 0);
                break;
            case 2:
                hcu.sizeEq(CompanyDetail.AGENCY_JOINS, 0);
                break;
        }

        return BCompany.makeArray(hcu.list());
    }
 
	public BCompany[] searchCompaniesForAgents(String name, int assocInd, int cap) {
        List orgids=ArahantSession.getHSU().createCriteriaNoCompanyFilter(CompanyDetail.class)
               .selectFields(CompanyDetail.ORGGROUPID)
               .joinTo(CompanyDetail.AGENCY_JOINS)
               .eq(AgencyJoin.AGENCY,agency)
               .list();

        final HibernateCriteriaUtil<CompanyDetail> hcu = ArahantSession.getHSU()
                .createCriteriaNoCompanyFilter(CompanyDetail.class)
                .in(CompanyDetail.ORGGROUPID, orgids)
                .setMaxResults(cap)
                .orderBy(CompanyDetail.NAME);

        hcu.like(CompanyDetail.NAME, name);

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
}

