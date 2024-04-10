
-- PostgreSQL patch from revision 2588 to revision 2589


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project" ADD COLUMN "required_workers" smallint DEFAULT 0 NOT NULL;

ALTER TABLE "project" ADD COLUMN "who_added" character(16);

ALTER TABLE "project" ADD COLUMN "when_added" timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL;


UPDATE project SET who_added='00000-0000000000', when_added='2000-01-01 00:00:00'; -- Arahant user


ALTER TABLE "project" ALTER COLUMN "who_added" SET NOT NULL;




--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_who_added_fk" ON "project" USING btree ("who_added");

COMMENT ON COLUMN "project"."required_workers" IS 'Number of required workers for project';

COMMENT ON COLUMN "project"."when_added" IS 'When was this project created';

COMMENT ON COLUMN "project"."who_added" IS 'Who added this project to the system';

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_who_added_fk" FOREIGN KEY ("who_added") REFERENCES "person" ("person_id");

