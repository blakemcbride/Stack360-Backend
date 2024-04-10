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
package com.arahant.reports;

import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.KissConnection;
import com.arahant.utils.Utils;
import org.kissweb.Groff;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Cursor;
import org.kissweb.database.Record;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Created on Feb 27, 2007
 * Modified on Dec 5, 2018
 */
public class HRTrainingCategoryReport {

    public HRTrainingCategoryReport() {
    }

    public String build(HibernateSessionUtil hsu, int activeType) throws Exception {
        Groff rpt = new Groff("TrainingCategory-", "Training Category Report", false);
        Connection con = KissConnection.get();
        rpt.out(".SP 1");
        if (activeType == 1) {
            rpt.out("Show Only Actives");
            rpt.out(".SP 2");
        } else if (activeType == 2) {
            rpt.out("Show Only Inactives");
            rpt.out(".SP 2");
        }
        try (Command cmd = con.newCommand()) {
            tenant(rpt, cmd, activeType, hsu.getCurrentCompany().getName());
            clients(rpt, cmd, activeType);
        }
        return rpt.process();
    }

    private void tenant(Groff rpt, Command cmd, int activeType, String name) throws Exception {
        Cursor c;
        switch (activeType) {
            case 1:  // active
                c = cmd.query("select * from hr_training_category where client_id is null and " +
                        "(last_active_date >= ? or last_active_date = 0) order by name", DateUtils.now());
                break;
            case 2:  // not active
                c = cmd.query("select * from hr_training_category where client_id is null and " +
                        "(last_active_date < ? and last_active_date <> 0) order by name", DateUtils.now());
                break;
            default:  //  all
                c = cmd.query("select * from hr_training_category where client_id is null order by name");
        }
        boolean once = true;
        while (c.isNext()) {
            Record rec = c.getRecord();
            if (once) {
                column_titles(rpt, name);
                once = false;
            }
            outRec(rpt, rec);
        }
        if (!once)
            rpt.out(".TE");
    }

    private void clients(Groff rpt, Command cmd, int activeType) throws Exception {
        Cursor c;
        switch (activeType) {
            case 1:  // active
                c = cmd.query("select htc.*, og.group_name gname from hr_training_category htc " +
                        "join org_group og " +
                        "  on htc.client_id = og.org_group_id " +
                        "where htc.client_id is not null and " +
                        "(htc.last_active_date >= ? or htc.last_active_date = 0) order by htc.name, htc.client_id", DateUtils.now());
                break;
            case 2:  // not active
                c = cmd.query("select htc.*, og.group_name gname from hr_training_category htc " +
                        "join org_group og " +
                        "  on htc.client_id = og.org_group_id " +
                        "where htc.client_id is not null and " +
                        "(htc.last_active_date < ? and htc.last_active_date <> 0) order by htc.name, htc.client_id", DateUtils.now());
                break;
            default:  //  all
                c = cmd.query("select htc.*, og.group_name gname from hr_training_category htc " +
                        "join org_group og " +
                        "  on htc.client_id = og.org_group_id " +
                        "where htc.client_id is not null order by htc.name, htc.client_id");
        }
        String lastClientId = null;
        while (c.isNext()) {
            Record rec = c.getRecord();
            String client_id = rec.getString("client_id");
            if (lastClientId  == null  ||  !lastClientId.equals(client_id)) {
                if (lastClientId != null)
                    rpt.out(".TE");
                rpt.out(".SP 2");
                column_titles(rpt, rec.getString("gname"));
                lastClientId = client_id;
            }
            outRec(rpt, rec);
        }
        if (lastClientId != null)
            rpt.out(".TE");
    }

    private void column_titles(Groff rpt, String client) {
        rpt.out(".SP");
        rpt.out("Client:  " + client);
        rpt.out(".SP");
        rpt.out(".TS H");
        rpt.out("l l l l l l l n n.");
        String cols = "Training\t\tType\t\tReq\t\tHours\t\tEnd Date";
        rpt.out(cols);
        rpt.out("\\_\t\t\\_\t\t\\_\t\t\\_\t\t\\_\t\t\\_\t\t\\_\t\t\\_\t\t");
        rpt.out(".TH");
    }

    private void outRec(Groff rpt, Record rec) throws SQLException {
        rpt.out(rec.getString("name") + "\t\t" +
                (rec.getShort("training_type") == 1 ? "Train\t\t" : "Cert\t\t") +
                (rec.getChar("required") == 'Y' ? "Yes\t\t" : "No\t\t") +
                Utils.Format(rec.getFloat("hours"), "B", 0, 2) + "\t\t" +
                DateUtils.getDateFormatted(rec.getInt("last_active_date"))
        );
    }

}

	
