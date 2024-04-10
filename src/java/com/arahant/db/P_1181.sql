
-- PostgreSQL patch from revision 1180 to revision 1181



--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "hr_note_category" ADD COLUMN "cat_code" character varying(5);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

COMMENT ON COLUMN "hr_note_category"."cat_code" IS 'Code used to refer to the field from other areas';


