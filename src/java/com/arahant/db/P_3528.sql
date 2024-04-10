
-- PostgreSQL patch from revision 3527 to revision 3528



--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE UNIQUE INDEX "rate_type_code_idx" ON "rate_type" USING btree ("rate_code");


