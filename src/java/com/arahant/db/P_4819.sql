
-- PostgreSQL patch from revision 4818 to revision 4819


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns

ALTER TABLE ONLY "login_log" DROP CONSTRAINT "login_log_pkey";

ALTER TABLE "login_log" ALTER COLUMN "log_name" TYPE character varying(50);

--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "login_log" ADD CONSTRAINT "login_log_pkey" PRIMARY KEY ("ltime", "log_name");

