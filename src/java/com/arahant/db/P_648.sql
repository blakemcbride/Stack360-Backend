
-- PostgreSQL patch from revision 647 to revision 648

ALTER TABLE "address_cr" DROP CONSTRAINT "address_cr_approver_fkey";

ALTER TABLE "address_cr" DROP CONSTRAINT "address_cr_project_fkey";

ALTER TABLE "address_cr" DROP CONSTRAINT "address_cr_real_fkey";

ALTER TABLE "address_cr" DROP CONSTRAINT "address_cr_recvhg_fkey";

ALTER TABLE "address_cr" DROP CONSTRAINT "address_cr_requestor_fkey";

ALTER TABLE "address" DROP CONSTRAINT "address_org_group_fkey";

ALTER TABLE "address" DROP CONSTRAINT "address_person_fkey";

ALTER TABLE "agency" DROP CONSTRAINT "agency_company_fkey";

ALTER TABLE "agency_join" DROP CONSTRAINT "agency_join_agency_fkey";

ALTER TABLE "agency_join" DROP CONSTRAINT "agency_join_company_fkey";

ALTER TABLE "agent_join" DROP CONSTRAINT "agent_join_agent_fkey";

ALTER TABLE "agent_join" DROP CONSTRAINT "agent_join_apppers_fkey";

ALTER TABLE "agent_join" DROP CONSTRAINT "agent_join_company_fkey";

ALTER TABLE "agent" DROP CONSTRAINT "agent_person_fkey";

ALTER TABLE "agreement_person_join" DROP CONSTRAINT "agreement_person_agree_fkey";

ALTER TABLE "agreement_person_join" DROP CONSTRAINT "agreement_person_person_fkey";

ALTER TABLE "alert" DROP CONSTRAINT "alert_last_person_fkey";

ALTER TABLE "alert" DROP CONSTRAINT "alert_org_group_fkey";

ALTER TABLE "alert_person_join" DROP CONSTRAINT "alert_person_alert_fkey";

ALTER TABLE "alert" DROP CONSTRAINT "alert_person_fkey";

ALTER TABLE "alert_person_join" DROP CONSTRAINT "alert_person_person_fkey";

ALTER TABLE "applicant_position_info" DROP CONSTRAINT "app_pos_info_pos_fkey";

ALTER TABLE "applicant_question_choice" DROP CONSTRAINT "app_ques_choice_ques_fkey";

ALTER TABLE "applicant_answer" DROP CONSTRAINT "applicant_ans_choice_fkey";

ALTER TABLE "applicant_answer" DROP CONSTRAINT "applicant_ans_person_fkey";

ALTER TABLE "applicant_answer" DROP CONSTRAINT "applicant_answer_ques_fkey";

ALTER TABLE "applicant" DROP CONSTRAINT "applicant_applicant_source_fkey";

ALTER TABLE "applicant" DROP CONSTRAINT "applicant_applicant_status_fkey";

ALTER TABLE "applicant_application" DROP CONSTRAINT "applicant_application_applicant_app_status_fkey";

ALTER TABLE "applicant_application" DROP CONSTRAINT "applicant_application_applicant_fkey";

ALTER TABLE "applicant_application" DROP CONSTRAINT "applicant_application_applicant_position_fkey";

ALTER TABLE "applicant_application" DROP CONSTRAINT "applicant_application_applicant_source_fkey";

ALTER TABLE "applicant_app_status" DROP CONSTRAINT "applicant_appstat_comp_fkey";

ALTER TABLE "applicant_contact" DROP CONSTRAINT "applicant_contact_applicant_application_fkey";

ALTER TABLE "applicant_contact" DROP CONSTRAINT "applicant_contact_person_fkey";

ALTER TABLE "applicant" DROP CONSTRAINT "applicant_eeo_race_fkey";

ALTER TABLE "applicant" DROP CONSTRAINT "applicant_person_fkey";

ALTER TABLE "applicant_position" DROP CONSTRAINT "applicant_position_job_type_fkey";

ALTER TABLE "applicant_position" DROP CONSTRAINT "applicant_position_org_group_fkey";

ALTER TABLE "applicant_question" DROP CONSTRAINT "applicant_question_comp_fkey";

ALTER TABLE "applicant_question" DROP CONSTRAINT "applicant_question_job_type_fkey";

ALTER TABLE "applicant_source" DROP CONSTRAINT "applicant_source_company_fkey";

ALTER TABLE "applicant_status" DROP CONSTRAINT "applicant_status_comp_fkey";

ALTER TABLE "applicant_application" DROP CONSTRAINT "application_app_job_type_fkey";

ALTER TABLE "appointment_location" DROP CONSTRAINT "appointment_loc_comp_fkey";

ALTER TABLE "appointment" DROP CONSTRAINT "appointment_location_id_fkey";

ALTER TABLE "assembly_template_detail" DROP CONSTRAINT "assem_temp_temp_fkey";

ALTER TABLE "assembly_template_detail" DROP CONSTRAINT "assembly_temp_parent_fkey";

ALTER TABLE "assembly_template_detail" DROP CONSTRAINT "assembly_template_product_fkey";

ALTER TABLE "bank_account" DROP CONSTRAINT "bank_account_org_group_fkey";

ALTER TABLE "bank_draft_detail" DROP CONSTRAINT "bank_draft_detail_draft_fkey";

ALTER TABLE "bank_draft_detail" DROP CONSTRAINT "bank_draft_detail_person_fkey";

ALTER TABLE "bank_draft_history" DROP CONSTRAINT "bank_draft_hist_draft_fkey";

ALTER TABLE "bank_draft_batch" DROP CONSTRAINT "bank_draftbatch_comp_fkey";

ALTER TABLE "benefit_change_reason_doc" DROP CONSTRAINT "bcr_doc_bcr_fkey";

ALTER TABLE "benefit_change_reason_doc" DROP CONSTRAINT "bcr_doc_comp_fkey";

ALTER TABLE "benefit_config_cost_age" DROP CONSTRAINT "ben_con_cost_age_con_fkey";

ALTER TABLE "benefit_config_cost_status" DROP CONSTRAINT "ben_concost_stat_emp_fkey";

ALTER TABLE "benefit_config_cost" DROP CONSTRAINT "ben_conf_cost_config_fkey";

ALTER TABLE "benefit_config_cost_status" DROP CONSTRAINT "ben_conf_cost_st_con_fkey";

ALTER TABLE "benefit_question_choice" DROP CONSTRAINT "ben_ques_choice_ques_fkey";

ALTER TABLE "benefit_group_class_join" DROP CONSTRAINT "benconf_clsjn_ben_fkey";

ALTER TABLE "benefit_group_class_join" DROP CONSTRAINT "benconf_conclsjn_ben_fkey";

ALTER TABLE "benefit_answer" DROP CONSTRAINT "benefit_ans_person_fkey";

ALTER TABLE "benefit_answer_h" DROP CONSTRAINT "benefit_ansh_h_recper_fkey";

ALTER TABLE "benefit_answer_h" DROP CONSTRAINT "benefit_ansh_person_fkey";

ALTER TABLE "benefit_answer_h" DROP CONSTRAINT "benefit_ansh_ques_fkey";

ALTER TABLE "benefit_answer" DROP CONSTRAINT "benefit_answer_ques_fkey";

ALTER TABLE "benefit_answer" DROP CONSTRAINT "benefit_answer_recper_fkey";

ALTER TABLE "benefit_class_join" DROP CONSTRAINT "benefit_class_join_class_fkey";

ALTER TABLE "benefit_class_join" DROP CONSTRAINT "benefit_class_join_config_fkey";

ALTER TABLE "benefit_config_cost" DROP CONSTRAINT "benefit_concost_org_kfkey";

ALTER TABLE "benefit_dependency" DROP CONSTRAINT "benefit_dependency_benefit_fkey";

ALTER TABLE "benefit_dependency" DROP CONSTRAINT "benefit_dependency_required_fkey";

ALTER TABLE "benefit_document" DROP CONSTRAINT "benefit_doc_benefit_fkey";

ALTER TABLE "benefit_document" DROP CONSTRAINT "benefit_doc_comp_fkey";

ALTER TABLE "benefit_question" DROP CONSTRAINT "benefit_ques_benefit_fkey";

ALTER TABLE "benefit_question" DROP CONSTRAINT "benefit_ques_recper_fkey";

ALTER TABLE "benefit_question_h" DROP CONSTRAINT "benefit_quesh_benefit_fkey";

ALTER TABLE "benefit_question_h" DROP CONSTRAINT "benefit_quesh_recper_fkey";

ALTER TABLE "benefit_restriction" DROP CONSTRAINT "benefit_restriction_bcr_fkey";

ALTER TABLE "benefit_restriction" DROP CONSTRAINT "benefit_restriction_bencat_fkey";

ALTER TABLE "hr_benefit_join" DROP CONSTRAINT "benenfit_join_project_fkey";

ALTER TABLE "project" DROP CONSTRAINT "bill_at_org_group_fkey";

ALTER TABLE "hr_checklist_detail" DROP CONSTRAINT "checklist_detail_item_fkey";

ALTER TABLE "client" DROP CONSTRAINT "client_client_status_fkey";

ALTER TABLE "client" DROP CONSTRAINT "client_company_company_fkey";

ALTER TABLE "client" DROP CONSTRAINT "client_company_fkey";

ALTER TABLE "client" DROP CONSTRAINT "client_dflt_ar_acct";

ALTER TABLE "client" DROP CONSTRAINT "client_dflt_sales_acct";

ALTER TABLE "client_contact" DROP CONSTRAINT "client_person_id_fkey";

ALTER TABLE "client_status" DROP CONSTRAINT "client_status_comp_fkey";

ALTER TABLE "company_form_folder" DROP CONSTRAINT "comp_form_folder_comp_fkey";

ALTER TABLE "company_form_folder_join" DROP CONSTRAINT "comp_formfoldj_fold_fkey";

ALTER TABLE "company_form_folder_join" DROP CONSTRAINT "comp_formfoldj_form_fkey";

ALTER TABLE "company_detail" DROP CONSTRAINT "company_det_adv_fkey";

ALTER TABLE "company_detail" DROP CONSTRAINT "company_det_ar_fkey";

ALTER TABLE "company_detail" DROP CONSTRAINT "company_detail_cash_fkey";

ALTER TABLE "company_detail" DROP CONSTRAINT "company_detail_company_fkey";

ALTER TABLE "company_form" DROP CONSTRAINT "company_form_company_fkey";

ALTER TABLE "company_form_folder" DROP CONSTRAINT "company_form_folder_parent_fkey";

ALTER TABLE "company_form_org_join" DROP CONSTRAINT "company_form_orgj_ffkey";

ALTER TABLE "company_form_org_join" DROP CONSTRAINT "company_form_orgj_ofkey";

ALTER TABLE "company_form" DROP CONSTRAINT "company_form_type_fkey";

ALTER TABLE "company_question_detail" DROP CONSTRAINT "company_ques_det_org_fkey";

ALTER TABLE "company_question" DROP CONSTRAINT "company_question_comp_fkey";

ALTER TABLE "contact_question" DROP CONSTRAINT "contact_question_comp_fkey";

ALTER TABLE "contact_question_detail" DROP CONSTRAINT "contact_question_person_fkey";

ALTER TABLE "drc_form_event" DROP CONSTRAINT "drc_form_event_causing_per_fkey";

ALTER TABLE "drc_form_event" DROP CONSTRAINT "drc_form_event_emp_fkey";

ALTER TABLE "drc_form_event" DROP CONSTRAINT "drc_form_event_per_fkey";

ALTER TABLE "drc_import_benefit" DROP CONSTRAINT "drc_import_ben_comp_fkey";

ALTER TABLE "drc_import_benefit_join" DROP CONSTRAINT "drc_import_benjn_ben_fkey";

ALTER TABLE "drc_import_benefit_join" DROP CONSTRAINT "drc_import_benjn_sub_fkey";

ALTER TABLE "drc_import_benefit_join" DROP CONSTRAINT "drc_import_benjoin_enr_fkey";

ALTER TABLE "drc_import_enrollee" DROP CONSTRAINT "drc_import_enr_comp_fkey";

ALTER TABLE "drc_import_enrollee" DROP CONSTRAINT "drc_import_enr_pers_fkey";

ALTER TABLE "e_signature" DROP CONSTRAINT "e_signature_person_fkey";

ALTER TABLE "edi_transaction" DROP CONSTRAINT "edi_trans_org_fkey";

ALTER TABLE "education" DROP CONSTRAINT "education_person_fky";

ALTER TABLE "hr_eeo1" DROP CONSTRAINT "eeo1_org_fkey";

ALTER TABLE "electronic_fund_transfer" DROP CONSTRAINT "eft_person_fkey";

ALTER TABLE "electronic_fund_transfer" DROP CONSTRAINT "eft_wage_type_fkey";

ALTER TABLE "employee" DROP CONSTRAINT "employee_bank_fkey";

ALTER TABLE "employee" DROP CONSTRAINT "employee_class_fkey";

ALTER TABLE "employee" DROP CONSTRAINT "employee_eeo_category_fkey";

ALTER TABLE "employee" DROP CONSTRAINT "employee_eeo_race_fkey";

ALTER TABLE "employee" DROP CONSTRAINT "employee_emp_status_fkey";

ALTER TABLE "employee" DROP CONSTRAINT "employee_person_id_fkey";

ALTER TABLE "form_type" DROP CONSTRAINT "form_type_org_fkey";

ALTER TABLE "garnishment_type" DROP CONSTRAINT "garn_typ_wage_typ_fkey";

ALTER TABLE "garnishment" DROP CONSTRAINT "garnishment_person_fkey";

ALTER TABLE "garnishment" DROP CONSTRAINT "garnishment_remit_to_fksy";

ALTER TABLE "garnishment" DROP CONSTRAINT "garnishment_type_fkey";

ALTER TABLE "gl_account" DROP CONSTRAINT "gl_account_comp_fkey";

ALTER TABLE "org_group_association" DROP CONSTRAINT "group_association_group_id_fkey";

ALTER TABLE "org_group_hierarchy" DROP CONSTRAINT "group_hierarchy_child_group_id_fkey";

ALTER TABLE "org_group_hierarchy" DROP CONSTRAINT "group_hierarchy_parent_group_id_fkey";

ALTER TABLE "hr_benefit_config" DROP CONSTRAINT "he_benconf_benefit_fkey";

ALTER TABLE "holiday" DROP CONSTRAINT "holiday_org_fkey";

ALTER TABLE "hr_accrual" DROP CONSTRAINT "hr_accrual_employee_fkey";

ALTER TABLE "hr_accrual" DROP CONSTRAINT "hr_accural_benefit_account_fkey";

ALTER TABLE "hr_benefit_change_reason" DROP CONSTRAINT "hr_bcr_company_fkey";

ALTER TABLE "hr_benefit_class" DROP CONSTRAINT "hr_ben_calss_org_fkey";

ALTER TABLE "hr_benefit_category_answer" DROP CONSTRAINT "hr_ben_cat_ans_ben_fkey";

ALTER TABLE "hr_benefit_category_answer" DROP CONSTRAINT "hr_ben_cat_ans_ques_fkey";

ALTER TABLE "hr_benefit_category_question" DROP CONSTRAINT "hr_ben_cat_ques_cat_fkey";

ALTER TABLE "hr_benefit_join" DROP CONSTRAINT "hr_ben_join_bcat_chk";

ALTER TABLE "hr_benefit_join" DROP CONSTRAINT "hr_ben_join_ben_chk";

ALTER TABLE "hr_benefit_join_h" DROP CONSTRAINT "hr_ben_join_rpi";

ALTER TABLE "hr_benefit_category" DROP CONSTRAINT "hr_bencat_oescrn_fkey";

ALTER TABLE "hr_benefit_category" DROP CONSTRAINT "hr_bencat_org_fkey";

ALTER TABLE "hr_benefit_category" DROP CONSTRAINT "hr_bencat_oscrn_fkey";

ALTER TABLE "hr_benefit" DROP CONSTRAINT "hr_benefit_ben_cat_fkey";

ALTER TABLE "hr_benefit_join" DROP CONSTRAINT "hr_benefit_benef_fkey";

ALTER TABLE "hr_benefit_join" DROP CONSTRAINT "hr_benefit_join_bcr_fkey";

ALTER TABLE "hr_benefit_join" DROP CONSTRAINT "hr_benefit_join_cpers_fkey";

ALTER TABLE "hr_benefit_join" DROP CONSTRAINT "hr_benefit_join_hrel_fkey";

ALTER TABLE "hr_benefit_join" DROP CONSTRAINT "hr_benefit_join_levent_fkey";

ALTER TABLE "hr_benefit_join" DROP CONSTRAINT "hr_benefit_join_ppers_fkey";

ALTER TABLE "hr_benefit_join" DROP CONSTRAINT "hr_benefit_join_recper_fkey";

ALTER TABLE "hr_benefit" DROP CONSTRAINT "hr_benefit_ooscreen_fkey";

ALTER TABLE "hr_benefit" DROP CONSTRAINT "hr_benefit_oscreen_fkey";

ALTER TABLE "hr_benefit_package_join" DROP CONSTRAINT "hr_benefit_pj_benefit_fkey";

ALTER TABLE "hr_benefit" DROP CONSTRAINT "hr_benefit_product_fkey";

ALTER TABLE "hr_benefit" DROP CONSTRAINT "hr_benefit_provider_fkey";

ALTER TABLE "hr_benefit" DROP CONSTRAINT "hr_benefit_replace_fkey";

ALTER TABLE "hr_benefit_rider" DROP CONSTRAINT "hr_benefit_rider_base_fkey";

ALTER TABLE "hr_benefit_rider" DROP CONSTRAINT "hr_benefit_rider_rider_fkey";

ALTER TABLE "hr_benefit" DROP CONSTRAINT "hr_benefit_wage_type_fkey";

ALTER TABLE "hr_billing_status_history" DROP CONSTRAINT "hr_bill_stat_hist_pers_fkey";

ALTER TABLE "hr_billing_status_history" DROP CONSTRAINT "hr_bill_stat_stat_fkey";

ALTER TABLE "hr_benefit_project_join" DROP CONSTRAINT "hr_bpjoin_config_fky";

ALTER TABLE "hr_benefit_project_join" DROP CONSTRAINT "hr_bpjoin_prokect_fkey";

ALTER TABLE "hr_checklist_detail" DROP CONSTRAINT "hr_checklist_detail_employee_fkey";

ALTER TABLE "hr_checklist_detail" DROP CONSTRAINT "hr_checklist_detail_super_fkey";

ALTER TABLE "hr_checklist_item" DROP CONSTRAINT "hr_checklist_item_status_fkey";

ALTER TABLE "hr_checklist_item" DROP CONSTRAINT "hr_chklst_item_org_fkey";

ALTER TABLE "hr_checklist_item" DROP CONSTRAINT "hr_chklstitm_compform_fkey";

ALTER TABLE "hr_checklist_item" DROP CONSTRAINT "hr_chklstitm_screen_fkey";

ALTER TABLE "hr_checklist_item" DROP CONSTRAINT "hr_chklstitm_screengrp_fkey";

ALTER TABLE "hr_employee_beneficiary" DROP CONSTRAINT "hr_emp_benefiary_rpi";

ALTER TABLE "hr_employee_beneficiary" DROP CONSTRAINT "hr_emp_beneficiary_eb_id";

ALTER TABLE "hr_employee_beneficiary_h" DROP CONSTRAINT "hr_emp_beneficiary_rpi";

ALTER TABLE "hr_employee_event" DROP CONSTRAINT "hr_emp_evt_employee_fkey";

ALTER TABLE "hr_employee_event" DROP CONSTRAINT "hr_emp_evt_supervisor_fkey";

ALTER TABLE "hr_empl_dependent_cr" DROP CONSTRAINT "hr_empl_dep_cr_approver_fkey";

ALTER TABLE "hr_empl_dependent_cr" DROP CONSTRAINT "hr_empl_dep_cr_project_fkey";

ALTER TABLE "hr_empl_dependent_cr" DROP CONSTRAINT "hr_empl_dep_cr_real_fkey";

ALTER TABLE "hr_empl_dependent_cr" DROP CONSTRAINT "hr_empl_dep_cr_recvhg_fkey";

ALTER TABLE "hr_empl_dependent_cr" DROP CONSTRAINT "hr_empl_dep_cr_requestor_fkey";

ALTER TABLE "hr_empl_dependent" DROP CONSTRAINT "hr_empl_dep_dep_fkey";

ALTER TABLE "hr_empl_dependent" DROP CONSTRAINT "hr_empl_dep_empl_id_fkey";

ALTER TABLE "hr_empl_eval_detail" DROP CONSTRAINT "hr_empl_eval_cat_fkey";

ALTER TABLE "hr_empl_eval_detail" DROP CONSTRAINT "hr_empl_eval_detail_eval_fkey";

ALTER TABLE "hr_employee_eval" DROP CONSTRAINT "hr_employee_eval_emp_fkey";

ALTER TABLE "hr_employee_eval" DROP CONSTRAINT "hr_employee_eval_super_fkey";

ALTER TABLE "hr_employee_status" DROP CONSTRAINT "hr_empstat_org_fkey";

ALTER TABLE "hr_emergency_contact" DROP CONSTRAINT "hr_emrcont_person_fkey";

ALTER TABLE "hr_eval_category" DROP CONSTRAINT "hr_eval_cat_org_fkey";

ALTER TABLE "hr_note_category" DROP CONSTRAINT "hr_notecat_org_fkey";

ALTER TABLE "hr_position" DROP CONSTRAINT "hr_pos_org_fkey";

ALTER TABLE "hr_position" DROP CONSTRAINT "hr_position_class_fkey";

ALTER TABLE "hr_empl_status_history" DROP CONSTRAINT "hr_status_hist_employee_fkey";

ALTER TABLE "hr_empl_status_history" DROP CONSTRAINT "hr_status_hist_status_fkey";

ALTER TABLE "hr_training_category" DROP CONSTRAINT "hr_traincat_org_fkey";

ALTER TABLE "hr_training_detail" DROP CONSTRAINT "hr_training_detail_cat_fkey";

ALTER TABLE "hr_training_detail" DROP CONSTRAINT "hr_training_detail_empl_fkey";

ALTER TABLE "hr_wage" DROP CONSTRAINT "hr_wage_employee_fkey";

ALTER TABLE "hr_wage" DROP CONSTRAINT "hr_wage_position_fkey";

ALTER TABLE "hr_wage" DROP CONSTRAINT "hr_wage_type_fley";

ALTER TABLE "import_column" DROP CONSTRAINT "import_column_filter_fkey";

ALTER TABLE "import_filter" DROP CONSTRAINT "import_filter_type_fkey";

ALTER TABLE "import_type" DROP CONSTRAINT "import_type_company_fkey";

ALTER TABLE "insurance_location_code" DROP CONSTRAINT "ins_loc_cde_ben_fkey";

ALTER TABLE "insurance_location_code" DROP CONSTRAINT "ins_loc_code_ben_fkey";

ALTER TABLE "insurance_location_code" DROP CONSTRAINT "ins_loc_code_empstat_fkey";

ALTER TABLE "interface_log" DROP CONSTRAINT "interface_log_comp_fkey";

ALTER TABLE "invoice_line_item" DROP CONSTRAINT "inv_line_item_gl_acct_fkey";

ALTER TABLE "invoice_line_item" DROP CONSTRAINT "inv_line_item_invoice_fkey";

ALTER TABLE "invoice_line_item" DROP CONSTRAINT "inv_line_item_product_fkey";

ALTER TABLE "inventory" DROP CONSTRAINT "inventory_location_fkey";

ALTER TABLE "inventory" DROP CONSTRAINT "inventory_product_fkey";

ALTER TABLE "invoice" DROP CONSTRAINT "invoice_company_fkey";

ALTER TABLE "invoice" DROP CONSTRAINT "invoice_gl_account_id_fkey";

ALTER TABLE "invoice" DROP CONSTRAINT "invoice_person_fkey";

ALTER TABLE "item" DROP CONSTRAINT "item_chg_person_fkey";

ALTER TABLE "item_inspection" DROP CONSTRAINT "item_inspection_inspector_fkey";

ALTER TABLE "item_inspection" DROP CONSTRAINT "item_inspection_item_fkey";

ALTER TABLE "item_inspection" DROP CONSTRAINT "item_inspection_recorder_fkey";

ALTER TABLE "item" DROP CONSTRAINT "item_location_fkey";

ALTER TABLE "item" DROP CONSTRAINT "item_lot_fkey";

ALTER TABLE "item" DROP CONSTRAINT "item_parent_fkey";

ALTER TABLE "item" DROP CONSTRAINT "item_product_fkey";

ALTER TABLE "job_type" DROP CONSTRAINT "job_type_comp_fkey";

ALTER TABLE "life_event" DROP CONSTRAINT "life_event_bcr_fkey";

ALTER TABLE "life_event" DROP CONSTRAINT "life_event_person_fkey";

ALTER TABLE "life_event" DROP CONSTRAINT "life_event_recpers_fkey";

ALTER TABLE "prophet_login_exception" DROP CONSTRAINT "login_exception_company_fkey";

ALTER TABLE "prophet_login_exception" DROP CONSTRAINT "login_exception_person_fkey";

ALTER TABLE "prophet_login_exception" DROP CONSTRAINT "login_exception_screen_fkey";

ALTER TABLE "prophet_login_exception" DROP CONSTRAINT "login_exception_security_fkey";

ALTER TABLE "prophet_login" DROP CONSTRAINT "login_person_id_fkey";

ALTER TABLE "prophet_login" DROP CONSTRAINT "login_screen_group_fkey";

ALTER TABLE "prophet_login" DROP CONSTRAINT "login_security_group_fkey";

ALTER TABLE "project" DROP CONSTRAINT "managing_employee_fkey";

ALTER TABLE "message" DROP CONSTRAINT "messages_from_person";

ALTER TABLE "message" DROP CONSTRAINT "messages_to_person";

ALTER TABLE "onboarding_task_complete" DROP CONSTRAINT "ob_task_complete_pers_fkey";

ALTER TABLE "onboarding_task_complete" DROP CONSTRAINT "ob_task_complete_task_fkey";

ALTER TABLE "onboarding_config" DROP CONSTRAINT "onboarding_config_company_id_fkey";

ALTER TABLE "onboarding_task" DROP CONSTRAINT "onboarding_task_config_fkey";

ALTER TABLE "onboarding_task" DROP CONSTRAINT "onboarding_task_screen_fkey";

ALTER TABLE "org_group" DROP CONSTRAINT "org_group_benclass_fkey";

ALTER TABLE "company_base" DROP CONSTRAINT "org_group_fkey";

ALTER TABLE "org_group" DROP CONSTRAINT "org_group_pay_sch_fkey";

ALTER TABLE "org_group" DROP CONSTRAINT "org_group_project_fkey";

ALTER TABLE "org_group_association" DROP CONSTRAINT "org_grp_recper_fkey";

ALTER TABLE "overtime_approval" DROP CONSTRAINT "overtime_app_emp_fkey";

ALTER TABLE "overtime_approval" DROP CONSTRAINT "overtime_app_super_fkey";

ALTER TABLE "org_group" DROP CONSTRAINT "owning_entity_fkey";

ALTER TABLE "hr_benefit_package_join" DROP CONSTRAINT "package_id";

ALTER TABLE "password_history" DROP CONSTRAINT "password_history_pers_fkey";

ALTER TABLE "pay_schedule_period" DROP CONSTRAINT "pay_sch_period_sched_fkey";

ALTER TABLE "pay_schedule" DROP CONSTRAINT "pay_schedule_company_fkey";

ALTER TABLE "payment_info" DROP CONSTRAINT "payment_info_benefit_fkey";

ALTER TABLE "payment_info" DROP CONSTRAINT "payment_info_person_fkey";

ALTER TABLE "project_employee_join" DROP CONSTRAINT "pej_person_fkey";

ALTER TABLE "project_employee_join" DROP CONSTRAINT "pej_project_fkey";

ALTER TABLE "person_changed" DROP CONSTRAINT "person_changed_person_fkey";

ALTER TABLE "person_change_request" DROP CONSTRAINT "person_chgreq_person_fkey";

ALTER TABLE "person" DROP CONSTRAINT "person_company_fkey";

ALTER TABLE "person_cr" DROP CONSTRAINT "person_cr_approver_fkey";

ALTER TABLE "person_cr" DROP CONSTRAINT "person_cr_project_fkey";

ALTER TABLE "person_cr" DROP CONSTRAINT "person_cr_real_fkey";

ALTER TABLE "person_cr" DROP CONSTRAINT "person_cr_recvhg_fkey";

ALTER TABLE "person_cr" DROP CONSTRAINT "person_cr_requestor_fkey";

ALTER TABLE "person" DROP CONSTRAINT "person_dflt_project_fkey";

ALTER TABLE "person_form" DROP CONSTRAINT "person_form_person_fkey";

ALTER TABLE "person_form" DROP CONSTRAINT "person_form_type_fkey";

ALTER TABLE "org_group_association" DROP CONSTRAINT "person_group_fkey";

ALTER TABLE "person_note" DROP CONSTRAINT "person_note_cat_fkey";

ALTER TABLE "person_note" DROP CONSTRAINT "person_note_emp_fkey";

ALTER TABLE "person" DROP CONSTRAINT "person_recper_fkey";

ALTER TABLE "personal_reference" DROP CONSTRAINT "personal_ref_person_fkey";

ALTER TABLE "phone_cr" DROP CONSTRAINT "phone_cr_approver_fkey";

ALTER TABLE "phone_cr" DROP CONSTRAINT "phone_cr_project_fkey";

ALTER TABLE "phone_cr" DROP CONSTRAINT "phone_cr_real_fkey";

ALTER TABLE "phone_cr" DROP CONSTRAINT "phone_cr_recvhg_fkey";

ALTER TABLE "phone_cr" DROP CONSTRAINT "phone_cr_requestor_fkey";

ALTER TABLE "phone" DROP CONSTRAINT "phone_org_group_fkey";

ALTER TABLE "phone" DROP CONSTRAINT "phone_person_fkey";

ALTER TABLE "physician" DROP CONSTRAINT "physicians_benefit_fkey";

ALTER TABLE "project_view_join" DROP CONSTRAINT "ppv_child_fkey";

ALTER TABLE "project_view_join" DROP CONSTRAINT "ppv_parent_fkey";

ALTER TABLE "previous_employment" DROP CONSTRAINT "prev_emp_person_fkey";

ALTER TABLE "process_history" DROP CONSTRAINT "process_history_sched_fkey";

ALTER TABLE "product_service" DROP CONSTRAINT "prod_serv_company_fkey";

ALTER TABLE "product_type" DROP CONSTRAINT "prod_typ_company_fkey";

ALTER TABLE "product_attribute_choice" DROP CONSTRAINT "product_att_choice_prod_fkey";

ALTER TABLE "product_attribute_dependency" DROP CONSTRAINT "product_attr_dep_ch_fkey";

ALTER TABLE "product_attribute_dependency" DROP CONSTRAINT "product_attr_dep_prod_fkey";

ALTER TABLE "product_attribute" DROP CONSTRAINT "product_attribute_company_fkey";

ALTER TABLE "product" DROP CONSTRAINT "product_product_service_fkey";

ALTER TABLE "product" DROP CONSTRAINT "product_product_type_fkey";

ALTER TABLE "product_service" DROP CONSTRAINT "product_service_exp_acct_fkey";

ALTER TABLE "product_type" DROP CONSTRAINT "product_type_self_fkey";

ALTER TABLE "product" DROP CONSTRAINT "product_vendor_fkey";

ALTER TABLE "project" DROP CONSTRAINT "project_app_per_fkey";

ALTER TABLE "project_category" DROP CONSTRAINT "project_catagory_comp_fkey";

ALTER TABLE "project" DROP CONSTRAINT "project_category_fkey";

ALTER TABLE "project_checklist_detail" DROP CONSTRAINT "project_chklst_chklst_fkey";

ALTER TABLE "project_checklist_detail" DROP CONSTRAINT "project_chklst_person_id_fkey";

ALTER TABLE "project_checklist_detail" DROP CONSTRAINT "project_chklst_project_fkey";

ALTER TABLE "project_comment" DROP CONSTRAINT "project_comment_person_fkey";

ALTER TABLE "project_comment" DROP CONSTRAINT "project_comment_project_id_fkey";

ALTER TABLE "project" DROP CONSTRAINT "project_current_route_stop_fkey";

ALTER TABLE "project_form" DROP CONSTRAINT "project_form_project_fkey";

ALTER TABLE "project_form" DROP CONSTRAINT "project_form_type_fkey";

ALTER TABLE "project_history_person" DROP CONSTRAINT "project_hist_pers_hist_fkey";

ALTER TABLE "project" DROP CONSTRAINT "project_owning_company_fkey";

ALTER TABLE "project_phase" DROP CONSTRAINT "project_phase_comp_fkey";

ALTER TABLE "project" DROP CONSTRAINT "project_product_fkey";

ALTER TABLE "project" DROP CONSTRAINT "project_proj_type_fkey";

ALTER TABLE "project" DROP CONSTRAINT "project_req_orggrp_fkey";

ALTER TABLE "project" DROP CONSTRAINT "project_route_fkey";

ALTER TABLE "project_status" DROP CONSTRAINT "project_status_comp_fkey";

ALTER TABLE "project" DROP CONSTRAINT "project_status_fkey";

ALTER TABLE "project" DROP CONSTRAINT "project_subject_person_fkey";

ALTER TABLE "project_template_benefit_a" DROP CONSTRAINT "project_tembena_pers_fkey";

ALTER TABLE "project_template_benefit" DROP CONSTRAINT "project_tempben_bcr_fkey";

ALTER TABLE "project_template_benefit" DROP CONSTRAINT "project_tempben_org_fkey";

ALTER TABLE "project_template_benefit" DROP CONSTRAINT "project_tempben_pcat_fkey";

ALTER TABLE "project_template_benefit" DROP CONSTRAINT "project_tempben_pstat_fkey";

ALTER TABLE "project_template_benefit" DROP CONSTRAINT "project_tempben_ptyp_fkey";

ALTER TABLE "project_template_benefit" DROP CONSTRAINT "project_tempbene_ben_fkey";

ALTER TABLE "project_template_benefit" DROP CONSTRAINT "project_tempbene_stat_fkey";

ALTER TABLE "project_template_benefit_a" DROP CONSTRAINT "project_tmpbena_temp_fkey";

ALTER TABLE "project_type" DROP CONSTRAINT "project_type_comp_fkey";

ALTER TABLE "project_status_rs_join" DROP CONSTRAINT "projstat_rsj_ps_fkey";

ALTER TABLE "project_status_rs_join" DROP CONSTRAINT "projstat_rsj_rs_kfey";

ALTER TABLE "prophet_login" DROP CONSTRAINT "prophet_login_nc_scrngrp_fkey";

ALTER TABLE "appointment_person_join" DROP CONSTRAINT "prospect_appt_appt_id_fkey";

ALTER TABLE "appointment" DROP CONSTRAINT "prospect_appt_org_grp_fkey";

ALTER TABLE "appointment_person_join" DROP CONSTRAINT "prospect_appt_person_fkey";

ALTER TABLE "company_question_detail" DROP CONSTRAINT "prospect_comp_ques_fkey";

ALTER TABLE "prospect" DROP CONSTRAINT "prospect_company_base_fkey";

ALTER TABLE "prospect" DROP CONSTRAINT "prospect_company_fkey";

ALTER TABLE "contact_question_detail" DROP CONSTRAINT "prospect_cont_ques_fkey";

ALTER TABLE "prospect_contact" DROP CONSTRAINT "prospect_contact_person_fkey";

ALTER TABLE "prospect_h" DROP CONSTRAINT "prospect_h_company_fkey";

ALTER TABLE "prospect_h" DROP CONSTRAINT "prospect_h_person_fkey";

ALTER TABLE "prospect_h" DROP CONSTRAINT "prospect_h_prospect_type_fkey";

ALTER TABLE "prospect_h" DROP CONSTRAINT "prospect_h_recpers_fkey";

ALTER TABLE "prospect_log" DROP CONSTRAINT "prospect_log_company_fkey";

ALTER TABLE "prospect_log" DROP CONSTRAINT "prospect_log_person_fkey";

ALTER TABLE "prospect_log" DROP CONSTRAINT "prospect_log_result_fkey";

ALTER TABLE "prospect_log" DROP CONSTRAINT "prospect_log_salesact_fkey";

ALTER TABLE "prospect" DROP CONSTRAINT "prospect_person_id_fkey";

ALTER TABLE "prospect" DROP CONSTRAINT "prospect_prospect_type_fkey";

ALTER TABLE "prospect" DROP CONSTRAINT "prospect_recchgper_fkey";

ALTER TABLE "prospect_source" DROP CONSTRAINT "prospect_source_company_fkey";

ALTER TABLE "prospect" DROP CONSTRAINT "prospect_source_fkey";

ALTER TABLE "prospect" DROP CONSTRAINT "prospect_status_chk";

ALTER TABLE "prospect_status" DROP CONSTRAINT "prospect_status_company_fkey";

ALTER TABLE "prospect_type" DROP CONSTRAINT "prospect_type_company_fkey";

ALTER TABLE "project_view" DROP CONSTRAINT "pview_person_fkey";

ALTER TABLE "project_view" DROP CONSTRAINT "pview_project_fkey";

ALTER TABLE "quickbooks_sync" DROP CONSTRAINT "qb_org_group_fkey";

ALTER TABLE "quote_table" DROP CONSTRAINT "quote_accept_peron_fkey";

ALTER TABLE "quote_adjustment" DROP CONSTRAINT "quote_adj_quote_fkey";

ALTER TABLE "quote_table" DROP CONSTRAINT "quote_client_fkey";

ALTER TABLE "quote_table" DROP CONSTRAINT "quote_created_by_fkey";

ALTER TABLE "quote_table" DROP CONSTRAINT "quote_final_by";

ALTER TABLE "quote_table" DROP CONSTRAINT "quote_location_fkey";

ALTER TABLE "quote_product" DROP CONSTRAINT "quote_prod_prod_fkey";

ALTER TABLE "quote_product" DROP CONSTRAINT "quote_prod_quote_fkey";

ALTER TABLE "quote_template_product" DROP CONSTRAINT "quote_tempprod_prod_fkey";

ALTER TABLE "quote_template_product" DROP CONSTRAINT "quote_temprod_quote_fkey";

ALTER TABLE "receipt" DROP CONSTRAINT "receipt_bank_draft_fkey";

ALTER TABLE "receipt_join" DROP CONSTRAINT "receipt_join_invoice_fkey";

ALTER TABLE "receipt_join" DROP CONSTRAINT "receipt_join_receipt_fkey";

ALTER TABLE "report_column" DROP CONSTRAINT "report_column_report_fkey";

ALTER TABLE "report" DROP CONSTRAINT "report_company_fkey";

ALTER TABLE "report_graphic" DROP CONSTRAINT "report_graphic_report_fkey";

ALTER TABLE "report_selection" DROP CONSTRAINT "report_selection_report_fkey";

ALTER TABLE "report_title" DROP CONSTRAINT "report_title_report_fkey";

ALTER TABLE "rights_association" DROP CONSTRAINT "rights_association_right_id_fkey";

ALTER TABLE "rights_association" DROP CONSTRAINT "rights_association_security_group_id_fkey";

ALTER TABLE "route_type_assoc" DROP CONSTRAINT "route_assoc_cat_fkey";

ALTER TABLE "route_type_assoc" DROP CONSTRAINT "route_assoc_route_fkey";

ALTER TABLE "route_type_assoc" DROP CONSTRAINT "route_assoc_type_fkey";

ALTER TABLE "route" DROP CONSTRAINT "route_comp_fkey";

ALTER TABLE "project_history" DROP CONSTRAINT "route_hist_from_status_fkey";

ALTER TABLE "project_history" DROP CONSTRAINT "route_hist_from_stop_fkey";

ALTER TABLE "project_history" DROP CONSTRAINT "route_hist_person_fkey";

ALTER TABLE "project_history" DROP CONSTRAINT "route_hist_project_fkey";

ALTER TABLE "project_history" DROP CONSTRAINT "route_hist_to_status_fkey";

ALTER TABLE "project_history" DROP CONSTRAINT "route_hist_to_stop_kkey";

ALTER TABLE "route_path" DROP CONSTRAINT "route_path_from_status_fkey";

ALTER TABLE "route_path" DROP CONSTRAINT "route_path_from_stop_fkey";

ALTER TABLE "route_path" DROP CONSTRAINT "route_path_to_status_fkey";

ALTER TABLE "route_path" DROP CONSTRAINT "route_path_to_stop_fkey";

ALTER TABLE "route" DROP CONSTRAINT "route_project_status_fkey";

ALTER TABLE "route" DROP CONSTRAINT "route_route_stop_fkey";

ALTER TABLE "route_stop_checklist" DROP CONSTRAINT "route_stop_cl_rsid_fkey";

ALTER TABLE "route_stop" DROP CONSTRAINT "route_stop_org_group_fkey";

ALTER TABLE "route_stop" DROP CONSTRAINT "route_stop_phase_fkey";

ALTER TABLE "route_stop" DROP CONSTRAINT "route_stop_route_fkey";

ALTER TABLE "sales_activity" DROP CONSTRAINT "sales_activity_company_fkey";

ALTER TABLE "sales_activity_result" DROP CONSTRAINT "sales_actres_act_fkey";

ALTER TABLE "sales_points" DROP CONSTRAINT "sales_points_emp_fkey";

ALTER TABLE "sales_points" DROP CONSTRAINT "sales_points_prospect_fkey";

ALTER TABLE "sales_points" DROP CONSTRAINT "sales_points_status_fkey";

ALTER TABLE "screen_group_access" DROP CONSTRAINT "screen_group_access_fk1";

ALTER TABLE "screen_group_access" DROP CONSTRAINT "screen_group_access_fk2";

ALTER TABLE "screen_group_hierarchy" DROP CONSTRAINT "screen_group_hier_screen_fkey";

ALTER TABLE "screen_group_hierarchy" DROP CONSTRAINT "screen_group_hierarchy_child_screen_group_id_fkey";

ALTER TABLE "screen_group_hierarchy" DROP CONSTRAINT "screen_group_hierarchy_parent_screen_group_id_fkey";

ALTER TABLE "screen_group" DROP CONSTRAINT "screen_group_screen_id_fkey";

ALTER TABLE "security_group_access" DROP CONSTRAINT "security_group_acc_fk1";

ALTER TABLE "security_group_access" DROP CONSTRAINT "security_group_acc_fk2";

ALTER TABLE "security_group_hierarchy" DROP CONSTRAINT "security_group_hierarchy_child_security_group_id_fkey";

ALTER TABLE "security_group_hierarchy" DROP CONSTRAINT "security_group_hierarchy_parent_security_group_id_fkey";

ALTER TABLE "service" DROP CONSTRAINT "service_product_service_fkey";

ALTER TABLE "service_subscribed" DROP CONSTRAINT "service_sub_org_fkey";

ALTER TABLE "service_subscribed_join" DROP CONSTRAINT "service_subjoin_org_fkey";

ALTER TABLE "service_subscribed_join" DROP CONSTRAINT "service_subjoin_service_fkey";

ALTER TABLE "standard_project" DROP CONSTRAINT "sp_bill_at_org_group_fkey";

ALTER TABLE "standard_project" DROP CONSTRAINT "sp_managing_emp_fkey";

ALTER TABLE "standard_project" DROP CONSTRAINT "sp_sponsor_fkey";

ALTER TABLE "project" DROP CONSTRAINT "sponsor_fkey";

ALTER TABLE "spousal_insurance_verif" DROP CONSTRAINT "spousal_verif_person_fkey";

ALTER TABLE "standard_project" DROP CONSTRAINT "standard_project_comp_fkey";

ALTER TABLE "standard_project" DROP CONSTRAINT "std_project_category_fkey";

ALTER TABLE "standard_project" DROP CONSTRAINT "std_project_product_fkey";

ALTER TABLE "standard_project" DROP CONSTRAINT "std_project_proj_type_fkey";

ALTER TABLE "student_verification" DROP CONSTRAINT "student_verif_person_fkey";

ALTER TABLE "time_off_request" DROP CONSTRAINT "time_off_req_app_person";

ALTER TABLE "time_off_request" DROP CONSTRAINT "time_off_req_proj_fkey";

ALTER TABLE "time_off_request" DROP CONSTRAINT "time_off_request_person_fkey";

ALTER TABLE "time_reject" DROP CONSTRAINT "time_reject_person_fkey";

ALTER TABLE "time_off_accrual_calc" DROP CONSTRAINT "timeoff_acc_calc_config_fkey";

ALTER TABLE "time_off_accrual_seniority" DROP CONSTRAINT "timeoff_accsen_calc_fkey";

ALTER TABLE "timesheet" DROP CONSTRAINT "timesheet_beg_person_id";

ALTER TABLE "timesheet" DROP CONSTRAINT "timesheet_end_person_id";

ALTER TABLE "timesheet" DROP CONSTRAINT "timesheet_invoice_fkey";

ALTER TABLE "timesheet" DROP CONSTRAINT "timesheet_message_fkey";

ALTER TABLE "timesheet" DROP CONSTRAINT "timesheet_person_id_fkey";

ALTER TABLE "timesheet" DROP CONSTRAINT "timesheet_project_id_fkey";

ALTER TABLE "timesheet" DROP CONSTRAINT "timesheet_wagetype_fkey";

ALTER TABLE "user_attribute" DROP CONSTRAINT "user_attribute_person_fkey";

ALTER TABLE "vendor" DROP CONSTRAINT "vandor_dflt_ap_acct";

ALTER TABLE "vendor" DROP CONSTRAINT "vandor_dflt_expense_acct";

ALTER TABLE "vendor" DROP CONSTRAINT "vendor_company_company_fkey";

ALTER TABLE "vendor" DROP CONSTRAINT "vendor_company_fkey";

ALTER TABLE "vendor_group" DROP CONSTRAINT "vendor_group_org_fkey";

ALTER TABLE "vendor_group" DROP CONSTRAINT "vendor_group_vendor_fkey";

ALTER TABLE "vendor_contact" DROP CONSTRAINT "vendor_person_fkey";

ALTER TABLE "wage_paid_detail" DROP CONSTRAINT "wage_detail_paid_fkey";

ALTER TABLE "wage_paid_detail" DROP CONSTRAINT "wage_paid_det_type_fkey";

ALTER TABLE "wage_paid" DROP CONSTRAINT "wage_paid_employee_fkey";

ALTER TABLE "wage_type" DROP CONSTRAINT "wage_type_gl_fkey";

ALTER TABLE "wage_type" DROP CONSTRAINT "wage_type_liab_fkey";

ALTER TABLE "wage_type" DROP CONSTRAINT "wage_type_org_fkey";

ALTER TABLE "wizard_config_benefit" DROP CONSTRAINT "wizard_ben_ben_fkey";

ALTER TABLE "wizard_config_benefit" DROP CONSTRAINT "wizard_ben_screen_fkey";

ALTER TABLE "wizard_config_benefit" DROP CONSTRAINT "wizard_ben_wcat_fkey";

ALTER TABLE "wizard_config_category" DROP CONSTRAINT "wizard_cat_config_fkey";

ALTER TABLE "wizard_config_category" DROP CONSTRAINT "wizard_cat_fkey";

ALTER TABLE "wizard_config_category" DROP CONSTRAINT "wizard_cat_screen_fkey";

ALTER TABLE "wizard_configuration" DROP CONSTRAINT "wizard_conf_benclass_fkey";

ALTER TABLE "wizard_configuration" DROP CONSTRAINT "wizard_conf_company_fkey";

ALTER TABLE "wizard_config_config" DROP CONSTRAINT "wizard_conf_conf_fkey";

ALTER TABLE "wizard_configuration" DROP CONSTRAINT "wizard_conf_projcat_fkey";

ALTER TABLE "wizard_configuration" DROP CONSTRAINT "wizard_conf_projstat_fkey";

ALTER TABLE "wizard_configuration" DROP CONSTRAINT "wizard_conf_projtype_fkey";

ALTER TABLE "wizard_config_config" DROP CONSTRAINT "wizard_conf_wben_fkey";

ALTER TABLE "wizard_configuration" DROP CONSTRAINT "wizard_config_contact_fkey";

ALTER TABLE "wizard_configuration" DROP CONSTRAINT "wizard_config_demoscn_fkey";

ALTER TABLE "wizard_config_project_a" DROP CONSTRAINT "wizard_confproja_pers_fkey";

ALTER TABLE "wizard_config_project_a" DROP CONSTRAINT "wizard_confproja_wiz_fkey";

ALTER TABLE "wizard_project" DROP CONSTRAINT "wizard_project_benjoin_fkey";

ALTER TABLE "wizard_project" DROP CONSTRAINT "wizard_project_hist_fkey";

ALTER TABLE "wizard_project" DROP CONSTRAINT "wizard_project_pers_comp_fkey";

ALTER TABLE "wizard_project" DROP CONSTRAINT "wizard_project_project_fkey";





--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "wizard_configuration" ADD COLUMN "allow_report_from_review" character(1) DEFAULT 'Y' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "wizard_configuration" ADD CONSTRAINT "wizard_config_review_rpt_chk" CHECK (((allow_report_from_review='Y')OR(allow_report_from_review='N')));

COMMENT ON COLUMN "wizard_configuration"."allow_report_from_review" IS 'Allow the benefit summary report on the benefit review screen';



-- ----------------------------------------------------------

ALTER TABLE ONLY "address_cr" ADD CONSTRAINT "address_cr_approver_fkey" FOREIGN KEY ("approver_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "address_cr" ADD CONSTRAINT "address_cr_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "address_cr" ADD CONSTRAINT "address_cr_real_fkey" FOREIGN KEY ("real_record_id") REFERENCES "address" ("address_id");

ALTER TABLE ONLY "address_cr" ADD CONSTRAINT "address_cr_recvhg_fkey" FOREIGN KEY ("change_record_id") REFERENCES "address" ("address_id");

ALTER TABLE ONLY "address_cr" ADD CONSTRAINT "address_cr_requestor_fkey" FOREIGN KEY ("requestor_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "address" ADD CONSTRAINT "address_org_group_fkey" FOREIGN KEY ("org_group_join") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "address" ADD CONSTRAINT "address_person_fkey" FOREIGN KEY ("person_join") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "agency" ADD CONSTRAINT "agency_company_fkey" FOREIGN KEY ("agency_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "agency_join" ADD CONSTRAINT "agency_join_agency_fkey" FOREIGN KEY ("agency_id") REFERENCES "agency" ("agency_id");

ALTER TABLE ONLY "agency_join" ADD CONSTRAINT "agency_join_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "agent_join" ADD CONSTRAINT "agent_join_agent_fkey" FOREIGN KEY ("agent_id") REFERENCES "agent" ("agent_id");

ALTER TABLE ONLY "agent_join" ADD CONSTRAINT "agent_join_apppers_fkey" FOREIGN KEY ("approved_by_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "agent_join" ADD CONSTRAINT "agent_join_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "agent" ADD CONSTRAINT "agent_person_fkey" FOREIGN KEY ("agent_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "agreement_person_join" ADD CONSTRAINT "agreement_person_agree_fkey" FOREIGN KEY ("agreement_form_id") REFERENCES "agreement_form" ("agreement_form_id");

ALTER TABLE ONLY "agreement_person_join" ADD CONSTRAINT "agreement_person_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "alert" ADD CONSTRAINT "alert_last_person_fkey" FOREIGN KEY ("last_change_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "alert" ADD CONSTRAINT "alert_org_group_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "alert_person_join" ADD CONSTRAINT "alert_person_alert_fkey" FOREIGN KEY ("alert_id") REFERENCES "alert" ("alert_id");

ALTER TABLE ONLY "alert" ADD CONSTRAINT "alert_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "alert_person_join" ADD CONSTRAINT "alert_person_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "applicant_position_info" ADD CONSTRAINT "app_pos_info_pos_fkey" FOREIGN KEY ("applicant_position_id") REFERENCES "applicant_position" ("applicant_position_id");

ALTER TABLE ONLY "applicant_question_choice" ADD CONSTRAINT "app_ques_choice_ques_fkey" FOREIGN KEY ("applicant_question_id") REFERENCES "applicant_question" ("applicant_question_id");

ALTER TABLE ONLY "applicant_answer" ADD CONSTRAINT "applicant_ans_choice_fkey" FOREIGN KEY ("applicant_question_choice_id") REFERENCES "applicant_question_choice" ("applicant_question_choice_id");

ALTER TABLE ONLY "applicant_answer" ADD CONSTRAINT "applicant_ans_person_fkey" FOREIGN KEY ("person_id") REFERENCES "applicant" ("person_id");

ALTER TABLE ONLY "applicant_answer" ADD CONSTRAINT "applicant_answer_ques_fkey" FOREIGN KEY ("applicant_question_id") REFERENCES "applicant_question" ("applicant_question_id");

ALTER TABLE ONLY "applicant" ADD CONSTRAINT "applicant_applicant_source_fkey" FOREIGN KEY ("applicant_source_id") REFERENCES "applicant_source" ("applicant_source_id");

ALTER TABLE ONLY "applicant" ADD CONSTRAINT "applicant_applicant_status_fkey" FOREIGN KEY ("applicant_status_id") REFERENCES "applicant_status" ("applicant_status_id");

ALTER TABLE ONLY "applicant_application" ADD CONSTRAINT "applicant_application_applicant_app_status_fkey" FOREIGN KEY ("applicant_app_status_id") REFERENCES "applicant_app_status" ("applicant_app_status_id");

ALTER TABLE ONLY "applicant_application" ADD CONSTRAINT "applicant_application_applicant_fkey" FOREIGN KEY ("person_id") REFERENCES "applicant" ("person_id");

ALTER TABLE ONLY "applicant_application" ADD CONSTRAINT "applicant_application_applicant_position_fkey" FOREIGN KEY ("applicant_position_id") REFERENCES "applicant_position" ("applicant_position_id");

ALTER TABLE ONLY "applicant_application" ADD CONSTRAINT "applicant_application_applicant_source_fkey" FOREIGN KEY ("applicant_source_id") REFERENCES "applicant_source" ("applicant_source_id");

ALTER TABLE ONLY "applicant_app_status" ADD CONSTRAINT "applicant_appstat_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "applicant_contact" ADD CONSTRAINT "applicant_contact_applicant_application_fkey" FOREIGN KEY ("applicant_application_id") REFERENCES "applicant_application" ("applicant_application_id");

ALTER TABLE ONLY "applicant_contact" ADD CONSTRAINT "applicant_contact_person_fkey" FOREIGN KEY ("person_id") REFERENCES "applicant" ("person_id");

ALTER TABLE ONLY "applicant" ADD CONSTRAINT "applicant_eeo_race_fkey" FOREIGN KEY ("eeo_race_id") REFERENCES "hr_eeo_race" ("eeo_id");

ALTER TABLE ONLY "applicant" ADD CONSTRAINT "applicant_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "applicant_position" ADD CONSTRAINT "applicant_position_job_type_fkey" FOREIGN KEY ("job_type_id") REFERENCES "job_type" ("job_type_id");

ALTER TABLE ONLY "applicant_position" ADD CONSTRAINT "applicant_position_org_group_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "applicant_question" ADD CONSTRAINT "applicant_question_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "applicant_question" ADD CONSTRAINT "applicant_question_job_type_fkey" FOREIGN KEY ("job_type_id") REFERENCES "job_type" ("job_type_id");

ALTER TABLE ONLY "applicant_source" ADD CONSTRAINT "applicant_source_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "applicant_status" ADD CONSTRAINT "applicant_status_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "applicant_application" ADD CONSTRAINT "application_app_job_type_fkey" FOREIGN KEY ("job_type_id") REFERENCES "job_type" ("job_type_id");

ALTER TABLE ONLY "appointment_location" ADD CONSTRAINT "appointment_loc_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "appointment" ADD CONSTRAINT "appointment_location_id_fkey" FOREIGN KEY ("location_id") REFERENCES "appointment_location" ("location_id");

ALTER TABLE ONLY "assembly_template_detail" ADD CONSTRAINT "assem_temp_temp_fkey" FOREIGN KEY ("assembly_template_id") REFERENCES "assembly_template" ("assembly_template_id");

ALTER TABLE ONLY "assembly_template_detail" ADD CONSTRAINT "assembly_temp_parent_fkey" FOREIGN KEY ("parent_detail_id") REFERENCES "assembly_template_detail" ("assembly_template_detail_id");

ALTER TABLE ONLY "assembly_template_detail" ADD CONSTRAINT "assembly_template_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("product_id");

ALTER TABLE ONLY "bank_account" ADD CONSTRAINT "bank_account_org_group_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "bank_draft_detail" ADD CONSTRAINT "bank_draft_detail_draft_fkey" FOREIGN KEY ("bank_draft_id") REFERENCES "bank_draft_batch" ("bank_draft_id");

ALTER TABLE ONLY "bank_draft_detail" ADD CONSTRAINT "bank_draft_detail_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "bank_draft_history" ADD CONSTRAINT "bank_draft_hist_draft_fkey" FOREIGN KEY ("bank_draft_id") REFERENCES "bank_draft_batch" ("bank_draft_id");

ALTER TABLE ONLY "bank_draft_batch" ADD CONSTRAINT "bank_draftbatch_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "benefit_change_reason_doc" ADD CONSTRAINT "bcr_doc_bcr_fkey" FOREIGN KEY ("bcr_id") REFERENCES "hr_benefit_change_reason" ("bcr_id");

ALTER TABLE ONLY "benefit_change_reason_doc" ADD CONSTRAINT "bcr_doc_comp_fkey" FOREIGN KEY ("company_form_id") REFERENCES "company_form" ("company_form_id");

ALTER TABLE ONLY "benefit_config_cost_age" ADD CONSTRAINT "ben_con_cost_age_con_fkey" FOREIGN KEY ("benefit_config_cost_id") REFERENCES "benefit_config_cost" ("benefit_config_cost_id");

ALTER TABLE ONLY "benefit_config_cost_status" ADD CONSTRAINT "ben_concost_stat_emp_fkey" FOREIGN KEY ("employee_status_id") REFERENCES "hr_employee_status" ("status_id");

ALTER TABLE ONLY "benefit_config_cost" ADD CONSTRAINT "ben_conf_cost_config_fkey" FOREIGN KEY ("benefit_config_id") REFERENCES "hr_benefit_config" ("benefit_config_id");

ALTER TABLE ONLY "benefit_config_cost_status" ADD CONSTRAINT "ben_conf_cost_st_con_fkey" FOREIGN KEY ("benefit_config_cost_id") REFERENCES "benefit_config_cost" ("benefit_config_cost_id");

ALTER TABLE ONLY "benefit_question_choice" ADD CONSTRAINT "ben_ques_choice_ques_fkey" FOREIGN KEY ("benefit_question_id") REFERENCES "benefit_question" ("benefit_question_id");

ALTER TABLE ONLY "benefit_group_class_join" ADD CONSTRAINT "benconf_clsjn_ben_fkey" FOREIGN KEY ("benefit_class_id") REFERENCES "hr_benefit_class" ("benefit_class_id");

ALTER TABLE ONLY "benefit_group_class_join" ADD CONSTRAINT "benconf_conclsjn_ben_fkey" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "benefit_answer" ADD CONSTRAINT "benefit_ans_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "benefit_answer_h" ADD CONSTRAINT "benefit_ansh_h_recper_fkey" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "benefit_answer_h" ADD CONSTRAINT "benefit_ansh_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "benefit_answer_h" ADD CONSTRAINT "benefit_ansh_ques_fkey" FOREIGN KEY ("benefit_question_id") REFERENCES "benefit_question" ("benefit_question_id");

ALTER TABLE ONLY "benefit_answer" ADD CONSTRAINT "benefit_answer_ques_fkey" FOREIGN KEY ("benefit_question_id") REFERENCES "benefit_question" ("benefit_question_id");

ALTER TABLE ONLY "benefit_answer" ADD CONSTRAINT "benefit_answer_recper_fkey" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "benefit_class_join" ADD CONSTRAINT "benefit_class_join_class_fkey" FOREIGN KEY ("benefit_class_id") REFERENCES "hr_benefit_class" ("benefit_class_id");

ALTER TABLE ONLY "benefit_class_join" ADD CONSTRAINT "benefit_class_join_config_fkey" FOREIGN KEY ("benefit_config_id") REFERENCES "hr_benefit_config" ("benefit_config_id");

ALTER TABLE ONLY "benefit_config_cost" ADD CONSTRAINT "benefit_concost_org_kfkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "benefit_dependency" ADD CONSTRAINT "benefit_dependency_benefit_fkey" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "benefit_dependency" ADD CONSTRAINT "benefit_dependency_required_fkey" FOREIGN KEY ("required_benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "benefit_document" ADD CONSTRAINT "benefit_doc_benefit_fkey" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "benefit_document" ADD CONSTRAINT "benefit_doc_comp_fkey" FOREIGN KEY ("company_form_id") REFERENCES "company_form" ("company_form_id");

ALTER TABLE ONLY "benefit_question" ADD CONSTRAINT "benefit_ques_benefit_fkey" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "benefit_question" ADD CONSTRAINT "benefit_ques_recper_fkey" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "benefit_question_h" ADD CONSTRAINT "benefit_quesh_benefit_fkey" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "benefit_question_h" ADD CONSTRAINT "benefit_quesh_recper_fkey" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "benefit_restriction" ADD CONSTRAINT "benefit_restriction_bcr_fkey" FOREIGN KEY ("bcr_id") REFERENCES "hr_benefit_change_reason" ("bcr_id");

ALTER TABLE ONLY "benefit_restriction" ADD CONSTRAINT "benefit_restriction_bencat_fkey" FOREIGN KEY ("benefit_cat_id") REFERENCES "hr_benefit_category" ("benefit_cat_id");

ALTER TABLE ONLY "hr_benefit_join" ADD CONSTRAINT "benenfit_join_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "bill_at_org_group_fkey" FOREIGN KEY ("bill_at_org_group") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "hr_checklist_detail" ADD CONSTRAINT "checklist_detail_item_fkey" FOREIGN KEY ("checklist_item_id") REFERENCES "hr_checklist_item" ("item_id");

ALTER TABLE ONLY "client" ADD CONSTRAINT "client_client_status_fkey" FOREIGN KEY ("client_status_id") REFERENCES "client_status" ("client_status_id");

ALTER TABLE ONLY "client" ADD CONSTRAINT "client_company_company_fkey" FOREIGN KEY ("org_group_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "client" ADD CONSTRAINT "client_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "client" ADD CONSTRAINT "client_dflt_ar_acct" FOREIGN KEY ("dflt_ar_acct") REFERENCES "gl_account" ("gl_account_id");

ALTER TABLE ONLY "client" ADD CONSTRAINT "client_dflt_sales_acct" FOREIGN KEY ("dflt_sales_acct") REFERENCES "gl_account" ("gl_account_id");

ALTER TABLE ONLY "client_contact" ADD CONSTRAINT "client_person_id_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "client_status" ADD CONSTRAINT "client_status_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "company_form_folder" ADD CONSTRAINT "comp_form_folder_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "company_form_folder_join" ADD CONSTRAINT "comp_formfoldj_fold_fkey" FOREIGN KEY ("folder_id") REFERENCES "company_form_folder" ("folder_id");

ALTER TABLE ONLY "company_form_folder_join" ADD CONSTRAINT "comp_formfoldj_form_fkey" FOREIGN KEY ("form_id") REFERENCES "company_form" ("company_form_id");

ALTER TABLE ONLY "company_detail" ADD CONSTRAINT "company_det_adv_fkey" FOREIGN KEY ("employee_advance_account_id") REFERENCES "gl_account" ("gl_account_id");

ALTER TABLE ONLY "company_detail" ADD CONSTRAINT "company_det_ar_fkey" FOREIGN KEY ("ar_account_id") REFERENCES "gl_account" ("gl_account_id");

ALTER TABLE ONLY "company_detail" ADD CONSTRAINT "company_detail_cash_fkey" FOREIGN KEY ("cash_account_id") REFERENCES "gl_account" ("gl_account_id");

ALTER TABLE ONLY "company_detail" ADD CONSTRAINT "company_detail_company_fkey" FOREIGN KEY ("org_group_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "company_form" ADD CONSTRAINT "company_form_company_fkey" FOREIGN KEY ("company_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "company_form_folder" ADD CONSTRAINT "company_form_folder_parent_fkey" FOREIGN KEY ("parent_folder_id") REFERENCES "company_form_folder" ("folder_id");

ALTER TABLE ONLY "company_form_org_join" ADD CONSTRAINT "company_form_orgj_ffkey" FOREIGN KEY ("folder_id") REFERENCES "company_form_folder" ("folder_id");

ALTER TABLE ONLY "company_form_org_join" ADD CONSTRAINT "company_form_orgj_ofkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "company_form" ADD CONSTRAINT "company_form_type_fkey" FOREIGN KEY ("form_type_id") REFERENCES "form_type" ("form_type_id");

ALTER TABLE ONLY "company_question_detail" ADD CONSTRAINT "company_ques_det_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "company_question" ADD CONSTRAINT "company_question_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "contact_question" ADD CONSTRAINT "contact_question_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "contact_question_detail" ADD CONSTRAINT "contact_question_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "drc_form_event" ADD CONSTRAINT "drc_form_event_causing_per_fkey" FOREIGN KEY ("person_id_causing_event") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "drc_form_event" ADD CONSTRAINT "drc_form_event_emp_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "drc_form_event" ADD CONSTRAINT "drc_form_event_per_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "drc_import_benefit" ADD CONSTRAINT "drc_import_ben_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "drc_import_benefit_join" ADD CONSTRAINT "drc_import_benjn_ben_fkey" FOREIGN KEY ("import_benefit_id") REFERENCES "drc_import_benefit" ("imported_benefit_id");

ALTER TABLE ONLY "drc_import_benefit_join" ADD CONSTRAINT "drc_import_benjn_sub_fkey" FOREIGN KEY ("subscriber_id") REFERENCES "drc_import_enrollee" ("enrollee_id");

ALTER TABLE ONLY "drc_import_benefit_join" ADD CONSTRAINT "drc_import_benjoin_enr_fkey" FOREIGN KEY ("enrollee_id") REFERENCES "drc_import_enrollee" ("enrollee_id");

ALTER TABLE ONLY "drc_import_enrollee" ADD CONSTRAINT "drc_import_enr_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "drc_import_enrollee" ADD CONSTRAINT "drc_import_enr_pers_fkey" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "e_signature" ADD CONSTRAINT "e_signature_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "edi_transaction" ADD CONSTRAINT "edi_trans_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "education" ADD CONSTRAINT "education_person_fky" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "hr_eeo1" ADD CONSTRAINT "eeo1_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "electronic_fund_transfer" ADD CONSTRAINT "eft_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "electronic_fund_transfer" ADD CONSTRAINT "eft_wage_type_fkey" FOREIGN KEY ("wage_type_id") REFERENCES "wage_type" ("wage_type_id");

ALTER TABLE ONLY "employee" ADD CONSTRAINT "employee_bank_fkey" FOREIGN KEY ("payroll_bank_code") REFERENCES "bank_account" ("bank_account_id");

ALTER TABLE ONLY "employee" ADD CONSTRAINT "employee_class_fkey" FOREIGN KEY ("benefit_class_id") REFERENCES "hr_benefit_class" ("benefit_class_id");

ALTER TABLE ONLY "employee" ADD CONSTRAINT "employee_eeo_category_fkey" FOREIGN KEY ("eeo_category_id") REFERENCES "hr_eeo_category" ("eeo_category_id");

ALTER TABLE ONLY "employee" ADD CONSTRAINT "employee_eeo_race_fkey" FOREIGN KEY ("eeo_race_id") REFERENCES "hr_eeo_race" ("eeo_id");

ALTER TABLE ONLY "employee" ADD CONSTRAINT "employee_emp_status_fkey" FOREIGN KEY ("status_id") REFERENCES "hr_employee_status" ("status_id");

ALTER TABLE ONLY "employee" ADD CONSTRAINT "employee_person_id_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "form_type" ADD CONSTRAINT "form_type_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "garnishment_type" ADD CONSTRAINT "garn_typ_wage_typ_fkey" FOREIGN KEY ("wage_type_id") REFERENCES "wage_type" ("wage_type_id");

ALTER TABLE ONLY "garnishment" ADD CONSTRAINT "garnishment_person_fkey" FOREIGN KEY ("person_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "garnishment" ADD CONSTRAINT "garnishment_remit_to_fksy" FOREIGN KEY ("remit_to") REFERENCES "address" ("address_id");

ALTER TABLE ONLY "garnishment" ADD CONSTRAINT "garnishment_type_fkey" FOREIGN KEY ("garnishment_type_id") REFERENCES "garnishment_type" ("garnishment_type_id");

ALTER TABLE ONLY "gl_account" ADD CONSTRAINT "gl_account_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "org_group_association" ADD CONSTRAINT "group_association_group_id_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "org_group_hierarchy" ADD CONSTRAINT "group_hierarchy_child_group_id_fkey" FOREIGN KEY ("child_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "org_group_hierarchy" ADD CONSTRAINT "group_hierarchy_parent_group_id_fkey" FOREIGN KEY ("parent_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "hr_benefit_config" ADD CONSTRAINT "he_benconf_benefit_fkey" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "holiday" ADD CONSTRAINT "holiday_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "hr_accrual" ADD CONSTRAINT "hr_accrual_employee_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "hr_accrual" ADD CONSTRAINT "hr_accural_benefit_account_fkey" FOREIGN KEY ("benefit_account") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "hr_benefit_change_reason" ADD CONSTRAINT "hr_bcr_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "hr_benefit_class" ADD CONSTRAINT "hr_ben_calss_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "hr_benefit_category_answer" ADD CONSTRAINT "hr_ben_cat_ans_ben_fkey" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "hr_benefit_category_answer" ADD CONSTRAINT "hr_ben_cat_ans_ques_fkey" FOREIGN KEY ("question_id") REFERENCES "hr_benefit_category_question" ("question_id");

ALTER TABLE ONLY "hr_benefit_category_question" ADD CONSTRAINT "hr_ben_cat_ques_cat_fkey" FOREIGN KEY ("benefit_cat_id") REFERENCES "hr_benefit_category" ("benefit_cat_id");

ALTER TABLE ONLY "hr_benefit_join" ADD CONSTRAINT "hr_ben_join_bcat_chk" FOREIGN KEY ("benefit_cat_id") REFERENCES "hr_benefit_category" ("benefit_cat_id");

ALTER TABLE ONLY "hr_benefit_join" ADD CONSTRAINT "hr_ben_join_ben_chk" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "hr_benefit_join_h" ADD CONSTRAINT "hr_ben_join_rpi" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "hr_benefit_category" ADD CONSTRAINT "hr_bencat_oescrn_fkey" FOREIGN KEY ("open_enrollment_screen_id") REFERENCES "screen" ("screen_id");

ALTER TABLE ONLY "hr_benefit_category" ADD CONSTRAINT "hr_bencat_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "hr_benefit_category" ADD CONSTRAINT "hr_bencat_oscrn_fkey" FOREIGN KEY ("onboarding_screen_id") REFERENCES "screen" ("screen_id");

ALTER TABLE ONLY "hr_benefit" ADD CONSTRAINT "hr_benefit_ben_cat_fkey" FOREIGN KEY ("benefit_cat_id") REFERENCES "hr_benefit_category" ("benefit_cat_id");

ALTER TABLE ONLY "hr_benefit_join" ADD CONSTRAINT "hr_benefit_benef_fkey" FOREIGN KEY ("benefit_config_id") REFERENCES "hr_benefit_config" ("benefit_config_id");

ALTER TABLE ONLY "hr_benefit_join" ADD CONSTRAINT "hr_benefit_join_bcr_fkey" FOREIGN KEY ("bcr_id") REFERENCES "hr_benefit_change_reason" ("bcr_id");

ALTER TABLE ONLY "hr_benefit_join" ADD CONSTRAINT "hr_benefit_join_cpers_fkey" FOREIGN KEY ("covered_person") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "hr_benefit_join" ADD CONSTRAINT "hr_benefit_join_hrel_fkey" FOREIGN KEY ("relationship_id") REFERENCES "hr_empl_dependent" ("relationship_id");

ALTER TABLE ONLY "hr_benefit_join" ADD CONSTRAINT "hr_benefit_join_levent_fkey" FOREIGN KEY ("life_event_id") REFERENCES "life_event" ("life_event_id");

ALTER TABLE ONLY "hr_benefit_join" ADD CONSTRAINT "hr_benefit_join_ppers_fkey" FOREIGN KEY ("paying_person") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "hr_benefit_join" ADD CONSTRAINT "hr_benefit_join_recper_fkey" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "hr_benefit" ADD CONSTRAINT "hr_benefit_ooscreen_fkey" FOREIGN KEY ("open_enrollment_screen_id") REFERENCES "screen" ("screen_id");

ALTER TABLE ONLY "hr_benefit" ADD CONSTRAINT "hr_benefit_oscreen_fkey" FOREIGN KEY ("onboarding_screen_id") REFERENCES "screen" ("screen_id");

ALTER TABLE ONLY "hr_benefit_package_join" ADD CONSTRAINT "hr_benefit_pj_benefit_fkey" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "hr_benefit" ADD CONSTRAINT "hr_benefit_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product_service" ("product_service_id");

ALTER TABLE ONLY "hr_benefit" ADD CONSTRAINT "hr_benefit_provider_fkey" FOREIGN KEY ("provider_id") REFERENCES "vendor" ("org_group_id");

ALTER TABLE ONLY "hr_benefit" ADD CONSTRAINT "hr_benefit_replace_fkey" FOREIGN KEY ("benefit_id_replaced_by") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "hr_benefit_rider" ADD CONSTRAINT "hr_benefit_rider_base_fkey" FOREIGN KEY ("base_benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "hr_benefit_rider" ADD CONSTRAINT "hr_benefit_rider_rider_fkey" FOREIGN KEY ("rider_benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "hr_benefit" ADD CONSTRAINT "hr_benefit_wage_type_fkey" FOREIGN KEY ("wage_type_id") REFERENCES "wage_type" ("wage_type_id");

ALTER TABLE ONLY "hr_billing_status_history" ADD CONSTRAINT "hr_bill_stat_hist_pers_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "hr_billing_status_history" ADD CONSTRAINT "hr_bill_stat_stat_fkey" FOREIGN KEY ("billing_status_id") REFERENCES "hr_billing_status" ("billing_status_id");

ALTER TABLE ONLY "hr_benefit_project_join" ADD CONSTRAINT "hr_bpjoin_config_fky" FOREIGN KEY ("benefit_config_id") REFERENCES "hr_benefit_config" ("benefit_config_id");

ALTER TABLE ONLY "hr_benefit_project_join" ADD CONSTRAINT "hr_bpjoin_prokect_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "hr_checklist_detail" ADD CONSTRAINT "hr_checklist_detail_employee_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "hr_checklist_detail" ADD CONSTRAINT "hr_checklist_detail_super_fkey" FOREIGN KEY ("supervisor_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "hr_checklist_item" ADD CONSTRAINT "hr_checklist_item_status_fkey" FOREIGN KEY ("employee_status_id") REFERENCES "hr_employee_status" ("status_id");

ALTER TABLE ONLY "hr_checklist_item" ADD CONSTRAINT "hr_chklst_item_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "hr_checklist_item" ADD CONSTRAINT "hr_chklstitm_compform_fkey" FOREIGN KEY ("company_form_id") REFERENCES "company_form" ("company_form_id");

ALTER TABLE ONLY "hr_checklist_item" ADD CONSTRAINT "hr_chklstitm_screen_fkey" FOREIGN KEY ("screen_id") REFERENCES "screen" ("screen_id");

ALTER TABLE ONLY "hr_checklist_item" ADD CONSTRAINT "hr_chklstitm_screengrp_fkey" FOREIGN KEY ("screen_group_id") REFERENCES "screen_group" ("screen_group_id");

ALTER TABLE ONLY "hr_employee_beneficiary" ADD CONSTRAINT "hr_emp_benefiary_rpi" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "hr_employee_beneficiary" ADD CONSTRAINT "hr_emp_beneficiary_eb_id" FOREIGN KEY ("benefit_join_id") REFERENCES "hr_benefit_join" ("benefit_join_id");

ALTER TABLE ONLY "hr_employee_beneficiary_h" ADD CONSTRAINT "hr_emp_beneficiary_rpi" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "hr_employee_event" ADD CONSTRAINT "hr_emp_evt_employee_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "hr_employee_event" ADD CONSTRAINT "hr_emp_evt_supervisor_fkey" FOREIGN KEY ("supervisor_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "hr_empl_dependent_cr" ADD CONSTRAINT "hr_empl_dep_cr_approver_fkey" FOREIGN KEY ("approver_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "hr_empl_dependent_cr" ADD CONSTRAINT "hr_empl_dep_cr_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "hr_empl_dependent_cr" ADD CONSTRAINT "hr_empl_dep_cr_real_fkey" FOREIGN KEY ("real_record_id") REFERENCES "hr_empl_dependent" ("relationship_id");

ALTER TABLE ONLY "hr_empl_dependent_cr" ADD CONSTRAINT "hr_empl_dep_cr_recvhg_fkey" FOREIGN KEY ("change_record_id") REFERENCES "hr_empl_dependent" ("relationship_id");

ALTER TABLE ONLY "hr_empl_dependent_cr" ADD CONSTRAINT "hr_empl_dep_cr_requestor_fkey" FOREIGN KEY ("requestor_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "hr_empl_dependent" ADD CONSTRAINT "hr_empl_dep_dep_fkey" FOREIGN KEY ("dependent_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "hr_empl_dependent" ADD CONSTRAINT "hr_empl_dep_empl_id_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "hr_empl_eval_detail" ADD CONSTRAINT "hr_empl_eval_cat_fkey" FOREIGN KEY ("cat_id") REFERENCES "hr_eval_category" ("eval_cat_id");

ALTER TABLE ONLY "hr_empl_eval_detail" ADD CONSTRAINT "hr_empl_eval_detail_eval_fkey" FOREIGN KEY ("eval_id") REFERENCES "hr_employee_eval" ("employee_eval_id");

ALTER TABLE ONLY "hr_employee_eval" ADD CONSTRAINT "hr_employee_eval_emp_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "hr_employee_eval" ADD CONSTRAINT "hr_employee_eval_super_fkey" FOREIGN KEY ("supervisor_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "hr_employee_status" ADD CONSTRAINT "hr_empstat_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "hr_emergency_contact" ADD CONSTRAINT "hr_emrcont_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "hr_eval_category" ADD CONSTRAINT "hr_eval_cat_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "hr_note_category" ADD CONSTRAINT "hr_notecat_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "hr_position" ADD CONSTRAINT "hr_pos_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "hr_position" ADD CONSTRAINT "hr_position_class_fkey" FOREIGN KEY ("benefit_class_id") REFERENCES "hr_benefit_class" ("benefit_class_id");

ALTER TABLE ONLY "hr_empl_status_history" ADD CONSTRAINT "hr_status_hist_employee_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "hr_empl_status_history" ADD CONSTRAINT "hr_status_hist_status_fkey" FOREIGN KEY ("status_id") REFERENCES "hr_employee_status" ("status_id");

ALTER TABLE ONLY "hr_training_category" ADD CONSTRAINT "hr_traincat_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "hr_training_detail" ADD CONSTRAINT "hr_training_detail_cat_fkey" FOREIGN KEY ("cat_id") REFERENCES "hr_training_category" ("cat_id");

ALTER TABLE ONLY "hr_training_detail" ADD CONSTRAINT "hr_training_detail_empl_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "hr_wage" ADD CONSTRAINT "hr_wage_employee_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "hr_wage" ADD CONSTRAINT "hr_wage_position_fkey" FOREIGN KEY ("position_id") REFERENCES "hr_position" ("position_id");

ALTER TABLE ONLY "hr_wage" ADD CONSTRAINT "hr_wage_type_fley" FOREIGN KEY ("wage_type_id") REFERENCES "wage_type" ("wage_type_id");

ALTER TABLE ONLY "import_column" ADD CONSTRAINT "import_column_filter_fkey" FOREIGN KEY ("import_filter_id") REFERENCES "import_filter" ("import_filter_id");

ALTER TABLE ONLY "import_filter" ADD CONSTRAINT "import_filter_type_fkey" FOREIGN KEY ("import_type_id") REFERENCES "import_type" ("import_type_id");

ALTER TABLE ONLY "import_type" ADD CONSTRAINT "import_type_company_fkey" FOREIGN KEY ("company_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "insurance_location_code" ADD CONSTRAINT "ins_loc_cde_ben_fkey" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "insurance_location_code" ADD CONSTRAINT "ins_loc_code_ben_fkey" FOREIGN KEY ("benefit_class_id") REFERENCES "hr_benefit_class" ("benefit_class_id");

ALTER TABLE ONLY "insurance_location_code" ADD CONSTRAINT "ins_loc_code_empstat_fkey" FOREIGN KEY ("employee_status_id") REFERENCES "hr_employee_status" ("status_id");

ALTER TABLE ONLY "interface_log" ADD CONSTRAINT "interface_log_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "invoice_line_item" ADD CONSTRAINT "inv_line_item_gl_acct_fkey" FOREIGN KEY ("expense_account_id") REFERENCES "gl_account" ("gl_account_id");

ALTER TABLE ONLY "invoice_line_item" ADD CONSTRAINT "inv_line_item_invoice_fkey" FOREIGN KEY ("invoice_id") REFERENCES "invoice" ("invoice_id");

ALTER TABLE ONLY "invoice_line_item" ADD CONSTRAINT "inv_line_item_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product_service" ("product_service_id");

ALTER TABLE ONLY "inventory" ADD CONSTRAINT "inventory_location_fkey" FOREIGN KEY ("location_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "inventory" ADD CONSTRAINT "inventory_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("product_id");

ALTER TABLE ONLY "invoice" ADD CONSTRAINT "invoice_company_fkey" FOREIGN KEY ("customer_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "invoice" ADD CONSTRAINT "invoice_gl_account_id_fkey" FOREIGN KEY ("ar_account_id") REFERENCES "gl_account" ("gl_account_id");

ALTER TABLE ONLY "invoice" ADD CONSTRAINT "invoice_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "item" ADD CONSTRAINT "item_chg_person_fkey" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "item_inspection" ADD CONSTRAINT "item_inspection_inspector_fkey" FOREIGN KEY ("person_inspecting") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "item_inspection" ADD CONSTRAINT "item_inspection_item_fkey" FOREIGN KEY ("item_id") REFERENCES "item" ("item_id");

ALTER TABLE ONLY "item_inspection" ADD CONSTRAINT "item_inspection_recorder_fkey" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "item" ADD CONSTRAINT "item_location_fkey" FOREIGN KEY ("location_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "item" ADD CONSTRAINT "item_lot_fkey" FOREIGN KEY ("lot_id") REFERENCES "lot" ("lot_id");

ALTER TABLE ONLY "item" ADD CONSTRAINT "item_parent_fkey" FOREIGN KEY ("parent_item_id") REFERENCES "item" ("item_id");

ALTER TABLE ONLY "item" ADD CONSTRAINT "item_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("product_id");

ALTER TABLE ONLY "job_type" ADD CONSTRAINT "job_type_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "life_event" ADD CONSTRAINT "life_event_bcr_fkey" FOREIGN KEY ("bcr_id") REFERENCES "hr_benefit_change_reason" ("bcr_id");

ALTER TABLE ONLY "life_event" ADD CONSTRAINT "life_event_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "life_event" ADD CONSTRAINT "life_event_recpers_fkey" FOREIGN KEY ("reporting_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "prophet_login_exception" ADD CONSTRAINT "login_exception_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "prophet_login_exception" ADD CONSTRAINT "login_exception_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "prophet_login_exception" ADD CONSTRAINT "login_exception_screen_fkey" FOREIGN KEY ("screen_group_id") REFERENCES "screen_group" ("screen_group_id");

ALTER TABLE ONLY "prophet_login_exception" ADD CONSTRAINT "login_exception_security_fkey" FOREIGN KEY ("security_group_id") REFERENCES "security_group" ("security_group_id");

ALTER TABLE ONLY "prophet_login" ADD CONSTRAINT "login_person_id_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "prophet_login" ADD CONSTRAINT "login_screen_group_fkey" FOREIGN KEY ("screen_group_id") REFERENCES "screen_group" ("screen_group_id");

ALTER TABLE ONLY "prophet_login" ADD CONSTRAINT "login_security_group_fkey" FOREIGN KEY ("security_group_id") REFERENCES "security_group" ("security_group_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "managing_employee_fkey" FOREIGN KEY ("managing_employee") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "message" ADD CONSTRAINT "messages_from_person" FOREIGN KEY ("from_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "message" ADD CONSTRAINT "messages_to_person" FOREIGN KEY ("to_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "onboarding_task_complete" ADD CONSTRAINT "ob_task_complete_pers_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "onboarding_task_complete" ADD CONSTRAINT "ob_task_complete_task_fkey" FOREIGN KEY ("onboarding_task_id") REFERENCES "onboarding_task" ("onboarding_task_id");

ALTER TABLE ONLY "onboarding_config" ADD CONSTRAINT "onboarding_config_company_id_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "onboarding_task" ADD CONSTRAINT "onboarding_task_config_fkey" FOREIGN KEY ("onboarding_config_id") REFERENCES "onboarding_config" ("onboarding_config_id");

ALTER TABLE ONLY "onboarding_task" ADD CONSTRAINT "onboarding_task_screen_fkey" FOREIGN KEY ("screen_id") REFERENCES "screen" ("screen_id");

ALTER TABLE ONLY "org_group" ADD CONSTRAINT "org_group_benclass_fkey" FOREIGN KEY ("benefit_class_id") REFERENCES "hr_benefit_class" ("benefit_class_id");

ALTER TABLE ONLY "company_base" ADD CONSTRAINT "org_group_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "org_group" ADD CONSTRAINT "org_group_pay_sch_fkey" FOREIGN KEY ("pay_schedule_id") REFERENCES "pay_schedule" ("pay_schedule_id");

ALTER TABLE ONLY "org_group" ADD CONSTRAINT "org_group_project_fkey" FOREIGN KEY ("default_project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "org_group_association" ADD CONSTRAINT "org_grp_recper_fkey" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "overtime_approval" ADD CONSTRAINT "overtime_app_emp_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "overtime_approval" ADD CONSTRAINT "overtime_app_super_fkey" FOREIGN KEY ("supervisor_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "org_group" ADD CONSTRAINT "owning_entity_fkey" FOREIGN KEY ("owning_entity_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "hr_benefit_package_join" ADD CONSTRAINT "package_id" FOREIGN KEY ("package_id") REFERENCES "hr_benefit_package" ("package_id");

ALTER TABLE ONLY "password_history" ADD CONSTRAINT "password_history_pers_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "pay_schedule_period" ADD CONSTRAINT "pay_sch_period_sched_fkey" FOREIGN KEY ("pay_schedule_id") REFERENCES "pay_schedule" ("pay_schedule_id");

ALTER TABLE ONLY "pay_schedule" ADD CONSTRAINT "pay_schedule_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "payment_info" ADD CONSTRAINT "payment_info_benefit_fkey" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "payment_info" ADD CONSTRAINT "payment_info_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_employee_join" ADD CONSTRAINT "pej_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_employee_join" ADD CONSTRAINT "pej_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "person_changed" ADD CONSTRAINT "person_changed_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "person_change_request" ADD CONSTRAINT "person_chgreq_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "person" ADD CONSTRAINT "person_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "person_cr" ADD CONSTRAINT "person_cr_approver_fkey" FOREIGN KEY ("approver_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "person_cr" ADD CONSTRAINT "person_cr_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "person_cr" ADD CONSTRAINT "person_cr_real_fkey" FOREIGN KEY ("real_record_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "person_cr" ADD CONSTRAINT "person_cr_recvhg_fkey" FOREIGN KEY ("change_record_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "person_cr" ADD CONSTRAINT "person_cr_requestor_fkey" FOREIGN KEY ("requestor_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "person" ADD CONSTRAINT "person_dflt_project_fkey" FOREIGN KEY ("default_project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "person_form" ADD CONSTRAINT "person_form_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "person_form" ADD CONSTRAINT "person_form_type_fkey" FOREIGN KEY ("form_type_id") REFERENCES "form_type" ("form_type_id");

ALTER TABLE ONLY "org_group_association" ADD CONSTRAINT "person_group_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "person_note" ADD CONSTRAINT "person_note_cat_fkey" FOREIGN KEY ("cat_id") REFERENCES "hr_note_category" ("cat_id");

ALTER TABLE ONLY "person_note" ADD CONSTRAINT "person_note_emp_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "person" ADD CONSTRAINT "person_recper_fkey" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "personal_reference" ADD CONSTRAINT "personal_ref_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "phone_cr" ADD CONSTRAINT "phone_cr_approver_fkey" FOREIGN KEY ("approver_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "phone_cr" ADD CONSTRAINT "phone_cr_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "phone_cr" ADD CONSTRAINT "phone_cr_real_fkey" FOREIGN KEY ("real_record_id") REFERENCES "phone" ("phone_id");

ALTER TABLE ONLY "phone_cr" ADD CONSTRAINT "phone_cr_recvhg_fkey" FOREIGN KEY ("change_record_id") REFERENCES "phone" ("phone_id");

ALTER TABLE ONLY "phone_cr" ADD CONSTRAINT "phone_cr_requestor_fkey" FOREIGN KEY ("requestor_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "phone" ADD CONSTRAINT "phone_org_group_fkey" FOREIGN KEY ("org_group_join") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "phone" ADD CONSTRAINT "phone_person_fkey" FOREIGN KEY ("person_join") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "physician" ADD CONSTRAINT "physicians_benefit_fkey" FOREIGN KEY ("benefit_join_id") REFERENCES "hr_benefit_join" ("benefit_join_id");

ALTER TABLE ONLY "project_view_join" ADD CONSTRAINT "ppv_child_fkey" FOREIGN KEY ("child_project_view_id") REFERENCES "project_view" ("project_view_id");

ALTER TABLE ONLY "project_view_join" ADD CONSTRAINT "ppv_parent_fkey" FOREIGN KEY ("parent_project_view_id") REFERENCES "project_view" ("project_view_id");

ALTER TABLE ONLY "previous_employment" ADD CONSTRAINT "prev_emp_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "process_history" ADD CONSTRAINT "process_history_sched_fkey" FOREIGN KEY ("process_schedule_id") REFERENCES "process_schedule" ("process_schedule_id");

ALTER TABLE ONLY "product_service" ADD CONSTRAINT "prod_serv_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "product_type" ADD CONSTRAINT "prod_typ_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "product_attribute_choice" ADD CONSTRAINT "product_att_choice_prod_fkey" FOREIGN KEY ("product_attribute_id") REFERENCES "product_attribute" ("product_attribute_id");

ALTER TABLE ONLY "product_attribute_dependency" ADD CONSTRAINT "product_attr_dep_ch_fkey" FOREIGN KEY ("dependent_attribute_id") REFERENCES "product_attribute" ("product_attribute_id");

ALTER TABLE ONLY "product_attribute_dependency" ADD CONSTRAINT "product_attr_dep_prod_fkey" FOREIGN KEY ("product_attribute_id") REFERENCES "product_attribute" ("product_attribute_id");

ALTER TABLE ONLY "product_attribute" ADD CONSTRAINT "product_attribute_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "product" ADD CONSTRAINT "product_product_service_fkey" FOREIGN KEY ("product_id") REFERENCES "product_service" ("product_service_id");

ALTER TABLE ONLY "product" ADD CONSTRAINT "product_product_type_fkey" FOREIGN KEY ("product_type_id") REFERENCES "product_type" ("product_type_id");

ALTER TABLE ONLY "product_service" ADD CONSTRAINT "product_service_exp_acct_fkey" FOREIGN KEY ("expense_account_id") REFERENCES "gl_account" ("gl_account_id");

ALTER TABLE ONLY "product_type" ADD CONSTRAINT "product_type_self_fkey" FOREIGN KEY ("parent_product_type_id") REFERENCES "product_type" ("product_type_id");

ALTER TABLE ONLY "product" ADD CONSTRAINT "product_vendor_fkey" FOREIGN KEY ("vendor_id") REFERENCES "vendor" ("org_group_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_app_per_fkey" FOREIGN KEY ("approving_person") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_category" ADD CONSTRAINT "project_catagory_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_category_fkey" FOREIGN KEY ("project_category_id") REFERENCES "project_category" ("project_category_id");

ALTER TABLE ONLY "project_checklist_detail" ADD CONSTRAINT "project_chklst_chklst_fkey" FOREIGN KEY ("route_stop_checklist_id") REFERENCES "route_stop_checklist" ("route_stop_checklist_id");

ALTER TABLE ONLY "project_checklist_detail" ADD CONSTRAINT "project_chklst_person_id_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_checklist_detail" ADD CONSTRAINT "project_chklst_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "project_comment" ADD CONSTRAINT "project_comment_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_comment" ADD CONSTRAINT "project_comment_project_id_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_current_route_stop_fkey" FOREIGN KEY ("current_route_stop") REFERENCES "route_stop" ("route_stop_id");

ALTER TABLE ONLY "project_form" ADD CONSTRAINT "project_form_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "project_form" ADD CONSTRAINT "project_form_type_fkey" FOREIGN KEY ("form_type_id") REFERENCES "form_type" ("form_type_id");

ALTER TABLE ONLY "project_history_person" ADD CONSTRAINT "project_hist_pers_hist_fkey" FOREIGN KEY ("project_history_id") REFERENCES "project_history" ("project_history_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_owning_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "project_phase" ADD CONSTRAINT "project_phase_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product_service" ("product_service_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_proj_type_fkey" FOREIGN KEY ("project_type_id") REFERENCES "project_type" ("project_type_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_req_orggrp_fkey" FOREIGN KEY ("requesting_org_group") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_route_fkey" FOREIGN KEY ("route_id") REFERENCES "route" ("route_id");

ALTER TABLE ONLY "project_status" ADD CONSTRAINT "project_status_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_status_fkey" FOREIGN KEY ("project_status_id") REFERENCES "project_status" ("project_status_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "project_subject_person_fkey" FOREIGN KEY ("subject_person") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_template_benefit_a" ADD CONSTRAINT "project_tembena_pers_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_template_benefit" ADD CONSTRAINT "project_tempben_bcr_fkey" FOREIGN KEY ("bcr_id") REFERENCES "hr_benefit_change_reason" ("bcr_id");

ALTER TABLE ONLY "project_template_benefit" ADD CONSTRAINT "project_tempben_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "project_template_benefit" ADD CONSTRAINT "project_tempben_pcat_fkey" FOREIGN KEY ("project_category_id") REFERENCES "project_category" ("project_category_id");

ALTER TABLE ONLY "project_template_benefit" ADD CONSTRAINT "project_tempben_pstat_fkey" FOREIGN KEY ("project_status_id") REFERENCES "project_status" ("project_status_id");

ALTER TABLE ONLY "project_template_benefit" ADD CONSTRAINT "project_tempben_ptyp_fkey" FOREIGN KEY ("project_type_id") REFERENCES "project_type" ("project_type_id");

ALTER TABLE ONLY "project_template_benefit" ADD CONSTRAINT "project_tempbene_ben_fkey" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "project_template_benefit" ADD CONSTRAINT "project_tempbene_stat_fkey" FOREIGN KEY ("status_id") REFERENCES "hr_employee_status" ("status_id");

ALTER TABLE ONLY "project_template_benefit_a" ADD CONSTRAINT "project_tmpbena_temp_fkey" FOREIGN KEY ("project_template_id") REFERENCES "project_template_benefit" ("project_template_id");

ALTER TABLE ONLY "project_type" ADD CONSTRAINT "project_type_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "project_status_rs_join" ADD CONSTRAINT "projstat_rsj_ps_fkey" FOREIGN KEY ("project_status_id") REFERENCES "project_status" ("project_status_id");

ALTER TABLE ONLY "project_status_rs_join" ADD CONSTRAINT "projstat_rsj_rs_kfey" FOREIGN KEY ("route_stop_id") REFERENCES "route_stop" ("route_stop_id");

ALTER TABLE ONLY "prophet_login" ADD CONSTRAINT "prophet_login_nc_scrngrp_fkey" FOREIGN KEY ("no_company_screen_group_id") REFERENCES "screen_group" ("screen_group_id");

ALTER TABLE ONLY "appointment_person_join" ADD CONSTRAINT "prospect_appt_appt_id_fkey" FOREIGN KEY ("appointment_id") REFERENCES "appointment" ("appointment_id");

ALTER TABLE ONLY "appointment" ADD CONSTRAINT "prospect_appt_org_grp_fkey" FOREIGN KEY ("org_group_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "appointment_person_join" ADD CONSTRAINT "prospect_appt_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "company_question_detail" ADD CONSTRAINT "prospect_comp_ques_fkey" FOREIGN KEY ("company_ques_id") REFERENCES "company_question" ("company_ques_id");

ALTER TABLE ONLY "prospect" ADD CONSTRAINT "prospect_company_base_fkey" FOREIGN KEY ("org_group_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "prospect" ADD CONSTRAINT "prospect_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "contact_question_detail" ADD CONSTRAINT "prospect_cont_ques_fkey" FOREIGN KEY ("contact_question_id") REFERENCES "contact_question" ("contact_question_id");

ALTER TABLE ONLY "prospect_contact" ADD CONSTRAINT "prospect_contact_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "prospect_h" ADD CONSTRAINT "prospect_h_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "prospect_h" ADD CONSTRAINT "prospect_h_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "prospect_h" ADD CONSTRAINT "prospect_h_prospect_type_fkey" FOREIGN KEY ("prospect_type_id") REFERENCES "prospect_type" ("prospect_type_id");

ALTER TABLE ONLY "prospect_h" ADD CONSTRAINT "prospect_h_recpers_fkey" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "prospect_log" ADD CONSTRAINT "prospect_log_company_fkey" FOREIGN KEY ("org_group_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "prospect_log" ADD CONSTRAINT "prospect_log_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "prospect_log" ADD CONSTRAINT "prospect_log_result_fkey" FOREIGN KEY ("sales_activity_result_id") REFERENCES "sales_activity_result" ("sales_activity_result_id");

ALTER TABLE ONLY "prospect_log" ADD CONSTRAINT "prospect_log_salesact_fkey" FOREIGN KEY ("sales_activity_id") REFERENCES "sales_activity" ("sales_activity_id");

ALTER TABLE ONLY "prospect" ADD CONSTRAINT "prospect_person_id_fkey" FOREIGN KEY ("person_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "prospect" ADD CONSTRAINT "prospect_prospect_type_fkey" FOREIGN KEY ("prospect_type_id") REFERENCES "prospect_type" ("prospect_type_id");

ALTER TABLE ONLY "prospect" ADD CONSTRAINT "prospect_recchgper_fkey" FOREIGN KEY ("record_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "prospect_source" ADD CONSTRAINT "prospect_source_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "prospect" ADD CONSTRAINT "prospect_source_fkey" FOREIGN KEY ("prospect_source_id") REFERENCES "prospect_source" ("prospect_source_id");

ALTER TABLE ONLY "prospect" ADD CONSTRAINT "prospect_status_chk" FOREIGN KEY ("prospect_status_id") REFERENCES "prospect_status" ("prospect_status_id");

ALTER TABLE ONLY "prospect_status" ADD CONSTRAINT "prospect_status_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "prospect_type" ADD CONSTRAINT "prospect_type_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "project_view" ADD CONSTRAINT "pview_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_view" ADD CONSTRAINT "pview_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "quickbooks_sync" ADD CONSTRAINT "qb_org_group_fkey" FOREIGN KEY ("org_group_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "quote_table" ADD CONSTRAINT "quote_accept_peron_fkey" FOREIGN KEY ("accepted_person") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "quote_adjustment" ADD CONSTRAINT "quote_adj_quote_fkey" FOREIGN KEY ("quote_id") REFERENCES "quote_table" ("quote_id");

ALTER TABLE ONLY "quote_table" ADD CONSTRAINT "quote_client_fkey" FOREIGN KEY ("client_id") REFERENCES "client" ("org_group_id");

ALTER TABLE ONLY "quote_table" ADD CONSTRAINT "quote_created_by_fkey" FOREIGN KEY ("created_by_person") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "quote_table" ADD CONSTRAINT "quote_final_by" FOREIGN KEY ("finalized_by_person") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "quote_table" ADD CONSTRAINT "quote_location_fkey" FOREIGN KEY ("location_cost_id") REFERENCES "location_cost" ("location_cost_id");

ALTER TABLE ONLY "quote_product" ADD CONSTRAINT "quote_prod_prod_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("product_id");

ALTER TABLE ONLY "quote_product" ADD CONSTRAINT "quote_prod_quote_fkey" FOREIGN KEY ("quote_id") REFERENCES "quote_table" ("quote_id");

ALTER TABLE ONLY "quote_template_product" ADD CONSTRAINT "quote_tempprod_prod_fkey" FOREIGN KEY ("product_id") REFERENCES "product" ("product_id");

ALTER TABLE ONLY "quote_template_product" ADD CONSTRAINT "quote_temprod_quote_fkey" FOREIGN KEY ("quote_template_id") REFERENCES "quote_template" ("quote_template_id");

ALTER TABLE ONLY "receipt" ADD CONSTRAINT "receipt_bank_draft_fkey" FOREIGN KEY ("bank_draft_history_id") REFERENCES "bank_draft_history" ("bank_draft_history_id");

ALTER TABLE ONLY "receipt_join" ADD CONSTRAINT "receipt_join_invoice_fkey" FOREIGN KEY ("invoice_id") REFERENCES "invoice" ("invoice_id");

ALTER TABLE ONLY "receipt_join" ADD CONSTRAINT "receipt_join_receipt_fkey" FOREIGN KEY ("receipt_id") REFERENCES "receipt" ("receipt_id");

ALTER TABLE ONLY "report_column" ADD CONSTRAINT "report_column_report_fkey" FOREIGN KEY ("report_id") REFERENCES "report" ("report_id");

ALTER TABLE ONLY "report" ADD CONSTRAINT "report_company_fkey" FOREIGN KEY ("company_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "report_graphic" ADD CONSTRAINT "report_graphic_report_fkey" FOREIGN KEY ("report_id") REFERENCES "report" ("report_id");

ALTER TABLE ONLY "report_selection" ADD CONSTRAINT "report_selection_report_fkey" FOREIGN KEY ("report_id") REFERENCES "report" ("report_id");

ALTER TABLE ONLY "report_title" ADD CONSTRAINT "report_title_report_fkey" FOREIGN KEY ("report_id") REFERENCES "report" ("report_id");

ALTER TABLE ONLY "rights_association" ADD CONSTRAINT "rights_association_right_id_fkey" FOREIGN KEY ("right_id") REFERENCES "security_token" ("right_id");

ALTER TABLE ONLY "rights_association" ADD CONSTRAINT "rights_association_security_group_id_fkey" FOREIGN KEY ("security_group_id") REFERENCES "security_group" ("security_group_id");

ALTER TABLE ONLY "route_type_assoc" ADD CONSTRAINT "route_assoc_cat_fkey" FOREIGN KEY ("project_category_id") REFERENCES "project_category" ("project_category_id");

ALTER TABLE ONLY "route_type_assoc" ADD CONSTRAINT "route_assoc_route_fkey" FOREIGN KEY ("route_id") REFERENCES "route" ("route_id");

ALTER TABLE ONLY "route_type_assoc" ADD CONSTRAINT "route_assoc_type_fkey" FOREIGN KEY ("project_type_id") REFERENCES "project_type" ("project_type_id");

ALTER TABLE ONLY "route" ADD CONSTRAINT "route_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "project_history" ADD CONSTRAINT "route_hist_from_status_fkey" FOREIGN KEY ("from_status_id") REFERENCES "project_status" ("project_status_id");

ALTER TABLE ONLY "project_history" ADD CONSTRAINT "route_hist_from_stop_fkey" FOREIGN KEY ("from_stop_id") REFERENCES "route_stop" ("route_stop_id");

ALTER TABLE ONLY "project_history" ADD CONSTRAINT "route_hist_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project_history" ADD CONSTRAINT "route_hist_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "project_history" ADD CONSTRAINT "route_hist_to_status_fkey" FOREIGN KEY ("to_status_id") REFERENCES "project_status" ("project_status_id");

ALTER TABLE ONLY "project_history" ADD CONSTRAINT "route_hist_to_stop_kkey" FOREIGN KEY ("to_stop_id") REFERENCES "route_stop" ("route_stop_id");

ALTER TABLE ONLY "route_path" ADD CONSTRAINT "route_path_from_status_fkey" FOREIGN KEY ("from_status_id") REFERENCES "project_status" ("project_status_id");

ALTER TABLE ONLY "route_path" ADD CONSTRAINT "route_path_from_stop_fkey" FOREIGN KEY ("from_stop_id") REFERENCES "route_stop" ("route_stop_id");

ALTER TABLE ONLY "route_path" ADD CONSTRAINT "route_path_to_status_fkey" FOREIGN KEY ("to_status_id") REFERENCES "project_status" ("project_status_id");

ALTER TABLE ONLY "route_path" ADD CONSTRAINT "route_path_to_stop_fkey" FOREIGN KEY ("to_stop_id") REFERENCES "route_stop" ("route_stop_id");

ALTER TABLE ONLY "route" ADD CONSTRAINT "route_project_status_fkey" FOREIGN KEY ("project_status_id") REFERENCES "project_status" ("project_status_id");

ALTER TABLE ONLY "route" ADD CONSTRAINT "route_route_stop_fkey" FOREIGN KEY ("route_stop_id") REFERENCES "route_stop" ("route_stop_id");

ALTER TABLE ONLY "route_stop_checklist" ADD CONSTRAINT "route_stop_cl_rsid_fkey" FOREIGN KEY ("route_stop_id") REFERENCES "route_stop" ("route_stop_id");

ALTER TABLE ONLY "route_stop" ADD CONSTRAINT "route_stop_org_group_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "route_stop" ADD CONSTRAINT "route_stop_phase_fkey" FOREIGN KEY ("project_phase_id") REFERENCES "project_phase" ("project_phase_id");

ALTER TABLE ONLY "route_stop" ADD CONSTRAINT "route_stop_route_fkey" FOREIGN KEY ("route_id") REFERENCES "route" ("route_id");

ALTER TABLE ONLY "sales_activity" ADD CONSTRAINT "sales_activity_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "sales_activity_result" ADD CONSTRAINT "sales_actres_act_fkey" FOREIGN KEY ("sales_activity_id") REFERENCES "sales_activity" ("sales_activity_id");

ALTER TABLE ONLY "sales_points" ADD CONSTRAINT "sales_points_emp_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "sales_points" ADD CONSTRAINT "sales_points_prospect_fkey" FOREIGN KEY ("prospect_id") REFERENCES "prospect" ("org_group_id");

ALTER TABLE ONLY "sales_points" ADD CONSTRAINT "sales_points_status_fkey" FOREIGN KEY ("prospect_status_id") REFERENCES "prospect_status" ("prospect_status_id");

ALTER TABLE ONLY "screen_group_access" ADD CONSTRAINT "screen_group_access_fk1" FOREIGN KEY ("security_group_id") REFERENCES "security_group" ("security_group_id");

ALTER TABLE ONLY "screen_group_access" ADD CONSTRAINT "screen_group_access_fk2" FOREIGN KEY ("can_access_screen_group_id") REFERENCES "screen_group" ("screen_group_id");

ALTER TABLE ONLY "screen_group_hierarchy" ADD CONSTRAINT "screen_group_hier_screen_fkey" FOREIGN KEY ("screen_id") REFERENCES "screen" ("screen_id");

ALTER TABLE ONLY "screen_group_hierarchy" ADD CONSTRAINT "screen_group_hierarchy_child_screen_group_id_fkey" FOREIGN KEY ("child_screen_group_id") REFERENCES "screen_group" ("screen_group_id");

ALTER TABLE ONLY "screen_group_hierarchy" ADD CONSTRAINT "screen_group_hierarchy_parent_screen_group_id_fkey" FOREIGN KEY ("parent_screen_group_id") REFERENCES "screen_group" ("screen_group_id");

ALTER TABLE ONLY "screen_group" ADD CONSTRAINT "screen_group_screen_id_fkey" FOREIGN KEY ("screen_id") REFERENCES "screen" ("screen_id");

ALTER TABLE ONLY "security_group_access" ADD CONSTRAINT "security_group_acc_fk1" FOREIGN KEY ("security_group_id") REFERENCES "security_group" ("security_group_id");

ALTER TABLE ONLY "security_group_access" ADD CONSTRAINT "security_group_acc_fk2" FOREIGN KEY ("can_access_security_group_id") REFERENCES "security_group" ("security_group_id");

ALTER TABLE ONLY "security_group_hierarchy" ADD CONSTRAINT "security_group_hierarchy_child_security_group_id_fkey" FOREIGN KEY ("child_security_group_id") REFERENCES "security_group" ("security_group_id");

ALTER TABLE ONLY "security_group_hierarchy" ADD CONSTRAINT "security_group_hierarchy_parent_security_group_id_fkey" FOREIGN KEY ("parent_security_group_id") REFERENCES "security_group" ("security_group_id");

ALTER TABLE ONLY "service" ADD CONSTRAINT "service_product_service_fkey" FOREIGN KEY ("service_id") REFERENCES "product_service" ("product_service_id");

ALTER TABLE ONLY "service_subscribed" ADD CONSTRAINT "service_sub_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "service_subscribed_join" ADD CONSTRAINT "service_subjoin_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "service_subscribed_join" ADD CONSTRAINT "service_subjoin_service_fkey" FOREIGN KEY ("service_id") REFERENCES "service_subscribed" ("service_id");

ALTER TABLE ONLY "standard_project" ADD CONSTRAINT "sp_bill_at_org_group_fkey" FOREIGN KEY ("bill_at_org_group") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "standard_project" ADD CONSTRAINT "sp_managing_emp_fkey" FOREIGN KEY ("managing_employee") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "standard_project" ADD CONSTRAINT "sp_sponsor_fkey" FOREIGN KEY ("project_sponsor_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "project" ADD CONSTRAINT "sponsor_fkey" FOREIGN KEY ("project_sponsor_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "spousal_insurance_verif" ADD CONSTRAINT "spousal_verif_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "standard_project" ADD CONSTRAINT "standard_project_comp_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "standard_project" ADD CONSTRAINT "std_project_category_fkey" FOREIGN KEY ("project_category_id") REFERENCES "project_category" ("project_category_id");

ALTER TABLE ONLY "standard_project" ADD CONSTRAINT "std_project_product_fkey" FOREIGN KEY ("product_id") REFERENCES "product_service" ("product_service_id");

ALTER TABLE ONLY "standard_project" ADD CONSTRAINT "std_project_proj_type_fkey" FOREIGN KEY ("project_type_id") REFERENCES "project_type" ("project_type_id");

ALTER TABLE ONLY "student_verification" ADD CONSTRAINT "student_verif_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "time_off_request" ADD CONSTRAINT "time_off_req_app_person" FOREIGN KEY ("approving_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "time_off_request" ADD CONSTRAINT "time_off_req_proj_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "time_off_request" ADD CONSTRAINT "time_off_request_person_fkey" FOREIGN KEY ("requesting_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "time_reject" ADD CONSTRAINT "time_reject_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "time_off_accrual_calc" ADD CONSTRAINT "timeoff_acc_calc_config_fkey" FOREIGN KEY ("benefit_config_id") REFERENCES "hr_benefit_config" ("benefit_config_id");

ALTER TABLE ONLY "time_off_accrual_seniority" ADD CONSTRAINT "timeoff_accsen_calc_fkey" FOREIGN KEY ("time_off_accrual_calc_id") REFERENCES "time_off_accrual_calc" ("time_off_accrual_calc_id");

ALTER TABLE ONLY "timesheet" ADD CONSTRAINT "timesheet_beg_person_id" FOREIGN KEY ("beginning_entry_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "timesheet" ADD CONSTRAINT "timesheet_end_person_id" FOREIGN KEY ("end_entry_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "timesheet" ADD CONSTRAINT "timesheet_invoice_fkey" FOREIGN KEY ("invoice_line_item_id") REFERENCES "invoice_line_item" ("invoice_line_item_id");

ALTER TABLE ONLY "timesheet" ADD CONSTRAINT "timesheet_message_fkey" FOREIGN KEY ("message_id") REFERENCES "message" ("message_id");

ALTER TABLE ONLY "timesheet" ADD CONSTRAINT "timesheet_person_id_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "timesheet" ADD CONSTRAINT "timesheet_project_id_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "timesheet" ADD CONSTRAINT "timesheet_wagetype_fkey" FOREIGN KEY ("wage_type_id") REFERENCES "wage_type" ("wage_type_id");

ALTER TABLE ONLY "user_attribute" ADD CONSTRAINT "user_attribute_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "vendor" ADD CONSTRAINT "vandor_dflt_ap_acct" FOREIGN KEY ("dflt_ap_acct") REFERENCES "gl_account" ("gl_account_id");

ALTER TABLE ONLY "vendor" ADD CONSTRAINT "vandor_dflt_expense_acct" FOREIGN KEY ("dflt_expense_acct") REFERENCES "gl_account" ("gl_account_id");

ALTER TABLE ONLY "vendor" ADD CONSTRAINT "vendor_company_company_fkey" FOREIGN KEY ("org_group_id") REFERENCES "company_base" ("org_group_id");

ALTER TABLE ONLY "vendor" ADD CONSTRAINT "vendor_company_fkey" FOREIGN KEY ("company_id") REFERENCES "company_detail" ("org_group_id");

ALTER TABLE ONLY "vendor_group" ADD CONSTRAINT "vendor_group_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "vendor_group" ADD CONSTRAINT "vendor_group_vendor_fkey" FOREIGN KEY ("vendor_id") REFERENCES "vendor" ("org_group_id");

ALTER TABLE ONLY "vendor_contact" ADD CONSTRAINT "vendor_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "wage_paid_detail" ADD CONSTRAINT "wage_detail_paid_fkey" FOREIGN KEY ("wage_paid_id") REFERENCES "wage_paid" ("wage_paid_id");

ALTER TABLE ONLY "wage_paid_detail" ADD CONSTRAINT "wage_paid_det_type_fkey" FOREIGN KEY ("wage_type_id") REFERENCES "wage_type" ("wage_type_id");

ALTER TABLE ONLY "wage_paid" ADD CONSTRAINT "wage_paid_employee_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "wage_type" ADD CONSTRAINT "wage_type_gl_fkey" FOREIGN KEY ("expense_account") REFERENCES "gl_account" ("gl_account_id");

ALTER TABLE ONLY "wage_type" ADD CONSTRAINT "wage_type_liab_fkey" FOREIGN KEY ("liability_account") REFERENCES "gl_account" ("gl_account_id");

ALTER TABLE ONLY "wage_type" ADD CONSTRAINT "wage_type_org_fkey" FOREIGN KEY ("org_group_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "wizard_config_benefit" ADD CONSTRAINT "wizard_ben_ben_fkey" FOREIGN KEY ("benefit_id") REFERENCES "hr_benefit" ("benefit_id");

ALTER TABLE ONLY "wizard_config_benefit" ADD CONSTRAINT "wizard_ben_screen_fkey" FOREIGN KEY ("screen_id") REFERENCES "screen" ("screen_id");

ALTER TABLE ONLY "wizard_config_benefit" ADD CONSTRAINT "wizard_ben_wcat_fkey" FOREIGN KEY ("wizard_config_cat_id") REFERENCES "wizard_config_category" ("wizard_config_cat_id");

ALTER TABLE ONLY "wizard_config_category" ADD CONSTRAINT "wizard_cat_config_fkey" FOREIGN KEY ("wizard_configuration_id") REFERENCES "wizard_configuration" ("wizard_configuration_id");

ALTER TABLE ONLY "wizard_config_category" ADD CONSTRAINT "wizard_cat_fkey" FOREIGN KEY ("benefit_cat_id") REFERENCES "hr_benefit_category" ("benefit_cat_id");

ALTER TABLE ONLY "wizard_config_category" ADD CONSTRAINT "wizard_cat_screen_fkey" FOREIGN KEY ("screen_id") REFERENCES "screen" ("screen_id");

ALTER TABLE ONLY "wizard_configuration" ADD CONSTRAINT "wizard_conf_benclass_fkey" FOREIGN KEY ("benefit_class_id") REFERENCES "hr_benefit_class" ("benefit_class_id");

ALTER TABLE ONLY "wizard_configuration" ADD CONSTRAINT "wizard_conf_company_fkey" FOREIGN KEY ("company_id") REFERENCES "org_group" ("org_group_id");

ALTER TABLE ONLY "wizard_config_config" ADD CONSTRAINT "wizard_conf_conf_fkey" FOREIGN KEY ("benefit_config_id") REFERENCES "hr_benefit_config" ("benefit_config_id");

ALTER TABLE ONLY "wizard_configuration" ADD CONSTRAINT "wizard_conf_projcat_fkey" FOREIGN KEY ("project_category_id") REFERENCES "project_category" ("project_category_id");

ALTER TABLE ONLY "wizard_configuration" ADD CONSTRAINT "wizard_conf_projstat_fkey" FOREIGN KEY ("project_status_id") REFERENCES "project_status" ("project_status_id");

ALTER TABLE ONLY "wizard_configuration" ADD CONSTRAINT "wizard_conf_projtype_fkey" FOREIGN KEY ("project_type_id") REFERENCES "project_type" ("project_type_id");

ALTER TABLE ONLY "wizard_config_config" ADD CONSTRAINT "wizard_conf_wben_fkey" FOREIGN KEY ("wizard_config_ben_id") REFERENCES "wizard_config_benefit" ("wizard_config_ben_id");

ALTER TABLE ONLY "wizard_configuration" ADD CONSTRAINT "wizard_config_contact_fkey" FOREIGN KEY ("hr_contact_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "wizard_configuration" ADD CONSTRAINT "wizard_config_demoscn_fkey" FOREIGN KEY ("demographic_screen_id") REFERENCES "screen" ("screen_id");

ALTER TABLE ONLY "wizard_config_project_a" ADD CONSTRAINT "wizard_confproja_pers_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "wizard_config_project_a" ADD CONSTRAINT "wizard_confproja_wiz_fkey" FOREIGN KEY ("wizard_configuration_id") REFERENCES "wizard_configuration" ("wizard_configuration_id");

ALTER TABLE ONLY "wizard_project" ADD CONSTRAINT "wizard_project_benjoin_fkey" FOREIGN KEY ("benefit_join_id") REFERENCES "hr_benefit_join" ("benefit_join_id");

ALTER TABLE ONLY "wizard_project" ADD CONSTRAINT "wizard_project_hist_fkey" FOREIGN KEY ("hr_benefit_join_h_id") REFERENCES "hr_benefit_join_h" ("history_id");

ALTER TABLE ONLY "wizard_project" ADD CONSTRAINT "wizard_project_pers_comp_fkey" FOREIGN KEY ("person_completed") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "wizard_project" ADD CONSTRAINT "wizard_project_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

