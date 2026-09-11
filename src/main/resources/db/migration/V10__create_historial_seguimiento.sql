CREATE TABLE historial_seguimiento (
    id_historial    BIGSERIAL PRIMARY KEY,
    id_paquete      BIGINT NOT NULL REFERENCES paquetes(id_paquete) ON UPDATE CASCADE ON DELETE CASCADE,
    titulo          VARCHAR(150) NOT NULL,
    descripcion     TEXT,
    fecha_hora      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_historial_id_paquete ON historial_seguimiento(id_paquete);
CREATE INDEX idx_historial_fecha_hora ON historial_seguimiento(fecha_hora);

