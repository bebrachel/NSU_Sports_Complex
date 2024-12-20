-- Name: schedules; Type: TABLE; Schema: public;
CREATE TABLE public.schedules
(
    id integer NOT NULL
);

-- Name: schedules_id_seq; Type: SEQUENCE; Schema: public;
ALTER TABLE public.schedules
    ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.schedules_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );


-- Name: sections; Type: TABLE; Schema: public;
CREATE TABLE public.sections
(
    id          integer                NOT NULL,
    schedule_id integer,
    name        character varying(255) NOT NULL,
    place       character varying(255),
    teacher     character varying(255)
);

-- Name: sections_id_seq; Type: SEQUENCE; Schema: public;
ALTER TABLE public.sections
    ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.sections_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );


-- Name: time_slots; Type: TABLE; Schema: public;
CREATE TABLE public.time_slots
(
    id          integer NOT NULL,
    start_time  time(6) without time zone,
    end_time    time(6) without time zone,
    schedule_id integer,
    day_of_week character varying(255),
    CONSTRAINT time_slots_day_of_week_check CHECK (((day_of_week)::text = ANY
                                                    ((ARRAY ['MONDAY'::character varying, 'TUESDAY'::character varying, 'WEDNESDAY'::character varying, 'THURSDAY'::character varying, 'FRIDAY'::character varying, 'SATURDAY'::character varying, 'SUNDAY'::character varying])::text[])))
);

-- Name: time_slots_id_seq; Type: SEQUENCE; Schema: public;
ALTER TABLE public.time_slots
    ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.time_slots_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );


-- Name: members; Type: TABLE; Schema: public;
CREATE TABLE public.members
(
    id    integer NOT NULL,
    email character varying(255),
    name  character varying(255)
);

-- Name: members_id_seq; Type: SEQUENCE; Schema: public;
ALTER TABLE public.members
    ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.members_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );

-- Name: users; Type: TABLE; Schema: public;
CREATE TABLE public.users
(
    id       integer NOT NULL,
    email    character varying(255),
    name     character varying(255),
    password character varying(255)
);

-- Name: users_id_seq; Type: SEQUENCE; Schema: public;
ALTER TABLE public.users
    ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.users_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );


-- Name: schedules schedules_pkey; Type: CONSTRAINT; Schema: public;
ALTER TABLE ONLY public.schedules
    ADD CONSTRAINT schedules_pkey PRIMARY KEY (id);


-- Name: sections sections_pkey; Type: CONSTRAINT; Schema: public;
ALTER TABLE ONLY public.sections
    ADD CONSTRAINT sections_pkey PRIMARY KEY (id);

-- Name: sections sections_name_key; Type: CONSTRAINT; Schema: public;
ALTER TABLE ONLY public.sections
    ADD CONSTRAINT sections_name_key UNIQUE (name);

-- Name: sections sections_schedule_id_key; Type: CONSTRAINT; Schema: public;
ALTER TABLE ONLY public.sections
    ADD CONSTRAINT sections_schedule_id_key UNIQUE (schedule_id);


-- Name: time_slots time_slots_pkey; Type: CONSTRAINT; Schema: public;
ALTER TABLE ONLY public.time_slots
    ADD CONSTRAINT time_slots_pkey PRIMARY KEY (id);


-- Name: members members_pkey; Type: CONSTRAINT; Schema: public;
ALTER TABLE ONLY public.members
    ADD CONSTRAINT members_pkey PRIMARY KEY (id);

-- Name: users users_pkey; Type: CONSTRAINT; Schema: public;
ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


-- Name: time_slots time_slots_schedule_fk; Type: FK CONSTRAINT; Schema: public;
ALTER TABLE ONLY public.time_slots
    ADD CONSTRAINT time_slots_schedule_fk FOREIGN KEY (schedule_id) REFERENCES public.schedules (id);


-- Name: sections sections_schedule_fk; Type: FK CONSTRAINT; Schema: public;
ALTER TABLE ONLY public.sections
    ADD CONSTRAINT sections_schedule_fk FOREIGN KEY (schedule_id) REFERENCES public.schedules (id);