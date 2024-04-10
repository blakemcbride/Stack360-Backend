
-- PostgreSQL patch from revision 1341 to revision 1345


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "client_item_inventory" (
	"client_item_inventory_id" character(16) NOT NULL,
	"quantity_on_hand" integer DEFAULT 0 NOT NULL,
	"usable_quantity_on_hand" integer DEFAULT 0 NOT NULL,
	"location" character varying(50),
	"project_id" character(16) NOT NULL,
	"item_category" character varying(20),
	"description" character varying(25),
	"model" character varying(15),
	"vendor" character varying(25),
	"vendor_item_code" character varying(12),
	"client_item_code" character varying(12),
	"unit_of_measure" character(1) NOT NULL,
	CONSTRAINT "client_item_inventory_unit_chk" CHECK (((unit_of_measure='E')OR(unit_of_measure='C')))
);

CREATE TABLE "delivery" (
	"delivery_id" character(16) NOT NULL,
	"delivery_datetime" timestamp without time zone,
	"who_accepted_delivery" character varying(30),
	"location" character varying(50),
	"project_po_id" character(16) NOT NULL
);

CREATE TABLE "delivery_detail" (
	"delivery_detail_id" character(16) NOT NULL,
	"project_po_detail_id" character(16) NOT NULL,
	"delivery_id" character(16) NOT NULL,
	"units_received" integer DEFAULT 0 NOT NULL,
	"condition" character varying(50),
	"usable" character(1) DEFAULT 'Y' NOT NULL,
	"location" character varying(50),
	CONSTRAINT "delivery_detail_usable_chk" CHECK (((usable='Y')OR(usable='N')))
);

CREATE TABLE "project_po_detail" (
	"project_po_detail_id" character(16) NOT NULL,
	"project_po_id" character(16) NOT NULL,
	"po_line_number" smallint DEFAULT 0 NOT NULL,
	"client_item_number" character varying(15),
	"quantity_ordered" integer DEFAULT 0 NOT NULL,
	"unit_of_measure" character(1) NOT NULL,
	"price_each" real DEFAULT 0 NOT NULL,
	"when_due" date,
	CONSTRAINT "project_po_detail_unit_chk" CHECK (((unit_of_measure='E')OR(unit_of_measure='C')))
);

CREATE TABLE "project_purchase_order" (
	"project_po_id" character(16) NOT NULL,
	"project_id" character(16) NOT NULL,
	"po_reference" character varying(20),
	"po_number" character varying(20),
	"vendor_name" character varying(30)
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "client_item_inventory" ADD CONSTRAINT "client_item_inventory_pkey" PRIMARY KEY ("client_item_inventory_id");

ALTER TABLE ONLY "delivery_detail" ADD CONSTRAINT "delivery_detail_pkey" PRIMARY KEY ("delivery_detail_id");

ALTER TABLE ONLY "delivery" ADD CONSTRAINT "delivery_pkey" PRIMARY KEY ("delivery_id");

CREATE INDEX "fki_client_item_inventory_vend_fkey" ON "client_item_inventory" USING btree ("project_id");

CREATE INDEX "fki_delivery_detail_delivery_fkey" ON "delivery_detail" USING btree ("delivery_id");

CREATE INDEX "fki_delivery_detail_po_fkey" ON "delivery_detail" USING btree ("project_po_detail_id");

CREATE INDEX "fki_delivery_po_fkey" ON "delivery" USING btree ("project_po_id");

CREATE INDEX "fki_project_po_det_po_fkey" ON "project_po_detail" USING btree ("project_po_id");

CREATE INDEX "fki_project_purchase_order_proj_fkey" ON "project_purchase_order" USING btree ("project_id");

ALTER TABLE ONLY "project_po_detail" ADD CONSTRAINT "project_po_det_pkey" PRIMARY KEY ("project_po_detail_id");

ALTER TABLE ONLY "project_purchase_order" ADD CONSTRAINT "project_purchase_order_pkey" PRIMARY KEY ("project_po_id");

COMMENT ON COLUMN "client_item_inventory"."unit_of_measure" IS 'Each or Case';

COMMENT ON COLUMN "delivery"."location" IS 'Where on client site delivery is located (if it''s all in one place)';

COMMENT ON COLUMN "delivery_detail"."location" IS 'Location if other than the whole delivery';

COMMENT ON COLUMN "delivery_detail"."usable" IS 'Yes or No';

COMMENT ON COLUMN "project_po_detail"."unit_of_measure" IS 'Each or Case';

-- --


ALTER TABLE ONLY "client_item_inventory" ADD CONSTRAINT "client_item_inventory_vend_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "delivery_detail" ADD CONSTRAINT "delivery_detail_delivery_fkey" FOREIGN KEY ("delivery_id") REFERENCES "delivery" ("delivery_id");

ALTER TABLE ONLY "delivery_detail" ADD CONSTRAINT "delivery_detail_po_fkey" FOREIGN KEY ("project_po_detail_id") REFERENCES "project_po_detail" ("project_po_detail_id");

ALTER TABLE ONLY "delivery" ADD CONSTRAINT "delivery_po_fkey" FOREIGN KEY ("project_po_id") REFERENCES "project_purchase_order" ("project_po_id");

ALTER TABLE ONLY "project_po_detail" ADD CONSTRAINT "project_po_det_po_fkey" FOREIGN KEY ("project_po_id") REFERENCES "project_purchase_order" ("project_po_id");

ALTER TABLE ONLY "project_purchase_order" ADD CONSTRAINT "project_purchase_order_proj_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");



-- ----------------------------------------------------------
