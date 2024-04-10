
-- PostgreSQL patch from revision 3166 to revision 3167


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "employee_not_available" (
	"employee_not_available_id" character(16) NOT NULL,
	"employee_id" character(16) NOT NULL,
	"start_date" integer NOT NULL,
	"end_date" integer NOT NULL,
	"reason" character varying(128) NOT NULL,
	CONSTRAINT "ena_date_chk" CHECK (((start_date>20200101)AND(start_date<21500101)AND(end_date>20200101)AND(end_date<21500101)))
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "employee_not_available" ADD CONSTRAINT "employee_not_available_pkey" PRIMARY KEY ("employee_not_available_id");

CREATE UNIQUE INDEX "ena_employee_idx" ON "employee_not_available" USING btree ("employee_id", "start_date");

COMMENT ON TABLE "employee_not_available" IS 'Date ranges a worker isn''t available to work';

ALTER TABLE ONLY "employee_not_available" ADD CONSTRAINT "ena_employee_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");


