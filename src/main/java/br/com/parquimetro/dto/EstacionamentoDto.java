package br.com.parquimetro.dto;

import br.com.parquimetro.validation.annotation.ModalidadeEstacionamentoNotExists;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstacionamentoDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    @NotNull(message = "o campo modalidade não pode ser nulo")
    @NotEmpty(message = "o campo modalidade não pode ser vazio")
    @ModalidadeEstacionamentoNotExists(message = "modalidade de estacionamento não existe")
    private String modalidade;

    @NotNull(message = "o campo tempo de duracao não pode ser nulo")
    @Positive(message = "o campo tempo de duracao nao pode ser zero ou menor que zero")
    @Digits(integer = 2, fraction = 0, message = "o campo tempo de duracao não pode ter casas decimais e não pode ter mais que 2 dígitos")
    private Integer tempoDuracao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime entrada;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime saida;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime saidaPrevista;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CondutorDto condutor;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private VeiculoDto veiculo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PagamentoDto pagamento;

}
