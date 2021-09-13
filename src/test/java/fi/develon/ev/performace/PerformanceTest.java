package fi.develon.ev.performace;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.ev.entity.Company;
import fi.develon.ev.model.BaseResponse;
import fi.develon.ev.model.CompanyDetailDto;
import fi.develon.ev.testcontainer.MongoDBIT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

/**
 * @author mahmood
 * @since 9/14/21
 */
@Disabled
public class PerformanceTest extends MongoDBIT {

    @Test
    void getCompanyDetailTest_OK() throws Exception {
        String id = addData();

        long t1 = System.currentTimeMillis();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/companies/" + id + "/details?include_children=true"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<CompanyDetailDto> companyDetailDto = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        long t2 = System.currentTimeMillis();
        System.out.println("time: " + (t2 - t1));
        Assertions.assertThat(companyDetailDto.isSuccessful()).isTrue();
    }

    private String addData() {

        String prevId = null;
        String firstId = null;
        for (int i = 0; i < 100000; i++) {
            Company test = companyRepository.save(Company.builder()
                    .id(UUID.randomUUID().toString())
                    .parentCompanyId(i == 0 ? null : prevId)
                    .name("test")
                    .build());
            if (i == 0) {
                firstId = test.getId();
            }
        }
        return firstId;
    }


}
