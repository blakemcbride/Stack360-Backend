
-- PostgreSQL patch from revision 3560 to revision 3562


--  Remove indexes and checks

DROP INDEX "fki_messages_from_person";


--  Add new tables


--  Add new columns

ALTER TABLE "message" ADD COLUMN "message_type" character(1) DEFAULT 'E' NOT NULL;

ALTER TABLE "message" ADD COLUMN "from_address" character varying(60);

ALTER TABLE "message_to" ADD COLUMN "to_address" character varying(60);

ALTER TABLE "message_to" ADD COLUMN "to_name" character varying(60);

ALTER TABLE "message_to" ADD COLUMN "date_received" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL;

ALTER TABLE "message" ALTER COLUMN "created_date" SET DEFAULT CURRENT_TIMESTAMP;

--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "message" ADD CONSTRAINT "message_type_chk" CHECK (((message_type='E')OR(message_type='T')));

CREATE UNIQUE INDEX "message_person_id_show_created_date_uindex" ON "message" USING btree ("from_person_id", "from_show", "created_date" DESC);

CREATE INDEX "message_to_main_idx" ON "message_to" USING btree ("to_person_id", "to_show", "date_received" DESC);

CREATE INDEX "message_to_message_id_idx" ON "message_to" USING btree ("message_id");

COMMENT ON COLUMN "message"."message_type" IS '(E)mail or (T)ext message';



