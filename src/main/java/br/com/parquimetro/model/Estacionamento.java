package br.com.parquimetro.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "estacionamento")
public class Estacionamento {

    @Id
    private String id;

    @Indexed
    private String status;

    private String modalidade;

    private Integer tempoDuracao;

    private LocalDateTime entrada;

    private LocalDateTime saida;

    private LocalDateTime saidaPrevista;

    @DBRef
    private Condutor condutor;

    @DBRef
    private Veiculo veiculo;

    @DBRef
    private Pagamento pagamento;

    @Version
    private Long version;

}
