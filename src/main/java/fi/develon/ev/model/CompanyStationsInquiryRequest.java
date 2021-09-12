package fi.develon.ev.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

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

    @ApiModelProperty(value = "Company id", example = "433sdfc")
    @Length(min = 1,max = 100)
    @NotNull
    String companyId;
}
