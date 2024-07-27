package br.com.parquimetro.error.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetodoPagamentoException extends RuntimeException {

    public MetodoPagamentoException(String mensagem) {
        super(mensagem);
    }

}
