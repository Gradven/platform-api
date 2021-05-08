package com.channelsharing.pub.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = IdCardValidator.class)
@Documented
public @interface IdCard {
	String message() default "{idCard.illegal}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
