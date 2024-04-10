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

package com.arahant.services.standard.project.workerAvailabilityReport

import com.arahant.beans.*
import com.arahant.business.BEmployee
import com.arahant.business.BHRPosition
import com.arahant.business.BProject
import com.arahant.exceptions.ArahantWarning
import com.arahant.servlets.REST
import com.arahant.utils.*
import org.kissweb.Groff
import org.kissweb.StringUtils
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 *
 * Creates report or sends email.
 *
 * Date: 7/23/18
 * Modified: 1/2/19
 */
class CreateReport {

    private int firstDate
    private int lastDate
    private String zipCode
    private int miles
    private boolean sendEmail
    private List<String> position_id
    private String zip1
    private JSONObject outJSON
    private HibernateSessionUtil hsu
    private String emailTitle, emailMessage
    private boolean isTest = false
    private boolean training
    private BProject project

    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        CreateReport inst = new CreateReport()
        inst.hsu = hsu
        inst.outJSON = outJSON

        String projectId = inJSON.getString("project_id")

        if (projectId) {
            inst.project = new BProject(projectId)
            if (inst.project.address != null)
                inst.zipCode = inst.project.address.zip
            inst.firstDate = inst.project.estimatedFirstDate
            inst.lastDate = inst.project.estimatedLastDate
        } else
            inst.zipCode = inJSON.getString("zipCode")

        StringBuilder buff = new StringBuilder()
        Boolean hasError = false
        if (inst.firstDate == 0 && inst.project) {
            hasError = true
            buff.append("first date")
        }

        if (inst.lastDate == 0 && inst.project) {
            hasError = true
            if (buff.length() != 0)
                buff.append(", ")
            buff.append("last date")
        }

        if (inst.zipCode == null) {
            hasError = true
            if (buff.length() != 0)
                buff.append(", ")
            buff.append("zip code")
        }

        if (hasError)
            throw new ArahantWarning("Project is missing " + buff.toString() + ".")

        inst.miles = inJSON.getInt "miles"
        inst.position_id = getPositionIds inJSON
        inst.sendEmail = inJSON.getBoolean "send_email"
        if (inst.sendEmail) {
            inst.emailTitle = inJSON.getString "email_title"
            inst.emailMessage = inJSON.getString "email_message"
        } else {
            inst.training = inJSON.getBoolean "include_training"
        }
        if (inst.firstDate == 0)
            inst.firstDate = inst.lastDate
        if (inst.lastDate == 0)
            inst.lastDate = inst.firstDate
        if (inst.firstDate == 0 && inst.lastDate == 0)
            inst.firstDate = inst.lastDate = DateUtils.today()
        if (inst.zipCode != null && !inst.zipCode.isEmpty())
            inst.zip1 = inst.zipCode
        else
            inst.zip1 = ""
        char availability = inJSON.getCharacter("availability") as char
        if (availability == 'T' as char || availability == 'A' as char)
            inst.doRptAvailable(availability)
        else
            inst.doRptAssigned()
    }

    private static List<String> getPositionIds(JSONObject injson) {
        List<String> ret = new ArrayList<String>()
        JSONArray ja = injson.getJSONArray 'position_id'  // an array because of multi-select
        int m = ja.length()
        for (int i = 0; i < m; i++) {
            String position_id = ja.getString(i)
            if (position_id.isEmpty()) {
                ret.clear()  // signify "All"
                break
            }
            ret.add ja.getString(i)
        }
        return ret
    }

    private String getPositionNames() {
        if (position_id.isEmpty())
            return "All"
        String ret = ""
        for (String pos in position_id) {
            if (!ret.isEmpty())
                ret += ", "
            ret += (new BHRPosition(pos)).getName()
        }
        return ret
    }

    private boolean applicablePosition(String pos) {
        if (position_id.isEmpty() || pos == null || pos.isEmpty())
            return true
        for (String p in position_id)
            if (p == pos)
                return true
        return false
    }

    private void doRptAssigned() {
        int num = 0
        SendEmailGeneric em = null
        Groff rpt = null
        List<ProjectTraining> projectTrainings = new ArrayList<>()
        Set<String> requiredTrainings = new HashSet<>()

        Connection con = KissConnection.get()
        int today = DateUtils.today()

        if (!sendEmail) {
            rpt = new Groff("WAReport-", "Workers Assigned Report", true)

            // Print the project information at the top
            if (project)
                rpt.out("Project: " + project.projectName + " - " + project.description + " (" + project.address.city + ", " + project.address.state + ")")
            else
                rpt.out("Zip Code: " + zipCode)

            if (project) {
                if (firstDate != 0)
                    rpt.out("Starting date: " + DateUtils.getDateFormatted(firstDate))
                if (lastDate != 0)
                    rpt.out("Ending date: " + DateUtils.getDateFormatted(lastDate))
            }
            rpt.out("Position(s): " + getPositionNames())
            rpt.out("Availability:  " + "Assigned")
            if (training && project) {
                rpt.out("Training (* = required)")

                projectTrainings = hsu.createCriteria(ProjectTraining.class)
                        .eq(ProjectTraining.PROJECT, project.getBean())
                        .orderBy(ProjectTraining.PROJECT)
                        .list()

                for (ProjectTraining training : projectTrainings) {
                    String trainingName = training.trainingCategory.name
                    if (training.required == (char) 'Y') {
                        trainingName += "*"
                        requiredTrainings.add training.getTrainingCategory().getCatId()
                    }
                    rpt.out('\t' + trainingName)
                }
            }
            rpt.out(".SP 2")

            rpt.out(".TS H")
            rpt.out("l l l l l l l .")
            if (training)
                rpt.out "Name (* = has all required)\t\tPhone\t\tPosition\t\t" + "Project Location"
            else
                rpt.out "Name\t\tPhone\t\tPosition\t\t" + "Project Location"
            rpt.out('\\_\t\t\\_\t\t\\_\t\t\\_')
            rpt.out('.TH')
        } else {
            em = SendEmailProvider.newEmail()
            em.setHTMLMessage(emailMessage)
        }
        Command cmd = con.newCommand()

        Cursor c = con.newCommand().query("select per.lname, per.fname, per.mname, per.personal_email, p.project_id, per.person_id, add.city, add.state, add.zip  " +
                "from employee e " +
                "join person per " +
                "  on e.person_id = per.person_id " +
                "join project_employee_join pej " +
                "  on e.person_id = pej.person_id " +
                "join project_shift ps " +
                "  on pej.project_shift_id = ps.project_shift_id " +
                "join project p " +
                "  on ps.project_id = p.project_id " +
                "left join address add " +
                "  on p.address_id = add.address_id " +
                "where p.estimated_first_date <= ? and p.estimated_last_date >= ? " +
                "order by add.zip, p.project_id, per.lname, per.fname, per.mname ",
                lastDate, firstDate
        )
        while (c.isNext()) {
            Record rec = c.getRecord()
            String person_id = rec.getString("person_id")

            if (notActive(cmd, person_id))
                continue

            Record posRec = getPosition(cmd, person_id)
            if (posRec != null && !applicablePosition(posRec.getString("position_id")))
                continue
            List<String> phones = getPhoneNumbers(cmd, person_id)
            String phone = ""
            for (String p in phones) {
                if (p) {
                    if (!phone.isEmpty())
                        phone += ', '
                    phone += p
                }
            }

            String name = makeNameLFM(rec)
            String position = posRec == null ? "" : posRec.getString("position_name")

            String city, state, cs
            city = rec.getString("city")
            state = rec.getString("state")
            cs = ""
            if (city != null && !city.isEmpty())
                cs = city
            if (state != null && !state.isEmpty()) {
                if (!cs.isEmpty())
                    cs += ", "
                cs += state
            }

            boolean overlap = false
            if (sendEmail) {
                String email = rec.getString("personal_email")
                if (isTest)
                    email = "blake1024@gmail.com"
                if (email != null && email.trim().length() >= 3 && email.contains("@"))
                    em.sendEmail(email, rec.getString("fname") + " " + rec.getString("lname"), emailTitle)
            } else {
                if (training) {
                    List<HrTrainingDetail> trecs = hsu.createCriteria(HrTrainingDetail.class)
                            .eq(HrTrainingDetail.EMPLOYEE, new BEmployee(person_id).getEmployee())
                            .geOrEq(HrTrainingDetail.EXPIREDATE, today, 0)
                            .le(HrTrainingDetail.TRAININGDATE, today)
                            .orderBy(HrTrainingDetail.EXPIREDATE)
                            .list()
                    if (hasRequiredTraining(trecs, requiredTrainings))
                        rpt.out("* " + name + "\t\t" + phone + "\t\t" + position + "\t\t" + (overlap ? "* " : "") + cs)
                    else
                        rpt.out(name + "\t\t" + phone + "\t\t" + position + "\t\t" + (overlap ? "* " : "") + cs)

                    for (HrTrainingDetail trec in trecs) {
                        // Check whether the training is in project projectTrainings.
                        boolean isProjectTraining = false
                        //ProjectTraining projectTraining = null
                        for (ProjectTraining training : projectTrainings) {
                            if (training.trainingCategory.catId == trec.hrTrainingCategory.catId) {
                                isProjectTraining = true
                                //projectTraining = training
                                break
                            }
                        }

                        if (isProjectTraining) {
                            int expDate = trec.getExpireDate()
                            String catName = trec.getHrTrainingCategory().getName()
//                            if (projectTraining.required == (char) 'Y')
//                                catName += "*"

                            if (expDate > 0)
                                rpt.out "     " + catName + " (" + DateUtils.getDateFormatted(trec.getExpireDate()) + ")"
                            else
                                rpt.out "     " + catName
                        }
                    }
                } else
                    rpt.out(name + "\t\t" + phone + "\t\t" + position + "\t\t" + (overlap ? "* " : "") + cs)
            }
            num++
        }

        if (!sendEmail) {
            rpt.out(".TE")

            rpt.out(".SP 2")
            rpt.out("Total assigned workers = " + Utils.Format(num, "C", 0, 0))

            outJSON.put("url", rpt.process())
        }
    }

    private static String makeNameLFM(Record rec) {
        String r = rec.getString("lname") + ", " + rec.getString("fname")
        String mname = rec.getString("mname")
        if (mname != null && !mname.isEmpty())
            r += " " + mname
        return r
    }

    private static List<String> getPhoneNumbers(Command cmd, String person_id) {
        List<Record> recs = cmd.fetchAll("select phone_number from phone where person_join = ?", person_id)
        List<String> ret = new ArrayList<>()
        for (Record rec in recs)
            ret.add(rec.getString("phone_number"))
        return ret
    }

    private boolean notAvailable(Command cmd, String person_id) {
        if (firstDate == 0 && lastDate == 0)
            return false  // available
        int fd = firstDate
        int ld = lastDate
        if (fd == 0)
            fd = ld
        if (ld == 0)
            ld = fd
        Record rec = cmd.fetchOne("""select count(*)
                                     from employee_not_available
                                     where employee_id = ?
                                           and (? >= start_date and ? <= end_date
                                           or ? >= start_date and ? <= end_date
                                           or ? < start_date and ? > end_date)""", person_id, fd, fd, ld, ld, fd, ld)
        long count = rec.getLong "count"
        return count != 0
    }

    private static boolean notActive(Command cmd, String person_id) {
        Record rec = cmd.fetchOne("select es.active from hr_empl_status_history esh " +
                "join hr_employee_status es " +
                "  on esh.status_id = es.status_id " +
                "where esh.employee_id=? and esh.effective_date <= ? " +
                "order by esh.effective_date desc",
                person_id, DateUtils.today())
        if (rec == null)
            return true
        return rec.getString("active") == "N"
    }

    private static Record getPosition(Command cmd, String person_id) {
        return cmd.fetchOne("select pos.position_name, pos.position_id from hr_wage w " +
                "join hr_position pos " +
                "  on w.position_id = pos.position_id " +
                "where w.employee_id=? and w.effective_date <= ? " +
                "order by w.effective_date desc",
                person_id, DateUtils.today())
    }

    private void doRptAvailable(char availability) {
        boolean doNotInRadius = miles != 0 && zipCode != null && !zipCode.isEmpty()
        int num = 0
        SendEmailGeneric em = null
        Groff rpt = null
        List<ProjectTraining> projectTrainings = new ArrayList<>()
        Set<String> requiredTrainings = new HashSet<>()

        Connection con = KissConnection.get()
        Command cmd = con.newCommand()

        if (!sendEmail) {
            rpt = new Groff("WAReport-", "Worker Availability Report", true)

            // Print the project information at the top
            if (project)
                rpt.out("Project: " + project.projectName + " - " + project.description + " (" + project.address.city + ", " + project.address.state + ")")
            else
                rpt.out("Zip Code: " + zipCode)

            if (project) {
                if (firstDate != 0)
                    rpt.out("Starting date: " + DateUtils.getDateFormatted(firstDate))
                if (lastDate != 0)
                    rpt.out("Ending date: " + DateUtils.getDateFormatted(lastDate))
            }
            rpt.out("Position(s): " + getPositionNames())
            if (doNotInRadius) {
                rpt.out("Zip Code: " + zipCode)
                rpt.out("Miles: " + miles)
            }
            rpt.out("Availability:  " + (availability == 'A' as char ? "Available" : "Terminated"))
            if (training && project) {
                rpt.out("Training (* = required)")

                projectTrainings = hsu.createCriteria(ProjectTraining.class)
                        .eq(ProjectTraining.PROJECT, project.getBean())
                        .orderBy(ProjectTraining.PROJECT)
                        .list()

                for (ProjectTraining training : projectTrainings) {
                    String trainingName = training.trainingCategory.name
                    if (training.required == (char) 'Y') {
                        trainingName += "*"
                        requiredTrainings.add training.getTrainingCategory().getCatId()
                    }
                    rpt.out('\t' + trainingName)
                }
            }
            rpt.out(".SP 2")

            rpt.out(".TS H")
            rpt.out("l l l l l l l .")
            if (training)
                rpt.out "Name (* = has all required)(days worked/days since last worked)\t\tPhone\t\tPosition\t\t" + "Worker Home Location"
            else
                rpt.out "Name (days worked/days since last worked)\t\tPhone\t\tPosition\t\t" + "Worker Home Location"
            rpt.out('\\_\t\t\\_\t\t\\_\t\t\\_')
            rpt.out('.TH')
        } else {
            em = SendEmailProvider.newEmail()
            em.setHTMLMessage(emailMessage)
        }
        int today = DateUtils.today()
        HibernateScrollUtil<Employee> scr = hsu.createCriteria(Employee.class)
                .orderBy(Employee.LNAME)
                .orderBy(Employee.FNAME)
                .scroll()
        while (scr.next()) {
            Employee emp = scr.get()
            BEmployee bemp = new BEmployee(emp)

            if (bemp.isActive() != 0 && availability == 'A' as char)  // 0=active employee
                continue

            if (bemp.isActive() == 0 && availability == 'T' as char)  // 0=active employee
                continue

            if (availability == 'T' as char && 0L == calcDaysWorked((emp)))
                continue;

            if (notAvailable(cmd, emp.getPersonId()))
                continue

            if (!applicablePosition(bemp.getLastPositionId()))
                continue

            boolean ia = isAssigned(hsu, emp, firstDate, lastDate)
            if (ia)
                continue

            Set<Phone> phones = bemp.getPhones()
            String phone = ""
            int i = 0
            for (Phone p in phones) {
                String ph = p.getPhoneNumber()
                if (ph) {
                    if (!phone.isEmpty())
                        phone += ', '
                    phone += ph
                    if (++i == 2)  // limit to two phone numbers
                        break
                }
            }

            zipCode = bemp.getZip()
            String zip2
            if (zipCode != null && !zipCode.isEmpty()) {
                int idx = zipCode.indexOf('-')
                try {
                    if (idx != -1)
                        zip2 = zipCode.substring(0, idx)
                    else
                        zip2 = zipCode
                } catch (Exception ignored) {
                    zip2 = ""
                }
            } else
                zip2 = ""

            boolean milesObtained = false
            String cs = ""

            int distance = ZipCodeDistance.distance(zip1, zip2)
            if (doNotInRadius && distance > miles)
                continue
            cs = distance + (project ? " miles from project" : " miles from zip code")
            milesObtained = distance >= 0

            if (!milesObtained) {
                if (doNotInRadius)
                    continue
                String city = bemp.getCity()
                String state = bemp.getState()
                boolean hasState = state != null && !state.isEmpty()
                if (city != null && !city.isEmpty()) {
                    cs = city
                    if (hasState)
                        cs += ", "
                }
                if (hasState)
                    cs += state
            }
            num++
            if (sendEmail) {
                String email = bemp.getPersonalEmail()
                if (isTest)
                    email = "blake1024@gmail.com"
                if (email != null && email.trim().length() >= 3 && email.contains("@"))
                    em.sendEmail(email, bemp.getFirstName() + " " + bemp.getLastName(), emailTitle)
            } else {
                String suffix
                long daysWorked = calcDaysWorked emp
                int daysSinceLastWorked
                if (daysWorked > 0) {
                    daysSinceLastWorked = calcDaysSinceLastWorked emp
                    suffix = " (" + daysWorked + "/" + daysSinceLastWorked + ")"
                } else
                    suffix = " (0)"
                String name = bemp.getNameLFM()
                if (name.length() > 30)
                    name = StringUtils.take(name, 30)
                if (training) {
                    List<HrTrainingDetail> trecs = hsu.createCriteria(HrTrainingDetail.class)
                            .eq(HrTrainingDetail.EMPLOYEE, bemp.getEmployee())
                            .geOrEq(HrTrainingDetail.EXPIREDATE, today, 0)
                            .le(HrTrainingDetail.TRAININGDATE, today)
                            .orderBy(HrTrainingDetail.EXPIREDATE)
                            .list()
                    if (hasRequiredTraining(trecs, requiredTrainings))
                        rpt.out("* " + name + suffix + "\t\t" + phone + "\t\t" + bemp.getPositionName() + "\t\t" + cs)
                    else
                        rpt.out(name + suffix + "\t\t" + phone + "\t\t" + bemp.getPositionName() + "\t\t" + cs)
                    for (HrTrainingDetail trec in trecs) {
                        // Check whether the training is in project projectTrainings.
                        boolean isProjectTraining = false
                        //ProjectTraining projectTraining = null
                        for (ProjectTraining training : projectTrainings) {
                            if (training.trainingCategory.catId == trec.hrTrainingCategory.catId) {
                                isProjectTraining = true
                                //projectTraining = training
                                break
                            }
                        }

                        if (isProjectTraining) {
                            int expDate = trec.getExpireDate()
                            String catName = trec.getHrTrainingCategory().getName()

//                            if (projectTraining.required == (char) 'Y')
//                                catName += "*"

                            if (expDate > 0)
                                rpt.out "     " + catName + " (" + DateUtils.getDateFormatted(trec.getExpireDate()) + ")"
                            else
                                rpt.out "     " + catName
                        }
                    }
                } else
                    rpt.out(name + suffix + "\t\t" + phone + "\t\t" + bemp.getPositionName() + "\t\t" + cs)
            }
        }
        scr.close()

        if (!sendEmail) {
            rpt.out(".TE")

            rpt.out(".SP 2")
            rpt.out("Total available workers = " + Utils.Format(num, "C", 0, 0))

            outJSON.put("url", rpt.process())
        }
    }

    private static boolean projectsOverlap(BProject bp, List<BProject> assignedProjects) {
        int firstDate = bp.getStartDate()
        int lastDate = bp.getEndDate()
        if (firstDate == 0 || lastDate == 0)
            return false
        String project_id = bp.getProjectId()
        for (BProject bp2 : assignedProjects) {
            if (project_id == bp2.getProjectId())
                continue
            if (DateUtils.overlap(bp2.getStartDate(), bp2.getEndDate(), firstDate, lastDate))
                return true
        }
        return false
    }

    private static boolean isAssigned(HibernateSessionUtil hsu, Person pers, int firstDate, int lastDate) {
        if (firstDate == 0 && lastDate == 0)
            return false
        List<ProjectEmployeeJoin> pejl = hsu.createCriteria(ProjectEmployeeJoin.class)
                .eq(ProjectEmployeeJoin.PERSON, pers).list()
        for (ProjectEmployeeJoin pej : pejl) {
            Project proj = pej.getProjectShift().getProject()
            ProjectStatus ps = proj.getProjectStatus()
            if (ps.getActive() == (char) 'N')
                continue
            int bp = proj.getEstimatedFirstDate()
            int ep = proj.getEstimatedLastDate()
            if (DateUtils.overlap(bp, ep, firstDate, lastDate))
                return true
        }
        return false
    }

    private static boolean hasRequiredTraining(List<HrTrainingDetail> actualTraining, Set<String> requiredTraining) {
        for (String rt : requiredTraining) {
            int i = 0
            for (; i < actualTraining.size(); i++) {
                HrTrainingDetail td = actualTraining.get i
                if (td.getHrTrainingCategory().getCatId() == rt)
                    break  //  found
            }
            if (i == actualTraining.size())
                return false  //  not found
        }
        return true  //  all found
    }

    private long calcDaysWorked(Employee emp) {
        Connection con = KissConnection.get()
        Record rec = con.fetchOne("select count(*) from " +
                "(select count(*) from timesheet where person_id=? and billable='Y' group by end_date) ts",
                emp.getPersonId())
        return rec.getLong('count')
    }

    private int calcDaysSinceLastWorked(Employee emp) {
        Connection con = KissConnection.get()
        Record rec = con.fetchOne("select end_date from timesheet where billable='Y' and person_id=? order by end_date desc",
                emp.getPersonId())
        if (rec == null)
            return 0
        int end_date = rec.getInt('end_date')
        return DateUtils.getDaysBetween(DateUtils.today(), end_date)
    }
}


