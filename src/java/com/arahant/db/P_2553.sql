
-- PostgreSQL patch from revision 2552 to revision 2553



--  Remove indexes and checks


--  Add new tables

CREATE TABLE "employee_label" (
	"employee_label_id" character(16) NOT NULL,
	"name" character varying(12) NOT NULL,
	"description" character varying(80),
	"auto_add_new_employee" character(1) DEFAULT 'N' NOT NULL,
	CONSTRAINT "employee_label_new_emp_chk" CHECK (((auto_add_new_employee='Y')OR(auto_add_new_employee='N')))
);

CREATE TABLE "employee_label_association" (
	"employee_id" character(16) NOT NULL,
	"employee_label_id" character(16) NOT NULL,
	"who_added" character(16) NOT NULL,
	"when_added" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"completed" character(1) DEFAULT 'N' NOT NULL,
	"when_completed" timestamp with time zone,
	"who_completed" character(16),
	"notes" character varying(1024),
	CONSTRAINT "employee_lbl_ass_completed_chk" CHECK (((completed='Y')OR(completed='N')))
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "employee_label" ADD CONSTRAINT "employee_label_pkey" PRIMARY KEY ("employee_label_id");

CREATE INDEX "employee_lbl_ass_lbl_idx" ON "employee_label_association" USING btree ("completed", "employee_label_id");

ALTER TABLE ONLY "employee_label_association" ADD CONSTRAINT "employee_lbl_ass_pkey" PRIMARY KEY ("employee_id", "employee_label_id");

CREATE INDEX "fki_employee_lbl_ass_lbl_fky" ON "employee_label_association" USING btree ("employee_label_id");

CREATE INDEX "fki_employee_lbl_ass_who_added_fkey" ON "employee_label_association" USING btree ("who_added");

CREATE INDEX "fki_employee_lbl_ass_who_comp_fkey" ON "employee_label_association" USING btree ("who_completed");


ALTER TABLE ONLY "employee_label_association" ADD CONSTRAINT "employee_lbl_ass_emp_fkey" FOREIGN KEY ("employee_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "employee_label_association" ADD CONSTRAINT "employee_lbl_ass_lbl_fky" FOREIGN KEY ("employee_label_id") REFERENCES "employee_label" ("employee_label_id");

ALTER TABLE ONLY "employee_label_association" ADD CONSTRAINT "employee_lbl_ass_who_added_fkey" FOREIGN KEY ("who_added") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "employee_label_association" ADD CONSTRAINT "employee_lbl_ass_who_comp_fkey" FOREIGN KEY ("who_completed") REFERENCES "person" ("person_id");


