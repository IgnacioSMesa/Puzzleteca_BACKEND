DROP SCHEMA puzzles CASCADE;
CREATE SCHEMA puzzles;
SET search_path TO puzzles;

-- ==========================
-- TABLA USUARIO
-- ==========================
CREATE TABLE usuario (
    id_usuario INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    passwd VARCHAR(255) NOT NULL,
    tipousuario VARCHAR(50) DEFAULT 'Usuario'
);

-- ==========================
-- TABLA PUZZLE
-- ==========================
CREATE TABLE puzzle (
    id_puzzle INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    titulo VARCHAR (150),
    autor VARCHAR(150),
    tiempo INT,
    piezas INT,
    dificultad VARCHAR(50),
    descripcion TEXT,
    color BOOLEAN,
    valoracion INT,
    estado text,
    id_usuario INT NOT NULL,
    imagen_url TEXT DEFAULT NULL,

    CONSTRAINT fk_puzzle_usuario
        FOREIGN KEY (id_usuario)
            REFERENCES usuario(id_usuario)
            ON DELETE CASCADE
);

-- ==========================
-- TABLA CONVERSACION
-- ==========================
CREATE TABLE conversacion (
    id_conversation INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    creado_en TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- ==========================
-- TABLA PARTICIPANTES_CONVERSACION
-- ==========================
CREATE TABLE participantes_conversacion (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_conversation INT NOT NULL,
    id_usuario INT NOT NULL,

    CONSTRAINT fk_participante_conversacion
        FOREIGN KEY (id_conversation)
            REFERENCES conversacion(id_conversation)
            ON DELETE CASCADE,

    CONSTRAINT fk_participante_usuario
        FOREIGN KEY (id_usuario)
            REFERENCES usuario(id_usuario)
            ON DELETE CASCADE,

    CONSTRAINT unique_participante
        UNIQUE (id_conversation, id_usuario)
);

-- ==========================
-- TABLA MENSAJE
-- ==========================
CREATE TABLE mensaje (
    id_mensaje INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    contenido TEXT NOT NULL,
    creado_en TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    leido_en TIMESTAMPTZ NULL,
    id_conversation INT NOT NULL,
    id_usuario INT NOT NULL,

    CONSTRAINT fk_mensaje_conversacion
     FOREIGN KEY (id_conversation)
         REFERENCES conversacion(id_conversation)
         ON DELETE CASCADE,

    CONSTRAINT fk_mensaje_usuario
     FOREIGN KEY (id_usuario)
         REFERENCES usuario(id_usuario)
         ON DELETE CASCADE
);
INSERT INTO puzzles.usuario (nombre, apellido, email, passwd, tipousuario) VALUES
                                                                               ('Ignacio',  'Mesa',     'ignaciosanzmesa@gmail.com',  '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin'),
                                                                               ('Natalia',  'García',   'natalia@gmail.com',  '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Usuario'),
                                                                               ('Carlos',   'López',    'carlos@gmail.com',   '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Usuario'),
                                                                               ('Sara',     'Martínez', 'sara@gmail.com',     '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Usuario'),
                                                                               ('Pablo',    'Sánchez',  'pablo@gmail.com',    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Usuario')
