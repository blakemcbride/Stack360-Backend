
-- PostgreSQL patch from revision 4866 to revision 4867


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "removed_from_roster" (
	"rfr_id" character(16) NOT NULL,
	"person_id" character(16) NOT NULL,
	"supervisor_id" character(16) NOT NULL,
	"when_removed" timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
	"description" character varying(80),
	"comments" text
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_rfr_person_fkey" ON "removed_from_roster" USING btree ("person_id");

CREATE INDEX "fki_rfr_supervisor_fkey" ON "removed_from_roster" USING btree ("supervisor_id");

ALTER TABLE ONLY "removed_from_roster" ADD CONSTRAINT "removed_from_roster_pkey" PRIMARY KEY ("rfr_id");

COMMENT ON COLUMN "removed_from_roster"."person_id" IS 'The person being removed from the roster';

COMMENT ON COLUMN "removed_from_roster"."supervisor_id" IS 'The supervisor who is removing the person';

COMMENT ON TABLE "removed_from_roster" IS 'People removed from the roster from the field';

ALTER TABLE ONLY "removed_from_roster" ADD CONSTRAINT "rfr_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "removed_from_roster" ADD CONSTRAINT "rfr_supervisor_fkey" FOREIGN KEY ("supervisor_id") REFERENCES "person" ("person_id");

