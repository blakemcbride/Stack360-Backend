
-- PostgreSQL patch from revision 5084 to revision 5085


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project_task_picture" ADD COLUMN "who_uploaded" character(16);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "project_task_picture_who_uploaded_index" ON "project_task_picture" USING btree ("who_uploaded");

ALTER TABLE ONLY "project_task_picture" ADD CONSTRAINT "project_task_picture_who_uploaded_fk" FOREIGN KEY ("who_uploaded") REFERENCES "person" ("person_id");

