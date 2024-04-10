
-- PostgreSQL patch from revision 1567 to revision 1568 branch 745


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns

--  Remove tables


--  Drop columns

ALTER TABLE "project_history_person" DROP COLUMN "date_unassigned";

ALTER TABLE "project_history_person" DROP COLUMN "time_unassigned";

ALTER TABLE "project_history_person" DROP CONSTRAINT "project_hist_per_reason_chk";

ALTER TABLE "project_history_person" DROP COLUMN "reason_unassigned";

ALTER TABLE "project_history_person" DROP COLUMN "unassigned_comment";


--  Add new indexes and checks



-- ----------------------------------------------------------
