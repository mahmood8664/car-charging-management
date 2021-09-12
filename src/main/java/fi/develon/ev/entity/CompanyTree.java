package fi.develon.ev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author mahmood
 * @since 9/10/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyTree extends Company {

    private List<CompanyTree> childCompanies;

    @Override
    public String toString() {
        return super.toString();
    }
}
