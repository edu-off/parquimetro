package br.com.parquimetro.dto;

import br.com.parquimetro.validation.annotation.CondutorExists;
import br.com.parquimetro.validation.annotation.CondutorNotExists;
import br.com.parquimetro.validation.group.AdicaoGroup;
import br.com.parquimetro.validation.group.AtualizacaoGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CondutorDto {

    @NotNull(message = "o campo cpf não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @NotEmpty(message = "o campo cpf não pode ser vazio", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @CPF(message = "o campo cpf deve possuir uma estrutura válida de CPF", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @CondutorExists(message = "o condutor já existe", groups = AdicaoGroup.class)
    @CondutorNotExists(message = "o condutor não existe", groups = AtualizacaoGroup.class)
    private String cpf;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    @NotNull(message = "o campo nome não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @NotEmpty(message = "o campo nome não pode ser vazio", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    private String nome;

    @Valid
    @NotNull(message = "objeto endereco não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    private EnderecoDto endereco;

    @Valid
    @NotNull(message = "objeto contato não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    private ContatoDto contato;

}
