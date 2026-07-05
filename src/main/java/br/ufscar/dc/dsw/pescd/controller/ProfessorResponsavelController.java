package br.ufscar.dc.dsw.pescd.controller;

import br.ufscar.dc.dsw.pescd.config.UsuarioLogadoUtil;
import br.ufscar.dc.dsw.pescd.model.InscricaoOferta;
import br.ufscar.dc.dsw.pescd.model.Usuario;
import br.ufscar.dc.dsw.pescd.repository.UsuarioRepository;
import br.ufscar.dc.dsw.pescd.service.ProfessorResponsavelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professor/responsavel")
public class ProfessorResponsavelController {

    private final ProfessorResponsavelService responsavelService;
    private final UsuarioRepository usuarioRepository;

    public ProfessorResponsavelController(ProfessorResponsavelService responsavelService,
                                          UsuarioRepository usuarioRepository) {
        this.responsavelService = responsavelService;
        this.usuarioRepository = usuarioRepository;
    }

    // PR.01
    @PutMapping("/concluir-relatorio/{inscricaoId}")
    public ResponseEntity<InscricaoOferta> concluirRelatorio(@PathVariable Long inscricaoId) {
        Usuario professor = UsuarioLogadoUtil.getUsuarioLogado(usuarioRepository);
        return ResponseEntity.ok(responsavelService.concluirRelatorio(inscricaoId, professor));
    }
}