package br.com.parquimetro.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dados_cartao")
public class DadosCartao {

    @Id
    private String id;

    private Long numero;

    private String nomeCompleto;

    private LocalDate dataVencimento;

    private Integer codigoSeguranca;

    @Version
    private Long version;

}
