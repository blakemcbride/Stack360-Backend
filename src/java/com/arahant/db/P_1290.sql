
-- PostgreSQL patch from revision 1289 to revision 1290



--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "client" ADD COLUMN "default_project_code" character varying(12);

ALTER TABLE "project" ADD COLUMN "project_code" character varying(12);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "client"."default_project_code" IS 'Defaults into project.project_code';

COMMENT ON COLUMN "project"."project_code" IS 'Arbitrary code used external to the system';

