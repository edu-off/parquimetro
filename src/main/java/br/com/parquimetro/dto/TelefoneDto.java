package br.com.parquimetro.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneDto {

    private Integer ddd;
    private Long telefone;

}
