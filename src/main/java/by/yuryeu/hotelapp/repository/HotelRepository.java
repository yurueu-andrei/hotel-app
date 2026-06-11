package by.yuryeu.hotelapp.repository;

import by.yuryeu.hotelapp.entity.Hotel;
import by.yuryeu.hotelapp.repository.projection.HistogramEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    @Query("""
            select new by.yuryeu.hotelapp.repository.projection.HistogramEntry(h.brand, count(h))
            from Hotel h
            group by h.brand
            """)
    List<HistogramEntry> countByBrand();

    @Query("""
            select new by.yuryeu.hotelapp.repository.projection.HistogramEntry(h.address.city, count(h))
            from Hotel h
            group by h.address.city
            """)
    List<HistogramEntry> countByCity();

    @Query("""
            select new by.yuryeu.hotelapp.repository.projection.HistogramEntry(h.address.country, count(h))
            from Hotel h
            group by h.address.country
            """)
    List<HistogramEntry> countByCountry();

    @Query("""
            select new by.yuryeu.hotelapp.repository.projection.HistogramEntry(amenity, count(h))
            from Hotel h join h.amenities amenity
            group by amenity
            """)
    List<HistogramEntry> countByAmenity();
}
