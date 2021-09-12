package fi.develon.ev.tdd;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.ev.entity.Company;
import fi.develon.ev.model.BaseResponse;
import fi.develon.ev.model.CompanyDto;
import fi.develon.ev.model.CreateCompanyRequest;
import fi.develon.ev.model.PagingResponse;
import fi.develon.ev.testcontainer.MongoDBIT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

/**
 * @author mahmood
 * @since 9/11/21
 */
public class StationControllerTest extends MongoDBIT {


    @Test
    void getAllCompaniesTest_OK() throws Exception {
        companyRepository.saveAll(List.of(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build(),
                Company.builder()
                        .id("2222")
                        .name("222")
                        .parentCompanyId("1111")
                        .build()));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/company?pageNumber=0&size=2"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<PagingResponse<CompanyDto>> allCompanyResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });


        Assertions.assertThat(allCompanyResponse.isSuccessful()).isTrue();
        Assertions.assertThat(allCompanyResponse.getResponse().getResponseList()).isNotNull();
        Assertions.assertThat(allCompanyResponse.getResponse().getResponseList().size()).isEqualTo(2);
        Assertions.assertThat(allCompanyResponse.getResponse().isHasNext()).isFalse();

        if (allCompanyResponse.getResponse().getResponseList().get(0).getCompanyId().equals("1111")) {
            Assertions.assertThat(allCompanyResponse.getResponse().getResponseList().get(0).getCompanyName()).isEqualTo("1");
        }

        if (allCompanyResponse.getResponse().getResponseList().get(0).getCompanyId().equals("2222")) {
            Assertions.assertThat(allCompanyResponse.getResponse().getResponseList().get(0).getCompanyName()).isEqualTo("2");
        }

    }

    @Test
    void getCompanyTest_OK() throws Exception {
        companyRepository.saveAll(List.of(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build(),
                Company.builder()
                        .id("2222")
                        .name("222")
                        .parentCompanyId("1111")
                        .build()));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/company/2222"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<CompanyDto> getCompanyResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(getCompanyResponse.isSuccessful()).isTrue();
        Assertions.assertThat(getCompanyResponse.getResponse()).isNotNull();
        Assertions.assertThat(getCompanyResponse.getResponse().getParentCompanyId()).isEqualTo("1111");
        Assertions.assertThat(getCompanyResponse.getResponse().getCompanyName()).isEqualTo("2");
        Assertions.assertThat(getCompanyResponse.getResponse().getParentCompanyId()).isEqualTo("1111");

    }

    @Test
    void getCompanyTest_NotFound() throws Exception {
        companyRepository.saveAll(List.of(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build(),
                Company.builder()
                        .id("2222")
                        .name("222")
                        .parentCompanyId("1111")
                        .build()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/company/3333"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    void createCompanyTest_OK() throws Exception {
        companyRepository.saveAll(List.of(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build(),
                Company.builder()
                        .id("2222")
                        .name("222")
                        .parentCompanyId("1111")
                        .build()));

        CreateCompanyRequest request = CreateCompanyRequest.builder()
                .companyName("3333")
                .parentCompanyId("2222")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<String> createResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(createResponse.isSuccessful()).isTrue();
        Assertions.assertThat(createResponse.getResponse()).isNotEmpty();

        Optional<Company> cmp = companyRepository.findOne(Example.of(Company.builder().id(createResponse.getResponse()).build()));
        cmp.ifPresentOrElse(company -> {
            Assertions.assertThat(company.getParentCompanyId()).isEqualTo(request.getParentCompanyId());
            Assertions.assertThat(company.getName()).isEqualTo(request.getCompanyName());
        }, () -> Assertions.fail("company is not created!"));

    }

    @Test
    void createCompanyTest_NotFound() throws Exception {
        companyRepository.saveAll(List.of(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build(),
                Company.builder()
                        .id("2222")
                        .name("222")
                        .parentCompanyId("1111")
                        .build()));

        CreateCompanyRequest request = CreateCompanyRequest.builder()
                .companyName("3333")
                .parentCompanyId("sdfds")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

    }


    @Test
    void updateCompanyTest_OK() throws Exception {
        companyRepository.saveAll(List.of(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build(),
                Company.builder()
                        .id("2222")
                        .name("222")
                        .parentCompanyId("1111")
                        .build(),
                Company.builder()
                        .id("3333")
                        .name("333")
                        .parentCompanyId("2222")
                        .build()));

        CompanyDto request = CompanyDto.builder()
                .companyId("3333")
                .companyName("new")
                .parentCompanyId("1111")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<Void> updateResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(updateResponse.isSuccessful()).isTrue();

        Optional<Company> cmp = companyRepository.findOne(Example.of(Company.builder().id("3333").build()));
        cmp.ifPresentOrElse(company -> {
            Assertions.assertThat(company.getParentCompanyId()).isEqualTo(request.getParentCompanyId());
            Assertions.assertThat(company.getName()).isEqualTo(request.getCompanyName());
        }, () -> Assertions.fail("company update is failed!"));

    }

    @Test
    void updateCompanyTest_NotFound() throws Exception {
        companyRepository.saveAll(List.of(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build(),
                Company.builder()
                        .id("2222")
                        .name("222")
                        .parentCompanyId("1111")
                        .build(),
                Company.builder()
                        .id("3333")
                        .name("333")
                        .parentCompanyId("2222")
                        .build()));

        CompanyDto request = CompanyDto.builder()
                .companyId("3333")
                .companyName("new")
                .parentCompanyId("1111111")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

    }


    @Test
    void deleteCompanyTest_OK() throws Exception {
        companyRepository.saveAll(List.of(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build(),
                Company.builder()
                        .id("2222")
                        .name("222")
                        .parentCompanyId("1111")
                        .build(),
                Company.builder()
                        .id("3333")
                        .name("333")
                        .parentCompanyId("2222")
                        .build()));


        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/company/2222")
        ).andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<Void> updateResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(updateResponse.isSuccessful()).isTrue();

        Optional<Company> cmp = companyRepository.findOne(Example.of(Company.builder().id("2222").build()));
        cmp.ifPresent(company -> Assertions.fail("deletion is not successful"));
        Optional<Company> cmp2 = companyRepository.findOne(Example.of(Company.builder().id("3333").build()));
        cmp2.ifPresentOrElse(company -> Assertions.assertThat(company.getParentCompanyId()).isEqualTo("1111"),
                () -> Assertions.fail("cannot find company with id 1111"));

    }

    @Test
    void deleteCompanyTest_NotFound() throws Exception {
        companyRepository.saveAll(List.of(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build(),
                Company.builder()
                        .id("2222")
                        .name("222")
                        .parentCompanyId("1111")
                        .build(),
                Company.builder()
                        .id("3333")
                        .name("333")
                        .parentCompanyId("2222")
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/company/3434")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

    }
}
