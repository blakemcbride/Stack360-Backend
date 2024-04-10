
-- PostgreSQL patch from revision 4573 to revision 4574


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "applicant_application" ADD COLUMN "position_id" character(16);


--  Change existing columns

ALTER TABLE "applicant_position" ALTER COLUMN "position_id" SET NOT NULL;

--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_applicant_application_position_fky" ON "applicant_application" USING btree ("position_id");

COMMENT ON COLUMN "applicant_application"."position_id" IS 'Irrespective of the job they have applied to, this field indicates what we are sloting them for.';

COMMENT ON COLUMN "applicant_position"."position_id" IS 'This is the position the ad is for.';

-- Correct pre-existing records

update applicant_application set position_id = (select position_id from applicant_position limit 1);

ALTER TABLE "applicant_application" ALTER COLUMN "position_id" SET NOT NULL;
