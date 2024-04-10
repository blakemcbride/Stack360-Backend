
-- PostgreSQL patch from revision 2538 to revision 2543


--  Remove indexes and checks

ALTER TABLE ONLY "zip_code_distance" DROP CONSTRAINT "zip_code_distance_pk";


--  Add new tables

CREATE TABLE "zipcode_location" (
	"zipcode" character(5) NOT NULL,
	"state_abr" character(2) NOT NULL,
	"city" character varying(30) NOT NULL,
	"state" character varying(25) NOT NULL,
	"latitude" double precision NOT NULL,
	"longitude" double precision NOT NULL
);


--  Add new columns


--  Change existing columns


--  Remove tables

DROP TABLE "zip_code_distance";


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "zipcode_location" ADD CONSTRAINT "zipcode_location_pkey" PRIMARY KEY ("zipcode");


