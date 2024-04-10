--
-- PostgreSQL database dump
--

-- Dumped from database version 15.4
-- Dumped by pg_dump version 15.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.address (
    address_id character(16) NOT NULL,
    street character varying(60),
    city character varying(60),
    state character varying(25),
    zip character varying(10),
    person_join character(16),
    org_group_join character(16),
    address_type integer NOT NULL,
    street2 character varying(60),
    country_code character(2) DEFAULT 'US'::bpchar NOT NULL,
    county character varying(30),
    record_type character(1) DEFAULT 'R'::bpchar NOT NULL,
    time_zone_offset smallint DEFAULT 100 NOT NULL,
    CONSTRAINT address_record_type_chk CHECK (((record_type = 'R'::bpchar) OR (record_type = 'C'::bpchar)))
);


ALTER TABLE public.address OWNER TO postgres;

--
-- Name: TABLE address; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.address IS 'Address table can be linked to companies, people, etc..  It should be used to store all addresses in the system.';


--
-- Name: COLUMN address.address_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.address.address_id IS 'Primary key';


--
-- Name: COLUMN address.street; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.address.street IS 'Street address';


--
-- Name: COLUMN address.city; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.address.city IS 'The city';


--
-- Name: COLUMN address.state; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.address.state IS 'Two character state abbreviation or four character foreign local, or province';


--
-- Name: COLUMN address.zip; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.address.zip IS 'Zip+4 code';


--
-- Name: COLUMN address.person_join; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.address.person_join IS 'Join to person table';


--
-- Name: COLUMN address.org_group_join; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.address.org_group_join IS 'Join to org_group table';


--
-- Name: COLUMN address.address_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.address.address_type IS 'Address type
1=work
2=home
3=P.O. Box';


--
-- Name: COLUMN address.country_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.address.country_code IS 'Codes from http://www.iso.org/iso/country_codes/iso_3166_code_lists/english_country_names_and_code_elements.htm';


--
-- Name: COLUMN address.record_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.address.record_type IS 'R = Real record
C = Change request';


--
-- Name: COLUMN address.time_zone_offset; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.address.time_zone_offset IS 'Offset from UTC.  100 means unknown.';


--
-- Name: address_cr; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.address_cr (
    change_request_id character(16) NOT NULL,
    real_record_id character(16),
    change_status character(1) NOT NULL,
    request_time timestamp with time zone NOT NULL,
    change_record_id character(16) NOT NULL,
    requestor_id character(16) NOT NULL,
    approver_id character(16),
    approval_time timestamp with time zone,
    project_id character(16) NOT NULL,
    CONSTRAINT address_rcr_status_chk CHECK (((change_status = 'P'::bpchar) OR (change_status = 'A'::bpchar) OR (change_status = 'R'::bpchar)))
);


ALTER TABLE public.address_cr OWNER TO postgres;

--
-- Name: TABLE address_cr; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.address_cr IS 'Holds record change requests for the address table.';


--
-- Name: COLUMN address_cr.change_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.address_cr.change_status IS 'P = Pending request
A = Approved
R = Rejected';


--
-- Name: agency; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.agency (
    agency_id character(16) NOT NULL,
    agency_external_id character varying(20)
);


ALTER TABLE public.agency OWNER TO postgres;

--
-- Name: agency_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.agency_join (
    agency_join_id character(16) NOT NULL,
    agency_id character(16) NOT NULL,
    company_id character(16) NOT NULL
);


ALTER TABLE public.agency_join OWNER TO postgres;

--
-- Name: agent; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.agent (
    agent_id character(16) NOT NULL,
    ext_ref character varying(11)
);


ALTER TABLE public.agent OWNER TO postgres;

--
-- Name: agent_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.agent_join (
    agent_join_id character(16) NOT NULL,
    agent_id character(16) NOT NULL,
    company_id character(16) NOT NULL,
    approved character(1) DEFAULT 'N'::bpchar NOT NULL,
    approved_by_person_id character(16),
    approved_date timestamp with time zone,
    CONSTRAINT agent_join_app_chk CHECK (((approved = 'Y'::bpchar) OR (approved = 'N'::bpchar)))
);


ALTER TABLE public.agent_join OWNER TO postgres;

--
-- Name: agreement_form; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.agreement_form (
    agreement_form_id character(16) NOT NULL,
    form_date timestamp with time zone NOT NULL,
    description character varying(60) NOT NULL,
    summary character varying(255),
    file_name_ext character varying(10) NOT NULL,
    expiration_date integer DEFAULT 0 NOT NULL,
    form bytea NOT NULL
);


ALTER TABLE public.agreement_form OWNER TO postgres;

--
-- Name: COLUMN agreement_form.form_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.agreement_form.form_date IS 'When the form was added';


--
-- Name: COLUMN agreement_form.file_name_ext; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.agreement_form.file_name_ext IS 'This tells us what type of form it is.';


--
-- Name: COLUMN agreement_form.expiration_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.agreement_form.expiration_date IS 'The form is no longer valad AFTER this date';


--
-- Name: agreement_person_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.agreement_person_join (
    agreement_person_join_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    agreement_form_id character(16) NOT NULL,
    agreement_time timestamp with time zone NOT NULL
);


ALTER TABLE public.agreement_person_join OWNER TO postgres;

--
-- Name: alert; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.alert (
    alert_id character(16) NOT NULL,
    alert_distribution smallint NOT NULL,
    start_date integer NOT NULL,
    last_date integer NOT NULL,
    alert_short character varying(2000),
    alert_long text,
    org_group_id character(16),
    person_id character(16),
    last_change_person_id character(16) NOT NULL,
    last_chage_datetime timestamp with time zone NOT NULL,
    CONSTRAINT alert_dist_chk CHECK (((alert_distribution >= 1) AND (alert_distribution <= 6))),
    CONSTRAINT alert_last_date_chk CHECK ((last_date > 0)),
    CONSTRAINT alert_start_date_chk CHECK ((start_date > 0))
);


ALTER TABLE public.alert OWNER TO postgres;

--
-- Name: COLUMN alert.alert_distribution; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.alert.alert_distribution IS '1 = to all employees of a company
2 = particular employees in a company
3 = to all employees of a company (from agent)
4 = to all members of an agent''s company
5 = to all agents
6 = all company main contacts';


--
-- Name: COLUMN alert.last_change_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.alert.last_change_person_id IS 'This is the person who last changed this record.';


--
-- Name: COLUMN alert.last_chage_datetime; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.alert.last_chage_datetime IS 'This is the date and time this record was last changed.';


--
-- Name: alert_person_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.alert_person_join (
    alert_id character(16) NOT NULL,
    person_id character(16) NOT NULL
);


ALTER TABLE public.alert_person_join OWNER TO postgres;

--
-- Name: applicant; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.applicant (
    person_id character(16) NOT NULL,
    applicant_source_id character(16) NOT NULL,
    applicant_status_id character(16) NOT NULL,
    first_aware_date integer DEFAULT 0 NOT NULL,
    comments character varying(2000),
    eeo_race_id character(16),
    date_available integer DEFAULT 0 NOT NULL,
    desired_salary integer DEFAULT 0 NOT NULL,
    referred_by character varying(40),
    years_experience smallint,
    day_shift character(1),
    night_shift character(1),
    veteran character(1),
    signature character varying(40),
    when_signed timestamp with time zone,
    travel_personal character(1) DEFAULT 'N'::bpchar NOT NULL,
    travel_friend character(1) DEFAULT 'N'::bpchar NOT NULL,
    travel_public character(1) DEFAULT 'N'::bpchar NOT NULL,
    travel_unknown character(1) DEFAULT 'N'::bpchar NOT NULL,
    agrees character(1) DEFAULT 'N'::bpchar NOT NULL,
    agreement_name character varying(50),
    agreement_date timestamp with time zone,
    background_check_authorized character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT applacant_travel_unknown_chk CHECK (((travel_unknown = 'Y'::bpchar) OR (travel_unknown = 'N'::bpchar))),
    CONSTRAINT applicant_agrees_chk CHECK (((agrees = 'Y'::bpchar) OR (agrees = 'N'::bpchar))),
    CONSTRAINT applicant_background_chk CHECK (((background_check_authorized = 'Y'::bpchar) OR (background_check_authorized = 'N'::bpchar))),
    CONSTRAINT applicant_travel_friend_chk CHECK (((travel_friend = 'Y'::bpchar) OR (travel_friend = 'N'::bpchar))),
    CONSTRAINT applicant_travel_personal_chk CHECK (((travel_personal = 'Y'::bpchar) OR (travel_personal = 'N'::bpchar))),
    CONSTRAINT applicant_travel_public_chk CHECK (((travel_public = 'Y'::bpchar) OR (travel_public = 'N'::bpchar)))
);


ALTER TABLE public.applicant OWNER TO postgres;

--
-- Name: COLUMN applicant.date_available; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant.date_available IS 'YYYYMMDD';


--
-- Name: COLUMN applicant.background_check_authorized; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant.background_check_authorized IS 'Did the applicant authorize a background check?';


--
-- Name: applicant_answer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.applicant_answer (
    applicant_answer_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    applicant_question_id character(16) NOT NULL,
    string_answer character varying(2000),
    date_answer integer,
    numeric_answer double precision,
    applicant_question_choice_id character(16)
);


ALTER TABLE public.applicant_answer OWNER TO postgres;

--
-- Name: applicant_app_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.applicant_app_status (
    applicant_app_status_id character(16) NOT NULL,
    status_order smallint NOT NULL,
    status_name character varying(40) NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    is_active character(1) DEFAULT 'Y'::bpchar,
    company_id character(16),
    phase smallint DEFAULT 0 NOT NULL,
    CONSTRAINT applicant_app_status_is_active_chk CHECK (((is_active = 'Y'::bpchar) OR (is_active = 'N'::bpchar)))
);


ALTER TABLE public.applicant_app_status OWNER TO postgres;

--
-- Name: COLUMN applicant_app_status.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_app_status.last_active_date IS 'Last date this record should be used in a new association.  0 means no end.';


--
-- Name: COLUMN applicant_app_status.is_active; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_app_status.is_active IS 'Is this a status that indicates an applicant that is still ellegable for hire nor or possibly sometime in the furture?';


--
-- Name: COLUMN applicant_app_status.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_app_status.company_id IS 'Company this record is associated with.  NULL means all companies.';


--
-- Name: COLUMN applicant_app_status.phase; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_app_status.phase IS '0 = no application
1 = application made
2 = offer extended
3 = offer accepted
4 = hired
5 = offer rejected
6 = offer retracted';


--
-- Name: applicant_application; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.applicant_application (
    applicant_application_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    applicant_position_id character(16),
    application_date integer DEFAULT 0 NOT NULL,
    applicant_app_status_id character(16) NOT NULL,
    offer_first_generated timestamp with time zone,
    offer_last_generated timestamp with time zone,
    offer_first_emailed timestamp with time zone,
    offer_last_emailed timestamp with time zone,
    offer_elec_signed_date timestamp with time zone,
    offer_elec_signed_ip character varying(15),
    pay_rate real,
    position_id character(16) NOT NULL,
    phase smallint DEFAULT 0 NOT NULL,
    offer_declined_date timestamp with time zone,
    offer_retracted_date timestamp with time zone,
    offer_last_viewed_date timestamp with time zone
);


ALTER TABLE public.applicant_application OWNER TO postgres;

--
-- Name: COLUMN applicant_application.applicant_position_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_application.applicant_position_id IS 'If applying for an actual position being offered';


--
-- Name: COLUMN applicant_application.position_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_application.position_id IS 'Their HR position ID.  Irrespective of the job they have applied to, this field indicates what we are sloting them for.';


--
-- Name: COLUMN applicant_application.phase; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_application.phase IS '0 = no application
1 = application made
2 = offer extended
3 = offer accepted
4 = hired
5 = offer rejected
6 = offer retracted';


--
-- Name: COLUMN applicant_application.offer_last_viewed_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_application.offer_last_viewed_date IS 'This is when the offer was viewed through the applicant interface.  We are not tracking if they viewed an offer made via email.';


--
-- Name: applicant_contact; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.applicant_contact (
    applicant_contact_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    applicant_application_id character(16),
    contact_date integer DEFAULT 0 NOT NULL,
    contact_time integer DEFAULT 0 NOT NULL,
    contact_mode character(1) NOT NULL,
    contact_status character(1) NOT NULL,
    description character varying(2000) NOT NULL,
    who_added character(16) NOT NULL,
    CONSTRAINT applicant_contact_contact_mode_chk CHECK (((contact_mode = 'P'::bpchar) OR (contact_mode = 'E'::bpchar) OR (contact_mode = 'F'::bpchar) OR (contact_mode = 'M'::bpchar) OR (contact_mode = 'C'::bpchar) OR (contact_mode = 'R'::bpchar) OR (contact_mode = 'G'::bpchar))),
    CONSTRAINT applicant_contact_contact_status_chk CHECK (((contact_status = 'S'::bpchar) OR (contact_status = 'A'::bpchar) OR (contact_status = 'C'::bpchar) OR (contact_status = 'N'::bpchar) OR (contact_status = 'L'::bpchar) OR (contact_status = 'O'::bpchar)))
);


ALTER TABLE public.applicant_contact OWNER TO postgres;

--
-- Name: COLUMN applicant_contact.contact_mode; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_contact.contact_mode IS 'Phone
Email
Fax
Mail
Company Site
Remote Location
General';


--
-- Name: COLUMN applicant_contact.contact_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_contact.contact_status IS 'appointment Set
Applicant cancelled or rescheduled
Company cancelled or rescheduled
No show
Late
On time';


--
-- Name: applicant_position; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.applicant_position (
    applicant_position_id character(16) NOT NULL,
    job_title character varying(80) NOT NULL,
    accept_applicant_date integer DEFAULT 0 NOT NULL,
    job_start_date integer DEFAULT 0 NOT NULL,
    org_group_id character(16) NOT NULL,
    position_status character(1) NOT NULL,
    ext_ref character varying(15),
    position_id character(16) NOT NULL,
    CONSTRAINT applicant_position_position_status_chk CHECK (((position_status = 'N'::bpchar) OR (position_status = 'A'::bpchar) OR (position_status = 'S'::bpchar) OR (position_status = 'F'::bpchar) OR (position_status = 'C'::bpchar)))
);


ALTER TABLE public.applicant_position OWNER TO postgres;

--
-- Name: TABLE applicant_position; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.applicant_position IS 'This table holds the open jobs that people can apply for.';


--
-- Name: COLUMN applicant_position.position_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_position.position_status IS 'New
Accepting applicants
Suspended
Filled
Cancelled';


--
-- Name: COLUMN applicant_position.ext_ref; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_position.ext_ref IS 'This is the company''s job ID';


--
-- Name: COLUMN applicant_position.position_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_position.position_id IS 'This is the position the ad is for.';


--
-- Name: applicant_question; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.applicant_question (
    applicant_question_id character(16) NOT NULL,
    question_order smallint NOT NULL,
    question character varying(80) NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    data_type character(1) DEFAULT 'S'::bpchar NOT NULL,
    internal_use character(1) DEFAULT 'N'::bpchar NOT NULL,
    company_id character(16),
    position_id character(16),
    CONSTRAINT app_ques_intern_use_chk CHECK (((internal_use = 'Y'::bpchar) OR (internal_use = 'N'::bpchar))),
    CONSTRAINT applicant_ques_data_chk CHECK (((data_type = 'N'::bpchar) OR (data_type = 'D'::bpchar) OR (data_type = 'S'::bpchar) OR (data_type = 'Y'::bpchar) OR (data_type = 'L'::bpchar)))
);


ALTER TABLE public.applicant_question OWNER TO postgres;

--
-- Name: COLUMN applicant_question.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_question.last_active_date IS 'Last date this record can be used in a new association.  0 means no end.';


--
-- Name: COLUMN applicant_question.data_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_question.data_type IS 'Numeric (double)
Date (YYYYMMDD)
String
Yes/no/unknown (Yes/No/Unknown in field)
List of choices (applicant_question_choice table)';


--
-- Name: COLUMN applicant_question.internal_use; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_question.internal_use IS 'If ''Y'' question used internally or on custom screen, not in stanbdard question dropdowns';


--
-- Name: COLUMN applicant_question.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_question.company_id IS 'Company this record is associated with.  NULL means all companies.';


--
-- Name: applicant_question_choice; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.applicant_question_choice (
    applicant_question_choice_id character(16) NOT NULL,
    applicant_question_id character(16) NOT NULL,
    description character varying(60) NOT NULL
);


ALTER TABLE public.applicant_question_choice OWNER TO postgres;

--
-- Name: applicant_source; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.applicant_source (
    applicant_source_id character(16) NOT NULL,
    description character varying(40) NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    company_id character(16)
);


ALTER TABLE public.applicant_source OWNER TO postgres;

--
-- Name: COLUMN applicant_source.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_source.last_active_date IS 'Last date this record should be used in a new association.  0 means no end.';


--
-- Name: COLUMN applicant_source.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_source.company_id IS 'Company this record is associated with.  NULL means all companies.';


--
-- Name: applicant_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.applicant_status (
    applicant_status_id character(16) NOT NULL,
    status_order smallint NOT NULL,
    name character varying(40) NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    consider_for_hire character(1) NOT NULL,
    send_email character(1) DEFAULT 'N'::bpchar NOT NULL,
    email_source character varying(50),
    email_text text,
    email_subject character varying(100),
    company_id character(16),
    CONSTRAINT applicant_status_consider_for_hire_chk CHECK (((consider_for_hire = 'Y'::bpchar) OR (consider_for_hire = 'N'::bpchar))),
    CONSTRAINT applicant_status_email_chk CHECK (((send_email = 'N'::bpchar) OR ((send_email = 'Y'::bpchar) AND (email_source IS NOT NULL))))
);


ALTER TABLE public.applicant_status OWNER TO postgres;

--
-- Name: COLUMN applicant_status.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_status.last_active_date IS 'Last date this record should be used in a new association.  0 means no end.';


--
-- Name: COLUMN applicant_status.consider_for_hire; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_status.consider_for_hire IS 'Yes
No';


--
-- Name: COLUMN applicant_status.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.applicant_status.company_id IS 'Company this record is associated with.  NULL means all companies.';


--
-- Name: appointment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.appointment (
    appointment_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    meeting_date integer NOT NULL,
    meeting_time integer NOT NULL,
    meeting_time_type character(1),
    attendees character varying(500),
    meeting_location character varying(500),
    status character(1) NOT NULL,
    purpose character varying(2000) NOT NULL,
    location_id character(16),
    meeting_length smallint NOT NULL,
    CONSTRAINT prospect_appt_status_chk CHECK (((status = 'A'::bpchar) OR (status = 'C'::bpchar) OR (status = 'D'::bpchar))),
    CONSTRAINT prospect_appt_time_type_chk CHECK (((meeting_time_type = 'P'::bpchar) OR (meeting_time_type = 'A'::bpchar)))
);


ALTER TABLE public.appointment OWNER TO postgres;

--
-- Name: COLUMN appointment.meeting_time_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.appointment.meeting_time_type IS 'P = at specified date and time
A = arrange after specified date and time';


--
-- Name: COLUMN appointment.attendees; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.appointment.attendees IS 'Attendee list';


--
-- Name: COLUMN appointment.meeting_location; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.appointment.meeting_location IS 'Location of meeting';


--
-- Name: COLUMN appointment.status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.appointment.status IS 'A = Active
C = Cancelled
D = Done / complete';


--
-- Name: COLUMN appointment.purpose; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.appointment.purpose IS 'Purpose of meeting or callback';


--
-- Name: COLUMN appointment.meeting_length; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.appointment.meeting_length IS 'Estimated length of meeting in minutes';


--
-- Name: appointment_location; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.appointment_location (
    location_id character(16) NOT NULL,
    code character varying(12) NOT NULL,
    description character varying(120) NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    company_id character(16)
);


ALTER TABLE public.appointment_location OWNER TO postgres;

--
-- Name: TABLE appointment_location; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.appointment_location IS 'Appointment locations can be specific like "Board Room #3" or general like "Client''s Office"';


--
-- Name: COLUMN appointment_location.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.appointment_location.last_active_date IS 'Last date this record can be used in a new association.  0 means no end.';


--
-- Name: COLUMN appointment_location.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.appointment_location.company_id IS 'Company this record is associated with.  NULL means all companies.';


--
-- Name: appointment_person_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.appointment_person_join (
    join_id character(16) NOT NULL,
    appointment_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    primary_person character(1) NOT NULL,
    CONSTRAINT prospect_appt_primary_emp_chk CHECK (((primary_person = 'Y'::bpchar) OR (primary_person = 'N'::bpchar)))
);


ALTER TABLE public.appointment_person_join OWNER TO postgres;

--
-- Name: COLUMN appointment_person_join.primary_person; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.appointment_person_join.primary_person IS 'Is this the primary employee archestrating the meeting?';


--
-- Name: assembly_template; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.assembly_template (
    assembly_template_id character(16) NOT NULL,
    assembly_name character varying(80) NOT NULL,
    description character varying(256)
);


ALTER TABLE public.assembly_template OWNER TO postgres;

--
-- Name: assembly_template_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.assembly_template_detail (
    assembly_template_detail_id character(16) NOT NULL,
    assembly_template_id character(16),
    parent_detail_id character(16),
    product_id character(16) NOT NULL,
    quantity integer NOT NULL,
    item_particulars character varying(256),
    track_to_item character(1) NOT NULL,
    CONSTRAINT assem_temp_pointer_chk CHECK ((((parent_detail_id IS NOT NULL) AND (assembly_template_id IS NULL)) OR ((parent_detail_id IS NULL) AND (assembly_template_id IS NOT NULL)))),
    CONSTRAINT assembly_template_track_check CHECK (((track_to_item = 'Y'::bpchar) OR (track_to_item = 'N'::bpchar)))
);


ALTER TABLE public.assembly_template_detail OWNER TO postgres;

--
-- Name: COLUMN assembly_template_detail.assembly_template_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.assembly_template_detail.assembly_template_id IS 'Use this when it is not in some other part';


--
-- Name: COLUMN assembly_template_detail.parent_detail_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.assembly_template_detail.parent_detail_id IS 'Use this when it is inside some other part';


--
-- Name: COLUMN assembly_template_detail.track_to_item; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.assembly_template_detail.track_to_item IS 'Y = Track to individual items
N = Track as a group (quantity)
';


--
-- Name: CONSTRAINT assem_temp_pointer_chk ON assembly_template_detail; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT assem_temp_pointer_chk ON public.assembly_template_detail IS 'Make sure only one of the pointer is used.';


--
-- Name: authenticated_senders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.authenticated_senders (
    auth_send_id character(16) NOT NULL,
    address_type character(1) NOT NULL,
    address character varying(50) NOT NULL,
    CONSTRAINT auth_send_type_chk CHECK (((address_type = 'D'::bpchar) OR (address_type = 'E'::bpchar)))
);


ALTER TABLE public.authenticated_senders OWNER TO postgres;

--
-- Name: TABLE authenticated_senders; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.authenticated_senders IS 'Domains or email addresses that are authenticated to send over email.  Note that this does not authenticate them.  This merely indicates what addresses are authenticated.';


--
-- Name: COLUMN authenticated_senders.address_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.authenticated_senders.address_type IS '(D)omain or (E)mail address';


--
-- Name: bank_account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bank_account (
    bank_account_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    bank_id character varying(10) NOT NULL,
    bank_name character varying(30) NOT NULL,
    bank_route character(9) NOT NULL,
    bank_account character varying(14) NOT NULL,
    account_type character(1) NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    CONSTRAINT bank_account_type_chk CHECK (((account_type = 'S'::bpchar) OR (account_type = 'C'::bpchar)))
);


ALTER TABLE public.bank_account OWNER TO postgres;

--
-- Name: COLUMN bank_account.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.bank_account.org_group_id IS 'What company or org group this account is associated with.';


--
-- Name: COLUMN bank_account.bank_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.bank_account.bank_id IS 'Code used to identify the bank account';


--
-- Name: COLUMN bank_account.account_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.bank_account.account_type IS 'Savings or Checking';


--
-- Name: COLUMN bank_account.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.bank_account.last_active_date IS 'Date record last active';


--
-- Name: bank_draft_batch; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bank_draft_batch (
    bank_draft_id character(16) NOT NULL,
    name character varying(100) NOT NULL,
    company_id character(16)
);


ALTER TABLE public.bank_draft_batch OWNER TO postgres;

--
-- Name: TABLE bank_draft_batch; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.bank_draft_batch IS 'Lists standard batches - not actual batches';


--
-- Name: bank_draft_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bank_draft_detail (
    bank_draft_id character(16) NOT NULL,
    person_id character(16) NOT NULL
);


ALTER TABLE public.bank_draft_detail OWNER TO postgres;

--
-- Name: TABLE bank_draft_detail; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.bank_draft_detail IS 'Detail for standard (not actual) batches';


--
-- Name: bank_draft_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bank_draft_history (
    bank_draft_history_id character(16) NOT NULL,
    bank_draft_id character(16) NOT NULL,
    date_made integer NOT NULL,
    receipt character varying(40) NOT NULL,
    CONSTRAINT bank_draft_hist_date_chk CHECK ((date_made > 20080101))
);


ALTER TABLE public.bank_draft_history OWNER TO postgres;

--
-- Name: TABLE bank_draft_history; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.bank_draft_history IS 'History of real bank drafts.  Receipts point back to this record.';


--
-- Name: benefit_answer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_answer (
    benefit_answer_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    benefit_question_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    string_answer character varying(1000),
    date_answer integer,
    numeric_answer double precision,
    CONSTRAINT benefit_ans_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.benefit_answer OWNER TO postgres;

--
-- Name: COLUMN benefit_answer.record_change_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_answer.record_change_type IS 'New / Modified / Deleted';


--
-- Name: COLUMN benefit_answer.record_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_answer.record_person_id IS 'Person who changed the record';


--
-- Name: benefit_answer_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_answer_h (
    history_id character(32) NOT NULL,
    benefit_answer_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    benefit_question_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    string_answer character varying(1000),
    date_answer integer,
    numeric_answer double precision,
    CONSTRAINT benefit_ansh_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.benefit_answer_h OWNER TO postgres;

--
-- Name: COLUMN benefit_answer_h.record_change_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_answer_h.record_change_type IS 'New / Modified / Deleted';


--
-- Name: COLUMN benefit_answer_h.record_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_answer_h.record_person_id IS 'Person who changed the record';


--
-- Name: benefit_change_reason_doc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_change_reason_doc (
    bcr_document_id character(16) NOT NULL,
    bcr_id character(16) NOT NULL,
    company_form_id character(16),
    instructions text
);


ALTER TABLE public.benefit_change_reason_doc OWNER TO postgres;

--
-- Name: benefit_class_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_class_join (
    benefit_class_id character(16) NOT NULL,
    benefit_config_id character(16) NOT NULL
);


ALTER TABLE public.benefit_class_join OWNER TO postgres;

--
-- Name: TABLE benefit_class_join; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.benefit_class_join IS 'Tells which benefit configs are in which benefit classes';


--
-- Name: benefit_config_cost; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_config_cost (
    benefit_config_cost_id character(16) NOT NULL,
    benefit_config_id character(16) NOT NULL,
    applies_to_status character(1) NOT NULL,
    org_group_id character(16) NOT NULL,
    first_active_date integer NOT NULL,
    last_active_date integer NOT NULL,
    age_calc_type character(1) DEFAULT 'N'::bpchar NOT NULL,
    fixed_employee_cost double precision DEFAULT 0 NOT NULL,
    fixed_employer_cost double precision DEFAULT 0 NOT NULL,
    benefit_amount double precision DEFAULT 0 NOT NULL,
    min_value double precision DEFAULT 0 NOT NULL,
    max_value double precision DEFAULT 0 NOT NULL,
    step_value double precision DEFAULT 0 NOT NULL,
    max_multiple_of_salary double precision DEFAULT 0 NOT NULL,
    rate_per_unit double precision DEFAULT 0 NOT NULL,
    rate_frequency smallint DEFAULT 12 NOT NULL,
    rate_relates_to character(1) DEFAULT 'B'::bpchar NOT NULL,
    salary_round_type character(1) DEFAULT 'D'::bpchar NOT NULL,
    salary_round_amount double precision DEFAULT 1 NOT NULL,
    benefit_round_type character(1) DEFAULT 'D'::bpchar NOT NULL,
    benefit_round_amount double precision DEFAULT 1 NOT NULL,
    cap_type character(1) DEFAULT 'L'::bpchar NOT NULL,
    guaranteed_issue_type character(1) DEFAULT 'N'::bpchar NOT NULL,
    guaranteed_issue_amount double precision DEFAULT 0 NOT NULL,
    CONSTRAINT ben_conf_cost_age_typ_chk CHECK (((age_calc_type = 'N'::bpchar) OR (age_calc_type = 'C'::bpchar) OR (age_calc_type = 'S'::bpchar) OR (age_calc_type = 'J'::bpchar))),
    CONSTRAINT benefit_confcost_gi_typ_chk CHECK (((guaranteed_issue_type = 'N'::bpchar) OR (guaranteed_issue_type = 'M'::bpchar) OR (guaranteed_issue_type = 'F'::bpchar))),
    CONSTRAINT benefit_config_cost_atstat_chk CHECK (((applies_to_status = 'A'::bpchar) OR (applies_to_status = 'C'::bpchar) OR (applies_to_status = 'S'::bpchar))),
    CONSTRAINT benefit_config_cost_rt_chk CHECK (((rate_relates_to = 'B'::bpchar) OR (rate_relates_to = 'S'::bpchar))),
    CONSTRAINT benefit_config_cost_srt_chk CHECK (((salary_round_type = 'N'::bpchar) OR (salary_round_type = 'U'::bpchar) OR (salary_round_type = 'D'::bpchar))),
    CONSTRAINT benefit_cost_config_brt_chk CHECK (((benefit_round_type = 'N'::bpchar) OR (benefit_round_type = 'U'::bpchar) OR (benefit_round_type = 'D'::bpchar)))
);


ALTER TABLE public.benefit_config_cost OWNER TO postgres;

--
-- Name: COLUMN benefit_config_cost.applies_to_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_config_cost.applies_to_status IS 'A = All status
C = COBRA
S = Status Table';


--
-- Name: COLUMN benefit_config_cost.age_calc_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_config_cost.age_calc_type IS 'What date is used to calculate the covered person''s age
N = Not applicable
C = Current date
S = Start of coverage date
J = age as of Jan 1st of Current Year';


--
-- Name: COLUMN benefit_config_cost.rate_frequency; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_config_cost.rate_frequency IS 'Divide year into how many units for calculation purposes.  In other words, all the other calculations will be monthly if this numer is 12, or yearly if this number is 1.';


--
-- Name: COLUMN benefit_config_cost.rate_relates_to; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_config_cost.rate_relates_to IS 'B = benefit amount
S = salary amount';


--
-- Name: COLUMN benefit_config_cost.salary_round_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_config_cost.salary_round_type IS 'N = nearest
U = up
D = down';


--
-- Name: COLUMN benefit_config_cost.benefit_round_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_config_cost.benefit_round_type IS 'N = nearest
U = up
D = down';


--
-- Name: COLUMN benefit_config_cost.cap_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_config_cost.cap_type IS 'The benefit is capped by the greater or lesser of multiple of salary vs. fixed cap
L = lesser of the two
G = greater of the two';


--
-- Name: COLUMN benefit_config_cost.guaranteed_issue_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_config_cost.guaranteed_issue_type IS 'N = Not applicable
M = Multiple of salary
F = Flat or fixed amount';


--
-- Name: COLUMN benefit_config_cost.guaranteed_issue_amount; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_config_cost.guaranteed_issue_amount IS 'Could be multiple or fixed amount';


--
-- Name: CONSTRAINT benefit_config_cost_rt_chk ON benefit_config_cost; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT benefit_config_cost_rt_chk ON public.benefit_config_cost IS '
';


--
-- Name: benefit_config_cost_age; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_config_cost_age (
    benefit_config_cost_age_id character(16) NOT NULL,
    benefit_config_cost_id character(16) NOT NULL,
    max_age smallint NOT NULL,
    ee_value double precision DEFAULT 0 NOT NULL,
    er_value double precision DEFAULT 0 NOT NULL,
    insurance_id character varying(15)
);


ALTER TABLE public.benefit_config_cost_age OWNER TO postgres;

--
-- Name: benefit_config_cost_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_config_cost_status (
    benefit_config_cost_status_id character(16) NOT NULL,
    benefit_config_cost_id character(16) NOT NULL,
    employee_status_id character(16) NOT NULL
);


ALTER TABLE public.benefit_config_cost_status OWNER TO postgres;

--
-- Name: benefit_dependency; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_dependency (
    benefit_dependency_id character(16) NOT NULL,
    required_benefit_id character(16) NOT NULL,
    benefit_id character(16) NOT NULL,
    required character(1) DEFAULT 'N'::bpchar NOT NULL,
    hidden character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT benefit_dep_auto_enroll_chk CHECK (((required = 'Y'::bpchar) OR (required = 'N'::bpchar))),
    CONSTRAINT benefit_dep_hidden_chk CHECK (((hidden = 'Y'::bpchar) OR (hidden = 'N'::bpchar)))
);


ALTER TABLE public.benefit_dependency OWNER TO postgres;

--
-- Name: TABLE benefit_dependency; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.benefit_dependency IS 'This table tells what benefits are required before a particular benefit may be selected.  If none of these records exost for a given benefit then the benefit has no restrictions.';


--
-- Name: COLUMN benefit_dependency.required; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_dependency.required IS 'Will auto enroll all of the employees of the required benefit in the dependent benefit';


--
-- Name: COLUMN benefit_dependency.hidden; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_dependency.hidden IS 'Is this benefit hidden from the enrollment wizard (Y/N)';


--
-- Name: benefit_document; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_document (
    benefit_document_id character(16) NOT NULL,
    benefit_id character(16) NOT NULL,
    company_form_id character(16),
    instructions text
);


ALTER TABLE public.benefit_document OWNER TO postgres;

--
-- Name: benefit_group_class_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_group_class_join (
    benefit_class_id character(16) NOT NULL,
    benefit_id character(16) NOT NULL
);


ALTER TABLE public.benefit_group_class_join OWNER TO postgres;

--
-- Name: benefit_question; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_question (
    benefit_question_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    benefit_id character(16) NOT NULL,
    question_order smallint NOT NULL,
    question character varying(1000) NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    data_type character(1) DEFAULT 'S'::bpchar NOT NULL,
    include_explanation character(1) DEFAULT 'N'::bpchar NOT NULL,
    applies_to_employee character(1) DEFAULT 'Y'::bpchar NOT NULL,
    applies_to_spouse character(1) DEFAULT 'Y'::bpchar NOT NULL,
    applies_to_child_other character(1) DEFAULT 'Y'::bpchar NOT NULL,
    explanation_text character varying(500),
    internal_id character varying(12),
    CONSTRAINT benefit_ques_data_chk CHECK (((data_type = 'N'::bpchar) OR (data_type = 'D'::bpchar) OR (data_type = 'S'::bpchar) OR (data_type = 'Y'::bpchar) OR (data_type = 'L'::bpchar))),
    CONSTRAINT benefit_ques_qchild_chk CHECK (((applies_to_child_other = 'Y'::bpchar) OR (applies_to_child_other = 'N'::bpchar))),
    CONSTRAINT benefit_ques_qspou_chk CHECK (((applies_to_spouse = 'Y'::bpchar) OR (applies_to_spouse = 'N'::bpchar))),
    CONSTRAINT benefit_ques_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT benefit_question_expl_chk CHECK (((include_explanation = 'Y'::bpchar) OR (include_explanation = 'N'::bpchar) OR (include_explanation = 'R'::bpchar))),
    CONSTRAINT benefit_question_qemp_chk CHECK (((applies_to_employee = 'Y'::bpchar) OR (applies_to_employee = 'N'::bpchar)))
);


ALTER TABLE public.benefit_question OWNER TO postgres;

--
-- Name: COLUMN benefit_question.record_change_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question.record_change_type IS 'New / Modified / Deleted';


--
-- Name: COLUMN benefit_question.record_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question.record_person_id IS 'Person who changed the record';


--
-- Name: COLUMN benefit_question.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question.last_active_date IS 'Last date this record can be used in a new association.  0 means no end.';


--
-- Name: COLUMN benefit_question.data_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question.data_type IS 'Numeric (double)
Date (YYYYMMDD)
String
Yes/no/unknown (Yes/No/Unknown in field)';


--
-- Name: COLUMN benefit_question.include_explanation; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question.include_explanation IS 'If employee answers ''Yes'' on a yes/no question, this allows them to explain their answer. 
N = no explanation, 
Y = explanation optional, 
R = explanation required';


--
-- Name: COLUMN benefit_question.applies_to_employee; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question.applies_to_employee IS 'Does the employee have to answer this question? ''Y'' or ''N''';


--
-- Name: COLUMN benefit_question.applies_to_spouse; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question.applies_to_spouse IS 'Does the spouse have to answer this question? ''Y'' or ''N''';


--
-- Name: COLUMN benefit_question.applies_to_child_other; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question.applies_to_child_other IS 'Does the child/other have to answer this question? ''Y'' or ''N''';


--
-- Name: COLUMN benefit_question.explanation_text; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question.explanation_text IS 'allows nulls, only displayed if include_explanation = ''Y''';


--
-- Name: COLUMN benefit_question.internal_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question.internal_id IS 'Internal Question ID for mapping questions/answers to exports';


--
-- Name: benefit_question_choice; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_question_choice (
    benefit_question_choice_id character(16) NOT NULL,
    benefit_question_id character(16) NOT NULL,
    description character varying(60) NOT NULL
);


ALTER TABLE public.benefit_question_choice OWNER TO postgres;

--
-- Name: benefit_question_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_question_h (
    history_id character(32) NOT NULL,
    benefit_question_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    benefit_id character(16) NOT NULL,
    question_order smallint NOT NULL,
    question character varying(1000) NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    data_type character(1) DEFAULT 'S'::bpchar NOT NULL,
    include_explanation character(1) DEFAULT 'N'::bpchar NOT NULL,
    applies_to_employee character(1) DEFAULT 'Y'::bpchar NOT NULL,
    applies_to_spouse character(1) DEFAULT 'N'::bpchar NOT NULL,
    applies_to_child_other character(1) DEFAULT 'N'::bpchar NOT NULL,
    explanation_text character varying(500),
    internal_id character varying(12),
    CONSTRAINT benefit_quesh_data_chk CHECK (((data_type = 'N'::bpchar) OR (data_type = 'D'::bpchar) OR (data_type = 'S'::bpchar) OR (data_type = 'Y'::bpchar) OR (data_type = 'L'::bpchar))),
    CONSTRAINT benefit_quesh_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT benefith_ques_qchild_chk CHECK (((applies_to_child_other = 'Y'::bpchar) OR (applies_to_child_other = 'N'::bpchar))),
    CONSTRAINT benefith_ques_qspou_chk CHECK (((applies_to_spouse = 'Y'::bpchar) OR (applies_to_spouse = 'N'::bpchar))),
    CONSTRAINT benefith_question_expl_chk CHECK (((include_explanation = 'Y'::bpchar) OR (include_explanation = 'N'::bpchar) OR (include_explanation = 'R'::bpchar))),
    CONSTRAINT benefith_question_qemp_chk CHECK (((applies_to_employee = 'Y'::bpchar) OR (applies_to_employee = 'N'::bpchar)))
);


ALTER TABLE public.benefit_question_h OWNER TO postgres;

--
-- Name: COLUMN benefit_question_h.record_change_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question_h.record_change_type IS 'New / Modified / Deleted';


--
-- Name: COLUMN benefit_question_h.record_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question_h.record_person_id IS 'Person who changed the record';


--
-- Name: COLUMN benefit_question_h.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question_h.last_active_date IS 'Last date this record can be used in a new association.  0 means no end.';


--
-- Name: COLUMN benefit_question_h.data_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.benefit_question_h.data_type IS 'Numeric (double)
Date (YYYYMMDD)
String
Yes/no/unknown (Yes/No/Unknown in field)';


--
-- Name: benefit_restriction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefit_restriction (
    benefit_restriction_id character(16) NOT NULL,
    bcr_id character(16) NOT NULL,
    benefit_cat_id character(16) NOT NULL
);


ALTER TABLE public.benefit_restriction OWNER TO postgres;

--
-- Name: TABLE benefit_restriction; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.benefit_restriction IS 'When this bcr_id is selected, the specified category does not allow user to change benefits in this category for the specified change reason';


--
-- Name: change_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.change_log (
    change_id bigint NOT NULL,
    change_when timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    change_person_id character(16) NOT NULL,
    person_id character(16),
    project_id character(16),
    client_id character(16),
    vendor_id character(16),
    table_changed character varying(60),
    column_changed character varying(60),
    old_value character varying(40),
    new_value character varying(40),
    description character varying(80)
);


ALTER TABLE public.change_log OWNER TO postgres;

--
-- Name: COLUMN change_log.change_when; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.change_log.change_when IS 'When was the change made';


--
-- Name: COLUMN change_log.change_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.change_log.change_person_id IS 'The person making the change';


--
-- Name: COLUMN change_log.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.change_log.person_id IS 'If the change is related to a person, this is the person the change is about';


--
-- Name: COLUMN change_log.project_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.change_log.project_id IS 'If the change is related to a project, this is the related project';


--
-- Name: COLUMN change_log.client_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.change_log.client_id IS 'If the change is related to a client, this is the client the change is related to';


--
-- Name: COLUMN change_log.vendor_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.change_log.vendor_id IS 'If the change is related to a vendor, this is the vendor the change is related to';


--
-- Name: change_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.change_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.change_log_id_seq OWNER TO postgres;

--
-- Name: change_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.change_log_id_seq OWNED BY public.change_log.change_id;


--
-- Name: client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client (
    org_group_id character(16) NOT NULL,
    inactive_date integer DEFAULT 0 NOT NULL,
    billing_rate real DEFAULT 0 NOT NULL,
    contract_date integer DEFAULT 0 NOT NULL,
    dflt_sales_acct character(16),
    dflt_ar_acct character(16),
    company_id character(16) NOT NULL,
    client_status_id character(16) NOT NULL,
    status_comments text,
    last_contact_date integer DEFAULT 0 NOT NULL,
    payment_terms smallint DEFAULT 0 NOT NULL,
    vendor_number character varying(20),
    default_project_code character varying(12),
    copy_pictures_to_disk character(1) DEFAULT 'N'::bpchar NOT NULL,
    picture_disk_path character varying(100),
    copy_only_external character(1) DEFAULT 'Y'::bpchar NOT NULL,
    copy_inactive_projects character(1) DEFAULT 'Y'::bpchar NOT NULL,
    CONSTRAINT client_copy_external_chk CHECK (((copy_only_external = 'Y'::bpchar) OR (copy_only_external = 'N'::bpchar))),
    CONSTRAINT client_copy_inactive_chk CHECK (((copy_inactive_projects = 'Y'::bpchar) OR (copy_inactive_projects = 'N'::bpchar))),
    CONSTRAINT copy_pictures_check CHECK (((copy_pictures_to_disk = 'Y'::bpchar) OR (copy_pictures_to_disk = 'N'::bpchar)))
);


ALTER TABLE public.client OWNER TO postgres;

--
-- Name: TABLE client; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.client IS 'This table stores information specific to a client company';


--
-- Name: COLUMN client.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client.org_group_id IS 'the main org-group this client is associated with';


--
-- Name: COLUMN client.inactive_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client.inactive_date IS 'The date the client became no longer active';


--
-- Name: COLUMN client.billing_rate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client.billing_rate IS 'The default billing rate for the client';


--
-- Name: COLUMN client.contract_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client.contract_date IS 'The date the contract with the client was signed, or that they became active.';


--
-- Name: COLUMN client.dflt_sales_acct; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client.dflt_sales_acct IS 'Default Sales GL Account';


--
-- Name: COLUMN client.dflt_ar_acct; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client.dflt_ar_acct IS 'Default Accounts Receivable GL Account';


--
-- Name: COLUMN client.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client.company_id IS 'The Arahant user company this client company is associated with.';


--
-- Name: COLUMN client.last_contact_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client.last_contact_date IS 'Date last contact with client - not last attempted contact';


--
-- Name: COLUMN client.payment_terms; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client.payment_terms IS 'Number of days';


--
-- Name: COLUMN client.vendor_number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client.vendor_number IS 'What is the client''s ID for us - our vendor number to them';


--
-- Name: COLUMN client.default_project_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client.default_project_code IS 'Defaults into project.project_code';


--
-- Name: COLUMN client.picture_disk_path; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client.picture_disk_path IS 'This path is used to store images that the client can access';


--
-- Name: client_contact; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client_contact (
    person_id character(16) NOT NULL,
    email_invoice character(1) DEFAULT 'N'::bpchar NOT NULL,
    contact_type smallint DEFAULT 5 NOT NULL,
    CONSTRAINT client_contact_email_chk CHECK (((email_invoice = 'Y'::bpchar) OR (email_invoice = 'N'::bpchar))),
    CONSTRAINT client_contact_type_chk CHECK (((contact_type >= 1) AND (contact_type <= 5)))
);


ALTER TABLE public.client_contact OWNER TO postgres;

--
-- Name: TABLE client_contact; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.client_contact IS 'This table is for storing information specific to a client person.';


--
-- Name: COLUMN client_contact.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client_contact.person_id IS 'Key to reference the person table.';


--
-- Name: COLUMN client_contact.email_invoice; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client_contact.email_invoice IS 'Are invoices emailed to this contact?';


--
-- Name: COLUMN client_contact.contact_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client_contact.contact_type IS '1 = decision maker
2 = department head
3 = user
4 = consultant / other employee
5 = Unknown';


--
-- Name: client_item_inventory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client_item_inventory (
    client_item_inventory_id character(36) NOT NULL,
    quantity_on_hand integer DEFAULT 0 NOT NULL,
    usable_quantity_on_hand integer DEFAULT 0 NOT NULL,
    location character varying(50),
    project_id character(16) NOT NULL,
    item_category character varying(20),
    description character varying(150),
    model character varying(15),
    vendor character varying(45),
    vendor_item_code character varying(25),
    client_item_code character varying(12),
    unit_of_measure character(1) NOT NULL,
    quantity_discarded integer DEFAULT 0 NOT NULL,
    CONSTRAINT client_item_inventory_unit_chk CHECK (((unit_of_measure = 'E'::bpchar) OR (unit_of_measure = 'C'::bpchar)))
);


ALTER TABLE public.client_item_inventory OWNER TO postgres;

--
-- Name: COLUMN client_item_inventory.client_item_inventory_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client_item_inventory.client_item_inventory_id IS 'UUID';


--
-- Name: COLUMN client_item_inventory.unit_of_measure; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client_item_inventory.unit_of_measure IS 'Each or Case';


--
-- Name: client_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client_status (
    client_status_id character(16) NOT NULL,
    code character varying(20) NOT NULL,
    seqno smallint NOT NULL,
    description character varying(100),
    last_active_date integer DEFAULT 0 NOT NULL,
    active character(1) DEFAULT 'Y'::bpchar NOT NULL,
    company_id character(16),
    CONSTRAINT client_status_active_chk CHECK (((active = 'Y'::bpchar) OR (active = 'N'::bpchar)))
);


ALTER TABLE public.client_status OWNER TO postgres;

--
-- Name: COLUMN client_status.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client_status.last_active_date IS 'Last date this record can be used in a new association.  0 means no end.';


--
-- Name: COLUMN client_status.active; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client_status.active IS 'Is this an active client (Y/N)?';


--
-- Name: COLUMN client_status.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.client_status.company_id IS 'Company this record is associated with.  NULL means all companies.';


--
-- Name: company_base; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_base (
    org_group_id character(16) NOT NULL,
    org_group_type integer NOT NULL,
    federal_employer_id character varying(11),
    com_url character varying(128),
    com_login character varying(50),
    com_password character varying(50),
    com_directory character varying(256),
    encryption_key_id character varying(80),
    application_sender_id character varying(16),
    application_receiver_id character varying(16),
    interchange_sender_id character varying(16),
    interchange_receiver_id character varying(16),
    public_encryption_key character varying(4000),
    arahant_url character varying(120),
    bill_to_name character varying(60),
    days_to_send character(7) DEFAULT 'NNNNNNN'::bpchar NOT NULL,
    time_to_send smallint DEFAULT 0 NOT NULL,
    edi_activated character(1) DEFAULT 'N'::bpchar NOT NULL,
    edi_file_type character(1) DEFAULT 'U'::bpchar NOT NULL,
    edi_file_status character(1) DEFAULT 'U'::bpchar NOT NULL,
    CONSTRAINT company_base_edi_act_chk CHECK (((edi_activated = 'Y'::bpchar) OR (edi_activated = 'N'::bpchar))),
    CONSTRAINT company_base_file_status_chk CHECK (((edi_file_status = 'U'::bpchar) OR (edi_file_status = 'P'::bpchar) OR (edi_file_status = 'T'::bpchar))),
    CONSTRAINT company_base_file_type_chk CHECK (((edi_file_type = 'U'::bpchar) OR (edi_file_type = 'F'::bpchar) OR (edi_file_type = 'C'::bpchar))),
    CONSTRAINT org_group_type_chk CHECK (((org_group_type = 1) OR (org_group_type = 2) OR (org_group_type = 4) OR (org_group_type = 8) OR (org_group_type = 16)))
);


ALTER TABLE public.company_base OWNER TO postgres;

--
-- Name: TABLE company_base; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.company_base IS 'This table stores common information for all company types, user''s company, client companies, and vendor companies.';


--
-- Name: COLUMN company_base.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.org_group_id IS 'key to the group table';


--
-- Name: COLUMN company_base.org_group_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.org_group_type IS '1=Company
2=Client
4=Vendor
8=Prospect
16-Agency';


--
-- Name: COLUMN company_base.federal_employer_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.federal_employer_id IS 'Federal employer id or SSN of owner for this company';


--
-- Name: COLUMN company_base.com_url; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.com_url IS 'For EDI';


--
-- Name: COLUMN company_base.com_login; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.com_login IS 'For EDI';


--
-- Name: COLUMN company_base.com_password; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.com_password IS 'For EDI';


--
-- Name: COLUMN company_base.com_directory; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.com_directory IS 'For EDI';


--
-- Name: COLUMN company_base.encryption_key_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.encryption_key_id IS 'For EDI';


--
-- Name: COLUMN company_base.application_sender_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.application_sender_id IS 'For EDI';


--
-- Name: COLUMN company_base.application_receiver_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.application_receiver_id IS 'For EDI';


--
-- Name: COLUMN company_base.interchange_sender_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.interchange_sender_id IS 'For EDI';


--
-- Name: COLUMN company_base.interchange_receiver_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.interchange_receiver_id IS 'For EDI';


--
-- Name: COLUMN company_base.arahant_url; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.arahant_url IS 'The URL of the Arahant instance for this company.  This is used for Arahant-to-Arahant Web Service communications.';


--
-- Name: COLUMN company_base.days_to_send; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.days_to_send IS 'Each position is Yes / No for Sunday through Saturday';


--
-- Name: COLUMN company_base.time_to_send; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.time_to_send IS 'HHMM using a 24 hour clock';


--
-- Name: COLUMN company_base.edi_activated; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.edi_activated IS 'Yes or No';


--
-- Name: COLUMN company_base.edi_file_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.edi_file_type IS 'U = Unknown
F = Full file
C = Change file';


--
-- Name: COLUMN company_base.edi_file_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_base.edi_file_status IS 'U = Unknown
P = Production
T = Test';


--
-- Name: company_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_detail (
    org_group_id character(16) NOT NULL,
    accounting_basis character(1) NOT NULL,
    billing_rate real DEFAULT 0 NOT NULL,
    cash_account_id character(16),
    ar_account_id character(16),
    employee_advance_account_id character(16),
    eeo1_id character varying(7),
    dun_bradstreet_num character varying(9),
    naics character varying(7),
    time_off_auto_accrual character(1) DEFAULT 'N'::bpchar NOT NULL,
    max_employees integer DEFAULT 0 NOT NULL,
    logo bytea,
    logo_source character varying(255),
    logo_extension character varying(10),
    fiscal_beginning_month smallint DEFAULT 1 NOT NULL,
    email_out_type smallint DEFAULT 0 NOT NULL,
    email_out_authentication character(1) DEFAULT 'N'::bpchar NOT NULL,
    email_out_host character varying(30),
    email_out_domain character varying(40),
    email_out_port integer DEFAULT 25 NOT NULL,
    email_out_encryption character(1) DEFAULT 'N'::bpchar NOT NULL,
    email_out_user_id character varying(50),
    email_out_user_pw character varying(30),
    email_out_from_name character varying(50),
    email_out_from_email character varying(50),
    CONSTRAINT accounting_basis_check CHECK (((accounting_basis = 'C'::bpchar) OR (accounting_basis = 'A'::bpchar))),
    CONSTRAINT company_detail_fiscal_begmon_chk CHECK (((fiscal_beginning_month >= 1) AND (fiscal_beginning_month <= 12))),
    CONSTRAINT company_detail_time_off_chk CHECK (((time_off_auto_accrual = 'N'::bpchar) OR (time_off_auto_accrual = 'Y'::bpchar))),
    CONSTRAINT compdet_eo_auth_chk CHECK (((email_out_authentication = 'N'::bpchar) OR (email_out_authentication = 'P'::bpchar))),
    CONSTRAINT compdet_eo_encrypt_chk CHECK (((email_out_encryption = 'N'::bpchar) OR (email_out_encryption = 'S'::bpchar) OR (email_out_encryption = 'T'::bpchar))),
    CONSTRAINT compdet_eo_type_chk CHECK (((email_out_type = 0) OR (email_out_type = 1) OR (email_out_type = 2)))
);


ALTER TABLE public.company_detail OWNER TO postgres;

--
-- Name: TABLE company_detail; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.company_detail IS 'Information specific to the Company';


--
-- Name: COLUMN company_detail.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.org_group_id IS 'detail information about a user company';


--
-- Name: COLUMN company_detail.accounting_basis; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.accounting_basis IS 'Cash or Accural Basis';


--
-- Name: COLUMN company_detail.employee_advance_account_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.employee_advance_account_id IS 'AR like account for employee advances like COBRA billing';


--
-- Name: COLUMN company_detail.time_off_auto_accrual; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.time_off_auto_accrual IS 'Y means that time off requests automatically generate time off accruals at the time they are accepted. ';


--
-- Name: COLUMN company_detail.max_employees; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.max_employees IS 'Max active employees allowed for this client.  Zero means no limit.';


--
-- Name: COLUMN company_detail.fiscal_beginning_month; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.fiscal_beginning_month IS 'Month number for beginning of fiscal year';


--
-- Name: COLUMN company_detail.email_out_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.email_out_type IS '0 = generic
1 = GMail
2 = Exchange';


--
-- Name: COLUMN company_detail.email_out_authentication; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.email_out_authentication IS 'N = None
P = Password';


--
-- Name: COLUMN company_detail.email_out_domain; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.email_out_domain IS 'For Exchange';


--
-- Name: COLUMN company_detail.email_out_encryption; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.email_out_encryption IS 'N = none
S = SSL
T = TLS';


--
-- Name: COLUMN company_detail.email_out_user_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.email_out_user_id IS 'User ID used to login to host';


--
-- Name: COLUMN company_detail.email_out_user_pw; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.email_out_user_pw IS 'Password used to log into host';


--
-- Name: COLUMN company_detail.email_out_from_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.email_out_from_name IS 'User name shown as the source of the email sent';


--
-- Name: COLUMN company_detail.email_out_from_email; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_detail.email_out_from_email IS 'From email address used when sending email';


--
-- Name: company_form; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_form (
    company_form_id character(16) NOT NULL,
    company_id character(16),
    form_type_id character(16) NOT NULL,
    comments character varying(255) NOT NULL,
    source character varying(255),
    form bytea NOT NULL,
    file_name_extension character varying(10) NOT NULL,
    first_active_date integer DEFAULT 0 NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    electronic_signature character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT company_form_sign_chk CHECK (((electronic_signature = 'Y'::bpchar) OR (electronic_signature = 'N'::bpchar)))
);


ALTER TABLE public.company_form OWNER TO postgres;

--
-- Name: COLUMN company_form.source; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_form.source IS 'The full path name to the file';


--
-- Name: COLUMN company_form.electronic_signature; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_form.electronic_signature IS 'Does the form support electronic signatures?  Yes / No
If so, it must be a PDF file.';


--
-- Name: company_form_folder; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_form_folder (
    folder_id character(16) NOT NULL,
    folder_name character varying(80) NOT NULL,
    company_id character(16),
    parent_folder_id character(16)
);


ALTER TABLE public.company_form_folder OWNER TO postgres;

--
-- Name: company_form_folder_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_form_folder_join (
    form_id character(16) NOT NULL,
    folder_id character(16) NOT NULL
);


ALTER TABLE public.company_form_folder_join OWNER TO postgres;

--
-- Name: company_form_org_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_form_org_join (
    folder_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL
);


ALTER TABLE public.company_form_org_join OWNER TO postgres;

--
-- Name: company_question; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_question (
    company_ques_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    question character varying(120) NOT NULL,
    company_id character(16),
    last_active_date integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.company_question OWNER TO postgres;

--
-- Name: COLUMN company_question.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_question.company_id IS 'Company this record is associated with.  NULL means all companies.';


--
-- Name: COLUMN company_question.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.company_question.last_active_date IS 'Last date this record can be used in a new association.  0 means no end.';


--
-- Name: company_question_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_question_detail (
    company_ques_det_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    company_ques_id character(16) NOT NULL,
    response character varying(2000) NOT NULL,
    when_added timestamp with time zone NOT NULL
);


ALTER TABLE public.company_question_detail OWNER TO postgres;

--
-- Name: contact_question; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contact_question (
    contact_question_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    question character varying(120) NOT NULL,
    company_id character(16),
    last_active_date integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.contact_question OWNER TO postgres;

--
-- Name: COLUMN contact_question.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.contact_question.company_id IS 'Company this record is associated with.  NULL means all companies.';


--
-- Name: COLUMN contact_question.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.contact_question.last_active_date IS 'Last date this record can be used in a new association.  0 means no end.';


--
-- Name: contact_question_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contact_question_detail (
    contact_question_det_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    contact_question_id character(16) NOT NULL,
    response character varying(2000) NOT NULL,
    when_added timestamp with time zone NOT NULL
);


ALTER TABLE public.contact_question_detail OWNER TO postgres;

--
-- Name: hr_empl_status_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_empl_status_history (
    status_hist_id character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    effective_date integer DEFAULT 0 NOT NULL,
    status_id character(16) NOT NULL,
    notes character varying(256),
    record_change_date timestamp with time zone,
    CONSTRAINT hr_status_hist_date_chk CHECK ((effective_date > 0))
);


ALTER TABLE public.hr_empl_status_history OWNER TO postgres;

--
-- Name: TABLE hr_empl_status_history; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_empl_status_history IS 'History of employee status';


--
-- Name: current_employee_status; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.current_employee_status AS
 SELECT esh.employee_id,
    esh.effective_date,
    esh.status_id,
    esh.notes,
    esh.record_change_date
   FROM ( SELECT hr_empl_status_history.status_hist_id,
            hr_empl_status_history.employee_id,
            hr_empl_status_history.effective_date,
            hr_empl_status_history.status_id,
            hr_empl_status_history.notes,
            hr_empl_status_history.record_change_date,
            row_number() OVER (PARTITION BY hr_empl_status_history.employee_id ORDER BY hr_empl_status_history.effective_date DESC) AS rn
           FROM public.hr_empl_status_history
          WHERE ((hr_empl_status_history.effective_date)::numeric <= to_number(to_char((CURRENT_DATE)::timestamp with time zone, 'YYYYMMDD'::text), '99999999'::text))) esh
  WHERE (esh.rn = 1);


ALTER TABLE public.current_employee_status OWNER TO postgres;

--
-- Name: hr_wage; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_wage (
    wage_id character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    wage_type_id character(16) NOT NULL,
    wage_amount double precision NOT NULL,
    effective_date integer NOT NULL,
    position_id character(16),
    notes character varying(256),
    CONSTRAINT hr_wage_date_chk CHECK ((effective_date > 19000101)),
    CONSTRAINT hr_wage_usage_chk CHECK (((wage_type_id IS NOT NULL) OR (position_id IS NOT NULL))),
    CONSTRAINT hr_wage_wage_chk CHECK (((wage_type_id IS NOT NULL) OR (wage_amount < (0.01)::double precision)))
);


ALTER TABLE public.hr_wage OWNER TO postgres;

--
-- Name: COLUMN hr_wage.wage_amount; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_wage.wage_amount IS 'Wage in dollars per period';


--
-- Name: COLUMN hr_wage.effective_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_wage.effective_date IS 'Date wage became effective';


--
-- Name: COLUMN hr_wage.position_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_wage.position_id IS 'Position';


--
-- Name: current_employee_wage; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.current_employee_wage AS
 SELECT w.employee_id,
    w.wage_type_id,
    w.wage_amount,
    w.effective_date,
    w.position_id,
    w.notes
   FROM ( SELECT hr_wage.wage_id,
            hr_wage.employee_id,
            hr_wage.wage_type_id,
            hr_wage.wage_amount,
            hr_wage.effective_date,
            hr_wage.position_id,
            hr_wage.notes,
            row_number() OVER (PARTITION BY hr_wage.employee_id ORDER BY hr_wage.effective_date DESC) AS rn
           FROM public.hr_wage
          WHERE ((hr_wage.effective_date)::numeric <= to_number(to_char((CURRENT_DATE)::timestamp with time zone, 'YYYYMMDD'::text), '99999999'::text))) w
  WHERE (w.rn = 1);


ALTER TABLE public.current_employee_wage OWNER TO postgres;

--
-- Name: delivery; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.delivery (
    delivery_id character(36) NOT NULL,
    who_accepted_delivery character varying(30),
    location character varying(50),
    project_po_id character(36) NOT NULL,
    delivery_date integer NOT NULL,
    delivery_time integer NOT NULL
);


ALTER TABLE public.delivery OWNER TO postgres;

--
-- Name: COLUMN delivery.delivery_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.delivery.delivery_id IS 'UUID';


--
-- Name: COLUMN delivery.location; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.delivery.location IS 'Where on client site delivery is located (if it''s all in one place)';


--
-- Name: COLUMN delivery.project_po_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.delivery.project_po_id IS 'UUID';


--
-- Name: COLUMN delivery.delivery_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.delivery.delivery_date IS 'YYYYMMDD';


--
-- Name: COLUMN delivery.delivery_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.delivery.delivery_time IS 'HHMM (-1 means not present)';


--
-- Name: delivery_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.delivery_detail (
    delivery_detail_id character(36) NOT NULL,
    project_po_detail_id character(36) NOT NULL,
    delivery_id character(36) NOT NULL,
    units_received integer DEFAULT 0 NOT NULL,
    condition character varying(50),
    usable character(1) DEFAULT 'Y'::bpchar NOT NULL,
    location character varying(50),
    CONSTRAINT delivery_detail_usable_chk CHECK (((usable = 'Y'::bpchar) OR (usable = 'N'::bpchar)))
);


ALTER TABLE public.delivery_detail OWNER TO postgres;

--
-- Name: COLUMN delivery_detail.delivery_detail_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.delivery_detail.delivery_detail_id IS 'UUID';


--
-- Name: COLUMN delivery_detail.project_po_detail_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.delivery_detail.project_po_detail_id IS 'UUID';


--
-- Name: COLUMN delivery_detail.delivery_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.delivery_detail.delivery_id IS 'UUID';


--
-- Name: COLUMN delivery_detail.usable; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.delivery_detail.usable IS 'Yes or No';


--
-- Name: COLUMN delivery_detail.location; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.delivery_detail.location IS 'Location if other than the whole delivery';


--
-- Name: drc_form_event; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.drc_form_event (
    form_event_id integer NOT NULL,
    event_type smallint NOT NULL,
    person_id_causing_event character(16) NOT NULL,
    event_date timestamp with time zone NOT NULL,
    person_id character(16) NOT NULL,
    employee_id character(16),
    transaction_type smallint NOT NULL,
    CONSTRAINT drc_form_event_event_chk CHECK (((event_type >= 1) AND (event_type <= 9))),
    CONSTRAINT drc_form_event_trans_chk CHECK (((transaction_type >= 1) AND (transaction_type <= 5)))
);


ALTER TABLE public.drc_form_event OWNER TO postgres;

--
-- Name: COLUMN drc_form_event.event_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.drc_form_event.event_type IS '1. Enroll employee in benefits
2. Modify employee - any info basic tab or address tab
3. Terminate employee
4. Cancel dependent benefits
5. Add dependent
6. Enrollment wizard
7. Edit dependents
8. Cancel benefits
9. Inactive employee selects benefits (ie COBRA)';


--
-- Name: COLUMN drc_form_event.person_id_causing_event; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.drc_form_event.person_id_causing_event IS 'The person logged in when the event occurred (i.e. caused the event).';


--
-- Name: COLUMN drc_form_event.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.drc_form_event.person_id IS 'The person the event is about.';


--
-- Name: COLUMN drc_form_event.transaction_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.drc_form_event.transaction_type IS '1 add
2 modify
3 cancel
4 resinstate
5 transfer';


--
-- Name: drc_import_benefit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.drc_import_benefit (
    imported_benefit_id character(16) NOT NULL,
    company_id character(16) NOT NULL,
    benefit_name character varying(30) NOT NULL,
    import_file_type_id character(16) NOT NULL
);


ALTER TABLE public.drc_import_benefit OWNER TO postgres;

--
-- Name: COLUMN drc_import_benefit.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.drc_import_benefit.company_id IS 'The company this benefit is associated to';


--
-- Name: drc_import_benefit_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.drc_import_benefit_join (
    import_benefit_join_id character(16) NOT NULL,
    enrollee_id character(16) NOT NULL,
    import_benefit_id character(16) NOT NULL,
    subscriber_id character(16) NOT NULL,
    coverage_start_date integer NOT NULL,
    coverage_end_date integer NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    CONSTRAINT drc_imp_ben_jn_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.drc_import_benefit_join OWNER TO postgres;

--
-- Name: drc_import_benefit_join_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.drc_import_benefit_join_h (
    history_id character(32) NOT NULL,
    import_benefit_join_id character(16) NOT NULL,
    enrollee_id character(16) NOT NULL,
    import_benefit_id character(16) NOT NULL,
    subscriber_id character(16) NOT NULL,
    coverage_start_date integer NOT NULL,
    coverage_end_date integer NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    CONSTRAINT drc_impbenjn_h_rct CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.drc_import_benefit_join_h OWNER TO postgres;

--
-- Name: COLUMN drc_import_benefit_join_h.history_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.drc_import_benefit_join_h.history_id IS 'A GUID';


--
-- Name: drc_import_enrollee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.drc_import_enrollee (
    enrollee_id character(16) NOT NULL,
    company_id character(16) NOT NULL,
    lname character varying(30),
    fname character varying(30),
    mname character varying(30),
    relationship character varying(30),
    ssn character varying(11),
    street1 character varying(60),
    street2 character varying(60),
    city character varying(60),
    state character varying(2),
    zip character varying(10),
    dob integer NOT NULL,
    import_file_type_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    CONSTRAINT drc_imoenr_h_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.drc_import_enrollee OWNER TO postgres;

--
-- Name: COLUMN drc_import_enrollee.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.drc_import_enrollee.company_id IS 'This is the company the enrollee is associated with';


--
-- Name: drc_import_enrollee_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.drc_import_enrollee_h (
    history_id character(32) NOT NULL,
    enrollee_id character(16) NOT NULL,
    company_id character(16) NOT NULL,
    lname character varying(30),
    fname character varying(30),
    mname character varying(30),
    relationship character varying(30),
    ssn character varying(11),
    street1 character varying(60),
    street2 character varying(60),
    city character varying(60),
    state character varying(2),
    zip character varying(10),
    dob integer NOT NULL,
    import_file_type_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    CONSTRAINT drc_imp_enr_h_rtc CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.drc_import_enrollee_h OWNER TO postgres;

--
-- Name: COLUMN drc_import_enrollee_h.history_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.drc_import_enrollee_h.history_id IS 'A GUID';


--
-- Name: dynace_mutex; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dynace_mutex (
    tag character varying(255) NOT NULL,
    data character varying(255),
    lastupdate timestamp with time zone NOT NULL
);


ALTER TABLE public.dynace_mutex OWNER TO postgres;

--
-- Name: TABLE dynace_mutex; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.dynace_mutex IS 'This table is used to control access to the dyance_sequence table';


--
-- Name: COLUMN dynace_mutex.tag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.dynace_mutex.tag IS 'tag for reference - usually a table name.';


--
-- Name: COLUMN dynace_mutex.data; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.dynace_mutex.data IS 'TODO: What is this used for?';


--
-- Name: COLUMN dynace_mutex.lastupdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.dynace_mutex.lastupdate IS 'The time this table was last updated.';


--
-- Name: dynace_sequence; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dynace_sequence (
    tag character varying(255) NOT NULL,
    lastvalue integer NOT NULL
);


ALTER TABLE public.dynace_sequence OWNER TO postgres;

--
-- Name: TABLE dynace_sequence; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.dynace_sequence IS 'this table is used to generate primary keys ';


--
-- Name: COLUMN dynace_sequence.tag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.dynace_sequence.tag IS 'A name for reference, usually a table name.';


--
-- Name: COLUMN dynace_sequence.lastvalue; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.dynace_sequence.lastvalue IS 'The last value in the sequence used.';


--
-- Name: e_signature; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.e_signature (
    e_signature_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    time_signed timestamp with time zone NOT NULL,
    address_ip character varying(30) NOT NULL,
    form_type character varying(40) NOT NULL,
    xml_sum character(32),
    xml_data text,
    form_data bytea,
    form_sum character(32),
    signature character varying(50),
    sig_date character varying(25)
);


ALTER TABLE public.e_signature OWNER TO postgres;

--
-- Name: COLUMN e_signature.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.e_signature.person_id IS 'Person who signed electronically';


--
-- Name: COLUMN e_signature.time_signed; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.e_signature.time_signed IS '(System) Date and time document signed';


--
-- Name: COLUMN e_signature.address_ip; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.e_signature.address_ip IS 'IP address of signer';


--
-- Name: COLUMN e_signature.form_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.e_signature.form_type IS 'String designator denoting the type of form being signed';


--
-- Name: COLUMN e_signature.xml_sum; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.e_signature.xml_sum IS 'md5sum of xml_data without any dashes';


--
-- Name: COLUMN e_signature.xml_data; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.e_signature.xml_data IS 'Data that has been signed in XML format';


--
-- Name: COLUMN e_signature.form_data; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.e_signature.form_data IS 'PDF file, for example';


--
-- Name: COLUMN e_signature.form_sum; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.e_signature.form_sum IS 'Md5sum of form_data';


--
-- Name: COLUMN e_signature.signature; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.e_signature.signature IS 'Signature typed in by user';


--
-- Name: COLUMN e_signature.sig_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.e_signature.sig_date IS 'Date signed typed in by user';


--
-- Name: edi_transaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.edi_transaction (
    edi_transaction_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    tscn integer NOT NULL,
    gcn integer NOT NULL,
    icn integer NOT NULL,
    transaction_datetime timestamp with time zone NOT NULL,
    transaction_status integer NOT NULL,
    transaction_status_desc character varying(45)
);


ALTER TABLE public.edi_transaction OWNER TO postgres;

--
-- Name: TABLE edi_transaction; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.edi_transaction IS 'Information about each EDI transaction with an insurance provider';


--
-- Name: COLUMN edi_transaction.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.edi_transaction.org_group_id IS 'Link to company_base table but really a link to the vendor that is being communicated with';


--
-- Name: COLUMN edi_transaction.tscn; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.edi_transaction.tscn IS 'Transaction set control number';


--
-- Name: COLUMN edi_transaction.gcn; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.edi_transaction.gcn IS 'Group control number';


--
-- Name: COLUMN edi_transaction.icn; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.edi_transaction.icn IS 'Interchange control number';


--
-- Name: COLUMN edi_transaction.transaction_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.edi_transaction.transaction_status IS '0 = File processed / Started?
1 = File created
2 = Error
3 = File Encrypted
4 = Encryption Failed
6 = Started
10 = File transmitted';


--
-- Name: education; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.education (
    education_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    start_date integer NOT NULL,
    end_date integer NOT NULL,
    school_type character(1) NOT NULL,
    school_name character varying(80) NOT NULL,
    school_location character varying(120) NOT NULL,
    graduate character(1) NOT NULL,
    subject character varying(60),
    other_type character varying(50),
    gpa smallint,
    current character(1),
    CONSTRAINT education_current_chk CHECK (((current = 'Y'::bpchar) OR (current = 'N'::bpchar))),
    CONSTRAINT education_graduate_chk CHECK (((graduate = 'Y'::bpchar) OR (graduate = 'N'::bpchar))),
    CONSTRAINT education_school_type_chk CHECK (((school_type = 'H'::bpchar) OR (school_type = 'U'::bpchar) OR (school_type = 'G'::bpchar) OR (school_type = 'T'::bpchar) OR (school_type = 'C'::bpchar) OR (school_type = 'O'::bpchar)))
);


ALTER TABLE public.education OWNER TO postgres;

--
-- Name: COLUMN education.start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.education.start_date IS 'YYYYMM';


--
-- Name: COLUMN education.end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.education.end_date IS 'YYYYMM';


--
-- Name: COLUMN education.school_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.education.school_type IS 'H = High school
U = College undergratuate
G = College graduate
T = Tech / trade school
C = Certificate
O = Other';


--
-- Name: COLUMN education.graduate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.education.graduate IS 'Yes / No';


--
-- Name: COLUMN education.other_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.education.other_type IS 'Used if school_type = ''O''';


--
-- Name: COLUMN education.current; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.education.current IS 'Are they still at this school?';


--
-- Name: electronic_fund_transfer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.electronic_fund_transfer (
    eft_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    bank_route character(9) NOT NULL,
    bank_account character varying(16) NOT NULL,
    account_type character(1) NOT NULL,
    amount_type character(1) NOT NULL,
    amount real NOT NULL,
    wage_type_id character(16) NOT NULL,
    CONSTRAINT eft_account_type_chk CHECK (((account_type = 'S'::bpchar) OR (account_type = 'C'::bpchar))),
    CONSTRAINT eft_amount_type_chk CHECK (((amount_type = 'F'::bpchar) OR (amount_type = 'P'::bpchar)))
);


ALTER TABLE public.electronic_fund_transfer OWNER TO postgres;

--
-- Name: COLUMN electronic_fund_transfer.account_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.electronic_fund_transfer.account_type IS 'Checking / Savings';


--
-- Name: COLUMN electronic_fund_transfer.amount_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.electronic_fund_transfer.amount_type IS 'Flat amount or Percent';


--
-- Name: email_notifications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.email_notifications (
    email_notification_id character(16) NOT NULL,
    notification_type character(1) DEFAULT 'W'::bpchar NOT NULL,
    name character varying(50) NOT NULL,
    title character varying(40),
    email character varying(50) NOT NULL,
    CONSTRAINT email_notification_type_chk CHECK ((notification_type = 'W'::bpchar))
);


ALTER TABLE public.email_notifications OWNER TO postgres;

--
-- Name: COLUMN email_notifications.notification_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.email_notifications.notification_type IS 'W = worker writeup';


--
-- Name: employee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee (
    person_id character(16) NOT NULL,
    timesheet_final_date integer DEFAULT 0 NOT NULL,
    eeo_category_id character(16),
    eeo_race_id character(16),
    ext_ref character varying(11),
    overtime_pay character(1) DEFAULT 'N'::bpchar NOT NULL,
    pay_periods_per_year smallint DEFAULT 0 NOT NULL,
    expected_hours_per_period real DEFAULT 0 NOT NULL,
    marital_status character(1) DEFAULT ' '::bpchar NOT NULL,
    local_tax_code character varying(6),
    earned_income_credit_status character(1) DEFAULT ' '::bpchar NOT NULL,
    add_federal_income_tax_type character(1) DEFAULT 'N'::bpchar NOT NULL,
    add_federal_income_tax real DEFAULT 0 NOT NULL,
    add_state_income_tax_type character(1) DEFAULT 'N'::bpchar NOT NULL,
    add_state_income_tax real DEFAULT 0 NOT NULL,
    add_local_income_tax_type character(1) DEFAULT 'N'::bpchar NOT NULL,
    add_local_income_tax real DEFAULT 0 NOT NULL,
    add_state_disability_tax_type character(1) DEFAULT 'N'::bpchar NOT NULL,
    add_state_disability_tax real DEFAULT 0 NOT NULL,
    number_federal_exemptions smallint DEFAULT 0 NOT NULL,
    number_state_exemptions smallint DEFAULT 0 NOT NULL,
    payroll_bank_code character(16),
    federal_extra_withhold real DEFAULT 0 NOT NULL,
    state_extra_withhold real DEFAULT 0 NOT NULL,
    tax_state character varying(4),
    unemployement_state character varying(4),
    benefit_class_id character(16),
    auto_time_log character(1) DEFAULT 'N'::bpchar NOT NULL,
    auto_overtime_logout character(1) DEFAULT 'N'::bpchar NOT NULL,
    length_of_work_day real DEFAULT 0 NOT NULL,
    length_of_breaks real DEFAULT 0 NOT NULL,
    workers_comp_code character varying(4),
    auto_log_time character(1) DEFAULT 'N'::bpchar NOT NULL,
    w4_status character(1) DEFAULT 'U'::bpchar NOT NULL,
    w4_exempt character(1) DEFAULT 'N'::bpchar NOT NULL,
    w4_name_differs character(1) DEFAULT 'N'::bpchar NOT NULL,
    medicare character(1) DEFAULT 'N'::bpchar NOT NULL,
    hr_admin character(1) DEFAULT 'N'::bpchar NOT NULL,
    benefit_wizard_status character(1) DEFAULT 'N'::bpchar NOT NULL,
    benefit_wizard_date integer DEFAULT 0 NOT NULL,
    status_id character(16),
    status_effective_date integer DEFAULT 0 NOT NULL,
    hours_per_week smallint DEFAULT 0 NOT NULL,
    employment_type character(1) DEFAULT 'E'::bpchar NOT NULL,
    adp_id character(16),
    CONSTRAINT employee_add_fed_chk CHECK (((add_federal_income_tax_type = 'A'::bpchar) OR (add_federal_income_tax_type = 'F'::bpchar) OR (add_federal_income_tax_type = 'N'::bpchar) OR (add_federal_income_tax_type = 'P'::bpchar))),
    CONSTRAINT employee_add_local_chk CHECK (((add_local_income_tax_type = 'A'::bpchar) OR (add_local_income_tax_type = 'F'::bpchar) OR (add_local_income_tax_type = 'N'::bpchar) OR (add_local_income_tax_type = 'P'::bpchar))),
    CONSTRAINT employee_add_state_chk CHECK (((add_state_income_tax_type = 'A'::bpchar) OR (add_state_income_tax_type = 'F'::bpchar) OR (add_state_income_tax_type = 'N'::bpchar) OR (add_state_income_tax_type = 'P'::bpchar))),
    CONSTRAINT employee_add_state_dis_chk CHECK (((add_state_disability_tax_type = 'A'::bpchar) OR (add_state_disability_tax_type = 'F'::bpchar) OR (add_state_disability_tax_type = 'N'::bpchar) OR (add_state_disability_tax_type = 'P'::bpchar))),
    CONSTRAINT employee_auto_overtime_chk CHECK (((auto_overtime_logout = 'Y'::bpchar) OR (auto_overtime_logout = 'N'::bpchar))),
    CONSTRAINT employee_auto_time_log_chk CHECK (((auto_time_log = 'Y'::bpchar) OR (auto_time_log = 'N'::bpchar))),
    CONSTRAINT employee_autolog_chk CHECK (((auto_log_time = 'Y'::bpchar) OR (auto_log_time = 'N'::bpchar))),
    CONSTRAINT employee_etype_chk CHECK (((employment_type = 'E'::bpchar) OR (employment_type = 'C'::bpchar))),
    CONSTRAINT employee_exempt_chk CHECK (((w4_exempt = 'Y'::bpchar) OR (w4_exempt = 'N'::bpchar))),
    CONSTRAINT employee_hr_admin_chk CHECK (((hr_admin = 'Y'::bpchar) OR (hr_admin = 'N'::bpchar))),
    CONSTRAINT employee_income_credit_status_chk CHECK (((earned_income_credit_status = ' '::bpchar) OR (earned_income_credit_status = 'I'::bpchar) OR (earned_income_credit_status = 'J'::bpchar) OR (earned_income_credit_status = 'W'::bpchar))),
    CONSTRAINT employee_marital_status_chk CHECK (((marital_status = ' '::bpchar) OR (marital_status = 'M'::bpchar) OR (marital_status = 'D'::bpchar) OR (marital_status = 'S'::bpchar) OR (marital_status = 'W'::bpchar))),
    CONSTRAINT employee_medicate_chk CHECK (((medicare = 'A'::bpchar) OR (medicare = 'B'::bpchar) OR (medicare = '2'::bpchar) OR (medicare = 'U'::bpchar) OR (medicare = 'N'::bpchar))),
    CONSTRAINT employee_name_differs_chk CHECK (((w4_name_differs = 'Y'::bpchar) OR (w4_name_differs = 'N'::bpchar))),
    CONSTRAINT employee_overtime_pay_chk CHECK (((overtime_pay = 'Y'::bpchar) OR (overtime_pay = 'N'::bpchar))),
    CONSTRAINT employee_status_chk CHECK (((w4_status = 'U'::bpchar) OR (w4_status = 'S'::bpchar) OR (w4_status = 'M'::bpchar) OR (w4_status = 'H'::bpchar))),
    CONSTRAINT employee_wstat_chk CHECK (((benefit_wizard_status = 'N'::bpchar) OR (benefit_wizard_status = 'U'::bpchar) OR (benefit_wizard_status = 'F'::bpchar) OR (benefit_wizard_status = 'P'::bpchar)))
);


ALTER TABLE public.employee OWNER TO postgres;

--
-- Name: TABLE employee; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.employee IS 'This table is used to store information about employees that is not contained in the person table.';


--
-- Name: COLUMN employee.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.person_id IS 'the base person record for this employee.';


--
-- Name: COLUMN employee.timesheet_final_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.timesheet_final_date IS 'The date at which all timesheets prior to which can not be edited.';


--
-- Name: COLUMN employee.ext_ref; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.ext_ref IS 'Employee ID used to refer to this employee externally to the system';


--
-- Name: COLUMN employee.overtime_pay; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.overtime_pay IS 'Is employee paid for overtime?';


--
-- Name: COLUMN employee.pay_periods_per_year; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.pay_periods_per_year IS 'Used to override the org_group standard';


--
-- Name: COLUMN employee.expected_hours_per_period; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.expected_hours_per_period IS 'Employee''s Pay Period Hours:   The number of hours an employee normally 
works during a pay period.  The hourly rate for salaried employees is based on this value.
';


--
-- Name: COLUMN employee.marital_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.marital_status IS 'blank = unknown
S = single
M = married
D = divorced
W = widowed';


--
-- Name: COLUMN employee.earned_income_credit_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.earned_income_credit_status IS 'I = Individual
J = Joint
W = Married filing w/o spouse
blank = does not qualify
';


--
-- Name: COLUMN employee.add_federal_income_tax_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.add_federal_income_tax_type IS 'A = Additional amount
F = Flat deduction amount
N = No tax deduction
P = Percentage deduction
';


--
-- Name: COLUMN employee.add_state_income_tax_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.add_state_income_tax_type IS 'A = Additional amount
F = Flat deduction amount
N = No tax deduction
P = Percentage deduction
';


--
-- Name: COLUMN employee.add_local_income_tax_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.add_local_income_tax_type IS 'A = Additional amount
F = Flat deduction amount
N = No tax deduction
P = Percentage deduction
';


--
-- Name: COLUMN employee.add_state_disability_tax_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.add_state_disability_tax_type IS 'A = Additional amount
F = Flat deduction amount
N = No tax deduction
P = Percentage deduction
';


--
-- Name: COLUMN employee.number_federal_exemptions; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.number_federal_exemptions IS 'From W4';


--
-- Name: COLUMN employee.number_state_exemptions; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.number_state_exemptions IS 'From W4';


--
-- Name: COLUMN employee.payroll_bank_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.payroll_bank_code IS 'Account employee is being paid from';


--
-- Name: COLUMN employee.auto_time_log; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.auto_time_log IS 'Y = auto log time when user logs in/out of the system
N = no auto log

Causes the system to auto log in/out time corresponding to a user''s login/out of the Arahant system.  Thus their logged time corresponds to the time they are in the Arahant system.
';


--
-- Name: COLUMN employee.auto_overtime_logout; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.auto_overtime_logout IS 'Y = logout user when overtime hit
N = no auto logout';


--
-- Name: COLUMN employee.auto_log_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.auto_log_time IS 'Yes or No

This field currently does not do anything.';


--
-- Name: COLUMN employee.w4_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.w4_status IS 'U = Unknown
S = Single
M = Married
H = Married but withhold at higher single rate';


--
-- Name: COLUMN employee.w4_exempt; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.w4_exempt IS 'Yes / No';


--
-- Name: COLUMN employee.w4_name_differs; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.w4_name_differs IS 'Yes / No';


--
-- Name: COLUMN employee.medicare; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.medicare IS 'A = Part A
B = Part B
2 = Part A & B
U = Part Unknown
N = Not on Medicare';


--
-- Name: COLUMN employee.hr_admin; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.hr_admin IS 'Is this employee an HR administrator?';


--
-- Name: COLUMN employee.benefit_wizard_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.benefit_wizard_status IS 'N = No change
U = Unfinished or unfinilized changes
F = Finished or finialized
P = Processed';


--
-- Name: COLUMN employee.benefit_wizard_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.benefit_wizard_date IS 'Date benefit_wizard_status was last changed';


--
-- Name: COLUMN employee.status_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.status_id IS 'This column is a copy of the latest value in the hr_empl_status_history table used for fast select purposes.';


--
-- Name: COLUMN employee.status_effective_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.status_effective_date IS 'This column is a copy of the latest value in the hr_empl_status_history table used for fast select purposes.';


--
-- Name: COLUMN employee.hours_per_week; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.hours_per_week IS 'Expected number of work hours per week';


--
-- Name: COLUMN employee.employment_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.employee.employment_type IS 'E = employee
C = Contractor';


--
-- Name: employee_label; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee_label (
    employee_label_id character(16) NOT NULL,
    name character varying(12) NOT NULL,
    description character varying(80),
    auto_add_new_employee character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT employee_label_new_emp_chk CHECK (((auto_add_new_employee = 'Y'::bpchar) OR (auto_add_new_employee = 'N'::bpchar)))
);


ALTER TABLE public.employee_label OWNER TO postgres;

--
-- Name: employee_label_association; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee_label_association (
    employee_id character(16) NOT NULL,
    employee_label_id character(16) NOT NULL,
    who_added character(16) NOT NULL,
    when_added timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    completed character(1) DEFAULT 'N'::bpchar NOT NULL,
    when_completed timestamp with time zone,
    who_completed character(16),
    notes character varying(1024),
    CONSTRAINT employee_lbl_ass_completed_chk CHECK (((completed = 'Y'::bpchar) OR (completed = 'N'::bpchar)))
);


ALTER TABLE public.employee_label_association OWNER TO postgres;

--
-- Name: employee_not_available; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee_not_available (
    employee_not_available_id character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    start_date integer NOT NULL,
    end_date integer NOT NULL,
    reason character varying(128) NOT NULL,
    CONSTRAINT ena_date_chk CHECK (((start_date > 20200101) AND (start_date < 21500101) AND (end_date > 20200101) AND (end_date < 21500101)))
);


ALTER TABLE public.employee_not_available OWNER TO postgres;

--
-- Name: TABLE employee_not_available; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.employee_not_available IS 'Date ranges a worker isn''t available to work';


--
-- Name: employee_rate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee_rate (
    employee_rate_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    rate_type_id character(16) NOT NULL,
    rate double precision DEFAULT 0.0 NOT NULL
);


ALTER TABLE public.employee_rate OWNER TO postgres;

--
-- Name: TABLE employee_rate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.employee_rate IS 'Billing rates associated with an employee';


--
-- Name: expense; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.expense (
    expense_id character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    project_id character(16) NOT NULL,
    date_paid integer DEFAULT 0 NOT NULL,
    per_diem_amount real DEFAULT 0 NOT NULL,
    expense_amount real DEFAULT 0 NOT NULL,
    advance_amount real DEFAULT 0 NOT NULL,
    payment_method character(1) NOT NULL,
    comments character varying(1024),
    auth_date integer NOT NULL,
    per_diem_return real DEFAULT 0 NOT NULL,
    auth_person_id character(16) NOT NULL,
    week_paid_for integer NOT NULL,
    scheduling_comments character varying(1024),
    hotel_amount real DEFAULT 0 NOT NULL,
    CONSTRAINT expense_method_chk CHECK (((payment_method = 'D'::bpchar) OR (payment_method = 'M'::bpchar) OR (payment_method = 'C'::bpchar) OR (payment_method = 'W'::bpchar) OR (payment_method = 'O'::bpchar)))
);


ALTER TABLE public.expense OWNER TO postgres;

--
-- Name: TABLE expense; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.expense IS 'Money paid to workers';


--
-- Name: COLUMN expense.date_paid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.expense.date_paid IS 'YYYYMMDD';


--
-- Name: COLUMN expense.per_diem_amount; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.expense.per_diem_amount IS 'Advanced pay for presumed future expenses';


--
-- Name: COLUMN expense.expense_amount; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.expense.expense_amount IS 'Expenses previously paid to employee for past expenses but not recorded';


--
-- Name: COLUMN expense.advance_amount; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.expense.advance_amount IS 'How much they were already previously paid';


--
-- Name: COLUMN expense.payment_method; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.expense.payment_method IS 'D = Direct deposit
M = Money / cash
C = Check
W = Walmart to Walmart
O = Comdata';


--
-- Name: COLUMN expense.comments; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.expense.comments IS 'Payroll comments';


--
-- Name: COLUMN expense.auth_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.expense.auth_date IS 'YYYYMMDD';


--
-- Name: COLUMN expense.per_diem_return; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.expense.per_diem_return IS 'Amount that was paid in advance and is now being returned';


--
-- Name: COLUMN expense.auth_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.expense.auth_person_id IS 'Person who added the record';


--
-- Name: COLUMN expense.week_paid_for; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.expense.week_paid_for IS 'Date of first day in week being paid for';


--
-- Name: expense_account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.expense_account (
    expense_account_id character(16) NOT NULL,
    expense_id character varying(10),
    description character varying(30) NOT NULL,
    gl_account_id character(16) NOT NULL
);


ALTER TABLE public.expense_account OWNER TO postgres;

--
-- Name: expense_receipt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.expense_receipt (
    expense_receipt_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    project_id character(16) NOT NULL,
    expense_account_id character(16) NOT NULL,
    receipt_date integer NOT NULL,
    when_uploaded timestamp with time zone NOT NULL,
    business_purpose character varying(80),
    amount double precision NOT NULL,
    who_uploaded character(16) NOT NULL,
    file_type character varying(8),
    payment_method character(1) NOT NULL,
    approved character(1) DEFAULT 'N'::bpchar NOT NULL,
    who_approved character(16),
    when_approved timestamp with time zone,
    CONSTRAINT expense_receipt_app_chk CHECK (((approved = 'Y'::bpchar) OR (approved = 'N'::bpchar))),
    CONSTRAINT expense_receipt_pm_chk CHECK (((payment_method = 'A'::bpchar) OR (payment_method = 'B'::bpchar) OR (payment_method = 'E'::bpchar)))
);


ALTER TABLE public.expense_receipt OWNER TO postgres;

--
-- Name: COLUMN expense_receipt.payment_method; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.expense_receipt.payment_method IS 'A = Company Debit Card
B = Employee Comdata Card
E = Employee Personal Account';


--
-- Name: COLUMN expense_receipt.approved; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.expense_receipt.approved IS 'Y/N';


--
-- Name: form_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.form_type (
    form_type_id character(16) NOT NULL,
    form_code character varying(15),
    description character varying(60) NOT NULL,
    form_type character(1) NOT NULL,
    org_group_id character(16),
    last_active_date integer DEFAULT 0 NOT NULL,
    field_downloadable character(1) DEFAULT 'N'::bpchar NOT NULL,
    internal character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT form_type_downloadable_chk CHECK (((field_downloadable = 'Y'::bpchar) OR (field_downloadable = 'N'::bpchar))),
    CONSTRAINT form_type_internal_chk CHECK (((internal = 'Y'::bpchar) OR (internal = 'N'::bpchar))),
    CONSTRAINT form_type_type_chk CHECK (((form_type = 'P'::bpchar) OR (form_type = 'E'::bpchar) OR (form_type = 'B'::bpchar)))
);


ALTER TABLE public.form_type OWNER TO postgres;

--
-- Name: COLUMN form_type.form_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.form_type.form_type IS 'P = Project
E = Person
B = Both';


--
-- Name: COLUMN form_type.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.form_type.org_group_id IS 'The company the record applies to, or NULL if it is globally applicable.';


--
-- Name: COLUMN form_type.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.form_type.last_active_date IS 'Last date this record should be used in a new association.  0 means no end.';


--
-- Name: COLUMN form_type.field_downloadable; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.form_type.field_downloadable IS 'Yes / No';


--
-- Name: COLUMN form_type.internal; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.form_type.internal IS 'Restrict for internal use?  Y/N';


--
-- Name: garnishment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.garnishment (
    garnishment_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    priority smallint NOT NULL,
    issue_state character(4) NOT NULL,
    fips_code character varying(15),
    docket_number character varying(25),
    remit_to_name character varying(50) NOT NULL,
    remit_to character(16) NOT NULL,
    collecting_state character(4) NOT NULL,
    max_percent real DEFAULT 0 NOT NULL,
    max_dollars double precision DEFAULT 0 NOT NULL,
    start_date integer NOT NULL,
    end_date integer NOT NULL,
    deduction_amount double precision DEFAULT 0 NOT NULL,
    deduction_percentage real DEFAULT 0 NOT NULL,
    net_or_gross character(1) NOT NULL,
    garnishment_type_id character(16) NOT NULL,
    CONSTRAINT garnishment_net_chk CHECK (((net_or_gross = 'N'::bpchar) OR (net_or_gross = 'G'::bpchar)))
);


ALTER TABLE public.garnishment OWNER TO postgres;

--
-- Name: COLUMN garnishment.net_or_gross; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.garnishment.net_or_gross IS 'Net or Gross';


--
-- Name: garnishment_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.garnishment_type (
    garnishment_type_id character(16) NOT NULL,
    description character varying(40),
    last_active_date integer DEFAULT 0 NOT NULL,
    wage_type_id character(16) NOT NULL
);


ALTER TABLE public.garnishment_type OWNER TO postgres;

--
-- Name: gl_account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.gl_account (
    gl_account_id character(16) NOT NULL,
    account_number character varying(15) NOT NULL,
    account_name character varying(30),
    account_type integer DEFAULT '-2'::integer NOT NULL,
    default_flag smallint DEFAULT 0 NOT NULL,
    company_id character(16)
);


ALTER TABLE public.gl_account OWNER TO postgres;

--
-- Name: TABLE gl_account; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.gl_account IS 'This table holds general ledger account information from the accounting system.';


--
-- Name: COLUMN gl_account.gl_account_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.gl_account.gl_account_id IS 'Unique key';


--
-- Name: COLUMN gl_account.account_number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.gl_account.account_number IS 'The account number in the accounting system.';


--
-- Name: COLUMN gl_account.account_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.gl_account.account_name IS 'The name of the account';


--
-- Name: COLUMN gl_account.account_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.gl_account.account_type IS 'Type of the account in the accouting system.  Specific to accounting system.';


--
-- Name: COLUMN gl_account.default_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.gl_account.default_flag IS 'Marks if this gl account should be the default one used for its type.';


--
-- Name: holiday; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.holiday (
    holiday_id character(16) NOT NULL,
    hdate integer NOT NULL,
    description character varying(80),
    org_group_id character(16),
    part_of_day character(1) DEFAULT 'F'::bpchar NOT NULL,
    CONSTRAINT holiday_part_chk CHECK (((part_of_day = 'F'::bpchar) OR (part_of_day = '1'::bpchar) OR (part_of_day = '2'::bpchar)))
);


ALTER TABLE public.holiday OWNER TO postgres;

--
-- Name: COLUMN holiday.hdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.holiday.hdate IS 'Holiday date';


--
-- Name: COLUMN holiday.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.holiday.description IS 'Name of holiday';


--
-- Name: COLUMN holiday.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.holiday.org_group_id IS 'The company the record applies to, or NULL if it is globally applicable.';


--
-- Name: COLUMN holiday.part_of_day; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.holiday.part_of_day IS 'F = Full day off
1 = 1st half off
2 = 2nd half off';


--
-- Name: hr_accrual; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_accrual (
    accrual_id character(16) NOT NULL,
    benefit_account character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    accrual_date integer NOT NULL,
    accrual_hours double precision NOT NULL,
    description character varying(80)
);


ALTER TABLE public.hr_accrual OWNER TO postgres;

--
-- Name: COLUMN hr_accrual.accrual_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_accrual.accrual_date IS 'Date accrual occured';


--
-- Name: COLUMN hr_accrual.accrual_hours; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_accrual.accrual_hours IS 'Hours addes or subtracted';


--
-- Name: hr_benefit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit (
    benefit_id character(16) NOT NULL,
    benefit_cat_id character(16) NOT NULL,
    benefit_name character varying(60) NOT NULL,
    rule_name character varying(80),
    time_related character(1) DEFAULT 'N'::bpchar NOT NULL,
    paid_benefit character(1) DEFAULT 'Y'::bpchar NOT NULL,
    pre_tax character(1) DEFAULT 'Y'::bpchar NOT NULL,
    provider_id character(16),
    group_id character varying(20),
    sub_group_id character varying(20),
    add_info character varying(60),
    has_beneficiaries character(1) DEFAULT 'N'::bpchar NOT NULL,
    plan_id character varying(20),
    plan_name character varying(100),
    start_date integer DEFAULT 0 NOT NULL,
    end_date integer DEFAULT 0 NOT NULL,
    requires_decline character(1) DEFAULT 'N'::bpchar NOT NULL,
    instructions text,
    cobra character(1) DEFAULT 'N'::bpchar NOT NULL,
    insurance_code character varying(10),
    payer_id character varying(20),
    product_id character(16),
    wage_type_id character(16) NOT NULL,
    description character varying(1000),
    eligibility_type smallint NOT NULL,
    eligibility_period smallint NOT NULL,
    dependent_max_age smallint NOT NULL,
    dependent_max_age_student smallint NOT NULL,
    coverage_end_type smallint NOT NULL,
    coverage_end_period smallint NOT NULL,
    process_type character(1) DEFAULT 'N'::bpchar NOT NULL,
    seqno smallint DEFAULT 0 NOT NULL,
    open_enrollment_wizard character(1) DEFAULT 'N'::bpchar NOT NULL,
    onboarding_wizard character(1) DEFAULT 'N'::bpchar NOT NULL,
    avatar_path character varying(256),
    open_enrollment_screen_id character(16),
    onboarding_screen_id character(16),
    avatar_location character(2),
    last_enrollment_date integer DEFAULT 0 NOT NULL,
    benefit_id_replaced_by character(16),
    contingent_beneficiaries character(1) DEFAULT 'N'::bpchar NOT NULL,
    has_physicians character(1) DEFAULT 'N'::bpchar NOT NULL,
    edi_field_descrption_1 character varying(60),
    edi_field_value_1 character varying(20),
    edi_field_descrption_2 character varying(60),
    edi_field_value_2 character varying(20),
    edi_field_descrption_3 character varying(60),
    edi_field_value_3 character varying(20),
    edi_field_descrption_4 character varying(60),
    edi_field_value_4 character varying(20),
    edi_field_descrption_5 character varying(60),
    edi_field_value_5 character varying(20),
    group_account_id character varying(20),
    auto_assign character(1) DEFAULT 'N'::bpchar NOT NULL,
    min_age smallint DEFAULT 0 NOT NULL,
    max_age smallint DEFAULT 0 NOT NULL,
    internal_id character varying(12),
    lisp_reference character varying(100),
    show_all_coverages character(1) DEFAULT 'N'::bpchar NOT NULL,
    cost_calc_type character(1) DEFAULT 'C'::bpchar NOT NULL,
    address_required character(1) DEFAULT 'N'::bpchar NOT NULL,
    employer_cost_model character(1) DEFAULT 'Z'::bpchar NOT NULL,
    employee_cost_model character(1) DEFAULT 'Z'::bpchar NOT NULL,
    benefit_amount_model character(1) DEFAULT 'N'::bpchar NOT NULL,
    min_pay double precision DEFAULT 0 NOT NULL,
    max_pay double precision DEFAULT 0 NOT NULL,
    min_hours_per_week real DEFAULT 0 NOT NULL,
    CONSTRAINT hr_benefit_addreq_chk CHECK (((address_required = 'Y'::bpchar) OR (address_required = 'N'::bpchar))),
    CONSTRAINT hr_benefit_autoasn_chk CHECK (((auto_assign = 'Y'::bpchar) OR (auto_assign = 'N'::bpchar))),
    CONSTRAINT hr_benefit_beneficiaries_chk CHECK (((has_beneficiaries = 'Y'::bpchar) OR (has_beneficiaries = 'N'::bpchar))),
    CONSTRAINT hr_benefit_cobra_chk CHECK (((cobra = 'Y'::bpchar) OR (cobra = 'N'::bpchar))),
    CONSTRAINT hr_benefit_cont_chk CHECK (((contingent_beneficiaries = 'Y'::bpchar) OR (contingent_beneficiaries = 'N'::bpchar))),
    CONSTRAINT hr_benefit_cost_type_chk CHECK (((cost_calc_type = 'C'::bpchar) OR (cost_calc_type = 'P'::bpchar))),
    CONSTRAINT hr_benefit_cov_end_chk CHECK (((coverage_end_type >= 1) AND (coverage_end_type <= 3))),
    CONSTRAINT hr_benefit_coverage_model_chk CHECK (((benefit_amount_model = 'N'::bpchar) OR (benefit_amount_model = 'F'::bpchar) OR (benefit_amount_model = 'R'::bpchar) OR (benefit_amount_model = 'S'::bpchar) OR (benefit_amount_model = 'M'::bpchar))),
    CONSTRAINT hr_benefit_decline_chk CHECK (((requires_decline = 'Y'::bpchar) OR (requires_decline = 'N'::bpchar))),
    CONSTRAINT hr_benefit_elig_typ_chk CHECK (((eligibility_type >= 1) AND (eligibility_type <= 5))),
    CONSTRAINT hr_benefit_employee_model_chk CHECK (((employee_cost_model = 'Z'::bpchar) OR (employee_cost_model = 'F'::bpchar) OR (employee_cost_model = 'A'::bpchar) OR (employee_cost_model = 'R'::bpchar) OR (employee_cost_model = 'B'::bpchar))),
    CONSTRAINT hr_benefit_employer_model_chk CHECK (((employer_cost_model = 'Z'::bpchar) OR (employer_cost_model = 'F'::bpchar) OR (employer_cost_model = 'A'::bpchar) OR (employer_cost_model = 'B'::bpchar))),
    CONSTRAINT hr_benefit_has_physicians_chk CHECK (((has_physicians = 'Y'::bpchar) OR (has_physicians = 'N'::bpchar))),
    CONSTRAINT hr_benefit_obnoard_chk CHECK (((onboarding_wizard = 'Y'::bpchar) OR (onboarding_wizard = 'N'::bpchar))),
    CONSTRAINT hr_benefit_openenrol_chk CHECK (((open_enrollment_wizard = 'Y'::bpchar) OR (open_enrollment_wizard = 'N'::bpchar))),
    CONSTRAINT hr_benefit_paid_chk CHECK (((paid_benefit = 'Y'::bpchar) OR (paid_benefit = 'N'::bpchar))),
    CONSTRAINT hr_benefit_pre_tax_chk CHECK (((pre_tax = 'Y'::bpchar) OR (pre_tax = 'N'::bpchar) OR (pre_tax = 'E'::bpchar))),
    CONSTRAINT hr_benefit_process_type_chk CHECK (((process_type = 'N'::bpchar) OR (process_type = 'E'::bpchar) OR (process_type = 'H'::bpchar) OR (process_type = 'V'::bpchar))),
    CONSTRAINT hr_benefit_showallcov_chk CHECK (((show_all_coverages = 'Y'::bpchar) OR (show_all_coverages = 'N'::bpchar))),
    CONSTRAINT hr_benefit_time_related_chk CHECK (((time_related = 'Y'::bpchar) OR (time_related = 'N'::bpchar)))
);


ALTER TABLE public.hr_benefit OWNER TO postgres;

--
-- Name: COLUMN hr_benefit.rule_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.rule_name IS 'Name of rule in rule engine to run';


--
-- Name: COLUMN hr_benefit.time_related; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.time_related IS 'Is this benefit something related to work hours?';


--
-- Name: COLUMN hr_benefit.paid_benefit; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.paid_benefit IS 'Does employer pay employee for time associated with this benefit?';


--
-- Name: COLUMN hr_benefit.pre_tax; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.pre_tax IS 'Is benefit payed with pre-tax dollars
  Yes
  No
  Employee chooses';


--
-- Name: COLUMN hr_benefit.provider_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.provider_id IS 'Link to VENDOR table';


--
-- Name: COLUMN hr_benefit.group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.group_id IS 'Carrier''s id for the group';


--
-- Name: COLUMN hr_benefit.add_info; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.add_info IS 'Optional additional info regarding the benefit';


--
-- Name: COLUMN hr_benefit.plan_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.plan_name IS 'Used for EDI';


--
-- Name: COLUMN hr_benefit.start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.start_date IS 'Date benefit became active';


--
-- Name: COLUMN hr_benefit.end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.end_date IS 'Last day benefit active';


--
-- Name: COLUMN hr_benefit.requires_decline; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.requires_decline IS 'Does this benefit require an explicit decline by employee';


--
-- Name: COLUMN hr_benefit.instructions; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.instructions IS 'HTML instructions page';


--
-- Name: COLUMN hr_benefit.cobra; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.cobra IS 'Does this benefit have a Cobra option?';


--
-- Name: COLUMN hr_benefit.insurance_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.insurance_code IS 'Used for EDI';


--
-- Name: COLUMN hr_benefit.payer_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.payer_id IS 'Used for EDI';


--
-- Name: COLUMN hr_benefit.wage_type_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.wage_type_id IS 'Used for specific custom purposes.  Does not limit the benefit to a specific wage type.';


--
-- Name: COLUMN hr_benefit.eligibility_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.eligibility_type IS '1 = first day of employment
2 = first day of the month following x days of employment
3 = first day of the month following x months of employment
4 = after x days of employment
5 = after x months of employment

x is in eligibility_period';


--
-- Name: COLUMN hr_benefit.eligibility_period; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.eligibility_period IS 'This number is x and relates to eligibility_type';


--
-- Name: COLUMN hr_benefit.dependent_max_age; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.dependent_max_age IS 'Beyond this age, dependents (who are not students) are terminated from this benefit';


--
-- Name: COLUMN hr_benefit.dependent_max_age_student; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.dependent_max_age_student IS 'Beyond this age dependents, who are students, are terminated from this benefit';


--
-- Name: COLUMN hr_benefit.coverage_end_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.coverage_end_type IS 'When does the benefit auto-terminate

1 = end of the month
2 = employment end date
3 = x days after qualifying event

x is stored in coverage_end_period';


--
-- Name: COLUMN hr_benefit.coverage_end_period; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.coverage_end_period IS 'This column is the x for coverage_end_type';


--
-- Name: COLUMN hr_benefit.process_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.process_type IS 'Used by the DRC

E = Employee process
H = HRSP process
V = Vendor process
N = No special processing';


--
-- Name: COLUMN hr_benefit.avatar_location; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.avatar_location IS 'Location of avatar on screen';


--
-- Name: COLUMN hr_benefit.last_enrollment_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.last_enrollment_date IS 'This is the last date a person can enroll in this benefit.';


--
-- Name: COLUMN hr_benefit.benefit_id_replaced_by; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.benefit_id_replaced_by IS 'This is the ID of the benefit this benefit is being replaced by.';


--
-- Name: COLUMN hr_benefit.contingent_beneficiaries; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.contingent_beneficiaries IS 'Does this benefit have contingent beneficiaries?  Y/N';


--
-- Name: COLUMN hr_benefit.has_physicians; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.has_physicians IS 'If yes then this benefit can have links from the physician table';


--
-- Name: COLUMN hr_benefit.min_age; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.min_age IS 'Minimum eligibility age for benefit, 0 means there is no limit';


--
-- Name: COLUMN hr_benefit.max_age; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.max_age IS 'Maximum eligibility age for benefit, 0 means there is no limi';


--
-- Name: COLUMN hr_benefit.internal_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.internal_id IS 'Internal benefit ID used to identify the same products across different companies';


--
-- Name: COLUMN hr_benefit.lisp_reference; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.lisp_reference IS 'Reference to a lisp function to be executed.  String to include file, package, and function name, separated by colons. Or package, and function name seperated by colon if package and file name are the same.';


--
-- Name: COLUMN hr_benefit.show_all_coverages; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.show_all_coverages IS 'If ''Y'', it will show each individual enrollment on the wizard review screen instead of the policy only';


--
-- Name: COLUMN hr_benefit.cost_calc_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.cost_calc_type IS 'C = use the cost that is currently effective
P = use the cost that was in effect when the policy was started';


--
-- Name: COLUMN hr_benefit.employer_cost_model; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.employer_cost_model IS 'Z = zero
F = fixed amount
A = age banded fixed amount
B = X percent of benefit amount based on age';


--
-- Name: COLUMN hr_benefit.employee_cost_model; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.employee_cost_model IS 'Z = zero
F = fixed amount
A = age banded fixed amount
R = Min / Max / Inc (fixed range)
B = X percent of benefit amount based on age';


--
-- Name: COLUMN hr_benefit.benefit_amount_model; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.benefit_amount_model IS 'N = not applicable
F = fixed amount
R = min / max / inc (with max mult of salary)
S = fixed percent of salary (with fixed min / max)
M = fixed multiple of salary (with fixed min / max)';


--
-- Name: COLUMN hr_benefit.min_pay; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.min_pay IS 'Minimum employee salary or hourly rate';


--
-- Name: COLUMN hr_benefit.max_pay; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit.max_pay IS 'Maximum employee salary or hourly rate';


--
-- Name: hr_benefit_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit_category (
    benefit_cat_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    description character varying(45) NOT NULL,
    benefit_type smallint NOT NULL,
    mutually_exclusive character(1) DEFAULT 'Y'::bpchar NOT NULL,
    requires_decline character(1) DEFAULT 'N'::bpchar NOT NULL,
    seqno smallint DEFAULT 0 NOT NULL,
    open_enrollment_wizard character(1) DEFAULT 'N'::bpchar NOT NULL,
    onboarding_wizard character(1) DEFAULT 'N'::bpchar NOT NULL,
    avatar_path character varying(256),
    instructions text,
    open_enrollment_screen_id character(16),
    onboarding_screen_id character(16),
    avatar_location character(2),
    CONSTRAINT hr_bencat_decline_chk CHECK (((requires_decline = 'Y'::bpchar) OR (requires_decline = 'N'::bpchar))),
    CONSTRAINT hr_bencat_enrwiz_chk CHECK (((open_enrollment_wizard = 'Y'::bpchar) OR (open_enrollment_wizard = 'N'::bpchar))),
    CONSTRAINT hr_bencat_me_chk CHECK (((mutually_exclusive = 'Y'::bpchar) OR (mutually_exclusive = 'N'::bpchar))),
    CONSTRAINT hr_bencat_onboard_chk CHECK (((onboarding_wizard = 'Y'::bpchar) OR (onboarding_wizard = 'N'::bpchar)))
);


ALTER TABLE public.hr_benefit_category OWNER TO postgres;

--
-- Name: COLUMN hr_benefit_category.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_category.org_group_id IS 'The org group (Company?) this category of benefits applies to';


--
-- Name: COLUMN hr_benefit_category.benefit_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_category.benefit_type IS '0 = HEALTH
1 = DENTAL
2 = LIFE
3 = VISION
4 = SHORT_TERM_CARE
5 = LONG_TERM_CARE
6 = FLEX_TYPE
7 = VOLUNTARY
8 = MISC
9 = PENSION';


--
-- Name: COLUMN hr_benefit_category.mutually_exclusive; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_category.mutually_exclusive IS 'Are benefits in this category mutually exclusive to each other (the user can only select one of the benefits in this category)';


--
-- Name: COLUMN hr_benefit_category.requires_decline; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_category.requires_decline IS 'Y means this category (as a whole) requires an explicit employee decline';


--
-- Name: COLUMN hr_benefit_category.open_enrollment_wizard; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_category.open_enrollment_wizard IS 'Yes / No';


--
-- Name: COLUMN hr_benefit_category.onboarding_wizard; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_category.onboarding_wizard IS 'Yes / No';


--
-- Name: COLUMN hr_benefit_category.instructions; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_category.instructions IS 'HTML';


--
-- Name: COLUMN hr_benefit_category.avatar_location; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_category.avatar_location IS 'Location of avatar on screen';


--
-- Name: hr_benefit_category_answer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit_category_answer (
    answer_id character(16) NOT NULL,
    question_id character(16) NOT NULL,
    benefit_id character(16) NOT NULL,
    answer character varying(200) NOT NULL
);


ALTER TABLE public.hr_benefit_category_answer OWNER TO postgres;

--
-- Name: hr_benefit_category_question; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit_category_question (
    question_id character(16) NOT NULL,
    benefit_cat_id character(16) NOT NULL,
    seqno smallint DEFAULT 0 NOT NULL,
    question character varying(100) NOT NULL
);


ALTER TABLE public.hr_benefit_category_question OWNER TO postgres;

--
-- Name: hr_benefit_change_reason; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit_change_reason (
    bcr_id character(16) NOT NULL,
    bcr_type smallint NOT NULL,
    description character varying(60) NOT NULL,
    start_date integer DEFAULT 0 NOT NULL,
    end_date integer DEFAULT 0 NOT NULL,
    effective_date integer DEFAULT 0 NOT NULL,
    event_type smallint DEFAULT 0 NOT NULL,
    company_id character(16),
    instructions text,
    CONSTRAINT hr_bcr_bcr_chk CHECK (((bcr_type >= 1) AND (bcr_type <= 4))),
    CONSTRAINT hr_benchgrsn_event_type_chk CHECK (((event_type >= 0) AND (event_type <= 12)))
);


ALTER TABLE public.hr_benefit_change_reason OWNER TO postgres;

--
-- Name: COLUMN hr_benefit_change_reason.bcr_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_change_reason.bcr_type IS '1=Qualifying Event
2=Open Enrollment
3=New Hire
4=Internal Staff Edit';


--
-- Name: COLUMN hr_benefit_change_reason.start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_change_reason.start_date IS 'When record becomes active AND (if open enrollment) when open enrollment starts';


--
-- Name: COLUMN hr_benefit_change_reason.end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_change_reason.end_date IS 'If open enrollment - last date of open enrollment. otherwise last date of record being active';


--
-- Name: COLUMN hr_benefit_change_reason.effective_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_change_reason.effective_date IS 'Only if this an open enrollment record - what date do the open enrollment requests become active?';


--
-- Name: COLUMN hr_benefit_change_reason.event_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_change_reason.event_type IS '0  Unknown or Other
1  Dissolution of Marriage
2  Birth, Adoption or Legal Custody of a Child
3  Death
4  Voluntary Termination
5  Involuntary Termination
6  Elected COBRA
7  Marriage
8  Gained Other Coverage
9  Dependent Ineligible
10 Reinstatement - dependent or employee eligible again
11 Part Time/Reduction in Hours
12 Entitled to Medicare';


--
-- Name: hr_benefit_class; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit_class (
    benefit_class_id character(16) NOT NULL,
    class_name character varying(20) NOT NULL,
    class_description character varying(80),
    org_group_id character(16) NOT NULL,
    first_active_date integer DEFAULT 0 NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.hr_benefit_class OWNER TO postgres;

--
-- Name: hr_benefit_config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit_config (
    benefit_config_id character(16) NOT NULL,
    benefit_id character(16) NOT NULL,
    config_name character varying(60),
    employee character(1) DEFAULT 'Y'::bpchar NOT NULL,
    children character(1) NOT NULL,
    max_children smallint DEFAULT 0 NOT NULL,
    employee_cobra_cost real DEFAULT 0 NOT NULL,
    add_info character varying(60),
    auto_assign character(1) DEFAULT 'N'::bpchar NOT NULL,
    spouse_declines_outside character(1) DEFAULT 'N'::bpchar NOT NULL,
    start_date integer DEFAULT 0 NOT NULL,
    end_date integer DEFAULT 0 NOT NULL,
    spouse_employee character(1) DEFAULT 'N'::bpchar NOT NULL,
    spouse_non_employee character(1) DEFAULT 'N'::bpchar NOT NULL,
    spouse_emp_or_children character(1) DEFAULT 'N'::bpchar NOT NULL,
    spouse_non_emp_or_children character(1) DEFAULT 'N'::bpchar NOT NULL,
    on_billing character(1) DEFAULT 'Y'::bpchar NOT NULL,
    insurance_code character varying(10),
    seqno smallint DEFAULT 0 NOT NULL,
    CONSTRAINT hr_ben_config_billing_chk CHECK (((on_billing = 'Y'::bpchar) OR (on_billing = 'N'::bpchar))),
    CONSTRAINT hr_benconf_auto_assign_chk CHECK (((auto_assign = 'Y'::bpchar) OR (auto_assign = 'N'::bpchar))),
    CONSTRAINT hr_benconf_children_chk CHECK (((children = 'Y'::bpchar) OR (children = 'N'::bpchar))),
    CONSTRAINT hr_benconf_employee_chk CHECK (((employee = 'Y'::bpchar) OR (employee = 'N'::bpchar))),
    CONSTRAINT hr_benconf_sp_emp_chk CHECK (((spouse_employee = 'Y'::bpchar) OR (spouse_employee = 'N'::bpchar))),
    CONSTRAINT hr_benconf_sp_nonemp_chk CHECK (((spouse_non_employee = 'Y'::bpchar) OR (spouse_non_employee = 'N'::bpchar))),
    CONSTRAINT hr_benconf_spouse_emp_child CHECK (((spouse_emp_or_children = 'Y'::bpchar) OR (spouse_emp_or_children = 'N'::bpchar))),
    CONSTRAINT hr_benconf_spouse_nemp_child CHECK (((spouse_non_emp_or_children = 'Y'::bpchar) OR (spouse_non_emp_or_children = 'N'::bpchar)))
);


ALTER TABLE public.hr_benefit_config OWNER TO postgres;

--
-- Name: COLUMN hr_benefit_config.employee; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_config.employee IS 'Is employee covered?';


--
-- Name: COLUMN hr_benefit_config.children; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_config.children IS 'Are children covered?';


--
-- Name: COLUMN hr_benefit_config.max_children; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_config.max_children IS 'Max number of children covered in plans that cover children, or max number of dependents for plans covering spouse OR children';


--
-- Name: COLUMN hr_benefit_config.spouse_declines_outside; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_config.spouse_declines_outside IS 'This benefit applies only if spouse is employed by another company and declined their insurance';


--
-- Name: COLUMN hr_benefit_config.start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_config.start_date IS 'Date benefit config became active';


--
-- Name: COLUMN hr_benefit_config.end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_config.end_date IS 'Last date benefit config was active';


--
-- Name: COLUMN hr_benefit_config.spouse_employee; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_config.spouse_employee IS 'Covers a spouse that is also an employee of the company';


--
-- Name: COLUMN hr_benefit_config.spouse_non_employee; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_config.spouse_non_employee IS 'Covers spouse that is not an employee of the company';


--
-- Name: COLUMN hr_benefit_config.spouse_emp_or_children; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_config.spouse_emp_or_children IS 'Covers spouse employed by company OR children';


--
-- Name: COLUMN hr_benefit_config.spouse_non_emp_or_children; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_config.spouse_non_emp_or_children IS 'Covers employee not employed by company OR children';


--
-- Name: COLUMN hr_benefit_config.on_billing; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_config.on_billing IS 'Include on consolidated billing report (the default)';


--
-- Name: COLUMN hr_benefit_config.insurance_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_config.insurance_code IS 'This is a code the insurance company assigns to this configuration';


--
-- Name: hr_benefit_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit_join (
    benefit_join_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    covered_person character(16) NOT NULL,
    relationship_id character(16),
    paying_person character(16) NOT NULL,
    benefit_declined character(1) DEFAULT 'N'::bpchar NOT NULL,
    benefit_config_id character(16),
    benefit_cat_id character(16),
    benefit_id character(16),
    insurance_id character varying(20),
    policy_start_date integer DEFAULT 0 NOT NULL,
    policy_end_date integer DEFAULT 0 NOT NULL,
    coverage_start_date integer DEFAULT 0 NOT NULL,
    coverage_end_date integer DEFAULT 0 NOT NULL,
    amount_paid_source character(1) DEFAULT 'C'::bpchar NOT NULL,
    amount_paid_type character(1) DEFAULT 'F'::bpchar NOT NULL,
    amount_paid double precision DEFAULT 0 NOT NULL,
    amount_covered double precision DEFAULT 0 NOT NULL,
    change_description character varying(60),
    benefit_approved character(1) DEFAULT 'Y'::bpchar NOT NULL,
    cobra character(1) DEFAULT 'N'::bpchar NOT NULL,
    comments character varying(80),
    cobra_acceptance_date integer DEFAULT 0 NOT NULL,
    max_months_on_cobra smallint DEFAULT 0 NOT NULL,
    other_insurance character varying(40),
    other_insurance_is_primary character(1) DEFAULT 'N'::bpchar NOT NULL,
    project_id character(16),
    life_event_id character(16),
    bcr_id character(16),
    employee_covered character(1) DEFAULT 'Y'::bpchar NOT NULL,
    employee_explanation character varying(200),
    requested_cost double precision DEFAULT 0 NOT NULL,
    requested_cost_period character(1) DEFAULT 'M'::bpchar NOT NULL,
    coverage_change_date integer DEFAULT 0 NOT NULL,
    original_coverage_date integer DEFAULT 0 NOT NULL,
    CONSTRAINT hr_ben_decl_chk CHECK (((benefit_declined = 'Y'::bpchar) OR (benefit_declined = 'N'::bpchar))),
    CONSTRAINT hr_ben_emp_covered_chk CHECK (((employee_covered = 'Y'::bpchar) OR (employee_covered = 'N'::bpchar))),
    CONSTRAINT hr_ben_join_cobra_chk CHECK (((cobra = 'Y'::bpchar) OR (cobra = 'N'::bpchar))),
    CONSTRAINT hr_ben_join_source_chk CHECK (((amount_paid_source = 'M'::bpchar) OR (amount_paid_source = 'C'::bpchar))),
    CONSTRAINT hr_benefit_join_bcr_chk CHECK ((((bcr_id IS NULL) AND (life_event_id IS NOT NULL)) OR ((bcr_id IS NOT NULL) AND (life_event_id IS NULL)))),
    CONSTRAINT hr_benefit_join_paid_type_chk CHECK (((amount_paid_type = 'F'::bpchar) OR (amount_paid_type = 'P'::bpchar))),
    CONSTRAINT hr_benefit_join_primary_insurance_chk CHECK (((other_insurance_is_primary = 'N'::bpchar) OR (other_insurance_is_primary = 'P'::bpchar) OR (other_insurance_is_primary = 'S'::bpchar))),
    CONSTRAINT hr_benefit_join_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT hr_benjoin_app_chk CHECK (((benefit_approved = 'Y'::bpchar) OR (benefit_approved = 'N'::bpchar) OR (benefit_approved = 'R'::bpchar))),
    CONSTRAINT hr_benjoin_reqcstper_chk CHECK (((requested_cost_period = 'W'::bpchar) OR (requested_cost_period = 'M'::bpchar) OR (requested_cost_period = 'Q'::bpchar) OR (requested_cost_period = 'S'::bpchar) OR (requested_cost_period = 'A'::bpchar)))
);


ALTER TABLE public.hr_benefit_join OWNER TO postgres;

--
-- Name: TABLE hr_benefit_join; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_benefit_join IS 'Joins a person with benefit categories';


--
-- Name: COLUMN hr_benefit_join.benefit_join_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.benefit_join_id IS 'Main key';


--
-- Name: COLUMN hr_benefit_join.record_change_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.record_change_type IS 'New / Modified / Deleted';


--
-- Name: COLUMN hr_benefit_join.record_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.record_person_id IS 'Person who changed the record';


--
-- Name: COLUMN hr_benefit_join.covered_person; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.covered_person IS 'Person being covered by the benefit.  Once added, this column should never change.';


--
-- Name: COLUMN hr_benefit_join.relationship_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.relationship_id IS 'Employee relationship that is providing this benefit to the covered person or NULL if this is the employee.  This column should never change.';


--
-- Name: COLUMN hr_benefit_join.paying_person; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.paying_person IS 'Person paying for the benefit.  This column can change.';


--
-- Name: COLUMN hr_benefit_join.benefit_declined; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.benefit_declined IS 'Is benefit declined by employee?';


--
-- Name: COLUMN hr_benefit_join.benefit_config_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.benefit_config_id IS 'Used as the exclusive link for people and projects that have a benefit (not declined it)';


--
-- Name: COLUMN hr_benefit_join.benefit_cat_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.benefit_cat_id IS 'Only used to signify an entire benefit category that is decliened';


--
-- Name: COLUMN hr_benefit_join.benefit_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.benefit_id IS 'Only used to signify an entire benefit that is decliened';


--
-- Name: COLUMN hr_benefit_join.policy_start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.policy_start_date IS 'Date benefit goes into effect (person starts paying for the plan)';


--
-- Name: COLUMN hr_benefit_join.policy_end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.policy_end_date IS 'Date benefit terminated (person stops paying for the plan)';


--
-- Name: COLUMN hr_benefit_join.coverage_start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.coverage_start_date IS 'Start date person is covered by the plan';


--
-- Name: COLUMN hr_benefit_join.coverage_end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.coverage_end_date IS 'End date person is covered by plan';


--
-- Name: COLUMN hr_benefit_join.amount_paid_source; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.amount_paid_source IS 'Manually entered
Calculated

This foeld is deprecated for now but should be used to be able to override calcualted amounts in the future.';


--
-- Name: COLUMN hr_benefit_join.amount_paid_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.amount_paid_type IS 'F = Flat amount
P = Percentage

This field is deprecated for now.';


--
-- Name: COLUMN hr_benefit_join.amount_paid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.amount_paid IS 'Annual amount contributed by person if person is provider';


--
-- Name: COLUMN hr_benefit_join.amount_covered; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.amount_covered IS 'Annual amount covered by benefit if applicable';


--
-- Name: COLUMN hr_benefit_join.change_description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.change_description IS 'Reason benefit was changed';


--
-- Name: COLUMN hr_benefit_join.benefit_approved; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.benefit_approved IS 'Is benefit approved by administrator?
  Y = Yes - approved
  N = No - not approved
  R = Approval Rejected';


--
-- Name: COLUMN hr_benefit_join.cobra; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.cobra IS 'Is covered person on Cobra?';


--
-- Name: COLUMN hr_benefit_join.project_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.project_id IS 'The project that is keeping track of change requests.';


--
-- Name: COLUMN hr_benefit_join.life_event_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.life_event_id IS 'The life event that the last edit to this record was related to.  hr_benenfit_hoin_h has the history.';


--
-- Name: COLUMN hr_benefit_join.employee_covered; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.employee_covered IS 'Is employee covered by this benefit?';


--
-- Name: COLUMN hr_benefit_join.requested_cost_period; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.requested_cost_period IS '''W'' weekly, ''M'' monthly, ''Q'' quarterly, ''S'' semi-annually, ''A'' annually';


--
-- Name: COLUMN hr_benefit_join.coverage_change_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.coverage_change_date IS 'Last date plan or coverage was changed but not vendor or policy.';


--
-- Name: COLUMN hr_benefit_join.original_coverage_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join.original_coverage_date IS 'Original date of coverage regardless of plan or coverage level changes.';


--
-- Name: CONSTRAINT hr_benefit_join_bcr_chk ON hr_benefit_join; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT hr_benefit_join_bcr_chk ON public.hr_benefit_join IS 'Benefit change reason gets associated either directly through bcr_id OR indirectly through life_event_id';


--
-- Name: hr_benefit_join_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit_join_h (
    history_id character(32) NOT NULL,
    benefit_join_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    covered_person character(16) NOT NULL,
    relationship_id character(16),
    paying_person character(16) NOT NULL,
    benefit_declined character(1) DEFAULT 'N'::bpchar NOT NULL,
    benefit_config_id character(16),
    benefit_cat_id character(16),
    benefit_id character(16),
    insurance_id character varying(20),
    policy_start_date integer DEFAULT 0 NOT NULL,
    policy_end_date integer DEFAULT 0 NOT NULL,
    coverage_start_date integer DEFAULT 0 NOT NULL,
    coverage_end_date integer DEFAULT 0 NOT NULL,
    amount_paid_source character(1) DEFAULT 'C'::bpchar NOT NULL,
    amount_paid_type character(1) DEFAULT 'F'::bpchar NOT NULL,
    amount_paid double precision DEFAULT 0 NOT NULL,
    amount_covered double precision DEFAULT 0 NOT NULL,
    change_description character varying(60),
    benefit_approved character(1) DEFAULT 'Y'::bpchar NOT NULL,
    cobra character(1) DEFAULT 'N'::bpchar NOT NULL,
    comments character varying(80),
    cobra_acceptance_date integer DEFAULT 0 NOT NULL,
    max_months_on_cobra smallint DEFAULT 0 NOT NULL,
    other_insurance character varying(40),
    other_insurance_is_primary character(1) DEFAULT 'N'::bpchar NOT NULL,
    project_id character(16),
    life_event_id character(16),
    bcr_id character(16),
    employee_covered character(1) DEFAULT 'Y'::bpchar NOT NULL,
    employee_explanation character varying(200),
    requested_cost double precision DEFAULT 0 NOT NULL,
    requested_cost_period character(1) DEFAULT 'M'::bpchar NOT NULL,
    coverage_change_date integer DEFAULT 0 NOT NULL,
    original_coverage_date integer DEFAULT 0 NOT NULL,
    CONSTRAINT hr_ben_decl_chk_h CHECK (((benefit_declined = 'Y'::bpchar) OR (benefit_declined = 'N'::bpchar))),
    CONSTRAINT hr_benefit_join_rct_chk_h CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT hr_benjnh_cobra_chk_h CHECK (((cobra = 'Y'::bpchar) OR (cobra = 'N'::bpchar))),
    CONSTRAINT hr_benjoin_app_chk_h CHECK (((benefit_approved = 'Y'::bpchar) OR (benefit_approved = 'N'::bpchar) OR (benefit_approved = 'R'::bpchar))),
    CONSTRAINT hr_benjoin_h_amt_paid_type_chk CHECK (((amount_paid_type = 'F'::bpchar) OR (amount_paid_type = 'P'::bpchar))),
    CONSTRAINT hr_benjoin_h_bcr_chk CHECK ((((bcr_id IS NULL) AND (life_event_id IS NOT NULL)) OR ((bcr_id IS NOT NULL) AND (life_event_id IS NULL)))),
    CONSTRAINT hr_benjoinh_emplcov_chk CHECK (((employee_covered = 'Y'::bpchar) OR (employee_covered = 'N'::bpchar))),
    CONSTRAINT hr_benjoinh_reqcstper_chk CHECK (((requested_cost_period = 'W'::bpchar) OR (requested_cost_period = 'M'::bpchar) OR (requested_cost_period = 'Q'::bpchar) OR (requested_cost_period = 'S'::bpchar) OR (requested_cost_period = 'A'::bpchar)))
);


ALTER TABLE public.hr_benefit_join_h OWNER TO postgres;

--
-- Name: TABLE hr_benefit_join_h; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_benefit_join_h IS 'Joins a person with benefit categories - history table';


--
-- Name: COLUMN hr_benefit_join_h.history_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.history_id IS 'A GUID';


--
-- Name: COLUMN hr_benefit_join_h.benefit_join_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.benefit_join_id IS 'Main key';


--
-- Name: COLUMN hr_benefit_join_h.record_change_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.record_change_type IS 'New / Modified / Deleted';


--
-- Name: COLUMN hr_benefit_join_h.record_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.record_person_id IS 'Person who changed the record';


--
-- Name: COLUMN hr_benefit_join_h.covered_person; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.covered_person IS 'Person being covered by the benefit.  Once added, this column should never change.';


--
-- Name: COLUMN hr_benefit_join_h.relationship_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.relationship_id IS 'Employee relationship that is providing this benefit to the covered person or NULL if this is the employee.  This column should never change.';


--
-- Name: COLUMN hr_benefit_join_h.paying_person; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.paying_person IS 'Person paying for the benefit.  This column can change.';


--
-- Name: COLUMN hr_benefit_join_h.benefit_declined; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.benefit_declined IS 'Is benefit declined by employee?';


--
-- Name: COLUMN hr_benefit_join_h.benefit_config_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.benefit_config_id IS 'Used as the exclusive link for people and projects that have a benefit (not declined it)';


--
-- Name: COLUMN hr_benefit_join_h.benefit_cat_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.benefit_cat_id IS 'Only used to signify an entire benefit category that is decliened';


--
-- Name: COLUMN hr_benefit_join_h.benefit_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.benefit_id IS 'Only used to signify an entire benefit that is decliened';


--
-- Name: COLUMN hr_benefit_join_h.policy_start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.policy_start_date IS 'Date benefit goes into effect (person starts paying for the plan)';


--
-- Name: COLUMN hr_benefit_join_h.policy_end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.policy_end_date IS 'Date benefit terminated (person stops paying for the plan)';


--
-- Name: COLUMN hr_benefit_join_h.coverage_start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.coverage_start_date IS 'Start date person is covered by the plan';


--
-- Name: COLUMN hr_benefit_join_h.coverage_end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.coverage_end_date IS 'End date person is covered by plan';


--
-- Name: COLUMN hr_benefit_join_h.amount_paid_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.amount_paid_type IS 'F = Flat amount
P = Percentage
';


--
-- Name: COLUMN hr_benefit_join_h.amount_paid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.amount_paid IS 'Amount contributed by person if person is provider';


--
-- Name: COLUMN hr_benefit_join_h.amount_covered; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.amount_covered IS 'Amount covered by benefit if applicable';


--
-- Name: COLUMN hr_benefit_join_h.change_description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.change_description IS 'Reason benefit was changed';


--
-- Name: COLUMN hr_benefit_join_h.benefit_approved; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.benefit_approved IS 'Is benefit approved by administrator?
  Y = Yes - approved
  N = No - not approved
  R = Approval Rejected';


--
-- Name: COLUMN hr_benefit_join_h.requested_cost_period; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_join_h.requested_cost_period IS '''W'' weekly, ''M'' monthly, ''Q'' quarterly, ''S'' semi-annually, ''A'' annually';


--
-- Name: hr_benefit_package; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit_package (
    package_id character(16) NOT NULL,
    name character varying(80) NOT NULL
);


ALTER TABLE public.hr_benefit_package OWNER TO postgres;

--
-- Name: hr_benefit_package_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit_package_join (
    package_id character(16) NOT NULL,
    benefit_id character(16) NOT NULL
);


ALTER TABLE public.hr_benefit_package_join OWNER TO postgres;

--
-- Name: hr_benefit_project_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit_project_join (
    project_id character(16) NOT NULL,
    benefit_config_id character(16) NOT NULL
);


ALTER TABLE public.hr_benefit_project_join OWNER TO postgres;

--
-- Name: TABLE hr_benefit_project_join; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_benefit_project_join IS 'Used to asociate time related benefits to projects people can apply time to.';


--
-- Name: hr_benefit_rider; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_benefit_rider (
    benefit_rider_id character(16) NOT NULL,
    base_benefit_id character(16) NOT NULL,
    rider_benefit_id character(16) NOT NULL,
    hidden character(1) DEFAULT 'N'::bpchar NOT NULL,
    required character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT hr_benefit_rider_hid_chk CHECK (((hidden = 'Y'::bpchar) OR (hidden = 'N'::bpchar))),
    CONSTRAINT hr_benefit_rider_req_chk CHECK (((required = 'Y'::bpchar) OR (required = 'N'::bpchar)))
);


ALTER TABLE public.hr_benefit_rider OWNER TO postgres;

--
-- Name: COLUMN hr_benefit_rider.base_benefit_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_rider.base_benefit_id IS 'Base benefit that this rider benefit is associated with, links to "benefit_id" column of hr_benefit table ';


--
-- Name: COLUMN hr_benefit_rider.rider_benefit_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_rider.rider_benefit_id IS 'benefit that is riding, links to "benefit_id" column of hr_benefit table';


--
-- Name: COLUMN hr_benefit_rider.hidden; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_rider.hidden IS 'Is this rider hidden, meaning it does not show up on screens or reports (''Y'' or ''N'')';


--
-- Name: COLUMN hr_benefit_rider.required; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_benefit_rider.required IS 'Is this rider required, meaning enrolling in the base benefit automatically enrolls in this rider (''Y'' or ''N'')';


--
-- Name: hr_billing_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_billing_status (
    billing_status_id character(16) NOT NULL,
    name character varying(60) NOT NULL
);


ALTER TABLE public.hr_billing_status OWNER TO postgres;

--
-- Name: hr_billing_status_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_billing_status_history (
    billing_status_hist_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    billing_status_id character(16) NOT NULL,
    start_date integer NOT NULL,
    final_date integer NOT NULL
);


ALTER TABLE public.hr_billing_status_history OWNER TO postgres;

--
-- Name: hr_checklist_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_checklist_detail (
    checklist_detail_id character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    checklist_item_id character(16) NOT NULL,
    date_completed integer DEFAULT 0 NOT NULL,
    supervisor_id character(16) NOT NULL,
    time_completed integer DEFAULT '-1'::integer NOT NULL
);


ALTER TABLE public.hr_checklist_detail OWNER TO postgres;

--
-- Name: TABLE hr_checklist_detail; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_checklist_detail IS 'Details on checklist items for employee';


--
-- Name: COLUMN hr_checklist_detail.employee_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_checklist_detail.employee_id IS 'The employee the checklist item applies to';


--
-- Name: COLUMN hr_checklist_detail.checklist_item_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_checklist_detail.checklist_item_id IS 'Which checklist item';


--
-- Name: COLUMN hr_checklist_detail.supervisor_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_checklist_detail.supervisor_id IS 'Employee who verified the checklist item';


--
-- Name: hr_checklist_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_checklist_item (
    item_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    name character varying(120) NOT NULL,
    employee_status_id character(16) NOT NULL,
    seq smallint NOT NULL,
    first_active_date integer DEFAULT 0 NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    responsibility character(1) DEFAULT 'H'::bpchar NOT NULL,
    screen_id character(16),
    screen_group_id character(16),
    company_form_id character(16),
    CONSTRAINT hr_chklst_itm_resp_chk CHECK (((responsibility = 'E'::bpchar) OR (responsibility = 'M'::bpchar) OR (responsibility = 'H'::bpchar)))
);


ALTER TABLE public.hr_checklist_item OWNER TO postgres;

--
-- Name: COLUMN hr_checklist_item.employee_status_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_checklist_item.employee_status_id IS 'What employee status this checklist item applies to';


--
-- Name: COLUMN hr_checklist_item.seq; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_checklist_item.seq IS 'Sequence';


--
-- Name: COLUMN hr_checklist_item.first_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_checklist_item.first_active_date IS 'Date item first bacame active';


--
-- Name: COLUMN hr_checklist_item.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_checklist_item.last_active_date IS 'Last date item active';


--
-- Name: COLUMN hr_checklist_item.responsibility; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_checklist_item.responsibility IS 'Employee
Manager
HR Dept.';


--
-- Name: hr_eeo1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_eeo1 (
    eeo1_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    beg_period integer DEFAULT 0 NOT NULL,
    end_period integer DEFAULT 0 NOT NULL,
    common_ownership character(1) DEFAULT ' '::bpchar NOT NULL,
    gov_contractor character(1) DEFAULT ' '::bpchar NOT NULL,
    date_created integer DEFAULT 0 NOT NULL,
    date_uploaded integer DEFAULT 0 NOT NULL,
    transmitted_data text,
    CONSTRAINT eeo1_common_owner_chk CHECK (((common_ownership = ' '::bpchar) OR (common_ownership = 'Y'::bpchar) OR (common_ownership = 'N'::bpchar))),
    CONSTRAINT eeo1_gov_chk CHECK (((gov_contractor = ' '::bpchar) OR (gov_contractor = 'Y'::bpchar) OR (gov_contractor = 'N'::bpchar)))
);


ALTER TABLE public.hr_eeo1 OWNER TO postgres;

--
-- Name: COLUMN hr_eeo1.common_ownership; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_eeo1.common_ownership IS ''' '' = unknown
Y = Yes
N = No
';


--
-- Name: COLUMN hr_eeo1.gov_contractor; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_eeo1.gov_contractor IS ''' '' = unknown
Y = Yes
N = No
';


--
-- Name: hr_eeo_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_eeo_category (
    eeo_category_id character(16) NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE public.hr_eeo_category OWNER TO postgres;

--
-- Name: TABLE hr_eeo_category; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_eeo_category IS 'Type of worker they are';


--
-- Name: hr_eeo_race; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_eeo_race (
    eeo_id character(16) NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE public.hr_eeo_race OWNER TO postgres;

--
-- Name: TABLE hr_eeo_race; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_eeo_race IS 'Race of employee';


--
-- Name: hr_emergency_contact; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_emergency_contact (
    contact_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    contact_name character varying(45) NOT NULL,
    home_phone character varying(20),
    relationship character varying(15),
    address character varying(160),
    work_phone character varying(20),
    cell_phone character varying(20)
);


ALTER TABLE public.hr_emergency_contact OWNER TO postgres;

--
-- Name: hr_empl_dependent; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_empl_dependent (
    relationship_id character(16) NOT NULL,
    dependent_id character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    relationship character varying(20),
    relationship_type character(1) NOT NULL,
    date_added integer DEFAULT 0 NOT NULL,
    date_inactive integer DEFAULT 0 NOT NULL,
    record_type character(1) DEFAULT 'R'::bpchar NOT NULL,
    CONSTRAINT hr_empl_dep_record_type_chk CHECK (((record_type = 'R'::bpchar) OR (record_type = 'C'::bpchar))),
    CONSTRAINT hr_empl_dep_rel_type_chk CHECK (((relationship_type = 'S'::bpchar) OR (relationship_type = 'C'::bpchar) OR (relationship_type = 'O'::bpchar)))
);


ALTER TABLE public.hr_empl_dependent OWNER TO postgres;

--
-- Name: COLUMN hr_empl_dependent.employee_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_dependent.employee_id IS 'Employee linked to this dependent';


--
-- Name: COLUMN hr_empl_dependent.relationship; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_dependent.relationship IS 'Relationship to dependent if other type';


--
-- Name: COLUMN hr_empl_dependent.relationship_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_dependent.relationship_type IS 'Spouse / Child / Other';


--
-- Name: COLUMN hr_empl_dependent.date_added; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_dependent.date_added IS 'Date dependent linked to employee ';


--
-- Name: COLUMN hr_empl_dependent.date_inactive; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_dependent.date_inactive IS 'Date dependent became inactive or no longer a dependent';


--
-- Name: COLUMN hr_empl_dependent.record_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_dependent.record_type IS 'R = Real record
C = Change request';


--
-- Name: hr_empl_dependent_cr; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_empl_dependent_cr (
    change_request_id character(16) NOT NULL,
    real_record_id character(16),
    change_status character(1) NOT NULL,
    request_time timestamp with time zone NOT NULL,
    change_record_id character(16) NOT NULL,
    requestor_id character(16) NOT NULL,
    approver_id character(16),
    approval_time timestamp with time zone,
    project_id character(16) NOT NULL,
    CONSTRAINT hr_empl_dep_rcr_status_chk CHECK (((change_status = 'P'::bpchar) OR (change_status = 'A'::bpchar) OR (change_status = 'R'::bpchar)))
);


ALTER TABLE public.hr_empl_dependent_cr OWNER TO postgres;

--
-- Name: TABLE hr_empl_dependent_cr; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_empl_dependent_cr IS 'Holds record change requests for the hr_empl_dependent table.';


--
-- Name: COLUMN hr_empl_dependent_cr.change_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_dependent_cr.change_status IS 'P = Pending request
A = Approved
R = Rejected';


--
-- Name: hr_empl_eval_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_empl_eval_detail (
    detail_id character(16) NOT NULL,
    eval_id character(16) NOT NULL,
    cat_id character(16) NOT NULL,
    score smallint DEFAULT 0 NOT NULL,
    notes text,
    e_score smallint DEFAULT 0 NOT NULL,
    e_notes text,
    p_notes text
);


ALTER TABLE public.hr_empl_eval_detail OWNER TO postgres;

--
-- Name: TABLE hr_empl_eval_detail; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_empl_eval_detail IS 'Employee evaluation detail';


--
-- Name: COLUMN hr_empl_eval_detail.eval_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_eval_detail.eval_id IS 'Which evaluation it applies to';


--
-- Name: COLUMN hr_empl_eval_detail.cat_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_eval_detail.cat_id IS 'Evaluation category ID';


--
-- Name: COLUMN hr_empl_eval_detail.score; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_eval_detail.score IS 'Supervisor entered score';


--
-- Name: COLUMN hr_empl_eval_detail.notes; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_eval_detail.notes IS 'Supervisor entered notes';


--
-- Name: COLUMN hr_empl_eval_detail.e_score; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_eval_detail.e_score IS 'Employee entered score';


--
-- Name: COLUMN hr_empl_eval_detail.e_notes; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_eval_detail.e_notes IS 'Employee entered notes';


--
-- Name: COLUMN hr_empl_eval_detail.p_notes; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_empl_eval_detail.p_notes IS 'Private notes';


--
-- Name: hr_employee_beneficiary; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_employee_beneficiary (
    beneficiary_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    benefit_join_id character(16) NOT NULL,
    beneficiary character varying(60) NOT NULL,
    relationship character varying(10) NOT NULL,
    benefit_percent smallint DEFAULT 0 NOT NULL,
    dob integer DEFAULT 0 NOT NULL,
    ssn character varying(32),
    address character varying(160),
    beneficiary_type character(1) DEFAULT 'P'::bpchar NOT NULL,
    CONSTRAINT hr_empbeneferc_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT hr_empbeny_type_chk CHECK (((beneficiary_type = 'P'::bpchar) OR (beneficiary_type = 'C'::bpchar)))
);


ALTER TABLE public.hr_employee_beneficiary OWNER TO postgres;

--
-- Name: COLUMN hr_employee_beneficiary.record_change_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary.record_change_date IS 'Date/time record last changed';


--
-- Name: COLUMN hr_employee_beneficiary.record_change_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary.record_change_type IS 'New / Modified / Deleted';


--
-- Name: COLUMN hr_employee_beneficiary.record_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary.record_person_id IS 'Person who changed the record';


--
-- Name: COLUMN hr_employee_beneficiary.dob; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary.dob IS 'Date of birth';


--
-- Name: COLUMN hr_employee_beneficiary.ssn; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary.ssn IS 'Encrypted Social security number';


--
-- Name: COLUMN hr_employee_beneficiary.beneficiary_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary.beneficiary_type IS 'Primary or Contingent';


--
-- Name: hr_employee_beneficiary_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_employee_beneficiary_h (
    history_id character(32) NOT NULL,
    beneficiary_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    benefit_join_id character(16) NOT NULL,
    beneficiary character varying(60) NOT NULL,
    relationship character varying(10) NOT NULL,
    benefit_percent smallint DEFAULT 0 NOT NULL,
    dob integer DEFAULT 0 NOT NULL,
    ssn character varying(32),
    address character varying(160),
    beneficiary_type character(1) DEFAULT 'P'::bpchar NOT NULL,
    CONSTRAINT hr_empbenefercy_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT hr_empbenh_type_chk CHECK (((beneficiary_type = 'P'::bpchar) OR (beneficiary_type = 'C'::bpchar)))
);


ALTER TABLE public.hr_employee_beneficiary_h OWNER TO postgres;

--
-- Name: COLUMN hr_employee_beneficiary_h.history_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary_h.history_id IS 'A GUID';


--
-- Name: COLUMN hr_employee_beneficiary_h.record_change_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary_h.record_change_date IS 'Date/time record last changed';


--
-- Name: COLUMN hr_employee_beneficiary_h.record_change_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary_h.record_change_type IS 'New / Modified / Deleted';


--
-- Name: COLUMN hr_employee_beneficiary_h.record_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary_h.record_person_id IS 'Person who changed the record';


--
-- Name: COLUMN hr_employee_beneficiary_h.dob; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary_h.dob IS 'Date of birth';


--
-- Name: COLUMN hr_employee_beneficiary_h.ssn; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary_h.ssn IS 'Encrypted Social security number';


--
-- Name: COLUMN hr_employee_beneficiary_h.beneficiary_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_beneficiary_h.beneficiary_type IS 'Primary or Contingent';


--
-- Name: hr_employee_eval; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_employee_eval (
    employee_eval_id character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    eval_date integer NOT NULL,
    supervisor_id character(16) NOT NULL,
    next_eval_date integer DEFAULT 0 NOT NULL,
    description character varying(80),
    comments text,
    state character(1) NOT NULL,
    e_comments text,
    p_comments text,
    final_date integer DEFAULT 0 NOT NULL,
    CONSTRAINT hr_employee_eval_state_chk CHECK (((state = 'E'::bpchar) OR (state = 'S'::bpchar) OR (state = 'F'::bpchar)))
);


ALTER TABLE public.hr_employee_eval OWNER TO postgres;

--
-- Name: TABLE hr_employee_eval; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_employee_eval IS 'Employee evaluations';


--
-- Name: COLUMN hr_employee_eval.employee_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_eval.employee_id IS 'The employee who had the evaluation done on them';


--
-- Name: COLUMN hr_employee_eval.eval_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_eval.eval_date IS 'Date evaluation performed';


--
-- Name: COLUMN hr_employee_eval.supervisor_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_eval.supervisor_id IS 'Employee performing evaluation';


--
-- Name: COLUMN hr_employee_eval.next_eval_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_eval.next_eval_date IS 'Date of next evaluation';


--
-- Name: COLUMN hr_employee_eval.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_eval.description IS 'Description of evaluation';


--
-- Name: COLUMN hr_employee_eval.comments; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_eval.comments IS 'Detail comments of the evaluation as a whole';


--
-- Name: COLUMN hr_employee_eval.state; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_eval.state IS 'Employee edit
Supervisor edit
Finalized';


--
-- Name: COLUMN hr_employee_eval.e_comments; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_eval.e_comments IS 'Employee comments';


--
-- Name: COLUMN hr_employee_eval.p_comments; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_eval.p_comments IS 'Private comments';


--
-- Name: COLUMN hr_employee_eval.final_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_eval.final_date IS 'Date evaluation finalized';


--
-- Name: hr_employee_event; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_employee_event (
    event_id character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    supervisor_id character(16) NOT NULL,
    event_date integer NOT NULL,
    employee_notified character(1) NOT NULL,
    summary character varying(60) NOT NULL,
    detail character varying(2000),
    date_notified integer DEFAULT 0 NOT NULL,
    event_type character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT hr_emp_event_type_chk CHECK (((event_type = 'N'::bpchar) OR (event_type = 'M'::bpchar) OR (event_type = 'L'::bpchar))),
    CONSTRAINT hr_emp_evt_event_date_chk CHECK ((event_date > 20050101)),
    CONSTRAINT hr_emp_evt_notified_chk CHECK (((employee_notified = 'Y'::bpchar) OR (employee_notified = 'N'::bpchar)))
);


ALTER TABLE public.hr_employee_event OWNER TO postgres;

--
-- Name: COLUMN hr_employee_event.event_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_event.event_type IS 'Normal
Mobile
Label';


--
-- Name: hr_employee_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_employee_status (
    status_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    name character varying(30) NOT NULL,
    active character(1) DEFAULT 'N'::bpchar NOT NULL,
    date_type character(1) DEFAULT 'S'::bpchar NOT NULL,
    benefit_type character(1) NOT NULL,
    CONSTRAINT hr_empl_stat_date_typ_chk CHECK (((date_type = 'S'::bpchar) OR (date_type = 'F'::bpchar))),
    CONSTRAINT hr_employee_status_active_chk CHECK (((active = 'Y'::bpchar) OR (active = 'N'::bpchar))),
    CONSTRAINT hr_empstat_bentyp_chk CHECK (((benefit_type = 'B'::bpchar) OR (benefit_type = 'C'::bpchar) OR (benefit_type = 'N'::bpchar)))
);


ALTER TABLE public.hr_employee_status OWNER TO postgres;

--
-- Name: TABLE hr_employee_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_employee_status IS 'Active / Inactive / Terminated';


--
-- Name: COLUMN hr_employee_status.active; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_status.active IS 'Is employee active';


--
-- Name: COLUMN hr_employee_status.date_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_status.date_type IS 'Start or Final date';


--
-- Name: COLUMN hr_employee_status.benefit_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_employee_status.benefit_type IS 'B = Non-Cobra benefits (active employee)
C = Cobra benefits
N = No benefits';


--
-- Name: hr_eval_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_eval_category (
    eval_cat_id character(16) NOT NULL,
    name character varying(30) NOT NULL,
    description character varying(1000),
    weight smallint DEFAULT 0 NOT NULL,
    org_group_id character(16) NOT NULL,
    first_active_date integer DEFAULT 0 NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.hr_eval_category OWNER TO postgres;

--
-- Name: TABLE hr_eval_category; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_eval_category IS 'Employee evaluation categories';


--
-- Name: COLUMN hr_eval_category.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_eval_category.description IS 'Detailed description of the category';


--
-- Name: COLUMN hr_eval_category.weight; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_eval_category.weight IS 'Weight associated with the category';


--
-- Name: COLUMN hr_eval_category.first_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_eval_category.first_active_date IS 'Date record became active';


--
-- Name: COLUMN hr_eval_category.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_eval_category.last_active_date IS 'Last date record is active';


--
-- Name: hr_note_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_note_category (
    cat_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    first_active_date integer DEFAULT 0 NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    name character varying(30) NOT NULL,
    cat_code character varying(5)
);


ALTER TABLE public.hr_note_category OWNER TO postgres;

--
-- Name: COLUMN hr_note_category.cat_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_note_category.cat_code IS 'Code used to refer to the field from other areas';


--
-- Name: hr_position; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_position (
    position_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    first_active_date integer DEFAULT 0 NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    position_name character varying(30) NOT NULL,
    benefit_class_id character(16),
    weekly_per_diem real DEFAULT 0 NOT NULL,
    seqno smallint DEFAULT 0 NOT NULL,
    applicant_default character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT hr_position_default_chk CHECK (((applicant_default = 'Y'::bpchar) OR (applicant_default = 'N'::bpchar)))
);


ALTER TABLE public.hr_position OWNER TO postgres;

--
-- Name: TABLE hr_position; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_position IS 'Various positions throughout the company';


--
-- Name: COLUMN hr_position.benefit_class_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_position.benefit_class_id IS 'Default benefit class for this position';


--
-- Name: COLUMN hr_position.applicant_default; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_position.applicant_default IS 'Y/N
Y if default position for applicant system
';


--
-- Name: hr_training_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_training_category (
    cat_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    name character varying(30) NOT NULL,
    training_type smallint NOT NULL,
    client_id character(16),
    required character(1) DEFAULT 'N'::bpchar NOT NULL,
    hours real DEFAULT 0 NOT NULL,
    CONSTRAINT hr_training_cat_req_chk CHECK (((required = 'Y'::bpchar) OR (required = 'N'::bpchar)))
);


ALTER TABLE public.hr_training_category OWNER TO postgres;

--
-- Name: COLUMN hr_training_category.training_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_training_category.training_type IS '1=training
2=certification';


--
-- Name: COLUMN hr_training_category.client_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_training_category.client_id IS 'Client org group associated with or NULL if not client specific';


--
-- Name: COLUMN hr_training_category.required; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_training_category.required IS 'Yes / No';


--
-- Name: COLUMN hr_training_category.hours; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_training_category.hours IS 'Normal number of hours for this type of training';


--
-- Name: hr_training_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_training_detail (
    training_id character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    training_date integer NOT NULL,
    cat_id character(16) NOT NULL,
    expire_date integer DEFAULT 0 NOT NULL,
    training_hours real NOT NULL
);


ALTER TABLE public.hr_training_detail OWNER TO postgres;

--
-- Name: TABLE hr_training_detail; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.hr_training_detail IS 'Detail on employee training';


--
-- Name: COLUMN hr_training_detail.training_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_training_detail.training_date IS 'Date of training';


--
-- Name: COLUMN hr_training_detail.cat_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_training_detail.cat_id IS 'Training category ID';


--
-- Name: COLUMN hr_training_detail.expire_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_training_detail.expire_date IS 'Date certification expires or training needs to be renewed';


--
-- Name: COLUMN hr_training_detail.training_hours; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.hr_training_detail.training_hours IS 'Number of training hours';


--
-- Name: import_column; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.import_column (
    import_column_id character(16) NOT NULL,
    import_filter_id character(16) NOT NULL,
    column_order smallint NOT NULL,
    column_name character varying(30) NOT NULL,
    start_pos smallint NOT NULL,
    last_pos smallint NOT NULL,
    date_format character varying(20)
);


ALTER TABLE public.import_column OWNER TO postgres;

--
-- Name: COLUMN import_column.column_order; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_column.column_order IS 'This is the order the column is shown to the user.  It comes from the backend.';


--
-- Name: COLUMN import_column.column_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_column.column_name IS 'Comes from the backend.';


--
-- Name: COLUMN import_column.start_pos; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_column.start_pos IS 'Field number for delimited files.  Starting character position for fixed length files.  The first column is 1 (not 0).';


--
-- Name: COLUMN import_column.last_pos; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_column.last_pos IS 'For fixed length files this is the index of the last position.  It is unused for delimited files.';


--
-- Name: COLUMN import_column.date_format; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_column.date_format IS 'The options that go in this field come from the backend.';


--
-- Name: import_filter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.import_filter (
    import_filter_id character(16) NOT NULL,
    import_type_id character(16) NOT NULL,
    import_filter_name character varying(50) NOT NULL,
    import_filter_desc character varying(120),
    filter_value character varying(25)
);


ALTER TABLE public.import_filter OWNER TO postgres;

--
-- Name: COLUMN import_filter.import_filter_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_filter.import_filter_name IS 'Use "(no filter)" if this record represents a non-filter.';


--
-- Name: import_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.import_type (
    import_type_id character(16) NOT NULL,
    company_id character(16),
    import_program_name character varying(30) NOT NULL,
    import_name character varying(50) NOT NULL,
    import_source character varying(50),
    file_format character(1) NOT NULL,
    delim_char character(1) DEFAULT ','::bpchar NOT NULL,
    quote_char character(1) DEFAULT '"'::bpchar NOT NULL,
    filter_start_pos smallint DEFAULT 0 NOT NULL,
    filter_end_pos smallint DEFAULT 0 NOT NULL,
    CONSTRAINT import_type_format_chk CHECK (((file_format = 'F'::bpchar) OR (file_format = 'D'::bpchar)))
);


ALTER TABLE public.import_type OWNER TO postgres;

--
-- Name: COLUMN import_type.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_type.company_id IS 'Null means it applies to all companies';


--
-- Name: COLUMN import_type.import_program_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_type.import_program_name IS 'This is a field that comes from the backend.';


--
-- Name: COLUMN import_type.import_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_type.import_name IS 'Arbitrary name given by the user to identify the import';


--
-- Name: COLUMN import_type.import_source; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_type.import_source IS 'Where the data comes from';


--
-- Name: COLUMN import_type.file_format; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_type.file_format IS 'Fixed length / comma Delimited';


--
-- Name: COLUMN import_type.filter_start_pos; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_type.filter_start_pos IS '0 means no filter. Field number for delimited files.  Starting character position for fixed length files.  The first column is 1 (not 0).';


--
-- Name: COLUMN import_type.filter_end_pos; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.import_type.filter_end_pos IS 'For fixed length files this is the index of the last position.  It is unused for delimited files.';


--
-- Name: insurance_location_code; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.insurance_location_code (
    insurance_location_code_id character(16) NOT NULL,
    benefit_class_id character(16) NOT NULL,
    employee_status_id character(16) NOT NULL,
    benefit_id character(16) NOT NULL,
    ins_location_code character varying(10) NOT NULL
);


ALTER TABLE public.insurance_location_code OWNER TO postgres;

--
-- Name: interface_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.interface_log (
    interface_log_id character(16) NOT NULL,
    interface_code smallint NOT NULL,
    last_run timestamp with time zone NOT NULL,
    status_message character varying(80),
    status_code smallint NOT NULL,
    company_id character(16) NOT NULL
);


ALTER TABLE public.interface_log OWNER TO postgres;

--
-- Name: inventory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inventory (
    inventory_id character(16) NOT NULL,
    product_id character(16) NOT NULL,
    location_id character(16) NOT NULL,
    reorder_level integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.inventory OWNER TO postgres;

--
-- Name: invoice; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoice (
    invoice_id character(16) NOT NULL,
    description text,
    create_date timestamp with time zone,
    export_date timestamp with time zone,
    ar_account_id character(16),
    customer_id character(16) NOT NULL,
    accounting_invoice_identifier character(16),
    invoice_type character(1) NOT NULL,
    person_id character(16),
    payed_off character(1) DEFAULT 'N'::bpchar NOT NULL,
    purchase_order character varying(20),
    payment_terms smallint DEFAULT 0 NOT NULL,
    CONSTRAINT invoice_payed_chk CHECK (((payed_off = 'Y'::bpchar) OR (payed_off = 'N'::bpchar))),
    CONSTRAINT invoice_type_chk CHECK (((invoice_type = 'C'::bpchar) OR (invoice_type = 'P'::bpchar)))
);


ALTER TABLE public.invoice OWNER TO postgres;

--
-- Name: TABLE invoice; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.invoice IS 'an invoice submitted to the accounting system';


--
-- Name: COLUMN invoice.invoice_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice.invoice_id IS 'Primary key for invoice table';


--
-- Name: COLUMN invoice.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice.description IS 'invoice description';


--
-- Name: COLUMN invoice.create_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice.create_date IS 'Date this invoice was created';


--
-- Name: COLUMN invoice.export_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice.export_date IS 'The date this invoice was exported to an accounting system.';


--
-- Name: COLUMN invoice.ar_account_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice.ar_account_id IS 'Accounting System AR account id';


--
-- Name: COLUMN invoice.customer_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice.customer_id IS 'join to client company being invoiced.';


--
-- Name: COLUMN invoice.accounting_invoice_identifier; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice.accounting_invoice_identifier IS 'This stores the invoice ID used by the accounting system.';


--
-- Name: COLUMN invoice.invoice_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice.invoice_type IS 'Company / Person';


--
-- Name: COLUMN invoice.payed_off; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice.payed_off IS 'Is invoice paid in full?';


--
-- Name: COLUMN invoice.payment_terms; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice.payment_terms IS 'Number of days';


--
-- Name: invoice_line_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoice_line_item (
    invoice_line_item_id character(16) NOT NULL,
    adj_hours real NOT NULL,
    adj_rate double precision DEFAULT 0 NOT NULL,
    invoice_id character(16),
    product_id character(16),
    expense_account_id character(16),
    description character varying(160),
    billing_type character(1) NOT NULL,
    amount double precision NOT NULL,
    benefit_join_id character(16),
    project_id character(16),
    CONSTRAINT invoice_line_type_chk CHECK (((billing_type = 'H'::bpchar) OR (billing_type = 'P'::bpchar) OR (billing_type = 'D'::bpchar)))
);


ALTER TABLE public.invoice_line_item OWNER TO postgres;

--
-- Name: TABLE invoice_line_item; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.invoice_line_item IS 'Line items for invoices';


--
-- Name: COLUMN invoice_line_item.invoice_line_item_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice_line_item.invoice_line_item_id IS 'primary key';


--
-- Name: COLUMN invoice_line_item.adj_hours; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice_line_item.adj_hours IS 'The adjusted hour total for this line item.';


--
-- Name: COLUMN invoice_line_item.adj_rate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice_line_item.adj_rate IS 'The adjusted rate total for this line item.';


--
-- Name: COLUMN invoice_line_item.invoice_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice_line_item.invoice_id IS 'key to invoice table';


--
-- Name: COLUMN invoice_line_item.product_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice_line_item.product_id IS 'key to the product_service table.';


--
-- Name: COLUMN invoice_line_item.expense_account_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice_line_item.expense_account_id IS 'key to the gl_account table.';


--
-- Name: COLUMN invoice_line_item.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice_line_item.description IS 'description for the invoice line item.';


--
-- Name: COLUMN invoice_line_item.billing_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice_line_item.billing_type IS 'Hours / Project / Dollars (unrelated to time or project billing, just an expense)
Hours uses adjHours * adjRate
Dollars uses adjHours * adjRate
Project uses amount';


--
-- Name: COLUMN invoice_line_item.amount; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice_line_item.amount IS 'Dollar amount if unrelated to time';


--
-- Name: COLUMN invoice_line_item.benefit_join_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice_line_item.benefit_join_id IS '(use no constraint here)';


--
-- Name: COLUMN invoice_line_item.project_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.invoice_line_item.project_id IS 'Used only if the project is billed by the project rather than hourly.';


--
-- Name: item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.item (
    item_id character(16) NOT NULL,
    product_id character(16) NOT NULL,
    location_id character(16),
    parent_item_id character(16),
    lot_id character(16) NOT NULL,
    serial_number character varying(30),
    quantity integer NOT NULL,
    item_particulars character varying(256),
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    item_name character varying(80),
    manufacturer character varying(50),
    model character varying(20),
    date_purchased integer DEFAULT 0 NOT NULL,
    original_cost real DEFAULT 0 NOT NULL,
    purchased_from character varying(20),
    notes character varying(256),
    item_status character(1) DEFAULT 'C'::bpchar NOT NULL,
    retirement_notes character varying(256),
    retirement_date integer DEFAULT 0 NOT NULL,
    reimbursement_status character(1) DEFAULT 'C'::bpchar NOT NULL,
    reimbursement_person_id character(16),
    requested_reimbursement_amount real DEFAULT 0 NOT NULL,
    reimbursement_amount_received real DEFAULT 0 NOT NULL,
    date_reimbursement_received integer DEFAULT 0 NOT NULL,
    person_accepting_reimbursement character(16),
    CONSTRAINT item_change_type_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT item_reimbursement_status_chk CHECK (((reimbursement_status = 'C'::bpchar) OR (reimbursement_status = 'Y'::bpchar) OR (reimbursement_status = 'N'::bpchar) OR (reimbursement_status = 'R'::bpchar))),
    CONSTRAINT item_status_chk CHECK (((item_status = 'C'::bpchar) OR (item_status = 'R'::bpchar) OR (item_status = 'L'::bpchar)))
);


ALTER TABLE public.item OWNER TO postgres;

--
-- Name: COLUMN item.item_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item.item_status IS 'C= Current
R = Retired
L = Lost';


--
-- Name: COLUMN item.retirement_notes; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item.retirement_notes IS 'Notes about item''s loss or retirement';


--
-- Name: COLUMN item.reimbursement_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item.reimbursement_status IS 'C = Not expecting reimbursement (item is current)
Y = Yes, reimbursement open
N = No reimbursement expected
R = reimbursement has been received';


--
-- Name: COLUMN item.reimbursement_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item.reimbursement_person_id IS 'Worker reimbursement expected from';


--
-- Name: COLUMN item.requested_reimbursement_amount; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item.requested_reimbursement_amount IS 'Expected / requested reimbursement amount';


--
-- Name: item_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.item_h (
    history_id character(32) NOT NULL,
    item_id character(16) NOT NULL,
    product_id character(16) NOT NULL,
    location_id character(16),
    parent_item_id character(16),
    lot_id character(16) NOT NULL,
    serial_number character varying(30),
    quantity integer NOT NULL,
    item_particulars character varying(256),
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    item_name character varying(80),
    manufacturer character varying(50),
    model character varying(20),
    date_purchased integer DEFAULT 0 NOT NULL,
    original_cost real DEFAULT 0 NOT NULL,
    purchased_from character varying(20),
    notes character varying(256),
    item_status character(1) DEFAULT 'C'::bpchar NOT NULL,
    retirement_notes character varying(256),
    retirement_date integer DEFAULT 0 NOT NULL,
    reimbursement_status character(1) DEFAULT 'C'::bpchar NOT NULL,
    reimbursement_person_id character(16),
    requested_reimbursement_amount real DEFAULT 0 NOT NULL,
    reimbursement_amount_received real DEFAULT 0 NOT NULL,
    date_reimbursement_received integer DEFAULT 0 NOT NULL,
    person_accepting_reimbursement character(16),
    CONSTRAINT item_chg_type_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT item_h_item_status_chk CHECK (((item_status = 'C'::bpchar) OR (item_status = 'R'::bpchar) OR (item_status = 'L'::bpchar))),
    CONSTRAINT item_h_reimbursement_status_chk CHECK (((reimbursement_status = 'C'::bpchar) OR (reimbursement_status = 'Y'::bpchar) OR (reimbursement_status = 'N'::bpchar) OR (reimbursement_status = 'R'::bpchar)))
);


ALTER TABLE public.item_h OWNER TO postgres;

--
-- Name: COLUMN item_h.history_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item_h.history_id IS 'A GUID';


--
-- Name: COLUMN item_h.item_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item_h.item_status IS 'C = Current
R = Retired
L = Lost';


--
-- Name: COLUMN item_h.reimbursement_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item_h.reimbursement_status IS 'C = Not expecting reimbursement (item is current)
Y = Yes, reimbursement open
N = No reimbursement expected
R = reimbursement has been receivedw';


--
-- Name: item_inspection; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.item_inspection (
    item_inspection_id character(16) NOT NULL,
    item_id character(16) NOT NULL,
    inspection_date integer NOT NULL,
    person_inspecting character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_person_id character(16) NOT NULL,
    inspection_status character(1) NOT NULL,
    inspection_comments character varying(512),
    CONSTRAINT item_inspection_status_chk CHECK (((inspection_status = 'P'::bpchar) OR (inspection_status = 'F'::bpchar)))
);


ALTER TABLE public.item_inspection OWNER TO postgres;

--
-- Name: COLUMN item_inspection.person_inspecting; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item_inspection.person_inspecting IS 'Person who performed the inspection';


--
-- Name: COLUMN item_inspection.record_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item_inspection.record_person_id IS 'Person who added this record';


--
-- Name: COLUMN item_inspection.inspection_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item_inspection.inspection_status IS 'Pass or Fail';


--
-- Name: life_event; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.life_event (
    life_event_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    event_date integer NOT NULL,
    bcr_id character(16) NOT NULL,
    description character varying(120),
    date_reported integer NOT NULL,
    reporting_person_id character(16) NOT NULL
);


ALTER TABLE public.life_event OWNER TO postgres;

--
-- Name: COLUMN life_event.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.life_event.person_id IS 'The person the event affects';


--
-- Name: COLUMN life_event.event_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.life_event.event_date IS 'The date the event did or is to take place';


--
-- Name: COLUMN life_event.bcr_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.life_event.bcr_id IS 'The life event (change reason) that occurred';


--
-- Name: COLUMN life_event.date_reported; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.life_event.date_reported IS 'Date the event was entered into the system (not the date the event took place)';


--
-- Name: COLUMN life_event.reporting_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.life_event.reporting_person_id IS 'The person logged in who made the event change';


--
-- Name: location_cost; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.location_cost (
    location_cost_id character(16) NOT NULL,
    description character varying(80) NOT NULL,
    location_cost double precision DEFAULT 0 NOT NULL
);


ALTER TABLE public.location_cost OWNER TO postgres;

--
-- Name: login_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.login_log (
    ltime timestamp with time zone NOT NULL,
    log_name character varying(50) NOT NULL,
    successful character(1),
    person_id character(16),
    address_ip character varying(40),
    address_url character varying(40),
    CONSTRAINT login_log_successful_check CHECK (((successful = 'Y'::bpchar) OR (successful = 'N'::bpchar)))
);


ALTER TABLE public.login_log OWNER TO postgres;

--
-- Name: TABLE login_log; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.login_log IS 'This table stores a log of login attempts';


--
-- Name: COLUMN login_log.ltime; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.login_log.ltime IS 'the time this login happened.';


--
-- Name: COLUMN login_log.log_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.login_log.log_name IS 'TODO: what is this for?';


--
-- Name: COLUMN login_log.successful; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.login_log.successful IS 'Y or N flag for if login was successful';


--
-- Name: COLUMN login_log.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.login_log.person_id IS 'join to person that logged in for successful attempts.';


--
-- Name: lot; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lot (
    lot_id character(16) NOT NULL,
    lot_number character varying(20),
    original_quantity integer NOT NULL,
    date_received integer NOT NULL,
    lot_cost double precision NOT NULL,
    lot_particulars character varying(256)
);


ALTER TABLE public.lot OWNER TO postgres;

--
-- Name: message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message (
    message_id character(16) NOT NULL,
    message text,
    from_person_id character(16),
    created_date timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    subject character varying(80),
    from_show character(1) DEFAULT 'Y'::bpchar NOT NULL,
    from_address character varying(60),
    from_name character varying(50),
    dont_send_body character(1) DEFAULT 'N'::bpchar NOT NULL,
    send_email character(1) DEFAULT 'N'::bpchar NOT NULL,
    send_text character(1) DEFAULT 'N'::bpchar NOT NULL,
    send_internal character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT from_show_chk CHECK (((from_show = 'Y'::bpchar) OR (from_show = 'N'::bpchar))),
    CONSTRAINT message_dont_send_body_chk CHECK (((dont_send_body = 'Y'::bpchar) OR (dont_send_body = 'N'::bpchar))),
    CONSTRAINT message_send_email_chk CHECK (((send_email = 'Y'::bpchar) OR (send_email = 'N'::bpchar))),
    CONSTRAINT message_send_internal_chk CHECK (((send_internal = 'Y'::bpchar) OR (send_internal = 'N'::bpchar))),
    CONSTRAINT message_send_text_chk CHECK (((send_text = 'Y'::bpchar) OR (send_text = 'N'::bpchar)))
);


ALTER TABLE public.message OWNER TO postgres;

--
-- Name: TABLE message; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.message IS 'This table stores messages sent from user to user.';


--
-- Name: COLUMN message.message_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.message.message_id IS 'Primary key';


--
-- Name: COLUMN message.message; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.message.message IS 'The message being sent.';


--
-- Name: COLUMN message.from_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.message.from_person_id IS 'Join to the person sending the message.  Null means the message came from the system.';


--
-- Name: COLUMN message.created_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.message.created_date IS 'Time the message was created.';


--
-- Name: COLUMN message.subject; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.message.subject IS 'Subject of the message';


--
-- Name: COLUMN message.from_show; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.message.from_show IS 'Should sender still see the message';


--
-- Name: COLUMN message.dont_send_body; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.message.dont_send_body IS 'Y=don''t send body of message over email, N=nornal';


--
-- Name: COLUMN message.send_email; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.message.send_email IS 'send via email too';


--
-- Name: COLUMN message.send_text; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.message.send_text IS 'Send via text message too';


--
-- Name: message_attachment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message_attachment (
    message_attachment_id character(16) NOT NULL,
    message_id character(16) NOT NULL,
    source_file_name character varying(80) NOT NULL
);


ALTER TABLE public.message_attachment OWNER TO postgres;

--
-- Name: message_to; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message_to (
    message_to_id character(16) NOT NULL,
    message_id character(16) NOT NULL,
    to_person_id character(16) NOT NULL,
    send_type character(1) NOT NULL,
    to_show character(1) DEFAULT 'Y'::bpchar NOT NULL,
    sent character(1) NOT NULL,
    when_viewed timestamp with time zone,
    to_address character varying(60),
    to_name character varying(60),
    date_received timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT message_to_sent_chk CHECK (((sent = 'Y'::bpchar) OR (sent = 'N'::bpchar))),
    CONSTRAINT message_to_show_chk CHECK (((to_show = 'Y'::bpchar) OR (to_show = 'N'::bpchar))),
    CONSTRAINT message_to_type_chk CHECK (((send_type = 'T'::bpchar) OR (send_type = 'C'::bpchar) OR (send_type = 'B'::bpchar)))
);


ALTER TABLE public.message_to OWNER TO postgres;

--
-- Name: COLUMN message_to.send_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.message_to.send_type IS 'To/Cc/Bcc';


--
-- Name: COLUMN message_to.to_show; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.message_to.to_show IS 'Y/N - does user still want to see this message?';


--
-- Name: COLUMN message_to.sent; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.message_to.sent IS 'Y/N - N means wasn''t able to send over regular email';


--
-- Name: onboarding_config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.onboarding_config (
    onboarding_config_id character(16) NOT NULL,
    company_id character(16),
    config_name character varying(50) NOT NULL
);


ALTER TABLE public.onboarding_config OWNER TO postgres;

--
-- Name: onboarding_task; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.onboarding_task (
    onboarding_task_id character(16) NOT NULL,
    onboarding_config_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    screen_id character(16) NOT NULL,
    task_name character varying(50) NOT NULL,
    description character varying(100),
    completed_by integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.onboarding_task OWNER TO postgres;

--
-- Name: onboarding_task_complete; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.onboarding_task_complete (
    task_complete_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    onboarding_task_id character(16) NOT NULL,
    completion_date integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.onboarding_task_complete OWNER TO postgres;

--
-- Name: org_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.org_group (
    org_group_id character(16) NOT NULL,
    group_name character varying(60) NOT NULL,
    org_group_type integer NOT NULL,
    owning_entity_id character(16),
    pay_periods_per_year smallint DEFAULT 0 NOT NULL,
    external_id character varying(20),
    default_project_id character(16),
    pay_schedule_id character(16),
    eeo1_unit character varying(7),
    eeo1_establishment character(1) DEFAULT 'N'::bpchar NOT NULL,
    eeo1_headquaters character(1) DEFAULT 'N'::bpchar NOT NULL,
    eeo1_filed_last_year character(1) DEFAULT 'N'::bpchar NOT NULL,
    eval_email_notify character(1) DEFAULT 'I'::bpchar NOT NULL,
    eval_email_first_days smallint DEFAULT 0 NOT NULL,
    eval_email_notify_days smallint DEFAULT 0 NOT NULL,
    eval_email_send_days character(5) DEFAULT 'NNNNN'::bpchar NOT NULL,
    new_week_begin_day smallint DEFAULT 0 NOT NULL,
    benefit_class_id character(16),
    org_group_guid character(32),
    timesheet_period_type character(1) DEFAULT 'I'::bpchar NOT NULL,
    timesheet_period_start_date integer DEFAULT 0 NOT NULL,
    timesheet_show_billable character(1) DEFAULT 'I'::bpchar NOT NULL,
    CONSTRAINT org_group_day_chk CHECK (((new_week_begin_day >= 0) AND (new_week_begin_day <= 7))),
    CONSTRAINT org_group_eeo1_ly_chk CHECK (((eeo1_filed_last_year = 'Y'::bpchar) OR (eeo1_filed_last_year = 'N'::bpchar))),
    CONSTRAINT org_group_est_chk CHECK (((eeo1_establishment = 'Y'::bpchar) OR (eeo1_establishment = 'N'::bpchar))),
    CONSTRAINT org_group_hq_chk CHECK (((eeo1_headquaters = 'Y'::bpchar) OR (eeo1_headquaters = 'N'::bpchar))),
    CONSTRAINT org_group_notify_chk CHECK (((eval_email_notify = 'Y'::bpchar) OR (eval_email_notify = 'N'::bpchar) OR (eval_email_notify = 'I'::bpchar))),
    CONSTRAINT org_group_period_type_chk CHECK (((timesheet_period_type = 'I'::bpchar) OR (timesheet_period_type = 'D'::bpchar) OR (timesheet_period_type = 'W'::bpchar) OR (timesheet_period_type = 'B'::bpchar) OR (timesheet_period_type = 'S'::bpchar) OR (timesheet_period_type = 'M'::bpchar))),
    CONSTRAINT org_group_show_bill_chk CHECK (((timesheet_show_billable = 'I'::bpchar) OR (timesheet_show_billable = 'Y'::bpchar) OR (timesheet_show_billable = 'N'::bpchar))),
    CONSTRAINT org_grp_type_chk CHECK (((org_group_type = 1) OR (org_group_type = 2) OR (org_group_type = 4) OR (org_group_type = 8) OR (org_group_type = 16)))
);


ALTER TABLE public.org_group OWNER TO postgres;

--
-- Name: TABLE org_group; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.org_group IS 'This table stores organization groups.';


--
-- Name: COLUMN org_group.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.org_group_id IS 'primary key';


--
-- Name: COLUMN org_group.group_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.group_name IS 'name of the org group';


--
-- Name: COLUMN org_group.org_group_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.org_group_type IS 'type of the org group
company=1
client=2
vendor=4
prospect=8
agency=16';


--
-- Name: COLUMN org_group.owning_entity_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.owning_entity_id IS 'If owning_entity_id = org_group_id then it is a top level group

I think there is a confusion here.  While it is used as described above, it''s also (incorrectly) used as a tenant ID.';


--
-- Name: COLUMN org_group.default_project_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.default_project_id IS 'Used for time punch in / out if no default project set in person table';


--
-- Name: COLUMN org_group.eval_email_notify; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.eval_email_notify IS 'Send employee evaluation e-mail notifications?
Y = Yes
N = No
I = Inherit answer from parent org group';


--
-- Name: COLUMN org_group.eval_email_first_days; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.eval_email_first_days IS 'Number of days after new hire for evaluation';


--
-- Name: COLUMN org_group.eval_email_notify_days; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.eval_email_notify_days IS 'How many days before evaluation to notify supervisor';


--
-- Name: COLUMN org_group.eval_email_send_days; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.eval_email_send_days IS 'Y/N for each of five days represending Yes / No for Monday, Tuesday, Wednesday, Thursday, and Friday';


--
-- Name: COLUMN org_group.new_week_begin_day; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.new_week_begin_day IS '0 = unknown
1 = Sunday
2 = Monday
3 = Tuesday
4 = Wednesday
5 = Thursday
6 = Friday
7 = Saturday';


--
-- Name: COLUMN org_group.benefit_class_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.benefit_class_id IS 'The default benefit class employees in this group get.  It can be overwritten by a benefit class assigned directly to the employee.';


--
-- Name: COLUMN org_group.timesheet_period_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.timesheet_period_type IS 'I = Inherit from parent org group
D = Daily
W = Weekly
B = Biweekly
S = Semi-monthly
M = Monthly';


--
-- Name: COLUMN org_group.timesheet_period_start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.timesheet_period_start_date IS 'The first date of a valid but arbitrary period.  Only used for weekly and biweekly computations.  Which period doesn''t matter so long as it is a valid period start date.  The system calculates from that point.  Format is YYYYMMDD.';


--
-- Name: COLUMN org_group.timesheet_show_billable; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group.timesheet_show_billable IS 'Should billible hours be shown on timesheet. 
I = Inherit from parent org group 
Y = Yes
N = No';


--
-- Name: org_group_association; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.org_group_association (
    person_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    primary_indicator character(1) DEFAULT 'N'::bpchar NOT NULL,
    org_group_type integer NOT NULL,
    start_date integer DEFAULT 0 NOT NULL,
    final_date integer DEFAULT 0 NOT NULL,
    CONSTRAINT org_grpass_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT supervisor_check CHECK (((primary_indicator = 'Y'::bpchar) OR (primary_indicator = 'N'::bpchar)))
);


ALTER TABLE public.org_group_association OWNER TO postgres;

--
-- Name: TABLE org_group_association; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.org_group_association IS 'This table joins people to organization groups.';


--
-- Name: COLUMN org_group_association.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group_association.person_id IS 'join to person table';


--
-- Name: COLUMN org_group_association.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group_association.org_group_id IS 'join to org group';


--
-- Name: COLUMN org_group_association.record_change_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group_association.record_change_date IS 'Date/time record last changed';


--
-- Name: COLUMN org_group_association.record_change_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group_association.record_change_type IS 'New / Modified / Deleted';


--
-- Name: COLUMN org_group_association.record_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group_association.record_person_id IS 'Person who changed the record';


--
-- Name: COLUMN org_group_association.primary_indicator; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group_association.primary_indicator IS 'Flag to denote supervisor or primary contact - Y or N';


--
-- Name: COLUMN org_group_association.org_group_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group_association.org_group_type IS '1=Company
2=Client
4=Vendor
8=Prospect
16-Agency';


--
-- Name: org_group_association_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.org_group_association_h (
    history_id character(32) NOT NULL,
    person_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    primary_indicator character(1) DEFAULT 'N'::bpchar NOT NULL,
    org_group_type integer NOT NULL,
    start_date integer DEFAULT 0 NOT NULL,
    final_date integer DEFAULT 0 NOT NULL,
    CONSTRAINT org_grpash_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT suervisor_check CHECK (((primary_indicator = 'Y'::bpchar) OR (primary_indicator = 'N'::bpchar)))
);


ALTER TABLE public.org_group_association_h OWNER TO postgres;

--
-- Name: TABLE org_group_association_h; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.org_group_association_h IS 'This table joins people to organization groups - history table.';


--
-- Name: COLUMN org_group_association_h.history_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group_association_h.history_id IS 'A GUID';


--
-- Name: org_group_hierarchy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.org_group_hierarchy (
    parent_group_id character(16) NOT NULL,
    child_group_id character(16) NOT NULL,
    org_group_type integer NOT NULL
);


ALTER TABLE public.org_group_hierarchy OWNER TO postgres;

--
-- Name: TABLE org_group_hierarchy; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.org_group_hierarchy IS 'This table controls the relationships between organization groups.';


--
-- Name: COLUMN org_group_hierarchy.parent_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group_hierarchy.parent_group_id IS 'join to the parent org group';


--
-- Name: COLUMN org_group_hierarchy.child_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group_hierarchy.child_group_id IS 'Join to the child org group.';


--
-- Name: COLUMN org_group_hierarchy.org_group_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.org_group_hierarchy.org_group_type IS '1=Company
2=Client
4=Vendor
8=Prospect
16-Agency';


--
-- Name: overtime_approval; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.overtime_approval (
    overtime_approval_id character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    supervisor_id character(16) NOT NULL,
    overtime_date integer NOT NULL,
    overtime_hours real NOT NULL,
    record_change_date timestamp with time zone NOT NULL
);


ALTER TABLE public.overtime_approval OWNER TO postgres;

--
-- Name: password_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.password_history (
    password_history_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    date_retired integer NOT NULL,
    user_password character varying(48) NOT NULL
);


ALTER TABLE public.password_history OWNER TO postgres;

--
-- Name: pay_schedule; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pay_schedule (
    pay_schedule_id character(16) NOT NULL,
    schedule_name character varying(20) NOT NULL,
    description character varying(80),
    company_id character(16)
);


ALTER TABLE public.pay_schedule OWNER TO postgres;

--
-- Name: COLUMN pay_schedule.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.pay_schedule.company_id IS 'The company this schedule is associated with.  NULL''s are allowed to allow for the possability of sharing a pay schedule across companies.';


--
-- Name: pay_schedule_period; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pay_schedule_period (
    pay_period_id character(16) NOT NULL,
    pay_schedule_id character(16),
    last_date integer NOT NULL,
    pay_date integer NOT NULL,
    beginning_of_year character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT pay_sch_per_beg_chk CHECK (((beginning_of_year = 'Y'::bpchar) OR (beginning_of_year = 'N'::bpchar)))
);


ALTER TABLE public.pay_schedule_period OWNER TO postgres;

--
-- Name: payment_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment_info (
    payment_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    date_authorized timestamp with time zone NOT NULL,
    address_ip character varying(30) NOT NULL,
    payment_type smallint DEFAULT 0 NOT NULL,
    account_name character varying(80) NOT NULL,
    account_number character varying(25) NOT NULL,
    bank_draft_bank_name character varying(40),
    bank_draft_bank_route character varying(20),
    cc_expire integer DEFAULT 0 NOT NULL,
    cc_cvc_code character varying(6),
    billing_street character varying(70),
    billing_city character varying(60),
    billing_state character varying(2),
    billing_zip character varying(10),
    benefit_id character(16)
);


ALTER TABLE public.payment_info OWNER TO postgres;

--
-- Name: TABLE payment_info; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.payment_info IS 'CC payment info';


--
-- Name: COLUMN payment_info.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.payment_info.person_id IS 'Person signed on that made payment info';


--
-- Name: COLUMN payment_info.date_authorized; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.payment_info.date_authorized IS 'Date payment info authorized';


--
-- Name: COLUMN payment_info.address_ip; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.payment_info.address_ip IS 'IP address of person authorizing payment';


--
-- Name: COLUMN payment_info.payment_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.payment_info.payment_type IS '1 = bank transfer (cheking)
2 = bank transfer (savings)
3 = Visa
4 = Master Card
5 = American Express';


--
-- Name: COLUMN payment_info.account_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.payment_info.account_name IS 'Name on account (bank draft or CC)';


--
-- Name: COLUMN payment_info.account_number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.payment_info.account_number IS 'Account number (bank draft or CC)';


--
-- Name: COLUMN payment_info.cc_expire; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.payment_info.cc_expire IS 'YYYYMM';


--
-- Name: COLUMN payment_info.cc_cvc_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.payment_info.cc_cvc_code IS 'CC cvc code';


--
-- Name: COLUMN payment_info.benefit_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.payment_info.benefit_id IS 'Benefit (not config) being paid for';


--
-- Name: per_diem_exception; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.per_diem_exception (
    per_diem_exception_id character(16) NOT NULL,
    exception_type character(1) NOT NULL,
    exception_amount real DEFAULT 0 NOT NULL,
    person_id character(16) NOT NULL,
    project_id character(16) NOT NULL,
    notes character varying(256),
    CONSTRAINT per_diem_exception_type_chk CHECK (((exception_type = 'A'::bpchar) OR (exception_type = 'D'::bpchar) OR (exception_type = 'S'::bpchar) OR (exception_type = 'R'::bpchar)))
);


ALTER TABLE public.per_diem_exception OWNER TO postgres;

--
-- Name: COLUMN per_diem_exception.exception_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.per_diem_exception.exception_type IS '(A)dd / (D)elete / (S)ubtract / (R)place';


--
-- Name: person; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person (
    person_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    lname character varying(30),
    fname character varying(30),
    mname character varying(20),
    personal_email character varying(60),
    job_title character varying(60),
    org_group_type integer NOT NULL,
    company_id character(16) NOT NULL,
    ssn character varying(32),
    sex character(1) DEFAULT 'U'::bpchar NOT NULL,
    dob integer DEFAULT 0 NOT NULL,
    handicap character(1) DEFAULT 'N'::bpchar NOT NULL,
    student character(1) DEFAULT 'N'::bpchar NOT NULL,
    citizenship character(2),
    visa character varying(10),
    visa_status_date integer DEFAULT 0 NOT NULL,
    visa_exp_date integer DEFAULT 0 NOT NULL,
    sort_order integer DEFAULT 0 NOT NULL,
    nickname character varying(15),
    drivers_license_state character varying(4),
    drivers_license_number character varying(20),
    drivers_license_exp integer DEFAULT 0 NOT NULL,
    smoker character(1) DEFAULT 'U'::bpchar NOT NULL,
    auto_insurance_carrier character varying(20),
    auto_insurance_policy character varying(20),
    auto_insurance_exp integer DEFAULT 0 NOT NULL,
    auto_insurance_start integer DEFAULT 0 NOT NULL,
    auto_insurance_coverage character varying(20),
    default_project_id character(16),
    student_calendar_type character(1) DEFAULT ' '::bpchar NOT NULL,
    height smallint DEFAULT 0 NOT NULL,
    weight smallint DEFAULT 0 NOT NULL,
    record_type character(1) DEFAULT 'R'::bpchar NOT NULL,
    smoking_program character(1) DEFAULT 'U'::bpchar NOT NULL,
    military_branch character(1) DEFAULT 'U'::bpchar NOT NULL,
    military_start_date integer DEFAULT 0 NOT NULL,
    military_end_date integer DEFAULT 0 NOT NULL,
    military_rank character varying(20),
    military_discharge_type character(1) DEFAULT 'U'::bpchar NOT NULL,
    military_discharge_explain character varying(100),
    convicted_of_crime character(1) DEFAULT 'U'::bpchar NOT NULL,
    convicted_of_what character varying(1000),
    worked_for_company_before character(1) DEFAULT 'N'::bpchar NOT NULL,
    worked_for_company_when character varying(20),
    actively_at_work character(1) DEFAULT ' '::bpchar NOT NULL,
    unable_to_perform character(1) DEFAULT ' '::bpchar NOT NULL,
    has_aids character(1) DEFAULT ' '::bpchar NOT NULL,
    has_cancer character(1) DEFAULT ' '::bpchar NOT NULL,
    has_heart_condition character(1) DEFAULT ' '::bpchar NOT NULL,
    hic_number character varying(12),
    agreement_date timestamp with time zone,
    agreement_address_ip character varying(40),
    agreement_address_url character varying(40),
    agreement_revision integer DEFAULT 0 NOT NULL,
    person_guid character(32),
    replace_employer_plan character(1) DEFAULT ' '::bpchar NOT NULL,
    not_missed_five_days character(1) DEFAULT ' '::bpchar NOT NULL,
    drug_alcohol_abuse character(1) DEFAULT ' '::bpchar NOT NULL,
    two_family_heart_cond character(1) DEFAULT ' '::bpchar NOT NULL,
    two_family_cancer character(1) DEFAULT ' '::bpchar NOT NULL,
    two_family_diabetes character(1) DEFAULT ' '::bpchar NOT NULL,
    has_other_medical character(1) DEFAULT ' '::bpchar NOT NULL,
    default_email_sender character(1) DEFAULT 'N'::bpchar NOT NULL,
    i9_part1 character(1) DEFAULT 'N'::bpchar NOT NULL,
    i9_part2 character(1) DEFAULT 'N'::bpchar NOT NULL,
    i9p1_confirmation character(14),
    i9p2_confirmation character(14),
    i9p1_person character(16),
    i9p2_person character(16),
    i9p1_when timestamp with time zone,
    i9p2_when timestamp with time zone,
    linkedin character varying(80),
    CONSTRAINT drug_alcohol_abuse_chk CHECK (((drug_alcohol_abuse = ' '::bpchar) OR (drug_alcohol_abuse = 'Y'::bpchar) OR (drug_alcohol_abuse = 'N'::bpchar))),
    CONSTRAINT not_missed_five_days_chk CHECK (((not_missed_five_days = ' '::bpchar) OR (not_missed_five_days = 'Y'::bpchar) OR (not_missed_five_days = 'N'::bpchar))),
    CONSTRAINT person_active_chk CHECK (((actively_at_work = ' '::bpchar) OR (actively_at_work = 'Y'::bpchar) OR (actively_at_work = 'N'::bpchar))),
    CONSTRAINT person_aids_chk CHECK (((has_aids = ' '::bpchar) OR (has_aids = 'Y'::bpchar) OR (has_aids = 'N'::bpchar))),
    CONSTRAINT person_cancer_chk CHECK (((has_cancer = ' '::bpchar) OR (has_cancer = 'Y'::bpchar) OR (has_cancer = 'N'::bpchar))),
    CONSTRAINT person_convicted_chk CHECK (((convicted_of_crime = 'U'::bpchar) OR (convicted_of_crime = 'Y'::bpchar) OR (convicted_of_crime = 'N'::bpchar))),
    CONSTRAINT person_default_email_chk CHECK (((default_email_sender = 'Y'::bpchar) OR (default_email_sender = 'N'::bpchar))),
    CONSTRAINT person_handicap_chk CHECK (((handicap = 'Y'::bpchar) OR (handicap = 'N'::bpchar))),
    CONSTRAINT person_heart_chk CHECK (((has_heart_condition = ' '::bpchar) OR (has_heart_condition = 'Y'::bpchar) OR (has_heart_condition = 'N'::bpchar))),
    CONSTRAINT person_i91_chk CHECK (((i9_part1 = 'Y'::bpchar) OR (i9_part1 = 'N'::bpchar))),
    CONSTRAINT person_i92_chk CHECK (((i9_part2 = 'Y'::bpchar) OR (i9_part2 = 'N'::bpchar))),
    CONSTRAINT person_military_branch_chk CHECK (((military_branch = 'U'::bpchar) OR (military_branch = 'A'::bpchar) OR (military_branch = 'F'::bpchar) OR (military_branch = 'N'::bpchar) OR (military_branch = 'M'::bpchar) OR (military_branch = 'C'::bpchar) OR (military_branch = 'G'::bpchar))),
    CONSTRAINT person_military_dis_chk CHECK (((military_discharge_type = 'U'::bpchar) OR (military_discharge_type = 'H'::bpchar) OR (military_discharge_type = 'G'::bpchar) OR (military_discharge_type = 'O'::bpchar) OR (military_discharge_type = 'B'::bpchar) OR (military_discharge_type = 'D'::bpchar))),
    CONSTRAINT person_other_medical_chk CHECK (((has_other_medical = ' '::bpchar) OR (has_other_medical = 'Y'::bpchar) OR (has_other_medical = 'N'::bpchar))),
    CONSTRAINT person_perform_chk CHECK (((unable_to_perform = ' '::bpchar) OR (unable_to_perform = 'Y'::bpchar) OR (unable_to_perform = 'N'::bpchar))),
    CONSTRAINT person_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT person_record_type_chk CHECK (((record_type = 'R'::bpchar) OR (record_type = 'C'::bpchar))),
    CONSTRAINT person_sex_chk CHECK (((sex = 'U'::bpchar) OR (sex = 'F'::bpchar) OR (sex = 'M'::bpchar))),
    CONSTRAINT person_smok_pgm_chk CHECK (((smoking_program = 'Y'::bpchar) OR (smoking_program = 'N'::bpchar) OR (smoking_program = 'U'::bpchar))),
    CONSTRAINT person_smoker_chk CHECK (((smoker = 'U'::bpchar) OR (smoker = 'Y'::bpchar) OR (smoker = 'N'::bpchar))),
    CONSTRAINT person_student_cal_type_chk CHECK (((student_calendar_type = ' '::bpchar) OR (student_calendar_type = 'S'::bpchar) OR (student_calendar_type = 'Q'::bpchar))),
    CONSTRAINT person_student_chk CHECK (((student = 'Y'::bpchar) OR (student = 'N'::bpchar))),
    CONSTRAINT person_worked_before_chk CHECK (((worked_for_company_before = 'Y'::bpchar) OR (worked_for_company_before = 'N'::bpchar))),
    CONSTRAINT replace_employer_plan_chk CHECK (((replace_employer_plan = ' '::bpchar) OR (replace_employer_plan = 'Y'::bpchar) OR (replace_employer_plan = 'N'::bpchar))),
    CONSTRAINT two_family_cancer_chk CHECK (((two_family_cancer = ' '::bpchar) OR (two_family_cancer = 'Y'::bpchar) OR (two_family_cancer = 'N'::bpchar))),
    CONSTRAINT two_family_diabetes_chk CHECK (((two_family_diabetes = ' '::bpchar) OR (two_family_diabetes = 'Y'::bpchar) OR (two_family_diabetes = 'N'::bpchar))),
    CONSTRAINT two_family_heart_cond_chk CHECK (((two_family_heart_cond = ' '::bpchar) OR (two_family_heart_cond = 'Y'::bpchar) OR (two_family_heart_cond = 'N'::bpchar)))
);


ALTER TABLE public.person OWNER TO postgres;

--
-- Name: TABLE person; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.person IS 'This table stores all the common  information about a person.';


--
-- Name: COLUMN person.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.person_id IS 'primary key';


--
-- Name: COLUMN person.lname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.lname IS 'Last name';


--
-- Name: COLUMN person.fname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.fname IS 'First name';


--
-- Name: COLUMN person.mname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.mname IS 'Middle name';


--
-- Name: COLUMN person.personal_email; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.personal_email IS 'Personal email address.';


--
-- Name: COLUMN person.org_group_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.org_group_type IS '1=Company
2=Client
4=Vendor
8=Prospect
16-Agency';


--
-- Name: COLUMN person.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.company_id IS 'The company this person is associated with.';


--
-- Name: COLUMN person.ssn; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.ssn IS 'Encrypted Social Security Number
The unencrypted format is expected to be "999-99-9999"
including the dashes';


--
-- Name: COLUMN person.sex; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.sex IS 'Unknown / Male / Female';


--
-- Name: COLUMN person.dob; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.dob IS 'Date of birth';


--
-- Name: COLUMN person.citizenship; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.citizenship IS 'Citizen of what country';


--
-- Name: COLUMN person.visa_exp_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.visa_exp_date IS 'Visa expiration date';


--
-- Name: COLUMN person.nickname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.nickname IS 'Persons alias or nickname';


--
-- Name: COLUMN person.drivers_license_exp; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.drivers_license_exp IS 'YYYYMMDD
';


--
-- Name: COLUMN person.smoker; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.smoker IS 'Yes / No / Unknown';


--
-- Name: COLUMN person.auto_insurance_policy; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.auto_insurance_policy IS 'Auto insurance policy number';


--
-- Name: COLUMN person.auto_insurance_exp; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.auto_insurance_exp IS 'Auto insurance policy expiration date (YYYYMMDD)';


--
-- Name: COLUMN person.auto_insurance_start; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.auto_insurance_start IS 'Auto insurance start date (YYYYMMDD)';


--
-- Name: COLUMN person.default_project_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.default_project_id IS 'Default project id when clocking in and out';


--
-- Name: COLUMN person.student_calendar_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.student_calendar_type IS 'blank
S = semester
Q = quater';


--
-- Name: COLUMN person.height; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.height IS 'Person''s height in inches';


--
-- Name: COLUMN person.weight; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.weight IS 'Person''s weight in pounds';


--
-- Name: COLUMN person.record_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.record_type IS 'R = Real record
C = Change request';


--
-- Name: COLUMN person.smoking_program; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.smoking_program IS 'Yes / No / Unknown';


--
-- Name: COLUMN person.military_branch; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.military_branch IS 'U = Unspecified
A = Army
F = Air Force
N = Navy
M = Marines
C = Coast Guard
G = National Guard';


--
-- Name: COLUMN person.military_start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.military_start_date IS 'YYYYMM';


--
-- Name: COLUMN person.military_end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.military_end_date IS 'YYYYMM';


--
-- Name: COLUMN person.military_discharge_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.military_discharge_type IS 'U = Unspecified
H = Honorable
G = General (under honorable conditions)
O = Other than honorable
B = Bad conduct
D = Dishonorable';


--
-- Name: COLUMN person.convicted_of_crime; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.convicted_of_crime IS 'U = Unspecified
Y = Yes
N = No';


--
-- Name: COLUMN person.actively_at_work; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.actively_at_work IS 'Insurance question answered by employee.  Is the person actively at work?
blank = not answered
Y = Yes
N = No';


--
-- Name: COLUMN person.unable_to_perform; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.unable_to_perform IS 'Insurance question answered by employee.  Is the person currently hospitalized or unable to perform their normal duties and activities?
blank = not answered
Y = Yes
N = No';


--
-- Name: COLUMN person.has_aids; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.has_aids IS ''' '' = not answered
''Y'' = Yes
''N'' = No';


--
-- Name: COLUMN person.has_cancer; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.has_cancer IS ''' '' = not answered
''Y'' = Yes
''N'' = No';


--
-- Name: COLUMN person.has_heart_condition; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.has_heart_condition IS ''' '' = not answered
''Y'' = Yes
''N'' = No';


--
-- Name: COLUMN person.hic_number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.hic_number IS 'Health Insurance Claim number for Medicare';


--
-- Name: COLUMN person.agreement_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.agreement_date IS 'This is the date the person accepted the Arahant license agreement.';


--
-- Name: COLUMN person.agreement_address_ip; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.agreement_address_ip IS 'This is the IP address the person came in on when they accepted the Arahant license agreement.';


--
-- Name: COLUMN person.agreement_address_url; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.agreement_address_url IS 'This is the URL of the person who accepted the Arahant license agreement.';


--
-- Name: COLUMN person.agreement_revision; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.agreement_revision IS 'This is the Subversion revision number of the Arahant application when the user accepted the license agreement.';


--
-- Name: COLUMN person.replace_employer_plan; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.replace_employer_plan IS 'Will this coverage replace a critical illness policy or certificate of insurance paid for, by, or through your employer?';


--
-- Name: COLUMN person.not_missed_five_days; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.not_missed_five_days IS 'Has the Proposed Insured been performing their normal duties at work, home, or school on a full-time basis and not having missed more than 5 consecutive days in the last 12 months due to illness or injury, except for normal pregnancy';


--
-- Name: COLUMN person.drug_alcohol_abuse; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.drug_alcohol_abuse IS 'Within the past 5 years, has any Proposed Insured been diagnosed with or treated for Drug abuse or alcohol abuse; disease of the liver, kidney or digestive system; disease or disorder of the lung; diabetes; diseases of the nervous system, including Parkinson''s, MS and cerebral palsy; or any disease or disorder which has led or may lead to a permanent or progressive loss of vision, hearing, or speech';


--
-- Name: COLUMN person.two_family_heart_cond; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.two_family_heart_cond IS 'To the best of your knowledge and belief, have any 2 of your natural parents or natural siblings (sisters or brothers) been diagnosed with Heart attack, heart disease, or stroke before age 60';


--
-- Name: COLUMN person.two_family_cancer; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.two_family_cancer IS 'To the best of your knowledge and belief, have any 2 of your natural parents or natural siblings (sisters or brothers) been diagnosed with Cancer before age 60';


--
-- Name: COLUMN person.two_family_diabetes; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.two_family_diabetes IS 'To the best of your knowledge and belief, have any 2 of your natural parents or natural siblings (sisters or brothers) been diagnosed with Kidney disease or diabetes before age 60';


--
-- Name: COLUMN person.has_other_medical; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.has_other_medical IS 'Will this coverage replace a critical illness policy or certificate of insurance paid for, by, or through your employer?';


--
-- Name: COLUMN person.default_email_sender; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.default_email_sender IS '''Y'' if this is the default email sender.  Only one record should be ''Y''.  Basically, sent email uses a person''s authenticated_email.  If they''re not authenticated, the default email address is used as the sender.';


--
-- Name: COLUMN person.linkedin; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.linkedin IS 'URL to their LinkedIn listing';


--
-- Name: person_change_request; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person_change_request (
    request_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    request_type smallint NOT NULL,
    request_date timestamp with time zone NOT NULL,
    request_data text
);


ALTER TABLE public.person_change_request OWNER TO postgres;

--
-- Name: TABLE person_change_request; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.person_change_request IS 'This table is used by Open Enrollment version 1
Only known user is Williamson County
The person_cr table replaces this one.';


--
-- Name: person_changed; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person_changed (
    interface_id smallint NOT NULL,
    person_id character(16) NOT NULL,
    earliest_change_date timestamp with time zone NOT NULL
);


ALTER TABLE public.person_changed OWNER TO postgres;

--
-- Name: person_cr; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person_cr (
    change_request_id character(16) NOT NULL,
    real_record_id character(16),
    change_status character(1) NOT NULL,
    request_time timestamp with time zone NOT NULL,
    change_record_id character(16) NOT NULL,
    requestor_id character(16) NOT NULL,
    approver_id character(16),
    approval_time timestamp with time zone,
    project_id character(16) NOT NULL,
    CONSTRAINT person_rcr_status_chk CHECK (((change_status = 'P'::bpchar) OR (change_status = 'A'::bpchar) OR (change_status = 'R'::bpchar)))
);


ALTER TABLE public.person_cr OWNER TO postgres;

--
-- Name: TABLE person_cr; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.person_cr IS 'Holds record change requests for the person table.
Used in version 2 and 3 of the Open Enrollment Wizard.
Table person_change_request was used prior.';


--
-- Name: COLUMN person_cr.change_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_cr.change_status IS 'P = Pending request
A = Approved
R = Rejected';


--
-- Name: person_email; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person_email (
    person_email_id character(16) NOT NULL,
    sent_to character varying(40) NOT NULL,
    subject character varying(100) NOT NULL,
    message text,
    person_id character(16) NOT NULL,
    date_sent timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.person_email OWNER TO postgres;

--
-- Name: person_form; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person_form (
    person_form_id character(16) NOT NULL,
    form_type_id character(16) NOT NULL,
    form_date integer NOT NULL,
    person_id character(16) NOT NULL,
    comments character varying(255),
    source character varying(255),
    ext_ref character varying(15),
    file_name_extension character varying(10) NOT NULL,
    electronically_signed character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT person_form_date_chk CHECK ((form_date > 0)),
    CONSTRAINT person_form_signed_chk CHECK (((electronically_signed = 'Y'::bpchar) OR (electronically_signed = 'N'::bpchar)))
);


ALTER TABLE public.person_form OWNER TO postgres;

--
-- Name: COLUMN person_form.form_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_form.form_date IS 'Date the form was uploaded';


--
-- Name: COLUMN person_form.source; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_form.source IS 'The source file or path to image';


--
-- Name: COLUMN person_form.electronically_signed; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_form.electronically_signed IS 'Yes or No';


--
-- Name: person_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person_h (
    history_id character(32) NOT NULL,
    person_id character(16) NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    lname character varying(30),
    fname character varying(30),
    mname character varying(20),
    personal_email character varying(60),
    job_title character varying(60),
    org_group_type integer NOT NULL,
    company_id character(16),
    ssn character varying(32),
    sex character(1) DEFAULT 'U'::bpchar NOT NULL,
    dob integer DEFAULT 0 NOT NULL,
    handicap character(1) DEFAULT 'N'::bpchar NOT NULL,
    student character(1) DEFAULT 'N'::bpchar NOT NULL,
    citizenship character(2),
    visa character varying(10),
    visa_status_date integer DEFAULT 0 NOT NULL,
    visa_exp_date integer DEFAULT 0 NOT NULL,
    sort_order integer DEFAULT 0 NOT NULL,
    nickname character varying(15),
    drivers_license_state character varying(4),
    drivers_license_number character varying(20),
    drivers_license_exp integer DEFAULT 0 NOT NULL,
    smoker character(1) DEFAULT 'U'::bpchar NOT NULL,
    auto_insurance_carrier character varying(20),
    auto_insurance_policy character varying(20),
    auto_insurance_exp integer DEFAULT 0 NOT NULL,
    auto_insurance_start integer DEFAULT 0 NOT NULL,
    auto_insurance_coverage character varying(20),
    default_project_id character(16),
    student_calendar_type character(1) DEFAULT ' '::bpchar NOT NULL,
    height smallint DEFAULT 0 NOT NULL,
    weight smallint DEFAULT 0 NOT NULL,
    record_type character(1) DEFAULT 'R'::bpchar NOT NULL,
    smoking_program character(1) DEFAULT 'U'::bpchar NOT NULL,
    military_branch character(1) DEFAULT 'U'::bpchar NOT NULL,
    military_start_date integer DEFAULT 0 NOT NULL,
    military_end_date integer DEFAULT 0 NOT NULL,
    military_rank character varying(20),
    military_discharge_type character(1) DEFAULT 'U'::bpchar NOT NULL,
    military_discharge_explain character varying(100),
    convicted_of_crime character(1) DEFAULT 'U'::bpchar NOT NULL,
    convicted_of_what character varying(1000),
    worked_for_company_before character(1) DEFAULT 'N'::bpchar NOT NULL,
    worked_for_company_when character varying(20),
    actively_at_work character(1) DEFAULT ' '::bpchar NOT NULL,
    unable_to_perform character(1) DEFAULT ' '::bpchar NOT NULL,
    has_aids character(1) DEFAULT ' '::bpchar NOT NULL,
    has_cancer character(1) DEFAULT ' '::bpchar NOT NULL,
    has_heart_condition character(1) DEFAULT ' '::bpchar NOT NULL,
    hic_number character varying(12),
    agreement_date timestamp with time zone,
    agreement_address_ip character varying(40),
    agreement_address_url character varying(40),
    agreement_revision integer DEFAULT 0 NOT NULL,
    person_guid character(32),
    replace_employer_plan character(1) DEFAULT ' '::bpchar NOT NULL,
    not_missed_five_days character(1) DEFAULT ' '::bpchar NOT NULL,
    drug_alcohol_abuse character(1) DEFAULT ' '::bpchar NOT NULL,
    two_family_heart_cond character(1) DEFAULT ' '::bpchar NOT NULL,
    two_family_cancer character(1) DEFAULT ' '::bpchar NOT NULL,
    two_family_diabetes character(1) DEFAULT ' '::bpchar NOT NULL,
    has_other_medical character(1) DEFAULT ' '::bpchar NOT NULL,
    i9_part1 character(1) DEFAULT 'N'::bpchar NOT NULL,
    i9_part2 character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT drug_alcohol_abuse_ch2 CHECK (((drug_alcohol_abuse = ' '::bpchar) OR (drug_alcohol_abuse = 'Y'::bpchar) OR (drug_alcohol_abuse = 'N'::bpchar))),
    CONSTRAINT not_missed_five_days_ch2 CHECK (((not_missed_five_days = ' '::bpchar) OR (not_missed_five_days = 'Y'::bpchar) OR (not_missed_five_days = 'N'::bpchar))),
    CONSTRAINT person_h_i91_chk CHECK (((i9_part1 = 'Y'::bpchar) OR (i9_part1 = 'N'::bpchar))),
    CONSTRAINT person_h_i92_chk CHECK (((i9_part2 = 'Y'::bpchar) OR (i9_part2 = 'N'::bpchar))),
    CONSTRAINT person_other_medical_chk2 CHECK (((has_other_medical = ' '::bpchar) OR (has_other_medical = 'Y'::bpchar) OR (has_other_medical = 'N'::bpchar))),
    CONSTRAINT personh_active_chk CHECK (((actively_at_work = ' '::bpchar) OR (actively_at_work = 'Y'::bpchar) OR (actively_at_work = 'N'::bpchar))),
    CONSTRAINT personh_aids_chk CHECK (((has_aids = ' '::bpchar) OR (has_aids = 'Y'::bpchar) OR (has_aids = 'N'::bpchar))),
    CONSTRAINT personh_cancer_chk CHECK (((has_cancer = ' '::bpchar) OR (has_cancer = 'Y'::bpchar) OR (has_cancer = 'N'::bpchar))),
    CONSTRAINT personh_convicted_chk CHECK (((convicted_of_crime = 'U'::bpchar) OR (convicted_of_crime = 'Y'::bpchar) OR (convicted_of_crime = 'N'::bpchar))),
    CONSTRAINT personh_handicap_chk CHECK (((handicap = 'Y'::bpchar) OR (handicap = 'N'::bpchar))),
    CONSTRAINT personh_heart_chk CHECK (((has_heart_condition = ' '::bpchar) OR (has_heart_condition = 'Y'::bpchar) OR (has_heart_condition = 'N'::bpchar))),
    CONSTRAINT personh_military_branch_chk CHECK (((military_branch = 'U'::bpchar) OR (military_branch = 'A'::bpchar) OR (military_branch = 'F'::bpchar) OR (military_branch = 'N'::bpchar) OR (military_branch = 'M'::bpchar) OR (military_branch = 'C'::bpchar) OR (military_branch = 'G'::bpchar))),
    CONSTRAINT personh_military_dis_chk CHECK (((military_discharge_type = 'U'::bpchar) OR (military_discharge_type = 'H'::bpchar) OR (military_discharge_type = 'G'::bpchar) OR (military_discharge_type = 'O'::bpchar) OR (military_discharge_type = 'B'::bpchar) OR (military_discharge_type = 'D'::bpchar))),
    CONSTRAINT personh_perform_chk CHECK (((unable_to_perform = ' '::bpchar) OR (unable_to_perform = 'Y'::bpchar) OR (unable_to_perform = 'N'::bpchar))),
    CONSTRAINT personh_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT personh_record_type_chk CHECK (((record_type = 'R'::bpchar) OR (record_type = 'C'::bpchar))),
    CONSTRAINT personh_sex_chk CHECK (((sex = 'U'::bpchar) OR (sex = 'F'::bpchar) OR (sex = 'M'::bpchar))),
    CONSTRAINT personh_smok_pgm_chk CHECK (((smoking_program = 'Y'::bpchar) OR (smoking_program = 'N'::bpchar) OR (smoking_program = 'U'::bpchar))),
    CONSTRAINT personh_smoker_chk CHECK (((smoker = 'U'::bpchar) OR (smoker = 'Y'::bpchar) OR (smoker = 'N'::bpchar))),
    CONSTRAINT personh_student_cal_type_chk CHECK (((student_calendar_type = ' '::bpchar) OR (student_calendar_type = 'S'::bpchar) OR (student_calendar_type = 'Q'::bpchar))),
    CONSTRAINT personh_student_chk CHECK (((student = 'Y'::bpchar) OR (student = 'N'::bpchar))),
    CONSTRAINT personh_worked_before_chk CHECK (((worked_for_company_before = 'Y'::bpchar) OR (worked_for_company_before = 'N'::bpchar))),
    CONSTRAINT replace_employer_plan_ch2 CHECK (((replace_employer_plan = ' '::bpchar) OR (replace_employer_plan = 'Y'::bpchar) OR (replace_employer_plan = 'N'::bpchar))),
    CONSTRAINT two_family_cancer_ch2 CHECK (((two_family_cancer = ' '::bpchar) OR (two_family_cancer = 'Y'::bpchar) OR (two_family_cancer = 'N'::bpchar))),
    CONSTRAINT two_family_diabetes_ch2 CHECK (((two_family_diabetes = ' '::bpchar) OR (two_family_diabetes = 'Y'::bpchar) OR (two_family_diabetes = 'N'::bpchar))),
    CONSTRAINT two_family_heart_cond_ch2 CHECK (((two_family_heart_cond = ' '::bpchar) OR (two_family_heart_cond = 'Y'::bpchar) OR (two_family_heart_cond = 'N'::bpchar)))
);


ALTER TABLE public.person_h OWNER TO postgres;

--
-- Name: COLUMN person_h.history_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_h.history_id IS 'A GUID';


--
-- Name: COLUMN person_h.replace_employer_plan; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_h.replace_employer_plan IS 'Will this coverage replace a critical illness policy or certificate of insurance paid for, by, or through your employer?';


--
-- Name: COLUMN person_h.not_missed_five_days; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_h.not_missed_five_days IS 'Has the Proposed Insured been performing their normal duties at work, home, or school on a full-time basis and not having missed more than 5 consecutive days in the last 12 months due to illness or injury, except for normal pregnancy';


--
-- Name: COLUMN person_h.drug_alcohol_abuse; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_h.drug_alcohol_abuse IS 'Within the past 5 years, has any Proposed Insured been diagnosed with or treated for Drug abuse or alcohol abuse; disease of the liver, kidney or digestive system; disease or disorder of the lung; diabetes; diseases of the nervous system, including Parkinson''s, MS and cerebral palsy; or any disease or disorder which has led or may lead to a permanent or progressive loss of vision, hearing, or speech';


--
-- Name: COLUMN person_h.two_family_heart_cond; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_h.two_family_heart_cond IS 'To the best of your knowledge and belief, have any 2 of your natural parents or natural siblings (sisters or brothers) been diagnosed with Heart attack, heart disease, or stroke before age 60';


--
-- Name: COLUMN person_h.two_family_cancer; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_h.two_family_cancer IS 'To the best of your knowledge and belief, have any 2 of your natural parents or natural siblings (sisters or brothers) been diagnosed with Cancer before age 60';


--
-- Name: COLUMN person_h.two_family_diabetes; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_h.two_family_diabetes IS 'To the best of your knowledge and belief, have any 2 of your natural parents or natural siblings (sisters or brothers) been diagnosed with Kidney disease or diabetes before age 60';


--
-- Name: person_note; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person_note (
    note_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    cat_id character(16) NOT NULL,
    note text
);


ALTER TABLE public.person_note OWNER TO postgres;

--
-- Name: TABLE person_note; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.person_note IS 'Person notes';


--
-- Name: COLUMN person_note.cat_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_note.cat_id IS 'Note category ID';


--
-- Name: COLUMN person_note.note; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person_note.note IS 'The note detail';


--
-- Name: personal_reference; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.personal_reference (
    reference_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    reference_name character varying(60) NOT NULL,
    relationship_type character(1) NOT NULL,
    relationship_other character varying(20),
    company character varying(60),
    phone character varying(20),
    address character varying(100),
    years_known smallint NOT NULL,
    CONSTRAINT personal_ref_rel_chk CHECK (((relationship_type = 'S'::bpchar) OR (relationship_type = 'C'::bpchar) OR (relationship_type = 'R'::bpchar) OR (relationship_type = 'F'::bpchar) OR (relationship_type = 'T'::bpchar) OR (relationship_type = 'O'::bpchar)))
);


ALTER TABLE public.personal_reference OWNER TO postgres;

--
-- Name: COLUMN personal_reference.relationship_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.personal_reference.relationship_type IS 'S = Supervisor
C = Co-worker
R = Relative
F = Friend
T = school Teacher
O = Other';


--
-- Name: phone; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.phone (
    phone_id character(16) NOT NULL,
    phone_number character varying(20),
    phone_type integer NOT NULL,
    person_join character(16),
    org_group_join character(16),
    record_type character(1) DEFAULT 'R'::bpchar NOT NULL,
    CONSTRAINT phone_record_type_chk CHECK (((record_type = 'R'::bpchar) OR (record_type = 'C'::bpchar)))
);


ALTER TABLE public.phone OWNER TO postgres;

--
-- Name: TABLE phone; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.phone IS 'This table stores phone numbers.';


--
-- Name: COLUMN phone.phone_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.phone.phone_id IS 'Primary key.';


--
-- Name: COLUMN phone.phone_number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.phone.phone_number IS 'The phone number.';


--
-- Name: COLUMN phone.phone_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.phone.phone_type IS 'The type of this phone number:
1=work
2=home
3=cell
4=fax';


--
-- Name: COLUMN phone.person_join; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.phone.person_join IS 'Join to person table';


--
-- Name: COLUMN phone.org_group_join; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.phone.org_group_join IS 'Join to org_group table';


--
-- Name: COLUMN phone.record_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.phone.record_type IS 'R = Real record
C = Change request';


--
-- Name: phone_cr; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.phone_cr (
    change_request_id character(16) NOT NULL,
    real_record_id character(16),
    change_status character(1) NOT NULL,
    request_time timestamp with time zone NOT NULL,
    change_record_id character(16) NOT NULL,
    requestor_id character(16) NOT NULL,
    approver_id character(16),
    approval_time timestamp with time zone,
    project_id character(16) NOT NULL,
    CONSTRAINT phone_rcr_status_chk CHECK (((change_status = 'P'::bpchar) OR (change_status = 'A'::bpchar) OR (change_status = 'R'::bpchar)))
);


ALTER TABLE public.phone_cr OWNER TO postgres;

--
-- Name: TABLE phone_cr; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.phone_cr IS 'Holds record change requests for the phone table.';


--
-- Name: COLUMN phone_cr.change_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.phone_cr.change_status IS 'P = Pending request
A = Approved
R = Rejected';


--
-- Name: physician; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.physician (
    physician_id character(16) NOT NULL,
    benefit_join_id character(16) NOT NULL,
    physician_name character varying(60) NOT NULL,
    physician_code character(10) NOT NULL,
    address character varying(160),
    change_reason character varying(160),
    change_date integer DEFAULT 0 NOT NULL,
    annual_visit character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT physician_annual_visit_chk CHECK (((annual_visit = 'Y'::bpchar) OR (annual_visit = 'N'::bpchar)))
);


ALTER TABLE public.physician OWNER TO postgres;

--
-- Name: previous_employment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.previous_employment (
    employment_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    start_date integer DEFAULT 0 NOT NULL,
    end_date integer DEFAULT 0 NOT NULL,
    company character varying(60) NOT NULL,
    phone character varying(20),
    supervisor character varying(40),
    job_title character varying(40),
    starting_salary integer NOT NULL,
    ending_salary integer NOT NULL,
    responsibilities character varying(1500),
    reason_for_leaving character varying(120),
    contact_supervisor character(1) NOT NULL,
    street character varying(45),
    city character varying(20),
    state character(2),
    CONSTRAINT prev_empl_contact_chk CHECK (((contact_supervisor = 'Y'::bpchar) OR (contact_supervisor = 'N'::bpchar) OR (contact_supervisor = 'L'::bpchar)))
);


ALTER TABLE public.previous_employment OWNER TO postgres;

--
-- Name: COLUMN previous_employment.start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.previous_employment.start_date IS 'YYYYMM';


--
-- Name: COLUMN previous_employment.end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.previous_employment.end_date IS 'YYYYMM';


--
-- Name: COLUMN previous_employment.contact_supervisor; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.previous_employment.contact_supervisor IS 'Is it okay to contact the former supervisor (Y/N)?';


--
-- Name: process_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.process_history (
    process_history_id character(16) NOT NULL,
    process_schedule_id character(16) NOT NULL,
    run_time timestamp with time zone NOT NULL,
    success character(1) NOT NULL,
    CONSTRAINT process_history_success_chk CHECK (((success = 'Y'::bpchar) OR (success = 'N'::bpchar)))
);


ALTER TABLE public.process_history OWNER TO postgres;

--
-- Name: COLUMN process_history.success; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.process_history.success IS 'Yes / No';


--
-- Name: process_schedule; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.process_schedule (
    process_schedule_id character(16) NOT NULL,
    java_class character varying(256) NOT NULL,
    start_date integer NOT NULL,
    start_time integer NOT NULL,
    perform_missing_runs character(1) NOT NULL,
    run_minutes character varying(180),
    run_hours character varying(80),
    run_days_of_month character varying(100),
    run_months character varying(40),
    run_days_of_week character varying(20),
    CONSTRAINT process_schedule_missing_runs_chk CHECK (((perform_missing_runs = 'Y'::bpchar) OR (perform_missing_runs = 'N'::bpchar)))
);


ALTER TABLE public.process_schedule OWNER TO postgres;

--
-- Name: COLUMN process_schedule.perform_missing_runs; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.process_schedule.perform_missing_runs IS 'Yes or No';


--
-- Name: COLUMN process_schedule.run_minutes; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.process_schedule.run_minutes IS 'See CRON documentation';


--
-- Name: COLUMN process_schedule.run_hours; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.process_schedule.run_hours IS 'See CRON documentation';


--
-- Name: COLUMN process_schedule.run_days_of_month; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.process_schedule.run_days_of_month IS 'See CRON documentation';


--
-- Name: COLUMN process_schedule.run_months; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.process_schedule.run_months IS 'See CRON documentation';


--
-- Name: COLUMN process_schedule.run_days_of_week; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.process_schedule.run_days_of_week IS 'See CRON documentation';


--
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    product_id character(16) NOT NULL,
    product_type_id character(16) NOT NULL,
    vendor_id character(16) NOT NULL,
    sku character varying(20),
    product_cost double precision NOT NULL,
    wholesale_price double precision NOT NULL,
    retail_price double precision NOT NULL,
    availability_date integer NOT NULL,
    term_date integer NOT NULL,
    man_hours double precision DEFAULT 0 NOT NULL,
    sell_as_type character(1) DEFAULT 'P'::bpchar NOT NULL,
    CONSTRAINT product_type_chk CHECK (((sell_as_type = 'P'::bpchar) OR (sell_as_type = 'S'::bpchar) OR (sell_as_type = 'B'::bpchar)))
);


ALTER TABLE public.product OWNER TO postgres;

--
-- Name: COLUMN product.sell_as_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.product.sell_as_type IS 'Product
Service
Both';


--
-- Name: product_attribute; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_attribute (
    product_attribute_id character(16) NOT NULL,
    company_id character(16) NOT NULL,
    attribute_order smallint NOT NULL,
    attribute character varying(80) NOT NULL,
    data_type character(1) DEFAULT 'Y'::bpchar NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    CONSTRAINT product_att_data_chk CHECK (((data_type = 'N'::bpchar) OR (data_type = 'D'::bpchar) OR (data_type = 'S'::bpchar) OR (data_type = 'Y'::bpchar) OR (data_type = 'L'::bpchar)))
);


ALTER TABLE public.product_attribute OWNER TO postgres;

--
-- Name: COLUMN product_attribute.data_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.product_attribute.data_type IS 'Numeric (double)
Date (YYYYMMDD)
String
Yes/no/unknown (Yes/No/Unknown in field)
List of choices (product_attribute_choice table)';


--
-- Name: product_attribute_choice; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_attribute_choice (
    product_attribute_choice_id character(16) NOT NULL,
    product_attribute_id character(16) NOT NULL,
    description character varying(60) NOT NULL,
    last_active_date integer NOT NULL,
    choice_order smallint DEFAULT 0 NOT NULL
);


ALTER TABLE public.product_attribute_choice OWNER TO postgres;

--
-- Name: product_attribute_dependency; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_attribute_dependency (
    attribute_dependency_id character(16) NOT NULL,
    product_attribute_id character(16) NOT NULL,
    dependent_attribute_id character(16) NOT NULL
);


ALTER TABLE public.product_attribute_dependency OWNER TO postgres;

--
-- Name: COLUMN product_attribute_dependency.product_attribute_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.product_attribute_dependency.product_attribute_id IS 'This column tells which attribute this record is refering to.';


--
-- Name: COLUMN product_attribute_dependency.dependent_attribute_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.product_attribute_dependency.dependent_attribute_id IS 'This column represents the attribute that must be set in order for the attribute this record refers to to be valid.';


--
-- Name: product_service; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_service (
    product_service_id character(16) NOT NULL,
    accsys_id character varying(30),
    expense_account_id character(16),
    description character varying(60) NOT NULL,
    service_type integer DEFAULT 0 NOT NULL,
    company_id character(16) NOT NULL,
    detailed_description character varying(512)
);


ALTER TABLE public.product_service OWNER TO postgres;

--
-- Name: TABLE product_service; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.product_service IS 'This table is a parent of the product & service tables.  Its main key is shared with those two tables.';


--
-- Name: COLUMN product_service.product_service_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.product_service.product_service_id IS 'Primary key';


--
-- Name: COLUMN product_service.accsys_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.product_service.accsys_id IS 'The id of this item in the accounting system.';


--
-- Name: COLUMN product_service.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.product_service.description IS 'Description of the item in the accounting system.';


--
-- Name: COLUMN product_service.service_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.product_service.service_type IS 'Type of service in the accounting system.   Acct system specific.';


--
-- Name: COLUMN product_service.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.product_service.company_id IS 'The Arahant user company this product or service is applicable to.';


--
-- Name: product_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_type (
    product_type_id character(16) NOT NULL,
    parent_product_type_id character(16),
    description character varying(160) NOT NULL,
    company_id character(16) NOT NULL
);


ALTER TABLE public.product_type OWNER TO postgres;

--
-- Name: COLUMN product_type.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.product_type.company_id IS 'The Arahant user company this product type is applicable to.';


--
-- Name: project; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project (
    project_id character(16) NOT NULL,
    reference character varying(20),
    description character varying(80),
    detail_desc text,
    project_sponsor_id character(16),
    managing_employee character(16),
    project_status_id character(16) NOT NULL,
    project_type_id character(16) NOT NULL,
    project_category_id character(16) NOT NULL,
    dollar_cap double precision DEFAULT 0 NOT NULL,
    billing_rate real DEFAULT 0 NOT NULL,
    billable character(1) DEFAULT 'U'::bpchar NOT NULL,
    project_name character varying(30),
    requesting_org_group character(16) NOT NULL,
    requester_name character varying(50),
    product_id character(16),
    date_reported integer DEFAULT 0 NOT NULL,
    billing_method integer DEFAULT 4 NOT NULL,
    next_billing_date integer DEFAULT 0 NOT NULL,
    bill_at_org_group character(16),
    all_employees character(1) DEFAULT 'N'::bpchar NOT NULL,
    route_id character(16),
    current_route_stop character(16),
    subject_person character(16),
    time_reported integer DEFAULT 0 NOT NULL,
    completed_date integer DEFAULT 0 NOT NULL,
    completed_time integer DEFAULT '-1'::integer NOT NULL,
    approving_person character(16),
    approving_timestamp timestamp with time zone,
    approving_person_txt character varying(30),
    priority_company smallint DEFAULT 10000 NOT NULL,
    priority_department smallint DEFAULT 10000 NOT NULL,
    priority_client smallint DEFAULT 10000 NOT NULL,
    estimate_hours real DEFAULT 0 NOT NULL,
    date_promised integer DEFAULT 0 NOT NULL,
    estimated_time_span integer DEFAULT 0 NOT NULL,
    date_of_estimate integer DEFAULT 0 NOT NULL,
    ongoing character(1) DEFAULT 'N'::bpchar NOT NULL,
    status_comments text,
    company_id character(16) NOT NULL,
    purchase_order character varying(15),
    rate_type_id character(16) NOT NULL,
    project_code character varying(12),
    project_state character(2),
    estimated_first_date integer DEFAULT 0 NOT NULL,
    estimated_last_date integer DEFAULT 0 NOT NULL,
    address_id character(16),
    store_number character varying(8),
    project_subtype_id character(16),
    location_description character varying(14),
    billing_type character(1) DEFAULT 'H'::bpchar NOT NULL,
    fixed_price_amount double precision DEFAULT 0 NOT NULL,
    last_report_date timestamp with time zone,
    project_days smallint DEFAULT 0 NOT NULL,
    CONSTRAINT project_all_emp_chk CHECK (((all_employees = 'Y'::bpchar) OR (all_employees = 'N'::bpchar))),
    CONSTRAINT project_billable_chk CHECK (((billable = 'U'::bpchar) OR (billable = 'Y'::bpchar) OR (billable = 'N'::bpchar) OR (billable = 'I'::bpchar))),
    CONSTRAINT project_billing_type_chk CHECK (((billing_type = 'H'::bpchar) OR (billing_type = 'F'::bpchar))),
    CONSTRAINT project_ongoing_chk CHECK (((ongoing = 'Y'::bpchar) OR (ongoing = 'N'::bpchar)))
);


ALTER TABLE public.project OWNER TO postgres;

--
-- Name: TABLE project; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.project IS 'This table stores projects or units of work to track and bill.';


--
-- Name: COLUMN project.project_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.project_id IS 'Primary key.';


--
-- Name: COLUMN project.reference; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.reference IS 'ID from other, external system';


--
-- Name: COLUMN project.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.description IS 'Short description of project.';


--
-- Name: COLUMN project.detail_desc; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.detail_desc IS 'Long description of project.';


--
-- Name: COLUMN project.project_sponsor_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.project_sponsor_id IS 'This is the person who created the project';


--
-- Name: COLUMN project.managing_employee; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.managing_employee IS 'join to employee table for person in charge of project.';


--
-- Name: COLUMN project.project_status_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.project_status_id IS 'join to status table.';


--
-- Name: COLUMN project.project_type_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.project_type_id IS 'Join to type table.';


--
-- Name: COLUMN project.project_category_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.project_category_id IS 'Join to category table.';


--
-- Name: COLUMN project.dollar_cap; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.dollar_cap IS 'Maximum amount to be spent on this project.';


--
-- Name: COLUMN project.billing_rate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.billing_rate IS 'Default rate to charge on this project.';


--
-- Name: COLUMN project.billable; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.billable IS 'U = Unknown
N = Not billable
Y = Billable outside any parent projects estimate
I = Billable within the parent project estimate';


--
-- Name: COLUMN project.project_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.project_name IS 'Arahant project ID field';


--
-- Name: COLUMN project.requesting_org_group; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.requesting_org_group IS 'Org group requesting and paying for project';


--
-- Name: COLUMN project.requester_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.requester_name IS 'Text entry of requester name, because person join isn''t required.';


--
-- Name: COLUMN project.product_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.product_id IS 'Join to product this project applies to.';


--
-- Name: COLUMN project.date_reported; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.date_reported IS 'Date the project was added to the system';


--
-- Name: COLUMN project.billing_method; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.billing_method IS '1=At proj status assoc with accounting
2=At specified date
3=Weekly
4=Monthly';


--
-- Name: COLUMN project.next_billing_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.next_billing_date IS 'Next date project should be billed';


--
-- Name: COLUMN project.bill_at_org_group; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.bill_at_org_group IS 'Bill when project reaches a status associated with theis org group';


--
-- Name: COLUMN project.all_employees; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.all_employees IS 'Is this project applicable to all employees?  (Y/N)';


--
-- Name: COLUMN project.route_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.route_id IS 'Assigned route';


--
-- Name: COLUMN project.subject_person; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.subject_person IS 'If the project has to do with a person in the system, this field tells which person it is for.';


--
-- Name: COLUMN project.time_reported; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.time_reported IS 'Time the project was added to the system';


--
-- Name: COLUMN project.completed_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.completed_date IS 'Date project completed';


--
-- Name: COLUMN project.approving_person; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.approving_person IS 'Person logged on when approval occured';


--
-- Name: COLUMN project.approving_timestamp; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.approving_timestamp IS 'Timestamp approval occured';


--
-- Name: COLUMN project.approving_person_txt; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.approving_person_txt IS 'Name of person who approved the project';


--
-- Name: COLUMN project.priority_company; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.priority_company IS '10000 means no priority';


--
-- Name: COLUMN project.priority_department; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.priority_department IS '10000 means no priority';


--
-- Name: COLUMN project.priority_client; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.priority_client IS '10000 means no priority';


--
-- Name: COLUMN project.estimate_hours; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.estimate_hours IS 'Estimated number of employee (presumably billable) hours to complete the project';


--
-- Name: COLUMN project.date_promised; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.date_promised IS 'Date the project was promissed to be completed';


--
-- Name: COLUMN project.estimated_time_span; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.estimated_time_span IS 'Estimated number of hours to complete the project.  This is not billable hours but span of elapsed time.';


--
-- Name: COLUMN project.date_of_estimate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.date_of_estimate IS 'Date estimate completed';


--
-- Name: COLUMN project.ongoing; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.ongoing IS 'Y means this is not a specific project but a general project to catch occational work of this type.  It is ususally not prioritized.';


--
-- Name: COLUMN project.status_comments; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.status_comments IS 'General commens about the current status of the project';


--
-- Name: COLUMN project.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.company_id IS 'The company who owns this project';


--
-- Name: COLUMN project.rate_type_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.rate_type_id IS 'Rate type associated with this project';


--
-- Name: COLUMN project.project_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.project_code IS 'Arbitrary code used external to the system';


--
-- Name: COLUMN project.project_state; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.project_state IS 'State project is in (e.g. FL, GA, NY, etc.)';


--
-- Name: COLUMN project.estimated_first_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.estimated_first_date IS 'Estimated start date of project';


--
-- Name: COLUMN project.estimated_last_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.estimated_last_date IS 'Estimated last date of project';


--
-- Name: COLUMN project.address_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.address_id IS 'Project site address';


--
-- Name: COLUMN project.location_description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.location_description IS 'Used to build the description column';


--
-- Name: COLUMN project.billing_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.billing_type IS '(H)ourly or (F)ixed price';


--
-- Name: COLUMN project.last_report_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.last_report_date IS 'Date/time project report last sent';


--
-- Name: COLUMN project.project_days; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project.project_days IS 'Numer of work days in project';


--
-- Name: project_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_category (
    project_category_id character(16) NOT NULL,
    code character varying(20) NOT NULL,
    description character varying(80),
    scope character(1) DEFAULT 'G'::bpchar NOT NULL,
    company_id character(16),
    last_active_date integer DEFAULT 0 NOT NULL,
    CONSTRAINT project_cat_type_chk CHECK (((scope = 'G'::bpchar) OR (scope = 'I'::bpchar)))
);


ALTER TABLE public.project_category OWNER TO postgres;

--
-- Name: TABLE project_category; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.project_category IS 'This table stores categories of projects.';


--
-- Name: COLUMN project_category.project_category_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_category.project_category_id IS 'Primary key.';


--
-- Name: COLUMN project_category.code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_category.code IS 'Code for category.';


--
-- Name: COLUMN project_category.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_category.description IS 'Description of category.';


--
-- Name: COLUMN project_category.scope; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_category.scope IS 'G = Globally available
I = Internal use ';


--
-- Name: COLUMN project_category.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_category.company_id IS 'The company the record applies to, or NULL if it is globally applicable.';


--
-- Name: COLUMN project_category.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_category.last_active_date IS 'Last date this record should be used in a new association.  0 means no end.';


--
-- Name: project_checklist_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_checklist_detail (
    project_checklist_detail_id character(16) NOT NULL,
    project_id character(16) NOT NULL,
    route_stop_checklist_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    completed_by character varying(60),
    date_completed integer NOT NULL,
    entry_timestamp timestamp with time zone NOT NULL,
    entry_comments character varying(1024)
);


ALTER TABLE public.project_checklist_detail OWNER TO postgres;

--
-- Name: COLUMN project_checklist_detail.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_checklist_detail.person_id IS 'Person logged in when item completed';


--
-- Name: COLUMN project_checklist_detail.completed_by; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_checklist_detail.completed_by IS 'Person who completed the task (if not the person who entered it)';


--
-- Name: COLUMN project_checklist_detail.entry_timestamp; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_checklist_detail.entry_timestamp IS 'Timestamp completion date entered';


--
-- Name: project_comment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_comment (
    date_entered timestamp with time zone NOT NULL,
    person_id character(16) NOT NULL,
    comment_txt text,
    internal character(1) NOT NULL,
    comment_id character(16) NOT NULL,
    project_shift_id character(16) NOT NULL,
    CONSTRAINT project_comment_internal_chk CHECK (((internal = 'Y'::bpchar) OR (internal = 'N'::bpchar)))
);


ALTER TABLE public.project_comment OWNER TO postgres;

--
-- Name: TABLE project_comment; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.project_comment IS 'This table stores project descriptions and comments.';


--
-- Name: COLUMN project_comment.date_entered; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_comment.date_entered IS 'date comment was entered.';


--
-- Name: COLUMN project_comment.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_comment.person_id IS 'Join to person table.';


--
-- Name: COLUMN project_comment.comment_txt; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_comment.comment_txt IS 'The comment';


--
-- Name: COLUMN project_comment.internal; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_comment.internal IS 'Flag to indicate if comment is internal to company only.  Y or N';


--
-- Name: COLUMN project_comment.comment_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_comment.comment_id IS 'primary key.';


--
-- Name: project_employee_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_employee_history (
    project_employee_history_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    change_person_id character(16) NOT NULL,
    change_date integer NOT NULL,
    change_time integer NOT NULL,
    change_type character(1) NOT NULL,
    project_shift_id character(16) NOT NULL,
    CONSTRAINT project_employee_hist_type_chk CHECK (((change_type = 'A'::bpchar) OR (change_type = 'D'::bpchar)))
);


ALTER TABLE public.project_employee_history OWNER TO postgres;

--
-- Name: TABLE project_employee_history; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.project_employee_history IS 'Track employee project assignment history';


--
-- Name: COLUMN project_employee_history.change_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_history.change_person_id IS 'The person who made the change';


--
-- Name: COLUMN project_employee_history.change_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_history.change_date IS 'Date the change occurred YYYYMMDD';


--
-- Name: COLUMN project_employee_history.change_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_history.change_time IS 'Time the change occurred HHMM';


--
-- Name: COLUMN project_employee_history.change_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_history.change_type IS '(A)dd or (D)elete';


--
-- Name: project_employee_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_employee_join (
    project_employee_join_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    person_priority smallint DEFAULT 10000 NOT NULL,
    date_assigned integer DEFAULT 0 NOT NULL,
    time_assigned integer DEFAULT '-1'::integer NOT NULL,
    start_date integer DEFAULT 0 NOT NULL,
    confirmed_date timestamp with time zone,
    confirmed_person_id character(16),
    verified_date timestamp with time zone,
    verified_person_id character(16),
    manager character(1) DEFAULT 'N'::bpchar NOT NULL,
    hours character(1) DEFAULT 'Y'::bpchar NOT NULL,
    project_shift_id character(16) NOT NULL,
    CONSTRAINT project_employee_join_hours_chk CHECK (((hours = 'Y'::bpchar) OR (hours = 'N'::bpchar))),
    CONSTRAINT project_employee_join_manage_chk CHECK (((manager = 'Y'::bpchar) OR (manager = 'N'::bpchar)))
);


ALTER TABLE public.project_employee_join OWNER TO postgres;

--
-- Name: COLUMN project_employee_join.date_assigned; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_join.date_assigned IS 'Date project assigned to person';


--
-- Name: COLUMN project_employee_join.time_assigned; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_join.time_assigned IS 'Time project assigned to person';


--
-- Name: COLUMN project_employee_join.start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_join.start_date IS 'Date worker is to start project.  Zero means on the start date of the project.';


--
-- Name: COLUMN project_employee_join.confirmed_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_join.confirmed_date IS 'This means the confirming person in the office was told (close to the beginning) by the worker that they will be at the project.';


--
-- Name: COLUMN project_employee_join.confirmed_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_join.confirmed_person_id IS 'Person who confirmed the worker';


--
-- Name: COLUMN project_employee_join.verified_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_join.verified_date IS 'Worker seen at site';


--
-- Name: COLUMN project_employee_join.verified_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_join.verified_person_id IS 'Person who verified the worker on site';


--
-- Name: COLUMN project_employee_join.manager; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_join.manager IS 'Is this person a manager?';


--
-- Name: COLUMN project_employee_join.hours; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_employee_join.hours IS 'Does this person log hours against this project?';


--
-- Name: project_form; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_form (
    project_form_id character(16) NOT NULL,
    form_type_id character(16) NOT NULL,
    form_date integer NOT NULL,
    comments character varying(255),
    source character varying(255),
    file_name_extension character varying(10) NOT NULL,
    internal character(1) DEFAULT 'Y'::bpchar NOT NULL,
    project_shift_id character varying(16) NOT NULL,
    CONSTRAINT project_form_date_chk CHECK ((form_date > 0)),
    CONSTRAINT project_form_internal_chk CHECK (((internal = 'Y'::bpchar) OR (internal = 'N'::bpchar)))
);


ALTER TABLE public.project_form OWNER TO postgres;

--
-- Name: COLUMN project_form.source; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_form.source IS 'Path or file name of original image';


--
-- Name: COLUMN project_form.internal; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_form.internal IS 'Is this form for internal use? (Y/N)';


--
-- Name: project_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_history (
    project_history_id character(16) NOT NULL,
    project_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    date_changed integer NOT NULL,
    time_changed integer NOT NULL,
    from_status_id character(16) NOT NULL,
    to_status_id character(16) NOT NULL,
    from_stop_id character(16),
    to_stop_id character(16)
);


ALTER TABLE public.project_history OWNER TO postgres;

--
-- Name: COLUMN project_history.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_history.person_id IS 'Person who made the project change';


--
-- Name: project_inventory_email; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_inventory_email (
    pie_id character(16) NOT NULL,
    project_id character(16) NOT NULL,
    person_id character(16) NOT NULL
);


ALTER TABLE public.project_inventory_email OWNER TO postgres;

--
-- Name: TABLE project_inventory_email; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.project_inventory_email IS 'This table keeps a list of people who are sent the project inventory report';


--
-- Name: project_phase; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_phase (
    project_phase_id character(16) NOT NULL,
    code character varying(20) NOT NULL,
    description character varying(80),
    security_level smallint DEFAULT 0 NOT NULL,
    company_id character(16),
    last_active_date integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.project_phase OWNER TO postgres;

--
-- Name: COLUMN project_phase.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_phase.company_id IS 'The company the record applies to, or NULL if it is globally applicable.';


--
-- Name: COLUMN project_phase.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_phase.last_active_date IS 'Last date this record should be used in a new association.  0 means no end.';


--
-- Name: project_po_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_po_detail (
    project_po_detail_id character(36) NOT NULL,
    project_po_id character(36) NOT NULL,
    po_line_number smallint DEFAULT 0 NOT NULL,
    quantity_ordered integer DEFAULT 0 NOT NULL,
    price_each real DEFAULT 0 NOT NULL,
    client_item_inventory_id character(36) NOT NULL,
    when_due_date integer NOT NULL,
    when_due_time integer NOT NULL
);


ALTER TABLE public.project_po_detail OWNER TO postgres;

--
-- Name: COLUMN project_po_detail.project_po_detail_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_po_detail.project_po_detail_id IS 'UUID';


--
-- Name: COLUMN project_po_detail.project_po_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_po_detail.project_po_id IS 'UUID';


--
-- Name: COLUMN project_po_detail.client_item_inventory_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_po_detail.client_item_inventory_id IS 'UUID';


--
-- Name: COLUMN project_po_detail.when_due_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_po_detail.when_due_date IS 'YYYYMMDD';


--
-- Name: COLUMN project_po_detail.when_due_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_po_detail.when_due_time IS 'HHMM (-1 means not present)';


--
-- Name: project_purchase_order; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_purchase_order (
    project_po_id character(36) NOT NULL,
    project_id character(16) NOT NULL,
    po_reference character varying(20),
    po_number character varying(20),
    vendor_name character varying(30)
);


ALTER TABLE public.project_purchase_order OWNER TO postgres;

--
-- Name: COLUMN project_purchase_order.project_po_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_purchase_order.project_po_id IS 'UUID';


--
-- Name: project_report_person; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_report_person (
    person_id character(16) NOT NULL,
    client_id character(16) NOT NULL
);


ALTER TABLE public.project_report_person OWNER TO postgres;

--
-- Name: project_shift; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_shift (
    project_shift_id character(16) NOT NULL,
    project_id character(16) NOT NULL,
    required_workers smallint DEFAULT 0 NOT NULL,
    shift_start character varying(10),
    description character varying(60),
    default_date integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.project_shift OWNER TO postgres;

--
-- Name: COLUMN project_shift.default_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_shift.default_date IS 'This is the default used by the mobile app.  It gets set back to zero when the day is complete.  If zero, the current date is used by the mobile app.';


--
-- Name: project_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_status (
    project_status_id character(16) NOT NULL,
    code character varying(20) NOT NULL,
    description character varying(80),
    active character(1) DEFAULT 'Y'::bpchar NOT NULL,
    company_id character(16),
    last_active_date integer DEFAULT 0 NOT NULL,
    CONSTRAINT project_status_active_chk CHECK (((active = 'Y'::bpchar) OR (active = 'N'::bpchar) OR (active = 'O'::bpchar)))
);


ALTER TABLE public.project_status OWNER TO postgres;

--
-- Name: TABLE project_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.project_status IS 'This table stores the statuses that a project can be in or its workflow.';


--
-- Name: COLUMN project_status.project_status_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_status.project_status_id IS 'Primary Key.';


--
-- Name: COLUMN project_status.code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_status.code IS 'Code for status.';


--
-- Name: COLUMN project_status.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_status.description IS 'Description of status.';


--
-- Name: COLUMN project_status.active; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_status.active IS 'Is this an active project? 
Y = active , in-process project
O = active, ongoing project (like meetings)
N = inactive project';


--
-- Name: COLUMN project_status.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_status.company_id IS 'The company the record applies to, or NULL if it is globally applicable.';


--
-- Name: COLUMN project_status.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_status.last_active_date IS 'Last date this record should be used in a new association.  0 means no end.';


--
-- Name: project_status_rs_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_status_rs_join (
    route_stop_id character(16) NOT NULL,
    project_status_id character(16) NOT NULL
);


ALTER TABLE public.project_status_rs_join OWNER TO postgres;

--
-- Name: TABLE project_status_rs_join; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.project_status_rs_join IS 'project status join to route stop or decision point';


--
-- Name: project_subtype; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_subtype (
    project_subtype_id character(16) NOT NULL,
    code character varying(20) NOT NULL,
    description character varying(80),
    scope character(1) DEFAULT 'G'::bpchar NOT NULL,
    company_id character(16),
    last_active_date integer DEFAULT 0 NOT NULL,
    CONSTRAINT project_subtype_scope_chk CHECK (((scope = 'G'::bpchar) OR (scope = 'I'::bpchar)))
);


ALTER TABLE public.project_subtype OWNER TO postgres;

--
-- Name: COLUMN project_subtype.scope; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_subtype.scope IS 'G = Globally accessible
I = Internal or private to a single company';


--
-- Name: COLUMN project_subtype.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_subtype.company_id IS 'The company this applies to or NULL if global';


--
-- Name: COLUMN project_subtype.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_subtype.last_active_date IS 'Last date this record should be used in a new association.  0 means no end.';


--
-- Name: project_task_assignment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_task_assignment (
    project_task_assignment_id character(16) NOT NULL,
    project_task_detail_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    team_lead character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT project_task_assignment_lead_chk CHECK (((team_lead = 'Y'::bpchar) OR (team_lead = 'N'::bpchar)))
);


ALTER TABLE public.project_task_assignment OWNER TO postgres;

--
-- Name: project_task_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_task_detail (
    project_task_detail_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    title character varying(80) NOT NULL,
    description character varying(1000),
    comments character varying(8000),
    missing_items character varying(1000),
    status smallint DEFAULT 0 NOT NULL,
    project_shift_id character(16) NOT NULL,
    task_date integer DEFAULT 0 NOT NULL,
    completion_date integer DEFAULT 0 NOT NULL,
    recurring character(1) DEFAULT 'N'::bpchar NOT NULL,
    recurring_schedule_id character(16),
    CONSTRAINT project_task_detail_recurring_chk CHECK (((recurring = 'Y'::bpchar) OR (recurring = 'N'::bpchar)))
);


ALTER TABLE public.project_task_detail OWNER TO postgres;

--
-- Name: COLUMN project_task_detail.status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_task_detail.status IS '0 = open
1 = complete
2 = cancelled
3 = incomplete - time
4 = incomplete - missing items
5 = incomplete - reassigned
6 = incomplete - see comments';


--
-- Name: COLUMN project_task_detail.task_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_task_detail.task_date IS 'First work date of task YYYYMMDD';


--
-- Name: COLUMN project_task_detail.completion_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_task_detail.completion_date IS 'work date task was completed YYYYMMDD';


--
-- Name: COLUMN project_task_detail.recurring; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_task_detail.recurring IS 'Is this a recurring task?  Y/N';


--
-- Name: project_task_picture; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_task_picture (
    project_task_picture_id character(16) NOT NULL,
    project_task_detail_id character(16) NOT NULL,
    when_uploaded timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    file_name_extension character varying(10) NOT NULL,
    comments character varying(256),
    who_uploaded character(16),
    picture_number smallint DEFAULT 0 NOT NULL
);


ALTER TABLE public.project_task_picture OWNER TO postgres;

--
-- Name: project_template_benefit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_template_benefit (
    project_template_id character(16) NOT NULL,
    benefit_id character(16),
    bcr_id character(16),
    status_id character(16),
    org_group_id character(16),
    project_status_id character(16) NOT NULL,
    project_category_id character(16) NOT NULL,
    project_type_id character(16) NOT NULL,
    project_description character varying(80) NOT NULL
);


ALTER TABLE public.project_template_benefit OWNER TO postgres;

--
-- Name: TABLE project_template_benefit; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.project_template_benefit IS 'This table specifies a project to be created when ALL of the criteria is met.  The criteria includes the benefit, the benefit change reason, and the org_group assocation of an employee.  NULL in any of these fields indicates ''any''.  These conditions are effectively grouped be an "AND".';


--
-- Name: project_template_benefit_a; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_template_benefit_a (
    template_assignment_id character(16) NOT NULL,
    project_template_id character(16) NOT NULL,
    person_id character(16) NOT NULL
);


ALTER TABLE public.project_template_benefit_a OWNER TO postgres;

--
-- Name: TABLE project_template_benefit_a; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.project_template_benefit_a IS 'This table defines what employees get auto-assigned to projects created with this template.';


--
-- Name: project_training; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_training (
    project_training_id character(16) NOT NULL,
    project_id character(16) NOT NULL,
    cat_id character(16) NOT NULL,
    required character(1) NOT NULL,
    CONSTRAINT project_training_required_chk CHECK (((required = 'Y'::bpchar) OR (required = 'N'::bpchar)))
);


ALTER TABLE public.project_training OWNER TO postgres;

--
-- Name: project_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_type (
    project_type_id character(16) NOT NULL,
    code character varying(20) NOT NULL,
    description character varying(80),
    scope character(1) DEFAULT 'G'::bpchar NOT NULL,
    company_id character(16),
    last_active_date integer DEFAULT 0 NOT NULL,
    CONSTRAINT project_type_scope_chk CHECK (((scope = 'G'::bpchar) OR (scope = 'I'::bpchar)))
);


ALTER TABLE public.project_type OWNER TO postgres;

--
-- Name: TABLE project_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.project_type IS 'This table stores the types of projects - mainly for reporting.';


--
-- Name: COLUMN project_type.project_type_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_type.project_type_id IS 'Primary key.';


--
-- Name: COLUMN project_type.code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_type.code IS 'Code for type.';


--
-- Name: COLUMN project_type.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_type.description IS 'Description of type.';


--
-- Name: COLUMN project_type.scope; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_type.scope IS 'G = Globally accessible
I = Internal use';


--
-- Name: COLUMN project_type.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_type.company_id IS 'The company the record applies to, or NULL if it is globally applicable.';


--
-- Name: COLUMN project_type.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_type.last_active_date IS 'Last date this record should be used in a new association.  0 means no end.';


--
-- Name: project_view; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_view (
    project_view_id character(16) NOT NULL,
    person_id character(16),
    node_title character varying(250),
    node_description character varying(3000),
    project_id character(16),
    CONSTRAINT project_view_null_typ_chk CHECK (((person_id IS NOT NULL) OR (project_id IS NOT NULL))),
    CONSTRAINT project_view_type_chk CHECK ((((node_title IS NULL) AND (node_description IS NULL) AND (project_id IS NOT NULL)) OR (project_id IS NULL)))
);


ALTER TABLE public.project_view OWNER TO postgres;

--
-- Name: COLUMN project_view.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_view.person_id IS 'If this column is NULL then this represents a project hierarchy and project_id must not be NULL';


--
-- Name: project_view_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_view_join (
    project_view_join_id character(16) NOT NULL,
    parent_project_view_id character(16),
    child_project_view_id character(16) NOT NULL,
    sequence_num integer NOT NULL,
    primary_billing character(1) DEFAULT 'Y'::bpchar NOT NULL,
    CONSTRAINT pvj_primary_billing_chk CHECK (((primary_billing = 'Y'::bpchar) OR (primary_billing = 'N'::bpchar)))
);


ALTER TABLE public.project_view_join OWNER TO postgres;

--
-- Name: COLUMN project_view_join.primary_billing; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.project_view_join.primary_billing IS 'Is this the primary billing relationship';


--
-- Name: property; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.property (
    prop_name character varying(30) NOT NULL,
    prop_value character varying(128),
    prop_desc character varying(128)
);


ALTER TABLE public.property OWNER TO postgres;

--
-- Name: prophet_login; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prophet_login (
    person_id character(16) NOT NULL,
    can_login character(1),
    user_login character varying(50),
    user_password character varying(64),
    screen_group_id character(16),
    security_group_id character(16),
    password_effective_date integer DEFAULT 0 NOT NULL,
    no_company_screen_group_id character(16),
    when_created timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    authentication_code character varying(36),
    number_of_resends smallint DEFAULT 0 NOT NULL,
    number_of_authentications smallint DEFAULT 0 NOT NULL,
    reset_password_date timestamp with time zone,
    user_type character(1) DEFAULT 'R'::bpchar NOT NULL,
    CONSTRAINT login_can_login_chk CHECK (((can_login = 'Y'::bpchar) OR (can_login = 'N'::bpchar) OR (can_login = 'X'::bpchar))),
    CONSTRAINT login_usr_type_chk CHECK (((user_type = 'R'::bpchar) OR (user_type = 'A'::bpchar)))
);


ALTER TABLE public.prophet_login OWNER TO postgres;

--
-- Name: TABLE prophet_login; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.prophet_login IS 'This table stores the user login information.';


--
-- Name: COLUMN prophet_login.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prophet_login.person_id IS 'Join to person table.';


--
-- Name: COLUMN prophet_login.can_login; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prophet_login.can_login IS 'Yes / No / X not authenticated';


--
-- Name: COLUMN prophet_login.user_login; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prophet_login.user_login IS 'User ID for login.';


--
-- Name: COLUMN prophet_login.user_password; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prophet_login.user_password IS 'User''s password.';


--
-- Name: COLUMN prophet_login.screen_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prophet_login.screen_group_id IS 'Screen group assigned to this user.';


--
-- Name: COLUMN prophet_login.no_company_screen_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prophet_login.no_company_screen_group_id IS 'Screen group used if the user hasn''t selected a company in a multi-company situation.';


--
-- Name: COLUMN prophet_login.authentication_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prophet_login.authentication_code IS 'Authentication code used by applicants who are creating their own login';


--
-- Name: COLUMN prophet_login.number_of_resends; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prophet_login.number_of_resends IS 'Number of times an applicant requested to re-send their authorization code';


--
-- Name: COLUMN prophet_login.number_of_authentications; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prophet_login.number_of_authentications IS 'Number of times an applicant attempted to authenticate';


--
-- Name: COLUMN prophet_login.reset_password_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prophet_login.reset_password_date IS 'Date a password reset was requested';


--
-- Name: COLUMN prophet_login.user_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prophet_login.user_type IS '(R)egular or (A)pplicant';


--
-- Name: prophet_login_exception; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prophet_login_exception (
    login_exception_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    company_id character(16) NOT NULL,
    screen_group_id character(16) NOT NULL,
    security_group_id character(16) NOT NULL
);


ALTER TABLE public.prophet_login_exception OWNER TO postgres;

--
-- Name: TABLE prophet_login_exception; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.prophet_login_exception IS 'This table keeps alternate (exception) security and screen groups for particular users when they log into or attempt to affect certain (exception) companies.';


--
-- Name: prospect; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect (
    org_group_id character(16) NOT NULL,
    first_contact_date integer NOT NULL,
    prospect_status_id character(16) NOT NULL,
    certainty smallint DEFAULT 0 NOT NULL,
    prospect_source_id character(16) NOT NULL,
    source_detail character varying(120),
    when_added timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    person_id character(16) NOT NULL,
    status_comments text,
    last_contact_date integer DEFAULT 0 NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    status_change_date timestamp with time zone NOT NULL,
    next_contact_date integer DEFAULT 0 NOT NULL,
    company_id character(16),
    prospect_type_id character(16) NOT NULL,
    opportunity_value double precision DEFAULT 0 NOT NULL,
    number_of_employees integer DEFAULT 0 NOT NULL,
    gross_income integer DEFAULT 0 NOT NULL,
    website character varying(80),
    CONSTRAINT prospect_certainty_chk CHECK (((certainty >= 0) AND (certainty <= 100))),
    CONSTRAINT prospect_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.prospect OWNER TO postgres;

--
-- Name: COLUMN prospect.certainty; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect.certainty IS 'The percent of likelyhood that this deal will close.  Note that this field is duplicated in the prospect status table.  That table is applicable if this column is zero.';


--
-- Name: COLUMN prospect.when_added; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect.when_added IS 'When was the record added';


--
-- Name: COLUMN prospect.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect.person_id IS 'Employee/salesman handling the prospect
';


--
-- Name: COLUMN prospect.record_change_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect.record_change_type IS 'New / Modified / Deleted';


--
-- Name: COLUMN prospect.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect.company_id IS 'The company this prospect is related to.';


--
-- Name: COLUMN prospect.opportunity_value; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect.opportunity_value IS 'The potential dollar value of this prospect';


--
-- Name: prospect_contact; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect_contact (
    person_id character(16) NOT NULL,
    contact_type smallint NOT NULL,
    CONSTRAINT prospect_contxt_type_chk CHECK (((contact_type >= 1) AND (contact_type <= 5)))
);


ALTER TABLE public.prospect_contact OWNER TO postgres;

--
-- Name: COLUMN prospect_contact.contact_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_contact.contact_type IS '1 = decision maker
2 = department head
3 = user
4 = consultant / other employee
5 = Unknown
';


--
-- Name: prospect_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect_h (
    history_id character(32) NOT NULL,
    org_group_id character(16) NOT NULL,
    first_contact_date integer NOT NULL,
    prospect_status_id character(16) NOT NULL,
    certainty smallint DEFAULT 0 NOT NULL,
    prospect_source_id character(16) NOT NULL,
    source_detail character varying(120),
    when_added timestamp with time zone,
    person_id character(16) NOT NULL,
    status_comments text,
    last_contact_date integer DEFAULT 0 NOT NULL,
    record_change_date timestamp with time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_person_id character(16) NOT NULL,
    status_change_date timestamp with time zone NOT NULL,
    next_contact_date integer DEFAULT 0 NOT NULL,
    company_id character(16),
    prospect_type_id character(16) NOT NULL,
    opportunity_value double precision DEFAULT 0.0 NOT NULL,
    CONSTRAINT prospect_rct_h_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.prospect_h OWNER TO postgres;

--
-- Name: COLUMN prospect_h.record_change_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_h.record_change_type IS 'New / Modified / Delete';


--
-- Name: COLUMN prospect_h.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_h.company_id IS 'The company that this prospcect is associated with';


--
-- Name: prospect_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect_log (
    prospect_log_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    contact_date integer NOT NULL,
    contact_time integer NOT NULL,
    contact_txt text NOT NULL,
    employees character varying(500) NOT NULL,
    prospect_employees character varying(500),
    sales_activity_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    entry_date timestamp with time zone NOT NULL,
    sales_activity_result_id character(16),
    task_completion_date integer NOT NULL
);


ALTER TABLE public.prospect_log OWNER TO postgres;

--
-- Name: COLUMN prospect_log.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_log.org_group_id IS 'This is the prospect''s org_group';


--
-- Name: COLUMN prospect_log.contact_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_log.contact_date IS 'The date of the contact with the customer or the date of the event.';


--
-- Name: COLUMN prospect_log.employees; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_log.employees IS 'What employees were present in the conversation?';


--
-- Name: COLUMN prospect_log.prospect_employees; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_log.prospect_employees IS 'What (if any) prospect employees were involved?
';


--
-- Name: COLUMN prospect_log.sales_activity_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_log.sales_activity_id IS 'This entry tell what activity is associated to this log entry.';


--
-- Name: COLUMN prospect_log.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_log.person_id IS 'This tells what person entered this log entry.';


--
-- Name: COLUMN prospect_log.entry_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_log.entry_date IS 'The data/time the entry was made (different form the date / time that the contact occurred).';


--
-- Name: COLUMN prospect_log.task_completion_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_log.task_completion_date IS 'Date the task associated to the sales_activity_result task was completed.';


--
-- Name: prospect_source; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect_source (
    prospect_source_id character(16) NOT NULL,
    source_code character varying(15) NOT NULL,
    description character varying(80),
    last_active_date integer DEFAULT 0 NOT NULL,
    company_id character(16)
);


ALTER TABLE public.prospect_source OWNER TO postgres;

--
-- Name: COLUMN prospect_source.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_source.last_active_date IS 'Last date this record should be used in a new association.  0 means no end.';


--
-- Name: COLUMN prospect_source.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_source.company_id IS 'Company this record is associated with.  NULL means all companies.';


--
-- Name: prospect_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect_status (
    prospect_status_id character(16) NOT NULL,
    code character varying(20) NOT NULL,
    seqno smallint NOT NULL,
    description character varying(80),
    active character(1) NOT NULL,
    company_id character(16),
    last_active_date integer DEFAULT 0 NOT NULL,
    show_in_sales_pipeline character(1) DEFAULT 'N'::bpchar NOT NULL,
    sales_points smallint DEFAULT 0 NOT NULL,
    fallback_days smallint DEFAULT 0 NOT NULL,
    certainty smallint DEFAULT 0 NOT NULL,
    CONSTRAINT prospect_status_active_chk CHECK (((active = 'Y'::bpchar) OR (active = 'N'::bpchar))),
    CONSTRAINT prospect_status_pipline_chk CHECK (((show_in_sales_pipeline = 'Y'::bpchar) OR (show_in_sales_pipeline = 'N'::bpchar)))
);


ALTER TABLE public.prospect_status OWNER TO postgres;

--
-- Name: COLUMN prospect_status.active; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_status.active IS 'Are prospects in this status active prospects?';


--
-- Name: COLUMN prospect_status.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_status.company_id IS 'Company this is specific to or NULL if it applies to all companies.';


--
-- Name: COLUMN prospect_status.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_status.last_active_date IS 'Last date this record should be used in a new association.  0 means no end.';


--
-- Name: COLUMN prospect_status.show_in_sales_pipeline; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_status.show_in_sales_pipeline IS 'Yes / No';


--
-- Name: COLUMN prospect_status.certainty; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_status.certainty IS 'The percent of certainty that this deal will close.  Note that this column is duplicated in the prospect table for prospect specific certainty level.  This value only applies if the prospect specific value is zero.';


--
-- Name: prospect_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect_type (
    prospect_type_id character(16) NOT NULL,
    company_id character(16),
    type_code character varying(25) NOT NULL,
    description character varying(80),
    last_active_date integer NOT NULL
);


ALTER TABLE public.prospect_type OWNER TO postgres;

--
-- Name: COLUMN prospect_type.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.prospect_type.company_id IS 'The company this type is applicable to or NULL for all companies.';


--
-- Name: quickbooks_change; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.quickbooks_change (
    record_id character(16) NOT NULL,
    arahant_table_name character varying(35) NOT NULL,
    arahant_record_id character(16) NOT NULL,
    qb_record_id character varying(20),
    qb_record_revision integer DEFAULT '-1'::integer NOT NULL,
    record_changed character(1) DEFAULT 'Y'::bpchar NOT NULL,
    CONSTRAINT qb_chg_change_chk CHECK (((record_changed = 'Y'::bpchar) OR (record_changed = 'N'::bpchar)))
);


ALTER TABLE public.quickbooks_change OWNER TO postgres;

--
-- Name: TABLE quickbooks_change; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.quickbooks_change IS 'Keep track of record change information for interface with quickbooks purposes';


--
-- Name: quickbooks_sync; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.quickbooks_sync (
    sync_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    interface_time timestamp with time zone NOT NULL,
    interface_code smallint NOT NULL,
    direction character(1) NOT NULL,
    CONSTRAINT qb_dir_check CHECK (((direction = 'T'::bpchar) OR (direction = 'F'::bpchar)))
);


ALTER TABLE public.quickbooks_sync OWNER TO postgres;

--
-- Name: COLUMN quickbooks_sync.direction; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.quickbooks_sync.direction IS 'T = To Quickbooks
F = From Quickbooks';


--
-- Name: quote_adjustment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.quote_adjustment (
    quote_adjustment_id character(16) NOT NULL,
    quote_id character(16) NOT NULL,
    adjustment_description character varying(120) NOT NULL,
    seqno smallint NOT NULL,
    quantity smallint DEFAULT 0 NOT NULL,
    adjusted_cost double precision NOT NULL
);


ALTER TABLE public.quote_adjustment OWNER TO postgres;

--
-- Name: quote_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.quote_product (
    quote_product_id character(16) NOT NULL,
    quote_id character(16) NOT NULL,
    product_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    quantity smallint DEFAULT 0 NOT NULL,
    retail_price double precision DEFAULT 0 NOT NULL,
    adjusted_retail_price double precision DEFAULT 0 NOT NULL,
    sell_as_type character(1) DEFAULT 'P'::bpchar NOT NULL,
    CONSTRAINT quote_prod_type_chk CHECK (((sell_as_type = 'P'::bpchar) OR (sell_as_type = 'S'::bpchar) OR (sell_as_type = 'B'::bpchar)))
);


ALTER TABLE public.quote_product OWNER TO postgres;

--
-- Name: COLUMN quote_product.sell_as_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.quote_product.sell_as_type IS 'Product
Service
Both';


--
-- Name: quote_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.quote_table (
    quote_id character(16) NOT NULL,
    client_id character(16) NOT NULL,
    location_cost_id character(16),
    quote_name character varying(60) NOT NULL,
    quote_description character varying(1000),
    created_date timestamp with time zone,
    created_by_person character(16) NOT NULL,
    finalized_date timestamp with time zone,
    finalized_by_person character(16),
    accepted_date timestamp with time zone,
    accepted_person character(16),
    accepted_by_client character varying(60),
    markup_percent double precision DEFAULT 0 NOT NULL,
    additional_cost double precision DEFAULT 0 NOT NULL
);


ALTER TABLE public.quote_table OWNER TO postgres;

--
-- Name: quote_template; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.quote_template (
    quote_template_id character(16) NOT NULL,
    template_name character varying(50) NOT NULL,
    template_description character varying(120)
);


ALTER TABLE public.quote_template OWNER TO postgres;

--
-- Name: quote_template_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.quote_template_product (
    quote_template_product_id character(16) NOT NULL,
    quote_template_id character(16) NOT NULL,
    product_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    default_quantity smallint DEFAULT 0 NOT NULL,
    sell_as_type character(1) DEFAULT 'P'::bpchar NOT NULL,
    CONSTRAINT quote_tmpprod_type_chk CHECK (((sell_as_type = 'P'::bpchar) OR (sell_as_type = 'S'::bpchar) OR (sell_as_type = 'B'::bpchar)))
);


ALTER TABLE public.quote_template_product OWNER TO postgres;

--
-- Name: COLUMN quote_template_product.sell_as_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.quote_template_product.sell_as_type IS 'Product
Service
Both';


--
-- Name: rate_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rate_type (
    rate_type_id character(16) NOT NULL,
    rate_code character(10) NOT NULL,
    description character varying(60) NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    rate_type character(1) NOT NULL,
    company_id character(16),
    CONSTRAINT rate_type_rate_type_chk CHECK (((rate_type = 'P'::bpchar) OR (rate_type = 'E'::bpchar) OR (rate_type = 'B'::bpchar)))
);


ALTER TABLE public.rate_type OWNER TO postgres;

--
-- Name: COLUMN rate_type.rate_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rate_type.rate_type IS 'P = Project
E = Person
B = Both';


--
-- Name: COLUMN rate_type.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rate_type.company_id IS 'The company this type is applicable to or NULL for all companies.';


--
-- Name: receipt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.receipt (
    receipt_id character(16) NOT NULL,
    receipt_date integer NOT NULL,
    receipt_type character(1) NOT NULL,
    reference character varying(30),
    amount double precision NOT NULL,
    source character(1) NOT NULL,
    company_id character(16),
    person_id character(16),
    bank_draft_history_id character(16),
    CONSTRAINT receipt_source_chk CHECK (((source = 'C'::bpchar) OR (source = 'P'::bpchar))),
    CONSTRAINT receipt_type_chk CHECK (((receipt_type = 'C'::bpchar) OR (receipt_type = 'D'::bpchar) OR (receipt_type = 'A'::bpchar)))
);


ALTER TABLE public.receipt OWNER TO postgres;

--
-- Name: COLUMN receipt.receipt_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.receipt.receipt_type IS 'Check / bank Depostit / Adjustment';


--
-- Name: COLUMN receipt.source; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.receipt.source IS 'Company / Person';


--
-- Name: receipt_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.receipt_join (
    receipt_join_id character(16) NOT NULL,
    invoice_id character(16) NOT NULL,
    receipt_id character(16) NOT NULL,
    amount double precision NOT NULL
);


ALTER TABLE public.receipt_join OWNER TO postgres;

--
-- Name: recurring_schedule; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.recurring_schedule (
    recurring_schedule_id character(16) NOT NULL,
    type smallint NOT NULL,
    month smallint,
    day smallint,
    day_of_week smallint,
    n smallint,
    ending_date integer
);


ALTER TABLE public.recurring_schedule OWNER TO postgres;

--
-- Name: TABLE recurring_schedule; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.recurring_schedule IS 'Details for recurring tasks';


--
-- Name: COLUMN recurring_schedule.type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.recurring_schedule.type IS '1 - specific month and day every year
2 - a particular day of each month
3 - last day of each month
4 - X day of Y week in each month
5 - particular day of each week
6 - every weekday
7 - everyday
8 - every X days';


--
-- Name: COLUMN recurring_schedule.month; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.recurring_schedule.month IS '1 - January
2 - February
3 - March
...';


--
-- Name: COLUMN recurring_schedule.day; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.recurring_schedule.day IS '1-31';


--
-- Name: COLUMN recurring_schedule.day_of_week; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.recurring_schedule.day_of_week IS '1-7';


--
-- Name: COLUMN recurring_schedule.n; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.recurring_schedule.n IS 'Used in [n] [day_of_week]
like 3rd Tuesday of each month';


--
-- Name: COLUMN recurring_schedule.ending_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.recurring_schedule.ending_date IS 'YYYYMMDD';


--
-- Name: reminder; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reminder (
    reminder_id character(16) NOT NULL,
    person_id character(16),
    about_person character(16),
    about_project character(16),
    reminder_date integer DEFAULT 0 NOT NULL,
    summary character varying(80) NOT NULL,
    detail character varying(4096),
    status character(1) NOT NULL,
    who_added character(16),
    date_added integer DEFAULT 0 NOT NULL,
    time_added integer DEFAULT 0 NOT NULL,
    who_inactivated character(16),
    date_inactive integer DEFAULT 0 NOT NULL,
    time_inactive integer DEFAULT 0 NOT NULL,
    CONSTRAINT reminder_status_chk CHECK (((status = 'A'::bpchar) OR (status = 'I'::bpchar)))
);


ALTER TABLE public.reminder OWNER TO postgres;

--
-- Name: COLUMN reminder.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.reminder.person_id IS 'This is the person the reminder is for.  If NULL it is for anyone.';


--
-- Name: COLUMN reminder.about_person; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.reminder.about_person IS 'This is the person the reminder is about.  If it''s not about a particular person this will be NULL.';


--
-- Name: COLUMN reminder.about_project; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.reminder.about_project IS 'This is the project the reminder is about.  I NULL, it''s not about a particular project.';


--
-- Name: COLUMN reminder.reminder_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.reminder.reminder_date IS 'The date the reminder becomes active.  0 = now.';


--
-- Name: COLUMN reminder.status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.reminder.status IS 'Active / Inactive';


--
-- Name: COLUMN reminder.date_added; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.reminder.date_added IS 'YYYYMMDD';


--
-- Name: COLUMN reminder.time_added; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.reminder.time_added IS 'HHMM';


--
-- Name: COLUMN reminder.date_inactive; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.reminder.date_inactive IS 'YYYYMMDD';


--
-- Name: COLUMN reminder.time_inactive; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.reminder.time_inactive IS 'HHMM';


--
-- Name: removed_from_roster; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.removed_from_roster (
    rfr_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    supervisor_id character(16) NOT NULL,
    when_removed timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    description character varying(80),
    comments text,
    shift_id character(16) NOT NULL
);


ALTER TABLE public.removed_from_roster OWNER TO postgres;

--
-- Name: TABLE removed_from_roster; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.removed_from_roster IS 'People removed from the roster from the field';


--
-- Name: COLUMN removed_from_roster.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.removed_from_roster.person_id IS 'The person being removed from the roster';


--
-- Name: COLUMN removed_from_roster.supervisor_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.removed_from_roster.supervisor_id IS 'The supervisor who is removing the person';


--
-- Name: report; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report (
    report_id character(16) NOT NULL,
    company_id character(16),
    report_type smallint NOT NULL,
    report_name character varying(80) NOT NULL,
    description character varying(2000),
    page_orientation character(1) DEFAULT 'P'::bpchar NOT NULL,
    page_offset_left real DEFAULT 0.5 NOT NULL,
    page_offset_top real DEFAULT 0.5 NOT NULL,
    default_font_size smallint DEFAULT 12 NOT NULL,
    lines_in_column_title smallint DEFAULT 1 NOT NULL,
    default_space_between_columns real DEFAULT 0.1 NOT NULL,
    CONSTRAINT report_orientation_chk CHECK (((page_orientation = 'P'::bpchar) OR (page_orientation = 'L'::bpchar)))
);


ALTER TABLE public.report OWNER TO postgres;

--
-- Name: COLUMN report.page_orientation; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report.page_orientation IS 'Portriate or Landscape';


--
-- Name: report_column; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_column (
    report_column_id character(16) NOT NULL,
    report_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    column_id smallint NOT NULL,
    leading_space real DEFAULT 0 NOT NULL,
    lines smallint DEFAULT 1 NOT NULL,
    use_all_lines character(1) DEFAULT 'N'::bpchar NOT NULL,
    vertical_justification character(1) DEFAULT 'T'::bpchar NOT NULL,
    title character varying(60),
    title_justification character(1) DEFAULT 'L'::bpchar NOT NULL,
    column_justification character(1) DEFAULT 'L'::bpchar NOT NULL,
    format_code smallint DEFAULT 0 NOT NULL,
    numeric_digits smallint DEFAULT 0 NOT NULL,
    display_totals character(1) DEFAULT 'N'::bpchar NOT NULL,
    break_level smallint DEFAULT 0 NOT NULL,
    sort_order smallint DEFAULT 0 NOT NULL,
    sort_direction character(1) DEFAULT 'A'::bpchar NOT NULL,
    CONSTRAINT report_column_all_lines_chk CHECK (((use_all_lines = 'Y'::bpchar) OR (use_all_lines = 'N'::bpchar))),
    CONSTRAINT report_column_coljust_chk CHECK (((column_justification = 'L'::bpchar) OR (column_justification = 'C'::bpchar) OR (column_justification = 'R'::bpchar))),
    CONSTRAINT report_column_distot_chk CHECK (((display_totals = 'Y'::bpchar) OR (display_totals = 'N'::bpchar))),
    CONSTRAINT report_column_titlejust_chk CHECK (((title_justification = 'L'::bpchar) OR (title_justification = 'C'::bpchar) OR (title_justification = 'R'::bpchar))),
    CONSTRAINT report_column_vertjust_chk CHECK (((vertical_justification = 'T'::bpchar) OR (vertical_justification = 'M'::bpchar) OR (vertical_justification = 'B'::bpchar))),
    CONSTRAINT sort_column_sortdir_chk CHECK (((sort_direction = 'A'::bpchar) OR (sort_direction = 'D'::bpchar)))
);


ALTER TABLE public.report_column OWNER TO postgres;

--
-- Name: COLUMN report_column.column_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.column_id IS 'Link to code';


--
-- Name: COLUMN report_column.leading_space; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.leading_space IS 'Space to the left of the column.  Zero means use page default.';


--
-- Name: COLUMN report_column.lines; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.lines IS 'How many lines one row of this column may take up.  Zero means unlimited.';


--
-- Name: COLUMN report_column.use_all_lines; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.use_all_lines IS 'Always display all ''lines'' lines even of not enough text.  Yes / No';


--
-- Name: COLUMN report_column.vertical_justification; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.vertical_justification IS 'T = Top
M = Middle
B = Bottom';


--
-- Name: COLUMN report_column.title_justification; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.title_justification IS 'L = Left
C = Center
R = Right';


--
-- Name: COLUMN report_column.column_justification; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.column_justification IS 'L = Left
C = Center
R = Right';


--
-- Name: COLUMN report_column.format_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.format_code IS 'How to format a date or number.  Hard coded options defined in the backend.';


--
-- Name: COLUMN report_column.numeric_digits; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.numeric_digits IS 'If numberic field, how many digits to the right of the decimal point to display';


--
-- Name: COLUMN report_column.display_totals; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.display_totals IS 'Yes / No';


--
-- Name: COLUMN report_column.break_level; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.break_level IS 'For multi-level sub-totals';


--
-- Name: COLUMN report_column.sort_order; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.sort_order IS 'Sort priority, default of 0 means that column is not sorted. sort_order of 1 has highest sort priority';


--
-- Name: COLUMN report_column.sort_direction; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_column.sort_direction IS 'Sort direction, ''A'' for ascending, ''D'' for descending';


--
-- Name: report_graphic; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_graphic (
    report_graphic_id character(16) NOT NULL,
    report_id character(16) NOT NULL,
    description character varying(50) NOT NULL,
    x_pos real NOT NULL,
    y_pos real NOT NULL,
    graphic bytea NOT NULL
);


ALTER TABLE public.report_graphic OWNER TO postgres;

--
-- Name: COLUMN report_graphic.x_pos; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_graphic.x_pos IS 'from page offset';


--
-- Name: COLUMN report_graphic.y_pos; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_graphic.y_pos IS 'from page offset';


--
-- Name: report_selection; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_selection (
    report_selection_id character(16) NOT NULL,
    report_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    selection_type character(1) DEFAULT 'V'::bpchar NOT NULL,
    selection_column smallint NOT NULL,
    description character varying(80),
    selection_operator character(2),
    selection_value character varying(60),
    selection_column2 smallint,
    logic_operator character(1) DEFAULT 'N'::bpchar NOT NULL,
    left_parens smallint DEFAULT 0 NOT NULL,
    right_parens smallint DEFAULT 0 NOT NULL,
    selection_value_list text,
    CONSTRAINT report_selection_logic_chk CHECK (((logic_operator = 'A'::bpchar) OR (logic_operator = 'O'::bpchar) OR (logic_operator = 'N'::bpchar))),
    CONSTRAINT report_selection_op_chk CHECK (((selection_operator = 'EQ'::bpchar) OR (selection_operator = 'LT'::bpchar) OR (selection_operator = 'LE'::bpchar) OR (selection_operator = 'GT'::bpchar) OR (selection_operator = 'GE'::bpchar) OR (selection_operator = 'NE'::bpchar) OR (selection_operator = 'IN'::bpchar) OR (selection_operator = 'NA'::bpchar) OR (selection_operator = 'LK'::bpchar))),
    CONSTRAINT report_selection_type_chk CHECK (((selection_type = 'V'::bpchar) OR (selection_type = 'C'::bpchar) OR (selection_type = 'D'::bpchar) OR (selection_type = 'R'::bpchar)))
);


ALTER TABLE public.report_selection OWNER TO postgres;

--
-- Name: COLUMN report_selection.selection_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_selection.selection_type IS 'What to compare to:
  V = selection_value
  R = runtime value
  C = selection_column2
  D = current date';


--
-- Name: COLUMN report_selection.selection_operator; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_selection.selection_operator IS 'EQ = Equal
GT = Greater Than
GE = Greater than or equal to
LT = Less than
LE = Less than or equal to
NE = Not Equal
IN = In
LK = Like
NA = Not Applicable';


--
-- Name: COLUMN report_selection.logic_operator; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_selection.logic_operator IS 'A = And
O = Or
N = None';


--
-- Name: COLUMN report_selection.selection_value_list; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_selection.selection_value_list IS 'For saving query lists';


--
-- Name: report_title; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_title (
    report_title_id character(16) NOT NULL,
    report_id character(16) NOT NULL,
    page_location character(1) NOT NULL,
    seqno smallint NOT NULL,
    field_type character(1) DEFAULT 'T'::bpchar NOT NULL,
    field_format smallint DEFAULT 0 NOT NULL,
    font_size smallint DEFAULT 0 NOT NULL,
    font_underline character(1) DEFAULT 'N'::bpchar NOT NULL,
    font_bold character(1) DEFAULT 'N'::bpchar NOT NULL,
    font_italic character(1) DEFAULT 'N'::bpchar NOT NULL,
    end_line character(1) DEFAULT 'Y'::bpchar NOT NULL,
    justification character(1) DEFAULT 'C'::bpchar NOT NULL,
    left_offset real DEFAULT 0 NOT NULL,
    verbiage character varying(100),
    CONSTRAINT report_title_bold_chk CHECK (((font_bold = 'Y'::bpchar) OR (font_bold = 'N'::bpchar))),
    CONSTRAINT report_title_end_line_chk CHECK (((end_line = 'Y'::bpchar) OR (end_line = 'N'::bpchar))),
    CONSTRAINT report_title_italic_chk CHECK (((font_italic = 'Y'::bpchar) OR (font_italic = 'N'::bpchar))),
    CONSTRAINT report_title_just_chk CHECK (((justification = 'L'::bpchar) OR (justification = 'C'::bpchar) OR (justification = 'R'::bpchar))),
    CONSTRAINT report_title_loc_chk CHECK (((page_location = 'A'::bpchar) OR (page_location = 'B'::bpchar))),
    CONSTRAINT report_title_type_chk CHECK (((field_type = 'T'::bpchar) OR (field_type = 'D'::bpchar) OR (field_type = 'P'::bpchar) OR (field_type = 'R'::bpchar) OR (field_type = 'C'::bpchar) OR (field_type = 'L'::bpchar) OR (field_type = 'S'::bpchar) OR (field_type = 'Q'::bpchar))),
    CONSTRAINT report_title_underline_chk CHECK (((font_underline = 'Y'::bpchar) OR (font_underline = 'N'::bpchar)))
);


ALTER TABLE public.report_title OWNER TO postgres;

--
-- Name: COLUMN report_title.page_location; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_title.page_location IS 'A = Page header
B = Page footer';


--
-- Name: COLUMN report_title.field_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_title.field_type IS 'T = text
D = current Date
P = Page number
R = Report name
C = Company name
L = Name of person logged in to Arahant
S = Sort description
Q = Selection description';


--
-- Name: COLUMN report_title.field_format; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_title.field_format IS 'What date, pageno, etc. format to use.  number refers to fixed option defined by the backend.';


--
-- Name: COLUMN report_title.font_size; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_title.font_size IS 'Uses default page size if 0';


--
-- Name: COLUMN report_title.font_underline; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_title.font_underline IS 'Yes / No';


--
-- Name: COLUMN report_title.font_bold; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_title.font_bold IS 'Yes / No';


--
-- Name: COLUMN report_title.font_italic; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_title.font_italic IS 'Yes / No';


--
-- Name: COLUMN report_title.end_line; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_title.end_line IS 'Should this line be terminated with a newline such that the next defined line will appear as the next line or on this same line?  Yes / No';


--
-- Name: COLUMN report_title.justification; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_title.justification IS 'L = Left
C = Center
R = Right';


--
-- Name: COLUMN report_title.left_offset; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_title.left_offset IS 'Only used for left justified columns (offset relative to page offset)';


--
-- Name: CONSTRAINT report_title_type_chk ON report_title; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT report_title_type_chk ON public.report_title IS '
';


--
-- Name: rights_association; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rights_association (
    security_group_id character(16) NOT NULL,
    right_id character(16) NOT NULL,
    access_level smallint DEFAULT 0 NOT NULL
);


ALTER TABLE public.rights_association OWNER TO postgres;

--
-- Name: TABLE rights_association; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.rights_association IS 'This table joins rights with users to allow or deny them a permission to do something.';


--
-- Name: COLUMN rights_association.security_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rights_association.security_group_id IS 'Join to security group.';


--
-- Name: COLUMN rights_association.right_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rights_association.right_id IS 'Join to right.';


--
-- Name: COLUMN rights_association.access_level; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rights_association.access_level IS '0=no access
1=read only
2=read/write';


--
-- Name: route; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.route (
    route_id character(16) NOT NULL,
    name character varying(30) NOT NULL,
    description character varying(80),
    project_status_id character(16),
    route_stop_id character(16),
    company_id character(16),
    last_active_date integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.route OWNER TO postgres;

--
-- Name: COLUMN route.project_status_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.route.project_status_id IS 'Default initial project status id to set on projects which are moved to this route';


--
-- Name: COLUMN route.route_stop_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.route.route_stop_id IS 'Default initial route stop id set on projects moved to this route';


--
-- Name: COLUMN route.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.route.company_id IS 'The company the record applies to, or NULL if it is globally applicable.';


--
-- Name: COLUMN route.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.route.last_active_date IS 'Last date this record should be used in a new association.  0 means no end.';


--
-- Name: route_path; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.route_path (
    route_path_id character(16) NOT NULL,
    from_status_id character(16) NOT NULL,
    to_status_id character(16) NOT NULL,
    from_stop_id character(16) NOT NULL,
    to_stop_id character(16) NOT NULL
);


ALTER TABLE public.route_path OWNER TO postgres;

--
-- Name: route_stop; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.route_stop (
    route_stop_id character(16) NOT NULL,
    route_id character(16) NOT NULL,
    org_group_id character(16),
    description character varying(80),
    project_phase_id character(16) NOT NULL,
    auto_assign_supervisor character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT route_stop_auto_chk CHECK (((auto_assign_supervisor = 'Y'::bpchar) OR (auto_assign_supervisor = 'N'::bpchar)))
);


ALTER TABLE public.route_stop OWNER TO postgres;

--
-- Name: TABLE route_stop; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.route_stop IS 'Also refered to as a decision point';


--
-- Name: COLUMN route_stop.auto_assign_supervisor; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.route_stop.auto_assign_supervisor IS 'Y or N';


--
-- Name: route_stop_checklist; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.route_stop_checklist (
    route_stop_checklist_id character(16) NOT NULL,
    route_stop_id character(16) NOT NULL,
    item_priority smallint NOT NULL,
    item_description character varying(120) NOT NULL,
    item_detail text,
    item_required character(1) NOT NULL,
    date_active integer NOT NULL,
    date_inactive integer NOT NULL,
    CONSTRAINT route_stop_cl_required_chk CHECK (((item_required = 'Y'::bpchar) OR (item_required = 'N'::bpchar)))
);


ALTER TABLE public.route_stop_checklist OWNER TO postgres;

--
-- Name: COLUMN route_stop_checklist.item_required; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.route_stop_checklist.item_required IS 'Y or N';


--
-- Name: route_type_assoc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.route_type_assoc (
    route_assoc_id character(16) NOT NULL,
    route_id character(16) NOT NULL,
    project_category_id character(16) NOT NULL,
    project_type_id character(16) NOT NULL
);


ALTER TABLE public.route_type_assoc OWNER TO postgres;

--
-- Name: sales_activity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sales_activity (
    sales_activity_id character(16) NOT NULL,
    company_id character(16),
    seqno smallint NOT NULL,
    activity_code character varying(12) NOT NULL,
    description character varying(120),
    sales_points smallint DEFAULT 0 NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.sales_activity OWNER TO postgres;

--
-- Name: COLUMN sales_activity.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sales_activity.company_id IS 'Company this applies to or NULL if ALL COMPANIES.';


--
-- Name: COLUMN sales_activity.last_active_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sales_activity.last_active_date IS 'Date record is last valid.  0 means still valid.';


--
-- Name: sales_activity_result; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sales_activity_result (
    sales_activity_result_id character(16) NOT NULL,
    sales_activity_id character(16) NOT NULL,
    description character varying(60) NOT NULL,
    seqno smallint NOT NULL,
    last_active_date integer NOT NULL,
    first_follow_up_days smallint NOT NULL,
    first_follow_up_task character varying(60),
    second_follow_up_days smallint NOT NULL,
    second_follow_up_task character varying(60),
    third_follow_up_days smallint NOT NULL,
    third_follow_up_task character varying(60)
);


ALTER TABLE public.sales_activity_result OWNER TO postgres;

--
-- Name: sales_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sales_points (
    sales_points_id character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    point_date timestamp with time zone NOT NULL,
    prospect_id character(16) NOT NULL,
    prospect_status_id character(16) NOT NULL
);


ALTER TABLE public.sales_points OWNER TO postgres;

--
-- Name: COLUMN sales_points.employee_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sales_points.employee_id IS 'Salesman who gets the points';


--
-- Name: COLUMN sales_points.point_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sales_points.point_date IS 'Timestamp of when the new status was assigned';


--
-- Name: COLUMN sales_points.prospect_status_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sales_points.prospect_status_id IS 'This is the new status ID';


--
-- Name: screen; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.screen (
    screen_id character(16) NOT NULL,
    filename character varying(255),
    auth_code character varying(48),
    screen_name character varying(100),
    description character varying(2000),
    screen_type smallint NOT NULL,
    avatar_path character varying(256),
    technology character(1) DEFAULT 'H'::bpchar NOT NULL,
    CONSTRAINT screen_screen_type_chk CHECK (((screen_type >= 1) AND (screen_type <= 5))),
    CONSTRAINT screen_tech_chk CHECK (((technology = 'F'::bpchar) OR (technology = 'H'::bpchar)))
);


ALTER TABLE public.screen OWNER TO postgres;

--
-- Name: TABLE screen; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.screen IS 'This table stores information about a screen in the system.';


--
-- Name: COLUMN screen.screen_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen.screen_id IS 'Primary key.';


--
-- Name: COLUMN screen.filename; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen.filename IS 'Directory where screen is stored';


--
-- Name: COLUMN screen.auth_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen.auth_code IS 'Code which authorizes the screen use - not used';


--
-- Name: COLUMN screen.screen_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen.screen_name IS 'Long name of screen';


--
-- Name: COLUMN screen.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen.description IS 'Detailed description of screen';


--
-- Name: COLUMN screen.screen_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen.screen_type IS '1=Regular
2=Parent
3=Child
4=wizard
5=wizard page';


--
-- Name: COLUMN screen.technology; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen.technology IS 'F = Flash
H = HTML';


--
-- Name: screen_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.screen_group (
    screen_group_id character(16) NOT NULL,
    group_name character varying(100),
    screen_id character(16),
    description character varying(2000),
    wizard_type character(1) DEFAULT 'N'::bpchar NOT NULL,
    technology character(1) DEFAULT 'F'::bpchar NOT NULL,
    CONSTRAINT screen_group_tech_chk CHECK (((technology = 'F'::bpchar) OR (technology = 'H'::bpchar))),
    CONSTRAINT screen_group_wizard_type_chk CHECK (((wizard_type = 'N'::bpchar) OR (wizard_type = 'E'::bpchar) OR (wizard_type = 'O'::bpchar)))
);


ALTER TABLE public.screen_group OWNER TO postgres;

--
-- Name: TABLE screen_group; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.screen_group IS 'This table defines a group of screens.';


--
-- Name: COLUMN screen_group.screen_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group.screen_group_id IS 'Primary key.';


--
-- Name: COLUMN screen_group.group_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group.group_name IS 'Description of screen group';


--
-- Name: COLUMN screen_group.screen_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group.screen_id IS 'Parent screen which has a bunch of dependent children';


--
-- Name: COLUMN screen_group.wizard_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group.wizard_type IS 'N = Not a Wizard
E = Benefit Enrollment Wizard
O = Employee Onboarding Wizard';


--
-- Name: COLUMN screen_group.technology; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group.technology IS 'F = Flash
H = HTML';


--
-- Name: screen_group_access; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.screen_group_access (
    security_group_id character(16) NOT NULL,
    can_access_screen_group_id character(16) NOT NULL
);


ALTER TABLE public.screen_group_access OWNER TO postgres;

--
-- Name: TABLE screen_group_access; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.screen_group_access IS 'This table determines which screen groups a user with a given security group can assign';


--
-- Name: COLUMN screen_group_access.security_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group_access.security_group_id IS 'This is the security group being controlled';


--
-- Name: COLUMN screen_group_access.can_access_screen_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group_access.can_access_screen_group_id IS 'This is the screen group it is being given access to';


--
-- Name: screen_group_hierarchy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.screen_group_hierarchy (
    screen_group_hierarchy_id character(16) NOT NULL,
    parent_screen_group_id character(16) NOT NULL,
    child_screen_group_id character(16),
    seq_no smallint NOT NULL,
    screen_id character(16),
    default_screen character(1) DEFAULT 'N'::bpchar NOT NULL,
    button_name character varying(100),
    CONSTRAINT screen_grp_hier_dflt_scn_chk CHECK (((default_screen = 'Y'::bpchar) OR (default_screen = 'N'::bpchar)))
);


ALTER TABLE public.screen_group_hierarchy OWNER TO postgres;

--
-- Name: TABLE screen_group_hierarchy; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.screen_group_hierarchy IS 'This table defines the relationships between groups of screens.';


--
-- Name: COLUMN screen_group_hierarchy.screen_group_hierarchy_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group_hierarchy.screen_group_hierarchy_id IS 'Main key';


--
-- Name: COLUMN screen_group_hierarchy.parent_screen_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group_hierarchy.parent_screen_group_id IS 'Join to parent screen group.';


--
-- Name: COLUMN screen_group_hierarchy.child_screen_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group_hierarchy.child_screen_group_id IS 'Join to child group.';


--
-- Name: COLUMN screen_group_hierarchy.seq_no; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group_hierarchy.seq_no IS 'Order of child in parent group.';


--
-- Name: COLUMN screen_group_hierarchy.screen_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group_hierarchy.screen_id IS 'Associated screen';


--
-- Name: COLUMN screen_group_hierarchy.default_screen; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group_hierarchy.default_screen IS 'Default screen in screen group';


--
-- Name: COLUMN screen_group_hierarchy.button_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.screen_group_hierarchy.button_name IS 'Only used to override a group or screen name';


--
-- Name: screen_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.screen_history (
    transaction_id integer NOT NULL,
    person_id character(16) NOT NULL,
    screen_id character(16) NOT NULL,
    time_in timestamp with time zone NOT NULL,
    time_out timestamp with time zone
);


ALTER TABLE public.screen_history OWNER TO postgres;

--
-- Name: screen_history_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.screen_history_transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.screen_history_transaction_id_seq OWNER TO postgres;

--
-- Name: screen_history_transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.screen_history_transaction_id_seq OWNED BY public.screen_history.transaction_id;


--
-- Name: security_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.security_group (
    security_group_id character(16) NOT NULL,
    id character varying(30) NOT NULL,
    name character varying(100)
);


ALTER TABLE public.security_group OWNER TO postgres;

--
-- Name: TABLE security_group; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.security_group IS 'This table defines a group of security users.';


--
-- Name: COLUMN security_group.security_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.security_group.security_group_id IS 'Primary key.';


--
-- Name: COLUMN security_group.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.security_group.id IS 'Identfier for security group';


--
-- Name: COLUMN security_group.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.security_group.name IS 'Name of security group.';


--
-- Name: security_group_access; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.security_group_access (
    security_group_id character(16) NOT NULL,
    can_access_security_group_id character(16) NOT NULL
);


ALTER TABLE public.security_group_access OWNER TO postgres;

--
-- Name: TABLE security_group_access; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.security_group_access IS 'This table determins which security group a user can assign';


--
-- Name: COLUMN security_group_access.security_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.security_group_access.security_group_id IS 'This is the security group being controlled';


--
-- Name: COLUMN security_group_access.can_access_security_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.security_group_access.can_access_security_group_id IS 'This is the security group it can access';


--
-- Name: security_group_hierarchy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.security_group_hierarchy (
    parent_security_group_id character(16) NOT NULL,
    child_security_group_id character(16) NOT NULL
);


ALTER TABLE public.security_group_hierarchy OWNER TO postgres;

--
-- Name: TABLE security_group_hierarchy; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.security_group_hierarchy IS 'This table controls the relationships between security user groups.';


--
-- Name: COLUMN security_group_hierarchy.parent_security_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.security_group_hierarchy.parent_security_group_id IS 'Join to parent security group.';


--
-- Name: COLUMN security_group_hierarchy.child_security_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.security_group_hierarchy.child_security_group_id IS 'Join to child security group.';


--
-- Name: security_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.security_token (
    right_id character(16) NOT NULL,
    identifier character varying(30) NOT NULL,
    description character varying(100)
);


ALTER TABLE public.security_token OWNER TO postgres;

--
-- Name: TABLE security_token; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.security_token IS 'This table stores rights or permissions that a user could have.';


--
-- Name: COLUMN security_token.right_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.security_token.right_id IS 'Primary key.';


--
-- Name: COLUMN security_token.identifier; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.security_token.identifier IS 'Name of the right.';


--
-- Name: COLUMN security_token.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.security_token.description IS 'A description of this right.';


--
-- Name: service; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service (
    service_id character(16) NOT NULL
);


ALTER TABLE public.service OWNER TO postgres;

--
-- Name: TABLE service; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.service IS 'Services being provided (not consumed) by company';


--
-- Name: service_subscribed; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service_subscribed (
    service_id character(16) NOT NULL,
    org_group_id character(16),
    service_name character varying(50) NOT NULL,
    description character varying(255),
    first_active_date integer NOT NULL,
    last_active_date integer NOT NULL,
    interface_code smallint DEFAULT 0 NOT NULL,
    CONSTRAINT services_sub_int_chk CHECK (((interface_code >= 0) AND (interface_code <= 1)))
);


ALTER TABLE public.service_subscribed OWNER TO postgres;

--
-- Name: TABLE service_subscribed; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.service_subscribed IS 'This is a list of possible services consumed (not provided) by a company.  This is not part of the service / product part of the system.';


--
-- Name: COLUMN service_subscribed.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.service_subscribed.org_group_id IS 'The org group this service is applicable to or NULL if all companies.';


--
-- Name: COLUMN service_subscribed.interface_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.service_subscribed.interface_code IS '0 = No interface
1 = Cobra Guard';


--
-- Name: service_subscribed_join; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service_subscribed_join (
    service_join_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    service_id character(16) NOT NULL,
    first_date integer NOT NULL,
    last_date integer NOT NULL,
    external_id character varying(20)
);


ALTER TABLE public.service_subscribed_join OWNER TO postgres;

--
-- Name: COLUMN service_subscribed_join.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.service_subscribed_join.org_group_id IS 'The org group that is utilizing the service.';


--
-- Name: COLUMN service_subscribed_join.first_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.service_subscribed_join.first_date IS 'The date the service started for this company.';


--
-- Name: COLUMN service_subscribed_join.last_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.service_subscribed_join.last_date IS 'The last date the service was active for the company.';


--
-- Name: spousal_insurance_verif; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.spousal_insurance_verif (
    spousal_ins_verif_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    date_received integer NOT NULL,
    verification_year smallint NOT NULL
);


ALTER TABLE public.spousal_insurance_verif OWNER TO postgres;

--
-- Name: standard_project; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.standard_project (
    project_id character(16) NOT NULL,
    reference character varying(10),
    description character varying(80),
    detail_desc text,
    project_sponsor_id character(16),
    managing_employee character(16),
    project_type_id character(16) NOT NULL,
    project_category_id character(16) NOT NULL,
    dollar_cap double precision DEFAULT 0 NOT NULL,
    billing_rate real DEFAULT 0 NOT NULL,
    billable character(1) DEFAULT 'U'::bpchar NOT NULL,
    requester_name character varying(50),
    product_id character(16),
    billing_method integer DEFAULT 0 NOT NULL,
    next_billing_date integer DEFAULT 0 NOT NULL,
    bill_at_org_group character(16),
    all_employees character(1) DEFAULT 'N'::bpchar NOT NULL,
    company_id character(16),
    CONSTRAINT sp_billable_check CHECK (((billable = 'Y'::bpchar) OR (billable = 'N'::bpchar) OR (billable = 'U'::bpchar))),
    CONSTRAINT standard_project_all_emp_chk CHECK (((all_employees = 'Y'::bpchar) OR (all_employees = 'N'::bpchar)))
);


ALTER TABLE public.standard_project OWNER TO postgres;

--
-- Name: TABLE standard_project; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.standard_project IS 'This table stores projects or units of work to track and bill.';


--
-- Name: COLUMN standard_project.project_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.project_id IS 'Primary key.';


--
-- Name: COLUMN standard_project.reference; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.reference IS 'ID from AQDev or some other project tracking system';


--
-- Name: COLUMN standard_project.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.description IS 'Short description of project.';


--
-- Name: COLUMN standard_project.detail_desc; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.detail_desc IS 'Long description of project.';


--
-- Name: COLUMN standard_project.project_sponsor_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.project_sponsor_id IS 'join to person table for person requesting project.';


--
-- Name: COLUMN standard_project.managing_employee; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.managing_employee IS 'join to employee table for person in charge of project.';


--
-- Name: COLUMN standard_project.project_type_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.project_type_id IS 'Join to type table.';


--
-- Name: COLUMN standard_project.project_category_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.project_category_id IS 'Join to category table.';


--
-- Name: COLUMN standard_project.dollar_cap; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.dollar_cap IS 'Maximum amount to be spent on this project.';


--
-- Name: COLUMN standard_project.billing_rate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.billing_rate IS 'Default rate to charge on this project.';


--
-- Name: COLUMN standard_project.billable; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.billable IS 'Flag to mark if this project is billable.  Y, N, or U';


--
-- Name: COLUMN standard_project.requester_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.requester_name IS 'Text entry of requester name, because person join isn''t required.';


--
-- Name: COLUMN standard_project.product_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.product_id IS 'Join to product this project applies to.';


--
-- Name: COLUMN standard_project.billing_method; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.billing_method IS '1=At proj status assoc with accounting
2=At specified date
3=Weekly
4=Monthly';


--
-- Name: COLUMN standard_project.next_billing_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.next_billing_date IS 'Next date project should be billed';


--
-- Name: COLUMN standard_project.bill_at_org_group; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.bill_at_org_group IS 'Bill when project reaches a status associated with theis org group';


--
-- Name: COLUMN standard_project.all_employees; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.standard_project.all_employees IS 'Is this project applicable to all employees?  (Y/N)';


--
-- Name: student_verification; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student_verification (
    student_verification_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    school_year smallint NOT NULL,
    calendar_period smallint NOT NULL,
    CONSTRAINT student_verif_period_chk CHECK (((calendar_period >= 1) AND (calendar_period <= 4))),
    CONSTRAINT student_verif_year_chk CHECK (((school_year > 2006) AND (school_year < 2040)))
);


ALTER TABLE public.student_verification OWNER TO postgres;

--
-- Name: time_off_accrual_calc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.time_off_accrual_calc (
    time_off_accrual_calc_id character(16) NOT NULL,
    benefit_config_id character(16) NOT NULL,
    accrual_method character(1) NOT NULL,
    trial_period smallint NOT NULL,
    period_start character(1) NOT NULL,
    max_carry_over_hours smallint NOT NULL,
    carry_over_percentage smallint NOT NULL,
    first_active_date integer NOT NULL,
    last_active_date integer NOT NULL,
    accrual_type character(1) DEFAULT 'D'::bpchar NOT NULL,
    period_start_date integer DEFAULT 0 NOT NULL,
    CONSTRAINT time_off_acccal_perchk CHECK (((carry_over_percentage >= 0) AND (carry_over_percentage <= 100))),
    CONSTRAINT time_off_acccalc_pstart_chk CHECK (((period_start = 'C'::bpchar) OR (period_start = 'H'::bpchar) OR (period_start = 'F'::bpchar) OR (period_start = 'S'::bpchar))),
    CONSTRAINT timeoff_acc_calc_meth_chk CHECK (((accrual_method = 'A'::bpchar) OR (accrual_method = 'H'::bpchar) OR (accrual_method = 'M'::bpchar) OR (accrual_method = 'P'::bpchar))),
    CONSTRAINT timeoff_acccalc_perdate CHECK (((period_start_date >= 0) AND (period_start_date <= 1231))),
    CONSTRAINT tomeoff_acccalc_type_chk CHECK (((accrual_type = 'D'::bpchar) OR (accrual_type = 'R'::bpchar)))
);


ALTER TABLE public.time_off_accrual_calc OWNER TO postgres;

--
-- Name: COLUMN time_off_accrual_calc.accrual_method; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_accrual_calc.accrual_method IS 'A = Annual allotment
M = Monthly allotment
P = Pay period allotment
H = Hourly allotment';


--
-- Name: COLUMN time_off_accrual_calc.trial_period; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_accrual_calc.trial_period IS 'Days after hire until vacation starts accruing';


--
-- Name: COLUMN time_off_accrual_calc.period_start; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_accrual_calc.period_start IS 'C = Calendar year
H = annaversory of Hire date
F = Fiscal year
S = Specific date (in period_start_date)';


--
-- Name: COLUMN time_off_accrual_calc.max_carry_over_hours; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_accrual_calc.max_carry_over_hours IS 'Max hours that can be carried over, 0 means none';


--
-- Name: COLUMN time_off_accrual_calc.carry_over_percentage; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_accrual_calc.carry_over_percentage IS 'Percentage of left over time that can be carried over.';


--
-- Name: COLUMN time_off_accrual_calc.accrual_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_accrual_calc.accrual_type IS 'D = aDvanced
R = aRrears';


--
-- Name: COLUMN time_off_accrual_calc.period_start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_accrual_calc.period_start_date IS 'MMDD';


--
-- Name: time_off_accrual_seniority; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.time_off_accrual_seniority (
    accrual_seniority_id character(16) NOT NULL,
    time_off_accrual_calc_id character(16) NOT NULL,
    years_of_service smallint NOT NULL,
    hours_accrued real NOT NULL
);


ALTER TABLE public.time_off_accrual_seniority OWNER TO postgres;

--
-- Name: COLUMN time_off_accrual_seniority.years_of_service; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_accrual_seniority.years_of_service IS 'Number of years when new accural takes affect';


--
-- Name: COLUMN time_off_accrual_seniority.hours_accrued; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_accrual_seniority.hours_accrued IS 'This is either the annual amount accrued, or hours accrued per hour worked based on the calc method.';


--
-- Name: time_off_request; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.time_off_request (
    request_id character(16) NOT NULL,
    requesting_person_id character(16) NOT NULL,
    project_id character(16) NOT NULL,
    start_date integer NOT NULL,
    start_time integer NOT NULL,
    return_date integer NOT NULL,
    return_time integer NOT NULL,
    request_status character(1) NOT NULL,
    request_date timestamp with time zone NOT NULL,
    approving_person_id character(16),
    approval_date timestamp with time zone,
    requesting_comment character varying(1024),
    approval_comment character varying(1024),
    CONSTRAINT time_off_status_chk CHECK (((request_status = 'O'::bpchar) OR (request_status = 'A'::bpchar) OR (request_status = 'R'::bpchar) OR (request_status = 'E'::bpchar)))
);


ALTER TABLE public.time_off_request OWNER TO postgres;

--
-- Name: COLUMN time_off_request.request_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_request.request_id IS 'main key';


--
-- Name: COLUMN time_off_request.requesting_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_request.requesting_person_id IS 'The person the time-off is for';


--
-- Name: COLUMN time_off_request.project_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_request.project_id IS 'What type of time off is it';


--
-- Name: COLUMN time_off_request.start_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_request.start_date IS 'The date the time-off is to start (YYYYMMDD)';


--
-- Name: COLUMN time_off_request.start_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_request.start_time IS 'The time the time-off is to start';


--
-- Name: COLUMN time_off_request.return_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_request.return_date IS 'The date the employee will return (YYYYMMDD)';


--
-- Name: COLUMN time_off_request.return_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_request.return_time IS 'The time the employee will return to work';


--
-- Name: COLUMN time_off_request.request_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_request.request_status IS 'Open request
Approved
Rejected
Entered into employee accrual
';


--
-- Name: COLUMN time_off_request.request_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_request.request_date IS 'Date / time request submitted';


--
-- Name: COLUMN time_off_request.approval_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_off_request.approval_date IS 'Date / time approval info last edited';


--
-- Name: time_reject; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.time_reject (
    time_reject_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    reject_date integer NOT NULL
);


ALTER TABLE public.time_reject OWNER TO postgres;

--
-- Name: COLUMN time_reject.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_reject.person_id IS 'The person who has the rejected time entry';


--
-- Name: COLUMN time_reject.reject_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_reject.reject_date IS 'Date of the rejection';


--
-- Name: time_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.time_type (
    time_type_id character(16) NOT NULL,
    description character varying(20) NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    default_billable character(1) DEFAULT 'U'::bpchar NOT NULL,
    default_type character(1) DEFAULT 'N'::bpchar NOT NULL,
    CONSTRAINT time_type_billable_chk CHECK (((default_billable = 'Y'::bpchar) OR (default_billable = 'N'::bpchar) OR (default_billable = 'U'::bpchar))),
    CONSTRAINT time_type_type_chk CHECK (((default_type = 'Y'::bpchar) OR (default_type = 'N'::bpchar)))
);


ALTER TABLE public.time_type OWNER TO postgres;

--
-- Name: COLUMN time_type.default_billable; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_type.default_billable IS 'Unknown, Yes, No';


--
-- Name: COLUMN time_type.default_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.time_type.default_type IS 'Yes, No';


--
-- Name: timesheet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.timesheet (
    timesheet_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    entry_state character(1) NOT NULL,
    billable character(1) DEFAULT 'U'::bpchar NOT NULL,
    message_id character(16),
    invoice_line_item_id character(16),
    description character varying(1500),
    beginning_entry_person_id character(16) NOT NULL,
    beginning_entry_date timestamp with time zone NOT NULL,
    beginning_date integer NOT NULL,
    beginning_time integer NOT NULL,
    beginning_tz_offset smallint DEFAULT 2000 NOT NULL,
    end_entry_person_id character(16),
    end_entry_date timestamp with time zone,
    end_date integer DEFAULT 0 NOT NULL,
    end_time integer NOT NULL,
    end_tz_offset smallint DEFAULT 2000 NOT NULL,
    total_hours double precision NOT NULL,
    wage_type_id character(16) NOT NULL,
    private_description character varying(1500),
    total_expenses real DEFAULT 0 NOT NULL,
    fixed_pay double precision DEFAULT 0.0 NOT NULL,
    project_shift_id character(16) NOT NULL,
    time_type_id character(16) NOT NULL,
    CONSTRAINT check_time_state CHECK (((entry_state = 'A'::bpchar) OR (entry_state = 'I'::bpchar) OR (entry_state = 'D'::bpchar) OR (entry_state = 'N'::bpchar) OR (entry_state = 'C'::bpchar) OR (entry_state = 'F'::bpchar) OR (entry_state = 'R'::bpchar) OR (entry_state = 'P'::bpchar))),
    CONSTRAINT timesheet_billable_chk CHECK (((billable = 'Y'::bpchar) OR (billable = 'N'::bpchar) OR (billable = 'U'::bpchar)))
);


ALTER TABLE public.timesheet OWNER TO postgres;

--
-- Name: TABLE timesheet; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.timesheet IS 'A timesheet entry for a user.';


--
-- Name: COLUMN timesheet.timesheet_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.timesheet_id IS 'Primary key.';


--
-- Name: COLUMN timesheet.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.person_id IS 'Join to employee.';


--
-- Name: COLUMN timesheet.entry_state; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.entry_state IS 'Approved
New
Changed
Fixed
Rejected
Deferred
Invoiced
Problem
';


--
-- Name: COLUMN timesheet.billable; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.billable IS 'Flag for if this entry is billable.  Y, N, or U';


--
-- Name: COLUMN timesheet.message_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.message_id IS 'Join to any message about this entry.';


--
-- Name: COLUMN timesheet.invoice_line_item_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.invoice_line_item_id IS 'Join to line item this entry may have been invoiced in.';


--
-- Name: COLUMN timesheet.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.description IS 'Description of time spent.';


--
-- Name: COLUMN timesheet.beginning_entry_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.beginning_entry_person_id IS 'Person who last edited beginning_date or beginning_time';


--
-- Name: COLUMN timesheet.beginning_entry_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.beginning_entry_date IS 'Date beginning_date & beginning_time where last edited';


--
-- Name: COLUMN timesheet.beginning_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.beginning_date IS 'Date work began - YYYYMMDD';


--
-- Name: COLUMN timesheet.beginning_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.beginning_time IS 'Start of time range this entry covers.  Format HHMMSSmmm';


--
-- Name: COLUMN timesheet.beginning_tz_offset; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.beginning_tz_offset IS 'Offset from UTC in minutes
2000 means default to server timezone';


--
-- Name: COLUMN timesheet.end_entry_person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.end_entry_person_id IS 'Person who last edited end_date or end_time';


--
-- Name: COLUMN timesheet.end_entry_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.end_entry_date IS 'Date end_date & end_time last edited';


--
-- Name: COLUMN timesheet.end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.end_date IS 'Date work ended - YYYYMMDD';


--
-- Name: COLUMN timesheet.end_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.end_time IS 'End of time range this entry covers.  Format HHMMSSmmm';


--
-- Name: COLUMN timesheet.end_tz_offset; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.end_tz_offset IS 'Offset from UTC in minutes
2000 means default to server timezone';


--
-- Name: COLUMN timesheet.total_hours; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.total_hours IS 'Amount of time covered by this entry.';


--
-- Name: COLUMN timesheet.fixed_pay; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.timesheet.fixed_pay IS 'Fixed payment amount for a week regardless of hours (what was agreed).  Used in weekly timekeeping.';


--
-- Name: user_attribute; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_attribute (
    person_id character(16) NOT NULL,
    user_attribute character varying(20) NOT NULL,
    attribute_value character varying(80) NOT NULL
);


ALTER TABLE public.user_attribute OWNER TO postgres;

--
-- Name: vendor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vendor (
    org_group_id character(16) NOT NULL,
    account_number character varying(40),
    dflt_expense_acct character(16),
    dflt_ap_acct character(16),
    company_id character(16),
    interface_id smallint DEFAULT 0 NOT NULL
);


ALTER TABLE public.vendor OWNER TO postgres;

--
-- Name: TABLE vendor; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.vendor IS 'This table stores information specific to a vendor company';


--
-- Name: COLUMN vendor.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.vendor.org_group_id IS 'Join to org group and company this vendor data applies to.';


--
-- Name: COLUMN vendor.account_number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.vendor.account_number IS 'User''s company account number with vendor.';


--
-- Name: COLUMN vendor.dflt_expense_acct; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.vendor.dflt_expense_acct IS 'Default Expense GL Account';


--
-- Name: COLUMN vendor.dflt_ap_acct; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.vendor.dflt_ap_acct IS 'Default Accounts Payable Account';


--
-- Name: COLUMN vendor.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.vendor.company_id IS 'The Arahant user company this vendor company is associated with.  If NULL the vendor applies to all companies.';


--
-- Name: COLUMN vendor.interface_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.vendor.interface_id IS 'Constants defined internally in the backend in bean.VendorCompany.java';


--
-- Name: vendor_contact; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vendor_contact (
    person_id character(16) NOT NULL
);


ALTER TABLE public.vendor_contact OWNER TO postgres;

--
-- Name: COLUMN vendor_contact.person_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.vendor_contact.person_id IS 'Join to person record.';


--
-- Name: vendor_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vendor_group (
    vendor_group_id character(16) NOT NULL,
    vendor_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    group_vendor_id character varying(20) NOT NULL
);


ALTER TABLE public.vendor_group OWNER TO postgres;

--
-- Name: TABLE vendor_group; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.vendor_group IS 'This table keeps group specific vendor (EDI) codes.';


--
-- Name: COLUMN vendor_group.org_group_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.vendor_group.org_group_id IS 'The org_group this is specific to.  Should also include sub-groups as well.';


--
-- Name: COLUMN vendor_group.group_vendor_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.vendor_group.group_vendor_id IS 'This is a group specific vendor ID likely used for EDI';


--
-- Name: wage_paid; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wage_paid (
    wage_paid_id character(16) NOT NULL,
    employee_id character(16) NOT NULL,
    beg_period integer NOT NULL,
    end_period integer NOT NULL,
    date_paid integer NOT NULL,
    payment_method smallint NOT NULL,
    check_number integer NOT NULL,
    CONSTRAINT wage_paid_method_chk CHECK (((payment_method >= 1) AND (payment_method <= 3)))
);


ALTER TABLE public.wage_paid OWNER TO postgres;

--
-- Name: COLUMN wage_paid.payment_method; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wage_paid.payment_method IS '1 = check
2 = deposit
3 = cash';


--
-- Name: wage_paid_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wage_paid_detail (
    wage_detail_id character(16) NOT NULL,
    wage_paid_id character(16) NOT NULL,
    wage_type_id character(16) NOT NULL,
    wage_amount double precision NOT NULL,
    wage_base double precision DEFAULT 0 NOT NULL
);


ALTER TABLE public.wage_paid_detail OWNER TO postgres;

--
-- Name: wage_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wage_type (
    wage_type_id character(16) NOT NULL,
    org_group_id character(16) NOT NULL,
    wage_name character varying(31) NOT NULL,
    period_type smallint NOT NULL,
    wage_type smallint NOT NULL,
    expense_account character(16),
    first_active_date integer DEFAULT 0 NOT NULL,
    last_active_date integer DEFAULT 0 NOT NULL,
    is_deduction character(1) NOT NULL,
    liability_account character(16),
    wage_code character varying(8),
    CONSTRAINT wage_type_deduct_chk CHECK (((is_deduction = 'Y'::bpchar) OR (is_deduction = 'N'::bpchar))),
    CONSTRAINT wage_type_period_type_chk CHECK (((period_type >= 1) AND (period_type <= 3))),
    CONSTRAINT wage_type_wage_type_ckk CHECK ((((wage_type >= 1) AND (wage_type <= 6)) OR ((wage_type >= 101) AND (wage_type <= 103)) OR ((wage_type >= 201) AND (wage_type <= 201)) OR (wage_type = 999)))
);


ALTER TABLE public.wage_type OWNER TO postgres;

--
-- Name: COLUMN wage_type.period_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wage_type.period_type IS '1 = hourly
2 = salary
3 = one time';


--
-- Name: COLUMN wage_type.wage_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wage_type.wage_type IS '1 = regular
2 = overtime
3 = commission
4 = vacation
5 = sick
6 = bonus
101 = FIT (Federal Income tax)
102 = FICA (Social Security)
103 = Medicare
201 = State Income Tax
999 = unknown';


--
-- Name: COLUMN wage_type.wage_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wage_type.wage_code IS 'This is the wage (or payroll) code used by the external accounting system.  It corresponds to the wage_type column (i.e. each different wage type would have its own wage code).';


--
-- Name: wizard_config_benefit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wizard_config_benefit (
    wizard_config_ben_id character(16) NOT NULL,
    wizard_config_cat_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    benefit_id character(16) NOT NULL,
    screen_id character(16),
    benefit_name character varying(60) NOT NULL,
    avatar_path character varying(256),
    avatar_location character(2) DEFAULT 'BC'::bpchar NOT NULL,
    instructions text,
    decline_message character varying(1200)
);


ALTER TABLE public.wizard_config_benefit OWNER TO postgres;

--
-- Name: COLUMN wizard_config_benefit.avatar_location; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_config_benefit.avatar_location IS 'Avatar location on the screen
TR = top right
TL = top left
TC = top center
BL = bottom left
BR = bottom right
BC = bottom center
CE = center';


--
-- Name: COLUMN wizard_config_benefit.decline_message; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_config_benefit.decline_message IS 'Message shown when user declines a benefit.  This overrides a default message.';


--
-- Name: wizard_config_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wizard_config_category (
    wizard_config_cat_id character(16) NOT NULL,
    wizard_configuration_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    benefit_cat_id character(16) NOT NULL,
    screen_id character(16),
    description character varying(45) NOT NULL,
    avatar_path character varying(256),
    avatar_location character(2) DEFAULT 'BC'::bpchar NOT NULL,
    instructions text
);


ALTER TABLE public.wizard_config_category OWNER TO postgres;

--
-- Name: COLUMN wizard_config_category.avatar_location; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_config_category.avatar_location IS 'Avatar location on the screen
TR = top right
TL = top left
TC = top center
BL = bottom left
BR = bottom right
BC = bottom center
CE = center';


--
-- Name: wizard_config_config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wizard_config_config (
    wizard_config_conf_id character(16) NOT NULL,
    wizard_config_ben_id character(16) NOT NULL,
    seqno smallint NOT NULL,
    benefit_config_id character(16) NOT NULL,
    config_name character varying(60) NOT NULL
);


ALTER TABLE public.wizard_config_config OWNER TO postgres;

--
-- Name: wizard_config_project_a; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wizard_config_project_a (
    wizard_config_project_a_id character(16) NOT NULL,
    wizard_configuration_id character(16) NOT NULL,
    person_id character(16) NOT NULL
);


ALTER TABLE public.wizard_config_project_a OWNER TO postgres;

--
-- Name: wizard_configuration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wizard_configuration (
    wizard_configuration_id character(16) NOT NULL,
    config_name character varying(20) NOT NULL,
    description character varying(60),
    wizard_type character(1),
    fullscreen character(1) DEFAULT 'N'::bpchar NOT NULL,
    show_inapplicable character(1) DEFAULT 'Y'::bpchar NOT NULL,
    allow_inapplicable character(1) DEFAULT 'N'::bpchar NOT NULL,
    remember_state character(1) DEFAULT 'Y'::bpchar NOT NULL,
    progress_pane_buttons character(1) DEFAULT 'Y'::bpchar NOT NULL,
    skip_presentation character(1) DEFAULT 'Y'::bpchar NOT NULL,
    use_tool_tips character(1) DEFAULT 'Y'::bpchar NOT NULL,
    show_demographics character(1) DEFAULT 'Y'::bpchar NOT NULL,
    show_dependents character(1) DEFAULT 'Y'::bpchar NOT NULL,
    company_id character(16),
    benefit_class_id character(16),
    project_summary character varying(20) NOT NULL,
    project_status_id character(16) NOT NULL,
    project_category_id character(16) NOT NULL,
    project_type_id character(16) NOT NULL,
    wizard_version character(1) NOT NULL,
    lock_on_finalize character(1) DEFAULT 'Y'::bpchar NOT NULL,
    hr_contact_id character(16),
    physician_selection_mode character(1) DEFAULT 'I'::bpchar NOT NULL,
    demographic_screen_id character(16),
    demographic_instructions character varying(1000),
    review_report smallint DEFAULT 0 NOT NULL,
    benefit_report smallint DEFAULT 0 NOT NULL,
    auto_approve_declines character(1) DEFAULT 'Y'::bpchar NOT NULL,
    payment_info character(1) DEFAULT 'N'::bpchar NOT NULL,
    welcome_avatar character varying(256),
    qualifying_event_avatar character varying(256),
    demographics_avatar character varying(256),
    dependents_avatar character varying(256),
    review_avatar character varying(256),
    finish_avatar character varying(256),
    enable_avatars character(1) DEFAULT 'Y'::bpchar NOT NULL,
    show_annual_cost character(1) DEFAULT 'N'::bpchar NOT NULL,
    show_monthly_cost character(1) DEFAULT 'N'::bpchar NOT NULL,
    show_ppp_cost character(1) DEFAULT 'Y'::bpchar NOT NULL,
    show_qualifying_event character(1) DEFAULT 'Y'::bpchar NOT NULL,
    allow_demographic_changes character(1) DEFAULT 'Y'::bpchar NOT NULL,
    hde_type smallint DEFAULT 1 NOT NULL,
    hde_period smallint DEFAULT 0 NOT NULL,
    hde_days_before smallint DEFAULT 0 NOT NULL,
    hde_days_after smallint DEFAULT 30 NOT NULL,
    allow_report_from_review character(1) DEFAULT 'Y'::bpchar NOT NULL,
    CONSTRAINT wizard_allow_chk CHECK (((allow_inapplicable = 'Y'::bpchar) OR (allow_inapplicable = 'N'::bpchar))),
    CONSTRAINT wizard_conf_adc_chk CHECK (((allow_demographic_changes = 'Y'::bpchar) OR (allow_demographic_changes = 'N'::bpchar))),
    CONSTRAINT wizard_conf_fullsc_chk CHECK (((fullscreen = 'Y'::bpchar) OR (fullscreen = 'N'::bpchar))),
    CONSTRAINT wizard_conf_hde_type_chk CHECK (((hde_type >= 1) AND (hde_type <= 5))),
    CONSTRAINT wizard_conf_show_chk CHECK (((show_inapplicable = 'Y'::bpchar) OR (show_inapplicable = 'N'::bpchar))),
    CONSTRAINT wizard_conf_sqe_chk CHECK (((show_qualifying_event = 'Y'::bpchar) OR (show_qualifying_event = 'N'::bpchar))),
    CONSTRAINT wizard_config_autodecline_chk CHECK (((auto_approve_declines = 'Y'::bpchar) OR (auto_approve_declines = 'N'::bpchar))),
    CONSTRAINT wizard_config_enable_avatar_chk CHECK (((enable_avatars = 'Y'::bpchar) OR (enable_avatars = 'N'::bpchar))),
    CONSTRAINT wizard_config_review_rpt_chk CHECK (((allow_report_from_review = 'Y'::bpchar) OR (allow_report_from_review = 'N'::bpchar))),
    CONSTRAINT wizard_config_show_ann_chk CHECK (((show_annual_cost = 'Y'::bpchar) OR (show_annual_cost = 'N'::bpchar))),
    CONSTRAINT wizard_config_show_mothly_chk CHECK (((show_monthly_cost = 'Y'::bpchar) OR (show_monthly_cost = 'N'::bpchar))),
    CONSTRAINT wizard_config_show_ppp_chk CHECK (((show_ppp_cost = 'Y'::bpchar) OR (show_ppp_cost = 'N'::bpchar))),
    CONSTRAINT wizard_config_type_chk CHECK (((wizard_type = 'E'::bpchar) OR (wizard_type = 'O'::bpchar))),
    CONSTRAINT wizard_config_version_chk CHECK (((wizard_version = '2'::bpchar) OR (wizard_version = '3'::bpchar))),
    CONSTRAINT wizard_configu_pay_chk CHECK (((payment_info = 'Y'::bpchar) OR (payment_info = 'N'::bpchar))),
    CONSTRAINT wizard_configuration_docshow_chk CHECK (((physician_selection_mode = 'I'::bpchar) OR (physician_selection_mode = 'E'::bpchar))),
    CONSTRAINT wizard_demograph_chk CHECK (((show_demographics = 'Y'::bpchar) OR (show_demographics = 'N'::bpchar))),
    CONSTRAINT wizard_progress_chk CHECK (((progress_pane_buttons = 'Y'::bpchar) OR (progress_pane_buttons = 'N'::bpchar))),
    CONSTRAINT wizard_remstate_chk CHECK (((remember_state = 'Y'::bpchar) OR (remember_state = 'N'::bpchar))),
    CONSTRAINT wizard_showdep_chk CHECK (((show_dependents = 'Y'::bpchar) OR (show_dependents = 'N'::bpchar))),
    CONSTRAINT wizard_skip_chk CHECK (((skip_presentation = 'Y'::bpchar) OR (skip_presentation = 'N'::bpchar))),
    CONSTRAINT wizard_tooltip_chk CHECK (((use_tool_tips = 'Y'::bpchar) OR (use_tool_tips = 'N'::bpchar)))
);


ALTER TABLE public.wizard_configuration OWNER TO postgres;

--
-- Name: COLUMN wizard_configuration.wizard_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.wizard_type IS 'E = Enrollment
O = Onboarding';


--
-- Name: COLUMN wizard_configuration.fullscreen; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.fullscreen IS 'Y = Yes
N = No';


--
-- Name: COLUMN wizard_configuration.show_inapplicable; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.show_inapplicable IS 'Y = Yes
N = No';


--
-- Name: COLUMN wizard_configuration.allow_inapplicable; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.allow_inapplicable IS 'Y = Yes
N = No';


--
-- Name: COLUMN wizard_configuration.remember_state; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.remember_state IS 'Y = Yes
N = No';


--
-- Name: COLUMN wizard_configuration.progress_pane_buttons; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.progress_pane_buttons IS 'Y = Yes
N = No';


--
-- Name: COLUMN wizard_configuration.skip_presentation; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.skip_presentation IS 'Y = Yes
N = No';


--
-- Name: COLUMN wizard_configuration.use_tool_tips; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.use_tool_tips IS 'Y = Yes
N = No';


--
-- Name: COLUMN wizard_configuration.show_demographics; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.show_demographics IS 'Y = Yes
N = No';


--
-- Name: COLUMN wizard_configuration.show_dependents; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.show_dependents IS 'Y = Yes
N = No';


--
-- Name: COLUMN wizard_configuration.company_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.company_id IS 'Applicable company (or org_group) or NULL for all';


--
-- Name: COLUMN wizard_configuration.benefit_class_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.benefit_class_id IS 'Applicable employee class or NULL for all';


--
-- Name: COLUMN wizard_configuration.project_summary; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.project_summary IS 'Name of project to create';


--
-- Name: COLUMN wizard_configuration.project_status_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.project_status_id IS 'Project status ID for created project';


--
-- Name: COLUMN wizard_configuration.project_category_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.project_category_id IS 'Project category of created project';


--
-- Name: COLUMN wizard_configuration.project_type_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.project_type_id IS 'Project type of created project';


--
-- Name: COLUMN wizard_configuration.wizard_version; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.wizard_version IS '2 or 3';


--
-- Name: COLUMN wizard_configuration.lock_on_finalize; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.lock_on_finalize IS 'Y = locks out employees after they finalize
N = allows them to go back in and make changes';


--
-- Name: COLUMN wizard_configuration.physician_selection_mode; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.physician_selection_mode IS 'I = Immediate
E = at End';


--
-- Name: COLUMN wizard_configuration.welcome_avatar; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.welcome_avatar IS 'Path to avatar (.flv) file';


--
-- Name: COLUMN wizard_configuration.qualifying_event_avatar; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.qualifying_event_avatar IS 'Path to avatar (.flv) file';


--
-- Name: COLUMN wizard_configuration.demographics_avatar; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.demographics_avatar IS 'Path to avatar (.flv) file';


--
-- Name: COLUMN wizard_configuration.dependents_avatar; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.dependents_avatar IS 'Path to avatar (.flv) file';


--
-- Name: COLUMN wizard_configuration.review_avatar; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.review_avatar IS 'Path to avatar (.flv) file';


--
-- Name: COLUMN wizard_configuration.finish_avatar; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.finish_avatar IS 'Path to avatar (.flv) file';


--
-- Name: COLUMN wizard_configuration.enable_avatars; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.enable_avatars IS 'Controls all avatars
Y = enabled
N = disable all avatars';


--
-- Name: COLUMN wizard_configuration.show_qualifying_event; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.show_qualifying_event IS 'Should qualifying event wizard types be accepted';


--
-- Name: COLUMN wizard_configuration.hde_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.hde_type IS 'Hire Date Elegibility type
1 = First day of employment
2 = First of the month following hde_period days of employment
3 = First of the month following hde_period months of employment
4 = After hde_period days of employment
5 = After hde_period months of employment';


--
-- Name: COLUMN wizard_configuration.hde_period; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.hde_period IS 'Period (days or months) depending on hde_type';


--
-- Name: COLUMN wizard_configuration.hde_days_before; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.hde_days_before IS 'Number of days before the elegibility date to allow new enrollment';


--
-- Name: COLUMN wizard_configuration.hde_days_after; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.hde_days_after IS 'Number of days after the elegibility date to allow new enrollment';


--
-- Name: COLUMN wizard_configuration.allow_report_from_review; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_configuration.allow_report_from_review IS 'Allow the benefit summary report on the benefit review screen';


--
-- Name: wizard_project; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wizard_project (
    wizard_project_id character(16) NOT NULL,
    project_id character(16) NOT NULL,
    project_action character(1) NOT NULL,
    benefit_join_id character(16),
    completed character(1) DEFAULT 'N'::bpchar NOT NULL,
    date_completed timestamp with time zone,
    person_completed character(16),
    hr_benefit_join_h_id character(32),
    CONSTRAINT wizard_project_action_chk CHECK (((project_action = 'A'::bpchar) OR (project_action = 'C'::bpchar) OR (project_action = 'D'::bpchar))),
    CONSTRAINT wizard_project_completed_chk CHECK (((completed = 'Y'::bpchar) OR (completed = 'N'::bpchar)))
);


ALTER TABLE public.wizard_project OWNER TO postgres;

--
-- Name: COLUMN wizard_project.project_action; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_project.project_action IS 'A = Approval
C = Benefit change reason
D = Demographic change';


--
-- Name: COLUMN wizard_project.benefit_join_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_project.benefit_join_id IS 'should be the policy join, required if project_action is ''A'' or ''C''';


--
-- Name: COLUMN wizard_project.hr_benefit_join_h_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.wizard_project.hr_benefit_join_h_id IS 'A GIUD that links to the history record if the record is moved to history.';


--
-- Name: worker_confirmation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.worker_confirmation (
    worker_confirmation_id character(16) NOT NULL,
    person_id character(16) NOT NULL,
    confirmation_time timestamp with time zone NOT NULL,
    latitude double precision DEFAULT 0 NOT NULL,
    longitude double precision DEFAULT 0 NOT NULL,
    distance integer DEFAULT '-1'::integer NOT NULL,
    location character varying(60),
    minutes integer DEFAULT '-1'::integer NOT NULL,
    project_shift_id character(16) NOT NULL,
    who_added character(16),
    notes character varying(256)
);


ALTER TABLE public.worker_confirmation OWNER TO postgres;

--
-- Name: COLUMN worker_confirmation.distance; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.worker_confirmation.distance IS 'Miles between current location and the project
-1 means unknown';


--
-- Name: COLUMN worker_confirmation.location; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.worker_confirmation.location IS 'City and state name';


--
-- Name: COLUMN worker_confirmation.minutes; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.worker_confirmation.minutes IS 'Remaining minutes of travel';


--
-- Name: COLUMN worker_confirmation.who_added; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.worker_confirmation.who_added IS 'Who performed the check in.  Null means person_id added it themselves.';


--
-- Name: zipcode_location; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zipcode_location (
    zipcode character(5) NOT NULL,
    state_abr character(2) NOT NULL,
    city character varying(30) NOT NULL,
    state character varying(25) NOT NULL,
    latitude double precision NOT NULL,
    longitude double precision NOT NULL
);


ALTER TABLE public.zipcode_location OWNER TO postgres;

--
-- Name: zipcode_lookup; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zipcode_lookup (
    zipcode character varying(15) NOT NULL,
    city character varying(50) NOT NULL,
    county character varying(30),
    state character varying(2),
    zipcode_id character(16) NOT NULL
);


ALTER TABLE public.zipcode_lookup OWNER TO postgres;

--
-- Name: TABLE zipcode_lookup; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.zipcode_lookup IS 'This is a zipcode lookup table to list all zipcodes, states, etc..';


--
-- Name: COLUMN zipcode_lookup.zipcode; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.zipcode_lookup.zipcode IS 'Zip code';


--
-- Name: COLUMN zipcode_lookup.city; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.zipcode_lookup.city IS 'The City.';


--
-- Name: COLUMN zipcode_lookup.county; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.zipcode_lookup.county IS 'The County.';


--
-- Name: COLUMN zipcode_lookup.state; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.zipcode_lookup.state IS 'The State.';


--
-- Name: COLUMN zipcode_lookup.zipcode_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.zipcode_lookup.zipcode_id IS 'Primary key.';


--
-- Name: change_log change_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.change_log ALTER COLUMN change_id SET DEFAULT nextval('public.change_log_id_seq'::regclass);


--
-- Name: screen_history transaction_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_history ALTER COLUMN transaction_id SET DEFAULT nextval('public.screen_history_transaction_id_seq'::regclass);


--
-- Data for Name: address; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.address (address_id, street, city, state, zip, person_join, org_group_join, address_type, street2, country_code, county, record_type, time_zone_offset) FROM stdin;
\.


--
-- Data for Name: address_cr; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.address_cr (change_request_id, real_record_id, change_status, request_time, change_record_id, requestor_id, approver_id, approval_time, project_id) FROM stdin;
\.


--
-- Data for Name: agency; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.agency (agency_id, agency_external_id) FROM stdin;
\.


--
-- Data for Name: agency_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.agency_join (agency_join_id, agency_id, company_id) FROM stdin;
\.


--
-- Data for Name: agent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.agent (agent_id, ext_ref) FROM stdin;
\.


--
-- Data for Name: agent_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.agent_join (agent_join_id, agent_id, company_id, approved, approved_by_person_id, approved_date) FROM stdin;
\.


--
-- Data for Name: agreement_form; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.agreement_form (agreement_form_id, form_date, description, summary, file_name_ext, expiration_date, form) FROM stdin;
\.


--
-- Data for Name: agreement_person_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.agreement_person_join (agreement_person_join_id, person_id, agreement_form_id, agreement_time) FROM stdin;
\.


--
-- Data for Name: alert; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.alert (alert_id, alert_distribution, start_date, last_date, alert_short, alert_long, org_group_id, person_id, last_change_person_id, last_chage_datetime) FROM stdin;
\.


--
-- Data for Name: alert_person_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.alert_person_join (alert_id, person_id) FROM stdin;
\.


--
-- Data for Name: applicant; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.applicant (person_id, applicant_source_id, applicant_status_id, first_aware_date, comments, eeo_race_id, date_available, desired_salary, referred_by, years_experience, day_shift, night_shift, veteran, signature, when_signed, travel_personal, travel_friend, travel_public, travel_unknown, agrees, agreement_name, agreement_date, background_check_authorized) FROM stdin;
\.


--
-- Data for Name: applicant_answer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.applicant_answer (applicant_answer_id, person_id, applicant_question_id, string_answer, date_answer, numeric_answer, applicant_question_choice_id) FROM stdin;
\.


--
-- Data for Name: applicant_app_status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.applicant_app_status (applicant_app_status_id, status_order, status_name, last_active_date, is_active, company_id, phase) FROM stdin;
\.


--
-- Data for Name: applicant_application; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.applicant_application (applicant_application_id, person_id, applicant_position_id, application_date, applicant_app_status_id, offer_first_generated, offer_last_generated, offer_first_emailed, offer_last_emailed, offer_elec_signed_date, offer_elec_signed_ip, pay_rate, position_id, phase, offer_declined_date, offer_retracted_date, offer_last_viewed_date) FROM stdin;
\.


--
-- Data for Name: applicant_contact; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.applicant_contact (applicant_contact_id, person_id, applicant_application_id, contact_date, contact_time, contact_mode, contact_status, description, who_added) FROM stdin;
\.


--
-- Data for Name: applicant_position; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.applicant_position (applicant_position_id, job_title, accept_applicant_date, job_start_date, org_group_id, position_status, ext_ref, position_id) FROM stdin;
\.


--
-- Data for Name: applicant_question; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.applicant_question (applicant_question_id, question_order, question, last_active_date, data_type, internal_use, company_id, position_id) FROM stdin;
\.


--
-- Data for Name: applicant_question_choice; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.applicant_question_choice (applicant_question_choice_id, applicant_question_id, description) FROM stdin;
\.


--
-- Data for Name: applicant_source; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.applicant_source (applicant_source_id, description, last_active_date, company_id) FROM stdin;
\.


--
-- Data for Name: applicant_status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.applicant_status (applicant_status_id, status_order, name, last_active_date, consider_for_hire, send_email, email_source, email_text, email_subject, company_id) FROM stdin;
\.


--
-- Data for Name: appointment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.appointment (appointment_id, org_group_id, meeting_date, meeting_time, meeting_time_type, attendees, meeting_location, status, purpose, location_id, meeting_length) FROM stdin;
\.


--
-- Data for Name: appointment_location; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.appointment_location (location_id, code, description, last_active_date, company_id) FROM stdin;
\.


--
-- Data for Name: appointment_person_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.appointment_person_join (join_id, appointment_id, person_id, primary_person) FROM stdin;
\.


--
-- Data for Name: assembly_template; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.assembly_template (assembly_template_id, assembly_name, description) FROM stdin;
\.


--
-- Data for Name: assembly_template_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.assembly_template_detail (assembly_template_detail_id, assembly_template_id, parent_detail_id, product_id, quantity, item_particulars, track_to_item) FROM stdin;
\.


--
-- Data for Name: authenticated_senders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.authenticated_senders (auth_send_id, address_type, address) FROM stdin;
\.


--
-- Data for Name: bank_account; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bank_account (bank_account_id, org_group_id, bank_id, bank_name, bank_route, bank_account, account_type, last_active_date) FROM stdin;
\.


--
-- Data for Name: bank_draft_batch; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bank_draft_batch (bank_draft_id, name, company_id) FROM stdin;
\.


--
-- Data for Name: bank_draft_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bank_draft_detail (bank_draft_id, person_id) FROM stdin;
\.


--
-- Data for Name: bank_draft_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bank_draft_history (bank_draft_history_id, bank_draft_id, date_made, receipt) FROM stdin;
\.


--
-- Data for Name: benefit_answer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_answer (benefit_answer_id, record_change_date, record_change_type, record_person_id, benefit_question_id, person_id, string_answer, date_answer, numeric_answer) FROM stdin;
\.


--
-- Data for Name: benefit_answer_h; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_answer_h (history_id, benefit_answer_id, record_change_date, record_change_type, record_person_id, benefit_question_id, person_id, string_answer, date_answer, numeric_answer) FROM stdin;
\.


--
-- Data for Name: benefit_change_reason_doc; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_change_reason_doc (bcr_document_id, bcr_id, company_form_id, instructions) FROM stdin;
\.


--
-- Data for Name: benefit_class_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_class_join (benefit_class_id, benefit_config_id) FROM stdin;
\.


--
-- Data for Name: benefit_config_cost; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_config_cost (benefit_config_cost_id, benefit_config_id, applies_to_status, org_group_id, first_active_date, last_active_date, age_calc_type, fixed_employee_cost, fixed_employer_cost, benefit_amount, min_value, max_value, step_value, max_multiple_of_salary, rate_per_unit, rate_frequency, rate_relates_to, salary_round_type, salary_round_amount, benefit_round_type, benefit_round_amount, cap_type, guaranteed_issue_type, guaranteed_issue_amount) FROM stdin;
\.


--
-- Data for Name: benefit_config_cost_age; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_config_cost_age (benefit_config_cost_age_id, benefit_config_cost_id, max_age, ee_value, er_value, insurance_id) FROM stdin;
\.


--
-- Data for Name: benefit_config_cost_status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_config_cost_status (benefit_config_cost_status_id, benefit_config_cost_id, employee_status_id) FROM stdin;
\.


--
-- Data for Name: benefit_dependency; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_dependency (benefit_dependency_id, required_benefit_id, benefit_id, required, hidden) FROM stdin;
\.


--
-- Data for Name: benefit_document; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_document (benefit_document_id, benefit_id, company_form_id, instructions) FROM stdin;
\.


--
-- Data for Name: benefit_group_class_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_group_class_join (benefit_class_id, benefit_id) FROM stdin;
\.


--
-- Data for Name: benefit_question; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_question (benefit_question_id, record_change_date, record_change_type, record_person_id, benefit_id, question_order, question, last_active_date, data_type, include_explanation, applies_to_employee, applies_to_spouse, applies_to_child_other, explanation_text, internal_id) FROM stdin;
\.


--
-- Data for Name: benefit_question_choice; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_question_choice (benefit_question_choice_id, benefit_question_id, description) FROM stdin;
\.


--
-- Data for Name: benefit_question_h; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_question_h (history_id, benefit_question_id, record_change_date, record_change_type, record_person_id, benefit_id, question_order, question, last_active_date, data_type, include_explanation, applies_to_employee, applies_to_spouse, applies_to_child_other, explanation_text, internal_id) FROM stdin;
\.


--
-- Data for Name: benefit_restriction; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefit_restriction (benefit_restriction_id, bcr_id, benefit_cat_id) FROM stdin;
\.


--
-- Data for Name: change_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.change_log (change_id, change_when, change_person_id, person_id, project_id, client_id, vendor_id, table_changed, column_changed, old_value, new_value, description) FROM stdin;
\.


--
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.client (org_group_id, inactive_date, billing_rate, contract_date, dflt_sales_acct, dflt_ar_acct, company_id, client_status_id, status_comments, last_contact_date, payment_terms, vendor_number, default_project_code, copy_pictures_to_disk, picture_disk_path, copy_only_external, copy_inactive_projects) FROM stdin;
\.


--
-- Data for Name: client_contact; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.client_contact (person_id, email_invoice, contact_type) FROM stdin;
\.


--
-- Data for Name: client_item_inventory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.client_item_inventory (client_item_inventory_id, quantity_on_hand, usable_quantity_on_hand, location, project_id, item_category, description, model, vendor, vendor_item_code, client_item_code, unit_of_measure, quantity_discarded) FROM stdin;
\.


--
-- Data for Name: client_status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.client_status (client_status_id, code, seqno, description, last_active_date, active, company_id) FROM stdin;
\.


--
-- Data for Name: company_base; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.company_base (org_group_id, org_group_type, federal_employer_id, com_url, com_login, com_password, com_directory, encryption_key_id, application_sender_id, application_receiver_id, interchange_sender_id, interchange_receiver_id, public_encryption_key, arahant_url, bill_to_name, days_to_send, time_to_send, edi_activated, edi_file_type, edi_file_status) FROM stdin;
00000-0000000000	1	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	NNNNNNN	0	N	U	U
\.


--
-- Data for Name: company_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.company_detail (org_group_id, accounting_basis, billing_rate, cash_account_id, ar_account_id, employee_advance_account_id, eeo1_id, dun_bradstreet_num, naics, time_off_auto_accrual, max_employees, logo, logo_source, logo_extension, fiscal_beginning_month, email_out_type, email_out_authentication, email_out_host, email_out_domain, email_out_port, email_out_encryption, email_out_user_id, email_out_user_pw, email_out_from_name, email_out_from_email) FROM stdin;
00000-0000000000	C	0	\N	\N	\N	\N	\N	\N	N	0	\N	\N	\N	1	0	N	\N	\N	25	N	\N	\N	\N	\N
\.


--
-- Data for Name: company_form; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.company_form (company_form_id, company_id, form_type_id, comments, source, form, file_name_extension, first_active_date, last_active_date, electronic_signature) FROM stdin;
\.


--
-- Data for Name: company_form_folder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.company_form_folder (folder_id, folder_name, company_id, parent_folder_id) FROM stdin;
\.


--
-- Data for Name: company_form_folder_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.company_form_folder_join (form_id, folder_id) FROM stdin;
\.


--
-- Data for Name: company_form_org_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.company_form_org_join (folder_id, org_group_id) FROM stdin;
\.


--
-- Data for Name: company_question; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.company_question (company_ques_id, seqno, question, company_id, last_active_date) FROM stdin;
\.


--
-- Data for Name: company_question_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.company_question_detail (company_ques_det_id, org_group_id, company_ques_id, response, when_added) FROM stdin;
\.


--
-- Data for Name: contact_question; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.contact_question (contact_question_id, seqno, question, company_id, last_active_date) FROM stdin;
\.


--
-- Data for Name: contact_question_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.contact_question_detail (contact_question_det_id, person_id, contact_question_id, response, when_added) FROM stdin;
\.


--
-- Data for Name: delivery; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.delivery (delivery_id, who_accepted_delivery, location, project_po_id, delivery_date, delivery_time) FROM stdin;
\.


--
-- Data for Name: delivery_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.delivery_detail (delivery_detail_id, project_po_detail_id, delivery_id, units_received, condition, usable, location) FROM stdin;
\.


--
-- Data for Name: drc_form_event; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.drc_form_event (form_event_id, event_type, person_id_causing_event, event_date, person_id, employee_id, transaction_type) FROM stdin;
\.


--
-- Data for Name: drc_import_benefit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.drc_import_benefit (imported_benefit_id, company_id, benefit_name, import_file_type_id) FROM stdin;
\.


--
-- Data for Name: drc_import_benefit_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.drc_import_benefit_join (import_benefit_join_id, enrollee_id, import_benefit_id, subscriber_id, coverage_start_date, coverage_end_date, record_change_date, record_change_type, record_person_id) FROM stdin;
\.


--
-- Data for Name: drc_import_benefit_join_h; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.drc_import_benefit_join_h (history_id, import_benefit_join_id, enrollee_id, import_benefit_id, subscriber_id, coverage_start_date, coverage_end_date, record_change_date, record_change_type, record_person_id) FROM stdin;
\.


--
-- Data for Name: drc_import_enrollee; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.drc_import_enrollee (enrollee_id, company_id, lname, fname, mname, relationship, ssn, street1, street2, city, state, zip, dob, import_file_type_id, record_change_date, record_change_type, record_person_id) FROM stdin;
\.


--
-- Data for Name: drc_import_enrollee_h; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.drc_import_enrollee_h (history_id, enrollee_id, company_id, lname, fname, mname, relationship, ssn, street1, street2, city, state, zip, dob, import_file_type_id, record_change_date, record_change_type, record_person_id) FROM stdin;
\.


--
-- Data for Name: dynace_mutex; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dynace_mutex (tag, data, lastupdate) FROM stdin;
\.


--
-- Data for Name: dynace_sequence; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dynace_sequence (tag, lastvalue) FROM stdin;
\.


--
-- Data for Name: e_signature; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.e_signature (e_signature_id, person_id, time_signed, address_ip, form_type, xml_sum, xml_data, form_data, form_sum, signature, sig_date) FROM stdin;
\.


--
-- Data for Name: edi_transaction; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.edi_transaction (edi_transaction_id, org_group_id, tscn, gcn, icn, transaction_datetime, transaction_status, transaction_status_desc) FROM stdin;
\.


--
-- Data for Name: education; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.education (education_id, person_id, start_date, end_date, school_type, school_name, school_location, graduate, subject, other_type, gpa, current) FROM stdin;
\.


--
-- Data for Name: electronic_fund_transfer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.electronic_fund_transfer (eft_id, person_id, seqno, bank_route, bank_account, account_type, amount_type, amount, wage_type_id) FROM stdin;
\.


--
-- Data for Name: email_notifications; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.email_notifications (email_notification_id, notification_type, name, title, email) FROM stdin;
\.


--
-- Data for Name: employee; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.employee (person_id, timesheet_final_date, eeo_category_id, eeo_race_id, ext_ref, overtime_pay, pay_periods_per_year, expected_hours_per_period, marital_status, local_tax_code, earned_income_credit_status, add_federal_income_tax_type, add_federal_income_tax, add_state_income_tax_type, add_state_income_tax, add_local_income_tax_type, add_local_income_tax, add_state_disability_tax_type, add_state_disability_tax, number_federal_exemptions, number_state_exemptions, payroll_bank_code, federal_extra_withhold, state_extra_withhold, tax_state, unemployement_state, benefit_class_id, auto_time_log, auto_overtime_logout, length_of_work_day, length_of_breaks, workers_comp_code, auto_log_time, w4_status, w4_exempt, w4_name_differs, medicare, hr_admin, benefit_wizard_status, benefit_wizard_date, status_id, status_effective_date, hours_per_week, employment_type, adp_id) FROM stdin;
\.


--
-- Data for Name: employee_label; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.employee_label (employee_label_id, name, description, auto_add_new_employee) FROM stdin;
\.


--
-- Data for Name: employee_label_association; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.employee_label_association (employee_id, employee_label_id, who_added, when_added, completed, when_completed, who_completed, notes) FROM stdin;
\.


--
-- Data for Name: employee_not_available; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.employee_not_available (employee_not_available_id, employee_id, start_date, end_date, reason) FROM stdin;
\.


--
-- Data for Name: employee_rate; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.employee_rate (employee_rate_id, person_id, rate_type_id, rate) FROM stdin;
\.


--
-- Data for Name: expense; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.expense (expense_id, employee_id, project_id, date_paid, per_diem_amount, expense_amount, advance_amount, payment_method, comments, auth_date, per_diem_return, auth_person_id, week_paid_for, scheduling_comments, hotel_amount) FROM stdin;
\.


--
-- Data for Name: expense_account; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.expense_account (expense_account_id, expense_id, description, gl_account_id) FROM stdin;
\.


--
-- Data for Name: expense_receipt; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.expense_receipt (expense_receipt_id, person_id, project_id, expense_account_id, receipt_date, when_uploaded, business_purpose, amount, who_uploaded, file_type, payment_method, approved, who_approved, when_approved) FROM stdin;
\.


--
-- Data for Name: form_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.form_type (form_type_id, form_code, description, form_type, org_group_id, last_active_date, field_downloadable, internal) FROM stdin;
\.


--
-- Data for Name: garnishment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.garnishment (garnishment_id, person_id, priority, issue_state, fips_code, docket_number, remit_to_name, remit_to, collecting_state, max_percent, max_dollars, start_date, end_date, deduction_amount, deduction_percentage, net_or_gross, garnishment_type_id) FROM stdin;
\.


--
-- Data for Name: garnishment_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.garnishment_type (garnishment_type_id, description, last_active_date, wage_type_id) FROM stdin;
\.


--
-- Data for Name: gl_account; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.gl_account (gl_account_id, account_number, account_name, account_type, default_flag, company_id) FROM stdin;
\.


--
-- Data for Name: holiday; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.holiday (holiday_id, hdate, description, org_group_id, part_of_day) FROM stdin;
\.


--
-- Data for Name: hr_accrual; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_accrual (accrual_id, benefit_account, employee_id, accrual_date, accrual_hours, description) FROM stdin;
\.


--
-- Data for Name: hr_benefit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit (benefit_id, benefit_cat_id, benefit_name, rule_name, time_related, paid_benefit, pre_tax, provider_id, group_id, sub_group_id, add_info, has_beneficiaries, plan_id, plan_name, start_date, end_date, requires_decline, instructions, cobra, insurance_code, payer_id, product_id, wage_type_id, description, eligibility_type, eligibility_period, dependent_max_age, dependent_max_age_student, coverage_end_type, coverage_end_period, process_type, seqno, open_enrollment_wizard, onboarding_wizard, avatar_path, open_enrollment_screen_id, onboarding_screen_id, avatar_location, last_enrollment_date, benefit_id_replaced_by, contingent_beneficiaries, has_physicians, edi_field_descrption_1, edi_field_value_1, edi_field_descrption_2, edi_field_value_2, edi_field_descrption_3, edi_field_value_3, edi_field_descrption_4, edi_field_value_4, edi_field_descrption_5, edi_field_value_5, group_account_id, auto_assign, min_age, max_age, internal_id, lisp_reference, show_all_coverages, cost_calc_type, address_required, employer_cost_model, employee_cost_model, benefit_amount_model, min_pay, max_pay, min_hours_per_week) FROM stdin;
\.


--
-- Data for Name: hr_benefit_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit_category (benefit_cat_id, org_group_id, description, benefit_type, mutually_exclusive, requires_decline, seqno, open_enrollment_wizard, onboarding_wizard, avatar_path, instructions, open_enrollment_screen_id, onboarding_screen_id, avatar_location) FROM stdin;
\.


--
-- Data for Name: hr_benefit_category_answer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit_category_answer (answer_id, question_id, benefit_id, answer) FROM stdin;
\.


--
-- Data for Name: hr_benefit_category_question; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit_category_question (question_id, benefit_cat_id, seqno, question) FROM stdin;
\.


--
-- Data for Name: hr_benefit_change_reason; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit_change_reason (bcr_id, bcr_type, description, start_date, end_date, effective_date, event_type, company_id, instructions) FROM stdin;
\.


--
-- Data for Name: hr_benefit_class; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit_class (benefit_class_id, class_name, class_description, org_group_id, first_active_date, last_active_date) FROM stdin;
\.


--
-- Data for Name: hr_benefit_config; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit_config (benefit_config_id, benefit_id, config_name, employee, children, max_children, employee_cobra_cost, add_info, auto_assign, spouse_declines_outside, start_date, end_date, spouse_employee, spouse_non_employee, spouse_emp_or_children, spouse_non_emp_or_children, on_billing, insurance_code, seqno) FROM stdin;
\.


--
-- Data for Name: hr_benefit_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit_join (benefit_join_id, record_change_date, record_change_type, record_person_id, covered_person, relationship_id, paying_person, benefit_declined, benefit_config_id, benefit_cat_id, benefit_id, insurance_id, policy_start_date, policy_end_date, coverage_start_date, coverage_end_date, amount_paid_source, amount_paid_type, amount_paid, amount_covered, change_description, benefit_approved, cobra, comments, cobra_acceptance_date, max_months_on_cobra, other_insurance, other_insurance_is_primary, project_id, life_event_id, bcr_id, employee_covered, employee_explanation, requested_cost, requested_cost_period, coverage_change_date, original_coverage_date) FROM stdin;
\.


--
-- Data for Name: hr_benefit_join_h; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit_join_h (history_id, benefit_join_id, record_change_date, record_change_type, record_person_id, covered_person, relationship_id, paying_person, benefit_declined, benefit_config_id, benefit_cat_id, benefit_id, insurance_id, policy_start_date, policy_end_date, coverage_start_date, coverage_end_date, amount_paid_source, amount_paid_type, amount_paid, amount_covered, change_description, benefit_approved, cobra, comments, cobra_acceptance_date, max_months_on_cobra, other_insurance, other_insurance_is_primary, project_id, life_event_id, bcr_id, employee_covered, employee_explanation, requested_cost, requested_cost_period, coverage_change_date, original_coverage_date) FROM stdin;
\.


--
-- Data for Name: hr_benefit_package; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit_package (package_id, name) FROM stdin;
\.


--
-- Data for Name: hr_benefit_package_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit_package_join (package_id, benefit_id) FROM stdin;
\.


--
-- Data for Name: hr_benefit_project_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit_project_join (project_id, benefit_config_id) FROM stdin;
\.


--
-- Data for Name: hr_benefit_rider; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_benefit_rider (benefit_rider_id, base_benefit_id, rider_benefit_id, hidden, required) FROM stdin;
\.


--
-- Data for Name: hr_billing_status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_billing_status (billing_status_id, name) FROM stdin;
\.


--
-- Data for Name: hr_billing_status_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_billing_status_history (billing_status_hist_id, person_id, billing_status_id, start_date, final_date) FROM stdin;
\.


--
-- Data for Name: hr_checklist_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_checklist_detail (checklist_detail_id, employee_id, checklist_item_id, date_completed, supervisor_id, time_completed) FROM stdin;
\.


--
-- Data for Name: hr_checklist_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_checklist_item (item_id, org_group_id, name, employee_status_id, seq, first_active_date, last_active_date, responsibility, screen_id, screen_group_id, company_form_id) FROM stdin;
\.


--
-- Data for Name: hr_eeo1; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_eeo1 (eeo1_id, org_group_id, beg_period, end_period, common_ownership, gov_contractor, date_created, date_uploaded, transmitted_data) FROM stdin;
\.


--
-- Data for Name: hr_eeo_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_eeo_category (eeo_category_id, name) FROM stdin;
\.


--
-- Data for Name: hr_eeo_race; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_eeo_race (eeo_id, name) FROM stdin;
\.


--
-- Data for Name: hr_emergency_contact; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_emergency_contact (contact_id, person_id, seqno, contact_name, home_phone, relationship, address, work_phone, cell_phone) FROM stdin;
\.


--
-- Data for Name: hr_empl_dependent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_empl_dependent (relationship_id, dependent_id, employee_id, relationship, relationship_type, date_added, date_inactive, record_type) FROM stdin;
\.


--
-- Data for Name: hr_empl_dependent_cr; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_empl_dependent_cr (change_request_id, real_record_id, change_status, request_time, change_record_id, requestor_id, approver_id, approval_time, project_id) FROM stdin;
\.


--
-- Data for Name: hr_empl_eval_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_empl_eval_detail (detail_id, eval_id, cat_id, score, notes, e_score, e_notes, p_notes) FROM stdin;
\.


--
-- Data for Name: hr_empl_status_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_empl_status_history (status_hist_id, employee_id, effective_date, status_id, notes, record_change_date) FROM stdin;
\.


--
-- Data for Name: hr_employee_beneficiary; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_employee_beneficiary (beneficiary_id, record_change_date, record_change_type, record_person_id, benefit_join_id, beneficiary, relationship, benefit_percent, dob, ssn, address, beneficiary_type) FROM stdin;
\.


--
-- Data for Name: hr_employee_beneficiary_h; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_employee_beneficiary_h (history_id, beneficiary_id, record_change_date, record_change_type, record_person_id, benefit_join_id, beneficiary, relationship, benefit_percent, dob, ssn, address, beneficiary_type) FROM stdin;
\.


--
-- Data for Name: hr_employee_eval; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_employee_eval (employee_eval_id, employee_id, eval_date, supervisor_id, next_eval_date, description, comments, state, e_comments, p_comments, final_date) FROM stdin;
\.


--
-- Data for Name: hr_employee_event; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_employee_event (event_id, employee_id, supervisor_id, event_date, employee_notified, summary, detail, date_notified, event_type) FROM stdin;
\.


--
-- Data for Name: hr_employee_status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_employee_status (status_id, org_group_id, last_active_date, name, active, date_type, benefit_type) FROM stdin;
\.


--
-- Data for Name: hr_eval_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_eval_category (eval_cat_id, name, description, weight, org_group_id, first_active_date, last_active_date) FROM stdin;
\.


--
-- Data for Name: hr_note_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_note_category (cat_id, org_group_id, first_active_date, last_active_date, name, cat_code) FROM stdin;
\.


--
-- Data for Name: hr_position; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_position (position_id, org_group_id, first_active_date, last_active_date, position_name, benefit_class_id, weekly_per_diem, seqno, applicant_default) FROM stdin;
\.


--
-- Data for Name: hr_training_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_training_category (cat_id, org_group_id, last_active_date, name, training_type, client_id, required, hours) FROM stdin;
\.


--
-- Data for Name: hr_training_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_training_detail (training_id, employee_id, training_date, cat_id, expire_date, training_hours) FROM stdin;
\.


--
-- Data for Name: hr_wage; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_wage (wage_id, employee_id, wage_type_id, wage_amount, effective_date, position_id, notes) FROM stdin;
\.


--
-- Data for Name: import_column; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.import_column (import_column_id, import_filter_id, column_order, column_name, start_pos, last_pos, date_format) FROM stdin;
\.


--
-- Data for Name: import_filter; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.import_filter (import_filter_id, import_type_id, import_filter_name, import_filter_desc, filter_value) FROM stdin;
\.


--
-- Data for Name: import_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.import_type (import_type_id, company_id, import_program_name, import_name, import_source, file_format, delim_char, quote_char, filter_start_pos, filter_end_pos) FROM stdin;
\.


--
-- Data for Name: insurance_location_code; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.insurance_location_code (insurance_location_code_id, benefit_class_id, employee_status_id, benefit_id, ins_location_code) FROM stdin;
\.


--
-- Data for Name: interface_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.interface_log (interface_log_id, interface_code, last_run, status_message, status_code, company_id) FROM stdin;
\.


--
-- Data for Name: inventory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.inventory (inventory_id, product_id, location_id, reorder_level) FROM stdin;
\.


--
-- Data for Name: invoice; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.invoice (invoice_id, description, create_date, export_date, ar_account_id, customer_id, accounting_invoice_identifier, invoice_type, person_id, payed_off, purchase_order, payment_terms) FROM stdin;
\.


--
-- Data for Name: invoice_line_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.invoice_line_item (invoice_line_item_id, adj_hours, adj_rate, invoice_id, product_id, expense_account_id, description, billing_type, amount, benefit_join_id, project_id) FROM stdin;
\.


--
-- Data for Name: item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.item (item_id, product_id, location_id, parent_item_id, lot_id, serial_number, quantity, item_particulars, record_change_date, record_change_type, record_person_id, item_name, manufacturer, model, date_purchased, original_cost, purchased_from, notes, item_status, retirement_notes, retirement_date, reimbursement_status, reimbursement_person_id, requested_reimbursement_amount, reimbursement_amount_received, date_reimbursement_received, person_accepting_reimbursement) FROM stdin;
\.


--
-- Data for Name: item_h; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.item_h (history_id, item_id, product_id, location_id, parent_item_id, lot_id, serial_number, quantity, item_particulars, record_change_date, record_change_type, record_person_id, item_name, manufacturer, model, date_purchased, original_cost, purchased_from, notes, item_status, retirement_notes, retirement_date, reimbursement_status, reimbursement_person_id, requested_reimbursement_amount, reimbursement_amount_received, date_reimbursement_received, person_accepting_reimbursement) FROM stdin;
\.


--
-- Data for Name: item_inspection; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.item_inspection (item_inspection_id, item_id, inspection_date, person_inspecting, record_change_date, record_person_id, inspection_status, inspection_comments) FROM stdin;
\.


--
-- Data for Name: life_event; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.life_event (life_event_id, person_id, event_date, bcr_id, description, date_reported, reporting_person_id) FROM stdin;
\.


--
-- Data for Name: location_cost; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.location_cost (location_cost_id, description, location_cost) FROM stdin;
\.


--
-- Data for Name: login_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.login_log (ltime, log_name, successful, person_id, address_ip, address_url) FROM stdin;
\.


--
-- Data for Name: lot; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lot (lot_id, lot_number, original_quantity, date_received, lot_cost, lot_particulars) FROM stdin;
\.


--
-- Data for Name: message; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.message (message_id, message, from_person_id, created_date, subject, from_show, from_address, from_name, dont_send_body, send_email, send_text, send_internal) FROM stdin;
\.


--
-- Data for Name: message_attachment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.message_attachment (message_attachment_id, message_id, source_file_name) FROM stdin;
\.


--
-- Data for Name: message_to; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.message_to (message_to_id, message_id, to_person_id, send_type, to_show, sent, when_viewed, to_address, to_name, date_received) FROM stdin;
\.


--
-- Data for Name: onboarding_config; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.onboarding_config (onboarding_config_id, company_id, config_name) FROM stdin;
\.


--
-- Data for Name: onboarding_task; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.onboarding_task (onboarding_task_id, onboarding_config_id, seqno, screen_id, task_name, description, completed_by) FROM stdin;
\.


--
-- Data for Name: onboarding_task_complete; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.onboarding_task_complete (task_complete_id, person_id, onboarding_task_id, completion_date) FROM stdin;
\.


--
-- Data for Name: org_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.org_group (org_group_id, group_name, org_group_type, owning_entity_id, pay_periods_per_year, external_id, default_project_id, pay_schedule_id, eeo1_unit, eeo1_establishment, eeo1_headquaters, eeo1_filed_last_year, eval_email_notify, eval_email_first_days, eval_email_notify_days, eval_email_send_days, new_week_begin_day, benefit_class_id, org_group_guid, timesheet_period_type, timesheet_period_start_date, timesheet_show_billable) FROM stdin;
00000-0000000000	Your Company Name	1	00000-0000000000	0	\N	\N	\N	\N	N	N	N	I	0	0	NNNNN	0	\N	\N	I	0	I
\.


--
-- Data for Name: org_group_association; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.org_group_association (person_id, org_group_id, record_change_date, record_change_type, record_person_id, primary_indicator, org_group_type, start_date, final_date) FROM stdin;
\.


--
-- Data for Name: org_group_association_h; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.org_group_association_h (history_id, person_id, org_group_id, record_change_date, record_change_type, record_person_id, primary_indicator, org_group_type, start_date, final_date) FROM stdin;
\.


--
-- Data for Name: org_group_hierarchy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.org_group_hierarchy (parent_group_id, child_group_id, org_group_type) FROM stdin;
\.


--
-- Data for Name: overtime_approval; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.overtime_approval (overtime_approval_id, employee_id, supervisor_id, overtime_date, overtime_hours, record_change_date) FROM stdin;
\.


--
-- Data for Name: password_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.password_history (password_history_id, person_id, date_retired, user_password) FROM stdin;
\.


--
-- Data for Name: pay_schedule; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pay_schedule (pay_schedule_id, schedule_name, description, company_id) FROM stdin;
\.


--
-- Data for Name: pay_schedule_period; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pay_schedule_period (pay_period_id, pay_schedule_id, last_date, pay_date, beginning_of_year) FROM stdin;
\.


--
-- Data for Name: payment_info; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.payment_info (payment_id, person_id, date_authorized, address_ip, payment_type, account_name, account_number, bank_draft_bank_name, bank_draft_bank_route, cc_expire, cc_cvc_code, billing_street, billing_city, billing_state, billing_zip, benefit_id) FROM stdin;
\.


--
-- Data for Name: per_diem_exception; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.per_diem_exception (per_diem_exception_id, exception_type, exception_amount, person_id, project_id, notes) FROM stdin;
\.


--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person (person_id, record_change_date, record_change_type, record_person_id, lname, fname, mname, personal_email, job_title, org_group_type, company_id, ssn, sex, dob, handicap, student, citizenship, visa, visa_status_date, visa_exp_date, sort_order, nickname, drivers_license_state, drivers_license_number, drivers_license_exp, smoker, auto_insurance_carrier, auto_insurance_policy, auto_insurance_exp, auto_insurance_start, auto_insurance_coverage, default_project_id, student_calendar_type, height, weight, record_type, smoking_program, military_branch, military_start_date, military_end_date, military_rank, military_discharge_type, military_discharge_explain, convicted_of_crime, convicted_of_what, worked_for_company_before, worked_for_company_when, actively_at_work, unable_to_perform, has_aids, has_cancer, has_heart_condition, hic_number, agreement_date, agreement_address_ip, agreement_address_url, agreement_revision, person_guid, replace_employer_plan, not_missed_five_days, drug_alcohol_abuse, two_family_heart_cond, two_family_cancer, two_family_diabetes, has_other_medical, default_email_sender, i9_part1, i9_part2, i9p1_confirmation, i9p2_confirmation, i9p1_person, i9p2_person, i9p1_when, i9p2_when, linkedin) FROM stdin;
00000-0000000000	2024-03-31 09:36:51.118-05	N	00000-0000000000	Administrator	Stack360	\N	\N	System Administrator	1	00000-0000000000	\N	U	0	N	N	\N	\N	0	0	0	\N	\N	\N	0	U	\N	\N	0	0	\N	\N	 	0	0	R	U	U	0	0	\N	U	\N	U	\N	N	\N	 	 	 	 	 	\N	\N	\N	\N	0	\N	 	 	 	 	 	 	 	N	N	N	\N	\N	\N	\N	\N	\N	\N
\.


--
-- Data for Name: person_change_request; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person_change_request (request_id, person_id, request_type, request_date, request_data) FROM stdin;
\.


--
-- Data for Name: person_changed; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person_changed (interface_id, person_id, earliest_change_date) FROM stdin;
\.


--
-- Data for Name: person_cr; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person_cr (change_request_id, real_record_id, change_status, request_time, change_record_id, requestor_id, approver_id, approval_time, project_id) FROM stdin;
\.


--
-- Data for Name: person_email; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person_email (person_email_id, sent_to, subject, message, person_id, date_sent) FROM stdin;
\.


--
-- Data for Name: person_form; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person_form (person_form_id, form_type_id, form_date, person_id, comments, source, ext_ref, file_name_extension, electronically_signed) FROM stdin;
\.


--
-- Data for Name: person_h; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person_h (history_id, person_id, record_change_date, record_change_type, record_person_id, lname, fname, mname, personal_email, job_title, org_group_type, company_id, ssn, sex, dob, handicap, student, citizenship, visa, visa_status_date, visa_exp_date, sort_order, nickname, drivers_license_state, drivers_license_number, drivers_license_exp, smoker, auto_insurance_carrier, auto_insurance_policy, auto_insurance_exp, auto_insurance_start, auto_insurance_coverage, default_project_id, student_calendar_type, height, weight, record_type, smoking_program, military_branch, military_start_date, military_end_date, military_rank, military_discharge_type, military_discharge_explain, convicted_of_crime, convicted_of_what, worked_for_company_before, worked_for_company_when, actively_at_work, unable_to_perform, has_aids, has_cancer, has_heart_condition, hic_number, agreement_date, agreement_address_ip, agreement_address_url, agreement_revision, person_guid, replace_employer_plan, not_missed_five_days, drug_alcohol_abuse, two_family_heart_cond, two_family_cancer, two_family_diabetes, has_other_medical, i9_part1, i9_part2) FROM stdin;
\.


--
-- Data for Name: person_note; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person_note (note_id, person_id, cat_id, note) FROM stdin;
\.


--
-- Data for Name: personal_reference; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.personal_reference (reference_id, person_id, reference_name, relationship_type, relationship_other, company, phone, address, years_known) FROM stdin;
\.


--
-- Data for Name: phone; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.phone (phone_id, phone_number, phone_type, person_join, org_group_join, record_type) FROM stdin;
\.


--
-- Data for Name: phone_cr; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.phone_cr (change_request_id, real_record_id, change_status, request_time, change_record_id, requestor_id, approver_id, approval_time, project_id) FROM stdin;
\.


--
-- Data for Name: physician; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.physician (physician_id, benefit_join_id, physician_name, physician_code, address, change_reason, change_date, annual_visit) FROM stdin;
\.


--
-- Data for Name: previous_employment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.previous_employment (employment_id, person_id, start_date, end_date, company, phone, supervisor, job_title, starting_salary, ending_salary, responsibilities, reason_for_leaving, contact_supervisor, street, city, state) FROM stdin;
\.


--
-- Data for Name: process_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.process_history (process_history_id, process_schedule_id, run_time, success) FROM stdin;
\.


--
-- Data for Name: process_schedule; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.process_schedule (process_schedule_id, java_class, start_date, start_time, perform_missing_runs, run_minutes, run_hours, run_days_of_month, run_months, run_days_of_week) FROM stdin;
\.


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (product_id, product_type_id, vendor_id, sku, product_cost, wholesale_price, retail_price, availability_date, term_date, man_hours, sell_as_type) FROM stdin;
\.


--
-- Data for Name: product_attribute; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product_attribute (product_attribute_id, company_id, attribute_order, attribute, data_type, last_active_date) FROM stdin;
\.


--
-- Data for Name: product_attribute_choice; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product_attribute_choice (product_attribute_choice_id, product_attribute_id, description, last_active_date, choice_order) FROM stdin;
\.


--
-- Data for Name: product_attribute_dependency; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product_attribute_dependency (attribute_dependency_id, product_attribute_id, dependent_attribute_id) FROM stdin;
\.


--
-- Data for Name: product_service; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product_service (product_service_id, accsys_id, expense_account_id, description, service_type, company_id, detailed_description) FROM stdin;
\.


--
-- Data for Name: product_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product_type (product_type_id, parent_product_type_id, description, company_id) FROM stdin;
\.


--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project (project_id, reference, description, detail_desc, project_sponsor_id, managing_employee, project_status_id, project_type_id, project_category_id, dollar_cap, billing_rate, billable, project_name, requesting_org_group, requester_name, product_id, date_reported, billing_method, next_billing_date, bill_at_org_group, all_employees, route_id, current_route_stop, subject_person, time_reported, completed_date, completed_time, approving_person, approving_timestamp, approving_person_txt, priority_company, priority_department, priority_client, estimate_hours, date_promised, estimated_time_span, date_of_estimate, ongoing, status_comments, company_id, purchase_order, rate_type_id, project_code, project_state, estimated_first_date, estimated_last_date, address_id, store_number, project_subtype_id, location_description, billing_type, fixed_price_amount, last_report_date, project_days) FROM stdin;
\.


--
-- Data for Name: project_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_category (project_category_id, code, description, scope, company_id, last_active_date) FROM stdin;
\.


--
-- Data for Name: project_checklist_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_checklist_detail (project_checklist_detail_id, project_id, route_stop_checklist_id, person_id, completed_by, date_completed, entry_timestamp, entry_comments) FROM stdin;
\.


--
-- Data for Name: project_comment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_comment (date_entered, person_id, comment_txt, internal, comment_id, project_shift_id) FROM stdin;
\.


--
-- Data for Name: project_employee_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_employee_history (project_employee_history_id, person_id, change_person_id, change_date, change_time, change_type, project_shift_id) FROM stdin;
\.


--
-- Data for Name: project_employee_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_employee_join (project_employee_join_id, person_id, person_priority, date_assigned, time_assigned, start_date, confirmed_date, confirmed_person_id, verified_date, verified_person_id, manager, hours, project_shift_id) FROM stdin;
\.


--
-- Data for Name: project_form; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_form (project_form_id, form_type_id, form_date, comments, source, file_name_extension, internal, project_shift_id) FROM stdin;
\.


--
-- Data for Name: project_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_history (project_history_id, project_id, person_id, date_changed, time_changed, from_status_id, to_status_id, from_stop_id, to_stop_id) FROM stdin;
\.


--
-- Data for Name: project_inventory_email; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_inventory_email (pie_id, project_id, person_id) FROM stdin;
\.


--
-- Data for Name: project_phase; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_phase (project_phase_id, code, description, security_level, company_id, last_active_date) FROM stdin;
\.


--
-- Data for Name: project_po_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_po_detail (project_po_detail_id, project_po_id, po_line_number, quantity_ordered, price_each, client_item_inventory_id, when_due_date, when_due_time) FROM stdin;
\.


--
-- Data for Name: project_purchase_order; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_purchase_order (project_po_id, project_id, po_reference, po_number, vendor_name) FROM stdin;
\.


--
-- Data for Name: project_report_person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_report_person (person_id, client_id) FROM stdin;
\.


--
-- Data for Name: project_shift; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_shift (project_shift_id, project_id, required_workers, shift_start, description, default_date) FROM stdin;
\.


--
-- Data for Name: project_status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_status (project_status_id, code, description, active, company_id, last_active_date) FROM stdin;
\.


--
-- Data for Name: project_status_rs_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_status_rs_join (route_stop_id, project_status_id) FROM stdin;
\.


--
-- Data for Name: project_subtype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_subtype (project_subtype_id, code, description, scope, company_id, last_active_date) FROM stdin;
\.


--
-- Data for Name: project_task_assignment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_task_assignment (project_task_assignment_id, project_task_detail_id, person_id, team_lead) FROM stdin;
\.


--
-- Data for Name: project_task_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_task_detail (project_task_detail_id, seqno, title, description, comments, missing_items, status, project_shift_id, task_date, completion_date, recurring, recurring_schedule_id) FROM stdin;
\.


--
-- Data for Name: project_task_picture; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_task_picture (project_task_picture_id, project_task_detail_id, when_uploaded, file_name_extension, comments, who_uploaded, picture_number) FROM stdin;
\.


--
-- Data for Name: project_template_benefit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_template_benefit (project_template_id, benefit_id, bcr_id, status_id, org_group_id, project_status_id, project_category_id, project_type_id, project_description) FROM stdin;
\.


--
-- Data for Name: project_template_benefit_a; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_template_benefit_a (template_assignment_id, project_template_id, person_id) FROM stdin;
\.


--
-- Data for Name: project_training; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_training (project_training_id, project_id, cat_id, required) FROM stdin;
\.


--
-- Data for Name: project_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_type (project_type_id, code, description, scope, company_id, last_active_date) FROM stdin;
\.


--
-- Data for Name: project_view; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_view (project_view_id, person_id, node_title, node_description, project_id) FROM stdin;
\.


--
-- Data for Name: project_view_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_view_join (project_view_join_id, parent_project_view_id, child_project_view_id, sequence_num, primary_billing) FROM stdin;
\.


--
-- Data for Name: property; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.property (prop_name, prop_value, prop_desc) FROM stdin;
\.


--
-- Data for Name: prophet_login; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prophet_login (person_id, can_login, user_login, user_password, screen_group_id, security_group_id, password_effective_date, no_company_screen_group_id, when_created, authentication_code, number_of_resends, number_of_authentications, reset_password_date, user_type) FROM stdin;
00000-0000000000	Y	stack360	stack360	00000-0000000000	\N	0	\N	2024-03-31 09:36:51.640459-05	\N	0	0	\N	R
\.


--
-- Data for Name: prophet_login_exception; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prophet_login_exception (login_exception_id, person_id, company_id, screen_group_id, security_group_id) FROM stdin;
\.


--
-- Data for Name: prospect; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect (org_group_id, first_contact_date, prospect_status_id, certainty, prospect_source_id, source_detail, when_added, person_id, status_comments, last_contact_date, record_change_date, record_change_type, record_person_id, status_change_date, next_contact_date, company_id, prospect_type_id, opportunity_value, number_of_employees, gross_income, website) FROM stdin;
\.


--
-- Data for Name: prospect_contact; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect_contact (person_id, contact_type) FROM stdin;
\.


--
-- Data for Name: prospect_h; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect_h (history_id, org_group_id, first_contact_date, prospect_status_id, certainty, prospect_source_id, source_detail, when_added, person_id, status_comments, last_contact_date, record_change_date, record_change_type, record_person_id, status_change_date, next_contact_date, company_id, prospect_type_id, opportunity_value) FROM stdin;
\.


--
-- Data for Name: prospect_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect_log (prospect_log_id, org_group_id, contact_date, contact_time, contact_txt, employees, prospect_employees, sales_activity_id, person_id, entry_date, sales_activity_result_id, task_completion_date) FROM stdin;
\.


--
-- Data for Name: prospect_source; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect_source (prospect_source_id, source_code, description, last_active_date, company_id) FROM stdin;
\.


--
-- Data for Name: prospect_status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect_status (prospect_status_id, code, seqno, description, active, company_id, last_active_date, show_in_sales_pipeline, sales_points, fallback_days, certainty) FROM stdin;
\.


--
-- Data for Name: prospect_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect_type (prospect_type_id, company_id, type_code, description, last_active_date) FROM stdin;
\.


--
-- Data for Name: quickbooks_change; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.quickbooks_change (record_id, arahant_table_name, arahant_record_id, qb_record_id, qb_record_revision, record_changed) FROM stdin;
\.


--
-- Data for Name: quickbooks_sync; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.quickbooks_sync (sync_id, org_group_id, interface_time, interface_code, direction) FROM stdin;
\.


--
-- Data for Name: quote_adjustment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.quote_adjustment (quote_adjustment_id, quote_id, adjustment_description, seqno, quantity, adjusted_cost) FROM stdin;
\.


--
-- Data for Name: quote_product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.quote_product (quote_product_id, quote_id, product_id, seqno, quantity, retail_price, adjusted_retail_price, sell_as_type) FROM stdin;
\.


--
-- Data for Name: quote_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.quote_table (quote_id, client_id, location_cost_id, quote_name, quote_description, created_date, created_by_person, finalized_date, finalized_by_person, accepted_date, accepted_person, accepted_by_client, markup_percent, additional_cost) FROM stdin;
\.


--
-- Data for Name: quote_template; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.quote_template (quote_template_id, template_name, template_description) FROM stdin;
\.


--
-- Data for Name: quote_template_product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.quote_template_product (quote_template_product_id, quote_template_id, product_id, seqno, default_quantity, sell_as_type) FROM stdin;
\.


--
-- Data for Name: rate_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rate_type (rate_type_id, rate_code, description, last_active_date, rate_type, company_id) FROM stdin;
\.


--
-- Data for Name: receipt; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.receipt (receipt_id, receipt_date, receipt_type, reference, amount, source, company_id, person_id, bank_draft_history_id) FROM stdin;
\.


--
-- Data for Name: receipt_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.receipt_join (receipt_join_id, invoice_id, receipt_id, amount) FROM stdin;
\.


--
-- Data for Name: recurring_schedule; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.recurring_schedule (recurring_schedule_id, type, month, day, day_of_week, n, ending_date) FROM stdin;
\.


--
-- Data for Name: reminder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reminder (reminder_id, person_id, about_person, about_project, reminder_date, summary, detail, status, who_added, date_added, time_added, who_inactivated, date_inactive, time_inactive) FROM stdin;
\.


--
-- Data for Name: removed_from_roster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.removed_from_roster (rfr_id, person_id, supervisor_id, when_removed, description, comments, shift_id) FROM stdin;
\.


--
-- Data for Name: report; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report (report_id, company_id, report_type, report_name, description, page_orientation, page_offset_left, page_offset_top, default_font_size, lines_in_column_title, default_space_between_columns) FROM stdin;
\.


--
-- Data for Name: report_column; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_column (report_column_id, report_id, seqno, column_id, leading_space, lines, use_all_lines, vertical_justification, title, title_justification, column_justification, format_code, numeric_digits, display_totals, break_level, sort_order, sort_direction) FROM stdin;
\.


--
-- Data for Name: report_graphic; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_graphic (report_graphic_id, report_id, description, x_pos, y_pos, graphic) FROM stdin;
\.


--
-- Data for Name: report_selection; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_selection (report_selection_id, report_id, seqno, selection_type, selection_column, description, selection_operator, selection_value, selection_column2, logic_operator, left_parens, right_parens, selection_value_list) FROM stdin;
\.


--
-- Data for Name: report_title; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_title (report_title_id, report_id, page_location, seqno, field_type, field_format, font_size, font_underline, font_bold, font_italic, end_line, justification, left_offset, verbiage) FROM stdin;
\.


--
-- Data for Name: rights_association; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rights_association (security_group_id, right_id, access_level) FROM stdin;
\.


--
-- Data for Name: route; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.route (route_id, name, description, project_status_id, route_stop_id, company_id, last_active_date) FROM stdin;
\.


--
-- Data for Name: route_path; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.route_path (route_path_id, from_status_id, to_status_id, from_stop_id, to_stop_id) FROM stdin;
\.


--
-- Data for Name: route_stop; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.route_stop (route_stop_id, route_id, org_group_id, description, project_phase_id, auto_assign_supervisor) FROM stdin;
\.


--
-- Data for Name: route_stop_checklist; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.route_stop_checklist (route_stop_checklist_id, route_stop_id, item_priority, item_description, item_detail, item_required, date_active, date_inactive) FROM stdin;
\.


--
-- Data for Name: route_type_assoc; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.route_type_assoc (route_assoc_id, route_id, project_category_id, project_type_id) FROM stdin;
\.


--
-- Data for Name: sales_activity; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sales_activity (sales_activity_id, company_id, seqno, activity_code, description, sales_points, last_active_date) FROM stdin;
\.


--
-- Data for Name: sales_activity_result; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sales_activity_result (sales_activity_result_id, sales_activity_id, description, seqno, last_active_date, first_follow_up_days, first_follow_up_task, second_follow_up_days, second_follow_up_task, third_follow_up_days, third_follow_up_task) FROM stdin;
\.


--
-- Data for Name: sales_points; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sales_points (sales_points_id, employee_id, point_date, prospect_id, prospect_status_id) FROM stdin;
\.


--
-- Data for Name: screen; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.screen (screen_id, filename, auth_code, screen_name, description, screen_type, avatar_path, technology) FROM stdin;
00001-0000000164	standard/hr/projectParent		Project Parent	Project Parent	2		F
00001-0000000456	standard/hr/lastActivityReport	\N	Last Activity Report	Last Activity Report	1		H
00001-0000000464	standard/hr/viewLog	\N	Applicant Event Log	Applicant Event Log	3		H
00001-0000000379	standard/crm/prospectSummary2	\N	Prospect Summary 2	Prospect Summary 2	1	\N	H
00001-0000000378	standard/crm/prospectParentDollars	\N	Prospect Parent Dollars	Prospect Parent Dollars	2	\N	H
00000-0000000125	standard/crm/prospectSalesPersonParent	\N	Prospects by Salespersons	Manage Prospects in the System by Salespersons	2	\N	H
00001-0000000263	standard/crm/prospectType	\N	Prospect Type	Prospect Type	1	\N	H
00000-0000000131	standard/crm/appointment	\N	Appointments	Manage Appointments in the System by Employee	1	\N	H
00000-0000000113	standard/crm/clientContact	\N	Client Contacts	Manage Contacts for a Client	3	\N	H
00001-0000000268	standard/crm/salesTasks	\N	Sales Tasks	Sales Tasks	1	\N	H
00001-0000000102	standard/tutorial/tutorial1	\N	Tutorial 1	Tutorial 1	1	\N	H
00001-0000000327	standard/tutorial/dynTutorial1	\N	Dyn Tutorial 1	Dyn Tutorial 1	1	\N	H
00001-0000000336	standard/tutorial/webServiceTest	\N	Web Service Test	Web Service Test	1	\N	H
00001-0000000109	standard/tutorial/tutorial16	\N	Tutorial 1 6	Tutorial 1 6	1	\N	H
00001-0000000113	standard/tutorial/tutorial4	\N	Tutorial 4	Tutorial 4	1	\N	H
00001-0000000330	standard/tutorial/dynTutorial4	\N	Dyn Tutorial 4	Dyn Tutorial 4	1	\N	H
00001-0000000329	standard/tutorial/dynTutorial3	\N	Dyn Tutorial 3	Dyn Tutorial 3	1	\N	H
00000-0000000042	standard/site/screenGroup	\N	Screen Groups	Manage Screen Groups in the System	1	\N	H
00001-0000000321	standard/site/userActivity	\N	User Activity	User Activity	1	\N	H
00001-0000000385	standard/wizard/benefitWizard/page/w4PDF/W4	\N	W 4	W 4	1	\N	H
00001-0000000371	standard/wizard/benefitWizard/page/voluntarySpouseRLG/BenefitWizardVoluntary	\N	Benefit Wizard Voluntary	Benefit Wizard Voluntary	1	\N	H
00001-0000000347	standard/wizard/benefitWizard/page/finish	\N	Finish	Finish	1	\N	H
00000-0000000055	standard/hr/hrDependent	\N	Dependents	Manage Dependents for Employee	3	\N	H
00001-0000000123	standard/hr/benefitsAddedReport	\N	Benefits Added Report	Benefits Added Report	1	\N	H
00001-0000000190	standard/hr/benefitChangeReport	\N	Benefit Change Report	Benefit Change Report	1	\N	H
00000-0000000114	standard/crm/clientQuestionDetail	\N	Client Question Details	Manage Question Details for a Client	3	\N	H
00001-0000000105	standard/tutorial/tutorial12	\N	Tutorial 1 2	Tutorial 1 2	1	\N	H
00001-0000000319	standard/site/companySecurityOverride	\N	Company Security Override	Company Security Override	1	\N	H
00001-0000000320	standard/site/databaseQuery	\N	Database Query	Database Query	1	\N	H
00001-0000000297	standard/hrConfig/wizardConfigurator	\N	Wizard Configurator	Wizard Configurator	1	\N	H
00000-0000000144	standard/hr/serviceListReport	\N	Service List	Reporting on Employee Service	1	\N	H
00000-0000000080	standard/hr/mailingLabelListReport	\N	Mailing Label List	Reporting on a List of Mailing Labels from Employees in the System	1	\N	H
00000-0000000146	standard/hr/employeeExport	\N	Employee Export	Employee Export	1	\N	H
00001-0000000410	standard/hr/rateType	\N	Rate Type	Rate Type	1	\N	H
00001-0000000185	standard/hr/benefitWizardApproval	\N	Benefit Wizard Approval	Benefit Wizard Approval	1	\N	H
00001-0000000145	standard/hr/spousalVerificationReport	\N	Spousal Verification Report	Spousal Verification Report	1	\N	H
00001-0000000277	standard/hr/benefitWizardStatus	\N	Benefit Wizard Status	Benefit Wizard Status	1	\N	H
00000-0000000078	standard/hr/benefitChangeReason	\N	Benefit Change Reasons	Manage Benefit Change Reasons in the System	1	\N	H
00000-0000000016	standard/hr/hrEEORace	\N	EEO Races	Manage EEO Races in the System	1	\N	H
00000-0000000010	standard/hr/benefitAssignment	\N	Benefits	Assign Benefits for Employee	3	\N	H
00001-0000000253	custom/humana/criticalIllnessExport	\N	Critical Illness Export	Critical Illness Export	1	\N	H
00001-0000000355	standard/wizard/benefitWizard/page/simpleBenefitAutoAssign	\N	Simple Benefit Auto Assign	Simple Benefit Auto Assign	1	\N	H
00001-0000000369	standard/wizard/benefitWizard/page/voluntaryChildRLG/BenefitWizardVoluntary	\N	Benefit Wizard Voluntary	Benefit Wizard Voluntary	1	\N	H
00001-0000000384	standard/wizard/benefitWizard/page/prismReview	\N	Prism Review	Prism Review	1	\N	H
00001-0000000349	standard/wizard/benefitWizard/page/groupCriticalIllnessWmCo	\N	Group Critical Illness Wm Co	Group Critical Illness Wm Co	1	\N	H
00001-0000000367	standard/wizard/benefitWizard/page/smoker/BenefitWizardSmoker	\N	Benefit Wizard Smoker	Benefit Wizard Smoker	1	\N	H
00001-0000000340	standard/wizard/benefitWizard/page/category/BenefitWizardCategory	\N	Benefit Wizard Category	Benefit Wizard Category	1	\N	H
00001-0000000343	standard/wizard/benefitWizard/page/criticalIllnessHumana/CriticalIllnessHumana2Custom	\N	Critical Illness Humana 2 Custom	Critical Illness Humana 2 Custom	1	\N	H
00000-0000000020	standard/hr/hrEvaluationResponse	\N	Employee Evaluation	Employee Evaluation Response	1	\N	H
00001-0000000420	standard/project/workerAvailabilityReport	\N	Worker Availability Report	Worker Availability Report	1		H
00000-0000000026	standard/hr/hrStatus	\N	Employee Statuses	Manage Employee Statuses in the System	1	\N	H
00000-0000000012	standard/hr/hrBenefitPackage	\N	Benefit Packages	Manage Benefit Packages in the System	1	\N	H
00000-0000000018	standard/hr/hrEvalCategory	\N	Evaluation Categories	Manage Evaluation Categories in the System	1	\N	H
00000-0000000071	standard/hr/hrEmployeeStatusChangedReport	\N	Employee Status Changed	Reporting on Employees by Status Changed	1	\N	H
00001-0000000173	standard/hr/vspVisionEnrollmentForm	\N	Vsp Vision Enrollment Form	Vsp Vision Enrollment Form	1	\N	H
00001-0000000081	custom/williamsonCounty/hrParentEdit	\N	Hr Parent Edit	Hr Parent Edit	1	\N	H
00000-0000000079	standard/hr/employeeListReport	\N	Employee List	Reporting on a List of Employees and optionally Dependents in the System	1	\N	H
00001-0000000186	standard/hr/cignaWizardExport	\N	Cigna Wizard Export	Cigna Wizard Export	1	\N	H
00001-0000000408	standard/hr/billingRateSetup	\N	Billing Rate Setup	Billing Rate Setup	1	\N	H
00001-0000000239	standard/hr/emergencyContact	\N	Emergency Contact	Emergency Contact	1	\N	H
00001-0000000129	standard/hr/wageType	\N	Wage Types	Manage Wage Types	1	\N	H
00001-0000000284	standard/hr/employmentHistory	\N	Employment History	Employment History	3		H
00001-0000000461	standard/project/shifts	\N	Project Shifts	Project Shifts	3		H
00001-0000000462	standard/project/task	\N	Project Tasks	Edit project tasks	3		H
00001-0000000404	custom/waytogo/applicantImport	\N	Applicant Import	Applicant Import	1	\N	H
00000-0000000133	standard/at/applicantJobType	\N	Applicant Job Types	Manage Applicant Job Types in the System by Employee	1	\N	H
00001-0000000443	standard/project/projectParent2		Project Parent 2	Project Parent screen used by WTG	1		F
00001-0000000467	standard/misc/run		Run	Run a test program	1		H
00001-0000000468	standard/at/message		Applicant Messages	Messaging for applicants globally	1		H
00001-0000000469	standard/at/messageSingle		Messaging for individual applicants	Messaging for individual applicants	3		H
00001-0000000470	standard/project/message		Project Messaging	Messaging associated with the selected project	3		H
00001-0000000465	standard/hr/message		HR messages	HR individual messages	3		H
00001-0000000471	standard/hr/message		Worker Messaging	Messaging for a particular worker	3		H
00000-0000000057	standard/billing/productService	\N	Product/Services	Manage Products and Services in the System	1	\N	H
00001-0000000365	standard/wizard/benefitWizard/page/simpleVoluntary	\N	Simple Voluntary	Simple Voluntary	1	\N	H
00001-0000000273	standard/hr/benefitAuditExport	\N	Benefit Audit Export	Benefit Audit Export	1	\N	H
00001-0000000180	standard/hr/paydayExport	\N	Payday Export	Payday Export	1	\N	H
00001-0000000311	standard/misc/onboardingForms	\N	Onboarding Forms	Onboarding Forms	1	\N	H
00001-0000000415	standard/billing/expenseEdit	\N	Expense Edit	Expense Edit	1		H
00001-0000000068	custom/williamsonCounty/cobraLettersReport	\N	Cobra Letters Report	Cobra Letters Report	1	\N	H
00001-0000000418	standard/billing/receipts	\N	Expense Receipts	Expense Receipts	1		H
00001-0000000419	standard/billing/receiptExport	\N	Expense Receipt Export	Expense Receipt Export	1		H
00001-0000000423	standard/project/inventoryExport	\N	Project Inventory Export	Project Inventory Export	1		H
00001-0000000424	standard/project/inventoryEmail	\N	Project Inventory Email	Project Inventory Email	3		H
00001-0000000427	standard/time/trainingTime	\N	Training Time	Training Time	1		H
00001-0000000432	standard/hr/emailView	\N	Person email view & send	Person email view & send	3		H
00001-0000000433	standard/project/workerAssignmentReport	\N	Worker Assignment Report	Worker Assignment Report	1		H
00001-0000000438	standard/project/projectHours	\N	Project Hours	Project Hours	1		H
00001-0000000434	standard/hr/ADPImportAll	\N	ADP Import All	ADP Import All	1		H
00001-0000000439	standard/hr/employeeLabelSetup	\N	Employee Label Setup	Employee Label Setup	1		H
00001-0000000440	standard/hr/employeeLabel	\N	Employee Labels	Employee Labels	3		H
00001-0000000067	custom/williamsonCounty/birthdayListReport	\N	Birthday List Report	Birthday List Report	1	\N	H
00001-0000000314	standard/misc/onboardingSetup	\N	Onboarding Setup	Onboarding Setup	1	\N	H
00001-0000000166	standard/inventory/productAttribute	\N	Product Attribute	Product Attribute	1	\N	H
00001-0000000429	standard/hr/adpExport/ADPExport	\N	ADP Export	ADP Export	1		H
00001-0000000426	standard/project/workerTrainingReport	\N	Worker Training Report	Worker Training Report	3		H
00001-0000000416	standard/billing/expenseEditByProject	\N	Expense Edit By Project	Expense Edit By Project	1		H
00001-0000000417	standard/billing/expenseAccounts	\N	Edit Expense Sub-accounts	Edit Expense Sub-accounts	1		H
00001-0000000442	standard/hr/hrParent2	\N	HR Search screen 2	HR Search screen 2	2		H
00001-0000000444	standard/project/projectSummary2	\N	Project Summary 2	Project Summary 2	3		H
00001-0000000445	standard/project/projectSubtype	\N	Project Subtype Setup	Project Subtype Setup	1		H
00001-0000000421	standard/project/sendEmail	\N	Project email assigned workers	Project email assigned workers	3		H
00001-0000000422	standard/hr/workerLastDayExport	\N	Worker last day export	Worker last day export	1		H
00001-0000000425	standard/project/requiredTraining	\N	Project Required Training	Project Required Training	3		H
00001-0000000428	standard/hr/sendemail/SendEmail	\N	Send email to people with time	Send email to people with time	1		H
00001-0000000430	standard/billing/projectProfitReport	\N	Project Net Income Report	Project Net Income Report	1		H
00001-0000000449	standard/project/noShowReport	\N	Worker No-Show Report	Worker No-Show Report	1		H
00001-0000000450	standard/project/projectBilling2	\N	Project Billing	Project Billing for Hourly & Project-based billing	3		H
00001-0000000301	standard/inventory/quote	\N	Quote	Quote	1	\N	H
00001-0000000182	standard/inventory/productGroup	\N	Product Group	Product Group	1	\N	H
00001-0000000118	standard/tutorial/tutorial9	\N	Tutorial 9	Tutorial 9	1	\N	H
00001-0000000106	standard/tutorial/tutorial13	\N	Tutorial 1 3	Tutorial 1 3	1	\N	H
00001-0000000112	standard/tutorial/tutorial3	\N	Tutorial 3	Tutorial 3	1	\N	H
00001-0000000074	custom/williamsonCounty/hrEmployeeStatusChangedReport	\N	Hr Employee Status Changed Report	Hr Employee Status Changed Report	1	\N	H
00001-0000000080	custom/williamsonCounty/hrParentAdd	\N	Hr Parent Add	Hr Parent Add	1	\N	H
00001-0000000075	custom/williamsonCounty/hrEnrollmentHistoryReport	\N	Hr Enrollment History Report	Hr Enrollment History Report	1	\N	H
00001-0000000141	custom/williamsonCounty/benefitRevenueReport	\N	Benefit Revenue Report	Benefit Revenue Report	1	\N	H
00001-0000000073	custom/williamsonCounty/hrEmployeesByStatusReport	\N	Hr Employees By Status Report	Hr Employees By Status Report	1	\N	H
00001-0000000451	standard/hr/employeeNotAvailable	\N	Employee Unavailability Dates	Employee Unavailability Dates	3		H
00001-0000000090	custom/williamsonCounty/projectListParentWC	\N	Project List Parent W C	Project List Parent W C	1	\N	H
00001-0000000257	custom/williamsonCounty/hrDependent	\N	Hr Dependent	Hr Dependent	1	\N	H
00001-0000000064	custom/williamsonCounty/benefitChangeReport	\N	Benefit Change Report	Benefit Change Report	1	\N	H
00001-0000000437	standard/billing/perDiemException	\N	Per Diem Exception	Per Diem Exception	1		H
00001-0000000087	custom/williamsonCounty/projectCheckList	\N	Project Check List	Project Check List	1	\N	H
00001-0000000446	standard/project/absentWorkerReport	\N	Absent Worker Report	Also called the Sub-8 Worker Report	1		H
00001-0000000447	standard/project/projectAssignment	\N	Project Assignment	Project Assignment	2		H
00001-0000000448	standard/project/rostersReport	\N	Rosters Report	Rosters Report	1		H
00001-0000000093	custom/williamsonCounty/retireeLettersReport	\N	Retiree Letters Report	Retiree Letters Report	1	\N	H
00001-0000000333	standard/tutorial/dynTutorial7	\N	Dyn Tutorial 7	Dyn Tutorial 7	1	\N	H
00001-0000000382	standard/wizard/benefitWizard/page/gradedDeathFidelityLife	\N	Graded Death Fidelity Life	Graded Death Fidelity Life	1	\N	H
00001-0000000348	standard/wizard/benefitWizard/page/flex/BenefitWizardFlex	\N	Benefit Wizard Flex	Benefit Wizard Flex	1	\N	H
00000-0000000074	standard/hr/benefitStatementReport	\N	Benefit Statements	Reporting on Employee Benefit Statements	1	\N	H
00001-0000000276	standard/hr/benefitsRequestedReport	\N	Benefits Requested Report	Benefits Requested Report	1	\N	H
00001-0000000258	custom/williamsonCounty/salaryCustomImport	\N	Salary Custom Import	Salary Custom Import	1	\N	H
00001-0000000256	custom/williamsonCounty/ameritasExport	\N	Ameritas Export	Ameritas Export	1	\N	H
00001-0000000070	custom/williamsonCounty/hrCallLogParent	\N	Hr Call Log Parent	Hr Call Log Parent	1	\N	H
00001-0000000086	custom/williamsonCounty/paymentCouponsReport	\N	Payment Coupons Report	Payment Coupons Report	1	\N	H
00001-0000000079	custom/williamsonCounty/hrOrgGroupAndStatusHistory	\N	Hr Org Group And Status History	Hr Org Group And Status History	1	\N	H
00001-0000000095	custom/williamsonCounty/terminationReport	\N	Termination Report	Termination Report	1	\N	H
00001-0000000088	custom/williamsonCounty/projectCommentWC	\N	Project Comment W C	Project Comment W C	1	\N	H
00001-0000000119	custom/williamsonCounty/lettersReport	\N	Letters Report	Letters Report	1	\N	H
00001-0000000091	custom/williamsonCounty/projectParentWC	\N	Project Parent W C	Project Parent W C	1	\N	H
00001-0000000121	custom/williamsonCounty/payrollExport	\N	Payroll Export	Payroll Export	1	\N	H
00001-0000000246	custom/drc/benefitCategory	\N	Benefit Category	Benefit Category	1	\N	H
00001-0000000250	custom/drc/vendorProcessing	\N	Vendor Processing	Vendor Processing	1	\N	H
00001-0000000244	custom/drc/agentImport	\N	Agent Import	Agent Import	1	\N	H
00001-0000000247	custom/drc/benefitParent	\N	Benefit Parent	Benefit Parent	2	\N	H
00001-0000000198	custom/drc/hrEmployee	\N	Hr Employee	Hr Employee	1	\N	H
00001-0000000248	custom/drc/benefitSetup	\N	Benefit Setup	Benefit Setup	1	\N	H
00001-0000000249	custom/drc/hrParent	\N	Hr Parent	Hr Parent	2	\N	H
00001-0000000307	standard/misc/documentSearch	\N	Document Search	Document Search	1	\N	H
00001-0000000085	custom/williamsonCounty/newHireReport	\N	New Hire Report	New Hire Report	1	\N	H
00001-0000000078	custom/williamsonCounty/hrMiscReport	\N	Hr Misc Report	Hr Misc Report	1	\N	H
00001-0000000092	custom/williamsonCounty/projectSummaryWC	\N	Project Summary W C	Project Summary W C	1	\N	H
00001-0000000142	custom/williamsonCounty/paymentDepositReport	\N	Payment Deposit Report	Payment Deposit Report	1	\N	H
00001-0000000452	standard/hr/assignmentHistory	\N	HR Assignment History	HR Assignment History	3		H
00001-0000000199	custom/drc/importBenefitEnrollee	\N	Import Benefit Enrollee	Import Benefit Enrollee	1	\N	H
00001-0000000403	custom/waytogo/employeeImport	\N	Employee Import	Employee Import	1	\N	H
00000-0000000033	standard/misc/message		Messages	Send and Receive Messages	1		H
00001-0000000260	standard/billing/productivity	\N	Productivity	Productivity	1	\N	H
00000-0000000147	standard/billing/bankAccount	\N	Bank Accounts	Bank Account	1	\N	H
00001-0000000405	custom/waytogo/timesheetEntry	\N	Timesheet Entry	Timesheet Entry	2		H
00000-0000000058	standard/project/projectRoute	\N	Project Routes	Manage Project Routes in the System	1	\N	H
00001-0000000151	standard/inventory/assemblyTemplate	\N	Assembly Template	Assembly Template	1	\N	H
00001-0000000181	standard/inventory/assemblyTemplateTree	\N	Assembly Template Tree	Assembly Template Tree	1	\N	H
00001-0000000436	standard/hr/currentAssignments	\N	Current worker assignments	Current worker assignments	3		H
00001-0000000361	standard/wizard/benefitWizard/page/simpleDependents	\N	Simple Dependents	Simple Dependents	1	\N	H
00000-0000000040	standard/hr/salaryReport	\N	Employee Salary List	Reporting on Employeee Salary Information	1	\N	H
00000-0000000139	standard/hr/benefitPriceUpload	\N	Benefit Price Upload	Upload Benefit Price Changes in the System	1	\N	H
00001-0000000259	standard/billing/billingDetailReport	\N	Billing Detail Report	Billing Detail Report	1	\N	H
00000-0000000126	standard/billing/bankDraftBatch	\N	Bank Draft Batches	Manage Bank Draft Batches in the System	1	\N	H
00000-0000000148	standard/billing/employeeInvoice	\N	Employee Invoice	Employee Invoice	1	\N	H
00000-0000000056	standard/billing/glAccount/GLAccount	\N	GL Accounts	Manage GL Accounts in the System	1		H
00000-0000000032	standard/billing/invoiceSearch	\N	Invoices	Search and manage already created Invoices	1	\N	H
00001-0000000309	standard/misc/genericWelcome	\N	Generic Welcome	Generic Welcome	1	\N	H
00001-0000000441	standard/hr/labelSearch	\N	Employee Label Search	Employee Label Search	2		H
00000-0000000152	standard/hr/missingBenefitCostReport	\N	Missing Benefit Cost Report	Missing Benefit Cost Report	1	\N	H
00001-0000000454	standard/hr/comments	\N	Applicant Comments	Applicant Comments	3		H
00001-0000000285	standard/hr/evolutionImport	\N	Evolution Import	Evolution Import	1	\N	H
00000-0000000069	standard/hr/hrDependentsAgeLimitReport	\N	Dependent Age Limit	Reporting on Dependents that are reaching an particular age limit	1	\N	H
00001-0000000170	standard/hr/enrollmentChangeReport	\N	Enrollment Change Report	Enrollment Change Report	1	\N	H
00000-0000000128	standard/hr/employeeBilling	\N	HR Employee/Dependent Billing	Manage Billing for an Employe/Dependent	3	\N	H
00000-0000000066	standard/hr/hrFormType	\N	Form Types	Manage HR Form Types in the System	1	\N	H
00001-0000000435	standard/project/assignmentHistory	\N	Worker Project Assignment History	Worker Project Assignment History	1		H
00001-0000000127	standard/hr/studentStatus	\N	Student Status Verifications	Manage Student Status and Verifications for a person	1	\N	H
00001-0000000187	standard/hr/wizardChangeRequestExport	\N	Wizard Change Request Export	Wizard Change Request Export	1	\N	H
00001-0000000138	standard/billing/service	\N	Services	Service	1	\N	H
00001-0000000104	standard/tutorial/tutorial11	\N	Tutorial 1 1	Tutorial 1 1	1	\N	H
00000-0000000127	standard/billing/billingStatus	\N	HR Billing Statuses	Manage HR Billing Statuses in the System	1	\N	H
00000-0000000006	standard/billing/createTimesheetInvoice	\N	Create Timesheet Invoice	Creation of Invoices from Timesheets	1	\N	H
00000-0000000108	standard/crm/prospectParent	\N	Prospects	Manage Prospect in the System	2	\N	H
00001-0000000264	standard/crm/salesActivity	\N	Sales Activity	Sales Activity	1	\N	H
00001-0000000455	standard/misc/reminder	\N	Reminders	Reminders	1		H
00000-0000000119	standard/crm/contactQuestion	\N	Contact Questions	Manage Contact Questions in the System	1	\N	H
00000-0000000111	standard/crm/clientSummary	\N	Client Summary	Manage Summary for a Client	3	\N	H
00001-0000000101	standard/misc/announcement/Announcements	\N	Announcements	Announcements	1	\N	H
00001-0000000304	standard/misc/agencyHomePage	\N	Agency Home Page	Agency Home Page	1	\N	H
00001-0000000174	standard/misc/alertAgent	\N	Alert Agent	Alert Agent	1	\N	H
00001-0000000381	standard/site/activeEmployeesReport	\N	Active Employees Report	Active Employees Report	1	\N	H
00001-0000000144	standard/hr/spousalVerification	\N	Spousal Verification	Spousal Verification	1	\N	H
00000-0000000013	standard/hr/hrCheckList	\N	Check List Items	Manage Check List Items in the System	1	\N	H
00001-0000000412	standard/hr/employeeBillingReport	\N	Employee Billing Report	Employee Billing Report	1	\N	H
00001-0000000179	standard/hr/benefitPendingChanges	\N	Benefit Pending Changes	Benefit Pending Changes	1	\N	H
00001-0000000154	standard/hr/hrEmployeeCompany	\N	Hr Employee Company	Hr Employee Company	1	\N	H
00001-0000000293	standard/hr/w4	\N	W-4	Employee Withholding Allowance Certificate	1	\N	H
00001-0000000072	custom/williamsonCounty/hrEmployeeHrDept	\N	Hr Employee Hr Dept	Hr Employee Hr Dept	1	\N	H
00001-0000000083	custom/williamsonCounty/missingEnrollmentListReport	\N	Missing Enrollment List Report	Missing Enrollment List Report	1	\N	H
00001-0000000065	custom/williamsonCounty/benefitStatementReport	\N	Benefit Statement Report	Benefit Statement Report	1	\N	H
00001-0000000084	custom/williamsonCounty/munisVoluntaryExport	\N	Munis Voluntary Export	Munis Voluntary Export	1	\N	H
00001-0000000071	custom/williamsonCounty/hrEmployee	\N	Hr Employee	Hr Employee	1	\N	H
00000-0000000123	standard/crm/prospectAppointment	\N	Prospect Appointments	Manage Appointments for a Prospect	3	\N	H
00000-0000000124	standard/crm/prospectLog	\N	Prospect Logs	Manage Logs for a Prospect	3	\N	H
00000-0000000099	standard/project/subProjectParent	\N	Sub-Projects	Manage Sub-Projects for a Project	2	\N	H
00000-0000000005	standard/misc/companyOrgGroup	\N	Company Groups	Manage Company Organizational Groups in the System	1	\N	H
00001-0000000305	standard/misc/agentApproval	\N	Agent Approval	Agent Approval	1	\N	H
00001-0000000302	standard/inventory/quoteParent	\N	Quote Parent	Quote Parent	2	\N	H
00001-0000000286	standard/hr/hrAccrualImport	\N	Hr Accrual Import	Hr Accrual Import	1	\N	H
00001-0000000278	standard/hr/dependentReport	\N	Dependent Report	Dependent Report	1	\N	H
00001-0000000161	standard/hr/projectCheckList	\N	Project Check List	Project Check List	1	\N	H
00000-0000000007	standard/hr/employeeProfile	\N	Profile	View Employee Profile	1	\N	H
00000-0000000009	standard/hr/hrAccruedTimeOff	\N	Accrued Time Off	Manage Accrued Time Off for Employee	3	\N	H
00001-0000000280	standard/hr/dynamicReportParent	\N	Dynamic Report Parent	Dynamic Report Parent	2	\N	H
00001-0000000076	custom/williamsonCounty/hrEnrollmentListBillingReport	\N	Hr Enrollment List Billing Report	Hr Enrollment List Billing Report	1	\N	H
00001-0000000124	custom/williamsonCounty/arJournalReport	\N	Ar Journal Report	Ar Journal Report	1	\N	H
00001-0000000094	custom/williamsonCounty/serviceListReport	\N	Service List Report	Service List Report	1	\N	H
00000-0000000094	standard/project/phase	\N	Project Phases	Manage Project Phases in the System	1	\N	H
00000-0000000067	standard/project/projectForm	\N	Project Forms	Manage Forms for a project	3	\N	H
00001-0000000453	standard/hr/assignments	\N	Worker Assignments	Worker Assignments	3		H
00001-0000000334	standard/tutorial/dynTutorial8	\N	Dyn Tutorial 8	Dyn Tutorial 8	1	\N	H
00001-0000000463	standard/project/myTasks	\N	My Tasks	My Tasks	1		H
00001-0000000331	standard/tutorial/dynTutorial5	\N	Dyn Tutorial 5	Dyn Tutorial 5	1	\N	H
00001-0000000335	standard/tutorial/iFrameTest	\N	I Frame Test	I Frame Test	1	\N	H
00000-0000000034	standard/site/loginListReport	\N	Login List	Reporting Failed/Successful Logins in the System	1	\N	H
00001-0000000196	standard/site/screenGroupCompanyAccess	\N	Screen Group Company Access	Screen Group Company Access	1	\N	H
00000-0000000092	standard/site/properties	\N	System Properties	Manage System Properties	1	\N	H
00000-0000000000	standard/site/changePassword	\N	Change Password	Allows a user to change his/her system login password	1	\N	H
00001-0000000204	standard/hrConfig/benefitSetup	\N	Benefit Setup	Benefit Setup	1	\N	H
00001-0000000296	standard/hrConfig/timeRelatedBenefit	\N	Time Related Benefit	Time Related Benefit	1	\N	H
00001-0000000345	standard/wizard/benefitWizard/page/demographics/BenefitWizardDemographics	\N	Benefit Wizard Demographics	Benefit Wizard Demographics	1	\N	H
00001-0000000386	standard/wizard/benefitWizard/page/prismDocSign	\N	Prism Doc Sign	Prism Doc Sign	1	\N	H
00001-0000000397	custom/prism/gradedDeathBenefitExport	\N	Graded Death Benefit Export	Graded Death Benefit Export	1	\N	H
00001-0000000077	custom/williamsonCounty/hrEnrollmentListReport	\N	Hr Enrollment List Report	Hr Enrollment List Report	1	\N	H
00001-0000000082	custom/williamsonCounty/hrParentHrDept	\N	Hr Parent Hr Dept	Hr Parent Hr Dept	1	\N	H
00001-0000000089	custom/williamsonCounty/projectHistory	\N	Project History	Project History	1	\N	H
00001-0000000066	custom/williamsonCounty/benefitWizard	\N	Benefit Wizard	Benefit Wizard	1	\N	H
00001-0000000255	custom/williamsonCounty/aflacExport	\N	Aflac Export	Aflac Export	1	\N	H
00001-0000000241	custom/cimplify/companyOrgGroup	\N	Company Org Group	Company Org Group	1	\N	H
00001-0000000243	custom/cimplify/timesheetEntryByClock	\N	Timesheet Entry By Clock	Timesheet Entry By Clock	1	\N	H
00001-0000000332	standard/tutorial/dynTutorial6	\N	Dyn Tutorial 6	Dyn Tutorial 6	1	\N	H
00001-0000000114	standard/tutorial/tutorial5	\N	Tutorial 5	Tutorial 5	1	\N	H
00001-0000000111	standard/tutorial/tutorial2	\N	Tutorial 2	Tutorial 2	1	\N	H
00001-0000000116	standard/tutorial/tutorial7	\N	Tutorial 7	Tutorial 7	1	\N	H
00001-0000000117	standard/tutorial/tutorial8	\N	Tutorial 8	Tutorial 8	1	\N	H
00001-0000000107	standard/tutorial/tutorial14	\N	Tutorial 1 4	Tutorial 1 4	1	\N	H
00001-0000000108	standard/tutorial/tutorial15	\N	Tutorial 1 5	Tutorial 1 5	1	\N	H
00001-0000000337	standard/webservices/dynamicWebServices	\N	Dynamic Web Services	Dynamic Web Services	1	\N	H
00001-0000000146	custom/druryGroup/censusReport	\N	Census Report	Census Report	1	\N	H
00001-0000000132	custom/druryGroup/hrEmployee	\N	Hr Employee	Hr Employee	1	\N	H
00001-0000000137	custom/druryGroup/basicLifeCalculator	\N	Basic Life Calculator	Basic Life Calculator	1	\N	H
00001-0000000252	custom/humana/criticalIllness/BenefitCostCalculator	\N	Benefit Cost Calculator	Benefit Cost Calculator	1	\N	H
00001-0000000240	custom/boxMaker/boxMakerEmployeeImport	\N	Box Maker Employee Import	Box Maker Employee Import	1	\N	H
00001-0000000168	custom/moelis/homeWarning	\N	Home Warning	Home Warning	1	\N	H
00001-0000000242	custom/cimplify/employeeProfile	\N	Employee Profile	Employee Profile	1	\N	H
00001-0000000200	custom/goldsteinSacks/timesheetAccountingReview	\N	Timesheet Accounting Review	Timesheet Accounting Review	1	\N	H
00000-0000000097	standard/project/timesheet	\N	Project Timesheets	View Timesheets logged against a Project	3	\N	H
00001-0000000188	standard/misc/documentManagement	\N	Document Management	Document Management	1	\N	H
00001-0000000184	standard/misc/serviceSubscribedAssociation	\N	Service Subscribed Association	Service Subscribed Association	1	\N	H
00000-0000000105	standard/crm/clientParent	\N	Clients	Manage Clients in the System	2	\N	H
00001-0000000270	standard/crm/visitorTrackImport	\N	Visitor Track Import	Visitor Track Import	1	\N	H
00001-0000000261	standard/crm/prospectAssignment	\N	Prospect Assignment	Prospect Assignment	1	\N	H
00000-0000000103	standard/crm/prospectSource	\N	Prospect Source	Manage Prospect Sources in the System	1	\N	H
00000-0000000112	standard/crm/clientEDI	\N	Client EDI	Manage EDI information for a Client	3	\N	H
00000-0000000110	standard/crm/prospectOrgGroup	\N	Prospect Groups	Manage Prospect Organizational Groups in the System	1	\N	H
00000-0000000120	standard/crm/prospectSummary	\N	Prospect Summary	Manage Summary for a Prospect	3	\N	H
00001-0000000269	standard/crm/standardProspectImport	\N	Standard Prospect Import	Standard Prospect Import	1	\N	H
00000-0000000121	standard/crm/prospectContact	\N	Prospect Contacts	Manage Contacts for a Prospect	3	\N	H
00001-0000000262	standard/crm/prospectImport	\N	Prospect Import	Prospect Import	1	\N	H
00001-0000000202	standard/crm/clientStatusDetailList	\N	Client Status Detail List	Client Status Detail List	1	\N	H
00000-0000000109	standard/crm/prospectListReport	\N	Prospect List	Reporting on Prospect Information	1	\N	H
00001-0000000266	standard/crm/salesPoints	\N	Sales Points	Sales Points	1	\N	H
00000-0000000115	standard/crm/clientAppointment	\N	Client Appointments	Manage Appointments for a Client	3	\N	H
00001-0000000267	standard/crm/salesQueue	\N	Sales Queue	Sales Queue	1	\N	H
00000-0000000116	standard/crm/clientLog	\N	Client Logs	Manage Logs for a Client	3	\N	H
00001-0000000411	standard/hr/employeePerformanceReport	\N	Employee Performance Report	Employee Performance Report	1		H
00001-0000000201	standard/crm/clientStatus	\N	Client Status	Client Status	1	\N	H
00000-0000000118	standard/crm/companyQuestion	\N	Company Questions	Manage Company Questions in the System	1	\N	H
00000-0000000107	standard/crm/clientOrgGroup	\N	Client Groups	Manage Client Organizational Groups in the System	1	\N	H
00000-0000000106	standard/crm/clientListReport	\N	Client List	Reporting on Client Information	1	\N	H
00000-0000000122	standard/crm/prospectQuestionDetail	\N	Prospect Question Details	Manage Question Details for a Prospect	3	\N	H
00001-0000000265	standard/crm/salesPipeline	\N	Sales Pipeline	Sales Pipeline	1	\N	H
00001-0000000203	standard/crm/clientStatusView	\N	Client Status View	Client Status View	3	\N	H
00000-0000000104	standard/crm/prospectStatus	\N	Prospect Status	Manage Prospect Statuses in the System	1	\N	H
00001-0000000457	standard/site/authenticatedSenders	\N	Authenticated Senders	Authenticated Senders	1		H
00000-0000000117	standard/crm/appointmentLocation	\N	Appointment Locations	Manage Appointment Locations in the System	1	\N	H
00001-0000000157	standard/misc/companySearch	\N	Company Search	Company Search	1	\N	H
00000-0000000084	standard/project/routingAndAssignmentHistory	\N	Project Routing and Assignment History	Routing and Assignment History for a Project	3	\N	H
00000-0000000100	standard/project/orgGroupProjectListClient	\N	Assignments by Organization Group - Client	Project assignments by organizational group - Client version	2	\N	H
00001-0000000317	standard/project/projectBillingReport	\N	Project Billing Report	Project Billing Report	1	\N	H
00000-0000000083	standard/project/projectBenefit	\N	Project Benefits	Manage associated benefits for a project	3	\N	H
00000-0000000043	standard/project/standardProject	\N	Standard Projects	Manage Standard Projects in the System	1	\N	H
00000-0000000036	standard/project/projectStatus	\N	Project Statuses	Manage Project Statiues in the System	1	\N	H
00001-0000000155	standard/misc/agency	\N	Agency	Agency	1	\N	H
00001-0000000197	standard/site/screenGroupReport	\N	Screen Group Report	Screen Group Report	1	\N	H
00000-0000000085	standard/project/orgGroupProjectList	\N	Assignments by Organization Group	Project assignments by organizational group	2	\N	H
00000-0000000060	standard/project/projectSummary	\N	Project Summary	Manage Summary for Project	3	\N	H
00000-0000000068	standard/project/projectFormType	\N	Form Types	Manage Project Form Types in the System	1	\N	H
00000-0000000044	standard/project/stdProjectInstantiation/StandardProjectInstantiation	\N	Standard Project Assignments	Apply standard Projects to Requesting Companies	1	\N	H
00000-0000000086	standard/project/employeeProjectList	\N	Assignments	Project Assignments for Employee	2	\N	H
00000-0000000061	standard/project/projectBilling	\N	Project Billing	Manage Billing for Project	3	\N	H
00001-0000000126	standard/hr/payHistory	\N	Pay History	Employee Payment History	1	\N	H
00000-0000000038	standard/project/projectTimesheetReport	\N	Project/Timesheet	Reporting on Projects and Timesheets in the System	2	\N	H
00001-0000000318	standard/project/projectTimesheetReportClientLimited	\N	Project Timesheet Report Client Limited	Project Timesheet Report Client Limited	1	\N	H
00000-0000000093	standard/project/projectListReport	\N	Project List	Reporting on Project Information	1	\N	H
00000-0000000101	standard/project/routeStopCheckListDetail	\N	Project Check Lists	Manage Check Lists for a project	3	\N	H
00000-0000000063	standard/project/projectComment	\N	Project Comments	Manage Comments for Project	3	\N	H
00000-0000000096	standard/project/projectTimesheetReportClient	\N	Project/Timesheet - Client	Reporting on Projects and Timesheets in the System - Client version	2	\N	H
00000-0000000062	standard/project/projectCurrentStatus	\N	Project Current Status	Manage Current Status for Project	2		H
00001-0000000380	standard/project/projectRouteSync	\N	Project Route Sync	Project Route Sync	1	\N	H
00000-0000000039	standard/project/projectType	\N	Project Types	Manage Project Types in the System	1	\N	H
00000-0000000142	standard/hr/employeeGarnishment	\N	Employee Wage Garnishments	Manage Wage Garnishments for an Employee	3	\N	H
00000-0000000035	standard/project/projectCategory	\N	Project Categories	Manage Project Categories in the System	1	\N	H
00000-0000000102	standard/project/detailReport	\N	Project Detail Report	Reporting on the detail of the current project	3	\N	H
00000-0000000098	standard/project/viewParent	\N	Project Views	Manage Project Views for the logged in person	2	\N	H
00001-0000000176	standard/misc/alertManagingCompany	\N	Alert Managing Company	Alert Managing Company	1	\N	H
00001-0000000175	standard/misc/alertHRAdmin	\N	Alert H R Admin	Alert H R Admin	1	\N	H
00001-0000000308	standard/misc/employeeHomePage	\N	Employee Home Page	Employee Home Page	1	\N	H
00001-0000000306	standard/misc/developerHomepage	\N	Developer Homepage	Developer Homepage	1	\N	H
00000-0000000129	standard/misc/processSchedule	\N	Process Schedule	Manage Process Schedules in the System	1	\N	H
00001-0000000183	standard/misc/serviceSubscribed	\N	Service Subscribed	Service Subscribed	1	\N	H
00001-0000000315	standard/misc/onboardingTasks	\N	Onboarding Tasks	Onboarding Tasks	1	\N	H
00001-0000000312	standard/misc/onboardingHomePage	\N	Onboarding Home Page	Onboarding Home Page	1	\N	H
00001-0000000359	standard/wizard/benefitWizard/page/simpleDemographics	\N	Simple Demographics	Simple Demographics	1	\N	H
00001-0000000351	standard/wizard/benefitWizard/page/physicians	\N	Physicians	Physicians	1	\N	H
00001-0000000165	standard/hr/projectSummary	\N	Project Summary	Project Summary	1	\N	H
00001-0000000274	standard/hr/benefitChangeApproval	\N	Benefit Change Approval	Benefit Change Approval	1	\N	H
00001-0000000128	standard/hr/studentStatusReport	\N	Student Status Report	Reporting on Students	1	\N	H
00000-0000000090	standard/hr/hrOrgGroupHistory	\N	Organizational Group History	View Organizational Group History for Employee	3	\N	H
00000-0000000050	standard/misc/vendor	\N	Vendors	Manage Vendors in the System	1	\N	H
00001-0000000310	standard/misc/interfaceLog	\N	Interface Log	Interface Log	1	\N	H
00000-0000000051	standard/misc/vendorOrgGroup	\N	Vendor Groups	Manage Vendor Organizational Groups in the System	1	\N	H
00000-0000000052	standard/misc/vendorReport	\N	Vendor List	Reporting on Vendor Information	1	\N	H
00001-0000000313	standard/misc/onboardingMonitoring	\N	Onboarding Monitoring	Onboarding Monitoring	1	\N	H
00001-0000000140	standard/inventory/productInventory	\N	Product Inventory	Product Inventory	1	\N	H
00001-0000000148	standard/inventory/location	\N	Location	Location	1	\N	H
00001-0000000147	standard/inventory/item	\N	Item	Item	1	\N	H
00001-0000000303	standard/inventory/quoteTemplate	\N	Quote Template	Quote Template	1	\N	H
00001-0000000103	standard/tutorial/tutorial10	\N	Tutorial 1 0	Tutorial 1 0	1	\N	H
00001-0000000115	standard/tutorial/tutorial6	\N	Tutorial 6	Tutorial 6	1	\N	H
00001-0000000110	standard/tutorial/tutorial17	\N	Tutorial 1 7	Tutorial 1 7	1	\N	H
00000-0000000041	standard/site/screen	\N	Screens	Manage Screens in the System	1	\N	H
00001-0000000195	standard/site/screenGroupAccess	\N	Screen Group Access	Screen Group Access	2		H
00001-0000000366	standard/wizard/benefitWizard/page/simpleWelcome	\N	Simple Welcome	Simple Welcome	1	\N	H
00001-0000000350	standard/wizard/benefitWizard/page/lifetimeTermFidelityLife	\N	Lifetime Term Fidelity Life	Lifetime Term Fidelity Life	1	\N	H
00000-0000000027	standard/hr/hrStatusHistory	\N	Status History	Manage Status History for Employee	3	\N	H
00000-0000000150	standard/time/timeOffRequestReview	\N	Time Off Requests Review	Approve/Reject Time Off Requests	1	\N	H
00001-0000000392	standard/time/timeCardSignatureReport	\N	Time Card Signature Report	Time Card Signature Report	1	\N	H
00000-0000000070	standard/hr/hrEmployeesByStatusReport	\N	Employees By Status	Reporting on Employees by current status	1	\N	H
00001-0000000135	standard/hr/benefitConfigAdvancedCost	\N	Coverage Configurations	Coverage Configuration for a benefit being managed by a parent.  This version has advanced costs.	3	\N	H
00000-0000000077	standard/hr/benefitParent	\N	Benefits Parent	Parent screen for managing benefits	2	\N	H
00000-0000000072	standard/hr/hrEnrollmentListReport	\N	Employee Enrollment	Reporting on Employee's Enrollments	1	\N	H
00000-0000000028	standard/hr/hrTraining	\N	Training	Manage Training for Employee	3	\N	H
00000-0000000089	standard/hr/benefitsUsingCOBRAReport	\N	Benefits Using COBRA	Reporting on Benefits Using COBRA	1	\N	H
00001-0000000163	standard/hr/projectHistory	\N	Project History	Project History	1	\N	H
00000-0000000004	standard/misc/company	\N	Companies	Manage Companies in the System	1	\N	H
00001-0000000205	standard/misc/employerHomePage	\N	Employer Home Page	Employer Home Page	1	\N	H
00001-0000000316	standard/misc/reportGenerator	\N	Report Generator	Report Generator	1	\N	H
00001-0000000152	standard/misc/userAgreement	\N	User Agreement	User Agreement	1	\N	H
00000-0000000087	standard/hr/missingEnrollmentListReport	\N	Missing Enrollment	Reporting on Employees that are missing enrollment in Benefits	1	\N	H
00000-0000000073	standard/hr/hrOrgGroup	\N	Organizational Groups	Manage Organizational Groups for an Employee	3	\N	H
00001-0000000287	standard/hr/hrEvaluationStatus	\N	Hr Evaluation Status	Hr Evaluation Status	1	\N	H
00001-0000000162	standard/hr/projectComment	\N	Project Comment	Project Comment	1	\N	H
00000-0000000135	standard/at/applicantQuestion	\N	Applicant Questions	Manage Applicant Questions in the System by Employee	1	\N	H
00000-0000000145	standard/hr/garnishmentType	\N	Garnishment Type	Garnishment Type	1	\N	H
00001-0000000282	standard/hr/education	\N	Education	Education	3		H
00001-0000000459	standard/project/dailyReportSetup	\N	Daily Report Setup	Daily Report Setup	1		H
00000-0000000140	standard/hr/employeePayroll	\N	Employee Payroll	Manage Payroll for an Employee	3	\N	H
00001-0000000291	standard/hr/standardImport	\N	Standard Import	Standard Import	1	\N	H
00000-0000000082	standard/hr/benefitHistory	\N	Benefit History	View Benefit History for Employee	3	\N	H
00000-0000000008	standard/billing/holiday	\N	Holidays	Manage Holidays for the current calendar year	1	\N	H
00000-0000000059	standard/project/projectParent	\N	Project Management	Manage Projects	2	\N	H
00001-0000000156	standard/misc/agencyOrgGroup	\N	Agency Org Group	Agency Org Group	1	\N	H
00001-0000000189	standard/misc/documentViewing	\N	Document Viewing	Document Viewing	1	\N	H
00001-0000000149	standard/inventory/lot	\N	Lot	Lot	1	\N	H
00000-0000000065	standard/hr/hrForm	\N	Employee Forms	Manage Forms for an employee	3	\N	H
00001-0000000292	standard/hr/totalCompensationReport	\N	Total Compensation Report	Total Compensation Report	1	\N	H
00001-0000000134	standard/hr/benefitClass	\N	Benefit Classes	Manage Employee Benefit Classes	1	\N	H
00000-0000000031	standard/hr/hrWageHistory	\N	Wage and Position History	Manage Wage History for Employee	3	\N	H
00001-0000000413	standard/hr/workHistory	\N	Work History	Work History	1	\N	H
00000-0000000023	standard/hr/hrNoteCategory	\N	Note Categories	Manage Note Categories in the System	1	\N	H
00000-0000000015	standard/hr/hrEEOCategory	\N	EEO Categories	Manage EEO Categories in the System	1	\N	H
00001-0000000139	standard/inventory/productByType	\N	Product By Type	Product By Type	1	\N	H
00001-0000000281	standard/hr/ediTransactionExport	\N	Edi Transaction Export	Edi Transaction Export	1	\N	H
00000-0000000022	standard/hr/hrNote	\N	Notes	Manage Employee Notes	3	\N	H
00001-0000000396	standard/hr/evolutionExport	\N	Evolution Export	Evolution Export	1	\N	H
00001-0000000275	standard/hr/benefitCostReport	\N	Benefit Cost Report	Benefit Cost Report	1	\N	H
00000-0000000017	standard/hr/hrEmployee	\N	Employee & Dependent Profile	Manage Basic information for Employee	3	\N	H
00000-0000000143	standard/hr/birthdayListReport	\N	Birthday List	Reporting on Employee Birthdays	1	\N	H
00000-0000000088	standard/hr/hrMiscReport	\N	Miscellaneous HR	Reporting on Miscellaneous HR	1	\N	H
00001-0000000294	standard/hr/wizardProjectApprovals	\N	Wizard Project Approvals	Wizard Project Approvals	1	\N	H
00001-0000000171	standard/hr/metLifeEnrollmentForm	\N	Met Life Enrollment Form	Met Life Enrollment Form	1	\N	H
00000-0000000021	standard/hr/hrEvent	\N	Events	Manage Events for Employee	3	\N	H
00001-0000000159	standard/hr/callLogListParent	\N	Call Log List Parent	Call Log List Parent	1	\N	H
00001-0000000069	custom/williamsonCounty/department	\N	Department	Department	1	\N	H
00001-0000000122	custom/williamsonCounty/retireeAgeReport	\N	Retiree Age Report	Retiree Age Report	1	\N	H
00001-0000000357	standard/wizard/benefitWizard/page/simpleCategory	\N	Simple Category	Simple Category	1	\N	H
00001-0000000358	standard/wizard/benefitWizard/page/simpleCoverage	\N	Simple Coverage	Simple Coverage	1	\N	H
00001-0000000290	standard/hr/reference	\N	Reference	Reference	3		H
00001-0000000458	standard/project/sendTextMessage	\N	Text Messages for Project Workers	Text Messages for Project Workers	3		H
00001-0000000363	standard/wizard/benefitWizard/page/simpleQualifyingEvent	\N	Simple Qualifying Event	Simple Qualifying Event	1	\N	H
00001-0000000387	standard/wizard/benefitWizard/page/madisonDental	\N	Madison Dental	Madison Dental	1	\N	H
00001-0000000364	standard/wizard/benefitWizard/page/simpleReview	\N	Simple Review	Simple Review	1	\N	H
00001-0000000372	standard/wizard/benefitWizard/page/welcome/BenefitWizardWelcome	\N	Benefit Wizard Welcome	Benefit Wizard Welcome	1	\N	H
00001-0000000339	standard/wizard/benefitWizard/page/benefit/BenefitWizardBenefit	\N	Benefit Wizard Benefit	Benefit Wizard Benefit	1	\N	H
00000-0000000134	standard/at/applicantPosition	\N	Applicant Positions	Manage Applicant Positions in the System by Employee	1	\N	H
00001-0000000344	standard/wizard/benefitWizard/page/criticalIllnessHumana/CriticalIllnessHumanaCustom	\N	Critical Illness Humana Custom	Critical Illness Humana Custom	1	\N	H
00001-0000000414	custom/waytogo/test	\N	Test	Test	1	\N	H
00001-0000000362	standard/wizard/benefitWizard/page/simpleFinish	\N	Simple Finish	Simple Finish	1	\N	H
00000-0000000064	standard/hr/benefitCategory	\N	Benefit Categories	Manage Benefit Categories in the System	1	\N	H
00001-0000000169	standard/hr/cignaEnrollmentForm	\N	Cigna Enrollment Form	Cigna Enrollment Form	1	\N	H
00001-0000000143	standard/hr/interArahantEnrollmentCompare	\N	Inter Arahant Enrollment Compare	Inter Arahant Enrollment Compare	1	\N	H
00001-0000000125	standard/hr/eeo1Survey	\N	EEO-1 Survey	EEO-1 Survey	1	\N	H
00001-0000000431	standard/project/imageUploadReport	\N	Project Image Upload Report	Project Image Upload Report	1		H
00001-0000000407	standard/hr/billingRateSetup/BillingRate	\N	Billing Rate	Billing Rate	1	\N	H
00001-0000000158	custom/druryGroup/visionExport	\N	Vision Export	Vision Export	1	\N	H
00001-0000000120	custom/druryGroup/consolidatedBillingReport	\N	Consolidated Billing Report	Consolidated Billing Report	1	\N	H
00001-0000000131	custom/druryGroup/callLogListParent	\N	Call Log List Parent	Call Log List Parent	1	\N	H
00001-0000000376	custom/druryGroup/billDetailReport	\N	Bill Detail Report	Bill Detail Report	1	\N	H
00001-0000000133	custom/druryGroup/hrParent	\N	Hr Parent	Hr Parent	1	\N	H
00001-0000000377	custom/prism/madisonDentalExport	\N	Madison Dental Export	Madison Dental Export	1	\N	H
00001-0000000254	custom/prism/lifetimeBenefitTermExport	\N	Lifetime Benefit Term Export	Lifetime Benefit Term Export	1	\N	H
00001-0000000251	custom/fidelity/lifetimeTerm/BenefitCostCalculator	\N	Benefit Cost Calculator	Benefit Cost Calculator	1	\N	H
00001-0000000245	custom/drc/benefit	\N	Benefit	Benefit	1	\N	H
00000-0000000025	standard/hr/hrPosition	\N	Employee Positions	Manage Employee Positions in the System	1	\N	H
00001-0000000326	standard/time/timesheetEntryByWeek	\N	Timesheet Entry By Week	Timesheet Entry By Week	1	\N	H
00000-0000000047	standard/time/timesheetFinalization/TimesheetFinalizations	\N	Timesheet Finalization	Reporting on Timesheet Finalizations in the System	1	\N	H
00001-0000000153	standard/time/overtimeHours	\N	Overtime Hours	Overtime Hours	1	\N	H
00000-0000000048	standard/time/timesheetReview	\N	Timesheets Review	Review and take action on finalized Timesheets in the System	1	\N	H
00001-0000000298	standard/hrConfig/wizardProjectTemplate	\N	Wizard Project Template	Wizard Project Template	1	\N	H
00001-0000000193	standard/hrConfig/benefitImportConfig	\N	Benefit Import Config	Benefit Import Config	1	\N	H
00001-0000000192	standard/hrConfig/benefitConfigVariant	\N	Time Off Config	Time Off Config	1	\N	H
00001-0000000299	standard/hrConfig/wizardRequiredDocuments	\N	Wizard Required Documents	Wizard Required Documents	1	\N	H
00001-0000000354	standard/wizard/benefitWizard/page/simpleBenefit	\N	Simple Benefit	Simple Benefit	1	\N	H
00001-0000000368	standard/wizard/benefitWizard/page/voluntary/BenefitWizardVoluntary	\N	Benefit Wizard Voluntary	Benefit Wizard Voluntary	1	\N	H
00001-0000000341	standard/wizard/benefitWizard/page/costNoSalaryBenefit	\N	Cost No Salary Benefit	Cost No Salary Benefit	1	\N	H
00001-0000000352	standard/wizard/benefitWizard/page/qualifyingEvent/BenefitWizardQualifyingEvent	\N	Benefit Wizard Qualifying Event	Benefit Wizard Qualifying Event	1	\N	H
00001-0000000342	standard/wizard/benefitWizard/page/costNoSalaryCategory	\N	Cost No Salary Category	Cost No Salary Category	1	\N	H
00001-0000000194	standard/security/securityGroupAccess	\N	Security Group Access	Security Group Access	1	\N	H
00000-0000000053	standard/security/securityGroup	\N	Security Groups	Manage Security Groups in the System	1	\N	H
00000-0000000054	standard/security/securityToken	\N	Security Tokens	Manage Security Tokens in the System	1	\N	H
00001-0000000177	standard/time/multiCompanyTimesheetExport	\N	Multi Company Timesheet Export	Multi Company Timesheet Export	1	\N	H
00001-0000000390	standard/time/timesheetEntrySemiMonthly	\N	Timesheet Entry Semi Monthly	Timesheet Entry Semi Monthly	1	\N	H
00001-0000000391	standard/time/approvedTimeOffRequestReport	\N	Approved Time Off Request Report	Approved Time Off Request Report	1	\N	H
00001-0000000323	standard/time/lbmcTimeExport	\N	Lbmc Time Export	Lbmc Time Export	1	\N	H
00001-0000000460	standard/project/checkInStatus	\N	Worker Check In Status	Worker check in status	1		H
00000-0000000149	standard/time/timeOffRequest	\N	Time Off Requests	Manage Time Off Requests per Employee	1	\N	H
00001-0000000322	standard/time/adpTimeExport	\N	Adp Time Export	Adp Time Export	1	\N	H
00001-0000000393	standard/time/timesheetEntryByClockSimple	\N	Timesheet Entry By Clock Simple	Timesheet Entry By Clock Simple	1	\N	H
00000-0000000153	standard/time/employeeTimesheetByClockReport	\N	Employee/Timesheet By Clock	Reporting on Employees and Timesheets By Clock In/Out	1	\N	H
00001-0000000373	standard/wizard/wizardPayrollExport	\N	Wizard Payroll Export	Wizard Payroll Export	1	\N	H
00001-0000000338	standard/wizard/benefitWizard	\N	Benefit Wizard	Benefit Wizard	1	\N	H
00001-0000000360	standard/wizard/benefitWizard/page/simpleDemographicsWmCo/SimpleDemographics	\N	Simple Demographics	Simple Demographics	1	\N	H
00001-0000000356	standard/wizard/benefitWizard/page/simpleBenefitEmpOnly	\N	Simple Benefit Emp Only	Simple Benefit Emp Only	1	\N	H
00001-0000000370	standard/wizard/benefitWizard/page/voluntaryEmployeeRLG/BenefitWizardVoluntary	\N	Benefit Wizard Voluntary	Benefit Wizard Voluntary	1	\N	H
00001-0000000346	standard/wizard/benefitWizard/page/dependents/BenefitWizardDependents	\N	Benefit Wizard Dependents	Benefit Wizard Dependents	1	\N	H
00001-0000000353	standard/wizard/benefitWizard/page/review/BenefitWizardReview	\N	Benefit Wizard Review	Benefit Wizard Review	1	\N	H
00001-0000000383	standard/wizard/benefitWizard/page/simpleDemographicsPrism/SimpleDemographics	\N	Simple Demographics	Simple Demographics	1	\N	H
00000-0000000049	standard/time/unpaidTime	\N	Unpaid Time	Lists time spent on time benefits that don't pay	1	\N	H
00001-0000000406	standard/time/timesheetEntryWeeklySummary	\N	Timesheet Entry Weekly Summary	Timesheet Entry Weekly Summary	1	\N	H
00000-0000000045	standard/time/timesheetEntry	\N	Timesheets	Create Timesheets in the System	1	\N	H
00001-0000000389	standard/time/timesheetReview2	\N	Timesheet Review 2	Timesheet Review 2	1	\N	H
00000-0000000046	standard/time/timesheetEntryExceptions	\N	Timesheet Exception	Reporting on Timesheets Missing in the System	1	\N	H
00001-0000000394	standard/time/singleCompanyTimesheetExport	\N	Single Company Timesheet Export	Single Company Timesheet Export	1	\N	H
00000-0000000130	standard/time/employeeTimesheetReport	\N	Employee/Timesheet	Reporting on Employees and Timesheets in the System	1	\N	H
00001-0000000388	standard/time/timesheetClientReview	\N	Timesheet Client Review	Timesheet Client Review	1	\N	H
00001-0000000325	standard/time/timesheetByPayPeriod	\N	Timesheet By Pay Period	Timesheet By Pay Period	1	\N	H
00000-0000000151	standard/time/timesheetEntryByClock	\N	Timesheets By Clock	Timesheet entry by Clock In/Out	1	\N	H
00000-0000000141	standard/time/employeeTimesheetChart	\N	Employee/Timesheet Chart	Charting on Employees and Timesheets in the System	1	\N	H
00000-0000000091	standard/time/timesheetAccountingReview	\N	Timesheets Accounting Review	Review and take action on Timesheets in an accounting state in the System	1	\N	H
00001-0000000324	standard/time/timeDetailReport	\N	Time Detail Report	Time Detail Report	1	\N	H
00001-0000000167	standard/time/payrollReport	\N	Payroll Report	Payroll Report	1	\N	H
00001-0000000178	standard/time/timesheetApproval	\N	Timesheet Approval	Timesheet Approval	1	\N	H
00001-0000000395	standard/hr/genericEmployeeImport	\N	Generic Employee Import	Generic Employee Import	1	\N	H
00001-0000000402	custom/waytogo/employeeExport	\N	Employee Export	Employee Export	1	\N	H
00001-0000000401	custom/waytogo/timesheetImport	\N	Timesheet Import	Timesheet Import	1	\N	H
00001-0000000375	standard/misc/cloneCompany	\N	Clone Company	Clone Company	1	\N	H
00001-0000000400	standard/hr/employeeExport	\N	Employee Export	Employee Export	1	\N	H
00001-0000000374	standard/hr/hrEEOCategory	\N	Hr E E O Category	Hr E E O Category	1	\N	H
00001-0000000466	standard/hr/removedFromRoster	\N	Removed From Roster	Removed From Roster	1		H
00001-0000000398	standard/hr/northAmericaAdminExport	\N	North America Admin Export	North America Admin Export	1	\N	H
00001-0000000399	standard/hr/paychexExport	\N	Paychex Payroll Export	Paychex Payroll Export	1		H
00001-0000000300	standard/inventory/locationCost	\N	Location Cost	Location Cost	1	\N	H
00001-0000000150	standard/inventory/productAssemblyTemplate	\N	Product Assembly Template	Product Assembly Template	1	\N	H
00001-0000000328	standard/tutorial/dynTutorial2	\N	Dyn Tutorial 2	Dyn Tutorial 2	1	\N	H
00001-0000000172	standard/hr/mutualEnrollmentForm	\N	Mutual Enrollment Form	Mutual Enrollment Form	1	\N	H
00000-0000000095	standard/hr/benefitExport	\N	Benefit Export	View and Create New Benefit Exports in the System	1	\N	H
00001-0000000409	standard/hr/billingRate	\N	Billing Rate	Billing Rate	1	\N	H
00001-0000000283	standard/hr/employeeAndDependentImport	\N	Employee And Dependent Import	Employee And Dependent Import	1	\N	H
00000-0000000154	standard/hr/paySchedule	\N	Pay Schedules	Manage Pay Schedules and Pay Periods in the System	1	\N	H
00001-0000000288	standard/hr/logFiles	\N	Log Files	Log Files	1	\N	H
00000-0000000075	standard/hr/benefit	\N	Summary	Summary for a benefit being managed by a parent benefit screen	3	\N	H
00001-0000000279	standard/hr/dynamicReport	\N	Dynamic Report	Dynamic Report	1	\N	H
00001-0000000160	standard/hr/hrCallLogParent	\N	Hr Call Log Parent	Hr Call Log Parent	1	\N	H
00001-0000000191	standard/hr/benefitImport	\N	Benefit Import	Benefit Import	1	\N	H
00000-0000000029	standard/hr/hrTrainingCategory	\N	Training Categories	Manage Training Categories in the System	1	\N	H
00001-0000000130	standard/hr/newBenefitSignupReport	\N	New Benefit Signup Report	New Benefit Signup Report	1	\N	H
00001-0000000295	standard/hr/wizardProjectDetail	\N	Wizard Project Detail	Wizard Project Detail	1	\N	H
00000-0000000024	standard/hr/hrParent	\N	HR	Parent screen for the HR Module	2	\N	H
00001-0000000289	standard/hr/personHistory	\N	Person History	Person History	1	\N	H
00000-0000000019	standard/hr/hrEvaluation	\N	Evaluations	Manage Evaluations for Employee	3	\N	H
00000-0000000014	standard/hr/hrCheckListDetail	\N	Check List Details	Manage Check List Details for Employee	3	\N	H
00000-0000000132	standard/at/applicantApplicationStatus	\N	Applicant Application Statuses	Manage Applicant Application Statuses in the System by Employee	1	\N	H
00001-0000000271	standard/at/applicantEeo	\N	Applicant Eeo	Applicant Eeo	1	\N	H
00000-0000000137	standard/at/applicantStatus	\N	Applicant Statuses	Manage Applicant Statuses in the System by Employee	1	\N	H
00001-0000000272	standard/at/applicantProfile	\N	Applicant Profile	Applicant Profile	3		H
00000-0000000138	standard/at/applicant	\N	Applicants	Manage Applicants in the System by Employee	2		H
00000-0000000136	standard/at/applicantSource	\N	Applicant Sources	Manage Applicant Sources	1		H
\.


--
-- Data for Name: screen_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.screen_group (screen_group_id, group_name, screen_id, description, wizard_type, technology) FROM stdin;
00000-0000000000	System Administrator	\N		N	H
00000-0000000002	Human Resources	\N	\N	N	H
00000-0000000016	Employees & Dependents	00001-0000000442		N	H
00000-0000000017	Employee Benefits	\N		N	H
00000-0000000018	Employee Status	\N		N	H
00000-0000000019	Employee Documentation	\N		N	H
00000-0000000020	HR Reports	\N	\N	N	H
00001-0000000346	Benefit Reports	\N		N	H
00000-0000000030	HR Interfaces	\N	\N	N	H
00001-0000000279	Label Search	00001-0000000441		N	H
00000-0000000021	HR Configuration	\N	\N	N	H
00000-0000000023	HR Benefit Configuration	\N	\N	N	H
00000-0000000024	Benefits Configuration Parent	00000-0000000077	\N	N	H
00001-0000000331	Applicants	\N		N	H
00001-0000000342	Applicant Tracking Configuration	\N		N	H
00001-0000000329	Applicants	00000-0000000138		N	H
00000-0000000004	Invoicing	\N	\N	N	H
00000-0000000026	Invoicing Configuration	\N	\N	N	H
00001-0000000228	Financial Reports	\N		N	H
00000-0000000001	Project Management	\N		N	H
00000-0000000013	Project Selection	00000-0000000059		N	H
00001-0000000277	Assignments	00001-0000000447		N	H
00000-0000000015	Project Management Configuration	\N	\N	N	H
00000-0000000014	Project Management Reports	\N	\N	N	H
00000-0000000031	Project Billable Hours	00000-0000000038		N	H
00001-0000000020	Project Selection by Group	00000-0000000100		N	H
00000-0000000005	Administrative Functions	\N		N	H
00000-0000000006	Entity Administration	\N	\N	N	H
00000-0000000011	Vendor Administration	\N	\N	N	H
00000-0000000012	Company Administration	\N	\N	N	H
00000-0000000007	System Configuration	\N	\N	N	H
00000-0000000008	Screens Configuration	\N	\N	N	H
00000-0000000009	Security Configuration	\N	\N	N	H
00001-0000000215	Screen Group Access	00001-0000000195		N	H
00000-0000000027	Admin Reports	\N	\N	N	H
00001-0000000023	Prospect Administration	\N		N	H
00001-0000000024	Prospect Selection	00000-0000000108		N	H
00001-0000000030	Configuration	\N		N	H
00000-0000000033	Project Views	00000-0000000098		N	H
00000-0000000003	Time Recording	\N		N	H
00000-0000000010	Client Administration	\N		N	H
00001-0000000037	Configuration	\N		N	H
00001-0000000036	Client Selection	00000-0000000105		N	H
\.


--
-- Data for Name: screen_group_access; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.screen_group_access (security_group_id, can_access_screen_group_id) FROM stdin;
\.


--
-- Data for Name: screen_group_hierarchy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.screen_group_hierarchy (screen_group_hierarchy_id, parent_screen_group_id, child_screen_group_id, seq_no, screen_id, default_screen, button_name) FROM stdin;
00001-0000002050	00000-0000000016	\N	1	00000-0000000022	N	Additional Fields
00000-0000000067	00000-0000000016	\N	0	00000-0000000017	Y	Profile
00001-0000002035	00000-0000000016	\N	6	00001-0000000413	N	
00001-0000002408	00000-0000000016	\N	8	00001-0000000440	N	\N
00000-0000000071	00000-0000000017	\N	0	00000-0000000009	N	\N
00000-0000000072	00000-0000000017	\N	1	00000-0000000010	N	\N
00000-0000000073	00000-0000000017	\N	2	00000-0000000082	N	\N
00000-0000000074	00000-0000000017	\N	3	00000-0000000055	N	\N
00000-0000000068	00000-0000000016	00000-0000000017	2	\N	N	Benefits
00000-0000000075	00000-0000000018	\N	0	00000-0000000027	Y	Status
00000-0000000076	00000-0000000018	\N	1	00000-0000000031	N	Wage & Position
00001-0000001524	00000-0000000018	\N	2	00000-0000000140	N	Payroll
00001-0000001526	00000-0000000018	\N	3	00000-0000000142	N	Garnishments
00001-0000001569	00000-0000000018	\N	4	00001-0000000126	N	\N
00000-0000000077	00000-0000000018	\N	5	00000-0000000073	N	\N
00000-0000000078	00000-0000000018	\N	6	00000-0000000090	N	\N
00000-0000000069	00000-0000000016	00000-0000000018	3	\N	N	Status
00000-0000000081	00000-0000000019	\N	2	00000-0000000021	Y	
00000-0000000079	00000-0000000019	\N	0	00000-0000000019	N	\N
00000-0000000080	00000-0000000019	\N	1	00000-0000000014	N	Check Lists
00000-0000000082	00000-0000000019	\N	3	00000-0000000022	N	\N
00000-0000000083	00000-0000000019	\N	4	00000-0000000028	N	\N
00000-0000000084	00000-0000000019	\N	5	00000-0000000065	N	Forms
00000-0000000070	00000-0000000016	00000-0000000019	4	\N	N	Documentation
00001-0000002384	00000-0000000016	\N	5	00001-0000000436	N	Current Assignments
00001-0000002373	00000-0000000016	\N	7	00001-0000000432	N	Email
00001-0000002490	00000-0000000016	\N	9	00001-0000000451	N	Dates Unavailable
00001-0000002492	00000-0000000016	\N	10	00001-0000000452	N	Assignment History
00001-0000002799	00000-0000000016	\N	11	00001-0000000239	N	\N
00001-0000002854	00000-0000000016	\N	12	00001-0000000471	N	Messages
00000-0000000012	00000-0000000002	00000-0000000016	0	\N	Y	
00001-0000002772	00000-0000000020	\N	1	00001-0000000456	N	\N
00000-0000000085	00000-0000000020	\N	0	00000-0000000079	N	\N
00001-0000002858	00000-0000000020	\N	2	00000-0000000040	N	\N
00001-0000002859	00000-0000000020	\N	3	00000-0000000070	N	\N
00001-0000002860	00000-0000000020	\N	4	00000-0000000071	N	\N
00001-0000002861	00000-0000000020	\N	5	00000-0000000080	N	\N
00001-0000002867	00000-0000000020	\N	12	00001-0000000128	N	Student Status
00001-0000002866	00000-0000000020	\N	11	00001-0000000125	N	\N
00001-0000002865	00000-0000000020	\N	10	00000-0000000088	N	Additional Reports
00001-0000002864	00000-0000000020	\N	9	00000-0000000049	N	\N
00001-0000002863	00000-0000000020	\N	8	00000-0000000144	N	\N
00001-0000002869	00001-0000000346	\N	0	00000-0000000072	N	\N
00001-0000002870	00001-0000000346	\N	1	00000-0000000074	N	\N
00001-0000002871	00001-0000000346	\N	2	00000-0000000087	N	\N
00001-0000002872	00001-0000000346	\N	3	00000-0000000089	N	\N
00001-0000002873	00001-0000000346	\N	4	00000-0000000152	N	\N
00001-0000002874	00001-0000000346	\N	5	00001-0000000123	N	\N
00001-0000002875	00001-0000000346	\N	6	00001-0000000130	N	\N
00001-0000002868	00000-0000000020	00001-0000000346	6	\N	N	\N
00001-0000002862	00000-0000000020	\N	7	00000-0000000143	N	\N
00000-0000000013	00000-0000000002	00000-0000000020	1	\N	N	Reports
00001-0000002377	00000-0000000030	\N	2	00001-0000000434	N	ADP Import
00001-0000002010	00000-0000000030	\N	0	00001-0000000399	Y	
00001-0000002071	00000-0000000030	\N	1	00001-0000000429	N	\N
00001-0000002876	00000-0000000030	\N	7	00000-0000000095	N	\N
00001-0000002877	00000-0000000030	\N	8	00001-0000000402	N	\N
00001-0000002878	00000-0000000030	\N	9	00001-0000000403	N	\N
00001-0000002056	00000-0000000030	\N	11	00001-0000000422	N	\N
00001-0000002879	00000-0000000030	\N	10	00001-0000000401	N	\N
00000-0000000014	00000-0000000002	00000-0000000030	2	\N	N	Interfaces
00001-0000002070	00000-0000000002	\N	3	00001-0000000428	N	\N
00001-0000002417	00001-0000000279	00000-0000000018	3	\N	N	\N
00001-0000002420	00001-0000000279	\N	6	00001-0000000432	N	\N
00001-0000002414	00001-0000000279	\N	0	00000-0000000017	N	\N
00001-0000002419	00001-0000000279	\N	5	00001-0000000436	N	\N
00001-0000002421	00001-0000000279	\N	7	00001-0000000440	Y	
00001-0000002415	00001-0000000279	\N	1	00000-0000000022	N	\N
00001-0000002416	00001-0000000279	00000-0000000017	2	\N	N	\N
00001-0000002418	00001-0000000279	00000-0000000019	4	\N	N	\N
00001-0000002413	00000-0000000002	00001-0000000279	4	\N	N	\N
00001-0000002844	00000-0000000002	\N	5	00001-0000000466	N	\N
00000-0000000110	00000-0000000023	\N	0	00000-0000000064	N	Categories
00000-0000000113	00000-0000000024	\N	0	00000-0000000075	Y	\N
00000-0000000114	00000-0000000024	\N	1	00001-0000000135	N	\N
00000-0000000111	00000-0000000023	00000-0000000024	1	\N	N	Benefits
00001-0000001572	00000-0000000023	\N	4	00001-0000000134	N	\N
00000-0000000093	00000-0000000021	00000-0000000023	0	\N	N	Benefits
00000-0000000094	00000-0000000021	\N	2	00000-0000000013	N	\N
00000-0000000095	00000-0000000021	\N	3	00000-0000000025	N	\N
00001-0000001566	00000-0000000021	\N	4	00001-0000000129	N	\N
00000-0000000096	00000-0000000021	\N	5	00000-0000000026	N	\N
00000-0000000097	00000-0000000021	\N	6	00000-0000000015	N	\N
00000-0000000098	00000-0000000021	\N	7	00000-0000000016	N	\N
00000-0000000099	00000-0000000021	\N	8	00000-0000000018	N	\N
00000-0000000100	00000-0000000021	\N	9	00000-0000000066	N	\N
00000-0000000101	00000-0000000021	\N	10	00000-0000000023	N	\N
00000-0000000102	00000-0000000021	\N	11	00000-0000000029	N	\N
00000-0000000015	00000-0000000002	00000-0000000021	6	\N	N	Configuration
00000-0000000002	00000-0000000000	00000-0000000002	0	\N	N	\N
00001-0000001570	00000-0000000021	\N	13	00000-0000000145	N	\N
00000-0000000104	00000-0000000021	\N	14	00000-0000000008	N	\N
00001-0000002407	00000-0000000021	\N	18	00001-0000000439	N	Employee Labels
00001-0000002857	00000-0000000021	\N	16	00000-0000000154	N	\N
00001-0000002000	00000-0000000021	\N	17	00001-0000000192	N	\N
00001-0000002813	00001-0000000342	\N	0	00000-0000000136	N	\N
00001-0000002814	00001-0000000342	\N	1	00000-0000000137	N	\N
00001-0000002815	00001-0000000342	\N	2	00000-0000000132	N	\N
00001-0000002816	00001-0000000342	\N	3	00000-0000000135	N	\N
00001-0000002812	00001-0000000331	00001-0000000342	5	\N	N	Configuration
00001-0000002760	00001-0000000331	\N	3	00000-0000000131	N	\N
00001-0000002847	00001-0000000331	\N	1	00001-0000000468	N	Messaging
00001-0000002755	00001-0000000329	\N	1	00001-0000000454	N	Comments
00001-0000002817	00001-0000000329	\N	5	00001-0000000239	N	\N
00001-0000002746	00001-0000000329	\N	0	00001-0000000272	Y	
00001-0000002747	00001-0000000329	\N	2	00001-0000000290	N	\N
00001-0000002748	00001-0000000329	\N	3	00001-0000000282	N	\N
00001-0000002749	00001-0000000329	\N	4	00001-0000000284	N	\N
00001-0000002849	00001-0000000329	\N	7	00001-0000000469	N	Messages
00001-0000002840	00001-0000000329	\N	6	00001-0000000464	N	Event Log
00001-0000002758	00001-0000000331	00001-0000000329	0	\N	Y	
00001-0000002761	00001-0000000331	\N	4	00001-0000000404	N	\N
00001-0000002759	00001-0000000331	\N	2	00000-0000000134	N	\N
00001-0000002757	00000-0000000000	00001-0000000331	1	\N	N	\N
00001-0000002765	00000-0000000000	\N	9	00001-0000000455	N	\N
00001-0000002403	00000-0000000004	\N	3	00001-0000000437	N	\N
00000-0000000019	00000-0000000004	\N	0	00000-0000000091	N	\N
00000-0000000020	00000-0000000004	\N	1	00000-0000000006	N	Create Invoice
00001-0000002049	00000-0000000004	\N	4	00001-0000000416	N	Per Diem by Project
00001-0000002052	00000-0000000004	\N	5	00001-0000000418	N	\N
00001-0000002053	00000-0000000004	\N	6	00001-0000000419	N	\N
00000-0000000117	00000-0000000026	\N	0	00000-0000000056	N	\N
00001-0000002051	00000-0000000026	\N	2	00001-0000000417	N	Expense Sub-accounts
00001-0000001577	00000-0000000026	\N	1	00001-0000000138	N	\N
00001-0000002887	00000-0000000026	\N	3	00000-0000000147	N	\N
00001-0000002888	00000-0000000026	\N	4	00000-0000000126	N	\N
00000-0000000022	00000-0000000004	00000-0000000026	8	\N	N	Configuration
00000-0000000021	00000-0000000004	\N	2	00000-0000000032	N	Display / Print Invoice
00001-0000002073	00001-0000000228	\N	0	00001-0000000430	Y	
00001-0000002072	00000-0000000004	00001-0000000228	7	\N	N	\N
00000-0000000004	00000-0000000000	00000-0000000004	7	\N	N	\N
00001-0000002851	00000-0000000013	\N	18	00001-0000000470	N	Messages
00001-0000002434	00000-0000000013	\N	0	00001-0000000444	N	Project Summary
00001-0000002470	00000-0000000013	\N	1	00001-0000000450	N	\N
00001-0000002432	00001-0000000277	\N	7	00001-0000000440	N	\N
00001-0000002392	00001-0000000277	\N	5	00001-0000000413	N	\N
00001-0000002391	00001-0000000277	\N	4	00001-0000000436	N	Current Assignments
00001-0000002388	00001-0000000277	\N	1	00000-0000000022	N	Additional Fields
00001-0000002389	00001-0000000277	00000-0000000018	2	\N	N	Status
00001-0000002390	00001-0000000277	00000-0000000019	3	\N	N	Documentation
00001-0000002393	00001-0000000277	\N	6	00001-0000000432	N	Email
00001-0000002387	00001-0000000277	\N	0	00000-0000000017	Y	Profile
00001-0000002386	00000-0000000013	00001-0000000277	3	\N	Y	
00000-0000000052	00000-0000000013	\N	4	00000-0000000067	N	Project Photos
00000-0000000055	00000-0000000013	\N	5	00000-0000000097	N	Timesheets
00001-0000000181	00000-0000000013	\N	6	00000-0000000101	N	Check Lists
00000-0000000051	00000-0000000013	\N	7	00000-0000000063	N	Comments
00000-0000000053	00000-0000000013	\N	8	00000-0000000084	N	Routing & Assignment History
00000-0000000054	00000-0000000013	\N	9	00000-0000000083	N	Benefit Association
00001-0000000189	00000-0000000013	\N	10	00000-0000000102	N	Detail Report
00001-0000002059	00000-0000000013	\N	15	00001-0000000425	N	Required Training
00001-0000002060	00000-0000000013	\N	16	00001-0000000426	N	\N
00001-0000002808	00000-0000000013	\N	2	00001-0000000461	N	Shifts
00001-0000002834	00000-0000000013	\N	17	00001-0000000462	N	Tasks
00000-0000000006	00000-0000000001	00000-0000000013	0	\N	Y	
00000-0000000061	00000-0000000015	\N	2	00000-0000000035	N	Categories
00000-0000000062	00000-0000000015	\N	3	00000-0000000039	N	Types
00000-0000000063	00000-0000000015	\N	5	00000-0000000036	N	Statuses
00000-0000000064	00000-0000000015	\N	6	00000-0000000094	N	Phases
00000-0000000065	00000-0000000015	\N	7	00000-0000000058	N	Routes
00000-0000000066	00000-0000000015	\N	8	00000-0000000068	N	\N
00001-0000002435	00000-0000000015	\N	4	00001-0000000445	N	Sub-Type
00001-0000002882	00000-0000000015	\N	9	00000-0000000043	N	\N
00001-0000002883	00000-0000000015	\N	10	00000-0000000044	N	\N
00000-0000000011	00000-0000000001	00000-0000000015	9	\N	N	Configuration
00001-0000002837	00000-0000000001	\N	8	00001-0000000463	N	\N
00001-0000002805	00000-0000000001	\N	7	00001-0000000460	N	\N
00001-0000002382	00000-0000000001	\N	6	00001-0000000435	N	Assignment History
00001-0000002375	00000-0000000014	\N	4	00001-0000000433	N	\N
00001-0000002441	00000-0000000014	\N	7	00001-0000000449	N	\N
00000-0000000137	00000-0000000031	\N	0	00000-0000000060	N	Summary
00000-0000000138	00000-0000000031	\N	1	00000-0000000061	N	Billing & Estimate
00000-0000000139	00000-0000000031	\N	2	00000-0000000062	N	Status
00000-0000000140	00000-0000000031	\N	3	00000-0000000063	N	Comments
00000-0000000141	00000-0000000031	\N	4	00000-0000000067	N	Forms
00000-0000000057	00000-0000000014	00000-0000000031	1	\N	N	Billable Hours
00000-0000000001	00000-0000000000	00000-0000000001	5	\N	N	Projects
00000-0000000142	00000-0000000031	\N	5	00000-0000000084	N	Routing & Assignment History
00000-0000000143	00000-0000000031	\N	6	00000-0000000083	N	Benefit Association
00000-0000000144	00000-0000000031	\N	7	00000-0000000097	Y	Timesheets
00001-0000000186	00000-0000000031	\N	8	00000-0000000101	N	Check Lists
00001-0000000193	00000-0000000031	\N	9	00000-0000000102	N	Detail Report
00000-0000000056	00000-0000000014	\N	0	00000-0000000093	Y	
00001-0000002440	00000-0000000014	\N	6	00001-0000000448	N	\N
00001-0000002054	00000-0000000014	\N	2	00001-0000000420	N	\N
00001-0000002371	00000-0000000014	\N	3	00001-0000000431	N	Image Upload Report
00001-0000002437	00000-0000000014	\N	5	00001-0000000446	N	Sub-8 Worker Report
00000-0000000010	00000-0000000001	00000-0000000014	5	\N	N	Reports
00001-0000002405	00000-0000000001	\N	3	00001-0000000438	N	\N
00001-0000000129	00001-0000000020	\N	0	00000-0000000060	N	Summary
00001-0000000130	00001-0000000020	\N	1	00000-0000000061	N	Billing & Estimate
00001-0000000132	00001-0000000020	\N	3	00000-0000000063	N	Comments
00001-0000000133	00001-0000000020	\N	4	00000-0000000067	N	Forms
00001-0000000131	00001-0000000020	\N	2	00000-0000000062	Y	Status
00001-0000000136	00001-0000000020	\N	6	00000-0000000097	N	Timesheets
00001-0000000179	00001-0000000020	\N	7	00000-0000000101	N	Check Lists
00001-0000000187	00001-0000000020	\N	9	00000-0000000102	N	Detail Report
00001-0000002880	00000-0000000001	00001-0000000020	1	\N	N	Project By Group
00000-0000000023	00000-0000000005	\N	0	00000-0000000000	N	\N
00000-0000000043	00000-0000000011	\N	0	00000-0000000050	N	\N
00000-0000000044	00000-0000000011	\N	1	00000-0000000051	N	\N
00000-0000000045	00000-0000000011	\N	2	00000-0000000052	N	\N
00000-0000000031	00000-0000000006	00000-0000000011	0	\N	N	Vendors
00000-0000000046	00000-0000000012	\N	0	00000-0000000004	N	\N
00000-0000000047	00000-0000000012	\N	1	00000-0000000005	N	\N
00000-0000000032	00000-0000000006	00000-0000000012	1	\N	N	Company
00000-0000000027	00000-0000000005	00000-0000000006	6	\N	N	\N
00000-0000000026	00000-0000000005	\N	5	00000-0000000008	N	\N
00001-0000001617	00000-0000000005	\N	9	00001-0000000188	N	\N
00001-0000002889	00000-0000000005	\N	3	00000-0000000007	N	\N
00001-0000001618	00000-0000000005	\N	10	00001-0000000189	N	\N
00000-0000000036	00000-0000000008	\N	0	00000-0000000041	N	\N
00000-0000000037	00000-0000000008	\N	1	00000-0000000042	N	\N
00000-0000000033	00000-0000000007	00000-0000000008	0	\N	N	Screens
00000-0000000038	00000-0000000009	\N	0	00000-0000000054	N	\N
00000-0000000039	00000-0000000009	\N	1	00000-0000000053	N	\N
00001-0000002003	00000-0000000009	\N	2	00001-0000000194	N	\N
00001-0000002006	00001-0000000215	\N	0	00000-0000000017	Y	
00001-0000002007	00001-0000000215	00000-0000000017	1	\N	N	\N
00001-0000002008	00001-0000000215	00000-0000000018	2	\N	N	\N
00001-0000002009	00001-0000000215	00000-0000000019	3	\N	N	\N
00001-0000002005	00000-0000000009	00001-0000000215	3	\N	N	
00000-0000000034	00000-0000000007	00000-0000000009	1	\N	N	Security
00000-0000000035	00000-0000000007	\N	2	00000-0000000092	N	\N
00001-0000002775	00000-0000000007	\N	3	00001-0000000457	N	\N
00000-0000000029	00000-0000000005	00000-0000000007	8	\N	N	\N
00000-0000000119	00000-0000000027	\N	0	00000-0000000034	N	\N
00000-0000000028	00000-0000000005	00000-0000000027	7	\N	N	Reports
00001-0000002890	00000-0000000005	\N	4	00000-0000000020	N	\N
00000-0000000005	00000-0000000000	00000-0000000005	13	\N	N	
00001-0000002891	00000-0000000000	\N	10	00001-0000000188	N	\N
00000-0000000000	00000-0000000000	\N	8	00000-0000000033	Y	
00001-0000002894	00000-0000000000	\N	11	00000-0000000131	N	\N
00001-0000000216	00001-0000000024	\N	0	00000-0000000120	Y	Summary
00001-0000000215	00001-0000000024	\N	3	00000-0000000123	N	Appointments
00001-0000000218	00001-0000000024	\N	4	00000-0000000124	N	Logs
00001-0000000219	00001-0000000024	\N	1	00000-0000000121	N	Contacts
00001-0000000217	00001-0000000024	\N	2	00000-0000000122	N	Company Details
00001-0000000196	00001-0000000023	00001-0000000024	0	\N	Y	Prospects
00001-0000000202	00001-0000000023	\N	1	00000-0000000110	N	\N
00001-0000000203	00001-0000000023	\N	3	00000-0000000109	N	\N
00001-0000002002	00001-0000000023	\N	2	00001-0000000269	N	\N
00001-0000000206	00001-0000000030	\N	0	00000-0000000103	N	\N
00001-0000000205	00001-0000000030	\N	1	00000-0000000104	N	\N
00001-0000002001	00001-0000000030	\N	2	00001-0000000263	N	\N
00001-0000000207	00001-0000000030	\N	3	00000-0000000117	N	\N
00001-0000000208	00001-0000000030	\N	4	00000-0000000118	N	\N
00001-0000000209	00001-0000000030	\N	5	00000-0000000119	N	\N
00001-0000000204	00001-0000000023	00001-0000000030	4	\N	N	
00001-0000002892	00000-0000000000	00001-0000000023	3	\N	N	Sales Prospects
00000-0000000153	00000-0000000033	\N	0	00000-0000000060	N	Summary
00000-0000000154	00000-0000000033	\N	1	00000-0000000061	N	Billing & Estimate
00000-0000000155	00000-0000000033	\N	2	00000-0000000062	Y	Status
00000-0000000156	00000-0000000033	\N	3	00000-0000000063	N	Comments
00000-0000000157	00000-0000000033	\N	4	00000-0000000067	N	Forms
00000-0000000158	00000-0000000033	\N	5	00000-0000000084	N	Routing & Assignment History
00000-0000000159	00000-0000000033	\N	6	00000-0000000083	N	Benefit Association
00000-0000000160	00000-0000000033	\N	7	00000-0000000097	N	Timesheets
00001-0000000184	00000-0000000033	\N	8	00000-0000000101	N	Check Lists
00001-0000000192	00000-0000000033	\N	9	00000-0000000102	N	Detail Report
00001-0000002895	00000-0000000000	00000-0000000033	12	\N	N	Outlines
00001-0000002025	00000-0000000003	\N	2	00001-0000000406	N	Per Diem
00000-0000000017	00000-0000000003	\N	3	00000-0000000048	N	\N
00001-0000002061	00000-0000000003	\N	4	00001-0000000427	N	\N
00001-0000002884	00000-0000000003	\N	5	00000-0000000151	N	\N
00001-0000002885	00000-0000000003	\N	6	00000-0000000149	N	\N
00001-0000002886	00000-0000000003	\N	7	00000-0000000150	N	\N
00000-0000000016	00000-0000000003	\N	0	00000-0000000045	Y	
00000-0000000003	00000-0000000000	00000-0000000003	6	\N	N	Worker Time
00001-0000000221	00000-0000000010	\N	1	00000-0000000107	N	\N
00001-0000000222	00000-0000000010	\N	3	00000-0000000106	N	Reports
00001-0000002802	00001-0000000037	\N	4	00001-0000000459	N	\N
00001-0000000224	00001-0000000037	\N	0	00000-0000000117	N	\N
00001-0000000225	00001-0000000037	\N	1	00000-0000000118	N	\N
00001-0000000226	00001-0000000037	\N	2	00000-0000000119	N	\N
00001-0000001620	00001-0000000037	\N	3	00001-0000000201	N	\N
00001-0000000223	00000-0000000010	00001-0000000037	4	\N	N	
00001-0000001622	00000-0000000010	\N	2	00001-0000000202	N	Client Status
00001-0000000228	00001-0000000036	\N	3	00000-0000000114	N	Company Details
00001-0000000231	00001-0000000036	\N	2	00000-0000000113	N	Contacts
00001-0000000232	00001-0000000036	\N	4	00000-0000000115	N	Appointments
00001-0000000229	00001-0000000036	\N	5	00000-0000000116	N	Logs
00001-0000000230	00001-0000000036	\N	1	00000-0000000112	N	EDI
00001-0000000227	00001-0000000036	\N	0	00000-0000000111	Y	Summary
00001-0000001621	00001-0000000036	\N	6	00001-0000000203	N	\N
00001-0000000220	00000-0000000010	00001-0000000036	0	\N	Y	Clients
00001-0000002893	00000-0000000000	00000-0000000010	4	\N	N	Clients
\.


--
-- Data for Name: screen_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.screen_history (transaction_id, person_id, screen_id, time_in, time_out) FROM stdin;
\.


--
-- Data for Name: security_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.security_group (security_group_id, id, name) FROM stdin;
\.


--
-- Data for Name: security_group_access; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.security_group_access (security_group_id, can_access_security_group_id) FROM stdin;
\.


--
-- Data for Name: security_group_hierarchy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.security_group_hierarchy (parent_security_group_id, child_security_group_id) FROM stdin;
\.


--
-- Data for Name: security_token; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.security_token (right_id, identifier, description) FROM stdin;
\.


--
-- Data for Name: service; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.service (service_id) FROM stdin;
\.


--
-- Data for Name: service_subscribed; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.service_subscribed (service_id, org_group_id, service_name, description, first_active_date, last_active_date, interface_code) FROM stdin;
\.


--
-- Data for Name: service_subscribed_join; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.service_subscribed_join (service_join_id, org_group_id, service_id, first_date, last_date, external_id) FROM stdin;
\.


--
-- Data for Name: spousal_insurance_verif; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spousal_insurance_verif (spousal_ins_verif_id, person_id, date_received, verification_year) FROM stdin;
\.


--
-- Data for Name: standard_project; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.standard_project (project_id, reference, description, detail_desc, project_sponsor_id, managing_employee, project_type_id, project_category_id, dollar_cap, billing_rate, billable, requester_name, product_id, billing_method, next_billing_date, bill_at_org_group, all_employees, company_id) FROM stdin;
\.


--
-- Data for Name: student_verification; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.student_verification (student_verification_id, person_id, school_year, calendar_period) FROM stdin;
\.


--
-- Data for Name: time_off_accrual_calc; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.time_off_accrual_calc (time_off_accrual_calc_id, benefit_config_id, accrual_method, trial_period, period_start, max_carry_over_hours, carry_over_percentage, first_active_date, last_active_date, accrual_type, period_start_date) FROM stdin;
\.


--
-- Data for Name: time_off_accrual_seniority; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.time_off_accrual_seniority (accrual_seniority_id, time_off_accrual_calc_id, years_of_service, hours_accrued) FROM stdin;
\.


--
-- Data for Name: time_off_request; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.time_off_request (request_id, requesting_person_id, project_id, start_date, start_time, return_date, return_time, request_status, request_date, approving_person_id, approval_date, requesting_comment, approval_comment) FROM stdin;
\.


--
-- Data for Name: time_reject; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.time_reject (time_reject_id, person_id, reject_date) FROM stdin;
\.


--
-- Data for Name: time_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.time_type (time_type_id, description, last_active_date, default_billable, default_type) FROM stdin;
\.


--
-- Data for Name: timesheet; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.timesheet (timesheet_id, person_id, entry_state, billable, message_id, invoice_line_item_id, description, beginning_entry_person_id, beginning_entry_date, beginning_date, beginning_time, beginning_tz_offset, end_entry_person_id, end_entry_date, end_date, end_time, end_tz_offset, total_hours, wage_type_id, private_description, total_expenses, fixed_pay, project_shift_id, time_type_id) FROM stdin;
\.


--
-- Data for Name: user_attribute; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_attribute (person_id, user_attribute, attribute_value) FROM stdin;
\.


--
-- Data for Name: vendor; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.vendor (org_group_id, account_number, dflt_expense_acct, dflt_ap_acct, company_id, interface_id) FROM stdin;
\.


--
-- Data for Name: vendor_contact; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.vendor_contact (person_id) FROM stdin;
\.


--
-- Data for Name: vendor_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.vendor_group (vendor_group_id, vendor_id, org_group_id, group_vendor_id) FROM stdin;
\.


--
-- Data for Name: wage_paid; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.wage_paid (wage_paid_id, employee_id, beg_period, end_period, date_paid, payment_method, check_number) FROM stdin;
\.


--
-- Data for Name: wage_paid_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.wage_paid_detail (wage_detail_id, wage_paid_id, wage_type_id, wage_amount, wage_base) FROM stdin;
\.


--
-- Data for Name: wage_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.wage_type (wage_type_id, org_group_id, wage_name, period_type, wage_type, expense_account, first_active_date, last_active_date, is_deduction, liability_account, wage_code) FROM stdin;
\.


--
-- Data for Name: wizard_config_benefit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.wizard_config_benefit (wizard_config_ben_id, wizard_config_cat_id, seqno, benefit_id, screen_id, benefit_name, avatar_path, avatar_location, instructions, decline_message) FROM stdin;
\.


--
-- Data for Name: wizard_config_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.wizard_config_category (wizard_config_cat_id, wizard_configuration_id, seqno, benefit_cat_id, screen_id, description, avatar_path, avatar_location, instructions) FROM stdin;
\.


--
-- Data for Name: wizard_config_config; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.wizard_config_config (wizard_config_conf_id, wizard_config_ben_id, seqno, benefit_config_id, config_name) FROM stdin;
\.


--
-- Data for Name: wizard_config_project_a; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.wizard_config_project_a (wizard_config_project_a_id, wizard_configuration_id, person_id) FROM stdin;
\.


--
-- Data for Name: wizard_configuration; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.wizard_configuration (wizard_configuration_id, config_name, description, wizard_type, fullscreen, show_inapplicable, allow_inapplicable, remember_state, progress_pane_buttons, skip_presentation, use_tool_tips, show_demographics, show_dependents, company_id, benefit_class_id, project_summary, project_status_id, project_category_id, project_type_id, wizard_version, lock_on_finalize, hr_contact_id, physician_selection_mode, demographic_screen_id, demographic_instructions, review_report, benefit_report, auto_approve_declines, payment_info, welcome_avatar, qualifying_event_avatar, demographics_avatar, dependents_avatar, review_avatar, finish_avatar, enable_avatars, show_annual_cost, show_monthly_cost, show_ppp_cost, show_qualifying_event, allow_demographic_changes, hde_type, hde_period, hde_days_before, hde_days_after, allow_report_from_review) FROM stdin;
\.


--
-- Data for Name: wizard_project; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.wizard_project (wizard_project_id, project_id, project_action, benefit_join_id, completed, date_completed, person_completed, hr_benefit_join_h_id) FROM stdin;
\.


--
-- Data for Name: worker_confirmation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.worker_confirmation (worker_confirmation_id, person_id, confirmation_time, latitude, longitude, distance, location, minutes, project_shift_id, who_added, notes) FROM stdin;
\.


--
-- Data for Name: zipcode_location; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.zipcode_location (zipcode, state_abr, city, state, latitude, longitude) FROM stdin;
\.


--
-- Data for Name: zipcode_lookup; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.zipcode_lookup (zipcode, city, county, state, zipcode_id) FROM stdin;
\.


--
-- Name: change_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.change_log_id_seq', 1, false);


--
-- Name: screen_history_transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.screen_history_transaction_id_seq', 1, false);


--
-- Name: address address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (address_id);


--
-- Name: address_cr address_rcr_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address_cr
    ADD CONSTRAINT address_rcr_pkey PRIMARY KEY (change_request_id);


--
-- Name: employee adp_oid_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT adp_oid_unique UNIQUE (adp_id);


--
-- Name: CONSTRAINT adp_oid_unique ON employee; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT adp_oid_unique ON public.employee IS 'ADP associateOID field';


--
-- Name: agency_join agency_join_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agency_join
    ADD CONSTRAINT agency_join_pkey PRIMARY KEY (agency_join_id);


--
-- Name: agency agency_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agency
    ADD CONSTRAINT agency_pkey PRIMARY KEY (agency_id);


--
-- Name: agent_join agent_join_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agent_join
    ADD CONSTRAINT agent_join_pkey PRIMARY KEY (agent_join_id);


--
-- Name: agent agent_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agent
    ADD CONSTRAINT agent_pkey PRIMARY KEY (agent_id);


--
-- Name: agreement_form agreement_form_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agreement_form
    ADD CONSTRAINT agreement_form_pkey PRIMARY KEY (agreement_form_id);


--
-- Name: agreement_person_join agreement_person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agreement_person_join
    ADD CONSTRAINT agreement_person_pkey PRIMARY KEY (agreement_person_join_id);


--
-- Name: alert_person_join alert_person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.alert_person_join
    ADD CONSTRAINT alert_person_pkey PRIMARY KEY (alert_id, person_id);


--
-- Name: alert alert_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.alert
    ADD CONSTRAINT alert_pkey PRIMARY KEY (alert_id);


--
-- Name: applicant_question_choice app_ques_choice_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_question_choice
    ADD CONSTRAINT app_ques_choice_pkey PRIMARY KEY (applicant_question_choice_id);


--
-- Name: applicant_answer applicant_answer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_answer
    ADD CONSTRAINT applicant_answer_pkey PRIMARY KEY (applicant_answer_id);


--
-- Name: applicant_app_status applicant_app_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_app_status
    ADD CONSTRAINT applicant_app_status_pkey PRIMARY KEY (applicant_app_status_id);


--
-- Name: applicant_application applicant_application_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_application
    ADD CONSTRAINT applicant_application_pkey PRIMARY KEY (applicant_application_id);


--
-- Name: applicant_contact applicant_contact_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_contact
    ADD CONSTRAINT applicant_contact_pkey PRIMARY KEY (applicant_contact_id);


--
-- Name: applicant applicant_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant
    ADD CONSTRAINT applicant_pkey PRIMARY KEY (person_id);


--
-- Name: applicant_position applicant_position_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_position
    ADD CONSTRAINT applicant_position_pkey PRIMARY KEY (applicant_position_id);


--
-- Name: applicant_question applicant_question_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_question
    ADD CONSTRAINT applicant_question_pkey PRIMARY KEY (applicant_question_id);


--
-- Name: applicant_source applicant_source_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_source
    ADD CONSTRAINT applicant_source_pkey PRIMARY KEY (applicant_source_id);


--
-- Name: applicant_status applicant_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_status
    ADD CONSTRAINT applicant_status_pkey PRIMARY KEY (applicant_status_id);


--
-- Name: assembly_template assemb_tem_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assembly_template
    ADD CONSTRAINT assemb_tem_pkey PRIMARY KEY (assembly_template_id);


--
-- Name: assembly_template_detail assembly_template_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assembly_template_detail
    ADD CONSTRAINT assembly_template_pkey PRIMARY KEY (assembly_template_detail_id);


--
-- Name: authenticated_senders authenticated_senders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authenticated_senders
    ADD CONSTRAINT authenticated_senders_pkey PRIMARY KEY (auth_send_id);


--
-- Name: bank_account bank_account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT bank_account_pkey PRIMARY KEY (bank_account_id);


--
-- Name: bank_draft_batch bank_draft_batch_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bank_draft_batch
    ADD CONSTRAINT bank_draft_batch_pkey PRIMARY KEY (bank_draft_id);


--
-- Name: bank_draft_detail bank_draft_det_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bank_draft_detail
    ADD CONSTRAINT bank_draft_det_pkey PRIMARY KEY (bank_draft_id, person_id);


--
-- Name: bank_draft_history bank_draft_hist_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bank_draft_history
    ADD CONSTRAINT bank_draft_hist_pkey PRIMARY KEY (bank_draft_history_id);


--
-- Name: benefit_change_reason_doc bcr_document_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_change_reason_doc
    ADD CONSTRAINT bcr_document_pkey PRIMARY KEY (bcr_document_id);


--
-- Name: benefit_config_cost_age ben_con_soage_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_config_cost_age
    ADD CONSTRAINT ben_con_soage_pkey PRIMARY KEY (benefit_config_cost_age_id);


--
-- Name: benefit_config_cost_status ben_concost_st_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_config_cost_status
    ADD CONSTRAINT ben_concost_st_pkey PRIMARY KEY (benefit_config_cost_status_id);


--
-- Name: benefit_config_cost ben_config_cost_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_config_cost
    ADD CONSTRAINT ben_config_cost_pkey PRIMARY KEY (benefit_config_cost_id);


--
-- Name: benefit_question_choice ben_ques_choice_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_question_choice
    ADD CONSTRAINT ben_ques_choice_pkey PRIMARY KEY (benefit_question_choice_id);


--
-- Name: benefit_group_class_join bencon_clsjn_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_group_class_join
    ADD CONSTRAINT bencon_clsjn_pkey PRIMARY KEY (benefit_class_id, benefit_id);


--
-- Name: benefit_answer_h benefit_answer_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_answer_h
    ADD CONSTRAINT benefit_answer_h_pkey PRIMARY KEY (history_id);


--
-- Name: benefit_answer benefit_answer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_answer
    ADD CONSTRAINT benefit_answer_pkey PRIMARY KEY (benefit_answer_id);


--
-- Name: benefit_class_join benefit_class_join_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_class_join
    ADD CONSTRAINT benefit_class_join_pkey PRIMARY KEY (benefit_class_id, benefit_config_id);


--
-- Name: benefit_dependency benefit_dependency_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_dependency
    ADD CONSTRAINT benefit_dependency_pkey PRIMARY KEY (benefit_dependency_id);


--
-- Name: benefit_document benefit_document_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_document
    ADD CONSTRAINT benefit_document_pkey PRIMARY KEY (benefit_document_id);


--
-- Name: benefit_question_h benefit_question_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_question_h
    ADD CONSTRAINT benefit_question_h_pkey PRIMARY KEY (history_id);


--
-- Name: benefit_question benefit_question_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_question
    ADD CONSTRAINT benefit_question_pkey PRIMARY KEY (benefit_question_id);


--
-- Name: benefit_restriction benefit_restriction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_restriction
    ADD CONSTRAINT benefit_restriction_pkey PRIMARY KEY (benefit_restriction_id);


--
-- Name: company_detail ccompany_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_detail
    ADD CONSTRAINT ccompany_detail_pkey PRIMARY KEY (org_group_id);


--
-- Name: change_log change_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.change_log
    ADD CONSTRAINT change_log_pkey PRIMARY KEY (change_id);


--
-- Name: client client_company_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_company_pkey PRIMARY KEY (org_group_id);


--
-- Name: client_item_inventory client_item_inventory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_item_inventory
    ADD CONSTRAINT client_item_inventory_pkey PRIMARY KEY (client_item_inventory_id);


--
-- Name: client_contact client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_contact
    ADD CONSTRAINT client_pkey PRIMARY KEY (person_id);


--
-- Name: client_status client_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_status
    ADD CONSTRAINT client_status_pkey PRIMARY KEY (client_status_id);


--
-- Name: company_form_folder_join comp_form_folder_jpkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_form_folder_join
    ADD CONSTRAINT comp_form_folder_jpkey PRIMARY KEY (form_id, folder_id);


--
-- Name: company_base company_base_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_base
    ADD CONSTRAINT company_base_pkey PRIMARY KEY (org_group_id);


--
-- Name: company_form_org_join company_form_org_join_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_form_org_join
    ADD CONSTRAINT company_form_org_join_pkey PRIMARY KEY (folder_id, org_group_id);


--
-- Name: company_form company_form_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_form
    ADD CONSTRAINT company_form_pkey PRIMARY KEY (company_form_id);


--
-- Name: delivery_detail delivery_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.delivery_detail
    ADD CONSTRAINT delivery_detail_pkey PRIMARY KEY (delivery_detail_id);


--
-- Name: delivery delivery_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.delivery
    ADD CONSTRAINT delivery_pkey PRIMARY KEY (delivery_id);


--
-- Name: drc_form_event drc_form_event_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_form_event
    ADD CONSTRAINT drc_form_event_pkey PRIMARY KEY (form_event_id);


--
-- Name: drc_import_benefit drc_imp_ben_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_import_benefit
    ADD CONSTRAINT drc_imp_ben_pkey PRIMARY KEY (imported_benefit_id);


--
-- Name: drc_import_benefit_join_h drc_imp_benjn_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_import_benefit_join_h
    ADD CONSTRAINT drc_imp_benjn_h_pkey PRIMARY KEY (history_id);


--
-- Name: drc_import_enrollee drc_imp_enr_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_import_enrollee
    ADD CONSTRAINT drc_imp_enr_pkey PRIMARY KEY (enrollee_id);


--
-- Name: drc_import_benefit_join drc_import_benjoin_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_import_benefit_join
    ADD CONSTRAINT drc_import_benjoin_pkey PRIMARY KEY (import_benefit_join_id);


--
-- Name: drc_import_enrollee_h drc_import_enrollee_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_import_enrollee_h
    ADD CONSTRAINT drc_import_enrollee_h_pkey PRIMARY KEY (history_id);


--
-- Name: dynace_mutex dynace_mutex_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dynace_mutex
    ADD CONSTRAINT dynace_mutex_pkey PRIMARY KEY (tag);


--
-- Name: e_signature e_signature_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.e_signature
    ADD CONSTRAINT e_signature_pkey PRIMARY KEY (e_signature_id);


--
-- Name: edi_transaction edi_transaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.edi_transaction
    ADD CONSTRAINT edi_transaction_pkey PRIMARY KEY (edi_transaction_id);


--
-- Name: education education_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.education
    ADD CONSTRAINT education_pkey PRIMARY KEY (education_id);


--
-- Name: hr_eeo_race eeo_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_eeo_race
    ADD CONSTRAINT eeo_id_pkey PRIMARY KEY (eeo_id);


--
-- Name: electronic_fund_transfer eft_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.electronic_fund_transfer
    ADD CONSTRAINT eft_pkey PRIMARY KEY (eft_id);


--
-- Name: email_notifications email_notifications_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email_notifications
    ADD CONSTRAINT email_notifications_pk PRIMARY KEY (email_notification_id);


--
-- Name: person_changed employee_changed_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_changed
    ADD CONSTRAINT employee_changed_pkey PRIMARY KEY (interface_id, person_id);


--
-- Name: employee_label employee_label_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_label
    ADD CONSTRAINT employee_label_pkey PRIMARY KEY (employee_label_id);


--
-- Name: employee_label_association employee_lbl_ass_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_label_association
    ADD CONSTRAINT employee_lbl_ass_pkey PRIMARY KEY (employee_id, employee_label_id);


--
-- Name: employee_not_available employee_not_available_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_not_available
    ADD CONSTRAINT employee_not_available_pkey PRIMARY KEY (employee_not_available_id);


--
-- Name: employee employee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (person_id);


--
-- Name: employee_rate employee_rate_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_rate
    ADD CONSTRAINT employee_rate_pkey PRIMARY KEY (employee_rate_id);


--
-- Name: expense_account expense_account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense_account
    ADD CONSTRAINT expense_account_pkey PRIMARY KEY (expense_account_id);


--
-- Name: expense expense_pki; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense
    ADD CONSTRAINT expense_pki PRIMARY KEY (expense_id);


--
-- Name: expense_receipt expense_receipt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense_receipt
    ADD CONSTRAINT expense_receipt_pkey PRIMARY KEY (expense_receipt_id);


--
-- Name: company_form_folder form_folder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_form_folder
    ADD CONSTRAINT form_folder_pkey PRIMARY KEY (folder_id);


--
-- Name: form_type form_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.form_type
    ADD CONSTRAINT form_type_pkey PRIMARY KEY (form_type_id);


--
-- Name: garnishment garnishment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.garnishment
    ADD CONSTRAINT garnishment_pkey PRIMARY KEY (garnishment_id);


--
-- Name: garnishment_type garnishment_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.garnishment_type
    ADD CONSTRAINT garnishment_type_pkey PRIMARY KEY (garnishment_type_id);


--
-- Name: gl_account gl_accounts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gl_account
    ADD CONSTRAINT gl_accounts_pkey PRIMARY KEY (gl_account_id);


--
-- Name: org_group_association group_association_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group_association
    ADD CONSTRAINT group_association_pkey PRIMARY KEY (person_id, org_group_id);


--
-- Name: org_group_hierarchy group_hierarchy_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group_hierarchy
    ADD CONSTRAINT group_hierarchy_pkey PRIMARY KEY (parent_group_id, child_group_id);


--
-- Name: org_group group_type_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group
    ADD CONSTRAINT group_type_unique UNIQUE (org_group_id, org_group_type);


--
-- Name: org_group groups_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group
    ADD CONSTRAINT groups_pkey PRIMARY KEY (org_group_id);


--
-- Name: holiday holiday_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.holiday
    ADD CONSTRAINT holiday_pkey PRIMARY KEY (holiday_id);


--
-- Name: hr_accrual hr_accrual_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_accrual
    ADD CONSTRAINT hr_accrual_pkey PRIMARY KEY (accrual_id);


--
-- Name: hr_benefit_change_reason hr_bcr_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_change_reason
    ADD CONSTRAINT hr_bcr_pkey PRIMARY KEY (bcr_id);


--
-- Name: hr_benefit_category_answer hr_ben_cat_answer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_category_answer
    ADD CONSTRAINT hr_ben_cat_answer_pkey PRIMARY KEY (answer_id);


--
-- Name: hr_benefit_category hr_ben_cat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_category
    ADD CONSTRAINT hr_ben_cat_pkey PRIMARY KEY (benefit_cat_id);


--
-- Name: hr_benefit_category_question hr_ben_cat_ques_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_category_question
    ADD CONSTRAINT hr_ben_cat_ques_pkey PRIMARY KEY (question_id);


--
-- Name: hr_benefit_class hr_ben_class_pky; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_class
    ADD CONSTRAINT hr_ben_class_pky PRIMARY KEY (benefit_class_id);


--
-- Name: hr_benefit_join_h hr_ben_join_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join_h
    ADD CONSTRAINT hr_ben_join_h_pkey PRIMARY KEY (history_id);


--
-- Name: hr_benefit_config hr_benconf_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_config
    ADD CONSTRAINT hr_benconf_pkey PRIMARY KEY (benefit_config_id);


--
-- Name: hr_employee_beneficiary hr_beneficiary_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_beneficiary
    ADD CONSTRAINT hr_beneficiary_pkey PRIMARY KEY (beneficiary_id);


--
-- Name: hr_benefit_join hr_benefit_join_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join
    ADD CONSTRAINT hr_benefit_join_pkey PRIMARY KEY (benefit_join_id);


--
-- Name: hr_benefit_package_join hr_benefit_pj_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_package_join
    ADD CONSTRAINT hr_benefit_pj_pkey PRIMARY KEY (package_id, benefit_id);


--
-- Name: hr_benefit hr_benefit_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit
    ADD CONSTRAINT hr_benefit_pkey PRIMARY KEY (benefit_id);


--
-- Name: hr_benefit_rider hr_benefit_rider_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_rider
    ADD CONSTRAINT hr_benefit_rider_pkey PRIMARY KEY (benefit_rider_id);


--
-- Name: hr_billing_status_history hr_bill_stat_hist_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_billing_status_history
    ADD CONSTRAINT hr_bill_stat_hist_pkey PRIMARY KEY (billing_status_hist_id);


--
-- Name: hr_billing_status hr_billing_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_billing_status
    ADD CONSTRAINT hr_billing_status_pkey PRIMARY KEY (billing_status_id);


--
-- Name: hr_checklist_detail hr_checklist_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_checklist_detail
    ADD CONSTRAINT hr_checklist_detail_pkey PRIMARY KEY (checklist_detail_id);


--
-- Name: hr_checklist_item hr_checklist_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_checklist_item
    ADD CONSTRAINT hr_checklist_item_pkey PRIMARY KEY (item_id);


--
-- Name: hr_eeo1 hr_eeo1_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_eeo1
    ADD CONSTRAINT hr_eeo1_pkey PRIMARY KEY (eeo1_id);


--
-- Name: hr_eeo_category hr_eeo_category_pky; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_eeo_category
    ADD CONSTRAINT hr_eeo_category_pky PRIMARY KEY (eeo_category_id);


--
-- Name: hr_employee_beneficiary_h hr_emp_bene_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_beneficiary_h
    ADD CONSTRAINT hr_emp_bene_h_pkey PRIMARY KEY (history_id);


--
-- Name: hr_empl_dependent hr_empl_dep_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_dependent
    ADD CONSTRAINT hr_empl_dep_pkey PRIMARY KEY (relationship_id);


--
-- Name: hr_empl_dependent_cr hr_empl_dep_rcr_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_dependent_cr
    ADD CONSTRAINT hr_empl_dep_rcr_pkey PRIMARY KEY (change_request_id);


--
-- Name: hr_empl_eval_detail hr_empl_eval_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_eval_detail
    ADD CONSTRAINT hr_empl_eval_detail_pkey PRIMARY KEY (detail_id);


--
-- Name: hr_employee_eval hr_employee_eval_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_eval
    ADD CONSTRAINT hr_employee_eval_id PRIMARY KEY (employee_eval_id);


--
-- Name: hr_employee_event hr_employee_evenet_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_event
    ADD CONSTRAINT hr_employee_evenet_pkey PRIMARY KEY (event_id);


--
-- Name: hr_employee_status hr_employee_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_status
    ADD CONSTRAINT hr_employee_status_pkey PRIMARY KEY (status_id);


--
-- Name: hr_emergency_contact hr_emrcont_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_emergency_contact
    ADD CONSTRAINT hr_emrcont_pkey PRIMARY KEY (contact_id);


--
-- Name: hr_eval_category hr_eval_cat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_eval_category
    ADD CONSTRAINT hr_eval_cat_pkey PRIMARY KEY (eval_cat_id);


--
-- Name: hr_note_category hr_note_cat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_note_category
    ADD CONSTRAINT hr_note_cat_pkey PRIMARY KEY (cat_id);


--
-- Name: hr_benefit_package hr_package_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_package
    ADD CONSTRAINT hr_package_pkey PRIMARY KEY (package_id);


--
-- Name: hr_position hr_position_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_position
    ADD CONSTRAINT hr_position_pkey PRIMARY KEY (position_id);


--
-- Name: hr_empl_status_history hr_status_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_status_history
    ADD CONSTRAINT hr_status_history_pkey PRIMARY KEY (status_hist_id);


--
-- Name: hr_training_category hr_training_cat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_training_category
    ADD CONSTRAINT hr_training_cat_pkey PRIMARY KEY (cat_id);


--
-- Name: hr_training_detail hr_training_detail_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_training_detail
    ADD CONSTRAINT hr_training_detail_id_pkey PRIMARY KEY (training_id);


--
-- Name: hr_wage hr_wage_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_wage
    ADD CONSTRAINT hr_wage_pkey PRIMARY KEY (wage_id);


--
-- Name: import_column import_column_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.import_column
    ADD CONSTRAINT import_column_pkey PRIMARY KEY (import_column_id);


--
-- Name: import_filter import_filter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.import_filter
    ADD CONSTRAINT import_filter_pkey PRIMARY KEY (import_filter_id);


--
-- Name: import_type import_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.import_type
    ADD CONSTRAINT import_type_pkey PRIMARY KEY (import_type_id);


--
-- Name: insurance_location_code insurance_location_code_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.insurance_location_code
    ADD CONSTRAINT insurance_location_code_pkey PRIMARY KEY (insurance_location_code_id);


--
-- Name: interface_log interface_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.interface_log
    ADD CONSTRAINT interface_log_pkey PRIMARY KEY (interface_log_id);


--
-- Name: item inventory_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT inventory_detail_pkey PRIMARY KEY (item_id);


--
-- Name: inventory inventory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventory
    ADD CONSTRAINT inventory_pkey PRIMARY KEY (inventory_id);


--
-- Name: invoice_line_item invoice_line_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_line_item
    ADD CONSTRAINT invoice_line_item_pkey PRIMARY KEY (invoice_line_item_id);


--
-- Name: item_h item_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_h
    ADD CONSTRAINT item_h_pkey PRIMARY KEY (history_id);


--
-- Name: item_inspection item_inspection_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_inspection
    ADD CONSTRAINT item_inspection_pkey PRIMARY KEY (item_inspection_id);


--
-- Name: life_event life_event_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.life_event
    ADD CONSTRAINT life_event_pkey PRIMARY KEY (life_event_id);


--
-- Name: location_cost location_cost_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_cost
    ADD CONSTRAINT location_cost_pkey PRIMARY KEY (location_cost_id);


--
-- Name: appointment_location location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment_location
    ADD CONSTRAINT location_pkey PRIMARY KEY (location_id);


--
-- Name: prophet_login_exception login_exception_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prophet_login_exception
    ADD CONSTRAINT login_exception_pkey PRIMARY KEY (login_exception_id);


--
-- Name: login_log login_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_log
    ADD CONSTRAINT login_log_pkey PRIMARY KEY (ltime, log_name);


--
-- Name: prophet_login login_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prophet_login
    ADD CONSTRAINT login_pkey PRIMARY KEY (person_id);


--
-- Name: lot lot_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lot
    ADD CONSTRAINT lot_pkey PRIMARY KEY (lot_id);


--
-- Name: message_attachment message_attachment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_attachment
    ADD CONSTRAINT message_attachment_pkey PRIMARY KEY (message_attachment_id);


--
-- Name: message_to message_to_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_to
    ADD CONSTRAINT message_to_pkey PRIMARY KEY (message_to_id);


--
-- Name: onboarding_task_complete ob_task_complete_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.onboarding_task_complete
    ADD CONSTRAINT ob_task_complete_pkey PRIMARY KEY (task_complete_id);


--
-- Name: onboarding_config onboarding_config_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.onboarding_config
    ADD CONSTRAINT onboarding_config_pkey PRIMARY KEY (onboarding_config_id);


--
-- Name: onboarding_task onboarding_task_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.onboarding_task
    ADD CONSTRAINT onboarding_task_pkey PRIMARY KEY (onboarding_task_id);


--
-- Name: org_group_association_h org_grpass_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group_association_h
    ADD CONSTRAINT org_grpass_h_pkey PRIMARY KEY (history_id);


--
-- Name: overtime_approval overtime_approval_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.overtime_approval
    ADD CONSTRAINT overtime_approval_pkey PRIMARY KEY (overtime_approval_id);


--
-- Name: password_history password_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_history
    ADD CONSTRAINT password_history_pkey PRIMARY KEY (password_history_id);


--
-- Name: pay_schedule_period pay_sch_period_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pay_schedule_period
    ADD CONSTRAINT pay_sch_period_pkey PRIMARY KEY (pay_period_id);


--
-- Name: pay_schedule pay_schedule_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pay_schedule
    ADD CONSTRAINT pay_schedule_pkey PRIMARY KEY (pay_schedule_id);


--
-- Name: payment_info payment_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_info
    ADD CONSTRAINT payment_info_pkey PRIMARY KEY (payment_id);


--
-- Name: per_diem_exception per_diem_exception_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.per_diem_exception
    ADD CONSTRAINT per_diem_exception_pkey PRIMARY KEY (per_diem_exception_id);


--
-- Name: person_change_request person_chg_req_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_change_request
    ADD CONSTRAINT person_chg_req_pkey PRIMARY KEY (request_id);


--
-- Name: person_email person_email_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_email
    ADD CONSTRAINT person_email_pkey PRIMARY KEY (person_email_id);


--
-- Name: person_form person_form_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_form
    ADD CONSTRAINT person_form_pkey PRIMARY KEY (person_form_id);


--
-- Name: person_h person_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_h
    ADD CONSTRAINT person_h_pkey PRIMARY KEY (history_id);


--
-- Name: person_note person_note_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_note
    ADD CONSTRAINT person_note_pkey PRIMARY KEY (note_id);


--
-- Name: person person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (person_id);


--
-- Name: person_cr person_rcr_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_cr
    ADD CONSTRAINT person_rcr_pkey PRIMARY KEY (change_request_id);


--
-- Name: person person_unique_type; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_unique_type UNIQUE (person_id, org_group_type);


--
-- Name: phone phone_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phone
    ADD CONSTRAINT phone_pkey PRIMARY KEY (phone_id);


--
-- Name: phone_cr phone_rcr_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phone_cr
    ADD CONSTRAINT phone_rcr_pkey PRIMARY KEY (change_request_id);


--
-- Name: physician physicians_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.physician
    ADD CONSTRAINT physicians_pkey PRIMARY KEY (physician_id);


--
-- Name: message pkey_messages; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT pkey_messages PRIMARY KEY (message_id);


--
-- Name: previous_employment previous_employment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.previous_employment
    ADD CONSTRAINT previous_employment_pkey PRIMARY KEY (employment_id);


--
-- Name: process_history process_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.process_history
    ADD CONSTRAINT process_history_pkey PRIMARY KEY (process_history_id);


--
-- Name: process_schedule process_schedule_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.process_schedule
    ADD CONSTRAINT process_schedule_pkey PRIMARY KEY (process_schedule_id);


--
-- Name: product_attribute_dependency product_att_depend_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_attribute_dependency
    ADD CONSTRAINT product_att_depend_pkey PRIMARY KEY (attribute_dependency_id);


--
-- Name: product_attribute_choice product_attribute_choice_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_attribute_choice
    ADD CONSTRAINT product_attribute_choice_pkey PRIMARY KEY (product_attribute_choice_id);


--
-- Name: product_attribute product_attribute_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_attribute
    ADD CONSTRAINT product_attribute_pkey PRIMARY KEY (product_attribute_id);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (product_id);


--
-- Name: product_type product_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_type
    ADD CONSTRAINT product_type_pkey PRIMARY KEY (product_type_id);


--
-- Name: product_service products_services_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_service
    ADD CONSTRAINT products_services_pkey PRIMARY KEY (product_service_id);


--
-- Name: project_category project_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_category
    ADD CONSTRAINT project_category_pkey PRIMARY KEY (project_category_id);


--
-- Name: project_checklist_detail project_chklst_det_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_checklist_detail
    ADD CONSTRAINT project_chklst_det_pkey PRIMARY KEY (project_checklist_detail_id);


--
-- Name: project_comment project_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_comment
    ADD CONSTRAINT project_comment_pkey PRIMARY KEY (comment_id);


--
-- Name: project_employee_history project_employee_history_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_employee_history
    ADD CONSTRAINT project_employee_history_id_pk PRIMARY KEY (project_employee_history_id);


--
-- Name: project_form project_form_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_form
    ADD CONSTRAINT project_form_pkey PRIMARY KEY (project_form_id);


--
-- Name: project_inventory_email project_inventory_email_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_inventory_email
    ADD CONSTRAINT project_inventory_email_pk PRIMARY KEY (pie_id);


--
-- Name: project_employee_join project_person_join_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_employee_join
    ADD CONSTRAINT project_person_join_pkey PRIMARY KEY (project_employee_join_id);


--
-- Name: project_phase project_phase_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_phase
    ADD CONSTRAINT project_phase_pkey PRIMARY KEY (project_phase_id);


--
-- Name: project project_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_pkey PRIMARY KEY (project_id);


--
-- Name: project_po_detail project_po_det_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_po_detail
    ADD CONSTRAINT project_po_det_pkey PRIMARY KEY (project_po_detail_id);


--
-- Name: project_purchase_order project_purchase_order_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_purchase_order
    ADD CONSTRAINT project_purchase_order_pkey PRIMARY KEY (project_po_id);


--
-- Name: project_report_person project_report_person_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_report_person
    ADD CONSTRAINT project_report_person_pk PRIMARY KEY (person_id, client_id);


--
-- Name: project_shift project_shift_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_shift
    ADD CONSTRAINT project_shift_pkey PRIMARY KEY (project_shift_id);


--
-- Name: project_status project_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_status
    ADD CONSTRAINT project_status_pkey PRIMARY KEY (project_status_id);


--
-- Name: project_subtype project_subtype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_subtype
    ADD CONSTRAINT project_subtype_pkey PRIMARY KEY (project_subtype_id);


--
-- Name: project_task_assignment project_task_assignment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_task_assignment
    ADD CONSTRAINT project_task_assignment_pkey PRIMARY KEY (project_task_assignment_id);


--
-- Name: project_task_detail project_task_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_task_detail
    ADD CONSTRAINT project_task_detail_pkey PRIMARY KEY (project_task_detail_id);


--
-- Name: project_task_picture project_task_picture_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_task_picture
    ADD CONSTRAINT project_task_picture_pkey PRIMARY KEY (project_task_picture_id);


--
-- Name: project_template_benefit_a project_template_benefit_a_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_template_benefit_a
    ADD CONSTRAINT project_template_benefit_a_pkey PRIMARY KEY (template_assignment_id);


--
-- Name: project_template_benefit project_template_benefit_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_template_benefit
    ADD CONSTRAINT project_template_benefit_pkey PRIMARY KEY (project_template_id);


--
-- Name: project_training project_training_pki; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_training
    ADD CONSTRAINT project_training_pki PRIMARY KEY (project_training_id);


--
-- Name: project_type project_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_type
    ADD CONSTRAINT project_type_pkey PRIMARY KEY (project_type_id);


--
-- Name: project_view_join project_view_join_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_view_join
    ADD CONSTRAINT project_view_join_pkey PRIMARY KEY (project_view_join_id);


--
-- Name: project_view project_view_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_view
    ADD CONSTRAINT project_view_pkey PRIMARY KEY (project_view_id);


--
-- Name: project_status_rs_join projstat_rs_join_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_status_rs_join
    ADD CONSTRAINT projstat_rs_join_pk PRIMARY KEY (route_stop_id, project_status_id);


--
-- Name: property property_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property
    ADD CONSTRAINT property_pkey PRIMARY KEY (prop_name);


--
-- Name: appointment prospect_appointment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT prospect_appointment_pkey PRIMARY KEY (appointment_id);


--
-- Name: appointment_person_join prospect_appt_emp_join_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment_person_join
    ADD CONSTRAINT prospect_appt_emp_join_pkey PRIMARY KEY (join_id);


--
-- Name: company_question_detail prospect_comp_ques_det_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_question_detail
    ADD CONSTRAINT prospect_comp_ques_det_pkey PRIMARY KEY (company_ques_det_id);


--
-- Name: company_question prospect_compques_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_question
    ADD CONSTRAINT prospect_compques_pkey PRIMARY KEY (company_ques_id);


--
-- Name: contact_question_detail prospect_cont_ques_det_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contact_question_detail
    ADD CONSTRAINT prospect_cont_ques_det_pkey PRIMARY KEY (contact_question_det_id);


--
-- Name: contact_question prospect_cont_ques_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contact_question
    ADD CONSTRAINT prospect_cont_ques_pkey PRIMARY KEY (contact_question_id);


--
-- Name: prospect_contact prospect_contact_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_contact
    ADD CONSTRAINT prospect_contact_pkey PRIMARY KEY (person_id);


--
-- Name: prospect_h prospect_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_h
    ADD CONSTRAINT prospect_h_pkey PRIMARY KEY (history_id);


--
-- Name: prospect_log prospect_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_log
    ADD CONSTRAINT prospect_log_pkey PRIMARY KEY (prospect_log_id);


--
-- Name: prospect prospect_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT prospect_pkey PRIMARY KEY (org_group_id);


--
-- Name: prospect_source prospect_source_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_source
    ADD CONSTRAINT prospect_source_pkey PRIMARY KEY (prospect_source_id);


--
-- Name: prospect_status prospect_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_status
    ADD CONSTRAINT prospect_status_pkey PRIMARY KEY (prospect_status_id);


--
-- Name: prospect_type prospect_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_type
    ADD CONSTRAINT prospect_type_pkey PRIMARY KEY (prospect_type_id);


--
-- Name: project_view_join pvj_unique_chk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_view_join
    ADD CONSTRAINT pvj_unique_chk UNIQUE (parent_project_view_id, child_project_view_id);


--
-- Name: quickbooks_change quickbooks_change_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quickbooks_change
    ADD CONSTRAINT quickbooks_change_pkey PRIMARY KEY (record_id);


--
-- Name: quickbooks_sync quickbooks_sync_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quickbooks_sync
    ADD CONSTRAINT quickbooks_sync_pkey PRIMARY KEY (sync_id);


--
-- Name: quote_adjustment quote_adjustment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_adjustment
    ADD CONSTRAINT quote_adjustment_pkey PRIMARY KEY (quote_adjustment_id);


--
-- Name: quote_table quote_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_table
    ADD CONSTRAINT quote_pkey PRIMARY KEY (quote_id);


--
-- Name: quote_product quote_product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_product
    ADD CONSTRAINT quote_product_pkey PRIMARY KEY (quote_product_id);


--
-- Name: quote_template quote_template_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_template
    ADD CONSTRAINT quote_template_pkey PRIMARY KEY (quote_template_id);


--
-- Name: quote_template_product quote_template_product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_template_product
    ADD CONSTRAINT quote_template_product_pkey PRIMARY KEY (quote_template_product_id);


--
-- Name: rate_type rate_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rate_type
    ADD CONSTRAINT rate_type_pkey PRIMARY KEY (rate_type_id);


--
-- Name: receipt_join receipt_join_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receipt_join
    ADD CONSTRAINT receipt_join_pkey PRIMARY KEY (receipt_join_id);


--
-- Name: receipt receipt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receipt
    ADD CONSTRAINT receipt_pkey PRIMARY KEY (receipt_id);


--
-- Name: recurring_schedule recurring_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recurring_schedule
    ADD CONSTRAINT recurring_pkey PRIMARY KEY (recurring_schedule_id);


--
-- Name: personal_reference reference_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personal_reference
    ADD CONSTRAINT reference_id_pkey PRIMARY KEY (reference_id);


--
-- Name: time_reject reject_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_reject
    ADD CONSTRAINT reject_id_pkey PRIMARY KEY (time_reject_id);


--
-- Name: reminder reminder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reminder
    ADD CONSTRAINT reminder_pkey PRIMARY KEY (reminder_id);


--
-- Name: removed_from_roster removed_from_roster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.removed_from_roster
    ADD CONSTRAINT removed_from_roster_pkey PRIMARY KEY (rfr_id);


--
-- Name: report_column report_column_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_column
    ADD CONSTRAINT report_column_pkey PRIMARY KEY (report_column_id);


--
-- Name: report_graphic report_graphic_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_graphic
    ADD CONSTRAINT report_graphic_pkey PRIMARY KEY (report_graphic_id);


--
-- Name: report report_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report
    ADD CONSTRAINT report_pkey PRIMARY KEY (report_id);


--
-- Name: report_selection report_selection_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_selection
    ADD CONSTRAINT report_selection_pkey PRIMARY KEY (report_selection_id);


--
-- Name: report_title report_title_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_title
    ADD CONSTRAINT report_title_pkey PRIMARY KEY (report_title_id);


--
-- Name: rights_association rights_association_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rights_association
    ADD CONSTRAINT rights_association_pkey PRIMARY KEY (security_group_id, right_id);


--
-- Name: security_token rights_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.security_token
    ADD CONSTRAINT rights_pkey PRIMARY KEY (right_id);


--
-- Name: project_history route_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_history
    ADD CONSTRAINT route_history_pkey PRIMARY KEY (project_history_id);


--
-- Name: route_path route_path_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_path
    ADD CONSTRAINT route_path_pkey PRIMARY KEY (route_path_id);


--
-- Name: route route_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route
    ADD CONSTRAINT route_pkey PRIMARY KEY (route_id);


--
-- Name: route_stop_checklist route_stop_cl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_stop_checklist
    ADD CONSTRAINT route_stop_cl_pkey PRIMARY KEY (route_stop_checklist_id);


--
-- Name: route_stop route_stop_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_stop
    ADD CONSTRAINT route_stop_pkey PRIMARY KEY (route_stop_id);


--
-- Name: route_type_assoc route_type_assoc_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_type_assoc
    ADD CONSTRAINT route_type_assoc_pkey PRIMARY KEY (route_assoc_id);


--
-- Name: sales_activity sales_activity_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_activity
    ADD CONSTRAINT sales_activity_pkey PRIMARY KEY (sales_activity_id);


--
-- Name: sales_activity_result sales_activity_result_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_activity_result
    ADD CONSTRAINT sales_activity_result_pkey PRIMARY KEY (sales_activity_result_id);


--
-- Name: sales_points sales_points_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_points
    ADD CONSTRAINT sales_points_pkey PRIMARY KEY (sales_points_id);


--
-- Name: screen_group_access screen_group_access_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_group_access
    ADD CONSTRAINT screen_group_access_pkey PRIMARY KEY (security_group_id, can_access_screen_group_id);


--
-- Name: screen_group_hierarchy screen_group_hierarchy_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_group_hierarchy
    ADD CONSTRAINT screen_group_hierarchy_pkey PRIMARY KEY (screen_group_hierarchy_id);


--
-- Name: screen_group screen_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_group
    ADD CONSTRAINT screen_group_pkey PRIMARY KEY (screen_group_id);


--
-- Name: screen_history screen_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_history
    ADD CONSTRAINT screen_history_pkey PRIMARY KEY (transaction_id);


--
-- Name: screen screens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen
    ADD CONSTRAINT screens_pkey PRIMARY KEY (screen_id);


--
-- Name: security_group_access security_group_access_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.security_group_access
    ADD CONSTRAINT security_group_access_pkey PRIMARY KEY (security_group_id, can_access_security_group_id);


--
-- Name: security_group_hierarchy security_group_hierarchy_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.security_group_hierarchy
    ADD CONSTRAINT security_group_hierarchy_pkey PRIMARY KEY (parent_security_group_id, child_security_group_id);


--
-- Name: security_group security_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.security_group
    ADD CONSTRAINT security_group_pkey PRIMARY KEY (security_group_id);


--
-- Name: service service_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT service_pkey PRIMARY KEY (service_id);


--
-- Name: service_subscribed_join service_sub_join_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_subscribed_join
    ADD CONSTRAINT service_sub_join_pkey PRIMARY KEY (service_join_id);


--
-- Name: service_subscribed service_subscribed_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_subscribed
    ADD CONSTRAINT service_subscribed_pkey PRIMARY KEY (service_id);


--
-- Name: spousal_insurance_verif spousal_verif_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.spousal_insurance_verif
    ADD CONSTRAINT spousal_verif_pkey PRIMARY KEY (spousal_ins_verif_id);


--
-- Name: standard_project standard_project_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standard_project
    ADD CONSTRAINT standard_project_pkey PRIMARY KEY (project_id);


--
-- Name: student_verification student_verif_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_verification
    ADD CONSTRAINT student_verif_pkey PRIMARY KEY (student_verification_id);


--
-- Name: time_off_request time_off_request_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_off_request
    ADD CONSTRAINT time_off_request_pkey PRIMARY KEY (request_id);


--
-- Name: time_type time_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_type
    ADD CONSTRAINT time_type_pkey PRIMARY KEY (time_type_id);


--
-- Name: time_off_accrual_calc timeoff_acc_calc_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_off_accrual_calc
    ADD CONSTRAINT timeoff_acc_calc_pkey PRIMARY KEY (time_off_accrual_calc_id);


--
-- Name: time_off_accrual_seniority timeoff_accsen_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_off_accrual_seniority
    ADD CONSTRAINT timeoff_accsen_pkey PRIMARY KEY (accrual_seniority_id);


--
-- Name: invoice timesheet_batch_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT timesheet_batch_pkey PRIMARY KEY (invoice_id);


--
-- Name: timesheet timesheet_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timesheet
    ADD CONSTRAINT timesheet_key PRIMARY KEY (timesheet_id);


--
-- Name: user_attribute user_attribute_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_attribute
    ADD CONSTRAINT user_attribute_pkey PRIMARY KEY (person_id, user_attribute);


--
-- Name: vendor vendor_company_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT vendor_company_pkey PRIMARY KEY (org_group_id);


--
-- Name: vendor_contact vendor_contact_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vendor_contact
    ADD CONSTRAINT vendor_contact_pkey PRIMARY KEY (person_id);


--
-- Name: vendor_group vendor_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vendor_group
    ADD CONSTRAINT vendor_group_pkey PRIMARY KEY (vendor_group_id);


--
-- Name: wage_paid_detail wage_paid_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wage_paid_detail
    ADD CONSTRAINT wage_paid_detail_pkey PRIMARY KEY (wage_detail_id);


--
-- Name: wage_paid wage_paid_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wage_paid
    ADD CONSTRAINT wage_paid_pkey PRIMARY KEY (wage_paid_id);


--
-- Name: wage_type wage_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wage_type
    ADD CONSTRAINT wage_type_pkey PRIMARY KEY (wage_type_id);


--
-- Name: wizard_config_benefit wizard_config_benefit_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_benefit
    ADD CONSTRAINT wizard_config_benefit_pkey PRIMARY KEY (wizard_config_ben_id);


--
-- Name: wizard_config_category wizard_config_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_category
    ADD CONSTRAINT wizard_config_category_pkey PRIMARY KEY (wizard_config_cat_id);


--
-- Name: wizard_config_config wizard_config_config_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_config
    ADD CONSTRAINT wizard_config_config_pkey PRIMARY KEY (wizard_config_conf_id);


--
-- Name: wizard_config_project_a wizard_config_project_a_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_project_a
    ADD CONSTRAINT wizard_config_project_a_pkey PRIMARY KEY (wizard_config_project_a_id);


--
-- Name: wizard_configuration wizard_configuration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_configuration
    ADD CONSTRAINT wizard_configuration_pkey PRIMARY KEY (wizard_configuration_id);


--
-- Name: wizard_project wizard_project_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_project
    ADD CONSTRAINT wizard_project_pkey PRIMARY KEY (wizard_project_id);


--
-- Name: worker_confirmation worker_confirmation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.worker_confirmation
    ADD CONSTRAINT worker_confirmation_pkey PRIMARY KEY (worker_confirmation_id);


--
-- Name: zipcode_location zipcode_location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zipcode_location
    ADD CONSTRAINT zipcode_location_pkey PRIMARY KEY (zipcode);


--
-- Name: zipcode_lookup zipcode_lookup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zipcode_lookup
    ADD CONSTRAINT zipcode_lookup_pkey PRIMARY KEY (zipcode_id);


--
-- Name: address_cr_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX address_cr_main_idx ON public.address_cr USING btree (real_record_id, change_status, request_time);


--
-- Name: agency_join_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX agency_join_pidx ON public.agency_join USING btree (company_id, agency_id);


--
-- Name: agent_join_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX agent_join_pidx ON public.agent_join USING btree (company_id, agent_id);


--
-- Name: agreement_form_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX agreement_form_name_idx ON public.agreement_form USING btree (description);


--
-- Name: alert_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX alert_main_idx ON public.alert USING btree (alert_distribution, last_date);


--
-- Name: app_ques_choice_item_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX app_ques_choice_item_idx ON public.applicant_question_choice USING btree (applicant_question_id, description);


--
-- Name: applicant_app_stat_order_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX applicant_app_stat_order_idx ON public.applicant_app_status USING btree (company_id NULLS FIRST, status_order NULLS FIRST);


--
-- Name: applicant_question_position_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX applicant_question_position_idx ON public.applicant_question USING btree (position_id, question_order);


--
-- Name: applicant_status_order_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX applicant_status_order_idx ON public.applicant_status USING btree (company_id NULLS FIRST, status_order NULLS FIRST);


--
-- Name: assemb_temp_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX assemb_temp_name_idx ON public.assembly_template USING btree (assembly_name);


--
-- Name: auth_send_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX auth_send_idx ON public.authenticated_senders USING btree (address_type, address);


--
-- Name: bank_account_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX bank_account_id_idx ON public.bank_account USING btree (bank_id);


--
-- Name: bank_draft_batch_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX bank_draft_batch_name_idx ON public.bank_draft_batch USING btree (name);


--
-- Name: bank_draft_hist_date_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bank_draft_hist_date_idx ON public.bank_draft_history USING btree (date_made);


--
-- Name: ben_ques_choice_item_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX ben_ques_choice_item_idx ON public.benefit_question_choice USING btree (benefit_question_id, description);


--
-- Name: benefit_ansh_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX benefit_ansh_main_idx ON public.benefit_answer_h USING btree (benefit_answer_id, record_change_date);


--
-- Name: benefit_ques_benefit_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX benefit_ques_benefit_idx ON public.benefit_question USING btree (benefit_id, question_order);


--
-- Name: benefit_quesh_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX benefit_quesh_main_idx ON public.benefit_question_h USING btree (benefit_question_id, record_change_date);


--
-- Name: change_log_table_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX change_log_table_idx ON public.change_log USING btree (table_changed, column_changed);


--
-- Name: client_contact_date_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX client_contact_date_idx ON public.client USING btree (last_contact_date);


--
-- Name: client_status_seq_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX client_status_seq_idx ON public.client_status USING btree (company_id NULLS FIRST, seqno);


--
-- Name: company_question_seq_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX company_question_seq_idx ON public.company_question USING btree (company_id NULLS FIRST, seqno NULLS FIRST);


--
-- Name: contact_question_seq_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX contact_question_seq_idx ON public.contact_question USING btree (company_id NULLS FIRST, seqno NULLS FIRST);


--
-- Name: dynace_sequence_pkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX dynace_sequence_pkey ON public.dynace_sequence USING btree (lower((tag)::text));


--
-- Name: edi_trans_gcn_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX edi_trans_gcn_idx ON public.edi_transaction USING btree (org_group_id, gcn);


--
-- Name: edi_trans_icn_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX edi_trans_icn_idx ON public.edi_transaction USING btree (org_group_id, icn);


--
-- Name: edi_trans_tscn_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX edi_trans_tscn_idx ON public.edi_transaction USING btree (org_group_id, tscn);


--
-- Name: education_main_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX education_main_index ON public.education USING btree (person_id, start_date);


--
-- Name: eft_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX eft_person_idx ON public.electronic_fund_transfer USING btree (person_id, seqno);


--
-- Name: employee_ext_ref_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX employee_ext_ref_idx ON public.employee USING btree (ext_ref);


--
-- Name: employee_lbl_ass_lbl_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX employee_lbl_ass_lbl_idx ON public.employee_label_association USING btree (completed, employee_label_id);


--
-- Name: employee_rate_type_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX employee_rate_type_idx ON public.employee_rate USING btree (rate_type_id, person_id);


--
-- Name: ena_employee_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX ena_employee_idx ON public.employee_not_available USING btree (employee_id, start_date);


--
-- Name: expense_apers_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX expense_apers_idx ON public.expense USING btree (auth_person_id);


--
-- Name: expense_employee_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX expense_employee_idx ON public.expense USING btree (employee_id, date_paid);


--
-- Name: expense_project_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX expense_project_idx ON public.expense USING btree (project_id);


--
-- Name: expense_receipt_exp_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX expense_receipt_exp_idx ON public.expense_receipt USING btree (expense_account_id);


--
-- Name: expense_receipt_pers_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX expense_receipt_pers_idx ON public.expense_receipt USING btree (person_id, project_id);


--
-- Name: expense_receipt_proj_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX expense_receipt_proj_idx ON public.expense_receipt USING btree (project_id, person_id);


--
-- Name: expense_week_paid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX expense_week_paid_idx ON public.expense USING btree (week_paid_for);


--
-- Name: fki_address_cr_approver; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_address_cr_approver ON public.address_cr USING btree (approver_id);


--
-- Name: fki_address_cr_project; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_address_cr_project ON public.address_cr USING btree (project_id);


--
-- Name: fki_address_cr_recvhg; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_address_cr_recvhg ON public.address_cr USING btree (change_record_id);


--
-- Name: fki_address_cr_requestor; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_address_cr_requestor ON public.address_cr USING btree (requestor_id);


--
-- Name: fki_address_org_group_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_address_org_group_fkey ON public.address USING btree (org_group_join);


--
-- Name: fki_address_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_address_person_fkey ON public.address USING btree (person_join);


--
-- Name: fki_agency_join_agency; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_agency_join_agency ON public.agency_join USING btree (agency_id);


--
-- Name: fki_agent_join_agent; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_agent_join_agent ON public.agent_join USING btree (agent_id);


--
-- Name: fki_agent_join_apppers; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_agent_join_apppers ON public.agent_join USING btree (approved_by_person_id);


--
-- Name: fki_agreement_person_agree; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_agreement_person_agree ON public.agreement_person_join USING btree (agreement_form_id);


--
-- Name: fki_agreement_person_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_agreement_person_person ON public.agreement_person_join USING btree (person_id);


--
-- Name: fki_alert_last_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_alert_last_person ON public.alert USING btree (last_change_person_id);


--
-- Name: fki_alert_org_group; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_alert_org_group ON public.alert USING btree (org_group_id);


--
-- Name: fki_alert_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_alert_person ON public.alert USING btree (person_id);


--
-- Name: fki_alert_person_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_alert_person_person ON public.alert_person_join USING btree (person_id);


--
-- Name: fki_applicant_ans_choice_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_ans_choice_fkey ON public.applicant_answer USING btree (applicant_question_choice_id);


--
-- Name: fki_applicant_ans_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_ans_person_fkey ON public.applicant_answer USING btree (person_id);


--
-- Name: fki_applicant_answer_ques_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_answer_ques_fkey ON public.applicant_answer USING btree (applicant_question_id);


--
-- Name: fki_applicant_applicant_source_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_applicant_source_fkey ON public.applicant USING btree (applicant_source_id);


--
-- Name: fki_applicant_applicant_status_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_applicant_status_fkey ON public.applicant USING btree (applicant_status_id);


--
-- Name: fki_applicant_application_applicant_app_status_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_application_applicant_app_status_fkey ON public.applicant_application USING btree (applicant_app_status_id);


--
-- Name: fki_applicant_application_applicant_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_application_applicant_fkey ON public.applicant_application USING btree (person_id);


--
-- Name: fki_applicant_application_applicant_position_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_application_applicant_position_fkey ON public.applicant_application USING btree (applicant_position_id);


--
-- Name: fki_applicant_application_position_fky; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_application_position_fky ON public.applicant_application USING btree (position_id);


--
-- Name: fki_applicant_appstat_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_appstat_comp ON public.applicant_app_status USING btree (company_id);


--
-- Name: fki_applicant_contact_applicant_application_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_contact_applicant_application_fkey ON public.applicant_contact USING btree (applicant_application_id);


--
-- Name: fki_applicant_contact_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_contact_person_fkey ON public.applicant_contact USING btree (person_id);


--
-- Name: fki_applicant_contact_who_added_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_contact_who_added_fkey ON public.applicant_contact USING btree (who_added);


--
-- Name: fki_applicant_eeo_race_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_eeo_race_fkey ON public.applicant USING btree (eeo_race_id);


--
-- Name: fki_applicant_position_org_group_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_position_org_group_fkey ON public.applicant_position USING btree (org_group_id);


--
-- Name: fki_applicant_position_position_fky; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_position_position_fky ON public.applicant_position USING btree (position_id);


--
-- Name: fki_applicant_source_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_source_company ON public.applicant_source USING btree (company_id);


--
-- Name: fki_applicant_status_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_applicant_status_comp ON public.applicant_status USING btree (company_id);


--
-- Name: fki_appointment_loc_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_appointment_loc_comp ON public.appointment_location USING btree (company_id);


--
-- Name: fki_appointment_location_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_appointment_location_id_fkey ON public.appointment USING btree (location_id);


--
-- Name: fki_assem_temp_temp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_assem_temp_temp ON public.assembly_template_detail USING btree (assembly_template_id);


--
-- Name: fki_assembly_tem_prod_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_assembly_tem_prod_fkey ON public.assembly_template_detail USING btree (product_id);


--
-- Name: fki_assembly_temp_parent; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_assembly_temp_parent ON public.assembly_template_detail USING btree (parent_detail_id);


--
-- Name: fki_bank_account_org_group_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_bank_account_org_group_fkey ON public.bank_account USING btree (org_group_id);


--
-- Name: fki_bank_draft_detail_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_bank_draft_detail_person_fkey ON public.bank_draft_detail USING btree (person_id);


--
-- Name: fki_bank_draft_hist_draft_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_bank_draft_hist_draft_fkey ON public.bank_draft_history USING btree (bank_draft_id);


--
-- Name: fki_bank_draftbatch_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_bank_draftbatch_comp ON public.bank_draft_batch USING btree (company_id);


--
-- Name: fki_bcr_doc_bcr; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_bcr_doc_bcr ON public.benefit_change_reason_doc USING btree (bcr_id);


--
-- Name: fki_bcr_doc_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_bcr_doc_comp ON public.benefit_change_reason_doc USING btree (company_form_id);


--
-- Name: fki_ben_con_cost_age_con_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_ben_con_cost_age_con_fkey ON public.benefit_config_cost_age USING btree (benefit_config_cost_id);


--
-- Name: fki_ben_concost_stat_emp_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_ben_concost_stat_emp_fkey ON public.benefit_config_cost_status USING btree (employee_status_id);


--
-- Name: fki_ben_conf_cost_config_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_ben_conf_cost_config_fkey ON public.benefit_config_cost USING btree (benefit_config_id);


--
-- Name: fki_ben_conf_cost_st_con_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_ben_conf_cost_st_con_fkey ON public.benefit_config_cost_status USING btree (benefit_config_cost_id);


--
-- Name: fki_benconf_conclsjn_ben; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benconf_conclsjn_ben ON public.benefit_group_class_join USING btree (benefit_id);


--
-- Name: fki_benef_ansh_recper_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benef_ansh_recper_fkey ON public.benefit_answer_h USING btree (record_person_id);


--
-- Name: fki_benef_quesh_recper_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benef_quesh_recper_fkey ON public.benefit_question_h USING btree (record_person_id);


--
-- Name: fki_benefit_ans_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_ans_person_fkey ON public.benefit_answer USING btree (person_id);


--
-- Name: fki_benefit_ans_recper_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_ans_recper_fkey ON public.benefit_answer USING btree (record_person_id);


--
-- Name: fki_benefit_ansh_pers_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_ansh_pers_fkey ON public.benefit_answer_h USING btree (person_id);


--
-- Name: fki_benefit_ansh_ques_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_ansh_ques_fkey ON public.benefit_answer_h USING btree (benefit_question_id);


--
-- Name: fki_benefit_answer_ques_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_answer_ques_fkey ON public.benefit_answer USING btree (benefit_question_id);


--
-- Name: fki_benefit_class_join_config_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_class_join_config_fkey ON public.benefit_class_join USING btree (benefit_config_id);


--
-- Name: fki_benefit_concost_org_kfkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_concost_org_kfkey ON public.benefit_config_cost USING btree (org_group_id);


--
-- Name: fki_benefit_dependency_benefit; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_dependency_benefit ON public.benefit_dependency USING btree (benefit_id);


--
-- Name: fki_benefit_dependency_required; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_dependency_required ON public.benefit_dependency USING btree (required_benefit_id);


--
-- Name: fki_benefit_doc_benefit; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_doc_benefit ON public.benefit_document USING btree (benefit_id);


--
-- Name: fki_benefit_doc_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_doc_comp ON public.benefit_document USING btree (company_form_id);


--
-- Name: fki_benefit_ques_recper_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_ques_recper_fkey ON public.benefit_question USING btree (record_person_id);


--
-- Name: fki_benefit_restriction_bcr; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_restriction_bcr ON public.benefit_restriction USING btree (bcr_id);


--
-- Name: fki_benefit_restriction_bencat; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benefit_restriction_bencat ON public.benefit_restriction USING btree (benefit_cat_id);


--
-- Name: fki_benenfit_join_project; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_benenfit_join_project ON public.hr_benefit_join USING btree (project_id);


--
-- Name: fki_bill_at_org_group_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_bill_at_org_group_fkey ON public.project USING btree (bill_at_org_group);


--
-- Name: fki_change_log_change_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_change_log_change_person_fkey ON public.change_log USING btree (change_person_id);


--
-- Name: fki_change_log_client_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_change_log_client_fkey ON public.change_log USING btree (client_id);


--
-- Name: fki_change_log_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_change_log_person_fkey ON public.change_log USING btree (person_id);


--
-- Name: fki_change_log_project_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_change_log_project_fkey ON public.change_log USING btree (project_id);


--
-- Name: fki_change_log_vendor_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_change_log_vendor_fkey ON public.change_log USING btree (vendor_id);


--
-- Name: fki_checklist_detail_item_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_checklist_detail_item_fkey ON public.hr_checklist_detail USING btree (checklist_item_id);


--
-- Name: fki_client_client_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_client_client_status ON public.client USING btree (client_status_id);


--
-- Name: fki_client_company_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_client_company_fkey ON public.client USING btree (company_id);


--
-- Name: fki_client_dflt_ar_acct; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_client_dflt_ar_acct ON public.client USING btree (dflt_ar_acct);


--
-- Name: fki_client_dflt_sales_acct; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_client_dflt_sales_acct ON public.client USING btree (dflt_sales_acct);


--
-- Name: fki_client_item_inventory_vend_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_client_item_inventory_vend_fkey ON public.client_item_inventory USING btree (project_id);


--
-- Name: fki_client_status_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_client_status_comp ON public.client_status USING btree (company_id);


--
-- Name: fki_comp_form_folder_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_comp_form_folder_comp ON public.company_form_folder USING btree (company_id);


--
-- Name: fki_comp_formfoldj_fold; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_comp_formfoldj_fold ON public.company_form_folder_join USING btree (folder_id);


--
-- Name: fki_company_det_adv_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_company_det_adv_fkey ON public.company_detail USING btree (employee_advance_account_id);


--
-- Name: fki_company_det_ar_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_company_det_ar_fkey ON public.company_detail USING btree (ar_account_id);


--
-- Name: fki_company_detail_cash_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_company_detail_cash_fkey ON public.company_detail USING btree (cash_account_id);


--
-- Name: fki_company_form_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_company_form_company ON public.company_form USING btree (company_id);


--
-- Name: fki_company_form_folder_parent; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_company_form_folder_parent ON public.company_form_folder USING btree (parent_folder_id);


--
-- Name: fki_company_form_orgj; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_company_form_orgj ON public.company_form_org_join USING btree (org_group_id);


--
-- Name: fki_company_form_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_company_form_type ON public.company_form USING btree (form_type_id);


--
-- Name: fki_company_question_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_company_question_comp ON public.company_question USING btree (company_id);


--
-- Name: fki_contact_question_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_contact_question_comp ON public.contact_question USING btree (company_id);


--
-- Name: fki_delivery_detail_delivery_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_delivery_detail_delivery_fkey ON public.delivery_detail USING btree (delivery_id);


--
-- Name: fki_delivery_detail_po_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_delivery_detail_po_fkey ON public.delivery_detail USING btree (project_po_detail_id);


--
-- Name: fki_delivery_po_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_delivery_po_fkey ON public.delivery USING btree (project_po_id);


--
-- Name: fki_drc_form_event_causing_per; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_drc_form_event_causing_per ON public.drc_form_event USING btree (person_id_causing_event);


--
-- Name: fki_drc_form_event_emp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_drc_form_event_emp ON public.drc_form_event USING btree (employee_id);


--
-- Name: fki_drc_form_event_per; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_drc_form_event_per ON public.drc_form_event USING btree (person_id);


--
-- Name: fki_drc_import_ben_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_drc_import_ben_comp ON public.drc_import_benefit USING btree (company_id);


--
-- Name: fki_drc_import_benjn_ben_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_drc_import_benjn_ben_fkey ON public.drc_import_benefit_join USING btree (import_benefit_id);


--
-- Name: fki_drc_import_benjn_sub; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_drc_import_benjn_sub ON public.drc_import_benefit_join USING btree (subscriber_id);


--
-- Name: fki_drc_import_benjoin_enr; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_drc_import_benjoin_enr ON public.drc_import_benefit_join USING btree (enrollee_id);


--
-- Name: fki_drc_import_enr_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_drc_import_enr_comp ON public.drc_import_enrollee USING btree (company_id);


--
-- Name: fki_drc_import_enr_pers; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_drc_import_enr_pers ON public.drc_import_enrollee USING btree (record_person_id);


--
-- Name: fki_e_signature_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_e_signature_person ON public.e_signature USING btree (person_id);


--
-- Name: fki_eft_wage_type_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_eft_wage_type_fkey ON public.electronic_fund_transfer USING btree (wage_type_id);


--
-- Name: fki_employee_bank_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_employee_bank_fkey ON public.employee USING btree (payroll_bank_code);


--
-- Name: fki_employee_changed_employee_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_employee_changed_employee_fkey ON public.person_changed USING btree (person_id);


--
-- Name: fki_employee_class_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_employee_class_fkey ON public.employee USING btree (benefit_class_id);


--
-- Name: fki_employee_eeo_category_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_employee_eeo_category_fkey ON public.employee USING btree (eeo_category_id);


--
-- Name: fki_employee_eeo_race_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_employee_eeo_race_fkey ON public.employee USING btree (eeo_race_id);


--
-- Name: fki_employee_emp_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_employee_emp_status ON public.employee USING btree (status_id);


--
-- Name: fki_employee_lbl_ass_lbl_fky; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_employee_lbl_ass_lbl_fky ON public.employee_label_association USING btree (employee_label_id);


--
-- Name: fki_employee_lbl_ass_who_added_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_employee_lbl_ass_who_added_fkey ON public.employee_label_association USING btree (who_added);


--
-- Name: fki_employee_lbl_ass_who_comp_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_employee_lbl_ass_who_comp_fkey ON public.employee_label_association USING btree (who_completed);


--
-- Name: fki_employee_rate_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_employee_rate_person_fkey ON public.employee_rate USING btree (person_id);


--
-- Name: fki_expense_receipt_who_app_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_expense_receipt_who_app_fkey ON public.expense_receipt USING btree (who_approved);


--
-- Name: fki_expense_receipt_who_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_expense_receipt_who_fkey ON public.expense_receipt USING btree (who_uploaded);


--
-- Name: fki_form_type_org; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_form_type_org ON public.form_type USING btree (org_group_id);


--
-- Name: fki_garn_typ_wage_typ; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_garn_typ_wage_typ ON public.garnishment_type USING btree (wage_type_id);


--
-- Name: fki_garnishment_remit_to_fksy; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_garnishment_remit_to_fksy ON public.garnishment USING btree (remit_to);


--
-- Name: fki_garnishment_type_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_garnishment_type_fkey ON public.garnishment USING btree (garnishment_type_id);


--
-- Name: fki_gl_account_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_gl_account_comp ON public.gl_account USING btree (company_id);


--
-- Name: fki_group_association_group_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_group_association_group_id_fkey ON public.org_group_association USING btree (org_group_id, org_group_type);


--
-- Name: fki_group_hierarchy_child_group_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_group_hierarchy_child_group_id_fkey ON public.org_group_hierarchy USING btree (child_group_id, org_group_type);


--
-- Name: fki_group_hierarchy_parent_group_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_group_hierarchy_parent_group_id_fkey ON public.org_group_hierarchy USING btree (parent_group_id, org_group_type);


--
-- Name: fki_he_benconf_benefit_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_he_benconf_benefit_fkey ON public.hr_benefit_config USING btree (benefit_id);


--
-- Name: fki_holiday_org; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_holiday_org ON public.holiday USING btree (org_group_id);


--
-- Name: fki_hr_accrual_employee_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_accrual_employee_fkey ON public.hr_accrual USING btree (employee_id);


--
-- Name: fki_hr_accural_benefit_account_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_accural_benefit_account_fkey ON public.hr_accrual USING btree (benefit_account);


--
-- Name: fki_hr_bcr_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_bcr_company ON public.hr_benefit_change_reason USING btree (company_id);


--
-- Name: fki_hr_ben_calss_org_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_ben_calss_org_fkey ON public.hr_benefit_class USING btree (org_group_id);


--
-- Name: fki_hr_ben_cat_ans_ben; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_ben_cat_ans_ben ON public.hr_benefit_category_answer USING btree (benefit_id);


--
-- Name: fki_hr_ben_cat_ans_ques_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_ben_cat_ans_ques_fkey ON public.hr_benefit_category_answer USING btree (question_id);


--
-- Name: fki_hr_ben_join_bcat_chk; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_ben_join_bcat_chk ON public.hr_benefit_join USING btree (benefit_cat_id);


--
-- Name: fki_hr_ben_join_ben_chk; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_ben_join_ben_chk ON public.hr_benefit_join USING btree (benefit_id);


--
-- Name: fki_hr_ben_join_rpi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_ben_join_rpi ON public.hr_benefit_join_h USING btree (record_person_id);


--
-- Name: fki_hr_bencat_oescrn; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_bencat_oescrn ON public.hr_benefit_category USING btree (open_enrollment_screen_id);


--
-- Name: fki_hr_bencat_org_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_bencat_org_fkey ON public.hr_benefit_category USING btree (org_group_id);


--
-- Name: fki_hr_bencat_oscrn; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_bencat_oscrn ON public.hr_benefit_category USING btree (onboarding_screen_id);


--
-- Name: fki_hr_benefit_ben_cat_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_ben_cat_fkey ON public.hr_benefit USING btree (benefit_cat_id);


--
-- Name: fki_hr_benefit_benef_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_benef_fkey ON public.hr_benefit_join USING btree (benefit_config_id);


--
-- Name: fki_hr_benefit_join_bcr; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_join_bcr ON public.hr_benefit_join USING btree (bcr_id);


--
-- Name: fki_hr_benefit_join_cpers_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_join_cpers_fkey ON public.hr_benefit_join USING btree (covered_person);


--
-- Name: fki_hr_benefit_join_hrel_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_join_hrel_fkey ON public.hr_benefit_join USING btree (relationship_id);


--
-- Name: fki_hr_benefit_join_levent; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_join_levent ON public.hr_benefit_join USING btree (life_event_id);


--
-- Name: fki_hr_benefit_join_ppers_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_join_ppers_fkey ON public.hr_benefit_join USING btree (paying_person);


--
-- Name: fki_hr_benefit_join_recper_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_join_recper_fkey ON public.hr_benefit_join USING btree (record_person_id);


--
-- Name: fki_hr_benefit_ooscreen; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_ooscreen ON public.hr_benefit USING btree (open_enrollment_screen_id);


--
-- Name: fki_hr_benefit_oscreen; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_oscreen ON public.hr_benefit USING btree (onboarding_screen_id);


--
-- Name: fki_hr_benefit_pj_benefit_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_pj_benefit_fkey ON public.hr_benefit_package_join USING btree (benefit_id);


--
-- Name: fki_hr_benefit_product_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_product_fkey ON public.hr_benefit USING btree (product_id);


--
-- Name: fki_hr_benefit_provider_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_provider_fkey ON public.hr_benefit USING btree (provider_id);


--
-- Name: fki_hr_benefit_replace; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_replace ON public.hr_benefit USING btree (benefit_id_replaced_by);


--
-- Name: fki_hr_benefit_rider_base; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_rider_base ON public.hr_benefit_rider USING btree (base_benefit_id);


--
-- Name: fki_hr_benefit_rider_rider; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_rider_rider ON public.hr_benefit_rider USING btree (rider_benefit_id);


--
-- Name: fki_hr_benefit_wage_type_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_benefit_wage_type_fkey ON public.hr_benefit USING btree (wage_type_id);


--
-- Name: fki_hr_bill_stat_hist_pers_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_bill_stat_hist_pers_fkey ON public.hr_billing_status_history USING btree (person_id);


--
-- Name: fki_hr_bill_stat_stat_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_bill_stat_stat_fkey ON public.hr_billing_status_history USING btree (billing_status_id);


--
-- Name: fki_hr_bpjoin_config_fky; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_bpjoin_config_fky ON public.hr_benefit_project_join USING btree (benefit_config_id);


--
-- Name: fki_hr_bpjoin_prokect_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_bpjoin_prokect_fkey ON public.hr_benefit_project_join USING btree (project_id);


--
-- Name: fki_hr_checklist_detail_employee_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_checklist_detail_employee_fkey ON public.hr_checklist_detail USING btree (employee_id);


--
-- Name: fki_hr_checklist_detail_super_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_checklist_detail_super_fkey ON public.hr_checklist_detail USING btree (supervisor_id);


--
-- Name: fki_hr_checklist_item_status_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_checklist_item_status_fkey ON public.hr_checklist_item USING btree (employee_status_id);


--
-- Name: fki_hr_chklst_item_org_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_chklst_item_org_fkey ON public.hr_checklist_item USING btree (org_group_id);


--
-- Name: fki_hr_chklstitm_compform; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_chklstitm_compform ON public.hr_checklist_item USING btree (company_form_id);


--
-- Name: fki_hr_chklstitm_screen; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_chklstitm_screen ON public.hr_checklist_item USING btree (screen_id);


--
-- Name: fki_hr_chklstitm_screengrp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_chklstitm_screengrp ON public.hr_checklist_item USING btree (screen_group_id);


--
-- Name: fki_hr_emp_beneciary_rpi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_emp_beneciary_rpi ON public.hr_employee_beneficiary USING btree (record_person_id);


--
-- Name: fki_hr_emp_beneficiary_rpi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_emp_beneficiary_rpi ON public.hr_employee_beneficiary_h USING btree (record_person_id);


--
-- Name: fki_hr_emp_evt_employee_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_emp_evt_employee_fkey ON public.hr_employee_event USING btree (employee_id);


--
-- Name: fki_hr_emp_evt_supervisor_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_emp_evt_supervisor_fkey ON public.hr_employee_event USING btree (supervisor_id);


--
-- Name: fki_hr_empl_dep_cr_approver; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_empl_dep_cr_approver ON public.hr_empl_dependent_cr USING btree (approver_id);


--
-- Name: fki_hr_empl_dep_cr_project; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_empl_dep_cr_project ON public.hr_empl_dependent_cr USING btree (project_id);


--
-- Name: fki_hr_empl_dep_cr_recvhg; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_empl_dep_cr_recvhg ON public.hr_empl_dependent_cr USING btree (change_record_id);


--
-- Name: fki_hr_empl_dep_empl_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_empl_dep_empl_id_fkey ON public.hr_empl_dependent USING btree (employee_id);


--
-- Name: fki_hr_empl_depen_cr_req; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_empl_depen_cr_req ON public.hr_empl_dependent_cr USING btree (requestor_id);


--
-- Name: fki_hr_empl_eval_cat_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_empl_eval_cat_fkey ON public.hr_empl_eval_detail USING btree (cat_id);


--
-- Name: fki_hr_empl_eval_detail_eval_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_empl_eval_detail_eval_fkey ON public.hr_empl_eval_detail USING btree (eval_id);


--
-- Name: fki_hr_employee_eval_emp_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_employee_eval_emp_fkey ON public.hr_employee_eval USING btree (employee_id);


--
-- Name: fki_hr_employee_eval_super_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_employee_eval_super_fkey ON public.hr_employee_eval USING btree (supervisor_id);


--
-- Name: fki_hr_empstat_org_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_empstat_org_fkey ON public.hr_employee_status USING btree (org_group_id);


--
-- Name: fki_hr_eval_cat_org_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_eval_cat_org_fkey ON public.hr_eval_category USING btree (org_group_id);


--
-- Name: fki_hr_notecat_org_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_notecat_org_fkey ON public.hr_note_category USING btree (org_group_id);


--
-- Name: fki_hr_position_class_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_position_class_fkey ON public.hr_position USING btree (benefit_class_id);


--
-- Name: fki_hr_status_hist_status_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_status_hist_status_fkey ON public.hr_empl_status_history USING btree (status_id);


--
-- Name: fki_hr_traincat_org_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_traincat_org_fkey ON public.hr_training_category USING btree (org_group_id);


--
-- Name: fki_hr_training_detail_cat_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_training_detail_cat_fkey ON public.hr_training_detail USING btree (cat_id);


--
-- Name: fki_hr_training_detail_empl_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_training_detail_empl_fkey ON public.hr_training_detail USING btree (employee_id);


--
-- Name: fki_hr_wage_position_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_wage_position_fkey ON public.hr_wage USING btree (position_id);


--
-- Name: fki_hr_wage_type_fley; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_hr_wage_type_fley ON public.hr_wage USING btree (wage_type_id);


--
-- Name: fki_import_column_filter; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_import_column_filter ON public.import_column USING btree (import_filter_id);


--
-- Name: fki_import_filter_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_import_filter_type ON public.import_filter USING btree (import_type_id);


--
-- Name: fki_import_type_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_import_type_company ON public.import_type USING btree (company_id);


--
-- Name: fki_ins_loc_cde_ben; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_ins_loc_cde_ben ON public.insurance_location_code USING btree (benefit_id);


--
-- Name: fki_ins_loc_code_ben; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_ins_loc_code_ben ON public.insurance_location_code USING btree (benefit_class_id);


--
-- Name: fki_ins_loc_code_empstat; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_ins_loc_code_empstat ON public.insurance_location_code USING btree (employee_status_id);


--
-- Name: fki_inv_line_item_gl_acct_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_inv_line_item_gl_acct_fkey ON public.invoice_line_item USING btree (expense_account_id);


--
-- Name: fki_inv_line_item_invoice_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_inv_line_item_invoice_fkey ON public.invoice_line_item USING btree (invoice_id);


--
-- Name: fki_inv_line_item_product_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_inv_line_item_product_fkey ON public.invoice_line_item USING btree (product_id);


--
-- Name: fki_inventory_location_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_inventory_location_fkey ON public.inventory USING btree (location_id);


--
-- Name: fki_inventory_product_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_inventory_product_fkey ON public.inventory USING btree (product_id);


--
-- Name: fki_invoice_company_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_invoice_company_fkey ON public.invoice USING btree (customer_id);


--
-- Name: fki_invoice_gl_account_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_invoice_gl_account_fkey ON public.invoice USING btree (ar_account_id);


--
-- Name: fki_invoice_li_project_fk; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_invoice_li_project_fk ON public.invoice_line_item USING btree (project_id);


--
-- Name: fki_invoice_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_invoice_person_fkey ON public.invoice USING btree (person_id);


--
-- Name: fki_item_chg_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_item_chg_person_fkey ON public.item USING btree (record_person_id);


--
-- Name: fki_item_inspection_inspector_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_item_inspection_inspector_fkey ON public.item_inspection USING btree (person_inspecting);


--
-- Name: fki_item_inspection_recorder_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_item_inspection_recorder_fkey ON public.item_inspection USING btree (record_person_id);


--
-- Name: fki_item_lot_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_item_lot_fkey ON public.item USING btree (lot_id);


--
-- Name: fki_item_parent_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_item_parent_fkey ON public.item USING btree (parent_item_id);


--
-- Name: fki_life_event_bcr; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_life_event_bcr ON public.life_event USING btree (bcr_id);


--
-- Name: fki_life_event_recpers; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_life_event_recpers ON public.life_event USING btree (reporting_person_id);


--
-- Name: fki_login_exception_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_login_exception_company ON public.prophet_login_exception USING btree (company_id);


--
-- Name: fki_login_exception_screen; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_login_exception_screen ON public.prophet_login_exception USING btree (screen_group_id);


--
-- Name: fki_login_exception_security; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_login_exception_security ON public.prophet_login_exception USING btree (security_group_id);


--
-- Name: fki_login_screen_group_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_login_screen_group_fkey ON public.prophet_login USING btree (screen_group_id);


--
-- Name: fki_login_security_group_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_login_security_group_fkey ON public.prophet_login USING btree (security_group_id);


--
-- Name: fki_managing_employee_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_managing_employee_fkey ON public.project USING btree (managing_employee);


--
-- Name: fki_message_attachment_message_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_message_attachment_message_id_fkey ON public.message_attachment USING btree (message_id);


--
-- Name: fki_message_to_person_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_message_to_person_id_fkey ON public.message_to USING btree (to_person_id);


--
-- Name: fki_ob_task_complete_pers; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_ob_task_complete_pers ON public.onboarding_task_complete USING btree (person_id);


--
-- Name: fki_ob_task_complete_task; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_ob_task_complete_task ON public.onboarding_task_complete USING btree (onboarding_task_id);


--
-- Name: fki_onboarding_task_screen; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_onboarding_task_screen ON public.onboarding_task USING btree (screen_id);


--
-- Name: fki_org_group_benclass; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_org_group_benclass ON public.org_group USING btree (benefit_class_id);


--
-- Name: fki_org_group_pay_sch_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_org_group_pay_sch_fkey ON public.org_group USING btree (pay_schedule_id);


--
-- Name: fki_org_group_project_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_org_group_project_fkey ON public.org_group USING btree (default_project_id);


--
-- Name: fki_org_grpass_recper_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_org_grpass_recper_fkey ON public.org_group_association USING btree (record_person_id);


--
-- Name: fki_overtime_app_emp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_overtime_app_emp ON public.overtime_approval USING btree (employee_id);


--
-- Name: fki_overtime_app_super; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_overtime_app_super ON public.overtime_approval USING btree (supervisor_id);


--
-- Name: fki_owning_entity_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_owning_entity_fkey ON public.org_group USING btree (owning_entity_id);


--
-- Name: fki_pay_sch_period_sched_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_pay_sch_period_sched_fkey ON public.pay_schedule_period USING btree (pay_schedule_id);


--
-- Name: fki_pay_schedule_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_pay_schedule_company ON public.pay_schedule USING btree (company_id);


--
-- Name: fki_payment_info_benefit; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_payment_info_benefit ON public.payment_info USING btree (benefit_id);


--
-- Name: fki_payment_info_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_payment_info_person ON public.payment_info USING btree (person_id);


--
-- Name: fki_per_diem_exception_proj_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_per_diem_exception_proj_fkey ON public.per_diem_exception USING btree (project_id);


--
-- Name: fki_person_company_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_company_fkey ON public.person USING btree (company_id);


--
-- Name: fki_person_cr_approver; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_cr_approver ON public.person_cr USING btree (approver_id);


--
-- Name: fki_person_cr_project; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_cr_project ON public.person_cr USING btree (project_id);


--
-- Name: fki_person_cr_recvhg; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_cr_recvhg ON public.person_cr USING btree (change_record_id);


--
-- Name: fki_person_cr_requestor; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_cr_requestor ON public.person_cr USING btree (requestor_id);


--
-- Name: fki_person_dflt_project_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_dflt_project_fkey ON public.person USING btree (default_project_id);


--
-- Name: fki_person_form_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_form_person_fkey ON public.person_form USING btree (person_id);


--
-- Name: fki_person_form_type_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_form_type_fkey ON public.person_form USING btree (form_type_id);


--
-- Name: fki_person_gkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_gkey ON public.org_group_association USING btree (person_id);


--
-- Name: fki_person_group_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_group_fkey ON public.org_group_association USING btree (person_id, org_group_type);


--
-- Name: fki_person_i9p1_person_fky; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_i9p1_person_fky ON public.person USING btree (i9p1_person);


--
-- Name: fki_person_i9p2_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_i9p2_person_fkey ON public.person USING btree (i9p2_person);


--
-- Name: fki_person_note_cat_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_note_cat_fkey ON public.person_note USING btree (cat_id);


--
-- Name: fki_person_note_emp_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_note_emp_fkey ON public.person_note USING btree (person_id);


--
-- Name: fki_person_recper_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_recper_fkey ON public.person USING btree (record_person_id);


--
-- Name: fki_personal_ref_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_personal_ref_person ON public.personal_reference USING btree (person_id);


--
-- Name: fki_phone_cr_approver; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_phone_cr_approver ON public.phone_cr USING btree (approver_id);


--
-- Name: fki_phone_cr_project; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_phone_cr_project ON public.phone_cr USING btree (project_id);


--
-- Name: fki_phone_cr_recvhg; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_phone_cr_recvhg ON public.phone_cr USING btree (change_record_id);


--
-- Name: fki_phone_cr_requestor; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_phone_cr_requestor ON public.phone_cr USING btree (requestor_id);


--
-- Name: fki_physicians_benefit; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_physicians_benefit ON public.physician USING btree (benefit_join_id);


--
-- Name: fki_ppv_child_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_ppv_child_fkey ON public.project_view_join USING btree (child_project_view_id);


--
-- Name: fki_ppv_parent_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_ppv_parent_fkey ON public.project_view_join USING btree (parent_project_view_id);


--
-- Name: fki_process_history_sched_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_process_history_sched_fkey ON public.process_history USING btree (process_schedule_id);


--
-- Name: fki_prod_serv_company_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prod_serv_company_fkey ON public.product_service USING btree (company_id);


--
-- Name: fki_prod_typ_company_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prod_typ_company_fkey ON public.product_type USING btree (company_id);


--
-- Name: fki_product_attr_dep_ch; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_product_attr_dep_ch ON public.product_attribute_dependency USING btree (dependent_attribute_id);


--
-- Name: fki_product_attr_dep_prod; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_product_attr_dep_prod ON public.product_attribute_dependency USING btree (product_attribute_id);


--
-- Name: fki_product_product_type_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_product_product_type_fkey ON public.product USING btree (product_type_id);


--
-- Name: fki_product_service_exp_acct_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_product_service_exp_acct_fkey ON public.product_service USING btree (expense_account_id);


--
-- Name: fki_product_type_self_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_product_type_self_fkey ON public.product_type USING btree (parent_product_type_id);


--
-- Name: fki_product_vendor_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_product_vendor_fkey ON public.product USING btree (vendor_id);


--
-- Name: fki_project_address_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_address_fkey ON public.project USING btree (address_id);


--
-- Name: fki_project_app_per_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_app_per_fkey ON public.project USING btree (approving_person);


--
-- Name: fki_project_catagory_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_catagory_comp ON public.project_category USING btree (company_id);


--
-- Name: fki_project_category_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_category_fkey ON public.project USING btree (project_category_id);


--
-- Name: fki_project_chklst_chklst_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_chklst_chklst_fkey ON public.project_checklist_detail USING btree (route_stop_checklist_id);


--
-- Name: fki_project_chklst_person_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_chklst_person_id_fkey ON public.project_checklist_detail USING btree (person_id);


--
-- Name: fki_project_chklst_project_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_chklst_project_fkey ON public.project_checklist_detail USING btree (project_id);


--
-- Name: fki_project_comment_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_comment_person_fkey ON public.project_comment USING btree (person_id);


--
-- Name: fki_project_comment_shift_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_comment_shift_fkey ON public.project_comment USING btree (project_shift_id);


--
-- Name: fki_project_current_route_stop_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_current_route_stop_fkey ON public.project USING btree (current_route_stop);


--
-- Name: fki_project_emp_join_confper_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_emp_join_confper_fkey ON public.project_employee_join USING btree (confirmed_person_id);


--
-- Name: fki_project_emp_join_verifper_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_emp_join_verifper_fkey ON public.project_employee_join USING btree (verified_person_id);


--
-- Name: fki_project_employee_hist_cpers_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_employee_hist_cpers_fkey ON public.project_employee_history USING btree (change_person_id);


--
-- Name: fki_project_employee_hist_per_fky; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_employee_hist_per_fky ON public.project_employee_history USING btree (person_id);


--
-- Name: fki_project_employee_hist_sch_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_employee_hist_sch_fkey ON public.project_employee_history USING btree (project_shift_id);


--
-- Name: fki_project_employee_join_schedule_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_employee_join_schedule_fkey ON public.project_employee_join USING btree (project_shift_id);


--
-- Name: fki_project_form_shift_fky; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_form_shift_fky ON public.project_form USING btree (project_shift_id);


--
-- Name: fki_project_form_type_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_form_type_fkey ON public.project_form USING btree (form_type_id);


--
-- Name: fki_project_inventory_email_per_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_inventory_email_per_fkey ON public.project_inventory_email USING btree (person_id);


--
-- Name: fki_project_owning_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_owning_company ON public.project USING btree (company_id);


--
-- Name: fki_project_phase_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_phase_comp ON public.project_phase USING btree (company_id);


--
-- Name: fki_project_po_det_po_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_po_det_po_fkey ON public.project_po_detail USING btree (project_po_id);


--
-- Name: fki_project_po_detail_item_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_po_detail_item_fkey ON public.project_po_detail USING btree (client_item_inventory_id);


--
-- Name: fki_project_product_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_product_fkey ON public.project USING btree (product_id);


--
-- Name: fki_project_proj_type_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_proj_type_fkey ON public.project USING btree (project_type_id);


--
-- Name: fki_project_purchase_order_proj_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_purchase_order_proj_fkey ON public.project_purchase_order USING btree (project_id);


--
-- Name: fki_project_rate_type_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_rate_type_fkey ON public.project USING btree (rate_type_id);


--
-- Name: fki_project_report_person_client_fk; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_report_person_client_fk ON public.project_report_person USING btree (client_id);


--
-- Name: fki_project_req_comp_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_req_comp_fkey ON public.project USING btree (requesting_org_group);


--
-- Name: fki_project_route_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_route_fkey ON public.project USING btree (route_id);


--
-- Name: fki_project_shift_project_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_shift_project_fkey ON public.project_shift USING btree (project_id);


--
-- Name: fki_project_status_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_status_comp ON public.project_status USING btree (company_id);


--
-- Name: fki_project_subject_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_subject_person_fkey ON public.project USING btree (subject_person);


--
-- Name: fki_project_subtype_comp_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_subtype_comp_fkey ON public.project_subtype USING btree (company_id);


--
-- Name: fki_project_subtype_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_subtype_fkey ON public.project USING btree (project_subtype_id);


--
-- Name: fki_project_task_assignment_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_task_assignment_person_fkey ON public.project_task_assignment USING btree (person_id);


--
-- Name: fki_project_task_detail_recurring_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_task_detail_recurring_fkey ON public.project_task_detail USING btree (recurring_schedule_id);


--
-- Name: fki_project_tembena_pers; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_tembena_pers ON public.project_template_benefit_a USING btree (person_id);


--
-- Name: fki_project_tempben_bcr; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_tempben_bcr ON public.project_template_benefit USING btree (bcr_id);


--
-- Name: fki_project_tempben_org; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_tempben_org ON public.project_template_benefit USING btree (org_group_id);


--
-- Name: fki_project_tempben_pcat; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_tempben_pcat ON public.project_template_benefit USING btree (project_category_id);


--
-- Name: fki_project_tempben_pstat; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_tempben_pstat ON public.project_template_benefit USING btree (project_status_id);


--
-- Name: fki_project_tempben_ptyp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_tempben_ptyp ON public.project_template_benefit USING btree (project_type_id);


--
-- Name: fki_project_tempbene_ben; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_tempbene_ben ON public.project_template_benefit USING btree (benefit_id);


--
-- Name: fki_project_tempbene_stat; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_tempbene_stat ON public.project_template_benefit USING btree (status_id);


--
-- Name: fki_project_tmpbena_temp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_tmpbena_temp ON public.project_template_benefit_a USING btree (project_template_id);


--
-- Name: fki_project_training_cat_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_training_cat_fkey ON public.project_training USING btree (cat_id);


--
-- Name: fki_project_training_project_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_training_project_fkey ON public.project_training USING btree (project_id);


--
-- Name: fki_project_type_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_type_comp ON public.project_type USING btree (company_id);


--
-- Name: fki_projstat_rsj_ps_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_projstat_rsj_ps_fkey ON public.project_status_rs_join USING btree (project_status_id);


--
-- Name: fki_projstat_rsj_rs_kfey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_projstat_rsj_rs_kfey ON public.project_status_rs_join USING btree (route_stop_id);


--
-- Name: fki_prophet_login_nc_scrngrp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prophet_login_nc_scrngrp ON public.prophet_login USING btree (no_company_screen_group_id);


--
-- Name: fki_prospect_appt_appt_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_appt_appt_id_fkey ON public.appointment_person_join USING btree (appointment_id);


--
-- Name: fki_prospect_appt_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_appt_person_fkey ON public.appointment_person_join USING btree (person_id);


--
-- Name: fki_prospect_comp_ques_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_comp_ques_fkey ON public.company_question_detail USING btree (company_ques_id);


--
-- Name: fki_prospect_comp_ques_og_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_comp_ques_og_fkey ON public.company_question_detail USING btree (org_group_id);


--
-- Name: fki_prospect_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_company ON public.prospect USING btree (company_id);


--
-- Name: fki_prospect_cont_pers_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_cont_pers_fkey ON public.contact_question_detail USING btree (person_id);


--
-- Name: fki_prospect_cont_ques_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_cont_ques_fkey ON public.contact_question_detail USING btree (contact_question_id);


--
-- Name: fki_prospect_h_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_h_company ON public.prospect_h USING btree (company_id);


--
-- Name: fki_prospect_h_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_h_person ON public.prospect_h USING btree (person_id);


--
-- Name: fki_prospect_h_prospect_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_h_prospect_type ON public.prospect_h USING btree (prospect_type_id);


--
-- Name: fki_prospect_h_recpers; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_h_recpers ON public.prospect_h USING btree (record_person_id);


--
-- Name: fki_prospect_log_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_log_person ON public.prospect_log USING btree (person_id);


--
-- Name: fki_prospect_log_result; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_log_result ON public.prospect_log USING btree (sales_activity_result_id);


--
-- Name: fki_prospect_log_salesact; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_log_salesact ON public.prospect_log USING btree (sales_activity_id);


--
-- Name: fki_prospect_prospect_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_prospect_type ON public.prospect USING btree (prospect_type_id);


--
-- Name: fki_prospect_recchgper_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_recchgper_fkey ON public.prospect USING btree (record_person_id);


--
-- Name: fki_prospect_source_company_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_source_company_fkey ON public.prospect_source USING btree (company_id);


--
-- Name: fki_prospect_source_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_source_fkey ON public.prospect USING btree (prospect_source_id);


--
-- Name: fki_prospect_status_chk; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_status_chk ON public.prospect USING btree (prospect_status_id);


--
-- Name: fki_prospect_status_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_prospect_status_company ON public.prospect_status USING btree (company_id);


--
-- Name: fki_pview_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_pview_person_fkey ON public.project_view USING btree (person_id);


--
-- Name: fki_pview_project_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_pview_project_fkey ON public.project_view USING btree (project_id);


--
-- Name: fki_quote_accept_peron; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_quote_accept_peron ON public.quote_table USING btree (accepted_person);


--
-- Name: fki_quote_created_by; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_quote_created_by ON public.quote_table USING btree (created_by_person);


--
-- Name: fki_quote_final_by; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_quote_final_by ON public.quote_table USING btree (finalized_by_person);


--
-- Name: fki_quote_location; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_quote_location ON public.quote_table USING btree (location_cost_id);


--
-- Name: fki_quote_prod_prod; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_quote_prod_prod ON public.quote_product USING btree (product_id);


--
-- Name: fki_quote_tempprod_prod; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_quote_tempprod_prod ON public.quote_template_product USING btree (product_id);


--
-- Name: fki_receipt_bank_draft_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_receipt_bank_draft_fkey ON public.receipt USING btree (bank_draft_history_id);


--
-- Name: fki_receipt_join_invoice_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_receipt_join_invoice_fkey ON public.receipt_join USING btree (invoice_id);


--
-- Name: fki_receipt_join_receipt_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_receipt_join_receipt_fkey ON public.receipt_join USING btree (receipt_id);


--
-- Name: fki_reminder_about_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_reminder_about_person_fkey ON public.reminder USING btree (about_person);


--
-- Name: fki_reminder_about_project_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_reminder_about_project_fkey ON public.reminder USING btree (about_project);


--
-- Name: fki_reminder_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_reminder_person_fkey ON public.reminder USING btree (person_id);


--
-- Name: fki_reminder_who_added_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_reminder_who_added_fkey ON public.reminder USING btree (who_added);


--
-- Name: fki_reminder_who_inactivated_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_reminder_who_inactivated_fkey ON public.reminder USING btree (who_inactivated);


--
-- Name: fki_report_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_report_company ON public.report USING btree (company_id);


--
-- Name: fki_report_graphic_report; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_report_graphic_report ON public.report_graphic USING btree (report_id);


--
-- Name: fki_rfr_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_rfr_person_fkey ON public.removed_from_roster USING btree (person_id);


--
-- Name: fki_rfr_shift_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_rfr_shift_fkey ON public.removed_from_roster USING btree (shift_id);


--
-- Name: fki_rfr_supervisor_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_rfr_supervisor_fkey ON public.removed_from_roster USING btree (supervisor_id);


--
-- Name: fki_rights_association_right_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_rights_association_right_id_fkey ON public.rights_association USING btree (right_id);


--
-- Name: fki_route_assoc_cat_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_assoc_cat_fkey ON public.route_type_assoc USING btree (project_category_id);


--
-- Name: fki_route_assoc_route_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_assoc_route_fkey ON public.route_type_assoc USING btree (route_id);


--
-- Name: fki_route_assoc_type_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_assoc_type_fkey ON public.route_type_assoc USING btree (project_type_id);


--
-- Name: fki_route_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_comp ON public.route USING btree (company_id);


--
-- Name: fki_route_hist_from_status_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_hist_from_status_fkey ON public.project_history USING btree (from_status_id);


--
-- Name: fki_route_hist_from_stop_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_hist_from_stop_fkey ON public.project_history USING btree (from_stop_id);


--
-- Name: fki_route_hist_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_hist_person_fkey ON public.project_history USING btree (person_id);


--
-- Name: fki_route_hist_project_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_hist_project_fkey ON public.project_history USING btree (project_id);


--
-- Name: fki_route_hist_to_status_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_hist_to_status_fkey ON public.project_history USING btree (to_status_id);


--
-- Name: fki_route_hist_to_stop_kkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_hist_to_stop_kkey ON public.project_history USING btree (to_stop_id);


--
-- Name: fki_route_path_from_status_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_path_from_status_fkey ON public.route_path USING btree (from_status_id);


--
-- Name: fki_route_path_from_stop_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_path_from_stop_fkey ON public.route_path USING btree (from_stop_id);


--
-- Name: fki_route_path_to_status_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_path_to_status_fkey ON public.route_path USING btree (to_status_id);


--
-- Name: fki_route_path_to_stop_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_path_to_stop_fkey ON public.route_path USING btree (to_stop_id);


--
-- Name: fki_route_project_status_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_project_status_fkey ON public.route USING btree (project_status_id);


--
-- Name: fki_route_route_stop_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_route_stop_fkey ON public.route USING btree (route_stop_id);


--
-- Name: fki_route_stop_org_group_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_stop_org_group_fkey ON public.route_stop USING btree (org_group_id);


--
-- Name: fki_route_stop_phase_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_stop_phase_fkey ON public.route_stop USING btree (project_phase_id);


--
-- Name: fki_route_stop_route_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_route_stop_route_fkey ON public.route_stop USING btree (route_id);


--
-- Name: fki_sales_activity_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_sales_activity_company ON public.sales_activity USING btree (company_id);


--
-- Name: fki_sales_points_prospect; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_sales_points_prospect ON public.sales_points USING btree (prospect_id);


--
-- Name: fki_sales_points_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_sales_points_status ON public.sales_points USING btree (prospect_status_id);


--
-- Name: fki_screen_group_access; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_screen_group_access ON public.screen_group_access USING btree (can_access_screen_group_id);


--
-- Name: fki_screen_group_hier_screen_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_screen_group_hier_screen_fkey ON public.screen_group_hierarchy USING btree (screen_id);


--
-- Name: fki_screen_group_hierarchy_child_screen_group_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_screen_group_hierarchy_child_screen_group_id_fkey ON public.screen_group_hierarchy USING btree (child_screen_group_id);


--
-- Name: fki_security_group_acc; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_security_group_acc ON public.security_group_access USING btree (can_access_security_group_id);


--
-- Name: fki_service_sub_org_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_service_sub_org_fkey ON public.service_subscribed USING btree (org_group_id);


--
-- Name: fki_service_subjoin_org; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_service_subjoin_org ON public.service_subscribed_join USING btree (org_group_id);


--
-- Name: fki_service_subjoin_service; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_service_subjoin_service ON public.service_subscribed_join USING btree (service_id);


--
-- Name: fki_sp_bill_at_org_group_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_sp_bill_at_org_group_fkey ON public.standard_project USING btree (bill_at_org_group);


--
-- Name: fki_sp_managing_employee_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_sp_managing_employee_fkey ON public.standard_project USING btree (managing_employee);


--
-- Name: fki_sp_sponsor_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_sp_sponsor_fkey ON public.standard_project USING btree (project_sponsor_id);


--
-- Name: fki_sponsor_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_sponsor_fkey ON public.project USING btree (project_sponsor_id);


--
-- Name: fki_spousal_verif_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_spousal_verif_person_fkey ON public.spousal_insurance_verif USING btree (person_id);


--
-- Name: fki_standard_project_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_standard_project_comp ON public.standard_project USING btree (company_id);


--
-- Name: fki_std_project_product_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_std_project_product_fkey ON public.standard_project USING btree (product_id);


--
-- Name: fki_time_off_req_app_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_time_off_req_app_person ON public.time_off_request USING btree (approving_person_id);


--
-- Name: fki_time_off_req_proj_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_time_off_req_proj_fkey ON public.time_off_request USING btree (project_id);


--
-- Name: fki_time_off_request_person_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_time_off_request_person_fkey ON public.time_off_request USING btree (requesting_person_id);


--
-- Name: fki_time_type_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_time_type_id_fkey ON public.timesheet USING btree (time_type_id);


--
-- Name: fki_timeoff_acc_calc_config; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_timeoff_acc_calc_config ON public.time_off_accrual_calc USING btree (benefit_config_id);


--
-- Name: fki_timesheet_beg_person_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_timesheet_beg_person_id ON public.timesheet USING btree (beginning_entry_person_id);


--
-- Name: fki_timesheet_end_person_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_timesheet_end_person_id ON public.timesheet USING btree (end_entry_person_id);


--
-- Name: fki_timesheet_invoice_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_timesheet_invoice_fkey ON public.timesheet USING btree (invoice_line_item_id);


--
-- Name: fki_timesheet_message_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_timesheet_message_fkey ON public.timesheet USING btree (message_id);


--
-- Name: fki_timesheet_person_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_timesheet_person_id_fkey ON public.timesheet USING btree (person_id);


--
-- Name: fki_timesheet_wagetype_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_timesheet_wagetype_fkey ON public.timesheet USING btree (wage_type_id);


--
-- Name: fki_vandor_dflt_ap_acct; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_vandor_dflt_ap_acct ON public.vendor USING btree (dflt_ap_acct);


--
-- Name: fki_vandor_dflt_expense_acct; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_vandor_dflt_expense_acct ON public.vendor USING btree (dflt_expense_acct);


--
-- Name: fki_vendor_company_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_vendor_company_fkey ON public.vendor USING btree (company_id);


--
-- Name: fki_vendor_group_org; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_vendor_group_org ON public.vendor_group USING btree (org_group_id);


--
-- Name: fki_vendor_group_vendor; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_vendor_group_vendor ON public.vendor_group USING btree (vendor_id);


--
-- Name: fki_wage_detail_paid_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wage_detail_paid_fkey ON public.wage_paid_detail USING btree (wage_paid_id);


--
-- Name: fki_wage_paid_det_type_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wage_paid_det_type_fkey ON public.wage_paid_detail USING btree (wage_type_id);


--
-- Name: fki_wage_type_gl_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wage_type_gl_fkey ON public.wage_type USING btree (expense_account);


--
-- Name: fki_wage_type_liab_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wage_type_liab_fkey ON public.wage_type USING btree (liability_account);


--
-- Name: fki_wizard_ben_ben_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_ben_ben_fkey ON public.wizard_config_benefit USING btree (benefit_id);


--
-- Name: fki_wizard_ben_screen; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_ben_screen ON public.wizard_config_benefit USING btree (screen_id);


--
-- Name: fki_wizard_cat; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_cat ON public.wizard_config_category USING btree (benefit_cat_id);


--
-- Name: fki_wizard_cat_screen; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_cat_screen ON public.wizard_config_category USING btree (screen_id);


--
-- Name: fki_wizard_conf_benclass; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_conf_benclass ON public.wizard_configuration USING btree (benefit_class_id);


--
-- Name: fki_wizard_conf_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_conf_company ON public.wizard_configuration USING btree (company_id);


--
-- Name: fki_wizard_conf_conf; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_conf_conf ON public.wizard_config_config USING btree (benefit_config_id);


--
-- Name: fki_wizard_conf_projcat; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_conf_projcat ON public.wizard_configuration USING btree (project_category_id);


--
-- Name: fki_wizard_conf_projstat; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_conf_projstat ON public.wizard_configuration USING btree (project_status_id);


--
-- Name: fki_wizard_conf_projtype; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_conf_projtype ON public.wizard_configuration USING btree (project_type_id);


--
-- Name: fki_wizard_config_contact; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_config_contact ON public.wizard_configuration USING btree (hr_contact_id);


--
-- Name: fki_wizard_config_demoscn; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_config_demoscn ON public.wizard_configuration USING btree (demographic_screen_id);


--
-- Name: fki_wizard_confproja_pers; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_confproja_pers ON public.wizard_config_project_a USING btree (person_id);


--
-- Name: fki_wizard_confproja_wiz; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_confproja_wiz ON public.wizard_config_project_a USING btree (wizard_configuration_id);


--
-- Name: fki_wizard_project_benjoin; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_project_benjoin ON public.wizard_project USING btree (benefit_join_id);


--
-- Name: fki_wizard_project_hist; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_project_hist ON public.wizard_project USING btree (hr_benefit_join_h_id);


--
-- Name: fki_wizard_project_pers_comp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_project_pers_comp ON public.wizard_project USING btree (person_completed);


--
-- Name: fki_wizard_project_project; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_wizard_project_project ON public.wizard_project USING btree (project_id);


--
-- Name: fki_worker_confirmation_sch_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_worker_confirmation_sch_fkey ON public.worker_confirmation USING btree (project_shift_id);


--
-- Name: fki_worker_confirmation_who_added_fk; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_worker_confirmation_who_added_fk ON public.worker_confirmation USING btree (who_added);


--
-- Name: garnishment_main_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX garnishment_main_key ON public.garnishment USING btree (person_id, priority);


--
-- Name: holiday_date_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX holiday_date_key ON public.holiday USING btree (hdate);


--
-- Name: hr_accural_emp_date_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hr_accural_emp_date_idx ON public.hr_accrual USING btree (employee_id, accrual_date);


--
-- Name: hr_ben_cat_desc_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hr_ben_cat_desc_idx ON public.hr_benefit_category USING btree (description);


--
-- Name: hr_ben_cat_ques_cat_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX hr_ben_cat_ques_cat_idx ON public.hr_benefit_category_question USING btree (benefit_cat_id, seqno);


--
-- Name: hr_benefit_join_h_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hr_benefit_join_h_idx ON public.hr_benefit_join_h USING btree (benefit_join_id, record_change_date);


--
-- Name: hr_benjn_cobpol_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hr_benjn_cobpol_idx ON public.hr_benefit_join USING btree (cobra, policy_end_date);


--
-- Name: hr_benjn_pedcob_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hr_benjn_pedcob_idx ON public.hr_benefit_join USING btree (policy_end_date, cobra);


--
-- Name: hr_checklist_item_emp_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hr_checklist_item_emp_idx ON public.hr_checklist_item USING btree (employee_status_id, seq);


--
-- Name: hr_emp_ben_join_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hr_emp_ben_join_idx ON public.hr_employee_beneficiary USING btree (benefit_join_id);


--
-- Name: hr_emp_beneficiary_h_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hr_emp_beneficiary_h_idx ON public.hr_employee_beneficiary_h USING btree (beneficiary_id, record_change_date);


--
-- Name: hr_empl_dep_cr_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hr_empl_dep_cr_main_idx ON public.hr_empl_dependent_cr USING btree (real_record_id, change_status, request_time);


--
-- Name: hr_empl_dep_dep_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hr_empl_dep_dep_idx ON public.hr_empl_dependent USING btree (dependent_id);


--
-- Name: hr_empl_stat_hist_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX hr_empl_stat_hist_idx ON public.hr_empl_status_history USING btree (employee_id, effective_date);


--
-- Name: hr_emrcont_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX hr_emrcont_person_idx ON public.hr_emergency_contact USING btree (person_id, seqno);


--
-- Name: hr_pos_org_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hr_pos_org_idx ON public.hr_position USING btree (org_group_id, seqno);


--
-- Name: hr_training_cat_client_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hr_training_cat_client_idx ON public.hr_training_category USING btree (client_id);


--
-- Name: hr_wage_employee_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX hr_wage_employee_idx ON public.hr_wage USING btree (employee_id, effective_date);


--
-- Name: import_type_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX import_type_main_idx ON public.import_type USING btree (company_id, import_program_name, import_name);


--
-- Name: import_type_program_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX import_type_program_idx ON public.import_type USING btree (import_program_name);


--
-- Name: interface_log_comp_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX interface_log_comp_idx ON public.interface_log USING btree (company_id, last_run);


--
-- Name: interface_log_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX interface_log_pidx ON public.interface_log USING btree (interface_code, last_run);


--
-- Name: invoice_line_benefit_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX invoice_line_benefit_idx ON public.invoice_line_item USING btree (benefit_join_id);


--
-- Name: item_accepting_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX item_accepting_person_idx ON public.item USING btree (person_accepting_reimbursement);


--
-- Name: item_h_accepting_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX item_h_accepting_person_idx ON public.item_h USING btree (person_accepting_reimbursement);


--
-- Name: item_h_item_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX item_h_item_idx ON public.item_h USING btree (item_id, record_change_date);


--
-- Name: item_h_lot_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX item_h_lot_id_idx ON public.item_h USING btree (lot_id);


--
-- Name: item_h_product_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX item_h_product_idx ON public.item_h USING btree (product_id);


--
-- Name: item_h_reimbursement_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX item_h_reimbursement_person_idx ON public.item_h USING btree (reimbursement_person_id);


--
-- Name: item_inspection_item_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX item_inspection_item_idx ON public.item_inspection USING btree (item_id, inspection_date);


--
-- Name: item_location_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX item_location_idx ON public.item USING btree (location_id, product_id);


--
-- Name: item_product_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX item_product_idx ON public.item USING btree (product_id, lot_id, location_id);


--
-- Name: item_reimbursement_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX item_reimbursement_person_idx ON public.item USING btree (reimbursement_person_id);


--
-- Name: item_serial_no_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX item_serial_no_idx ON public.item USING btree (product_id, serial_number);


--
-- Name: life_event_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX life_event_main_idx ON public.life_event USING btree (person_id, event_date);


--
-- Name: location_cost_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX location_cost_pidx ON public.location_cost USING btree (description);


--
-- Name: login_exception_pers_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX login_exception_pers_idx ON public.prophet_login_exception USING btree (person_id, company_id);


--
-- Name: lot_lot_number_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX lot_lot_number_idx ON public.lot USING btree (lot_number);


--
-- Name: message_person_id_show_created_date_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX message_person_id_show_created_date_uindex ON public.message USING btree (from_person_id, from_show, created_date DESC);


--
-- Name: message_to_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX message_to_main_idx ON public.message_to USING btree (to_person_id, to_show, date_received DESC NULLS LAST);


--
-- Name: message_to_message_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX message_to_message_id_idx ON public.message_to USING btree (message_id);


--
-- Name: onboarding_task_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX onboarding_task_main_idx ON public.onboarding_task USING btree (onboarding_config_id, seqno);


--
-- Name: onboardomg_config_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX onboardomg_config_main_idx ON public.onboarding_config USING btree (company_id, config_name);


--
-- Name: org_group_external_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX org_group_external_id_idx ON public.org_group USING btree (external_id);


--
-- Name: org_group_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX org_group_name_idx ON public.org_group USING btree (lower((group_name)::text));


--
-- Name: org_group_orggrp_guid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX org_group_orggrp_guid_idx ON public.org_group USING btree (org_group_guid);


--
-- Name: org_grpass_h_ss_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX org_grpass_h_ss_idx ON public.org_group_association_h USING btree (person_id, org_group_id, record_change_date);


--
-- Name: password_history_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX password_history_person_idx ON public.password_history USING btree (person_id, date_retired);


--
-- Name: pej_person_pri_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pej_person_pri_idx ON public.project_employee_join USING btree (person_id, person_priority);


--
-- Name: per_diem_exception_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX per_diem_exception_person_idx ON public.per_diem_exception USING btree (person_id);


--
-- Name: person_chgreq_main_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX person_chgreq_main_key ON public.person_change_request USING btree (person_id, request_type, request_date);


--
-- Name: person_cr_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX person_cr_main_idx ON public.person_cr USING btree (real_record_id, change_status, request_time);


--
-- Name: person_default_email_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX person_default_email_idx ON public.person USING btree (default_email_sender);


--
-- Name: person_email_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX person_email_idx ON public.person USING btree (lower((personal_email)::text));


--
-- Name: person_email_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX person_email_main_idx ON public.person_email USING btree (person_id, date_sent);


--
-- Name: person_form_ref_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX person_form_ref_idx ON public.person_form USING btree (ext_ref);


--
-- Name: person_h_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX person_h_person_idx ON public.person_h USING btree (person_id, record_change_date);


--
-- Name: person_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX person_name_idx ON public.person USING btree (lower((lname)::text), lower((fname)::text));


--
-- Name: person_person_guid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX person_person_guid_idx ON public.person USING btree (person_guid);


--
-- Name: person_sort_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX person_sort_idx ON public.person USING btree (sort_order);


--
-- Name: person_ssn_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX person_ssn_idx ON public.person USING btree (ssn);


--
-- Name: phone_cr_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX phone_cr_main_idx ON public.phone_cr USING btree (real_record_id, change_status, request_time);


--
-- Name: phone_number_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX phone_number_idx ON public.phone USING btree (phone_number);


--
-- Name: phone_org_group_uidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX phone_org_group_uidx ON public.phone USING btree (org_group_join, phone_type);


--
-- Name: phone_person_uidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX phone_person_uidx ON public.phone USING btree (person_join, phone_type);


--
-- Name: previous_empl_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX previous_empl_main_idx ON public.previous_employment USING btree (person_id, start_date);


--
-- Name: process_history_proc_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX process_history_proc_idx ON public.process_history USING btree (process_schedule_id, run_time);


--
-- Name: process_history_time_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX process_history_time_idx ON public.process_history USING btree (run_time);


--
-- Name: product_attribute_choice_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX product_attribute_choice_idx ON public.product_attribute_choice USING btree (product_attribute_id, choice_order, description);


--
-- Name: product_attribute_order_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX product_attribute_order_idx ON public.product_attribute USING btree (company_id, attribute_order, attribute);


--
-- Name: project_category_code_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX project_category_code_idx ON public.project_category USING btree (code);


--
-- Name: project_inventory_email_proj_pers_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX project_inventory_email_proj_pers_idx ON public.project_inventory_email USING btree (project_id, person_id);


--
-- Name: project_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX project_name_idx ON public.project USING btree (project_name);


--
-- Name: project_phase_code_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX project_phase_code_idx ON public.project_phase USING btree (code);


--
-- Name: project_priority_client_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX project_priority_client_idx ON public.project USING btree (priority_client);


--
-- Name: project_priority_company_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX project_priority_company_idx ON public.project USING btree (priority_company);


--
-- Name: project_priority_dept_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX project_priority_dept_idx ON public.project USING btree (priority_department);


--
-- Name: project_reference_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX project_reference_idx ON public.project USING btree (lower((reference)::text));


--
-- Name: project_status_code_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX project_status_code_idx ON public.project_status USING btree (code);


--
-- Name: project_task_assignment_task_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX project_task_assignment_task_idx ON public.project_task_assignment USING btree (project_task_detail_id, person_id);


--
-- Name: project_task_detail_shift_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX project_task_detail_shift_idx ON public.project_task_detail USING btree (project_shift_id, seqno);


--
-- Name: project_task_picture_task_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX project_task_picture_task_idx ON public.project_task_picture USING btree (project_task_detail_id, when_uploaded);


--
-- Name: project_task_picture_who_uploaded_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX project_task_picture_who_uploaded_index ON public.project_task_picture USING btree (who_uploaded);


--
-- Name: project_type_code_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX project_type_code_idx ON public.project_type USING btree (code);


--
-- Name: prophet_login_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX prophet_login_user_idx ON public.prophet_login USING btree (lower((user_login)::text));


--
-- Name: prospect_appt_time_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX prospect_appt_time_idx ON public.appointment USING btree (org_group_id, meeting_date, meeting_time);


--
-- Name: prospect_h_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX prospect_h_pidx ON public.prospect_h USING btree (org_group_id, record_change_date);


--
-- Name: prospect_log_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX prospect_log_main_idx ON public.prospect_log USING btree (org_group_id, contact_date, contact_time);


--
-- Name: prospect_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX prospect_person_idx ON public.prospect USING btree (person_id, next_contact_date);


--
-- Name: prospect_status_seq_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX prospect_status_seq_idx ON public.prospect_status USING btree (company_id NULLS FIRST, seqno);


--
-- Name: prospect_type_company_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX prospect_type_company_idx ON public.prospect_type USING btree (company_id, type_code);


--
-- Name: pvj_display_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pvj_display_idx ON public.project_view_join USING btree (parent_project_view_id, sequence_num);


--
-- Name: qb_chg_main_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX qb_chg_main_idx ON public.quickbooks_change USING btree (arahant_table_name, arahant_record_id);


--
-- Name: qb_main_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX qb_main_key ON public.quickbooks_sync USING btree (org_group_id, interface_time);


--
-- Name: quote_adj_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX quote_adj_pidx ON public.quote_adjustment USING btree (quote_id, seqno);


--
-- Name: quote_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX quote_pidx ON public.quote_table USING btree (client_id, finalized_date DESC);


--
-- Name: quote_prod_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX quote_prod_pidx ON public.quote_product USING btree (quote_id, seqno);


--
-- Name: quote_template_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX quote_template_pidx ON public.quote_template USING btree (template_name);


--
-- Name: quote_temprod_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX quote_temprod_pidx ON public.quote_template_product USING btree (quote_template_id, seqno);


--
-- Name: rate_type_code_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX rate_type_code_idx ON public.rate_type USING btree (rate_code);


--
-- Name: rate_type_company_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rate_type_company_idx ON public.rate_type USING btree (company_id);


--
-- Name: receipt_company_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX receipt_company_idx ON public.receipt USING btree (company_id);


--
-- Name: receipt_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX receipt_person_idx ON public.receipt USING btree (person_id);


--
-- Name: report_column_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX report_column_pidx ON public.report_column USING btree (report_id, seqno);


--
-- Name: report_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX report_pidx ON public.report USING btree (report_type, report_name);


--
-- Name: report_selection_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX report_selection_pidx ON public.report_selection USING btree (report_id, seqno);


--
-- Name: report_title_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX report_title_pidx ON public.report_title USING btree (report_id, page_location, seqno);


--
-- Name: rights_id_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX rights_id_key ON public.security_token USING btree (lower((identifier)::text));


--
-- Name: route_hist_main_lookup; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX route_hist_main_lookup ON public.project_history USING btree (project_id, date_changed, time_changed);


--
-- Name: route_name_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX route_name_key ON public.route USING btree (lower((name)::text));


--
-- Name: route_path_trigger_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX route_path_trigger_idx ON public.route_path USING btree (from_stop_id, from_status_id);


--
-- Name: route_stop_main_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX route_stop_main_key ON public.route_stop_checklist USING btree (route_stop_id, item_priority);


--
-- Name: route_type_assoc_lookup_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX route_type_assoc_lookup_idx ON public.route_type_assoc USING btree (project_category_id, project_type_id);


--
-- Name: sales_acativity_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX sales_acativity_idx ON public.sales_activity USING btree (company_id, seqno, sales_activity_id);


--
-- Name: sales_actres_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX sales_actres_pidx ON public.sales_activity_result USING btree (sales_activity_id, seqno);


--
-- Name: sales_points_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX sales_points_pidx ON public.sales_points USING btree (employee_id, point_date);


--
-- Name: screen_group_hierarchy_screen_seq_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX screen_group_hierarchy_screen_seq_key ON public.screen_group_hierarchy USING btree (parent_screen_group_id, seq_no);


--
-- Name: screen_group_screen_id_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX screen_group_screen_id_key ON public.screen_group USING btree (screen_id);


--
-- Name: screen_history_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX screen_history_person_idx ON public.screen_history USING btree (person_id, time_out NULLS FIRST);


--
-- Name: screen_history_screen_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX screen_history_screen_idx ON public.screen_history USING btree (screen_id);


--
-- Name: security_group_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX security_group_id_idx ON public.security_group USING btree (lower((id)::text));


--
-- Name: service_sub_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX service_sub_name_idx ON public.service_subscribed USING btree (service_name);


--
-- Name: student_verif_main_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX student_verif_main_key ON public.student_verification USING btree (person_id, school_year, calendar_period);


--
-- Name: time_off_accsen_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX time_off_accsen_pidx ON public.time_off_accrual_seniority USING btree (time_off_accrual_calc_id, years_of_service);


--
-- Name: time_off_approver_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX time_off_approver_idx ON public.time_off_request USING btree (approving_person_id, request_status, start_date, start_time);


--
-- Name: time_off_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX time_off_person_idx ON public.time_off_request USING btree (requesting_person_id, start_date, start_time);


--
-- Name: time_off_time_off_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX time_off_time_off_idx ON public.time_off_request USING btree (request_status, start_date, start_time);


--
-- Name: time_reject_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX time_reject_person_idx ON public.time_reject USING btree (person_id, reject_date);


--
-- Name: timesheet_beginning_date_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX timesheet_beginning_date_idx ON public.timesheet USING btree (beginning_date, beginning_time);


--
-- Name: timesheet_ending_date_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX timesheet_ending_date_idx ON public.timesheet USING btree (end_date, beginning_date, beginning_time);


--
-- Name: timesheet_person_date_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX timesheet_person_date_idx ON public.timesheet USING btree (person_id, beginning_date, beginning_time);


--
-- Name: timesheet_schedule_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX timesheet_schedule_index ON public.timesheet USING btree (project_shift_id, beginning_date, beginning_time);


--
-- Name: wage_paid_check_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX wage_paid_check_idx ON public.wage_paid USING btree (check_number);


--
-- Name: wage_paid_empl_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX wage_paid_empl_idx ON public.wage_paid USING btree (employee_id, beg_period);


--
-- Name: wage_paid_period_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX wage_paid_period_key ON public.wage_paid USING btree (beg_period, employee_id);


--
-- Name: wage_type_wage_code_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX wage_type_wage_code_idx ON public.wage_type USING btree (org_group_id, wage_code);


--
-- Name: wizard_benefit_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX wizard_benefit_pidx ON public.wizard_config_benefit USING btree (wizard_config_cat_id, seqno);


--
-- Name: wizard_cat_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX wizard_cat_pidx ON public.wizard_config_category USING btree (wizard_configuration_id, seqno);


--
-- Name: wizard_conf_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX wizard_conf_pidx ON public.wizard_config_config USING btree (wizard_config_ben_id, seqno);


--
-- Name: worker_confirmation_person_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX worker_confirmation_person_idx ON public.worker_confirmation USING btree (person_id, confirmation_time);


--
-- Name: zip_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX zip_index ON public.zipcode_lookup USING btree (zipcode);


--
-- Name: address_cr address_cr_approver_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address_cr
    ADD CONSTRAINT address_cr_approver_fkey FOREIGN KEY (approver_id) REFERENCES public.person(person_id);


--
-- Name: address_cr address_cr_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address_cr
    ADD CONSTRAINT address_cr_project_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: address_cr address_cr_real_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address_cr
    ADD CONSTRAINT address_cr_real_fkey FOREIGN KEY (real_record_id) REFERENCES public.address(address_id);


--
-- Name: address_cr address_cr_recvhg_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address_cr
    ADD CONSTRAINT address_cr_recvhg_fkey FOREIGN KEY (change_record_id) REFERENCES public.address(address_id);


--
-- Name: address_cr address_cr_requestor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address_cr
    ADD CONSTRAINT address_cr_requestor_fkey FOREIGN KEY (requestor_id) REFERENCES public.person(person_id);


--
-- Name: address address_org_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address
    ADD CONSTRAINT address_org_group_fkey FOREIGN KEY (org_group_join) REFERENCES public.org_group(org_group_id);


--
-- Name: address address_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address
    ADD CONSTRAINT address_person_fkey FOREIGN KEY (person_join) REFERENCES public.person(person_id);


--
-- Name: agency agency_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agency
    ADD CONSTRAINT agency_company_fkey FOREIGN KEY (agency_id) REFERENCES public.company_base(org_group_id);


--
-- Name: agency_join agency_join_agency_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agency_join
    ADD CONSTRAINT agency_join_agency_fkey FOREIGN KEY (agency_id) REFERENCES public.agency(agency_id);


--
-- Name: agency_join agency_join_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agency_join
    ADD CONSTRAINT agency_join_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_base(org_group_id);


--
-- Name: agent_join agent_join_agent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agent_join
    ADD CONSTRAINT agent_join_agent_fkey FOREIGN KEY (agent_id) REFERENCES public.agent(agent_id);


--
-- Name: agent_join agent_join_apppers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agent_join
    ADD CONSTRAINT agent_join_apppers_fkey FOREIGN KEY (approved_by_person_id) REFERENCES public.person(person_id);


--
-- Name: agent_join agent_join_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agent_join
    ADD CONSTRAINT agent_join_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_base(org_group_id);


--
-- Name: agent agent_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agent
    ADD CONSTRAINT agent_person_fkey FOREIGN KEY (agent_id) REFERENCES public.person(person_id);


--
-- Name: agreement_person_join agreement_person_agree_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agreement_person_join
    ADD CONSTRAINT agreement_person_agree_fkey FOREIGN KEY (agreement_form_id) REFERENCES public.agreement_form(agreement_form_id);


--
-- Name: agreement_person_join agreement_person_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agreement_person_join
    ADD CONSTRAINT agreement_person_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: alert alert_last_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.alert
    ADD CONSTRAINT alert_last_person_fkey FOREIGN KEY (last_change_person_id) REFERENCES public.person(person_id);


--
-- Name: alert alert_org_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.alert
    ADD CONSTRAINT alert_org_group_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: alert_person_join alert_person_alert_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.alert_person_join
    ADD CONSTRAINT alert_person_alert_fkey FOREIGN KEY (alert_id) REFERENCES public.alert(alert_id);


--
-- Name: alert alert_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.alert
    ADD CONSTRAINT alert_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: alert_person_join alert_person_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.alert_person_join
    ADD CONSTRAINT alert_person_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: applicant_question_choice app_ques_choice_ques_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_question_choice
    ADD CONSTRAINT app_ques_choice_ques_fkey FOREIGN KEY (applicant_question_id) REFERENCES public.applicant_question(applicant_question_id);


--
-- Name: applicant_answer applicant_ans_choice_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_answer
    ADD CONSTRAINT applicant_ans_choice_fkey FOREIGN KEY (applicant_question_choice_id) REFERENCES public.applicant_question_choice(applicant_question_choice_id);


--
-- Name: applicant_answer applicant_ans_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_answer
    ADD CONSTRAINT applicant_ans_person_fkey FOREIGN KEY (person_id) REFERENCES public.applicant(person_id);


--
-- Name: applicant_answer applicant_answer_ques_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_answer
    ADD CONSTRAINT applicant_answer_ques_fkey FOREIGN KEY (applicant_question_id) REFERENCES public.applicant_question(applicant_question_id);


--
-- Name: applicant applicant_applicant_source_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant
    ADD CONSTRAINT applicant_applicant_source_fkey FOREIGN KEY (applicant_source_id) REFERENCES public.applicant_source(applicant_source_id);


--
-- Name: applicant applicant_applicant_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant
    ADD CONSTRAINT applicant_applicant_status_fkey FOREIGN KEY (applicant_status_id) REFERENCES public.applicant_status(applicant_status_id);


--
-- Name: applicant_application applicant_application_applicant_app_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_application
    ADD CONSTRAINT applicant_application_applicant_app_status_fkey FOREIGN KEY (applicant_app_status_id) REFERENCES public.applicant_app_status(applicant_app_status_id);


--
-- Name: applicant_application applicant_application_applicant_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_application
    ADD CONSTRAINT applicant_application_applicant_fkey FOREIGN KEY (person_id) REFERENCES public.applicant(person_id);


--
-- Name: applicant_application applicant_application_applicant_position_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_application
    ADD CONSTRAINT applicant_application_applicant_position_fkey FOREIGN KEY (applicant_position_id) REFERENCES public.applicant_position(applicant_position_id);


--
-- Name: applicant_application applicant_application_position_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_application
    ADD CONSTRAINT applicant_application_position_fky FOREIGN KEY (position_id) REFERENCES public.hr_position(position_id);


--
-- Name: applicant_app_status applicant_appstat_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_app_status
    ADD CONSTRAINT applicant_appstat_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: applicant_contact applicant_contact_applicant_application_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_contact
    ADD CONSTRAINT applicant_contact_applicant_application_fkey FOREIGN KEY (applicant_application_id) REFERENCES public.applicant_application(applicant_application_id);


--
-- Name: applicant_contact applicant_contact_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_contact
    ADD CONSTRAINT applicant_contact_person_fkey FOREIGN KEY (person_id) REFERENCES public.applicant(person_id);


--
-- Name: applicant_contact applicant_contact_who_added_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_contact
    ADD CONSTRAINT applicant_contact_who_added_fkey FOREIGN KEY (who_added) REFERENCES public.person(person_id);


--
-- Name: applicant applicant_eeo_race_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant
    ADD CONSTRAINT applicant_eeo_race_fkey FOREIGN KEY (eeo_race_id) REFERENCES public.hr_eeo_race(eeo_id);


--
-- Name: applicant applicant_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant
    ADD CONSTRAINT applicant_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: applicant_position applicant_position_org_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_position
    ADD CONSTRAINT applicant_position_org_group_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: applicant_position applicant_position_position_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_position
    ADD CONSTRAINT applicant_position_position_fky FOREIGN KEY (position_id) REFERENCES public.hr_position(position_id);


--
-- Name: applicant_question applicant_question_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_question
    ADD CONSTRAINT applicant_question_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: applicant_question applicant_question_position_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_question
    ADD CONSTRAINT applicant_question_position_fkey FOREIGN KEY (position_id) REFERENCES public.hr_position(position_id);


--
-- Name: applicant_source applicant_source_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_source
    ADD CONSTRAINT applicant_source_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: applicant_status applicant_status_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applicant_status
    ADD CONSTRAINT applicant_status_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: appointment_location appointment_loc_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment_location
    ADD CONSTRAINT appointment_loc_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: appointment appointment_location_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_location_id_fkey FOREIGN KEY (location_id) REFERENCES public.appointment_location(location_id);


--
-- Name: assembly_template_detail assem_temp_temp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assembly_template_detail
    ADD CONSTRAINT assem_temp_temp_fkey FOREIGN KEY (assembly_template_id) REFERENCES public.assembly_template(assembly_template_id);


--
-- Name: assembly_template_detail assembly_temp_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assembly_template_detail
    ADD CONSTRAINT assembly_temp_parent_fkey FOREIGN KEY (parent_detail_id) REFERENCES public.assembly_template_detail(assembly_template_detail_id);


--
-- Name: assembly_template_detail assembly_template_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assembly_template_detail
    ADD CONSTRAINT assembly_template_product_fkey FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- Name: bank_account bank_account_org_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT bank_account_org_group_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: bank_draft_detail bank_draft_detail_draft_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bank_draft_detail
    ADD CONSTRAINT bank_draft_detail_draft_fkey FOREIGN KEY (bank_draft_id) REFERENCES public.bank_draft_batch(bank_draft_id);


--
-- Name: bank_draft_detail bank_draft_detail_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bank_draft_detail
    ADD CONSTRAINT bank_draft_detail_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: bank_draft_history bank_draft_hist_draft_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bank_draft_history
    ADD CONSTRAINT bank_draft_hist_draft_fkey FOREIGN KEY (bank_draft_id) REFERENCES public.bank_draft_batch(bank_draft_id);


--
-- Name: bank_draft_batch bank_draftbatch_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bank_draft_batch
    ADD CONSTRAINT bank_draftbatch_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: benefit_change_reason_doc bcr_doc_bcr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_change_reason_doc
    ADD CONSTRAINT bcr_doc_bcr_fkey FOREIGN KEY (bcr_id) REFERENCES public.hr_benefit_change_reason(bcr_id);


--
-- Name: benefit_change_reason_doc bcr_doc_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_change_reason_doc
    ADD CONSTRAINT bcr_doc_comp_fkey FOREIGN KEY (company_form_id) REFERENCES public.company_form(company_form_id);


--
-- Name: benefit_config_cost_age ben_con_cost_age_con_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_config_cost_age
    ADD CONSTRAINT ben_con_cost_age_con_fkey FOREIGN KEY (benefit_config_cost_id) REFERENCES public.benefit_config_cost(benefit_config_cost_id);


--
-- Name: benefit_config_cost_status ben_concost_stat_emp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_config_cost_status
    ADD CONSTRAINT ben_concost_stat_emp_fkey FOREIGN KEY (employee_status_id) REFERENCES public.hr_employee_status(status_id);


--
-- Name: benefit_config_cost ben_conf_cost_config_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_config_cost
    ADD CONSTRAINT ben_conf_cost_config_fkey FOREIGN KEY (benefit_config_id) REFERENCES public.hr_benefit_config(benefit_config_id);


--
-- Name: benefit_config_cost_status ben_conf_cost_st_con_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_config_cost_status
    ADD CONSTRAINT ben_conf_cost_st_con_fkey FOREIGN KEY (benefit_config_cost_id) REFERENCES public.benefit_config_cost(benefit_config_cost_id);


--
-- Name: benefit_question_choice ben_ques_choice_ques_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_question_choice
    ADD CONSTRAINT ben_ques_choice_ques_fkey FOREIGN KEY (benefit_question_id) REFERENCES public.benefit_question(benefit_question_id);


--
-- Name: benefit_group_class_join benconf_clsjn_ben_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_group_class_join
    ADD CONSTRAINT benconf_clsjn_ben_fkey FOREIGN KEY (benefit_class_id) REFERENCES public.hr_benefit_class(benefit_class_id);


--
-- Name: benefit_group_class_join benconf_conclsjn_ben_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_group_class_join
    ADD CONSTRAINT benconf_conclsjn_ben_fkey FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: benefit_answer benefit_ans_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_answer
    ADD CONSTRAINT benefit_ans_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: benefit_answer_h benefit_ansh_h_recper_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_answer_h
    ADD CONSTRAINT benefit_ansh_h_recper_fkey FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: benefit_answer_h benefit_ansh_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_answer_h
    ADD CONSTRAINT benefit_ansh_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: benefit_answer_h benefit_ansh_ques_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_answer_h
    ADD CONSTRAINT benefit_ansh_ques_fkey FOREIGN KEY (benefit_question_id) REFERENCES public.benefit_question(benefit_question_id);


--
-- Name: benefit_answer benefit_answer_ques_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_answer
    ADD CONSTRAINT benefit_answer_ques_fkey FOREIGN KEY (benefit_question_id) REFERENCES public.benefit_question(benefit_question_id);


--
-- Name: benefit_answer benefit_answer_recper_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_answer
    ADD CONSTRAINT benefit_answer_recper_fkey FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: benefit_class_join benefit_class_join_class_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_class_join
    ADD CONSTRAINT benefit_class_join_class_fkey FOREIGN KEY (benefit_class_id) REFERENCES public.hr_benefit_class(benefit_class_id);


--
-- Name: benefit_class_join benefit_class_join_config_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_class_join
    ADD CONSTRAINT benefit_class_join_config_fkey FOREIGN KEY (benefit_config_id) REFERENCES public.hr_benefit_config(benefit_config_id);


--
-- Name: benefit_config_cost benefit_concost_org_kfkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_config_cost
    ADD CONSTRAINT benefit_concost_org_kfkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: benefit_dependency benefit_dependency_benefit_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_dependency
    ADD CONSTRAINT benefit_dependency_benefit_fkey FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: benefit_dependency benefit_dependency_required_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_dependency
    ADD CONSTRAINT benefit_dependency_required_fkey FOREIGN KEY (required_benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: benefit_document benefit_doc_benefit_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_document
    ADD CONSTRAINT benefit_doc_benefit_fkey FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: benefit_document benefit_doc_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_document
    ADD CONSTRAINT benefit_doc_comp_fkey FOREIGN KEY (company_form_id) REFERENCES public.company_form(company_form_id);


--
-- Name: benefit_question benefit_ques_benefit_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_question
    ADD CONSTRAINT benefit_ques_benefit_fkey FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: benefit_question benefit_ques_recper_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_question
    ADD CONSTRAINT benefit_ques_recper_fkey FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: benefit_question_h benefit_quesh_benefit_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_question_h
    ADD CONSTRAINT benefit_quesh_benefit_fkey FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: benefit_question_h benefit_quesh_recper_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_question_h
    ADD CONSTRAINT benefit_quesh_recper_fkey FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: benefit_restriction benefit_restriction_bcr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_restriction
    ADD CONSTRAINT benefit_restriction_bcr_fkey FOREIGN KEY (bcr_id) REFERENCES public.hr_benefit_change_reason(bcr_id);


--
-- Name: benefit_restriction benefit_restriction_bencat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefit_restriction
    ADD CONSTRAINT benefit_restriction_bencat_fkey FOREIGN KEY (benefit_cat_id) REFERENCES public.hr_benefit_category(benefit_cat_id);


--
-- Name: hr_benefit_join benenfit_join_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join
    ADD CONSTRAINT benenfit_join_project_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: project bill_at_org_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT bill_at_org_group_fkey FOREIGN KEY (bill_at_org_group) REFERENCES public.org_group(org_group_id);


--
-- Name: change_log change_log_change_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.change_log
    ADD CONSTRAINT change_log_change_person_fkey FOREIGN KEY (change_person_id) REFERENCES public.person(person_id);


--
-- Name: change_log change_log_client_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.change_log
    ADD CONSTRAINT change_log_client_fkey FOREIGN KEY (client_id) REFERENCES public.client(org_group_id);


--
-- Name: change_log change_log_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.change_log
    ADD CONSTRAINT change_log_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: change_log change_log_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.change_log
    ADD CONSTRAINT change_log_project_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: change_log change_log_vendor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.change_log
    ADD CONSTRAINT change_log_vendor_fkey FOREIGN KEY (vendor_id) REFERENCES public.vendor(org_group_id);


--
-- Name: hr_checklist_detail checklist_detail_item_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_checklist_detail
    ADD CONSTRAINT checklist_detail_item_fkey FOREIGN KEY (checklist_item_id) REFERENCES public.hr_checklist_item(item_id);


--
-- Name: client client_client_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_client_status_fkey FOREIGN KEY (client_status_id) REFERENCES public.client_status(client_status_id);


--
-- Name: client client_company_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_company_company_fkey FOREIGN KEY (org_group_id) REFERENCES public.company_base(org_group_id);


--
-- Name: client client_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: client client_dflt_ar_acct; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_dflt_ar_acct FOREIGN KEY (dflt_ar_acct) REFERENCES public.gl_account(gl_account_id);


--
-- Name: client client_dflt_sales_acct; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_dflt_sales_acct FOREIGN KEY (dflt_sales_acct) REFERENCES public.gl_account(gl_account_id);


--
-- Name: client_item_inventory client_item_inventory_vend_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_item_inventory
    ADD CONSTRAINT client_item_inventory_vend_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: client_contact client_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_contact
    ADD CONSTRAINT client_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: client_status client_status_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_status
    ADD CONSTRAINT client_status_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: company_form_folder comp_form_folder_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_form_folder
    ADD CONSTRAINT comp_form_folder_comp_fkey FOREIGN KEY (company_id) REFERENCES public.org_group(org_group_id);


--
-- Name: company_form_folder_join comp_formfoldj_fold_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_form_folder_join
    ADD CONSTRAINT comp_formfoldj_fold_fkey FOREIGN KEY (folder_id) REFERENCES public.company_form_folder(folder_id);


--
-- Name: company_form_folder_join comp_formfoldj_form_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_form_folder_join
    ADD CONSTRAINT comp_formfoldj_form_fkey FOREIGN KEY (form_id) REFERENCES public.company_form(company_form_id);


--
-- Name: company_detail company_det_adv_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_detail
    ADD CONSTRAINT company_det_adv_fkey FOREIGN KEY (employee_advance_account_id) REFERENCES public.gl_account(gl_account_id);


--
-- Name: company_detail company_det_ar_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_detail
    ADD CONSTRAINT company_det_ar_fkey FOREIGN KEY (ar_account_id) REFERENCES public.gl_account(gl_account_id);


--
-- Name: company_detail company_detail_cash_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_detail
    ADD CONSTRAINT company_detail_cash_fkey FOREIGN KEY (cash_account_id) REFERENCES public.gl_account(gl_account_id);


--
-- Name: company_detail company_detail_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_detail
    ADD CONSTRAINT company_detail_company_fkey FOREIGN KEY (org_group_id) REFERENCES public.company_base(org_group_id);


--
-- Name: company_form company_form_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_form
    ADD CONSTRAINT company_form_company_fkey FOREIGN KEY (company_id) REFERENCES public.org_group(org_group_id);


--
-- Name: company_form_folder company_form_folder_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_form_folder
    ADD CONSTRAINT company_form_folder_parent_fkey FOREIGN KEY (parent_folder_id) REFERENCES public.company_form_folder(folder_id);


--
-- Name: company_form_org_join company_form_orgj_ffkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_form_org_join
    ADD CONSTRAINT company_form_orgj_ffkey FOREIGN KEY (folder_id) REFERENCES public.company_form_folder(folder_id);


--
-- Name: company_form_org_join company_form_orgj_ofkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_form_org_join
    ADD CONSTRAINT company_form_orgj_ofkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: company_form company_form_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_form
    ADD CONSTRAINT company_form_type_fkey FOREIGN KEY (form_type_id) REFERENCES public.form_type(form_type_id);


--
-- Name: company_question_detail company_ques_det_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_question_detail
    ADD CONSTRAINT company_ques_det_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.company_base(org_group_id);


--
-- Name: company_question company_question_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_question
    ADD CONSTRAINT company_question_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: contact_question contact_question_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contact_question
    ADD CONSTRAINT contact_question_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: contact_question_detail contact_question_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contact_question_detail
    ADD CONSTRAINT contact_question_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: delivery_detail delivery_detail_delivery_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.delivery_detail
    ADD CONSTRAINT delivery_detail_delivery_fkey FOREIGN KEY (delivery_id) REFERENCES public.delivery(delivery_id);


--
-- Name: delivery_detail delivery_detail_po_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.delivery_detail
    ADD CONSTRAINT delivery_detail_po_fkey FOREIGN KEY (project_po_detail_id) REFERENCES public.project_po_detail(project_po_detail_id);


--
-- Name: delivery delivery_po_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.delivery
    ADD CONSTRAINT delivery_po_fkey FOREIGN KEY (project_po_id) REFERENCES public.project_purchase_order(project_po_id);


--
-- Name: drc_form_event drc_form_event_causing_per_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_form_event
    ADD CONSTRAINT drc_form_event_causing_per_fkey FOREIGN KEY (person_id_causing_event) REFERENCES public.person(person_id);


--
-- Name: drc_form_event drc_form_event_emp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_form_event
    ADD CONSTRAINT drc_form_event_emp_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: drc_form_event drc_form_event_per_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_form_event
    ADD CONSTRAINT drc_form_event_per_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: drc_import_benefit drc_import_ben_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_import_benefit
    ADD CONSTRAINT drc_import_ben_comp_fkey FOREIGN KEY (company_id) REFERENCES public.org_group(org_group_id);


--
-- Name: drc_import_benefit_join drc_import_benjn_ben_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_import_benefit_join
    ADD CONSTRAINT drc_import_benjn_ben_fkey FOREIGN KEY (import_benefit_id) REFERENCES public.drc_import_benefit(imported_benefit_id);


--
-- Name: drc_import_benefit_join drc_import_benjn_sub_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_import_benefit_join
    ADD CONSTRAINT drc_import_benjn_sub_fkey FOREIGN KEY (subscriber_id) REFERENCES public.drc_import_enrollee(enrollee_id);


--
-- Name: drc_import_benefit_join drc_import_benjoin_enr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_import_benefit_join
    ADD CONSTRAINT drc_import_benjoin_enr_fkey FOREIGN KEY (enrollee_id) REFERENCES public.drc_import_enrollee(enrollee_id);


--
-- Name: drc_import_enrollee drc_import_enr_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_import_enrollee
    ADD CONSTRAINT drc_import_enr_comp_fkey FOREIGN KEY (company_id) REFERENCES public.org_group(org_group_id);


--
-- Name: drc_import_enrollee drc_import_enr_pers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drc_import_enrollee
    ADD CONSTRAINT drc_import_enr_pers_fkey FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: e_signature e_signature_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.e_signature
    ADD CONSTRAINT e_signature_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: edi_transaction edi_trans_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.edi_transaction
    ADD CONSTRAINT edi_trans_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.company_base(org_group_id);


--
-- Name: education education_person_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.education
    ADD CONSTRAINT education_person_fky FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: hr_eeo1 eeo1_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_eeo1
    ADD CONSTRAINT eeo1_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: electronic_fund_transfer eft_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.electronic_fund_transfer
    ADD CONSTRAINT eft_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: electronic_fund_transfer eft_wage_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.electronic_fund_transfer
    ADD CONSTRAINT eft_wage_type_fkey FOREIGN KEY (wage_type_id) REFERENCES public.wage_type(wage_type_id);


--
-- Name: employee_rate emp_rate_ratetyp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_rate
    ADD CONSTRAINT emp_rate_ratetyp_fkey FOREIGN KEY (rate_type_id) REFERENCES public.rate_type(rate_type_id);


--
-- Name: employee employee_bank_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_bank_fkey FOREIGN KEY (payroll_bank_code) REFERENCES public.bank_account(bank_account_id);


--
-- Name: employee employee_class_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_class_fkey FOREIGN KEY (benefit_class_id) REFERENCES public.hr_benefit_class(benefit_class_id);


--
-- Name: employee employee_eeo_category_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_eeo_category_fkey FOREIGN KEY (eeo_category_id) REFERENCES public.hr_eeo_category(eeo_category_id);


--
-- Name: employee employee_eeo_race_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_eeo_race_fkey FOREIGN KEY (eeo_race_id) REFERENCES public.hr_eeo_race(eeo_id);


--
-- Name: employee employee_emp_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_emp_status_fkey FOREIGN KEY (status_id) REFERENCES public.hr_employee_status(status_id);


--
-- Name: employee_label_association employee_lbl_ass_emp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_label_association
    ADD CONSTRAINT employee_lbl_ass_emp_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: employee_label_association employee_lbl_ass_lbl_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_label_association
    ADD CONSTRAINT employee_lbl_ass_lbl_fky FOREIGN KEY (employee_label_id) REFERENCES public.employee_label(employee_label_id);


--
-- Name: employee_label_association employee_lbl_ass_who_added_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_label_association
    ADD CONSTRAINT employee_lbl_ass_who_added_fkey FOREIGN KEY (who_added) REFERENCES public.person(person_id);


--
-- Name: employee_label_association employee_lbl_ass_who_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_label_association
    ADD CONSTRAINT employee_lbl_ass_who_comp_fkey FOREIGN KEY (who_completed) REFERENCES public.person(person_id);


--
-- Name: employee employee_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: employee_rate employee_rate_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_rate
    ADD CONSTRAINT employee_rate_person_fkey FOREIGN KEY (person_id) REFERENCES public.employee(person_id);


--
-- Name: employee_not_available ena_employee_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_not_available
    ADD CONSTRAINT ena_employee_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: expense_account expense_account_gl_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense_account
    ADD CONSTRAINT expense_account_gl_fkey FOREIGN KEY (gl_account_id) REFERENCES public.gl_account(gl_account_id);


--
-- Name: expense expense_auth_person_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense
    ADD CONSTRAINT expense_auth_person_fk FOREIGN KEY (auth_person_id) REFERENCES public.person(person_id);


--
-- Name: expense expense_employee_fki; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense
    ADD CONSTRAINT expense_employee_fki FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: expense expense_project_fki; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense
    ADD CONSTRAINT expense_project_fki FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: expense_receipt expense_receipt_exp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense_receipt
    ADD CONSTRAINT expense_receipt_exp_fkey FOREIGN KEY (expense_account_id) REFERENCES public.expense_account(expense_account_id);


--
-- Name: expense_receipt expense_receipt_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense_receipt
    ADD CONSTRAINT expense_receipt_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: expense_receipt expense_receipt_proj_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense_receipt
    ADD CONSTRAINT expense_receipt_proj_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: expense_receipt expense_receipt_who_app_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense_receipt
    ADD CONSTRAINT expense_receipt_who_app_fkey FOREIGN KEY (who_approved) REFERENCES public.person(person_id);


--
-- Name: expense_receipt expense_receipt_who_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense_receipt
    ADD CONSTRAINT expense_receipt_who_fkey FOREIGN KEY (who_uploaded) REFERENCES public.person(person_id);


--
-- Name: form_type form_type_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.form_type
    ADD CONSTRAINT form_type_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: garnishment_type garn_typ_wage_typ_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.garnishment_type
    ADD CONSTRAINT garn_typ_wage_typ_fkey FOREIGN KEY (wage_type_id) REFERENCES public.wage_type(wage_type_id);


--
-- Name: garnishment garnishment_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.garnishment
    ADD CONSTRAINT garnishment_person_fkey FOREIGN KEY (person_id) REFERENCES public.employee(person_id);


--
-- Name: garnishment garnishment_remit_to_fksy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.garnishment
    ADD CONSTRAINT garnishment_remit_to_fksy FOREIGN KEY (remit_to) REFERENCES public.address(address_id);


--
-- Name: garnishment garnishment_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.garnishment
    ADD CONSTRAINT garnishment_type_fkey FOREIGN KEY (garnishment_type_id) REFERENCES public.garnishment_type(garnishment_type_id);


--
-- Name: gl_account gl_account_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gl_account
    ADD CONSTRAINT gl_account_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: org_group_association group_association_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group_association
    ADD CONSTRAINT group_association_group_id_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: org_group_hierarchy group_hierarchy_child_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group_hierarchy
    ADD CONSTRAINT group_hierarchy_child_group_id_fkey FOREIGN KEY (child_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: org_group_hierarchy group_hierarchy_parent_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group_hierarchy
    ADD CONSTRAINT group_hierarchy_parent_group_id_fkey FOREIGN KEY (parent_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: hr_benefit_config he_benconf_benefit_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_config
    ADD CONSTRAINT he_benconf_benefit_fkey FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: holiday holiday_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.holiday
    ADD CONSTRAINT holiday_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: hr_accrual hr_accrual_employee_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_accrual
    ADD CONSTRAINT hr_accrual_employee_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: hr_accrual hr_accural_benefit_account_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_accrual
    ADD CONSTRAINT hr_accural_benefit_account_fkey FOREIGN KEY (benefit_account) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: hr_benefit_change_reason hr_bcr_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_change_reason
    ADD CONSTRAINT hr_bcr_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: hr_benefit_class hr_ben_calss_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_class
    ADD CONSTRAINT hr_ben_calss_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: hr_benefit_category_answer hr_ben_cat_ans_ben_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_category_answer
    ADD CONSTRAINT hr_ben_cat_ans_ben_fkey FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: hr_benefit_category_answer hr_ben_cat_ans_ques_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_category_answer
    ADD CONSTRAINT hr_ben_cat_ans_ques_fkey FOREIGN KEY (question_id) REFERENCES public.hr_benefit_category_question(question_id);


--
-- Name: hr_benefit_category_question hr_ben_cat_ques_cat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_category_question
    ADD CONSTRAINT hr_ben_cat_ques_cat_fkey FOREIGN KEY (benefit_cat_id) REFERENCES public.hr_benefit_category(benefit_cat_id);


--
-- Name: hr_benefit_join hr_ben_join_bcat_chk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join
    ADD CONSTRAINT hr_ben_join_bcat_chk FOREIGN KEY (benefit_cat_id) REFERENCES public.hr_benefit_category(benefit_cat_id);


--
-- Name: hr_benefit_join hr_ben_join_ben_chk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join
    ADD CONSTRAINT hr_ben_join_ben_chk FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: hr_benefit_join_h hr_ben_join_rpi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join_h
    ADD CONSTRAINT hr_ben_join_rpi FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: hr_benefit_category hr_bencat_oescrn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_category
    ADD CONSTRAINT hr_bencat_oescrn_fkey FOREIGN KEY (open_enrollment_screen_id) REFERENCES public.screen(screen_id);


--
-- Name: hr_benefit_category hr_bencat_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_category
    ADD CONSTRAINT hr_bencat_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: hr_benefit_category hr_bencat_oscrn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_category
    ADD CONSTRAINT hr_bencat_oscrn_fkey FOREIGN KEY (onboarding_screen_id) REFERENCES public.screen(screen_id);


--
-- Name: hr_benefit hr_benefit_ben_cat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit
    ADD CONSTRAINT hr_benefit_ben_cat_fkey FOREIGN KEY (benefit_cat_id) REFERENCES public.hr_benefit_category(benefit_cat_id);


--
-- Name: hr_benefit_join hr_benefit_benef_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join
    ADD CONSTRAINT hr_benefit_benef_fkey FOREIGN KEY (benefit_config_id) REFERENCES public.hr_benefit_config(benefit_config_id);


--
-- Name: hr_benefit_join hr_benefit_join_bcr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join
    ADD CONSTRAINT hr_benefit_join_bcr_fkey FOREIGN KEY (bcr_id) REFERENCES public.hr_benefit_change_reason(bcr_id);


--
-- Name: hr_benefit_join hr_benefit_join_cpers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join
    ADD CONSTRAINT hr_benefit_join_cpers_fkey FOREIGN KEY (covered_person) REFERENCES public.person(person_id);


--
-- Name: hr_benefit_join hr_benefit_join_hrel_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join
    ADD CONSTRAINT hr_benefit_join_hrel_fkey FOREIGN KEY (relationship_id) REFERENCES public.hr_empl_dependent(relationship_id);


--
-- Name: hr_benefit_join hr_benefit_join_levent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join
    ADD CONSTRAINT hr_benefit_join_levent_fkey FOREIGN KEY (life_event_id) REFERENCES public.life_event(life_event_id);


--
-- Name: hr_benefit_join hr_benefit_join_ppers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join
    ADD CONSTRAINT hr_benefit_join_ppers_fkey FOREIGN KEY (paying_person) REFERENCES public.person(person_id);


--
-- Name: hr_benefit_join hr_benefit_join_recper_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_join
    ADD CONSTRAINT hr_benefit_join_recper_fkey FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: hr_benefit hr_benefit_ooscreen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit
    ADD CONSTRAINT hr_benefit_ooscreen_fkey FOREIGN KEY (open_enrollment_screen_id) REFERENCES public.screen(screen_id);


--
-- Name: hr_benefit hr_benefit_oscreen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit
    ADD CONSTRAINT hr_benefit_oscreen_fkey FOREIGN KEY (onboarding_screen_id) REFERENCES public.screen(screen_id);


--
-- Name: hr_benefit_package_join hr_benefit_pj_benefit_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_package_join
    ADD CONSTRAINT hr_benefit_pj_benefit_fkey FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: hr_benefit hr_benefit_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit
    ADD CONSTRAINT hr_benefit_product_fkey FOREIGN KEY (product_id) REFERENCES public.product_service(product_service_id);


--
-- Name: hr_benefit hr_benefit_provider_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit
    ADD CONSTRAINT hr_benefit_provider_fkey FOREIGN KEY (provider_id) REFERENCES public.vendor(org_group_id);


--
-- Name: hr_benefit hr_benefit_replace_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit
    ADD CONSTRAINT hr_benefit_replace_fkey FOREIGN KEY (benefit_id_replaced_by) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: hr_benefit_rider hr_benefit_rider_base_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_rider
    ADD CONSTRAINT hr_benefit_rider_base_fkey FOREIGN KEY (base_benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: hr_benefit_rider hr_benefit_rider_rider_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_rider
    ADD CONSTRAINT hr_benefit_rider_rider_fkey FOREIGN KEY (rider_benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: hr_benefit hr_benefit_wage_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit
    ADD CONSTRAINT hr_benefit_wage_type_fkey FOREIGN KEY (wage_type_id) REFERENCES public.wage_type(wage_type_id);


--
-- Name: hr_billing_status_history hr_bill_stat_hist_pers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_billing_status_history
    ADD CONSTRAINT hr_bill_stat_hist_pers_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: hr_billing_status_history hr_bill_stat_stat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_billing_status_history
    ADD CONSTRAINT hr_bill_stat_stat_fkey FOREIGN KEY (billing_status_id) REFERENCES public.hr_billing_status(billing_status_id);


--
-- Name: hr_benefit_project_join hr_bpjoin_config_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_project_join
    ADD CONSTRAINT hr_bpjoin_config_fky FOREIGN KEY (benefit_config_id) REFERENCES public.hr_benefit_config(benefit_config_id);


--
-- Name: hr_benefit_project_join hr_bpjoin_prokect_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_project_join
    ADD CONSTRAINT hr_bpjoin_prokect_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: hr_checklist_detail hr_checklist_detail_employee_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_checklist_detail
    ADD CONSTRAINT hr_checklist_detail_employee_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: hr_checklist_detail hr_checklist_detail_super_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_checklist_detail
    ADD CONSTRAINT hr_checklist_detail_super_fkey FOREIGN KEY (supervisor_id) REFERENCES public.employee(person_id);


--
-- Name: hr_checklist_item hr_checklist_item_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_checklist_item
    ADD CONSTRAINT hr_checklist_item_status_fkey FOREIGN KEY (employee_status_id) REFERENCES public.hr_employee_status(status_id);


--
-- Name: hr_checklist_item hr_chklst_item_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_checklist_item
    ADD CONSTRAINT hr_chklst_item_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: hr_checklist_item hr_chklstitm_compform_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_checklist_item
    ADD CONSTRAINT hr_chklstitm_compform_fkey FOREIGN KEY (company_form_id) REFERENCES public.company_form(company_form_id);


--
-- Name: hr_checklist_item hr_chklstitm_screen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_checklist_item
    ADD CONSTRAINT hr_chklstitm_screen_fkey FOREIGN KEY (screen_id) REFERENCES public.screen(screen_id);


--
-- Name: hr_checklist_item hr_chklstitm_screengrp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_checklist_item
    ADD CONSTRAINT hr_chklstitm_screengrp_fkey FOREIGN KEY (screen_group_id) REFERENCES public.screen_group(screen_group_id);


--
-- Name: hr_employee_beneficiary hr_emp_benefiary_rpi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_beneficiary
    ADD CONSTRAINT hr_emp_benefiary_rpi FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: hr_employee_beneficiary hr_emp_beneficiary_eb_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_beneficiary
    ADD CONSTRAINT hr_emp_beneficiary_eb_id FOREIGN KEY (benefit_join_id) REFERENCES public.hr_benefit_join(benefit_join_id);


--
-- Name: hr_employee_beneficiary_h hr_emp_beneficiary_rpi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_beneficiary_h
    ADD CONSTRAINT hr_emp_beneficiary_rpi FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: hr_employee_event hr_emp_evt_employee_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_event
    ADD CONSTRAINT hr_emp_evt_employee_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: hr_empl_dependent_cr hr_empl_dep_cr_approver_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_dependent_cr
    ADD CONSTRAINT hr_empl_dep_cr_approver_fkey FOREIGN KEY (approver_id) REFERENCES public.person(person_id);


--
-- Name: hr_empl_dependent_cr hr_empl_dep_cr_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_dependent_cr
    ADD CONSTRAINT hr_empl_dep_cr_project_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: hr_empl_dependent_cr hr_empl_dep_cr_real_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_dependent_cr
    ADD CONSTRAINT hr_empl_dep_cr_real_fkey FOREIGN KEY (real_record_id) REFERENCES public.hr_empl_dependent(relationship_id);


--
-- Name: hr_empl_dependent_cr hr_empl_dep_cr_recvhg_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_dependent_cr
    ADD CONSTRAINT hr_empl_dep_cr_recvhg_fkey FOREIGN KEY (change_record_id) REFERENCES public.hr_empl_dependent(relationship_id);


--
-- Name: hr_empl_dependent_cr hr_empl_dep_cr_requestor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_dependent_cr
    ADD CONSTRAINT hr_empl_dep_cr_requestor_fkey FOREIGN KEY (requestor_id) REFERENCES public.person(person_id);


--
-- Name: hr_empl_dependent hr_empl_dep_dep_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_dependent
    ADD CONSTRAINT hr_empl_dep_dep_fkey FOREIGN KEY (dependent_id) REFERENCES public.person(person_id);


--
-- Name: hr_empl_dependent hr_empl_dep_empl_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_dependent
    ADD CONSTRAINT hr_empl_dep_empl_id_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: hr_empl_eval_detail hr_empl_eval_cat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_eval_detail
    ADD CONSTRAINT hr_empl_eval_cat_fkey FOREIGN KEY (cat_id) REFERENCES public.hr_eval_category(eval_cat_id);


--
-- Name: hr_empl_eval_detail hr_empl_eval_detail_eval_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_eval_detail
    ADD CONSTRAINT hr_empl_eval_detail_eval_fkey FOREIGN KEY (eval_id) REFERENCES public.hr_employee_eval(employee_eval_id);


--
-- Name: hr_employee_eval hr_employee_eval_emp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_eval
    ADD CONSTRAINT hr_employee_eval_emp_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: hr_employee_eval hr_employee_eval_super_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_eval
    ADD CONSTRAINT hr_employee_eval_super_fkey FOREIGN KEY (supervisor_id) REFERENCES public.employee(person_id);


--
-- Name: hr_employee_event hr_employee_event_supervisor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_event
    ADD CONSTRAINT hr_employee_event_supervisor_fkey FOREIGN KEY (supervisor_id) REFERENCES public.person(person_id);


--
-- Name: hr_employee_status hr_empstat_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_employee_status
    ADD CONSTRAINT hr_empstat_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: hr_emergency_contact hr_emrcont_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_emergency_contact
    ADD CONSTRAINT hr_emrcont_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: hr_eval_category hr_eval_cat_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_eval_category
    ADD CONSTRAINT hr_eval_cat_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: hr_note_category hr_notecat_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_note_category
    ADD CONSTRAINT hr_notecat_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: hr_position hr_pos_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_position
    ADD CONSTRAINT hr_pos_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: hr_position hr_position_class_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_position
    ADD CONSTRAINT hr_position_class_fkey FOREIGN KEY (benefit_class_id) REFERENCES public.hr_benefit_class(benefit_class_id);


--
-- Name: hr_empl_status_history hr_status_hist_employee_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_status_history
    ADD CONSTRAINT hr_status_hist_employee_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: hr_empl_status_history hr_status_hist_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_empl_status_history
    ADD CONSTRAINT hr_status_hist_status_fkey FOREIGN KEY (status_id) REFERENCES public.hr_employee_status(status_id);


--
-- Name: hr_training_category hr_traincat_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_training_category
    ADD CONSTRAINT hr_traincat_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: hr_training_category hr_training_cat_cleint_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_training_category
    ADD CONSTRAINT hr_training_cat_cleint_fkey FOREIGN KEY (client_id) REFERENCES public.org_group(org_group_id);


--
-- Name: hr_training_detail hr_training_detail_cat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_training_detail
    ADD CONSTRAINT hr_training_detail_cat_fkey FOREIGN KEY (cat_id) REFERENCES public.hr_training_category(cat_id);


--
-- Name: hr_training_detail hr_training_detail_empl_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_training_detail
    ADD CONSTRAINT hr_training_detail_empl_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: hr_wage hr_wage_employee_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_wage
    ADD CONSTRAINT hr_wage_employee_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: hr_wage hr_wage_position_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_wage
    ADD CONSTRAINT hr_wage_position_fkey FOREIGN KEY (position_id) REFERENCES public.hr_position(position_id);


--
-- Name: hr_wage hr_wage_type_fley; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_wage
    ADD CONSTRAINT hr_wage_type_fley FOREIGN KEY (wage_type_id) REFERENCES public.wage_type(wage_type_id);


--
-- Name: import_column import_column_filter_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.import_column
    ADD CONSTRAINT import_column_filter_fkey FOREIGN KEY (import_filter_id) REFERENCES public.import_filter(import_filter_id);


--
-- Name: import_filter import_filter_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.import_filter
    ADD CONSTRAINT import_filter_type_fkey FOREIGN KEY (import_type_id) REFERENCES public.import_type(import_type_id);


--
-- Name: import_type import_type_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.import_type
    ADD CONSTRAINT import_type_company_fkey FOREIGN KEY (company_id) REFERENCES public.org_group(org_group_id);


--
-- Name: insurance_location_code ins_loc_cde_ben_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.insurance_location_code
    ADD CONSTRAINT ins_loc_cde_ben_fkey FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: insurance_location_code ins_loc_code_ben_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.insurance_location_code
    ADD CONSTRAINT ins_loc_code_ben_fkey FOREIGN KEY (benefit_class_id) REFERENCES public.hr_benefit_class(benefit_class_id);


--
-- Name: insurance_location_code ins_loc_code_empstat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.insurance_location_code
    ADD CONSTRAINT ins_loc_code_empstat_fkey FOREIGN KEY (employee_status_id) REFERENCES public.hr_employee_status(status_id);


--
-- Name: interface_log interface_log_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.interface_log
    ADD CONSTRAINT interface_log_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_base(org_group_id);


--
-- Name: invoice_line_item inv_line_item_gl_acct_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_line_item
    ADD CONSTRAINT inv_line_item_gl_acct_fkey FOREIGN KEY (expense_account_id) REFERENCES public.gl_account(gl_account_id);


--
-- Name: invoice_line_item inv_line_item_invoice_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_line_item
    ADD CONSTRAINT inv_line_item_invoice_fkey FOREIGN KEY (invoice_id) REFERENCES public.invoice(invoice_id);


--
-- Name: invoice_line_item inv_line_item_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_line_item
    ADD CONSTRAINT inv_line_item_product_fkey FOREIGN KEY (product_id) REFERENCES public.product_service(product_service_id);


--
-- Name: inventory inventory_location_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventory
    ADD CONSTRAINT inventory_location_fkey FOREIGN KEY (location_id) REFERENCES public.org_group(org_group_id);


--
-- Name: inventory inventory_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventory
    ADD CONSTRAINT inventory_product_fkey FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- Name: invoice invoice_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_company_fkey FOREIGN KEY (customer_id) REFERENCES public.company_base(org_group_id);


--
-- Name: invoice invoice_gl_account_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_gl_account_id_fkey FOREIGN KEY (ar_account_id) REFERENCES public.gl_account(gl_account_id);


--
-- Name: invoice_line_item invoice_li_project_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_line_item
    ADD CONSTRAINT invoice_li_project_fk FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: invoice invoice_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: item item_accepting_person_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_accepting_person_fky FOREIGN KEY (person_accepting_reimbursement) REFERENCES public.person(person_id);


--
-- Name: item item_chg_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_chg_person_fkey FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: item_h item_h_accepting_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_h
    ADD CONSTRAINT item_h_accepting_person_fkey FOREIGN KEY (person_accepting_reimbursement) REFERENCES public.person(person_id);


--
-- Name: item_h item_h_reimbursement_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_h
    ADD CONSTRAINT item_h_reimbursement_person_fkey FOREIGN KEY (reimbursement_person_id) REFERENCES public.person(person_id);


--
-- Name: item_inspection item_inspection_inspector_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_inspection
    ADD CONSTRAINT item_inspection_inspector_fkey FOREIGN KEY (person_inspecting) REFERENCES public.person(person_id);


--
-- Name: item_inspection item_inspection_item_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_inspection
    ADD CONSTRAINT item_inspection_item_fkey FOREIGN KEY (item_id) REFERENCES public.item(item_id);


--
-- Name: item_inspection item_inspection_recorder_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_inspection
    ADD CONSTRAINT item_inspection_recorder_fkey FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: item item_location_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_location_fkey FOREIGN KEY (location_id) REFERENCES public.org_group(org_group_id);


--
-- Name: item item_lot_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_lot_fkey FOREIGN KEY (lot_id) REFERENCES public.lot(lot_id);


--
-- Name: item item_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_parent_fkey FOREIGN KEY (parent_item_id) REFERENCES public.item(item_id);


--
-- Name: item item_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_product_fkey FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- Name: item item_reimbursement_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_reimbursement_person_fkey FOREIGN KEY (reimbursement_person_id) REFERENCES public.person(person_id);


--
-- Name: life_event life_event_bcr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.life_event
    ADD CONSTRAINT life_event_bcr_fkey FOREIGN KEY (bcr_id) REFERENCES public.hr_benefit_change_reason(bcr_id);


--
-- Name: life_event life_event_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.life_event
    ADD CONSTRAINT life_event_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: life_event life_event_recpers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.life_event
    ADD CONSTRAINT life_event_recpers_fkey FOREIGN KEY (reporting_person_id) REFERENCES public.person(person_id);


--
-- Name: prophet_login_exception login_exception_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prophet_login_exception
    ADD CONSTRAINT login_exception_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_base(org_group_id);


--
-- Name: prophet_login_exception login_exception_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prophet_login_exception
    ADD CONSTRAINT login_exception_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: prophet_login_exception login_exception_screen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prophet_login_exception
    ADD CONSTRAINT login_exception_screen_fkey FOREIGN KEY (screen_group_id) REFERENCES public.screen_group(screen_group_id);


--
-- Name: prophet_login_exception login_exception_security_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prophet_login_exception
    ADD CONSTRAINT login_exception_security_fkey FOREIGN KEY (security_group_id) REFERENCES public.security_group(security_group_id);


--
-- Name: prophet_login login_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prophet_login
    ADD CONSTRAINT login_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: prophet_login login_screen_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prophet_login
    ADD CONSTRAINT login_screen_group_fkey FOREIGN KEY (screen_group_id) REFERENCES public.screen_group(screen_group_id);


--
-- Name: prophet_login login_security_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prophet_login
    ADD CONSTRAINT login_security_group_fkey FOREIGN KEY (security_group_id) REFERENCES public.security_group(security_group_id);


--
-- Name: project managing_employee_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT managing_employee_fkey FOREIGN KEY (managing_employee) REFERENCES public.employee(person_id);


--
-- Name: message_attachment message_attachment_message_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_attachment
    ADD CONSTRAINT message_attachment_message_id_fkey FOREIGN KEY (message_id) REFERENCES public.message(message_id);


--
-- Name: message_to message_to_message_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_to
    ADD CONSTRAINT message_to_message_id_fkey FOREIGN KEY (message_id) REFERENCES public.message(message_id);


--
-- Name: message_to message_to_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_to
    ADD CONSTRAINT message_to_person_id_fkey FOREIGN KEY (to_person_id) REFERENCES public.person(person_id);


--
-- Name: message messages_from_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT messages_from_person FOREIGN KEY (from_person_id) REFERENCES public.person(person_id);


--
-- Name: onboarding_task_complete ob_task_complete_pers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.onboarding_task_complete
    ADD CONSTRAINT ob_task_complete_pers_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: onboarding_task_complete ob_task_complete_task_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.onboarding_task_complete
    ADD CONSTRAINT ob_task_complete_task_fkey FOREIGN KEY (onboarding_task_id) REFERENCES public.onboarding_task(onboarding_task_id);


--
-- Name: onboarding_config onboarding_config_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.onboarding_config
    ADD CONSTRAINT onboarding_config_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: onboarding_task onboarding_task_config_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.onboarding_task
    ADD CONSTRAINT onboarding_task_config_fkey FOREIGN KEY (onboarding_config_id) REFERENCES public.onboarding_config(onboarding_config_id);


--
-- Name: onboarding_task onboarding_task_screen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.onboarding_task
    ADD CONSTRAINT onboarding_task_screen_fkey FOREIGN KEY (screen_id) REFERENCES public.screen(screen_id);


--
-- Name: org_group org_group_benclass_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group
    ADD CONSTRAINT org_group_benclass_fkey FOREIGN KEY (benefit_class_id) REFERENCES public.hr_benefit_class(benefit_class_id);


--
-- Name: company_base org_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_base
    ADD CONSTRAINT org_group_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: org_group org_group_pay_sch_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group
    ADD CONSTRAINT org_group_pay_sch_fkey FOREIGN KEY (pay_schedule_id) REFERENCES public.pay_schedule(pay_schedule_id);


--
-- Name: org_group org_group_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group
    ADD CONSTRAINT org_group_project_fkey FOREIGN KEY (default_project_id) REFERENCES public.project(project_id);


--
-- Name: org_group_association org_grp_recper_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group_association
    ADD CONSTRAINT org_grp_recper_fkey FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: overtime_approval overtime_app_emp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.overtime_approval
    ADD CONSTRAINT overtime_app_emp_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: overtime_approval overtime_app_super_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.overtime_approval
    ADD CONSTRAINT overtime_app_super_fkey FOREIGN KEY (supervisor_id) REFERENCES public.employee(person_id);


--
-- Name: org_group owning_entity_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group
    ADD CONSTRAINT owning_entity_fkey FOREIGN KEY (owning_entity_id) REFERENCES public.company_base(org_group_id);


--
-- Name: hr_benefit_package_join package_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_benefit_package_join
    ADD CONSTRAINT package_id FOREIGN KEY (package_id) REFERENCES public.hr_benefit_package(package_id);


--
-- Name: password_history password_history_pers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_history
    ADD CONSTRAINT password_history_pers_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: pay_schedule_period pay_sch_period_sched_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pay_schedule_period
    ADD CONSTRAINT pay_sch_period_sched_fkey FOREIGN KEY (pay_schedule_id) REFERENCES public.pay_schedule(pay_schedule_id);


--
-- Name: pay_schedule pay_schedule_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pay_schedule
    ADD CONSTRAINT pay_schedule_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: payment_info payment_info_benefit_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_info
    ADD CONSTRAINT payment_info_benefit_fkey FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: payment_info payment_info_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_info
    ADD CONSTRAINT payment_info_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: project_employee_join pej_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_employee_join
    ADD CONSTRAINT pej_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: per_diem_exception per_diem_exception_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.per_diem_exception
    ADD CONSTRAINT per_diem_exception_person_fkey FOREIGN KEY (person_id) REFERENCES public.employee(person_id);


--
-- Name: per_diem_exception per_diem_exception_proj_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.per_diem_exception
    ADD CONSTRAINT per_diem_exception_proj_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: person_changed person_changed_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_changed
    ADD CONSTRAINT person_changed_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: person_change_request person_chgreq_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_change_request
    ADD CONSTRAINT person_chgreq_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: person person_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_base(org_group_id);


--
-- Name: person_cr person_cr_approver_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_cr
    ADD CONSTRAINT person_cr_approver_fkey FOREIGN KEY (approver_id) REFERENCES public.person(person_id);


--
-- Name: person_cr person_cr_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_cr
    ADD CONSTRAINT person_cr_project_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: person_cr person_cr_real_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_cr
    ADD CONSTRAINT person_cr_real_fkey FOREIGN KEY (real_record_id) REFERENCES public.person(person_id);


--
-- Name: person_cr person_cr_recvhg_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_cr
    ADD CONSTRAINT person_cr_recvhg_fkey FOREIGN KEY (change_record_id) REFERENCES public.person(person_id);


--
-- Name: person_cr person_cr_requestor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_cr
    ADD CONSTRAINT person_cr_requestor_fkey FOREIGN KEY (requestor_id) REFERENCES public.person(person_id);


--
-- Name: person person_dflt_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_dflt_project_fkey FOREIGN KEY (default_project_id) REFERENCES public.project(project_id);


--
-- Name: person_email person_email_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_email
    ADD CONSTRAINT person_email_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: person_form person_form_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_form
    ADD CONSTRAINT person_form_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: person_form person_form_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_form
    ADD CONSTRAINT person_form_type_fkey FOREIGN KEY (form_type_id) REFERENCES public.form_type(form_type_id);


--
-- Name: org_group_association person_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.org_group_association
    ADD CONSTRAINT person_group_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: person person_i9p1_person_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_i9p1_person_fky FOREIGN KEY (i9p1_person) REFERENCES public.person(person_id);


--
-- Name: person person_i9p2_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_i9p2_person_fkey FOREIGN KEY (i9p2_person) REFERENCES public.person(person_id);


--
-- Name: person_note person_note_cat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_note
    ADD CONSTRAINT person_note_cat_fkey FOREIGN KEY (cat_id) REFERENCES public.hr_note_category(cat_id);


--
-- Name: person_note person_note_emp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person_note
    ADD CONSTRAINT person_note_emp_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: person person_recper_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_recper_fkey FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: personal_reference personal_ref_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personal_reference
    ADD CONSTRAINT personal_ref_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: phone_cr phone_cr_approver_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phone_cr
    ADD CONSTRAINT phone_cr_approver_fkey FOREIGN KEY (approver_id) REFERENCES public.person(person_id);


--
-- Name: phone_cr phone_cr_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phone_cr
    ADD CONSTRAINT phone_cr_project_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: phone_cr phone_cr_real_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phone_cr
    ADD CONSTRAINT phone_cr_real_fkey FOREIGN KEY (real_record_id) REFERENCES public.phone(phone_id);


--
-- Name: phone_cr phone_cr_recvhg_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phone_cr
    ADD CONSTRAINT phone_cr_recvhg_fkey FOREIGN KEY (change_record_id) REFERENCES public.phone(phone_id);


--
-- Name: phone_cr phone_cr_requestor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phone_cr
    ADD CONSTRAINT phone_cr_requestor_fkey FOREIGN KEY (requestor_id) REFERENCES public.person(person_id);


--
-- Name: phone phone_org_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phone
    ADD CONSTRAINT phone_org_group_fkey FOREIGN KEY (org_group_join) REFERENCES public.org_group(org_group_id);


--
-- Name: phone phone_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phone
    ADD CONSTRAINT phone_person_fkey FOREIGN KEY (person_join) REFERENCES public.person(person_id);


--
-- Name: physician physicians_benefit_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.physician
    ADD CONSTRAINT physicians_benefit_fkey FOREIGN KEY (benefit_join_id) REFERENCES public.hr_benefit_join(benefit_join_id);


--
-- Name: project_view_join ppv_child_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_view_join
    ADD CONSTRAINT ppv_child_fkey FOREIGN KEY (child_project_view_id) REFERENCES public.project_view(project_view_id);


--
-- Name: project_view_join ppv_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_view_join
    ADD CONSTRAINT ppv_parent_fkey FOREIGN KEY (parent_project_view_id) REFERENCES public.project_view(project_view_id);


--
-- Name: previous_employment prev_emp_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.previous_employment
    ADD CONSTRAINT prev_emp_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: process_history process_history_sched_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.process_history
    ADD CONSTRAINT process_history_sched_fkey FOREIGN KEY (process_schedule_id) REFERENCES public.process_schedule(process_schedule_id);


--
-- Name: product_service prod_serv_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_service
    ADD CONSTRAINT prod_serv_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: product_type prod_typ_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_type
    ADD CONSTRAINT prod_typ_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: product_attribute_choice product_att_choice_prod_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_attribute_choice
    ADD CONSTRAINT product_att_choice_prod_fkey FOREIGN KEY (product_attribute_id) REFERENCES public.product_attribute(product_attribute_id);


--
-- Name: product_attribute_dependency product_attr_dep_ch_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_attribute_dependency
    ADD CONSTRAINT product_attr_dep_ch_fkey FOREIGN KEY (dependent_attribute_id) REFERENCES public.product_attribute(product_attribute_id);


--
-- Name: product_attribute_dependency product_attr_dep_prod_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_attribute_dependency
    ADD CONSTRAINT product_attr_dep_prod_fkey FOREIGN KEY (product_attribute_id) REFERENCES public.product_attribute(product_attribute_id);


--
-- Name: product_attribute product_attribute_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_attribute
    ADD CONSTRAINT product_attribute_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: product product_product_service_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_product_service_fkey FOREIGN KEY (product_id) REFERENCES public.product_service(product_service_id);


--
-- Name: product product_product_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_product_type_fkey FOREIGN KEY (product_type_id) REFERENCES public.product_type(product_type_id);


--
-- Name: product_service product_service_exp_acct_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_service
    ADD CONSTRAINT product_service_exp_acct_fkey FOREIGN KEY (expense_account_id) REFERENCES public.gl_account(gl_account_id);


--
-- Name: product_type product_type_self_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_type
    ADD CONSTRAINT product_type_self_fkey FOREIGN KEY (parent_product_type_id) REFERENCES public.product_type(product_type_id);


--
-- Name: product product_vendor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_vendor_fkey FOREIGN KEY (vendor_id) REFERENCES public.vendor(org_group_id);


--
-- Name: project project_address_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_address_fkey FOREIGN KEY (address_id) REFERENCES public.address(address_id);


--
-- Name: project project_app_per_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_app_per_fkey FOREIGN KEY (approving_person) REFERENCES public.person(person_id);


--
-- Name: project_category project_catagory_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_category
    ADD CONSTRAINT project_catagory_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: project project_category_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_category_fkey FOREIGN KEY (project_category_id) REFERENCES public.project_category(project_category_id);


--
-- Name: project_checklist_detail project_chklst_chklst_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_checklist_detail
    ADD CONSTRAINT project_chklst_chklst_fkey FOREIGN KEY (route_stop_checklist_id) REFERENCES public.route_stop_checklist(route_stop_checklist_id);


--
-- Name: project_checklist_detail project_chklst_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_checklist_detail
    ADD CONSTRAINT project_chklst_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: project_checklist_detail project_chklst_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_checklist_detail
    ADD CONSTRAINT project_chklst_project_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: project_comment project_comment_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_comment
    ADD CONSTRAINT project_comment_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: project_comment project_comment_shift_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_comment
    ADD CONSTRAINT project_comment_shift_fkey FOREIGN KEY (project_shift_id) REFERENCES public.project_shift(project_shift_id);


--
-- Name: project project_current_route_stop_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_current_route_stop_fkey FOREIGN KEY (current_route_stop) REFERENCES public.route_stop(route_stop_id);


--
-- Name: project_employee_join project_emp_join_confper_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_employee_join
    ADD CONSTRAINT project_emp_join_confper_fkey FOREIGN KEY (confirmed_person_id) REFERENCES public.person(person_id);


--
-- Name: project_employee_join project_emp_join_verifper_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_employee_join
    ADD CONSTRAINT project_emp_join_verifper_fkey FOREIGN KEY (verified_person_id) REFERENCES public.person(person_id);


--
-- Name: project_employee_history project_employee_hist_cpers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_employee_history
    ADD CONSTRAINT project_employee_hist_cpers_fkey FOREIGN KEY (change_person_id) REFERENCES public.person(person_id);


--
-- Name: project_employee_history project_employee_hist_per_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_employee_history
    ADD CONSTRAINT project_employee_hist_per_fky FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: project_employee_history project_employee_hist_sch_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_employee_history
    ADD CONSTRAINT project_employee_hist_sch_fkey FOREIGN KEY (project_shift_id) REFERENCES public.project_shift(project_shift_id);


--
-- Name: project_employee_join project_employee_join_schedule_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_employee_join
    ADD CONSTRAINT project_employee_join_schedule_fkey FOREIGN KEY (project_shift_id) REFERENCES public.project_shift(project_shift_id);


--
-- Name: project_form project_form_shift_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_form
    ADD CONSTRAINT project_form_shift_fky FOREIGN KEY (project_shift_id) REFERENCES public.project_shift(project_shift_id);


--
-- Name: project_form project_form_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_form
    ADD CONSTRAINT project_form_type_fkey FOREIGN KEY (form_type_id) REFERENCES public.form_type(form_type_id);


--
-- Name: project_inventory_email project_inventory_email_per_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_inventory_email
    ADD CONSTRAINT project_inventory_email_per_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: project_inventory_email project_inventory_email_proj_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_inventory_email
    ADD CONSTRAINT project_inventory_email_proj_fky FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: project project_owning_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_owning_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: project_phase project_phase_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_phase
    ADD CONSTRAINT project_phase_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: project_po_detail project_po_det_po_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_po_detail
    ADD CONSTRAINT project_po_det_po_fkey FOREIGN KEY (project_po_id) REFERENCES public.project_purchase_order(project_po_id);


--
-- Name: project_po_detail project_po_detail_item_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_po_detail
    ADD CONSTRAINT project_po_detail_item_fkey FOREIGN KEY (client_item_inventory_id) REFERENCES public.client_item_inventory(client_item_inventory_id);


--
-- Name: project project_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_product_fkey FOREIGN KEY (product_id) REFERENCES public.product_service(product_service_id);


--
-- Name: project project_proj_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_proj_type_fkey FOREIGN KEY (project_type_id) REFERENCES public.project_type(project_type_id);


--
-- Name: project_purchase_order project_purchase_order_proj_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_purchase_order
    ADD CONSTRAINT project_purchase_order_proj_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: project project_rate_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_rate_type_fkey FOREIGN KEY (rate_type_id) REFERENCES public.rate_type(rate_type_id);


--
-- Name: project_report_person project_report_person_client_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_report_person
    ADD CONSTRAINT project_report_person_client_fk FOREIGN KEY (client_id) REFERENCES public.client(org_group_id);


--
-- Name: project_report_person project_report_person_person_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_report_person
    ADD CONSTRAINT project_report_person_person_fk FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: project project_req_orggrp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_req_orggrp_fkey FOREIGN KEY (requesting_org_group) REFERENCES public.org_group(org_group_id);


--
-- Name: project project_route_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_route_fkey FOREIGN KEY (route_id) REFERENCES public.route(route_id);


--
-- Name: project_shift project_shift_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_shift
    ADD CONSTRAINT project_shift_project_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: project_status project_status_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_status
    ADD CONSTRAINT project_status_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: project project_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_status_fkey FOREIGN KEY (project_status_id) REFERENCES public.project_status(project_status_id);


--
-- Name: project project_subject_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_subject_person_fkey FOREIGN KEY (subject_person) REFERENCES public.person(person_id);


--
-- Name: project_subtype project_subtype_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_subtype
    ADD CONSTRAINT project_subtype_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: project project_subtype_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_subtype_fkey FOREIGN KEY (project_subtype_id) REFERENCES public.project_subtype(project_subtype_id);


--
-- Name: project_task_assignment project_task_assignment_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_task_assignment
    ADD CONSTRAINT project_task_assignment_person_fkey FOREIGN KEY (person_id) REFERENCES public.employee(person_id);


--
-- Name: project_task_assignment project_task_assignment_task_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_task_assignment
    ADD CONSTRAINT project_task_assignment_task_fkey FOREIGN KEY (project_task_detail_id) REFERENCES public.project_task_detail(project_task_detail_id);


--
-- Name: project_task_detail project_task_detail_recurring_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_task_detail
    ADD CONSTRAINT project_task_detail_recurring_fkey FOREIGN KEY (recurring_schedule_id) REFERENCES public.recurring_schedule(recurring_schedule_id);


--
-- Name: project_task_detail project_task_detail_shift_fky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_task_detail
    ADD CONSTRAINT project_task_detail_shift_fky FOREIGN KEY (project_shift_id) REFERENCES public.project_shift(project_shift_id);


--
-- Name: project_task_picture project_task_picture_task_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_task_picture
    ADD CONSTRAINT project_task_picture_task_fkey FOREIGN KEY (project_task_detail_id) REFERENCES public.project_task_detail(project_task_detail_id);


--
-- Name: project_task_picture project_task_picture_who_uploaded_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_task_picture
    ADD CONSTRAINT project_task_picture_who_uploaded_fk FOREIGN KEY (who_uploaded) REFERENCES public.person(person_id);


--
-- Name: project_template_benefit_a project_tembena_pers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_template_benefit_a
    ADD CONSTRAINT project_tembena_pers_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: project_template_benefit project_tempben_bcr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_template_benefit
    ADD CONSTRAINT project_tempben_bcr_fkey FOREIGN KEY (bcr_id) REFERENCES public.hr_benefit_change_reason(bcr_id);


--
-- Name: project_template_benefit project_tempben_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_template_benefit
    ADD CONSTRAINT project_tempben_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: project_template_benefit project_tempben_pcat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_template_benefit
    ADD CONSTRAINT project_tempben_pcat_fkey FOREIGN KEY (project_category_id) REFERENCES public.project_category(project_category_id);


--
-- Name: project_template_benefit project_tempben_pstat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_template_benefit
    ADD CONSTRAINT project_tempben_pstat_fkey FOREIGN KEY (project_status_id) REFERENCES public.project_status(project_status_id);


--
-- Name: project_template_benefit project_tempben_ptyp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_template_benefit
    ADD CONSTRAINT project_tempben_ptyp_fkey FOREIGN KEY (project_type_id) REFERENCES public.project_type(project_type_id);


--
-- Name: project_template_benefit project_tempbene_ben_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_template_benefit
    ADD CONSTRAINT project_tempbene_ben_fkey FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: project_template_benefit project_tempbene_stat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_template_benefit
    ADD CONSTRAINT project_tempbene_stat_fkey FOREIGN KEY (status_id) REFERENCES public.hr_employee_status(status_id);


--
-- Name: project_template_benefit_a project_tmpbena_temp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_template_benefit_a
    ADD CONSTRAINT project_tmpbena_temp_fkey FOREIGN KEY (project_template_id) REFERENCES public.project_template_benefit(project_template_id);


--
-- Name: project_training project_training_cat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_training
    ADD CONSTRAINT project_training_cat_fkey FOREIGN KEY (cat_id) REFERENCES public.hr_training_category(cat_id);


--
-- Name: project_training project_training_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_training
    ADD CONSTRAINT project_training_project_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: project_type project_type_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_type
    ADD CONSTRAINT project_type_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: project_status_rs_join projstat_rsj_ps_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_status_rs_join
    ADD CONSTRAINT projstat_rsj_ps_fkey FOREIGN KEY (project_status_id) REFERENCES public.project_status(project_status_id);


--
-- Name: project_status_rs_join projstat_rsj_rs_kfey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_status_rs_join
    ADD CONSTRAINT projstat_rsj_rs_kfey FOREIGN KEY (route_stop_id) REFERENCES public.route_stop(route_stop_id);


--
-- Name: prophet_login prophet_login_nc_scrngrp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prophet_login
    ADD CONSTRAINT prophet_login_nc_scrngrp_fkey FOREIGN KEY (no_company_screen_group_id) REFERENCES public.screen_group(screen_group_id);


--
-- Name: appointment_person_join prospect_appt_appt_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment_person_join
    ADD CONSTRAINT prospect_appt_appt_id_fkey FOREIGN KEY (appointment_id) REFERENCES public.appointment(appointment_id);


--
-- Name: appointment prospect_appt_org_grp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT prospect_appt_org_grp_fkey FOREIGN KEY (org_group_id) REFERENCES public.company_base(org_group_id);


--
-- Name: appointment_person_join prospect_appt_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment_person_join
    ADD CONSTRAINT prospect_appt_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: company_question_detail prospect_comp_ques_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_question_detail
    ADD CONSTRAINT prospect_comp_ques_fkey FOREIGN KEY (company_ques_id) REFERENCES public.company_question(company_ques_id);


--
-- Name: prospect prospect_company_base_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT prospect_company_base_fkey FOREIGN KEY (org_group_id) REFERENCES public.company_base(org_group_id);


--
-- Name: prospect prospect_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT prospect_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: contact_question_detail prospect_cont_ques_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contact_question_detail
    ADD CONSTRAINT prospect_cont_ques_fkey FOREIGN KEY (contact_question_id) REFERENCES public.contact_question(contact_question_id);


--
-- Name: prospect_contact prospect_contact_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_contact
    ADD CONSTRAINT prospect_contact_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: prospect_h prospect_h_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_h
    ADD CONSTRAINT prospect_h_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: prospect_h prospect_h_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_h
    ADD CONSTRAINT prospect_h_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: prospect_h prospect_h_prospect_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_h
    ADD CONSTRAINT prospect_h_prospect_type_fkey FOREIGN KEY (prospect_type_id) REFERENCES public.prospect_type(prospect_type_id);


--
-- Name: prospect_h prospect_h_recpers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_h
    ADD CONSTRAINT prospect_h_recpers_fkey FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: prospect_log prospect_log_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_log
    ADD CONSTRAINT prospect_log_company_fkey FOREIGN KEY (org_group_id) REFERENCES public.company_base(org_group_id);


--
-- Name: prospect_log prospect_log_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_log
    ADD CONSTRAINT prospect_log_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: prospect_log prospect_log_result_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_log
    ADD CONSTRAINT prospect_log_result_fkey FOREIGN KEY (sales_activity_result_id) REFERENCES public.sales_activity_result(sales_activity_result_id);


--
-- Name: prospect_log prospect_log_salesact_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_log
    ADD CONSTRAINT prospect_log_salesact_fkey FOREIGN KEY (sales_activity_id) REFERENCES public.sales_activity(sales_activity_id);


--
-- Name: prospect prospect_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT prospect_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.employee(person_id);


--
-- Name: prospect prospect_prospect_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT prospect_prospect_type_fkey FOREIGN KEY (prospect_type_id) REFERENCES public.prospect_type(prospect_type_id);


--
-- Name: prospect prospect_recchgper_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT prospect_recchgper_fkey FOREIGN KEY (record_person_id) REFERENCES public.person(person_id);


--
-- Name: prospect_source prospect_source_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_source
    ADD CONSTRAINT prospect_source_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: prospect prospect_source_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT prospect_source_fkey FOREIGN KEY (prospect_source_id) REFERENCES public.prospect_source(prospect_source_id);


--
-- Name: prospect prospect_status_chk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT prospect_status_chk FOREIGN KEY (prospect_status_id) REFERENCES public.prospect_status(prospect_status_id);


--
-- Name: prospect_status prospect_status_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_status
    ADD CONSTRAINT prospect_status_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: prospect_type prospect_type_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_type
    ADD CONSTRAINT prospect_type_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: project_view pview_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_view
    ADD CONSTRAINT pview_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: project_view pview_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_view
    ADD CONSTRAINT pview_project_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: quickbooks_sync qb_org_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quickbooks_sync
    ADD CONSTRAINT qb_org_group_fkey FOREIGN KEY (org_group_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: quote_table quote_accept_peron_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_table
    ADD CONSTRAINT quote_accept_peron_fkey FOREIGN KEY (accepted_person) REFERENCES public.employee(person_id);


--
-- Name: quote_adjustment quote_adj_quote_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_adjustment
    ADD CONSTRAINT quote_adj_quote_fkey FOREIGN KEY (quote_id) REFERENCES public.quote_table(quote_id);


--
-- Name: quote_table quote_client_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_table
    ADD CONSTRAINT quote_client_fkey FOREIGN KEY (client_id) REFERENCES public.client(org_group_id);


--
-- Name: quote_table quote_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_table
    ADD CONSTRAINT quote_created_by_fkey FOREIGN KEY (created_by_person) REFERENCES public.employee(person_id);


--
-- Name: quote_table quote_final_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_table
    ADD CONSTRAINT quote_final_by FOREIGN KEY (finalized_by_person) REFERENCES public.employee(person_id);


--
-- Name: quote_table quote_location_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_table
    ADD CONSTRAINT quote_location_fkey FOREIGN KEY (location_cost_id) REFERENCES public.location_cost(location_cost_id);


--
-- Name: quote_product quote_prod_prod_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_product
    ADD CONSTRAINT quote_prod_prod_fkey FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- Name: quote_product quote_prod_quote_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_product
    ADD CONSTRAINT quote_prod_quote_fkey FOREIGN KEY (quote_id) REFERENCES public.quote_table(quote_id);


--
-- Name: quote_template_product quote_tempprod_prod_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_template_product
    ADD CONSTRAINT quote_tempprod_prod_fkey FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- Name: quote_template_product quote_temprod_quote_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quote_template_product
    ADD CONSTRAINT quote_temprod_quote_fkey FOREIGN KEY (quote_template_id) REFERENCES public.quote_template(quote_template_id);


--
-- Name: rate_type rate_type_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rate_type
    ADD CONSTRAINT rate_type_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: receipt receipt_bank_draft_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receipt
    ADD CONSTRAINT receipt_bank_draft_fkey FOREIGN KEY (bank_draft_history_id) REFERENCES public.bank_draft_history(bank_draft_history_id);


--
-- Name: receipt_join receipt_join_invoice_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receipt_join
    ADD CONSTRAINT receipt_join_invoice_fkey FOREIGN KEY (invoice_id) REFERENCES public.invoice(invoice_id);


--
-- Name: receipt_join receipt_join_receipt_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receipt_join
    ADD CONSTRAINT receipt_join_receipt_fkey FOREIGN KEY (receipt_id) REFERENCES public.receipt(receipt_id);


--
-- Name: reminder reminder_about_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reminder
    ADD CONSTRAINT reminder_about_person_fkey FOREIGN KEY (about_person) REFERENCES public.person(person_id);


--
-- Name: reminder reminder_about_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reminder
    ADD CONSTRAINT reminder_about_project_fkey FOREIGN KEY (about_project) REFERENCES public.project(project_id);


--
-- Name: reminder reminder_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reminder
    ADD CONSTRAINT reminder_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: reminder reminder_who_added_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reminder
    ADD CONSTRAINT reminder_who_added_fkey FOREIGN KEY (who_added) REFERENCES public.person(person_id);


--
-- Name: reminder reminder_who_inactivated_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reminder
    ADD CONSTRAINT reminder_who_inactivated_fkey FOREIGN KEY (who_inactivated) REFERENCES public.person(person_id);


--
-- Name: report_column report_column_report_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_column
    ADD CONSTRAINT report_column_report_fkey FOREIGN KEY (report_id) REFERENCES public.report(report_id);


--
-- Name: report report_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report
    ADD CONSTRAINT report_company_fkey FOREIGN KEY (company_id) REFERENCES public.org_group(org_group_id);


--
-- Name: report_graphic report_graphic_report_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_graphic
    ADD CONSTRAINT report_graphic_report_fkey FOREIGN KEY (report_id) REFERENCES public.report(report_id);


--
-- Name: report_selection report_selection_report_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_selection
    ADD CONSTRAINT report_selection_report_fkey FOREIGN KEY (report_id) REFERENCES public.report(report_id);


--
-- Name: report_title report_title_report_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_title
    ADD CONSTRAINT report_title_report_fkey FOREIGN KEY (report_id) REFERENCES public.report(report_id);


--
-- Name: removed_from_roster rfr_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.removed_from_roster
    ADD CONSTRAINT rfr_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: removed_from_roster rfr_shift_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.removed_from_roster
    ADD CONSTRAINT rfr_shift_fkey FOREIGN KEY (shift_id) REFERENCES public.project_shift(project_shift_id);


--
-- Name: removed_from_roster rfr_supervisor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.removed_from_roster
    ADD CONSTRAINT rfr_supervisor_fkey FOREIGN KEY (supervisor_id) REFERENCES public.person(person_id);


--
-- Name: rights_association rights_association_right_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rights_association
    ADD CONSTRAINT rights_association_right_id_fkey FOREIGN KEY (right_id) REFERENCES public.security_token(right_id);


--
-- Name: rights_association rights_association_security_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rights_association
    ADD CONSTRAINT rights_association_security_group_id_fkey FOREIGN KEY (security_group_id) REFERENCES public.security_group(security_group_id);


--
-- Name: route_type_assoc route_assoc_cat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_type_assoc
    ADD CONSTRAINT route_assoc_cat_fkey FOREIGN KEY (project_category_id) REFERENCES public.project_category(project_category_id);


--
-- Name: route_type_assoc route_assoc_route_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_type_assoc
    ADD CONSTRAINT route_assoc_route_fkey FOREIGN KEY (route_id) REFERENCES public.route(route_id);


--
-- Name: route_type_assoc route_assoc_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_type_assoc
    ADD CONSTRAINT route_assoc_type_fkey FOREIGN KEY (project_type_id) REFERENCES public.project_type(project_type_id);


--
-- Name: route route_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route
    ADD CONSTRAINT route_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: project_history route_hist_from_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_history
    ADD CONSTRAINT route_hist_from_status_fkey FOREIGN KEY (from_status_id) REFERENCES public.project_status(project_status_id);


--
-- Name: project_history route_hist_from_stop_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_history
    ADD CONSTRAINT route_hist_from_stop_fkey FOREIGN KEY (from_stop_id) REFERENCES public.route_stop(route_stop_id);


--
-- Name: project_history route_hist_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_history
    ADD CONSTRAINT route_hist_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: project_history route_hist_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_history
    ADD CONSTRAINT route_hist_project_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: project_history route_hist_to_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_history
    ADD CONSTRAINT route_hist_to_status_fkey FOREIGN KEY (to_status_id) REFERENCES public.project_status(project_status_id);


--
-- Name: project_history route_hist_to_stop_kkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_history
    ADD CONSTRAINT route_hist_to_stop_kkey FOREIGN KEY (to_stop_id) REFERENCES public.route_stop(route_stop_id);


--
-- Name: route_path route_path_from_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_path
    ADD CONSTRAINT route_path_from_status_fkey FOREIGN KEY (from_status_id) REFERENCES public.project_status(project_status_id);


--
-- Name: route_path route_path_from_stop_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_path
    ADD CONSTRAINT route_path_from_stop_fkey FOREIGN KEY (from_stop_id) REFERENCES public.route_stop(route_stop_id);


--
-- Name: route_path route_path_to_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_path
    ADD CONSTRAINT route_path_to_status_fkey FOREIGN KEY (to_status_id) REFERENCES public.project_status(project_status_id);


--
-- Name: route_path route_path_to_stop_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_path
    ADD CONSTRAINT route_path_to_stop_fkey FOREIGN KEY (to_stop_id) REFERENCES public.route_stop(route_stop_id);


--
-- Name: route route_project_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route
    ADD CONSTRAINT route_project_status_fkey FOREIGN KEY (project_status_id) REFERENCES public.project_status(project_status_id);


--
-- Name: route route_route_stop_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route
    ADD CONSTRAINT route_route_stop_fkey FOREIGN KEY (route_stop_id) REFERENCES public.route_stop(route_stop_id);


--
-- Name: route_stop_checklist route_stop_cl_rsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_stop_checklist
    ADD CONSTRAINT route_stop_cl_rsid_fkey FOREIGN KEY (route_stop_id) REFERENCES public.route_stop(route_stop_id);


--
-- Name: route_stop route_stop_org_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_stop
    ADD CONSTRAINT route_stop_org_group_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: route_stop route_stop_phase_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_stop
    ADD CONSTRAINT route_stop_phase_fkey FOREIGN KEY (project_phase_id) REFERENCES public.project_phase(project_phase_id);


--
-- Name: route_stop route_stop_route_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.route_stop
    ADD CONSTRAINT route_stop_route_fkey FOREIGN KEY (route_id) REFERENCES public.route(route_id);


--
-- Name: sales_activity sales_activity_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_activity
    ADD CONSTRAINT sales_activity_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: sales_activity_result sales_actres_act_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_activity_result
    ADD CONSTRAINT sales_actres_act_fkey FOREIGN KEY (sales_activity_id) REFERENCES public.sales_activity(sales_activity_id);


--
-- Name: sales_points sales_points_emp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_points
    ADD CONSTRAINT sales_points_emp_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: sales_points sales_points_prospect_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_points
    ADD CONSTRAINT sales_points_prospect_fkey FOREIGN KEY (prospect_id) REFERENCES public.prospect(org_group_id);


--
-- Name: sales_points sales_points_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_points
    ADD CONSTRAINT sales_points_status_fkey FOREIGN KEY (prospect_status_id) REFERENCES public.prospect_status(prospect_status_id);


--
-- Name: screen_group_access screen_group_access_fk1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_group_access
    ADD CONSTRAINT screen_group_access_fk1 FOREIGN KEY (security_group_id) REFERENCES public.security_group(security_group_id);


--
-- Name: screen_group_access screen_group_access_fk2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_group_access
    ADD CONSTRAINT screen_group_access_fk2 FOREIGN KEY (can_access_screen_group_id) REFERENCES public.screen_group(screen_group_id);


--
-- Name: screen_group_hierarchy screen_group_hier_screen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_group_hierarchy
    ADD CONSTRAINT screen_group_hier_screen_fkey FOREIGN KEY (screen_id) REFERENCES public.screen(screen_id);


--
-- Name: screen_group_hierarchy screen_group_hierarchy_child_screen_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_group_hierarchy
    ADD CONSTRAINT screen_group_hierarchy_child_screen_group_id_fkey FOREIGN KEY (child_screen_group_id) REFERENCES public.screen_group(screen_group_id);


--
-- Name: screen_group_hierarchy screen_group_hierarchy_parent_screen_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_group_hierarchy
    ADD CONSTRAINT screen_group_hierarchy_parent_screen_group_id_fkey FOREIGN KEY (parent_screen_group_id) REFERENCES public.screen_group(screen_group_id);


--
-- Name: screen_group screen_group_screen_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_group
    ADD CONSTRAINT screen_group_screen_id_fkey FOREIGN KEY (screen_id) REFERENCES public.screen(screen_id);


--
-- Name: screen_history screen_history_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_history
    ADD CONSTRAINT screen_history_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: screen_history screen_history_screen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screen_history
    ADD CONSTRAINT screen_history_screen_fkey FOREIGN KEY (screen_id) REFERENCES public.screen(screen_id);


--
-- Name: security_group_access security_group_acc_fk1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.security_group_access
    ADD CONSTRAINT security_group_acc_fk1 FOREIGN KEY (security_group_id) REFERENCES public.security_group(security_group_id);


--
-- Name: security_group_access security_group_acc_fk2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.security_group_access
    ADD CONSTRAINT security_group_acc_fk2 FOREIGN KEY (can_access_security_group_id) REFERENCES public.security_group(security_group_id);


--
-- Name: security_group_hierarchy security_group_hierarchy_child_security_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.security_group_hierarchy
    ADD CONSTRAINT security_group_hierarchy_child_security_group_id_fkey FOREIGN KEY (child_security_group_id) REFERENCES public.security_group(security_group_id);


--
-- Name: security_group_hierarchy security_group_hierarchy_parent_security_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.security_group_hierarchy
    ADD CONSTRAINT security_group_hierarchy_parent_security_group_id_fkey FOREIGN KEY (parent_security_group_id) REFERENCES public.security_group(security_group_id);


--
-- Name: service service_product_service_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT service_product_service_fkey FOREIGN KEY (service_id) REFERENCES public.product_service(product_service_id);


--
-- Name: service_subscribed service_sub_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_subscribed
    ADD CONSTRAINT service_sub_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: service_subscribed_join service_subjoin_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_subscribed_join
    ADD CONSTRAINT service_subjoin_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: service_subscribed_join service_subjoin_service_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_subscribed_join
    ADD CONSTRAINT service_subjoin_service_fkey FOREIGN KEY (service_id) REFERENCES public.service_subscribed(service_id);


--
-- Name: standard_project sp_bill_at_org_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standard_project
    ADD CONSTRAINT sp_bill_at_org_group_fkey FOREIGN KEY (bill_at_org_group) REFERENCES public.org_group(org_group_id);


--
-- Name: standard_project sp_managing_emp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standard_project
    ADD CONSTRAINT sp_managing_emp_fkey FOREIGN KEY (managing_employee) REFERENCES public.employee(person_id);


--
-- Name: standard_project sp_sponsor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standard_project
    ADD CONSTRAINT sp_sponsor_fkey FOREIGN KEY (project_sponsor_id) REFERENCES public.person(person_id);


--
-- Name: project sponsor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT sponsor_fkey FOREIGN KEY (project_sponsor_id) REFERENCES public.person(person_id);


--
-- Name: spousal_insurance_verif spousal_verif_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.spousal_insurance_verif
    ADD CONSTRAINT spousal_verif_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: standard_project standard_project_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standard_project
    ADD CONSTRAINT standard_project_comp_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: standard_project std_project_category_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standard_project
    ADD CONSTRAINT std_project_category_fkey FOREIGN KEY (project_category_id) REFERENCES public.project_category(project_category_id);


--
-- Name: standard_project std_project_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standard_project
    ADD CONSTRAINT std_project_product_fkey FOREIGN KEY (product_id) REFERENCES public.product_service(product_service_id);


--
-- Name: standard_project std_project_proj_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standard_project
    ADD CONSTRAINT std_project_proj_type_fkey FOREIGN KEY (project_type_id) REFERENCES public.project_type(project_type_id);


--
-- Name: student_verification student_verif_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_verification
    ADD CONSTRAINT student_verif_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: time_off_request time_off_req_app_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_off_request
    ADD CONSTRAINT time_off_req_app_person FOREIGN KEY (approving_person_id) REFERENCES public.person(person_id);


--
-- Name: time_off_request time_off_req_proj_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_off_request
    ADD CONSTRAINT time_off_req_proj_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: time_off_request time_off_request_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_off_request
    ADD CONSTRAINT time_off_request_person_fkey FOREIGN KEY (requesting_person_id) REFERENCES public.person(person_id);


--
-- Name: time_reject time_reject_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_reject
    ADD CONSTRAINT time_reject_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: timesheet time_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timesheet
    ADD CONSTRAINT time_type_id_fkey FOREIGN KEY (time_type_id) REFERENCES public.time_type(time_type_id);


--
-- Name: time_off_accrual_calc timeoff_acc_calc_config_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_off_accrual_calc
    ADD CONSTRAINT timeoff_acc_calc_config_fkey FOREIGN KEY (benefit_config_id) REFERENCES public.hr_benefit_config(benefit_config_id);


--
-- Name: time_off_accrual_seniority timeoff_accsen_calc_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_off_accrual_seniority
    ADD CONSTRAINT timeoff_accsen_calc_fkey FOREIGN KEY (time_off_accrual_calc_id) REFERENCES public.time_off_accrual_calc(time_off_accrual_calc_id);


--
-- Name: timesheet timesheet_beg_person_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timesheet
    ADD CONSTRAINT timesheet_beg_person_id FOREIGN KEY (beginning_entry_person_id) REFERENCES public.person(person_id);


--
-- Name: timesheet timesheet_end_person_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timesheet
    ADD CONSTRAINT timesheet_end_person_id FOREIGN KEY (end_entry_person_id) REFERENCES public.person(person_id);


--
-- Name: timesheet timesheet_invoice_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timesheet
    ADD CONSTRAINT timesheet_invoice_fkey FOREIGN KEY (invoice_line_item_id) REFERENCES public.invoice_line_item(invoice_line_item_id);


--
-- Name: timesheet timesheet_message_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timesheet
    ADD CONSTRAINT timesheet_message_fkey FOREIGN KEY (message_id) REFERENCES public.message(message_id);


--
-- Name: timesheet timesheet_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timesheet
    ADD CONSTRAINT timesheet_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: timesheet timesheet_project_shift_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timesheet
    ADD CONSTRAINT timesheet_project_shift_fkey FOREIGN KEY (project_shift_id) REFERENCES public.project_shift(project_shift_id);


--
-- Name: timesheet timesheet_wagetype_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timesheet
    ADD CONSTRAINT timesheet_wagetype_fkey FOREIGN KEY (wage_type_id) REFERENCES public.wage_type(wage_type_id);


--
-- Name: user_attribute user_attribute_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_attribute
    ADD CONSTRAINT user_attribute_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: vendor vandor_dflt_ap_acct; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT vandor_dflt_ap_acct FOREIGN KEY (dflt_ap_acct) REFERENCES public.gl_account(gl_account_id);


--
-- Name: vendor vandor_dflt_expense_acct; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT vandor_dflt_expense_acct FOREIGN KEY (dflt_expense_acct) REFERENCES public.gl_account(gl_account_id);


--
-- Name: vendor vendor_company_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT vendor_company_company_fkey FOREIGN KEY (org_group_id) REFERENCES public.company_base(org_group_id);


--
-- Name: vendor vendor_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT vendor_company_fkey FOREIGN KEY (company_id) REFERENCES public.company_detail(org_group_id);


--
-- Name: vendor_group vendor_group_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vendor_group
    ADD CONSTRAINT vendor_group_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: vendor_group vendor_group_vendor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vendor_group
    ADD CONSTRAINT vendor_group_vendor_fkey FOREIGN KEY (vendor_id) REFERENCES public.vendor(org_group_id);


--
-- Name: vendor_contact vendor_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vendor_contact
    ADD CONSTRAINT vendor_person_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: wage_paid_detail wage_detail_paid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wage_paid_detail
    ADD CONSTRAINT wage_detail_paid_fkey FOREIGN KEY (wage_paid_id) REFERENCES public.wage_paid(wage_paid_id);


--
-- Name: wage_paid_detail wage_paid_det_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wage_paid_detail
    ADD CONSTRAINT wage_paid_det_type_fkey FOREIGN KEY (wage_type_id) REFERENCES public.wage_type(wage_type_id);


--
-- Name: wage_paid wage_paid_employee_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wage_paid
    ADD CONSTRAINT wage_paid_employee_fkey FOREIGN KEY (employee_id) REFERENCES public.employee(person_id);


--
-- Name: wage_type wage_type_gl_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wage_type
    ADD CONSTRAINT wage_type_gl_fkey FOREIGN KEY (expense_account) REFERENCES public.gl_account(gl_account_id);


--
-- Name: wage_type wage_type_liab_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wage_type
    ADD CONSTRAINT wage_type_liab_fkey FOREIGN KEY (liability_account) REFERENCES public.gl_account(gl_account_id);


--
-- Name: wage_type wage_type_org_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wage_type
    ADD CONSTRAINT wage_type_org_fkey FOREIGN KEY (org_group_id) REFERENCES public.org_group(org_group_id);


--
-- Name: wizard_config_benefit wizard_ben_ben_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_benefit
    ADD CONSTRAINT wizard_ben_ben_fkey FOREIGN KEY (benefit_id) REFERENCES public.hr_benefit(benefit_id);


--
-- Name: wizard_config_benefit wizard_ben_screen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_benefit
    ADD CONSTRAINT wizard_ben_screen_fkey FOREIGN KEY (screen_id) REFERENCES public.screen(screen_id);


--
-- Name: wizard_config_benefit wizard_ben_wcat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_benefit
    ADD CONSTRAINT wizard_ben_wcat_fkey FOREIGN KEY (wizard_config_cat_id) REFERENCES public.wizard_config_category(wizard_config_cat_id);


--
-- Name: wizard_config_category wizard_cat_config_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_category
    ADD CONSTRAINT wizard_cat_config_fkey FOREIGN KEY (wizard_configuration_id) REFERENCES public.wizard_configuration(wizard_configuration_id);


--
-- Name: wizard_config_category wizard_cat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_category
    ADD CONSTRAINT wizard_cat_fkey FOREIGN KEY (benefit_cat_id) REFERENCES public.hr_benefit_category(benefit_cat_id);


--
-- Name: wizard_config_category wizard_cat_screen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_category
    ADD CONSTRAINT wizard_cat_screen_fkey FOREIGN KEY (screen_id) REFERENCES public.screen(screen_id);


--
-- Name: wizard_configuration wizard_conf_benclass_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_configuration
    ADD CONSTRAINT wizard_conf_benclass_fkey FOREIGN KEY (benefit_class_id) REFERENCES public.hr_benefit_class(benefit_class_id);


--
-- Name: wizard_configuration wizard_conf_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_configuration
    ADD CONSTRAINT wizard_conf_company_fkey FOREIGN KEY (company_id) REFERENCES public.org_group(org_group_id);


--
-- Name: wizard_config_config wizard_conf_conf_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_config
    ADD CONSTRAINT wizard_conf_conf_fkey FOREIGN KEY (benefit_config_id) REFERENCES public.hr_benefit_config(benefit_config_id);


--
-- Name: wizard_configuration wizard_conf_projcat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_configuration
    ADD CONSTRAINT wizard_conf_projcat_fkey FOREIGN KEY (project_category_id) REFERENCES public.project_category(project_category_id);


--
-- Name: wizard_configuration wizard_conf_projstat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_configuration
    ADD CONSTRAINT wizard_conf_projstat_fkey FOREIGN KEY (project_status_id) REFERENCES public.project_status(project_status_id);


--
-- Name: wizard_configuration wizard_conf_projtype_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_configuration
    ADD CONSTRAINT wizard_conf_projtype_fkey FOREIGN KEY (project_type_id) REFERENCES public.project_type(project_type_id);


--
-- Name: wizard_config_config wizard_conf_wben_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_config
    ADD CONSTRAINT wizard_conf_wben_fkey FOREIGN KEY (wizard_config_ben_id) REFERENCES public.wizard_config_benefit(wizard_config_ben_id);


--
-- Name: wizard_configuration wizard_config_contact_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_configuration
    ADD CONSTRAINT wizard_config_contact_fkey FOREIGN KEY (hr_contact_id) REFERENCES public.employee(person_id);


--
-- Name: wizard_configuration wizard_config_demoscn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_configuration
    ADD CONSTRAINT wizard_config_demoscn_fkey FOREIGN KEY (demographic_screen_id) REFERENCES public.screen(screen_id);


--
-- Name: wizard_config_project_a wizard_confproja_pers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_project_a
    ADD CONSTRAINT wizard_confproja_pers_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: wizard_config_project_a wizard_confproja_wiz_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_config_project_a
    ADD CONSTRAINT wizard_confproja_wiz_fkey FOREIGN KEY (wizard_configuration_id) REFERENCES public.wizard_configuration(wizard_configuration_id);


--
-- Name: wizard_project wizard_project_benjoin_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_project
    ADD CONSTRAINT wizard_project_benjoin_fkey FOREIGN KEY (benefit_join_id) REFERENCES public.hr_benefit_join(benefit_join_id);


--
-- Name: wizard_project wizard_project_hist_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_project
    ADD CONSTRAINT wizard_project_hist_fkey FOREIGN KEY (hr_benefit_join_h_id) REFERENCES public.hr_benefit_join_h(history_id);


--
-- Name: wizard_project wizard_project_pers_comp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_project
    ADD CONSTRAINT wizard_project_pers_comp_fkey FOREIGN KEY (person_completed) REFERENCES public.person(person_id);


--
-- Name: wizard_project wizard_project_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wizard_project
    ADD CONSTRAINT wizard_project_project_fkey FOREIGN KEY (project_id) REFERENCES public.project(project_id);


--
-- Name: worker_confirmation worker_confirmation_person_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.worker_confirmation
    ADD CONSTRAINT worker_confirmation_person_fk FOREIGN KEY (person_id) REFERENCES public.employee(person_id);


--
-- Name: worker_confirmation worker_confirmation_sch_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.worker_confirmation
    ADD CONSTRAINT worker_confirmation_sch_fkey FOREIGN KEY (project_shift_id) REFERENCES public.project_shift(project_shift_id);


--
-- Name: worker_confirmation worker_confirmation_who_added_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.worker_confirmation
    ADD CONSTRAINT worker_confirmation_who_added_fk FOREIGN KEY (who_added) REFERENCES public.employee(person_id);


--
-- PostgreSQL database dump complete
--

