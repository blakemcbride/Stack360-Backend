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


import org.kissweb.database.*;

static void main(String [] args) {
    final Connection demoDB = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "demo", "postgres", "postgres")
    final Connection stack360DB = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "stack360", "postgres", "postgres")
    Record rec

    Record ogrec = stack360DB.newRecord("org_group")
    ogrec.set("org_group_id", "00000-0000000000")
    ogrec.set("group_name", "Your Company Name") // This is fine here.  It gets updated via system screens later.
    ogrec.set("org_group_type", 1)
    ogrec.addRecord()

    rec = stack360DB.newRecord("company_base")
    rec.set("org_group_id", "00000-0000000000")
    rec.set("org_group_type", 1)
    rec.addRecord()

    ogrec.set("owning_entity_id", "00000-0000000000")
    ogrec.update()

    rec = stack360DB.newRecord("company_detail")
    rec.set("org_group_id", "00000-0000000000")
    rec.set("accounting_basis", "C")
    rec.addRecord()

    rec = stack360DB.newRecord("person")
    rec.set("person_id", "00000-0000000000")
    rec.setDateTime("record_change_date", new Date())
    rec.set("record_change_type", "N")
    rec.set("record_person_id", "00000-0000000000")
    rec.set("org_group_type", 1)
    rec.set("company_id", "00000-0000000000")
    rec.set("fname", "Stack360")
    rec.set("lname", "Administrator")
    rec.set("job_title", "System Administrator")
    rec.addRecord()

    copyScreens(demoDB, stack360DB)
    copyScreenGroup(demoDB, stack360DB)

    rec = stack360DB.newRecord("prophet_login")
    rec.set("person_id", "00000-0000000000")
    rec.set("can_login", "Y")
    rec.set("user_login", "stack360")
    rec.set("user_password", "stack360")
    rec.set("screen_group_id", "00000-0000000000")

    rec.addRecord()

    stack360DB.commit()


    demoDB.close()
    stack360DB.commit()
    stack360DB.close()
}

static void copyScreens(Connection demoDB, Connection stack360DB) {
    int n = 0
    Command fcmd = demoDB.newCommand()
    Cursor c = fcmd.query("select * from screen")
    while (c.isNext()) {
        Record frec = c.getRecord()
        Record trec = stack360DB.newRecord("screen")
        trec.copyCorresponding(frec)
        trec.addRecord()
        if (++n % 50 == 0)
            stack360DB.commit()
    }
    stack360DB.commit()
    c.close()
    fcmd.close()
}

static void copyScreenGroup(Connection demoDB, Connection stack360DB) {
    System.out = new PrintStream(new BufferedOutputStream(System.out), true)  // don't buffer output
    copyScreenGroupHierarchy(demoDB, stack360DB, "00000-0000000000")
}

static void copyScreenGroupHierarchy(Connection demoDB, Connection stack360DB, String screenGroupId) {

    if (stack360DB.exists("select * from screen_group where screen_group_id = ?", screenGroupId))
        return

    Record frec = demoDB.fetchOne("select * from screen_group where screen_group_id = ?", screenGroupId)
    Record trec = stack360DB.newRecord("screen_group")
    trec.copyCorresponding(frec)
    trec.set("technology", "H")
    println "Creating screen_group " + trec.getString("screen_group_id")
    trec.addRecord()

    List<Record> recs = demoDB.fetchAll("select * from screen_group_hierarchy where parent_screen_group_id = ?", screenGroupId)
    for (Record rec : recs) {
        Record srec = stack360DB.newRecord("screen_group_hierarchy")
        srec.copyCorresponding(rec)
        srec.set("child_screen_group_id", null)
        println "  Creating screen_group_hierarchy " + srec.getString("screen_group_hierarchy_id") + " (parent_screen_group_id = " + srec.getString("parent_screen_group_id") + ")"
        srec.addRecord()
        String childScreenGroupId = rec.getString("child_screen_group_id")
        if (childScreenGroupId != null) {
            copyScreenGroupHierarchy(demoDB, stack360DB, childScreenGroupId)
            srec.set("child_screen_group_id", childScreenGroupId)
            println("    changing child_screen_group_id to " + childScreenGroupId)
            srec.update()
        }
    }
    stack360DB.commit()
}
