
-- PostgreSQL patch from revision 4366 to revision 4369


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "project_task_assignment" (
	"project_task_assignment_id" character(16) NOT NULL,
	"project_task_detail_id" character(16) NOT NULL,
	"person_id" character(16) NOT NULL,
	"team_lead" character(1) DEFAULT 'N' NOT NULL,
	CONSTRAINT "project_task_assignment_lead_chk" CHECK (((team_lead='Y')OR(team_lead='N')))
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_task_assignment_person_fkey" ON "project_task_assignment" USING btree ("person_id");

CREATE UNIQUE INDEX "project_task_assignment_task_idx" ON "project_task_assignment" USING btree ("project_task_detail_id", "person_id");

ALTER TABLE ONLY "project_task_assignment" ADD CONSTRAINT "project_task_assignment_pkey" PRIMARY KEY ("project_task_assignment_id");

ALTER TABLE ONLY "project_task_assignment" ADD CONSTRAINT "project_task_assignment_person_fkey" FOREIGN KEY ("person_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "project_task_assignment" ADD CONSTRAINT "project_task_assignment_task_fkey" FOREIGN KEY ("project_task_detail_id") REFERENCES "project_task_detail" ("project_task_detail_id");

