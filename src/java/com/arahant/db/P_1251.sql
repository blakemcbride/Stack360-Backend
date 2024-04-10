
-- PostgreSQL patch from revision 1250 to revision 1251


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "expense_receipt" ADD COLUMN "who_uploaded" character(16) NOT NULL;

ALTER TABLE "expense_receipt" ADD COLUMN "file_type" character varying(8);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_expense_receipt_who_fkey" ON "expense_receipt" USING btree ("who_uploaded");

COMMENT ON COLUMN "expense_receipt"."picture1" IS 'the receipt';

COMMENT ON COLUMN "expense_receipt"."picture2" IS 'from the CC company';

ALTER TABLE ONLY "expense_receipt" ADD CONSTRAINT "expense_receipt_who_fkey" FOREIGN KEY ("who_uploaded") REFERENCES "person" ("person_id");
