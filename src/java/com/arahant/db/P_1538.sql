
-- PostgreSQL patch from revision 1536 to revision 1538


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "project_history_person" ADD COLUMN "date_unassigned" integer DEFAULT 0 NOT NULL;

ALTER TABLE "project_history_person" ADD COLUMN "time_unassigned" integer DEFAULT -1 NOT NULL;

ALTER TABLE "project_history_person" ADD COLUMN "reason_unassigned" character(1) DEFAULT ' ' NOT NULL;

ALTER TABLE "project_history_person" ADD COLUMN "unassigned_comment" character varying(120);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "project_history_person" ADD CONSTRAINT "project_hist_per_reason_chk" CHECK ((((((((reason_unassigned=' ')OR(reason_unassigned=' 1'))OR(reason_unassigned=' 2'))OR(reason_unassigned='3 '))OR(reason_unassigned='4 '))OR(reason_unassigned=' 5'))OR(reason_unassigned='0 ')));

COMMENT ON COLUMN "project_history_person"."reason_unassigned" IS 'blank = no reason given
1 = No show
2 = nonproductive
3 = insubordinate
4 = client request
5 = employee request
0 = other
';


