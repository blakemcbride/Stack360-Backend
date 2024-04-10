
-- PostgreSQL patch from revision 4447 to revision 4448


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "applicant" ADD COLUMN "referred_by" character varying(40);

ALTER TABLE "applicant" ADD COLUMN "years_experience" smallint;

ALTER TABLE "applicant" ADD COLUMN "travel_method" character(1);

ALTER TABLE "applicant" ADD COLUMN "day_shift" character(1);

ALTER TABLE "applicant" ADD COLUMN "night_shift" character(1);

ALTER TABLE "applicant" ADD COLUMN "veteran" character(1);

ALTER TABLE "applicant" ADD COLUMN "gender" character(1);

ALTER TABLE "applicant" ADD COLUMN "signature" character varying(40);

ALTER TABLE "applicant" ADD COLUMN "when_signed" timestamp with time zone;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

