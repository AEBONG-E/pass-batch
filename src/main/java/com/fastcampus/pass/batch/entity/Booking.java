package com.fastcampus.pass.batch.entity;

import com.fastcampus.pass.batch.entity.enums.BookingStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingSeq;
    private Integer passSeq;
    private String userId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private boolean usedPass;
    private boolean attended;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime cancelledAt;

    @Builder
    public Booking(Integer bookingSeq,
                   Integer passSeq,
                   String userId,
                   BookingStatus status,
                   boolean usedPass,
                   boolean attended,
                   LocalDateTime startedAt,
                   LocalDateTime endedAt,
                   LocalDateTime cancelledAt) {
        this.bookingSeq = bookingSeq;
        this.passSeq = passSeq;
        this.userId = userId;
        this.status = status;
        this.usedPass = usedPass;
        this.attended = attended;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.cancelledAt = cancelledAt;
    }
}
