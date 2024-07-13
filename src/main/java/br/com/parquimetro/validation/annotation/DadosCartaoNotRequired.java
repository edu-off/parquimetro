package br.com.parquimetro.validation.annotation;

import br.com.parquimetro.validation.validator.DadosCartaoNotRequiredValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DadosCartaoNotRequiredValidator.class)
public @interface DadosCartaoNotRequired {

    String message() default "dados cartao not required error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
