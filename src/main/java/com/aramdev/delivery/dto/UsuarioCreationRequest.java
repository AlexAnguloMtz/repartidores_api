package com.aramdev.delivery.dto;

import com.aramdev.delivery.validation.ValidPassword;
import com.aramdev.delivery.validation.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioCreationRequest(

        @NotNull
        Integer idRol,

        @NotBlank
        @Size(max = 100)
        String nombre,

        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotBlank
        @ValidPhone
        String telefono,

        @NotBlank
        @ValidPassword
        String password

) {
}