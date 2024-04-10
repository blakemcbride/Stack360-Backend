CREATE OR REPLACE VIEW current_employee_status AS
SELECT
    esh.employee_id,
    esh.effective_date,
    esh.status_id,
    esh.notes,
    esh.record_change_date
FROM
    (
        SELECT *,
               ROW_NUMBER() OVER (PARTITION BY employee_id ORDER BY effective_date DESC) as rn
        FROM public.hr_empl_status_history
        WHERE effective_date <= TO_NUMBER(TO_CHAR(CURRENT_DATE, 'YYYYMMDD'), '99999999')
    ) as esh
WHERE esh.rn = 1;



CREATE OR REPLACE VIEW current_employee_wage AS
SELECT
    w.employee_id,
    w.wage_type_id,
    w.wage_amount,
    w.effective_date,
    w.position_id,
    w.notes
FROM
    (
        SELECT *,
               ROW_NUMBER() OVER (PARTITION BY employee_id ORDER BY effective_date DESC) as rn
        FROM public.hr_wage
        WHERE effective_date <= TO_NUMBER(TO_CHAR(CURRENT_DATE, 'YYYYMMDD'), '99999999')
    ) as w
WHERE w.rn = 1;
