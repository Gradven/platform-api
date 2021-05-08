package com.channelsharing.hongqu.portal.api.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = SensitiveWordValidator.class)
@Documented
public @interface Sensitive {
	String message() default "{sensitive.word}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
