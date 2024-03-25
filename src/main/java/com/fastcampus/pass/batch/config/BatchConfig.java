package com.fastcampus.pass.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    public static final String JOB_NAME = "passJob";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job passJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(passStep())
                .build();
    }

    @Bean
    public Step passStep() {
        return stepBuilderFactory.get("passStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("==================== execute passTasklet ====================");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
