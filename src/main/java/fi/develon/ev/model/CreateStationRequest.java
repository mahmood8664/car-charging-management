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
 * Create new station request
 * @author mahmood
 * @since 9/10/21
 */
@Builder
@Value
public class CreateStationRequest {

    @ApiModelProperty(value = "Station name", example = "DFR-124")
    @Length(min = 3, max = 100)
    @NotNull
    String stationName;

    @ApiModelProperty(value = "latitude of station's location, between 90 and -90", example = "25.32652655")
    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    @NotNull
    BigDecimal latitude;

    @ApiModelProperty(value = "latitude of station's location, between 90 and -90", example = "60.32652655")
    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    @NotNull
    BigDecimal longitude;

    @ApiModelProperty(value = "company id", example = "25")
    @Positive
    @NotNull
    Long companyId;
}
