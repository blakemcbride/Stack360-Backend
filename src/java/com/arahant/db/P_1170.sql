
-- PostgreSQL patch from revision 1169 to revision 1170



--  Remove indexes and checks


--  Add new tables

CREATE TABLE "screen_history" (
	"transaction_id" serial NOT NULL,
	"person_id" character(16) NOT NULL,
	"screen_id" character(16) NOT NULL,
	"time_in" timestamp without time zone NOT NULL,
	"time_out" timestamp without time zone
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "screen_history_person_idx" ON "screen_history" USING btree ("person_id", "time_out", "NULLS", "FIRST");

ALTER TABLE ONLY "screen_history" ADD CONSTRAINT "screen_history_pkey" PRIMARY KEY ("transaction_id");

CREATE INDEX "screen_history_screen_idx" ON "screen_history" USING btree ("screen_id");

ALTER TABLE ONLY "screen_history" ADD CONSTRAINT "screen_history_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "screen_history" ADD CONSTRAINT "screen_history_screen_fkey" FOREIGN KEY ("screen_id") REFERENCES "screen" ("screen_id");
