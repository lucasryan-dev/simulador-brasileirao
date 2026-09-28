package Simulador.Brasileirao.services;

import Simulador.Brasileirao.domain.Times;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassificacaoService {
    private final TimesService timesService;

    @Transactional(readOnly = true)
    public List<Times> recalcular(){
        List<Times> times = timesService.listarTodos();

        return times.stream()
                .sorted(
                Comparator.comparingInt(Times::getPontos).reversed()
                .thenComparing(Comparator.comparingInt(Times::getVitorias).reversed())
                .thenComparing(Comparator.comparingInt(this::getSaldoGols).reversed())
                .thenComparing(Comparator.comparingInt(Times::getGolsMarcados).reversed())
                )
                .toList();
    }

    private int getSaldoGols (Times times){
        return times.getGolsMarcados() - times.getGolsSofridos();
    }
}
