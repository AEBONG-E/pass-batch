package com.fastcampus.pass.batch.repository.custom;

public interface BookingCustomRepository {

    long updateUsedPass(Integer passSeq, boolean usedPass);

}
