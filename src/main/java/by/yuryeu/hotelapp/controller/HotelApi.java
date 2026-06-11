package by.yuryeu.hotelapp.controller;

import by.yuryeu.hotelapp.controller.request.CreateHotelRequest;
import by.yuryeu.hotelapp.controller.response.HotelDetailedResponse;
import by.yuryeu.hotelapp.controller.response.HotelSummaryResponse;
import by.yuryeu.hotelapp.enums.HistogramParameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Tag(name = "Hotels", description = "Hotel catalogue operations")
public interface HotelApi {

    @Operation(
            summary = "Get all hotels",
            description = "Supports sorting, for example: sort=name,asc"
    )
    @ApiResponse(responseCode = "200", description = "Hotels were returned")
    List<HotelSummaryResponse> getHotels(@ParameterObject Sort sort);

    @Operation(summary = "Get detailed hotel information")
    @ApiResponse(responseCode = "200", description = "Hotel was returned")
    @ApiResponse(
            responseCode = "404",
            description = "Hotel was not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
    HotelDetailedResponse getHotel(Long id);

    @Operation(summary = "Search hotels", description = "All supplied filters are combined with AND")
    List<HotelSummaryResponse> search(String name, String brand, String city, String country, String amenities);

    @Operation(summary = "Create a hotel")
    @ApiResponse(responseCode = "201", description = "Hotel was created")
    @ApiResponse(
            responseCode = "400",
            description = "Request is invalid",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
    ResponseEntity<HotelSummaryResponse> createHotel(@Valid CreateHotelRequest request);

    @Operation(summary = "Add amenities to a hotel")
    @ApiResponse(responseCode = "200", description = "Amenities were added")
    void addAmenities(Long id, @NotEmpty List<@NotBlank @Size(max = 150) String> amenities);

    @Operation(summary = "Get a hotel histogram")
    @ApiResponse(responseCode = "200", description = "Histogram was returned")
    Map<String, Long> getHistogram(HistogramParameter param);
}
