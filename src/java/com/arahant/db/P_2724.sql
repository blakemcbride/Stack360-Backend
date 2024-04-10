
-- PostgreSQL patch from revision 2723 to revision 2724

--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project" ADD COLUMN "store_number" character varying(8);

ALTER TABLE "project" ADD COLUMN "shift_start" character varying(3);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

