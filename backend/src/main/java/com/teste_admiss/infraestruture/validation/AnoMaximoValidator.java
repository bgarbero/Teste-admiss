package com.teste_admiss.infraestruture.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AnoMaximoValidator implements ConstraintValidator<AnoMaximo, Integer> {

    @Override
    public boolean isValid(Integer ano, ConstraintValidatorContext context) {
        if (ano == null) return true;
        return ano <= LocalDate.now().getYear();
    }
}
