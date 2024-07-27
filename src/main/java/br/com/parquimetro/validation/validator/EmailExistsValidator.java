package br.com.parquimetro.validation.validator;

import br.com.parquimetro.repository.ContatoRepository;
import br.com.parquimetro.validation.annotation.EmailExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailExistsValidator implements ConstraintValidator<EmailExists, String> {

    @Autowired
    private ContatoRepository contatoRepository;

    @Override
    public void initialize(EmailExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !contatoRepository.existsByEmail(email);
    }

}
