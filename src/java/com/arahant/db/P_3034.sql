
-- PostgreSQL patch from revision 3033 to revision 3034


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project" ADD COLUMN "billing_type" character(1) DEFAULT 'H' NOT NULL;

ALTER TABLE "project" ADD COLUMN "fixed_price_amount" double precision DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "project" ADD CONSTRAINT "project_billing_type_chk" CHECK (((billing_type='H')OR(billing_type='F')));

COMMENT ON COLUMN "project"."billing_type" IS '(H)ourly or (F)ixed price';


