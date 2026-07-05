package br.ufscar.dc.dsw.pescd.config;

import br.ufscar.dc.dsw.pescd.repository.UsuarioRepository;
import br.ufscar.dc.dsw.pescd.model.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Se nao tem cabeçalho ou não começa com Bearer , vai seguir sem autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrai o token
        String token = authHeader.substring(7);
        String nomeUsuario = jwtService.extrairNomeUsuario(token);

        // Se achou o usuário e ainda não ta autenticado nesta requisição
        if (nomeUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Usuario usuario = usuarioRepository.findByNomeUsuario(nomeUsuario);

            if (usuario != null && jwtService.tokenValido(token, nomeUsuario)) {
                var authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name())
                );

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(nomeUsuario, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}