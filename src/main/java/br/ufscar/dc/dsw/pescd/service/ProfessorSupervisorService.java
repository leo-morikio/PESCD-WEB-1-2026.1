package br.ufscar.dc.dsw.pescd.service;

import br.ufscar.dc.dsw.pescd.model.InscricaoOferta;
import br.ufscar.dc.dsw.pescd.model.LogStatus;
import br.ufscar.dc.dsw.pescd.model.Usuario;
import br.ufscar.dc.dsw.pescd.model.enums.StatusAluno;
import br.ufscar.dc.dsw.pescd.repository.InscricaoOfertaRepository;
import br.ufscar.dc.dsw.pescd.repository.LogStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorSupervisorService {

    private final InscricaoOfertaRepository inscricaoRepository;
    private final LogStatusRepository logStatusRepository;

    public ProfessorSupervisorService(InscricaoOfertaRepository inscricaoRepository,
                                      LogStatusRepository logStatusRepository) {
        this.inscricaoRepository = inscricaoRepository;
        this.logStatusRepository = logStatusRepository;
    }

    // PS.01 - lista as inscrições que o professor supervisiona
    public List<InscricaoOferta> listarInscricoes(Usuario professor) {
        return inscricaoRepository.findByProfessorSupervisor(professor);
    }

    // PS.02 - aprova o plano de trabalho
    public InscricaoOferta aprovarPlano(Long inscricaoId, Usuario professor) {
        InscricaoOferta inscricao = inscricaoRepository.findById(inscricaoId)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));

        if (inscricao.getStatus() != StatusAluno.PLANO_ENVIADO) {
            throw new RuntimeException("Só é possível aprovar um plano que foi enviado.");
        }

        String statusAnterior = inscricao.getStatus().name();
        inscricao.setStatus(StatusAluno.PLANO_APROVADO);
        inscricaoRepository.save(inscricao);

        registrarLog(inscricao, statusAnterior, StatusAluno.PLANO_APROVADO.name(), professor);
        return inscricao;
    }

    // PS.03 - aprova o relatório final
    public InscricaoOferta aprovarRelatorio(Long inscricaoId, Usuario professor) {
        InscricaoOferta inscricao = inscricaoRepository.findById(inscricaoId)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));

        if (inscricao.getStatus() != StatusAluno.RELATORIO_ENVIADO) {
            throw new RuntimeException("Só é possível aprovar um relatório que foi enviado.");
        }

        String statusAnterior = inscricao.getStatus().name();
        inscricao.setStatus(StatusAluno.RELATORIO_APROVADO_SUPERVISOR);
        inscricaoRepository.save(inscricao);

        registrarLog(inscricao, statusAnterior, StatusAluno.RELATORIO_APROVADO_SUPERVISOR.name(), professor);
        return inscricao;
    }

    // método privado reutilizado pelos dois - evita repetição
    private void registrarLog(InscricaoOferta inscricao, String anterior, String novo, Usuario professor) {
        logStatusRepository.save(new LogStatus(inscricao, anterior, novo, professor));
    }
}