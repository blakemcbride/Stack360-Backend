
-- PostgreSQL patch from revision 995 to revision 996

--  Remove indexes and checks


--  Add new tables

CREATE TABLE "employee_rate" (
	"employee_rate_id" character(16) NOT NULL,
	"person_id" character(16) NOT NULL,
	"rate_type_id" character(16) NOT NULL,
	"rate" double precision DEFAULT 0.0 NOT NULL
);

CREATE TABLE "rate_type" (
	"rate_type_id" character(16) NOT NULL,
	"description" character varying(40) NOT NULL
);
-- insert into rate_type (rate_type_id, description) values ('00001-0000000001', 'Default Billing Rate');


--  Add new columns

ALTER TABLE "timesheet" ADD COLUMN "fixed_pay" double precision DEFAULT 0.0 NOT NULL;

ALTER TABLE "project" ADD COLUMN "rate_type_id" character(16);
update project set rate_type_id='00001-0000000001';
alter table project alter column rate_type_id set not null;

--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "employee_rate" ADD CONSTRAINT "employee_rate_pkey" PRIMARY KEY ("employee_rate_id");

CREATE INDEX "fki_emp_rate_ratetyp_fkey" ON "employee_rate" USING btree ("rate_type_id");

CREATE INDEX "fki_employee_rate_person_fkey" ON "employee_rate" USING btree ("person_id");

CREATE INDEX "fki_project_rate_type_fkey" ON "project" USING btree ("rate_type_id");

ALTER TABLE ONLY "rate_type" ADD CONSTRAINT "rate_type_pkey" PRIMARY KEY ("rate_type_id");

COMMENT ON COLUMN "project"."rate_type_id" IS 'Rate type associated with this project';

COMMENT ON COLUMN "timesheet"."fixed_pay" IS 'Fixed payment amount for a week regardless of hours (what was agreed).  Used in weekly timekeeping.';

COMMENT ON TABLE "employee_rate" IS 'Billing rates associated with an employee';



