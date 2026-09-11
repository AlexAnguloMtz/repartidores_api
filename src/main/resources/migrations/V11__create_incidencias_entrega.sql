CREATE TABLE incidencias_entrega (
    id_incidencia       BIGSERIAL PRIMARY KEY,
    id_paquete          BIGINT NOT NULL REFERENCES paquetes(id_paquete) ON UPDATE CASCADE ON DELETE CASCADE,
    id_repartidor       BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT,
    tipo_incidencia     VARCHAR(50) NOT NULL
                            CHECK (tipo_incidencia IN
                                ('direccion_incorrecta', 'ausencia_destinatario', 'paquete_danado',
                                 'rechazo_destinatario', 'zona_insegura', 'otro')),
    comentario          TEXT,
    url_foto_reporte    VARCHAR(500),
    fecha_hora          TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_incidencias_id_paquete ON incidencias_entrega(id_paquete);
CREATE INDEX idx_incidencias_id_repartidor ON incidencias_entrega(id_repartidor);
CREATE INDEX idx_incidencias_tipo ON incidencias_entrega(tipo_incidencia);

