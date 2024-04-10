
-- PostgreSQL patch from revision 2012 to revision 2017



--  Remove indexes and checks


--  Add new tables

CREATE TABLE "person_email" (
	"person_email_id" character(16) NOT NULL,
	"sent_to" character varying(40) NOT NULL,
	"subject" character varying(100) NOT NULL,
	"message" text,
	"person_id" character(16) NOT NULL,
	"date_sent" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "person_email_main_idx" ON "person_email" USING btree ("person_id", "date_sent");

ALTER TABLE ONLY "person_email" ADD CONSTRAINT "person_email_pkey" PRIMARY KEY ("person_email_id");

ALTER TABLE ONLY "person_email" ADD CONSTRAINT "person_email_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");
