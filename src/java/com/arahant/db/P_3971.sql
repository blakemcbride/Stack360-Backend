
-- PostgreSQL patch from revision 3969 to revision 3971


--  Remove indexes and checks

DROP INDEX "fki_project_employee_hist_proj_fkey";

DROP INDEX "fki_timesheet_project_id_fkey";

DROP INDEX "fki_timesheet_project_shift_fkey";

DROP INDEX "timesheet_project_date_idx";

DROP INDEX "worker_confirmation_project_idx";


--  Add new tables


--  Add new columns


--  Change existing columns

DROP INDEX "fki_project_employee_hist_sch_fkey";

DROP INDEX "fki_project_employee_join_schedule_fkey";

DROP INDEX "fki_worker_confirmation_sch_fkey";

ALTER TABLE "project_employee_history" ALTER COLUMN "project_shift_id" SET NOT NULL;
ALTER TABLE "project_employee_join" ALTER COLUMN "project_shift_id" SET NOT NULL;
ALTER TABLE "timesheet" ALTER COLUMN "project_shift_id" SET NOT NULL;
ALTER TABLE "worker_confirmation" ALTER COLUMN "project_shift_id" SET NOT NULL;

--  Remove tables


--  Drop columns

ALTER TABLE "project_employee_history" DROP COLUMN "project_id";

ALTER TABLE "timesheet" DROP COLUMN "project_id";

ALTER TABLE "worker_confirmation" DROP COLUMN "project_id";


--  Add new indexes and checks

CREATE INDEX "fki_project_employee_hist_sch_fkey" ON "project_employee_history" USING btree ("project_shift_id");

CREATE INDEX "fki_project_employee_join_schedule_fkey" ON "project_employee_join" USING btree ("project_shift_id");

CREATE INDEX "fki_worker_confirmation_sch_fkey" ON "worker_confirmation" USING btree ("project_shift_id");

CREATE INDEX "timesheet_schedule_index" ON "timesheet" USING btree ("project_shift_id", "beginning_date", "beginning_time");

