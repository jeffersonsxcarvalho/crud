package com.exercises.crud.dto;

import org.springframework.http.HttpStatus;

public record ErroResponse(
        int status,
        String erro,
        String mensagem
) {
}
