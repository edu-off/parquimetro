package br.com.parquimetro.validation.validator;

import br.com.parquimetro.enumerator.ModalidadesEstacionamento;
import br.com.parquimetro.repository.CondutorRepository;
import br.com.parquimetro.validation.annotation.ModalidadeEstacionamentoNotExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ModalidadeEstacionamentoNotExistsValidator implements ConstraintValidator<ModalidadeEstacionamentoNotExists, String> {

    @Autowired
    private CondutorRepository condutorRepository;

    @Override
    public void initialize(ModalidadeEstacionamentoNotExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String modalidade, ConstraintValidatorContext context) {
        for (ModalidadesEstacionamento modalidadeValida : ModalidadesEstacionamento.values()) {
            if (modalidadeValida.toString().equalsIgnoreCase(modalidade))
                return true;
        }
        return false;
    }

}
