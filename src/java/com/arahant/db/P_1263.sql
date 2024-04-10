
-- PostgreSQL patch from revision 1262 to revision 1263

--  Remove indexes and checks

--  Add new tables

--  Add new columns

ALTER TABLE "expense_receipt" ADD COLUMN "payment_method" character(1) NOT NULL DEFAULT 'A';

ALTER TABLE "expense_receipt" ADD COLUMN "approved" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "expense_receipt" ADD COLUMN "who_approved" character(16);

ALTER TABLE "expense_receipt" ADD COLUMN "when_approved" timestamp without time zone;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "expense_receipt" ADD CONSTRAINT "expense_receipt_app_chk" CHECK (((approved='Y')OR(approved='N')));

ALTER TABLE "expense_receipt" ADD CONSTRAINT "expense_receipt_pm_chk" CHECK ((((payment_method='A')OR(payment_method='B'))OR(payment_method='E')));

CREATE INDEX "fki_expense_receipt_who_app_fkey" ON "expense_receipt" USING btree ("who_approved");

COMMENT ON COLUMN "expense_receipt"."approved" IS 'Y/N';

COMMENT ON COLUMN "expense_receipt"."payment_method" IS 'A = Company Debit Card
B = Employee Comdata Card
E = Employee Personal Account';

ALTER TABLE ONLY "expense_receipt" ADD CONSTRAINT "expense_receipt_who_app_fkey" FOREIGN KEY ("who_approved") REFERENCES "person" ("person_id");

