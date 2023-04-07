package br.com.teste.sicredi.representation.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AbrirSessaoRequest {

    private Integer idPauta;

    private LocalDateTime dataLimite;
}
