
-- PostgreSQL patch from revision 2795 to revision 2796

--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project_employee_join" ADD COLUMN "start_date" integer DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "project_employee_join"."start_date" IS 'Date worker is to start project.  Zero means on the start date of the project.';


