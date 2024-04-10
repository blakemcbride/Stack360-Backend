
-- PostgreSQL patch from revision 4932 to revision 4933


--  Remove indexes and checks

DROP INDEX "message_person_id_show_created_date_uindex";


--  Add new tables


--  Add new columns

ALTER TABLE "message" ADD COLUMN "send_internal" character(1) DEFAULT 'N' NOT NULL;


--  Change existing columns

ALTER TABLE "screen" ALTER COLUMN "technology" SET DEFAULT 'H';


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "message" ADD CONSTRAINT "message_send_internal_chk" CHECK (((send_internal='Y')OR(send_internal='N')));

CREATE INDEX "message_person_id_show_created_date_uindex" ON "message" USING btree ("from_person_id", "from_show", "created_date" DESC);


