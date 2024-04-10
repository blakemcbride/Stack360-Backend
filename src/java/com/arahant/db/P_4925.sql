
-- PostgreSQL patch from revision 4923 to revision 4924


--  Remove indexes and checks

DROP INDEX "text_message_person_idx";

ALTER TABLE ONLY "text_message" DROP CONSTRAINT "text_message_pkey";


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables

DROP TABLE "text_message";


--  Drop columns


--  Add new indexes and checks


