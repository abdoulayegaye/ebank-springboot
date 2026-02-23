package sn.xoslu.tech.ebank.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sn.xoslu.tech.ebank.annotations.impl.UniqueEmailValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@Documented
public @interface UniqueEmail {
    String message() default "Cet email est déjà enregistré.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
