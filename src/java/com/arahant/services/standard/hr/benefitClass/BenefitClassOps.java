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
package com.arahant.services.standard.hr.benefitClass;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.BenefitClassesReport;
import com.arahant.utils.ArahantSession;
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
 *
 *
 */
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrBenefitClassOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class BenefitClassOps extends ServiceBase {

    private static final transient ArahantLogger logger = new ArahantLogger(
            BenefitClassOps.class);

    public BenefitClassOps() {
        super();
    }

    @WebMethod()
    public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
        final CheckRightReturn ret = new CheckRightReturn();

        try {
            checkLogin(in);

            ret.setAccessLevel(BRight.checkRight(ACCESS_HRSETUPS));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public ListBenefitClassesReturn listBenefitClasses(/*@WebParam(name = "in")*/final ListBenefitClassesInput in) {
        final ListBenefitClassesReturn ret = new ListBenefitClassesReturn();
        try {
            checkLogin(in);

            ret.setItem(BBenefitClass.list(in.getActiveType()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public NewBenefitClassReturn newBenefitClass(/*@WebParam(name = "in")*/final NewBenefitClassInput in) {
        final NewBenefitClassReturn ret = new NewBenefitClassReturn();
        try {
            checkLogin(in);

            final BBenefitClass x = new BBenefitClass();
            ret.setId(x.create());
            in.setData(x);
            x.insert();

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SaveBenefitClassReturn saveBenefitClass(/*@WebParam(name = "in")*/final SaveBenefitClassInput in) {
        final SaveBenefitClassReturn ret = new SaveBenefitClassReturn();
        try {
            checkLogin(in);

            final BBenefitClass x = new BBenefitClass(in.getId());
            in.setData(x);
            x.update();

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public DeleteBenefitClassesReturn deleteBenefitClasses(/*@WebParam(name = "in")*/final DeleteBenefitClassesInput in) {
        final DeleteBenefitClassesReturn ret = new DeleteBenefitClassesReturn();
        try {
            checkLogin(in);

            BBenefitClass.delete(in.getIds());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {
        final GetReportReturn ret = new GetReportReturn();
        try {
            checkLogin(in);

            ret.setReportUrl(new BenefitClassesReport().build(in.getActiveType()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListAssociatedBenefitsReturn listAssociatedBenefits(/*@WebParam(name = "in")*/final ListAssociatedBenefitsInput in) {
        final ListAssociatedBenefitsReturn ret = new ListAssociatedBenefitsReturn();
        try {
            checkLogin(in);
            BBenefitClass bb = new BBenefitClass(in.getClassId());
            ret.setItem(bb.getBean().getBenefits());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListAssociatedConfigsReturn listAssociatedConfigs(/*@WebParam(name = "in")*/final ListAssociatedConfigsInput in) {
        final ListAssociatedConfigsReturn ret = new ListAssociatedConfigsReturn();
        try {
            checkLogin(in);
            BBenefitClass bb = new BBenefitClass(in.getClassId());
                ret.setItem(bb.getBean().getConfigs());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public AssociateBenefitsReturn associateBenefits(/*@WebParam(name = "in")*/final AssociateBenefitsInput in) {
        final AssociateBenefitsReturn ret = new AssociateBenefitsReturn();
        try {
            checkLogin(in);

            BBenefitClass bb = new BBenefitClass(in.getClassId());

            for (String benefitId : in.getIdsArray()) 
                bb.associateBenefit(benefitId);
            
            bb.update();

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public DisassociateBenefitsReturn disassociateBenefits(/*@WebParam(name = "in")*/final DisassociateBenefitsInput in) {
        final DisassociateBenefitsReturn ret = new DisassociateBenefitsReturn();
        try {
            checkLogin(in);

            BBenefitClass bb = new BBenefitClass(in.getClassId());
            for (String benefitId : in.getIdsArray())
                bb.disassociateBenefit(benefitId);
            
            bb.update();
            
            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public AssociateConfigsReturn associateConfigs(/*@WebParam(name = "in")*/final AssociateConfigsInput in) {
        final AssociateConfigsReturn ret = new AssociateConfigsReturn();
        try {
            checkLogin(in);

            BBenefitClass bb = new BBenefitClass(in.getClassId());

            for (String configId : in.getIdsArray())
                bb.associateConfig(configId);

            bb.update();

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public DisassociateConfigsReturn disassociateConfigs(/*@WebParam(name = "in")*/final DisassociateConfigsInput in) {
        final DisassociateConfigsReturn ret = new DisassociateConfigsReturn();
        try {
            checkLogin(in);

           BBenefitClass bb = new BBenefitClass(in.getClassId());
            for (String configId : in.getIdsArray())
                bb.disassociateConfig(configId);

            bb.update();

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListAvailableBenefitsReturn listAvailableBenefits(/*@WebParam(name = "in")*/final ListAvailableBenefitsInput in) {
        final ListAvailableBenefitsReturn ret = new ListAvailableBenefitsReturn();
        try {
            checkLogin(in);

            List l = ArahantSession.getHSU().createCriteria(HrBenefit.class).notIn(HrBenefit.BENEFITID, in.getExcludedIdsArray()).list();
            ret.setItem(l);
            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListAvailableConfigsReturn listAvailableConfigs(/*@WebParam(name = "in")*/final ListAvailableConfigsInput in) {
        final ListAvailableConfigsReturn ret = new ListAvailableConfigsReturn();
        try {
           checkLogin(in);

            List l = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
                    .notIn(HrBenefitConfig.BENEFIT_CONFIG_ID, in.getExcludedIdsArray())
                    .joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, in.getBenefitId())
                    .list();
            ret.setItem(l);

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    //=============
    public static void main(String[] args) {
        BenefitClassOps bo = new BenefitClassOps();
        ListAssociatedConfigsInput in = new ListAssociatedConfigsInput();
        String[] x = new String[1];
        x[0]= "asdff";
        in.setClassId("00001-0000004107");
        String s = "00001-0000004104";
        in.setClassId(s);
       ListAssociatedConfigsReturn ret= bo.listAssociatedConfigs(in);
        System.out.println("Ret " + ret);

//        "00001-0000116253"
//"00001-0000116241"
//
//"00001-0000004104" classid
    }
}
