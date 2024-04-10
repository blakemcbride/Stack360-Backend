
-- PostgreSQL patch from revision 4853 to revision 4854


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "form_type" ADD COLUMN "internal" character(1) DEFAULT 'N' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "form_type" ADD CONSTRAINT "form_type_internal_chk" CHECK (((internal='Y')OR(internal='N')));

COMMENT ON COLUMN "form_type"."internal" IS 'Restrict for internal use?  Y/N';

