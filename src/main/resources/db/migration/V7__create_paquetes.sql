CREATE TABLE paquetes (
    id_paquete          BIGSERIAL PRIMARY KEY,
    folio               VARCHAR(30) NOT NULL UNIQUE,
    id_cliente          BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT,
    id_centro_origen    INTEGER NOT NULL REFERENCES centros_distribucion(id_centro) ON UPDATE CASCADE ON DELETE RESTRICT,
    direccion_origen    VARCHAR(255) NOT NULL,
    direccion_destino   VARCHAR(255) NOT NULL,
    coordenadas_destino POINT,
    peso_kg             NUMERIC(6,2) NOT NULL CHECK (peso_kg > 0),
    tamano_etiqueta     VARCHAR(20) CHECK (tamano_etiqueta IN ('CHICO', 'MEDIANO', 'GRANDE')),
    es_prioritario      BOOLEAN NOT NULL DEFAULT false,
    es_fragil           BOOLEAN NOT NULL DEFAULT false,
    estado_actual       VARCHAR(30) NOT NULL,
    fecha_creacion      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_paquetes_id_cliente ON paquetes(id_cliente);
CREATE INDEX idx_paquetes_id_centro_origen ON paquetes(id_centro_origen);
CREATE INDEX idx_paquetes_estado_actual ON paquetes(estado_actual);
CREATE INDEX idx_paquetes_folio ON paquetes(folio);