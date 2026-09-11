CREATE TABLE unidades (
    id_unidad       SERIAL PRIMARY KEY,
    id_centro       INTEGER NOT NULL REFERENCES centros_distribucion(id_centro) ON UPDATE CASCADE ON DELETE RESTRICT,
    codigo_unidad   VARCHAR(20) NOT NULL UNIQUE,
    placas          VARCHAR(15) NOT NULL UNIQUE
);

CREATE INDEX idx_unidades_id_centro ON unidades(id_centro);

