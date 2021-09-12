package fi.develon.ev.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Represent a company
 *
 * @author mahmood
 * @since 9/10/21
 */
@Value
@Builder
public class CompanyDto {
    @ApiModelProperty(value = "Company id", example = "222")
    @Length(min = 1,max = 100)
    @NotNull
    String companyId;
    @ApiModelProperty(value = "Company name", example = "DSF-343")
    @Length(min = 3, max = 100)
    @NotNull
    String companyName;
    @ApiModelProperty(value = "Parent company id", example = "454")
    @Length(min = 1,max = 100)
    @NotNull
    String parentCompanyId;
}
