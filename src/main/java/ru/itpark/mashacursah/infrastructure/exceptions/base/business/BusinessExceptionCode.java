package ru.itpark.mashacursah.infrastructure.exceptions.base.business;

public enum BusinessExceptionCode {
    USER_NOT_FOUND("Пользователь не найден"),
    USER_ALREADY_EXISTS("Пользователь уже существует"),
    ROLE_NOT_FOUND("Роль не найдена"),
    BOOKING_ERROR("Ошибка при бронировании комнаты"),
    BOOKING_NOT_FOUND("Бронь не найдена"),
    PAYMENT_NOT_FOUND("Платеж не найден"),
    ;

    final String value;

    BusinessExceptionCode(String value) {
        this.value = value;
    }
}
