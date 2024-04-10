
-- PostgreSQL patch from revision 1295 to revision 1296


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "form_type" ADD COLUMN "field_downloadable" character(1) DEFAULT 'N' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "form_type" ADD CONSTRAINT "form_type_downloadable_chk" CHECK (((field_downloadable='Y')OR(field_downloadable='N')));

COMMENT ON COLUMN "form_type"."field_downloadable" IS 'Yes / No';


