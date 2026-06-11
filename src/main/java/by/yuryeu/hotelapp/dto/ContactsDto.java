package by.yuryeu.hotelapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContactsDto(
        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^\\+(?:[0-9]?){6,14}[0-9]$", message = "must be a valid phone number")
        String phone,
        @NotBlank @Email @Size(max = 254) String email
) {
}
