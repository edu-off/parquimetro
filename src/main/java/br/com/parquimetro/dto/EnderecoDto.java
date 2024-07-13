package br.com.parquimetro.dto;

import br.com.parquimetro.validation.annotation.UfNotExists;
import br.com.parquimetro.validation.group.AdicaoGroup;
import br.com.parquimetro.validation.group.AtualizacaoGroup;
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
public class EnderecoDto {

    @NotNull(message = "o campo logradouro não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @NotEmpty(message = "o campo logradouro não pode ser vazio", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    private String logradouro;

    @NotNull(message = "o campo bairro não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @NotEmpty(message = "o campo bairro não pode ser vazio", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    private String bairro;

    @NotNull(message = "o campo cidade não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @NotEmpty(message = "o campo cidade não pode ser vazio", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    private String cidade;

    @NotNull(message = "o campo uf não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @NotEmpty(message = "o campo uf não pode ser vazio", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @UfNotExists(message = "a uf preenchida não existe", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    private String uf;

    @NotNull(message = "o campo cep não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @Positive(message = "o campo cep nao pode ser zero ou menor que zero", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @Digits(integer = 8, fraction = 0, message = "o campo cep não pode ter casas decimais e não pode ter mais que 8 dígitos", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    private Long cep;

}
