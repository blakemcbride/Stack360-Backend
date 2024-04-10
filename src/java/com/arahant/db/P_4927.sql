
update screen set technology = 'H', auth_code = null;

alter table public.screen
    alter column technology set default 'H';

comment on column public.screen.filename is 'Directory where screen is stored';

comment on column public.screen.auth_code is 'Code which authorizes the screen use - not used';

update screen set filename = 'custom/waytogo/applicantImport'
       where filename = 'com/arahant/app/screen/custom/waytogo/applicantImport/ApplicantImportScreen.swf';

update screen set filename = 'standard/at/applicantQuestion'
       where filename = 'com/arahant/app/screen/standard/hr/applicantQuestion/ApplicantQuestionScreen.swf';

update screen set filename = 'standard/at/applicantPosition'
       where filename = 'com/arahant/app/screen/standard/hr/applicantPosition/ApplicantPositionScreen.swf';

update screen set filename = 'standard/at/applicantJobType'
       where filename = 'com/arahant/app/screen/standard/hr/applicantJobType/ApplicantJobTypeScreen.swf';

update screen set filename = 'standard/at/applicantApplicationStatus'
       where filename = 'com/arahant/app/screen/standard/hr/applicantApplicationStatus/ApplicantApplicationStatusScreen.swf';

update screen set filename = 'standard/at/applicantEeo'
       where filename = 'com/arahant/app/screen/standard/hr/applicantEeo/ApplicantEeoScreen.swf';

update screen set filename = 'standard/at/applicantStatus'
       where filename = 'com/arahant/app/screen/standard/hr/applicantStatus/ApplicantStatusScreen.swf';

delete from screen
       where filename = 'com/arahant/app/screen/custom/mms/applicant/ApplicantScreen.swf';

update screen set filename = 'standard/at/applicantProfile'
       where filename = 'com/arahant/app/screen/standard/hr/applicantProfile/ApplicantProfileScreen.swf';

update screen set filename = 'standard/at/applicant'
       where filename = 'com/arahant/app/screen/standard/hr/applicant/ApplicantScreen.swf';

update screen set filename = 'standard/at/applicantSource'
       where filename = 'com/arahant/app/screen/standard/hr/applicantSource/ApplicantSourceScreen.swf';


