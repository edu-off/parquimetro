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
@Document(collection = "metodo_pagamento")
public class MetodoPagamento {

    @Id
    private String id;

    private String status;

    private String metodo;

    @DBRef
    private DadosCartao dadosCartao;

    @Version
    private Long version;

}
