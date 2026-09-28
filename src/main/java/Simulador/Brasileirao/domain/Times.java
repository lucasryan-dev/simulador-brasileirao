package Simulador.Brasileirao.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "Times")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Times {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private  String name;
    private  String sigla;
    private  String estado;
    private  double ataque;
    private  double defesa;
    private double forma;
    private int pontos;
    private int jogos;
    private int vitorias;
    private int empates;
    private int derrotas;
    private int golsMarcados;
    private int golsSofridos;
}
