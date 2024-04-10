
-- PostgreSQL patch from revision 4889 to revision 4892


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "phone_email_idx" ON "person" USING btree (lower(personal_email));


