package Simulador.Brasileirao.controller;

import Simulador.Brasileirao.domain.Confronto;
import Simulador.Brasileirao.domain.Times;
import Simulador.Brasileirao.services.CalendarioService;
import Simulador.Brasileirao.services.ClassificacaoService;
import Simulador.Brasileirao.services.SimuladorService;
import Simulador.Brasileirao.services.TimesService;
import Simulador.Brasileirao.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("Simulacoes")
@Log4j2
@RequiredArgsConstructor
public class FutebolController {
    private final DateUtil dateUtil;
    private final SimuladorService simuladorService;
    private final TimesService timesService;
    private final ClassificacaoService classificacaoService;
    private final CalendarioService calendarioService;


    @GetMapping("/times")
    public ResponseEntity<List<Times>> List(){
        log.info(dateUtil.FormatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(timesService.listarTodos(), HttpStatus.OK);

    }

    @GetMapping("/classificacao")
    public ResponseEntity <List<Times>> verClassificacao(){
    return new ResponseEntity<>(classificacaoService.recalcular(), HttpStatus.OK);
    }

    @PostMapping("/definir-placar")
    public List<Times> definirPlacar (@RequestParam String mandante,
                                                                    @RequestParam String visitante,
                                                                    @RequestParam int golsMandante,
                                                                    @RequestParam int golsVisitante, TimesService timesService){
        simuladorService.definirPlacarManual(mandante, visitante,golsMandante, golsVisitante);
        return timesService.listarTodos();
    }

    @PostMapping("/rodada/{numero}")
    public ResponseEntity<List<Times>> simularRodada(@PathVariable int numero){
        simuladorService.simularRodada(numero);
        return new ResponseEntity<>(classificacaoService.recalcular(), HttpStatus.OK);
    }

    @GetMapping("/calendario")
    public ResponseEntity<List<Confronto>> ListarCalendar(){
        return new ResponseEntity<>(calendarioService.listarTodos(), HttpStatus.OK);
    }


    @GetMapping("/calendario/rodada/{numero}")
    public ResponseEntity<List<Confronto>> verRodada(@PathVariable int numero) {
        return new ResponseEntity<>(calendarioService.listarPorRodada(numero), HttpStatus.OK);
    }

    @PatchMapping("/confronto/{id}/placar")
    public ResponseEntity<List<Times>> definirPlacarConfronto(@PathVariable Long id,
                                                              @RequestParam int golsMandante,
                                                              @RequestParam int golsVisitante) {
        simuladorService.definirPlacarConfronto(id, golsMandante, golsVisitante);
        return new ResponseEntity<>(classificacaoService.recalcular(), HttpStatus.OK);
    }

    @PostMapping("/resetar")
    public ResponseEntity<String> resetar() {
        simuladorService.resetarSimulacao();
        return new ResponseEntity<>("Simulação resetada com sucesso", HttpStatus.OK);
    }

    @PostMapping("/calendario/gerar")
    public ResponseEntity<String> gerarCalendario() {
        calendarioService.gerarCalendar();
        return new ResponseEntity<>("Calendário gerado com sucesso", HttpStatus.CREATED);
    }

}
