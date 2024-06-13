package com.fastcampus.pass.batch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@TestConfiguration
@EnableAutoConfiguration
@EnableBatchProcessing
@EntityScan("com.fastcampus.pass.batch.entity")
@EnableJpaRepositories("com.fastcampus.pass.batch.repository")
@EnableJpaAuditing
@EnableTransactionManagement
public class TestBatchConfig {

}
