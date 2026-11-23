--
-- PostgreSQL database dump
--


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
1	2	1	2026-11-23	16:00:00	Aula B-204	6	ACTIVA	\N	2026-11-22 23:08:37.317375
2	2	2	2026-11-24	10:00:00	Aula B-205	8	ACTIVA	\N	2026-11-22 23:08:37.334429
3	2	4	2026-11-26	12:00:00	Aula B-110	5	ACTIVA	\N	2026-11-22 23:08:37.345196
4	3	5	2026-11-23	11:00:00	Lab de química	6	ACTIVA	\N	2026-11-22 23:08:37.358161
5	3	6	2026-11-25	13:00:00	Lab de química	4	ACTIVA	\N	2026-11-22 23:08:37.365606
6	3	12	2026-11-27	09:00:00	Aula C-101	10	ACTIVA	\N	2026-11-22 23:08:37.373074
7	4	2	2026-11-24	14:00:00	Aula A-12	6	ACTIVA	\N	2026-11-22 23:08:37.378923
8	4	3	2026-11-25	16:00:00	Aula A-14	8	ACTIVA	\N	2026-11-22 23:08:37.386079
9	4	8	2026-11-28	10:00:00	Lab de física	5	ACTIVA	\N	2026-11-22 23:08:37.393085
10	4	9	2026-11-29	12:00:00	Lab de física	6	ACTIVA	\N	2026-11-22 23:08:37.401025
11	5	7	2026-11-24	09:00:00	Lab de biología	7	ACTIVA	\N	2026-11-22 23:08:37.409635
12	5	10	2026-11-26	15:00:00	Sala de cómputo 1	10	ACTIVA	\N	2026-11-22 23:08:37.41916
13	5	11	2026-11-27	17:00:00	Sala de cómputo 2	8	ACTIVA	\N	2026-11-22 23:08:37.426439
14	5	10	2026-11-30	11:00:00	Sala de cómputo 1	12	ACTIVA	\N	2026-11-22 23:08:37.432469
15	3	5	2026-12-01	10:00:00	Lab de química	6	ACTIVA	\N	2026-11-22 23:08:37.440419
\.


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	crear roles y usuarios	SQL	V1__crear_roles_y_usuarios.sql	1047942275	asesorias_app	2026-11-22 23:08:12.325179	158	t
2	2	crear materias y profesor materia	SQL	V2__crear_materias_y_profesor_materia.sql	-789521193	asesorias_app	2026-11-22 23:08:12.660468	76	t
3	3	crear asesorias e inscripciones	SQL	V3__crear_asesorias_e_inscripciones.sql	785193787	asesorias_app	2026-11-22 23:08:12.85554	118	t
4	4	crear tokens	SQL	V4__crear_tokens.sql	1931524805	asesorias_app	2026-11-22 23:08:13.048527	80	t
5	5	crear notificaciones	SQL	V5__crear_notificaciones.sql	441870441	asesorias_app	2026-11-22 23:08:13.196683	68	t
6	6	crear tokens recuperacion	SQL	V6__crear_tokens_recuperacion.sql	-1620299635	asesorias_app	2026-11-22 23:08:13.334523	38	t
\.


--
-- Data for Name: inscripciones; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.inscripciones (id, asesoria_id, alumno_id, inscrito_en) FROM stdin;
1	1	6	2026-11-22 23:08:37.457901
2	1	7	2026-11-22 23:08:37.468585
3	1	8	2026-11-22 23:08:37.477104
4	2	6	2026-11-22 23:08:37.482604
5	2	9	2026-11-22 23:08:37.489891
6	3	10	2026-11-22 23:08:37.499028
7	4	6	2026-11-22 23:08:37.50572
8	4	11	2026-11-22 23:08:37.510294
9	5	7	2026-11-22 23:08:37.517145
10	5	8	2026-11-22 23:08:37.521723
11	5	9	2026-11-22 23:08:37.525226
12	5	12	2026-11-22 23:08:37.532152
13	6	13	2026-11-22 23:08:37.535246
14	7	6	2026-11-22 23:08:37.539562
15	7	14	2026-11-22 23:08:37.544082
16	8	15	2026-11-22 23:08:37.548394
17	9	7	2026-11-22 23:08:37.554374
18	10	8	2026-11-22 23:08:37.559426
19	11	9	2026-11-22 23:08:37.564329
20	12	6	2026-11-22 23:08:37.56757
21	13	10	2026-11-22 23:08:37.572623
\.


--
-- Data for Name: materias; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.materias (id, codigo, nombre) FROM stdin;
1	MAT-101	Cálculo integral
2	MAT-102	Cálculo diferencial
3	MAT-103	Geometría analítica
4	MAT-104	Álgebra lineal
5	QUI-101	Química orgánica
6	QUI-102	Química inorgánica
7	BIO-101	Biología celular
8	FIS-101	Física mecánica
9	FIS-102	Electricidad y magnetismo
10	PRO-101	Programación básica
11	PRO-102	Estructuras de datos
12	EST-101	Probabilidad y estadística
\.


--
-- Data for Name: profesor_materia; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.profesor_materia (profesor_id, materia_id) FROM stdin;
2	1
2	4
2	2
3	5
3	6
3	12
4	9
4	3
4	8
4	2
5	7
5	11
5	10
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
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.usuarios (id, rol_id, nombre, correo, password_hash, telefono, avatar_url, activo, creado_en) FROM stdin;
1	1	Administrador	admin@asesorias.com	$2a$10$JnoozGdtLJIexWI4ED.gfunP0RPbwOBwIBR3jNnbS1z7kQQc1.ILq	\N	\N	t	2026-11-22 23:08:34.566542
2	2	Aracely Ramirez	aracely.ramirez@asesorias.com	$2a$10$ahNMeR.5rIRKtggD..1w/OQJIq6Xxdr4XE8uhCGeNdmJYk5u2RnXu	\N	\N	t	2026-11-22 23:08:35.093037
3	2	Adelina Martinez	adelina.mtz@asesorias.com	$2a$10$hOdTFziF4CDnpLhIDNp6CeNYbOis4rLeCveBaFxyetD8AF5e39rui	\N	\N	t	2026-11-22 23:08:35.323978
4	2	Bibiana Diaz	bibiana.diaz@asesorias.com	$2a$10$C3O.jB0.jBe8lp2cWQxOQOY1LzI8C2njzmPOM6J4DyK2JDnEhC0Re	\N	\N	t	2026-11-22 23:08:35.520704
5	2	Luis Alonso	luis.alonso@asesorias.com	$2a$10$U3lMEAogXgXCNuupP3TeF.mtmB38sDehIRin996gTk8bjYiqfxLc6	\N	\N	t	2026-11-22 23:08:35.7252
6	3	Isaac Diaz	carlos.juarez@asesorias.com	$2a$10$5Zd/0AhjP5LzxYIvugxZy.GgEfma1AVXd2pQFGp64XAaocUCceqP.	+529510000001	\N	t	2026-11-22 23:08:35.940294
7	3	María Hernández	maria.hernandez@asesorias.com	$2a$10$8FatWFGEnb0GuHK1qRbRJu2291RAFj4LJQaQbeRbsM1mc8YmhCS6O	+529510000002	\N	t	2026-11-22 23:08:36.128493
8	3	Diego Pérez	diego.perez@asesorias.com	$2a$10$hf6xSuT4H5DqpyhdVqvkU.8jQlFrQq/QBpUVenftON7X0BfZpLjTu	+529510000003	\N	t	2026-11-22 23:08:36.318463
9	3	Ana López	ana.lopez@asesorias.com	$2a$10$366ZeBtFuY5xGl5k2QliX.tbIvSz1GD5MYWvasPJGYQCQOmkB4GEi	+529510000004	\N	t	2026-11-22 23:08:36.513584
10	3	Jorge Santos	jorge.santos@asesorias.com	$2a$10$tGsdNdB1cNg/yWEm5.GznukqWO5SswBBUkkAD.LXw7Gz69Th9k2Xy	+529510000005	\N	t	2026-11-22 23:08:36.695505
11	3	Valeria Cruz	valeria.cruz@asesorias.com	$2a$10$4fOBQauIjllq/8jkb/wv6OBOfqfhuFqzui66w.IrUlE1aTvc11uOO	+529510000006	\N	t	2026-11-22 23:08:36.875057
12	3	Mateo Ramos	mateo.ramos@asesorias.com	$2a$10$d9DF8qQFcdncB02feFW2DeWAtjq/6YmFkL2NZNL.3IVDPrD1F58jS	+529510000007	\N	t	2026-11-22 23:08:37.015734
13	3	Camila Torres	camila.torres@asesorias.com	$2a$10$p885Zq.iGtHnYnv9GE7kQ.zOUKZaK5Aqwn4Ofg.9uMz2RCJa/aIVG	+529510000008	\N	t	2026-11-22 23:08:37.107915
14	3	Pablo Vargas	pablo.vargas@asesorias.com	$2a$10$akoD3dTrpD41afMvZcGJIOM8wQWOFgU.4Bfe9lQMwUTrIVrOAL/Y6	+529510000009	\N	t	2026-11-22 23:08:37.206141
15	3	Fernanda Ortiz	fernanda.ortiz@asesorias.com	$2a$10$vX5pTqixW.Tvo7PBga2fPOuaqw/G4E.h35duuRRxKqdD8vGqSsZH2	+529510000010	\N	t	2026-11-22 23:08:37.303643
\.


--
-- Name: asesorias_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.asesorias_id_seq', 15, true);


--
-- Name: inscripciones_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.inscripciones_id_seq', 24, true);


--
-- Name: materias_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.materias_id_seq', 12, true);


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

SELECT pg_catalog.setval('public.tokens_recuperacion_id_seq', 1, true);


--
-- Name: tokens_revocados_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.tokens_revocados_id_seq', 10, true);


--
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.usuarios_id_seq', 16, true);


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


