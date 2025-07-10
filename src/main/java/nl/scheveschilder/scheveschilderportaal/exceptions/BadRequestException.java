package nl.scheveschilder.scheveschilderportaal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for cases where a client's request is invalid.
 * The @ResponseStatus annotation causes Spring to return a 400 BAD REQUEST status code.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
