
-- PostgreSQL patch from revision 3278 to revision 3279

-- since we are adding a non-null column, we have to delete old records
delete from applicant_contact;

--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "applicant_contact" ADD COLUMN "who_added" character(16) NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_applicant_contact_who_added_fkey" ON "applicant_contact" USING btree ("who_added");

ALTER TABLE ONLY "applicant_contact" ADD CONSTRAINT "applicant_contact_who_added_fkey" FOREIGN KEY ("who_added") REFERENCES "person" ("person_id");
