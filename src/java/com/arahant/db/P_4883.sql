

DROP INDEX IF EXISTS fki_phone_org_group_fkey;
DROP INDEX IF EXISTS fki_phone_person_fkey;

DROP INDEX IF EXISTS phone_org_group_uidx;
DROP INDEX IF EXISTS phone_person_uidx;

CREATE UNIQUE INDEX phone_person_uidx
    ON public.phone USING btree
    (person_join ASC NULLS LAST, phone_type ASC NULLS LAST);

CREATE UNIQUE INDEX phone_org_group_uidx
    ON public.phone USING btree
    (org_group_join ASC NULLS LAST, phone_type ASC NULLS LAST);

delete from phone where phone_number is null;

ALTER TABLE phone
    ALTER COLUMN phone_number SET NOT NULL;
