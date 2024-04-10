
-- PostgreSQL patch from revision 2531 to revision 2533


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "person" ADD COLUMN "i9_part1" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "person" ADD COLUMN "i9_part2" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "person_h" ADD COLUMN "i9_part1" character(1) DEFAULT 'N' NOT NULL;

ALTER TABLE "person_h" ADD COLUMN "i9_part2" character(1) DEFAULT 'N' NOT NULL;


--  Change existing columns


-- Assume all existing people have their I9 parts 1 & 2 complete
update person set i9_part1='Y';
update person set i9_part2='Y';
update person_h set i9_part1=i9_completed;
update person_h set i9_part2=i9_completed;



--  Remove tables


--  Drop columns

ALTER TABLE "person" DROP CONSTRAINT "person_i9_completed_chk";

ALTER TABLE "person" DROP COLUMN "i9_completed";

ALTER TABLE "person_h" DROP CONSTRAINT "personh_i9_completed_chk";

ALTER TABLE "person_h" DROP COLUMN "i9_completed";


--  Add new indexes and checks

ALTER TABLE "person" ADD CONSTRAINT "person_i91_chk" CHECK (((i9_part1='Y')OR(i9_part1='N')));

ALTER TABLE "person" ADD CONSTRAINT "person_i92_chk" CHECK (((i9_part2='Y')OR(i9_part2='N')));

ALTER TABLE "person_h" ADD CONSTRAINT "person_h_i91_chk" CHECK (((i9_part1='Y')OR(i9_part1='N')));

ALTER TABLE "person_h" ADD CONSTRAINT "person_h_i92_chk" CHECK (((i9_part2='Y')OR(i9_part2='N')));

