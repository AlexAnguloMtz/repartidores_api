CREATE TABLE turnos_repartidor (
    id_turno                BIGSERIAL PRIMARY KEY,
    id_repartidor           BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT,
    id_unidad               INTEGER NOT NULL REFERENCES unidades(id_unidad) ON UPDATE CASCADE ON DELETE RESTRICT,
    fecha_turno             DATE NOT NULL,
    estado_turno            VARCHAR(20) NOT NULL DEFAULT 'programado'
                                CHECK (estado_turno IN ('programado', 'en_curso', 'finalizado', 'cancelado')),
    total_paquetes          INTEGER NOT NULL DEFAULT 0 CHECK (total_paquetes >= 0),
    tiempo_estimado_horas   NUMERIC(5,2) CHECK (tiempo_estimado_horas >= 0)
);

CREATE INDEX idx_turnos_id_repartidor ON turnos_repartidor(id_repartidor);
CREATE INDEX idx_turnos_id_unidad ON turnos_repartidor(id_unidad);
CREATE INDEX idx_turnos_fecha_turno ON turnos_repartidor(fecha_turno);

