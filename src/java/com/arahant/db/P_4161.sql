
-- PostgreSQL patch from revision 4160 to revision 4161


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "worker_confirmation" ADD COLUMN "who_added" character(16);

ALTER TABLE "worker_confirmation" ADD COLUMN "notes" character varying(256);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_worker_confirmation_who_added_fk" ON "worker_confirmation" USING btree ("who_added");

COMMENT ON COLUMN "worker_confirmation"."who_added" IS 'Who performed the check in.  NULL means person_id did the checkin';

ALTER TABLE ONLY "worker_confirmation" ADD CONSTRAINT "worker_confirmation_who_added_fk" FOREIGN KEY ("who_added") REFERENCES "employee" ("person_id");

