
-- PostgreSQL patch from revision 5090 to revision 5091


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project_task_picture" ADD COLUMN "picture_number" smallint DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

