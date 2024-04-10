
-- PostgreSQL patch from revision 4934 to revision 4935


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE "message" DROP CONSTRAINT "message_type_chk";

ALTER TABLE "message" DROP COLUMN "message_type";


--  Add new indexes and checks

