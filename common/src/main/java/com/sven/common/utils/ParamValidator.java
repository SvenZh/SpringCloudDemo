package com.sven.common.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

public class ParamValidator {

    private static Validator validator;

    public static String validateSingleRetrunKey(final Object object) throws Exception {
        StringBuffer resultStr = new StringBuffer();
        Set<ConstraintViolation<Object>> constraintViolations = getInstance().validate(object);
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
        Set<ConstraintViolation<Object>> constraintViolations = getInstance().validate(object, groups);
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

    private static synchronized Validator getInstance() {
        if (validator == null) {
            validator = Validation.buildDefaultValidatorFactory().getValidator();
        }

        return validator;
    }
}
