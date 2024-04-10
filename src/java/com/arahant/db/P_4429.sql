
-- PostgreSQL patch from revision 4428 to revision 4429


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns

ALTER TABLE "prophet_login" ALTER COLUMN "authentication_code" TYPE character varying(36);

--  Remove tables


--  Drop columns


--  Add new indexes and checks

