
-- PostgreSQL patch from revision 4473 to revision 4474


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "applicant" ADD COLUMN "background_check_authorized" character(1) DEFAULT 'N' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "applicant" ADD CONSTRAINT "applicant_background_chk" CHECK (((background_check_authorized='Y')OR(background_check_authorized='N')));

COMMENT ON COLUMN "applicant"."background_check_authorized" IS 'Did the applicant authorize a background check?';


