package by.yuryeu.hotelapp.controller.request;

import by.yuryeu.hotelapp.dto.AddressDto;
import by.yuryeu.hotelapp.dto.ArrivalTimeDto;
import by.yuryeu.hotelapp.dto.ContactsDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateHotelRequest(
        @NotBlank @Size(max = 200) String name,
        @Size(max = 2000) String description,
        @NotBlank @Size(max = 100) String brand,
        @NotNull @Valid AddressDto address,
        @NotNull @Valid ContactsDto contacts,
        @NotNull @Valid ArrivalTimeDto arrivalTime
) {
}
