
-- PostgreSQL patch from revision 2760 to revision 2761




--  Remove indexes and checks


--  Add new tables

CREATE TABLE "project_subtype" (
	"project_subtype_id" character(16) NOT NULL,
	"code" character varying(20) NOT NULL,
	"description" character varying(80),
	"scope" character(1) DEFAULT 'G' NOT NULL,
	"company_id" character(16),
	"last_active_date" integer DEFAULT 0 NOT NULL,
	CONSTRAINT "project_subtype_scope_chk" CHECK (((scope='G')OR(scope='I')))
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_subtype_comp_fkey" ON "project_subtype" USING btree ("company_id");

ALTER TABLE ONLY "project_subtype" ADD CONSTRAINT "project_subtype_pkey" PRIMARY KEY ("project_subtype_id");

COMMENT ON COLUMN "project_subtype"."company_id" IS 'The company this applies to or NULL if global';

COMMENT ON COLUMN "project_subtype"."last_active_date" IS 'Last date this record should be used in a new association.  0 means no end.';

COMMENT ON COLUMN "project_subtype"."scope" IS 'G = Globally accessible
I = Internal or private to a single company';

ALTER TABLE ONLY "project_subtype" ADD CONSTRAINT "project_subtype_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

