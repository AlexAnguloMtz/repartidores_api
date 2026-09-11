CREATE TABLE paradas_ruta (
    id_parada       BIGSERIAL PRIMARY KEY,
    id_turno        BIGINT NOT NULL REFERENCES turnos_repartidor(id_turno) ON UPDATE CASCADE ON DELETE CASCADE,
    id_paquete      BIGINT NOT NULL REFERENCES paquetes(id_paquete) ON UPDATE CASCADE ON DELETE RESTRICT,
    orden_secuencia INTEGER NOT NULL CHECK (orden_secuencia > 0),
    estado_parada   VARCHAR(20) NOT NULL DEFAULT 'pendiente'
                        CHECK (estado_parada IN ('pendiente', 'en_camino', 'completada', 'fallida')),
    UNIQUE (id_turno, orden_secuencia)
);

CREATE INDEX idx_paradas_id_turno ON paradas_ruta(id_turno);
CREATE INDEX idx_paradas_id_paquete ON paradas_ruta(id_paquete);

