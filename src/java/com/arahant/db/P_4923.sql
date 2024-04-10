
-- PostgreSQL patch from revision 4922 to revision 4923


--  Remove indexes and checks

DROP INDEX if exists "phone_email_idx";


--  Add new tables


--  Add new columns


--  Change existing columns

DROP INDEX if exists "message_person_id_show_created_date_uindex";

ALTER TABLE "message" ALTER COLUMN "from_person_id" DROP NOT NULL;

--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "message_person_id_show_created_date_uindex" ON "message" USING btree ("from_person_id", "from_show", "created_date" DESC);

CREATE INDEX "person_email_idx" ON "person" USING btree (lower("personal_email"));


