package br.ufscar.dc.dsw.pescd.controller;

import br.ufscar.dc.dsw.pescd.config.UsuarioLogadoUtil;
import br.ufscar.dc.dsw.pescd.model.InscricaoOferta;
import br.ufscar.dc.dsw.pescd.model.Usuario;
import br.ufscar.dc.dsw.pescd.repository.UsuarioRepository;
import br.ufscar.dc.dsw.pescd.service.ProfessorSupervisorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professor/supervisor")
public class ProfessorSupervisorController {

    private final ProfessorSupervisorService supervisorService;
    private final UsuarioRepository usuarioRepository;

    public ProfessorSupervisorController(ProfessorSupervisorService supervisorService,
                                         UsuarioRepository usuarioRepository) {
        this.supervisorService = supervisorService;
        this.usuarioRepository = usuarioRepository;
    }

    // PS.01
    @GetMapping("/inscricoes")
    public ResponseEntity<List<InscricaoOferta>> listarInscricoes() {
        Usuario professor = UsuarioLogadoUtil.getUsuarioLogado(usuarioRepository);
        return ResponseEntity.ok(supervisorService.listarInscricoes(professor));
    }

    // PS.02
    @PutMapping("/aprovar-plano/{inscricaoId}")
    public ResponseEntity<InscricaoOferta> aprovarPlano(@PathVariable Long inscricaoId) {
        Usuario professor = UsuarioLogadoUtil.getUsuarioLogado(usuarioRepository);
        return ResponseEntity.ok(supervisorService.aprovarPlano(inscricaoId, professor));
    }

    // PS.03
    @PutMapping("/aprovar-relatorio/{inscricaoId}")
    public ResponseEntity<InscricaoOferta> aprovarRelatorio(@PathVariable Long inscricaoId) {
        Usuario professor = UsuarioLogadoUtil.getUsuarioLogado(usuarioRepository);
        return ResponseEntity.ok(supervisorService.aprovarRelatorio(inscricaoId, professor));
    }
}