package by.yuryeu.hotelapp.integration.web;

final class HotelTestData {

    static final String CREATE_HOTEL_REQUEST = """
            {
              "name": "Hotel Europe",
              "description": "Historic hotel",
              "brand": "Independent",
              "address": {
                "houseNumber": 28,
                "street": "Internatsionalnaya Street",
                "city": "Minsk",
                "country": "Belarus",
                "postCode": "220030"
              },
              "contacts": {
                "phone": "+375 17 229 83 33",
                "email": "info@hoteleurope.by"
              },
              "arrivalTime": {
                "checkIn": "14:00",
                "checkOut": "12:00"
              }
            }
            """;

    static final String INVALID_HOTEL_REQUEST = """
            {
              "name": " ",
              "brand": "Brand",
              "address": null,
              "contacts": null,
              "arrivalTime": null
            }
            """;

    private HotelTestData() {
    }
}
