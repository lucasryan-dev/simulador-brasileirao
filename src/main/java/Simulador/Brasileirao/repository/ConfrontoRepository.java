package Simulador.Brasileirao.repository;

import Simulador.Brasileirao.domain.Confronto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfrontoRepository extends JpaRepository<Confronto, Long> {
    List<Confronto> findByRodada(int rodada);
}
