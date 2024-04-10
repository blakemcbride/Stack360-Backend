
-- PostgreSQL patch from revision 3569 to revision 3570



--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "message" ADD COLUMN "from_name" character varying(50);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks


