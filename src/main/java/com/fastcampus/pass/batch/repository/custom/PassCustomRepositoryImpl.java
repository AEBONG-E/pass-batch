package com.fastcampus.pass.batch.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static com.fastcampus.pass.batch.entity.QPass.pass;

@Repository
public class PassCustomRepositoryImpl implements PassCustomRepository {

    private final JPAQueryFactory queryFactory;

    public PassCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    @Override
    public long updateRemainingCount(Integer passSeq, Integer remainingCount) {

        return queryFactory
                .update(pass)
                .set(pass.remainingCount, remainingCount)
                .set(pass.modifiedAt, LocalDateTime.now())
                .where(pass.passSeq.eq(passSeq))
                .execute();
    }

}
