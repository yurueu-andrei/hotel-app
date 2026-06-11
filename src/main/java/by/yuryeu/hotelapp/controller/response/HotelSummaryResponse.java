package by.yuryeu.hotelapp.controller.response;

public record HotelSummaryResponse(
        Long id,
        String name,
        String description,
        String address,
        String phone
) {
}
