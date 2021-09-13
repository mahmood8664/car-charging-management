package fi.develon.ev.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
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
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginationRequest {
    @ApiModelProperty(value = "page number starts from 0", example = "0")
    @Max(10000)
    @Min(0)
    private int pageNumber = 0;

    @ApiModelProperty(value = "page size, value must between 1 and 10000", example = "50")
    @Max(10000)
    @Min(1)
    private int size = 20;

}
