package br.com.parquimetro.error.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionException extends RuntimeException {

    public TransactionException(String mensagem) {
        super(mensagem);
    }

}
