package br.com.parquimetro.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Telefone {

    private Integer ddd;
    private Long telefone;

}
