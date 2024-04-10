
-- PostgreSQL patch from revision 4143 to revision 4144


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns

ALTER TABLE "timesheet" ALTER COLUMN "billable" SET DEFAULT 'U';


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "timesheet" ADD CONSTRAINT "timesheet_billable_chk" CHECK (((billable='Y')OR(billable='N')OR(billable='U')));

