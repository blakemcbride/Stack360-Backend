
-- PostgreSQL patch from revision 1013 to revision 1014


--  Change existing columns

ALTER TABLE "invoice" ALTER COLUMN "purchase_order" TYPE character varying(20);


--  Drop columns

ALTER TABLE "employee" DROP COLUMN "billing_rate";


