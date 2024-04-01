package com.fastcampus.pass.batch.repository;

import com.fastcampus.pass.batch.entity.BulkPass;
import com.fastcampus.pass.batch.entity.enums.BulkPassStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BulkPassRepository extends JpaRepository<BulkPass, Integer> {

    /**
     * 원래 startedAt 이 파라미터로 받은 startedAt 보다 크다면 조회한다.
     * @param status 대량 이용권 상태
     * @param startedAt 파라미터로 받은 startedAt (원래 startedAt 은 파라미터로 받은 보다 큰지 비교 대상)
     * @return List<BulkPass>
     */
    // WHERE status = :status AND startedAt > :startedAt
    List<BulkPass> findByStatusAndStartedAtGreaterThan(BulkPassStatus status, LocalDateTime startedAt);
}
