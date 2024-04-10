
-- PostgreSQL patch from revision 4455 to revision 4456


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "applicant" ADD COLUMN "agrees" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "applicant" ADD COLUMN "agreement_name" character varying(50);

ALTER TABLE "applicant" ADD COLUMN "agreement_date" timestamp with time zone;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "applicant" ADD CONSTRAINT "applicant_agrees_chk" CHECK (((agrees='Y')OR(agrees='N')));

ALTER TABLE "applicant" DROP CONSTRAINT "applicant_travel_friend_chk";

ALTER TABLE "applicant" ADD CONSTRAINT "applicant_travel_friend_chk" CHECK (((travel_friend='Y')OR(travel_friend='N')));


