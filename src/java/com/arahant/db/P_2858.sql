
-- PostgreSQL patch from revision 2857 to revision 2858


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project" ADD COLUMN "location_description" character varying(14);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "project"."location_description" IS 'Used to build the description column';

