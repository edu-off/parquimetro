package br.com.parquimetro.dto;

import br.com.parquimetro.validation.annotation.DadosCartaoNotRequired;
import br.com.parquimetro.validation.annotation.DadosCartaoRequired;
import br.com.parquimetro.validation.annotation.MetodoPagamentoInvalid;
import br.com.parquimetro.validation.group.DadosCartaoNotRequiredGroup;
import br.com.parquimetro.validation.group.DadosCartaoRequiredGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPagamentoDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    @NotNull(message = "o campo metodo não pode ser nulo", groups = {DadosCartaoRequiredGroup.class, DadosCartaoNotRequiredGroup.class})
    @NotEmpty(message = "o campo metodo não pode ser vazio", groups = {DadosCartaoRequiredGroup.class, DadosCartaoNotRequiredGroup.class})
    @MetodoPagamentoInvalid(message = "método de pagamento inválido", groups = {DadosCartaoRequiredGroup.class, DadosCartaoNotRequiredGroup.class})
    private String metodo;

    @DadosCartaoRequired(message = "o objeto dados cartão não foi informado", groups = DadosCartaoRequiredGroup.class)
    @DadosCartaoNotRequired(message = "o objeto dados cartão é inválido neste contexto", groups = DadosCartaoNotRequiredGroup.class)
    private DadosCartaoDto dadosCartao;

}
