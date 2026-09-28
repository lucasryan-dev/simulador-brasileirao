package Simulador.Brasileirao.repository;

import Simulador.Brasileirao.domain.Times;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimesRepository extends JpaRepository<Times, Long> {
    Optional<Times> findBySigla(String sigla);

    String sigla(String sigla);
}
