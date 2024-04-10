
-- PostgreSQL patch from revision 3955 to revision 3959


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "worker_confirmation" ADD COLUMN "distance" integer DEFAULT -1 NOT NULL;

ALTER TABLE "worker_confirmation" ADD COLUMN "location" character varying(60);

ALTER TABLE "worker_confirmation" ADD COLUMN "minutes" integer DEFAULT -1 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "worker_confirmation"."distance" IS 'Miles between current location and the project
-1 means unknown';

COMMENT ON COLUMN "worker_confirmation"."location" IS 'City and state name';

COMMENT ON COLUMN "worker_confirmation"."minutes" IS 'Remaining minutes of travel';

