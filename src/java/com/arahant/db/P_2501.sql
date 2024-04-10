
-- PostgreSQL patch from revision 2498 to revision 2501

--  Remove indexes and checks


--  Add new tables

CREATE TABLE "per_diem_exception" (
	"per_diem_exception_id" character(16) NOT NULL,
	"exception_type" character(1) NOT NULL,
	"exception_amount" real DEFAULT 0 NOT NULL,
	"person_id" character(16) NOT NULL,
	"project_id" character(16) NOT NULL,
	"notes" character varying(256),
	CONSTRAINT "per_diem_exception_type_chk" CHECK (((exception_type='A')OR(exception_type='D')OR(exception_type='S')OR(exception_type='R')))
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE INDEX "fki_per_diem_exception_proj_fkey" ON "per_diem_exception" USING btree ("project_id");

CREATE INDEX "per_diem_exception_person_idx" ON "per_diem_exception" USING btree ("person_id");

ALTER TABLE ONLY "per_diem_exception" ADD CONSTRAINT "per_diem_exception_pkey" PRIMARY KEY ("per_diem_exception_id");

COMMENT ON COLUMN "per_diem_exception"."exception_type" IS '(A)dd / (D)elete / (S)ubtract / (R)place';

ALTER TABLE ONLY "per_diem_exception" ADD CONSTRAINT "per_diem_exception_person_fkey" FOREIGN KEY ("person_id") REFERENCES "employee" ("person_id");

ALTER TABLE ONLY "per_diem_exception" ADD CONSTRAINT "per_diem_exception_proj_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

