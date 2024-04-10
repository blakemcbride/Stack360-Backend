
-- PostgreSQL patch from revision 4176 to revision 4177


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns

update prospect set when_added = '2012-01-01' where when_added is null;

ALTER TABLE "prospect" ALTER COLUMN "when_added" SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE "prospect" ALTER COLUMN "when_added" SET NOT NULL;

--  Remove tables


--  Drop columns


--  Add new indexes and checks


