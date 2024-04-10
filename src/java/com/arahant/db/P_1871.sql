
-- PostgreSQL patch from revision 1870 to revision 1871


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "client" ADD COLUMN "copy_pictures_to_disk" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "client" ADD COLUMN "picture_disk_path" character varying(100);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "client" ADD CONSTRAINT "copy_pictures_check" CHECK (((copy_pictures_to_disk='Y')OR(copy_pictures_to_disk='N')));



-- ----------------------------------------------------------
