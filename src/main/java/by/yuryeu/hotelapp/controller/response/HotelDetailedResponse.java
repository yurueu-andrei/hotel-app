package by.yuryeu.hotelapp.controller.response;

import by.yuryeu.hotelapp.dto.AddressDto;
import by.yuryeu.hotelapp.dto.ArrivalTimeDto;
import by.yuryeu.hotelapp.dto.ContactsDto;

import java.util.List;

public record HotelDetailedResponse(
        Long id,
        String name,
        String description,
        String brand,
        AddressDto address,
        ContactsDto contacts,
        ArrivalTimeDto arrivalTime,
        List<String> amenities
) {
}
