package by.yuryeu.hotelapp.service;

import by.yuryeu.hotelapp.controller.request.CreateHotelRequest;
import by.yuryeu.hotelapp.controller.response.HotelDetailedResponse;
import by.yuryeu.hotelapp.controller.response.HotelSummaryResponse;
import by.yuryeu.hotelapp.dto.HotelSearchParams;
import by.yuryeu.hotelapp.entity.Hotel;
import by.yuryeu.hotelapp.enums.HistogramParameter;
import by.yuryeu.hotelapp.exception.HotelNotFoundException;
import by.yuryeu.hotelapp.mapper.HotelMapper;
import by.yuryeu.hotelapp.repository.HotelRepository;
import by.yuryeu.hotelapp.repository.projection.HistogramEntry;
import by.yuryeu.hotelapp.repository.util.HotelSpecificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
@Transactional
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Transactional(readOnly = true)
    public List<HotelSummaryResponse> findAll(Sort sort) {
        return hotelMapper.toSummaries(hotelRepository.findAll(sort));
    }

    @Transactional(readOnly = true)
    public HotelDetailedResponse findById(Long id) {
        return hotelMapper.toDetails(getHotel(id));
    }

    @Transactional(readOnly = true)
    public List<HotelSummaryResponse> search(HotelSearchParams criteria, Sort sort) {
        return hotelMapper.toSummaries(hotelRepository.findAll(HotelSpecificationUtil.matches(criteria), sort));
    }

    public HotelSummaryResponse create(CreateHotelRequest request) {
        return hotelMapper.toSummary(hotelRepository.save(hotelMapper.toEntity(request)));
    }

    public void addAmenities(Long id, List<String> amenities) {
        Hotel hotel = getHotel(id);
        hotel.addAmenities(new LinkedHashSet<>(amenities));
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getHistogram(HistogramParameter param) {
        List<HistogramEntry> entries = switch (param) {
            case BRAND -> hotelRepository.countByBrand();
            case CITY -> hotelRepository.countByCity();
            case COUNTRY -> hotelRepository.countByCountry();
            case AMENITIES -> hotelRepository.countByAmenity();
        };

        Map<String, Long> histogram = new TreeMap<>();
        entries.forEach(entry -> histogram.put(entry.value(), entry.count()));
        return histogram;
    }

    private Hotel getHotel(Long id) {
        return hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(id));
    }

}
