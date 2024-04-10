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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.site.userActivity;

import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.reports.SystemAccessRecordReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
/**
 *
 * Arahant
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardSiteUserActivityOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class UserActivityOps extends ServiceBase {

private static final transient ArahantLogger logger = new ArahantLogger(
                    UserActivityOps.class);

    public UserActivityOps() {
            super();
    }

    @WebMethod()
    public GetUserActivityReturn getUserActivity(/*@WebParam(name = "in")*/final GetUserActivityInput in)	{
        
        final GetUserActivityReturn ret = new GetUserActivityReturn();
        /*try
        {
            checkLogin(in);
            SystemAccessRecordReport sar = new SystemAccessRecordReport();
            ret.setReportUrl(sar.Build(in.getSubordinateIds(), in.getStartingDate(), in.getEndingDate(), in.isIncludeUser()));
            finishService(ret);
        }
        catch (final Exception e)
        {
                handleError(hsu, e, ret, logger);
        }*/
        return ret;
    }

    @WebMethod()
    public GetSubordinatesReturn getSubordinates(/*@WebParam(name = "in")*/ final GetSubordinatesInput in){
        final GetSubordinatesReturn ret=new GetSubordinatesReturn();
        /*try
        {
            checkLogin(in);

            Person pers = ArahantSession.getHSU().getCurrentPerson();      

            // If the person is not an employee and therefore has no subordinates, return all users for that company
            if (ArahantSession.getHSU().currentlyArahantUser()){
                HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(Person.class);
                hcu.orderBy(Person.LNAME);
                List<Person> personList = hcu.list();
                List <SubordinateEmployee> subEmps = new ArrayList<SubordinateEmployee>();
                
                for (int loop = 0; loop < personList.size(); loop++){
                    SubordinateEmployee se = new SubordinateEmployee();
                    
                    se.setFname(personList.get(loop).getFname());
                    se.setLname(personList.get(loop).getLname());
                    se.setId(personList.get(loop).getPersonId());
                    
                    subEmps.add(se);
                }

                SubordinateEmployee[] subEmpArray = subEmps.toArray(new SubordinateEmployee[0]);
                ret.setSubordinateEmployees(subEmpArray);
            }
            
            else{
                BPerson bpers = new BPerson(pers);
                BEmployee bemp = new BEmployee(bpers);
                List<String> bemps = bemp.getSubordinateIds(false, true);

                List <SubordinateEmployee> subEmps = new ArrayList<SubordinateEmployee>();

                for (int loop = 0; loop < bemps.size(); loop++){

                    BPerson bpers2 = new BPerson(bemps.get(loop));

                    SubordinateEmployee subEmp = new SubordinateEmployee(); 
                    //System.out.println("getSubordinates " + bpers2.getFirstName() + ", " + bpers2.getLastName() + ", " + bpers2.getFirstName() + ", " + bemps.get(loop));

                    subEmp.setId(bemps.get(loop));
                    subEmp.setFname(bpers2.getFirstName());
                    subEmp.setLname(bpers2.getLastName());
                    subEmps.add(subEmp);
                }

                SubordinateEmployee[] subEmpArray = subEmps.toArray(new SubordinateEmployee[0]);

                ret.setSubordinateEmployees(subEmpArray);
                finishService(ret);
            }
        }
        catch (final Exception e)
        {
                handleError(hsu, e, ret, logger);
        }*/
        return ret;
    }
}
