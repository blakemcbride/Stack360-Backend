
-- PostgreSQL patch from revision 4001 to revision 4002


--  Remove indexes and checks

DROP INDEX "fki_project_comment_project_id_fkey";

DROP INDEX "project_comment_main_idx";

ALTER TABLE "project_comment" DROP CONSTRAINT "project_comment_project_id_fkey";


--  Add new tables


--  Add new columns


--  Change existing columns

ALTER TABLE "project_comment" ALTER COLUMN "project_shift_id" SET NOT NULL;

--  Remove tables


--  Drop columns

ALTER TABLE "project_comment" DROP COLUMN "project_id";


--  Add new indexes and checks


