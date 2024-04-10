
-- PostgreSQL patch from revision 1541 to revision 1543


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "project_history_person" DROP CONSTRAINT "project_hist_per_reason_chk";

ALTER TABLE "project_history_person" ADD CONSTRAINT "project_hist_per_reason_chk" CHECK ((((((((reason_unassigned=' ')OR(reason_unassigned='1'))OR(reason_unassigned='2'))OR(reason_unassigned='3'))OR(reason_unassigned='4'))OR(reason_unassigned='5'))OR(reason_unassigned='0')));



-- ----------------------------------------------------------
