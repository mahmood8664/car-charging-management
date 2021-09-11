package fi.develon.ev.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * Company Details including child companies
 *
 * @author mahmood
 * @since 9/11/21
 */
@Value
@Builder
public class CompanyDetailDto {
    @ApiModelProperty(value = "Company info of given company id")
    CompanyDto company;

    @ApiModelProperty(value = "List of stations")
    List<StationDto> stations;

    @ApiModelProperty(value = "List of children companies with their stations")
    List<CompanyDetailDto> childCompanies;
}
