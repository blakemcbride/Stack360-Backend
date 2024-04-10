
-- PostgreSQL patch from revision 1164 to revision 1165



--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "timesheet" ADD COLUMN "nonbillable_hours" real DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "timesheet"."nonbillable_hours" IS 'If record is flagged billable and there were additional, non-billable hours, this column is used.';


