package ru.itpark.mashacursah.infrastructure.exceptions.base.business;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final BusinessExceptionCode code;

    public BusinessException(BusinessExceptionCode code) {
        super(code.value);
        this.code = code;
    }

}