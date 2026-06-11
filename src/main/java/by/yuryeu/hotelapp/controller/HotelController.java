package by.yuryeu.hotelapp.controller;

import by.yuryeu.hotelapp.controller.request.CreateHotelRequest;
import by.yuryeu.hotelapp.controller.response.HotelDetailedResponse;
import by.yuryeu.hotelapp.controller.response.HotelSummaryResponse;
import by.yuryeu.hotelapp.dto.HotelSearchParams;
import by.yuryeu.hotelapp.enums.HistogramParameter;
import by.yuryeu.hotelapp.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/property-view")
public class HotelController implements HotelApi {

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "name");

    private final HotelService hotelService;

    @Override
    @GetMapping("/hotels")
    public List<HotelSummaryResponse> getHotels(@SortDefault(sort = "name") Sort sort) {
        return hotelService.findAll(sort);
    }

    @Override
    @GetMapping("/hotels/{id}")
    public HotelDetailedResponse getHotel(@PathVariable Long id) {
        return hotelService.findById(id);
    }

    @Override
    @GetMapping("/search")
    public List<HotelSummaryResponse> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String amenities
    ) {
        return hotelService.search(new HotelSearchParams(name, brand, city, country, amenities), DEFAULT_SORT);
    }

    @Override
    @PostMapping("/hotels")
    public ResponseEntity<HotelSummaryResponse> createHotel(@RequestBody CreateHotelRequest request) {
        HotelSummaryResponse createdHotel = hotelService.create(request);
        URI location = URI.create("/property-view/hotels/" + createdHotel.id());
        return ResponseEntity.created(location).body(createdHotel);
    }

    @Override
    @PostMapping("/hotels/{id}/amenities")
    public void addAmenities(
            @PathVariable Long id,
            @RequestBody List<String> amenities
    ) {
        hotelService.addAmenities(id, amenities);
    }

    @Override
    @GetMapping("/histogram/{param}")
    public Map<String, Long> getHistogram(@PathVariable HistogramParameter param) {
        return hotelService.getHistogram(param);
    }
}
