
-- PostgreSQL patch from revision 3966 to revision 3967


--  Remove indexes and checks

DROP INDEX "fki_pej_project_fkey";

DROP INDEX "fki_project_employee_join_schedule_fky";

ALTER TABLE ONLY "project_employee_join" DROP CONSTRAINT "pej_nodup_emp";


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE "project" DROP COLUMN "required_workers";

ALTER TABLE "project" DROP COLUMN "shift_start";

ALTER TABLE "project_employee_join" DROP COLUMN "project_id";


--  Add new indexes and checks

CREATE INDEX "fki_project_employee_join_schedule_fkey" ON "project_employee_join" USING btree ("project_shift_id");

ALTER TABLE ONLY "project_employee_join" ADD CONSTRAINT "project_employee_join_schedule_fkey" FOREIGN KEY ("project_shift_id") REFERENCES "project_shift" ("project_shift_id");
