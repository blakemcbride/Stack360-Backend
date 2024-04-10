
-- PostgreSQL patch from revision 2004 to revision 2005

--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns

ALTER TABLE "client" ALTER COLUMN "vendor_number" TYPE character varying(20);

--  Remove tables


--  Drop columns


--  Add new indexes and checks


