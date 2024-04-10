
-- PostgreSQL patch from revision 4815 to revision 4816


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "recurring_schedule" (
	"recurring_schedule_id" character(16) NOT NULL,
	"type" smallint NOT NULL,
	"month" smallint,
	"day" smallint,
	"day_of_week" smallint,
	"n" smallint,
	"ending_date" integer
);


--  Add new columns

ALTER TABLE "project_task_detail" ADD COLUMN "recurring_schedule_id" character(16);


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_project_task_detail_recurring_fkey" ON "project_task_detail" USING btree ("recurring_schedule_id");

ALTER TABLE ONLY "recurring_schedule" ADD CONSTRAINT "recurring_pkey" PRIMARY KEY ("recurring_schedule_id");

COMMENT ON COLUMN "recurring_schedule"."day" IS '1-31';

COMMENT ON COLUMN "recurring_schedule"."day_of_week" IS '1-7';

COMMENT ON COLUMN "recurring_schedule"."ending_date" IS 'YYYYMMDD';

COMMENT ON COLUMN "recurring_schedule"."month" IS '1 - January
2 - February
3 - March
...';

COMMENT ON COLUMN "recurring_schedule"."n" IS 'Used in [n] [day_of_week]
like 3rd Tuesday of each month';

COMMENT ON COLUMN "recurring_schedule"."type" IS '1 - specific month and day every year
2 - a particular day of each month
3 - last day of each month
4 - X day of Y week in each month
5 - particular day of each week
6 - every weekday
7 - everyday
8 - every X days';

COMMENT ON TABLE "recurring_schedule" IS 'Details for recurring tasks';

ALTER TABLE ONLY "project_task_detail" ADD CONSTRAINT "project_task_detail_recurring_fkey" FOREIGN KEY ("recurring_schedule_id") REFERENCES "recurring_schedule" ("recurring_schedule_id");

