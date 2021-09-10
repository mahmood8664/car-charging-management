package fi.develon.ev.model;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

/**
 * @author mahmood
 * @since 9/10/21
 */
@Value
@Builder
public class BaseResponse<T> {
    boolean successful;
    T response;
    ErrorData errorData;

    public BaseResponse(T response) {
        this.successful = true;
        this.response = response;
        this.errorData = null;
    }

    public BaseResponse() {
        this.successful = true;
        this.response = null;
        this.errorData = null;
    }

    public BaseResponse(ErrorData errorData) {
        this.successful = false;
        this.errorData = errorData;
        this.response = null;
    }

    public static <S> BaseResponse<S> of(S response) {
        return new BaseResponse<>(response);
    }

    public static <S> BaseResponse<S> ok() {
        return new BaseResponse<>();
    }

    public static BaseResponse<?> error(ErrorData errorData) {
        return new BaseResponse<>(errorData);
    }

    @Value
    @Builder
    public static class ErrorData {
        int errorCode;
        String message;
        Map<String, Object> data;
    }

}
