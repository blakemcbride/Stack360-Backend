
-- PostgreSQL patch from revision 4397 to revision 4398


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project_shift" ADD COLUMN "default_date" integer DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "project_shift"."default_date" IS 'This is the default used by the mobile app.  It gets set back to zero when the day is complete.  If zero, the current date is used by the mobile app.';

