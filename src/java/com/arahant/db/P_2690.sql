
-- PostgreSQL patch from revision 2689 to revision 2690

ALTER TABLE "hr_employee_event" DROP CONSTRAINT "hr_emp_evt_supervisor_fkey";


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "hr_employee_event" ADD CONSTRAINT "hr_employee_event_supervisor_fkey" FOREIGN KEY ("supervisor_id") REFERENCES "person" ("person_id");




