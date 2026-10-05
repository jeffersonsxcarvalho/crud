package com.exercises.crud.dto;

import com.exercises.crud.entity.Usuario;

public record UsuarioResponse(
        Long id,

        String nome,

        String email
) {
    public static UsuarioResponse de(Usuario usuario){
            return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
