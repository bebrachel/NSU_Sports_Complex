ALTER TABLE public.sections
    ADD COLUMN capacity integer NOT NULL DEFAULT 1;

-- Name: sections_members; Type: TABLE; Schema: public;
CREATE TABLE public.sections_members
(
    section_id integer NOT NULL,
    member_id  integer NOT NULL,
    PRIMARY KEY (section_id, member_id),
    CONSTRAINT fk_section FOREIGN KEY (section_id) REFERENCES public.sections (id) ON DELETE CASCADE,
    CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES public.members (id) ON DELETE CASCADE
);
