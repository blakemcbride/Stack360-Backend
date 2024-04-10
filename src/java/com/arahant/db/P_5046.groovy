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

package com.arahant.db

import com.arahant.utils.ArahantLogger
import com.arahant.utils.DateUtils
import com.arahant.utils.ExternalFile
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import org.kissweb.DelimitedFileReader
import org.kissweb.DelimitedFileWriter
import org.kissweb.FileUtils
import org.kissweb.StringUtils
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * Author: Blake McBride
 * Date: 11/19/23
 */
class P_5046 {

    private static final ArahantLogger logger = new ArahantLogger(P_5046.class)

    private static final String imageBackupPath = "/mnt/waytogo/forms-backup/";

    public static run(Connection db) {

        KissConnection.set(db)
        println "Begin table message_attachment"

        String CSVfile = imageBackupPath + "Files.csv";
        if (!(new File(CSVfile)).exists()) {
            logger.error("File " + CSVfile + " not found")
            return
        }

        purgeProjectImagesInDatabase(db);  // this moves all non-purged images to old purge location
        loadAllImages(db);    //  copy all images from old purge location to new external storage location
    }

    private static void loadAllImages(Connection db) {
        logger.info("Starting loadAllImages");
        String CSVfile = imageBackupPath + "Files.csv";
        DelimitedFileReader dfr = new DelimitedFileReader(CSVfile);
        dfr.readHeader();
        while (dfr.nextLine()) {
            String uuid = dfr.getString("File Name");
            String project_id = dfr.getString("Project ID");
            int form_date = dfr.getDate("Form Date");
            String form_type_id = dfr.getString("Form Type ID");
            String comments = dfr.getString("Comments");
            String extension = dfr.getString("Extension");
            String internal = dfr.getString("Internal");
            String source = dfr.getString("Source");
            String shift_id;

            if ((char) project_id.charAt(0) == 'S' as char) {
                shift_id = project_id.substring(2);
                Record prec = db.fetchOne("select project_id from project_shift where project_shift_id = ?", shift_id);
                project_id = prec.getString("project_id");
            } else {
                Record srec = db.fetchOne("select project_shift_id from project_shift where project_id = ? order by shift_start", project_id);
                shift_id = srec.getString("project_shift_id");
            }

            String dirName = imageBackupPath + StringUtils.drop(project_id, -2);
            dirName += "/" + project_id;
            String fileName = dirName + "/" + uuid + "." + extension;
            byte[] form = FileUtils.readFileBytes(fileName)
            Record irec = db.newRecord("project_form");
            String project_form_id = IDGenerator.generate(irec, "project_form_id")
            irec.set("form_type_id", form_type_id);
            irec.set("form_date", form_date);
            irec.set("comments", comments);
            irec.set("source", source);
            //irec.set("form", form);
            irec.set("file_name_extension", extension);
            irec.set("internal", internal);
            irec.set("project_shift_id", shift_id)
            irec.set("form", "ABC".getBytes())
            irec.addRecord();

            ExternalFile.saveData(ExternalFile.PROJECT_FORM_IMAGE, project_form_id, extension, form)
        }
        dfr.close();
        logger.info("Finished loadAllImages");
    }


    /**
     * Purge all project_forms out to remote storage so that all project forms are in the same place.
     *
     */
    private static void purgeProjectImagesInDatabase(Connection db) {
        logger.info("Starting purge of project images");
        Command cmd = db.newCommand();
        String CSVfile = imageBackupPath + "Files.csv";
        DelimitedFileWriter csv = null;
        int n = 0
        try {
            if ((new File(CSVfile)).exists())
                csv = new DelimitedFileWriter(CSVfile, true);
            else {
                csv = new DelimitedFileWriter(CSVfile, false);
                csv.writeField("File Name");
                csv.writeField("Project ID");
                csv.writeField("Form Date");
                csv.writeField("Form Type ID");
                csv.writeField("Comments");
                csv.writeField("Extension");
                csv.writeField("Internal");
                csv.writeField("Source");
                csv.endRecord();
            }

            int today = DateUtils.today();
            List<Record> recs = db.fetchAll("select distinct pf.project_form_id, pf.form_date, p.estimated_last_date " +
                    "from project_form pf " +
                    "join project_shift ps " +
                    "  on pf.project_shift_id = ps.project_shift_id " +
                    "join project p " +
                    "  on ps.project_id = p.project_id ");
            for (Record rec : recs) {
                String uuid = UUID.randomUUID().toString();
                Record irec = cmd.fetchOne("select pf.*, ps.project_id " +
                        "from project_form pf " +
                        "join project_shift ps " +
                        "  on pf.project_shift_id = ps.project_shift_id " +
                        "where pf.project_form_id=?", rec.getString("project_form_id"));
                String dirName = imageBackupPath + StringUtils.drop(irec.getString("project_id"), -2);
                (new File(dirName)).mkdir();
                dirName += "/" + irec.getString("project_id");
                (new File(dirName)).mkdir();
                String fileName = dirName + "/" + uuid + "." + irec.getString("file_name_extension");
                byte [] form = irec.getByteArray("form");
                Files.write(Paths.get(fileName), form, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                csv.writeField(uuid);
                String shiftId = irec.getString("project_shift_id");
                shiftId = "S-" + shiftId; // indicate a shift id rather than a project id
                csv.writeField(shiftId); // shift id in project_id field
                csv.writeDate(irec.getInt("form_date"));
                csv.writeField(irec.getString("form_type_id"));
                String comments = irec.getString("comments");
                if (comments == null)
                    comments = "";
                else {
                    comments = comments.replace("\n", " ");
                    comments = comments.replace("\r", " ");
                    comments = comments.replace("  ", " ");
                }
                csv.writeField(comments);
                csv.writeField(irec.getString("file_name_extension"));
                csv.writeField(irec.getString("internal"));
                csv.writeField(irec.getString("source"));
                csv.endRecord();
                //csv.flush();
                irec.delete();
                if (++n >= 10) {
                    db.commit();
                    n = 0
                }
            }
            csv.close();
            csv = null;
        } catch (Exception e) {
            logger.error(e);
        }
        db.commit()
        cmd.close()
        logger.info("Finished purge of project images");
    }

}
