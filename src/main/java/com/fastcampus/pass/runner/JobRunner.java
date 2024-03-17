package com.fastcampus.pass.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job passJob;

    public JobRunner(JobLauncher jobLauncher, Job passJob) {
        this.jobLauncher = jobLauncher;
        this.passJob = passJob;
    }

    @Override
    public void run(String... args) throws Exception {
        jobLauncher.run(passJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters());
    }
}
