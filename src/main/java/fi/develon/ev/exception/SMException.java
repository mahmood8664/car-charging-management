package fi.develon.ev.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Map;

/**
 * @author mahmood
 * @since 9/10/21
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class SMException extends RuntimeException {

    SMExceptionType errorType;
    Map<String, Object> data;

    public SMException(SMExceptionType errorType) {
        this.errorType = errorType;
        this.data = null;
    }

    public SMException(SMExceptionType errorType, String message) {
        super(message);
        this.errorType = errorType;
        this.data = null;
    }

    public SMException(SMExceptionType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
        this.data = null;
    }

    public SMException(SMExceptionType errorType, Map<String, Object> data) {
        this.errorType = errorType;
        this.data = data;
    }

    public SMException(SMExceptionType errorType, String message, Map<String, Object> data) {
        super(message);
        this.errorType = errorType;
        this.data = data;
    }

    public SMException(SMExceptionType errorType, String message, Throwable cause, Map<String, Object> data) {
        super(message, cause);
        this.errorType = errorType;
        this.data = data;
    }

}
