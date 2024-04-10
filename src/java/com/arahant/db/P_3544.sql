
-- PostgreSQL patch from revision 3543 to revision 3544


--  Remove indexes and checks


--  Add new tables

CREATE TABLE "authenticated_senders" (
	"auth_send_id" character(16) NOT NULL,
	"address_type" character(1) NOT NULL,
	"address" character varying(50) NOT NULL,
	CONSTRAINT "auth_send_type_chk" CHECK (((address_type='D')OR(address_type='E')))
);


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

CREATE UNIQUE INDEX "auth_send_idx" ON "authenticated_senders" USING btree ("address_type", "address");

ALTER TABLE ONLY "authenticated_senders" ADD CONSTRAINT "authenticated_senders_pkey" PRIMARY KEY ("auth_send_id");

COMMENT ON COLUMN "authenticated_senders"."address_type" IS '(D)omain or (E)mail address';

COMMENT ON TABLE "authenticated_senders" IS 'Domains or email addresses that are authenticated to send over email.  Note that this does not authenticate them.  This merely indicates what addresses are authenticated.';

