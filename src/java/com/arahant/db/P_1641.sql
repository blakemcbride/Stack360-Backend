
-- PostgreSQL patch from revision 1640 to revision 1641


--  Add new tables

CREATE TABLE "project_training" (
	"project_training_id" character(16) NOT NULL,
	"project_id" character(16) NOT NULL,
	"cat_id" character(16) NOT NULL,
	"required" character(1) NOT NULL,
	CONSTRAINT "project_training_required_chk" CHECK (((required='Y')OR(required='N')))
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_training_cat_fkey" ON "project_training" USING btree ("cat_id");

CREATE INDEX "fki_project_training_project_fkey" ON "project_training" USING btree ("project_id");

ALTER TABLE ONLY "project_training" ADD CONSTRAINT "project_training_pki" PRIMARY KEY ("project_training_id");

ALTER TABLE ONLY "project_training" ADD CONSTRAINT "project_training_cat_fkey" FOREIGN KEY ("cat_id") REFERENCES "hr_training_category" ("cat_id");

ALTER TABLE ONLY "project_training" ADD CONSTRAINT "project_training_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

