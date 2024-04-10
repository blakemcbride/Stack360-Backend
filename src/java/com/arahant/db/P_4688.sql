
-- PostgreSQL patch from revision 4687 to revision 4688


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "applicant_app_status" ADD COLUMN "phase" smallint DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "applicant_app_status"."phase" IS '0 = no application
1 = application made
2 = offer extended
3 = offer accepted
4 = hired
5 = offer rejected
6 = offer retracted';


