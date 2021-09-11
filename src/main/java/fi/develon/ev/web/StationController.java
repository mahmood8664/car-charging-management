package fi.develon.ev.web;

import fi.develon.ev.model.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Station controller including all APIs related to station resource
 * @author mahmood
 * @since 9/10/21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/station")
public class StationController {

    @ApiOperation(value = "Returns all stations, this service is paginated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("")
    public BaseResponse<PagingResponse<StationDto>> allStations(PaginationRequest request) {
        return null;
    }

    @ApiOperation(value = "Returns nearby station by given coordinates and distance(KM), ordered by distance")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/nearby")
    public BaseResponse<PagingResponse<StationDto>> nearbyStations(NearbyStationsInquiryRequest request) {
        return null;
    }

    @ApiOperation(value = "Returns station information of given station id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Station not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/{station_id}")
    public BaseResponse<StationDto> getStation(@PathVariable("station_id") Long stationId) {
        return null;
    }

    @ApiOperation(value = "Creates new station")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Associated company not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("")
    public BaseResponse<Long> createStation(@RequestBody @Valid CreateStationRequest request) {
        return null;
    }

    @ApiOperation(value = "Updates new station")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Associated company not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PutMapping("")
    public BaseResponse<Void> updateStation(@RequestBody @Valid StationDto request) {
        return null;
    }

    @ApiOperation(value = "Delete a station")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Associated company not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @DeleteMapping("/{station_id}")
    public BaseResponse<Void> deleteStation(@PathVariable("station_id") Long stationId) {
        return null;
    }


}
