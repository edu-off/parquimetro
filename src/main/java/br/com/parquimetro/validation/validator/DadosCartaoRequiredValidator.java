package br.com.parquimetro.validation.validator;

import br.com.parquimetro.dto.DadosCartaoDto;
import br.com.parquimetro.validation.annotation.DadosCartaoRequired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class DadosCartaoRequiredValidator implements ConstraintValidator<DadosCartaoRequired, DadosCartaoDto> {

    @Override
    public void initialize(DadosCartaoRequired constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(DadosCartaoDto dto, ConstraintValidatorContext context) {
        return !Objects.isNull(dto);
    }

}
