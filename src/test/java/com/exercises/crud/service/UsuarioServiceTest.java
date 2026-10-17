package com.exercises.crud.service;

import com.exercises.crud.dto.UsuarioRequest;
import com.exercises.crud.dto.UsuarioResponse;
import com.exercises.crud.entity.Usuario;
import com.exercises.crud.exception.EmailJaCadastradoException;
import com.exercises.crud.exception.UsuarioNaoEncontradoException;
import com.exercises.crud.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    public void deveRetornarListaResponse() {
        //Arrange

        List<Usuario> usuarios = List.of(
                new Usuario(1L, "Ana", "ana@email.com", "hash1", "USER"),
                new Usuario(2L, "João", "joao@email.com", "hash2", "USER")
        );

        when(repository.findAll()).thenReturn(usuarios);

        //Act
        List<UsuarioResponse> lista = usuarioService.listarTodos();

        //Assert
        assertEquals(usuarios.size(), lista.size());
        assertEquals("Ana", lista.get(0).nome());
    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoCadastradoAoBuscarPorId() {
        //Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(
                UsuarioNaoEncontradoException.class,
                () -> usuarioService.buscarPorId(1L)
        );
    }

    @Test
    public void deveRetornarUsuarioPeloId() {
        //Arrange
        Usuario usuario = new Usuario(1L,"Ana", "ana@email.com", "hash1", "USER");

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        //Act
        UsuarioResponse usuarioResponse = usuarioService.buscarPorId(1L);

        //Assert
        assertEquals("Ana", usuarioResponse.nome());
        assertEquals("ana@email.com", usuarioResponse.email());

        verify(repository).findById(1L);
    }

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

        verify(repository, never()).save(any(Usuario.class));
        verify(passwordEncoder, never()).encode(anyString());

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

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoEncontradoAoAtualizar() {
        //Arrange
        UsuarioRequest usuarioRequest = new UsuarioRequest("Ana", "ana@email.com", "hash", "USER");

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(
                UsuarioNaoEncontradoException.class,
                () -> usuarioService.atualizar(1L, usuarioRequest)
        );

        verify(repository, never()).save(any(Usuario.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    public void deveAtualizarUsuario() {
        //Arrange
        Usuario usuario = new Usuario(1L, "Ana", "ana@email.com", "hash", "USER");
        UsuarioRequest usuarioRequest = new UsuarioRequest("Ana", "ana@email.com", "hash", "USER");

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        when(passwordEncoder.encode(usuarioRequest.senha()))
                .thenReturn("hash-simulado");

        when(repository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        UsuarioResponse usuarioResponse = usuarioService.atualizar(1L, usuarioRequest);

        //Assert
        assertEquals("Ana", usuarioResponse.nome());
        assertEquals("ana@email.com", usuarioResponse.email());

        verify(passwordEncoder).encode(usuarioRequest.senha());

        verify(repository).save(argThat(
                usuario1 -> usuario1
                        .getSenha()
                        .equals("hash-simulado")
        ));
    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoEncontradoAoDeletar() {
        //Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(
                UsuarioNaoEncontradoException.class,
                () -> usuarioService.deletar(1L)
        );

        verify(repository, never()).delete(any(Usuario.class));
    }

    @Test
    public void deveDeletarUsuario() {
        Usuario usuario = new Usuario(1L, "Ana", "ana@email.com", "hash", "USER");
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.deletar(1L);

        verify(repository).delete(usuario);
    }


}
