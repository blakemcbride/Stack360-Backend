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

import com.arahant.utils.ElectronicallySign
import com.arahant.utils.ExternalFile
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.KissConnection
import org.kissweb.StringUtils
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 5/8/23
 *
 * Copy existing job offers into their respective perform_form records.
 */
class SignAllOffers {

    static void main(String[] args) {
        Connection db = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "waytogo", "postgres", "postgres")
        KissConnection.set(db)
        Command cmd = db.newCommand()
        Cursor c = cmd.query("""select applicant_application_id
                                from applicant_application""")
        while (c.isNext()) {
            Record rec = c.getRecord()
            String applicationId = rec.getString("applicant_application_id")
            File signedOfferHtmlFile = new File(ExternalFile.makeExternalFilePath(ExternalFile.APPLICANT_APPLICATION_SIGNED_OFFER, applicationId, "html"))
            File signedOfferSigFile = new File(ExternalFile.makeExternalFilePath(ExternalFile.APPLICANT_APPLICATION_SIGNED_OFFER, applicationId, "sig"))
            File signedOfferGpgFile = new File(ExternalFile.makeExternalFilePath(ExternalFile.APPLICANT_APPLICATION_SIGNED_OFFER, applicationId, "zip.gpg"))
            if (signedOfferGpgFile.exists()) {
                signedOfferSigFile.delete()
                signedOfferHtmlFile.delete()
                println "Deleting component files"
            } else if (signedOfferHtmlFile.exists()) {
                String addr
                String sig
                if (signedOfferSigFile.exists()) {
                    BufferedReader fp = new BufferedReader(new FileReader(signedOfferSigFile))
                    fp.readLine()  // File:
                    sig = StringUtils.drop(fp.readLine(), 12)
                    fp.readLine() // Date:
                    addr = StringUtils.drop(fp.readLine(), 13)
                    fp.close()
                } else {
                    addr = "(unknown)"
                    sig = "(on form)"
                }
                println "Electronically signing offer letter"
                ElectronicallySign.electronicallySignDocument(signedOfferHtmlFile.toString(), addr, sig, "password")
                signedOfferSigFile.delete()
                signedOfferHtmlFile.delete()
            }
        }
    }



}
