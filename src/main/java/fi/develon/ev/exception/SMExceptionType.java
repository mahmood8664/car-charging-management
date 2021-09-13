package fi.develon.ev.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author mahmood
 * @since 9/10/21
 */
@Getter
public enum SMExceptionType {
    GENERAL(1000, HttpStatus.INTERNAL_SERVER_ERROR, "fi.develon.ev.error.general"),
    VALIDATION(1001, HttpStatus.BAD_REQUEST, "fi.develon.ev.error.validation"),
    NOT_FOUND(1002, HttpStatus.NOT_FOUND, "fi.develon.ev.error.not_found"),
    BAD_REQUEST(1003, HttpStatus.BAD_REQUEST, "fi.develon.ev.error.bad_request");

    private final int code;
    private final HttpStatus httpStatus;
    private final String messageKey;

    SMExceptionType(int code, HttpStatus httpStatus, String messageKey) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }

}
