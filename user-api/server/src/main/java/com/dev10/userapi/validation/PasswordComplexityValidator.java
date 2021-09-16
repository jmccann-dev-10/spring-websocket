package com.dev10.userapi.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

public class PasswordComplexityValidator implements ConstraintValidator<PasswordComplexity, String> {
    @Override
    public void initialize(PasswordComplexity constraintAnnotation) {
        // Nothing to do.
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // Only validate the complexity of the password
        // if it's not null and at least 8 characters in length.
        if (value == null || value.length() < 8) {
            return true;
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : value.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        if (digits == 0 || letters == 0 || others == 0) {
            return false;
        }

        return true;
    }
}
