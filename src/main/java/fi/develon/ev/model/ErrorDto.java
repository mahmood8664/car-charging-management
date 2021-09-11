package fi.develon.ev.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

/**
 * Error data response
 *
 * @author mahmood
 * @since 9/10/21
 */
@Value
@Builder
public class ErrorDto {

    @ApiModelProperty(value = "Error code starts from 1000", example = "1001")
    int errorCode;

    @ApiModelProperty(value = "Error message", example = "Not found")
    String message;

    @ApiModelProperty(value = "Error additional data")
    Map<String, Object> data;
}
