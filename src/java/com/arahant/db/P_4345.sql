
-- PostgreSQL patch from revision 4343 to revision 4345

--  Remove indexes and checks

DROP INDEX "project_task_picture_pic_idx";


--  Add new tables


--  Add new columns

ALTER TABLE "project_task_picture" ADD COLUMN "file_name_extension" character varying(10) NOT NULL;

ALTER TABLE "project_task_picture" ADD COLUMN "comments" character varying(256);


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE "project_task_picture" DROP COLUMN "seqno";


--  Add new indexes and checks

CREATE INDEX "project_task_picture_task_idx" ON "project_task_picture" USING btree ("project_task_detail_id", "when_uploaded");


