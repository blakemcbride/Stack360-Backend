
-- PostgreSQL patch from revision 2762 to revision 2763


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project" ADD COLUMN "project_subtype_id" character(16);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_subtype_fkey" ON "project" USING btree ("project_subtype_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_subtype_fkey" FOREIGN KEY ("project_subtype_id") REFERENCES "project_subtype" ("project_subtype_id");

