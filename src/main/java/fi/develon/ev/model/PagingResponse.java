package fi.develon.ev.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * Paginated response
 *
 * @author mahmood
 * @since 9/10/21
 */
@Value
@Builder
public class PagingResponse<T> {

    @ApiModelProperty(value = "response list")
    List<T> responseList;
    @ApiModelProperty(value = "true if there are other items otherwise false", example = "true")
    boolean hasNext;

    public static <E> PagingResponse<E> of(List<E> list, boolean hasNext) {
        return new PagingResponse<>(list, hasNext);
    }
}
