-- DROP SCHEMA public;

CREATE SCHEMA IF NOT EXISTS public AUTHORIZATION pg_database_owner;

-- DROP SEQUENCE public.content_t_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.content_t_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
-- DROP SEQUENCE public.course_t_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.course_t_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
-- DROP SEQUENCE public.group_t_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.group_t_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
-- DROP SEQUENCE public.module_t_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.module_t_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
-- DROP SEQUENCE public.sequence_id_auto_gen;

CREATE SEQUENCE IF NOT EXISTS public.answer_sequence_id_auto_gen
    INCREMENT BY 15
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 15
    NO CYCLE;

CREATE SEQUENCE IF NOT EXISTS public.deadline_sequence_id_auto_gen
    INCREMENT BY 15
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 15
    NO CYCLE;

CREATE SEQUENCE IF NOT EXISTS public.user_course_sequence_id_auto_gen
    INCREMENT BY 15
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 15
    NO CYCLE;

CREATE SEQUENCE IF NOT EXISTS public.topic_t_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
-- DROP SEQUENCE public.user_content_t_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.user_content_t_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
-- DROP SEQUENCE public.user_module_t_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.user_module_t_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
-- DROP SEQUENCE public.user_topic_t_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.user_topic_t_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
-- public.course_t определение

-- Drop table

-- DROP TABLE public.course_t;

CREATE TABLE IF NOT EXISTS public.course_t
(
    id                      int8 GENERATED BY DEFAULT AS IDENTITY ( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
    description             varchar(360)                                                                                                             NULL,
    picture_url             varchar(255)                                                                                                             NULL,
    score                   int4 DEFAULT 0                                                                                                           NOT NULL,
    tags                    text                                                                                                                     NULL,
    title                   varchar(255)                                                                                                             NULL,
    is_published            bool DEFAULT false                                                                                                       NULL,
    count_answered_contents int4 DEFAULT 0                                                                                                           NULL,
    created_at              timestamp                                                                                                                NOT NULL,
    CONSTRAINT course_t_pkey PRIMARY KEY (id),
    CONSTRAINT unique_course UNIQUE (title)
);


-- public.role_t определение

-- Drop table

-- DROP TABLE public.role_t;

CREATE TABLE IF NOT EXISTS public.role_t
(
    "name" varchar(255) NOT NULL,
    CONSTRAINT role_t_name_check CHECK (((name)::text = ANY
                                         ((ARRAY ['ADMIN'::character varying, 'USER'::character varying])::text[]))),
    CONSTRAINT role_t_pkey PRIMARY KEY (name)
);


-- public.group_t определение

-- Drop table

-- DROP TABLE public.group_t;

CREATE TABLE IF NOT EXISTS public.group_t
(
    id            int8 GENERATED BY DEFAULT AS IDENTITY ( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
    count_members int4                                                                                                                     NOT NULL,
    is_default    bool                                                                                                                     NOT NULL,
    "name"        varchar(255)                                                                                                             NULL,
    course_id     int8                                                                                                                     NOT NULL,
    CONSTRAINT group_t_pkey PRIMARY KEY (id),
    CONSTRAINT unique_group_name_course UNIQUE (name, course_id),
    CONSTRAINT fknqpqd4l7n2hc2uxdti4myvq97 FOREIGN KEY (course_id) REFERENCES public.course_t (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_group_course_id ON public.group_t USING btree (course_id);


-- public.module_t определение

-- Drop table

-- DROP TABLE public.module_t;

CREATE TABLE IF NOT EXISTS public.module_t
(
    id                      int8 GENERATED BY DEFAULT AS IDENTITY ( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
    "position"              int4                                                                                                                     NOT NULL,
    score                   int4                                                                                                                     NOT NULL,
    title                   varchar(255)                                                                                                             NULL,
    course_id               int8                                                                                                                     NOT NULL,
    count_answered_contents int4 DEFAULT 0                                                                                                           NULL,
    CONSTRAINT module_t_pkey PRIMARY KEY (id),
    CONSTRAINT unique_title_course UNIQUE (title, course_id),
    CONSTRAINT fk56j2pbwpuqtltqhchjpq3bq35 FOREIGN KEY (course_id) REFERENCES public.course_t (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_module_course_id ON public.module_t USING btree (course_id);
CREATE INDEX IF NOT EXISTS idx_module_course_id_position ON public.module_t USING btree (course_id, "position");


-- public.topic_t определение

-- Drop table

-- DROP TABLE public.topic_t;

CREATE TABLE IF NOT EXISTS public.topic_t
(
    id                      int8 GENERATED BY DEFAULT AS IDENTITY ( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
    "position"              int4                                                                                                                     NOT NULL,
    score                   int4                                                                                                                     NOT NULL,
    title                   varchar(255)                                                                                                             NULL,
    module_id               int8                                                                                                                     NOT NULL,
    count_answered_contents int4 DEFAULT 0                                                                                                           NULL,
    CONSTRAINT topic_t_pkey PRIMARY KEY (id),
    CONSTRAINT unique_title_module UNIQUE (title, module_id),
    CONSTRAINT fkmh7gfc97rnjsxasc8xgp3nbx5 FOREIGN KEY (module_id) REFERENCES public.module_t (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_topic_module_id ON public.topic_t USING btree (module_id);
CREATE INDEX IF NOT EXISTS idx_topic_module_id_position ON public.topic_t USING btree (module_id, "position");


-- public.user_t определение

-- Drop table

-- DROP TABLE public.user_t;

CREATE TABLE IF NOT EXISTS public.user_t
(
    id         int8 GENERATED BY DEFAULT AS IDENTITY ( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
    email      varchar(255)                                                                                                             NOT NULL,
    username   varchar(255)                                                                                                             NOT NULL,
    git_hub_id varchar(255)                                                                                                             NULL,
    git_lab_id varchar(255)                                                                                                             NULL,
    role_name  varchar(255)                                                                                                             NOT NULL,
    avatar_url varchar(255)                                                                                                             NULL,
    CONSTRAINT user_t_pkey PRIMARY KEY (id),
    CONSTRAINT fkrt32bwk8idgwdtb5334ifskic FOREIGN KEY (role_name) REFERENCES public.role_t ("name")
);
CREATE INDEX IF NOT EXISTS idx_email ON public.user_t USING btree (email);
CREATE INDEX IF NOT EXISTS idx_username ON public.user_t USING btree (username);
CREATE INDEX IF NOT EXISTS idx_git_hub_id ON public.user_t USING btree (git_hub_id);
CREATE INDEX IF NOT EXISTS idx_git_lab_id ON public.user_t USING btree (git_lab_id);

-- public.content_t определение

-- Drop table

-- DROP TABLE public.content_t;

CREATE TABLE IF NOT EXISTS public.content_t
(
    dtype          varchar(31)                                                                                                              NOT NULL,
    id             int8 GENERATED BY DEFAULT AS IDENTITY ( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
    description    varchar(3000)                                                                                                            NULL,
    "position"     int4                                                                                                                     NOT NULL,
    title          varchar(255)                                                                                                             NULL,
    "type"         varchar(255)                                                                                                             NULL,
    score          int4                                                                                                                     NULL,
    count_attempts int4                                                                                                                     NULL,
    file_url       varchar(255)                                                                                                             NULL,
    topic_id       int8                                                                                                                     NOT NULL,
    CONSTRAINT content_t_pkey PRIMARY KEY (id),
    CONSTRAINT content_t_type_check CHECK (((type)::text = ANY
                                            ((ARRAY ['SINGLE_ANSWER'::character varying, 'MULTI_ANSWER'::character varying, 'DETAILED_ANSWER'::character varying, 'FREEFORM_ANSWER'::character varying, 'VIDEO'::character varying, 'PICTURE'::character varying, 'TEXT'::character varying])::text[]))),
    CONSTRAINT fkegne3l2ohamvvggk4kpfy2lmi FOREIGN KEY (topic_id) REFERENCES public.topic_t (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_content_topic_id ON public.content_t USING btree (topic_id);
CREATE INDEX IF NOT EXISTS idx_content_topic_id_position ON public.content_t USING btree (topic_id, "position");


-- public.deadline_t определение

-- Drop table

-- DROP TABLE public.deadline_t;

CREATE TABLE IF NOT EXISTS public.deadline_t
(
    id         int8         NOT NULL,
    end_time   timestamp(6) NULL,
    start_time timestamp(6) NULL,
    group_id   int8         NOT NULL,
    module_id  int8         NOT NULL,
    CONSTRAINT deadline_t_pkey PRIMARY KEY (id),
    CONSTRAINT fkbde7qjdrbh3tp7gygdcl3hv1t FOREIGN KEY (module_id) REFERENCES public.module_t (id) ON DELETE CASCADE,
    CONSTRAINT fko94jvcifmjfdnjkm8rs052e0q FOREIGN KEY (group_id) REFERENCES public.group_t (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_deadline_groups_id ON public.deadline_t USING btree (group_id);
CREATE INDEX IF NOT EXISTS idx_deadline_module_group_id ON public.deadline_t USING btree (module_id, group_id);


-- public.user_course_t определение

-- Drop table

-- DROP TABLE public.user_course_t;

CREATE TABLE IF NOT EXISTS public.user_course_t
(
    id                      int8 NOT NULL,
    count_answered_contents int4 NOT NULL,
    current_score           int4 NOT NULL,
    status                  varchar(255) CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'FINISHED')),
    course_id               int8 NOT NULL,
    user_id                 int8 NOT NULL,
    CONSTRAINT user_course_t_pkey PRIMARY KEY (id),
    CONSTRAINT unique_user_id_course_id UNIQUE (user_id, course_id),
    CONSTRAINT fk98r35psrapqk31v9iydamelim FOREIGN KEY (course_id) REFERENCES public.course_t (id) ON DELETE CASCADE,
    CONSTRAINT fkj54at3l6ut5rqes8maddah7t6 FOREIGN KEY (user_id) REFERENCES public.user_t (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_course_id ON public.user_course_t USING btree (course_id);
CREATE INDEX IF NOT EXISTS idx_user_id ON public.user_course_t USING btree (user_id);


-- public.user_group_t определение

-- Drop table

-- DROP TABLE public.user_group_t;

CREATE TABLE IF NOT EXISTS public.user_group_t
(
    user_id  int8 NOT NULL,
    group_id int8 NOT NULL,
    CONSTRAINT unique_user_group_t PRIMARY KEY (user_id, group_id),
    CONSTRAINT fk10hib1mm573q3yj6k2slctgl4 FOREIGN KEY (group_id) REFERENCES public.group_t (id) ON DELETE CASCADE,
    CONSTRAINT fkk4i65nl5if6rjr78nj6j4tnm FOREIGN KEY (user_id) REFERENCES public.user_t (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_user_id_group_username ON public.user_group_t USING btree (user_id);


-- public.user_module_t определение

-- Drop table

-- DROP TABLE public.user_module_t;

CREATE TABLE IF NOT EXISTS public.user_module_t
(
    id                      int8 GENERATED BY DEFAULT AS IDENTITY ( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
    count_answered_contents int4                                                                                                                     NOT NULL,
    current_score           int4                                                                                                                     NOT NULL,
    is_completed            bool                                                                                                                     NOT NULL,
    course_id               int8                                                                                                                     NULL,
    module_id               int8                                                                                                                     NULL,
    CONSTRAINT user_module_t_pkey PRIMARY KEY (id),
    CONSTRAINT unique_user_course_id_module_id UNIQUE (course_id, module_id),
    CONSTRAINT fk7k7knsptpa0uu816nnb2kprda FOREIGN KEY (module_id) REFERENCES public.module_t (id) ON DELETE CASCADE,
    CONSTRAINT fkq4obpf01lp57ijb3bhiinr625 FOREIGN KEY (course_id) REFERENCES public.user_course_t (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_user_course_id ON public.user_module_t USING btree (course_id);


-- public.user_topic_t определение

-- Drop table

-- DROP TABLE public.user_topic_t;

CREATE TABLE IF NOT EXISTS public.user_topic_t
(
    id                      int8 GENERATED BY DEFAULT AS IDENTITY ( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
    count_answered_contents int4                                                                                                                     NOT NULL,
    current_score           int4                                                                                                                     NOT NULL,
    is_completed            bool                                                                                                                     NOT NULL,
    module_id               int8                                                                                                                     NULL,
    topic_id                int8                                                                                                                     NULL,
    CONSTRAINT user_topic_t_pkey PRIMARY KEY (id),
    CONSTRAINT unique_user_module_id_topic_id UNIQUE (module_id, topic_id),
    CONSTRAINT fkc1uyls2mkgyft71ew03ydopy6 FOREIGN KEY (module_id) REFERENCES public.user_module_t (id) ON DELETE CASCADE,
    CONSTRAINT fksnirfplri4gc945enkbw01v88 FOREIGN KEY (topic_id) REFERENCES public.topic_t (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_user_module_id ON public.user_topic_t USING btree (module_id);


-- public.answer_t определение

-- Drop table

-- DROP TABLE public.answer_t;

CREATE TABLE IF NOT EXISTS public.answer_t
(
    id         int8               NOT NULL,
    answer     varchar(500)       NULL,
    content_id int8               NULL,
    is_right   bool DEFAULT false NULL,
    CONSTRAINT answer_t_pkey PRIMARY KEY (id),
    CONSTRAINT fk4ln0fibomw5anvcajlq4r6nm1 FOREIGN KEY (content_id) REFERENCES public.content_t (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_answer_content_id ON public.answer_t USING btree (content_id);


-- public.user_content_t определение

-- Drop table

-- DROP TABLE public.user_content_t;

CREATE TABLE IF NOT EXISTS public.user_content_t
(
    id               int8 GENERATED BY DEFAULT AS IDENTITY ( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
    current_attempts int4                                                                                                                     NOT NULL,
    is_completed     bool                                                                                                                     NOT NULL,
    is_success       bool                                                                                                                     NOT NULL,
    content_id       int8                                                                                                                     NULL,
    topic_id         int8                                                                                                                     NULL,
    answer           varchar(255)                                                                                                             NULL,
    CONSTRAINT user_content_t_pkey PRIMARY KEY (id),
    CONSTRAINT fkhtl6t6yakjrdehof6uj2rx1un FOREIGN KEY (topic_id) REFERENCES public.user_topic_t (id) ON DELETE CASCADE,
    CONSTRAINT fkso6juykhvw5ufwg0r7f4b6siv FOREIGN KEY (content_id) REFERENCES public.content_t (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_user_topic_id ON user_content_t (topic_id)