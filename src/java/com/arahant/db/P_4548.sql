
-- PostgreSQL patch from revision 4547 to revision 4548


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "previous_employment" DROP CONSTRAINT "prev_empl_contact_chk";

ALTER TABLE "previous_employment" ADD CONSTRAINT "prev_empl_contact_chk" CHECK (((contact_supervisor='Y')OR(contact_supervisor='N')OR(contact_supervisor='L')));


