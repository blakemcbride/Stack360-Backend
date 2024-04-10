
-- PostgreSQL patch from revision 4450 to revision 4451


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "applicant" ADD COLUMN "travel_personal" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "applicant" ADD COLUMN "travel_friend" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "applicant" ADD COLUMN "travel_public" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "applicant" ADD COLUMN "travel_unknown" character(1) DEFAULT 'N' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE "applicant" DROP COLUMN "travel_method";


--  Add new indexes and checks

ALTER TABLE "applicant" ADD CONSTRAINT "applacant_travel_unknown_chk" CHECK (((travel_unknown='Y')OR(travel_unknown='N')));

ALTER TABLE "applicant" ADD CONSTRAINT "applicant_travel_friend_chk" CHECK (((travel_friend='Y')OR(travel_friend='N')));

ALTER TABLE "applicant" ADD CONSTRAINT "applicant_travel_personal_chk" CHECK (((travel_personal='Y')OR(travel_personal='N')));

ALTER TABLE "applicant" ADD CONSTRAINT "applicant_travel_public_chk" CHECK (((travel_public='Y')OR(travel_public='N')));

