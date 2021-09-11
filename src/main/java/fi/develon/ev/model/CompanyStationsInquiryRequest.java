package fi.develon.ev.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author mahmood
 * @since 9/10/21
 */
@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder
public class CompanyStationsInquiryRequest extends PaginationRequest {

    @ApiModelProperty(value = "Company id", example = "45")
    @Positive
    @NotNull
    Long companyId;
}
