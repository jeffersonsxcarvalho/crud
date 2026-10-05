package com.exercises.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroResponse(
        int status,
        String erro,
        String mensagem,
        Map<String, String> campos
) {

    public static ErroResponse semCampos(
            HttpStatus status,
            String mensagem
    ) {
        return new ErroResponse(
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                null
        );
    }

    public static ErroResponse comFields(
            HttpStatus status,
            String message,
            List<FieldError> errors
    ) {
        Map<String, String> fields = errors.stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));

        return new ErroResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                fields
        );
    }

}
