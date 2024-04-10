
-- PostgreSQL patch from revision 3633 to revision 3634

--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns

DROP INDEX "fki_eft_wage_type_fkey";

DROP INDEX "fki_garn_typ_wage_typ";

DROP INDEX "fki_hr_benefit_wage_type_fkey";

DROP INDEX "fki_hr_wage_type_fley";

DROP INDEX "fki_timesheet_wagetype_fkey";

DROP INDEX "fki_wage_paid_det_type_fkey";

ALTER TABLE "hr_wage" ALTER COLUMN "wage_type_id" SET NOT NULL;

--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_eft_wage_type_fkey" ON "electronic_fund_transfer" USING btree ("wage_type_id");

CREATE INDEX "fki_garn_typ_wage_typ" ON "garnishment_type" USING btree ("wage_type_id");

CREATE INDEX "fki_hr_benefit_wage_type_fkey" ON "hr_benefit" USING btree ("wage_type_id");

CREATE INDEX "fki_hr_wage_type_fley" ON "hr_wage" USING btree ("wage_type_id");

CREATE INDEX "fki_timesheet_wagetype_fkey" ON "timesheet" USING btree ("wage_type_id");

CREATE INDEX "fki_wage_paid_det_type_fkey" ON "wage_paid_detail" USING btree ("wage_type_id");

