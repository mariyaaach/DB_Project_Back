package ru.itpark.mashacursah.infrastructure.exceptions.base.invocation;

import lombok.Getter;

@Getter
public class InvocationException extends RuntimeException {
    private final InvocationExceptionCode code;

    public InvocationException(InvocationExceptionCode code) {
        super(code.value);
        this.code = code;
    }

}