
-- PostgreSQL patch from revision 3775 to revision 3776



--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project" ADD COLUMN "last_report_date" timestamp without time zone;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "project"."last_report_date" IS 'Date/time project report last sent';

