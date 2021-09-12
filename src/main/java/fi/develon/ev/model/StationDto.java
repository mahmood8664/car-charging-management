package fi.develon.ev.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * represent a station model
 *
 * @author mahmood
 * @since 9/10/21
 */
@Value
@Builder
public class StationDto {
    @ApiModelProperty(value = "Station id", example = "123sdf")
    @Positive
    @NotNull
    String stationId;

    @ApiModelProperty(value = "Station name", example = "JST-123")
    @Length(min = 3, max = 100)
    @NotNull
    String stationName;

    @ApiModelProperty(value = "Latitude of station location, between 90 and -90", example = "25.32652655")
    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    @NotNull
    BigDecimal latitude;

    @ApiModelProperty(value = "Longitude of station location, between 180 and -180", example = "135.32652655")
    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    @NotNull
    BigDecimal longitude;

    @ApiModelProperty(value = "Company id", example = "433sdfc")
    @Positive
    @NotNull
    String companyId;
}
