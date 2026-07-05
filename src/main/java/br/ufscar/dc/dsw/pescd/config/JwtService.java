package br.ufscar.dc.dsw.pescd.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // Chave secreta usada para assinar os tokens
    private static final String SECRET = "minhaChaveSecretaSuperSeguraParaAssinarTokensJWT1234567890";
    private static final long EXPIRACAO = 1000 * 60 * 60 * 10; // 10 horas em ms

    private SecretKey getChave() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Gera um token a partir do nome de usuaario
    public String gerarToken(String nomeUsuario) {
        return Jwts.builder()
                .subject(nomeUsuario)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRACAO))
                .signWith(getChave())
                .compact();
    }

    // Extrai o nome de usuário de dento do token
    public String extrairNomeUsuario(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    // Verifica se o token é valido
    public boolean tokenValido(String token, String nomeUsuario) {
        String usuarioDoToken = extrairNomeUsuario(token);
        return usuarioDoToken.equals(nomeUsuario) && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        Date expiracao = extrairClaim(token, Claims::getExpiration);
        return expiracao.before(new Date());
    }

    private <T> T extrairClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getChave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }
}