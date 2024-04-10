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

import com.arahant.utils.Crypto
import org.kissweb.database.*


class CorrectSSNs {

    private static final String encKey = "2384658293468fac86876b65764ac987657fd8757b76c530";

    public static void main(String[] args) {
        Connection db = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "waytogo", "postgres", "postgres")
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select * from person")
        Record rec
        int n = 0
        while ((rec = c.next()) != null) {
            String ssn = decryptSsn(rec.getString("ssn"))
            if (ssn == null  ||  ssn.isEmpty())
                continue
            print ssn + " "
            if (true) {
                String ssn2 = formatSsn(ssn)
                println ssn2
                String essn2 = encryptSsn(ssn2)
                rec.set("ssn", essn2)
                rec.update()
                if (++n % 50 === 0)
                    db.commit()
            } else
                println ""
        }
        db.close()
    }

    static String formatSsn(String ssn) {
        if (ssn == null || ssn.isEmpty())
            return null
        String n = ssn.replaceAll("\\D", "")
        if (n.size() !== 9)
            return ssn;
        return n.substring(0, 3) + '-' + n.substring(3, 5) + '-' + n.substring(5, 9);
    }

    public static String decryptSsn(String ssn) {
        if (ssn != null && ssn.length() > 11)
            try {
                return Crypto.decryptTripleDES(encKey, ssn);
            } catch (Exception e) {
                return ssn;
            }
        return ssn;
    }

    public static String encryptSsn(String ssn) {
        if (ssn == null || ssn.length() == 0)
            return "";
        else
            try {
                return Crypto.encryptTripleDES(encKey, ssn);
            } catch (Exception e) {
                return "";
            }
    }

}
