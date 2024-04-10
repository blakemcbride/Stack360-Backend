
-- PostgreSQL patch from revision 1327 to revision 1329


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "item" ADD COLUMN "item_name" character varying(80);

ALTER TABLE "item" ADD COLUMN "manufacturer" character varying(50);

ALTER TABLE "item" ADD COLUMN "model" character varying(20);

ALTER TABLE "item" ADD COLUMN "date_purchased" integer DEFAULT 0 NOT NULL;

ALTER TABLE "item" ADD COLUMN "original_cost" real DEFAULT 0 NOT NULL;

ALTER TABLE "item" ADD COLUMN "purchased_from" character varying(20);

ALTER TABLE "item" ADD COLUMN "notes" character varying(256);

ALTER TABLE "item" ADD COLUMN "item_status" character(1) DEFAULT 'C' NOT NULL;

ALTER TABLE "item" ADD COLUMN "retirement_notes" character varying(256);

ALTER TABLE "item" ADD COLUMN "retirement_date" integer DEFAULT 0 NOT NULL;

ALTER TABLE "item" ADD COLUMN "reimbursement_status" character(1) DEFAULT 'C' NOT NULL;

ALTER TABLE "item" ADD COLUMN "reimbursement_person_id" character(16);

ALTER TABLE "item" ADD COLUMN "requested_reimbursement_amount" real DEFAULT 0 NOT NULL;

ALTER TABLE "item" ADD COLUMN "reimbursement_amount_received" real DEFAULT 0 NOT NULL;

ALTER TABLE "item" ADD COLUMN "date_reimbursement_received" integer DEFAULT 0 NOT NULL;

ALTER TABLE "item" ADD COLUMN "person_accepting_reimbursement" character(16);

ALTER TABLE "item_h" ADD COLUMN "item_name" character varying(80);

ALTER TABLE "item_h" ADD COLUMN "manufacturer" character varying(50);

ALTER TABLE "item_h" ADD COLUMN "model" character varying(20);

ALTER TABLE "item_h" ADD COLUMN "date_purchased" integer DEFAULT 0 NOT NULL;

ALTER TABLE "item_h" ADD COLUMN "original_cost" real DEFAULT 0 NOT NULL;

ALTER TABLE "item_h" ADD COLUMN "purchased_from" character varying(20);

ALTER TABLE "item_h" ADD COLUMN "notes" character varying(256);

ALTER TABLE "item_h" ADD COLUMN "item_status" character(1) DEFAULT 'C' NOT NULL;

ALTER TABLE "item_h" ADD COLUMN "retirement_notes" character varying(256);

ALTER TABLE "item_h" ADD COLUMN "retirement_date" integer DEFAULT 0 NOT NULL;

ALTER TABLE "item_h" ADD COLUMN "reimbursement_status" character(1) DEFAULT 'C' NOT NULL;

ALTER TABLE "item_h" ADD COLUMN "reimbursement_person_id" character(16);

ALTER TABLE "item_h" ADD COLUMN "requested_reimbursement_amount" real DEFAULT 0 NOT NULL;

ALTER TABLE "item_h" ADD COLUMN "reimbursement_amount_received" real DEFAULT 0 NOT NULL;

ALTER TABLE "item_h" ADD COLUMN "date_reimbursement_received" integer DEFAULT 0 NOT NULL;

ALTER TABLE "item_h" ADD COLUMN "person_accepting_reimbursement" character(16);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "item" ADD CONSTRAINT "item_reimbursement_status_chk" CHECK (((((reimbursement_status='C')OR(reimbursement_status='Y'))OR(reimbursement_status='N'))OR(reimbursement_status='R')));

ALTER TABLE "item" ADD CONSTRAINT "item_status_chk" CHECK ((((item_status='C')OR(item_status='R'))OR(item_status='L')));

ALTER TABLE "item_h" ADD CONSTRAINT "item_h_item_status_chk" CHECK ((((item_status='C')OR(item_status='R'))OR(item_status='L')));

ALTER TABLE "item_h" ADD CONSTRAINT "item_h_reimbursement_status_chk" CHECK (((((reimbursement_status='C')OR(reimbursement_status='Y'))OR(reimbursement_status='N'))OR(reimbursement_status='R')));

CREATE INDEX "item_accepting_person_idx" ON "item" USING btree ("person_accepting_reimbursement");

CREATE INDEX "item_h_accepting_person_idx" ON "item_h" USING btree ("person_accepting_reimbursement");

CREATE INDEX "item_h_reimbursement_person_idx" ON "item_h" USING btree ("reimbursement_person_id");

CREATE INDEX "item_reimbursement_person_idx" ON "item" USING btree ("reimbursement_person_id");

COMMENT ON COLUMN "item"."item_status" IS 'C= Current
R = Retired
L = Lost';

COMMENT ON COLUMN "item"."reimbursement_person_id" IS 'Worker reimbursement expected from';

COMMENT ON COLUMN "item"."reimbursement_status" IS 'C = Not expecting reimbursement (item is current)
Y = Yes, reimbursement open
N = No reimbursement expected
R = reimbursement has been received';

COMMENT ON COLUMN "item"."requested_reimbursement_amount" IS 'Expected / requested reimbursement amount';

COMMENT ON COLUMN "item"."retirement_notes" IS 'Notes about item''s loss or retirement';

COMMENT ON COLUMN "item_h"."item_status" IS 'C = Current
R = Retired
L = Lost';

COMMENT ON COLUMN "item_h"."reimbursement_status" IS 'C = Not expecting reimbursement (item is current)
Y = Yes, reimbursement open
N = No reimbursement expected
R = reimbursement has been receivedw';


ALTER TABLE ONLY "item_h" ADD CONSTRAINT "item_h_reimbursement_person_fkey" FOREIGN KEY ("reimbursement_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "item" ADD CONSTRAINT "item_reimbursement_person_fkey" FOREIGN KEY ("reimbursement_person_id") REFERENCES "person" ("person_id");
ALTER TABLE ONLY "item" ADD CONSTRAINT "item_accepting_person_fky" FOREIGN KEY ("person_accepting_reimbursement") REFERENCES "person" ("person_id");
ALTER TABLE ONLY "item_h" ADD CONSTRAINT "item_h_accepting_person_fkey" FOREIGN KEY ("person_accepting_reimbursement") REFERENCES "person" ("person_id");

