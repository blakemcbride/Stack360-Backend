
-- PostgreSQL patch from revision 1231 to revision 1233


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "expense_receipt" (
	"expense_receipt_id" character(16) NOT NULL,
	"person_id" character(16) NOT NULL,
	"project_id" character(16) NOT NULL,
	"expense_account_id" character(16) NOT NULL,
	"receipt_date" integer NOT NULL,
	"when_uploaded" timestamp without time zone NOT NULL,
	"business_purpose" character varying(80),
	"amount" double precision NOT NULL,
	"picture1" bytea,
	"picture2" bytea
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "expense_receipt_exp_idx" ON "expense_receipt" USING btree ("expense_account_id");

CREATE INDEX "expense_receipt_pers_idx" ON "expense_receipt" USING btree ("person_id", "project_id");

ALTER TABLE ONLY "expense_receipt" ADD CONSTRAINT "expense_receipt_pkey" PRIMARY KEY ("expense_receipt_id");

CREATE INDEX "expense_receipt_proj_idx" ON "expense_receipt" USING btree ("project_id", "person_id");


ALTER TABLE ONLY "expense_receipt" ADD CONSTRAINT "expense_receipt_exp_fkey" FOREIGN KEY ("expense_account_id") REFERENCES "expense_account" ("expense_account_id");

ALTER TABLE ONLY "expense_receipt" ADD CONSTRAINT "expense_receipt_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "expense_receipt" ADD CONSTRAINT "expense_receipt_proj_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");
