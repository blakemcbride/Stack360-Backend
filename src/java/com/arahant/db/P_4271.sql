
-- PostgreSQL patch from revision 4270 to revision 4271


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "time_type" ADD COLUMN "last_active_date" integer DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks


