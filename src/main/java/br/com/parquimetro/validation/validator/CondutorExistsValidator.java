package br.com.parquimetro.validation.validator;

import br.com.parquimetro.repository.CondutorRepository;
import br.com.parquimetro.validation.annotation.CondutorExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CondutorExistsValidator implements ConstraintValidator<CondutorExists, String> {

    @Autowired
    private CondutorRepository condutorRepository;

    @Override
    public void initialize(CondutorExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        return condutorRepository.findById(cpf).isEmpty();
    }

}
