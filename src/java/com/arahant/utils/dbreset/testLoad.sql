INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('org_group', 9);
UPDATE dynace_sequence SET lastvalue=8 WHERE tag='person';
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('address', 13);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('phone', 19);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('holiday', 8);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('hr_benefit_package', 1);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('hr_employee_status', 3);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('hr_checklist_item', 7);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('hr_eval_category', 2);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('hr_note_category', 2);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('hr_training_category', 3);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('project_type', 3);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('project_category', 2);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('route', 1);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('route_stop', 3);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('route_type_assoc', 6);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('project_status', 13);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('route_path', 3);
INSERT INTO dynace_sequence (tag, lastvalue) VALUES ('hr_empl_status_history', 3);

insert into gl_account (gl_account_id,account_number,account_name,account_type) values ('00000-0000000000','accnt 1','gl account',1);


insert into wage_type (wage_type_id,org_group_id, wage_name,period_type,wage_type, expense_account,is_deduction) values ('00000-0000000003','00000-0000000005','Benefits',1,1,'00000-0000000000','N');

--  Add Benefit records
INSERT INTO hr_benefit_category (benefit_cat_id ,description,benefit_type,mutually_exclusive,requires_decline, org_group_id ) VALUES ('00000-0000000010','Standard',8,'N','N','00000-0000000005');
INSERT INTO hr_benefit (benefit_id, benefit_name, rule_name, time_related, paid_benefit, pre_tax, start_date, end_date, benefit_cat_id,wage_type_id, eligibility_type, eligibility_period, dependent_max_age, dependent_max_age_student, coverage_end_type, coverage_end_period) VALUES ('00000-0000000000', 'Vacation', 'Vacation', 'Y', 'Y','N',19000101,30000101,'00000-0000000010','00000-0000000003', 1, 0, 18, 24, 2, 0);
INSERT INTO hr_benefit (benefit_id, benefit_name, rule_name, time_related, paid_benefit, pre_tax, start_date, end_date, benefit_cat_id,wage_type_id, eligibility_type, eligibility_period, dependent_max_age, dependent_max_age_student, coverage_end_type, coverage_end_period) VALUES ('00000-0000000001', 'Sick', 'Sick', 'Y', 'Y','N',19000101,30000101,'00000-0000000010','00000-0000000003', 1, 0, 18, 24, 2, 0);
INSERT INTO hr_benefit (benefit_id, benefit_name, rule_name, time_related, paid_benefit, pre_tax, start_date, end_date, benefit_cat_id,wage_type_id, eligibility_type, eligibility_period, dependent_max_age, dependent_max_age_student, coverage_end_type, coverage_end_period) VALUES ('00000-0000000002', 'Personal Time Off', 'Personal', 'Y', 'Y','N',19000101,30000101,'00000-0000000010','00000-0000000003', 1, 0, 18, 24, 2, 0);
INSERT INTO hr_benefit (benefit_id, benefit_name, rule_name, time_related, paid_benefit, pre_tax, start_date, end_date, benefit_cat_id,wage_type_id, eligibility_type, eligibility_period, dependent_max_age, dependent_max_age_student, coverage_end_type, coverage_end_period) VALUES ('00000-0000000003', 'Unpaid Time Off', '', 'Y', 'N','N',19000101,30000101,'00000-0000000010','00000-0000000003', 1, 0, 18, 24, 2, 0);

INSERT INTO hr_benefit_config (benefit_config_id, benefit_id, employee,  spouse_non_employee, children, max_children, employee_cost, employer_cost, add_info, spouse_non_emp_or_children, auto_assign, spouse_declines_outside,  spouse_employee, spouse_emp_or_children)  VALUES ('00000-0000000000','00000-0000000000','Y','N','N',0,0,0,'','N','N','N','N','N');
INSERT INTO hr_benefit_config (benefit_config_id, benefit_id, employee,  spouse_non_employee, children, max_children, employee_cost, employer_cost, add_info, spouse_non_emp_or_children, auto_assign, spouse_declines_outside,  spouse_employee, spouse_emp_or_children)  VALUES ('00000-0000000001','00000-0000000001','Y','N','N',0,0,0,'','N','N','N','N','N');
INSERT INTO hr_benefit_config (benefit_config_id, benefit_id, employee,  spouse_non_employee, children, max_children, employee_cost, employer_cost, add_info, spouse_non_emp_or_children, auto_assign, spouse_declines_outside,  spouse_employee, spouse_emp_or_children)  VALUES ('00000-0000000002','00000-0000000002','Y','N','N',0,0,0,'','N','N','N','N','N');
INSERT INTO hr_benefit_config (benefit_config_id, benefit_id, employee,  spouse_non_employee, children, max_children, employee_cost, employer_cost, add_info, spouse_non_emp_or_children, auto_assign, spouse_declines_outside,  spouse_employee, spouse_emp_or_children)  VALUES ('00000-0000000003','00000-0000000003','Y','N','N',0,0,0,'','N','N','N','N','N');



INSERT INTO org_group (org_group_id, group_name, org_group_type, owning_entity_id) VALUES ('00000-0000000001', 'Client One', 2, NULL);
INSERT INTO org_group (org_group_id, group_name, org_group_type, owning_entity_id) VALUES ('00000-0000000002', 'Client Two', 2, NULL);
INSERT INTO org_group (org_group_id, group_name, org_group_type, owning_entity_id) VALUES ('00000-0000000003', 'Vendor One', 4, NULL);
INSERT INTO org_group (org_group_id, group_name, org_group_type, owning_entity_id) VALUES ('00000-0000000004', 'Vendor Two', 4, NULL);
INSERT INTO org_group (org_group_id, group_name, org_group_type, owning_entity_id) VALUES ('00000-0000000006', 'Quality Assurance', 1, NULL);
INSERT INTO org_group (org_group_id, group_name, org_group_type, owning_entity_id) VALUES ('00000-0000000007', 'Development', 1, NULL);
INSERT INTO org_group (org_group_id, group_name, org_group_type, owning_entity_id) VALUES ('00000-0000000008', 'Networking & Infrastructure', 1, NULL);
INSERT INTO org_group (org_group_id, group_name, org_group_type, owning_entity_id) VALUES ('00000-0000000009', 'Documentation', 1, NULL);

INSERT INTO company_base (org_group_id, org_group_type, federal_employer_id) VALUES ('00000-0000000001', 2,  '111-11');
INSERT INTO company_base (org_group_id, org_group_type, federal_employer_id) VALUES ('00000-0000000002', 2,  '');
INSERT INTO company_base (org_group_id, org_group_type, federal_employer_id) VALUES ('00000-0000000003', 4,  '111-11');
INSERT INTO company_base (org_group_id, org_group_type, federal_employer_id) VALUES ('00000-0000000004', 4,  '');


UPDATE org_group SET owning_entity_id='00000-0000000005' WHERE org_group_id='00000-0000000006';
UPDATE org_group SET owning_entity_id='00000-0000000005' WHERE org_group_id='00000-0000000007';
UPDATE org_group SET owning_entity_id='00000-0000000005' WHERE org_group_id='00000-0000000008';
UPDATE org_group SET owning_entity_id='00000-0000000005' WHERE org_group_id='00000-0000000009';

INSERT INTO client_status (client_status_id, code, seqno) VALUES ('00000-0000000001', 'No Status', 1);

INSERT INTO client (org_group_id, inactive_date, billing_rate, contract_date, dflt_sales_acct, dflt_ar_acct,company_id, client_status_id) VALUES ('00000-0000000001', 0, 0, 0, NULL, NULL,'00000-0000000005', '00000-0000000001');
INSERT INTO client (org_group_id, inactive_date, billing_rate, contract_date, dflt_sales_acct, dflt_ar_acct,company_id, client_status_id) VALUES ('00000-0000000002', 0, 0, 0, NULL, NULL,'00000-0000000005', '00000-0000000001');

INSERT INTO vendor (org_group_id, account_number, dflt_expense_acct, dflt_ap_acct,company_id) VALUES ('00000-0000000003', '', NULL, NULL,'00000-0000000005');
INSERT INTO vendor (org_group_id, account_number, dflt_expense_acct, dflt_ap_acct,company_id) VALUES ('00000-0000000004', '', NULL, NULL,'00000-0000000005');

INSERT INTO org_group_hierarchy (parent_group_id, child_group_id, org_group_type) VALUES ('00000-0000000005', '00000-0000000006', 1);
INSERT INTO org_group_hierarchy (parent_group_id, child_group_id, org_group_type) VALUES ('00000-0000000005', '00000-0000000007', 1);
INSERT INTO org_group_hierarchy (parent_group_id, child_group_id, org_group_type) VALUES ('00000-0000000005', '00000-0000000008', 1);
INSERT INTO org_group_hierarchy (parent_group_id, child_group_id, org_group_type) VALUES ('00000-0000000006', '00000-0000000009', 1);

--employees
INSERT INTO person (person_id, lname, fname, personal_email, job_title, org_group_type, company_id, mname, dob, ssn, sex, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000001', 'QAManager', 'Sally', 'ssue@mysoftware.com', 'QA Lead', 1, '00000-0000000005', 'S',19650101,'111-11-1112', 'F', '2008-01-01','N','00000-0000000000');
INSERT INTO person (person_id, lname, fname, personal_email, job_title, org_group_type, company_id, mname, dob, ssn, sex, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000002', 'QATester', 'Joe', 'jblow@mysoftware.com', 'Tester', 1, '00000-0000000005', 'B',19650101,'111-11-1113', 'M', '2008-01-01','N','00000-0000000000');
INSERT INTO person (person_id, lname, fname, personal_email, job_title, org_group_type, company_id, mname, dob, ssn, sex, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000003', 'Owner', 'Owen', 'oowner@mysoftware.com', 'Owner', 1, '00000-0000000005', 'O',19650101,'111-11-1111', 'M', '2008-01-01','N','00000-0000000000');
INSERT INTO person (person_id, lname, fname, personal_email, job_title, org_group_type, company_id, mname, dob, ssn, sex, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000004', 'Accountant', 'Angie', '', 'Accountant', 1, '00000-0000000005', ' ',19650101,'111-11-1114', 'M', '2008-01-01','N','00000-0000000000');
INSERT INTO person (person_id, lname, fname, personal_email, job_title, org_group_type, company_id, mname, dob, ssn, sex, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000005', 'DevManager', 'Tom', '', 'Dev Lead', 1, '00000-0000000005', ' ',19650101,'111-12-1115', 'F', '2008-01-01','N','00000-0000000000');
INSERT INTO person (person_id, lname, fname, personal_email, job_title, org_group_type, company_id, mname, dob, ssn, sex, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000006', 'Developer', 'Henry', '', 'Developer', 1, '00000-0000000005', ' ',19650101,'111-11-1116', 'M', '2008-01-01','N','00000-0000000000');
INSERT INTO person (person_id, lname, fname, personal_email, job_title, org_group_type, company_id, mname, dob, ssn, sex, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000007', 'NetManager', 'Brian', '', 'Net Mgr', 1, '00000-0000000005', ' ',19650101,'111-11-1117', 'M', '2008-01-01','N','00000-0000000000');
INSERT INTO person (person_id, lname, fname, personal_email, job_title, org_group_type, company_id, mname, dob, ssn, sex, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000008', 'Documentor', 'Bill', '', '', 1, '00000-0000000005', 'B',19650101,'111-11-1118', 'F', '2008-01-01','N','00000-0000000000');
--dependents
INSERT INTO person (person_id, lname, fname, personal_email, job_title, org_group_type, company_id, mname, dob, ssn, sex, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000009', 'Kid', 'Billy', '', '', 1, '00000-0000000005', 'B',20010101,'121-11-1118', 'M', '2008-01-01','N','00000-0000000000');
INSERT INTO person (person_id, lname, fname, personal_email, job_title, org_group_type, company_id, mname, dob, ssn, sex, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000010', 'Kid', 'Sally', '', '', 1, '00000-0000000005', 'B',20010101,'121-21-1118', 'F', '2008-01-01','N','00000-0000000000');
INSERT INTO person (person_id, lname, fname, personal_email, job_title, org_group_type, company_id, mname, dob, ssn, sex, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000011', 'Kid', 'Mary', '', '', 1, '00000-0000000005', 'B',20010101,'121-21-1218', 'F', '2008-01-01','N','00000-0000000000');
--clients
INSERT INTO person (person_id, lname, fname, personal_email, job_title, org_group_type, company_id, mname, dob, ssn, sex, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000012', 'Client1', 'Client1', '', '', 2, '00000-0000000001', 'B',19650101,'111-11-1119', 'F', '2008-01-01','N','00000-0000000000');

insert into hr_benefit_change_reason (bcr_id, bcr_type,description,start_date,end_date, effective_date ) values ('00000-0000000000',4,'internal', 10102000,0,10102000 );


insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000000','2001-01-01','N','00000-0000000001','00000-0000000001','00000-0000000001','00000-0000000000',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000001','2001-01-01','N','00000-0000000002','00000-0000000002','00000-0000000002','00000-0000000000',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000002','2001-01-01','N','00000-0000000003','00000-0000000003','00000-0000000003','00000-0000000000',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000003','2001-01-01','N','00000-0000000004','00000-0000000004','00000-0000000004','00000-0000000000',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000004','2001-01-01','N','00000-0000000005','00000-0000000005','00000-0000000005','00000-0000000000',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000005','2001-01-01','N','00000-0000000006','00000-0000000006','00000-0000000006','00000-0000000000',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000006','2001-01-01','N','00000-0000000007','00000-0000000007','00000-0000000007','00000-0000000000',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000007','2001-01-01','N','00000-0000000008','00000-0000000008','00000-0000000008','00000-0000000000',20000101,20000101,'00000-0000000000');


insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000008','2001-01-01','N','00000-0000000001','00000-0000000001','00000-0000000001','00000-0000000001',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000009','2001-01-01','N','00000-0000000002','00000-0000000002','00000-0000000002','00000-0000000001',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000010','2001-01-01','N','00000-0000000003','00000-0000000003','00000-0000000003','00000-0000000001',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000011','2001-01-01','N','00000-0000000004','00000-0000000004','00000-0000000004','00000-0000000001',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000012','2001-01-01','N','00000-0000000005','00000-0000000005','00000-0000000005','00000-0000000001',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000013','2001-01-01','N','00000-0000000006','00000-0000000006','00000-0000000006','00000-0000000001',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000014','2001-01-01','N','00000-0000000007','00000-0000000007','00000-0000000007','00000-0000000001',20000101,20000101,'00000-0000000000');
insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date,bcr_id) values ('00000-0000000015','2001-01-01','N','00000-0000000008','00000-0000000008','00000-0000000008','00000-0000000001',20000101,20000101,'00000-0000000000');

--employees
INSERT INTO org_group_association (person_id, org_group_id, primary_indicator, org_group_type, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000001', '00000-0000000006', 'Y', 1, '2008-01-01','N','00000-0000000000');
INSERT INTO org_group_association (person_id, org_group_id, primary_indicator, org_group_type, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000002', '00000-0000000006', 'N', 1, '2008-01-01','N','00000-0000000000');
INSERT INTO org_group_association (person_id, org_group_id, primary_indicator, org_group_type, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000003', '00000-0000000005', 'Y', 1, '2008-01-01','N','00000-0000000000');
INSERT INTO org_group_association (person_id, org_group_id, primary_indicator, org_group_type, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000004', '00000-0000000005', 'N', 1, '2008-01-01','N','00000-0000000000');
INSERT INTO org_group_association (person_id, org_group_id, primary_indicator, org_group_type, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000005', '00000-0000000007', 'Y', 1, '2008-01-01','N','00000-0000000000');
INSERT INTO org_group_association (person_id, org_group_id, primary_indicator, org_group_type, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000006', '00000-0000000007', 'N', 1, '2008-01-01','N','00000-0000000000');
INSERT INTO org_group_association (person_id, org_group_id, primary_indicator, org_group_type, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000007', '00000-0000000008', 'Y', 1, '2008-01-01','N','00000-0000000000');
INSERT INTO org_group_association (person_id, org_group_id, primary_indicator, org_group_type, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000008', '00000-0000000009', 'N', 1, '2008-01-01','N','00000-0000000000');
--clients
INSERT INTO org_group_association (person_id, org_group_id, primary_indicator, org_group_type, record_change_date, record_change_type, record_person_id) VALUES ('00000-0000000012', '00000-0000000001', 'Y', 1, '2008-01-01','N','00000-0000000000');


INSERT INTO employee (person_id, timesheet_final_date,  billing_rate, eeo_category_id, eeo_race_id, ext_ref, overtime_pay) VALUES ('00000-0000000001', 0, 0, '00000-0000000001', '00000-0000000000',  '',  'N');
INSERT INTO employee (person_id, timesheet_final_date,  billing_rate, eeo_category_id, eeo_race_id, ext_ref, overtime_pay) VALUES ('00000-0000000002', 0,  0, '00000-0000000001', '00000-0000000003',  '',  'N');
INSERT INTO employee (person_id, timesheet_final_date,  billing_rate, eeo_category_id, eeo_race_id, ext_ref, overtime_pay) VALUES ('00000-0000000003', 0,  0, '00000-0000000001', '00000-0000000001', '',   'N');
INSERT INTO employee (person_id, timesheet_final_date,  billing_rate, eeo_category_id, eeo_race_id, ext_ref, overtime_pay) VALUES ('00000-0000000004', 0, 0, '00000-0000000001', '00000-0000000002', '', 'N');
INSERT INTO employee (person_id, timesheet_final_date,  billing_rate, eeo_category_id, eeo_race_id, ext_ref, overtime_pay) VALUES ('00000-0000000005', 0, 0, NULL, NULL,  '',   'N');
INSERT INTO employee (person_id, timesheet_final_date,  billing_rate, eeo_category_id, eeo_race_id, ext_ref, overtime_pay) VALUES ('00000-0000000006', 0,  0, NULL, NULL,  '',   'N');
INSERT INTO employee (person_id, timesheet_final_date,  billing_rate, eeo_category_id, eeo_race_id, ext_ref, overtime_pay) VALUES ('00000-0000000007', 0, 0, NULL, NULL,  '',  'N');
INSERT INTO employee (person_id, timesheet_final_date,  billing_rate, eeo_category_id, eeo_race_id, ext_ref, overtime_pay) VALUES ('00000-0000000008', 0, 0, NULL, NULL,  '',   'N');

INSERT INTO client_contact (person_id) VALUES ('00000-0000000012');

insert into hr_empl_dependent ( relationship_id, dependent_id,  employee_id,relationship, relationship_type) values ('00000-0000000003','00000-0000000009','00000-0000000005','','C');
insert into hr_empl_dependent ( relationship_id, dependent_id,  employee_id,relationship, relationship_type) values ('00000-0000000005','00000-0000000009','00000-0000000001','','C');
insert into hr_empl_dependent ( relationship_id, dependent_id,  employee_id,relationship, relationship_type) values ('00000-0000000001','00000-0000000001','00000-0000000005','','S');
insert into hr_empl_dependent ( relationship_id, dependent_id,  employee_id,relationship, relationship_type) values ('00000-0000000002','00000-0000000005','00000-0000000001','','S');
insert into hr_empl_dependent ( relationship_id, dependent_id,  employee_id,relationship, relationship_type) values ('00000-0000000004','00000-0000000010','00000-0000000001','','C');
insert into hr_empl_dependent ( relationship_id, dependent_id,  employee_id,relationship, relationship_type) values ('00000-0000000006','00000-0000000010','00000-0000000005','','C');
insert into hr_empl_dependent ( relationship_id, dependent_id,  employee_id,relationship, relationship_type) values ('00000-0000000007','00000-0000000011','00000-0000000001','','C');


--employees
INSERT INTO prophet_login (person_id, can_login, user_login, user_password, screen_group_id, security_group_id) VALUES ('00000-0000000001', 'Y', 'qamanager', '62e9a833719dafcc4845149b166d1d0f', '00000-0000000000', '00000-0000000002');
INSERT INTO prophet_login (person_id, can_login, user_login, user_password, screen_group_id, security_group_id) VALUES ('00000-0000000002', 'Y', 'qatester', '62e9a833719dafcc4845149b166d1d0f', '00000-0000000000', '00000-0000000001');
INSERT INTO prophet_login (person_id, can_login, user_login, user_password, screen_group_id, security_group_id) VALUES ('00000-0000000005', 'Y', 'devmanager', '62e9a833719dafcc4845149b166d1d0f', '00000-0000000000', '00000-0000000002');
INSERT INTO prophet_login (person_id, can_login, user_login, user_password, screen_group_id, security_group_id) VALUES ('00000-0000000004', 'Y', 'accountant', '62e9a833719dafcc4845149b166d1d0f', '00000-0000000000', '00000-0000000003');
INSERT INTO prophet_login (person_id, can_login, user_login, user_password, screen_group_id, security_group_id) VALUES ('00000-0000000003', 'Y', 'owner', '62e9a833719dafcc4845149b166d1d0f', '00000-0000000000', '00000-0000000004');
INSERT INTO prophet_login (person_id, can_login, user_login, user_password, screen_group_id, security_group_id) VALUES ('00000-0000000006', 'Y', 'developer', '62e9a833719dafcc4845149b166d1d0f', '00000-0000000000', '00000-0000000001');
INSERT INTO prophet_login (person_id, can_login, user_login, user_password, screen_group_id, security_group_id) VALUES ('00000-0000000007', 'Y', 'netmanager', '62e9a833719dafcc4845149b166d1d0f', '00000-0000000000', '00000-0000000002');
INSERT INTO prophet_login (person_id, can_login, user_login, user_password, screen_group_id, security_group_id) VALUES ('00000-0000000008', 'Y', 'documentor', '62e9a833719dafcc4845149b166d1d0f', '00000-0000000000', '00000-0000000001');
--clients
INSERT INTO prophet_login (person_id, can_login, user_login, user_password, screen_group_id, security_group_id) VALUES ('00000-0000000012', 'Y', 'client1', '62e9a833719dafcc4845149b166d1d0f', '00000-0000000000', '00000-0000000005');


INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000001', '111 First Client Street', 'Columbus', 'OH', '43229', NULL, '00000-0000000001', 0);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000002', '', '', '', '', NULL, '00000-0000000002', 0);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000003', '111 Vendor Avenue', 'Vendorville', 'VA', '12345', NULL, '00000-0000000003', 0);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000004', '', '', '', '', NULL, '00000-0000000004', 0);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000005', '123 Maryland Way', 'Brentwood', 'TN', '37027', NULL, '00000-0000000005', 0);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000006', '', '', '', '', '00000-0000000001', NULL, 2);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000007', '', '', '', '', '00000-0000000002', NULL, 2);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000009', '', '', '', '', '00000-0000000004', NULL, 2);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000008', '1234 Owner Way', 'Boston', 'MA', '11111', '00000-0000000003', NULL, 2);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000010', '', '', '', '', '00000-0000000005', NULL, 2);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000011', '', '', '', '', '00000-0000000006', NULL, 2);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000012', '', '', '', '', '00000-0000000007', NULL, 2);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000013', '', '', '', '', '00000-0000000008', NULL, 2);
INSERT INTO address (address_id, street, city, state, zip, person_join, org_group_join, address_type) VALUES ('00000-0000000014', '', '', '', '', '00000-0000000009', NULL, 2);


INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000001', '614 885 4023', 1, NULL, '00000-0000000001');
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000002', '', 4, NULL, '00000-0000000001');
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000003', '', 1, NULL, '00000-0000000002');
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000004', '', 4, NULL, '00000-0000000002');
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000005', '', 1, NULL, '00000-0000000003');
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000006', '', 4, NULL, '00000-0000000003');
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000007', '', 1, NULL, '00000-0000000004');
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000008', '', 4, NULL, '00000-0000000004');
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000009', '555-5555', 1, NULL, '00000-0000000005');
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000010', '', 4, NULL, '00000-0000000005');
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000011', 'Mobile Phone:', 3, '00000-0000000001', NULL);
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000012', 'Mobile Phone:', 3, '00000-0000000002', NULL);
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000013', 'Mobile Phone:', 3, '00000-0000000003', NULL);
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000014', 'Mobile Phone:', 3, '00000-0000000004', NULL);
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000015', '2222222222', 2, '00000-0000000003', NULL);
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000016', 'Mobile Phone:', 3, '00000-0000000005', NULL);
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000017', 'Mobile Phone:', 3, '00000-0000000006', NULL);
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000018', 'Mobile Phone:', 3, '00000-0000000007', NULL);
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000019', 'Mobile Phone:', 3, '00000-0000000008', NULL);
INSERT INTO phone (phone_id, phone_number, phone_type, person_join, org_group_join) VALUES ('00000-0000000020', 'Mobile Phone:', 3, '00000-0000000009', NULL);

INSERT INTO project_category (project_category_id, code, description) VALUES ('00000-0000000001', 'INT_STA_LITE', 'Integration Station Lite (scaled back version)');
INSERT INTO project_category (project_category_id, code, description) VALUES ('00000-0000000002', 'INT_STA', 'Integration Station (full version)');

INSERT INTO project_type (project_type_id, code, description) VALUES ('00000-0000000002', 'DEFECT', 'Used for projects that are working on a reported defect');
INSERT INTO project_type (project_type_id, code, description) VALUES ('00000-0000000001', 'NEW_DEVELOPMENT', 'Used for projects that are new development.');
INSERT INTO project_type (project_type_id, code, description) VALUES ('00000-0000000003', 'ENHANCEMENT_REQUEST', 'Used for projects that are requested enhancements from clients.');

INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000001', 'ESTIMATE_RDY', 'Project is ready to be estimated');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000002', 'ESTIMATE', 'Project is being estimated');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000003', 'ESTIMATE_COMPLETE', 'Project has had an estimate completed');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000004', 'EST_APPROVAL_RDY', 'Project estimate is ready for approval');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000005', 'EST_APPROVAL', 'Project estimate approval is being determined');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000006', 'EST_APPROVED', 'Project estimate has been approved');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000007', 'EST_REJECTED', 'Project estimate has been rejected');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000008', 'DESIGN_READY', 'Project is ready to be designed');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000009', 'DESIGN', 'Project is under design');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000010', 'DESIGN_COMPLETE', 'Project has completed design phase');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000011', 'DEV_READY', 'Project is ready to be developed');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000012', 'DEV', 'Project is being developed');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000013', 'DEV_COMPLETE', 'Project has completed development');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000014', 'TEST_RDY', 'Project is ready to be tested');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000015', 'TEST', 'Project is being tested');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000016', 'TEST_REJECTED', 'Project has been tested and failed');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000017', 'TEST_APPROVED', 'Project has been tested and is approved');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000018', 'DEPLOY_RDY', 'Project is ready to be deployed');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000019', 'DEPLOY', 'Project is being deployed');
INSERT INTO project_status (project_status_id, code, description) VALUES ('00000-0000000020', 'DEPLOY_COMPLETE', 'Project has been deployed');

INSERT INTO holiday (holiday_id, hdate, description) VALUES ('00000-0000000001', 20071224, 'Christmas Eve');
INSERT INTO holiday (holiday_id, hdate, description) VALUES ('00000-0000000002', 20071225, 'Christmas');
INSERT INTO holiday (holiday_id, hdate, description) VALUES ('00000-0000000003', 20070704, 'July 4');
INSERT INTO holiday (holiday_id, hdate, description) VALUES ('00000-0000000004', 20070101, 'New Year''s Day');
INSERT INTO holiday (holiday_id, hdate, description) VALUES ('00000-0000000005', 20070528, 'Memorial Day');
INSERT INTO holiday (holiday_id, hdate, description) VALUES ('00000-0000000006', 20070903, 'Labor Day');
INSERT INTO holiday (holiday_id, hdate, description) VALUES ('00000-0000000007', 20071122, 'Thanksgiving Day');
INSERT INTO holiday (holiday_id, hdate, description) VALUES ('00000-0000000008', 20071123, 'Thanksgiving Day After');

INSERT INTO hr_benefit_package (package_id, name) VALUES ('00000-0000000001', 'Standard Employee');

INSERT INTO hr_benefit_package_join (package_id, benefit_id) VALUES ('00000-0000000001', '00000-0000000003');
INSERT INTO hr_benefit_package_join (package_id, benefit_id) VALUES ('00000-0000000001', '00000-0000000002');
INSERT INTO hr_benefit_package_join (package_id, benefit_id) VALUES ('00000-0000000001', '00000-0000000001');
INSERT INTO hr_benefit_package_join (package_id, benefit_id) VALUES ('00000-0000000001', '00000-0000000000');

INSERT INTO hr_training_category (cat_id, name, training_type, org_group_id) VALUES ('00000-0000000001', 'Java Certification', 2, '00000-0000000005');
INSERT INTO hr_training_category (cat_id, name, training_type, org_group_id) VALUES ('00000-0000000002', '.NET Certification', 2, '00000-0000000005');
INSERT INTO hr_training_category (cat_id, name, training_type, org_group_id) VALUES ('00000-0000000003', 'Diversity Training', 1, '00000-0000000005');

INSERT INTO hr_employee_status (status_id, name, active, benefit_type, org_group_id) VALUES ('00000-0000000001', 'Hired', 'Y','B','00000-0000000005');
INSERT INTO hr_employee_status (status_id, name, active, benefit_type, org_group_id) VALUES ('00000-0000000002', 'Terminated', 'N','N','00000-0000000005');
INSERT INTO hr_employee_status (status_id, name, active, benefit_type, org_group_id) VALUES ('00000-0000000003', 'Laid Off', 'N','C','00000-0000000005');

INSERT INTO hr_empl_status_history (status_hist_id, employee_id, effective_date, status_id, notes) VALUES ('00000-0000000001', '00000-0000000004', 20060801, '00000-0000000001', '');
INSERT INTO hr_empl_status_history (status_hist_id, employee_id, effective_date, status_id, notes) VALUES ('00000-0000000002', '00000-0000000005', 20060707, '00000-0000000001', '');
INSERT INTO hr_empl_status_history (status_hist_id, employee_id, effective_date, status_id, notes) VALUES ('00000-0000000003', '00000-0000000006', 20060613, '00000-0000000001', '');
INSERT INTO hr_empl_status_history (status_hist_id, employee_id, effective_date, status_id, notes) VALUES ('00000-0000000004', '00000-0000000001', 20060101, '00000-0000000001', '');
INSERT INTO hr_empl_status_history (status_hist_id, employee_id, effective_date, status_id, notes) VALUES ('00000-0000000008', '00000-0000000008', 20060401, '00000-0000000001', '');
INSERT INTO hr_empl_status_history (status_hist_id, employee_id, effective_date, status_id, notes) VALUES ('00000-0000000007', '00000-0000000007', 20060322, '00000-0000000001', '');
INSERT INTO hr_empl_status_history (status_hist_id, employee_id, effective_date, status_id, notes) VALUES ('00000-0000000006', '00000-0000000003', 20060110, '00000-0000000001', '');
INSERT INTO hr_empl_status_history (status_hist_id, employee_id, effective_date, status_id, notes) VALUES ('00000-0000000005', '00000-0000000002', 20060101, '00000-0000000001', '');

INSERT INTO hr_checklist_item (item_id, name, employee_status_id, seq, first_active_date, last_active_date, org_group_id) VALUES ('00000-0000000001', 'Sign NDA', '00000-0000000001', 0, 20070621, 99999999, '00000-0000000005');
INSERT INTO hr_checklist_item (item_id, name, employee_status_id, seq, first_active_date, last_active_date, org_group_id) VALUES ('00000-0000000002', 'Distribute Key Card', '00000-0000000001', 0, 20070621, 99999999, '00000-0000000005');
INSERT INTO hr_checklist_item (item_id, name, employee_status_id, seq, first_active_date, last_active_date, org_group_id) VALUES ('00000-0000000003', 'Exit Interview', '00000-0000000003', 0, 20070621, 99999999, '00000-0000000005');
INSERT INTO hr_checklist_item (item_id, name, employee_status_id, seq, first_active_date, last_active_date, org_group_id) VALUES ('00000-0000000004', 'Collect Key Card', '00000-0000000003', 0, 20070621, 99999999, '00000-0000000005');
INSERT INTO hr_checklist_item (item_id, name, employee_status_id, seq, first_active_date, last_active_date, org_group_id) VALUES ('00000-0000000005', 'Exit Interview', '00000-0000000002', 0, 20070621, 99999999, '00000-0000000005');
INSERT INTO hr_checklist_item (item_id, name, employee_status_id, seq, first_active_date, last_active_date, org_group_id) VALUES ('00000-0000000006', 'Collect Key Card', '00000-0000000002', 0, 20070621, 99999999, '00000-0000000005');
INSERT INTO hr_checklist_item (item_id, name, employee_status_id, seq, first_active_date, last_active_date, org_group_id) VALUES ('00000-0000000007', 'Benefits Overview', '00000-0000000002', 0, 20070621, 99999999, '00000-0000000005');

INSERT INTO hr_eval_category (eval_cat_id, name, description, weight, org_group_id) VALUES ('00000-0000000001', 'Promptness', 'Measure of how often the employee made it to work and meetings on time', 2, '00000-0000000005');
INSERT INTO hr_eval_category (eval_cat_id, name, description, weight, org_group_id) VALUES ('00000-0000000002', 'Completes Work on Time', 'Measure of how well employee meets deadlines', 0, '00000-0000000005');
INSERT INTO hr_note_category (cat_id, name, org_group_id) VALUES ('00000-0000000001', 'Hobbies', '00000-0000000005');
INSERT INTO hr_note_category (cat_id, name, org_group_id) VALUES ('00000-0000000002', 'Additional Certifications', '00000-0000000005');

INSERT INTO route (route_id, name, description, project_status_id, route_stop_id) VALUES ('00000-0000000001', 'Defect Request', 'Use this when a defect is reported', '00000-0000000009', NULL);

INSERT INTO route_type_assoc (route_assoc_id, route_id, project_category_id, project_type_id) VALUES ('00000-0000000005', '00000-0000000001', '00000-0000000001', '00000-0000000002');
INSERT INTO route_type_assoc (route_assoc_id, route_id, project_category_id, project_type_id) VALUES ('00000-0000000006', '00000-0000000001', '00000-0000000002', '00000-0000000002');

INSERT INTO project_phase (project_phase_id, code, description, security_level) values ('00000-0000000001','Estimate','Project Phase for estimates',0);
INSERT INTO project_phase (project_phase_id, code, description, security_level) values ('00000-0000000002','Development','Project Phase for development',1);
INSERT INTO project_phase (project_phase_id, code, description, security_level) values ('00000-0000000003','QA','Project Phase for quality assurance',2);
INSERT INTO project_phase (project_phase_id, code, description, security_level) values ('00000-0000000004','Implementation','Project Phase for beta testing',3);

INSERT INTO route_stop (route_stop_id, route_id, org_group_id, description,project_phase_id) VALUES ('00000-0000000001', '00000-0000000001', '00000-0000000007', 'Request for Estimate', '00000-0000000001');
INSERT INTO route_stop (route_stop_id, route_id, org_group_id, description,project_phase_id) VALUES ('00000-0000000002', '00000-0000000001', '00000-0000000001', 'Request Estimate Approval', '00000-0000000001');
INSERT INTO route_stop (route_stop_id, route_id, org_group_id, description,project_phase_id) VALUES ('00000-0000000003', '00000-0000000001', '00000-0000000007', 'Design Request', '00000-0000000002');
INSERT INTO route_stop (route_stop_id, route_id, org_group_id, description,project_phase_id) VALUES ('00000-0000000004', '00000-0000000001', '00000-0000000007', 'Code Request', '00000-0000000002');
INSERT INTO route_stop (route_stop_id, route_id, org_group_id, description,project_phase_id) VALUES ('00000-0000000005', '00000-0000000001', '00000-0000000006', 'Test Defect Fix', '00000-0000000003');
INSERT INTO route_stop (route_stop_id, route_id, org_group_id, description,project_phase_id) VALUES ('00000-0000000006', '00000-0000000001', '00000-0000000008', 'Deploy Defect Fix', '00000-0000000004');

INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000001', '00000-0000000001');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000001', '00000-0000000002');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000001', '00000-0000000003');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000002', '00000-0000000004');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000002', '00000-0000000005');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000002', '00000-0000000006');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000002', '00000-0000000007');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000003', '00000-0000000008');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000003', '00000-0000000009');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000003', '00000-0000000010');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000004', '00000-0000000011');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000004', '00000-0000000012');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000004', '00000-0000000013');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000005', '00000-0000000015');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000005', '00000-0000000016');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000005', '00000-0000000017');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000006', '00000-0000000018');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000006', '00000-0000000019');
INSERT INTO project_status_rs_join (route_stop_id, project_status_id) VALUES ('00000-0000000006', '00000-0000000020');

UPDATE route SET route_stop_id='00000-0000000001' WHERE route_id='00000-0000000001';

INSERT INTO route_path (route_path_id, from_status_id, to_status_id, from_stop_id, to_stop_id) VALUES ('00000-0000000001', '00000-0000000003', '00000-0000000004', '00000-0000000001', '00000-0000000002');
INSERT INTO route_path (route_path_id, from_status_id, to_status_id, from_stop_id, to_stop_id) VALUES ('00000-0000000002', '00000-0000000006', '00000-0000000008', '00000-0000000002', '00000-0000000003');
INSERT INTO route_path (route_path_id, from_status_id, to_status_id, from_stop_id, to_stop_id) VALUES ('00000-0000000003', '00000-0000000007', '00000-0000000001', '00000-0000000002', '00000-0000000001');
INSERT INTO route_path (route_path_id, from_status_id, to_status_id, from_stop_id, to_stop_id) VALUES ('00000-0000000004', '00000-0000000010', '00000-0000000011', '00000-0000000003', '00000-0000000004');
INSERT INTO route_path (route_path_id, from_status_id, to_status_id, from_stop_id, to_stop_id) VALUES ('00000-0000000005', '00000-0000000013', '00000-0000000014', '00000-0000000004', '00000-0000000005');
INSERT INTO route_path (route_path_id, from_status_id, to_status_id, from_stop_id, to_stop_id) VALUES ('00000-0000000006', '00000-0000000016', '00000-0000000011', '00000-0000000005', '00000-0000000004');
INSERT INTO route_path (route_path_id, from_status_id, to_status_id, from_stop_id, to_stop_id) VALUES ('00000-0000000007', '00000-0000000017', '00000-0000000018', '00000-0000000005', '00000-0000000006');

insert into project (project_id,project_status_id,project_type_id,project_category_id,requesting_org_group,date_reported,project_name,description,time_reported,completed_date,completed_time,current_route_stop) values ('00000-0000000001','00000-0000000001','00000-0000000002','00000-0000000001','00000-0000000001',20070101,'TEST-1','Can''t Connect to System',0,0,0,'00000-0000000001');
insert into project_employee_join (project_employee_join_id, person_id, project_id) values ('00000-0000000001','00000-0000000005','00000-0000000001');

insert into form_type (form_type_id,form_code,description,form_type) values ('00000-0000000000','Form','General form','E');
insert into form_type (form_type_id,form_code,description,form_type) values ('00000-0000000001','Misc','Misc. form','E');



--- Less perfect data (lore) for testing - Remove when better data available

insert into invoice (invoice_id, description,  customer_id, invoice_type, accounting_invoice_identifier) values ('00000-0000000000','test invoice','00000-0000000001','C','200906010001');
insert into invoice (invoice_id, description,  customer_id, invoice_type, accounting_invoice_identifier) values ('00000-0000000001','test invoice 2','00000-0000000001','C','200906010002');




insert into wage_type (wage_type_id,org_group_id, wage_name,period_type,wage_type, expense_account,is_deduction) values ('00001-0000000000','00000-0000000005','Regular Pay',1,1,'00000-0000000000','N');
insert into timesheet (timesheet_id,project_id,person_id,description,beginning_entry_date, total_hours, beginning_date, beginning_time, end_time,billable,entry_state,beginning_entry_person_id,wage_type_id) values ('00000-0000000000','00000-0000000001','00000-0000000005','test','2000-01-01',10,20070101,0,0,'Y','N','00000-0000000005','00001-0000000000');
insert into standard_project (project_id,project_type_id,project_category_id, billing_method, next_billing_date ) values ('00000-0000000001','00000-0000000001','00000-0000000001',0,0) ;
insert into hr_position(position_id,position_name,org_group_id) values ('00000-0000000000','worker', '00000-0000000005');
insert into product_service (product_service_id, accsys_id , expense_account_id,  description ,  service_type, company_id) values ('00000-0000000000','3F0000-1071530163','00000-0000000000','a service',1,'00000-0000000005');
insert into service (service_id) values ('00000-0000000000');

insert into hr_eval_category (eval_cat_id,name,description,weight, org_group_id) values ('00000-0000000000','name','description',1, '00000-0000000005');
insert into hr_employee_eval ( employee_eval_id, employee_id, eval_date , supervisor_id , next_eval_date, description ,  comments ,  state ,  e_comments ,  p_comments,  final_date) 
	values ('00000-0000000000','00000-0000000005',20070101,'00000-0000000005',20080101,'description','comments','E','e comments','p comments',0);


insert into invoice_line_item (invoice_line_item_id, adj_hours ,adj_rate,invoice_id, product_id, expense_account_id,description,billing_type, amount) values ('00000-0000000000',0,0,'00000-0000000000','00000-0000000000','00000-0000000000','test','D',1000);
insert into invoice_line_item (invoice_line_item_id, adj_hours ,adj_rate,invoice_id, product_id, expense_account_id,description,billing_type, amount) values ('00000-0000000001',10,500,'00000-0000000001','00000-0000000000','00000-0000000000','test','H',1000);



insert into hr_wage(wage_id, employee_id, wage_type_id, wage_amount,effective_date, position_id , notes) values ('00001-0000000000','00000-0000000001','00001-0000000000',50,20000101,'00000-0000000000','Notes');
insert into hr_wage(wage_id, employee_id, wage_type_id, wage_amount,effective_date, position_id , notes) values ('00001-0000000001','00000-0000000002','00001-0000000000',50,20000101,'00000-0000000000','Notes');
insert into hr_wage(wage_id, employee_id, wage_type_id, wage_amount,effective_date, position_id , notes) values ('00001-0000000002','00000-0000000003','00001-0000000000',50,20000101,'00000-0000000000','Notes');
insert into hr_wage(wage_id, employee_id, wage_type_id, wage_amount,effective_date, position_id , notes) values ('00001-0000000003','00000-0000000004','00001-0000000000',50,20000101,'00000-0000000000','Notes');
insert into hr_wage(wage_id, employee_id, wage_type_id, wage_amount,effective_date, position_id , notes) values ('00001-0000000004','00000-0000000005','00001-0000000000',50,20000101,'00000-0000000000','Notes');
insert into hr_wage(wage_id, employee_id, wage_type_id, wage_amount,effective_date, position_id , notes) values ('00001-0000000005','00000-0000000006','00001-0000000000',50,20000101,'00000-0000000000','Notes');
insert into hr_wage(wage_id, employee_id, wage_type_id, wage_amount,effective_date, position_id , notes) values ('00001-0000000006','00000-0000000007','00001-0000000000',50,20000101,'00000-0000000000','Notes');
insert into hr_wage(wage_id, employee_id, wage_type_id, wage_amount,effective_date, position_id , notes) values ('00001-0000000007','00000-0000000008','00001-0000000000',50,20000101,'00000-0000000000','Notes');


insert into hr_benefit_category (benefit_cat_id, description, benefit_type, org_group_id) values ('00000-0000000000','Health Plan',0,'00000-0000000005');
insert into hr_benefit_category (benefit_cat_id, description, benefit_type, org_group_id) values ('00000-0000000001','Dental Plan',1,'00000-0000000005');
insert into hr_benefit_category (benefit_cat_id, description, benefit_type, org_group_id) values ('00000-0000000002','Vision Plan',2,'00000-0000000005');

insert into hr_benefit (benefit_id, benefit_name, rule_name ,benefit_cat_id, wage_type_id, eligibility_type, eligibility_period, dependent_max_age, dependent_max_age_student, coverage_end_type, coverage_end_period) values ('00000-0000000004','Opt1: Emp','', '00000-0000000000','00000-0000000003', 1, 0, 18, 24, 2, 0);
insert into hr_benefit (benefit_id, benefit_name, rule_name ,benefit_cat_id, wage_type_id, eligibility_type, eligibility_period, dependent_max_age, dependent_max_age_student, coverage_end_type, coverage_end_period) values ('00000-0000000009','Dent Opt1: Emp','', '00000-0000000001','00000-0000000003', 1, 0, 18, 24, 2, 0);


INSERT INTO hr_benefit_config (benefit_config_id, benefit_id, employee,  spouse_non_employee, children, max_children, employee_cost, employer_cost, add_info, spouse_non_emp_or_children, auto_assign, spouse_declines_outside,  spouse_employee,spouse_emp_or_children)  VALUES ('00000-0000000004','00000-0000000004','Y','N','N',0,0,0,'11111','N','N','N','N','N');
INSERT INTO hr_benefit_config (benefit_config_id, benefit_id, employee,  spouse_non_employee, children, max_children, employee_cost, employer_cost, add_info, spouse_non_emp_or_children, auto_assign, spouse_declines_outside,  spouse_employee,spouse_emp_or_children)  VALUES ('00000-0000000005','00000-0000000004','Y','Y','N',0,0,0,'11111','N','N','N','N','N');
INSERT INTO hr_benefit_config (benefit_config_id, benefit_id, employee,  spouse_non_employee, children, max_children, employee_cost, employer_cost, add_info, spouse_non_emp_or_children, auto_assign, spouse_declines_outside,  spouse_employee,spouse_emp_or_children)  VALUES ('00000-0000000006','00000-0000000004','Y','Y','Y',0,0,0,'11111','N','N','N','N','N');
INSERT INTO hr_benefit_config (benefit_config_id, benefit_id, employee,  spouse_non_employee, children, max_children, employee_cost, employer_cost, add_info, spouse_non_emp_or_children, auto_assign, spouse_declines_outside,  spouse_employee,spouse_emp_or_children)  VALUES ('00000-0000000007','00000-0000000004','Y','Y','Y',2,0,0,'11111','N','N','N','N','N');
INSERT INTO hr_benefit_config (benefit_config_id, benefit_id, employee,  spouse_non_employee, children, max_children, employee_cost, employer_cost, add_info, spouse_non_emp_or_children, auto_assign, spouse_declines_outside,  spouse_employee,spouse_emp_or_children)  VALUES ('00000-0000000008','00000-0000000004','Y','Y','N',0,0,0,'11111','N','Y','N','N','N');

INSERT INTO hr_benefit_config (benefit_config_id, benefit_id, employee,  spouse_non_employee, children, max_children, employee_cost, employer_cost, add_info, spouse_non_emp_or_children, auto_assign, spouse_declines_outside, spouse_employee,  spouse_emp_or_children)  VALUES ('00000-0000000009','00000-0000000009','Y','Y','N',0,0,0,'11111','N','Y','N','N','N');

insert into hr_benefit_join( benefit_join_id,record_change_date,record_change_type,record_person_id,covered_person,paying_person,benefit_config_id,policy_start_date,coverage_start_date, bcr_id) values ('00000-0000000016','2001-01-01','N','00000-0000000008','00000-0000000008','00000-0000000008','00000-0000000004',20000101,20000101,'00000-0000000000');



insert into property (prop_name,prop_value) values ('DefaultSecurityGroupId', '00000-0000000001');
insert into property (prop_name,prop_value) values ('DefaultScreenGroupId', '00000-0000000008');
insert into property (prop_name,prop_value) values ('DefaultEmployeeStatusId', '00000-0000000001');
insert into property (prop_name,prop_value) values ('Use Quickbooks', 'TRUE');


--insert into hr_employee_benefit_join ( employee_id, benefit_config_id ,employee_benefit_id ,  insurance_id ,  policy_start_date,  policy_end_date ,  amount_paid, amount_covered, coverage_start_date, coverage_end_date, change_reason, change_description) values ('00000-0000000001','00000-0000000004','00000-000000000','ins id',20070707,20080808,220,0,20070101, 0, 4, 'Test Load');
--insert into hr_employee_benefit_join ( employee_id, benefit_config_id ,employee_benefit_id ,  insurance_id ,  policy_start_date,  policy_end_date ,  amount_paid, amount_covered, coverage_start_date, coverage_end_date, change_reason, change_description) values ('00000-0000000001','00000-0000000009','00000-000000001','ins id',20070707,20080808,0,0,20070101, 0, 4, 'Test Load');

--insert into hr_employee_beneficiary (beneficiary_id , employee_benefit_id, beneficiary,  relationship,benefit_percent) values ('00000-0000000001','00000-000000000','bene','rel',100);


insert into project_history (project_history_id ,project_id,person_id, date_changed , time_changed,from_status_id,  to_status_id) values ('00000-0000000000','00000-0000000001','00000-0000000000',0,0,'00000-0000000001','00000-0000000001');



--INSERT INTO screen (screen_id, filename, auth_code, name, description, screen_type) VALUES ('00001-0000000078', 'com/arahant/app/screen/custom/williamsonCounty/benefitWizard/BenefitWizardScreen.swf', '', 'Benefits Wizard', 'Wiz', 1);
--INSERT INTO screen_group_hierarchy (parent_screen_group_id, child_screen_group_id, seq_no, screen_id, default_screen, screen_group_hierarchy_id) VALUES ('00000-0000000007', NULL, 10, '00001-0000000078', 'N', '00002-0000000051');
--insert into hr_benefit_change_reason (bcr_id,bcr_type,description,start_date,end_date,effective_date) values ('00000-0000000001',1,'test',0,0,0);


update org_group set owning_entity_id=org_group_id where owning_entity_id is null;

--insert into property values ('Combo Max','20','Combo Max');
--insert into property values ('Search Max','20','Search Max');

insert into wage_paid (wage_paid_id,employee_id,beg_period,end_period,date_paid,payment_method,check_number) values ('00000-0000000000','00000-0000000001',0,0,20090601,1,1000);
insert into wage_paid_detail (wage_detail_id,wage_paid_id, wage_type_id,wage_amount) values ('00000-0000000000','00000-0000000000','00001-0000000000',1200);

insert into hr_benefit_class(benefit_class_id, class_name, class_description,org_group_id) values ('00000-0000000000','Test Class','Test Class','00000-0000000005');
insert into job_type (job_type_id,description) values ('00000-0000000000','test');
insert into applicant_source(applicant_source_id,description) values ('00000-0000000000','description');
insert into applicant_status(applicant_status_id,"name",status_order,consider_for_hire) values ('00000-0000000000','name',0,'Y');
insert into applicant_position(applicant_position_id,"name",org_group_id,position_status,job_type_id) values ('00000-0000000000','name','00000-0000000005','N','00000-0000000000');

insert into product_type(product_type_id, description,company_id) values ('00000-0000000001','prod type','00000-0000000005');
insert into product_service (product_service_id, accsys_id , expense_account_id,  description ,  service_type, company_id) values ('00000-0000000001','3F0000-1071530153','00000-0000000000','product',1,'00000-0000000005');
insert into product (product_id,product_type_id,vendor_id,sku,product_cost,wholesale_price,retail_price,availability_date,term_date) values ('00000-0000000001','00000-0000000001','00000-0000000003','sku1',22,0,0,0,0);

