
-- PostgreSQL patch from revision 1155 to revision 1156



--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "expense" ADD COLUMN "scheduling_comments" character varying(1024);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "expense"."comments" IS 'Payroll comments';


