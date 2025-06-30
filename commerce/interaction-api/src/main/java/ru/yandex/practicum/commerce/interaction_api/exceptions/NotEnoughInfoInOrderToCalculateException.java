package ru.yandex.practicum.commerce.interaction_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when there is not enough information in the order to perform calculations.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {
    public NotEnoughInfoInOrderToCalculateException(String message) {
        super(message);
    }
}