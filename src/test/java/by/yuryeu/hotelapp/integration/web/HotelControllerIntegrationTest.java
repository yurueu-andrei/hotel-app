package by.yuryeu.hotelapp.integration.web;

import by.yuryeu.hotelapp.integration.persistence.TestContainerConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static by.yuryeu.hotelapp.integration.web.HotelTestData.CREATE_HOTEL_REQUEST;
import static by.yuryeu.hotelapp.integration.web.HotelTestData.INVALID_HOTEL_REQUEST;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestContainerConfig.class)
@ActiveProfiles("test")
@Transactional
class HotelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @Test
    void usesTestContainer() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String url = connection.getMetaData().getURL();
            Assertions.assertTrue(url.startsWith("jdbc:postgresql://"));
        }
    }

    @Test
    void returnsAllHotelsWithDefaultSorting() throws Exception {
        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(15)))
                .andExpect(jsonPath("$[0].name").value("Atrium Hotel Mogilev"))
                .andExpect(jsonPath("$[0].address")
                        .value("22 Pervomayskaya Street, Mogilev, 212030, Belarus"));
    }

    @Test
    void appliesRequestedSorting() throws Exception {
        mockMvc.perform(get("/property-view/hotels")
                        .param("sort", "name,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(15)))
                .andExpect(jsonPath("$[0].name").value("Warsaw Marriott Hotel"))
                .andExpect(jsonPath("$[14].name").value("Atrium Hotel Mogilev"));
    }

    @Test
    void returnsHotelDetails() throws Exception {
        mockMvc.perform(get("/property-view/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Hilton"))
                .andExpect(jsonPath("$.address.houseNumber").value(9))
                .andExpect(jsonPath("$.arrivalTime.checkIn").value("14:00"))
                .andExpect(jsonPath("$.amenities", hasItem("Free WiFi")));
    }

    @Test
    void combinesSearchFiltersIgnoringCase() throws Exception {
        mockMvc.perform(get("/property-view/search")
                        .param("city", "minsk")
                        .param("brand", "HILT")
                        .param("amenities", "wifi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void returnsEmptySearchResultWhenAnyFilterDoesNotMatch() throws Exception {
        mockMvc.perform(get("/property-view/search")
                        .param("city", "Minsk")
                        .param("country", "Poland"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void createsHotel() throws Exception {
        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREATE_HOTEL_REQUEST))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/property-view/hotels/16"))
                .andExpect(jsonPath("$.id").value(16))
                .andExpect(jsonPath("$.name").value("Hotel Europe"));
    }

    @Test
    void rejectsInvalidHotel() throws Exception {
        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(INVALID_HOTEL_REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors.name").exists());
    }

    @Test
    void addsAmenitiesAndReflectsThemInDetailsAndHistogram() throws Exception {
        mockMvc.perform(post("/property-view/hotels/1/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"Spa\", \"Sauna\"]"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/property-view/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities", hasItem("Spa")));

        mockMvc.perform(get("/property-view/histogram/AMENITIES"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Spa").value(5));
    }

    @ParameterizedTest
    @CsvSource({
            "BRAND, Hilton, 2, 10",
            "CITY, Minsk, 6, 9",
            "COUNTRY, Belarus, 10, 5",
            "AMENITIES, 'Free WiFi', 15, 18"
    })
    void returnsEveryHistogramType(String parameter, String entry, int count, int histogramSize) throws Exception {
        mockMvc.perform(get("/property-view/histogram/{parameter}", parameter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(histogramSize)))
                .andExpect(jsonPath("$['" + entry + "']").value(count));
    }

    @Test
    void handlesUnknownHotelAndHistogramParameter() throws Exception {
        mockMvc.perform(get("/property-view/hotels/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Hotel not found"));

        mockMvc.perform(get("/property-view/histogram/stars"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid request"));
    }
}
