package br.com.teste.sicredi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "db_associado")
@SequenceGenerator(name = "seq_db_associado", sequenceName = "seq_db_associado", allocationSize = 1)
public class Associado {

    @Id
    @GeneratedValue(generator = "seq_db_associado")
    @Column(name = "id", nullable = false)
    private Integer id;

    private String cpf;
}
