
-- PostgreSQL patch from revision 1034 to revision 1035
--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "rate_type" ADD COLUMN "rate_type" character(1);
UPDATE rate_type set rate_type = 'E';
alter table rate_type alter column rate_type set not null;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "rate_type" ADD CONSTRAINT "rate_type_rate_type_chk" CHECK ((((rate_type='P')OR(rate_type='E'))OR(rate_type='B')));

COMMENT ON COLUMN "rate_type"."rate_type" IS 'P = Project
E = Person
B = Both';



