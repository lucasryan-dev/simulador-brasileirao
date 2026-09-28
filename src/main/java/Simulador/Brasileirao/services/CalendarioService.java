package Simulador.Brasileirao.services;

import Simulador.Brasileirao.domain.Confronto;
import Simulador.Brasileirao.domain.Times;
import Simulador.Brasileirao.exception.ConfrontoNaoEncontradoException;
import Simulador.Brasileirao.repository.ConfrontoRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CalendarioService {
    private final TimesService timesService;
    private final ConfrontoRepository confrontoRepository;

    @PostConstruct
    public void inicializar(){
        if (confrontoRepository.count() == 0) {
            gerarCalendar();
        }
    }

    @Transactional
    public List<Confronto> gerarCalendar(){

        if (confrontoRepository.count() > 0){
            throw new IllegalStateException("O calendário já foi gerado");
        }
        List<Times> times = new ArrayList<>(timesService.listarTodos());
        List<Confronto> confrontos = new ArrayList<>();

        if (times.size() % 2 != 0) {
            times.add(null);
        }


        int n = times.size();
        int totalRodadasTurno = n - 1;
        int jogosPorRodada = n / 2;

        List<Times> rotacao = new ArrayList<>(times);

        for (int rodada = 1; rodada <= totalRodadasTurno; rodada++){
            for (int i = 0; i < jogosPorRodada ; i++) {
                Times mandante = rotacao.get(i);
                Times visitante = rotacao.get(n - 1 - i);

                if (mandante != null && visitante != null){
                    if (rodada % 2 == 0){
                        confrontos.add(new Confronto(rodada, visitante, mandante, null,null, false));
                    } else {
                        confrontos.add(new Confronto(rodada, mandante, visitante, null, null, false));
                    }
                }
            }
            rotacionar(rotacao);
        }

        int totalRodadas = totalRodadasTurno;
        List<Confronto> turno = new ArrayList<>(confrontos);
        for(Confronto c : turno){
            confrontos.add(new Confronto(c.getRodada() + totalRodadas,
                                                c.getVisitante(),
                                                c.getMandante(),
                                    null, null, false));
        }

        return confrontoRepository.saveAll(confrontos);
    }

    private void rotacionar(List<Times> lista) {
        Times ultimo = lista.remove(lista.size() - 1);
        lista.add(1, ultimo);
    }

    public List<Confronto> listarPorRodada(int rodada){
        return confrontoRepository.findByRodada(rodada);
    }

    public List<Confronto> listarTodos(){
        return confrontoRepository.findAll(Sort.by("id"));
    }

    public Confronto buscarPorId(Long id) {
        return confrontoRepository.findById(id)
                .orElseThrow(() -> new ConfrontoNaoEncontradoException(id));
    }

    @Transactional
    public void resetarConfrontos() {
        List<Confronto> confrontos = confrontoRepository.findAll();
        for (Confronto confronto : confrontos) {
            confronto.setGolsMandante(null);
            confronto.setGolsVisitante(null);
            confronto.setSimulado(false);
        }
        confrontoRepository.saveAll(confrontos);
    }
}
