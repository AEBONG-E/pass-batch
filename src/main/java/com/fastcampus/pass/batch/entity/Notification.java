package com.fastcampus.pass.batch.entity;

import com.fastcampus.pass.batch.entity.enums.NotificationEvent;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 DB에 위임합니다. (AUTO_INCREMENT)
    private Integer notificationSeq;
    private String uuid;

    private NotificationEvent event;
    private String text;
    private boolean sent;
    private LocalDateTime sentAt;

    @Builder
    public Notification(Integer notificationSeq,
                        String uuid,
                        NotificationEvent event,
                        String text,
                        boolean sent,
                        LocalDateTime sentAt) {
        this.notificationSeq = notificationSeq;
        this.uuid = uuid;
        this.event = event;
        this.text = text;
        this.sent = sent;
        this.sentAt = sentAt;
    }
}