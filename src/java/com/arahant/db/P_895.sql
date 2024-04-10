

--  Remove indexes and checks


--  Add new tables


--  Add new columns

ALTER TABLE "screen" ADD COLUMN "technology" character(1) DEFAULT 'F' NOT NULL;

ALTER TABLE "screen_group" ADD COLUMN "technology" character(1) DEFAULT 'F' NOT NULL;


--  Change existing columns


--  Remove tables


--  Drop columns


--  Add new indexes and checks

ALTER TABLE "screen" ADD CONSTRAINT "screen_tech_chk" CHECK (((technology='F')OR(technology='H')));

ALTER TABLE "screen_group" ADD CONSTRAINT "screen_group_tech_chk" CHECK (((technology='F')OR(technology='H')));

COMMENT ON COLUMN "screen"."technology" IS 'F = Flash
H = HTML';

COMMENT ON COLUMN "screen_group"."technology" IS 'F = Flash
H = HTML';



