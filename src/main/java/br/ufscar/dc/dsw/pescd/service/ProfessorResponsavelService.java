package br.ufscar.dc.dsw.pescd.service;

import br.ufscar.dc.dsw.pescd.model.InscricaoOferta;
import br.ufscar.dc.dsw.pescd.model.LogStatus;
import br.ufscar.dc.dsw.pescd.model.Usuario;
import br.ufscar.dc.dsw.pescd.model.enums.StatusAluno;
import br.ufscar.dc.dsw.pescd.repository.InscricaoOfertaRepository;
import br.ufscar.dc.dsw.pescd.repository.LogStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfessorResponsavelService {

    private final InscricaoOfertaRepository inscricaoRepository;
    private final LogStatusRepository logStatusRepository;

    public ProfessorResponsavelService(InscricaoOfertaRepository inscricaoRepository,
                                       LogStatusRepository logStatusRepository) {
        this.inscricaoRepository = inscricaoRepository;
        this.logStatusRepository = logStatusRepository;
    }

    // PR.01 - conclui o relatório do aluno
    public InscricaoOferta concluirRelatorio(Long inscricaoId, Usuario professor) {
        InscricaoOferta inscricao = inscricaoRepository.findById(inscricaoId)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));

        if (inscricao.getStatus() != StatusAluno.RELATORIO_APROVADO_SUPERVISOR) {
            throw new RuntimeException("O relatório precisa estar aprovado pelo supervisor antes de concluir.");
        }

        String statusAnterior = inscricao.getStatus().name();
        inscricao.setStatus(StatusAluno.CONCLUIDO_RESPONSAVEL);
        inscricaoRepository.save(inscricao);

        logStatusRepository.save(new LogStatus(inscricao, statusAnterior,
                StatusAluno.CONCLUIDO_RESPONSAVEL.name(), professor));

        return inscricao;
    }
}