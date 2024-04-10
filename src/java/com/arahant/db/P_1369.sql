
-- PostgreSQL patch from revision 1368 to revision 1369

--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "delivery" ADD COLUMN "delivery_date" integer NOT NULL;

ALTER TABLE "delivery" ADD COLUMN "delivery_time" integer NOT NULL;

ALTER TABLE "project_po_detail" ADD COLUMN "client_item_inventory_id" character(16) NOT NULL;

ALTER TABLE "project_po_detail" ADD COLUMN "when_due_date" integer NOT NULL;

ALTER TABLE "project_po_detail" ADD COLUMN "when_due_time" integer NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE "delivery" DROP COLUMN "delivery_datetime";

ALTER TABLE "project_po_detail" DROP COLUMN "client_item_number";

ALTER TABLE "project_po_detail" DROP CONSTRAINT "project_po_detail_unit_chk";

ALTER TABLE "project_po_detail" DROP COLUMN "unit_of_measure";

ALTER TABLE "project_po_detail" DROP COLUMN "when_due";


--  Add new indexes and checks

CREATE INDEX "fki_project_po_detail_item_fkey" ON "project_po_detail" USING btree ("client_item_inventory_id");

COMMENT ON COLUMN "delivery"."delivery_date" IS 'YYYYMMDD';

COMMENT ON COLUMN "delivery"."delivery_time" IS 'HHMM (-1 means not present)';

COMMENT ON COLUMN "project_po_detail"."when_due_date" IS 'YYYYMMDD';

COMMENT ON COLUMN "project_po_detail"."when_due_time" IS 'HHMM (-1 means not present)';

ALTER TABLE ONLY "project_po_detail" ADD CONSTRAINT "project_po_detail_item_fkey" FOREIGN KEY ("client_item_inventory_id") REFERENCES "client_item_inventory" ("client_item_inventory_id");


