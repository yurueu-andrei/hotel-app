package by.yuryeu.hotelapp.mapper;

import by.yuryeu.hotelapp.dto.AddressDto;
import by.yuryeu.hotelapp.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(AddressDto dto);

    AddressDto toDto(Address address);

    default String toSingleLine(Address address) {
        return "%s %s, %s, %s, %s".formatted(
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry()
        );
    }
}
