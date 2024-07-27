package br.com.parquimetro.error.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstacionamentoException extends RuntimeException {

    public EstacionamentoException(String mensagem) {
        super(mensagem);
    }

}
