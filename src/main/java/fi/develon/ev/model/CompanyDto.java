package fi.develon.ev.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Represent a company
 *
 * @author mahmood
 * @since 9/10/21
 */
@Value
@Builder
public class CompanyDto {
    @ApiModelProperty(value = "Company id", example = "433sdfc")
    @Length(min = 1, max = 100)
    @NotNull
    String companyId;
    @ApiModelProperty(value = "Company name", example = "DSF-343")
    @Length(min = 3, max = 100)
    @NotNull
    String companyName;
    @ApiModelProperty(value = "Parent company id", example = "433sdfc")
    @Length(min = 1, max = 100)
    String parentCompanyId;
}
