
-- PostgreSQL patch from revision 3962 to revision 3963


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "project_shift" (
	"project_shift_id" character(16) NOT NULL,
	"project_id" character(16) NOT NULL,
	"required_workers" smallint DEFAULT 0 NOT NULL,
	"shift_start" character varying(10),
	"description" character varying(60)
);


--  Add new columns

ALTER TABLE "project_employee_join" ADD COLUMN "project_shift_id" character(16);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_employee_join_schedule_fky" ON "project_employee_join" USING btree ("project_shift_id");

CREATE INDEX "fki_project_shift_project_fkey" ON "project_shift" USING btree ("project_id");

ALTER TABLE ONLY "project_shift" ADD CONSTRAINT "project_shift_pkey" PRIMARY KEY ("project_shift_id");

ALTER TABLE ONLY "project_employee_join" ADD CONSTRAINT "project_employee_join_schedule_fky" FOREIGN KEY ("project_shift_id") REFERENCES "project_shift" ("project_shift_id");

ALTER TABLE ONLY "project_shift" ADD CONSTRAINT "project_shift_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");


