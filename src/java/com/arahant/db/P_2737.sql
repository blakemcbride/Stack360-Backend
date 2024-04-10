
-- PostgreSQL patch from revision 2736 to revision 2737



--  Remove indexes and checks

DROP INDEX "fki_project_who_added_fk";


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE "project" DROP COLUMN "who_added";

ALTER TABLE "project" DROP COLUMN "when_added";


--  Add new indexes and checks

