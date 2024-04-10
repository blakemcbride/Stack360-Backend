
update previous_employment set start_date = start_date / 100 where start_date > 999999;

update previous_employment set end_date = end_date / 100 where end_date > 999999;


update education set start_date = start_date / 100 where start_date > 999999;

update education set end_date = end_date / 100 where end_date > 999999;


