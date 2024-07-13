package br.com.parquimetro.validation.annotation;

import br.com.parquimetro.validation.validator.CondutorNotExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CondutorNotExistsValidator.class)
public @interface CondutorNotExists {

    String message() default "condutor not exists error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
