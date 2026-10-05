package com.exercises.crud.exception;

import com.exercises.crud.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarUsuarioNaoEncontrado(
            UsuarioNaoEncontradoException exception
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ErroResponse erro = ErroResponse.semCampos(
                status,
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(erro);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErroResponse> tratarEmailJacadastrado(
            EmailJaCadastradoException exception
    ) {
        HttpStatus status = HttpStatus.CONFLICT;

        ErroResponse erro = ErroResponse.semCampos(
                status,
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarErroDeValidacao(
            MethodArgumentNotValidException exception
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErroResponse erro = ErroResponse.comFields(
                status,
                "Erro de validação.",
                exception.getBindingResult().getFieldErrors()
        );

        return ResponseEntity
                .status(status)
                .body(erro);
    }
}
