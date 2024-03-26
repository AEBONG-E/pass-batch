package com.fastcampus.pass.batch.job;

import com.fastcampus.pass.batch.config.TestBatchConfig;
import com.fastcampus.pass.batch.entity.Pass;
import com.fastcampus.pass.batch.entity.enums.PassStatus;
import com.fastcampus.pass.batch.repository.PassRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {ExpirePassesJobConfig.class, TestBatchConfig.class})
class ExpirePassesJobConfigTest {

    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private PassRepository passRepository;

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

    @DisplayName("이용권 만료 배치 수행 테스트")
    @Test
    void givenPassList_whenJobExecuting_thenReturnsJobInstance() throws Exception {

        // given
        int size = 10;
        createdPassList(size);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance jobInstance = jobExecution.getJobInstance();

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals("expirePassesJob", jobInstance.getJobName());

    }

    private void createdPassList(int size) {
        final LocalDateTime now = LocalDateTime.now();
        final Random random = new Random();

        List<Pass> passList = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            Pass pass = Pass.builder()
                    .packageSeq(1)
                    .userId("A" + 1000000 + i)
                    .status(PassStatus.PROGRESSED)
                    .remainingCount(random.nextInt(11))
                    .startedAt(now.minusDays(60))
                    .endedAt(now.minusDays(1))
                    .build();

            passList.add(pass);
        }
        passRepository.saveAll(passList);
    }

}