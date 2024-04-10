
-- PostgreSQL patch from revision 1096 to revision 1097

--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project_form" ADD COLUMN "internal" character(1) DEFAULT 'Y' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "project_form" ADD CONSTRAINT "project_form_internal_chk" CHECK (((internal='Y')OR(internal='N')));

COMMENT ON COLUMN "project_form"."internal" IS 'Is this form for internal use? (Y/N)';


