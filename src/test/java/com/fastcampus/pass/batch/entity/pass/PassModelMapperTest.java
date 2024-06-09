package com.fastcampus.pass.batch.entity.pass;

import com.fastcampus.pass.batch.entity.BulkPass;
import com.fastcampus.pass.batch.entity.Pass;
import com.fastcampus.pass.batch.entity.enums.BulkPassStatus;
import com.fastcampus.pass.batch.entity.enums.PassStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class PassModelMapperTest {

    @DisplayName("Pass 와 BulkPass 간의 매핑 테스트")
    @Test
    void test_toPass() {

        // given
        final LocalDateTime now = LocalDateTime.now();
        final String userId = "A1000000";

        BulkPass bulkPass = BulkPass.builder()
                .packageSeq(1)
                .userGroupId("GROUP")
                .status(BulkPassStatus.COMPLETED)
                .count(10)
                .startedAt(now.minusDays(60))
                .endedAt(now)
                .build();

        // when
        final Pass pass = PassModelMapper.INSTANCE.toPass(bulkPass, userId);

        // then
        assertEquals(1, pass.getPassSeq());
        assertEquals(PassStatus.READY, pass.getStatus());
        assertEquals(10, pass.getRemainingCount());
        assertEquals(now.minusDays(60), pass.getStartedAt());
        assertEquals(now, pass.getEndedAt());
        assertEquals(userId, pass.getUserId());
    }

}