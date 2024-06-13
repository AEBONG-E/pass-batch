package com.fastcampus.pass.batch.job;

import com.fastcampus.pass.batch.adapter.message.KakaoTalkMessageAdapter;
import com.fastcampus.pass.batch.entity.Notification;
import com.fastcampus.pass.batch.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class SendNotificationItemWriter implements ItemWriter<Notification> {
    private final NotificationRepository notificationRepository;
    private final KakaoTalkMessageAdapter kakaoTalkMessageAdapter;

    public SendNotificationItemWriter(NotificationRepository notificationRepository, KakaoTalkMessageAdapter kakaoTalkMessageAdapter) {
        this.notificationRepository = notificationRepository;
        this.kakaoTalkMessageAdapter = kakaoTalkMessageAdapter;
    }

    @Override
    public void write(List<? extends Notification> notificationEntities) throws Exception {
        int count = 0;

        for (Notification notification : notificationEntities) {
            boolean successful = kakaoTalkMessageAdapter.sendKakaoTalkMessage(notification.getUuid(), notification.getText());

            if (successful) {
                Notification newNotification = Notification.builder()
                        .sent(true)
                        .sentAt(LocalDateTime.now())
                        .build();
                notificationRepository.save(newNotification);
                count++;
            }

        }
        log.info("SendNotificationItemWriter - write: 수업 전 알람 {}/{}건 전송 성공", count, notificationEntities.size());

    }
}
