
-- PostgreSQL patch from revision 1965 to revision 1966


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "email_notifications" (
	"email_notification_id" character(16) NOT NULL,
	"notification_type" character(1) DEFAULT 'W' NOT NULL,
	"name" character varying(50) NOT NULL,
	"title" character varying(40),
	"email" character varying(50) NOT NULL,
	CONSTRAINT "email_notification_type_chk" CHECK ((notification_type='W'))
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "email_notifications" ADD CONSTRAINT "email_notifications_pk" PRIMARY KEY ("email_notification_id");

COMMENT ON COLUMN "email_notifications"."notification_type" IS 'W = worker writeup';

