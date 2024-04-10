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

package com.arahant.utils;

import com.arahant.timertasks.TimerTaskBase;
import org.kissweb.DelimitedFileWriter;
import org.kissweb.StringUtils;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;
import org.apache.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Author: Blake McBride
 * Date: 1/20/20
 */
public class AutoDataPurge extends TimerTaskBase {

    private static final ArahantLogger logger = new ArahantLogger(AutoDataPurge.class);

    @Override
    public void execute() throws Exception {
        logger.setLevel(Level.INFO);
        purgeOldWebImages();
        purgeProjectImagesInDatabase();
    }

    /**
     * This just deletes files that were temporarily restored for client access.
     * They're just copies hat can be deleted at any time.
     */
    private void purgeOldWebImages() {
        logger.info("Starting purge of old web images");
        long current = (new Date()).getTime();
        String rootDir = getImagePath();
        File[] dirs = (new File(rootDir)).listFiles();
        if (dirs == null) {
            logger.error("Path " + rootDir + " not found");
            return;
        }
        for (File dir : dirs) {
            if (!dir.isDirectory() || "WEB-INF".equals(dir.getName()))
                continue;
            File [] dirs2 = dir.listFiles();
            if (dirs2 == null)
                continue;
            for (File dir2 : dirs2) {
                File [] files = dir2.listFiles();
                if (files == null)
                    continue;
                for (File file : files) {
                    long last = file.lastModified();
                    long millisOld = current - last;
                    long daysOld = millisOld / (1000 * 60 * 60 * 24);
                    if (daysOld > 60)
                        file.delete();
                }
                dir2.delete();  // delete directory.  Will only succeed if empty - just what we want.
            }
            dir.delete();  // delete directory.  Will only succeed if empty - just what we want.
        }
    }

    private static final String imageBackupPath = "/mnt/waytogo/forms-backup/";

    private void purgeProjectImagesInDatabase() {
        logger.info("Starting purge of project images");
        Connection db = KissConnection.get();
        Command cmd = db.newCommand();
        String CSVfile = imageBackupPath + "Files.csv";
        DelimitedFileWriter csv = null;
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
            int date = DateUtils.addDays(today, -45);  // forms at least 45 days old
            List<Record> recs = db.fetchAll("select distinct pf.project_form_id, pf.form_date, p.estimated_last_date " +
                    "from project_form pf " +
                    "join project_shift ps " +
                    "  on pf.project_shift_id = ps.project_shift_id " +
                    "join project p " +
                    "  on ps.project_id = p.project_id " +
                    "where pf.form_date < ?", date);
            for (Record rec : recs) {
                Integer projectLastDate = rec.getInt("estimated_last_date");
                if (projectLastDate == null  ||  projectLastDate > date)
                    continue;
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
            }
            csv.close();
            csv = null;
        } catch (Exception e) {
            logger.error(e);
        }
        if (csv != null)
            csv.close();
    }

    private static String getImagePath() {
        String dir = appendSlash(FileSystemUtils.getWorkingDirectory().getAbsolutePath()) + "../";
        return dir + "../webapps/files";
    }

    private static String appendSlash(String s) {
        if (s == null  ||  s.isEmpty())
            return s;
        return s.endsWith("/") ? s : s + "/";
    }
}
