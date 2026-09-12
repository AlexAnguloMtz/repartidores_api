package com.aramdev.delivery.dto;

import com.aramdev.delivery.validation.ValidPassword;
import com.aramdev.delivery.validation.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(

        Integer idRol,

        @Size(max = 100)
        String nombre,

        @Email
        @Size(max = 100)
        String email,

        @ValidPhone
        String telefono,

        @ValidPassword
        String password

) {
}