
-- PostgreSQL patch from revision 2437 to revision 2439


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "person" ADD COLUMN "authenticated_email" character varying(50);

ALTER TABLE "person" ADD COLUMN "default_email_sender" character(1) DEFAULT 'N' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "person" ADD CONSTRAINT "person_default_email_chk" CHECK (((default_email_sender='Y')OR(default_email_sender='N')));

CREATE INDEX "person_default_email_idx" ON "person" USING btree ("default_email_sender");

COMMENT ON COLUMN "person"."authenticated_email" IS 'This email address has been authorized as a valid sender through the email system.';

COMMENT ON COLUMN "person"."default_email_sender" IS '''Y'' if this is the default email sender.  Only one record should be ''Y''.  Basically, sent email uses a person''s authenticated_email.  If they''re not authenticated, the default email address is used as the sender.';

