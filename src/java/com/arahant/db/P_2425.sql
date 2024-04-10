
-- PostgreSQL patch from revision 2424 to revision 2425



--  Remove indexes and checks


--  Add new tables

CREATE TABLE "project_employee_history" (
	"project_employee_history_id" character(16) NOT NULL,
	"person_id" character(16) NOT NULL,
	"project_id" character(16) NOT NULL,
	"change_person_id" character(16) NOT NULL,
	"change_date" integer NOT NULL,
	"change_time" integer NOT NULL,
	"change_type" character(1) NOT NULL,
	CONSTRAINT "project_employee_hist_type_chk" CHECK (((change_type='A')OR(change_type='D')))
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_employee_hist_cpers_fkey" ON "project_employee_history" USING btree ("change_person_id");

CREATE INDEX "fki_project_employee_hist_per_fky" ON "project_employee_history" USING btree ("person_id");

CREATE INDEX "fki_project_employee_hist_proj_fkey" ON "project_employee_history" USING btree ("project_id");

ALTER TABLE ONLY "project_employee_history" ADD CONSTRAINT "project_employee_history_id_pk" PRIMARY KEY ("project_employee_history_id");

COMMENT ON COLUMN "project_employee_history"."change_date" IS 'Date the change occurred YYYYMMDD';

COMMENT ON COLUMN "project_employee_history"."change_person_id" IS 'The person who made the change';

COMMENT ON COLUMN "project_employee_history"."change_time" IS 'Time the change occurred HHMM';

COMMENT ON COLUMN "project_employee_history"."change_type" IS '(A)dd or (D)elete';

ALTER TABLE ONLY "project_employee_history" ADD CONSTRAINT "project_employee_hist_cpers_fkey" FOREIGN KEY ("change_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_employee_history" ADD CONSTRAINT "project_employee_hist_per_fky" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_employee_history" ADD CONSTRAINT "project_employee_hist_proj_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");


