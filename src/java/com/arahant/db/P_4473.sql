
-- PostgreSQL patch from revision 4472 to revision 4473


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "education" ADD COLUMN "gpa" smallint;

ALTER TABLE "education" ADD COLUMN "current" character(1);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "education" ADD CONSTRAINT "education_current_chk" CHECK (((current='Y')OR(current='N')));

COMMENT ON COLUMN "education"."current" IS 'Are they still at this school?';


