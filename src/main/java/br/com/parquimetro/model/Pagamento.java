package br.com.parquimetro.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pagamento")
public class Pagamento {

    @Id
    private String id;

    private String status;

    private Double valor;

    @DBRef
    private MetodoPagamento metodoPagamento;

    @Version
    private Long version;

}
