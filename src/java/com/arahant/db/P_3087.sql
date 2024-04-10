
-- PostgreSQL patch from revision 3086 to revision 3087


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "invoice_line_item" ADD COLUMN "project_id" character(16);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_invoice_li_project_fk" ON "invoice_line_item" USING btree ("project_id");

COMMENT ON COLUMN "invoice_line_item"."project_id" IS 'Used only if the project is billed by the project rather than hourly.';

ALTER TABLE ONLY "invoice_line_item" ADD CONSTRAINT "invoice_li_project_fk" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

