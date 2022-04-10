package com.api.parkingcontrol.validation.contrains;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LicensePlateCarValidator implements ConstraintValidator<LicensePlateCarAnnotation, String> {

    @Override
    public void initialize(LicensePlateCarAnnotation licensePlateCarAnnotation) {
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {

        String part1 = string.substring(0, 3);
        String part2 = string.substring(3);

        if (part1.matches("[0-9]+")) {
            return false;
        }
        if (part2.matches("[a-w]+")) {
            return false;
        }
        return true;
    }
}


