
-- PostgreSQL patch from revision 4780 to revision 4781


--  Remove indexes and checks


--  Add new tables


--  Drop columns

ALTER TABLE "project_task_detail" DROP CONSTRAINT "project_task_detail_assigned_chk";

ALTER TABLE "project_task_detail" DROP COLUMN "assigned";

ALTER TABLE "project_task_detail" DROP CONSTRAINT "project_task_detail_complete_chk";

ALTER TABLE "project_task_detail" DROP COLUMN "complete";


--  Add new columns

ALTER TABLE "project_task_detail" ADD COLUMN "recurring" character(1) DEFAULT 'N' NOT NULL;


--  Change existing columns


--  Remove tables


--  Add new indexes and checks

ALTER TABLE "project_task_detail" ADD CONSTRAINT "project_task_detail_recurring_chk" CHECK (((recurring='Y')OR(recurring='N')));

COMMENT ON COLUMN "project_task_detail"."recurring" IS 'Is this a recurring task?  Y/N';

COMMENT ON COLUMN "project_task_detail"."status" IS '0 = open
1 = complete
2 = cancelled
3 = incomplete - time
4 = incomplete - missing items
5 = incomplete - reassigned
6 = incomplete - see comments';


