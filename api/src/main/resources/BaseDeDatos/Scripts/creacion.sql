DROP SCHEMA puzzles CASCADE;
CREATE SCHEMA puzzles;
SET search_path TO puzzles;

-- ==========================
-- TABLA USUARIO
-- ==========================
CREATE TABLE usuario
(
    id_usuario  INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    apellido    VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    contrasenna VARCHAR(255) NOT NULL,
    tipousuario VARCHAR(50) DEFAULT 'Usuario'
);

-- ==========================
-- TABLA PUZZLE
-- ==========================
CREATE TABLE puzzle
(
    id_puzzle          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    titulo             VARCHAR(150),
    autor              VARCHAR(150),
    tiempo             INT,
    piezas             INT,
    dificultad         VARCHAR(50),
    descripcion        TEXT,
    color              BOOLEAN,
    valoracion_media   DECIMAL(3, 2) DEFAULT 0,
    total_valoraciones INT           DEFAULT 0,
    estado             text,
    imagen_url         TEXT          DEFAULT 'puzzles/foto_predeterminada.png',
    id_usuario         INT NOT NULL,

    CONSTRAINT fk_puzzle_usuario
        FOREIGN KEY (id_usuario)
            REFERENCES usuario (id_usuario)
            ON DELETE CASCADE
);

-- ==========================
-- TABLA VALORACION_PUZZLE
-- ==========================
CREATE TABLE valoracion_puzzle
(
    id_valoracion INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    id_usuario    INT NOT NULL,
    id_puzzle     INT NOT NULL,

    valoracion    INT NOT NULL CHECK (valoracion BETWEEN 1 AND 5),

    fecha         TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_valoracion_usuario
        FOREIGN KEY (id_usuario)
            REFERENCES usuario (id_usuario)
            ON DELETE CASCADE,

    CONSTRAINT fk_valoracion_puzzle
        FOREIGN KEY (id_puzzle)
            REFERENCES puzzle (id_puzzle)
            ON DELETE CASCADE,

    -- 🔒 Impide que un usuario vote más de una vez
    CONSTRAINT unique_valoracion_usuario_puzzle
        UNIQUE (id_usuario, id_puzzle)
);

-- ==========================
-- TABLA CONVERSACION
-- ==========================
CREATE TABLE conversacion
(
    id_conversation INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    creado_en       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    actualizado_en  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- ==========================
-- TABLA PARTICIPANTES_CONVERSACION
-- ==========================
CREATE TABLE participantes_conversacion
(
    id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_conversation INT NOT NULL,
    id_usuario      INT NOT NULL,

    CONSTRAINT fk_participante_conversacion
        FOREIGN KEY (id_conversation)
            REFERENCES conversacion (id_conversation)
            ON DELETE CASCADE,

    CONSTRAINT fk_participante_usuario
        FOREIGN KEY (id_usuario)
            REFERENCES usuario (id_usuario)
            ON DELETE CASCADE,

    CONSTRAINT unique_participante
        UNIQUE (id_conversation, id_usuario)
);

-- ==========================
-- TABLA MENSAJE
-- ==========================
CREATE TABLE mensaje
(
    id_mensaje      INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    contenido       TEXT NOT NULL,
    creado_en       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    leido_en        TIMESTAMPTZ DEFAULT NULL,
    id_conversation INT  NOT NULL,
    id_usuario      INT  NOT NULL,

    CONSTRAINT fk_mensaje_conversacion
        FOREIGN KEY (id_conversation)
            REFERENCES conversacion (id_conversation)
            ON DELETE CASCADE,

    CONSTRAINT fk_mensaje_usuario
        FOREIGN KEY (id_usuario)
            REFERENCES usuario (id_usuario)
            ON DELETE CASCADE
);

-- ==========================
-- TABLA POST (foro)
-- ==========================
CREATE TABLE post
(
    id_post           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    contenido         TEXT,
    imagen_url        TEXT DEFAULT 'puzzles/foto_predeterminada.png',

    fecha_creacion    TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    total_likes       INT         DEFAULT 0 CHECK (total_likes >= 0),
    total_comentarios INT         DEFAULT 0 CHECK (total_comentarios >= 0),

    id_usuario        INT NOT NULL,

    CONSTRAINT fk_post_usuario
        FOREIGN KEY (id_usuario)
            REFERENCES usuario (id_usuario)
            ON DELETE CASCADE
);

-- ==========================
-- TABLA COMENTARIO (Comentarios de un post)
-- ==========================
CREATE TABLE comentario
(
    id_comentario  INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    contenido      TEXT NOT NULL,
    fecha_creacion TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    id_usuario     INT  NOT NULL,
    id_post        INT  NOT NULL,

    CONSTRAINT fk_comentario_usuario
        FOREIGN KEY (id_usuario)
            REFERENCES usuario (id_usuario)
            ON DELETE CASCADE,

    CONSTRAINT fk_comentario_post
        FOREIGN KEY (id_post)
            REFERENCES post (id_post)
            ON DELETE CASCADE
);

INSERT INTO usuario (nombre, apellido, email, contrasenna, tipousuario)
VALUES ('Ignacio', 'Mesa', 'ignaciosanzmesa@gmail.com', '$2a$10$jXbGrZ5BlBECqM.ysP1F8OwcqDwHdY09D/P5zKKsqNsWfIss8xOE2',
        'Admin'),
    ('n', 'n', 'n@gmail.com', '$2a$10$jXbGrZ5BlBECqM.ysP1F8OwcqDwHdY09D/P5zKKsqNsWfIss8xOE2',
        'Usuario'),
       ('a', 'a', 'a@gmail.com', '$2a$10$jXbGrZ5BlBECqM.ysP1F8OwcqDwHdY09D/P5zKKsqNsWfIss8xOE2',
        'Admin');;