
-- PostgreSQL patch from revision 3333 to revision 3337



--  Remove indexes and checks


--  Add new tables

CREATE TABLE "reminder" (
	"reminder_id" character(16) NOT NULL,
	"person_id" character(16),
	"about_person" character(16),
	"about_project" character(16),
	"reminder_date" integer DEFAULT 0 NOT NULL,
	"summary" character varying(80) NOT NULL,
	"detail" character varying(4096),
	"status" character(1) NOT NULL,
	"who_added" character(16),
	"date_added" integer DEFAULT 0 NOT NULL,
	"time_added" integer DEFAULT 0 NOT NULL,
	"who_inactivated" character(16),
	"date_inactive" integer DEFAULT 0 NOT NULL,
	"time_inactive" integer DEFAULT 0 NOT NULL,
	CONSTRAINT "reminder_status_chk" CHECK (((status='A')OR(status='I')))
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_reminder_about_person_fkey" ON "reminder" USING btree ("about_person");

CREATE INDEX "fki_reminder_about_project_fkey" ON "reminder" USING btree ("about_project");

CREATE INDEX "fki_reminder_person_fkey" ON "reminder" USING btree ("person_id");

CREATE INDEX "fki_reminder_who_added_fkey" ON "reminder" USING btree ("who_added");

CREATE INDEX "fki_reminder_who_inactivated_fkey" ON "reminder" USING btree ("who_inactivated");

ALTER TABLE ONLY "reminder" ADD CONSTRAINT "reminder_pkey" PRIMARY KEY ("reminder_id");

COMMENT ON COLUMN "reminder"."about_person" IS 'This is the person the reminder is about.  If it''s not about a particular person this will be NULL.';

COMMENT ON COLUMN "reminder"."about_project" IS 'This is the project the reminder is about.  I NULL, it''s not about a particular project.';

COMMENT ON COLUMN "reminder"."date_added" IS 'YYYYMMDD';

COMMENT ON COLUMN "reminder"."date_inactive" IS 'YYYYMMDD';

COMMENT ON COLUMN "reminder"."person_id" IS 'This is the person the reminder is for.  If NULL it is for anyone.';

COMMENT ON COLUMN "reminder"."reminder_date" IS 'The date the reminder becomes active.  0 = now.';

COMMENT ON COLUMN "reminder"."status" IS 'Active / Inactive';

COMMENT ON COLUMN "reminder"."time_added" IS 'HHMM';

COMMENT ON COLUMN "reminder"."time_inactive" IS 'HHMM';

ALTER TABLE ONLY "reminder" ADD CONSTRAINT "reminder_about_person_fkey" FOREIGN KEY ("about_person") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "reminder" ADD CONSTRAINT "reminder_about_project_fkey" FOREIGN KEY ("about_project") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "reminder" ADD CONSTRAINT "reminder_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "reminder" ADD CONSTRAINT "reminder_who_added_fkey" FOREIGN KEY ("who_added") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "reminder" ADD CONSTRAINT "reminder_who_inactivated_fkey" FOREIGN KEY ("who_inactivated") REFERENCES "person" ("person_id");

