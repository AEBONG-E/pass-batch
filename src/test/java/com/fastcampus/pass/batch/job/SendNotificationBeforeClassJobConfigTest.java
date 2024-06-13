package com.fastcampus.pass.batch.job;

import com.fastcampus.pass.batch.adapter.message.KakaoTalkMessageAdapter;
import com.fastcampus.pass.batch.config.KakaoTalkMessageConfig;
import com.fastcampus.pass.batch.config.TestBatchConfig;
import com.fastcampus.pass.batch.entity.Booking;
import com.fastcampus.pass.batch.entity.Pass;
import com.fastcampus.pass.batch.entity.User;
import com.fastcampus.pass.batch.entity.enums.BookingStatus;
import com.fastcampus.pass.batch.entity.enums.PassStatus;
import com.fastcampus.pass.batch.entity.enums.UserStatus;
import com.fastcampus.pass.batch.repository.BookingRepository;
import com.fastcampus.pass.batch.repository.PassRepository;
import com.fastcampus.pass.batch.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ContextConfiguration(classes = {
        SendNotificationBeforeClassJobConfig.class,
        TestBatchConfig.class,
        SendNotificationItemWriter.class,
        KakaoTalkMessageConfig.class,
        KakaoTalkMessageAdapter.class
})
@ActiveProfiles("local")
@SpringBatchTest
@SpringBootTest
class SendNotificationBeforeClassJobConfigTest {

    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private PassRepository passRepository;
    @Autowired private UserRepository userRepository;

    @DisplayName("예약된 수업전 알람 발송 step 테스트")
    @Test
    void test_addNotificationStep() throws Exception {

        // given
        addBooking();;

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("addNotificationStep");

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

    }

    private void addBooking() {
        final LocalDateTime now = LocalDateTime.now();
        final String userId = "A100" + RandomStringUtils.randomNumeric(4);

        User newUser = User.builder()
                .userId(userId)
                .userName("술봉이")
                .status(UserStatus.ACTIVE)
                .phone("01028661825")
                .meta(Map.of("uuid", "abcd1234"))
                .build();
        userRepository.save(newUser);

        Pass newPass = Pass.builder()
                .packageSeq(1)
                .userId(userId)
                .status(PassStatus.PROGRESSED)
                .remainingCount(10)
                .startedAt(now.minusDays(60))
                .endedAt(now.minusDays(1))
                .build();
        passRepository.save(newPass);

        Booking newBooking = Booking.builder()
                .passSeq(newPass.getPassSeq())
                .userId(userId)
                .status(BookingStatus.READY)
                .startedAt(now.plusMinutes(10))
                .endedAt(now.plusMinutes(10).plusMinutes(50))
                .build();
        bookingRepository.save(newBooking);

    }

}