package by.yuryeu.hotelapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ArrivalTime {

    @Column(name = "check_in", nullable = false)
    private LocalTime checkIn;

    @Column(name = "check_out")
    private LocalTime checkOut;
}
