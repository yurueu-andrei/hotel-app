package by.yuryeu.hotelapp.unit.service;

import by.yuryeu.hotelapp.enums.HistogramParameter;
import by.yuryeu.hotelapp.exception.HotelNotFoundException;
import by.yuryeu.hotelapp.mapper.HotelMapper;
import by.yuryeu.hotelapp.repository.HotelRepository;
import by.yuryeu.hotelapp.repository.projection.HistogramEntry;
import by.yuryeu.hotelapp.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        hotelService = new HotelService(hotelRepository, hotelMapper);
    }

    @Test
    void throwsWhenHotelDoesNotExist() {
        when(hotelRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.findById(42L))
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessageContaining("42");
    }

    @Test
    void convertsHistogramEntriesToOrderedMap() {
        when(hotelRepository.countByCity()).thenReturn(List.of(
                new HistogramEntry("Vitebsk", 1L),
                new HistogramEntry("Minsk", 2L)
        ));

        Map<String, Long> histogram = hotelService.getHistogram(HistogramParameter.CITY);

        assertThat(histogram).containsExactly(Map.entry("Minsk", 2L), Map.entry("Vitebsk", 1L));
    }

    @Test
    void returnsBrandHistogram() {
        when(hotelRepository.countByBrand()).thenReturn(List.of(
                new HistogramEntry("Hilton", 2L)
        ));

        assertThat(hotelService.getHistogram(HistogramParameter.BRAND))
                .containsExactly(Map.entry("Hilton", 2L));
    }

    @Test
    void returnsCountryHistogram() {
        when(hotelRepository.countByCountry()).thenReturn(List.of(
                new HistogramEntry("Belarus", 10L)
        ));

        assertThat(hotelService.getHistogram(HistogramParameter.COUNTRY))
                .containsExactly(Map.entry("Belarus", 10L));
    }

    @Test
    void returnsAmenitiesHistogram() {
        when(hotelRepository.countByAmenity()).thenReturn(List.of(
                new HistogramEntry("Free WiFi", 15L)
        ));

        assertThat(hotelService.getHistogram(HistogramParameter.AMENITIES))
                .containsExactly(Map.entry("Free WiFi", 15L));
    }
}
