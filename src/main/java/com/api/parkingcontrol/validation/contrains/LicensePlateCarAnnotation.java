package com.api.parkingcontrol.validation.contrains;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = LicensePlateCarValidator.class)
@Target(value = {METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)

public @interface LicensePlateCarAnnotation {

    String message() default "{A placa precisa conter 3 letras e 4 numeros, no padrao: abc1234}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}