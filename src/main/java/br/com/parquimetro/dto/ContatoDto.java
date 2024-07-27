package br.com.parquimetro.dto;

import br.com.parquimetro.validation.annotation.EmailExists;
import br.com.parquimetro.validation.group.AdicaoGroup;
import br.com.parquimetro.validation.group.AtualizacaoGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContatoDto {

    @NotNull(message = "o campo status não pode ser nulo", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @NotEmpty(message = "o campo status não pode ser vazio", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @Email(message = "o campo email deve possuir uma estrutura válida de e-mail", groups = {AdicaoGroup.class, AtualizacaoGroup.class})
    @EmailExists(message = "o e-mail preenchido já existe", groups = AdicaoGroup.class)
    private String email;

    private List<TelefoneDto> telefones;

}
