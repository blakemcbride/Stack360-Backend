
-- PostgreSQL patch from revision 4531 to revision 4532


--  Remove indexes and checks

ALTER TABLE applicant_application DROP CONSTRAINT applicant_application_applicant_source_fkey;

DROP INDEX "fki_applicant_application_applicant_source_fkey";


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE "applicant_application" DROP COLUMN "applicant_source_id";


--  Add new indexes and checks

