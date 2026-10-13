package com.exercises.crud;

import com.exercises.crud.dto.UsuarioRequest;
import com.exercises.crud.dto.UsuarioResponse;
import com.exercises.crud.entity.Usuario;
import com.exercises.crud.exception.EmailJaCadastradoException;
import com.exercises.crud.repository.UsuarioRepository;
import com.exercises.crud.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    public void deveLancarExcecaoQuandoEmailJaCadastrado() {
        // Arrange

        UsuarioRequest request = new UsuarioRequest("Nome", "exemplo@exemplo.com", "senha123", "ROLE_USER");

        when(repository.findByEmail(request.email()))
                .thenReturn(Optional.of(new Usuario(1L, "Nome", "exemplo@exemplo.com", "senha123", "ROLE_USER")));

        //ACT + Assert
        assertThrows(
                EmailJaCadastradoException.class,
                () -> usuarioService.cadastrar(request)
        );
    }

    @Test
    public void deveCadastrarUsuario() {
        // Arrange
        UsuarioRequest request = new UsuarioRequest("Nome", "exemplo@exemplo.com", "senha123", "USER");

        when(repository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(request.senha()))
                .thenReturn("hash-simulado");

        when(repository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        UsuarioResponse response = usuarioService.cadastrar(request);

        //Assert
        assertEquals("Nome", response.nome());
        assertEquals("exemplo@exemplo.com", response.email());

        verify(repository).save(argThat(
                usuario -> usuario
                        .getSenha()
                        .equals("hash-simulado")
        ));
    }

}
