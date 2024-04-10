
-- PostgreSQL patch from revision 1916 to revision 1918


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "client" ADD COLUMN "copy_inactive_projects" character(1) DEFAULT 'Y' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "client" ADD CONSTRAINT "client_copy_external_chk" CHECK (((copy_only_external='Y')OR(copy_only_external='N')));

ALTER TABLE "client" ADD CONSTRAINT "client_copy_inactive_chk" CHECK (((copy_inactive_projects='Y')OR(copy_inactive_projects='N')));


