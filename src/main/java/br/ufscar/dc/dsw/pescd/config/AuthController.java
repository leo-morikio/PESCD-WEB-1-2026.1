package br.ufscar.dc.dsw.pescd.controller;

import br.ufscar.dc.dsw.pescd.config.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> dados) {
        String nomeUsuario = dados.get("nomeUsuario");
        String senha = dados.get("senha");

        // Verifica usuário e senha usando o Spring Security
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(nomeUsuario, senha)
        );

        // Se passou, gera o token
        String token = jwtService.gerarToken(nomeUsuario);

        return ResponseEntity.ok(Map.of("token", token));
    }
}