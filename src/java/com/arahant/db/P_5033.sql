
-- PostgreSQL patch from revision 5032 to revision 5033


--  Remove indexes and checks


--  Add new tables


--  Add new columns


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE "message_attachment" DROP COLUMN "attachment";


--  Add new indexes and checks

COMMENT ON COLUMN "client"."picture_disk_path" IS 'This path is used to store images that the client can access';


