package br.com.parquimetro.error.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse implements Serializable {

    private String timestamp;
    private Integer status;
    private String message;
    private String path;

}
