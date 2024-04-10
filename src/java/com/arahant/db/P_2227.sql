
-- PostgreSQL patch from revision 2226 to revision 2227


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "employee" ADD COLUMN "adp_id" character(16);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "employee" ADD CONSTRAINT "adp_oid_unique" UNIQUE ("adp_id");

COMMENT ON CONSTRAINT "adp_oid_unique" ON "employee" IS 'ADP associateOID field';


