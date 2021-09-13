package fi.develon.ev.model;

import lombok.Builder;
import lombok.Value;

/**
 * General base response to return in rest services
 *
 * @author mahmood
 * @since 9/10/21
 */
@Value
@Builder
public class BaseResponse<T> {
    boolean successful;
    T response;
    ErrorDto errorDto;

    public static <S> BaseResponse<S> of(S response) {
        return BaseResponse.<S>builder()
                .response(response)
                .successful(true)
                .errorDto(null)
                .build();
    }

    public static <S> BaseResponse<S> ok() {
        return BaseResponse.<S>builder()
                .response(null)
                .successful(true)
                .errorDto(null)
                .build();
    }

    public static BaseResponse<ErrorDto> error(ErrorDto errorDto) {
        return BaseResponse.<ErrorDto>builder()
                .response(null)
                .successful(false)
                .errorDto(errorDto)
                .build();
    }

}
