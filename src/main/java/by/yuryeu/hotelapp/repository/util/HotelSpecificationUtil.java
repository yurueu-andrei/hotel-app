package by.yuryeu.hotelapp.repository.util;

import by.yuryeu.hotelapp.dto.HotelSearchParams;
import by.yuryeu.hotelapp.entity.Hotel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HotelSpecificationUtil {

    private static final String NAME = "name";
    private static final String BRAND = "brand";
    private static final String ADDRESS = "address";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String AMENITIES = "amenities";

    public static Specification<Hotel> matches(HotelSearchParams params) {
        return Specification.allOf(
                hotelFieldContains(NAME, params.name()),
                hotelFieldContains(BRAND, params.brand()),
                addressFieldContains(CITY, params.city()),
                addressFieldContains(COUNTRY, params.country()),
                amenityContains(params.amenities())
        );
    }

    private static Specification<Hotel> hotelFieldContains(String field, String value) {
        return (root, query, builder) ->
                containsIgnoreCase(builder, root.get(field), value);
    }

    private static Specification<Hotel> addressFieldContains(String field, String value) {
        return (root, query, builder) ->
                containsIgnoreCase(builder, root.get(ADDRESS).get(field), value);
    }

    private static Specification<Hotel> amenityContains(String value) {
        return (root, query, builder) -> {
            if (!hasText(value)) {
                return builder.conjunction();
            }

            query.distinct(true);
            return builder.like(builder.lower(root.joinSet(AMENITIES, JoinType.INNER)), containsPattern(value));
        };
    }

    private static Predicate containsIgnoreCase(
            CriteriaBuilder builder,
            Path<String> path,
            String value
    ) {
        return hasText(value) ? builder.like(builder.lower(path), containsPattern(value)) : builder.conjunction();
    }

    private static String containsPattern(String value) {
        return "%" + value.toLowerCase(Locale.ROOT) + "%";
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
