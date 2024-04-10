
--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "rate_type" ADD COLUMN "rate_code" character(10);
UPDATE rate_type set rate_code = 'A';
alter table rate_type alter column rate_code set not null;


ALTER TABLE "rate_type" ADD COLUMN "org_group_id" character(16);

ALTER TABLE "rate_type" ADD COLUMN "last_active_date" integer DEFAULT 0 NOT NULL;


--  Change existing columns

DROP INDEX "agreement_form_name_idx";

DROP INDEX "app_ques_choice_item_idx";

DROP INDEX "ben_ques_choice_item_idx";

DROP INDEX "hr_ben_cat_desc_idx";

DROP INDEX "location_cost_pidx";

DROP INDEX "product_attribute_choice_idx";

ALTER TABLE "rate_type" ALTER COLUMN "description" TYPE character varying(60);

--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "agreement_form_name_idx" ON "agreement_form" USING btree ("description");

CREATE UNIQUE INDEX "app_ques_choice_item_idx" ON "applicant_question_choice" USING btree ("applicant_question_id", "description");

CREATE UNIQUE INDEX "ben_ques_choice_item_idx" ON "benefit_question_choice" USING btree ("benefit_question_id", "description");

CREATE INDEX "hr_ben_cat_desc_idx" ON "hr_benefit_category" USING btree ("description");

CREATE UNIQUE INDEX "location_cost_pidx" ON "location_cost" USING btree ("description");

CREATE INDEX "product_attribute_choice_idx" ON "product_attribute_choice" USING btree ("product_attribute_id", "choice_order", "description");

CREATE INDEX "rate_type_org_group_idx" ON "rate_type" USING btree ("org_group_id");

COMMENT ON COLUMN "rate_type"."org_group_id" IS 'The company the record applies to, or NULL if it is globally applicable.';



