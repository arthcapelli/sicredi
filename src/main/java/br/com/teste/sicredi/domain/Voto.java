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

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "db_voto")
@SequenceGenerator(name = "seq_db_voto", sequenceName = "seq_db_voto", allocationSize = 1)
public class Voto {

    @Id
    @GeneratedValue(generator = "seq_db_voto")
    @Column(name = "id", nullable = false)
    private Integer id;

    private Integer idAssociado;

    private Integer idPauta;

    private String valorVoto;
}
