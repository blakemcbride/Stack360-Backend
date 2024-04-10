
-- PostgreSQL patch from revision 4332 to revision 4334


--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "prophet_login" ADD COLUMN "when_created" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL;

ALTER TABLE "prophet_login" ADD COLUMN "authentication_code" character varying(6);

ALTER TABLE "prophet_login" ADD COLUMN "number_of_resends" smallint DEFAULT 0 NOT NULL;

ALTER TABLE "prophet_login" ADD COLUMN "number_of_authentications" smallint DEFAULT 0 NOT NULL;

ALTER TABLE "prophet_login" ADD COLUMN "reset_password_date" timestamp with time zone;

ALTER TABLE "prophet_login" ADD COLUMN "user_type" character(1) DEFAULT 'R' NOT NULL;


UPDATE prophet_login set when_created = '1980-01-01';


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE "prophet_login" DROP CONSTRAINT "login_can_login_check";


--  Add new indexes and checks

ALTER TABLE "prophet_login" ADD CONSTRAINT "login_can_login_chk" CHECK (((can_login='Y')OR(can_login='N')OR(can_login='X')));

ALTER TABLE "prophet_login" ADD CONSTRAINT "login_usr_type_chk" CHECK (((user_type='R')OR(user_type='A')));

COMMENT ON COLUMN "prophet_login"."authentication_code" IS 'Authentication code used by applicants who are creating their own login';

COMMENT ON COLUMN "prophet_login"."can_login" IS 'Yes / No / X not authenticated';

COMMENT ON COLUMN "prophet_login"."number_of_authentications" IS 'Number of times an applicant attempted to authenticate';

COMMENT ON COLUMN "prophet_login"."number_of_resends" IS 'Number of times an applicant requested to re-send their authorization code';

COMMENT ON COLUMN "prophet_login"."reset_password_date" IS 'Date a password reset was requested';

COMMENT ON COLUMN "prophet_login"."user_type" IS '(R)egular or (A)pplicant';

