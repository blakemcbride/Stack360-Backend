
-- PostgreSQL patch from revision 4393 to revision 4394


--  Remove indexes and checks

DROP INDEX "fki_hr_pos_org_fkey";


--  Add new tables


--  Add new columns

ALTER TABLE "hr_position" ADD COLUMN "seqno" smallint DEFAULT 0 NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "hr_pos_org_idx" ON "hr_position" USING btree ("org_group_id", "seqno");


