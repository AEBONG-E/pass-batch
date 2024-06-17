package com.fastcampus.pass.batch.job;

import com.fastcampus.pass.batch.entity.Booking;

import com.fastcampus.pass.batch.entity.enums.BookingStatus;
import com.fastcampus.pass.batch.repository.BookingRepository;
import com.fastcampus.pass.batch.repository.PassRepository;
import com.fastcampus.pass.batch.repository.custom.BookingCustomRepository;
import com.fastcampus.pass.batch.repository.custom.PassCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Configuration
public class UserPassesJobConfig {

    private final int CHUNK_SIZE = 10;

    // @EnableBatchProcessing로 인해 Bean으로 제공된 JobBuilderFactory, StepBuilderFactory
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final PassCustomRepository passCustomRepository;
    private final BookingCustomRepository bookingCustomRepository;


    @Bean
    public Job userPassesJob() {
        return this.jobBuilderFactory.get("userPassesJob")
                .start(userPassesStep())
                .build();
    }

    @Bean
    public Step userPassesStep() {
        return this.stepBuilderFactory.get("userPassesStep")
                .<Booking, Future<Booking>>chunk(CHUNK_SIZE)
                .reader(userPassesItemReader())
                .processor(usePassesAsyncItemProcessor())
                .writer(usePassesAsyncItemWriter())
                .build();
    }

    @Bean
    public JpaCursorItemReader<Booking> userPassesItemReader() {
        return new JpaCursorItemReaderBuilder<Booking>()
                .name("userPassesItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT b FROM Booking b JOIN FETCH b.pass " +
                        "     WHERE b.status = :status AND b.usedPass = FALSE AND b.endedAt < :endedAt")
                .parameterValues(Map.of("status", BookingStatus.COMPLETED, "endedAt", LocalDateTime.now()))
                .build();
    }

    @Bean
    public AsyncItemProcessor<Booking, Booking> usePassesAsyncItemProcessor() {
        AsyncItemProcessor<Booking, Booking> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(usePassesItemProcessor());
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return asyncItemProcessor;
    }

    @Bean
    public ItemProcessor<Booking, Booking> usePassesItemProcessor() {
        return booking -> {
            booking.usePass();
            return booking;
        };
    }

    @Bean
    public AsyncItemWriter<Booking> usePassesAsyncItemWriter() {
        AsyncItemWriter<Booking> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(usePassesItemWriter());
        return asyncItemWriter;
    }

    @Bean
    public ItemWriter<Booking> usePassesItemWriter() {
        return bookingList -> {
            for (Booking booking : bookingList) {
                long updatedCount = passCustomRepository.updateRemainingCount(booking.getPassSeq(), booking.getPass().getRemainingCount());

                if (updatedCount > 0) {
                    bookingCustomRepository.updateUsedPass(booking.getPassSeq(), booking.isUsedPass());
                }
            }
        };
    }

}
