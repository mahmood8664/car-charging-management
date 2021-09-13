package fi.develon.ev.tdd;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.ev.entity.Company;
import fi.develon.ev.entity.Station;
import fi.develon.ev.model.BaseResponse;
import fi.develon.ev.model.PagingResponse;
import fi.develon.ev.model.StationDto;
import fi.develon.ev.testcontainer.MongoDBIT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

/**
 * @author mahmood
 * @since 9/11/21
 */
public class StationNearbyTest extends MongoDBIT {


    @Test
    void nearbyStationsTest() throws Exception {

        addCompaniesAndStations();

        /*
         * we have 4 stations with coordinates: (11,11),(12,12),(21,21),(22,22), our point: (10,10), distance = 200 KM
         * We expect 2 stations: 11111,22222
         */

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stations/nearby?latitude=10&longitude=10&distance=1000"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<PagingResponse<StationDto>> nearbyStations = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(nearbyStations.isSuccessful()).isTrue();
        Assertions.assertThat(nearbyStations.getResponse().getResponseList()).isNotNull();
        Assertions.assertThat(nearbyStations.getResponse().getResponseList().size()).isEqualTo(2);
        Assertions.assertThat(nearbyStations.getResponse().isHasNext()).isFalse();

        nearbyStations.getResponse().getResponseList().forEach(stationDto -> Assertions.assertThat(stationDto.getStationId()).isIn("11111", "22222"));

    }

    private void addCompaniesAndStations() {
        Company company1 = companyRepository.save(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build());

        Company company2 = companyRepository.save(
                Company.builder()
                        .id("2222")
                        .name("222")
                        .build());

        stationRepository.saveAll(List.of(Station.builder()
                        .id("11111")
                        .companyId(company1.getId())
                        .location(new GeoJsonPoint(11, 11))
                        .name("S11")
                        .build(),
                Station.builder()
                        .id("22222")
                        .companyId(company1.getId())
                        .location(new GeoJsonPoint(12, 12))
                        .name("S12")
                        .build(),
                Station.builder()
                        .id("33333")
                        .companyId(company2.getId())
                        .location(new GeoJsonPoint(21, 21))
                        .name("S21")
                        .build(),
                Station.builder()
                        .id("44444")
                        .companyId(company2.getId())
                        .location(new GeoJsonPoint(22, 22))
                        .name("S22")
                        .build()));
    }

}
