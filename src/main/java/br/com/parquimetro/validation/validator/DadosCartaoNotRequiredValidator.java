package br.com.parquimetro.validation.validator;

import br.com.parquimetro.dto.DadosCartaoDto;
import br.com.parquimetro.validation.annotation.DadosCartaoNotRequired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class DadosCartaoNotRequiredValidator implements ConstraintValidator<DadosCartaoNotRequired, DadosCartaoDto> {

    @Override
    public void initialize(DadosCartaoNotRequired constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(DadosCartaoDto dto, ConstraintValidatorContext context) {
        return Objects.isNull(dto);
    }

}
