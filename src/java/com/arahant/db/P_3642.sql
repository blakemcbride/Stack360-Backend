
-- PostgreSQL patch from revision 3640 to revision 3642


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "text_message" (
	"text_message_id" character(16) NOT NULL,
	"when_sent" timestamp with time zone NOT NULL,
	"person_id" character(16) NOT NULL,
	"phone_number" character varying(20) NOT NULL,
	"message" character varying(320) NOT NULL,
	"message_id" character varying(40) NOT NULL
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE UNIQUE INDEX "text_message_person_idx" ON "text_message" USING btree ("person_id", "when_sent");

ALTER TABLE ONLY "text_message" ADD CONSTRAINT "text_message_pkey" PRIMARY KEY ("text_message_id");

COMMENT ON COLUMN "text_message"."message_id" IS 'This is the message id that is returned from the vendor';

COMMENT ON TABLE "text_message" IS 'Sent text (SMS) messages';

ALTER TABLE ONLY "text_message" ADD CONSTRAINT "text_message_person_fky" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");
