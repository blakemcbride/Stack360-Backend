
-- PostgreSQL patch from revision 3990 to revision 3991


--  Remove indexes and checks

ALTER TABLE "project_form" DROP CONSTRAINT "project_form_project_fkey";

DROP INDEX "fki_project_form_project_fkey";


--  Add new tables


--  Add new columns


--  Change existing columns

DROP INDEX "fki_project_form_shift_fky";

ALTER TABLE "project_form" ALTER COLUMN "project_shift_id" SET NOT NULL;

--  Remove tables


--  Drop columns

ALTER TABLE "project_form" DROP COLUMN "project_id";


--  Add new indexes and checks

CREATE INDEX "fki_project_form_shift_fky" ON "project_form" USING btree ("project_shift_id");


