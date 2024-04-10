
-- PostgreSQL patch from revision 2923 to revision 2924

--  Remove indexes and checks

DROP INDEX "hr_empl_stat_hist_idx";


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE UNIQUE INDEX "hr_empl_stat_hist_idx" ON "hr_empl_status_history" USING btree ("employee_id", "effective_date");

