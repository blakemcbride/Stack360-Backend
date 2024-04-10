
-- PostgreSQL patch from revision 3679 to revision 3680



--  Remove indexes and checks


--  Add new tables

CREATE TABLE "project_report_person" (
	"person_id" character(16) NOT NULL,
	"client_id" character(16) NOT NULL
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_report_person_client_fk" ON "project_report_person" USING btree ("client_id");

ALTER TABLE ONLY "project_report_person" ADD CONSTRAINT "project_report_person_pk" PRIMARY KEY ("person_id", "client_id");

ALTER TABLE ONLY "project_report_person" ADD CONSTRAINT "project_report_person_client_fk" FOREIGN KEY ("client_id") REFERENCES "client" ("org_group_id");

ALTER TABLE ONLY "project_report_person" ADD CONSTRAINT "project_report_person_person_fk" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");
