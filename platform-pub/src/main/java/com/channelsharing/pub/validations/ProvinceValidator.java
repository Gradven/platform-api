package com.channelsharing.pub.validations;


import com.channelsharing.common.utils.IdCardUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <p>
 * 省份代码合法性校验

 */
public class ProvinceValidator implements ConstraintValidator<Province, String> {


	@Override
	public void initialize(Province constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value))
			return true;
		else
			return IdCardUtils.validateProvince(value);
	}
}
