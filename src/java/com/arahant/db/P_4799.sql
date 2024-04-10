
-- PostgreSQL patch from revision 4796 to revision 4797


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "change_log" (
	"change_id" bigint NOT NULL,
	"change_when" timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
	"change_person_id" character(16) NOT NULL,
	"person_id" character(16),
	"project_id" character(16),
	"client_id" character(16),
	"vendor_id" character(16),
    "table_changed" character varying(60),
    "column_changed" character varying(60),
    "old_value" character varying(40),
	"new_value" character varying(40),
	"description" character varying(80)
);

CREATE SEQUENCE public.change_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.change_log_id_seq OWNER TO postgres;

ALTER SEQUENCE public.change_log_id_seq OWNED BY public.change_log.change_id;

ALTER TABLE ONLY public.change_log ALTER COLUMN change_id SET DEFAULT nextval('public.change_log_id_seq'::regclass);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE ONLY "change_log" ADD CONSTRAINT "change_log_pkey" PRIMARY KEY ("change_id");

CREATE INDEX "fki_change_log_change_person_fkey" ON "change_log" USING btree ("change_person_id");

CREATE INDEX "fki_change_log_client_fkey" ON "change_log" USING btree ("client_id");

CREATE INDEX "fki_change_log_person_fkey" ON "change_log" USING btree ("person_id");

CREATE INDEX "fki_change_log_project_fkey" ON "change_log" USING btree ("project_id");

CREATE INDEX "fki_change_log_vendor_fkey" ON "change_log" USING btree ("vendor_id");

CREATE INDEX "change_log_table_idx" ON "change_log" USING btree ("table_changed", "column_changed");

COMMENT ON COLUMN "change_log"."change_person_id" IS 'The person making the change';

COMMENT ON COLUMN "change_log"."change_when" IS 'When the change was made';

COMMENT ON COLUMN "change_log"."client_id" IS 'If the change is related to a client, this is the client the change is related to';

COMMENT ON COLUMN "change_log"."person_id" IS 'If the change is related to a person, this is the person the change is about';

COMMENT ON COLUMN "change_log"."project_id" IS 'If the change is related to a project, this is the related project';

COMMENT ON COLUMN "change_log"."vendor_id" IS 'If the change is related to a vendor, this is the vendor the change is related to';


ALTER TABLE ONLY "change_log" ADD CONSTRAINT "change_log_change_person_fkey" FOREIGN KEY ("change_person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "change_log" ADD CONSTRAINT "change_log_client_fkey" FOREIGN KEY ("client_id") REFERENCES "client" ("org_group_id");

ALTER TABLE ONLY "change_log" ADD CONSTRAINT "change_log_person_fkey" FOREIGN KEY ("person_id") REFERENCES "person" ("person_id");

ALTER TABLE ONLY "change_log" ADD CONSTRAINT "change_log_project_fkey" FOREIGN KEY ("project_id") REFERENCES "project" ("project_id");

ALTER TABLE ONLY "change_log" ADD CONSTRAINT "change_log_vendor_fkey" FOREIGN KEY ("vendor_id") REFERENCES "vendor" ("org_group_id");

