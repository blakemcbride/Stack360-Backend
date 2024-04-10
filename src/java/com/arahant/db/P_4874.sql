
-- PostgreSQL patch from revision 4873 to revision 4874


--  Remove indexes and checks


--  Add new tables


--  Add new columns

delete from removed_from_roster;

ALTER TABLE "removed_from_roster" ADD COLUMN "shift_id" character(16) NOT NULL;


--  Change existing columns

ALTER TABLE "removed_from_roster" ALTER COLUMN "when_removed" SET DEFAULT CURRENT_TIMESTAMP;


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_rfr_shift_fkey" ON "removed_from_roster" USING btree ("shift_id");


ALTER TABLE ONLY "removed_from_roster" ADD CONSTRAINT "rfr_shift_fkey" FOREIGN KEY ("shift_id") REFERENCES "project_shift" ("project_shift_id");

