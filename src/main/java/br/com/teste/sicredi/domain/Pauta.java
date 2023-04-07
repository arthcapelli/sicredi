package br.com.teste.sicredi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Pauta {

    private Integer id;

    private String titulo;

    private Integer limiteVotos;

    private LocalDateTime dataCriacao;

    private List<Voto> votos;

}
