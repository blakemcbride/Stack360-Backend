
-- PostgreSQL patch from revision 4259 to revision 4260


CREATE TABLE "time_type" (
	"time_type_id" character(16) NOT NULL,
	"description" character varying(20) NOT NULL
);

ALTER TABLE ONLY "time_type" ADD CONSTRAINT "time_type_pkey" PRIMARY KEY ("time_type_id");


ALTER TABLE "timesheet" ADD COLUMN "time_type_id" character(16);


INSERT INTO time_type (time_type_id, description) VALUES ('00001-0000000001', 'Unspecified'); 

UPDATE timesheet set time_type_id = '00001-0000000001';


ALTER TABLE "timesheet" ALTER COLUMN "time_type_id" SET NOT NULL;


CREATE INDEX "fki_time_type_id_fkey" ON "timesheet" USING btree ("time_type_id");


ALTER TABLE ONLY "timesheet" ADD CONSTRAINT "time_type_id_fkey" FOREIGN KEY ("time_type_id") REFERENCES "time_type" ("time_type_id");

