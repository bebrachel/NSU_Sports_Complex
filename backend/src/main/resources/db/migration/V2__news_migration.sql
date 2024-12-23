-- Name: news; Type: TABLE; Schema: public;
CREATE TABLE public.news
(
    id    integer NOT NULL,
    title       VARCHAR(200) NOT NULL,
    content     TEXT NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL,
    author      VARCHAR(255)
);

-- Name: news_id_seq; Type: SEQUENCE; Schema: public;
ALTER TABLE public.news
ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.news_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );

-- Name: news news_pkey; Type: CONSTRAINT; Schema: public;
ALTER TABLE ONLY public.news
    ADD CONSTRAINT news_pkey PRIMARY KEY (id);
