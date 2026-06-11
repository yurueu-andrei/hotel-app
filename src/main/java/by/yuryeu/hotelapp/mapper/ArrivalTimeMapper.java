package by.yuryeu.hotelapp.mapper;

import by.yuryeu.hotelapp.dto.ArrivalTimeDto;
import by.yuryeu.hotelapp.entity.ArrivalTime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArrivalTimeMapper {

    ArrivalTime toEntity(ArrivalTimeDto dto);

    ArrivalTimeDto toDto(ArrivalTime arrivalTime);
}
