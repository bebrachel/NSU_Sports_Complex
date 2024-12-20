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

-- Name: news news_pkey; Type: CONSTRAINT; Schema: public;
ALTER TABLE ONLY public.news
    ADD CONSTRAINT news_pkey PRIMARY KEY (id);
