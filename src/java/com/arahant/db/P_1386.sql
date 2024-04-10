
-- PostgreSQL patch from revision 1385 to revision 1386



--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns

DROP INDEX "agreement_form_name_idx";

DROP INDEX "app_ques_choice_item_idx";

DROP INDEX "ben_ques_choice_item_idx";

DROP INDEX "hr_ben_cat_desc_idx";

DROP INDEX "location_cost_pidx";

DROP INDEX "product_attribute_choice_idx";

ALTER TABLE "client_item_inventory" ALTER COLUMN "description" TYPE character varying(150);
ALTER TABLE "client_item_inventory" ALTER COLUMN "vendor" TYPE character varying(45);
ALTER TABLE "client_item_inventory" ALTER COLUMN "vendor_item_code" TYPE character varying(25);

--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "agreement_form_name_idx" ON "agreement_form" USING btree ("description");

CREATE UNIQUE INDEX "app_ques_choice_item_idx" ON "applicant_question_choice" USING btree ("applicant_question_id", "description");

CREATE UNIQUE INDEX "ben_ques_choice_item_idx" ON "benefit_question_choice" USING btree ("benefit_question_id", "description");

CREATE INDEX "hr_ben_cat_desc_idx" ON "hr_benefit_category" USING btree ("description");

CREATE UNIQUE INDEX "location_cost_pidx" ON "location_cost" USING btree ("description");

CREATE INDEX "product_attribute_choice_idx" ON "product_attribute_choice" USING btree ("product_attribute_id", "choice_order", "description");



-- ----------------------------------------------------------
