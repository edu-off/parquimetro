package br.com.parquimetro.dto;

import br.com.parquimetro.validation.group.DadosCartaoNotRequiredGroup;
import br.com.parquimetro.validation.group.DadosCartaoRequiredGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DadosCartaoDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @NotNull(message = "o campo numero não pode ser nulo", groups = {DadosCartaoRequiredGroup.class, DadosCartaoNotRequiredGroup.class})
    @Positive(message = "o campo numero nao pode ser zero ou menor que zero", groups = {DadosCartaoRequiredGroup.class, DadosCartaoNotRequiredGroup.class})
    @Digits(integer = 16, fraction = 0, message = "o campo numero não pode ter casas decimais e não pode ter mais que 16 dígitos", groups = {DadosCartaoRequiredGroup.class, DadosCartaoNotRequiredGroup.class})
    private Long numero;

    @NotNull(message = "o campo nome completo não pode ser nulo", groups = {DadosCartaoRequiredGroup.class, DadosCartaoNotRequiredGroup.class})
    @NotEmpty(message = "o campo nome completo não pode ser vazio", groups = {DadosCartaoRequiredGroup.class, DadosCartaoNotRequiredGroup.class})
    private String nomeCompleto;

    @Future(message = "o campo data de vencimento deve ser uma data futura", groups = {DadosCartaoRequiredGroup.class, DadosCartaoNotRequiredGroup.class})
    private LocalDate dataVencimento;

    @NotNull(message = "o campo codigo seguranca não pode ser nulo", groups = {DadosCartaoRequiredGroup.class, DadosCartaoNotRequiredGroup.class})
    @Positive(message = "o campo codigo seguranca nao pode ser zero ou menor que zero", groups = {DadosCartaoRequiredGroup.class, DadosCartaoNotRequiredGroup.class})
    @Digits(integer = 16, fraction = 0, message = "o campo codigo seguranca não pode ter casas decimais e não pode ter mais que 3 dígitos", groups = {DadosCartaoRequiredGroup.class, DadosCartaoNotRequiredGroup.class})
    private Integer codigoSeguranca;

}
