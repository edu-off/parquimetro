package br.com.parquimetro.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPagamentoException extends RuntimeException {

    private String mensagem;

}
