CREATE TABLE tickets_soporte (
    id_ticket       BIGSERIAL PRIMARY KEY,
    id_cliente      BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT,
    id_paquete      BIGINT REFERENCES paquetes(id_paquete) ON UPDATE CASCADE ON DELETE SET NULL,
    motivo          VARCHAR(150) NOT NULL,
    descripcion     TEXT,
    estado          VARCHAR(20) NOT NULL DEFAULT 'abierto'
                        CHECK (estado IN ('abierto', 'en_proceso', 'resuelto', 'cerrado')),
    fecha_creacion  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_tickets_id_cliente ON tickets_soporte(id_cliente);
CREATE INDEX idx_tickets_id_paquete ON tickets_soporte(id_paquete);
CREATE INDEX idx_tickets_estado ON tickets_soporte(estado);

