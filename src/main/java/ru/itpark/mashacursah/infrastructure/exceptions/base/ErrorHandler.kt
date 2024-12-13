package ru.mai.coursework.infrastructure.exceptions.base

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ErrorHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<String?> {
        return ResponseEntity<String?>(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvocationException::class)
    fun handleInvocationException(ex: InvocationException): ResponseEntity<String?> {
        return ResponseEntity<String?>(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
    @ExceptionHandler(ValidationException::class)
    fun handleInvocationException(ex: ValidationException): ResponseEntity<String?> {
        return ResponseEntity<String?>(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}