set search_path to puzzlesbbdd;
-- ==========================
-- TABLA USUARIO
-- ==========================
CREATE TABLE usuario (
  id_usuario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  passwd VARCHAR(255) NOT NULL,
  tipousuario VARCHAR(50) NOT NULL
);

-- ==========================
-- TABLA PUZZLE
-- ==========================
CREATE TABLE puzzle (
  id_puzzle BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  autor VARCHAR(150),
  tiempo INT,
  piezas INT,
  dificultad VARCHAR(50),
  descripcion TEXT,
  color VARCHAR(50),
  valoracion NUMERIC(3,2),
  id_usuario BIGINT NOT NULL,
  
  CONSTRAINT fk_puzzle_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuario(id_usuario)
    ON DELETE CASCADE
);

-- ==========================
-- TABLA CONVERSACION
-- ==========================
CREATE TABLE conversacion (
  id_conversation BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  creado_en TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  actualizado_en TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- ==========================
-- TABLA PARTICIPANTES_CONVERSACION
-- ==========================
CREATE TABLE participantes_conversacion (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  id_conversation BIGINT NOT NULL,
  id_usuario BIGINT NOT NULL,
  
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
  id_mensaje BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  contenido TEXT NOT NULL,
  creado_en TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  leido_en TIMESTAMPTZ NULL,
  id_conversation BIGINT NOT NULL,
  id_usuario BIGINT NOT NULL,
  
  CONSTRAINT fk_mensaje_conversacion
    FOREIGN KEY (id_conversation)
    REFERENCES conversacion(id_conversation)
    ON DELETE CASCADE,
    
  CONSTRAINT fk_mensaje_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuario(id_usuario)
    ON DELETE CASCADE
);