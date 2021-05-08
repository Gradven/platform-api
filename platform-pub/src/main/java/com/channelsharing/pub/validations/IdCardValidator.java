package com.channelsharing.pub.validations;



import com.channelsharing.common.utils.IdCardUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <p>
 * 身份证合法性校验

 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {


	@Override
	public void initialize(IdCard constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return IdCardUtils.validateAllIdCard(value);
	}
}
