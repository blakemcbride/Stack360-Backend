
-- PostgreSQL patch from revision 4128 to revision 4129


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

update hr_employee_eval set state = 'S' where state <> 'E' and state <> 'F';

ALTER TABLE "hr_employee_eval" ADD CONSTRAINT "hr_employee_eval_state_chk" CHECK (((state='E')OR(state='S')OR(state='F')));

