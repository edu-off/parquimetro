package br.com.parquimetro.validation.validator;

import br.com.parquimetro.enumerator.UnidadesFederativas;
import br.com.parquimetro.validation.annotation.UfNotExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UfNotExistsValidator implements ConstraintValidator<UfNotExists, String> {

    @Override
    public void initialize(UfNotExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String uf, ConstraintValidatorContext context) {
        for (UnidadesFederativas ufValida : UnidadesFederativas.values()) {
            if (ufValida.toString().equalsIgnoreCase(uf))
                return true;
        }
        return false;
    }

}
