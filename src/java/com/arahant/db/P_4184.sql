
-- PostgreSQL patch from revision 4183 to revision 4184


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "person" ADD COLUMN "linkedin" character varying(80);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "person"."linkedin" IS 'URL to their LinkedIn listing';

