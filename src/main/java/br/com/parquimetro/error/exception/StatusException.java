package br.com.parquimetro.error.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusException extends RuntimeException {

    public StatusException(String mensagem) {
        super(mensagem);
    }

}
