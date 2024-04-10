
-- PostgreSQL patch from revision 2232 to revision 2233


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns

ALTER TABLE "address" ALTER COLUMN "county" TYPE character varying(30);

--  Remove tables


--  Drop columns


--  Add new indexes and checks


