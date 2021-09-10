package fi.develon.ev.model;

import lombok.Builder;
import lombok.Value;

/**
 * Represent a company
 * @author mahmood
 * @since 9/10/21
 */
@Value
@Builder
public class CompanyDto {
    Long id;
    String name;
    Long parentId;
}
