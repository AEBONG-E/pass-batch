package com.fastcampus.pass.batch.job;

import com.fastcampus.pass.batch.config.TestBatchConfig;
import com.fastcampus.pass.batch.entity.BulkPass;
import com.fastcampus.pass.batch.entity.UserGroupMapping;
import com.fastcampus.pass.batch.repository.BulkPassRepository;
import com.fastcampus.pass.batch.repository.UserGroupMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {AddPassesJobConfig.class, TestBatchConfig.class, AddPassesTasklet.class})
public class AddPassesJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private BulkPassRepository bulkPassRepository;

    @Autowired
    private UserGroupMappingRepository userGroupMappingRepository;


    @Test
    public void test_addPassesJob() throws Exception {
        // given
        addBulkPassEntity();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance jobInstance = jobExecution.getJobInstance();

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals("addPassesJob", jobInstance.getJobName());

    }

    private void addBulkPassEntity() {
        final LocalDateTime now = LocalDateTime.now();
        final String userGroupId = RandomStringUtils.randomAlphabetic(6);
        final String userId = "A100" + RandomStringUtils.randomNumeric(4);

        BulkPass bulkPass = BulkPass.builder()

                .build();

        bulkPassRepository.save(bulkPass);

        UserGroupMapping userGroupMapping = UserGroupMapping.builder()

                .build();

        userGroupMappingRepository.save(userGroupMapping);

    }

}
