
-- PostgreSQL patch from revision 5201 to revision 5202


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "expense" ADD COLUMN "hotel_amount" real DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks


