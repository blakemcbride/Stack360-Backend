
-- PostgreSQL patch from revision 1480 to revision 1488



--  Remove indexes and checks


--  Add new tables

CREATE TABLE "project_inventory_email" (
	"pie_id" character(16) NOT NULL,
	"project_id" character(16) NOT NULL,
	"person_id" character(16) NOT NULL
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_inventory_email_per_fkey" ON "project_inventory_email" USING btree ("person_id");

ALTER TABLE ONLY "project_inventory_email" ADD CONSTRAINT "project_inventory_email_pk" PRIMARY KEY ("pie_id");

CREATE UNIQUE INDEX "project_inventory_email_proj_pers_idx" ON "project_inventory_email" USING btree ("project_id", "person_id");

COMMENT ON TABLE "project_inventory_email" IS 'This table keeps a list of people who are sent the project inventory report';

ALTER TABLE ONLY "project_inventory_email" ADD CONSTRAINT "project_inventory_email_per_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_inventory_email" ADD CONSTRAINT "project_inventory_email_proj_fky" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

