
-- PostgreSQL patch from revision 3529 to revision 3530


--  Remove indexes and checks

DROP INDEX "fki_emp_rate_ratetyp_fkey";


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE UNIQUE INDEX "employee_rate_type_idx" ON "employee_rate" USING btree ("rate_type_id", "person_id");

