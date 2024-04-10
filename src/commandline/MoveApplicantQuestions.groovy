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

import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor


class MoveApplicantQuestions {

    static void main(String[] args) {
        Connection db = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "waytogo", "postgres", "postgres")
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select * from applicant")
        while (c.isNext()) {
            Record rec = c.getRecord()

        }

        db.commit()
    }

}