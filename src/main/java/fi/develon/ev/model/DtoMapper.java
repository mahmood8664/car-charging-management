package fi.develon.ev.model;

import fi.develon.ev.entity.Company;

/**
 * @author mahmood
 * @since 9/13/21
 */
public class DtoMapper {

    private DtoMapper() {
    }

    public static CompanyDto getCompanyDto(Company company) {
        return CompanyDto.builder()
                .companyId(company.getId())
                .companyName(company.getName())
                .parentCompanyId(company.getParentCompanyId())
                .build();
    }
}
