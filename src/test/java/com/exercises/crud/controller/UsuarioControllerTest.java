package com.exercises.crud.controller;

import com.exercises.crud.config.JwtFilter;
import com.exercises.crud.dto.UsuarioRequest;
import com.exercises.crud.dto.UsuarioResponse;
import com.exercises.crud.entity.Usuario;
import com.exercises.crud.service.JwtService;
import com.exercises.crud.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(
        controllers = UsuarioController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void deveListarUsuarios() throws Exception {
        //Arrange
        List<UsuarioResponse> usuarios = List.of(
                new UsuarioResponse(1L, "Ana", "ana@email.com"),
                new UsuarioResponse(2L, "João", "joao@email.com")
        );

        when(usuarioService.listarTodos()).thenReturn(usuarios);

        //Act + Assert
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Ana"))
                .andExpect(jsonPath("$[1].nome").value("João"));

        verify(usuarioService).listarTodos();
    }

    @Test
    public void deveBuscarUsuarioPorId() throws Exception {
        //Arrange
        Long id = 1L;

        UsuarioResponse usuario = new UsuarioResponse(id, "Ana", "ana@email.com");

        when(usuarioService.buscarPorId(id)).thenReturn(usuario);

        //Act + Assert
        mockMvc.perform(get("/api/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Ana"))
                .andExpect(jsonPath("$.email").value("ana@email.com"));

        verify(usuarioService).buscarPorId(id);
    }

    @Test
    public void deveCadastrarUsuario() throws Exception {
        //Arrange
        UsuarioRequest request = new UsuarioRequest("Ana", "ana@email.com", "hash1", "USER");
        UsuarioResponse usuario = new UsuarioResponse(1L, "Ana", "ana@email.com");
        ArgumentCaptor<UsuarioRequest> captor = ArgumentCaptor.forClass(UsuarioRequest.class);

        when(usuarioService.cadastrar(any(UsuarioRequest.class))).thenReturn(usuario);

        //Act + Assert
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)
                        )
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "/api/usuarios/1"
                ));

        verify(usuarioService).cadastrar(captor.capture());

        UsuarioRequest capturado = captor.getValue();

        assertEquals("Ana", capturado.nome());
        assertEquals("ana@email.com", capturado.email());
        assertEquals("hash1", capturado.senha());
        assertEquals("USER", capturado.role());
    }

    @Test
    public void deveAtualizar() throws Exception{
        //Arrange
        Long id = 1L;
        UsuarioRequest request = new UsuarioRequest("Ana", "ana@email.com", "hash1", "USER");
        UsuarioResponse usuario = new UsuarioResponse(id, "Ana", "ana@email.com");
        ArgumentCaptor<UsuarioRequest> captor = ArgumentCaptor.forClass(UsuarioRequest.class);

        when(usuarioService.atualizar(eq(id), any(UsuarioRequest.class))).thenReturn(usuario);

        //Act + Assert
        mockMvc.perform(put("/api/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)
                        )
                )
                .andExpect(status().isOk());

        verify(usuarioService).atualizar(eq(id), captor.capture());

        UsuarioRequest capturado = captor.getValue();

        assertEquals("Ana", capturado.nome());
        assertEquals("ana@email.com", capturado.email());
        assertEquals("hash1", capturado.senha());
        assertEquals("USER", capturado.role());
    }

    @Test
    public void deveDeletar() throws Exception{
        //Arrange
        Long id = 1L;

        //Act + Assert
        mockMvc.perform(delete("/api/usuarios/{id}", id))
                .andExpect(status().isNoContent());

        verify(usuarioService).deletar(id);
    }

}

