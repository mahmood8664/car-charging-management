package fi.develon.ev.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Create company request
 *
 * @author mahmood
 * @since 9/10/21
 */
@Builder
@Value
public class CreateCompanyRequest {
    @ApiModelProperty(value = "Company name", example = "DFG-454")
    @NotNull
    @Length(min = 3)
    String companyName;

    @ApiModelProperty(value = "Parent company id", example = "433sdfc")
    @Length(min = 1, max = 100)
    String parentCompanyId;
}
