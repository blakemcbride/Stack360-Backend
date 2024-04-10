
-- PostgreSQL patch from revision 4529 to revision 4531


--  Remove indexes and checks

DROP INDEX "fki_application_app_job_type_fkey";

DROP INDEX "fki_job_type_comp";

ALTER TABLE applicant_position DROP CONSTRAINT applicant_position_job_type_fkey;

DROP INDEX fki_applicant_position_job_type_fkey;


--  Add new tables


--  Add new columns

ALTER TABLE "applicant_question" ADD COLUMN "position_id" character(16);

ALTER TABLE applicant_position ADD COLUMN "position_id" character(16);


--  Change existing columns

--  Drop columns

ALTER TABLE "applicant_application" DROP COLUMN "job_type_id";

ALTER TABLE "applicant_question" DROP COLUMN "job_type_id";

ALTER TABLE "applicant_position" DROP COLUMN "job_type_id";

ALTER TABLE "applicant_application" DROP COLUMN "title";



--  Remove tables

DROP TABLE "job_type";


--  Add new indexes and checks

CREATE INDEX "fki_applicant_position_position_fky" ON "applicant_position" USING btree ("position_id");

ALTER TABLE ONLY "applicant_position" ADD CONSTRAINT "applicant_position_position_fky" FOREIGN KEY ("position_id") REFERENCES "hr_position" ("position_id");

CREATE UNIQUE INDEX "applicant_question_position_idx" ON "applicant_question" USING btree ("position_id", "question_order");

ALTER TABLE ONLY "applicant_question" ADD CONSTRAINT "applicant_question_position_fkey" FOREIGN KEY ("position_id") REFERENCES "hr_position" ("position_id");

ALTER TABLE applicant_position RENAME "name" TO job_title;


update applicant_position set position_id = '00001-0000000011';  -- for Way To Go
ALTER TABLE applicant_position ALTER COLUMN position_id SET NOT NULL;

COMMENT ON TABLE public.applicant_position
    IS 'This table holds the open jobs that people can apply for.';
