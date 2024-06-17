package com.fastcampus.pass.batch.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.fastcampus.pass.batch.entity.QBooking.booking;

@Repository
public class BookingCustomRepositoryImpl implements BookingCustomRepository {

    private final JPAQueryFactory queryFactory;

    public BookingCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    @Override
    public long updateUsedPass(Integer passSeq, boolean usedPass) {

        return queryFactory
                .update(booking)
                .set(booking.usedPass, usedPass)
                .set(booking.modifiedAt, LocalDateTime.now())
                .where(booking.passSeq.eq(passSeq))
                .execute();
    }
}
