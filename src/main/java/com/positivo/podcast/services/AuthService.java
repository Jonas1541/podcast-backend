package com.positivo.podcast.services;

import com.positivo.podcast.dtos.request.LoginRequestDto;
import com.positivo.podcast.dtos.request.RegisterRequestDto;
import com.positivo.podcast.dtos.response.AuthResponseDto;
import com.positivo.podcast.dtos.response.UsuarioResponseDto;
import com.positivo.podcast.entities.Usuario;
import com.positivo.podcast.exceptions.EmailAlreadyExistsException;
import com.positivo.podcast.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponseDto login(LoginRequestDto loginRequestDto) { // 1. Mudar o tipo de retorno
        var usernamePassword = new UsernamePasswordAuthenticationToken(
            loginRequestDto.email(),
            loginRequestDto.senha()
        );
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 2. Pegar o objeto Usuario completo
        var usuario = (Usuario) auth.getPrincipal();

        // 3. Gerar o token
        var token = tokenService.generateToken(usuario);

        // 4. Criar o DTO de resposta do usuário
        var usuarioDto = new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole());

        // 5. Retornar o DTO de autenticação completo
        return new AuthResponseDto(token, usuarioDto);
    }

    public void register(RegisterRequestDto registerRequestDto) {
        // Verifica se o usuário já existe
        if (usuarioRepository.findByEmail(registerRequestDto.email()).isPresent()) {
            throw new EmailAlreadyExistsException("O email '" + registerRequestDto.email() + "' já está em uso.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(registerRequestDto.nome());
        novoUsuario.setEmail(registerRequestDto.email());
        novoUsuario.setSenha(passwordEncoder.encode(registerRequestDto.senha()));
        novoUsuario.setRole("USER"); // Define a role padrão para novos usuários

        usuarioRepository.save(novoUsuario);
    }
}
