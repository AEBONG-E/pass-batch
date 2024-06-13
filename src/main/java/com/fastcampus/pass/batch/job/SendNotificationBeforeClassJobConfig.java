package com.fastcampus.pass.batch.job;

import com.fastcampus.pass.batch.entity.Booking;
import com.fastcampus.pass.batch.entity.Notification;
import com.fastcampus.pass.batch.entity.enums.BookingStatus;
import com.fastcampus.pass.batch.entity.enums.NotificationEvent;
import com.fastcampus.pass.batch.entity.mapper.NotificationModelMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;

@Configuration
public class SendNotificationBeforeClassJobConfig {
    private final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final SendNotificationItemWriter sendNotificationItemWriter;

    public SendNotificationBeforeClassJobConfig(JobBuilderFactory jobBuilderFactory,
                                                StepBuilderFactory stepBuilderFactory,
                                                EntityManagerFactory entityManagerFactory,
                                                SendNotificationItemWriter sendNotificationItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.sendNotificationItemWriter = sendNotificationItemWriter;
    }

    @Bean
    public Job sendNotificationBeforeClassJob() {
        return this.jobBuilderFactory.get("sendNotificationBeforeClassJob")
                .start(addNotificationStep())
                .next(sendNotificationStep())
                .build();
    }

    /* ==========================================  Step 1 시작  =============================================== */

    @Bean
    public Step addNotificationStep() {
        return this.stepBuilderFactory.get("addNotificationStep")
                .<Booking, Notification>chunk(CHUNK_SIZE)
                .reader(addNotificationItemReader())
                .processor(addNotificationItemProcessor())
                .writer(addNotificationItemWriter())
                .build();

    }

    /**
     * JpaPagingItemReader: JPA에서 사용하는 페이징 기법으로
     * 쿼리 당 pageSize만큼 가져오며 다른 PagingItemReader와 마찬가지로 Thread-safe 하다.
     */
    @Bean
    public JpaPagingItemReader<Booking> addNotificationItemReader() {
        return new JpaPagingItemReaderBuilder<Booking>()
                .name("addNotificationItemReader")
                .entityManagerFactory(entityManagerFactory)
                // pageSize: 한 번에 조회할 row 수
                .pageSize(CHUNK_SIZE)
                // 상태(status)가 준비중이며, 시작일시(startedAt)이 10분 후 시작하는 예약이 알람 대상이 된다.
                .queryString(
                        "SELECT b FROM Booking b " +
                                "JOIN FETCH b.user " +
                                "WHERE b.status = :status " +
                                "AND b.startedAt <= :startedAt ORDER BY b.bookingSeq"
                )
                .parameterValues(Map.of("status", BookingStatus.READY, "startedAt", LocalDateTime.now().plusMinutes(10)))
                .build();
    }

    @Bean
    public ItemProcessor<Booking, Notification> addNotificationItemProcessor() {
        return booking -> NotificationModelMapper.INSTANCE.toNotification(booking, NotificationEvent.BEFORE_CLASS);
    }

    @Bean
    public JpaItemWriter<Notification> addNotificationItemWriter() {
        return new JpaItemWriterBuilder<Notification>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    /* ==========================================  Step 1 종료  =============================================== */


    /* ==========================================  Step 2 시작  =============================================== */

    @Bean
    public Step sendNotificationStep() {
        return this.stepBuilderFactory.get("sendNotificationStep")
                .<Notification, Notification>chunk(CHUNK_SIZE)
                .reader(sendNotificationItemReader())
                .writer(sendNotificationItemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public SynchronizedItemStreamReader<Notification> sendNotificationItemReader() {
        // 이벤트(event)가 수업 전이며, 발송 여부(sent)가 미발송인 알람이 조회 대상이 된다.
        JpaCursorItemReader<Notification> itemReader = new JpaCursorItemReaderBuilder<Notification>()
                .name("sendNotificationItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT n FROM Notification n WHERE n.event = :event AND n.sent = :sent")
                .parameterValues(Map.of("event", NotificationEvent.BEFORE_CLASS, "sent", false))
                .build();

        return new SynchronizedItemStreamReaderBuilder<Notification>()
                .delegate(itemReader)
                .build();
    }

    /* ==========================================  Step 2 종료  =============================================== */

}
