package com.fastcampus.pass.batch.repository;

import com.fastcampus.pass.batch.entity.Package;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface PackageRepository extends JpaRepository<Package, Integer> {

    List<Package> findByCreatedAtAfter(LocalDateTime dateTime, Pageable packageSeq);

    @Transactional
    @Modifying
    @Query(value = " UPDATE Package p " +
            "           SET p.count = :count," +
            "               p.period = :period" +
            "         WHERE p.packageSeq = :packageSeq")
    int updateCountAndPeriod(Integer packageSeq, Integer count, Integer period);
}
