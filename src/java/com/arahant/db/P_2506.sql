
-- PostgreSQL patch from revision 2505 to revision 2506


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "expense_week_paid_idx" ON "expense" USING btree ("week_paid_for");

