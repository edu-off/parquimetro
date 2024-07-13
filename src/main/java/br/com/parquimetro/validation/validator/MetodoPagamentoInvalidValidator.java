package br.com.parquimetro.validation.validator;

import br.com.parquimetro.enumerator.MetodosPagamento;
import br.com.parquimetro.validation.annotation.MetodoPagamentoInvalid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MetodoPagamentoInvalidValidator implements ConstraintValidator<MetodoPagamentoInvalid, String> {

    @Override
    public void initialize(MetodoPagamentoInvalid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String metodo, ConstraintValidatorContext context) {
        for (MetodosPagamento metodoValido : MetodosPagamento.values()) {
            if (metodoValido.toString().equalsIgnoreCase(metodo))
                return true;
        }
        return false;
    }

}
