
-- PostgreSQL patch from revision 3576 to revision 3577


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "message" ADD COLUMN "dont_send_body" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "message" ADD COLUMN "send_email" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "message" ADD COLUMN "send_text" character(1) DEFAULT 'N' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "message" ADD CONSTRAINT "message_dont_send_body_chk" CHECK (((dont_send_body='Y')OR(dont_send_body='N')));

ALTER TABLE "message" ADD CONSTRAINT "message_send_email_chk" CHECK (((send_email='Y')OR(send_email='N')));

ALTER TABLE "message" ADD CONSTRAINT "message_send_text_chk" CHECK (((send_text='Y')OR(send_text='N')));

COMMENT ON COLUMN "message"."dont_send_body" IS 'Y=don''t send body of message over email, N=nornal';

COMMENT ON COLUMN "message"."send_email" IS 'send via email too';

COMMENT ON COLUMN "message"."send_text" IS 'Send via text message too';

