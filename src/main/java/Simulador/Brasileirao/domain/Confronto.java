package Simulador.Brasileirao.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Confronto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Confronto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rodada;

    @ManyToOne
    @JoinColumn (name = "mandante_id")
    private Times mandante;

    @ManyToOne
    @JoinColumn (name = "visitante_id")
    private Times visitante;


    private Integer golsMandante;
    private Integer golsVisitante;
    private boolean simulado;


    public Confronto(int rodada, Times mandante, Times visitante, Integer golsMandante, Integer golsVisitante, boolean simulado) {
        this.rodada = rodada;
        this.mandante = mandante;
        this.visitante = visitante;
        this.golsMandante = (Integer) golsMandante;
        this.golsVisitante = (Integer) golsVisitante;
        this.simulado = simulado;
    }
}
