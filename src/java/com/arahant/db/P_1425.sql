
-- PostgreSQL patch from revision 1424 to revision 1425


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "zip_code_distance" (
	"lower_zip_code" integer NOT NULL,
	"higher_zip_code" integer NOT NULL,
	"distance" smallint NOT NULL
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "zip_code_distance" ADD CONSTRAINT "zip_code_distance_pk" PRIMARY KEY ("lower_zip_code", "higher_zip_code");

