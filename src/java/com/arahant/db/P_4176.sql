
-- PostgreSQL patch from revision 4175 to revision 4176


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "prospect" ADD COLUMN "number_of_employees" integer DEFAULT 0 NOT NULL;

ALTER TABLE "prospect" ADD COLUMN "gross_income" integer DEFAULT 0 NOT NULL;

ALTER TABLE "prospect" ADD COLUMN "website" character varying(80);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks


