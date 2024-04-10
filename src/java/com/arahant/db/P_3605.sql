
-- PostgreSQL patch from revision 3604 to revision 3605



--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "client_contact" ADD COLUMN "contact_type" smallint DEFAULT 5 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "client_contact" ADD CONSTRAINT "client_contact_type_chk" CHECK (((contact_type>=1)AND(contact_type<=5)));

COMMENT ON COLUMN "client_contact"."contact_type" IS '1 = decision maker
2 = department head
3 = user
4 = consultant / other employee
5 = Unknown';

