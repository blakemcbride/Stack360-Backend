
-- PostgreSQL patch from revision 3438 to revision 3439

--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project_employee_join" ADD COLUMN "confirmed_date" timestamp with time zone;

ALTER TABLE "project_employee_join" ADD COLUMN "confirmed_person_id" character(16);

ALTER TABLE "project_employee_join" ADD COLUMN "verified_date" timestamp with time zone;

ALTER TABLE "project_employee_join" ADD COLUMN "verified_person_id" character(16);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_emp_join_confper_fkey" ON "project_employee_join" USING btree ("confirmed_person_id");

CREATE INDEX "fki_project_emp_join_verifper_fkey" ON "project_employee_join" USING btree ("verified_person_id");

COMMENT ON COLUMN "project_employee_join"."confirmed_date" IS 'This is prior to the beginning of the project';

COMMENT ON COLUMN "project_employee_join"."confirmed_person_id" IS 'Person who confirmed the worker';

COMMENT ON COLUMN "project_employee_join"."verified_date" IS 'Worker verified at site';

COMMENT ON COLUMN "project_employee_join"."verified_person_id" IS 'Person who verified the worker on site';

ALTER TABLE ONLY "project_employee_join" ADD CONSTRAINT "project_emp_join_confper_fkey" FOREIGN KEY ("confirmed_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_employee_join" ADD CONSTRAINT "project_emp_join_verifper_fkey" FOREIGN KEY ("verified_person_id") REFERENCES "person" ("person_id");


