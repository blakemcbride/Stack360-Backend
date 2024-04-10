
-- PostgreSQL patch from revision 4156 to revision 4157


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project" ADD COLUMN "project_days" smallint DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "project"."project_days" IS 'Numer of work days in project';


