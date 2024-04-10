
-- PostgreSQL patch from revision 2116 to revision 2119

update timesheet set message_id = null;

DELETE FROM message;  --  delete all old records


--  Remove indexes and checks

DROP INDEX "fki_messages_to_person";


--  Add new tables

CREATE TABLE "message_attachment" (
	"message_attachment_id" character(16) NOT NULL,
	"message_id" character(16) NOT NULL,
	"source_file_name" character varying(80) NOT NULL,
	"attachment" bytea NOT NULL
);

CREATE TABLE "message_to" (
	"message_to_id" character(16) NOT NULL,
	"message_id" character(16) NOT NULL,
	"to_person_id" character(16) NOT NULL,
	"send_type" character(1) NOT NULL,
	"to_show" character(1) DEFAULT 'Y' NOT NULL,
	"sent" character(1) NOT NULL,
	"when_viewed" timestamp with time zone,
	CONSTRAINT "message_to_sent_chk" CHECK (((sent='Y')OR(sent='N'))),
	CONSTRAINT "message_to_show_chk" CHECK (((to_show='Y')OR(to_show='N'))),
	CONSTRAINT "message_to_type_chk" CHECK (((send_type='T')OR(send_type='C')OR(send_type='B')))
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE "message" DROP COLUMN "to_person_id";

ALTER TABLE "message" DROP CONSTRAINT "to_show_chk";

ALTER TABLE "message" DROP COLUMN "to_show";


--  Add new indexes and checks

CREATE INDEX "fki_message_attachment_message_id_fkey" ON "message_attachment" USING btree ("message_id");

CREATE INDEX "fki_message_to_person_id_fkey" ON "message_to" USING btree ("to_person_id");

ALTER TABLE ONLY "message_attachment" ADD CONSTRAINT "message_attachment_pkey" PRIMARY KEY ("message_attachment_id");

ALTER TABLE ONLY "message_to" ADD CONSTRAINT "message_to_pkey" PRIMARY KEY ("message_to_id");

COMMENT ON COLUMN "message_to"."send_type" IS 'To/Cc/Bcc';

COMMENT ON COLUMN "message_to"."sent" IS 'Y/N - N means wasn''t able to send over regular email';

COMMENT ON COLUMN "message_to"."to_show" IS 'Y/N - does user still want to see this message?';

--


ALTER TABLE ONLY "message_to" ADD CONSTRAINT "message_to_message_id_fkey" FOREIGN KEY ("message_id") REFERENCES "message" ("message_id");

ALTER TABLE ONLY "message_to" ADD CONSTRAINT "message_to_person_id_fkey" FOREIGN KEY ("to_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "message_attachment" ADD CONSTRAINT "message_attachment_message_id_fkey" FOREIGN KEY ("message_id") REFERENCES "message" ("message_id");


