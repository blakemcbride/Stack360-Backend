
-- PostgreSQL patch from revision 4557 to revision 4558


--  Remove indexes and checks

DROP INDEX "person_ssn_idx";


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE UNIQUE INDEX "person_ssn_idx" ON "person" USING btree ("ssn");


