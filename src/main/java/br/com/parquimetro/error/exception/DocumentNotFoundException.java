package br.com.parquimetro.error.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentNotFoundException extends RuntimeException {

    public DocumentNotFoundException(String mensagem) {
        super(mensagem);
    }

}
