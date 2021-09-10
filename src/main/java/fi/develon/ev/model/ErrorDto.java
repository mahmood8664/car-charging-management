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
public class ErrorDto {
    int errorCode;
    String message;
    Map<String, Object> data;
}
