
-- PostgreSQL patch from revision 4468 to revision 4469


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "previous_employment" ADD COLUMN "street" character varying(45);

ALTER TABLE "previous_employment" ADD COLUMN "city" character varying(20);

ALTER TABLE "previous_employment" ADD COLUMN "state" character(2);


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE "previous_employment" DROP COLUMN "address";


--  Add new indexes and checks

