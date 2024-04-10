
-- PostgreSQL patch from revision 3627 to revision 3628

--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project_employee_join" ADD COLUMN "manager" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "project_employee_join" ADD COLUMN "hours" character(1) DEFAULT 'Y' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "project_employee_join" ADD CONSTRAINT "project_employee_join_hours_chk" CHECK (((hours='Y')OR(hours='N')));

ALTER TABLE "project_employee_join" ADD CONSTRAINT "project_employee_join_manage_chk" CHECK (((manager='Y')OR(manager='N')));

COMMENT ON COLUMN "project_employee_join"."hours" IS 'Does this person log hours against this project?';

COMMENT ON COLUMN "project_employee_join"."manager" IS 'Is this person a manager?';


