
-- PostgreSQL patch from revision 4276 to revision 4277

--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "time_type" ADD COLUMN "default_billable" character(1) DEFAULT 'U' NOT NULL;

ALTER TABLE "time_type" ADD COLUMN "default_type" character(1) DEFAULT 'N' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "time_type" ADD CONSTRAINT "time_type_billable_chk" CHECK (((default_billable='Y')OR(default_billable='N')OR(default_billable='U')));

ALTER TABLE "time_type" ADD CONSTRAINT "time_type_type_chk" CHECK (((default_type='Y')OR(default_type='N')));

COMMENT ON COLUMN "time_type"."default_billable" IS 'Unknown, Yes, No';

COMMENT ON COLUMN "time_type"."default_type" IS 'Yes, No';

