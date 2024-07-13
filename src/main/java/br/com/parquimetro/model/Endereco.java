package br.com.parquimetro.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "endereco")
public class Endereco {

    @Id
    private String id;

    private String logradouro;

    private String bairro;

    private String cidade;

    private String uf;

    private Long cep;

    @Version
    private Long version;

}
