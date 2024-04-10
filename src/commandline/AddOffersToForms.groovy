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

import com.arahant.utils.DateUtils
import com.arahant.utils.ExternalFile
import com.arahant.utils.IDGeneratorKiss
import com.arahant.utils.KissConnection
import org.kissweb.FileUtils
import org.kissweb.builder.BuildUtils
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 5/21/23
 */
class AddOffersToForms {

    private final static String EMPLOYMENT_AGREEMENT = "00001-0000000004"

    static void main(String[] args) {
        Connection db = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "waytogo", "postgres", "postgres")
        KissConnection.set(db)
        Command cmd = db.newCommand()
        int i = 0

        Cursor c = cmd.query("""select applicant_application_id, person_id
                                from applicant_application""")
        while (c.isNext()) {
            Record rec = c.getRecord()
            String applicationId = rec.getString("applicant_application_id")

            File signedOfferHtmlFile = new File(ExternalFile.makeExternalFilePath(ExternalFile.APPLICANT_APPLICATION_SIGNED_OFFER, applicationId, "html"))
            File signedOfferSigFile = new File(ExternalFile.makeExternalFilePath(ExternalFile.APPLICANT_APPLICATION_SIGNED_OFFER, applicationId, "sig"))
            File signedOfferGpgFile = new File(ExternalFile.makeExternalFilePath(ExternalFile.APPLICANT_APPLICATION_SIGNED_OFFER, applicationId, "zip.gpg"))
            File signedOfferZipFile = new File(ExternalFile.makeExternalFilePath(ExternalFile.APPLICANT_APPLICATION_SIGNED_OFFER, applicationId, "zip"))
            if (signedOfferGpgFile.exists()) {
                if (!db.exists("select 1 from person_form where person_id = ? and form_type_id = ?", rec.getString("person_id"), EMPLOYMENT_AGREEMENT)) {
                    final String path = signedOfferGpgFile.getCanonicalFile().getParent();
                    signedOfferZipFile.delete()
                    BuildUtils.run(false, true, false, false, path, "gpg -q " + signedOfferGpgFile.getName())
                    BuildUtils.run(false, true, false, false, path, "unzip -o " + signedOfferZipFile.getName())
                    Record frec = db.newRecord("person_form")
                    String personFormId = IDGeneratorKiss.generate(frec, "person_form_id")
                    frec.set("form_type_id", EMPLOYMENT_AGREEMENT)
                    frec.set("form_date", DateUtils.getDate(new Date(signedOfferHtmlFile.lastModified())))
                    frec.set("person_id", rec.getString("person_id"))
                    frec.set("comments", "Signed offer letter")
                    frec.set("source", "Electronically signed")
                    frec.set("file_name_extension", "html")
                    frec.set("electronically_signed", "Y")
                    frec.addRecord()
                    ExternalFile.saveData(ExternalFile.PERSON_FORM, personFormId, "html", FileUtils.readFile(signedOfferHtmlFile.getAbsolutePath()).getBytes())
                    println "Adding offer letter to forms " + i
                    if (++i > 40) {
                        db.commit()
                        i = 0
                    }

                    signedOfferSigFile.delete()
                    signedOfferHtmlFile.delete()
                    signedOfferZipFile.delete()
                }
            }
        }
        db.commit()
        db.close()
    }


}
