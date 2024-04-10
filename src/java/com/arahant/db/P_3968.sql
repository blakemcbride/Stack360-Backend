
-- PostgreSQL patch from revision 3967 to revision 3968


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project_employee_history" ADD COLUMN "project_shift_id" character(16);

ALTER TABLE "timesheet" ADD COLUMN "project_shift_id" character(16);

ALTER TABLE "worker_confirmation" ADD COLUMN "project_shift_id" character(16);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_employee_hist_sch_fkey" ON "project_employee_history" USING btree ("project_shift_id");

CREATE INDEX "fki_timesheet_project_shift_fkey" ON "timesheet" USING btree ("project_shift_id");

CREATE INDEX "fki_worker_confirmation_sch_fkey" ON "worker_confirmation" USING btree ("project_shift_id");

ALTER TABLE ONLY "project_employee_history" ADD CONSTRAINT "project_employee_hist_sch_fkey" FOREIGN KEY ("project_shift_id") REFERENCES "project_shift" ("project_shift_id");

ALTER TABLE ONLY "timesheet" ADD CONSTRAINT "timesheet_project_shift_fkey" FOREIGN KEY ("project_shift_id") REFERENCES "project_shift" ("project_shift_id");

ALTER TABLE ONLY "worker_confirmation" ADD CONSTRAINT "worker_confirmation_sch_fkey" FOREIGN KEY ("project_shift_id") REFERENCES "project_shift" ("project_shift_id");


