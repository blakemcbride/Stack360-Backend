
-- PostgreSQL patch from revision 4667 to revision 4668


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "hr_position" ADD COLUMN "applicant_default" character(1) DEFAULT 'N' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "hr_position" ADD CONSTRAINT "hr_position_default_chk" CHECK (((applicant_default='Y')OR(applicant_default='N')));

COMMENT ON COLUMN "hr_position"."applicant_default" IS 'Y/N
Y if default position for applicant system
';


