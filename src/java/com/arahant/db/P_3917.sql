
-- PostgreSQL patch from revision 3916 to revision 3917


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "worker_confirmation" (
	"worker_confirmation_id" character(16) NOT NULL,
	"person_id" character(16) NOT NULL,
	"project_id" character(16) NOT NULL,
	"confirmation_time" timestamp with time zone NOT NULL,
	"latitude" double precision DEFAULT 0 NOT NULL,
	"longitude" double precision DEFAULT 0 NOT NULL
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE UNIQUE INDEX "worker_confirmation_person_idx" ON "worker_confirmation" USING btree ("person_id", "confirmation_time");

ALTER TABLE ONLY "worker_confirmation" ADD CONSTRAINT "worker_confirmation_pkey" PRIMARY KEY ("worker_confirmation_id");

CREATE INDEX "worker_confirmation_project_idx" ON "worker_confirmation" USING btree ("project_id", "confirmation_time");

ALTER TABLE ONLY "worker_confirmation" ADD CONSTRAINT "worker_confirmation_person_fk" FOREIGN KEY ("person_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "worker_confirmation" ADD CONSTRAINT "worker_confirmation_project_fk" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

