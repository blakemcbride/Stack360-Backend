
-- PostgreSQL patch from revision 4686 to revision 4687


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "applicant_application" ADD COLUMN "phase" smallint DEFAULT 0 NOT NULL;

ALTER TABLE "applicant_application" ADD COLUMN "offer_declined_date" timestamp with time zone;

ALTER TABLE "applicant_application" ADD COLUMN "offer_retracted_date" timestamp with time zone;

ALTER TABLE "applicant_application" ADD COLUMN "offer_last_viewed_date" timestamp with time zone;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "applicant_application"."phase" IS '0 = no application
1 = application made
2 = offer extended
3 = offer accepted
4 = hired
5 = offer rejected
6 = offer retracted';

COMMENT ON COLUMN "applicant_application"."offer_last_viewed_date" IS 'This is when the offer was viewed through the applicant interface.  We are not tracking if they viewed an offer made via email.';


