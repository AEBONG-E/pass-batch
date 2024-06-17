package com.fastcampus.pass.batch.repository.custom;

import org.springframework.stereotype.Repository;

@Repository
public interface PassCustomRepository {

    long updateRemainingCount(Integer passSeq, Integer remainingCount);

}
