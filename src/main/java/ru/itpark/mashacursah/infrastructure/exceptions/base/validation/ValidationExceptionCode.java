package ru.itpark.mashacursah.infrastructure.exceptions.base.validation;

public enum ValidationExceptionCode {
    ACCESS_DENIED("Доступ запрещен"),
    INVALID_REFRESH_TOKEN("Невалидный токен")
    ;

    final String value;

    ValidationExceptionCode(String value) {
        this.value = value;
    }
}
