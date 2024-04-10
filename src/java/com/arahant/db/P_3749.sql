
-- PostgreSQL patch from revision 3748 to revision 3749


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "phone_number_idx" ON "phone" USING btree ("phone_number");

