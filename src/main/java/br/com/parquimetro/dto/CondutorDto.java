package br.com.parquimetro.dto;

import br.com.parquimetro.validation.annotation.CondutorExists;
import br.com.parquimetro.validation.annotation.CondutorNotExists;
import br.com.parquimetro.validation.annotation.EmailExists;
import br.com.parquimetro.validation.group.AdicaoGroup;
import br.com.parquimetro.validation.group.AtualizacaoGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.validation.annotation.Validated;

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

    @NotNull(message = "o campo status não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @NotEmpty(message = "o campo status não pode ser vazio", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @Email(message = "o campo email deve possuir uma estrutura válida de e-mail", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @EmailExists(message = "o e-mail preenchido já existe", groups = AdicaoGroup.class)
    private String email;

    @NotNull(message = "o campo ddd não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @Positive(message = "o campo ddd nao pode ser zero ou menor que zero", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @Digits(integer = 2, fraction = 0, message = "o campo ddd não pode ter casas decimais e não pode ter mais que 2 dígitos", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    private Integer ddd;

    @NotNull(message = "o campo telefone não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @Positive(message = "o campo telefone nao pode ser zero ou menor que zero", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @Digits(integer = 9, fraction = 0, message = "o campo telefone não pode ter casas decimais e não pode ter mais que 9 dígitos", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    private Long telefone;

    @Valid
    @NotNull(message = "objeto endereco não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    private EnderecoDto endereco;

}
