
-- PostgreSQL patch from revision 1147 to revision 1149

--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "expense" ADD COLUMN "week_paid_for" integer DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "expense"."week_paid_for" IS 'Date of first day in week being paid for';


