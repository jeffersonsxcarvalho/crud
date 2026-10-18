package com.exercises.crud.service;

import com.exercises.crud.dto.UsuarioRequest;
import com.exercises.crud.dto.UsuarioResponse;
import com.exercises.crud.entity.Usuario;
import com.exercises.crud.exception.EmailJaCadastradoException;
import com.exercises.crud.exception.UsuarioNaoEncontradoException;
import com.exercises.crud.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;


    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponse> listarTodos() {
        return repository.findAll().stream().map(UsuarioResponse::de).toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException("Usuário não encontrado.")
                );

        return UsuarioResponse.de(usuario);
    }

    @Transactional
    public UsuarioResponse cadastrar(UsuarioRequest usuarioRequest) {

        if(repository.findByEmail(usuarioRequest.email()).isPresent()) {
            throw new EmailJaCadastradoException("Email já cadastrado.");
        }

        String senhaHash = passwordEncoder.encode(usuarioRequest.senha());

        Usuario usuario = new Usuario(usuarioRequest.nome(), usuarioRequest.email(), senhaHash, usuarioRequest.role());

        return UsuarioResponse.de(repository.save(usuario));
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest usuarioRequest) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException("Usuário não encontrado.")
                );

        if(repository.findByEmail(usuarioRequest.email()).isPresent()
                && !usuario.getEmail().equals(usuarioRequest.email())){
            throw new EmailJaCadastradoException("Email já cadastrado.");
        }

        usuario.setNome(usuarioRequest.nome());
        usuario.setEmail(usuarioRequest.email());
        usuario.setRole(usuarioRequest.role());

        String senhaHash = passwordEncoder.encode(usuarioRequest.senha());
        usuario.setSenha(senhaHash);

        return UsuarioResponse.de(repository.save(usuario));
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = repository.findById(id)
                        .orElseThrow(() ->
                                new UsuarioNaoEncontradoException("Usuário não encontrado.")
                        );

        repository.delete(usuario);

    }
}
