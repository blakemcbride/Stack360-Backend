
-- PostgreSQL patch from revision 3620 to revision 3621


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "hr_employee_event" ADD COLUMN "event_type" character(1) DEFAULT 'N' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "hr_employee_event" ADD CONSTRAINT "hr_emp_event_type_chk" CHECK (((event_type='N')OR(event_type='M')OR(event_type='L')));

COMMENT ON COLUMN "hr_employee_event"."event_type" IS 'Normal
Mobile
Label';

