
-- PostgreSQL patch from revision 1219 to revision 1220


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "expense_account" (
	"expense_account_id" character(16) NOT NULL,
	"expense_id" character varying(10),
	"description" character varying(30) NOT NULL,
	"gl_account_id" character(16) NOT NULL
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "expense_account" ADD CONSTRAINT "expense_account_pkey" PRIMARY KEY ("expense_account_id");

ALTER TABLE ONLY "expense_account" ADD CONSTRAINT "expense_account_gl_fkey" FOREIGN KEY ("gl_account_id") REFERENCES "gl_account" ("gl_account_id");


