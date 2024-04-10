

--  Remove indexes and checks

DROP INDEX "rate_type_org_group_idx";


--  Add new tables


--  Add new columns

ALTER TABLE "rate_type" ADD COLUMN "company_id" character(16);
UPDATE rate_type set company_id = (select org_group_id from company_detail limit 1);


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE "rate_type" DROP COLUMN "org_group_id";


--  Add new indexes and checks

CREATE INDEX "rate_type_company_idx" ON "rate_type" USING btree ("company_id");

COMMENT ON COLUMN "rate_type"."company_id" IS 'The company this type is applicable to or NULL for all companies.';



