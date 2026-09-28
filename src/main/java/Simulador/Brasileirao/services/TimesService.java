package Simulador.Brasileirao.services;

import Simulador.Brasileirao.domain.Times;
import Simulador.Brasileirao.exception.TimeNaoEncontradoException;
import Simulador.Brasileirao.repository.TimesRepository;
import Simulador.Brasileirao.repository.TimesRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimesService {
    private final TimesRepository timeRepository;

    @PostConstruct
    public void inicializar() {
        if (timeRepository.count() == 0) {
            timeRepository.saveAll(List.of(
                    new Times(null, "Palmeiras", "PAL", "SP", 72, 66, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Flamengo", "FLA", "RJ", 80, 66, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Athletico-PR", "CAP", "PR", 59, 60, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Fluminense", "FLU", "RJ", 62, 49, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Bahia", "BAH", "BA", 59, 52, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Cruzeiro", "CRU", "MG", 56, 42, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Coritiba", "CBA", "PR", 53, 47, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Atletico-MG", "CAM", "MG", 53, 53, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "RB Bragantino", "RBB", "SP", 48, 58, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Corinthians", "COR", "SP", 42, 60, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Sao Paulo", "SAO", "SP", 48, 53, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Botafogo", "BOT", "RJ", 62, 33, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Vitoria", "VIT", "BA", 38, 41, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Santos", "SAN", "SP", 57, 40, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Gremio", "GRE", "RS", 45, 47, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Internacional", "INT", "RS", 42, 50, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Mirassol", "MIR", "SP", 43, 38, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Vasco", "VAS", "RJ", 45, 35, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Remo", "REM", "PA", 48, 33, 0.0, 0, 0, 0, 0, 0, 0, 0),
                    new Times(null, "Chapecoense", "CHA", "SC", 42, 18, 0.0, 0, 0, 0, 0, 0, 0, 0)
            ));
        }
    }

    public List<Times> listarTodos() {
        return timeRepository.findAll();
    }

    public Times buscaPorSigla(String sigla) {
        return timeRepository.findBySigla(sigla)
                .orElseThrow(() -> new TimeNaoEncontradoException(sigla));
    }

    @Transactional
    public void resetarEstatisticas() {
        List<Times> times = timeRepository.findAll();
        for (Times time : times) {
            time.setJogos(0);
            time.setVitorias(0);
            time.setEmpates(0);
            time.setDerrotas(0);
            time.setGolsMarcados(0);
            time.setGolsSofridos(0);
            time.setPontos(0);
        }
        timeRepository.saveAll(times);
    }
}

