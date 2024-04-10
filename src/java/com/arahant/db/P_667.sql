ALTER TABLE "person" ALTER COLUMN "replace_employer_plan" SET DEFAULT ' ';

ALTER TABLE "person" ALTER COLUMN "not_missed_five_days" SET DEFAULT ' ';

ALTER TABLE "person" ALTER COLUMN "drug_alcohol_abuse" SET DEFAULT ' ';

ALTER TABLE "person" ALTER COLUMN "two_family_heart_cond" SET DEFAULT ' ';

ALTER TABLE "person" ALTER COLUMN "two_family_cancer" SET DEFAULT ' ';

ALTER TABLE "person" ALTER COLUMN "two_family_diabetes" SET DEFAULT ' ';

ALTER TABLE "person_h" ALTER COLUMN "replace_employer_plan" SET DEFAULT ' ';

ALTER TABLE "person_h" ALTER COLUMN "not_missed_five_days" SET DEFAULT ' ';

ALTER TABLE "person_h" ALTER COLUMN "drug_alcohol_abuse" SET DEFAULT ' ';

ALTER TABLE "person_h" ALTER COLUMN "two_family_heart_cond" SET DEFAULT ' ';

ALTER TABLE "person_h" ALTER COLUMN "two_family_cancer" SET DEFAULT ' ';

ALTER TABLE "person_h" ALTER COLUMN "two_family_diabetes" SET DEFAULT ' ';







ALTER TABLE "person" DROP CONSTRAINT "drug_alcohol_abuse_chk";

UPDATE "person" SET "drug_alcohol_abuse" = ' ' where "drug_alcohol_abuse" = 'U';

ALTER TABLE "person" ADD CONSTRAINT "drug_alcohol_abuse_chk" CHECK ((((drug_alcohol_abuse=' ')OR(drug_alcohol_abuse='Y'))OR(drug_alcohol_abuse='N')));

ALTER TABLE "person" DROP CONSTRAINT "not_missed_five_days_chk";

UPDATE "person" SET "not_missed_five_days" = ' ' where "not_missed_five_days" = 'U';

ALTER TABLE "person" ADD CONSTRAINT "not_missed_five_days_chk" CHECK ((((not_missed_five_days=' ')OR(not_missed_five_days='Y'))OR(not_missed_five_days='N')));

ALTER TABLE "person" DROP CONSTRAINT "replace_employer_plan_chk";

UPDATE "person" SET "replace_employer_plan" = ' ' where "replace_employer_plan" = 'U';

ALTER TABLE "person" ADD CONSTRAINT "replace_employer_plan_chk" CHECK ((((replace_employer_plan=' ')OR(replace_employer_plan='Y'))OR(replace_employer_plan='N')));

ALTER TABLE "person" DROP CONSTRAINT "two_family_cancer_chk";

UPDATE "person" SET "two_family_cancer" = ' ' where "two_family_cancer" = 'U';

ALTER TABLE "person" ADD CONSTRAINT "two_family_cancer_chk" CHECK ((((two_family_cancer=' ')OR(two_family_cancer='Y'))OR(two_family_cancer='N')));

ALTER TABLE "person" DROP CONSTRAINT "two_family_diabetes_chk";

UPDATE "person" SET "two_family_diabetes" = ' ' where "two_family_diabetes" = 'U';

ALTER TABLE "person" ADD CONSTRAINT "two_family_diabetes_chk" CHECK ((((two_family_diabetes=' ')OR(two_family_diabetes='Y'))OR(two_family_diabetes='N')));

ALTER TABLE "person" DROP CONSTRAINT "two_family_heart_cond_chk";

UPDATE "person" SET "two_family_heart_cond" = ' ' where "two_family_heart_cond" = 'U';

ALTER TABLE "person" ADD CONSTRAINT "two_family_heart_cond_chk" CHECK ((((two_family_heart_cond=' ')OR(two_family_heart_cond='Y'))OR(two_family_heart_cond='N')));

ALTER TABLE "person_h" DROP CONSTRAINT "drug_alcohol_abuse_ch2";

UPDATE "person_h" SET "drug_alcohol_abuse" = ' ' where "drug_alcohol_abuse" = 'U';

ALTER TABLE "person_h" ADD CONSTRAINT "drug_alcohol_abuse_ch2" CHECK ((((drug_alcohol_abuse=' ')OR(drug_alcohol_abuse='Y'))OR(drug_alcohol_abuse='N')));

ALTER TABLE "person_h" DROP CONSTRAINT "not_missed_five_days_ch2";

UPDATE "person_h" SET "not_missed_five_days" = ' ' where "not_missed_five_days" = 'U';

ALTER TABLE "person_h" ADD CONSTRAINT "not_missed_five_days_ch2" CHECK ((((not_missed_five_days=' ')OR(not_missed_five_days='Y'))OR(not_missed_five_days='N')));

ALTER TABLE "person_h" DROP CONSTRAINT "replace_employer_plan_ch2";

UPDATE "person_h" SET "replace_employer_plan" = ' ' where "replace_employer_plan" = 'U';

ALTER TABLE "person_h" ADD CONSTRAINT "replace_employer_plan_ch2" CHECK ((((replace_employer_plan=' ')OR(replace_employer_plan='Y'))OR(replace_employer_plan='N')));

ALTER TABLE "person_h" DROP CONSTRAINT "two_family_cancer_ch2";

UPDATE "person_h" SET "two_family_cancer" = ' ' where "two_family_cancer" = 'U';

ALTER TABLE "person_h" ADD CONSTRAINT "two_family_cancer_ch2" CHECK ((((two_family_cancer=' ')OR(two_family_cancer='Y'))OR(two_family_cancer='N')));

ALTER TABLE "person_h" DROP CONSTRAINT "two_family_diabetes_ch2";

UPDATE "person_h" SET "two_family_diabetes" = ' ' where "two_family_diabetes" = 'U';

ALTER TABLE "person_h" ADD CONSTRAINT "two_family_diabetes_ch2" CHECK ((((two_family_diabetes=' ')OR(two_family_diabetes='Y'))OR(two_family_diabetes='N')));

ALTER TABLE "person_h" DROP CONSTRAINT "two_family_heart_cond_ch2";

UPDATE "person_h" SET "two_family_heart_cond" = ' ' where "two_family_heart_cond" = 'U';

ALTER TABLE "person_h" ADD CONSTRAINT "two_family_heart_cond_ch2" CHECK ((((two_family_heart_cond=' ')OR(two_family_heart_cond='Y'))OR(two_family_heart_cond='N')));
