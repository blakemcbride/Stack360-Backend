
-- PostgreSQL patch from revision 4526 to revision 4527


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "applicant_application" ADD COLUMN "offer_first_generated" timestamp with time zone;

ALTER TABLE "applicant_application" ADD COLUMN "offer_last_generated" timestamp with time zone;

ALTER TABLE "applicant_application" ADD COLUMN "offer_first_emailed" timestamp with time zone;

ALTER TABLE "applicant_application" ADD COLUMN "offer_last_emailed" timestamp with time zone;

ALTER TABLE "applicant_application" ADD COLUMN "offer_elec_signed_date" timestamp with time zone;

ALTER TABLE "applicant_application" ADD COLUMN "offer_elec_signed_ip" character varying(15);

ALTER TABLE "applicant_application" ADD COLUMN "title" character varying(50);

ALTER TABLE "applicant_application" ADD COLUMN "pay_rate" real;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks


