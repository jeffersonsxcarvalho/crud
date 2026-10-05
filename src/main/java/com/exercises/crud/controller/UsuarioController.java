package com.exercises.crud.controller;

import com.exercises.crud.dto.UsuarioRequest;
import com.exercises.crud.dto.UsuarioResponse;
import com.exercises.crud.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar usuários")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {

        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable("id") Long id) {

        return ResponseEntity.ok(service.buscarPorId(id));

    }

    @PostMapping
    @Operation(summary = "Criar usuario")
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody UsuarioRequest request) {

        UsuarioResponse response = service.cadastrar(request);

        return ResponseEntity
                .created(URI.create("/api/usuarios/" + response.id()))
                .body(response);

    }

    @PutMapping("/{id}")
    @Operation(summary = "Criar usuario")
    public UsuarioResponse atualizar(@PathVariable("id") Long id, @RequestBody UsuarioRequest usuario) {

        return service.atualizar(id, usuario);

    }
}
