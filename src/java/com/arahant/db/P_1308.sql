
-- PostgreSQL patch from revision 1307 to revision 1308


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project" ADD COLUMN "project_state" character(2);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "project"."project_state" IS 'State project is in (e.g. FL, GA, NY, etc.)';

