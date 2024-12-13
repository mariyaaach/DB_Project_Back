package ru.itpark.mashacursah.infrastructure.exceptions.base.validation;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final ValidationExceptionCode code;

    public ValidationException(ValidationExceptionCode code) {
        super(code.value);
        this.code = code;
    }

}