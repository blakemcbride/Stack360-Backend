
-- PostgreSQL patch from revision 3806 to revision 3807

--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns

DROP INDEX "address_cr_main_idx";

DROP INDEX "hr_empl_dep_cr_main_idx";

DROP INDEX "person_cr_main_idx";

DROP INDEX "phone_cr_main_idx";

ALTER TABLE "address_cr" ALTER COLUMN "request_time" TYPE timestamp with time zone;
ALTER TABLE "address_cr" ALTER COLUMN "approval_time" TYPE timestamp with time zone;
ALTER TABLE "agent_join" ALTER COLUMN "approved_date" TYPE timestamp with time zone;
ALTER TABLE "agreement_form" ALTER COLUMN "form_date" TYPE timestamp with time zone;
ALTER TABLE "agreement_person_join" ALTER COLUMN "agreement_time" TYPE timestamp with time zone;
ALTER TABLE "alert" ALTER COLUMN "last_chage_datetime" TYPE timestamp with time zone;
DROP INDEX "benefit_ansh_main_idx";

DROP INDEX "benefit_quesh_main_idx";

DROP INDEX "hr_benefit_join_h_idx";

DROP INDEX "hr_emp_beneficiary_h_idx";

DROP INDEX "item_h_item_idx";

DROP INDEX "org_grpass_h_ss_idx";

DROP INDEX "person_h_person_idx";

DROP INDEX "prospect_h_pidx";

ALTER TABLE "benefit_answer" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "benefit_answer_h" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "benefit_question" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "benefit_question_h" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "company_question_detail" ALTER COLUMN "when_added" TYPE timestamp with time zone;
ALTER TABLE "contact_question_detail" ALTER COLUMN "when_added" TYPE timestamp with time zone;
DROP INDEX "life_event_main_idx";

ALTER TABLE "drc_form_event" ALTER COLUMN "event_date" TYPE timestamp with time zone;
ALTER TABLE "drc_import_benefit_join" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "drc_import_benefit_join_h" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "drc_import_enrollee" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "drc_import_enrollee_h" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "dynace_mutex" ALTER COLUMN "lastupdate" TYPE timestamp with time zone;
ALTER TABLE "edi_transaction" ALTER COLUMN "transaction_datetime" TYPE timestamp with time zone;
ALTER TABLE "expense_receipt" ALTER COLUMN "when_uploaded" TYPE timestamp with time zone;
ALTER TABLE "expense_receipt" ALTER COLUMN "when_approved" TYPE timestamp with time zone;
ALTER TABLE "hr_benefit_join" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "hr_benefit_join_h" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "hr_empl_dependent_cr" ALTER COLUMN "request_time" TYPE timestamp with time zone;
ALTER TABLE "hr_empl_dependent_cr" ALTER COLUMN "approval_time" TYPE timestamp with time zone;
ALTER TABLE "hr_empl_status_history" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "hr_employee_beneficiary" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "hr_employee_beneficiary_h" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
DROP INDEX "interface_log_comp_idx";

DROP INDEX "interface_log_pidx";

ALTER TABLE "interface_log" ALTER COLUMN "last_run" TYPE timestamp with time zone;
ALTER TABLE "invoice" ALTER COLUMN "create_date" TYPE timestamp with time zone;
ALTER TABLE "invoice" ALTER COLUMN "export_date" TYPE timestamp with time zone;
ALTER TABLE "item" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "item_h" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "item_inspection" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE ONLY "login_log" DROP CONSTRAINT "login_log_pkey";

ALTER TABLE "login_log" ALTER COLUMN "ltime" TYPE timestamp with time zone;
ALTER TABLE "message" ALTER COLUMN "created_date" TYPE timestamp with time zone;
ALTER TABLE "org_group_association" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "org_group_association_h" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "overtime_approval" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "person" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "person" ALTER COLUMN "agreement_date" TYPE timestamp with time zone;
ALTER TABLE "person" ALTER COLUMN "i9p1_when" TYPE timestamp with time zone;
ALTER TABLE "person" ALTER COLUMN "i9p2_when" TYPE timestamp with time zone;
DROP INDEX "person_chgreq_main_key";

ALTER TABLE "person_change_request" ALTER COLUMN "request_date" TYPE timestamp with time zone;
ALTER TABLE "person_changed" ALTER COLUMN "earliest_change_date" TYPE timestamp with time zone;
ALTER TABLE "person_cr" ALTER COLUMN "request_time" TYPE timestamp with time zone;
ALTER TABLE "person_cr" ALTER COLUMN "approval_time" TYPE timestamp with time zone;
ALTER TABLE "person_h" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "person_h" ALTER COLUMN "agreement_date" TYPE timestamp with time zone;
ALTER TABLE "phone_cr" ALTER COLUMN "request_time" TYPE timestamp with time zone;
ALTER TABLE "phone_cr" ALTER COLUMN "approval_time" TYPE timestamp with time zone;
DROP INDEX "process_history_proc_idx";

DROP INDEX "process_history_time_idx";

ALTER TABLE "process_history" ALTER COLUMN "run_time" TYPE timestamp with time zone;
ALTER TABLE "project" ALTER COLUMN "approving_timestamp" TYPE timestamp with time zone;
ALTER TABLE "project" ALTER COLUMN "last_report_date" TYPE timestamp with time zone;
ALTER TABLE "project_checklist_detail" ALTER COLUMN "entry_timestamp" TYPE timestamp with time zone;
DROP INDEX "project_comment_main_idx";

ALTER TABLE "project_comment" ALTER COLUMN "date_entered" TYPE timestamp with time zone;
ALTER TABLE "prospect" ALTER COLUMN "when_added" TYPE timestamp with time zone;
ALTER TABLE "prospect" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "prospect" ALTER COLUMN "status_change_date" TYPE timestamp with time zone;
ALTER TABLE "prospect_h" ALTER COLUMN "when_added" TYPE timestamp with time zone;
ALTER TABLE "prospect_h" ALTER COLUMN "record_change_date" TYPE timestamp with time zone;
ALTER TABLE "prospect_h" ALTER COLUMN "status_change_date" TYPE timestamp with time zone;
ALTER TABLE "prospect_log" ALTER COLUMN "entry_date" TYPE timestamp with time zone;
DROP INDEX "qb_main_key";

ALTER TABLE "quickbooks_sync" ALTER COLUMN "interface_time" TYPE timestamp with time zone;
ALTER TABLE "quote_table" ALTER COLUMN "created_date" TYPE timestamp with time zone;
ALTER TABLE "quote_table" ALTER COLUMN "finalized_date" TYPE timestamp with time zone;
ALTER TABLE "quote_table" ALTER COLUMN "accepted_date" TYPE timestamp with time zone;
DROP INDEX "sales_points_pidx";

ALTER TABLE "sales_points" ALTER COLUMN "point_date" TYPE timestamp with time zone;
ALTER TABLE "screen_history" ALTER COLUMN "time_in" TYPE timestamp with time zone;

ALTER TABLE "screen_history" ALTER COLUMN "time_out" TYPE timestamp with time zone;
ALTER TABLE "time_off_request" ALTER COLUMN "request_date" TYPE timestamp with time zone;
ALTER TABLE "time_off_request" ALTER COLUMN "approval_date" TYPE timestamp with time zone;
ALTER TABLE "timesheet" ALTER COLUMN "beginning_entry_date" TYPE timestamp with time zone;
ALTER TABLE "timesheet" ALTER COLUMN "end_entry_date" TYPE timestamp with time zone;
ALTER TABLE "wizard_project" ALTER COLUMN "date_completed" TYPE timestamp with time zone;

--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "address_cr_main_idx" ON "address_cr" USING btree ("real_record_id", "change_status", "request_time");

CREATE INDEX "benefit_ansh_main_idx" ON "benefit_answer_h" USING btree ("benefit_answer_id", "record_change_date");

CREATE INDEX "benefit_quesh_main_idx" ON "benefit_question_h" USING btree ("benefit_question_id", "record_change_date");

CREATE INDEX "hr_benefit_join_h_idx" ON "hr_benefit_join_h" USING btree ("benefit_join_id", "record_change_date");

CREATE INDEX "hr_emp_beneficiary_h_idx" ON "hr_employee_beneficiary_h" USING btree ("beneficiary_id", "record_change_date");

CREATE INDEX "hr_empl_dep_cr_main_idx" ON "hr_empl_dependent_cr" USING btree ("real_record_id", "change_status", "request_time");

CREATE INDEX "interface_log_comp_idx" ON "interface_log" USING btree ("company_id", "last_run");

CREATE INDEX "interface_log_pidx" ON "interface_log" USING btree ("interface_code", "last_run");

CREATE INDEX "item_h_item_idx" ON "item_h" USING btree ("item_id", "record_change_date");

CREATE INDEX "life_event_main_idx" ON "life_event" USING btree ("person_id", "event_date");

ALTER TABLE ONLY "login_log" ADD CONSTRAINT "login_log_pkey" PRIMARY KEY ("ltime", "log_name");

CREATE INDEX "org_grpass_h_ss_idx" ON "org_group_association_h" USING btree ("person_id", "org_group_id", "record_change_date");

CREATE INDEX "person_chgreq_main_key" ON "person_change_request" USING btree ("person_id", "request_type", "request_date");

CREATE INDEX "person_cr_main_idx" ON "person_cr" USING btree ("real_record_id", "change_status", "request_time");

CREATE INDEX "person_h_person_idx" ON "person_h" USING btree ("person_id", "record_change_date");

CREATE INDEX "phone_cr_main_idx" ON "phone_cr" USING btree ("real_record_id", "change_status", "request_time");

CREATE INDEX "process_history_proc_idx" ON "process_history" USING btree ("process_schedule_id", "run_time");

CREATE INDEX "process_history_time_idx" ON "process_history" USING btree ("run_time");

CREATE INDEX "project_comment_main_idx" ON "project_comment" USING btree ("project_id", "date_entered");

CREATE INDEX "prospect_h_pidx" ON "prospect_h" USING btree ("org_group_id", "record_change_date");

CREATE INDEX "qb_main_key" ON "quickbooks_sync" USING btree ("org_group_id", "interface_time");

CREATE INDEX "sales_points_pidx" ON "sales_points" USING btree ("employee_id", "point_date");


