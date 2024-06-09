package com.fastcampus.pass.batch.repository;

import com.fastcampus.pass.batch.entity.Package;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PackageRepositoryTest {

    @Autowired private PackageRepository packageRepository;

//    @Test
//    void given_when_then() {
//
//        // given
//
//        // when
//
//        // then
//
//    }

    @DisplayName("패키지 등록 테스트")
    @Test
    void givenPackage_whenSaving_thenReturnsNothing() {

        // given
        Package newPackage = Package.builder()
                .packageName("바디 챌린지 PT 12주")
                .period(120)
                .build();

        // when
        packageRepository.save(newPackage);

        // then
        assertNotNull(newPackage.getPackageSeq());

    }

    @DisplayName("생성일자 이후의 패키지 리스트 조회 테스트")
    @Test
    void givenDateTime_whenFindingPackagesByCreatedAtAfter_thenReturnsPackage() {

        // given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        Package package1 = Package.builder()
                .packageName("학생 전용 3개월")
                .period(90)
                .build();
        packageRepository.save(package1);

        Package package2 = Package.builder()
                .packageName("학생 전용 6개월")
                .period(180)
                .build();
        packageRepository.save(package2);

        // when
        final List<Package> packageList =
                packageRepository.findByCreatedAtAfter(
                        dateTime,
                        PageRequest.of(0, 1, Sort.by("packageSeq").descending())
                );

        // then
        assertEquals(1, packageList.size());
        assertEquals(package2.getPackageSeq(), packageList.get(0).getPackageSeq());

    }

    @DisplayName("패키지의 기간과 카운팅 수정 테스트")
    @Test
    void givenPackageAndPeriod_whenUpdatingPackage_thenReturnsUpdatedPackage() {

        // given
        Package newPackage = Package.builder()
                .packageName("바디프로필 이벤트 4개월")
                .period(90)
                .build();
        packageRepository.save(newPackage);

        // when
        int updatedCount = packageRepository.updateCountAndPeriod(newPackage.getPackageSeq(), 30, 120);
        final Package updatedPackage =
                packageRepository.findById(newPackage.getPackageSeq()).get();

        // then
        assertEquals(1, updatedCount);
        assertEquals(30, updatedPackage.getCount());
        assertEquals(120, updatedPackage.getPeriod());

    }

    @DisplayName("패키지 삭제 테스트")
    @Test
    void givenPackageSeq_whenDeletingPackage_thenReturnsNothing() {

        // given
        Package newPackage = Package.builder()
                .packageName("제거할 이용권")
                .count(1)
                .build();
        packageRepository.save(newPackage);

        // when
        packageRepository.deleteById(newPackage.getPackageSeq());

        // then
        assertTrue(packageRepository.findById(newPackage.getPackageSeq()).isEmpty());

    }

}
