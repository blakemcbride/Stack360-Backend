
-- PostgreSQL patch from revision 3117 to revision 3118


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "invoice_line_item" DROP CONSTRAINT "invoice_line_type_chk";

ALTER TABLE "invoice_line_item" ADD CONSTRAINT "invoice_line_type_chk" CHECK (((billing_type='H')OR(billing_type='P')OR(billing_type='D')));


