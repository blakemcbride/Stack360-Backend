
-- PostgreSQL patch from revision 3570 to revision 3571

--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "message" DROP CONSTRAINT "message_type_chk";

ALTER TABLE "message" ADD CONSTRAINT "message_type_chk" CHECK (((message_type='M')OR(message_type='E')OR(message_type='T')));


