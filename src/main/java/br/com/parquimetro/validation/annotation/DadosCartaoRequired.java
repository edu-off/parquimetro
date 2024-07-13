package br.com.parquimetro.validation.annotation;

import br.com.parquimetro.validation.validator.DadosCartaoRequiredValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DadosCartaoRequiredValidator.class)
public @interface DadosCartaoRequired {

    String message() default "dados cartao required error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
