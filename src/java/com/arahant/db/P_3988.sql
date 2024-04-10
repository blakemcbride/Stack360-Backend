
-- PostgreSQL patch from revision 3987 to revision 3988


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project_form" ADD COLUMN "project_shift_id" character varying(16);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_form_shift_fky" ON "project_form" USING btree ("project_shift_id");

ALTER TABLE ONLY "project_form" ADD CONSTRAINT "project_form_shift_fky" FOREIGN KEY ("project_shift_id") REFERENCES "project_shift" ("project_shift_id");

