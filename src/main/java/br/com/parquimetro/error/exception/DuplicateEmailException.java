package br.com.parquimetro.error.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String mensagem) {
        super(mensagem);
    }

}
