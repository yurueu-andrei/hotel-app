package by.yuryeu.hotelapp.mapper;

import by.yuryeu.hotelapp.controller.request.CreateHotelRequest;
import by.yuryeu.hotelapp.controller.response.HotelDetailedResponse;
import by.yuryeu.hotelapp.controller.response.HotelSummaryResponse;
import by.yuryeu.hotelapp.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {
                AddressMapper.class,
                ContactsMapper.class,
                ArrivalTimeMapper.class,
        }
)
public interface HotelMapper {

    Hotel toEntity(CreateHotelRequest request);

    @Mapping(target = "phone", source = "contacts.phone")
    HotelSummaryResponse toSummary(Hotel hotel);

    HotelDetailedResponse toDetails(Hotel hotel);

    List<HotelSummaryResponse> toSummaries(List<Hotel> hotels);
}
