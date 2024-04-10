
-- PostgreSQL patch from revision 5144 to revision 5145


--  Remove indexes and checks

DROP INDEX "app_pos_info_pidx";

ALTER TABLE ONLY "applicant_position_info" DROP CONSTRAINT "app_pos_info_pkey";

DROP INDEX "fki_app_pos_info_pos";


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables

DROP TABLE "applicant_position_info";


--  Drop columns


--  Add new indexes and checks


