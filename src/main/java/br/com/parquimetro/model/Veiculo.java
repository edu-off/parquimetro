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
@Document(collection = "veiculo")
public class Veiculo {

    @Id
    private String id;

    private String status;

    private String placa;

    private String modelo;

    private Integer ano;

    private String fabricante;

    private String cor;

    @Version
    private Long version;

}
