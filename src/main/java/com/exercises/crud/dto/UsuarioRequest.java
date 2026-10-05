package com.exercises.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequest(
        @Schema(description = "Informar nome do usuário", example = "Jefferson")
        @NotBlank(message = "Nome deve ser preenchido.")
        String nome,

        @Schema(description = "Informar o email do usuário", example = "Jefferson@jeff.com")
        @NotBlank(message = "Email deve ser preenchido.")
        String email,

        @Schema(description = "Envia senha", example = "Jefferson")
        @NotBlank(message = "Senha deve ser preenchida.")
        String senha,

        @Schema(description = "Envia role", example = "ADMIN")
        @NotBlank(message = "Role deve ser preenchida.")
        String role
) {

}
