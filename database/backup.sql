--
-- PostgreSQL database dump
--

\restrict FvhZl0D2FXu4sSb3uLSHTujaz1ST1rDG31XIZ22Ic3pZS1utL60uzDWUctfPOep

-- Dumped from database version 16.15 (Ubuntu 16.15-0ubuntu0.24.04.1)
-- Dumped by pg_dump version 16.15 (Ubuntu 16.15-0ubuntu0.24.04.1)

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
-- Name: asesorias; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.asesorias (
    id bigint NOT NULL,
    profesor_id bigint NOT NULL,
    materia_id bigint NOT NULL,
    fecha date NOT NULL,
    hora time without time zone NOT NULL,
    lugar character varying(120) NOT NULL,
    cupo_max integer NOT NULL,
    estado character varying(20) DEFAULT 'ACTIVA'::character varying NOT NULL,
    notas character varying(500),
    creado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT asesorias_cupo_max_check CHECK (((cupo_max >= 1) AND (cupo_max <= 100))),
    CONSTRAINT asesorias_estado_check CHECK (((estado)::text = ANY ((ARRAY['ACTIVA'::character varying, 'CANCELADA'::character varying])::text[])))
);


--
-- Name: asesorias_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.asesorias_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: asesorias_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.asesorias_id_seq OWNED BY public.asesorias.id;


--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


--
-- Name: inscripciones; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.inscripciones (
    id bigint NOT NULL,
    asesoria_id bigint NOT NULL,
    alumno_id bigint NOT NULL,
    inscrito_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: inscripciones_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.inscripciones_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: inscripciones_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.inscripciones_id_seq OWNED BY public.inscripciones.id;


--
-- Name: materias; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.materias (
    id bigint NOT NULL,
    codigo character varying(20) NOT NULL,
    nombre character varying(120) NOT NULL
);


--
-- Name: materias_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.materias_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: materias_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.materias_id_seq OWNED BY public.materias.id;


--
-- Name: notificaciones; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notificaciones (
    id bigint NOT NULL,
    usuario_id bigint,
    canal character varying(10) NOT NULL,
    destino character varying(120) NOT NULL,
    asunto character varying(200),
    estado character varying(10) NOT NULL,
    error character varying(500),
    enviada_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT notificaciones_canal_check CHECK (((canal)::text = ANY ((ARRAY['EMAIL'::character varying, 'SMS'::character varying, 'WHATSAPP'::character varying])::text[]))),
    CONSTRAINT notificaciones_estado_check CHECK (((estado)::text = ANY ((ARRAY['ENVIADA'::character varying, 'FALLIDA'::character varying])::text[])))
);


--
-- Name: notificaciones_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.notificaciones_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: notificaciones_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.notificaciones_id_seq OWNED BY public.notificaciones.id;


--
-- Name: password_reset_tokens; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.password_reset_tokens (
    id bigint NOT NULL,
    usuario_id bigint NOT NULL,
    token_hash character varying(64) NOT NULL,
    expira_en timestamp without time zone NOT NULL,
    usado boolean DEFAULT false NOT NULL,
    creado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: password_reset_tokens_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.password_reset_tokens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: password_reset_tokens_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.password_reset_tokens_id_seq OWNED BY public.password_reset_tokens.id;


--
-- Name: profesor_materia; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.profesor_materia (
    profesor_id bigint NOT NULL,
    materia_id bigint NOT NULL
);


--
-- Name: roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    nombre character varying(20) NOT NULL
);


--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- Name: tokens_recuperacion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tokens_recuperacion (
    id bigint NOT NULL,
    usuario_id bigint NOT NULL,
    token_hash character varying(64) NOT NULL,
    expira_en timestamp without time zone NOT NULL,
    usado boolean DEFAULT false NOT NULL,
    creado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: tokens_recuperacion_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tokens_recuperacion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: tokens_recuperacion_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.tokens_recuperacion_id_seq OWNED BY public.tokens_recuperacion.id;


--
-- Name: tokens_revocados; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tokens_revocados (
    id bigint NOT NULL,
    jti character varying(64) NOT NULL,
    expira_en timestamp without time zone NOT NULL,
    revocado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: tokens_revocados_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tokens_revocados_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: tokens_revocados_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.tokens_revocados_id_seq OWNED BY public.tokens_revocados.id;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuarios (
    id bigint NOT NULL,
    rol_id bigint NOT NULL,
    nombre character varying(120) NOT NULL,
    correo character varying(120) NOT NULL,
    password_hash character varying(255) NOT NULL,
    telefono character varying(20),
    avatar_url character varying(255),
    activo boolean DEFAULT true NOT NULL,
    creado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: usuarios_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.usuarios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: usuarios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.usuarios_id_seq OWNED BY public.usuarios.id;


--
-- Name: asesorias id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesorias ALTER COLUMN id SET DEFAULT nextval('public.asesorias_id_seq'::regclass);


--
-- Name: inscripciones id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.inscripciones ALTER COLUMN id SET DEFAULT nextval('public.inscripciones_id_seq'::regclass);


--
-- Name: materias id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.materias ALTER COLUMN id SET DEFAULT nextval('public.materias_id_seq'::regclass);


--
-- Name: notificaciones id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notificaciones ALTER COLUMN id SET DEFAULT nextval('public.notificaciones_id_seq'::regclass);


--
-- Name: password_reset_tokens id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_reset_tokens ALTER COLUMN id SET DEFAULT nextval('public.password_reset_tokens_id_seq'::regclass);


--
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- Name: tokens_recuperacion id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tokens_recuperacion ALTER COLUMN id SET DEFAULT nextval('public.tokens_recuperacion_id_seq'::regclass);


--
-- Name: tokens_revocados id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tokens_revocados ALTER COLUMN id SET DEFAULT nextval('public.tokens_revocados_id_seq'::regclass);


--
-- Name: usuarios id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id SET DEFAULT nextval('public.usuarios_id_seq'::regclass);


--
-- Data for Name: asesorias; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.asesorias (id, profesor_id, materia_id, fecha, hora, lugar, cupo_max, estado, notas, creado_en) FROM stdin;
\.


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	crear roles y usuarios	SQL	V1__crear_roles_y_usuarios.sql	1047942275	asesorias_app	2026-11-16 02:46:47.711862	216	t
2	2	crear materias y profesor materia	SQL	V2__crear_materias_y_profesor_materia.sql	-789521193	asesorias_app	2026-11-16 02:46:48.181299	69	t
3	3	crear asesorias e inscripciones	SQL	V3__crear_asesorias_e_inscripciones.sql	785193787	asesorias_app	2026-11-16 02:46:48.394526	165	t
4	4	crear tokens	SQL	V4__crear_tokens.sql	1931524805	asesorias_app	2026-11-16 02:46:48.64777	90	t
5	5	crear notificaciones	SQL	V5__crear_notificaciones.sql	441870441	asesorias_app	2026-11-16 02:46:48.818043	61	t
6	6	crear tokens recuperacion	SQL	V6__crear_tokens_recuperacion.sql	-1620299635	asesorias_app	2026-11-20 23:56:00.167993	270	t
\.


--
-- Data for Name: inscripciones; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.inscripciones (id, asesoria_id, alumno_id, inscrito_en) FROM stdin;
\.


--
-- Data for Name: materias; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.materias (id, codigo, nombre) FROM stdin;
\.


--
-- Data for Name: notificaciones; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.notificaciones (id, usuario_id, canal, destino, asunto, estado, error, enviada_en) FROM stdin;
\.


--
-- Data for Name: password_reset_tokens; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.password_reset_tokens (id, usuario_id, token_hash, expira_en, usado, creado_en) FROM stdin;
\.


--
-- Data for Name: profesor_materia; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.profesor_materia (profesor_id, materia_id) FROM stdin;
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.roles (id, nombre) FROM stdin;
1	ADMIN
2	PROFESOR
3	ALUMNO
\.


--
-- Data for Name: tokens_recuperacion; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.tokens_recuperacion (id, usuario_id, token_hash, expira_en, usado, creado_en) FROM stdin;
2	2	d366b4d824827538d55aef1ec4ddc1d24ebc1f2931d9f3f1e9c25bcef2303693	2026-11-21 00:35:28.262661	t	2026-11-21 00:05:28.263182
\.


--
-- Data for Name: tokens_revocados; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.tokens_revocados (id, jti, expira_en, revocado_en) FROM stdin;
\.


--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.usuarios (id, rol_id, nombre, correo, password_hash, telefono, avatar_url, activo, creado_en) FROM stdin;
2	3	Francisco	jd.uarez.aniel08@gmail.com	$2a$10$1RrPCXsQIh7O6von1DtphuIJYrg4ybSKgZ2RDD3qkfXul3j6PVbrK	+529512468578	\N	t	2026-11-21 00:04:06.255831
\.


--
-- Name: asesorias_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.asesorias_id_seq', 1, false);


--
-- Name: inscripciones_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.inscripciones_id_seq', 1, false);


--
-- Name: materias_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.materias_id_seq', 1, false);


--
-- Name: notificaciones_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.notificaciones_id_seq', 1, false);


--
-- Name: password_reset_tokens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.password_reset_tokens_id_seq', 1, false);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.roles_id_seq', 3, true);


--
-- Name: tokens_recuperacion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.tokens_recuperacion_id_seq', 2, true);


--
-- Name: tokens_revocados_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.tokens_revocados_id_seq', 1, false);


--
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.usuarios_id_seq', 2, true);


--
-- Name: asesorias asesorias_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesorias
    ADD CONSTRAINT asesorias_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: inscripciones inscripciones_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.inscripciones
    ADD CONSTRAINT inscripciones_pkey PRIMARY KEY (id);


--
-- Name: materias materias_codigo_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.materias
    ADD CONSTRAINT materias_codigo_key UNIQUE (codigo);


--
-- Name: materias materias_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.materias
    ADD CONSTRAINT materias_pkey PRIMARY KEY (id);


--
-- Name: notificaciones notificaciones_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notificaciones
    ADD CONSTRAINT notificaciones_pkey PRIMARY KEY (id);


--
-- Name: password_reset_tokens password_reset_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_reset_tokens
    ADD CONSTRAINT password_reset_tokens_pkey PRIMARY KEY (id);


--
-- Name: password_reset_tokens password_reset_tokens_token_hash_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_reset_tokens
    ADD CONSTRAINT password_reset_tokens_token_hash_key UNIQUE (token_hash);


--
-- Name: profesor_materia profesor_materia_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.profesor_materia
    ADD CONSTRAINT profesor_materia_pkey PRIMARY KEY (profesor_id, materia_id);


--
-- Name: roles roles_nombre_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_nombre_key UNIQUE (nombre);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: tokens_recuperacion tokens_recuperacion_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tokens_recuperacion
    ADD CONSTRAINT tokens_recuperacion_pkey PRIMARY KEY (id);


--
-- Name: tokens_recuperacion tokens_recuperacion_token_hash_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tokens_recuperacion
    ADD CONSTRAINT tokens_recuperacion_token_hash_key UNIQUE (token_hash);


--
-- Name: tokens_revocados tokens_revocados_jti_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tokens_revocados
    ADD CONSTRAINT tokens_revocados_jti_key UNIQUE (jti);


--
-- Name: tokens_revocados tokens_revocados_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tokens_revocados
    ADD CONSTRAINT tokens_revocados_pkey PRIMARY KEY (id);


--
-- Name: inscripciones uq_inscripcion_asesoria_alumno; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.inscripciones
    ADD CONSTRAINT uq_inscripcion_asesoria_alumno UNIQUE (asesoria_id, alumno_id);


--
-- Name: usuarios usuarios_correo_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_correo_key UNIQUE (correo);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: idx_asesorias_fecha; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_asesorias_fecha ON public.asesorias USING btree (fecha);


--
-- Name: idx_asesorias_materia; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_asesorias_materia ON public.asesorias USING btree (materia_id);


--
-- Name: idx_asesorias_profesor; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_asesorias_profesor ON public.asesorias USING btree (profesor_id);


--
-- Name: idx_inscripciones_alumno; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_inscripciones_alumno ON public.inscripciones USING btree (alumno_id);


--
-- Name: idx_notificaciones_canal; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_notificaciones_canal ON public.notificaciones USING btree (canal);


--
-- Name: idx_notificaciones_usuario; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_notificaciones_usuario ON public.notificaciones USING btree (usuario_id);


--
-- Name: idx_password_reset_usuario; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_password_reset_usuario ON public.password_reset_tokens USING btree (usuario_id);


--
-- Name: idx_profesor_materia_materia; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_profesor_materia_materia ON public.profesor_materia USING btree (materia_id);


--
-- Name: idx_tokens_recuperacion_usuario; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_tokens_recuperacion_usuario ON public.tokens_recuperacion USING btree (usuario_id);


--
-- Name: idx_usuarios_rol; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_usuarios_rol ON public.usuarios USING btree (rol_id);


--
-- Name: asesorias asesorias_materia_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesorias
    ADD CONSTRAINT asesorias_materia_id_fkey FOREIGN KEY (materia_id) REFERENCES public.materias(id);


--
-- Name: asesorias asesorias_profesor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesorias
    ADD CONSTRAINT asesorias_profesor_id_fkey FOREIGN KEY (profesor_id) REFERENCES public.usuarios(id);


--
-- Name: inscripciones inscripciones_alumno_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.inscripciones
    ADD CONSTRAINT inscripciones_alumno_id_fkey FOREIGN KEY (alumno_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


--
-- Name: inscripciones inscripciones_asesoria_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.inscripciones
    ADD CONSTRAINT inscripciones_asesoria_id_fkey FOREIGN KEY (asesoria_id) REFERENCES public.asesorias(id) ON DELETE CASCADE;


--
-- Name: notificaciones notificaciones_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notificaciones
    ADD CONSTRAINT notificaciones_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE SET NULL;


--
-- Name: password_reset_tokens password_reset_tokens_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_reset_tokens
    ADD CONSTRAINT password_reset_tokens_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


--
-- Name: profesor_materia profesor_materia_materia_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.profesor_materia
    ADD CONSTRAINT profesor_materia_materia_id_fkey FOREIGN KEY (materia_id) REFERENCES public.materias(id) ON DELETE CASCADE;


--
-- Name: profesor_materia profesor_materia_profesor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.profesor_materia
    ADD CONSTRAINT profesor_materia_profesor_id_fkey FOREIGN KEY (profesor_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


--
-- Name: tokens_recuperacion tokens_recuperacion_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tokens_recuperacion
    ADD CONSTRAINT tokens_recuperacion_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


--
-- Name: usuarios usuarios_rol_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_rol_id_fkey FOREIGN KEY (rol_id) REFERENCES public.roles(id);


--
-- PostgreSQL database dump complete
--

\unrestrict FvhZl0D2FXu4sSb3uLSHTujaz1ST1rDG31XIZ22Ic3pZS1utL60uzDWUctfPOep

