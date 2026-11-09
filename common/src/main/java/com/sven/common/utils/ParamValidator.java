package com.sven.common.utils;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ParamValidator {

    private static final Validator validator;

	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

    public static String validateSingleRetrunKey(final Object object) throws Exception {
        StringBuffer resultStr = new StringBuffer();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<Object> violation : constraintViolations) {
                final String columnName = violation.getPropertyPath().toString();
                final String message = violation.getMessage();
                String textMessage = message.replace("{", "").replace("}", "");
                resultStr.append(textMessage);
                break;
            }
        }

        return resultStr.toString();
    }

    public static String validateSingleRetrunKey(final Object object, Class<?>... groups) throws Exception {
        StringBuffer resultStr = new StringBuffer();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<Object> violation : constraintViolations) {
                final String columnName = violation.getPropertyPath().toString();
                final String message = violation.getMessage();
                // messsage转换成国际化
                String textMessage = message.replace("{", "").replace("}", "");
                resultStr.append(textMessage);
                break;
            }
        }

        return resultStr.toString();
    }
}
