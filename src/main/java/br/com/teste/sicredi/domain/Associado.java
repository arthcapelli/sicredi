package br.com.teste.sicredi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Associado {

    private Integer id;

    private String cpf;
}
