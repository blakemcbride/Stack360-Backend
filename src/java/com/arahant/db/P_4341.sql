
-- PostgreSQL patch from revision 4339 to revision 4341


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "project_task_detail" (
	"project_task_detail_id" character(16) NOT NULL,
	"project_task_list_id" character(16) NOT NULL,
	"seqno" smallint NOT NULL,
	"title" character varying(80) NOT NULL,
	"description" character varying(1000),
	"assigned" character(1) DEFAULT 'N' NOT NULL,
	"comments" character varying(8000),
	"missing_items" character varying(1000),
	"status" smallint DEFAULT 0 NOT NULL,
	CONSTRAINT "project_task_detail_assigned_chk" CHECK (((assigned='Y')OR(assigned='N')))
);

CREATE TABLE "project_task_list" (
	"project_task_list_id" character(16) NOT NULL,
	"project_shift_id" character(16) NOT NULL,
	"task_date" integer NOT NULL
);

CREATE TABLE "project_task_picture" (
	"project_task_picture_id" character(16) NOT NULL,
	"project_task_detail_id" character(16) NOT NULL,
	"seqno" smallint NOT NULL,
	"when_uploaded" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"picture_path" character varying(120) NOT NULL
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE UNIQUE INDEX "project_task_detail_list_idx" ON "project_task_detail" USING btree ("project_task_list_id", "seqno");

ALTER TABLE ONLY "project_task_detail" ADD CONSTRAINT "project_task_detail_pkey" PRIMARY KEY ("project_task_detail_id");

ALTER TABLE ONLY "project_task_list" ADD CONSTRAINT "project_task_list_pkey" PRIMARY KEY ("project_task_list_id");

CREATE UNIQUE INDEX "project_task_picture_pic_idx" ON "project_task_picture" USING btree ("project_task_detail_id", "seqno");

ALTER TABLE ONLY "project_task_picture" ADD CONSTRAINT "project_task_picture_pkey" PRIMARY KEY ("project_task_picture_id");

CREATE UNIQUE INDEX "project_task_project_idx" ON "project_task_list" USING btree ("project_shift_id", "task_date");

COMMENT ON COLUMN "project_task_detail"."assigned" IS 'Yes/No';

COMMENT ON COLUMN "project_task_list"."task_date" IS 'YYYYMMDD';



ALTER TABLE ONLY "project_task_detail" ADD CONSTRAINT "project_task_detail_task_fky" FOREIGN KEY ("project_task_list_id") REFERENCES "project_task_list" ("project_task_list_id");

ALTER TABLE ONLY "project_task_picture" ADD CONSTRAINT "project_task_picture_task_fkey" FOREIGN KEY ("project_task_detail_id") REFERENCES "project_task_detail" ("project_task_detail_id");

ALTER TABLE ONLY "project_task_list" ADD CONSTRAINT "project_task_project_fki" FOREIGN KEY ("project_shift_id") REFERENCES "project_shift" ("project_shift_id");


