package br.com.parquimetro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    @NotNull(message = "o campo valor não pode ser nulo")
    @Positive(message = "o campo valor nao pode ser zero ou menor que zero")
    @Digits(integer = 7, fraction = 2, message = "o campo valor não pode ter mais que 2 casas decimais e não pode ter mais que 7 dígitos")
    private Double valor;

    @Valid
    @NotNull(message = "o objeto metodo de pagamento não pode ser nulo")
    private MetodoPagamentoDto metodoPagamento;

}
