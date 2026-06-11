package by.yuryeu.hotelapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Contacts {

    @Column(nullable = false, length = 50)
    private String phone;

    @Column(nullable = false, length = 254)
    private String email;
}
