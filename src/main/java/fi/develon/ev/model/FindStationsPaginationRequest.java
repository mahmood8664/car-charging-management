package fi.develon.ev.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Pagination request
 *
 * @author mahmood
 * @since 9/10/21
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindStationsPaginationRequest extends PaginationRequest{
    @Length(max = 100)
    private String companyId;
}
