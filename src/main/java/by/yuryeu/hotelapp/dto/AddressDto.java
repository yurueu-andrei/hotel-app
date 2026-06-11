package by.yuryeu.hotelapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AddressDto(
        @NotNull @Positive Integer houseNumber,
        @NotBlank @Size(max = 150) String street,
        @NotBlank @Size(max = 100) String city,
        @NotBlank @Size(max = 100) String country,
        @NotBlank @Size(max = 20) String postCode
) {
}
