
-- PostgreSQL patch from revision 1417 to revision 1418


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project" ADD COLUMN "estimated_first_date" integer DEFAULT 0 NOT NULL;

ALTER TABLE "project" ADD COLUMN "estimated_last_date" integer DEFAULT 0 NOT NULL;

ALTER TABLE "project" ADD COLUMN "address_id" character(16);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_address_fkey" ON "project" USING btree ("address_id");

COMMENT ON COLUMN "project"."address_id" IS 'Project site address';

COMMENT ON COLUMN "project"."estimated_first_date" IS 'Estimated start date of project';

COMMENT ON COLUMN "project"."estimated_last_date" IS 'Estimated last date of project';

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_address_fkey" FOREIGN KEY ("address_id") REFERENCES "address" ("address_id");

