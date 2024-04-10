
-- PostgreSQL patch from revision 3374 to revision 3375


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "person" ADD COLUMN "i9p1_confirmation" character(14);

ALTER TABLE "person" ADD COLUMN "i9p2_confirmation" character(14);

ALTER TABLE "person" ADD COLUMN "i9p1_person" character(16);

ALTER TABLE "person" ADD COLUMN "i9p2_person" character(16);

ALTER TABLE "person" ADD COLUMN "i9p1_when" timestamp without time zone;

ALTER TABLE "person" ADD COLUMN "i9p2_when" timestamp without time zone;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_person_i9p1_person_fky" ON "person" USING btree ("i9p1_person");

CREATE INDEX "fki_person_i9p2_person_fkey" ON "person" USING btree ("i9p2_person");

ALTER TABLE ONLY "person" ADD CONSTRAINT "person_i9p1_person_fky" FOREIGN KEY ("i9p1_person") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "person" ADD CONSTRAINT "person_i9p2_person_fkey" FOREIGN KEY ("i9p2_person") REFERENCES "person" ("person_id");


