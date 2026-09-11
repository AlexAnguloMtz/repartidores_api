CREATE TABLE comprobantes_entrega (
    id_comprobante      BIGSERIAL PRIMARY KEY,
    id_paquete          BIGINT NOT NULL REFERENCES paquetes(id_paquete) ON UPDATE CASCADE ON DELETE CASCADE,
    id_repartidor       BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT,
    nombre_receptor     VARCHAR(150) NOT NULL,
    url_foto_evidencia  VARCHAR(500),
    url_firma_receptor  VARCHAR(500),
    fecha_hora          TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_comprobantes_id_paquete ON comprobantes_entrega(id_paquete);
CREATE INDEX idx_comprobantes_id_repartidor ON comprobantes_entrega(id_repartidor);

