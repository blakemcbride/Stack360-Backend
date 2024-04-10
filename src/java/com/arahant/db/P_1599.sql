
-- PostgreSQL patch from revision 1596 to revision 1599



--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "hr_training_category" ADD COLUMN "client_id" character(16);

ALTER TABLE "hr_training_category" ADD COLUMN "required" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "hr_training_category" ADD COLUMN "hours" real DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "hr_training_category" ADD CONSTRAINT "hr_training_cat_req_chk" CHECK (((required='Y')OR(required='N')));

CREATE INDEX "hr_training_cat_client_idx" ON "hr_training_category" USING btree ("client_id");

COMMENT ON COLUMN "hr_training_category"."client_id" IS 'Client org group associated with or NULL if not client specific';

COMMENT ON COLUMN "hr_training_category"."hours" IS 'Normal number of hours for this type of training';

COMMENT ON COLUMN "hr_training_category"."required" IS 'Yes / No';


ALTER TABLE ONLY "hr_training_category" ADD CONSTRAINT "hr_training_cat_cleint_fkey" FOREIGN KEY ("client_id") REFERENCES "org_group" ("org_group_id");

