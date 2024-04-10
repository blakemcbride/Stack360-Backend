
-- PostgreSQL patch from revision 1371 to revision 1375

--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns

ALTER TABLE ONLY "client_item_inventory" DROP CONSTRAINT "client_item_inventory_pkey";

DROP INDEX "fki_project_po_detail_item_fkey";

ALTER TABLE "client_item_inventory" ALTER COLUMN "client_item_inventory_id" TYPE character(36);
ALTER TABLE ONLY "delivery" DROP CONSTRAINT "delivery_pkey";

DROP INDEX "fki_delivery_detail_delivery_fkey";

ALTER TABLE "delivery" ALTER COLUMN "delivery_id" TYPE character(36);
DROP INDEX "fki_delivery_po_fkey";

DROP INDEX "fki_project_po_det_po_fkey";

ALTER TABLE ONLY "project_purchase_order" DROP CONSTRAINT "project_purchase_order_pkey";

ALTER TABLE "delivery" ALTER COLUMN "project_po_id" TYPE character(36);
ALTER TABLE ONLY "delivery_detail" DROP CONSTRAINT "delivery_detail_pkey";

ALTER TABLE "delivery_detail" ALTER COLUMN "delivery_detail_id" TYPE character(36);
DROP INDEX "fki_delivery_detail_po_fkey";

ALTER TABLE ONLY "project_po_detail" DROP CONSTRAINT "project_po_det_pkey";

ALTER TABLE "delivery_detail" ALTER COLUMN "project_po_detail_id" TYPE character(36);
ALTER TABLE "delivery_detail" ALTER COLUMN "delivery_id" TYPE character(36);
ALTER TABLE "project_po_detail" ALTER COLUMN "project_po_detail_id" TYPE character(36);
ALTER TABLE "project_po_detail" ALTER COLUMN "project_po_id" TYPE character(36);
ALTER TABLE "project_po_detail" ALTER COLUMN "client_item_inventory_id" TYPE character(36);
ALTER TABLE "project_purchase_order" ALTER COLUMN "project_po_id" TYPE character(36);

--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "client_item_inventory" ADD CONSTRAINT "client_item_inventory_pkey" PRIMARY KEY ("client_item_inventory_id");

ALTER TABLE ONLY "delivery_detail" ADD CONSTRAINT "delivery_detail_pkey" PRIMARY KEY ("delivery_detail_id");

ALTER TABLE ONLY "delivery" ADD CONSTRAINT "delivery_pkey" PRIMARY KEY ("delivery_id");

CREATE INDEX "fki_delivery_detail_delivery_fkey" ON "delivery_detail" USING btree ("delivery_id");

CREATE INDEX "fki_delivery_detail_po_fkey" ON "delivery_detail" USING btree ("project_po_detail_id");

CREATE INDEX "fki_delivery_po_fkey" ON "delivery" USING btree ("project_po_id");

CREATE INDEX "fki_project_po_det_po_fkey" ON "project_po_detail" USING btree ("project_po_id");

CREATE INDEX "fki_project_po_detail_item_fkey" ON "project_po_detail" USING btree ("client_item_inventory_id");

ALTER TABLE ONLY "project_po_detail" ADD CONSTRAINT "project_po_det_pkey" PRIMARY KEY ("project_po_detail_id");

ALTER TABLE ONLY "project_purchase_order" ADD CONSTRAINT "project_purchase_order_pkey" PRIMARY KEY ("project_po_id");

COMMENT ON COLUMN "client_item_inventory"."client_item_inventory_id" IS 'UUID';

COMMENT ON COLUMN "delivery"."delivery_id" IS 'UUID';

COMMENT ON COLUMN "delivery"."project_po_id" IS 'UUID';

COMMENT ON COLUMN "delivery_detail"."delivery_detail_id" IS 'UUID';

COMMENT ON COLUMN "delivery_detail"."delivery_id" IS 'UUID';

COMMENT ON COLUMN "delivery_detail"."project_po_detail_id" IS 'UUID';

COMMENT ON COLUMN "project_po_detail"."client_item_inventory_id" IS 'UUID';

COMMENT ON COLUMN "project_po_detail"."project_po_detail_id" IS 'UUID';

COMMENT ON COLUMN "project_po_detail"."project_po_id" IS 'UUID';

COMMENT ON COLUMN "project_purchase_order"."project_po_id" IS 'UUID';


