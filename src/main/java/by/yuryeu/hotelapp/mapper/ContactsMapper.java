package by.yuryeu.hotelapp.mapper;

import by.yuryeu.hotelapp.dto.ContactsDto;
import by.yuryeu.hotelapp.entity.Contacts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactsMapper {

    Contacts toEntity(ContactsDto dto);

    ContactsDto toDto(Contacts contacts);
}
