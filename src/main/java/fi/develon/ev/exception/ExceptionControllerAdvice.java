package fi.develon.ev.exception;

import fi.develon.ev.model.BaseResponse;
import fi.develon.ev.model.ErrorDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Locale;

/**
 * @author mahmood
 * @since 9/10/21
 */
@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class ExceptionControllerAdvice {

    private final ResourceBundleMessageSource messageSource;

    @ExceptionHandler(SMException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<ErrorDto>> handleWalletException(SMException e) {
        log.debug("wallet exception occurred: ", e);
        String message = messageSource.getMessage(e.getErrorType().getMessageKey(), null, Locale.getDefault());
        return ResponseEntity.status(e.getErrorType().getHttpStatus()).body(BaseResponse.error(ErrorDto.builder()
                .data(e.getData())
                .errorCode(e.getErrorType().getCode())
                .message(message)
                .build()));
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<ErrorDto>> validationException2(BindException e) {
        log.debug("validation exception occurred: ", e);
        String errorMsg;
        String errorField = null;
        if (e.getBindingResult().getFieldError() != null) {
            errorMsg = e.getBindingResult().getFieldError().getDefaultMessage();
            errorField = e.getBindingResult().getFieldError().getField();
        } else {
            errorMsg = e.getMessage();
        }
        if (errorMsg != null && errorMsg.startsWith("{")) {
            String newMsg = messageSource.getMessage(errorMsg.substring(1, errorMsg.length() - 1), null, Locale.getDefault());
            if (!StringUtils.isEmpty(newMsg)) {
                errorMsg = newMsg;
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.error(
                ErrorDto
                        .builder()
                        .errorCode(SMExceptionType.VALIDATION.getCode())
                        .message(errorMsg)
                        .data(errorField == null ? null : Collections.singletonMap("errorField", errorField))
                        .build()
        ));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<ErrorDto>> handleException(Exception e) {
        log.warn("exception occurred: ", e);
        String message = messageSource.getMessage(SMExceptionType.GENERAL.getMessageKey(), null, Locale.getDefault());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(
                ErrorDto
                        .builder()
                        .errorCode(1000)
                        .message(message)
                        .build()
        ));
    }

}
