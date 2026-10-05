package com.exercises.crud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ter formato válido")
        @Schema(example = "usuario.user@exemplo.com")
        String email,

        @NotBlank(message = "Senha é obrigatória.")
        @Schema(
                    example = "senha123",
                    accessMode = Schema.AccessMode.READ_ONLY,
                    description = "Usada somente pelo PasswordEncoder.matches, nunca entra no jwt"
        )
        String senha
) {
}
