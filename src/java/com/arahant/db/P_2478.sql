
-- PostgreSQL patch from revision 2477 to revision 2478

--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "hr_position" ADD COLUMN "weekly_per_diem" real DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks



