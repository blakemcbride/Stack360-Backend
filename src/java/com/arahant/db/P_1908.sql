
-- PostgreSQL patch from revision 1907 to revision 1908



--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "client" ADD COLUMN "copy_only_external" character(1) DEFAULT 'Y' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks



-- ----------------------------------------------------------
