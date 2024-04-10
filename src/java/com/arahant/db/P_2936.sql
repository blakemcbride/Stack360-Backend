
-- PostgreSQL patch from revision 2935 to revision 2936

--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns

ALTER TABLE "person" ALTER COLUMN "company_id" SET NOT NULL;

--  Remove tables


--  Drop columns


--  Add new indexes and checks


-- ----------------------------------------------------------

