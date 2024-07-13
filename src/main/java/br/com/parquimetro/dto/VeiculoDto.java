package br.com.parquimetro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    @NotNull(message = "o campo placa não pode ser nulo")
    @NotEmpty(message = "o campo placa não pode ser vazio")
    private String placa;

    @NotNull(message = "o campo modelo não pode ser nulo")
    @NotEmpty(message = "o campo modelo não pode ser vazio")
    private String modelo;

    @NotNull(message = "o campo ano não pode ser nulo")
    @Positive(message = "o campo ano nao pode ser zero ou menor que zero")
    @Digits(integer = 4, fraction = 0, message = "o campo ano não pode ter casas decimais e não pode ter mais que 4 dígitos")
    private Integer ano;

    @NotNull(message = "o campo cor não pode ser nulo")
    @NotEmpty(message = "o campo cor não pode ser vazio")
    private String cor;

    @NotNull(message = "o campo fabricante não pode ser nulo")
    @NotEmpty(message = "o campo fabricante não pode ser vazio")
    private String fabricante;

}
