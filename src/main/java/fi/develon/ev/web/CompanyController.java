package fi.develon.ev.web;

import fi.develon.ev.model.*;
import fi.develon.ev.service.CompanyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Company controller including all APIs related to company resource
 *
 * @author mahmood
 * @since 9/10/21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    @ApiOperation(value = "Returns all companies, this services is paginated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("")
    public BaseResponse<PagingResponse<CompanyDto>> allCompanies(PaginationRequest request) {
        return BaseResponse.of(companyService.allCompanies(request));
    }

    @ApiOperation(value = "Returns given company id's information, not including stations, for retrieve stations " +
            "call api/v**/company/{company_id}/details endpoint")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Company not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/{company_id}")
    public BaseResponse<CompanyDto> getCompany(@PathVariable("company_id") @Valid @Length(max = 100) String companyId) {
        return BaseResponse.of(companyService.getCompany(companyId));
    }

    @ApiOperation(value = "Returns company details including child companies and stations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Company not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/{company_id}/details")
    public BaseResponse<CompanyDetailDto> getCompanyDetails(@PathVariable("company_id") @Valid @Length(max = 100) String companyId,
                                                            @RequestParam(name = "includeChildren", defaultValue = "true") boolean includeChildren) {
        return BaseResponse.of(companyService.getCompanyDetails(companyId, includeChildren));
    }

    @ApiOperation(value = "Create new company")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Parent company not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("")
    public BaseResponse<String> createCompany(@RequestBody @Valid CreateCompanyRequest request) {
        return BaseResponse.of(companyService.createCompany(request));
    }

    @ApiOperation(value = "Update company info, not it's stations, to add/edit/delete stations use stations API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Company not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PutMapping("")
    public BaseResponse<Void> updateCompany(@RequestBody @Valid CompanyDto request) {
        companyService.updateCompany(request);
        return BaseResponse.ok();
    }

    @ApiOperation(value = "Delete company and it's direct stations, set the children parent company to null")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Company not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @DeleteMapping("/{company_id}")
    public BaseResponse<Void> deleteCompany(@PathVariable("company_id") @Valid @Length(max = 100) String companyId) {
        companyService.deleteCompany(companyId);
        return BaseResponse.ok();
    }


}
