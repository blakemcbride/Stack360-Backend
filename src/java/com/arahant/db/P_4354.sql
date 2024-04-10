
-- PostgreSQL patch from revision 4351 to revision 4354


delete from project_task_picture;
delete from project_task_detail;

DROP INDEX "project_task_detail_list_idx";


--  Drop columns

ALTER TABLE "project_task_detail" DROP CONSTRAINT "project_task_detail_task_fky";

ALTER TABLE "project_task_detail" DROP COLUMN "project_task_list_id";


--  Remove indexes and checks


ALTER TABLE "project_task_list" DROP CONSTRAINT "project_task_project_fki";

ALTER TABLE ONLY "project_task_list" DROP CONSTRAINT "project_task_list_pkey";

DROP INDEX "project_task_project_idx";


--  Add new tables


--  Add new columns

ALTER TABLE "project_task_detail" ADD COLUMN "project_shift_id" character(16) NOT NULL;

ALTER TABLE "project_task_detail" ADD COLUMN "complete" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE ONLY "project_task_detail" ADD CONSTRAINT "project_task_detail_shift_fky" FOREIGN KEY ("project_shift_id") REFERENCES "project_shift" ("project_shift_id");

ALTER TABLE ONLY public.project_task_detail
    ADD COLUMN task_date integer NOT NULL DEFAULT 0;

COMMENT ON COLUMN public.project_task_detail.task_date
    IS 'First work date of task YYYYMMDD';

ALTER TABLE ONLY public.project_task_detail
    ADD COLUMN completion_date integer NOT NULL DEFAULT 0;

COMMENT ON COLUMN public.project_task_detail.completion_date
    IS 'work date task was completed YYYYMMDD';

--  Change existing columns


--  Remove tables

DROP TABLE "project_task_list";


--  Add new indexes and checks

ALTER TABLE "project_task_detail" ADD CONSTRAINT "project_task_detail_complete_chk" CHECK (((complete='Y')OR(complete='N')));

CREATE UNIQUE INDEX "project_task_detail_shift_idx" ON "project_task_detail" USING btree ("project_shift_id", "seqno");

