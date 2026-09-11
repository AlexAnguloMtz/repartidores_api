CREATE TABLE centros_distribucion (
    id_centro   SERIAL PRIMARY KEY,
    nombre      VARCHAR(150) NOT NULL,
    ciudad      VARCHAR(100) NOT NULL,
    direccion   VARCHAR(255) NOT NULL
);

CREATE INDEX idx_centros_ciudad ON centros_distribucion(ciudad);

