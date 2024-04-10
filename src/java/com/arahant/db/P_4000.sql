
-- PostgreSQL patch from revision 3999 to revision 4000


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project_comment" ADD COLUMN "project_shift_id" character(16);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_comment_shift_fkey" ON "project_comment" USING btree ("project_shift_id");

ALTER TABLE ONLY "project_comment" ADD CONSTRAINT "project_comment_shift_fkey" FOREIGN KEY ("project_shift_id") REFERENCES "project_shift" ("project_shift_id");


