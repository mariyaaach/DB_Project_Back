package ru.itpark.mashacursah.infrastructure.exceptions.base.invocation;

public enum InvocationExceptionCode {
    USER_NOT_FOUND("Ошибка при поиске пользователя в db.users");

    final String value;

    InvocationExceptionCode(String value) {
        this.value = value;
    }
}
