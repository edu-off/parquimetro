package br.com.parquimetro.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "condutor")
public class Condutor {

    @Id
    private String cpf;

    private String status;

    @TextIndexed
    private String nome;

    @DBRef
    private Endereco endereco;

    @DBRef
    private Contato contato;

    @DBRef
    private List<Veiculo> veiculos = new ArrayList<>();

    @DBRef
    private List<MetodoPagamento> metodosPagamento = new ArrayList<>();

    @Version
    private Long version;

}
