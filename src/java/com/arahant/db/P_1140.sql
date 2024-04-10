
-- PostgreSQL patch from revision 1139 to revision 1140



--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "expense" DROP CONSTRAINT "expense_method_chk";

ALTER TABLE "expense" ADD CONSTRAINT "expense_method_chk" CHECK ((((((payment_method='D')OR(payment_method='M'))OR(payment_method='C'))OR(payment_method='W'))OR(payment_method='O')));



-- ----------------------------------------------------------
