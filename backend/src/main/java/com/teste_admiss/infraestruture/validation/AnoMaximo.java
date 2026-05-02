package com.teste_admiss.infraestruture.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = AnoMaximoValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnoMaximo {
    String message() default "The year of manufacture cannot be in the future tense.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
