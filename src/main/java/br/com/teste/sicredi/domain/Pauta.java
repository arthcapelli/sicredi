package br.com.teste.sicredi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "db_pauta")
@SequenceGenerator(name = "seq_db_pauta", sequenceName = "seq_db_pauta", allocationSize = 1)
public class Pauta {

    @Id
    @GeneratedValue(generator = "seq_db_pauta")
    @Column(name = "id", nullable = false)
    private Integer id;

    private String titulo;

    private Integer limiteVotos;

    private LocalDateTime dataCriacao;

}
