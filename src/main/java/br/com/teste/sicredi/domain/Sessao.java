package br.com.teste.sicredi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "db_sessao")
@SequenceGenerator(name = "seq_db_sessao", sequenceName = "seq_db_sessao", allocationSize = 1)
public class Sessao {

    @Id
    @GeneratedValue(generator = "seq_db_sessao")
    @Column(name = "id", nullable = false)
    private Integer id;

    private Integer idPauta;

    private LocalDateTime dataLimite;
}
