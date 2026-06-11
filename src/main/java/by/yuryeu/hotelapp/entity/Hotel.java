package by.yuryeu.hotelapp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "hotels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false, length = 100)
    private String brand;

    @Embedded
    private Address address;

    @Embedded
    private Contacts contacts;

    @Embedded
    private ArrivalTime arrivalTime;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "hotel_amenities", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "amenity", nullable = false, length = 150)
    private final Set<String> amenities = new LinkedHashSet<>();

    public Hotel(
            String name,
            String description,
            String brand,
            Address address,
            Contacts contacts,
            ArrivalTime arrivalTime
    ) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.address = address;
        this.contacts = contacts;
        this.arrivalTime = arrivalTime;
    }

    public void addAmenities(Set<String> newAmenities) {
        amenities.addAll(newAmenities);
    }
}
