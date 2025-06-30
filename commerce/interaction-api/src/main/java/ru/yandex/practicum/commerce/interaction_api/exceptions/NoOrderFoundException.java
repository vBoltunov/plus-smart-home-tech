package ru.yandex.practicum.commerce.interaction_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an order is not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoOrderFoundException extends RuntimeException {
    public NoOrderFoundException(String message) {
        super(message);
    }
}
