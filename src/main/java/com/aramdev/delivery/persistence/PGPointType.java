package com.aramdev.delivery.persistence;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.usertype.UserType;
import org.postgresql.geometric.PGpoint;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class PGPointType implements UserType<PGpoint> {

    @Override
    public int getSqlType() {
        return Types.OTHER;
    }

    @Override
    public Class<PGpoint> returnedClass() {
        return PGpoint.class;
    }

    @Override
    public PGpoint nullSafeGet(
            ResultSet rs,
            int position,
            WrapperOptions options
    ) throws SQLException {
        return (PGpoint) rs.getObject(position);
    }

    @Override
    public void nullSafeSet(
            PreparedStatement st,
            PGpoint value,
            int position,
            WrapperOptions options
    ) throws SQLException {

        if (value == null) {
            st.setNull(position, Types.OTHER);
        } else {
            st.setObject(position, value);
        }
    }

    @Override
    public PGpoint deepCopy(PGpoint value) {
        if (value == null) {
            return null;
        }

        return new PGpoint(value.x, value.y);
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(PGpoint value) {
        return value;
    }

    @Override
    public PGpoint replace(PGpoint detached, PGpoint managed, Object owner) {
        return detached;
    }
}