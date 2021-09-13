package fi.develon.ev.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * finding nearby stations inquiry request
 *
 * @author mahmood
 * @since 9/10/21
 */
@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder
@AllArgsConstructor
public class NearbyStationsInquiryRequest extends PaginationRequest {

    @ApiModelProperty(value = "latitude of the given point, between 90 and -90", example = "25.32652655")
    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    @NotNull
    BigDecimal latitude;

    @ApiModelProperty(value = "longitude of the given point, between 180 and -180", example = "50.32652655")
    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    @NotNull
    BigDecimal longitude;

    @ApiModelProperty(value = "Maximum distance of stations from given point (in Kilometer)", example = "3")
    @Positive
    int distance;
}
