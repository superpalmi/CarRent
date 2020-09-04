package com.palmieri.demo.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UniquePlateValidator.class)
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface UniquePlate {
    public String message() default "There is already a vehicle with this plate!";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default{};
}
