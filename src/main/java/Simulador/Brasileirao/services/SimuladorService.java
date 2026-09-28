package Simulador.Brasileirao.services;

import Simulador.Brasileirao.domain.Confronto;
import Simulador.Brasileirao.domain.Times;
import Simulador.Brasileirao.exception.PlacarInvalidoException;
import Simulador.Brasileirao.repository.ConfrontoRepository;
import Simulador.Brasileirao.repository.TimesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimuladorService {
    private final TimesService timesService;
    private final ClassificacaoService classificacaoService;
    private final CalendarioService calendarioService;
    private final TimesRepository timesRepository;
    private final ConfrontoRepository confrontoRepository;

    @Transactional
    public void definirPlacarManual (String siglaMandante, String siglaVisitante, int golsMandante, int golsVisitante) {

        validarPlacar(siglaMandante, siglaVisitante, golsMandante, golsVisitante);

        Times mandante = timesService.buscaPorSigla(siglaMandante);
        Times visitante = timesService.buscaPorSigla(siglaVisitante);

        atualizarEstatisticas(mandante, golsMandante, golsVisitante);
        atualizarEstatisticas(visitante, golsVisitante, golsMandante);

        timesRepository.save(mandante);
        timesRepository.save(visitante);
    }


    private void atualizarEstatisticas(Times time, int golsPro, int golsContra){
        time.setJogos(time.getJogos() + 1);
        time.setGolsMarcados(time.getGolsMarcados() + golsPro);
        time.setGolsSofridos(time.getGolsSofridos() + golsContra);

        if (golsPro > golsContra){
            time.setVitorias(time.getVitorias() + 1);
            time.setPontos(time.getPontos() + 3);
        } else if (golsPro == golsContra) {
            time.setEmpates(time.getEmpates() + 1);
            time.setPontos(time.getEmpates() + 1);
        } else {
            time.setDerrotas(time.getDerrotas() + 1);
        }
    }

    public void simularConfronto (Confronto confronto){
        if (confronto.isSimulado()) {
            return;
        }

        Times timesMandante = confronto.getMandante();
        Times timesVisitante = confronto.getVisitante();

        int golsMandante = confronto.getGolsMandante();
        int golsVisitante = confronto.getGolsVisitante();

        confronto.setGolsMandante(confronto.getGolsMandante());
        confronto.setGolsVisitante(confronto.getGolsVisitante());
        confronto.setSimulado(true);

        atualizarEstatisticas (timesMandante, golsMandante, golsVisitante);
        atualizarEstatisticas (timesVisitante, golsVisitante, golsMandante);
    }

    @Transactional
    public void definirPlacarConfronto(Long confrontoId, int golsMandante, int golsVisitante) {
        if (golsMandante < 0 || golsVisitante < 0) {
            throw new PlacarInvalidoException("Gols não podem ser negativos");
        }

        Confronto confronto = calendarioService.buscarPorId(confrontoId);

        if (confronto.isSimulado()) {
            throw new PlacarInvalidoException("Esse confronto já foi definido");
        }

        confronto.setGolsMandante(golsMandante);
        confronto.setGolsVisitante(golsVisitante);
        confronto.setSimulado(true);

        atualizarEstatisticas(confronto.getMandante(), golsMandante, golsVisitante);
        atualizarEstatisticas(confronto.getVisitante(), golsVisitante, golsMandante);
    }


    public void simularRodada(int numeroRodada){
        List<Confronto> confrontosDaRodada = calendarioService.listarPorRodada(numeroRodada);

        for (Confronto confronto : confrontosDaRodada) {
            simularConfronto(confronto);
        }
    }

    @Transactional
    public void resetarSimulacao() {
        timesService.resetarEstatisticas();
        calendarioService.resetarConfrontos();
    }

    private void validarPlacar(String siglaMandante, String siglaVisitante,
                               int golsMandante, int golsVisitante) {
        if (golsMandante < 0 || golsVisitante < 0) {
            throw new PlacarInvalidoException("Gols não podem ser negativos");
        }
        if (siglaMandante.equalsIgnoreCase(siglaVisitante)) {
            throw new PlacarInvalidoException("Um time não pode jogar contra si mesmo");
        }
    }
}
