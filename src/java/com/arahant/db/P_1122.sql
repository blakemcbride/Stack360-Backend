
-- PostgreSQL patch from revision 1121 to revision 1122
--  Remove indexes and checks


--  Add new tables

CREATE TABLE "expense" (
	"expense_id" character(16) NOT NULL,
	"employee_id" character(16) NOT NULL,
	"project_id" character(16) NOT NULL,
	"date_paid" integer NOT NULL,
	"per_diem_amount" real DEFAULT 0 NOT NULL,
	"expense_amount" real DEFAULT 0 NOT NULL,
	"advance_amount" real DEFAULT 0 NOT NULL,
	"payment_method" character(1) NOT NULL,
	"comments" character varying(1024),
	"auth_date" integer NOT NULL,
	"auth_employee_id" character(16) NOT NULL,
	"per_diem_return" real DEFAULT 0 NOT NULL,
	CONSTRAINT "expense_method_chk" CHECK (((((payment_method='D')OR(payment_method='M'))OR(payment_method='C'))OR(payment_method='W')))
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "expense_apers_idx" ON "expense" USING btree ("auth_employee_id");

CREATE INDEX "expense_person_idx" ON "expense" USING btree ("employee_id", "date_paid");

ALTER TABLE ONLY "expense" ADD CONSTRAINT "expense_pki" PRIMARY KEY ("expense_id");

CREATE INDEX "expense_project_idx" ON "expense" USING btree ("project_id");

COMMENT ON COLUMN "expense"."advance_amount" IS 'How much they were already previously paid';

COMMENT ON COLUMN "expense"."auth_date" IS 'YYYYMMDD';

COMMENT ON COLUMN "expense"."date_paid" IS 'YYYYMMDD';

COMMENT ON COLUMN "expense"."expense_amount" IS 'Expenses previously paid to employee for past expenses but not recorded';

COMMENT ON COLUMN "expense"."payment_method" IS 'D = Direct deposit
M = Money / cash
C = Check
W = Walmart to Walmart';

COMMENT ON COLUMN "expense"."per_diem_amount" IS 'Advanced pay for presumed future expenses';

COMMENT ON COLUMN "expense"."per_diem_return" IS 'Amount that was paid in advance and is now being returned';

COMMENT ON TABLE "expense" IS 'Money paid to workers';



